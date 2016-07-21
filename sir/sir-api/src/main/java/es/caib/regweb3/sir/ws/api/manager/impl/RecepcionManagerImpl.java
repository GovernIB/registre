package es.caib.regweb3.sir.ws.api.manager.impl;

import es.caib.regweb3.persistence.ejb.WebServicesMethodsLocal;
import es.caib.regweb3.sir.api.schema.Fichero_Intercambio_SICRES_3;
import es.caib.regweb3.sir.core.excepcion.ServiceException;
import es.caib.regweb3.sir.core.excepcion.ValidacionException;
import es.caib.regweb3.sir.core.model.AsientoRegistralSir;
import es.caib.regweb3.sir.core.model.Errores;
import es.caib.regweb3.sir.core.model.TipoAnotacion;
import es.caib.regweb3.sir.core.model.TipoMensaje;
import es.caib.regweb3.sir.ws.api.manager.MensajeManager;
import es.caib.regweb3.sir.ws.api.manager.RecepcionManager;
import es.caib.regweb3.sir.ws.api.manager.SicresXMLManager;
import es.caib.regweb3.sir.ws.api.utils.FicheroIntercambio;
import es.caib.regweb3.sir.ws.api.utils.Mensaje;
import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.springframework.util.Assert;

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
    public void recibirFicheroIntercambio(String xmlFicheroIntercambio, WebServicesMethodsLocal webServicesMethodsEjb) {

        FicheroIntercambio ficheroIntercambio = null;
        AsientoRegistralSir asientoRegistralSir = null;

        try {

            Assert.hasText(xmlFicheroIntercambio, "'xmlFicheroIntercambio' no puede estar vacio");

            // Convertimos la información recibida del asiento registral sir
            ficheroIntercambio = sicresXMLManager.parseXMLFicheroIntercambio(xmlFicheroIntercambio);

            // Creamos el AsientoRegistralSir a partir del xml recibido
            asientoRegistralSir = recibirFicheroIntercambio(ficheroIntercambio, xmlFicheroIntercambio, webServicesMethodsEjb);

            enviarACK(ficheroIntercambio);

        } catch (RuntimeException e) {

            String codigoError = Errores.ERROR_0063.getValue();

            if (e instanceof ValidacionException) {
                Errores errorValidacion = ((ValidacionException) e).getErrorValidacion();
                if (errorValidacion != null) {
                    codigoError = errorValidacion.getValue();
                }
                log.info("Error en la Valicacion: " + codigoError);
            }else{
                log.info("Error al recibir el fichero de intercambio", e);
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
            throw new ValidacionException(Errores.ERROR_0037);
        }

        // tipo anotacion envio
        if (TipoAnotacion.ENVIO.getValue().equals(ficheroIntercambio.getTipoAnotacion())) {

            try {
                asientoRegistralSir = webServicesMethodsEjb.crearAsientoRegistralSir(ficheroIntercambio.getAsientoRegistralSir(webServicesMethodsEjb));
            } catch (Exception e) {
                log.info("Error al crear el AsientoRegistralSir", e);
                throw new ServiceException(Errores.ERROR_INESPERADO,e);
            }
        }


        // tipo anotacion reenvio
        if (TipoAnotacion.REENVIO.getValue().equals(ficheroIntercambio.getTipoAnotacion())) {

            //preRegistro = recibirFicheroIntercambioReenvio(ficheroIntercambio,webServicesMethodsEjb);
        }

        // tipo anotacion rechazo
        if (TipoAnotacion.RECHAZO.getValue().equals(ficheroIntercambio.getTipoAnotacion())) {

            //preRegistro = recibirFicheroIntercambioRechazo(ficheroIntercambio,webServicesMethodsEjb);
        }

        return asientoRegistralSir;
    }

    /**
     * Método que parsea el xml sin validaciones de schema para poder generar mensajes de error en algun momento
     *
     * @param xml
     * @return
     */
    private FicheroIntercambio parserForError(String xml) {

        FicheroIntercambio ficheroIntercambio = new FicheroIntercambio();
        Fichero_Intercambio_SICRES_3 fiSICRES3 = null;

        Unmarshaller unmarshaller = new Unmarshaller(Fichero_Intercambio_SICRES_3.class);
        //desactivamos la validacion
        unmarshaller.setValidation(false);

        try {

            fiSICRES3 = (Fichero_Intercambio_SICRES_3) unmarshaller.unmarshal(new StringReader(xml));

        } catch (MarshalException e) {
            log.error("Imposible parsear el xml recibido:"+xml+" excepción "+e.getLocalizedMessage());
            throw new RuntimeException(e);
        } catch (ValidationException e) {
            log.error("Imposible parsear el xml recibido:"+xml+" excepción "+e.getLocalizedMessage());
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
