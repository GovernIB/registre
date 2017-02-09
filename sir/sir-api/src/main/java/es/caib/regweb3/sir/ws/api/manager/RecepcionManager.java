package es.caib.regweb3.sir.ws.api.manager;

import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;
import es.caib.regweb3.sir.core.excepcion.ServiceException;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.model.*;
import es.caib.regweb3.sir.ws.api.utils.FicheroIntercambio;
import es.caib.regweb3.sir.ws.api.utils.Mensaje;
import es.caib.regweb3.sir.ws.api.utils.XPathReaderUtil;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathConstants;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 *
 */
public class RecepcionManager {

    public final Logger log = Logger.getLogger(getClass());

    public SicresXMLManager sicresXMLManager = new SicresXMLManager();
    public MensajeManager mensajeManager = new MensajeManager();

    private String errorGenerico = Errores.ERROR_0065.getValue(); // ERROR GENÉRICO: ERROR_EN_EL_ASIENTO
    private final String Codigo_Entidad_Registral_Destino_Xpath = "//Codigo_Entidad_Registral_Destino/text()";
    private final String Codigo_Entidad_Registral_Origen_Xpath = "//Codigo_Entidad_Registral_Origen/text()";
    private final String Identificador_Intercambio_Xpath = "//Identificador_Intercambio/text()";


    public RecepcionManager() {
        super();
    }

    /**
     * Recibe un fichero de intercambio
     *
     * @param xmlFicheroIntercambio
     * @return PreRegistro creado
     */
    public void recibirFicheroIntercambio(String xmlFicheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb) {

        FicheroIntercambio ficheroIntercambio = null;
        String descripcionError = null;

        try {

            Assert.hasText(xmlFicheroIntercambio, "'xmlFicheroIntercambio' no puede estar vacio");

            // Convertimos y validamos el xml recibido en un FicheroIntercambio mediate xsd FicheroIntercambio.xsd
            ficheroIntercambio = sicresXMLManager.parseXMLFicheroIntercambio(xmlFicheroIntercambio);

            // Creamos el AsientoRegistralSir a partir del xml recibido y validado
            recibirFicheroIntercambio(ficheroIntercambio, xmlFicheroIntercambio, webServicesMethodsEjb);

            // Si ha ido bien, enviamos el ACK
            enviarACK(ficheroIntercambio);

        } catch (RuntimeException e) {

            // Si el error es de Validación, obtenemos su código de error
            if (e instanceof ValidacionException) {
                Errores errorValidacion = ((ValidacionException) e).getErrorValidacion();
                descripcionError = ((ValidacionException) e).getErrorException().getMessage();
                if (errorValidacion != null) {
                    errorGenerico = errorValidacion.getValue();
                }
                log.info("Error de Valicacion: " + ((ValidacionException) e).getErrorException().getMessage());
            }else{
                log.info("Error al recibir el fichero de intercambio", e);
                descripcionError = e.getMessage();
            }

            // Enviamos el mensaje de error
            try {
                // Intentamos parsear el xml los 3 campos necesarios para mensaje de error
                Mensaje mensaje = parserForError(xmlFicheroIntercambio);

                // Si el código de error es el de duplicado, se vuelve a enviar un ACK
                if (Errores.ERROR_0205.getValue().equals(errorGenerico)) {
                    enviarACK(mensaje);

                // Enviar mensaje de error, sino tiene nada que ver con los campos CodigoEntidad
                } else if (!Errores.ERROR_COD_ENTIDAD_INVALIDO.getValue().equals(errorGenerico)) {
                    enviarMensajeError(mensaje, errorGenerico, descripcionError, null);
                }

            } catch (RuntimeException ex) {
                // Comprobamos una posible excepción al no disponer de los datos necesarios para enviar los mensajes
                log.info("No se ha podido enviar el mensaje de error debido a una excepción producida, podría ser debido a la falta de campos requeridos para el envío del mismo:", ex);
            }
            throw e;
        }

        //return asientoRegistralSir;

    }

    /**
     * @param ficheroIntercambio
     * @param xmlFicheroIntercambio
     * @return
     */
    protected AsientoRegistralSir recibirFicheroIntercambio(FicheroIntercambio ficheroIntercambio, String xmlFicheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb) {

        AsientoRegistralSir asientoRegistralSir = null;

        // Validamos el Fichero de Intercambio creado a partir del xml recibido
        try {
            sicresXMLManager.validarFicheroIntercambio(ficheroIntercambio);
        } catch (IllegalArgumentException e) {
            log.error("Se produjo un error de validacion del xml recibido: " + e.getMessage());
            throw new ValidacionException(Errores.ERROR_0037, e);
        }

        // tipo anotacion envio
        if (TipoAnotacion.ENVIO.getValue().equals(ficheroIntercambio.getTipoAnotacion())) {

            try {
                asientoRegistralSir = webServicesMethodsEjb.getAsientoRegistral(ficheroIntercambio.getIdentificadorIntercambio(),ficheroIntercambio.getCodigoEntidadRegistralDestino());
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException(Errores.ERROR_INESPERADO,e);
            }

            if(asientoRegistralSir != null) { // Ya existe en el sistema

                if(EstadoAsientoRegistralSir.RECIBIDO.equals(asientoRegistralSir.getEstado())){

                    log.info("El AsientoRegistral" + asientoRegistralSir.getIdentificadorIntercambio() +" ya se ha recibido.");
                    throw new ValidacionException(Errores.ERROR_0205);

                }else if(EstadoAsientoRegistralSir.RECHAZADO.equals(asientoRegistralSir.getEstado()) ||
                            EstadoAsientoRegistralSir.RECHAZADO_Y_ACK.equals(asientoRegistralSir.getEstado()) ||
                            EstadoAsientoRegistralSir.RECHAZADO_Y_ERROR.equals(asientoRegistralSir.getEstado()) ||
                            EstadoAsientoRegistralSir.REENVIADO.equals(asientoRegistralSir.getEstado())){


                }


            }else{ // No existe en el sistema, lo creamos.

                try {
                    // Convertimos el Fichero de Intercambio SICRES3 en {@link es.caib.regweb3.model.AsientoRegistralSir}
                    asientoRegistralSir = ficheroIntercambio.getAsientoRegistralSir(webServicesMethodsEjb);

                    // Guardamos el nuevo AsientoRegistrarSir
                    asientoRegistralSir = webServicesMethodsEjb.crearAsientoRegistralSir(asientoRegistralSir);
                } catch (Exception e) {
                    log.info("Error al crear el AsientoRegistralSir", e);
                    throw new ServiceException(Errores.ERROR_INESPERADO,e);
                }
            }


        }


        // tipo anotacion reenvio
        if (TipoAnotacion.REENVIO.getValue().equals(ficheroIntercambio.getTipoAnotacion())) {

        }

        // tipo anotacion rechazo
        if (TipoAnotacion.RECHAZO.getValue().equals(ficheroIntercambio.getTipoAnotacion())) {

        }

        return asientoRegistralSir;
    }

    /**
     * Obtiene del xml de intercambios los campos necesarios para crear un mensaje de error
     * @param xml
     * @return
     */
    private Mensaje parserForError(String xml) {

        log.debug("Intentamos parsear el xml recibido para enviar el mensaje de Error");

        Mensaje mensaje = new Mensaje();

        XPathReaderUtil reader = null;
        // procesamos el xml para procesar las peticiones xpath
        try {
            reader = new XPathReaderUtil(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            log.error("Imposible parsear el xml recibido:"+xml+" excepción "+e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

        Node codigoEntidadRegistralDestino = (Node)reader.read(Codigo_Entidad_Registral_Destino_Xpath, XPathConstants.NODE);
        Node codigoEntidadRegistralOrigen = (Node)reader.read(Codigo_Entidad_Registral_Origen_Xpath, XPathConstants.NODE);
        Node identificadorIntercambio = (Node)reader.read(Identificador_Intercambio_Xpath, XPathConstants.NODE);

        //log.info("CodigoEntidadRegistralDestino: " + codigoEntidadRegistralDestino.getNodeValue());
        //log.info("CodigoEntidadRegistralOrigen: " + codigoEntidadRegistralOrigen.getNodeValue());
        //log.info("IdentificadorIntercambio: " + identificadorIntercambio.getNodeValue());

        mensaje.setCodigoEntidadRegistralDestino(codigoEntidadRegistralDestino.getNodeValue());
        mensaje.setCodigoEntidadRegistralOrigen(codigoEntidadRegistralOrigen.getNodeValue());
        mensaje.setIdentificadorIntercambio(identificadorIntercambio.getNodeValue());

        return mensaje;
    }

    /**
     * Envía un mensaje de control ACK.
     *
     * @param ficheroIntercambio Información del asiento registral.
     */
    protected void enviarACK(FicheroIntercambio ficheroIntercambio) {

        Mensaje mensaje = new Mensaje();
        mensaje.setCodigoEntidadRegistralOrigen(ficheroIntercambio.getCodigoEntidadRegistralDestino());
        mensaje.setCodigoEntidadRegistralDestino(ficheroIntercambio.getCodigoEntidadRegistralOrigen());
        mensaje.setIdentificadorIntercambio(ficheroIntercambio.getIdentificadorIntercambio());
        mensaje.setTipoMensaje(TipoMensaje.ACK);
        mensaje.setDescripcionMensaje(TipoMensaje.ACK.getName());


        mensajeManager.enviarMensaje(mensaje);

        log.info("Mensaje de control (ACK) enviado");
    }

    /**
     * Envía un mensaje de control ACK.
     *
     * @param mensaje Campos del mensaje obtenidos del FicheroIntercambio recibido
     */
    protected void enviarACK(Mensaje mensaje) {

        mensaje.setTipoMensaje(TipoMensaje.ACK);
        mensaje.setDescripcionMensaje(TipoMensaje.ACK.getName());

        mensajeManager.enviarMensaje(mensaje);

        log.info("Mensaje de control (ACK) enviado");
    }

    /**
     * Envía un mensaje de control de tipo ERROR.
     *
     * @param mensaje Campos del mensaje obtenidos del FicheroIntercambio recibido
     * @param codigoError
     * @param descError
     * @param identificadoresFicheros
     */
    protected void enviarMensajeError(Mensaje mensaje, String codigoError, String descError, List<String> identificadoresFicheros) {
        mensaje.setTipoMensaje(TipoMensaje.ERROR);
        mensaje.setCodigoError(codigoError);
        mensaje.setDescripcionMensaje(descError);
        mensaje.setIdentificadoresFicheros(identificadoresFicheros);

        mensajeManager.enviarMensaje(mensaje);

        log.info("Mensaje de control (ERROR) enviado");

    }
}
