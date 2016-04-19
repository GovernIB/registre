package es.caib.regweb3.ws.v3.test;

import es.caib.regweb3.ws.api.v3.*;
import es.caib.regweb3.ws.api.v3.utils.WsClientUtils;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.sql.Timestamp;


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

   //@Test
  public void obtenerRegistroEntrada() {

    try {
      RegistroEntradaResponseWs registroEntradaWs = registroEntradaApi.obtenerRegistroEntrada("PRES-E-3/2015", "mgonzalez", "EA0004518");
      System.out.printf("Idioma: " + registroEntradaWs.getIdioma() + "\n");
      System.out.printf("TipoAsunto: " + registroEntradaWs.getTipoAsunto() + "\n");
      System.out.printf("TipoTransporte: " + registroEntradaWs.getTipoTransporte() + "\n");
      System.out.printf("Oficina: " + registroEntradaWs.getOficinaCodigo() + " - " + registroEntradaWs.getOficinaDenominacion() +"\n");
      System.out.printf("Destino: " + registroEntradaWs.getDestinoCodigo() + " - " + registroEntradaWs.getDestinoDenominacion()+ "\n");
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

    
    for(int i=0;i<1;i++){


      RegistroEntradaWs registroEntradaWs = new RegistroEntradaWs();

      registroEntradaWs.setDestino(getTestDestinoCodigoDir3());
      registroEntradaWs.setOficina(getTestDestinoOficinaCodigoDir3());
      registroEntradaWs.setLibro(getTestDestinoLibro());

      registroEntradaWs.setExtracto(System.currentTimeMillis() + " probando ws");
      registroEntradaWs.setDocFisica((long) 1);
      registroEntradaWs.setIdioma("es");
      registroEntradaWs.setTipoAsunto(getTestTipoAsunto());

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


      // Interesados
      InteresadoWs interesadoWs = new InteresadoWs();

      DatosInteresadoWs interesado = new DatosInteresadoWs();
      interesado.setTipoInteresado(TIPO_INTERESADO_PERSONA_FISICA);
      interesado.setTipoDocumentoIdentificacion("N");
      interesado.setDocumento("00000001R");
      interesado.setEmail("earrigi@gmail.com");
      interesado.setNombre("Pepito");
      interesado.setApellido1("Garcia");
      interesado.setPais((long) 724);
      interesado.setProvincia((long) 46);
      interesadoWs.setInteresado(interesado);

      DatosInteresadoWs representante = new DatosInteresadoWs();
      representante.setTipoInteresado(TIPO_INTERESADO_PERSONA_FISICA); // == 3
      representante.setTipoDocumentoIdentificacion("N");
      representante.setDocumento("33456299Q");
      representante.setEmail("earrigi@gmail.com");
      representante.setNombre("Juanito");
      representante.setApellido1("De la torre");
      representante.setPais((long) 724);
      representante.setProvincia((long) 46);
      interesadoWs.setRepresentante(representante);

      registroEntradaWs.getInteresados().add(interesadoWs);

     /* InteresadoWs interesadoWs2 = new InteresadoWs();
      DatosInteresadoWs organismo = new DatosInteresadoWs();
      organismo.setTipoInteresado(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION); // == 1
      organismo.setNombre("Presidencia Govern de les Illes Balears");
      interesadoWs2.setInteresado(organismo);

      registroEntradaWs.getInteresados().add(interesadoWs2);*/

      //registroEntradaWs.getAnexos().addAll(getAnexos());

      try {
        IdentificadorWs identificadorWs = registroEntradaApi.altaRegistroEntrada(registroEntradaWs);
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

      // registroEntradaWs.getAnexos().addAll(getAnexos());

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

  //@Test
  public void crearRegistroEntradaconAnexos() {
    RegistroEntradaWs registroEntradaWs = new RegistroEntradaWs();

    registroEntradaWs.setDestino(getTestDestinoCodigoDir3());
    registroEntradaWs.setOficina(getTestDestinoOficinaCodigoDir3());
    registroEntradaWs.setLibro(getTestDestinoLibro());

    registroEntradaWs.setExtracto(System.currentTimeMillis() + " probando ws");
    registroEntradaWs.setDocFisica((long) 1);
    registroEntradaWs.setIdioma("es");
    registroEntradaWs.setTipoAsunto(getTestTipoAsunto());

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

    // Interesados
    InteresadoWs interesadoWs = new InteresadoWs();

    DatosInteresadoWs interesado = new DatosInteresadoWs();
    interesado.setTipoInteresado((long) 2);
    interesado.setNombre("Lucas");
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
    String fichero = "Extensionescompletas_Fundació_BIT.1.xlsx";
    String sfirma = "queesticfent.txt";
    // Anexo sin firma
    AnexoWs anexoWs = new AnexoWs();

    anexoWs.setTitulo("Anexo amb canvi AnexoFull");
    anexoWs.setValidezDocumento("01");
    anexoWs.setTipoDocumental(getTestAnexoTipoDocumental());
    anexoWs.setTipoDocumento("01");
    anexoWs.setOrigenCiudadanoAdmin(new Integer(0));
    anexoWs.setObservaciones("Observaciones de Marilen");

    anexoWs.setModoFirma(0);
    // Fichero Anexado
    MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
    try {


      File file = new File(getTestArchivosPath() + fichero);
      System.out.println(file.exists());
      System.out.println(file.getAbsolutePath());

      anexoWs.setFicheroAnexado(FileUtils.readFileToByteArray(file));
      anexoWs.setNombreFicheroAnexado(file.getName());
      anexoWs.setTipoMIMEFicheroAnexado(mimeTypesMap.getContentType(file));
      // anexoWs.setTamanoFicheroAnexado(file.length());

    } catch (Exception e) {
      e.printStackTrace();
    }

    anexoWs.setFechaCaptura(new Timestamp(System.currentTimeMillis()));

    registroEntradaWs.getAnexos().add(anexoWs);

        /* ANEXO CON FIRMA ATACHED   */
    AnexoWs anexoFirmaAtached = new AnexoWs();

    anexoFirmaAtached.setTitulo("Anexo firma atached");
    anexoFirmaAtached.setValidezDocumento("02");
    anexoFirmaAtached.setTipoDocumental(getTestAnexoTipoDocumental());
    anexoFirmaAtached.setTipoDocumento("02");
    anexoFirmaAtached.setOrigenCiudadanoAdmin(new Integer(1));
    anexoFirmaAtached.setObservaciones("Observaciones firma atached");
    anexoFirmaAtached.setModoFirma(1);
    // Fichero Anexado

    try {
      File file = new File(getTestArchivosPath() + fichero);

      anexoFirmaAtached.setFicheroAnexado(FileUtils.readFileToByteArray(file));
      anexoFirmaAtached.setNombreFicheroAnexado(file.getName());
      anexoFirmaAtached.setTipoMIMEFicheroAnexado(mimeTypesMap.getContentType(file));
      //anexoFirmaAtached.setsetTamanoFicheroAnexado(file.length());

    } catch (Exception e) {
      e.printStackTrace();
    }


    anexoFirmaAtached.setFechaCaptura(new Timestamp(System.currentTimeMillis()));

    registroEntradaWs.getAnexos().add(anexoFirmaAtached);
         /* FIN ANEXO CON FIRMA ATACHED   */

    /** ANEXO CON FIRMA DETACHED */
    AnexoWs anexoFirmaDetached = new AnexoWs();

    anexoFirmaDetached.setTitulo("Anexo firma detached");
    anexoFirmaDetached.setValidezDocumento("03");
    anexoFirmaDetached.setTipoDocumental(getTestAnexoTipoDocumental());
    anexoFirmaDetached.setTipoDocumento("03");
    anexoFirmaDetached.setOrigenCiudadanoAdmin(new Integer(0));
    anexoFirmaDetached.setObservaciones("Observaciones firma detached");
    anexoFirmaDetached.setModoFirma(2);
    // Fichero Anexado

    try {
      //archivo
      File file = new File(getTestArchivosPath() + fichero);

      anexoFirmaDetached.setFicheroAnexado(FileUtils.readFileToByteArray(file));
      anexoFirmaDetached.setNombreFicheroAnexado(file.getName());
      anexoFirmaDetached.setTipoMIMEFicheroAnexado(mimeTypesMap.getContentType(file));
      // anexoFirmaDetached.setTamanoFicheroAnexado(file.length());

      //firma
      File firma = new File(getTestArchivosPath() + sfirma);

      anexoFirmaDetached.setFirmaAnexada(FileUtils.readFileToByteArray(firma));
      anexoFirmaDetached.setNombreFirmaAnexada(firma.getName());
      anexoFirmaDetached.setTipoMIMEFirmaAnexada(mimeTypesMap.getContentType(firma));
      // anexoFirmaDetached.setTamanoFirmaAnexada(firma.length());

    } catch (Exception e) {
      e.printStackTrace();
    }


    anexoFirmaDetached.setFechaCaptura(new Timestamp(System.currentTimeMillis()));
    registroEntradaWs.getAnexos().add(anexoFirmaDetached);
    /** FIN ANEXO CON FIRMA DETACHED */

    /** ANEXO CON FIRMA DETACHED COPIA */
    AnexoWs anexoFirmaDetachedCopia = new AnexoWs();

    anexoFirmaDetachedCopia.setTitulo("Anexo firma detached copia");
    anexoFirmaDetachedCopia.setValidezDocumento("01");
    anexoFirmaDetachedCopia.setTipoDocumental(getTestAnexoTipoDocumental());
    anexoFirmaDetachedCopia.setTipoDocumento("01");
    anexoFirmaDetachedCopia.setOrigenCiudadanoAdmin(new Integer(0));
    anexoFirmaDetachedCopia.setObservaciones("Observaciones firma detached copia");
    anexoFirmaDetachedCopia.setModoFirma(2);
    // Fichero Anexado

    try {
      //archivo
      File file = new File(getTestArchivosPath() + fichero);

      anexoFirmaDetachedCopia.setFicheroAnexado(FileUtils.readFileToByteArray(file));
      anexoFirmaDetachedCopia.setNombreFicheroAnexado(file.getName());
      anexoFirmaDetachedCopia.setTipoMIMEFicheroAnexado(mimeTypesMap.getContentType(file));
      //anexoFirmaDetachedCopia.setTamanoFicheroAnexado(file.length());

      //firma
      File firma = new File(getTestArchivosPath() + sfirma);

      anexoFirmaDetachedCopia.setFirmaAnexada(FileUtils.readFileToByteArray(firma));
      anexoFirmaDetachedCopia.setNombreFirmaAnexada(firma.getName());
      anexoFirmaDetachedCopia.setTipoMIMEFirmaAnexada(mimeTypesMap.getContentType(firma));
      // anexoFirmaDetachedCopia.setTamanoFirmaAnexada(firma.length());

    } catch (Exception e) {
      e.printStackTrace();
    }


    anexoFirmaDetachedCopia.setFechaCaptura(new Timestamp(System.currentTimeMillis()));
    registroEntradaWs.getAnexos().add(anexoFirmaDetachedCopia);
    /** FIN ANEXO CON FIRMA DETACHED  COPIA*/


    try {
      IdentificadorWs identificadorWs = registroEntradaApi.altaRegistroEntrada(registroEntradaWs);
      System.out.println("Numero: " + identificadorWs.getNumero());
      System.out.println("Fecha: " + identificadorWs.getFecha());
    } catch (WsI18NException e) {
      e.printStackTrace();
    } catch (WsValidationException e) {
      e.printStackTrace();
    }

  }



}
