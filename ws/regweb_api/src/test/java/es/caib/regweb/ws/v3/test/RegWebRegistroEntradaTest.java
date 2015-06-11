package es.caib.regweb.ws.v3.test;

import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.ws.api.v3.*;
import es.caib.regweb.ws.api.v3.utils.WsClientUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Created by earrivi on 4/11/14.
 * @author anadal
 */
public class RegWebRegistroEntradaTest extends RegWebTestUtils  {

  protected static RegWebRegistroEntradaWs registroEntradaApi;

  /**
   * S'executa una vegada abans de l'execució de tots els tests d'aquesta classe
   *
   * @throws Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    registroEntradaApi = getRegistroEntradaApi();
  }

  //@Test
  public void testVersio() throws Exception {
    String version = registroEntradaApi.getVersion();
    if (version.indexOf('-') != -1) {
      Assert.assertEquals("3.0.0-caib", version);
    } else {
      Assert.assertEquals("3.0.0", version);
    }
  }

  //@Test
  public void testVersioWs() throws Exception {
    Assert.assertEquals(3, registroEntradaApi.getVersionWs());
  }

  // @Test
  public void obtenerRegistroEntrada() {

    try {
      RegistroEntradaWs registroEntradaWs = registroEntradaApi.obtenerRegistroEntrada(
          "ADM-E-17/2015", "earrivi", "A04006741");
      System.out.printf("Idioma: " + registroEntradaWs.getIdioma() + "\n");
      System.out.printf("TipoAsunto: " + registroEntradaWs.getTipoAsunto() + "\n");
      System.out.printf("TipoTransporte: " + registroEntradaWs.getTipoTransporte() + "\n");
      System.out.printf("Oficina: " + registroEntradaWs.getOficina() + "\n");
      System.out.printf("Destino: " + registroEntradaWs.getDestino() + "\n");
      System.out.printf("----\n");

      for (InteresadoWs interesadoWs : registroEntradaWs.getInteresados()) {
        System.out.println(interesadoWs.getInteresado().getNombre() + " "
            + interesadoWs.getInteresado().getApellido1() + " "
            + interesadoWs.getInteresado().getApellido2() + "\n");

      }

      for (AnexoWs anexoWs : registroEntradaWs.getAnexos()) {
        System.out.println(anexoWs.getTitulo() + " " + anexoWs.getTipoDocumental() + "\n");

      }

    } catch (WsI18NException e) {
      e.printStackTrace();
    } catch (WsValidationException e) {
      e.printStackTrace();
    }
  }

  // @Test
  public void tramitarRegistroEntrada() {

    try {
      registroEntradaApi.tramitarRegistroEntrada("ADMP-E-11/2014", "earrivi", "A04006741");

    } catch (WsI18NException e) {
      e.printStackTrace();
    } catch (WsValidationException e) {
      e.printStackTrace();
    }
  }

  // @Test
  public void anularRegistroEntrada() {

    try {
      registroEntradaApi.anularRegistroEntrada("ADMP-E-2/2014", "earrivi", "A04006741", true);

      registroEntradaApi.anularRegistroEntrada("ADMP-E-2/2014", "earrivi", "A04006741", false);

    } catch (WsI18NException e) {
      e.printStackTrace();
    } catch (WsValidationException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void crearRegistroEntrada() throws Exception {

    
    for(int i=0;i<15;i++){


      RegistroEntradaWs registroEntradaWs = new RegistroEntradaWs();

      registroEntradaWs.setDestino(getTestDestinoCodigoDir3());
      registroEntradaWs.setOficina(getTestDestinoOficinaCodigoDir3());
      registroEntradaWs.setLibro(getTestDestinoLibro());

      registroEntradaWs.setExtracto(System.currentTimeMillis() + " probando ws");
      registroEntradaWs.setDocFisica((long) 1);
      registroEntradaWs.setIdioma("es");
      registroEntradaWs.setTipoAsunto("TS01");

      registroEntradaWs.setAplicacion("WsTest");
      registroEntradaWs.setVersion("1");

      registroEntradaWs.setCodigoUsuario(getTestUserName());
      registroEntradaWs.setContactoUsuario("earrivi@fundaciobit.org");

      registroEntradaWs.setNumExpediente("");
      registroEntradaWs.setNumTransporte("");
      registroEntradaWs.setObservaciones("");

      registroEntradaWs.setRefExterna("");
      registroEntradaWs.setCodigoAsunto(null);
      registroEntradaWs.setTipoTransporte("");

      registroEntradaWs.setExpone("expone");
      registroEntradaWs.setSolicita("solicita");
      
      registroEntradaWs.setTipoAsunto(getTestTipoAsunto());

      // Interesados
      InteresadoWs interesadoWs = new InteresadoWs();

      DatosInteresadoWs interesado = new DatosInteresadoWs();
      interesado.setTipoInteresado((long) 2);
      interesado.setNombre("Jaime");
      interesado.setApellido1("Arrivi");
      interesadoWs.setInteresado(interesado);

      DatosInteresadoWs representante = new DatosInteresadoWs();
      representante.setTipoInteresado(TIPO_INTERESADO_PERSONA_JURIDICA); // == 3
      representante.setRazonSocial("McDonalds");
      interesadoWs.setRepresentante(representante);

      registroEntradaWs.getInteresados().add(interesadoWs);

      InteresadoWs interesadoWs2 = new InteresadoWs();
      DatosInteresadoWs organismo = new DatosInteresadoWs();
      organismo.setTipoInteresado(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION); // == 1
      organismo.setNombre("Presidencia Govern de les Illes Balears");
      interesadoWs2.setInteresado(organismo);

      registroEntradaWs.getInteresados().add(interesadoWs2);

      //registroEntradaWs.getAnexos().addAll(getAnexos());

      try {
        IdentificadorWs identificadorWs = registroEntradaApi
            .altaRegistroEntrada(registroEntradaWs);
        System.out.println("NumeroEntrada: " + identificadorWs.getNumero());
        System.out.println("Fecha: " + identificadorWs.getFecha());
      } catch (WsI18NException e) {
        String msg = WsClientUtils.toString(e);
        System.out.println("Error WsI18NException: " + msg);
        throw e;
      } catch (WsValidationException e) {
        String msg = WsClientUtils.toString(e);
        System.out.println("Error WsValidationException: " + msg);
        throw e;
      }
    }
  }

  // @Test
  public void crearRegistroEntradaIndra() throws Exception {

    for (int i = 0; i < 1; i++) {

      RegistroEntradaWs registroEntradaWs = new RegistroEntradaWs();

      registroEntradaWs.setDestino("A04009204");
      registroEntradaWs.setOficina("O00015093");
      registroEntradaWs.setLibro("PRES");

      registroEntradaWs.setExtracto("probando ws");
      registroEntradaWs.setDocFisica((long) 1);
      registroEntradaWs.setIdioma("es");
      registroEntradaWs.setTipoAsunto("CONTR");

      registroEntradaWs.setAplicacion("WsTest");
      registroEntradaWs.setVersion("1");

      registroEntradaWs.setCodigoUsuario("indraapp");
      registroEntradaWs.setContactoUsuario("");

      registroEntradaWs.setNumExpediente("");
      registroEntradaWs.setNumTransporte("");
      registroEntradaWs.setObservaciones("");

      registroEntradaWs.setRefExterna("");
      registroEntradaWs.setCodigoAsunto(null);
      registroEntradaWs.setTipoTransporte("");

      registroEntradaWs.setExpone("expone");
      registroEntradaWs.setSolicita("solicita");

      // Interesados
      InteresadoWs interesadoWs = new InteresadoWs();

      DatosInteresadoWs interesado = new DatosInteresadoWs();
      interesado.setTipoInteresado((long) 2);
      interesado.setNombre("Gerardo");
      interesado.setApellido1("Martinez");
      interesadoWs.setInteresado(interesado);

      DatosInteresadoWs representante = new DatosInteresadoWs();
      representante.setTipoInteresado((long) 3);
      representante.setRazonSocial("Endesa");
      interesadoWs.setRepresentante(representante);

      registroEntradaWs.getInteresados().add(interesadoWs);

      InteresadoWs interesadoWs2 = new InteresadoWs();
      DatosInteresadoWs organismo = new DatosInteresadoWs();
      organismo.setTipoInteresado((long) 1);
      organismo.setNombre("Presidencia Govern de les Illes Balears");
      interesadoWs2.setInteresado(organismo);

      registroEntradaWs.getInteresados().add(interesadoWs2);

      // Anexos
      registroEntradaWs.getAnexos().addAll(getAnexos());

      try {
        IdentificadorWs identificadorWs = registroEntradaApi
            .altaRegistroEntrada(registroEntradaWs);
        System.out.println("NumeroEntrada: " + identificadorWs.getNumero());
        System.out.println("Fecha: " + identificadorWs.getFecha());
      } catch (WsI18NException e) {
        String msg = WsClientUtils.toString(e);
        System.out.println("Error WsI18NException: " + msg);
        throw e;
      } catch (WsValidationException e) {
        String msg = WsClientUtils.toString(e);
        System.out.println("Error WsValidationException: " + msg);
        throw e;
      }

    }

  }



}
