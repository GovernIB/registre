package es.caib.regweb3.ws.api.v3;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.0.2
 * 2021-10-22T11:33:50.254+02:00
 * Generated source version: 3.0.2
 * 
 */
@WebServiceClient(name = "RegWebRegistroSalidaWsService", 
                  wsdlLocation = "http://localhost:8080/regweb3/ws/v3/RegWebRegistroSalida?wsdl",
                  targetNamespace = "http://impl.v3.ws.regweb3.caib.es/") 
public class RegWebRegistroSalidaWsService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://impl.v3.ws.regweb3.caib.es/", "RegWebRegistroSalidaWsService");
    public final static QName RegWebRegistroSalidaWs = new QName("http://impl.v3.ws.regweb3.caib.es/", "RegWebRegistroSalidaWs");
    static {
        URL url = null;
        try {
            url = new URL("http://localhost:8080/regweb3/ws/v3/RegWebRegistroSalida?wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(RegWebRegistroSalidaWsService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "http://localhost:8080/regweb3/ws/v3/RegWebRegistroSalida?wsdl");
        }
        WSDL_LOCATION = url;
    }

    public RegWebRegistroSalidaWsService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public RegWebRegistroSalidaWsService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public RegWebRegistroSalidaWsService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RegWebRegistroSalidaWsService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RegWebRegistroSalidaWsService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public RegWebRegistroSalidaWsService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    

    /**
     *
     * @return
     *     returns RegWebRegistroSalidaWs
     */
    @WebEndpoint(name = "RegWebRegistroSalidaWs")
    public RegWebRegistroSalidaWs getRegWebRegistroSalidaWs() {
        return super.getPort(RegWebRegistroSalidaWs, RegWebRegistroSalidaWs.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns RegWebRegistroSalidaWs
     */
    @WebEndpoint(name = "RegWebRegistroSalidaWs")
    public RegWebRegistroSalidaWs getRegWebRegistroSalidaWs(WebServiceFeature... features) {
        return super.getPort(RegWebRegistroSalidaWs, RegWebRegistroSalidaWs.class, features);
    }

}
