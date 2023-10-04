package es.caib.regweb3.ws.v3.test;

import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.ws.api.v3.*;
import es.caib.regweb3.ws.api.v3.utils.I18NUtils;
import org.apache.commons.io.IOUtils;
import org.fundaciobit.genapp.common.utils.Utils;
import org.fundaciobit.plugins.utils.XTrustProvider;

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
   // private static String entorno = "_localhost";
    private static String entorno = "_proves";

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


    public static void setEntorno(String entorno) {
        RegWebTestUtils.entorno = entorno;
    }

    public static String getEndPoint(String api) {
        return testProperties.getProperty("test_host" + entorno) + api;
    }

    public static String getTestAppUserName() {
        return testProperties.getProperty("test_usr" + entorno);
    }


    public static String getTestAppPassword() {
        return testProperties.getProperty("test_pwd" + entorno);
    }

    public static String getTestEntidadCodigoDir3() {
        return testProperties.getProperty("test_entidad_dir3" + entorno);
    }

    public static String getTestDestinoCodigoDir3() {
        return testProperties.getProperty("test_destino_dir3" + entorno);
    }

    public static String getTestOrigenCodigoDir3() {
        return testProperties.getProperty("test_origen_dir3" + entorno);
    }

    public static String getTestOficinaOrigenCodigoDir3() {
        return testProperties.getProperty("test_oficina_origen_dir3" + entorno);
    }

    public static String getTestDestinoLibro() {
        return testProperties.getProperty("test_libro" + entorno);
    }

    public static String getTestUserName() {
        return testProperties.getProperty("test_username" + entorno);
    }


    public static String getTestTipoAsunto() {
        return testProperties.getProperty("test_tipoasunto" + entorno);
    }


    public static String getTestAnexoTipoDocumental() {
        return testProperties.getProperty("test_anexo_tipodocumental" + entorno);
    }


    public static String getTestArchivosPath() {
        return testProperties.getProperty("test_archivos_path" + entorno);
    }

    public static void configAddressUserPassword(String usr, String pwd,
                                                 String endpoint, Object api) {

        Map<String, Object> reqContext = ((BindingProvider) api).getRequestContext();
        reqContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
        reqContext.put(BindingProvider.USERNAME_PROPERTY, usr);
        reqContext.put(BindingProvider.PASSWORD_PROPERTY, pwd);
    }

    public static Long getTestCodigoSia() {
        return new Long(testProperties.getProperty("test_codigo_sia" + entorno));
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

        if(endpoint.startsWith("https")){
            XTrustProvider.install();
        }

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

        asiento.setAplicacionTelematica("LOCAL-APP");
        asiento.setCodigoAsunto(null);
        asiento.setCodigoSia(getTestCodigoSia());
        asiento.setCodigoUsuario(getTestUserName());
        asiento.setEntidadCodigo(getTestEntidadCodigoDir3());

        asiento.setEntidadRegistralOrigenCodigo(getTestOficinaOrigenCodigoDir3());
        asiento.setExpone(getLoremIpsum());
        asiento.setSolicita(getLoremIpsum());
        asiento.setIdioma(RegwebConstantes.IDIOMA_CATALAN_ID);
        asiento.setLibroCodigo(getTestDestinoLibro());
        asiento.setPresencial(false);
        asiento.setResumen("Registro de test " + System.currentTimeMillis());
        asiento.setUnidadTramitacionOrigenCodigo(getTestOrigenCodigoDir3());
        asiento.setUnidadTramitacionDestinoCodigo(getTestDestinoCodigoDir3());
        asiento.setTipoDocumentacionFisicaCodigo(RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC);

        asiento.setReferenciaExterna("FE4567Y");
        asiento.setNumeroExpediente("34567Y/2019");
        asiento.setTipoTransporte("01");
        asiento.setObservaciones("Asiento registral realizado mediante el api WS");

        List<MetadatoWs> metadatos = new ArrayList<>();
        MetadatoWs metadatoWs = new MetadatoWs();
        metadatoWs.setCampo("Metadato Asiento 1");
        metadatoWs.setValor("Valor del metadato Asiento 1 ");
        metadatoWs.setTipo(METADATO_GENERAL);

        metadatos.add(metadatoWs);
        asiento.getMetadatos().addAll(metadatos);

        return asiento;
    }

    /**
     *
     * @param tipoRegistro
     * @return
     */
    public AsientoRegistralWs getAsiento_to_AdministracionSir(Long tipoRegistro, Boolean anexos){

        // Datos comunes
        AsientoRegistralWs asiento = getDatosComunesAsiento(tipoRegistro);

        // Interesados
        InteresadoWs interesadoWs = new InteresadoWs();
        interesadoWs.setInteresado(getAdministracionSir());

        asiento.getInteresados().add(interesadoWs);

        if(anexos){
            try {
                asiento.getAnexos().addAll(getAnexos());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return asiento;
    }



    /**
     *
     * @param tipoRegistro
     * @param destino destino al que queremos enviar el registro de entrada
     * @return
     */
    private AsientoRegistralWs getDatosComunesAsientoDestino(Long tipoRegistro, String destino){

        System.out.println("Destino " + destino);
        System.out.println("Origen " + getTestOficinaOrigenCodigoDir3());

        AsientoRegistralWs asiento = getDatosComunesAsiento(tipoRegistro);
        asiento.setUnidadTramitacionDestinoCodigo(destino);


        return asiento;
    }


    /**
     * Define un interesado tipo administración dirigido a una administración externa.
     * @param tipoRegistro
     * @return
     */
    public AsientoRegistralWs getAsiento_to_AdministracionExterna(Long tipoRegistro, Boolean anexos){

        // Datos comunes
        AsientoRegistralWs asiento = getDatosComunesAsiento(tipoRegistro);

        // Interesados
        InteresadoWs interesadoWs = new InteresadoWs();
        interesadoWs.setInteresado(getAdministracionExterna());

        asiento.getInteresados().add(interesadoWs);

        if(anexos){
            try {
                asiento.getAnexos().addAll(getAnexos());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return asiento;
    }


    /**
     *
     * @param tipoRegistro
     * @return
     */
    public AsientoRegistralWs getAsiento_to_Administracion(Long tipoRegistro, Boolean anexos, String razonSocial, String documento){

        // Datos comunes
        AsientoRegistralWs asiento = getDatosComunesAsiento(tipoRegistro);

        // Interesados
        InteresadoWs interesadoWs = new InteresadoWs();
        interesadoWs.setInteresado(getAdministracion(razonSocial, documento));

        asiento.getInteresados().add(interesadoWs);

        if(anexos){
            try {
                asiento.getAnexos().addAll(getAnexos());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return asiento;
    }


     /**
     *
     * @param tipoRegistro
     * @return
     */
    public AsientoRegistralWs getAsiento_to_PersonaFisica(Long tipoRegistro, Boolean representante, Boolean anexos) {

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

        if(anexos){
            try {
                asiento.getAnexos().addAll(getAnexos());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return asiento;
    }

    /**
     *
     * @param tipoRegistro
     * @param representante
     * @return
     */
    public AsientoRegistralWs getAsiento_to_PersonaFisicaDestino(Long tipoRegistro, Boolean representante, Boolean anexos, String destino) {

        // Datos comunes
        AsientoRegistralWs asiento = getDatosComunesAsientoDestino(tipoRegistro, destino);

        // Interesados
        InteresadoWs interesadoWs = new InteresadoWs();

        // Interesado persona fisica principal
        interesadoWs.setInteresado(getPersonaFisica());

        // Representante persona fisica
        if(representante){
            interesadoWs.setRepresentante(getRepresentante(TIPO_INTERESADO_PERSONA_FISICA));
        }

        asiento.getInteresados().add(interesadoWs);

        if(anexos){
            try {
                asiento.getAnexos().addAll(getAnexos());
                asiento.setTipoDocumentacionFisicaCodigo(RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return asiento;
    }





    /**
     *
     * @param tipoRegistro
     * @param representante
     * @return
     */
    public AsientoRegistralWs getAsiento_to_PersonaJuridica(Long tipoRegistro, Boolean representante, Boolean anexos) {

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

        if(anexos){
            try {
                asiento.getAnexos().addAll(getAnexos());
                asiento.setTipoDocumentacionFisicaCodigo(RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

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
        personaFisica.setDocumento("44328254D");
        personaFisica.setEmail("mgonzalez@fundaciobit.org");
        personaFisica.setNombre("Marilen");
        personaFisica.setApellido1("González");
        personaFisica.setCanal((long) 1);
        personaFisica.setDireccion("Calle Aragón, 24, 5ºD");
        personaFisica.setLocalidad((long) 407);
        personaFisica.setPais((long) 724);
        personaFisica.setProvincia((long) 7);
        personaFisica.setCp("07010");
        personaFisica.setTelefonoMovil("678345123");
        personaFisica.setReceptorNotificaciones(false);
        personaFisica.setAvisoNotificacionSMS(true);
        personaFisica.setAvisoCorreoElectronico(true);

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
        personaJuridica.setAvisoCorreoElectronico(false);
        personaJuridica.setAvisoNotificacionSMS(false);
        personaJuridica.setReceptorNotificaciones(false);

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
            representante.setAvisoCorreoElectronico(false);
            representante.setAvisoNotificacionSMS(false);
            representante.setReceptorNotificaciones(false);

        }else if(tipoPersona.equals(TIPO_INTERESADO_PERSONA_JURIDICA)){
            representante.setTipoInteresado(TIPO_INTERESADO_PERSONA_JURIDICA);
            representante.setTipoDocumentoIdentificacion(String.valueOf(TIPODOCUMENTOID_CIF));
            representante.setDocumento("A42539585");
            representante.setRazonSocial("Mercadona");
            representante.setEmail("info@mercadona.es");
            representante.setPais((long) 724);
            representante.setProvincia((long) 46);
            representante.setAvisoCorreoElectronico(false);
            representante.setAvisoNotificacionSMS(false);
            representante.setReceptorNotificaciones(false);
        }

        return representante;
    }


    /*
      Conjunto de métodos para definir distintos interesados de tipo administración
      Se han creado para realizar las distintas pruebas de registros de salida en un entorno multientidad
    */

    /*
      Define un interesado de tipo administración integrado en SIR
     */
    public DatosInteresadoWs getAdministracionSir(){
       // return getAdministracion("Unidad Demoorve CCAA 1","A13010361");
        return getAdministracion("Ayuntamiento de Onís","L01330432");
    }


    /*
      Define un interesado de tipo administración como administración externa
     */
    public DatosInteresadoWs getAdministracionExterna(){
        return getAdministracion("Ajuntament d'Escorca","L01070197");
    }


    /*
      Define un interesado de tipo administración
     */
    public DatosInteresadoWs getAdministracion( String razonSocial, String documento){
        DatosInteresadoWs administracion = new DatosInteresadoWs();
        administracion.setTipoInteresado(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION); // == 1
        administracion.setTipoDocumentoIdentificacion(String.valueOf(TIPODOCUMENTOID_CODIGO_ORIGEN));
        administracion.setRazonSocial(razonSocial);
        administracion.setDocumento(documento);
        administracion.setReceptorNotificaciones(false);
        administracion.setAvisoCorreoElectronico(false);
        administracion.setAvisoNotificacionSMS(false);

        return administracion;
    }


    /*
      Conjunto de métodos para definir distintos organismos destino en los dos entornos de trabajo de dir3 (PRE y PRO)
      Se han creado para realizar las distintas pruebas de registros de entrada en un entorno multientidad
     */

    public String getDestinoInterno(String entorno){
        if("PRO".equals(entorno)){
            return "A04005605";//Fundación Banco de Sangre y Tejidos de Les Illes Balears
        }else{ //PRE
            return "A04031606";//Servei D'Ocupació de Les Illes Balears
        }
    }

    public String getDestinoSirMultientidad(String entorno) {

        if ("PRO".equals(entorno)) {
            return "A04013587"; //Agencia Tributaria Islas Baleares (ATIB)
        } else {
            return "A04032198"; //Agencia Tributaria Islas Baleares (ATIB)
        }
    }

    public String getDestinoNoSirMultientidad(String entorno){

        if("PRO".equals(entorno)){
            return "A04019927"; //Delegación de la Atib de Ibiza
        }else{
            return null; // No hay equivalente en pre
        }
    }

    public String getDestinoExternoSir(String entorno){

        if("PRO".equals(entorno)){
            return "L01070033"; //Ajuntament Alcúdia
        }else{
            return "L01070033"; //Ajuntament Alcúdia (tienen el mismo codigo en los 2 entornos)
        }

    }

    public String getDestinoExternoNoSir(String entorno){

        if("PRO".equals(entorno)){
            return "L01070197"; //Ajuntament d'Escorca
        }else{
            return "L01070197"; //Ajuntament d'Escorca (tienen el mismo codigo en los 2 entornos)
        }
    }

    public String getLoremIpsum(){
        return "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum";
    }


    protected List<AnexoWs> getAnexos() throws Exception {

        List<AnexoWs> anexos = new ArrayList<AnexoWs>();

        // Anexo sin firma
        {
            AnexoWs anexoSinFirma = new AnexoWs();

            //anexoSinFirma.setConfidencial(true);
            //anexoSinFirma.setHash("sdfsdfsdfsd".getBytes(StandardCharsets.UTF_8));
            //anexoSinFirma.setTamanoFichero(2157);

            final String fichero = "pdf_sin_firma.pdf";
            anexoSinFirma.setTitulo("Anexo Sin Firma");
            anexoSinFirma.setTipoDocumental(getTestAnexoTipoDocumental());
            String formulario = CODIGO_SICRES_BY_TIPO_ANEXO.get(TIPO_DOCUMENTO_FORMULARIO);
            anexoSinFirma.setTipoDocumento(formulario);
            anexoSinFirma.setOrigenCiudadanoAdmin(ANEXO_ORIGEN_CIUDADANO);
            anexoSinFirma.setObservaciones("Observacionesde anexo");

            anexoSinFirma.setModoFirma(MODO_FIRMA_ANEXO_SINFIRMA); // == 0
            anexoSinFirma.setFechaCaptura(new Timestamp(new Date().getTime()));

            // Fichero
            anexoSinFirma.setFicheroAnexado(RegWebTestUtils.constructFitxerFromResource(fichero));
            anexoSinFirma.setNombreFicheroAnexado(fichero);
            anexoSinFirma.setTipoMIMEFicheroAnexado(Utils.getMimeType(fichero));

            anexos.add(anexoSinFirma);

            List<MetadatoWs> metadatos = new ArrayList<>();
            MetadatoWs metadatoWs = new MetadatoWs();
            metadatoWs.setCampo("Metadato 1");
            metadatoWs.setValor("Valor del metadato1 ");
            metadatoWs.setTipo(METADATO_GENERAL);

            metadatos.add(metadatoWs);

            MetadatoWs metadatoWs2 = new MetadatoWs();
            metadatoWs2.setCampo("Metadato 2");
            metadatoWs2.setValor("Valor del metadato2 ");
            metadatoWs2.setTipo(METADATO_PARTICULAR);
            metadatos.add(metadatoWs2);
            anexoSinFirma.getMetadatos().addAll(metadatos);

        }

        // Anexo con firma attached

        {
            AnexoWs anexoConFirmaAttached = new AnexoWs();

            //anexoConFirmaAttached.setConfidencial(true);
            //anexoConFirmaAttached.setHash("sdfsdfsdfsd".getBytes(StandardCharsets.UTF_8));
            //anexoConFirmaAttached.setTamanoFichero(5684);

            final String fichero = "pdf_con_firma.pdf";
            anexoConFirmaAttached.setTitulo("Anexo Con Firma Attached");
            anexoConFirmaAttached.setTipoDocumental(getTestAnexoTipoDocumental());
            String formulario = CODIGO_SICRES_BY_TIPO_ANEXO.get(TIPO_DOCUMENTO_FORMULARIO);
            anexoConFirmaAttached.setTipoDocumento(formulario);
            anexoConFirmaAttached.setOrigenCiudadanoAdmin(ANEXO_ORIGEN_CIUDADANO);
            anexoConFirmaAttached.setObservaciones("Observaciones de Marilen");

            anexoConFirmaAttached.setModoFirma(MODO_FIRMA_ANEXO_ATTACHED); // == 1
            anexoConFirmaAttached.setFechaCaptura(new Timestamp(new Date().getTime()));

            // Fichero con firma
            anexoConFirmaAttached.setFicheroAnexado(RegWebTestUtils.constructFitxerFromResource(fichero));
            anexoConFirmaAttached.setNombreFicheroAnexado(fichero);
            anexoConFirmaAttached.setTipoMIMEFicheroAnexado(Utils.getMimeType(fichero));

            anexos.add(anexoConFirmaAttached);
        }

        // Anexo con firma detached
        {
            AnexoWs anexoConFirmaDetached = new AnexoWs();

            //anexoConFirmaDetached.setConfidencial(true);
            //anexoConFirmaDetached.setHash("jyuushbdcjsabdoqbkacdcnoifw".getBytes(StandardCharsets.UTF_8));
            //anexoConFirmaDetached.setTamanoFichero(3000);

            anexoConFirmaDetached.setTitulo("Anexo Con Firma Detached");
            anexoConFirmaDetached.setTipoDocumental(getTestAnexoTipoDocumental());
            String formulario = CODIGO_SICRES_BY_TIPO_ANEXO.get(TIPO_DOCUMENTO_FORMULARIO);
            anexoConFirmaDetached.setTipoDocumento(formulario);
            anexoConFirmaDetached.setOrigenCiudadanoAdmin(ANEXO_ORIGEN_CIUDADANO);
            anexoConFirmaDetached.setObservaciones("Observaciones de Marilen");

            anexoConFirmaDetached.setModoFirma(MODO_FIRMA_ANEXO_DETACHED); // == 2
            anexoConFirmaDetached.setFechaCaptura(new Timestamp(new Date().getTime()));

            // Fichero
            final String fichero = "foto.jpg";
            anexoConFirmaDetached.setFicheroAnexado(RegWebTestUtils.constructFitxerFromResource(fichero));
            anexoConFirmaDetached.setNombreFicheroAnexado(fichero);
            anexoConFirmaDetached.setTipoMIMEFicheroAnexado(Utils.getMimeType(fichero));

            // Firma
            final String firma = "2018-01-24_CAdES_Detached_foto_jpg.csig";
            anexoConFirmaDetached.setFirmaAnexada(RegWebTestUtils.constructFitxerFromResource(firma));
            anexoConFirmaDetached.setNombreFirmaAnexada(firma);
            anexoConFirmaDetached.setTipoMIMEFirmaAnexada("application/octet-stream");

            //anexos.add(anexoConFirmaDetached);

        }

        //Anexo confidencial
        /*{

            AnexoWs anexoConfidencial = new AnexoWs();

            // Campos exclusivos para un anexo confidencial
            anexoConfidencial.setConfidencial(true);
            anexoConfidencial.setHash("sdfsdfsdfsd".getBytes(StandardCharsets.UTF_8));
            anexoConfidencial.setTamanoFichero(5684);


            final String fichero = "pdf_con_firma.pdf";
            anexoConfidencial.setTitulo("Anexo Con Firma Attached");
            String original = CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO.get(TIPOVALIDEZDOCUMENTO_ORIGINAL);
            anexoConfidencial.setValidezDocumento(original);
            anexoConfidencial.setTipoDocumental(getTestAnexoTipoDocumental());
            String formulario = CODIGO_SICRES_BY_TIPO_ANEXO.get(TIPO_DOCUMENTO_FORMULARIO);
            anexoConfidencial.setTipoDocumento(formulario);
            anexoConfidencial.setOrigenCiudadanoAdmin(ANEXO_ORIGEN_CIUDADANO);
            anexoConfidencial.setObservaciones("Observaciones de Marilen");

            anexoConfidencial.setModoFirma(MODO_FIRMA_ANEXO_ATTACHED); // == 1
            anexoConfidencial.setFechaCaptura(new Timestamp(new Date().getTime()));

            // Fichero con firma
            //anexoConfidencial.setFicheroAnexado(RegWebTestUtils
             //  .constructFitxerFromResource(fichero));
            anexoConfidencial.setNombreFicheroAnexado(fichero);
            anexoConfidencial.setTipoMIMEFicheroAnexado(Utils.getMimeType(fichero));

            anexos.add(anexoConfidencial);
        }*/

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

    /**
     * Muestra por pantala el contenido de un AsientoRegistralWs
     * @param asientoRegistralWs
     */
    public void printAsientoBasico(AsientoRegistralWs asientoRegistralWs){
        System.out.println("-------------------------------------------------------------");

        System.out.println("Num. Registro: " + asientoRegistralWs.getNumeroRegistro());
        System.out.println("Num. Registro formateado: " + asientoRegistralWs.getNumeroRegistroFormateado());
        System.out.println("Fecha Registro: " + asientoRegistralWs.getFechaRegistro());

        System.out.println("");
    }

    /**
     * Muestra por pantala el contenido de un AsientoRegistralWs
     * @param asientoRegistralWs
     */
    public void printAsiento(AsientoRegistralWs asientoRegistralWs){
        System.out.println("-------------------------------------------------------------");

        System.out.println("Num. Registro: " + asientoRegistralWs.getNumeroRegistroFormateado());
        System.out.println("Tipo registro: " + asientoRegistralWs.getTipoRegistro());
        System.out.println("Fecha Registro: " + asientoRegistralWs.getFechaRegistro());
        System.out.println("Resumen: " + asientoRegistralWs.getResumen());

        printAnexosWs(asientoRegistralWs.getAnexos());
        printInteresadosWs(asientoRegistralWs.getInteresados());

        System.out.println("");
    }

    /**
     *
     * @param anexos
     */
    public void printAnexosWs(List<AnexoWs> anexos){

        System.out.println("");
        System.out.println("Total anexos: " + anexos.size());
        for (AnexoWs anexo : anexos) {
            System.out.println("");
            if(!anexo.isConfidencial()){
                System.out.println("Nombre anexo: " + anexo.getTitulo());
                System.out.println("isJustificante: " + anexo.isJustificante());
            }else{
                System.out.println("Nombre anexo confidencial: " + anexo.getTitulo());
                System.out.println("Nombre fichero confidencial: " + anexo.getNombreFicheroAnexado());
                System.out.println("Hash confidencial: " + anexo.getHash().toString());
                System.out.println("Tamaño confidencial: " + anexo.getTamanoFichero());
            }
        }
    }

    /**
     *
     * @param interesados
     */
    public void printInteresadosWs(List<InteresadoWs> interesados){

        System.out.println("");
        System.out.println("Total interesados: " + interesados.size());

        for (InteresadoWs i : interesados) {
            System.out.println("");
            System.out.println("Interesado: " + printInteresadoWs(i.getInteresado()));

            if(i.getRepresentante() != null){
                System.out.println("Representante: " +printInteresadoWs(i.getRepresentante()));

            }
        }
    }

    public String printInteresadoWs(DatosInteresadoWs i){

        if(i.getTipoInteresado().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)){
            return i.getRazonSocial() + " " + i.getDocumento();

        }else if(i.getTipoInteresado().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)){
            return i.getNombre() + " " + i.getApellido1();

        }else if(i.getTipoInteresado().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)){
            return i.getNombre() + " " + i.getApellido1();
        }

        return "";
    }


    /** METODOS PARA LA CAPA ANTIGUA
     *
     */

    /**
     *
     *
     * @return
     */
    private RegistroEntradaWs getDatosComunesRegistroEntrada(){

        RegistroEntradaWs registroEntradaWs = new RegistroEntradaWs();

        registroEntradaWs.setDestino(getTestDestinoCodigoDir3());
        registroEntradaWs.setOficina(getTestOficinaOrigenCodigoDir3());
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

        return registroEntradaWs;
    }

    /**
     *
     *
     * @param representante
     * @return
     */
    public RegistroEntradaWs getRegistroEntrada_to_PersonaFisica( Boolean representante) {

        // Datos comunes
        RegistroEntradaWs registro = getDatosComunesRegistroEntrada();

        // Interesados
        InteresadoWs interesadoWs = new InteresadoWs();

        // Interesado persona fisica principal
        interesadoWs.setInteresado(getPersonaFisica());

        // Representante persona fisica
        if(representante){
            interesadoWs.setRepresentante(getRepresentante(TIPO_INTERESADO_PERSONA_FISICA));
        }

        registro.getInteresados().add(interesadoWs);

        try {
            registro.getAnexos().addAll(getAnexos());
        }catch (Exception e){
            e.printStackTrace();
        }

        return registro;
    }

    /**
     * Muestra por pantala el contenido de un AsientoRegistralWs
     * @param identificadorWs
     */
    public void printIdentificadorWSBasico(IdentificadorWs identificadorWs){
        System.out.println("-------------------------------------------------------------");

        System.out.println("Num. Registro: " + identificadorWs.getNumero());
        System.out.println("Num. Registro formateado: " + identificadorWs.getNumeroRegistroFormateado());
        System.out.println("Fecha Registro: " + identificadorWs.getFecha());

        System.out.println("");
    }


    Timestamp setDate(int day, int month, int year){

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, day);
        cal.set(Calendar.YEAR, year);


        return new Timestamp(cal.getTime().getTime());
    }

}
