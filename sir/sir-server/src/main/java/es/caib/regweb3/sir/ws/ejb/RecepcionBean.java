package es.caib.regweb3.sir.ws.ejb;

import es.caib.regweb3.model.AnexoSir;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.RegistroSir;
import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;
import es.caib.regweb3.persistence.utils.FileSystemManager;
import es.caib.regweb3.sir.core.excepcion.ServiceException;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.model.Errores;
import es.caib.regweb3.sir.core.model.TipoAnotacion;
import es.caib.regweb3.sir.core.model.TipoMensaje;
import es.caib.regweb3.sir.core.utils.Assert;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.sir.core.utils.Mensaje;
import es.caib.regweb3.sir.ejb.MensajeLocal;
import es.caib.regweb3.sir.utils.Sicres3XML;
import es.caib.regweb3.sir.utils.XPathReaderUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.xpath.XPathConstants;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

/**
 * Ejb para la gestión de la Recepción de Ficheros de Intercambios y Mensajes de Dtos de Control
 * en formato SICRES3 desde un nodo distribuido
 */
@Stateless(name = "RecepcionEJB")
public class RecepcionBean implements RecepcionLocal{

    private final Logger log = Logger.getLogger(getClass());

    @EJB private MensajeLocal mensajeEjb;

    private Sicres3XML sicres3XML = new Sicres3XML();


    private String errorGenerico = Errores.ERROR_0065.getValue(); // ERROR GENÉRICO: ERROR_EN_EL_ASIENTO
    private final String Codigo_Entidad_Registral_Destino_Xpath = "//Codigo_Entidad_Registral_Destino/text()";
    private final String Codigo_Entidad_Registral_Origen_Xpath = "//Codigo_Entidad_Registral_Origen/text()";
    private final String Identificador_Intercambio_Xpath = "//Identificador_Intercambio/text()";


    /**
     * Recibe un fichero de intercambio en formato SICRES3 desde un nodo distribuido
     *
     * @param xmlFicheroIntercambio
     *
     */
    public void recibirFicheroIntercambio(String xmlFicheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb) throws Exception {

        RegistroSir registroSir = null;
        Entidad entidad = null;
        FicheroIntercambio ficheroIntercambio = null;
        Mensaje mensajeError = null;
        StringBuilder peticion = new StringBuilder();
        String descripcion = "Recepción FicheroIntercambio: ";
        long tiempo = System.currentTimeMillis();

        try {

            Assert.hasText(xmlFicheroIntercambio, "El xml del FicheroIntercambio no puede estar vacio");

            // Convertimos y validamos el xml recibido en un FicheroIntercambio mediate xsd FicheroIntercambio.xsd
            ficheroIntercambio = sicres3XML.parseXMLFicheroIntercambio(xmlFicheroIntercambio);

            // Validamos el Fichero de Intercambio creado a partir del xml recibido
            try {
                // Comprobamos que la Entidad a la que va dirigida el Asiento existe y está activa
                entidad = obtenerEntidad(ficheroIntercambio.getCodigoEntidadRegistralDestino(), webServicesMethodsEjb);
                Assert.notNull(entidad,"No existe ninguna Entidad a la que corresponda este envío");
                Assert.isTrue(entidad.getActivo(), "La Entidad a la que va dirigida el Asiento Registral no está activa");
                Assert.isTrue(entidad.getSir(), "La Entidad a la que va dirigida el Asiento Registral no tiene el servicio SIR activo");

                sicres3XML.validarFicheroIntercambio(ficheroIntercambio, webServicesMethodsEjb.getObtenerOficinasService(), webServicesMethodsEjb.getObtenerUnidadesService(), webServicesMethodsEjb.getFormatosAnexosSir());
            } catch (IllegalArgumentException e) {
                log.info("Se produjo un error de validacion del xml recibido: " + e.getMessage());
                throw new ValidacionException(Errores.ERROR_0037, e.getMessage(), e);
            }

            // Creamos el RegistroSir a partir del xml recibido y validado
            registroSir = webServicesMethodsEjb.recibirFicheroIntercambio(ficheroIntercambio);

            if (registroSir != null){ // Enviamos el ack si se ha creado un RegistroSir
                mensajeEjb.enviarACK(ficheroIntercambio);
            }

        }catch (ValidacionException e) {
            log.info("Error de validacion: " + e.getMensajeError());
            Errores errorValidacion = e.getErrorValidacion();
            String descripcionError = e.getMensajeError();

            if(ficheroIntercambio != null && (!descripcionError.contains("CodigoEntidadRegistralOrigen") && !descripcionError.contains("CodigoEntidadRegistralDestino") && !descripcionError.contains("IdentificadorIntercambio"))){
                mensajeError = crearMensajeError(ficheroIntercambio, errorValidacion.getValue(), descripcionError);
                enviarMensajeError(mensajeError);

                // Integración
                if(entidad != null){
                    descripcion = descripcion.concat(TipoAnotacion.getTipoAnotacion(ficheroIntercambio.getTipoAnotacion()).getName());
                    peticion.append("TipoAnotación: ").append(TipoAnotacion.getTipoAnotacion(ficheroIntercambio.getTipoAnotacion()).getName()).append(System.getProperty("line.separator"));
                    peticion.append("IdentificadorIntercambio: ").append(ficheroIntercambio.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
                    peticion.append("Origen: ").append(ficheroIntercambio.getDecodificacionEntidadRegistralOrigen()).append(" (").append(ficheroIntercambio.getCodigoEntidadRegistralOrigen()).append(")").append(System.getProperty("line.separator"));
                    peticion.append("Destino: ").append(ficheroIntercambio.getDescripcionEntidadRegistralDestino()).append(" (").append(ficheroIntercambio.getCodigoEntidadRegistralDestino()).append(")").append(System.getProperty("line.separator"));

                    webServicesMethodsEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e,descripcionError, System.currentTimeMillis() - tiempo, entidad.getId(), ficheroIntercambio.getIdentificadorIntercambio());
                }

            }else if(ficheroIntercambio == null && !Errores.ERROR_COD_ENTIDAD_INVALIDO.getValue().equals(errorValidacion.getValue())){
                mensajeError = parserForError(xmlFicheroIntercambio, errorValidacion.getValue(), descripcionError);
                enviarMensajeError(mensajeError);
            }else{
                log.info("El error de validacion afecta a campos necesarios para componer el mensaje de error, no se podra enviar");
            }

            throw e;

        } catch (RuntimeException e) {

            log.info("-------- Error inesperado (RuntimeException) recibiendo el Fichero de Intercambio, enviamos un mensaje de control de error --------", e);
            log.info("");

            if(ficheroIntercambio != null){
                mensajeError = crearMensajeError(ficheroIntercambio, errorGenerico, e.getMessage());
                enviarMensajeError(mensajeError);
            }

            // Eliminamos los posibles archivos creados en filesystem del RegistroSir
            if(registroSir != null){
                log.info("RuntimeException, eliminamos los archivosen filesystem de los anexosSir creados: ");
                for(AnexoSir anexoSir: registroSir.getAnexos()){
                    FileSystemManager.eliminarArchivo(anexoSir.getAnexo().getId());
                    log.info("Eliminamos archivo: " + anexoSir.getAnexo().getId());
                }
            }

            // Integración
            if(entidad != null){
                descripcion = descripcion.concat(TipoAnotacion.getTipoAnotacion(ficheroIntercambio.getTipoAnotacion()).getName());
                peticion.append("TipoAnotación: ").append(TipoAnotacion.getTipoAnotacion(ficheroIntercambio.getTipoAnotacion()).getName()).append(System.getProperty("line.separator"));
                peticion.append("IdentificadorIntercambio: ").append(ficheroIntercambio.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
                peticion.append("Origen: ").append(ficheroIntercambio.getDecodificacionEntidadRegistralOrigen()).append(" (").append(ficheroIntercambio.getCodigoEntidadRegistralOrigen()).append(")").append(System.getProperty("line.separator"));
                peticion.append("Destino: ").append(ficheroIntercambio.getDescripcionEntidadRegistralDestino()).append(" (").append(ficheroIntercambio.getCodigoEntidadRegistralDestino()).append(")").append(System.getProperty("line.separator"));

                webServicesMethodsEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidad.getId(), ficheroIntercambio.getIdentificadorIntercambio());
            }
            log.info("");
            log.info("------------------------------------------------------------------------------------------------------------");

            throw e;
        }
    }

    /**
     * Recibe un fichero de datos de control del nodo distribuido asociado.
     *
     * @param xmlMensaje XML con la información del mensaje en formato SICRES 3.0.
     */
    public void recibirMensajeDatosControl(String xmlMensaje, WebServicesMethodsLocal webServicesMethodsEjb){

        // Parseamos el mensaje xml
        Mensaje mensaje = sicres3XML.parseXMLMensaje(xmlMensaje);

        // Validamos el mensaje recibido
        sicres3XML.validarMensaje(mensaje);

        log.info("Recibiendo mensaje de control: " + mensaje.getTipoMensaje() + " - " + mensaje.getIdentificadorIntercambio());

        try {

            webServicesMethodsEjb.recibirMensajeDatosControl(mensaje);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(Errores.ERROR_NO_CONTROLADO,e);
        }

        log.info("Mensaje recibido y procesado correctamente: " + mensaje.getIdentificadorIntercambio());
    }


    /**
     *
     * @param mensaje
     */
    private void enviarMensajeError(Mensaje mensaje){

        try {

            mensajeEjb.enviarMensajeError(mensaje);

        }catch (RuntimeException ex) {
            // Comprobamos una posible excepción al no disponer de los datos necesarios para enviar los mensajes
            log.info("No es posible enviar el mensaje de Error, posiblemente por falta de campos minimos requeridos", ex);
        }

    }

    /**
     * Obtiene del xml de intercambio los campos necesarios para crear un mensaje de error
     * @param ficheroIntercambio
     * @return
     */
    private Mensaje crearMensajeError(FicheroIntercambio ficheroIntercambio, String codigoError, String descripcionError) {

        Mensaje mensaje = new Mensaje();

        mensaje.setCodigoEntidadRegistralOrigen(ficheroIntercambio.getCodigoEntidadRegistralDestino());
        mensaje.setCodigoEntidadRegistralDestino(ficheroIntercambio.getCodigoEntidadRegistralOrigen());
        mensaje.setIdentificadorIntercambio(ficheroIntercambio.getIdentificadorIntercambio());
        mensaje.setTipoMensaje(TipoMensaje.ERROR);
        mensaje.setCodigoError(codigoError);
        mensaje.setDescripcionMensaje(descripcionError);

        return mensaje;

    }

    /**
     * Obtiene del xml de intercambio los campos necesarios para crear un mensaje de error
     * @param xml
     * @return
     */
    private Mensaje parserForError(String xml, String codigoError, String descripcionError) {

        log.info("Intentamos parsear el xml recibido para enviar el mensaje de Error");

        Mensaje mensaje = new Mensaje();

        XPathReaderUtil reader = null;
        // procesamos el xml para procesar las peticiones xpath
        try {
            reader = new XPathReaderUtil(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            log.info("Imposible parsear el xml recibido:"+xml+" excepción "+e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

        try{

            Node codigoEntidadRegistralDestino = (Node)reader.read(Codigo_Entidad_Registral_Destino_Xpath, XPathConstants.NODE);
            Node codigoEntidadRegistralOrigen = (Node)reader.read(Codigo_Entidad_Registral_Origen_Xpath, XPathConstants.NODE);
            Node identificadorIntercambio = (Node)reader.read(Identificador_Intercambio_Xpath, XPathConstants.NODE);

            mensaje.setCodigoEntidadRegistralOrigen(codigoEntidadRegistralDestino.getNodeValue());
            mensaje.setCodigoEntidadRegistralDestino(codigoEntidadRegistralOrigen.getNodeValue());
            mensaje.setIdentificadorIntercambio(identificadorIntercambio.getNodeValue());
            mensaje.setTipoMensaje(TipoMensaje.ERROR);
            mensaje.setCodigoError(codigoError);
            mensaje.setDescripcionMensaje(descripcionError);

        }catch (RuntimeException e){
            log.info("Imposible parsear el fichero de intercambio para obtener los campos minimos para componer el mensaje de error");
            throw new ValidacionException(Errores.ERROR_0037, "Imposible parsear el fichero de intercambio para obtener los campos minimos para componer el mensaje de error", e);
        }

        return mensaje;
    }

    /**
     * Obtiene la Entidad de REBWEB3 a partir de la Oficina destino
     * @param codigoEntidadRegistralDestino
     * @throws Exception
     */
    private Entidad obtenerEntidad(String codigoEntidadRegistralDestino, WebServicesMethodsLocal webServicesMethodsEjb) throws Exception{

        if(codigoEntidadRegistralDestino != null){

            Oficina oficina = webServicesMethodsEjb.obtenerOficina(codigoEntidadRegistralDestino);

            if(oficina != null){
                return oficina.getOrganismoResponsable().getEntidad();
            }
        }

        return null;

    }

}
