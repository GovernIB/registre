package es.caib.regweb3.ws.api.v3;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.6.4
 * 2019-10-29T13:10:55.543+01:00
 * Generated source version: 2.6.4
 * 
 */
@WebService(targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", name = "RegWebHelloWorldWs")
@XmlSeeAlso({ObjectFactory.class})
public interface RegWebHelloWorldWs {

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "getVersion", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.GetVersion")
    @WebMethod
    @ResponseWrapper(localName = "getVersionResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.GetVersionResponse")
    public java.lang.String getVersion();

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "getVersionWs", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.GetVersionWs")
    @WebMethod
    @ResponseWrapper(localName = "getVersionWsResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.GetVersionWsResponse")
    public int getVersionWs();

    @WebResult(name = "return", targetNamespace = "")
    @RequestWrapper(localName = "echo", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.Echo")
    @WebMethod
    @ResponseWrapper(localName = "echoResponse", targetNamespace = "http://impl.v3.ws.regweb3.caib.es/", className = "es.caib.regweb3.ws.api.v3.EchoResponse")
    public java.lang.String echo(
        @WebParam(name = "echo", targetNamespace = "")
        java.lang.String echo
    );
}
