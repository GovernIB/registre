package es.caib.regweb3.utils;

import es.caib.dir3caib.ws.api.catalogo.Dir3CaibObtenerCatalogosWs;
import es.caib.dir3caib.ws.api.catalogo.Dir3CaibObtenerCatalogosWsService;
import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWsService;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWsService;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.BindingProvider;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

/**
 * @author anadal
 * @author mgonzalez
 */
public class Dir3CaibUtils {

    public static final Logger log = LoggerFactory.getLogger(Dir3CaibUtils.class);

    private static final String OBTENER_CATALOGOS = "/ws/Dir3CaibObtenerCatalogos";
    private static final String OBTENER_UNIDADES = "/ws/Dir3CaibObtenerUnidades";
    private static final String OBTENER_OFICINAS = "/ws/Dir3CaibObtenerOficinas";
    private static final String UNIDAD_DENOMINACION = "/rest/unidad/denominacion";
    private static final String OFICINA_DENOMINACION = "/rest/oficina/denominacion";

    private static final Long TIMEOUT = 500000L;

    /**
     * @return
     */
    public static Dir3CaibObtenerUnidadesWs getObtenerUnidadesService(String server, String username, String password) throws I18NException {

        final String endpoint = server + OBTENER_UNIDADES;

        URL wsdlLocation = null;
        try {
            wsdlLocation = new URL(endpoint + "?wsdl");
        } catch (MalformedURLException e) {
            throw new I18NException("Error generando la url del servicio de Dir3Caib");
        }

        Dir3CaibObtenerUnidadesWsService service = new Dir3CaibObtenerUnidadesWsService(wsdlLocation);

        Dir3CaibObtenerUnidadesWs api = service.getDir3CaibObtenerUnidadesWs();

        configAddressUserPasswordTimeout(username, password, endpoint, 500000L, api);

        return api;
    }


    /**
     * @return
     */
    public static Dir3CaibObtenerCatalogosWs getObtenerCatalogosService(String server, String username, String password) throws I18NException {

        final String endpoint = server + OBTENER_CATALOGOS;

        URL wsdlLocation = null;
        try {
            wsdlLocation = new URL(endpoint + "?wsdl");
        } catch (MalformedURLException e) {
            throw new I18NException("Error generando la url del servicio de Dir3Caib");
        }

        Dir3CaibObtenerCatalogosWsService service = new Dir3CaibObtenerCatalogosWsService(wsdlLocation);

        Dir3CaibObtenerCatalogosWs api = service.getDir3CaibObtenerCatalogosWs();

        configAddressUserPasswordTimeout(username, password, endpoint, TIMEOUT, api);

        return api;
    }


    /**
     * @return
     */
    public static Dir3CaibObtenerOficinasWs getObtenerOficinasService(String server, String username, String password) throws I18NException {

        final String endpoint = server + OBTENER_OFICINAS;

        URL wsdlLocation = null;
        try {
            wsdlLocation = new URL(endpoint + "?wsdl");
        } catch (MalformedURLException e) {
            throw new I18NException("Error generando la url del servicio de Dir3Caib");
        }

        Dir3CaibObtenerOficinasWsService service = new Dir3CaibObtenerOficinasWsService(wsdlLocation);

        Dir3CaibObtenerOficinasWs api = service.getDir3CaibObtenerOficinasWs();

        configAddressUserPasswordTimeout(username, password, endpoint, TIMEOUT, api);

        return api;
    }

    /**
     * Obtiene la Denominacion de una unidad u oficina a partir de su CódigoDir3
     *
     * @param codigoDir3
     * @param tipo
     * @return
     * @throws Exception
     */
    public static String denominacion(String server, String codigoDir3, String tipo) {

        String url = null;
        String denominacion;

        if (tipo.equalsIgnoreCase("oficina")) {
            url = server + OFICINA_DENOMINACION;
        } else if (tipo.equalsIgnoreCase("unidad")) {
            url = server + UNIDAD_DENOMINACION;
        }

        // Parámetro codigo
        url = url + "?codigo=" + codigoDir3;

        HttpClient httpClient = HttpClient.newBuilder().build();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            denominacion = response.body();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            log.info("Error obteniendo la denominacion de: " + codigoDir3);
            return null;
        }

        return denominacion;
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
