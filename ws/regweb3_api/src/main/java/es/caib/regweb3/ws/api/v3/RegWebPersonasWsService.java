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
 * 2021-03-04T14:08:40.634+01:00
 * Generated source version: 3.0.2
 * 
 */
@WebServiceClient(name = "RegWebPersonasWsService", 
                  wsdlLocation = "http://localhost:8080/regweb3/ws/v3/RegWebPersonas?wsdl",
                  targetNamespace = "http://impl.v3.ws.regweb3.caib.es/") 
public class RegWebPersonasWsService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://impl.v3.ws.regweb3.caib.es/", "RegWebPersonasWsService");
    public final static QName RegWebPersonasWs = new QName("http://impl.v3.ws.regweb3.caib.es/", "RegWebPersonasWs");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:8080/regweb3/ws/v3/RegWebPersonas?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(RegWebPersonasWsService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://localhost:8080/regweb3/ws/v3/RegWebPersonas?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public RegWebPersonasWsService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public RegWebPersonasWsService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public RegWebPersonasWsService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RegWebPersonasWsService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RegWebPersonasWsService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RegWebPersonasWsService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    

    /**
     *
     * @return
     *     returns RegWebPersonasWs
     */
    @WebEndpoint(name = "RegWebPersonasWs")
    public RegWebPersonasWs getRegWebPersonasWs() {
        return super.getPort(RegWebPersonasWs, RegWebPersonasWs.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns RegWebPersonasWs
     */
    @WebEndpoint(name = "RegWebPersonasWs")
    public RegWebPersonasWs getRegWebPersonasWs(WebServiceFeature... features) {
        return super.getPort(RegWebPersonasWs, RegWebPersonasWs.class, features);
    }

}
