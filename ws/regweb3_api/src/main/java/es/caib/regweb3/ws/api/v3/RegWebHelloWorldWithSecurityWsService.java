package es.caib.regweb3.ws.api.v3;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class was generated by Apache CXF 3.0.2
 * 2021-03-22T14:06:17.601+01:00
 * Generated source version: 3.0.2
 * 
 */
@WebServiceClient(name = "RegWebHelloWorldWithSecurityWsService", 
                  wsdlLocation = "http://localhost:8080/regweb3/ws/v3/RegWebHelloWorldWithSecurity?wsdl",
                  targetNamespace = "http://impl.v3.ws.regweb3.caib.es/") 
public class RegWebHelloWorldWithSecurityWsService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://impl.v3.ws.regweb3.caib.es/", "RegWebHelloWorldWithSecurityWsService");
    public final static QName RegWebHelloWorldWithSecurityWs = new QName("http://impl.v3.ws.regweb3.caib.es/", "RegWebHelloWorldWithSecurityWs");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:8080/regweb3/ws/v3/RegWebHelloWorldWithSecurity?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(RegWebHelloWorldWithSecurityWsService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://localhost:8080/regweb3/ws/v3/RegWebHelloWorldWithSecurity?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public RegWebHelloWorldWithSecurityWsService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public RegWebHelloWorldWithSecurityWsService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public RegWebHelloWorldWithSecurityWsService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RegWebHelloWorldWithSecurityWsService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RegWebHelloWorldWithSecurityWsService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RegWebHelloWorldWithSecurityWsService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    

    /**
     *
     * @return
     *     returns RegWebHelloWorldWithSecurityWs
     */
    @WebEndpoint(name = "RegWebHelloWorldWithSecurityWs")
    public RegWebHelloWorldWithSecurityWs getRegWebHelloWorldWithSecurityWs() {
        return super.getPort(RegWebHelloWorldWithSecurityWs, RegWebHelloWorldWithSecurityWs.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns RegWebHelloWorldWithSecurityWs
     */
    @WebEndpoint(name = "RegWebHelloWorldWithSecurityWs")
    public RegWebHelloWorldWithSecurityWs getRegWebHelloWorldWithSecurityWs(WebServiceFeature... features) {
        return super.getPort(RegWebHelloWorldWithSecurityWs, RegWebHelloWorldWithSecurityWs.class, features);
    }

}
