package es.caib.regweb3.utils;

import es.caib.dir3caib.ws.api.catalogo.Dir3CaibObtenerCatalogosWs;
import es.caib.dir3caib.ws.api.catalogo.Dir3CaibObtenerCatalogosWsService;
import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWsService;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWsService;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import javax.xml.ws.BindingProvider;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.util.Map;

/**
 *
 * @author anadal
 * @author mgonzalez
 *
 */
public class Dir3CaibUtils {

  public static final Logger log = Logger.getLogger(Dir3CaibUtils.class);

  private static final String OBTENER_CATALOGOS = "/ws/Dir3CaibObtenerCatalogos";

  private static final String OBTENER_UNIDADES = "/ws/Dir3CaibObtenerUnidades";

  private static final String OBTENER_OFICINAS = "/ws/Dir3CaibObtenerOficinas";

  private static final String UNIDAD_DENOMINACION = "/rest/unidad/denominacion";

  private static final String OFICINA_DENOMINACION = "/rest/oficina/denominacion";

  private static final Long TIMEOUT = 500000L;

  /**
   *
   * @return
   */
  public static Dir3CaibObtenerUnidadesWs getObtenerUnidadesService(String server, String username, String password) throws Exception {

    final String endpoint = server + OBTENER_UNIDADES;

    URL wsdlLocation = new URL(endpoint + "?wsdl");

    Dir3CaibObtenerUnidadesWsService service = new Dir3CaibObtenerUnidadesWsService(
        wsdlLocation);

    Dir3CaibObtenerUnidadesWs api = service.getDir3CaibObtenerUnidadesWs();

    configAddressUserPasswordTimeout(username, password, endpoint, 500000L, api);

    return api;
  }


  /**
   *
   * @return
   */
  public static Dir3CaibObtenerCatalogosWs getObtenerCatalogosService(String server, String username, String password) throws Exception {

    final String endpoint = server + OBTENER_CATALOGOS;

    URL wsdlLocation = new URL(endpoint + "?wsdl");

    Dir3CaibObtenerCatalogosWsService service = new Dir3CaibObtenerCatalogosWsService(
        wsdlLocation);

    Dir3CaibObtenerCatalogosWs api = service.getDir3CaibObtenerCatalogosWs();

    configAddressUserPasswordTimeout(username,password, endpoint, TIMEOUT, api);

    return api;
  }


  /**
   *
   * @return
   */
  public static Dir3CaibObtenerOficinasWs getObtenerOficinasService(String server, String username, String password) throws Exception {

    final String endpoint = server + OBTENER_OFICINAS;

    URL wsdlLocation = new URL(endpoint + "?wsdl");

    Dir3CaibObtenerOficinasWsService service = new Dir3CaibObtenerOficinasWsService(
        wsdlLocation);

    Dir3CaibObtenerOficinasWs api = service.getDir3CaibObtenerOficinasWs();

    configAddressUserPasswordTimeout(username,password, endpoint, TIMEOUT, api);

    return api;
  }

  /**
   * Obtiene la Denominacion de una unidad u oficina a partir de su CódigoDir3
   * @param codigoDir3
   * @param tipo
   * @return
   * @throws Exception
   */
  public static String denominacion(String server, String codigoDir3, String tipo) {
    log.debug("tipo: " + tipo);
    log.debug("codigoDir3: " + codigoDir3);

    DefaultHttpClient httpClient = new DefaultHttpClient();
    String url = null;
    String denominacion;

    if(tipo.toLowerCase().equals("oficina")){
      url = server + OFICINA_DENOMINACION;
    }else if(tipo.toLowerCase().equals("unidad")){
      url = server + UNIDAD_DENOMINACION;
    }

    url = url + "?codigo=" + codigoDir3;

    HttpGet getRequest = new HttpGet(url);
    getRequest.addHeader("accept", "application/json");

    HttpResponse response = null;
    try {
      response = httpClient.execute(getRequest);

      if (response.getStatusLine().getStatusCode() != 200) {
        //throw new RuntimeException("Failed : HTTP error code : "  + response.getStatusLine().getStatusCode());
        log.info("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
        return null;
      }

      StringWriter writer = new StringWriter();
      IOUtils.copy(response.getEntity().getContent(), writer, "UTF-8");
      denominacion = writer.toString();

    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }


    log.debug("denominacion: " + denominacion);
    log.debug("");

    return denominacion;

  }



  private static void configAddressUserPasswordTimeout(String usr, String pwd, String endpoint, Long timeout,
      Object api) {

    Map<String, Object> reqContext = ((BindingProvider) api).getRequestContext();
    reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
    reqContext.put(BindingProvider.USERNAME_PROPERTY, usr);
    reqContext.put(BindingProvider.PASSWORD_PROPERTY, pwd);

    reqContext.put("javax.xml.ws.client.connectionTimeout", timeout);
    reqContext.put("javax.xml.ws.client.receiveTimeout", timeout);


  }

}
