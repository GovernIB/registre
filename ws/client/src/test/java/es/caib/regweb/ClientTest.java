package es.caib.regweb;

import es.caib.regweb.ws.services.regwebfacade.ListaResultados;
import es.caib.regweb.ws.services.regwebfacade.RegwebFacade_PortType;
import es.caib.regweb.ws.services.regwebfacade.RegwebFacadeServiceLocator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * @author anadal
 * 
 */
public class ClientTest extends TestCase {

  RegwebFacade_PortType rf;

  /**
   * Create the test case
   * 
   * @param testName
   *          name of the test case
   */
  public ClientTest(String testName) throws Exception {
    super(testName);
    final String hostport = "localhost:8080";
    RegwebFacadeServiceLocator service = new RegwebFacadeServiceLocator();
    service.setRegwebFacadeEndpointAddress("http://" + hostport
        + "/regwebWS/services/RegwebFacade");

    rf = service.getRegwebFacade();
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(ClientTest.class);
  }

  public static void main(String[] args) {
    try {
      ClientTest ct = new ClientTest("Hola");
      ct.test_buscarTodosDestinatarios();
      ct.test_buscarDocumentos();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void test_buscarTodosDestinatarios() throws Exception {
    ListaResultados destList = rf.buscarTodosDestinatarios("admin", "admin");

    System.out.println(" =========== buscarTodosDestinatarios =========== ");

    String[] dest = destList.getResultado();

    assertNotNull(dest);

    assertTrue(dest.length > 0);

    for (int i = 0; i < dest.length; i++) {
      System.out.println(i + ".- " + dest[i]);
    }
  }

  public void test_buscarDocumentos() throws Exception {

    System.out.println(" =========== buscarDocumentos =========== ");

    ListaResultados destList = rf.buscarDocumentos("admin", "admin");

    String[] dest = destList.getResultado();

    assertNotNull(dest);

    for (int i = 0; i < dest.length; i++) {
      System.out.println(i + ".- " + dest[i]);
    }
  }
}
