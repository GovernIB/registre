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

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext smc) {

        log.info(" ------------------ RegWeb3SirWSHandler  handleMessage --------------");
        log.info(" ");
        log.info("HTTP_REQUEST_HEADERS :" + smc.get(MessageContext.HTTP_REQUEST_HEADERS));
        log.info("HTTP_REQUEST_METHOD :" + smc.get(MessageContext.HTTP_REQUEST_METHOD));
        log.info("HTTP_RESPONSE_CODE :" + smc.get(MessageContext.HTTP_RESPONSE_CODE));
        log.info("INBOUND_MESSAGE_ATTACHMENTS :" + smc.get(MessageContext.INBOUND_MESSAGE_ATTACHMENTS));
        log.info("MESSAGE_OUTBOUND_PROPERTY :" + smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY));
        log.info("OUTBOUND_MESSAGE_ATTACHMENTS :" + smc.get(MessageContext.OUTBOUND_MESSAGE_ATTACHMENTS));
        log.info("PATH_INFO :" + smc.get(MessageContext.PATH_INFO));
        log.info("QUERY_STRING :" + smc.get(MessageContext.QUERY_STRING));
        log.info("REFERENCE_PARAMETERS :" + smc.get(MessageContext.REFERENCE_PARAMETERS));
        log.info("SERVLET_CONTEXT :" + smc.get(MessageContext.SERVLET_CONTEXT));
        log.info("SERVLET_REQUEST :" + smc.get(MessageContext.SERVLET_REQUEST));
        log.info("SERVLET_RESPONSE :" + smc.get(MessageContext.SERVLET_RESPONSE));
        log.info("WSDL_DESCRIPTION :" + smc.get(MessageContext.WSDL_DESCRIPTION));
        log.info("WSDL_INTERFACE :" + smc.get(MessageContext.WSDL_INTERFACE));
        log.info("WSDL_OPERATION :" + smc.get(MessageContext.WSDL_OPERATION));
        log.info("WSDL_SERVICE :" + smc.get(MessageContext.WSDL_SERVICE));

        // Obtenemos el tipo a procesar
        Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        try {

            if (!outboundProperty) { // Mensaje de entrada

                String mensaje = soapMessageToString(smc.getMessage());

                // Añadimos el namespace al inicio
                mensaje = mensaje.replace("<envioFicherosAAplicacion", "<ns1:envioFicherosAAplicacion xmlns:ns1=\"http://impl.manager.cct.map.es\"");

                // Añadimos el namespace al final
                mensaje = mensaje.replace("</envioFicherosAAplicacion>","</ns1:envioFicherosAAplicacion>");

                // Rehacemos la petición con el nuevo mensaje
                InputStream bStream = new ByteArrayInputStream(mensaje.getBytes());
                SOAPMessage request = MessageFactory.newInstance().createMessage(null, bStream);
                smc.setMessage(request);

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
