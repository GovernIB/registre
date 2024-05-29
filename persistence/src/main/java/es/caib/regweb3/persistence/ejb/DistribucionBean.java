package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Cola;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RespuestaDistribucion;
import es.caib.regweb3.plugins.distribucion.IDistribucionPlugin;
import es.caib.regweb3.plugins.distribucion.email.DistribucionEmailPlugin;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by mgonzalez on 27/11/2018.
 */
@Stateless(name = "DistribucionEJB")
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI", "RWE_WS_ENTRADA", "RWE_WS_SALIDA"})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class DistribucionBean implements DistribucionLocal {

    protected final org.apache.log4j.Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private RegistroEntradaConsultaLocal registroEntradaConsultaEjb;
    @EJB private JustificanteLocal justificanteEjb;
    @EJB private IntegracionLocal integracionEjb;
    @EJB private PluginLocal pluginEjb;
    @EJB private ColaLocal colaEjb;
    @EJB private EntidadLocal entidadEjb;

    /**
     * Método que envia a distribuir el registro en función del valor de la propiedad envioCola de cada plugin.
     * @param re registro de entrada a distribuir
     * @param usuarioEntidad
     * @return
     * @throws I18NException
     * @throws I18NException
     */
    @Override
    public RespuestaDistribucion distribuir(RegistroEntrada re, UsuarioEntidad usuarioEntidad, String descripcion, String emails, String motivo) throws I18NException {

        RespuestaDistribucion respuestaDistribucion = new RespuestaDistribucion();

        Boolean distribuido = false;

        StringBuilder peticion = new StringBuilder();
        Date inicio = new Date();

        try {
            // Si es el plugin de email hay que asignar los valores.
            IDistribucionPlugin plugin = (IDistribucionPlugin) pluginEjb.getPlugin(usuarioEntidad.getEntidad().getId(), RegwebConstantes.PLUGIN_DISTRIBUCION, true);

            // Montamos la petición de la integración
            peticion.append("usuario: ").append(re.getUsuario().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));
            peticion.append("registro: ").append(re.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
            peticion.append("oficina: ").append(re.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
            peticion.append("plugin: ").append(plugin.getClass().getName()).append(System.getProperty("line.separator"));

            // Plugin E-mail
            DistribucionEmailPlugin distribucionEmailPlugin;
            if(plugin.getClass().getName().contains("DistribucionEmailPlugin")){
                distribucionEmailPlugin = (DistribucionEmailPlugin)plugin;
                distribucionEmailPlugin.setEmails(emails);
                distribucionEmailPlugin.setMotivo(motivo);
                respuestaDistribucion.setEnvioMail(true);
            }

            // Activada la Cola de Distribución
            if(plugin.getEnvioCola()){
                Boolean encolado = colaEjb.enviarAColaDistribucion(re, usuarioEntidad, descripcion);
                respuestaDistribucion.setEncolado(encolado);
            }else{ // Distribución inmediata
                Entidad entidad = entidadEjb.findByIdLigero(usuarioEntidad.getEntidad().getId());
                distribuido = distribuirRegistro(entidad, usuarioEntidad, RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion, re, plugin, peticion, inicio);
                respuestaDistribucion.setDistribuido(distribuido);
            }

        } catch ( Exception  e) {
            log.info("Error distribuyendo registro: " + re.getNumeroRegistroFormateado());
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion, peticion.toString(), e, null,System.currentTimeMillis() - inicio.getTime(), usuarioEntidad.getEntidad().getId(), re.getNumeroRegistroFormateado());
            throw new I18NException("registroEntrada.distribuir.error");
        }

        return respuestaDistribucion;
    }

    /**
     * Método que distribuye un registro de entrada de manera atómica generando el justificante
     * e invocando al webservice de distribucion
     *
     * @param registroEntrada
     * @param distribucionPlugin
     * @return
     * @throws I18NException
     * @throws I18NValidationException
     * @throws I18NException
     */
    private Boolean distribuirRegistroEntrada(Entidad entidad, RegistroEntrada registroEntrada, IDistribucionPlugin distribucionPlugin) throws I18NValidationException, I18NException {

        Boolean distribuido = false;

        log.info("------- Distribuyendo el registro: " + registroEntrada.getNumeroRegistroFormateado() + " -------");

        // Si no tiene Justificante, lo creamos
        if (!registroEntrada.getRegistroDetalle().getTieneJustificante()) {
            AnexoFull justificante = justificanteEjb.crearJustificante(entidad, registroEntrada.getUsuario(), registroEntrada, RegwebConstantes.REGISTRO_ENTRADA, Configuracio.getDefaultLanguage());
            registroEntrada.getRegistroDetalle().getAnexosFull().add(justificante);

        // Si la custodia en diferido está activa, tiene Justificante, pero no está custodiado, no distribuimos!
        }else if (PropiedadGlobalUtil.getCustodiaDiferida(registroEntrada.getUsuario().getEntidad().getId()) && !registroEntrada.getRegistroDetalle().getTieneJustificanteCustodiado()) {
            log.info("El registro: " + registroEntrada.getNumeroRegistroFormateado()+" no se distribuira en esta iteracion porque no tiene el Justificante custodiado");
            return false;
        }

        // Distribuimos
        distribuido = distribucionPlugin.distribuir(registroEntrada, new Locale(RegwebConstantes.IDIOMA_CATALAN_CODIGO));

        return distribuido;
    }

    /**
     * Procesa un registro de la cola de manera individual
     *
     * @param elemento elemento de la cola
     * @param entidad
     * @return
     * @throws I18NException
     * @throws I18NException
     */
    @Override
    public Boolean distribuirRegistroEnCola(Cola elemento, Entidad entidad, UsuarioEntidad usuarioEntidad, Long tipoIntegracion) throws I18NException {

        Boolean distribuido = false;

        // Integración
        StringBuilder peticion = new StringBuilder();
        Date inicio = new Date();
        String descripcion = "Distribución desde Cola";
        String hora = "<b>" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(inicio) + "</b>&nbsp;&nbsp;&nbsp;";

        try {

            //Obtenermos plugin distribución
            IDistribucionPlugin distribucionPlugin = (IDistribucionPlugin) pluginEjb.getPlugin(entidad.getId(), RegwebConstantes.PLUGIN_DISTRIBUCION, true);

            //Obtenemos el registro de entrada que se debe distribuir
            RegistroEntrada registroEntrada = null;
            if(distribucionPlugin.getClass().getName().contains("DistribucionGoibPlugin")){
                registroEntrada = registroEntradaEjb.getConAnexosFullDistribuir(elemento.getIdObjeto());
            }else{
                registroEntrada = registroEntradaEjb.getConAnexosFull(elemento.getIdObjeto());
            }

            //Montamos la petición de la integración
            peticion.append("usuario: ").append(registroEntrada.getUsuario().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));
            peticion.append("registro: ").append(registroEntrada.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
            peticion.append("oficina: ").append(registroEntrada.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
            peticion.append("plugin: ").append(distribucionPlugin.getClass().getName()).append(System.getProperty("line.separator"));

            distribuido = distribuirRegistro(entidad, usuarioEntidad, tipoIntegracion, I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()), "distribucion.cola"), registroEntrada, distribucionPlugin,peticion,inicio);

            if (distribuido) { //Si la distribución ha ido bien
                colaEjb.procesarElemento(elemento);
            }

        } catch (I18NException | I18NValidationException  e) {
            log.info("Error distribuyendo registro de la Cola: " + elemento.getDescripcionObjeto());
            e.printStackTrace();
            colaEjb.actualizarElementoCola(elemento, entidad.getId(), hora + e.getMessage());
            // Añadimos el error a la integración
            integracionEjb.addIntegracionError(tipoIntegracion, descripcion, peticion.toString(), e, null,System.currentTimeMillis() - inicio.getTime(), entidad.getId(), elemento.getDescripcionObjeto());
        }

        return distribuido;

    }
    @Override
    public Integer distribuirAutomaticamente(Entidad entidad) throws I18NException{

        List<RegistroEntrada> registros = registroEntradaConsultaEjb.getDistribucionAutomatica(entidad.getId());
        int total = 0;
        for(RegistroEntrada registro:registros){

            // Solo se distribuyen los registros con TipoDoc=1 o TipoDoc=2/3 con anexos
            if(registro.getRegistroDetalle().getTipoDocumentacionFisica().equals(RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA) ||
                    (registro.getRegistroDetalle().getTipoDocumentacionFisica().equals(RegwebConstantes.TIPO_DOCFISICA_ACOMPANYA_DOC_COMPLEMENTARIA) && registro.getRegistroDetalle().getTieneAnexos()) ||
                    (registro.getRegistroDetalle().getTipoDocumentacionFisica().equals(RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC) && registro.getRegistroDetalle().getTieneAnexos())){

                distribuir(registro, registro.getUsuario(), I18NLogicUtils.tradueix(new Locale(Configuracio.getDefaultLanguage()), "distribucion.automatica"),null,null);
                total++;
            }
        }

        return total;
    }


    /**
     * Método común para distribuir un registro que monta la petición
     * @param entidad
     * @param usuarioEntidad
     * @param tipoIntegracion
     * @param descripcion
     * @param registroEntrada
     * @param distribucionPlugin
     * @param peticion
     * @param inicio
     * @return
     * @throws I18NException
     */
    private Boolean distribuirRegistro(Entidad entidad, UsuarioEntidad usuarioEntidad, Long tipoIntegracion, String descripcion, RegistroEntrada registroEntrada, IDistribucionPlugin distribucionPlugin, StringBuilder peticion, Date inicio) throws I18NException, I18NValidationException {

        Boolean distribuido = false;

        //Distribuimos el registro de entrada.
        distribuido = distribuirRegistroEntrada(entidad, registroEntrada, distribucionPlugin);

        if (distribuido) { //Si la distribución ha ido bien
            registroEntradaEjb.marcarDistribuido(registroEntrada, usuarioEntidad, descripcion);
            integracionEjb.addIntegracionOk(inicio, tipoIntegracion, descripcion, peticion.toString(), System.currentTimeMillis() - inicio.getTime(), registroEntrada.getUsuario().getEntidad().getId(), registroEntrada.getNumeroRegistroFormateado());
        }

        return distribuido;
    }

    /**
     * Re-Distribuye un registro de entrada
     *
     * @param idRegistro del Registro de Entrada
     * @param entidad
     * @return
     * @throws I18NException
     * @throws I18NException
     */
    @Override
    public Boolean reDistribuirRegistro(Long idRegistro, Entidad entidad) throws I18NException {

        Boolean distribuido = false;
        RegistroEntrada registroEntrada = null;

        // Integración
        StringBuilder peticion = new StringBuilder();
        Date inicio = new Date();
        String descripcion = "Re-Distribución";

        try {

            //Obtenermos plugin distribución
            IDistribucionPlugin distribucionPlugin = (IDistribucionPlugin) pluginEjb.getPlugin(entidad.getId(), RegwebConstantes.PLUGIN_DISTRIBUCION, true);

            //Obtenemos el registro de entrada que se debe distribuir
            if(distribucionPlugin.getClass().getName().contains("DistribucionGoibPlugin")){
                registroEntrada = registroEntradaEjb.getConAnexosFullDistribuir(idRegistro);
            }else{
                registroEntrada = registroEntradaEjb.getConAnexosFull(idRegistro);
            }

            // Se comprueba los anexos ya han sido purgados, en ese caso no se podrá distribuir
            if (registroEntrada.getRegistroDetalle().isAnexosPurgado()) {
                return false;
            }

            // Integración
            peticion.append("usuario: ").append(registroEntrada.getUsuario().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));
            peticion.append("registro: ").append(registroEntrada.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
            peticion.append("oficina: ").append(registroEntrada.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
            peticion.append("plugin: ").append(distribucionPlugin.getClass().getName()).append(System.getProperty("line.separator"));

            //Distribuimos el registro de entrada.
            distribuido = distribuirRegistroEntrada(entidad, registroEntrada, distribucionPlugin);

            if (distribuido) { //Si la distribución ha ido bien
                integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion, peticion.toString(), System.currentTimeMillis() - inicio.getTime(), registroEntrada.getUsuario().getEntidad().getId(), registroEntrada.getNumeroRegistroFormateado());
            }

        } catch (I18NException | I18NValidationException e) {
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_DISTRIBUCION, descripcion, peticion.toString(), e, null,System.currentTimeMillis() - inicio.getTime(), entidad.getId(), registroEntrada.getNumeroRegistroFormateado());
        }

        return distribuido;

    }

    @Override
    @TransactionTimeout(value = 1800)  // 30 minutos
    public void distribuirRegistrosEnCola(Entidad entidad) throws I18NException {

        //obtiene un numero de elementos (configurable) pendientes de distribuir que estan en la cola
        List<Cola> elementosADistribuir = colaEjb.findByTipoEntidad(RegwebConstantes.COLA_DISTRIBUCION, entidad.getId(),null, PropiedadGlobalUtil.getElementosCola(entidad.getId()));

        log.info("");
        log.info("Cola de DISTRIBUCION: Hay " + elementosADistribuir.size() + " elementos que se van a distribuir en esta iteracion");

        for (Cola elemento : elementosADistribuir) {

            distribuirRegistroEnCola(elemento, entidad, elemento.getUsuarioEntidad(), RegwebConstantes.INTEGRACION_SCHEDULERS);
        }

    }

    /**
     * Este método lo que hace es eliminar de la lista de anexos a distribuir, aquellos que el Arxiu no soporta que son
     * de formato xml y que no puede hacer el upgrade de la firma.
     * Lo que hace es comparar el nombre del Fichero y la aplicación de la que procede, para determinar si lo elimina o no.
     *
     *
     * @param nombreFichero
     * @param anexosFull
     * @param anexosFullADistribuir
     * @return
     * @throws I18NException
     */
    private List<AnexoFull> gestionarByAplicacionByNombreFichero(String nombreFichero, List<AnexoFull> anexosFull, List<AnexoFull> anexosFullADistribuir) throws I18NException {
        //para cada uno de los anexos miramos si es uno de los conflictivos.
        //Estan tipificados en Regweb3Constantes.
        for (AnexoFull anexoFull : anexosFull) {
            String nombreFicheroTemp = "";
            //Si es un documento sin firma, el nombre està en DocumentCustody
            if (anexoFull.getAnexo().getModoFirma() == RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA) {
                nombreFicheroTemp = anexoFull.getDocumentoCustody().getName();
            } else { // Si tiene firma, el nombre está en SignatureCustody.
                nombreFicheroTemp = anexoFull.getSignatureCustody().getName();
            }

            //Si el nombre del fichero es distinto al que nos han indicado, se puede distribuir
            if (!nombreFichero.equals(nombreFicheroTemp)) {
                anexosFullADistribuir.add(anexoFull);
            }
        }
        return anexosFullADistribuir;
    }


    @Override
    public Boolean isDistribucionPluginEmail(Long idEntidad) throws I18NException {
        try{

            IDistribucionPlugin plugin =  (IDistribucionPlugin)  pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_DISTRIBUCION, true);
            return plugin.getClass().getName().contains("DistribucionEmailPlugin");

        }catch (I18NException e){
            e.printStackTrace();
            throw new I18NException("registroEntrada.distribuir.error");
        }

    }
}
