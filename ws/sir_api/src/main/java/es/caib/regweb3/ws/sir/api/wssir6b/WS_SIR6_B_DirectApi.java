package es.caib.regweb3.ws.sir.api.wssir6b;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;

/**
 * 
 * @author anadal
 *
 */
public class WS_SIR6_B_DirectApi {

  private static  final Logger log = Logger.getLogger(WS_SIR6_B_DirectApi.class);
  

  public static RespuestaWS recepcionFicheroDeAplicacion(String xml, String strURL)
    throws Exception {
    int pos = xml.indexOf('\n');

    xml = xml.substring(pos + 1);

    log.debug(xml);
    
    
    String strSoapAction = "";
    // Get file to be posted
    String strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><soapenv:Body><ns1:recepcionFicheroDeAplicacion soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns1=\"http://impl.manager.cct.map.es\"><registro xsi:type=\"xsd:string\">&lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot; standalone=&quot;yes&quot;?&gt;\n"
        + xml          
        + "</registro></ns1:recepcionFicheroDeAplicacion></soapenv:Body></soapenv:Envelope>";
    //File input = new File(strXMLFilename);
    // Prepare HTTP post
    PostMethod post = new PostMethod(strURL);
    // Request content will be retrieved directly
    // from the input stream
    RequestEntity entity = new StringRequestEntity(strXML, "text/xml", "ISO-8859-1");
      // new FileRequestEntity(input, "text/xml; charset=ISO-8859-1");
    post.setRequestEntity(entity);
    // consult documentation for your web service
    post.setRequestHeader("SOAPAction", strSoapAction);
    // Get HTTP client
    HttpClient httpclient = new HttpClient();
    // Execute request
    try {
      int result = httpclient.executeMethod(post);
      // Display status code
      log.debug("Response status code: " + result);
      // Display response
      log.debug("Response body: ");
      String body = post.getResponseBodyAsString();
      log.debug(body);
      
      int pos1= body.indexOf("<codigo>");
      int pos2= body.indexOf("</codigo>");
      
      String codigo = body.substring(pos1 + "<codigo>".length(), pos2);
      log.debug("CODIGO = " + codigo);

      pos1= body.indexOf("<descripcion>");
      pos2= body.indexOf("</descripcion>");
      
      String desc = body.substring(pos1 + "<descripcion>".length(), pos2);
      
      log.debug("DESC. = " + desc);
      
      RespuestaWS resp = new RespuestaWS();
      resp.setCodigo(codigo);
      resp.setDescripcion(desc);
      
      return resp;
      
    } finally {
      // Release current connection to the connection pool once you are done
      post.releaseConnection();
    }
  }
}
