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
 * 2021-03-31T09:29:20.787+02:00
 * Generated source version: 3.0.2
 * 
 */
@WebServiceClient(name = "RegWebAsientoRegistralWsService", 
                  wsdlLocation = "http://localhost:8080/regweb3/ws/v3/RegWebAsientoRegistral?wsdl",
                  targetNamespace = "http://impl.v3.ws.regweb3.caib.es/") 
public class RegWebAsientoRegistralWsService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://impl.v3.ws.regweb3.caib.es/", "RegWebAsientoRegistralWsService");
    public final static QName RegWebAsientoRegistralWs = new QName("http://impl.v3.ws.regweb3.caib.es/", "RegWebAsientoRegistralWs");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:8080/regweb3/ws/v3/RegWebAsientoRegistral?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(RegWebAsientoRegistralWsService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://localhost:8080/regweb3/ws/v3/RegWebAsientoRegistral?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public RegWebAsientoRegistralWsService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public RegWebAsientoRegistralWsService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public RegWebAsientoRegistralWsService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RegWebAsientoRegistralWsService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RegWebAsientoRegistralWsService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RegWebAsientoRegistralWsService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    

    /**
     *
     * @return
     *     returns RegWebAsientoRegistralWs
     */
    @WebEndpoint(name = "RegWebAsientoRegistralWs")
    public RegWebAsientoRegistralWs getRegWebAsientoRegistralWs() {
        return super.getPort(RegWebAsientoRegistralWs, RegWebAsientoRegistralWs.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns RegWebAsientoRegistralWs
     */
    @WebEndpoint(name = "RegWebAsientoRegistralWs")
    public RegWebAsientoRegistralWs getRegWebAsientoRegistralWs(WebServiceFeature... features) {
        return super.getPort(RegWebAsientoRegistralWs, RegWebAsientoRegistralWs.class, features);
    }

}
