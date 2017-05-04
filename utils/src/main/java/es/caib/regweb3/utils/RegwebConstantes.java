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
    int REGISTROS_PANTALLA_INICIO = 5;

    /*-------------- VARIABLES DE SESION --------------*/
    String SESSION_USUARIO = "usuarioAutenticado";
    String SESSION_USUARIO_ENTIDAD = "usuarioEntidadActivo";
    String SESSION_ROLES = "rolesAutenticado";
    String SESSION_ROL = "rolAutenticado";
    String SESSION_ENTIDADES = "entidades";
    String SESSION_ENTIDAD = "entidadActiva";
    String SESSION_OFICINAS = "oficinas";
    String SESSION_OFICINA = "oficinaActiva";
    String SESSION_ORGANISMOS_OFICINA = "organismosOficinaActiva";
    String SESSION_OFICINAS_ADMINISTRADAS = "oficinasAdministradas";
    String SESSION_LIBROSADMINISTRADOS = "librosAdministrados";
    String SESSION_MIGRADOS = "registrosMigrados";
    String SESSION_CONFIGURACION = "configuracion";
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
    String ROL_SUPERADMIN = "RWE_SUPERADMIN";// SuperAdministradores entidad
    Long ROL_SUPERADMIN_ID = 1L;

    String ROL_ADMIN = "RWE_ADMIN";  // Administradores entidad
    Long ROL_ADMIN_ID = 2L;

    String ROL_USUARI = "RWE_USUARI"; // Usuarios entidad
    Long ROL_USUARI_ID = 3L;

    /* -------------- ESTADO ENTIDAD --------------*/
    String ESTADO_ENTIDAD_ANULADO = "A";
    String ESTADO_ENTIDAD_EXTINGUIDO = "E";
    String ESTADO_ENTIDAD_TRANSITORIO = "T";
    String ESTADO_ENTIDAD_VIGENTE = "V";

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
    Long PERMISO_ADMINISTRACION_LIBRO = 7L;
    Long PERMISO_DISTRIBUCION_REGISTRO = 8L;
    Long PERMISO_SIR = 9L;

    Long[] PERMISOS = {
            PERMISO_REGISTRO_ENTRADA,
            PERMISO_REGISTRO_SALIDA,
            PERMISO_CONSULTA_REGISTRO_ENTRADA,
            PERMISO_CONSULTA_REGISTRO_SALIDA,
            PERMISO_MODIFICACION_REGISTRO_ENTRADA,
            PERMISO_MODIFICACION_REGISTRO_SALIDA,
            PERMISO_ADMINISTRACION_LIBRO,
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
            TIPO_INTERESADO_ADMINISTRACION,
            TIPO_INTERESADO_PERSONA_FISICA,
            TIPO_INTERESADO_PERSONA_JURIDICA
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
            TIPOVALIDEZDOCUMENTO_COPIA_COMPULSADA,
            TIPOVALIDEZDOCUMENTO_COPIA_ORIGINAL,
            TIPOVALIDEZDOCUMENTO_ORIGINAL
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
        put(TIPOVALIDEZDOCUMENTO_COPIA_COMPULSADA, "EE99");
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
    /*Long REGISTRO_ENVIADO = 6L;*/
    Long REGISTRO_TRAMITADO = 7L;
    Long REGISTRO_ANULADO = 8L;
    Long REGISTRO_RECTIFICADO = 9L;
    Long REGISTRO_RECHAZADO = 10L;

    Long[] ESTADOS_REGISTRO = {
            REGISTRO_VALIDO,
            REGISTRO_RESERVA,
            REGISTRO_PENDIENTE_VISAR,
            REGISTRO_OFICIO_EXTERNO,
            REGISTRO_OFICIO_INTERNO,
            REGISTRO_TRAMITADO,
            REGISTRO_ANULADO,
            REGISTRO_RECTIFICADO,
            REGISTRO_RECHAZADO};

    /* -------- TIPOS DE OFICIO DE REMISION ---------- */
    Long TIPO_OFICIO_REMISION_ENTRADA = 1L;
    Long TIPO_OFICIO_REMISION_SALIDA = 2L;

    Long[] TIPOS_OFICIO_REMISION = {
            TIPO_OFICIO_REMISION_ENTRADA,
            TIPO_OFICIO_REMISION_SALIDA
    };

    /* -------- ESTADO DE UN OFICIO DE REMISION ---------- */
    int OFICIO_INTERNO = 0;
    int OFICIO_EXTERNO = 1;
    int OFICIO_ACEPTADO = 2;
    int OFICIO_SIR_ENVIADO = 3;
    int OFICIO_SIR_ENVIADO_ACK = 4;
    int OFICIO_SIR_ENVIADO_ERROR = 5;
    int OFICIO_SIR_REENVIADO = 6;
    int OFICIO_SIR_REENVIADO_ACK = 7;
    int OFICIO_SIR_REENVIADO_ERROR = 8;
    int OFICIO_SIR_RECHAZADO = 9;
    int OFICIO_SIR_RECHAZADO_ACK = 10;
    int OFICIO_SIR_RECHAZADO_ERROR = 11;
    int OFICIO_SIR_DEVUELTO = 12;
    int OFICIO_ANULADO = 13;

    Integer[] ESTADOS_OFICIO_REMISION = {
            OFICIO_INTERNO,
            OFICIO_EXTERNO,
            OFICIO_ACEPTADO,
            OFICIO_SIR_ENVIADO,
            OFICIO_SIR_ENVIADO_ACK,
            OFICIO_SIR_ENVIADO_ERROR,
            OFICIO_SIR_REENVIADO,
            OFICIO_SIR_REENVIADO_ACK,
            OFICIO_SIR_REENVIADO_ERROR,
            OFICIO_SIR_RECHAZADO,
            OFICIO_SIR_RECHAZADO_ACK,
            OFICIO_SIR_RECHAZADO_ERROR,
            OFICIO_SIR_DEVUELTO,
            OFICIO_ANULADO
    };


    /* -------- TIPO TRAZABILIDAD ---------- */
    Long TRAZABILIDAD_OFICIO = 1L;
    Long TRAZABILIDAD_OFICIO_SIR = 2L;
    Long TRAZABILIDAD_RECIBIDO_SIR = 3L;
    Long TRAZABILIDAD_RECTIFICACION_ENTRADA = 4L;
    Long TRAZABILIDAD_RECTIFICACION_SALIDA = 5L;

    Long[] TIPOS_TRAZABILIDAD = {
            TRAZABILIDAD_OFICIO, TRAZABILIDAD_OFICIO_SIR, TRAZABILIDAD_RECIBIDO_SIR,
            TRAZABILIDAD_RECTIFICACION_ENTRADA, TRAZABILIDAD_RECTIFICACION_SALIDA
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

    Long[] TIPOS_FIRMA = {TIPO_FIRMA_CSV,
            TIPO_FIRMA_XADES_DETACHED_SIGNATURE,
            TIPO_FIRMA_XADES_ENVELOPE_SIGNATURE,
            TIPO_FIRMA_CADES_DETACHED_EXPLICIT_SIGNATURE,
            TIPO_FIRMA_CADES_ATTACHED_IMPLICIT_SIGNAUTRE,
            TIPO_FIRMA_PADES};

    Map<Long, String> CODIGO_NTI_BY_TIPOFIRMA = new HashMap<Long, String>() {{
        put(TIPO_FIRMA_CSV, "TF0" + TIPO_FIRMA_CSV);
        put(TIPO_FIRMA_XADES_DETACHED_SIGNATURE, "TF0" + TIPO_FIRMA_XADES_DETACHED_SIGNATURE);
        put(TIPO_FIRMA_XADES_ENVELOPE_SIGNATURE, "TF0" + TIPO_FIRMA_XADES_ENVELOPE_SIGNATURE);
        put(TIPO_FIRMA_CADES_DETACHED_EXPLICIT_SIGNATURE, "TF0" + TIPO_FIRMA_CADES_DETACHED_EXPLICIT_SIGNATURE);
        put(TIPO_FIRMA_CADES_ATTACHED_IMPLICIT_SIGNAUTRE, "TF0" + TIPO_FIRMA_CADES_ATTACHED_IMPLICIT_SIGNAUTRE);
        put(TIPO_FIRMA_PADES, "TF0" + TIPO_FIRMA_PADES);
    }};

    Map<String, Long> TIPOFIRMA_BY_CODIGO_NTI = RegwebUtils.invert(CODIGO_NTI_BY_TIPOFIRMA);


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
            TIPODOCUMENTOID_PERSONA_FISICA_ID, TIPODOCUMENTOID_CODIGO_ORIGEN_ID
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


    /* -------- TIPO DOCUMENTO ---------- */
    Long TIPO_DOCUMENTO_FORMULARIO = 1L;
    Long TIPO_DOCUMENTO_DOC_ADJUNTO = 2L;
    Long TIPO_DOCUMENTO_FICHERO_TECNICO = 3L;

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


    /* -------------- ANEXO ORIGEN -------------------------- */
    Long ANEXO_ORIGEN_CIUDADANO = 0L;
    Long ANEXO_ORIGEN_ADMINISTRACION = 1L;


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


    /* -------------- ANEXO MIME --------------*/
    int ANEXO_TIPOMIME_MAXLENGTH_SIR = 20;
    int ANEXO_NOMBREFICHERO_MAXLENGTH_SIR = 80;
    int ANEXO_OBSERVACIONES_MAXLENGTH_SIR = 50;
    int ANEXO_IDENTIFICADOR_MAXLENGTH_SIR = 50;


    /* -------------- EXPRESIÓN CRON --------------*/
    String CRON_INICIALIZAR_CONTADORES = "0 0 0 1 1 ? *";
    String CRON_ENVIOS_SIR_PENDIENTES = "0 0/2 * 1/1 * ? *"; //0 0 0/2 1/1 * ? *

    /* -------------- PLUGINS POR DEFECTO --------------*/
    String PLUGIN_DISTRIBUCION_CLASS_MOCK="es.caib.regweb3.plugins.distribucion.mock.DistribucionMockPlugin";
    String PLUGIN_JUSTIFICANTE_CLASS_MOCK="es.caib.regweb3.plugins.justificante.mock.JustificanteMockPlugin";

    /* -------------- TIPO PLUGIN --------------*/
    Long PLUGIN_CUSTODIA = 0L;
    Long PLUGIN_JUSTIFICANTE = 1L;
    Long PLUGIN_DISTRIBUCION = 2L;
    Long PLUGIN_POSTPROCESO = 3L;
    Long PLUGIN_FIRMA_SERVIDOR = 4L;
    Long PLUGIN_USER_INFORMATION = 5L;
    Long PLUGIN_SCAN = 6L;
    Long PLUGIN_CUSTODIA_JUSTIFICANTE = 7L;

    Long[] TIPOS_PLUGIN = {
            PLUGIN_CUSTODIA,
            PLUGIN_JUSTIFICANTE,
            PLUGIN_DISTRIBUCION,
            PLUGIN_POSTPROCESO,
            PLUGIN_FIRMA_SERVIDOR,
            PLUGIN_USER_INFORMATION,
            PLUGIN_SCAN,
            PLUGIN_CUSTODIA_JUSTIFICANTE
    };
}
