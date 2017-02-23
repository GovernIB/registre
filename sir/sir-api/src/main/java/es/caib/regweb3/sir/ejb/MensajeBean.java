package es.caib.regweb3.sir.ejb;

import es.caib.regweb3.sir.core.excepcion.SIRException;
import es.caib.regweb3.sir.core.model.AsientoRegistralSir;
import es.caib.regweb3.sir.core.model.TipoMensaje;
import es.caib.regweb3.sir.utils.FicheroIntercambio;
import es.caib.regweb3.sir.utils.Mensaje;
import es.caib.regweb3.sir.utils.Sicres3XML;
import es.caib.regweb3.sir.ws.api.wssir7.RespuestaWS;
import es.caib.regweb3.sir.ws.api.wssir7.WS_SIR7ServiceLocator;
import es.caib.regweb3.sir.ws.api.wssir7.WS_SIR7_PortType;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.xml.rpc.ServiceException;
import java.util.Date;

/**
 * Ejb para la gestión de la emisión de Mensajes de Datos de Control SICRES3 a un nodo distribuido
 */
@Stateless(name = "MensajeEJB")
public class MensajeBean implements MensajeLocal {

    public final Logger log = Logger.getLogger(getClass());

    public Sicres3XML sicres3XML = new Sicres3XML();


    /**
     * Envía un mensaje de control de confirmación.
     *
     * @param asientoRegistralSir Información del asiento registral.
     */
    public void enviarMensajeConfirmacion(AsientoRegistralSir asientoRegistralSir, String numeroRegistro) {

        Mensaje confirmacion = new Mensaje();
        confirmacion.setCodigoEntidadRegistralOrigen(asientoRegistralSir.getCodigoEntidadRegistralDestino());
        confirmacion.setCodigoEntidadRegistralDestino(asientoRegistralSir.getCodigoEntidadRegistralInicio());
        confirmacion.setIdentificadorIntercambio(asientoRegistralSir.getIdentificadorIntercambio());
        confirmacion.setTipoMensaje(TipoMensaje.CONFIRMACION);
        confirmacion.setDescripcionMensaje(TipoMensaje.CONFIRMACION.getName());
        confirmacion.setNumeroRegistroEntradaDestino(numeroRegistro);
        confirmacion.setFechaEntradaDestino(new Date());

        enviarMensaje(confirmacion);

        log.info("Mensaje de confirmación  enviado");
    }

    /**
     * Envía un mensaje de control ACK.
     *
     * @param ficheroIntercambio Información del asiento registral.
     */
    public void enviarACK(FicheroIntercambio ficheroIntercambio) {

        Mensaje mensaje = new Mensaje();
        mensaje.setCodigoEntidadRegistralOrigen(ficheroIntercambio.getCodigoEntidadRegistralDestino());
        mensaje.setCodigoEntidadRegistralDestino(ficheroIntercambio.getCodigoEntidadRegistralOrigen());
        mensaje.setIdentificadorIntercambio(ficheroIntercambio.getIdentificadorIntercambio());
        mensaje.setTipoMensaje(TipoMensaje.ACK);
        mensaje.setDescripcionMensaje(TipoMensaje.ACK.getName());


        enviarMensaje(mensaje);

        log.info("Mensaje de control (ACK) enviado");
    }

    /**
     * Envía un mensaje de control ACK.
     *
     * @param mensaje Campos del mensaje obtenidos del FicheroIntercambio recibido
     */
    public void enviarACK(Mensaje mensaje) {

        mensaje.setTipoMensaje(TipoMensaje.ACK);
        mensaje.setDescripcionMensaje(TipoMensaje.ACK.getName());

        enviarMensaje(mensaje);

        log.info("Mensaje de control (ACK) enviado");
    }

    /**
     * Envía un mensaje de control de tipo ERROR.
     *
     * @param mensaje Campos del mensaje obtenidos del FicheroIntercambio recibido
     * @param codigoError
     * @param descError
     */
    public void enviarMensajeError(Mensaje mensaje, String codigoError, String descError) {
        mensaje.setTipoMensaje(TipoMensaje.ERROR);
        mensaje.setCodigoError(codigoError);
        mensaje.setDescripcionMensaje(descError);

        enviarMensaje(mensaje);

        log.info("Mensaje de control (ERROR) enviado");

    }

    protected void enviarMensaje(Mensaje mensaje) {

        sicres3XML.validarMensaje(mensaje);

        RespuestaWS respuesta = null;

        // Crear el XML del mensaje en formato SICRES 3.0
        String xml = sicres3XML.createXMLMensaje(mensaje);

        log.info("Mensaje a ws_sir7: " + xml);

        WS_SIR7ServiceLocator locator = new WS_SIR7ServiceLocator();
        WS_SIR7_PortType ws_sir7 = null;
        try {
            ws_sir7 = locator.getWS_SIR7();
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        try {
            respuesta = ws_sir7.recepcionMensajeDatosControlDeAplicacion(xml);
        } catch (Exception e) {
            log.info("Error al enviar el mensaje");
            e.printStackTrace();
            throw new SIRException("Error en la llamada al servicio de recepción de mensaje de datos de control (WS_SIR7)");
        }

        if (respuesta != null) {

            log.info("Respuesta ws_sir7: " + respuesta.getCodigo() + " - " + respuesta.getDescripcion());

        }
    }
}
