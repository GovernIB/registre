package es.caib.regweb3.sir.ws.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * Created by earrivi on 15/03/2017.
 */
public class RegWeb3SirWSHandler implements SOAPHandler<SOAPMessageContext> {

    protected final Log log = LogFactory.getLog(getClass());

    private static final String WS_SIR8_B_QUERY_STRING = "envioFicherosAAplicacion";
    private static final String WS_SIR9_QUERY_STRING = "envioMensajeDatosControlAAplicacion";

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext smc) {

        String query_string =  (String) smc.get(MessageContext.QUERY_STRING);
        Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY); //Mensaje de entrada o salida

        try {

            // Petición al WS_SIR8_B
            if (query_string != null && query_string.contains(WS_SIR8_B_QUERY_STRING)) {

                if (!outboundProperty) { // Mensaje de entrada

                    log.info("Se trata de una petición al WS_SIR8_B, modificamos las cabeceras");

                    // Obtenemos el mensaje de la petición
                    String mensaje = soapMessageToString(smc.getMessage());

                    // Añadimos el namespace al inicio del mensaje
                    mensaje = mensaje.replace("<envioFicherosAAplicacion", "<ns1:envioFicherosAAplicacion xmlns:ns1=\"http://impl.manager.cct.map.es\"");

                    // Añadimos el namespace al final del mensaje
                    mensaje = mensaje.replace("</envioFicherosAAplicacion>","</ns1:envioFicherosAAplicacion>");

                    // Rehacemos la petición con el nuevo mensaje
                    InputStream bStream = new ByteArrayInputStream(mensaje.getBytes());
                    SOAPMessage request = MessageFactory.newInstance().createMessage(null, bStream);
                    smc.setMessage(request);

                }

            // Petición al WS_SIR9
            }else if(query_string != null && query_string.contains(WS_SIR9_QUERY_STRING)){

                if (!outboundProperty) { // Mensaje de entrada

                    log.info("Se trata de una petición al WS_SIR9, modificamos las cabeceras");

                    // Obtenemos el mensaje de la petición
                    String mensaje = soapMessageToString(smc.getMessage());

                    // Añadimos el namespace al inicio del mensaje
                    mensaje = mensaje.replace("<envioMensajeDatosControlAAplicacion", "<ns1:envioMensajeDatosControlAAplicacion xmlns:ns1=\"http://impl.manager.cct.map.es\"");

                    // Añadimos el namespace al final del mensaje
                    mensaje = mensaje.replace("</envioMensajeDatosControlAAplicacion>","</ns1:envioMensajeDatosControlAAplicacion>");

                    // Rehacemos la petición con el nuevo mensaje
                    InputStream bStream = new ByteArrayInputStream(mensaje.getBytes());
                    SOAPMessage request = MessageFactory.newInstance().createMessage(null, bStream);
                    smc.setMessage(request);

                }
            }


        } catch (SOAPException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean handleFault(SOAPMessageContext smc) {
        return false;
    }

    @Override
    public void close(MessageContext messageContext) {

    }

    /**
     * Convierte un SOAPMessage en un String
     * @param message
     * @return
     */
    public String soapMessageToString(SOAPMessage message)
    {
        String result = null;

        if (message != null)
        {
            ByteArrayOutputStream baos = null;
            try
            {
                baos = new ByteArrayOutputStream();
                message.writeTo(baos);
                result = baos.toString();
            }  catch (Exception e)
            {} finally
            {
                if (baos != null)
                {
                    try
                    {
                        baos.close();
                    } catch (IOException ioe)
                    {
                    }
                }
            }
        }
        return result;
    }
}
