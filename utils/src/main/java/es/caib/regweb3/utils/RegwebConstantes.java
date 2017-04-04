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

    /**
     * --------------- SISTEMA --------------
     */
    public static final String REGWEB3_PROPERTY_BASE = "es.caib.regweb3.";
    public static final String SECURITY_DOMAIN = "seycon";

    /*
     *-------------------GENERAL-------------------
     */
    public static final String CODIGO_APLICACION = "RWE3";

    /**
     * -------------- VARIABLES DE SESION --------------
     */
    public static final String SESSION_USUARIO = "usuarioAutenticado";
    public static final String SESSION_USUARIO_ENTIDAD = "usuarioEntidadActivo";
    public static final String SESSION_ROLES = "rolesAutenticado";
    public static final String SESSION_ROL = "rolAutenticado";
    public static final String SESSION_ENTIDADES = "entidades";
    public static final String SESSION_ENTIDAD = "entidadActiva";
    public static final String SESSION_OFICINAS = "oficinas";
    public static final String SESSION_OFICINA = "oficinaActiva";
    public static final String SESSION_ORGANISMOS_OFICINA = "organismosOficinaActiva";
    public static final String SESSION_ORGANISMOS_SIR = "organismosSIR";
    public static final String SESSION_OFICINAS_ADMINISTRADAS = "oficinasAdministradas";
    public static final String SESSION_LIBROSADMINISTRADOS = "librosAdministrados";
    public static final String SESSION_MIGRADOS = "registrosMigrados";
    public static final String SESSION_TIENE_ASR = "tieneASR";
    public static final String SESSION_CONFIGURACION = "configuracion";
    public static final String SESSION_INTERESADOS_ENTRADA = "interesadosEntrada";
    public static final String SESSION_INTERESADOS_SALIDA = "interesadosSalida";

    /**
     * -------------- IDIOMA --------------
     */
    public static final Long IDIOMA_CATALAN_ID = 1L;
    public static final String IDIOMA_CATALAN_CODIGO = "ca";

    public static final Long IDIOMA_CASTELLANO_ID = 2L;
    public static final String IDIOMA_CASTELLANO_CODIGO = "es";

    public static final Long IDIOMA_GALLEGO_ID = 3L;
    public static final String IDIOMA_GALLEGO_CODIGO = "gl";

    public static final Long IDIOMA_EUSKERA_ID = 4L;
    public static final String IDIOMA_EUSKERA_CODIGO = "eu";

    public static final Long IDIOMA_INGLES_ID = 5L;
    public static final String IDIOMA_INGLES_CODIGO = "en";

    public static final Long IDIOMA_OTROS_ID = 6L;
    public static final String IDIOMA_OTROS_CODIGO = "ot";

    public static final Long[] IDIOMAS_UI = {
            IDIOMA_CATALAN_ID, IDIOMA_CASTELLANO_ID
    };

    public static final Long[] IDIOMAS_REGISTRO = {
            IDIOMA_CATALAN_ID, IDIOMA_CASTELLANO_ID,
            IDIOMA_GALLEGO_ID, IDIOMA_EUSKERA_ID,
            IDIOMA_INGLES_ID, IDIOMA_OTROS_ID
    };

    public static final Long[] IDIOMAS_REGISTRO_ES = {
            IDIOMA_CASTELLANO_ID, IDIOMA_CATALAN_ID,
            IDIOMA_GALLEGO_ID, IDIOMA_EUSKERA_ID,
            IDIOMA_INGLES_ID, IDIOMA_OTROS_ID
    };

    public static final Map<Long, String> CODIGO_BY_IDIOMA_ID = new HashMap<Long, String>() {{
        put(IDIOMA_CATALAN_ID, IDIOMA_CATALAN_CODIGO);
        put(IDIOMA_CASTELLANO_ID, IDIOMA_CASTELLANO_CODIGO);
        put(IDIOMA_GALLEGO_ID, IDIOMA_GALLEGO_CODIGO);
        put(IDIOMA_EUSKERA_ID, IDIOMA_EUSKERA_CODIGO);
        put(IDIOMA_INGLES_ID, IDIOMA_INGLES_CODIGO);
        put(IDIOMA_OTROS_ID, IDIOMA_OTROS_CODIGO);
    }};


    public static final Map<String, Long> IDIOMA_ID_BY_CODIGO = RegwebUtils.invert(CODIGO_BY_IDIOMA_ID);

    /* -------------- PROPERTIES DE APLICACIÓN --------------*/
    public static final String ARCHIVOS_LOCATION_PROPERTY = "es.caib.regweb3.archivos.path";

    /* -------------- ROLES --------------*/
    public static final String ROL_SUPERADMIN = "RWE_SUPERADMIN";
    public static final String ROL_ADMIN = "RWE_ADMIN";  // Administradores entidad
    public static final String ROL_USUARI = "RWE_USUARI";

    public static final Long ROL_SUPERADMIN_ID = 1L;
    public static final Long ROL_ADMIN_ID = 2L;  // Administradores entidad
    public static final Long ROL_USUARI_ID = 3L;

    /* -------------- ESTADO ENTIDAD --------------*/
    public static final String ESTADO_ENTIDAD_ANULADO = "A";
    public static final String ESTADO_ENTIDAD_EXTINGUIDO = "E";
    public static final String ESTADO_ENTIDAD_TRANSITORIO = "T";
    public static final String ESTADO_ENTIDAD_VIGENTE = "V";

    /* -------------- FORMATOS --------------*/
    public static final String FORMATO_FECHA = "dd/MM/yyyy";
    public static final String FORMATO_FECHA_HORA = "dd/MM/yyyy HH:mm";
    public static final Integer ANY = 2015;

    /* -------------- PERMISOS LIBRO REGISTROS --------------*/
    public static final Long PERMISO_REGISTRO_ENTRADA = 1L;
    public static final Long PERMISO_REGISTRO_SALIDA = 2L;
    public static final Long PERMISO_CONSULTA_REGISTRO_ENTRADA = 3L;
    public static final Long PERMISO_CONSULTA_REGISTRO_SALIDA = 4L;
    public static final Long PERMISO_MODIFICACION_REGISTRO_ENTRADA = 5L;
    public static final Long PERMISO_MODIFICACION_REGISTRO_SALIDA = 6L;
    public static final Long PERMISO_ADMINISTRACION_LIBRO = 7L;
    public static final Long PERMISO_DISTRIBUCION_REGISTRO = 8L;
    public static final Long PERMISO_SIR = 9L;


    public static final Long[] PERMISOS = {
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
    public static final Long TIPO_PERSONA_FISICA = 2L;
    public static final Long TIPO_PERSONA_JURIDICA = 3L;

    public static final Long[] TIPOS_PERSONA = {
            TIPO_PERSONA_FISICA,
            TIPO_PERSONA_JURIDICA
    };


    /* -------------- TIPO INTERESADO --------------*/
    public static final Long TIPO_INTERESADO_ADMINISTRACION = 1L;
    public static final Long TIPO_INTERESADO_PERSONA_FISICA = 2L;
    public static final Long TIPO_INTERESADO_PERSONA_JURIDICA = 3L;

    public static final Long[] TIPOS_INTERESADO = {
            TIPO_INTERESADO_ADMINISTRACION,
            TIPO_INTERESADO_PERSONA_FISICA,
            TIPO_INTERESADO_PERSONA_JURIDICA
    };
    
    
    
    /* -------------- TIPO ANOTACION --------------*/

    public static final Long TIPO_ANOTACION_PENDIENTE = 1L;
    public static final Long TIPO_ANOTACION_ENVIO = 2L;
    public static final Long TIPO_ANOTACION_REENVIO = 3L;
    public static final Long TIPO_ANOTACION_RECHAZO = 4L;


    public static final Long[] TIPOS_ANOTACION = {
            TIPO_ANOTACION_PENDIENTE, TIPO_ANOTACION_ENVIO,
            TIPO_ANOTACION_REENVIO, TIPO_ANOTACION_RECHAZO
    };


    public static final Map<Long, String> CODIGO_BY_TIPO_ANOTACION = new HashMap<Long, String>() {{
        put(TIPO_ANOTACION_PENDIENTE, "0" + TIPO_ANOTACION_PENDIENTE);
        put(TIPO_ANOTACION_ENVIO, "0" + TIPO_ANOTACION_ENVIO);
        put(TIPO_ANOTACION_REENVIO, "0" + TIPO_ANOTACION_REENVIO);
        put(TIPO_ANOTACION_RECHAZO, "0" + TIPO_ANOTACION_RECHAZO);
    }};


    public static final Map<String, Long> TIPO_ANOTACION_BY_CODIGO = RegwebUtils.invert(CODIGO_BY_TIPO_ANOTACION);


    /**
     * ------------ TIPO VALIDEZ DOCUMENTAL ------------
     */

    // NTI    SICRES

    public static final Long TIPOVALIDEZDOCUMENTO_COPIA = 1L;
    public static final Long TIPOVALIDEZDOCUMENTO_COPIA_COMPULSADA = 2L;
    public static final Long TIPOVALIDEZDOCUMENTO_COPIA_ORIGINAL = 3L;
    public static final Long TIPOVALIDEZDOCUMENTO_ORIGINAL = 4L;

    public static final Long[] TIPOS_VALIDEZDOCUMENTO = {
            TIPOVALIDEZDOCUMENTO_COPIA,
            TIPOVALIDEZDOCUMENTO_COPIA_COMPULSADA,
            TIPOVALIDEZDOCUMENTO_COPIA_ORIGINAL,
            TIPOVALIDEZDOCUMENTO_ORIGINAL
    };


    public static final Map<Long, String> CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO = new HashMap<Long, String>() {{
        put(TIPOVALIDEZDOCUMENTO_COPIA, "0" + TIPOVALIDEZDOCUMENTO_COPIA);
        put(TIPOVALIDEZDOCUMENTO_COPIA_COMPULSADA, "0" + TIPOVALIDEZDOCUMENTO_COPIA_COMPULSADA);
        put(TIPOVALIDEZDOCUMENTO_COPIA_ORIGINAL, "0" + TIPOVALIDEZDOCUMENTO_COPIA_ORIGINAL);
        put(TIPOVALIDEZDOCUMENTO_ORIGINAL, "0" + TIPOVALIDEZDOCUMENTO_ORIGINAL);
    }};

    public static final Map<String, Long> TIPOVALIDEZDOCUMENTO_BY_CODIGO_SICRES
            = RegwebUtils.invert(CODIGO_SICRES_BY_TIPOVALIDEZDOCUMENTO);


    public static final Map<Long, String> CODIGO_NTI_BY_TIPOVALIDEZDOCUMENTO = new HashMap<Long, String>() {{
        put(TIPOVALIDEZDOCUMENTO_COPIA, "EE0" + TIPOVALIDEZDOCUMENTO_COPIA);
        put(TIPOVALIDEZDOCUMENTO_COPIA_COMPULSADA, "EE0" + TIPOVALIDEZDOCUMENTO_COPIA_COMPULSADA);
        put(TIPOVALIDEZDOCUMENTO_COPIA_ORIGINAL, "EE0" + TIPOVALIDEZDOCUMENTO_COPIA_ORIGINAL);
        put(TIPOVALIDEZDOCUMENTO_ORIGINAL, "EE0" + TIPOVALIDEZDOCUMENTO_ORIGINAL);
    }};

    public static final Map<String, Long> TIPOVALIDEZDOCUMENTO_BY_CODIGO_NTI
            = RegwebUtils.invert(CODIGO_NTI_BY_TIPOVALIDEZDOCUMENTO);

    /* -------------- ESTADO REGISTRO --------------*/
    public static final Long REGISTRO_VALIDO = 1L;
    public static final Long REGISTRO_RESERVA = 2L;
    public static final Long REGISTRO_PENDIENTE_VISAR = 3L;
    public static final Long REGISTRO_OFICIO_EXTERNO = 4L;
    public static final Long REGISTRO_OFICIO_INTERNO = 5L;
    /*public static final Long REGISTRO_ENVIADO = 6L;*/
    public static final Long REGISTRO_TRAMITADO = 7L;
    public static final Long REGISTRO_ANULADO = 8L;

    public static final Long[] ESTADOS_REGISTRO = {
            REGISTRO_VALIDO,
            REGISTRO_RESERVA,
            REGISTRO_PENDIENTE_VISAR,
            REGISTRO_OFICIO_EXTERNO,
            REGISTRO_OFICIO_INTERNO,
            REGISTRO_TRAMITADO,
            REGISTRO_ANULADO};

    /* ------------- TRANSPORTE ---------------*/

    public static final Long TRANSPORTE_SERVICIO_MENSAJEROS = 1L;
    public static final Long TRANSPORTE_CORREO_POSTAL = 2L;
    public static final Long TRANSPORTE_CORREO_POSTAL_CERTIFICADO = 3L;
    public static final Long TRANSPORTE_BUROFAX = 4L;
    public static final Long TRANSPORTE_EN_MANO = 5L;
    public static final Long TRANSPORTE_FAX = 6L;
    public static final Long TRANSPORTE_OTROS = 7L;


    public static final Long[] TRANSPORTES = {
            TRANSPORTE_SERVICIO_MENSAJEROS, TRANSPORTE_CORREO_POSTAL,
            TRANSPORTE_CORREO_POSTAL_CERTIFICADO, TRANSPORTE_BUROFAX,
            TRANSPORTE_EN_MANO, TRANSPORTE_FAX, TRANSPORTE_OTROS
    };


    public static final Map<Long, String> CODIGO_SICRES_BY_TRANSPORTE = new HashMap<Long, String>() {{
        put(TRANSPORTE_SERVICIO_MENSAJEROS, "0".concat(TRANSPORTE_SERVICIO_MENSAJEROS.toString()));
        put(TRANSPORTE_CORREO_POSTAL, "0".concat(TRANSPORTE_CORREO_POSTAL.toString()));
        put(TRANSPORTE_CORREO_POSTAL_CERTIFICADO, "0".concat(TRANSPORTE_CORREO_POSTAL_CERTIFICADO.toString()));
        put(TRANSPORTE_BUROFAX, "0".concat(TRANSPORTE_BUROFAX.toString()));
        put(TRANSPORTE_EN_MANO, "0".concat(TRANSPORTE_EN_MANO.toString()));
        put(TRANSPORTE_FAX, "0".concat(TRANSPORTE_FAX.toString()));
        put(TRANSPORTE_OTROS, "0".concat(TRANSPORTE_OTROS.toString()));
    }};


    public static final Map<String, Long> TRANSPORTE_BY_CODIGO_SICRES = RegwebUtils.invert(CODIGO_SICRES_BY_TRANSPORTE);


    /* --------------------CONFIGURACIONPERSONA ------------------------*/
    public static final long CONFIGURACION_PERSONA_SIN_GUARDAR = 1L;
    public static final long CONFIGURACION_PERSONA_GUARDAR_TODOS = 2L;
    public static final long CONFIGURACION_PERSONA_CONFIRMAR_NUEVA_PERSONA = 3L;
    public static final long CONFIGURACION_PERSONA_SISTEMA_EXTERNO = 4L;

    public static final long[] CONFIGURACIONES_PERSONA = {
            CONFIGURACION_PERSONA_SIN_GUARDAR,
            CONFIGURACION_PERSONA_GUARDAR_TODOS,
            CONFIGURACION_PERSONA_CONFIRMAR_NUEVA_PERSONA,
            CONFIGURACION_PERSONA_SISTEMA_EXTERNO
    };


    /* -------------- DESCARGA TIPOS --------------*/
    public static final String UNIDAD = "unidad";
    public static final String OFICINA = "oficina";
    public static final String CATALOGO = "catalogo";

    /* -------------- PAIS --------------*/
    public static final Long PAIS_ESPAÑA = 724L;

    /* -------------- CANAL NOTIFICACIÓN --------------*/
    public static final Long CANAL_DIRECCION_POSTAL = 1L;
    public static final Long CANAL_DIRECCION_ELECTRONICA = 2L;
    public static final Long CANAL_COMPARECENCIA_ELECTRONICA = 3L;

    /* --------------------TIPO PROPIEDAD GLOBAL ------------------------*/
    public static final long TIPO_PROPIEDAD_GENERAL = 1L;
    public static final long TIPO_PROPIEDAD_DIR3CAIB = 2L;
    public static final long TIPO_PROPIEDAD_CUSTODIA = 3L;
    public static final long TIPO_PROPIEDAD_USERINFORMATION = 4L;
    public static final long TIPO_PROPIEDAD_SCANNER = 5L;
    public static final long TIPO_PROPIEDAD_DISTRIBUCION = 6L;
    public static final long TIPO_PROPIEDAD_SIR = 7L;

    public static final long[] TIPOS_PROPIEDAD_GLOBAL = {
            TIPO_PROPIEDAD_GENERAL,
            TIPO_PROPIEDAD_DIR3CAIB,
            TIPO_PROPIEDAD_CUSTODIA,
            TIPO_PROPIEDAD_USERINFORMATION,
            TIPO_PROPIEDAD_SCANNER,
            TIPO_PROPIEDAD_DISTRIBUCION,
            TIPO_PROPIEDAD_SIR
    };


    /**
     * --------------- TIPO FIRMA --------------
     */
    public static final Long TIPO_FIRMA_CSV = 1L;
    public static final Long TIPO_FIRMA_XADES_DETACHED_SIGNATURE = 2L;
    public static final Long TIPO_FIRMA_XADES_ENVELOPE_SIGNATURE = 3L;
    public static final Long TIPO_FIRMA_CADES_DETACHED_EXPLICIT_SIGNATURE = 4L;
    public static final Long TIPO_FIRMA_CADES_ATTACHED_IMPLICIT_SIGNAUTRE = 5L;
    public static final Long TIPO_FIRMA_PADES = 6L;

    public static final Long[] TIPOS_FIRMA = {TIPO_FIRMA_CSV,
            TIPO_FIRMA_XADES_DETACHED_SIGNATURE,
            TIPO_FIRMA_XADES_ENVELOPE_SIGNATURE,
            TIPO_FIRMA_CADES_DETACHED_EXPLICIT_SIGNATURE,
            TIPO_FIRMA_CADES_ATTACHED_IMPLICIT_SIGNAUTRE,
            TIPO_FIRMA_PADES};


    public static final Map<Long, String> CODIGO_NTI_BY_TIPOFIRMA = new HashMap<Long, String>() {{
        put(TIPO_FIRMA_CSV, "TF0" + TIPO_FIRMA_CSV);
        put(TIPO_FIRMA_XADES_DETACHED_SIGNATURE, "TF0" + TIPO_FIRMA_XADES_DETACHED_SIGNATURE);
        put(TIPO_FIRMA_XADES_ENVELOPE_SIGNATURE, "TF0" + TIPO_FIRMA_XADES_ENVELOPE_SIGNATURE);
        put(TIPO_FIRMA_CADES_DETACHED_EXPLICIT_SIGNATURE, "TF0" + TIPO_FIRMA_CADES_DETACHED_EXPLICIT_SIGNATURE);
        put(TIPO_FIRMA_CADES_ATTACHED_IMPLICIT_SIGNAUTRE, "TF0" + TIPO_FIRMA_CADES_ATTACHED_IMPLICIT_SIGNAUTRE);
        put(TIPO_FIRMA_PADES, "TF0" + TIPO_FIRMA_PADES);
    }};


    public static final Map<String, Long> TIPOFIRMA_BY_CODIGO_NTI = RegwebUtils.invert(CODIGO_NTI_BY_TIPOFIRMA);

    
    
    /*
     * 1;"TF01";"CSV"
2;"TF02";"XAdES internally detached signature"
3;"TF03";"XAdES envelope signature"
4;"TF04";"CAdES detached/explicit signature"
5;"TF05";"CAdES attached/implicit signautre"
6;"TF06";"PAdES"

     */

    /* -------------- TIPO USUARIO --------------*/
    public static final Long TIPO_USUARIO_PERSONA = 1L;
    public static final Long TIPO_USUARIO_APLICACION = 2L;

    public static final Long[] TIPOS_USUARIO = {
            TIPO_USUARIO_PERSONA, TIPO_USUARIO_APLICACION
    };

    /* -------------- ACCION LOPD --------------*/
    public static final Long LOPD_LISTADO = 1L;
    public static final Long LOPD_CONSULTA = 2L;
    public static final Long LOPD_CREACION = 3L;
    public static final Long LOPD_MODIFICACION = 4L;
    public static final String LOPDMIGRADO_LISTADO = "LIST";
    public static final String LOPDMIGRADO_CONSULTA = "SELECT";
    public static final String LOPDMIGRADO_CREACION = "INSERT";
    public static final String LOPDMIGRADO_MODIFICACION = "UPDATE";
    public static final Long LOPD_TIPO_MIGRADO = 3L;

    /* -------------- TIPO REGISTRO -------------*/
    public static final Long REGISTRO_ENTRADA = 1L;
    public static final Long REGISTRO_SALIDA = 2L;
    public static final String REGISTRO_ENTRADA_ESCRITO = "Entrada";
    public static final String REGISTRO_SALIDA_ESCRITO = "Sortida";
    public static final String REGISTRO_ENTRADA_ESCRITO_CASTELLANO = "Entrada";
    public static final String REGISTRO_SALIDA_ESCRITO_CASTELLANO = "Salida";

    /* -------------- TIPO DOCUMENTO IDENTIFICACION --------------*/
    public static final char TIPODOCUMENTOID_NIF = 'N';
    public static final long TIPODOCUMENTOID_NIF_ID = 1;

    public static final char TIPODOCUMENTOID_CIF = 'C';
    public static final long TIPODOCUMENTOID_CIF_ID = 2;

    public static final char TIPODOCUMENTOID_PASSAPORT = 'P';
    public static final long TIPODOCUMENTOID_PASSAPORT_ID = 3;

    public static final char TIPODOCUMENTOID_NIE = 'E';
    public static final long TIPODOCUMENTOID_NIE_ID = 4;

    public static final char TIPODOCUMENTOID_PERSONA_FISICA = 'X';
    public static final long TIPODOCUMENTOID_PERSONA_FISICA_ID = 5;

    public static final char TIPODOCUMENTOID_CODIGO_ORIGEN = 'O';
    public static final long TIPODOCUMENTOID_CODIGO_ORIGEN_ID = 6;


    public static final long[] TIPOS_DOCUMENTOID = {
            TIPODOCUMENTOID_NIF_ID, TIPODOCUMENTOID_CIF_ID,
            TIPODOCUMENTOID_PASSAPORT_ID, TIPODOCUMENTOID_NIE_ID,
            TIPODOCUMENTOID_PERSONA_FISICA_ID, TIPODOCUMENTOID_CODIGO_ORIGEN_ID
    };


    public static final Map<Long, Character> CODIGO_NTI_BY_TIPODOCUMENTOID = new HashMap<Long, Character>() {{
        put(TIPODOCUMENTOID_NIF_ID, TIPODOCUMENTOID_NIF);
        put(TIPODOCUMENTOID_CIF_ID, TIPODOCUMENTOID_CIF);
        put(TIPODOCUMENTOID_PASSAPORT_ID, TIPODOCUMENTOID_PASSAPORT);
        put(TIPODOCUMENTOID_NIE_ID, TIPODOCUMENTOID_NIE);
        put(TIPODOCUMENTOID_PERSONA_FISICA_ID, TIPODOCUMENTOID_PERSONA_FISICA);
        put(TIPODOCUMENTOID_CODIGO_ORIGEN_ID, TIPODOCUMENTOID_CODIGO_ORIGEN);
    }};


    public static final Map<Character, Long> TIPODOCUMENTOID_BY_CODIGO_NTI = RegwebUtils.invert(CODIGO_NTI_BY_TIPODOCUMENTOID);

    /* -------------- CANAL NOTIFICACION --------------*/
    public static final long CANALNOTIFICACION_DIRECCION_POSTAL = 1;
    public static final long CANALNOTIFICACION_DIRECCION_ELECTRONICA_HABILITADA = 2;
    public static final long CANALNOTIFICACION_COMPARECENCIA_ELECTRONICA = 3;

    public static final long[] CANALES_NOTIFICACION = {
            CANALNOTIFICACION_DIRECCION_POSTAL,
            CANALNOTIFICACION_DIRECCION_ELECTRONICA_HABILITADA,
            CANALNOTIFICACION_COMPARECENCIA_ELECTRONICA
    };


    public static final Map<Long, String> CODIGO_BY_CANALNOTIFICACION = new HashMap<Long, String>() {{
        put(CANALNOTIFICACION_DIRECCION_POSTAL, "0" + CANALNOTIFICACION_DIRECCION_POSTAL);
        put(CANALNOTIFICACION_DIRECCION_ELECTRONICA_HABILITADA, "0" + CANALNOTIFICACION_DIRECCION_ELECTRONICA_HABILITADA);
        put(CANALNOTIFICACION_COMPARECENCIA_ELECTRONICA, "0" + CANALNOTIFICACION_COMPARECENCIA_ELECTRONICA);
    }};


    public static final Map<String, Long> CANALNOTIFICACION_BY_CODIGO
            = RegwebUtils.invert(CODIGO_BY_CANALNOTIFICACION);

    /* ----------------ANEXOS --------------------------*/
    /* SICRES 3 establece 80 max. Se quitan los 4 que emplea la extensión .ext*/
    public static final int ANEXO_NOMBREARCHIVO_MAXLENGTH = 76;
    public static final String ANEXO_TIPOVALIDEZDOCUMENTO_COPIA = "01";


    /* ----------------MODO FIRMA ANEXOS --------------------------*/
    public static final int MODO_FIRMA_ANEXO_SINFIRMA = 0;
    public static final int MODO_FIRMA_ANEXO_ATTACHED = 1; // Document amb firma adjunta
    public static final int MODO_FIRMA_ANEXO_DETACHED = 2; // Firma en document separat


    /* -------- TIPO DOCUMENTO ---------- */
    public static final Long TIPO_DOCUMENTO_FORMULARIO = 1L;
    public static final Long TIPO_DOCUMENTO_DOC_ADJUNTO = 2L;
    public static final Long TIPO_DOCUMENTO_FICHERO_TECNICO = 3L;

    public static final Long[] TIPOS_DOCUMENTO = {
            TIPO_DOCUMENTO_FORMULARIO,
            TIPO_DOCUMENTO_DOC_ADJUNTO,
            TIPO_DOCUMENTO_FICHERO_TECNICO
    };

    public static final Map<Long, String> CODIGO_SICRES_BY_TIPO_DOCUMENTO = new HashMap<Long, String>() {{
        put(TIPO_DOCUMENTO_FORMULARIO, "0" + TIPO_DOCUMENTO_FORMULARIO);
        put(TIPO_DOCUMENTO_DOC_ADJUNTO, "0" + TIPO_DOCUMENTO_DOC_ADJUNTO);
        put(TIPO_DOCUMENTO_FICHERO_TECNICO, "0" + TIPO_DOCUMENTO_FICHERO_TECNICO);
    }};


    public static final Map<String, Long> TIPO_DOCUMENTO_BY_CODIGO_NTI
            = RegwebUtils.invert(CODIGO_SICRES_BY_TIPO_DOCUMENTO);

    /* -------- TIPO DOCUMENTACION FISICA ---------- */

    public static final Long TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA = 1L;
    public static final Long TIPO_DOCFISICA_ACOMPANYA_DOC_COMPLEMENTARIA = 2L;
    public static final Long TIPO_DOCFISICA_NO_ACOMPANYA_DOC = 3L;


    public static final Long[] TIPOS_DOCFISICA = {
            TIPO_DOCFISICA_ACOMPANYA_DOC_REQUERIDA,
            TIPO_DOCFISICA_ACOMPANYA_DOC_COMPLEMENTARIA,
            TIPO_DOCFISICA_NO_ACOMPANYA_DOC
    };

    /* -------- DESTINOS DE OFICIO DE REMISION ---------- */
    public static final Long DESTINO_OFICIO_REMISION_INTERNO = 1L;
    public static final Long DESTINO_OFICIO_REMISION_EXTERNO = 2L;

    public static final Long[] DESTINOS_OFICIO_REMISION = {
            DESTINO_OFICIO_REMISION_INTERNO,
            DESTINO_OFICIO_REMISION_EXTERNO
    };

    /* -------- TIPOS DE OFICIO DE REMISION ---------- */
    public static final Long TIPO_OFICIO_REMISION_ENTRADA = 1L;
    public static final Long TIPO_OFICIO_REMISION_SALIDA = 2L;

    public static final Long[] TIPOS_OFICIO_REMISION = {
            TIPO_OFICIO_REMISION_ENTRADA,
            TIPO_OFICIO_REMISION_SALIDA
    };

    /* -------- ESTADO DE UN OFICIO DE REMISION ---------- */
    public static final int OFICIO_REMISION_INTERNO_ENVIADO = 0; // Interno
    public static final int OFICIO_REMISION_EXTERNO_ENVIADO = 1; // Externo
    public static final int OFICIO_REMISION_ACEPTADO = 2; // Interno y Externo
    public static final int OFICIO_REMISION_RECHAZADO = 3; // Externo
    public static final int OFICIO_REMISION_REENVIADO = 4; // Externo
    public static final int OFICIO_REMISION_ANULADO = 5; // Interno y Externo


    public static final int OFICIO_INTERNO = 0;
    public static final int OFICIO_EXTERNO = 1;
    public static final int OFICIO_ACEPTADO = 2;
    public static final int OFICIO_SIR_ENVIADO = 3;
    public static final int OFICIO_SIR_ENVIADO_ACK = 4;
    public static final int OFICIO_SIR_ENVIADO_ERROR = 5;
    public static final int OFICIO_SIR_REENVIADO = 6;
    public static final int OFICIO_SIR_REENVIADO_ACK = 7;
    public static final int OFICIO_SIR_REENVIADO_ERROR = 8;
    public static final int OFICIO_SIR_RECHAZADO = 9;
    public static final int OFICIO_SIR_RECHAZADO_ACK = 10;
    public static final int OFICIO_SIR_RECHAZADO_ERROR = 11;
    public static final int OFICIO_SIR_DEVUELTO = 12;
    public static final int OFICIO_ANULADO = 13;

    public static final Integer[] ESTADOS_OFICIO_REMISION = {
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

    public static final int REGISTROS_PANTALLA_INICIO = 5;


    /* -------------- INFORMES - TIPOS REGISTRO --------------*/
    public static final Long INFORME_TIPO_REGISTRO_ENTRADASALIDA = 0L;
    public static final Long INFORME_TIPO_REGISTRO_ENTRADA = 1L;
    public static final Long INFORME_TIPO_REGISTRO_SALIDA = 2L;


    /* -------------- ANEXO ORIGEN -------------------------- */
    public static final Long ANEXO_ORIGEN_CIUDADANO = 0L;
    public static final Long ANEXO_ORIGEN_ADMINISTRACION = 1L;

    /* -------------- CAT SERVICIOS -------------------------- */
    public static final Long OFICINA_REGISTRO = 1L;
    public static final Long OFICINA_INFORMACION = 2L;
    public static final Long OFICINA_TRAMITACION = 3L;
    public static final Long REGISTRO_VIRTUAL_NO_PRESENCIAL = 4L;
    public static final Long OFICINA_INTEGRADA_SIR = 5L;
    public static final Long OFICINA_INTEGRADA_SIR_ENVIO = 6L;
    public static final Long OFICINA_INTEGRADA_SIR_RECEPCION = 7L;
    public static final Long OFICINA_INTEGRADA_060 = 8L;
    public static final Long OFICINA_CORREOS = 9L;
    public static final Long OFICINA_EXTRANJERIA = 10L;
    public static final Long OFICINA_VIOLENCIA_GENERO = 11L;
    public static final Long OFICINA_ACCESIBLE = 12L;

    /* -------- Límite expresiones en ORACLE in (:elements) (ORA-01795) ---------- */
    public static final int NUMBER_EXPRESSIONS_IN = 1000;

    /* -------------- LOGOMENU --------------*/
    public int LOGOMENU_AMPLADA_MAX = 600;
    public int LOGOMENU_ALSADA_MAX = 90;

    /* -------------- OFICINA VIRTUAL --------------*/
    public Boolean OFICINA_VIRTUAL_SI = true; // Las incluimos en los resultados obtenidos
    public Boolean OFICINA_VIRTUAL_NO = false; // No las incluimos en los resultados obtenidos

    /* -------------- ANEXO MIME --------------*/
    public int ANEXO_TIPOMIME_MAXLENGTH_SIR = 20;
    public int ANEXO_NOMBREFICHERO_MAXLENGTH_SIR = 80;
    public int ANEXO_OBSERVACIONES_MAXLENGTH_SIR = 50;
    public int ANEXO_IDENTIFICADOR_MAXLENGTH_SIR = 50;

    String CRON_INICIALIZAR_CONTADORES = "0 0 0 1 1 ? *";
}
