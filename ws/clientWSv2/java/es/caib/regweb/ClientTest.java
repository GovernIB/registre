package es.caib.regweb;

import es.caib.regweb.ws.cliente.model.*;
import es.caib.regweb.ws.model.ListaResultados;
import es.caib.regweb.ws.services.regwebfacade.RegwebFacade_PortType;
import es.caib.regweb.ws.services.regwebfacade.RegwebFacadeServiceLocator;
import es.caib.regweb.ws.cliente.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * @author anadal
 * 
 */
/**
 * @author u104848
 *
 */
public class ClientTest extends TestCase {

  RegwebFacade_PortType rf;
  static final String USUARI_CONEXIO_NOM = "$helium_regweb";
  static final String USUARI_CONEXIO_CLAU = "helium_regweb";
  static final String USUARI_REGISTRE_NOM = "u104848";
  //DESENVOLUPAMENT
  static final String SERVIDOR_REGISTRE = "sdesapplin12:48080";
  // LOCAL
  //static final String SERVIDOR_REGISTRE = "epreinf123.caib.es:8081";
  /**
   * Create the test case
   * 
   * @param testName
   *          name of the test case
   */
  public ClientTest(String testName) throws Exception {
    super(testName);
    final String hostport = SERVIDOR_REGISTRE;
    RegwebFacadeServiceLocator service = new RegwebFacadeServiceLocator();
    System.out.println(" EndpointAddress: "+"http://" + hostport + "/regwebWS/services/RegwebFacade");
    service.setRegwebFacadeEndpointAddress("http://" + hostport + "/regwebWS/services/RegwebFacade");

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
      
      ct.test_buscarOficinasfisicas();
      ct.test_buscarTodosDestinatarios();
      ct.test_buscarDocumentos();
      ct.test_leerSalida();
      ct.test_leerEntrada();
      ct.test_registroEntrada1();
      ct.test_registroEntrada2();
      ct.test_registroSalida();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  
  /**
   * Llama al WS de entrada del Registro web con el campo remitente otros y procedencia geografica "Fuera de Baleares"
   * 
 * @throws Exception
 */
public void test_registroEntrada1() throws Exception {
	 
	  ParametrosRegistroEntrada paramRegistroEntrada = new ParametrosRegistroEntrada();
	  ParametrosUsuarioConexion usuConexion = new ParametrosUsuarioConexion();
	  
	  //DAtos conexion
	  usuConexion.setIdUsuario(USUARI_CONEXIO_NOM);
	  usuConexion.setPassword(USUARI_CONEXIO_CLAU);
	  
	  //Datos registro
	  paramRegistroEntrada.setUsuarioRegistro(USUARI_REGISTRE_NOM);
	  paramRegistroEntrada.setFechaEntrada(Util.getFecha());
	  paramRegistroEntrada.setHoraEntrada(Util.getHorasMinutos());
	  paramRegistroEntrada.setNumOficinaRegistro("11");
	  paramRegistroEntrada.setNumOficinaFisica("00");
	  paramRegistroEntrada.setRemitenteOtros("Prueba desde test WS");
	  paramRegistroEntrada.setOrganismoDestinatario("1100");
	  paramRegistroEntrada.setProcedenciaFueraBaleares("Mi ordenador");
	  paramRegistroEntrada.setExtractoRegistro("Esto es la prueba 1 desde el ClienTest del WS de registro de entrada.");
	  paramRegistroEntrada.setIdiomaExtracto("1");
	  paramRegistroEntrada.setFechaDocumento("01/04/2014");
	  paramRegistroEntrada.setIdiomaDocumento("1");
	  paramRegistroEntrada.setTipoDocumento("CA");
	  
	  System.out.println("=========== test_registroEntrada 1 ===========");
	  IdentificadorRegistroEntrada rtdo = ConexionRegistro.registrarEntrada(usuConexion, paramRegistroEntrada, rf);

	  assertNotNull(rtdo.getNumeroRegistro());
	  
	  System.out.println(" Registro de salida: "+rtdo.getNumeroRegistro()+"/"+rtdo.getAnyoRegistro()+ " Oficina:"+rtdo.getNumOficinaRegistro());
  }

/**
 * Llama al WS de entrada del Registro web con el campo remitente otros y procedencia geografica "Fuera de Baleares"
 * 
* @throws Exception
*/
public void test_registroEntrada2() throws Exception {
	 
	  ParametrosRegistroEntrada paramRegistroEntrada = new ParametrosRegistroEntrada();
	  
	  ParametrosUsuarioConexion usuConexion = new ParametrosUsuarioConexion();
	  
	  usuConexion.setIdUsuario(USUARI_CONEXIO_NOM);
	  usuConexion.setPassword(USUARI_CONEXIO_CLAU);
	  
	  paramRegistroEntrada.setUsuarioRegistro(USUARI_REGISTRE_NOM);
	  paramRegistroEntrada.setFechaEntrada(Util.getFecha());
	  paramRegistroEntrada.setHoraEntrada(Util.getHorasMinutos());
	  paramRegistroEntrada.setNumOficinaRegistro("16");
	  paramRegistroEntrada.setNumOficinaFisica("00");
	  paramRegistroEntrada.setRemitenteCodigo1Entidad("TRIBUNA");
	  paramRegistroEntrada.setRemitenteCodigo1EntidadCastellano("TRIBUNA");
	  paramRegistroEntrada.setRemitenteCodigo2Entidad("1");
	  paramRegistroEntrada.setOrganismoDestinatario("1100");
	  paramRegistroEntrada.setProcedenciaBaleares("40");
	  paramRegistroEntrada.setExtractoRegistro("Esto es la prueba 2 desde el ClienTest del WS de registro de entrada.");
	  paramRegistroEntrada.setIdiomaExtracto("1");
	  paramRegistroEntrada.setFechaDocumento("01/04/2014");
	  paramRegistroEntrada.setIdiomaDocumento("1");
	  paramRegistroEntrada.setTipoDocumento("CA");
	  
	  System.out.println("=========== test_registroEntrada 2 ===========");
	  IdentificadorRegistroEntrada rtdo = ConexionRegistro.registrarEntrada(usuConexion, paramRegistroEntrada, rf);

	  assertNotNull(rtdo.getNumeroRegistro());
	  
	  System.out.println(" Registro de salida: "+rtdo.getNumeroRegistro()+"/"+rtdo.getAnyoRegistro()+ " Oficina:"+rtdo.getNumOficinaRegistro());
}
 

public void test_leerEntrada() throws Exception {
	  IdentificadorRegistroEntrada idRegistro = new IdentificadorRegistroEntrada();
	  ParametrosRegistroEntrada registroEntrada = new ParametrosRegistroEntrada();
	  ParametrosUsuarioConexion usuConexion = new ParametrosUsuarioConexion();
	  String usuariolectura;
	  
	  //Datos para hacer la conexion
	  usuConexion.setIdUsuario(USUARI_CONEXIO_NOM);
	  usuConexion.setPassword(USUARI_CONEXIO_CLAU);
	  usuariolectura = USUARI_REGISTRE_NOM;
	  
	  //Identificador del registro a leer
	  idRegistro.setNumeroRegistro("1");
	  idRegistro.setNumOficinaRegistro("12");
	  idRegistro.setAnyoRegistro("2014");
	  
	  System.out.println("=========== test_leer_registro_entrada ===========");
	  registroEntrada = ConexionRegistro.leerEntrada(usuConexion, idRegistro,usuariolectura,  rf);
	  
	  System.out.println(" Extracto del registro de salida leido: '"+registroEntrada.getExtractoRegistro()+"'");
}

public void test_leerSalida() throws Exception {
	  IdentificadorRegistroSalida idRegistro = new IdentificadorRegistroSalida();
	  ParametrosRegistroSalida registroSalida = new ParametrosRegistroSalida();
	  ParametrosUsuarioConexion usuConexion = new ParametrosUsuarioConexion();
	  String usuariolectura;
	  
	  //Datos para hacer la conexion
	  usuConexion.setIdUsuario(USUARI_CONEXIO_NOM);
	  usuConexion.setPassword(USUARI_CONEXIO_CLAU);
	  usuariolectura = USUARI_REGISTRE_NOM;
	  
	  //Identificador del registro a leer
	  idRegistro.setNumeroRegistro("6");
	  idRegistro.setNumOficinaRegistro("11");
	  idRegistro.setAnyoRegistro("2014");
	  
	  System.out.println("=========== test_leer_registro_salida ===========");
	  registroSalida = ConexionRegistro.leerSalida(usuConexion, idRegistro,usuariolectura,  rf);
	  
	  System.out.println(" Extracto del registro de salida leido: '"+registroSalida.getExtractoRegistro()+"'");
}
  
  public void test_registroSalida() throws Exception {
		 
	  ParametrosRegistroSalida paramRegistroSalida = new ParametrosRegistroSalida();
	  ParametrosUsuarioConexion usuConexion = new ParametrosUsuarioConexion();
	  
	  //Datos para hacer la conexion
	  usuConexion.setIdUsuario(USUARI_CONEXIO_NOM);
	  usuConexion.setPassword(USUARI_CONEXIO_CLAU);
	  
	  //Datos del registro
	  paramRegistroSalida.setUsuarioRegistro(USUARI_REGISTRE_NOM);
	  paramRegistroSalida.setFechaSalida(Util.getFecha());
	  paramRegistroSalida.setHoraSalida(Util.getHorasMinutos());
	  paramRegistroSalida.setNumOficinaRegistro("11");
	  paramRegistroSalida.setNumOficinaFisica("00");
	  paramRegistroSalida.setDestinatarioOtros("Prueba desde test WS. Salidas");
	  paramRegistroSalida.setOrganismoEmisor("1100");
	  paramRegistroSalida.setDestinoFueraBaleares("Mi ordenador");
	  paramRegistroSalida.setExtractoRegistro("Esto es la prueba 1 desde el ClienTest del WS de registro de salida.");
	  paramRegistroSalida.setIdiomaExtracto("1");
	  paramRegistroSalida.setFechaDocumento("05/04/2014");
	  paramRegistroSalida.setIdiomaDocumento("2");
	  paramRegistroSalida.setTipoDocumento("CA");
	  
	  System.out.println("=========== test_registro_Salida ===========");
	  IdentificadorRegistroSalida rtdo = ConexionRegistro.registrarSalida(usuConexion,paramRegistroSalida, rf);

	  assertNotNull(rtdo.getNumeroRegistro());
	  
	  System.out.println(" Registro de entrada: "+rtdo.getNumeroRegistro()+"/"+rtdo.getAnyoRegistro()+ " Oficina:"+rtdo.getNumOficinaRegistro());
  }
  
  public void test_buscarTodosDestinatarios() throws Exception {
	  
    ListaResultados destList = rf.buscarTodosDestinatarios(USUARI_CONEXIO_NOM, USUARI_CONEXIO_CLAU);

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

    ListaResultados destList = rf.buscarDocumentos(USUARI_CONEXIO_NOM, USUARI_CONEXIO_CLAU);

    String[] dest = destList.getResultado();

    assertNotNull(dest);

    for (int i = 0; i < dest.length; i++) {
      System.out.println(i + ".- " + dest[i]);
    }
  }
  
  
  public void test_buscarOficinasfisicas() throws Exception {

	    System.out.println(" =========== buscar Oficinas fisicas =========== ");

	    ListaResultados oficinasFisicas = rf.buscarOficinasFisicasDescripcion(USUARI_CONEXIO_NOM, USUARI_CONEXIO_CLAU, USUARI_REGISTRE_NOM,  "CE");


	    assertNotNull(oficinasFisicas);
	    
	    String[] dest = oficinasFisicas.getResultado();

	    for (int i = 0; i < dest.length; i++) {
	      System.out.println(i + ".- " + dest[i]);
	    }
	  }
}
