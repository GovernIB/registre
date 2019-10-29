package es.caib.regweb3.ws.api.v3;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class was generated by Apache CXF 2.6.4
 * 2019-10-29T13:10:55.577+01:00
 * Generated source version: 2.6.4
 * 
 */
@WebServiceClient(name = "RegWebHelloWorldWsService", 
                  wsdlLocation = "http://localhost:8080/regweb3/ws/v3/RegWebHelloWorld?wsdl",
                  targetNamespace = "http://impl.v3.ws.regweb3.caib.es/") 
public class RegWebHelloWorldWsService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://impl.v3.ws.regweb3.caib.es/", "RegWebHelloWorldWsService");
    public final static QName RegWebHelloWorldWs = new QName("http://impl.v3.ws.regweb3.caib.es/", "RegWebHelloWorldWs");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:8080/regweb3/ws/v3/RegWebHelloWorld?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(RegWebHelloWorldWsService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://localhost:8080/regweb3/ws/v3/RegWebHelloWorld?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public RegWebHelloWorldWsService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public RegWebHelloWorldWsService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public RegWebHelloWorldWsService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RegWebHelloWorldWsService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RegWebHelloWorldWsService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RegWebHelloWorldWsService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns RegWebHelloWorldWs
     */
    @WebEndpoint(name = "RegWebHelloWorldWs")
    public RegWebHelloWorldWs getRegWebHelloWorldWs() {
        return super.getPort(RegWebHelloWorldWs, RegWebHelloWorldWs.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns RegWebHelloWorldWs
     */
    @WebEndpoint(name = "RegWebHelloWorldWs")
    public RegWebHelloWorldWs getRegWebHelloWorldWs(WebServiceFeature... features) {
        return super.getPort(RegWebHelloWorldWs, RegWebHelloWorldWs.class, features);
    }

}
