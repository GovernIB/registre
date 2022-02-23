package es.caib.regweb3.utils;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal
 *         Date: 6/02/14
 */
public interface RegwebConstantes {

    /*--------------- SISTEMA --------------*/
    String REGWEB3_PROPERTY_BASE = "es.caib.regweb3.";
    String SECURITY_DOMAIN = "seycon";
    String ARCHIVOS_LOCATION_PROPERTY = "es.caib.regweb3.archivos.path";

    /*-------------------GENERAL-------------------*/
    String CODIGO_APLICACION = "RWE3";
    String APLICACION_NOMBRE = "REGWEB3";
    String APLICACION_EMAIL = "no_responder@regweb3.com";
    int REGISTROS_PANTALLA_INICIO = 5;

    /*-------------- VARIABLES DE SESION --------------*/
    String SESSION_LOGIN_INFO = "loginInfo";
    String SESSION_INTERESADOS_ENTRADA = "interesadosEntrada";
    String SESSION_INTERESADOS_SALIDA = "interesadosSalida";

    /*-------------- IDIOMA --------------*/
    Long IDIOMA_CATALAN_ID = 1L;
    String IDIOMA_CATALAN_CODIGO = "ca";

    Long IDIOMA_CASTELLANO_ID = 2L;
    String IDIOMA_CASTELLANO_CODIGO = "es";

    Long IDIOMA_GALLEGO_ID = 3L;
    String IDIOMA_GALLEGO_CODIGO = "gl";

    Long IDIOMA_EUSKERA_ID = 4L;
    String IDIOMA_EUSKERA_CODIGO = "eu";

    Long IDIOMA_INGLES_ID = 5L;
    String IDIOMA_INGLES_CODIGO = "en";

    Long IDIOMA_OTROS_ID = 6L;
    String IDIOMA_OTROS_CODIGO = "ot";

    Long[] IDIOMAS_UI = {
            IDIOMA_CATALAN_ID, IDIOMA_CASTELLANO_ID
    };

    Long[] IDIOMAS_REGISTRO = {
            IDIOMA_CATALAN_ID, IDIOMA_CASTELLANO_ID,
            IDIOMA_GALLEGO_ID, IDIOMA_EUSKERA_ID,
            IDIOMA_INGLES_ID, IDIOMA_OTROS_ID
    };

    Long[] IDIOMAS_REGISTRO_ES = {
            IDIOMA_CASTELLANO_ID, IDIOMA_CATALAN_ID,
            IDIOMA_GALLEGO_ID, IDIOMA_EUSKERA_ID,
            IDIOMA_INGLES_ID, IDIOMA_OTROS_ID
    };

    Map<Long, String> CODIGO_BY_IDIOMA_ID = new HashMap<Long, String>() {{
        put(IDIOMA_CATALAN_ID, IDIOMA_CATALAN_CODIGO);
        put(IDIOMA_CASTELLANO_ID, IDIOMA_CASTELLANO_CODIGO);
        put(IDIOMA_GALLEGO_ID, IDIOMA_GALLEGO_CODIGO);
        put(IDIOMA_EUSKERA_ID, IDIOMA_EUSKERA_CODIGO);
        put(IDIOMA_INGLES_ID, IDIOMA_INGLES_CODIGO);
        put(IDIOMA_OTROS_ID, IDIOMA_OTROS_CODIGO);
    }};


    Map<String, Long> IDIOMA_ID_BY_CODIGO = RegwebUtils.invert(CODIGO_BY_IDIOMA_ID);


    /* -------------- ROLES --------------*/
    String RWE_SUPERADMIN = "RWE_SUPERADMIN";// SuperAdministradores entidad
    Long RWE_SUPERADMIN_ID = 1L;

    String RWE_ADMIN = "RWE_ADMIN";  // Administradores entidad
    Long RWE_ADMIN_ID = 2L;

    String RWE_USUARI = "RWE_USUARI"; // Usuarios entidad
    Long RWE_USUARI_ID = 3L;

    String RWE_WS_ENTRADA = "RWE_WS_ENTRADA"; // Rol para realizar Registros de Entrada via WS
    Long RWE_WS_ENTRADA_ID = 4L;

    String RWE_WS_SALIDA = "RWE_WS_SALIDA"; // Rol para realizar Registros de Salida via WS
    Long RWE_WS_SALIDA_ID = 5L;

    String RWE_WS_CIUDADANO = "RWE_WS_CIUDADANO"; // Rol para obtener registros de un usuario
    Long RWE_WS_CIUDADANO_ID = 6L;

    String DIB_USER_RW = "DIB_USER_RW"; // Rol para realizar copias auténticas
    Long DIB_USER_ID = 7L;


    /* -------------- ESTADO ENTIDAD --------------*/
    String ESTADO_ENTIDAD_ANULADO = "A";
    String ESTADO_ENTIDAD_EXTINGUIDO = "E";
    String ESTADO_ENTIDAD_TRANSITORIO = "T";
    String ESTADO_ENTIDAD_VIGENTE = "V";

    /* -------------- PERFIL CUSTODIA --------------*/
    Long PERFIL_CUSTODIA_DOCUMENT_CUSTODY = 1L;
    Long PERFIL_CUSTODIA_ARXIU = 2L;

    long[] PERFILES_CUSTODIA = {
            PERFIL_CUSTODIA_DOCUMENT_CUSTODY,
            PERFIL_CUSTODIA_ARXIU
    };

    /* -------------- ARXIU CAIB --------------*/
    String ARXIU_VERSION_DOC = "1.0";

    /* -------------- FORMATOS --------------*/
    String FORMATO_FECHA = "dd/MM/yyyy";
    String FORMATO_FECHA_HORA = "dd/MM/yyyy HH:mm";
    Integer ANY = 2015;

    /* -------------- PERMISOS LIBRO REGISTROS --------------*/
    Long PERMISO_REGISTRO_ENTRADA = 1L;
    Long PERMISO_REGISTRO_SALIDA = 2L;
    Long PERMISO_CONSULTA_REGISTRO_ENTRADA = 3L;
    Long PERMISO_CONSULTA_REGISTRO_SALIDA = 4L;
    Long PERMISO_MODIFICACION_REGISTRO_ENTRADA = 5L;
    Long PERMISO_MODIFICACION_REGISTRO_SALIDA = 6L;
    Long PERMISO_RESPONSABLE_OFICINA = 7L;
    Long PERMISO_DISTRIBUCION_REGISTRO = 8L;
    Long PERMISO_SIR = 9L;

    Long[] PERMISOS = {
            PERMISO_REGISTRO_ENTRADA,
            PERMISO_REGISTRO_SALIDA,
            PERMISO_CONSULTA_REGISTRO_ENTRADA,
            PERMISO_CONSULTA_REGISTRO_SALIDA,
            PERMISO_MODIFICACION_REGISTRO_ENTRADA,
            PERMISO_MODIFICACION_REGISTRO_SALIDA,
            PERMISO_RESPONSABLE_OFICINA,
            PERMISO_DISTRIBUCION_REGISTRO,
            PERMISO_SIR
    };


    /* -------------- TIPO PERSONA --------------*/
    Long TIPO_PERSONA_FISICA = 2L;
    Long TIPO_PERSONA_JURIDICA = 3L;

    Long[] TIPOS_PERSONA = {
            TIPO_PERSONA_FISICA,
            TIPO_PERSONA_JURIDICA
    };


    /* -------------- TIPO INTERESADO --------------*/
    Long TIPO_INTERESADO_ADMINISTRACION = 1L;
    Long TIPO_INTERESADO_PERSONA_FISICA = 2L;
    Long TIPO_INTERESADO_PERSONA_JURIDICA = 3L;

    Long[] TIPOS_INTERESADO = {
            TIPO_INTERESADO_PERSONA_FISICA,
            TIPO_INTERESADO_PERSONA_JURIDICA,
            TIPO_INTERESADO_ADMINISTRACION
    };
    

    /* -------------- TIPO ANOTACION --------------*/
    Long TIPO_ANOTACION_PENDIENTE = 1L;
    Long TIPO_ANOTACION_ENVIO = 2L;
    Long TIPO_ANOTACION_REENVIO = 3L;
    Long TIPO_ANOTACION_RECHAZO = 4L;

    Long[] TIPOS_ANOTACION = {
            TIPO_ANOTACION_PENDIENTE, TIPO_ANOTACION_ENVIO,
            TIPO_ANOTACION_REENVIO, TIPO_ANOTACION_RECHAZO
    };

    Map<Long, String> CODIGO_BY_TIPO_ANOTACION = new HashMap<Long, String>() {{
        put(TIPO_ANOTACION_PENDIENTE, "0" + TIPO_ANOTACION_PENDIENTE);
        put(TIPO_ANOTACION_ENVIO, "0" + TIPO_ANOTACION_ENVIO);
        put(TIPO_ANOTACION_REENVIO, "0" + TIPO_ANOTACION_REENVIO);
        put(TIPO_ANOTACION_RECHAZO, "0" + TIPO_ANOTACION_RECHAZO);
    }};

    Map<String, Long> TIPO_ANOTACION_BY_CODIGO = RegwebUtils.invert(CODIGO_BY_TIPO_ANOTACION);


    /*------------ TIPO VALIDEZ DOCUMENTAL ------------*/

    //SICRES
    Long TIPOVALIDEZDOCUMENTO_COPIA = 1L;
    Long TIPOVALIDEZDOCUMENTO_COPIA_COMPULSADA = 2L;
    Long TIPOVALIDEZDOCUMENTO_COPIA_ORIGINAL = 3L;
    Long TIPOVALIDEZDOCUMENTO_ORIGINAL = 4L;

    Long[] TIPOS_VALIDEZDOCUMENTO = {
            TIPOVALIDEZDOCUMENTO_COPIA,
            TIPOVALIDEZDOCUMENTO_COPIA_ORIGINAL,
            TIPOVALIDEZDOCUMENTO_ORIGINAL
    };

    Long[] TIPOS_VALIDEZDOCUMENTO_SCAN = {
            TIPOVALIDEZDOCUMENTO_COPIA
    };

    Long[] TIPOS_VALIDEZDOCUMENTO_SCAN_ORIGINAL = {
            TIPOVALIDEZDOCUMENTO_COPIA,
            TIPOVALIDEZDOCUMENTO_COPIA_ORIGINAL
    };


    Map<Long, String> CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO = new HashMap<Long, String>() {{
        put(TIPOVALIDEZDOCUMENTO_COPIA, "0" + TIPOVALIDEZDOCUMENTO_COPIA);
        put(TIPOVALIDEZDOCUMENTO_COPIA_COMPULSADA, "0" + TIPOVALIDEZDOCUMENTO_COPIA_COMPULSADA);
        put(TIPOVALIDEZDOCUMENTO_COPIA_ORIGINAL, "0" + TIPOVALIDEZDOCUMENTO_COPIA_ORIGINAL);
        put(TIPOVALIDEZDOCUMENTO_ORIGINAL, "0" + TIPOVALIDEZDOCUMENTO_ORIGINAL);
    }};

    Map<String, Long> TIPOVALIDEZDOCUMENTO_BY_CODIGO_SICRES
            = RegwebUtils.invert(CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO);


    Map<Long, String> CODIGO_NTI_BY_TIPOVALIDEZDOCUMENTO = new HashMap<Long, String>() {{
        put(TIPOVALIDEZDOCUMENTO_COPIA, "EE99");
        put(TIPOVALIDEZDOCUMENTO_COPIA_ORIGINAL, "EE03");
        put(TIPOVALIDEZDOCUMENTO_ORIGINAL, "EE01");
    }};

    Map<String, Long> TIPOVALIDEZDOCUMENTO_BY_CODIGO_NTI
            = RegwebUtils.invert(CODIGO_NTI_BY_TIPOVALIDEZDOCUMENTO);


    /* -------------- ESTADO REGISTRO --------------*/
    Long REGISTRO_VALIDO = 1L;
    Long REGISTRO_RESERVA = 2L;
    Long REGISTRO_PENDIENTE_VISAR = 3L;
    Long REGISTRO_OFICIO_EXTERNO = 4L;
    Long REGISTRO_OFICIO_INTERNO = 5L;
    Long REGISTRO_OFICIO_ACEPTADO = 6L;
    Long REGISTRO_DISTRIBUIDO = 7L;
    Long REGISTRO_ANULADO = 8L;
    Long REGISTRO_RECTIFICADO = 9L;
    Long REGISTRO_RECHAZADO = 10L;
    Long REGISTRO_REENVIADO = 11L;
    Long REGISTRO_DISTRIBUYENDO = 12L; //en la cola de distribución
    Long REGISTRO_OFICIO_SIR = 13L;
    Long REGISTRO_ENVIADO_NOTIFICAR = 14L; //Creado mediante NOTIB

    Long[] ESTADOS_REGISTRO = {
            REGISTRO_VALIDO,
            REGISTRO_RESERVA,
            REGISTRO_PENDIENTE_VISAR,
            REGISTRO_OFICIO_EXTERNO,
            REGISTRO_OFICIO_INTERNO,
            REGISTRO_OFICIO_SIR,
            REGISTRO_OFICIO_ACEPTADO,
            REGISTRO_DISTRIBUIDO,
            REGISTRO_ANULADO,
            REGISTRO_RECTIFICADO,
            REGISTRO_RECHAZADO,
            REGISTRO_REENVIADO,
            REGISTRO_DISTRIBUYENDO,
            REGISTRO_ENVIADO_NOTIFICAR};

    /* -------- TIPOS DE OFICIO DE REMISION ---------- */
    Long TIPO_OFICIO_REMISION_ENTRADA = 1L;
    Long TIPO_OFICIO_REMISION_SALIDA = 2L;

    Long[] TIPOS_OFICIO_REMISION = {
            TIPO_OFICIO_REMISION_ENTRADA,
            TIPO_OFICIO_REMISION_SALIDA
    };

    /* -------- ESTADO DE UN OFICIO DE REMISION ---------- */
    int OFICIO_INTERNO_ENVIADO = 0;
    int OFICIO_EXTERNO_ENVIADO = 1;
    int OFICIO_ACEPTADO = 2;
    int OFICIO_SIR_ENVIADO = 3;
    int OFICIO_SIR_ENVIADO_ACK = 4;
    int OFICIO_SIR_ENVIADO_ERROR = 5;
    int OFICIO_SIR_REENVIADO = 6;
    int OFICIO_SIR_REENVIADO_ACK = 7;
    int OFICIO_SIR_REENVIADO_ERROR = 8;
    /*int OFICIO_SIR_RECHAZADO = 9;
    int OFICIO_SIR_RECHAZADO_ACK = 10;*/
    int OFICIO_SIR_DEVUELTO = 11;
    int OFICIO_SIR_RECHAZADO = 12;
    int OFICIO_ANULADO = 13;

    Integer[] ESTADOS_OFICIO_REMISION = {
            OFICIO_ACEPTADO,
            OFICIO_INTERNO_ENVIADO,
            OFICIO_EXTERNO_ENVIADO,
            OFICIO_ANULADO
    };

    Integer[] ESTADOS_OFICIO_REMISION_SIR = {
            OFICIO_ACEPTADO,
            OFICIO_SIR_ENVIADO,
            OFICIO_SIR_ENVIADO_ACK,
            OFICIO_SIR_ENVIADO_ERROR,
            OFICIO_SIR_REENVIADO,
            OFICIO_SIR_REENVIADO_ACK,
            OFICIO_SIR_REENVIADO_ERROR,
            OFICIO_SIR_DEVUELTO,
            OFICIO_SIR_RECHAZADO
    };


    /* -------- TIPO TRAZABILIDAD ---------- */
    Long TRAZABILIDAD_OFICIO = 1L;
    Long TRAZABILIDAD_OFICIO_SIR = 2L;
    Long TRAZABILIDAD_RECIBIDO_SIR = 3L;
    Long TRAZABILIDAD_RECTIFICACION_ENTRADA = 4L;
    Long TRAZABILIDAD_RECTIFICACION_SALIDA = 5L;
    Long TRAZABILIDAD_DISTRIBUCION = 6L;

    Long[] TIPOS_TRAZABILIDAD = {
            TRAZABILIDAD_OFICIO, TRAZABILIDAD_OFICIO_SIR, TRAZABILIDAD_RECIBIDO_SIR,
            TRAZABILIDAD_RECTIFICACION_ENTRADA, TRAZABILIDAD_RECTIFICACION_SALIDA, TRAZABILIDAD_DISTRIBUCION
    };

    /* -------- TIPO TRAZABILIDAD SIR---------- */
    Long TRAZABILIDAD_SIR_RECEPCION = 1L;
    Long TRAZABILIDAD_SIR_REENVIO = 2L;
    Long TRAZABILIDAD_SIR_RECHAZO = 3L;
    Long TRAZABILIDAD_SIR_ACEPTADO = 4L;
    Long TRAZABILIDAD_SIR_RECHAZO_ORIGEN = 5L;
    Long TRAZABILIDAD_SIR_ELIMINAR = 6L;

    Long[] TIPOS_TRAZABILIDAD_SIR = {
            TRAZABILIDAD_SIR_RECEPCION, TRAZABILIDAD_SIR_REENVIO, TRAZABILIDAD_SIR_RECHAZO,
            TRAZABILIDAD_SIR_ACEPTADO, TRAZABILIDAD_SIR_RECHAZO_ORIGEN
    };

    /* -------- MENSAJE CONTROL---------- */
    String MENSAJE_CONTROL_ACK = "01";
    String MENSAJE_CONTROL_ERROR = "02";
    String MENSAJE_CONTROL_CONFIRMACION = "03";

    String[] TIPOS_MENSAJE_CONTROL = {
            MENSAJE_CONTROL_ACK, MENSAJE_CONTROL_ERROR, MENSAJE_CONTROL_CONFIRMACION
    };

    Long TIPO_COMUNICACION_ENVIADO = 1L;
    Long TIPO_COMUNICACION_RECIBIDO = 2L;

    Long[] TIPOS_COMUNICACION_MENSAJE = {
            TIPO_COMUNICACION_ENVIADO, TIPO_COMUNICACION_RECIBIDO
    };

    /* ------------- TRANSPORTE ---------------*/
    Long TRANSPORTE_SERVICIO_MENSAJEROS = 1L;
    Long TRANSPORTE_CORREO_POSTAL = 2L;
    Long TRANSPORTE_CORREO_POSTAL_CERTIFICADO = 3L;
    Long TRANSPORTE_BUROFAX = 4L;
    Long TRANSPORTE_EN_MANO = 5L;
    Long TRANSPORTE_FAX = 6L;
    Long TRANSPORTE_OTROS = 7L;

    Long[] TRANSPORTES = {
            TRANSPORTE_SERVICIO_MENSAJEROS, TRANSPORTE_CORREO_POSTAL,
            TRANSPORTE_CORREO_POSTAL_CERTIFICADO, TRANSPORTE_BUROFAX,
            TRANSPORTE_EN_MANO, TRANSPORTE_FAX, TRANSPORTE_OTROS
    };

    Map<Long, String> CODIGO_SICRES_BY_TRANSPORTE = new HashMap<Long, String>() {{
        put(TRANSPORTE_SERVICIO_MENSAJEROS, "0".concat(TRANSPORTE_SERVICIO_MENSAJEROS.toString()));
        put(TRANSPORTE_CORREO_POSTAL, "0".concat(TRANSPORTE_CORREO_POSTAL.toString()));
        put(TRANSPORTE_CORREO_POSTAL_CERTIFICADO, "0".concat(TRANSPORTE_CORREO_POSTAL_CERTIFICADO.toString()));
        put(TRANSPORTE_BUROFAX, "0".concat(TRANSPORTE_BUROFAX.toString()));
        put(TRANSPORTE_EN_MANO, "0".concat(TRANSPORTE_EN_MANO.toString()));
        put(TRANSPORTE_FAX, "0".concat(TRANSPORTE_FAX.toString()));
        put(TRANSPORTE_OTROS, "0".concat(TRANSPORTE_OTROS.toString()));
    }};

    Map<String, Long> TRANSPORTE_BY_CODIGO_SICRES = RegwebUtils.invert(CODIGO_SICRES_BY_TRANSPORTE);


    /* --------------------CONFIGURACION PERSONA ------------------------*/
    long CONFIGURACION_PERSONA_SIN_GUARDAR = 1L;
    long CONFIGURACION_PERSONA_GUARDAR_TODOS = 2L;
    long CONFIGURACION_PERSONA_CONFIRMAR_NUEVA_PERSONA = 3L;
    long CONFIGURACION_PERSONA_SISTEMA_EXTERNO = 4L;

    long[] CONFIGURACIONES_PERSONA = {
            CONFIGURACION_PERSONA_SIN_GUARDAR,
            CONFIGURACION_PERSONA_GUARDAR_TODOS,
            CONFIGURACION_PERSONA_CONFIRMAR_NUEVA_PERSONA,
            CONFIGURACION_PERSONA_SISTEMA_EXTERNO
    };


    /* -------------- DESCARGA TIPOS --------------*/
    String UNIDAD = "unidad";
    String OFICINA = "oficina";
    String CATALOGO = "catalogo";

    /* -------------- PAIS --------------*/
    Long PAIS_ESPAÑA = 724L;
    Long nivelAdminAutonomica = 2L; // Administración Autonómica
    Long comunidadBaleares = 4L; // Illes Balears

    /* -------------- ENTIDAD GEOGRÁFICA --------------*/
    String ENTIDAD_GEOGRAFICA_MUNICIPIO = "01";

    /* -------------- CANAL NOTIFICACIÓN --------------*/
    Long CANAL_DIRECCION_POSTAL = 1L;
    Long CANAL_DIRECCION_ELECTRONICA = 2L;
    Long CANAL_COMPARECENCIA_ELECTRONICA = 3L;

    /* --------------------TIPO PROPIEDAD GLOBAL ------------------------*/
    long TIPO_PROPIEDAD_GENERAL = 1L;
    long TIPO_PROPIEDAD_DIR3CAIB = 2L;
    long TIPO_PROPIEDAD_CUSTODIA = 3L;
    long TIPO_PROPIEDAD_USERINFORMATION = 4L;
    long TIPO_PROPIEDAD_SCANNER = 5L;
    long TIPO_PROPIEDAD_DISTRIBUCION = 6L;
    long TIPO_PROPIEDAD_SIR = 7L;

    long[] TIPOS_PROPIEDAD_GLOBAL = {
            TIPO_PROPIEDAD_GENERAL,
            TIPO_PROPIEDAD_DIR3CAIB,
            TIPO_PROPIEDAD_CUSTODIA,
            TIPO_PROPIEDAD_USERINFORMATION,
            TIPO_PROPIEDAD_SCANNER,
            TIPO_PROPIEDAD_DISTRIBUCION,
            TIPO_PROPIEDAD_SIR
    };


    /*--------------- TIPO FIRMA --------------*/
    Long TIPO_FIRMA_CSV = 1L;
    Long TIPO_FIRMA_XADES_DETACHED_SIGNATURE = 2L;
    Long TIPO_FIRMA_XADES_ENVELOPE_SIGNATURE = 3L;
    Long TIPO_FIRMA_CADES_DETACHED_EXPLICIT_SIGNATURE = 4L;
    Long TIPO_FIRMA_CADES_ATTACHED_IMPLICIT_SIGNAUTRE = 5L;
    Long TIPO_FIRMA_PADES = 6L;
    Long TIPO_FIRMA_SMIME = 7L; //No s'emplea
    Long TIPO_FIRMA_ODF = 8L;
    Long TIPO_FIRMA_OOXML = 9L;

    Long[] TIPOS_FIRMA = {TIPO_FIRMA_CSV,
            TIPO_FIRMA_XADES_DETACHED_SIGNATURE,
            TIPO_FIRMA_XADES_ENVELOPE_SIGNATURE,
            TIPO_FIRMA_CADES_DETACHED_EXPLICIT_SIGNATURE,
            TIPO_FIRMA_CADES_ATTACHED_IMPLICIT_SIGNAUTRE,
            TIPO_FIRMA_PADES,TIPO_FIRMA_SMIME,TIPO_FIRMA_ODF,TIPO_FIRMA_OOXML};

    Map<Long, String> CODIGO_NTI_BY_TIPOFIRMA = new HashMap<Long, String>() {{
        put(TIPO_FIRMA_CSV, "TF0" + TIPO_FIRMA_CSV);
        put(TIPO_FIRMA_XADES_DETACHED_SIGNATURE, "TF0" + TIPO_FIRMA_XADES_DETACHED_SIGNATURE);
        put(TIPO_FIRMA_XADES_ENVELOPE_SIGNATURE, "TF0" + TIPO_FIRMA_XADES_ENVELOPE_SIGNATURE);
        put(TIPO_FIRMA_CADES_DETACHED_EXPLICIT_SIGNATURE, "TF0" + TIPO_FIRMA_CADES_DETACHED_EXPLICIT_SIGNATURE);
        put(TIPO_FIRMA_CADES_ATTACHED_IMPLICIT_SIGNAUTRE, "TF0" + TIPO_FIRMA_CADES_ATTACHED_IMPLICIT_SIGNAUTRE);
        put(TIPO_FIRMA_PADES, "TF0" + TIPO_FIRMA_PADES);
        put(TIPO_FIRMA_SMIME, "TF0" + TIPO_FIRMA_SMIME);
        put(TIPO_FIRMA_ODF, "TF0" + TIPO_FIRMA_ODF);
        put(TIPO_FIRMA_OOXML, "TF0" + TIPO_FIRMA_OOXML);
    }};

    Map<String, Long> TIPOFIRMA_BY_CODIGO_NTI = RegwebUtils.invert(CODIGO_NTI_BY_TIPOFIRMA);

    /*
     PERFIL DE FIRMA ESPERADO POR RIPEA
    */
    String PERFIL_FIRMA_BES= "BES";
    String PERFIL_FIRMA_EPES="EPES";
    String PERFIL_FIRMA_LTV= "LTV";
    String PERFIL_FIRMA_T= "T";
    String PERFIL_FIRMA_C= "C";
    String PERFIL_FIRMA_X= "X";
    String PERFIL_FIRMA_XL= "XL";
    String PERFIL_FIRMA_A= "A";


    /* -------------- TIPO USUARIO --------------*/
    Long TIPO_USUARIO_PERSONA = 1L;
    Long TIPO_USUARIO_APLICACION = 2L;

    Long[] TIPOS_USUARIO = {
            TIPO_USUARIO_PERSONA, TIPO_USUARIO_APLICACION
    };


    /* -------------- ACCION LOPD --------------*/
    Long LOPD_LISTADO = 1L;
    Long LOPD_CONSULTA = 2L;
    Long LOPD_CREACION = 3L;
    Long LOPD_MODIFICACION = 4L;
    Long LOPD_JUSTIFICANTE = 5L;
    String LOPDMIGRADO_LISTADO = "LIST";
    String LOPDMIGRADO_CONSULTA = "SELECT";
    String LOPDMIGRADO_CREACION = "INSERT";
    String LOPDMIGRADO_MODIFICACION = "UPDATE";
    Long LOPD_TIPO_MIGRADO = 3L;


    /* -------------- TIPO REGISTRO -------------*/
    Long REGISTRO_ENTRADA = 1L;
    Long REGISTRO_SALIDA = 2L;
    String REGISTRO_ENTRADA_ESCRITO = "Entrada";
    String REGISTRO_SALIDA_ESCRITO = "Sortida";
    String REGISTRO_ENTRADA_ESCRITO_CASTELLANO = "Entrada";
    String REGISTRO_SALIDA_ESCRITO_CASTELLANO = "Salida";

    /* -------------- TIPO DOCUMENTO IDENTIFICACION --------------*/
    char TIPODOCUMENTOID_NIF = 'N';
    long TIPODOCUMENTOID_NIF_ID = 1;

    char TIPODOCUMENTOID_CIF = 'C';
    long TIPODOCUMENTOID_CIF_ID = 2;

    char TIPODOCUMENTOID_PASSAPORT = 'P';
    long TIPODOCUMENTOID_PASSAPORT_ID = 3;

    char TIPODOCUMENTOID_NIE = 'E';
    long TIPODOCUMENTOID_NIE_ID = 4;

    char TIPODOCUMENTOID_PERSONA_FISICA = 'X';
    long TIPODOCUMENTOID_PERSONA_FISICA_ID = 5;

    char TIPODOCUMENTOID_CODIGO_ORIGEN = 'O';
    long TIPODOCUMENTOID_CODIGO_ORIGEN_ID = 6;

    long[] TIPOS_DOCUMENTOID = {
            TIPODOCUMENTOID_NIF_ID, TIPODOCUMENTOID_CIF_ID,
            TIPODOCUMENTOID_PASSAPORT_ID, TIPODOCUMENTOID_NIE_ID,
            TIPODOCUMENTOID_PERSONA_FISICA_ID
    };

    Map<Long, Character> CODIGO_NTI_BY_TIPODOCUMENTOID = new HashMap<Long, Character>() {{
        put(TIPODOCUMENTOID_NIF_ID, TIPODOCUMENTOID_NIF);
        put(TIPODOCUMENTOID_CIF_ID, TIPODOCUMENTOID_CIF);
        put(TIPODOCUMENTOID_PASSAPORT_ID, TIPODOCUMENTOID_PASSAPORT);
        put(TIPODOCUMENTOID_NIE_ID, TIPODOCUMENTOID_NIE);
        put(TIPODOCUMENTOID_PERSONA_FISICA_ID, TIPODOCUMENTOID_PERSONA_FISICA);
        put(TIPODOCUMENTOID_CODIGO_ORIGEN_ID, TIPODOCUMENTOID_CODIGO_ORIGEN);
    }};

    Map<Character, Long> TIPODOCUMENTOID_BY_CODIGO_NTI = RegwebUtils.invert(CODIGO_NTI_BY_TIPODOCUMENTOID);


    /* -------------- CANAL NOTIFICACION --------------*/
    long CANALNOTIFICACION_DIRECCION_POSTAL = 1;
    long CANALNOTIFICACION_DIRECCION_ELECTRONICA_HABILITADA = 2;
    long CANALNOTIFICACION_COMPARECENCIA_ELECTRONICA = 3;

    long[] CANALES_NOTIFICACION = {
            CANALNOTIFICACION_DIRECCION_POSTAL,
            CANALNOTIFICACION_DIRECCION_ELECTRONICA_HABILITADA,
            CANALNOTIFICACION_COMPARECENCIA_ELECTRONICA
    };

    Map<Long, String> CODIGO_BY_CANALNOTIFICACION = new HashMap<Long, String>() {{
        put(CANALNOTIFICACION_DIRECCION_POSTAL, "0" + CANALNOTIFICACION_DIRECCION_POSTAL);
        put(CANALNOTIFICACION_DIRECCION_ELECTRONICA_HABILITADA, "0" + CANALNOTIFICACION_DIRECCION_ELECTRONICA_HABILITADA);
        put(CANALNOTIFICACION_COMPARECENCIA_ELECTRONICA, "0" + CANALNOTIFICACION_COMPARECENCIA_ELECTRONICA);
    }};

    Map<String, Long> CANALNOTIFICACION_BY_CODIGO = RegwebUtils.invert(CODIGO_BY_CANALNOTIFICACION);


    /* ----------------ANEXOS --------------------------*/
    /* SICRES 3 establece 80 max. Se quitan los 4 que emplea la extensión .ext*/
    int ANEXO_NOMBREARCHIVO_MAXLENGTH = 76;
    String ANEXO_TIPOVALIDEZDOCUMENTO_COPIA = "01";


    /* ----------------MODO FIRMA ANEXOS --------------------------*/
    int MODO_FIRMA_ANEXO_SINFIRMA = 0;
    int MODO_FIRMA_ANEXO_ATTACHED = 1; // Document amb firma adjunta
    int MODO_FIRMA_ANEXO_DETACHED = 2; // Firma en document separat

    /* -------------- ANEXO ORIGEN -------------------------- */
    Integer ANEXO_ORIGEN_CIUDADANO = 0;
    Integer ANEXO_ORIGEN_ADMINISTRACION = 1;

    /* -------------- ANEXO MIME --------------*/
    int ANEXO_TIPOMIME_MAXLENGTH_SIR = 20;
    int ANEXO_NOMBREFICHERO_MAXLENGTH_SIR = 80;
    int ANEXO_OBSERVACIONES_MAXLENGTH_SIR = 50;
    int ANEXO_IDENTIFICADOR_MAXLENGTH_SIR = 50;

    /* ---------------ANEXO LIMITACIONES SIR ----------*/
    String[] ANEXO_EXTENSIONES_SIR = new String[]{"jpg", "jpeg", "odt", "odp", "ods", "odg", "docx", "xlsx", "pptx", "pdf", "png", "rtf", "svg", "tiff", "txt", "xml", "xsig"};
    int    ANEXO_NUMEROMAX_SIR = 5;
    Long   ANEXO_TAMANOMAX_SIR = 10485760L;
    Long   ANEXO_TAMANOMAXTOTAL_SIR = 15728640L;
    String CARACTERES_NO_PERMITIDOS_SIR = ">%*&:;¿?/\\|\"!<¡#";
    char[] CARACTERES_NO_PERMITIDOS_ARXIU = {'+','>','%','*','&',':',';','¿','?','/','|','!','<','¡','\''};

    /* ---------------ANEXO ESTADOS FIRMA -------*/

    int ANEXO_FIRMA_VALIDA = 1;
    int ANEXO_FIRMA_ERROR = -1;
    int ANEXO_FIRMA_INVALIDA = -2;
    int ANEXO_FIRMA_NOINFO = 0;


    /* -------- TIPO DOCUMENTO ---------- */
    Long TIPO_DOCUMENTO_FORMULARIO = 1L;
    Long TIPO_DOCUMENTO_DOC_ADJUNTO = 2L;
    Long TIPO_DOCUMENTO_FICHERO_TECNICO = 3L;

    String TIPO_DOCUMENTO_FORMULARIO_SICRES = "01";
    String TIPO_DOCUMENTO_DOC_ADJUNTO_SICRES = "02";
    String TIPO_DOCUMENTO_FICHERO_TECNICO_SICRES = "03";

    Long[] TIPOS_DOCUMENTO = {
            TIPO_DOCUMENTO_FORMULARIO,
            TIPO_DOCUMENTO_DOC_ADJUNTO,
            TIPO_DOCUMENTO_FICHERO_TECNICO
    };

    Map<Long, String> CODIGO_SICRES_BY_TIPO_DOCUMENTO = new HashMap<Long, String>() {{
        put(TIPO_DOCUMENTO_FORMULARIO, "0" + TIPO_DOCUMENTO_FORMULARIO);
        put(TIPO_DOCUMENTO_DOC_ADJUNTO, "0" + TIPO_DOCUMENTO_DOC_ADJUNTO);
        put(TIPO_DOCUMENTO_FICHERO_TECNICO, "0" + TIPO_DOCUMENTO_FICHERO_TECNICO);
    }};

    Map<String, Long> TIPO_DOCUMENTO_BY_CODIGO_NTI
            = RegwebUtils.invert(CODIGO_SICRES_BY_TIPO_DOCUMENTO);


    /* -------- TIPO DOCUMENTACION FISICA ---------- */
    Long TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA = 1L;
    Long TIPO_DOCFISICA_ACOMPANYA_DOC_COMPLEMENTARIA = 2L;
    Long TIPO_DOCFISICA_NO_ACOMPANYA_DOC = 3L;

    Long[] TIPOS_DOCFISICA = {
            TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA,
            TIPO_DOCFISICA_ACOMPANYA_DOC_COMPLEMENTARIA,
            TIPO_DOCFISICA_NO_ACOMPANYA_DOC
    };


    /* -------- DESTINOS DE OFICIO DE REMISION ---------- */
    Long DESTINO_OFICIO_REMISION_INTERNO = 1L;
    Long DESTINO_OFICIO_REMISION_EXTERNO = 2L;

    Long[] DESTINOS_OFICIO_REMISION = {
            DESTINO_OFICIO_REMISION_INTERNO,
            DESTINO_OFICIO_REMISION_EXTERNO
    };


    /* -------------- INFORMES - TIPOS REGISTRO --------------*/
    Long INFORME_TIPO_REGISTRO_ENTRADASALIDA = 0L;
    Long INFORME_TIPO_REGISTRO_ENTRADA = 1L;
    Long INFORME_TIPO_REGISTRO_SALIDA = 2L;



    /* -------------- CAT SERVICIOS -------------------------- */
    Long OFICINA_REGISTRO = 1L;
    Long OFICINA_INFORMACION = 2L;
    Long OFICINA_TRAMITACION = 3L;
    Long REGISTRO_VIRTUAL_NO_PRESENCIAL = 4L;
    Long OFICINA_INTEGRADA_SIR = 5L;
    Long OFICINA_INTEGRADA_SIR_ENVIO = 6L;
    Long OFICINA_INTEGRADA_SIR_RECEPCION = 7L;
    Long OFICINA_INTEGRADA_060 = 8L;
    Long OFICINA_CORREOS = 9L;
    Long OFICINA_EXTRANJERIA = 10L;
    Long OFICINA_VIOLENCIA_GENERO = 11L;
    Long OFICINA_ACCESIBLE = 12L;

    /* -------- Límite expresiones en ORACLE in (:elements) (ORA-01795) ---------- */
    int NUMBER_EXPRESSIONS_IN = 1000;


    /* -------------- LOGOMENU --------------*/
    int LOGOMENU_AMPLADA_MAX = 600;
    int LOGOMENU_ALSADA_MAX = 90;


    /* -------------- OFICINA VIRTUAL --------------*/
    Boolean OFICINA_VIRTUAL_SI = true; // Las incluimos en los resultados obtenidos
    Boolean OFICINA_VIRTUAL_NO = false; // No las incluimos en los resultados obtenidos


    /* -------------- EXPRESIÓN CRON --------------*/
    String CRON_INICIALIZAR_CONTADORES = "0 0 0 1 1 ? *";
    String CRON_ENVIOS_SIR_PENDIENTES = "0 0/2 * 1/1 * ? *"; //0 0 0/2 1/1 * ? *

    /* -------------- TIPO PLUGIN --------------*/
    Long PLUGIN_CUSTODIA_ANEXOS = 0L;
    Long PLUGIN_JUSTIFICANTE = 1L;
    Long PLUGIN_DISTRIBUCION = 2L;
    Long PLUGIN_POSTPROCESO = 3L;
    Long PLUGIN_FIRMA_SERVIDOR = 4L;
    Long PLUGIN_USER_INFORMATION = 5L;
    Long PLUGIN_SCAN = 6L;
    Long PLUGIN_CUSTODIA_JUSTIFICANTE = 7L;
    Long PLUGIN_VALIDACION_FIRMAS = 8L;
    Long PLUGIN_CUSTODIA_FS_JUSTIFICANTE = 9L;
    Long PLUGIN_ARXIU_JUSTIFICANTE = 10L;

    Long[] TIPOS_PLUGIN = {
            PLUGIN_CUSTODIA_ANEXOS,
            PLUGIN_JUSTIFICANTE,
            PLUGIN_DISTRIBUCION,
            PLUGIN_POSTPROCESO,
            PLUGIN_FIRMA_SERVIDOR,
            PLUGIN_USER_INFORMATION,
            PLUGIN_SCAN,
            PLUGIN_CUSTODIA_JUSTIFICANTE,
            PLUGIN_VALIDACION_FIRMAS,
            PLUGIN_ARXIU_JUSTIFICANTE
    };

    Long[] TIPOS_PLUGIN_CAIB = {
            PLUGIN_CUSTODIA_ANEXOS,
            PLUGIN_JUSTIFICANTE,
            PLUGIN_DISTRIBUCION,
            PLUGIN_POSTPROCESO,
            PLUGIN_FIRMA_SERVIDOR,
            PLUGIN_USER_INFORMATION,
            PLUGIN_SCAN,
            PLUGIN_CUSTODIA_JUSTIFICANTE,
            PLUGIN_VALIDACION_FIRMAS,
            PLUGIN_CUSTODIA_FS_JUSTIFICANTE,
            PLUGIN_ARXIU_JUSTIFICANTE
    };

    /* -------------- TIPO INTEGRACION --------------*/
    Long INTEGRACION_CUSTODIA = 0L;
    Long INTEGRACION_FIRMA = 1L;
    Long INTEGRACION_DISTRIBUCION = 2L;
    Long INTEGRACION_WS = 3L;
    Long INTEGRACION_SIR = 4L;
    Long INTEGRACION_JUSTIFICANTE = 5L;
    Long INTEGRACION_CERRAR_EXPEDIENTE = 6L;
    Long INTEGRACION_SCHEDULERS = 7L;

    Long[] INTEGRACION_TIPOS = {
            INTEGRACION_CUSTODIA,
            INTEGRACION_JUSTIFICANTE,
            INTEGRACION_FIRMA,
            INTEGRACION_DISTRIBUCION,
            INTEGRACION_WS,
            INTEGRACION_SIR,
            INTEGRACION_SCHEDULERS
    };

    Long[] INTEGRACION_TIPOS_CAIB = {
            INTEGRACION_CUSTODIA,
            INTEGRACION_JUSTIFICANTE,
            INTEGRACION_FIRMA,
            INTEGRACION_DISTRIBUCION,
            INTEGRACION_WS,
            INTEGRACION_CERRAR_EXPEDIENTE,
            INTEGRACION_SIR,
            INTEGRACION_SCHEDULERS
    };

    /* -------------- ESTADO INTEGRACION --------------*/
    Long INTEGRACION_ESTADO_OK = 0L;
    Long INTEGRACION_ESTADO_ERROR = 1L;

    Long[] INTEGRACION_ESTADOS = {
            INTEGRACION_ESTADO_OK,
            INTEGRACION_ESTADO_ERROR
    };

    /* -------------- NOTIFICACIONES --------------*/

    Long NOTIFICACION_TIPO_COMUNICADO = 0L; // Enviado por el Administrador de entidad
    Long NOTIFICACION_TIPO_AVISO = 1L; // Enviado autoáticamente por el sistema
    Long NOTIFICACION_TIPO_ERROR = 2L; // Error

    Long[] NOTIFICACION_TIPOS = {
            NOTIFICACION_TIPO_COMUNICADO
    };

    Long NOTIFICACION_ESTADO_NUEVA = 0L;
    Long NOTIFICACION_ESTADO_LEIDA = 1L;

    Long[] NOTIFICACION_ESTADOS = {
            NOTIFICACION_ESTADO_NUEVA,
            NOTIFICACION_ESTADO_LEIDA
    };

    /* ------------ COLA -------------------------*/

    Long COLA_DISTRIBUCION = 0L;
    Long COLA_CUSTODIA = 1L;

    Long[] COLA_TIPOS = {
            COLA_DISTRIBUCION
    };

    Long[] COLA_TIPOS_CAIB = {
            COLA_DISTRIBUCION,
            COLA_CUSTODIA
    };

    /* -------------- ESTADO COLAS --------------*/
    Long COLA_ESTADO_ERROR = 1L;
    Long COLA_ESTADO_PENDIENTE = 2L;
    Long COLA_ESTADO_PROCESADO = 3L;

    Long[] COLA_ESTADOS = {
            COLA_ESTADO_ERROR,
            COLA_ESTADO_PENDIENTE,
            COLA_ESTADO_PROCESADO
    };

    String MIME_JPG  = "image/jpeg";
    String MIME_XML  = "application/xml";
    String MIME_PDF  = "application/pdf";
    String MIME_PNG  = "image/png";
    String MIME_RTF  = "text/rtf";
    String MIME_RTF2  = "application/rtf";
    String MIME_SVG  = "image/svg+xml";
    String MIME_TIFF = "image/tiff";
    String MIME_TXT  = "text/plain";

    String[] TIPOS_MIME_ACEPTADO_SIR = {MIME_JPG, MIME_XML, MIME_PDF, MIME_PNG, MIME_RTF, MIME_RTF2,
                                       MIME_SVG, MIME_TIFF, MIME_TXT};



    /*------ CODIGOS APLICACIONES INTEGRADAS SIR -------------*/
    String APLICACION_SIR_REGISTROELECTRONICO = "REC2";
    String APLICACION_SIR_DEFENSORPUEBLO = "DEFP";
    String APLICACION_SIR_ORVE = "ORVE";
    String APLICACION_SIR_ESPU = "ESPU";

    /*--------------- NOMBRES ARCHIVOS PROBLEMATICOS SIR----------------*/
    String FICHERO_REGISTROELECTRONICO = "XMLResumenSolicitudENI.xml";
    String FICHERO_DEFENSORPUEBLO = "sicres_firmado.xsig";

    /* -------------- TIPO OPERACION WS NUEVA SALIDA --------------*/
    Long TIPO_OPERACION_NOTIFICACION = 1L;
    Long TIPO_OPERACION_COMUNICACION = 2L;

    Long[] TIPOS_OPERACION = {
            TIPO_OPERACION_NOTIFICACION,
            TIPO_OPERACION_COMUNICACION
    };

    /* -------------- PRÓXIMAS EVENTO POSIBLES REGISTRO --------------*/
    Long EVENTO_PROCESADO = 0L;
    Long EVENTO_DISTRIBUIR = 1L;
    Long EVENTO_OFICIO_INTERNO = 2L;
    Long EVENTO_OFICIO_EXTERNO = 3L;
    Long EVENTO_OFICIO_SIR = 4L;

    Long[] EVENTOS_REGISTRO = {
            EVENTO_DISTRIBUIR,
            EVENTO_OFICIO_INTERNO,
            EVENTO_OFICIO_EXTERNO,
            EVENTO_OFICIO_SIR
    };

    /* -------------- ESTADOS SESIÓN --------------*/
    Long SESION_NO_INICIADA = 0L;
    Long SESION_INICIADA = 1L;
    Long SESION_FINALIZADA = 2L;
    Long SESION_ERROR = 3L;

    Long[] ESTADOS_SESION = {
            SESION_NO_INICIADA,
            SESION_INICIADA,
            SESION_FINALIZADA,
            SESION_ERROR
    };
}
