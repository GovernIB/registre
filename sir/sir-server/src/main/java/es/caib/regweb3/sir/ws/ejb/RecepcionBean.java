package es.caib.regweb3.sir.ws.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.sir.Errores;
import es.caib.regweb3.model.sir.MensajeControl;
import es.caib.regweb3.model.sir.TipoAnotacion;
import es.caib.regweb3.model.sir.TipoMensaje;
import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;
import es.caib.regweb3.persistence.utils.RespuestaRecepcionSir;
import es.caib.regweb3.sir.core.excepcion.ServiceException;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.utils.Assert;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.sir.ejb.MensajeLocal;
import es.caib.regweb3.sir.utils.Sicres3XML;
import es.caib.regweb3.sir.utils.XPathReaderUtil;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.xml.xpath.XPathConstants;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * Ejb para la gestión de la Recepción de Ficheros de Intercambios y Mensajes de Dtos de Control
 * en formato SICRES3 desde un nodo distribuido
 */
@Stateless(name = "RecepcionEJB")
public class RecepcionBean implements RecepcionLocal{

    private final Logger log = LoggerFactory.getLogger(getClass());

    @EJB private MensajeLocal mensajeEjb;

    private Sicres3XML sicres3XML = new Sicres3XML();

    private final String errorGenerico = Errores.ERROR_0065.getValue(); // ERROR GENÉRICO: ERROR_EN_EL_ASIENTO
    private final String Codigo_Entidad_Registral_Destino_Xpath = "//Codigo_Entidad_Registral_Destino/text()";
    private final String Codigo_Entidad_Registral_Origen_Xpath = "//Codigo_Entidad_Registral_Origen/text()";
    private final String Identificador_Intercambio_Xpath = "//Identificador_Intercambio/text()";

    private WebServicesMethodsLocal webServicesMethodsEjb;


    /**
     * Recibe un fichero de intercambio en formato SICRES3 desde un nodo distribuido
     *
     * @param xmlFicheroIntercambio
     *
     */
    public void recibirFicheroIntercambio(String xmlFicheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb) throws Exception {

        this.webServicesMethodsEjb = webServicesMethodsEjb;

        RespuestaRecepcionSir respuesta = null;
        Entidad entidad = null;
        FicheroIntercambio ficheroIntercambio = null;
        MensajeControl mensajeError = null;
        Date inicio = new Date();
        StringBuilder peticion = new StringBuilder();
        String descripcion = "Recepción Intercambio: ";
        

        try {

            Assert.hasText(xmlFicheroIntercambio, "El xml del FicheroIntercambio no puede estar vacio");

            // Convertimos y validamos el xml recibido en un FicheroIntercambio mediate xsd FicheroIntercambio.xsd
            ficheroIntercambio = sicres3XML.parseXMLFicheroIntercambio(xmlFicheroIntercambio);

            // Validamos el Fichero de Intercambio creado a partir del xml recibido
            try {
                // Comprobamos que la Entidad a la que va dirigida el Asiento existe y está activa
                entidad = obtenerEntidad(ficheroIntercambio.getCodigoEntidadRegistralDestino());
                Assert.notNull(entidad,"No existe ninguna Entidad a la que corresponda este envío");
                Assert.isTrue(entidad.getActivo(), "La Entidad a la que va dirigida el Asiento Registral no está activa");
                Assert.isTrue(entidad.getSir(), "La Entidad a la que va dirigida el Asiento Registral no tiene el servicio SIR activo");

                sicres3XML.validarFicheroIntercambio(ficheroIntercambio, webServicesMethodsEjb.getObtenerOficinasService(), webServicesMethodsEjb.getObtenerUnidadesService());
            } catch (IllegalArgumentException e) {
                log.info("ERROR DE VALIDACION DEL XML RECIBIDO: " + e.getLocalizedMessage());
                throw new ValidacionException(Errores.ERROR_0037, e.getMessage(), e);
            }

            // Procesamos el xml de intercambio recibido y validado
            respuesta = webServicesMethodsEjb.procesarFicheroIntercambio(ficheroIntercambio);

            // Enviamos el ack
            if(respuesta.getAck()){
                MensajeControl mensajeACK = mensajeEjb.enviarACK(ficheroIntercambio);
                mensajeACK.setEntidad(entidad);
                webServicesMethodsEjb.guardarMensajeControl(mensajeACK);
            }

            // Integración
            descripcion = descripcion.concat(TipoAnotacion.getTipoAnotacion(ficheroIntercambio.getTipoAnotacion()).getName());
            datosIntegracion(ficheroIntercambio, peticion);
            webServicesMethodsEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), System.currentTimeMillis() - inicio.getTime(), entidad.getId(), ficheroIntercambio.getIdentificadorIntercambio());

        }catch (ValidacionException e) {
            Errores errorValidacion = e.getErrorValidacion();
            String descripcionError = e.getMensajeError();

            // Error de validación que permite crear un Mensaje de Error
            if(ficheroIntercambio != null && (!descripcionError.contains("CodigoEntidadRegistralOrigen") && !descripcionError.contains("CodigoEntidadRegistralDestino") && !descripcionError.contains("IdentificadorIntercambio"))){
                mensajeError = crearMensajeError(ficheroIntercambio, errorValidacion.getValue(), descripcionError, entidad);
                enviarMensajeError(mensajeError);

                // Integración
                if(entidad != null){
                    descripcion = descripcion.concat("Error validación");
                    datosIntegracion(ficheroIntercambio, peticion);
                    webServicesMethodsEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e,descripcionError, System.currentTimeMillis() - inicio.getTime(), entidad.getId(), ficheroIntercambio.getIdentificadorIntercambio());
                }

            // Error de validación en algún campo que complica la creación de un Mensaje de Error
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
                mensajeError = crearMensajeError(ficheroIntercambio, errorGenerico, e.getMessage(), entidad);
                enviarMensajeError(mensajeError);
            }

            // Eliminamos el RegistroSir al completo
            /** Lo comentamos a raiz de los ERTES y los continuos fallos de comunicación con componente CIR*/
            /*if(respuesta.getRegistroSir() != null){
                webServicesMethodsEjb.eliminarRegistroSir(respuesta.getRegistroSir());
                peticion.append("REGISTRO_SIR ELIMINADO: ").append("Se elimina el RegistroSir completo por errores previos").append(" (").append(ficheroIntercambio.getCodigoEntidadRegistralOrigen()).append(")").append(System.getProperty("line.separator"));
                *//*log.info("RuntimeException, eliminamos los archivos en filesystem de los anexosSir creados: ");
                for(AnexoSir anexoSir: respuesta.getRegistroSir().getAnexos()){
                    FileSystemManager.eliminarArchivo(anexoSir.getAnexo().getId());
                    log.info("Eliminamos archivo: " + anexoSir.getAnexo().getId());
                }*//*
            }*/

            // Integración
            if(entidad != null){
                descripcion = descripcion.concat("Error no gestionado");
                datosIntegracion(ficheroIntercambio, peticion);
                webServicesMethodsEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SIR, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - inicio.getTime(), entidad.getId(), ficheroIntercambio.getIdentificadorIntercambio());
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

        this.webServicesMethodsEjb = webServicesMethodsEjb;

        // Parseamos el mensaje xml
        MensajeControl mensaje = sicres3XML.parseXMLMensaje(xmlMensaje);

        // Validamos el mensaje recibido
        sicres3XML.validarMensaje(mensaje);

        log.info("Recibiendo mensaje de control: " + mensaje.getTipoMensaje() + " - " + mensaje.getIdentificadorIntercambio());

        try {

            this.webServicesMethodsEjb.procesarMensajeDatosControl(mensaje);

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
    private void enviarMensajeError(MensajeControl mensaje){

        try {

            mensajeEjb.enviarMensajeError(mensaje);
            webServicesMethodsEjb.guardarMensajeControl(mensaje);

        }catch (RuntimeException ex) {
            // Comprobamos una posible excepción al no disponer de los datos necesarios para enviar los mensajes
            log.info("No es posible enviar el mensaje de Error, posiblemente por falta de campos minimos requeridos", ex);
        } catch (Exception e) {
            log.info("No es posible guardar el mensaje de Error, posiblemente por falta de campos minimos requeridos", e);
            e.printStackTrace();
        }
    }

    /**
     * Obtiene del xml de intercambio los campos necesarios para crear un mensaje de error
     * @param ficheroIntercambio
     * @return
     */
    private MensajeControl crearMensajeError(FicheroIntercambio ficheroIntercambio, String codigoError, String descripcionError, Entidad entidad) {

        MensajeControl mensaje = new MensajeControl(RegwebConstantes.TIPO_COMUNICACION_ENVIADO);

        mensaje.setCodigoEntidadRegistralOrigen(ficheroIntercambio.getCodigoEntidadRegistralDestino());
        mensaje.setCodigoEntidadRegistralDestino(ficheroIntercambio.getCodigoEntidadRegistralOrigen());
        mensaje.setIdentificadorIntercambio(ficheroIntercambio.getIdentificadorIntercambio());
        mensaje.setTipoMensaje(TipoMensaje.ERROR.getValue());
        mensaje.setCodigoError(codigoError);
        mensaje.setDescripcionMensaje(descripcionError);
        mensaje.setEntidad(entidad);

        return mensaje;
    }

    /**
     * Obtiene del xml de intercambio los campos necesarios para crear un mensaje de error
     * @param xml
     * @return
     */
    private MensajeControl parserForError(String xml, String codigoError, String descripcionError) {

        log.info("Intentamos parsear el xml recibido para enviar el mensaje de Error");

        MensajeControl mensaje = new MensajeControl(RegwebConstantes.TIPO_COMUNICACION_ENVIADO);

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
            mensaje.setTipoMensaje(TipoMensaje.ERROR.getValue());
            mensaje.setCodigoError(codigoError);
            mensaje.setDescripcionMensaje(descripcionError);

            mensaje.setEntidad(obtenerEntidad(codigoEntidadRegistralDestino.getNodeValue()));

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
    private Entidad obtenerEntidad(String codigoEntidadRegistralDestino){

        if(codigoEntidadRegistralDestino != null){

            Oficina oficina = null;
            try {
                oficina = webServicesMethodsEjb.obtenerOficina(codigoEntidadRegistralDestino);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            if(oficina != null){
                return oficina.getOrganismoResponsable().getEntidad();
            }
        }

        return null;
    }

    /**
     *
     * @param ficheroIntercambio
     * @param peticion
     */
    private void datosIntegracion(FicheroIntercambio ficheroIntercambio, StringBuilder peticion){
        peticion.append("TipoAnotación: ").append(TipoAnotacion.getTipoAnotacion(ficheroIntercambio.getTipoAnotacion()).getName()).append(System.getProperty("line.separator"));
        peticion.append("IdentificadorIntercambio: ").append(ficheroIntercambio.getIdentificadorIntercambio()).append(System.getProperty("line.separator"));
        peticion.append("Origen: ").append(ficheroIntercambio.getDecodificacionEntidadRegistralOrigen()).append(" (").append(ficheroIntercambio.getCodigoEntidadRegistralOrigen()).append(")").append(System.getProperty("line.separator"));
        peticion.append("Destino: ").append(ficheroIntercambio.getDescripcionEntidadRegistralDestino()).append(" (").append(ficheroIntercambio.getCodigoEntidadRegistralDestino()).append(")").append(System.getProperty("line.separator"));

        if(StringUtils.isNotEmpty(ficheroIntercambio.getDescripcionTipoAnotacion())){
            peticion.append("Motivo: ").append(ficheroIntercambio.getDescripcionTipoAnotacion()).append(System.getProperty("line.separator"));
        }
    }

}
