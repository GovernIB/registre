package es.caib.regweb3.sir.ws.api.manager.impl;

import es.caib.regweb3.model.PreRegistro;
import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.model.Errores;
import es.caib.regweb3.sir.core.model.TipoAnotacion;
import es.caib.regweb3.sir.core.model.TipoMensaje;
import es.caib.regweb3.sir.core.schema.FicheroIntercambioSICRES3;
import es.caib.regweb3.sir.core.schema.ObjectFactory;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.sir.core.utils.Mensaje;
import es.caib.regweb3.sir.ws.api.manager.MensajeManager;
import es.caib.regweb3.sir.ws.api.manager.RecepcionManager;
import es.caib.regweb3.sir.ws.api.manager.SicresXMLManager;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.List;

/**
 *
 */
public class RecepcionManagerImpl implements RecepcionManager {

    public final Logger log = Logger.getLogger(getClass());

    public SicresXMLManager sicresXMLManager = new SicresXMLManagerImpl();
    public MensajeManager mensajeManager = new MensajeManagerImpl();


    public RecepcionManagerImpl() {
        super();
    }

    /**
     * Recibe un fichero de intercambio
     *
     * @param xmlFicheroIntercambio
     * @return PreRegistro creado
     */
    public PreRegistro recibirFicheroIntercambio(String xmlFicheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb) {

        FicheroIntercambio ficheroIntercambio = null;
        PreRegistro preRegistro = null;

        try {

            // Convertimos la información recibida del asiento registral sir
            ficheroIntercambio = sicresXMLManager.parseXMLFicheroIntercambio(xmlFicheroIntercambio);

            // Creamos el PreRegistro a partir del xml recibido
            preRegistro = recibirFicheroIntercambio(ficheroIntercambio, xmlFicheroIntercambio, webServicesMethodsEjb);

            enviarACK(ficheroIntercambio);

        } catch (RuntimeException e) {
            log.info("Error al recibir el fichero de intercambio", e);

            String codigoError = Errores.ERROR_0063.getValue();

            if (e instanceof ValidacionException) {
                Errores errorValidacion = ((ValidacionException) e).getErrorValidacion();
                if (errorValidacion != null) {
                    codigoError = errorValidacion.getValue();
                }
                log.info("Codigo de error: " + codigoError);
            }

            // Enviamos el mensaje de error
            try {
                // intentamos parsear el xml los campos necesarios para mensaje de error
                if (ficheroIntercambio == null) {
                    ficheroIntercambio = parserForError(xmlFicheroIntercambio);
                }
                //si el código de error es el de duplicado, se vuelve a enviar un ACK
                // Enviar ACK
                if (Errores.ERROR_0205.getValue().equals(codigoError)) {
                    enviarACK(ficheroIntercambio);
                } else {
                    // Enviar mensaje de error
                    enviarMensajeError(ficheroIntercambio, codigoError, e.getMessage(), null);
                }

            } catch (RuntimeException ex) {
                // puede darse el caso de que se genere una excepcion al no
                // poder tener todos lo datos necesarios para generar el mensaje de error
                log.error("No se ha podido enviar el mensaje de error debido a una excepción producida, podría ser debido a la falta de campos requeridos para el envío del mismo:", ex);
            }
            throw e;
        }

        return preRegistro;

    }

    /**
     * @param ficheroIntercambio
     * @param xmlFicheroIntercambio
     * @return
     */
    protected PreRegistro recibirFicheroIntercambio(FicheroIntercambio ficheroIntercambio, String xmlFicheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb) {

        PreRegistro preRegistro = null;

        try {
            sicresXMLManager.validarFicheroIntercambio(ficheroIntercambio);
        } catch (IllegalArgumentException e) {
            log.error("Se produjo un error de validacion del xml recibido: " + e.getMessage());
            throw new ValidacionException(Errores.ERROR_0037);
        }

        // tipo anotacion envio
        if (TipoAnotacion.ENVIO.equals(ficheroIntercambio.getTipoAnotacion())) {

            preRegistro = recibirFicheroIntercambioEnvio(ficheroIntercambio, webServicesMethodsEjb);
        }


        // tipo anotacion reenvio
        if (TipoAnotacion.REENVIO.equals(ficheroIntercambio.getTipoAnotacion())) {

            //preRegistro = recibirFicheroIntercambioReenvio(ficheroIntercambio,webServicesMethodsEjb);
        }

        // tipo anotacion rechazo
        if (TipoAnotacion.RECHAZO.equals(ficheroIntercambio.getTipoAnotacion())) {

            //preRegistro = recibirFicheroIntercambioRechazo(ficheroIntercambio,webServicesMethodsEjb);
        }

        return preRegistro;
    }

    /**
     * @param ficheroIntercambio
     * @return
     */
    protected PreRegistro recibirFicheroIntercambioEnvio(FicheroIntercambio ficheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb) {

        PreRegistro preRegistro = null;


        try {
            synchronized (this) {
                preRegistro = webServicesMethodsEjb.crearPreRegistro(ficheroIntercambio.getPreRegistro());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return preRegistro;
    }

    /**
     * Método que parsea el xml sin validaciones de schema para poder generar mensajes de error en algun momento
     *
     * @param xml
     * @return
     */
    private FicheroIntercambio parserForError(String xml) {

        FicheroIntercambio ficheroIntercambio = new FicheroIntercambio();
        FicheroIntercambioSICRES3 fiSICRES3 = null;

        try {

            JAXBContext jc = JAXBContext.newInstance(ObjectFactory.class);
            Unmarshaller u = jc.createUnmarshaller();

            StringReader reader = new StringReader(xml);
            u.setSchema(null);
            fiSICRES3 = (FicheroIntercambioSICRES3) u.unmarshal(reader);

        } catch (JAXBException e) {
            log.info("Imposible parsear el xml recibido: " + xml + " excepción " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }

        ficheroIntercambio.setFicheroIntercambio(fiSICRES3);
        return ficheroIntercambio;
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
     * Envía un mensaje de control de tipo ERROR.
     *
     * @param ficheroIntercambio
     * @param codigoError
     * @param descError
     * @param identificadoresFicheros
     */
    protected void enviarMensajeError(FicheroIntercambio ficheroIntercambio, String codigoError, String descError, List<String> identificadoresFicheros) {

        if (ficheroIntercambio != null) {
            Mensaje mensaje = new Mensaje();
            mensaje.setCodigoEntidadRegistralOrigen(ficheroIntercambio.getCodigoEntidadRegistralDestino());
            mensaje.setCodigoEntidadRegistralDestino(ficheroIntercambio.getCodigoEntidadRegistralOrigen());
            mensaje.setIdentificadorIntercambio(ficheroIntercambio.getIdentificadorIntercambio());
            mensaje.setTipoMensaje(TipoMensaje.ERROR);
            mensaje.setCodigoError(codigoError);
            mensaje.setDescripcionMensaje(descError);
            mensaje.setIdentificadoresFicheros(identificadoresFicheros);

            mensajeManager.enviarMensaje(mensaje);

            log.info("Mensaje de control (ERROR) enviado");
        }
    }
}
