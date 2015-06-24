package es.caib.regweb3.ws.v3.apiaxis.test;

import es.caib.regweb3.ws.v3.apiaxis.RegWebHelloWorldWsServiceLocator;
import es.caib.regweb3.ws.v3.apiaxis.RegWebHelloWorldWs_PortType;




/**
 * 
 * @author anadal
 *
 */
public class TestApiAxis {

  
  public static void main(String[] args) {

    try {
      
      String url = "http://ibit151:8080/regweb3/ws/v3/RegWebHelloWorld";

      
      RegWebHelloWorldWsServiceLocator locator = new RegWebHelloWorldWsServiceLocator();
      locator.setRegWebHelloWorldWsEndpointAddress(url);
      RegWebHelloWorldWs_PortType service = locator.getRegWebHelloWorldWs();
      
      
      System.out.println("Versio: " +  service.getVersion());

      /*
 String url = "http://ibit151:8080/regweb3/ws/v3/RegWebHelloWorldWithSecurity";

      
 RegWebHelloWorldWithSecurityWsServiceLocator locator = new RegWebHelloWorldWithSecurityWsServiceLocator();
      locator.setRegWebHelloWorldWithSecurityWsEndpointAddress(url);
      RegWebHelloWorldWithSecurityWs_PortType service = locator.getRegWebHelloWorldWithSecurityWs();
      
      ((Stub) service)._setProperty(Call.USERNAME_PROPERTY, "caibapp");
      ((Stub) service)._setProperty(Call.PASSWORD_PROPERTY, "caibapp");
      
      System.out.println("Versio: " +  service.getVersion());
      */
      
/*
    String url = "http://localhost:8080/regweb3/ws/v3/RegWebRegistroEntrada";

    
    RegWebRegistroSalidaWsServiceLocator locator = new RegWebRegistroSalidaWsServiceLocator();
    locator.setRegWebRegistroSalidaWsEndpointAddress(url);
    RegWebRegistroSalidaWs_PortType service = locator.getRegWebRegistroSalidaWs();

    // to use Basic HTTP Authentication:
    ((Stub) service)._setProperty(Call.USERNAME_PROPERTY, "user name");
    ((Stub) service)._setProperty(Call.PASSWORD_PROPERTY, "password");
*/

    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
    }
    
    
  }
  

}