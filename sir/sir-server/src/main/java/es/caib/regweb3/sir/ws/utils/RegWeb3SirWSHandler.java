package es.caib.regweb3.sir.ws.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPEnvelope;
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


        Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        try {

            SOAPEnvelope msg = smc.getMessage().getSOAPPart().getEnvelope(); //get the SOAP Message envelope

            if (outboundProperty.booleanValue()) {

                log.info("Mesaje de salida: " + soapMessageToString(smc.getMessage()));
            } else {
                log.info("Mesaje de entrada: " + soapMessageToString(smc.getMessage()));
                log.info("");
                log.info("");

                String result = soapMessageToString(smc.getMessage()).replace("<envioFicherosAAplicacion", "<ns1:envioFicherosAAplicacion xmlns:ns1=\"http://impl.manager.cct.map.es\"");

                result = result.replace("</envioFicherosAAplicacion>","</ns1:envioFicherosAAplicacion>");

                InputStream bStream = new ByteArrayInputStream(result.getBytes());
                SOAPMessage request = MessageFactory.newInstance().createMessage(null, bStream);
                smc.setMessage(request);

                /*SOAPBody body = msg.getBody();
                //body.removeNamespaceDeclaration(UNWANTED_NS_PREFIX);
                body.addNamespaceDeclaration("ns1", "http://impl.manager.cct.map.es");

                msg.addBody();*/



                log.info("Mesaje de entrada modificado: " + soapMessageToString(smc.getMessage()));


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
