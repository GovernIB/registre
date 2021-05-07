package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.ContactoTF;
import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.sir.MensajeControl;
import es.caib.regweb3.model.sir.TipoAnotacion;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.CamposNTI;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
import es.caib.regweb3.model.utils.IndicadorPrueba;
import es.caib.regweb3.persistence.integracion.ArxiuCaibUtils;
import es.caib.regweb3.persistence.utils.FileSystemManager;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.sir.core.excepcion.SIRException;
import es.caib.regweb3.sir.ejb.EmisionLocal;
import es.caib.regweb3.sir.ejb.MensajeLocal;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.hibernate.Session;
import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ejb3.annotation.TransactionTimeout;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */

@Stateless(name = "SirEnvioEJB")
@SecurityDomain("seycon")
public class SirEnvioBean implements SirEnvioLocal {

    protected final Logger log = Logger.getLogger(getClass());

    @PersistenceContext(unitName = "regweb3")
    private EntityManager em;

    @EJB private RegistroEntradaLocal registroEntradaEjb;
    @EJB private RegistroSalidaLocal registroSalidaEjb;
    @EJB private RegistroSirLocal registroSirEjb;
    @EJB private OficioRemisionLocal oficioRemisionEjb;
    @EJB private JustificanteLocal justificanteEjb;
    @EJB private EmisionLocal emisionEjb;
    @EJB private MensajeLocal mensajeEjb;
    @EJB private MensajeControlLocal mensajeControlEjb;
    @EJB private TrazabilidadSirLocal trazabilidadSirEjb;
    @EJB private ContadorLocal contadorEjb;
    @EJB private IntegracionLocal integracionEjb;
    @EJB private DistribucionLocal distribucionEjb;
    @EJB private OrganismoLocal organismoEjb;
    @Autowired ArxiuCaibUtils arxiuCaibUtils;


    @Override
    @TransactionTimeout(value = 3000)  // 50 minutos
    public Integer aceptarRegistrosERTE(List<Long> registros, String destino, Oficina oficina,Long idLibro, UsuarioEntidad usuarioEntidad, Long idEntidad) throws Exception{

        // ruta actual: /app/caib/regweb/archivos
        // ruta erte: /app/caib/regweb/dades/erte

        final String rutaERTE = PropiedadGlobalUtil.getErtePath(idEntidad);

        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss");

        try{

            log.info("Total registros pendientes erte: " + registros.size());
            log.info("");

            for(Long erte:registros){

                try{

                    // Cargamos el registro
                    RegistroSir registroSir = registroSirEjb.findById(erte);

                    log.info("Procesando el registro sir pendiente: " + registroSir.getId());

                    // Crear List<CamposNTI> ficticia
                    List<CamposNTI> camposNTIS =  new ArrayList<CamposNTI>();
                    for(AnexoSir anexoSir:registroSir.getAnexos()){
                        CamposNTI campoNTI = new CamposNTI();
                        campoNTI.setId(anexoSir.getId());

                        camposNTIS.add(campoNTI);
                    }

                    Organismo organismoDestino = organismoEjb.findByCodigoEntidadLigero(destino, idEntidad);

                    //Aceptar el RegistroSir
                    RegistroEntrada registroEntrada = aceptarRegistroSir(registroSir, usuarioEntidad, oficina,idLibro,RegwebConstantes.IDIOMA_CASTELLANO_ID,camposNTIS, organismoDestino.getId(), true);

                    // Copiamos cada anexo en la carpeta creada
                    for(AnexoSir anexoSir:registroSir.getAnexos()){

                        Archivo archivo = anexoSir.getAnexo();

                        File origen = FileSystemManager.getArchivo(archivo.getId());

                        String rutaDestino = rutaERTE + formatDate.format(registroEntrada.getFecha()) + " - " + registroEntrada.getNumeroRegistroFormateado().replace("/","-");

                        Files.createDirectories(Paths.get(rutaDestino));

                        try{
                            log.info("Copiamos la documentación del registro aceptado a: " + rutaDestino);
                            Files.copy(origen.toPath(), (new File(rutaDestino +"/"+ archivo.getNombre())).toPath(), StandardCopyOption.REPLACE_EXISTING);

                        }catch (Exception e){
                            log.info("No encuentra el fichero");
                        }
                    }

                    distribucionEjb.distribuir(registroEntrada, usuarioEntidad);

                }catch (Exception e){
                    log.info("Error procesando un registro sir");
                }
            }


            return registros.size();

        } catch(Exception e){
            log.info("Error generando carpetas ERTE");
            e.printStackTrace();
        } catch (I18NValidationException | I18NException e) {
            log.info("Error aceptando el RegistroSir");
            e.printStackTrace();
        }

        return 0;

    }

    @Override
    @TransactionTimeout(value = 3000)  // 50 minutos
    public Integer copiarDocumentacionERTE(List<Long> registros, Long idEntidad) throws Exception{

        // ruta actual: /app/caib/regweb/archivos
        // ruta erte: /app/caib/regweb/dades/erte

        final String rutaERTE = PropiedadGlobalUtil.getErtePath(idEntidad);

        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss");

        try{

            log.info("Total registros aceptados erte: " + registros.size());
            log.info("");

            for(Long erte:registros){

                try{

                    // Cargamos el registro
                    TrazabilidadSir trazabilidadSir = trazabilidadSirEjb.getByRegistroSirAceptado(erte);

                    RegistroSir registroSir = trazabilidadSir.getRegistroSir();
                    RegistroEntrada registroEntrada = trazabilidadSir.getRegistroEntrada();

                    log.info("Procesando el registro aceptado: " + registroSir.getId());

                    // Copiamos cada anexo en la carpeta creada
                    for(AnexoSir anexoSir:registroSir.getAnexos()){

                        Archivo archivo = anexoSir.getAnexo();

                        File origen = FileSystemManager.getArchivo(archivo.getId());

                        String rutaDestino = rutaERTE + formatDate.format(registroEntrada.getFecha()) + " - " + registroEntrada.getNumeroRegistroFormateado().replace("/","-");

                        Files.createDirectories(Paths.get(rutaDestino));

                        try{
                            log.info("Copiamos la documentación del registro aceptado a: " + rutaDestino);
                            Files.copy(origen.toPath(), (new File(rutaDestino +"/"+ archivo.getNombre())).toPath(), StandardCopyOption.REPLACE_EXISTING);

                        }catch (Exception e){
                            log.info("No encuentra el fichero");
                        }
                    }

                }catch (Exception e){
                    log.info("Error procesando un registro sir");
                }
            }


            return registros.size();

        } catch(Exception e){
            log.info("Error generando carpetas ERTE");
            e.printStackTrace();
        }

        return 0;

    }



    /**
     * @param idRegistro
     * @param oficinaActiva
     * @param usuario
     * @param codigoOficinaSir
     * @return
     * @throws Exception
     * @throws I18NException
     */
    @Override
    public OficioRemision enviarIntercambio(Long tipoRegistro, Long idRegistro,
                                            Oficina oficinaActiva, UsuarioEntidad usuario, String codigoOficinaSir)
            throws Exception, I18NException, I18NValidationException {

        RegistroSir registroSir = null;
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Envio SIR a " + codigoOficinaSir;
        peticion.append("TipoAnotación: ").append(TipoAnotacion.ENVIO.getName()).append(System.getProperty("line.separator"));

        // Creamos el OficioRemision
        OficioRemision oficioRemision = new OficioRemision();
        oficioRemision.setSir(true);
        oficioRemision.setEstado(RegwebConstantes.OFICIO_SIR_ENVIADO);
        oficioRemision.setFechaEstado(new Date());
        oficioRemision.setOficina(oficinaActiva);
        oficioRemision.setUsuarioResponsable(usuario);

        try {

            // OficinaSir destino
            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());
            OficinaTF oficinaSirDestino = oficinasService.obtenerOficina(codigoOficinaSir, null, null);

            //Obtenemos los contactos de la oficina Sir de destino
            String contactosEntidadRegistralDestino = getContactosOficinaSir(oficinaSirDestino);

            if (tipoRegistro.equals(RegwebConstantes.REGISTRO_ENTRADA)) {

                RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFull(idRegistro);
                RegistroDetalle registroDetalle = registroEntrada.getRegistroDetalle();

                peticion.append("Número registro: ").append(registroEntrada.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));

                log.info("----------------------------------------------------------------------------------------------");
                log.info("Enviando FicheroIntercambio del registro: " + registroEntrada.getNumeroRegistroFormateado() + " mediante SIR a: " + oficinaSirDestino.getDenominacion());
                log.info("");

                // Si no tiene generado el Justificante, lo hacemos
                if (!registroDetalle.getTieneJustificante()) {

                    // Creamos el anexo del justificante y se lo añadimos al registro
                    AnexoFull anexoFull = justificanteEjb.crearJustificante(usuario, registroEntrada, tipoRegistro, Configuracio.getDefaultLanguage());
                    registroDetalle.getAnexosFull().add(anexoFull);
                }

                // Actualizamos el Registro con campos SIR
                registroDetalle.setIndicadorPrueba(IndicadorPrueba.NORMAL);
                registroDetalle.setIdentificadorIntercambio(generarIdentificadorIntercambio(registroEntrada.getOficina().getCodigo(), usuario.getEntidad()));
                registroDetalle.setCodigoEntidadRegistralDestino(oficinaSirDestino.getCodigo());
                registroDetalle.setDecodificacionEntidadRegistralDestino(oficinaSirDestino.getDenominacion());
                registroDetalle.setTipoAnotacion(TipoAnotacion.ENVIO.getValue());
                registroDetalle.setDecodificacionTipoAnotacion(TipoAnotacion.ENVIO.getName());

                // Nos aseguramos que los campos origen sean los del registro, sobreescribiendo los posibles valores de un oficio interno
                registroDetalle.setOficinaOrigen(registroEntrada.getOficina());
                registroDetalle.setOficinaOrigenExternoCodigo(null);
                registroDetalle.setOficinaOrigenExternoDenominacion(null);
                registroDetalle.setNumeroRegistroOrigen(registroEntrada.getNumeroRegistroFormateado());
                registroDetalle.setFechaOrigen(registroEntrada.getFecha());

                // Actualizamos el registro
                registroEntrada = registroEntradaEjb.merge(registroEntrada);

                // Datos del Oficio de remisión
                oficioRemision.setLibro(new Libro(registroEntrada.getLibro().getId()));
                oficioRemision.setIdentificadorIntercambio(registroEntrada.getRegistroDetalle().getIdentificadorIntercambio());
                oficioRemision.setTipoOficioRemision(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA);
                oficioRemision.setDestinoExternoCodigo(registroEntrada.getDestinoExternoCodigo());
                oficioRemision.setDestinoExternoDenominacion(registroEntrada.getDestinoExternoDenominacion());
                oficioRemision.setRegistrosEntrada(Collections.singletonList(registroEntrada));
                oficioRemision.setOrganismoDestinatario(null);
                oficioRemision.setRegistrosSalida(null);
                oficioRemision.setCodigoEntidadRegistralDestino(oficinaSirDestino.getCodigo());
                oficioRemision.setDecodificacionEntidadRegistralDestino(oficinaSirDestino.getDenominacion());
                oficioRemision.setContactosEntidadRegistralDestino(contactosEntidadRegistralDestino);

                //Transformamos el registro de Entrada a RegistroSir
                registroSir = registroSirEjb.transformarRegistroEntrada(registroEntrada);

            } else if (tipoRegistro.equals(RegwebConstantes.REGISTRO_SALIDA)) {

                RegistroSalida registroSalida = registroSalidaEjb.getConAnexosFull(idRegistro);
                RegistroDetalle registroDetalle = registroSalida.getRegistroDetalle();

                peticion.append("Número registro: ").append(registroSalida.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));

                log.info("----------------------------------------------------------------------------------------------");
                log.info("Enviando FicheroIntercambio del registro: " + registroSalida.getNumeroRegistroFormateado() + " mediante SIR a: " + oficinaSirDestino.getDenominacion());
                log.info("");


                // Si no tiene generado el Justificante, lo hacemos
                if (!registroDetalle.getTieneJustificante()) {
                    // Creamos el anexo del justificante y se lo añadimos al registro
                    AnexoFull anexoFull = justificanteEjb.crearJustificante(usuario, registroSalida, tipoRegistro, Configuracio.getDefaultLanguage());
                    registroDetalle.getAnexosFull().add(anexoFull);
                }

                // Actualizamos el Registro con campos SIR
                registroDetalle.setIndicadorPrueba(IndicadorPrueba.NORMAL);
                registroDetalle.setIdentificadorIntercambio(generarIdentificadorIntercambio(registroSalida.getOficina().getCodigo(), usuario.getEntidad()));
                registroDetalle.setCodigoEntidadRegistralDestino(oficinaSirDestino.getCodigo());
                registroDetalle.setDecodificacionEntidadRegistralDestino(oficinaSirDestino.getDenominacion());
                registroDetalle.setTipoAnotacion(TipoAnotacion.ENVIO.getValue());
                registroDetalle.setDecodificacionTipoAnotacion(TipoAnotacion.ENVIO.getName());

                // Nos aseguramos que los campos origen sean los del registro, sobreescribiendo los posibles valores de un oficio interno
                registroDetalle.setOficinaOrigen(registroSalida.getOficina());
                registroDetalle.setNumeroRegistroOrigen(registroSalida.getNumeroRegistroFormateado());
                registroDetalle.setFechaOrigen(registroSalida.getFecha());

                // Actualizamos el registro
                registroSalida = registroSalidaEjb.merge(registroSalida);

                // Datos del Oficio de remisión
                oficioRemision.setLibro(new Libro(registroSalida.getLibro().getId()));
                oficioRemision.setIdentificadorIntercambio(registroSalida.getRegistroDetalle().getIdentificadorIntercambio());
                oficioRemision.setTipoOficioRemision(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA);
                oficioRemision.setDestinoExternoCodigo(registroSalida.interesadoDestinoCodigo());
                oficioRemision.setDestinoExternoDenominacion(registroSalida.getInteresadoDestinoDenominacion());
                oficioRemision.setRegistrosSalida(Collections.singletonList(registroSalida));
                oficioRemision.setOrganismoDestinatario(null);
                oficioRemision.setRegistrosEntrada(null);
                oficioRemision.setCodigoEntidadRegistralDestino(oficinaSirDestino.getCodigo());
                oficioRemision.setDecodificacionEntidadRegistralDestino(oficinaSirDestino.getDenominacion());
                oficioRemision.setContactosEntidadRegistralDestino(contactosEntidadRegistralDestino);

                // Transformamos el RegistroSalida en un RegistroSir
                registroSir = registroSirEjb.transformarRegistroSalida(registroSalida);

            }

            // Integración
            peticion.append("IdentificadorIntercambio: ").append(registroSir.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
            peticion.append("Origen: ").append(registroSir.getDecodificacionEntidadRegistralOrigen()).append(System.getProperty("line.separator"));
            peticion.append("Destino: ").append(registroSir.getDecodificacionEntidadRegistralDestino()).append(System.getProperty("line.separator"));
            peticion.append("Usuario: ").append(usuario.getNombreCompleto()).append(System.getProperty("line.separator"));

            // Registramos el Oficio de Remisión SIR
            oficioRemision = oficioRemisionEjb.registrarOficioRemision(oficioRemision, RegwebConstantes.REGISTRO_OFICIO_SIR);

            // Enviamos el Registro al Componente CIR
            emisionEjb.enviarFicheroIntercambio(registroSir);

            // Modificamos el estado del OficioRemision
            oficioRemisionEjb.modificarEstado(oficioRemision.getId(), RegwebConstantes.OFICIO_SIR_ENVIADO);

            // Integración
            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, registroSir.getEntidad().getId(), registroSir.getIdentificadorIntercambio());

            log.info("");
            log.info("Fin enviando FicheroIntercambio del registro: " + registroSir.getNumeroRegistro());
            log.info("----------------------------------------------------------------------------------------------");


        } catch (SIRException | I18NValidationException | I18NException s) {
            s.printStackTrace();
            if (registroSir != null) {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), s, null, System.currentTimeMillis() - tiempo, usuario.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
            }
            throw s;
        } catch (Exception e) {
            e.printStackTrace();
            if (registroSir != null) {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, usuario.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
            }
            throw e;
        }

        return oficioRemision;
    }

    @Override
    public void reenviarIntercambio(Long idOficioRemision)throws Exception, I18NException{

        OficioRemision oficioRemision = oficioRemisionEjb.findById(idOficioRemision);
        RegistroSir registroSir = null;
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "";

        peticion.append("TipoAnotación: ").append(TipoAnotacion.ENVIO.getName()).append(System.getProperty("line.separator"));

        try {

            // Obtenemos el Registro correspondiente
            if (oficioRemision.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA)) {
                RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFull(oficioRemision.getRegistrosEntrada().get(0).getId());
                registroSir = registroSirEjb.transformarRegistroEntrada(registroEntrada);

            } else if (oficioRemision.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA)) {
                RegistroSalida registroSalida = registroSalidaEjb.getConAnexosFull(oficioRemision.getRegistrosSalida().get(0).getId());
                registroSir = registroSirEjb.transformarRegistroSalida(registroSalida);
            }

            // Integración
            descripcion = "Se vuelve a enviar el Intercambio a " + registroSir.getCodigoEntidadRegistralDestino();
            peticion.append("IdentificadorIntercambio: ").append(registroSir.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
            peticion.append("Origen: ").append(registroSir.getDecodificacionEntidadRegistralOrigen()).append(System.getProperty("line.separator"));
            peticion.append("Destino: ").append(registroSir.getDecodificacionEntidadRegistralDestino()).append(System.getProperty("line.separator"));

            // Enviamos el Registro al Componente CIR
            if(registroSir != null){
                emisionEjb.enviarFicheroIntercambio(registroSir);
            }

            // Integración
            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, registroSir.getEntidad().getId(), registroSir.getIdentificadorIntercambio());

        }catch(I18NException | Exception e){
            log.info("Ha ocurrido un error reenviando el intercambio: " + e.getLocalizedMessage());
            e.printStackTrace();
            if (registroSir != null) {
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, registroSir.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
            }
            throw e;
        }

    }

    /**
     * Acepta un RegistroSir, creando un Registro de Entrada
     *
     * @param registroSir
     * @throws Exception
     */
    @Override
    public RegistroEntrada aceptarRegistroSir(RegistroSir registroSir, UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro, Long idIdioma, List<CamposNTI> camposNTIs, Long idOrganismoDestino, Boolean distribuir)
            throws Exception, I18NException, I18NValidationException {

        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Aceptando RegistroSir: " + TipoAnotacion.getTipoAnotacion(registroSir.getTipoAnotacion()).getName();
        peticion.append("IdentificadorIntercambio: ").append(registroSir.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
        peticion.append("Origen: ").append(registroSir.getDecodificacionEntidadRegistralOrigen()).append(System.getProperty("line.separator"));
        peticion.append("Destino: ").append(registroSir.getDecodificacionEntidadRegistralDestino()).append(System.getProperty("line.separator"));
        peticion.append("Usuario: ").append(usuario.getNombreCompleto()).append(System.getProperty("line.separator"));

        log.info("");
        log.info("Aceptando RegistroSir " + registroSir.getIdentificadorIntercambio());

        RegistroEntrada registroEntrada;

        try {
            // Creamos y registramos el RegistroEntrada a partir del RegistroSir aceptado

            registroEntrada = registroSirEjb.aceptarRegistroSirEntrada(registroSir, usuario, oficinaActiva, idLibro, idIdioma, camposNTIs, idOrganismoDestino);

            // Enviamos el Mensaje de Confirmación
            enviarMensajeConfirmacion(registroSir, registroEntrada.getNumeroRegistroFormateado(), registroEntrada.getFecha());

            // Distribuimos el Registro de Entrada si así se ha indicado
            if(distribuir){
                distribucionEjb.distribuir(registroEntrada, usuario);
            }

            // Integracion
            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, registroSir.getEntidad().getId(), registroSir.getIdentificadorIntercambio());

            return registroEntrada;

        } catch (I18NException | I18NValidationException | Exception e) {
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, registroSir.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
            throw e;
        }

    }

    @Override
    public void reenviarRegistroSir(RegistroSir registroSir, Oficina oficinaReenvio, Oficina oficinaActiva, Usuario usuario, String observaciones) throws Exception {

        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Reenviando RegistroSir a " + oficinaReenvio.getDenominacion();
        peticion.append("IdentificadorIntercambio: ").append(registroSir.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
        peticion.append("Origen: ").append(registroSir.getDecodificacionEntidadRegistralOrigen()).append(System.getProperty("line.separator"));
        peticion.append("Destino: ").append(registroSir.getDecodificacionEntidadRegistralDestino()).append(System.getProperty("line.separator"));
        peticion.append("Usuario: ").append(usuario.getNombreCompleto()).append(System.getProperty("line.separator"));

        log.info("----------------------------------------------------------------------------------------------");
        log.info("Reenviando RegistroSIR: " + registroSir.getNumeroRegistro() + " mediante SIR a: " + oficinaReenvio.getDenominacion());
        log.info("");

        try {

            // Actualizamos la oficina destino con la escogida por el usuario
            registroSir.setCodigoEntidadRegistralDestino(oficinaReenvio.getCodigo());
            registroSir.setDecodificacionEntidadRegistralDestino(oficinaReenvio.getDenominacion());
            registroSir.setCodigoUnidadTramitacionDestino(oficinaReenvio.getOrganismoResponsable().getCodigo());
            registroSir.setDecodificacionUnidadTramitacionDestino(oficinaReenvio.getOrganismoResponsable().getDenominacion());

            // Actualizamos la oficina de origen con la oficina activa
            registroSir.setCodigoEntidadRegistralOrigen(oficinaActiva.getCodigo());
            registroSir.setDecodificacionEntidadRegistralOrigen(oficinaActiva.getDenominacion());

            // Modificamos usuario, contacto, aplicacion
            registroSir.setAplicacion(RegwebConstantes.CODIGO_APLICACION);
            registroSir.setNombreUsuario(usuario.getNombreCompleto());
            registroSir.setContactoUsuario(usuario.getEmail());
            registroSir.setTipoAnotacion(TipoAnotacion.REENVIO.getValue());
            registroSir.setDecodificacionTipoAnotacion(observaciones);

            // Actualizamos el RegistroSir
            registroSir = registroSirEjb.merge(registroSir);
            registroSir = registroSirEjb.getRegistroSirConAnexos(registroSir.getId());

            // Enviamos el Registro al Componente CIR
            emisionEjb.reenviarFicheroIntercambio(registroSir);

            // Creamos la TrazabilidadSir para el Reenvio
            TrazabilidadSir trazabilidadSir = new TrazabilidadSir(RegwebConstantes.TRAZABILIDAD_SIR_REENVIO);
            trazabilidadSir.setRegistroSir(registroSir);
            trazabilidadSir.setCodigoEntidadRegistralOrigen(oficinaActiva.getCodigo());
            trazabilidadSir.setDecodificacionEntidadRegistralOrigen(oficinaActiva.getDenominacion());
            trazabilidadSir.setCodigoEntidadRegistralDestino(oficinaReenvio.getCodigo());
            trazabilidadSir.setDecodificacionEntidadRegistralDestino(oficinaReenvio.getDenominacion());
            trazabilidadSir.setCodigoUnidadTramitacionDestino(oficinaReenvio.getOrganismoResponsable().getCodigo());
            trazabilidadSir.setDecodificacionUnidadTramitacionDestino(oficinaReenvio.getOrganismoResponsable().getDenominacion());
            trazabilidadSir.setAplicacion(RegwebConstantes.CODIGO_APLICACION);
            trazabilidadSir.setNombreUsuario(usuario.getNombreCompleto());
            trazabilidadSir.setContactoUsuario(usuario.getEmail());
            trazabilidadSir.setObservaciones(observaciones);
            trazabilidadSirEjb.persist(trazabilidadSir);

            // Modificamos el estado del RegistroSir
            registroSirEjb.modificarEstado(registroSir.getId(), EstadoRegistroSir.REENVIADO);

            // Integracion
            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, registroSir.getEntidad().getId(), registroSir.getIdentificadorIntercambio());

            log.info("");
            log.info("Fin reenviando RegistroSIR: " + registroSir.getNumeroRegistro());
            log.info("----------------------------------------------------------------------------------------------");

        } catch (Exception e) {
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, registroSir.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
            throw e;
        }

    }

    /**
     * @param registroSir
     * @param oficinaActiva
     * @param usuario
     * @return
     * @throws Exception
     */
    @Override
    public void rechazarRegistroSir(RegistroSir registroSir, Oficina oficinaActiva, Usuario usuario, String observaciones) throws Exception {

        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Rechazando RegistroSir";
        peticion.append("IdentificadorIntercambio: ").append(registroSir.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
        peticion.append("Origen: ").append(registroSir.getDecodificacionEntidadRegistralOrigen()).append(System.getProperty("line.separator"));
        peticion.append("Destino: ").append(registroSir.getDecodificacionEntidadRegistralDestino()).append(System.getProperty("line.separator"));
        peticion.append("Usuario: ").append(usuario.getNombreCompleto()).append(System.getProperty("line.separator"));

        log.info("----------------------------------------------------------------------------------------------");
        log.info("Rechazando RegistroSIR: " + registroSir.getNumeroRegistro() + " mediante SIR a: " + registroSir.getDecodificacionEntidadRegistralInicio());
        log.info("");

        try {

            // Modificamos la oficina destino con la de inicio
            registroSir.setCodigoEntidadRegistralDestino(registroSir.getCodigoEntidadRegistralInicio());
            registroSir.setDecodificacionEntidadRegistralDestino(registroSir.getDecodificacionEntidadRegistralInicio());
            //registroSir.setCodigoUnidadTramitacionDestino("");
            //registroSir.setDecodificacionUnidadTramitacionDestino("");

            // Modificamos la oficina de origen con la oficina activa
            registroSir.setCodigoEntidadRegistralOrigen(oficinaActiva.getCodigo());
            registroSir.setDecodificacionEntidadRegistralOrigen(oficinaActiva.getDenominacion());

            // Modificamos usuario, contacto, aplicacion
            registroSir.setAplicacion(RegwebConstantes.CODIGO_APLICACION);
            registroSir.setNombreUsuario(usuario.getNombreCompleto());
            registroSir.setContactoUsuario(usuario.getEmail());

            registroSir.setTipoAnotacion(TipoAnotacion.RECHAZO.getValue());
            registroSir.setDecodificacionTipoAnotacion(observaciones);

            registroSir = registroSirEjb.merge(registroSir);

            registroSir = registroSirEjb.getRegistroSirConAnexos(registroSir.getId());

            // Rechazamos el RegistroSir
            emisionEjb.rechazarFicheroIntercambio(registroSir);

            // Creamos la TrazabilidadSir para el Rechazo
            TrazabilidadSir trazabilidadSir = new TrazabilidadSir(RegwebConstantes.TRAZABILIDAD_SIR_RECHAZO);
            trazabilidadSir.setRegistroSir(registroSir);
            trazabilidadSir.setCodigoEntidadRegistralOrigen(oficinaActiva.getCodigo());
            trazabilidadSir.setDecodificacionEntidadRegistralOrigen(oficinaActiva.getDenominacion());
            trazabilidadSir.setCodigoEntidadRegistralDestino(registroSir.getCodigoEntidadRegistralInicio());
            trazabilidadSir.setDecodificacionEntidadRegistralDestino(registroSir.getDecodificacionEntidadRegistralInicio());
            trazabilidadSir.setCodigoUnidadTramitacionDestino(null);
            trazabilidadSir.setDecodificacionUnidadTramitacionDestino(null);
            trazabilidadSir.setAplicacion(RegwebConstantes.CODIGO_APLICACION);
            trazabilidadSir.setNombreUsuario(usuario.getNombreCompleto());
            trazabilidadSir.setContactoUsuario(usuario.getEmail());
            trazabilidadSir.setObservaciones(observaciones);
            trazabilidadSirEjb.persist(trazabilidadSir);

            // Modificamos el estado del RegistroSir
            registroSirEjb.modificarEstado(registroSir.getId(), EstadoRegistroSir.RECHAZADO);

            // Integracion
            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, registroSir.getEntidad().getId(), registroSir.getIdentificadorIntercambio());

            log.info("");
            log.info("Fin rechazando RegistroSIR: " + registroSir.getNumeroRegistro());
            log.info("----------------------------------------------------------------------------------------------");

        } catch (Exception e) {
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, registroSir.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
            throw e;
        }

    }

    @Override
    public void reenviarIntercambio(Long tipoRegistro, Long idRegistro, Oficina oficinaReenvio, Oficina oficinaActiva, UsuarioEntidad usuario, String observaciones) throws Exception, I18NException, I18NValidationException {

        RegistroSir registroSir = null;
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Reenvío SIR a " + oficinaReenvio.getDenominacion();
        peticion.append("TipoAnotación: ").append(TipoAnotacion.ENVIO.getName()).append(System.getProperty("line.separator"));

        // Creamos el OficioRemision
        OficioRemision oficioRemision = new OficioRemision();
        oficioRemision.setSir(true);
        oficioRemision.setEstado(RegwebConstantes.OFICIO_EXTERNO_ENVIADO);
        oficioRemision.setFechaEstado(new Date());
        oficioRemision.setOficina(oficinaActiva);
        oficioRemision.setUsuarioResponsable(usuario);

        try {

            if (tipoRegistro.equals(RegwebConstantes.REGISTRO_ENTRADA)) {

                RegistroEntrada registroEntrada = registroEntradaEjb.findByIdConAnexos(idRegistro);
                RegistroDetalle registroDetalle = registroEntrada.getRegistroDetalle();

                peticion.append("Número registro: ").append(registroEntrada.getNumeroRegistroFormateado()).append(System.getProperty("line.separator"));

                log.info("----------------------------------------------------------------------------------------------");
                log.info("Reenviando FicheroIntercambio del registro: " + registroEntrada.getNumeroRegistroFormateado() + " mediante SIR a: " + oficinaReenvio.getDenominacion());
                log.info("");

                // Actualizamos el Registro con campos SIR
                registroDetalle.setIndicadorPrueba(IndicadorPrueba.NORMAL);
                registroDetalle.setCodigoEntidadRegistralDestino(oficinaReenvio.getCodigo());
                registroDetalle.setDecodificacionEntidadRegistralDestino(oficinaReenvio.getDenominacion());
                registroDetalle.setTipoAnotacion(TipoAnotacion.REENVIO.getValue());
                registroDetalle.setDecodificacionTipoAnotacion(observaciones);

                // Actualizamos el registro
                registroEntrada = registroEntradaEjb.merge(registroEntrada);

                // Datos del Oficio de remisión
                oficioRemision.setLibro(new Libro(registroEntrada.getLibro().getId()));
                oficioRemision.setIdentificadorIntercambio(registroEntrada.getRegistroDetalle().getIdentificadorIntercambio());
                oficioRemision.setTipoOficioRemision(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA);
                oficioRemision.setDestinoExternoCodigo(oficinaReenvio.getOrganismoResponsable().getCodigo());
                oficioRemision.setDestinoExternoDenominacion(oficinaReenvio.getOrganismoResponsable().getDenominacion());
                oficioRemision.setRegistrosEntrada(Collections.singletonList(registroEntrada));
                oficioRemision.setOrganismoDestinatario(null);
                oficioRemision.setRegistrosSalida(null);

                // Transformamos el RegistroEntrada en un RegistroSir
                registroSir = registroSirEjb.transformarRegistroEntrada(registroEntrada);

            } else if (tipoRegistro.equals(RegwebConstantes.REGISTRO_SALIDA)) {

                RegistroSalida registroSalida = registroSalidaEjb.findById(idRegistro);
                RegistroDetalle registroDetalle = registroSalida.getRegistroDetalle();

                log.info("----------------------------------------------------------------------------------------------");
                log.info("Enviando FicheroIntercambio del registro: " + registroSalida.getNumeroRegistroFormateado() + " mediante SIR a: " + oficinaReenvio.getDenominacion());
                log.info("");

                // Actualizamos el Registro con campos SIR
                registroDetalle.setIndicadorPrueba(IndicadorPrueba.NORMAL);
                registroDetalle.setCodigoEntidadRegistralDestino(oficinaReenvio.getCodigo());
                registroDetalle.setDecodificacionEntidadRegistralDestino(oficinaReenvio.getDenominacion());
                registroDetalle.setTipoAnotacion(TipoAnotacion.REENVIO.getValue());
                registroDetalle.setDecodificacionTipoAnotacion(observaciones);

                // Actualizamos el registro
                registroSalida = registroSalidaEjb.merge(registroSalida);

                // Datos del Oficio de remisión
                oficioRemision.setLibro(new Libro(registroSalida.getLibro().getId()));
                oficioRemision.setIdentificadorIntercambio(registroSalida.getRegistroDetalle().getIdentificadorIntercambio());
                oficioRemision.setTipoOficioRemision(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA);
                oficioRemision.setDestinoExternoCodigo(registroSalida.interesadoDestinoCodigo());
                oficioRemision.setDestinoExternoDenominacion(registroSalida.getInteresadoDestinoDenominacion());
                oficioRemision.setRegistrosSalida(Collections.singletonList(registroSalida));
                oficioRemision.setOrganismoDestinatario(null);
                oficioRemision.setRegistrosEntrada(null);

                // Transformamos el RegistroSalida en un RegistroSir
                registroSir = registroSirEjb.transformarRegistroSalida(registroSalida);

            }

            // Integración
            peticion.append("IdentificadorIntercambio: ").append(registroSir.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
            peticion.append("Origen: ").append(registroSir.getDecodificacionEntidadRegistralOrigen()).append(System.getProperty("line.separator"));
            peticion.append("Destino: ").append(registroSir.getDecodificacionEntidadRegistralDestino()).append(System.getProperty("line.separator"));
            peticion.append("Usuario: ").append(usuario.getNombreCompleto()).append(System.getProperty("line.separator"));

            // Registramos el Oficio de Remisión SIR
            oficioRemision = oficioRemisionEjb.registrarOficioRemision(oficioRemision, RegwebConstantes.REGISTRO_OFICIO_SIR);

            // Actualizamos la unidad de tramitación destino con el organismo responsable de la oficina de reenvio
            registroSir.setCodigoUnidadTramitacionDestino(oficinaReenvio.getOrganismoResponsable().getCodigo());
            registroSir.setDecodificacionUnidadTramitacionDestino(oficinaReenvio.getOrganismoResponsable().getDenominacion());

            // Enviamos el Registro al Componente CIR
            emisionEjb.reenviarFicheroIntercambio(registroSir);

            // Modificamos el estado del OficioRemision
            oficioRemisionEjb.modificarEstado(oficioRemision.getId(), RegwebConstantes.OFICIO_SIR_REENVIADO);

            // Integración
            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, registroSir.getEntidad().getId(), registroSir.getIdentificadorIntercambio());

            log.info("");
            log.info("Fin reenviando FicheroIntercambio del registro: " + registroSir.getNumeroRegistro());
            log.info("----------------------------------------------------------------------------------------------");

        } catch (Exception e) {
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, usuario.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
            throw e;

        } catch (I18NException e) {
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, usuario.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
            throw e;

        } catch (I18NValidationException e) {
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, usuario.getEntidad().getId(), registroSir.getIdentificadorIntercambio());
            throw e;

        }

    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean enviarACK(Long idRegistroSir) throws Exception {

        try {
            RegistroSir registroSir = registroSirEjb.findById(idRegistroSir);

            MensajeControl mensaje = new MensajeControl(RegwebConstantes.TIPO_COMUNICACION_ENVIADO);
            mensaje.setCodigoEntidadRegistralOrigen(registroSir.getCodigoEntidadRegistralDestino());
            mensaje.setCodigoEntidadRegistralDestino(registroSir.getCodigoEntidadRegistralOrigen());
            mensaje.setIdentificadorIntercambio(registroSir.getIdentificadorIntercambio());
            mensaje.setEntidad(registroSir.getEntidad());

            mensaje = mensajeEjb.enviarACK(mensaje);

            mensajeControlEjb.persist(mensaje);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean enviarConfirmacion(Long idRegistroSir) throws Exception {

        TrazabilidadSir trazabilidadSir = trazabilidadSirEjb.getByRegistroSirAceptado(idRegistroSir);

        if (trazabilidadSir.getTipo().equals(RegwebConstantes.TRAZABILIDAD_SIR_ACEPTADO)) {

            RegistroEntrada registroEntrada = trazabilidadSir.getRegistroEntrada();

            enviarMensajeConfirmacion(trazabilidadSir.getRegistroSir(), registroEntrada.getNumeroRegistroFormateado(), registroEntrada.getFecha());

            return true;
        }

        return false;
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Boolean reenviarMensaje(MensajeControl mensaje) throws Exception {

        try {

            // Reenviar el mensaje
            mensaje = mensajeEjb.reenviarMensajeControl(mensaje);

            // Volver a guardar el mensaje enviado
            // Detach de la sesion para poder duplicar el registro
            Session session = (Session) em.getDelegate();
            session.evict(mensaje);
            mensaje.setId(null);
            mensaje.setFecha(new Date());
            mensajeControlEjb.persist(mensaje);

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            log.info("Error reenviando el mensaje de control : " + mensaje.getIdentificadorIntercambio());
        }

        return false;

    }

    @Override
    public void reintentarEnviosSinConfirmacion(Entidad entidad) throws Exception {

        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Reintentar Envios Sir sin confirmación";
        Date inicio = new Date();

        try {

            peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

            // RegistrosSir pendientes de volver a intentar su envío
            List<Long> registrosSir = registroSirEjb.getEnviadosSinAck(entidad.getId());

            peticion.append("registrosSir: ").append(registrosSir.size()).append(System.getProperty("line.separator"));

            if (registrosSir.size() > 0) {


                log.info("Hay " + registrosSir.size() + " RegistrosSir pendientes de volver a enviar al nodo CIR");

                // Volvemos a enviar los RegistrosSir
                for (Long registroSir : registrosSir) {

                    reintentarEnvioRegistroSir(registroSir);
                }
            } else {
                log.info("No hay RegistrosSir pendientes de volver a enviar al nodo CIR");
            }

            // OficiosRemision pendientes de volver a intentar su envío
            List<OficioRemision> oficios = oficioRemisionEjb.getEnviadosSinAck(entidad.getId());

            peticion.append("oficios: ").append(oficios.size()).append(System.getProperty("line.separator"));

            if (oficios.size() > 0) {

                log.info("Hay " + oficios.size() + " Oficios de Remision pendientes de volver a enviar al nodo CIR");

                // Volvemos a enviar los OficiosRemision
                for (OficioRemision oficio : oficios) {

                    reintentarEnvioOficioRemision(oficio);
                }
            } else {
                log.info("No hay Oficios de Remision pendientes de volver a enviar al nodo CIR");
            }

            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");


        } catch (Exception | I18NException e) {
            log.info("Error al reintenar el envio de registros sin confirmacion");
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidad.getId(), "");
        }
    }

    @Override
    public void reintentarEnviosConError(Entidad entidad) throws Exception {

        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Reintentar Envios Sir con Error";
        Date inicio = new Date();

        try {

            peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

            // RegistrosSir enviados con errores
            List<Long> registrosSir = registroSirEjb.getEnviadosConError(entidad.getId());

            peticion.append("registrosSir: ").append(registrosSir.size()).append(System.getProperty("line.separator"));

            if (registrosSir.size() > 0) {

                log.info("Hay " + registrosSir.size() + " RegistrosSir enviados con errores, pendientes de volver a enviar al nodo CIR");

                // Volvemos a enviar los RegistrosSir
                for (Long registroSir : registrosSir) {

                    reintentarEnvioRegistroSir(registroSir);
                }
            } else {
                log.info("No hay RegistrosSir enviados con errores, pendientes de volver a enviar al nodo CIR");
            }

            // OficiosRemision enviados con errores
            List<OficioRemision> oficios = oficioRemisionEjb.getEnviadosConError(entidad.getId());

            peticion.append("oficios: ").append(oficios.size()).append(System.getProperty("line.separator"));

            if (oficios.size() > 0) {

                log.info("Hay " + oficios.size() + " Oficios de Remision enviados con errores, pendientes de volver a enviar al nodo CIR");

                // Volvemos a enviar los OficiosRemision
                for (OficioRemision oficio : oficios) {

                    reintentarEnvioOficioRemision(oficio);
                }
            } else {
                log.info("No hay Oficios de Remision enviados con errores, pendientes de volver a enviar al nodo CIR");
            }

            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");


        } catch (Exception | I18NException e) {
            log.info("Error al reintenar el envio de registros con error");
            e.printStackTrace();
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidad.getId(), "");
        }
    }

    /**
     * Envía un mensaje de Confirmación y lo guarda en bbdd
     *
     * @param registroSir
     * @param numeroRegistroFormateado
     * @throws Exception
     */
    private void enviarMensajeConfirmacion(RegistroSir registroSir, String numeroRegistroFormateado, Date fechaRegistro) throws Exception {

        // Enviamos el mensaje de confirmación
        MensajeControl confirmacion = mensajeEjb.enviarMensajeConfirmacion(registroSir, numeroRegistroFormateado, fechaRegistro);

        // Guardamos el mensaje de confirmación
        mensajeControlEjb.persist(confirmacion);
    }

    /**
     * @param idRegistroSir
     * @throws Exception
     */
    private void reintentarEnvioRegistroSir(Long idRegistroSir) throws Exception {

        RegistroSir registroSir = registroSirEjb.getRegistroSirConAnexos(idRegistroSir);

        log.info("Reintentando envio registroSir " + registroSir.getIdentificadorIntercambio() + " a " + registroSir.getDecodificacionEntidadRegistralDestino());

        emisionEjb.enviarFicheroIntercambio(registroSir);
        registroSir.setNumeroReintentos(registroSir.getNumeroReintentos() + 1);
        registroSir.setFechaEstado(new Date());

        // Modificamos su estado si estaba marcado con ERROR
        if (registroSir.getEstado().equals(EstadoRegistroSir.REENVIADO_Y_ERROR)) {
            registroSir.setEstado(EstadoRegistroSir.REENVIADO);
        } else if (registroSir.getEstado().equals(EstadoRegistroSir.RECHAZADO_Y_ERROR)) {
            registroSir.setEstado(EstadoRegistroSir.RECHAZADO);
        }

        registroSirEjb.merge(registroSir);

    }

    /**
     * @param oficio
     * @throws Exception
     * @throws I18NException
     */
    private void reintentarEnvioOficioRemision(OficioRemision oficio) throws Exception, I18NException {

        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        long tiempo = System.currentTimeMillis();
        String descripcion = "Reintentar envío a: " + oficio.getCodigoEntidadRegistralDestino();
        peticion.append("IdentificadorIntercambio: ").append(oficio.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
        peticion.append("Origen: ").append(oficio.getOficina().getDenominacion()).append(System.getProperty("line.separator"));
        peticion.append("Destino: ").append(oficio.getDecodificacionEntidadRegistralDestino()).append(System.getProperty("line.separator"));

        if (oficio.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA)) {

            try {
                log.info("Reintentando envio OficioRemisionSir entrada " + oficio.getIdentificadorIntercambio() + " a " + oficio.getDecodificacionEntidadRegistralDestino() + " (" + oficio.getCodigoEntidadRegistralDestino() + ")");

                // Transformamos el RegistroEntrada en un RegistroSir
                RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFull(oficio.getRegistrosEntrada().get(0).getId());
                RegistroSir registroSir = registroSirEjb.transformarRegistroEntrada(registroEntrada);

                // Enviamos el Registro al Componente CIR
                emisionEjb.enviarFicheroIntercambio(registroSir);

            }catch (I18NException | Exception e){
                e.printStackTrace();
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, oficio.getUsuarioResponsable().getEntidad().getId(), oficio.getIdentificadorIntercambio());
                throw e;
            }


        } else if (oficio.getTipoOficioRemision().equals(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA)) {

            try {
                log.info("Reintentando envio OficioRemisionSir salida " + oficio.getIdentificadorIntercambio() + " a " + oficio.getDecodificacionEntidadRegistralDestino() + " (" + oficio.getCodigoEntidadRegistralDestino() + ")");

                // Transformamos el RegistroSalida en un RegistroSir
                RegistroSalida registroSalida = registroSalidaEjb.getConAnexosFull(oficio.getRegistrosSalida().get(0).getId());
                RegistroSir registroSir = registroSirEjb.transformarRegistroSalida(registroSalida);

                // Enviamos el Registro al Componente CIR
                emisionEjb.enviarFicheroIntercambio(registroSir);

            }catch (I18NException | Exception e){
                e.printStackTrace();
                integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, oficio.getUsuarioResponsable().getEntidad().getId(), oficio.getIdentificadorIntercambio());
                throw e;
            }

        }

        // Contabilizamos los reintentos
        oficio.setNumeroReintentos(oficio.getNumeroReintentos() + 1);
        oficio.setFechaEstado(new Date());

        // Modificamos su estado si estaba marcado con ERROR
        if (oficio.getEstado() == RegwebConstantes.OFICIO_SIR_ENVIADO_ERROR) {
            oficio.setEstado(RegwebConstantes.OFICIO_SIR_ENVIADO);
        } else if (oficio.getEstado() == RegwebConstantes.OFICIO_SIR_REENVIADO_ERROR) {
            oficio.setEstado(RegwebConstantes.OFICIO_SIR_REENVIADO);
        }

        // Actualizamos el Oficio
        oficioRemisionEjb.merge(oficio);

        //Integración
        integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, oficio.getUsuarioResponsable().getEntidad().getId(), oficio.getIdentificadorIntercambio());

    }

    /**
     * Indica si el RegistroSir  se puede reenviar, en función de su estado
     *
     * @param estado del registroSir
     * @return
     */
    public boolean puedeReenviarRegistroSir(EstadoRegistroSir estado) {
        return estado.equals(EstadoRegistroSir.RECIBIDO) ||
                estado.equals(EstadoRegistroSir.REENVIADO) ||
                estado.equals(EstadoRegistroSir.REENVIADO_Y_ERROR);

    }

    /**
     * Genera el identificador de intercambio a partir del código de la oficina de origen
     *
     * @param codOficinaOrigen
     * @return el identificador intercambio (String)
     * @throws Exception
     */
    private String generarIdentificadorIntercambio(String codOficinaOrigen, Entidad entidad) throws Exception {

        SimpleDateFormat anyo = new SimpleDateFormat("yy"); // Just the year, with 2 digits
        String secuencia = contadorEjb.secuenciaSir(entidad.getLibro().getContadorSir().getId());

        return codOficinaOrigen + "_" + anyo.format(Calendar.getInstance().getTime()) + "_" + secuencia;

    }

    /**
     * Método que obtiene los contactos de la oficina Sir de destino
     *
     * @param oficinaSir
     * @return
     * @throws Exception
     */
    private String getContactosOficinaSir(OficinaTF oficinaSir) throws Exception {
        StringBuilder stb = new StringBuilder();
        for (ContactoTF contactoTF : oficinaSir.getContactos()) {
            String scontactoTF = "<b>" + contactoTF.getTipoContacto() + "</b>: " + contactoTF.getValorContacto();
            stb.append(scontactoTF);
            stb.append("<br>");
        }

        return stb.toString();

    }

}
