package es.caib.regweb3.utils;


import es.caib.interdoc.ws.api.ObtenerReferenciaWs;
import es.caib.interdoc.ws.api.ObtenerReferenciaWsService;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.BindingProvider;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * @author mgonzalez
 * @version 1
 * 16/02/2023
 */
public class ReferenciaUnicaUtils {

    public static final Logger log = LoggerFactory.getLogger(ReferenciaUnicaUtils.class);


    private static final String OBTENER_REFERENCIA_WS = "interdocapi/interna/protected/ObtenerReferenciaWs";


    /**
     * @return
     */
    public static ObtenerReferenciaWs getObtenerReferenciaService(String server, String username, String password) throws I18NException {

        final String endpoint = server + OBTENER_REFERENCIA_WS;
        log.info(server);

        URL wsdlLocation = null;
        try {
            wsdlLocation = new URL(endpoint + "?wsdl");
            log.info("url " + wsdlLocation);
        } catch (MalformedURLException e) {
            throw new I18NException("error.generando.url.servicio.interdoc");
        }

        ObtenerReferenciaWsService service = new ObtenerReferenciaWsService(wsdlLocation);

        ObtenerReferenciaWs api = service.getObtenerReferenciaWs();

        configAddressUserPasswordTimeout(username, password, endpoint, 500000L, api);

        return api;
    }

    private static void configAddressUserPasswordTimeout(String usr, String pwd, String endpoint, Long timeout, Object api) {

        Map<String, Object> reqContext = ((BindingProvider) api).getRequestContext();
        reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
        reqContext.put(BindingProvider.USERNAME_PROPERTY, usr);
        reqContext.put(BindingProvider.PASSWORD_PROPERTY, pwd);

        reqContext.put("javax.xml.ws.client.connectionTimeout", timeout);
        reqContext.put("javax.xml.ws.client.receiveTimeout", timeout);

    }


}
