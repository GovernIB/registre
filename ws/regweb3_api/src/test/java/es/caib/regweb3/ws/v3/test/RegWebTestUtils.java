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
 * @author anadal
 */
public abstract class RegWebTestUtils implements RegwebConstantes {

    public static final String HELLO_WORLD = "RegWebHelloWorld";

    public static final String HELLO_WORLD_WITH_SECURITY = "RegWebHelloWorldWithSecurity";

    public static final String REGWEB3_PERSONAS = "RegWebPersonas";
    public static final String REGWEB3_ASIENTO_REGISTRAL = "RegWebAsientoRegistral";
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

    public static String getTestOrigenCodigoDir3() {
        return testProperties.getProperty("test_origen_codigodir3");
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

    public static Long getTestCodigoSia() {
        return new Long(testProperties.getProperty("test_codigosia"));
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


    public static RegWebPersonasWs getPersonasApi() throws Exception {
        final String endpoint = getEndPoint(REGWEB3_PERSONAS);

        final URL wsdl = new URL(endpoint + "?wsdl");
        RegWebPersonasWsService service = new RegWebPersonasWsService(wsdl);

        RegWebPersonasWs api = service.getRegWebPersonasWs();

        configAddressUserPassword(getTestAppUserName(), getTestAppPassword(), endpoint, api);

        return api;
    }

    public static RegWebAsientoRegistralWs getAsientoRegistralApi() throws Exception {
        final String endpoint = getEndPoint(REGWEB3_ASIENTO_REGISTRAL);

        final URL wsdl = new URL(endpoint + "?wsdl");
        RegWebAsientoRegistralWsService service = new RegWebAsientoRegistralWsService(wsdl);

        RegWebAsientoRegistralWs api = service.getRegWebAsientoRegistralWs();

        configAddressUserPassword(getTestAppUserName(), getTestAppPassword(), endpoint, api);

        return api;
    }

    public static RegWebRegistroEntradaWs getRegistroEntradaApi() throws Exception {
        final String endpoint = getEndPoint(REGWEB3_REGISTRO_ENTRADA);

        final URL wsdl = new URL(endpoint + "?wsdl");
        RegWebRegistroEntradaWsService service = new RegWebRegistroEntradaWsService(wsdl);

        RegWebRegistroEntradaWs api = service.getRegWebRegistroEntradaWs();

        configAddressUserPassword(getTestAppUserName(), getTestAppPassword(), endpoint, api);

        return api;
    }

    public static RegWebRegistroSalidaWs getRegistroSalidaApi() throws Exception {
        final String endpoint = getEndPoint(REGWEB3_REGISTRO_SALIDA);

        final URL wsdl = new URL(endpoint + "?wsdl");
        RegWebRegistroSalidaWsService service = new RegWebRegistroSalidaWsService(wsdl);

        RegWebRegistroSalidaWs api = service.getRegWebRegistroSalidaWs();

        configAddressUserPassword(getTestAppUserName(), getTestAppPassword(), endpoint, api);

        return api;
    }

    public static RegWebInfoWs getInfoApi() throws Exception {
        final String endpoint = getEndPoint(REGWEB3_INFO);

        final URL wsdl = new URL(endpoint + "?wsdl");
        RegWebInfoWsService service = new RegWebInfoWsService(wsdl);

        RegWebInfoWs api = service.getRegWebInfoWs();

        configAddressUserPassword(getTestAppUserName(), getTestAppPassword(), endpoint, api);

        return api;
    }

    /**
     *
     * @param tipoRegistro
     * @return
     */
    private AsientoRegistralWs getDatosComunesAsiento(Long tipoRegistro){

        AsientoRegistralWs asiento = new AsientoRegistralWs();
        asiento.setTipoRegistro(tipoRegistro);

        asiento.setAplicacion("REGWEB3");
        asiento.setAplicacionTelematica("REGWEB3");
        asiento.setCodigoAsunto(null);
        asiento.setCodigoSia(getTestCodigoSia());
        asiento.setCodigoUsuario("caibapp");
        asiento.setEntidadCodigo(getTestEntidadCodigoDir3());

        asiento.setEntidadRegistralOrigenCodigo(getTestOficinaOrigenCodigoDir3());
        asiento.setExpone(getLoremIpsum());
        asiento.setSolicita(getLoremIpsum());
        asiento.setIdioma(RegwebConstantes.IDIOMA_CATALAN_ID);
        asiento.setLibroCodigo(getTestDestinoLibro());
        asiento.setPresencial(false);
        asiento.setResumen("Regsitro de test WS");
        asiento.setUnidadTramitacionOrigenCodigo(getTestOrigenCodigoDir3());
        asiento.setUnidadTramitacionDestinoCodigo(getTestDestinoCodigoDir3());
        asiento.setTipoDocumentacionFisicaCodigo(RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC);

        asiento.setReferenciaExterna("FE4567Y");
        asiento.setNumeroExpediente("34567Y/2019");
        asiento.setTipoTransporte("01");
        asiento.setObservaciones("Asiento registral realizado mediante el api WS");

        return asiento;
    }

    /**
     *
     * @param tipoRegistro
     * @return
     */
    public AsientoRegistralWs getAsiento_to_AdministracionSir(Long tipoRegistro){

        // Datos comunes
        AsientoRegistralWs asiento = getDatosComunesAsiento(tipoRegistro);

        // Interesados
        InteresadoWs interesadoWs = new InteresadoWs();
        interesadoWs.setInteresado(getAdministracionSir());

        asiento.getInteresados().add(interesadoWs);

        return asiento;
    }

    /**
     *
     * @param tipoRegistro
     * @return
     */
    public AsientoRegistralWs getAsiento_to_AdministracionInterna(Long tipoRegistro){

        // Datos comunes
        AsientoRegistralWs asiento = getDatosComunesAsiento(tipoRegistro);

        // Interesados
        InteresadoWs interesadoWs = new InteresadoWs();
        interesadoWs.setInteresado(getAdministracionInterna());

        asiento.getInteresados().add(interesadoWs);

        return asiento;
    }

    /**
     *
     * @param tipoRegistro
     * @return
     */
    public AsientoRegistralWs getAsiento_to_AdministracionExterna(Long tipoRegistro){

        // Datos comunes
        AsientoRegistralWs asiento = getDatosComunesAsiento(tipoRegistro);

        // Interesados
        InteresadoWs interesadoWs = new InteresadoWs();
        interesadoWs.setInteresado(getAdministracionExterna());

        asiento.getInteresados().add(interesadoWs);

        return asiento;
    }

    /**
     *
     * @param tipoRegistro
     * @param representante
     * @return
     */
    public AsientoRegistralWs getAsiento_to_PersonaFisica(Long tipoRegistro, Boolean representante) {

        // Datos comunes
        AsientoRegistralWs asiento = getDatosComunesAsiento(tipoRegistro);

        // Interesados
        InteresadoWs interesadoWs = new InteresadoWs();

        // Interesado persona fisica principal
        interesadoWs.setInteresado(getPersonaFisica());

        // Representante persona fisica
        if(representante){
            interesadoWs.setRepresentante(getRepresentante(TIPO_INTERESADO_PERSONA_FISICA));
        }

        asiento.getInteresados().add(interesadoWs);

        //asientoRegistralWs.getAnexos().addAll(getAnexos());

        return asiento;
    }

    /**
     *
     * @param tipoRegistro
     * @param representante
     * @return
     */
    public AsientoRegistralWs getAsiento_to_PersonaJuridica(Long tipoRegistro, Boolean representante) {

        // Datos comunes
        AsientoRegistralWs asiento = getDatosComunesAsiento(tipoRegistro);

        // Interesados
        InteresadoWs interesadoWs = new InteresadoWs();

        // Interesado persona fisica principal
        interesadoWs.setInteresado(getPersonaJuridica());

        // Representante persona fisica
        if(representante){
            interesadoWs.setRepresentante(getRepresentante(TIPO_INTERESADO_PERSONA_FISICA));
        }

        asiento.getInteresados().add(interesadoWs);

        //asientoRegistralWs.getAnexos().addAll(getAnexos());

        return asiento;
    }

    /**
     * Retorna un interesado de tipo Persona Fisica
     * @return
     */
    public DatosInteresadoWs getPersonaFisica(){

        DatosInteresadoWs personaFisica = new DatosInteresadoWs();
        personaFisica.setTipoInteresado(TIPO_INTERESADO_PERSONA_FISICA);
        personaFisica.setTipoDocumentoIdentificacion("N");
        personaFisica.setDocumento("46164250F");
        personaFisica.setEmail("pgarcia@gmail.com");
        personaFisica.setNombre("Julian");
        personaFisica.setApellido1("González");
        personaFisica.setCanal((long) 1);
        personaFisica.setDireccion("Calle Aragón, 24, 5ºD");
        personaFisica.setLocalidad((long) 407);
        personaFisica.setPais((long) 724);
        personaFisica.setProvincia((long) 7);

        return personaFisica;
    }

    /**
     * Retorna un interesado de tipo Persona Juridica
     * @return
     */
    public DatosInteresadoWs getPersonaJuridica(){

        DatosInteresadoWs personaJuridica = new DatosInteresadoWs();
        personaJuridica.setTipoInteresado(TIPO_INTERESADO_PERSONA_JURIDICA);
        personaJuridica.setTipoDocumentoIdentificacion(String.valueOf(TIPODOCUMENTOID_CIF));
        personaJuridica.setDocumento("A42539585");
        personaJuridica.setRazonSocial("Mercadona");
        personaJuridica.setEmail("info@mercadona.es");
        personaJuridica.setPais((long) 724);
        personaJuridica.setProvincia((long) 46);

        return personaJuridica;
    }

    public DatosInteresadoWs getRepresentante(Long tipoPersona){

        DatosInteresadoWs representante = new DatosInteresadoWs();

        if(tipoPersona.equals(TIPO_INTERESADO_PERSONA_FISICA)){
            representante.setTipoInteresado(TIPO_INTERESADO_PERSONA_FISICA);
            representante.setTipoDocumentoIdentificacion(String.valueOf(TIPODOCUMENTOID_NIF));
            representante.setDocumento("33456299Q");
            representante.setEmail("jdelatorre@gmail.com");
            representante.setNombre("Juanito");
            representante.setApellido1("De la torre");
            representante.setPais((long) 724);
            representante.setProvincia((long) 46);

        }else if(tipoPersona.equals(TIPO_INTERESADO_PERSONA_JURIDICA)){
            representante.setTipoInteresado(TIPO_INTERESADO_PERSONA_JURIDICA);
            representante.setTipoDocumentoIdentificacion(String.valueOf(TIPODOCUMENTOID_CIF));
            representante.setDocumento("A42539585");
            representante.setRazonSocial("Mercadona");
            representante.setEmail("info@mercadona.es");
            representante.setPais((long) 724);
            representante.setProvincia((long) 46);
        }

        return representante;
    }

    public DatosInteresadoWs getAdministracionSir(){
        DatosInteresadoWs administracion = new DatosInteresadoWs();
        administracion.setTipoInteresado(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION); // == 1
        administracion.setTipoDocumentoIdentificacion(String.valueOf(TIPODOCUMENTOID_CODIGO_ORIGEN));
        administracion.setRazonSocial("Ayuntamiento de Jun");
        administracion.setDocumento("L01181113");

        return administracion;
    }

    public DatosInteresadoWs getAdministracionExterna(){
        DatosInteresadoWs administracion = new DatosInteresadoWs();
        administracion.setTipoInteresado(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION); // == 1
        administracion.setTipoDocumentoIdentificacion(String.valueOf(TIPODOCUMENTOID_CODIGO_ORIGEN));
        administracion.setRazonSocial("Ayuntamiento de Algaida");
        administracion.setDocumento("L01070048");

        return administracion;
    }

    public DatosInteresadoWs getAdministracionInterna(){
        DatosInteresadoWs administracion = new DatosInteresadoWs();
        administracion.setTipoInteresado(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION); // == 1
        administracion.setTipoDocumentoIdentificacion(String.valueOf(TIPODOCUMENTOID_CODIGO_ORIGEN));
        administracion.setRazonSocial("Conselleria de Presidencia y Vicepresidencia");
        administracion.setDocumento("A04015411");

        return administracion;
    }

    public String getLoremIpsum(){
        return "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum";
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
            String original = CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO.get(TIPOVALIDEZDOCUMENTO_ORIGINAL);
            anexoConFirmaAttached.setValidezDocumento(original);
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
            String original = CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO.get(TIPOVALIDEZDOCUMENTO_ORIGINAL);
            anexoConFirmaDetached.setValidezDocumento(original);
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

    public static byte[] constructFitxerFromResource(String name) throws Exception {
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
