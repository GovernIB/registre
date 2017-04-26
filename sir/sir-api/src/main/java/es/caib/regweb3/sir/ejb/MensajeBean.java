package es.caib.regweb3.sir.ejb;

import es.caib.regweb3.model.RegistroSir;
import es.caib.regweb3.sir.core.excepcion.SIRException;
import es.caib.regweb3.sir.core.model.Errores;
import es.caib.regweb3.sir.core.model.TipoMensaje;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.sir.core.utils.Mensaje;
import es.caib.regweb3.sir.utils.Sicres3XML;
import es.caib.regweb3.sir.ws.api.wssir7.RespuestaWS;
import es.caib.regweb3.sir.ws.api.wssir7.WS_SIR7ServiceLocator;
import es.caib.regweb3.sir.ws.api.wssir7.WS_SIR7_PortType;
import es.caib.regweb3.utils.Configuracio;
import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import java.net.URL;
import java.util.Date;

/**
 * Ejb para la gestión de la emisión de Mensajes de Datos de Control SICRES3 a un nodo distribuido
 */
@Stateless(name = "MensajeEJB")
public class MensajeBean implements MensajeLocal {

    public final Logger log = Logger.getLogger(getClass());

    private Sicres3XML sicres3XML = new Sicres3XML();


    /**
     * Envía un mensaje de control de confirmación.
     *
     * @param numeroRegistro Información del RegistroSir
     */
    public void enviarMensajeConfirmacion(RegistroSir registroSir, String numeroRegistro) {

        log.info("Enviando Mensaje de confirmación del RegistroSir: " + registroSir.getIdentificadorIntercambio());

        Mensaje confirmacion = new Mensaje();
        confirmacion.setCodigoEntidadRegistralOrigen(registroSir.getCodigoEntidadRegistralDestino());
        confirmacion.setCodigoEntidadRegistralDestino(registroSir.getCodigoEntidadRegistralInicio());
        confirmacion.setIdentificadorIntercambio(registroSir.getIdentificadorIntercambio());
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
     * @param ficheroIntercambio Información del RegistroSir.
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

    private void enviarMensaje(Mensaje mensaje) {

        sicres3XML.validarMensaje(mensaje);

        RespuestaWS respuesta = null;

        // Crear el XML del mensaje en formato SICRES 3.0
        String xml = sicres3XML.createXMLMensaje(mensaje);

        log.info("Mensaje a ws_sir7: " + xml);

        try {
            WS_SIR7_PortType ws_sir7 = getWS_SIR7();

            respuesta = ws_sir7.recepcionMensajeDatosControlDeAplicacion(xml);

        } catch (Exception e) {
            log.info("Error al enviar el mensaje");
            e.printStackTrace();
            throw new SIRException("Error en la llamada al servicio de recepción de mensaje de datos de control (WS_SIR7)");
        }

        if (respuesta != null) {

            log.info("Respuesta ws_sir7: " + respuesta.getCodigo() + " - " + respuesta.getDescripcion());

            if (!Errores.OK.getValue().equals(respuesta.getCodigo())) {
                log.info("La respuesta de WS_SIR7 no es correcta");
                throw new SIRException("Error en la respuesta del servicio de recepción de mensaje de datos de control (WS_SIR7)");
            }

        }
    }

    /**
     * @return
     * @throws Exception
     */
    private WS_SIR7_PortType getWS_SIR7() throws Exception {
        WS_SIR7ServiceLocator locator = new WS_SIR7ServiceLocator();
        URL url = new URL(Configuracio.getSirServerBase() + "/WS_SIR7");

        return  locator.getWS_SIR7(url);
    }
}
