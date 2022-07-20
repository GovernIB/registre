package es.caib.regweb3.sir.ws.utils;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.binding.soap.interceptor.SoapPreProtocolOutInterceptor;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Interceptor para modificar el mensaje recibido desde el Componente CIR y que funcione correctamente
 *
 * Basado en: http://stackoverflow.com/questions/6915428/how-to-modify-the-raw-xml-message-of-an-outbound-cxf-request
 * @author earrivi
 */

public class SirInInterceptor extends AbstractPhaseInterceptor<Message> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private static final String WS_SIR8_B_QUERY_STRING = "envioFicherosAAplicacion";
    private static final String WS_SIR9_QUERY_STRING = "envioMensajeDatosControlAAplicacion";

    public SirInInterceptor() {
        super(Phase.PRE_STREAM);
        addBefore(SoapPreProtocolOutInterceptor.class.getName());
    }

    @SuppressWarnings("unchecked")
    public void handleMessage(Message message) throws Fault {

        log.debug(" ------------------- Dentro SirInInterceptor -------------------");

        boolean isOutbound = false;
        isOutbound = message == message.getExchange().getOutMessage() || message == message.getExchange().getOutFaultMessage();

        //Obtener cabeceras
        Map<String, List<String>> headers = CastUtils.cast((Map)message.get(Message.PROTOCOL_HEADERS));
        // Mostrar cabeceras
        /*for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            log.debug(entry.getKey() + ": " + entry.getValue().toString());
        }*/

        // Eliminamos la cabecera SOAPAction para evitar el error: org.apache.cxf.interceptor.Fault: The given SOAPAction does not match an operation.
        List<String> sa = headers.get("SOAPAction");
        if (sa != null && !sa.isEmpty()) {
            headers.remove("SOAPAction");
        }

        // Tratamos el mensaje SOAP recibido
        if (!isOutbound) {
            try {

                // Obtenemos el mensaje recibido
                InputStream is = message.getContent(InputStream.class);
                String envelopeMessage = IOUtils.toString(is, "UTF-8");
                IOUtils.closeQuietly(is);
                String res = "";

                // Petición al WS_SIR8_B
                if (envelopeMessage != null && envelopeMessage.contains(WS_SIR8_B_QUERY_STRING)) {

                    res = changeWS_SIR8_BMessage(envelopeMessage);
                  
                // Petición al WS_SIR9
                } else if(envelopeMessage != null && envelopeMessage.contains(WS_SIR9_QUERY_STRING)){

                    res = changeWS_SIR9Message(envelopeMessage);
                }

                res = res != null ? res : envelopeMessage;

                // Rehacemos la petición con el mensaje transformado
                is = IOUtils.toInputStream(res, "UTF-8");
                message.setContent(InputStream.class, is);
                IOUtils.closeQuietly(is);

            } catch (IOException ioe) {

                log.info("Error parseando mensaje SOAP");
                throw new RuntimeException(ioe);
            }
        }

    }

    /**
     * Añadimos el namespace al inicio y final del mensaje
     * @param message
     * @return
     */
    private String changeWS_SIR9Message(String message) {

        // Añadimos el namespace al inicio del mensaje
        message = message.replace("<envioMensajeDatosControlAAplicacion", "<ns1:envioMensajeDatosControlAAplicacion xmlns:ns1=\"http://impl.manager.cct.map.es\"");

        // Añadimos el namespace al final del mensaje
        message = message.replace("</envioMensajeDatosControlAAplicacion>","</ns1:envioMensajeDatosControlAAplicacion>");

        return message;
    }

    /**
     * Añadimos el namespace al inicio y final del mensaje
     * @param message
     * @return
     */
    private String changeWS_SIR8_BMessage(String message) {

        // Añadimos el namespace al inicio del mensaje
        message = message.replace("<envioFicherosAAplicacion", "<ns1:envioFicherosAAplicacion xmlns:ns1=\"http://impl.manager.cct.map.es\"");

        // Añadimos el namespace al final del mensaje
        message = message.replace("</envioFicherosAAplicacion>","</ns1:envioFicherosAAplicacion>");

        return message;
    }

}