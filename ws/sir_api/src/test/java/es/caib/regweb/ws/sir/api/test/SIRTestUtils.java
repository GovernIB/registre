package es.caib.regweb.ws.sir.api.test;

import javax.xml.ws.BindingProvider;

import es.caib.regweb.ws.sir.api.wssir6b.WS_SIR6_BServiceLocator;
import es.caib.regweb.ws.sir.api.wssir6b.WS_SIR6_B_PortType;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * @author anadal
 * 
 */
public abstract class SIRTestUtils {

  public static final String SIR_6_B = "WS_SIR6_B";

  private static Properties testProperties = new Properties();

  static {
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

  public static String getTestArchivosPath() {
    return testProperties.getProperty("test_archivos_path");
  }

  public static void configAddress(String endpoint, Object api) {

    Map<String, Object> reqContext = ((BindingProvider) api).getRequestContext();
    reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);

  }

  public static WS_SIR6_B_PortType getSir6B() throws Exception {

    final String endpoint = getEndPoint(SIR_6_B);

    WS_SIR6_BServiceLocator locator = new WS_SIR6_BServiceLocator();
    URL url = new URL(endpoint);
    WS_SIR6_B_PortType ws_sir6_b = locator.getWS_SIR6_B(url);
    return ws_sir6_b;

  }

}
