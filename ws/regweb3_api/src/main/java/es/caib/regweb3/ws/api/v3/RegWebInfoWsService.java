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
 * 2021-03-04T14:33:26.345+01:00
 * Generated source version: 3.0.2
 * 
 */
@WebServiceClient(name = "RegWebInfoWsService", 
                  wsdlLocation = "http://localhost:8080/regweb3/ws/v3/RegWebInfo?wsdl",
                  targetNamespace = "http://impl.v3.ws.regweb3.caib.es/") 
public class RegWebInfoWsService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://impl.v3.ws.regweb3.caib.es/", "RegWebInfoWsService");
    public final static QName RegWebInfoWs = new QName("http://impl.v3.ws.regweb3.caib.es/", "RegWebInfoWs");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:8080/regweb3/ws/v3/RegWebInfo?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(RegWebInfoWsService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://localhost:8080/regweb3/ws/v3/RegWebInfo?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public RegWebInfoWsService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public RegWebInfoWsService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public RegWebInfoWsService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RegWebInfoWsService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RegWebInfoWsService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RegWebInfoWsService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    

    /**
     *
     * @return
     *     returns RegWebInfoWs
     */
    @WebEndpoint(name = "RegWebInfoWs")
    public RegWebInfoWs getRegWebInfoWs() {
        return super.getPort(RegWebInfoWs, RegWebInfoWs.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns RegWebInfoWs
     */
    @WebEndpoint(name = "RegWebInfoWs")
    public RegWebInfoWs getRegWebInfoWs(WebServiceFeature... features) {
        return super.getPort(RegWebInfoWs, RegWebInfoWs.class, features);
    }

}
