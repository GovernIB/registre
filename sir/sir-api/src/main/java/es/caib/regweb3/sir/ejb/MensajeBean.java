package es.caib.regweb3.sir.ejb;

import es.caib.regweb3.model.RegistroSir;
import es.caib.regweb3.model.sir.Errores;
import es.caib.regweb3.model.sir.MensajeControl;
import es.caib.regweb3.model.sir.TipoMensaje;
import es.caib.regweb3.sir.core.excepcion.SIRException;
import es.caib.regweb3.sir.core.utils.FicheroIntercambio;
import es.caib.regweb3.sir.utils.Sicres3XML;
import es.caib.regweb3.sir.ws.api.wssir7.RespuestaWS;
import es.caib.regweb3.sir.ws.api.wssir7.WS_SIR7ServiceLocator;
import es.caib.regweb3.sir.ws.api.wssir7.WS_SIR7_PortType;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import java.net.URL;
import java.util.Date;

/**
 * Ejb para la gestión de la emisión de Mensajes de Datos de Control SICRES3 a un nodo distribuido
 */
@Stateless(name = "MensajeEJB")
public class MensajeBean implements MensajeLocal {

    public final Logger log = LoggerFactory.getLogger(getClass());

    private Sicres3XML sicres3XML = new Sicres3XML();


    /**
     * Envía un mensaje de control de confirmación.
     *
     * @param numeroRegistro Información del RegistroSir
     */
    public MensajeControl enviarMensajeConfirmacion(RegistroSir registroSir, String numeroRegistro, Date fechaRegistro) {

        log.info("Enviando Mensaje de confirmación del RegistroSir: " + registroSir.getIdentificadorIntercambio());

        MensajeControl confirmacion = new MensajeControl(RegwebConstantes.TIPO_COMUNICACION_ENVIADO);
        confirmacion.setEntidad(registroSir.getEntidad());
        confirmacion.setCodigoEntidadRegistralOrigen(registroSir.getCodigoEntidadRegistralDestino());
        confirmacion.setCodigoEntidadRegistralDestino(registroSir.getCodigoEntidadRegistralInicio());
        confirmacion.setIdentificadorIntercambio(registroSir.getIdentificadorIntercambio());
        confirmacion.setTipoMensaje(TipoMensaje.CONFIRMACION.getValue());
        confirmacion.setDescripcionMensaje(TipoMensaje.CONFIRMACION.getName());
        confirmacion.setNumeroRegistroEntradaDestino(numeroRegistro);
        if(fechaRegistro != null){
            confirmacion.setFechaEntradaDestino(fechaRegistro);
        }else {
            confirmacion.setFechaEntradaDestino(new Date());
        }

        return enviarMensaje(confirmacion);
    }

    /**
     * Envía un mensaje de control ACK.
     *
     * @param ficheroIntercambio Información del RegistroSir.
     */
    public MensajeControl enviarACK(FicheroIntercambio ficheroIntercambio) {

        log.info("Enviando Mensaje ACK: " + ficheroIntercambio.getIdentificadorIntercambio());

        MensajeControl mensaje = new MensajeControl(RegwebConstantes.TIPO_COMUNICACION_ENVIADO);
        mensaje.setCodigoEntidadRegistralOrigen(ficheroIntercambio.getCodigoEntidadRegistralDestino());
        mensaje.setCodigoEntidadRegistralDestino(ficheroIntercambio.getCodigoEntidadRegistralOrigen());
        mensaje.setIdentificadorIntercambio(ficheroIntercambio.getIdentificadorIntercambio());
        mensaje.setTipoMensaje(TipoMensaje.ACK.getValue());
        mensaje.setDescripcionMensaje(TipoMensaje.ACK.getName());

        return enviarMensaje(mensaje);
    }

    /**
     * Envía un mensaje de control ACK.
     *
     * @param mensaje Campos del mensaje obtenidos del FicheroIntercambio recibido
     */
    public MensajeControl enviarACK(MensajeControl mensaje) {

        log.info("Enviando Mensaje ACK: " + mensaje.getIdentificadorIntercambio());

        mensaje.setTipoMensaje(TipoMensaje.ACK.getValue());
        mensaje.setDescripcionMensaje(TipoMensaje.ACK.getName());

        return enviarMensaje(mensaje);
    }

    /**
     * Envía un mensaje de control de tipo ERROR.
     *
     * @param mensaje Campos del mensaje obtenidos del FicheroIntercambio recibido

     */
    public MensajeControl enviarMensajeError(MensajeControl mensaje) {

        return enviarMensaje(mensaje);
    }

    /**
     * Reenvia un mensaje de control
     * @param mensaje
     * @return
     */
    public MensajeControl reenviarMensajeControl(MensajeControl mensaje) {

        return enviarMensaje(mensaje);
    }

    private MensajeControl enviarMensaje(MensajeControl mensaje) {

        sicres3XML.validarMensaje(mensaje);

        RespuestaWS respuesta = null;

        // Crear el XML del mensaje en formato SICRES 3.0
        String xml = sicres3XML.createXMLMensaje(mensaje);

        try {
            WS_SIR7_PortType ws_sir7 = getWS_SIR7();

            respuesta = ws_sir7.recepcionMensajeDatosControlDeAplicacion(xml);

        } catch (Exception e) {
            log.info("Error al enviar el mensaje (" + mensaje.getIdentificadorIntercambio()+ ") - (" + mensaje.getDescripcionMensaje()+ ")");
            e.printStackTrace();
            throw new SIRException("Error en la llamada al servicio de recepción de mensaje de datos de control (WS_SIR7)");
        }

        if (respuesta != null) {

            //log.info("Respuesta ws_sir7: " + respuesta.getCodigo() + " - " + respuesta.getDescripcion());

            if (!Errores.OK.getValue().equals(respuesta.getCodigo())) {
                log.info("La respuesta de WS_SIR7 no es correcta");
                throw new SIRException("Error en la respuesta del servicio de recepción de mensaje de datos de control (WS_SIR7)");
            }
        }

        //log.info("Mensaje de control (" + mensaje.getDescripcionMensaje()+ ") enviado correctamente");

        return mensaje;
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
