package es.caib.regweb3.ws.v3.test;

import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.ws.api.v3.*;
import es.caib.regweb3.ws.api.v3.utils.I18NUtils;
import org.apache.commons.io.IOUtils;
import org.fundaciobit.genapp.common.utils.Utils;

import javax.xml.ws.BindingProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

/**
 * 
 * @author anadal
 * 
 */
public abstract class RegWebTestUtils implements RegwebConstantes {

  public static final String HELLO_WORLD = "RegWebHelloWorld";
  
  public static final String HELLO_WORLD_WITH_SECURITY = "RegWebHelloWorldWithSecurity";
  
  public static final String REGWEB3_PERSONAS = "RegWebPersonas";
  public static final String REGWEB3_REGISTRO_ENTRADA = "RegWebRegistroEntrada";
  public static final String REGWEB3_REGISTRO_SALIDA = "RegWebRegistroSalida";
  public static final String REGWEB3_INFO = "RegWebInfo";

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
  
  public static String getTestDestinoCodigoDir3() {
    return testProperties.getProperty("test_destinocodigodir3");
  }
  
  public static String getTestOficinaOrigenCodigoDir3() {
    return testProperties.getProperty("test_oficina_origen_codigodir3");
  }
  
  public static String getTestDestinoLibro() {
    return testProperties.getProperty("test_destinolibro");
  }
  
  public static String getTestUserName() {
    return testProperties.getProperty("test_username");
  }
  
  
  public static String getTestTipoAsunto() {
    return testProperties.getProperty("test_tipoasunto");
  }
  

  public static String getTestAnexoTipoDocumental() {
    return testProperties.getProperty("test_anexo_tipodocumental");
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
    final String endpoint = getEndPoint(REGWEB3_PERSONAS);

    final URL wsdl = new URL(endpoint + "?wsdl");
    RegWebPersonasWsService service = new RegWebPersonasWsService(wsdl);

    RegWebPersonasWs api = service.getRegWebPersonasWs();

    configAddressUserPassword(getTestAppUserName(), getTestAppPassword(), endpoint, api);

    return api;
  }

    public static RegWebRegistroEntradaWs getRegistroEntradaApi() throws Exception  {
        final String endpoint = getEndPoint(REGWEB3_REGISTRO_ENTRADA);

        final URL wsdl = new URL(endpoint + "?wsdl");
        RegWebRegistroEntradaWsService service = new RegWebRegistroEntradaWsService(wsdl);

        RegWebRegistroEntradaWs api = service.getRegWebRegistroEntradaWs();

        configAddressUserPassword(getTestAppUserName(), getTestAppPassword(), endpoint, api);

        return api;
    }

    public static RegWebRegistroSalidaWs getRegistroSalidaApi() throws Exception  {
        final String endpoint = getEndPoint(REGWEB3_REGISTRO_SALIDA);

        final URL wsdl = new URL(endpoint + "?wsdl");
        RegWebRegistroSalidaWsService service = new RegWebRegistroSalidaWsService(wsdl);

        RegWebRegistroSalidaWs api = service.getRegWebRegistroSalidaWs();

        configAddressUserPassword(getTestAppUserName(), getTestAppPassword(), endpoint, api);

        return api;
    }

    public static RegWebInfoWs getInfoApi() throws Exception  {
        final String endpoint = getEndPoint(REGWEB3_INFO);

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
    
    
    protected List<AnexoWs> getAnexos() throws Exception {

      List<AnexoWs> anexos = new ArrayList<AnexoWs>();

      // Anexo sin firma
      {
        AnexoWs anexoSinFirma = new AnexoWs();
        final String fichero = "pdf_sin_firma.pdf";
        anexoSinFirma.setTitulo("Anexo Sin Firma");
        String copia = CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO.get(TIPOVALIDEZDOCUMENTO_COPIA);
        anexoSinFirma.setValidezDocumento(copia);
        anexoSinFirma.setTipoDocumental(getTestAnexoTipoDocumental());
        String formulario = CODIGO_SICRES_BY_TIPO_DOCUMENTO.get(TIPO_DOCUMENTO_FORMULARIO);
        anexoSinFirma.setTipoDocumento(formulario);
        anexoSinFirma.setOrigenCiudadanoAdmin(ANEXO_ORIGEN_CIUDADANO);
        anexoSinFirma.setObservaciones("Observaciones de Marilen");

        anexoSinFirma.setModoFirma(MODO_FIRMA_ANEXO_SINFIRMA); // == 0
        anexoSinFirma.setFechaCaptura(new Timestamp(new Date().getTime()));

        // Fichero
        anexoSinFirma.setFicheroAnexado(RegWebTestUtils.constructFitxerFromResource(fichero));
        anexoSinFirma.setNombreFicheroAnexado(fichero);
        anexoSinFirma.setTipoMIMEFicheroAnexado(Utils.getMimeType(fichero));

        anexos.add(anexoSinFirma);
      }

      // Anexo con firma attached

      {
        AnexoWs anexoConFirmaAttached = new AnexoWs();

        final String fichero = "pdf_con_firma.pdf";
        anexoConFirmaAttached.setTitulo("Anexo Con Firma Attached");
        String copia = CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO.get(TIPOVALIDEZDOCUMENTO_COPIA);
        anexoConFirmaAttached.setValidezDocumento(copia);
        anexoConFirmaAttached.setTipoDocumental(getTestAnexoTipoDocumental());
        String formulario = CODIGO_SICRES_BY_TIPO_DOCUMENTO.get(TIPO_DOCUMENTO_FORMULARIO);
        anexoConFirmaAttached.setTipoDocumento(formulario);
        anexoConFirmaAttached.setOrigenCiudadanoAdmin(ANEXO_ORIGEN_CIUDADANO);
        anexoConFirmaAttached.setObservaciones("Observaciones de Marilen");

        anexoConFirmaAttached.setModoFirma(MODO_FIRMA_ANEXO_ATTACHED); // == 1
        anexoConFirmaAttached.setFechaCaptura(new Timestamp(new Date().getTime()));

        // Fichero con firma
        anexoConFirmaAttached.setFicheroAnexado(RegWebTestUtils
            .constructFitxerFromResource(fichero));
        anexoConFirmaAttached.setNombreFicheroAnexado(fichero);
        anexoConFirmaAttached.setTipoMIMEFicheroAnexado(Utils.getMimeType(fichero));

        anexos.add(anexoConFirmaAttached);
      }

      // Anexo con firma detached
      {
        AnexoWs anexoConFirmaDetached = new AnexoWs();

        anexoConFirmaDetached.setTitulo("Anexo Con Firma Detached");
        String copia = CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO.get(TIPOVALIDEZDOCUMENTO_COPIA);
        anexoConFirmaDetached.setValidezDocumento(copia);
        anexoConFirmaDetached.setTipoDocumental(getTestAnexoTipoDocumental());
        String formulario = CODIGO_SICRES_BY_TIPO_DOCUMENTO.get(TIPO_DOCUMENTO_FORMULARIO);
        anexoConFirmaDetached.setTipoDocumento(formulario);
        anexoConFirmaDetached.setOrigenCiudadanoAdmin(ANEXO_ORIGEN_CIUDADANO);
        anexoConFirmaDetached.setObservaciones("Observaciones de Marilen");

        anexoConFirmaDetached.setModoFirma(MODO_FIRMA_ANEXO_DETACHED); // == 2
        anexoConFirmaDetached.setFechaCaptura(new Timestamp(new Date().getTime()));

        // Fichero
        final String fichero = "xades_doc.txt";
        anexoConFirmaDetached.setFicheroAnexado(RegWebTestUtils
            .constructFitxerFromResource(fichero));
        anexoConFirmaDetached.setNombreFicheroAnexado(fichero);
        anexoConFirmaDetached.setTipoMIMEFicheroAnexado(Utils.getMimeType(fichero));

        // Firma
        final String firma = "xades_firma.xml";
        anexoConFirmaDetached
            .setFirmaAnexada(RegWebTestUtils.constructFitxerFromResource(firma));
        anexoConFirmaDetached.setNombreFirmaAnexada(firma);
        anexoConFirmaDetached.setTipoMIMEFirmaAnexada("application/xml");

        anexos.add(anexoConFirmaDetached);

      }

      return anexos;
    }

  
}
