package es.caib.regweb.ws.v3.test;

import es.caib.regweb.ws.api.v3.*;
import es.caib.regweb.ws.api.v3.utils.I18NUtils;

import javax.xml.ws.BindingProvider;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * @author anadal
 * 
 */
public abstract class RegWebTestUtils {

  public static final String HELLO_WORLD = "RegWebHelloWorld";
  
  public static final String HELLO_WORLD_WITH_SECURITY = "RegWebHelloWorldWithSecurity";
  
  public static final String REGWEB_PERSONAS = "RegWebPersonas";
  public static final String REGWEB_REGISTRO_ENTRADA = "RegWebRegistroEntrada";
  public static final String REGWEB_REGISTRO_SALIDA = "RegWebRegistroSalida";
  public static final String REGWEB_INFO = "RegWebInfo";

  // TODO GEN APP ADD OTHERS
  
  private static Properties testProperties = new Properties();
  
  static {
    // Traduccions
    try {
      Class.forName(I18NUtils.class.getName());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    // Propietats del Servidor
    try {
        System.out.println(new File(".").getAbsolutePath());
      testProperties.load(new FileInputStream("test.properties"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
 

  public static String getEndPoint(String api) {
    return testProperties.getProperty("test_host") + api;
  }

  public static String getTestAppUserName() {
    return testProperties.getProperty("test_usr");
  }
  

  public static String getTestAppPassword() {
    return testProperties.getProperty("test_pwd");
  }

  public static String getTestEntidadCodigoDir3() {
    return testProperties.getProperty("test_entidadcodigodir3");
  }

  public static String getTestArchivosPath() {
    return testProperties.getProperty("test_archivos_path");
  }

  public static void configAddressUserPassword(String usr, String pwd,
      String endpoint, Object api) {

    Map<String, Object> reqContext = ((BindingProvider) api).getRequestContext();
    reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
    reqContext.put(BindingProvider.USERNAME_PROPERTY, usr);
    reqContext.put(BindingProvider.PASSWORD_PROPERTY, pwd);
  }

  public static RegWebHelloWorldWs getHelloWorldApi() throws Exception {

    final String endpoint = getEndPoint(HELLO_WORLD);
    
    final URL wsdl = new URL(endpoint + "?wsdl");
    

    RegWebHelloWorldWsService helloService = new RegWebHelloWorldWsService(wsdl);

    RegWebHelloWorldWs helloApi = helloService.getRegWebHelloWorldWs();

    // Adreça servidor
    Map<String, Object> reqContext = ((BindingProvider) helloApi).getRequestContext();
    reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);

    return helloApi;

  }


  
 
  public static RegWebHelloWorldWithSecurityWs getHelloWorldWithSecurityApi() throws Exception {
    final String endpoint = getEndPoint(HELLO_WORLD_WITH_SECURITY);
    final URL wsdl = new URL(endpoint + "?wsdl");
    RegWebHelloWorldWithSecurityWsService service = new RegWebHelloWorldWithSecurityWsService(wsdl);

    RegWebHelloWorldWithSecurityWs api = service.getRegWebHelloWorldWithSecurityWs();

    configAddressUserPassword(getTestAppUserName(), getTestAppPassword(), endpoint, api);

    return api;
  }
  
  
  public static RegWebPersonasWs getPersonasApi() throws Exception  {
    final String endpoint = getEndPoint(REGWEB_PERSONAS);

    final URL wsdl = new URL(endpoint + "?wsdl");
    RegWebPersonasWsService service = new RegWebPersonasWsService(wsdl);

    RegWebPersonasWs api = service.getRegWebPersonasWs();

    configAddressUserPassword(getTestAppUserName(), getTestAppPassword(), endpoint, api);

    return api;
  }

    public static RegWebRegistroEntradaWs getRegistroEntradaApi() throws Exception  {
        final String endpoint = getEndPoint(REGWEB_REGISTRO_ENTRADA);

        final URL wsdl = new URL(endpoint + "?wsdl");
        RegWebRegistroEntradaWsService service = new RegWebRegistroEntradaWsService(wsdl);

        RegWebRegistroEntradaWs api = service.getRegWebRegistroEntradaWs();

        configAddressUserPassword(getTestAppUserName(), getTestAppPassword(), endpoint, api);

        return api;
    }

    public static RegWebRegistroSalidaWs getRegistroSalidaApi() throws Exception  {
        final String endpoint = getEndPoint(REGWEB_REGISTRO_SALIDA);

        final URL wsdl = new URL(endpoint + "?wsdl");
        RegWebRegistroSalidaWsService service = new RegWebRegistroSalidaWsService(wsdl);

        RegWebRegistroSalidaWs api = service.getRegWebRegistroSalidaWs();

        configAddressUserPassword(getTestAppUserName(), getTestAppPassword(), endpoint, api);

        return api;
    }

    public static RegWebInfoWs getInfoApi() throws Exception  {
        final String endpoint = getEndPoint(REGWEB_INFO);

        final URL wsdl = new URL(endpoint + "?wsdl");
        RegWebInfoWsService service = new RegWebInfoWsService(wsdl);

        RegWebInfoWs api = service.getRegWebInfoWs();

        configAddressUserPassword(getTestAppUserName(), getTestAppPassword(), endpoint, api);

        return api;
    }
    
    
    public static byte[] constructFitxerFromResource(String name) throws Exception  {
      String filename;
      if (name.startsWith("/")) {
        filename = name.substring(1);
      } else {
        filename = '/' + name; 
      }
      InputStream is = RegWebTestUtils.class.getResourceAsStream(filename);
      if (is == null) {
        return null;
      }
      try {
        return IOUtils.toByteArray(is);
      } finally {
        try {
          is.close();
        } catch (Exception e) {
        }
      }

    }

  
}
