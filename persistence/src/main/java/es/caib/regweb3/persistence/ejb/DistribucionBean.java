package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.persistence.utils.MailUtils;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RespuestaDistribucion;
import es.caib.regweb3.persistence.utils.VerificacioFirmaException;
import es.caib.regweb3.plugins.distribucion.IDistribucionPlugin;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.annotation.TransactionTimeout;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by mgonzalez on 27/11/2018.
 */
@Stateless(name = "DistribucionEJB")
@SecurityDomain("seycon")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class DistribucionBean implements DistribucionLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private JustificanteLocal justificanteEjb;
    @EJB private IntegracionLocal integracionEjb;
    @EJB private PluginLocal pluginEjb;
    @EJB private ColaLocal colaEjb;


    /**
     * Método que envía a la cola de Distribución el Registro indicado
     * @param re registro de entrada a distribuir
     * @param usuarioEntidad
     * @return
     * @throws Exception
     * @throws I18NException
     */
    @Override
    public RespuestaDistribucion distribuir(RegistroEntrada re, UsuarioEntidad usuarioEntidad) throws Exception, I18NException {

        RespuestaDistribucion respuestaDistribucion = new RespuestaDistribucion();

        try {

            //Obtenemos plugin
            IDistribucionPlugin distribucionPlugin = (IDistribucionPlugin) pluginEjb.getPlugin(usuarioEntidad.getEntidad().getId(), RegwebConstantes.PLUGIN_DISTRIBUCION);

            if (distribucionPlugin != null) {

                // Enviamos el registro a la Cola de Distribución
                Boolean encolado = colaEjb.enviarAColaDistribucion(re, usuarioEntidad);

                respuestaDistribucion.setEnviadoCola(encolado);
                respuestaDistribucion.setHayPlugin(true);

                return respuestaDistribucion;

            }else{
                respuestaDistribucion.setHayPlugin(false);
                respuestaDistribucion.setEnviadoCola(false);
            }

        } catch (I18NException | Exception e) {
            e.printStackTrace();
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
     * @throws Exception
     * @throws I18NValidationException
     * @throws I18NException
     */
    private Boolean distribuirRegistroEntrada(RegistroEntrada registroEntrada, IDistribucionPlugin distribucionPlugin) throws Exception, I18NValidationException, I18NException {

        Boolean distribuido;

        log.info("------------------------------------------------------------");
        log.info("Distribuyendo el registro: " + registroEntrada.getNumeroRegistroFormateado());
        log.info("");

        // Si no tiene Justificante, lo creamos
        if (!registroEntrada.getRegistroDetalle().getTieneJustificante()) {
            AnexoFull justificante = justificanteEjb.crearJustificante(registroEntrada.getUsuario(), registroEntrada, RegwebConstantes.REGISTRO_ENTRADA, Configuracio.getDefaultLanguage());
            registroEntrada.getRegistroDetalle().getAnexosFull().add(justificante);

            // Si la custodia en diferido está activa, tiene Justificante, pero no está custodiado, no distribuimos!
        }else if (PropiedadGlobalUtil.getCustodiaDiferida(registroEntrada.getUsuario().getEntidad().getId()) && !registroEntrada.getRegistroDetalle().getTieneJustificanteCustodiado()) {
            log.info("El registro: " + registroEntrada.getNumeroRegistroFormateado()+" no se distribuira en esta iteracion porque no tiene el Justificante custodiado");
            return false;
        }

        // Si no hay plugin de distribución configurado, marcamos correctamente la Distribución
        if (distribucionPlugin == null) {
            return true;
        }else{

            distribuido = distribucionPlugin.distribuir(registroEntrada, new Locale(RegwebConstantes.IDIOMA_CATALAN_CODIGO));
        }

        return distribuido;
    }

    /**
     * Procesa un registro de la cola de manera individual
     *
     * @param elemento elemento de la cola
     * @param idEntidad
     * @return
     * @throws Exception
     * @throws I18NException
     */
    @Override
    public Boolean distribuirRegistroEnCola(Cola elemento, Long idEntidad, Long tipoIntegracon) throws Exception {

        Boolean distribuido = false;

        // Integración
        StringBuilder peticion = new StringBuilder();
        Date inicio = new Date();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Distribución desde Cola";
        String hora = "<b>" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(inicio) + "</b>&nbsp;&nbsp;&nbsp;";

        String error = "";

        try {

            //Obtenermos plugin distribución
            IDistribucionPlugin distribucionPlugin = (IDistribucionPlugin) pluginEjb.getPlugin(idEntidad, RegwebConstantes.PLUGIN_DISTRIBUCION);

            //Obtenemos el registro de entrada que se debe distribuir
            RegistroEntrada registroEntrada = null;
            if(distribucionPlugin.getClass().getName().contains("DistribucionGoibPlugin")){
                registroEntrada = registroEntradaEjb.getConAnexosFullDistribuir(elemento.getIdObjeto());
            }else{
                registroEntrada = registroEntradaEjb.getConAnexosFull(elemento.getIdObjeto());
            }

            List<Anexo> anexos = registroEntrada.getRegistroDetalle().getAnexos();
            for (Anexo anexo : anexos) {
            	if (!anexo.getFirmaverificada()) {
            		throw new VerificacioFirmaException("La firma de algunos anexos no está verificada");
            	}
            }
            //Montamos la petición de la integración
            peticion.append("usuario: ").append(registroEntrada.getUsuario().getUsuario().getNombreIdentificador()).append(System.getProperty("line.separator"));
            peticion.append("registro: ").append(registroEntrada.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));
            peticion.append("oficina: ").append(registroEntrada.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
            peticion.append("clase: ").append(distribucionPlugin.getClass().getName()).append(System.getProperty("line.separator"));

            //Distribuimos el registro de entrada.
            distribuido = distribuirRegistroEntrada(registroEntrada, distribucionPlugin);

            if (distribuido) { //Si la distribución ha ido bien
                colaEjb.procesarElementoDistribucion(elemento);
                registroEntradaEjb.marcarDistribuido(registroEntrada);
                integracionEjb.addIntegracionOk(inicio, tipoIntegracon, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, registroEntrada.getUsuario().getEntidad().getId(), registroEntrada.getNumeroRegistroFormateado());
            }

        } catch (Exception | I18NException | I18NValidationException e) {
            log.info("Error distribuyendo registro de la Cola: " + elemento.getDescripcionObjeto());
            e.printStackTrace();
            error = hora + e.getMessage();
            colaEjb.actualizarElementoCola(elemento, idEntidad, error);
            // Añadimos el error a la integración
            integracionEjb.addIntegracionError(tipoIntegracon, descripcion, peticion.toString(), e, null,System.currentTimeMillis() - tiempo, idEntidad, elemento.getDescripcionObjeto());
        }

        return distribuido;

    }

    @Override
    @TransactionTimeout(value = 1800)  // 30 minutos
    public void distribuirRegistrosEnCola(Long idEntidad) throws Exception {

        //obtiene un numero de elementos (configurable) pendientes de distribuir que estan en la cola
        List<Cola> elementosADistribuir = colaEjb.findByTipoEntidad(RegwebConstantes.COLA_DISTRIBUCION, idEntidad, PropiedadGlobalUtil.getElementosCola(idEntidad));

        log.info("");
        log.info("Hay " + elementosADistribuir.size() + " elementos que se van a distribuir en esta iteracion");

        for (Cola elemento : elementosADistribuir) {

            distribuirRegistroEnCola(elemento, idEntidad, RegwebConstantes.INTEGRACION_SCHEDULERS);
        }

    }

    @Override
    public void enviarEmailErrorDistribucion(Entidad entidad) throws Exception {

        Integer maxReintentos = PropiedadGlobalUtil.getMaxReintentosCola(entidad.getId());
        Locale locale = new Locale(RegwebConstantes.IDIOMA_CATALAN_CODIGO);

        //Obtenemos el numero de registros que han alcanzado el máximo de reintentos
        int numRegistrosMaxReintentos = colaEjb.findByTipoMaxReintentos(RegwebConstantes.COLA_DISTRIBUCION, entidad.getId(), maxReintentos).size();
        String entorno = PropiedadGlobalUtil.getEntorno();
        
        if (numRegistrosMaxReintentos > 0) {


            // Obtenemos los usuarios a los que hay que enviarles el mail
            List<Usuario> usuariosANotificar = new ArrayList<Usuario>();

            // Propietario Entidad
            usuariosANotificar.add(entidad.getPropietario());

            // Administradores Entidad
            for (UsuarioEntidad usuarioEntidad : entidad.getAdministradores()) {
                usuariosANotificar.add(usuarioEntidad.getUsuario());
            }

            // Asunto
            String[] argsEntorno = {entorno != null ? entorno : "PRO"};
            String asunto = I18NLogicUtils.tradueix(locale, "cola.mail.asunto", argsEntorno);

            //Montamos el mensaje del mail con el nombre de la Entidad
            String[] args = {Integer.toString(numRegistrosMaxReintentos),entidad.getNombre()};
            String mensajeTexto = I18NLogicUtils.tradueix(locale, "cola.mail.cuerpo", args);

            //Enviamos el mail a todos los usuarios
            InternetAddress addressFrom = new InternetAddress(RegwebConstantes.APLICACION_EMAIL, RegwebConstantes.APLICACION_NOMBRE);

            for (Usuario usuario : usuariosANotificar) {

                if (StringUtils.isNotEmpty(usuario.getEmail())) {

                    try {

                        MailUtils.enviaMail(asunto, mensajeTexto, addressFrom, Message.RecipientType.TO, usuario.getEmail());
                    } catch (Exception e) {
                        //Si se produce una excepción continuamos con el proceso.
                        log.error("Se ha producido un excepcion enviando mail");
                        e.printStackTrace();
                    }

                }
            }
        }
    }


    /**
     * Este método elimina los anexos que no se pueden enviar a Arxiu porque no estan soportados.
     * Son ficheros xml de los cuales no puede hacer el upgrade de la firma y se ha decidido que no se distribuyan.
     *
     * @param original
     * @return
     * @throws Exception
     * @throws I18NException
     * @throws I18NValidationException
     */
    private RegistroEntrada gestionAnexosByAplicacionSIR(RegistroEntrada original) throws Exception, I18NException, I18NValidationException {

        List<AnexoFull> anexosFullADistribuir = new ArrayList<AnexoFull>();
        //Obtenemos los anexos del registro para tratarlos
        List<AnexoFull> anexosFull = original.getRegistroDetalle().getAnexosFull();
        //Lista de anexos para el procesamiento intermedio
        List<AnexoFull> anexosFullIntermedio = new ArrayList<AnexoFull>();


        gestionarByAplicacionByNombreFichero(RegwebConstantes.FICHERO_REGISTROELECTRONICO, anexosFull, anexosFullIntermedio);
        gestionarByAplicacionByNombreFichero(RegwebConstantes.FICHERO_DEFENSORPUEBLO, anexosFullIntermedio, anexosFullADistribuir);

        original.getRegistroDetalle().setAnexosFull(anexosFullADistribuir);

        return original;
    }


    /**
     * Este método lo que hace es eliminar de la lista de anexos a distribuir, aquellos que el Arxiu no soporta que son
     * de formato xml y que no puede hacer el upgrade de la firma.
     * Lo que hace es comparar el nombre del Fichero y la aplicación de la que procede, para determinar si lo elimina o no.
     *
     * @param nombreFichero
     * @param anexosFull
     * @param anexosFullADistribuir
     * @return
     * @throws Exception
     */
    private List<AnexoFull> gestionarByAplicacionByNombreFichero(String nombreFichero, List<AnexoFull> anexosFull, List<AnexoFull> anexosFullADistribuir) throws Exception {
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

}
