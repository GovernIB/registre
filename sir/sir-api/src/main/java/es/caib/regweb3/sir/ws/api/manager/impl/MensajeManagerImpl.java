package es.caib.regweb3.sir.ws.api.manager.impl;

import es.caib.regweb3.sir.core.excepcion.SIRException;
import es.caib.regweb3.sir.ws.api.manager.MensajeManager;
import es.caib.regweb3.sir.ws.api.manager.SicresXMLManager;
import es.caib.regweb3.sir.ws.api.utils.Mensaje;
import es.caib.regweb3.sir.ws.api.wssir7.RespuestaWS;
import es.caib.regweb3.sir.ws.api.wssir7.WS_SIR7ServiceLocator;
import es.caib.regweb3.sir.ws.api.wssir7.WS_SIR7_PortType;
import org.apache.log4j.Logger;

import javax.xml.rpc.ServiceException;

/**
 *
 */
public class MensajeManagerImpl implements MensajeManager {

    public final Logger log = Logger.getLogger(getClass());

    public SicresXMLManager sicresXMLManager = new SicresXMLManagerImpl();


    public void enviarMensaje(Mensaje mensaje) {

        sicresXMLManager.validarMensaje(mensaje);

        RespuestaWS respuesta = null;

        // Crear el XML del mensaje en formato SICRES 3.0
        String xml = sicresXMLManager.createXMLMensaje(mensaje);

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
