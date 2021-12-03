create table RWE_ANEXO
(
    ID                   int8         not null,
    CERTIFICADO          bytea,
    CSV                  varchar(255),
    CONFIDENCIAL            bool not null,
    CUSTODIAID           varchar(256),
    CUSTODIADO           bool       not null,
    ESTADOFIRMA          int4,
    EXPEDIENTEID         varchar(256),
    FECHACAPTURA         timestamp    not null,
    FECHAVALIDACION      timestamp,
    FIRMA                text,
    FIRMAVALIDA          bool,
    HASH                 bytea,
    JUSTIFICANTE         bool         not null,
    MODOFIRMA            int4,
    MOTIVONOVALID        varchar(255),
    NOMBRE_FICHERO       varchar(255),
    OBSERVACIONES        varchar(50),
    ORIGEN               int4,
    PERFIL_CUSTODIA      int8,
    PURGADO              bool,
    SCAN                 bool,
    SIGNFORMAT           varchar(255),
    SIGNPROFILE          varchar(255),
    SIGNTYPE             varchar(255),
    TIMESTAMP            bytea,
    TIPODOC              int8,
    TITULO               varchar(200) not null,
    VAL_OCSP_CERTIFICADO bytea,
    TVALDOC              int8,
    REGISTRODETALLE      int8,
    TAMANO_FICHERO        int4,
    TDOCUMENTAL          int8,
    primary key (ID)
);

create table RWE_ANEXO_SIR
(
    ID                    int8        not null,
    CERTIFICADO           text,
    FIRMA                 text,
    HASH                  text        not null,
    ID_DOCUMENTO_FIRMADO  varchar(50),
    IDENTIFICADOR_FICHERO varchar(50) not null,
    NOMBRE_FICHERO        varchar(80) not null,
    OBSERVACIONES         varchar(50),
    PURGADO               bool        not null,
    TIMESTAMP             text,
    TIPO_DOCUMENTO        varchar(2)  not null,
    TIPO_MIME             varchar(20),
    VAL_OCSP_CE           text,
    VALIDEZ_DOCUMENTO     varchar(2),
    ANEXO                 int8,
    REGISTRO_SIR          int8,
    primary key (ID)
);

create table RWE_ARCHIVO
(
    ID     int8         not null,
    MIME   varchar(255) not null,
    NOMBRE varchar(255) not null,
    TAMANO int8         not null,
    primary key (ID)
);

create table RWE_CATCOMUNIDADAUTONOMA
(
    ID                   int8        not null,
    C_CODIGO_DIR2        int8,
    C_COMUNIDAD_RPC      varchar(2),
    CODIGOCOMUNIDAD      int8        not null,
    DESCRIPCIONCOMUNIDAD varchar(50) not null,
    PAIS                 int8,
    primary key (ID)
);

create table RWE_CATENTIDADGEOGRAFICA
(
    ID                           int8        not null,
    CODIGOENTIDADGEOGRAFICA      varchar(2)  not null,
    DESCRIPCIONENTIDADGEOGRAFICA varchar(50) not null,
    primary key (ID)
);

create table RWE_CATESTADOENTIDAD
(
    ID                       int8        not null,
    CODIGOESTADOENTIDAD      varchar(2)  not null unique,
    DESCRIPCIONESTADOENTIDAD varchar(50) not null,
    primary key (ID)
);

create table RWE_CATLOCALIDAD
(
    ID                   int8        not null,
    CODIGOLOCALIDAD      int8        not null,
    DESCRIPCIONLOCALIDAD varchar(50) not null,
    ENTIDADGEOGRAFICA    int8,
    PROVINCIA            int8,
    primary key (ID)
);

create table RWE_CATNIVELADMINISTRACION
(
    ID                             int8         not null,
    CODIGONIVELADMINISTRACION      int8         not null,
    DESCRIPCIONNIVELADMINISTRACION varchar(300) not null,
    primary key (ID)
);

create table RWE_CATPAIS
(
    ID              int8         not null,
    ALFA2PAIS       varchar(2),
    ALFA3PAIS       varchar(3),
    CODIGOPAIS      int8         not null,
    DESCRIPCIONPAIS varchar(100) not null,
    primary key (ID)
);

create table RWE_CATPROVINCIA
(
    ID                   int8        not null,
    CODIGOPROVINCIA      int8        not null,
    DESCRIPCIONPROVINCIA varchar(50) not null,
    COMUNIDADAUTONOMA    int8,
    primary key (ID)
);

create table RWE_CATSERVICIO
(
    ID                  int8         not null,
    CODIGOSERVICIO      int8         not null,
    DESCRIPCIONSERVICIO varchar(300) not null,
    primary key (ID)
);

create table RWE_CATTIPOVIA
(
    ID                 int8         not null,
    CODIGOTIPOVIA      int8         not null,
    DESCRIPCIONTIPOVIA varchar(300) not null,
    primary key (ID)
);

create table RWE_CODIGOASUNTO
(
    ID      int8        not null,
    ACTIVO  bool        not null,
    CODIGO  varchar(16) not null,
    ENTIDAD int8        not null,
    primary key (ID),
    unique (CODIGO)
);

create table RWE_COLA
(
    ID                  int8 not null,
    DENOMINACIONOFICINA varchar(255),
    DESCRIPCIONOBJETO   varchar(255),
    ERROR               text,
    ESTADO              int8,
    FECHA               timestamp,
    FECHAPROCESADO      timestamp,
    IDOBJETO            int8,
    NUMMAXREINTENTOS    int4,
    NUMREINTENTOS       int4,
    TIPO                int8,
    TIPOREGISTRO        int8,
    USUARIOENTIDAD      int8 not null,
    primary key (ID)
);

create table RWE_CONFIGURACION
(
    ID        int8 not null,
    COLORMENU varchar(255),
    TEXTOPIE  varchar(4000),
    LOGOMENU  int8,
    LOGOPIE   int8,
    primary key (ID)
);

create table RWE_CONTADOR
(
    ID     int8 not null,
    NUMERO int4 not null,
    primary key (ID)
);

create table RWE_DESCARGA
(
    ID               int8 not null,
    FECHAFIN         timestamp,
    FECHAIMPORTACION timestamp,
    FECHAINICIO      timestamp,
    TIPO             varchar(255),
    ENTIDAD          int8,
    primary key (ID)
);

create table RWE_ENTIDAD
(
    ID              int8          not null,
    ACTIVO          bool          not null,
    CODIGODIR3      varchar(255)  not null unique,
    COLORMENU       varchar(255),
    CONFIGPERSONA   int8,
    DESCRIPCION     varchar(4000) not null,
    DIASVISADO      int4,
    MANTENIMIENTO   bool          not null,
    NOMBRE          varchar(255)  not null,
    NUMREGISTRO     varchar(4000),
    OFICIOREMISION  bool          not null,
    PERFIL_CUSTODIA int8,
    POSXSELLO       int4,
    POSYSELLO       int4,
    SELLO           varchar(4000),
    SIR             bool          not null,
    TEXTOPIE        varchar(4000),
    TIPSCAN         varchar(20),
    CONTADOR_SIR    int8,
    LIBRO           int8,
    LOGOMENU        int8,
    LOGOPIE         int8,
    LOGOSELLO       int8,
    PROPIETARIO     int8,
    primary key (ID)
);

create table RWE_ENTIDAD_USUENT
(
    IDENTIDAD int8 not null,
    IDUSUENT  int8 not null,
    primary key (IDENTIDAD, IDUSUENT)
);

create table RWE_HISTORICOUO
(
    CODANTERIOR int8 not null,
    CODULTIMA   int8 not null,
    primary key (CODANTERIOR, CODULTIMA)
);

create table RWE_HISTORICO_REGISTRO_ENTRADA
(
    ID               int8         not null,
    ESTADO           int8,
    FECHA            timestamp    not null,
    MODIFICACION     varchar(255) not null,
    RE_ORIGINAL      text,
    REGISTRO_ENTRADA int8,
    USUARIO          int8,
    primary key (ID)
);

create table RWE_HISTORICO_REGISTRO_SALIDA
(
    ID              int8         not null,
    ESTADO          int8,
    FECHA           timestamp    not null,
    MODIFICACION    varchar(255) not null,
    RS_ORIGINAL     text,
    REGISTRO_SALIDA int8,
    USUARIO         int8,
    primary key (ID)
);

create table RWE_INTEGRACION
(
    ID           int8 not null,
    DESCRIPCION  varchar(400),
    ERROR        text,
    ESTADO       int8 not null,
    EXCEPCION    text,
    FECHA        timestamp,
    NUMREGFORMAT varchar(255),
    PETICION     varchar(2000),
    TIEMPO       int8 not null,
    TIPO         int8 not null,
    ENTIDAD      int8,
    primary key (ID)
);

create table RWE_INTERESADO
(
    ID              int8 not null,
    APELLIDO1       varchar(255),
    APELLIDO2       varchar(255),
    CANALNOTIF      int8,
    CODIGODIR3      varchar(255),
    CODIGODIRE      varchar(15),
    CP              varchar(5),
    DIRECCION       varchar(160),
    DIRELECTRONICA  varchar(160),
    DOCUMENTO       varchar(17),
    EMAIL           varchar(160),
    ISREPRESENTANTE bool,
    NOMBRE          varchar(255),
    OBSERVACIONES   varchar(160),
    RAZONSOCIAL     varchar(2000),
    TELEFONO        varchar(20),
    TIPOINTERESADO  int8,
    TIPODOCIDENT    int8,
    LOCALIDAD       int8,
    PAIS            int8,
    PROVINCIA       int8,
    REGISTRODETALLE int8,
    REPRESENTADO    int8,
    REPRESENTANTE   int8,
    primary key (ID)
);

create table RWE_INTERESADO_SIR
(
    ID                            int8 not null,
    CANAL_NOTIF_INTERESADO        varchar(2),
    CANAL_NOTIF_REPRESENTANTE     varchar(2),
    COD_MUNICIPIO_INTERESADO      varchar(5),
    COD_MUNICIPIO_REPRESENTANTE   varchar(5),
    COD_PAIS_INTERESADO           varchar(4),
    COD_PAIS_REPRESENTANTE        varchar(4),
    CP_INTERESADO                 varchar(5),
    CP_REPRESENTANTE              varchar(5),
    COD_PROVINCIA_INTERESADO      varchar(2),
    COD_PROVINCIA_REPRESENTANTE   varchar(2),
    EMAIL_INTERESADO              varchar(160),
    EMAIL_REPRESENTANTE           varchar(160),
    DIR_ELECTRONICA_INTERESADO    varchar(160),
    DIR_ELECTRONICA_REPRESENTANTE varchar(160),
    DIRECCION_INTERESADO          varchar(160),
    DIRECCION_REPRESENTANTE       varchar(160),
    DOCUMENTO_INTERESADO          varchar(17),
    DOCUMENTO_REPRESENTANTE       varchar(17),
    NOMBRE_INTERESADO             varchar(30),
    NOMBRE_REPRESENTANTE          varchar(30),
    OBSERVACIONES                 varchar(160),
    APELLIDO1_INTERESADO          varchar(30),
    APELLIDO1_REPRESENTANTE       varchar(30),
    RAZON_SOCIAL_INTERESADO       varchar(80),
    RAZON_SOCIAL_REPRESENTANTE    varchar(80),
    APELLIDO2_INTERESADO          varchar(30),
    APELLIDO2_REPRESENTANTE       varchar(30),
    TELEFONO_INTERESADO           varchar(20),
    TELEFONO_REPRESENTANTE        varchar(20),
    T_DOCUMENTO_INTERESADO        varchar(1),
    T_DOCUMENTO_REPRESENTANTE     varchar(1),
    REGISTRO_SIR                  int8,
    primary key (ID)
);

create table RWE_LIBRO
(
    ID                       int8         not null,
    ACTIVO                   bool         not null,
    CODIGO                   varchar(4)   not null,
    NOMBRE                   varchar(255) not null,
    CONTADOR_ENTRADA         int8,
    CONTADOR_OFICIO_REMISION int8,
    CONTADOR_SALIDA          int8,
    CONTADOR_SIR             int8,
    ORGANISMO                int8,
    primary key (ID)
);

create table RWE_LOPD
(
    ID           int8         not null,
    ACCION       int8         not null,
    ANYOREGISTRO varchar(255) not null,
    FECHA        timestamp    not null,
    NUMREGISTRO  int4         not null,
    TIPOREGISTRO int8         not null,
    LIBRO        int8         not null,
    USUARIO      int8         not null,
    primary key (ID)
);

create table RWE_MENSAJE_CONTROL
(
    ID                int8        not null,
    COD_ENT_REG_DEST  varchar(21) not null,
    COD_ENT_REG_ORI   varchar(21) not null,
    COD_ERROR         varchar(4),
    DESCRIPCION       varchar(1024),
    FECHA             timestamp,
    FECHA_DESTINO     timestamp,
    ID_INTERCAMBIO    varchar(33) not null,
    INDICADOR_PRUEBA  int4        not null,
    NUM_REG_DESTINO   varchar(20),
    TIPO_COMUNICACION int8        not null,
    TIPO_MENSAJE      varchar(2)  not null,
    ENTIDAD           int8        not null,
    primary key (ID)
);

create table RWE_MODELO_OFICIO_REMISION
(
    ID      int8         not null,
    NOMBRE  varchar(255) not null,
    ENTIDAD int8,
    MODELO  int8,
    primary key (ID)
);

create table RWE_MODELO_RECIBO
(
    ID      int8         not null,
    NOMBRE  varchar(255) not null,
    ENTIDAD int8,
    MODELO  int8,
    primary key (ID)
);

create table RWE_MODIFICACIONLOPD_MIGRADO
(
    ID         int8         not null,
    FECHA      timestamp    not null,
    FECHAMOD   timestamp    not null,
    TIPOACCESO varchar(10)  not null,
    USUARIO    varchar(255) not null,
    REGMIG     int8,
    primary key (ID)
);

create table RWE_NOTIFICACION
(
    ID            int8 not null,
    ASUNTO        varchar(200),
    ESTADO        int8 not null,
    FECHA_ENVIADO timestamp,
    FECHA_LEIDO   timestamp,
    MENSAJE       varchar(4000),
    TIPO          int8 not null,
    DESTINATARIO  int8 not null,
    REMITENTE     int8,
    primary key (ID)
);

create table RWE_OFICINA
(
    ID                   int8         not null,
    CODPOSTAL            varchar(14),
    CODIGO               varchar(9)   not null,
    DENOMINACION         varchar(300) not null,
    NOMBREVIA            varchar(300),
    NUMVIA               varchar(20),
    COMUNIDAD            int8,
    PAIS                 int8,
    ESTADO               int8,
    LOCALIDAD            int8,
    OFICINARESPONSABLE   int8,
    ORGANISMORESPONSABLE int8,
    TIPOVIA              int8,
    primary key (ID)
);

create table RWE_OFICINA_SERVICIO
(
    IDOFICINA  int8 not null,
    IDSERVICIO int8 not null,
    primary key (IDOFICINA, IDSERVICIO)
);

create table RWE_OFICIO_REMISION
(
    ID                     int8      not null,
    COD_ENT_REG_DEST       varchar(21),
    COD_ENT_REG_PROC       varchar(21),
    COD_ERROR              varchar(255),
    CONTACTOSDESTINO       varchar(2000),
    DEC_ENT_REG_DEST       varchar(80),
    DEC_ENT_REG_PROC       varchar(80),
    DESC_ERROR             varchar(2000),
    DESTINOEXTERNOCODIGO   varchar(9),
    DESTINOEXTERNODENOMINA varchar(300),
    ESTADO                 int4      not null,
    FECHA                  timestamp not null,
    FECHA_DESTINO          timestamp,
    FECHA_ESTADO           timestamp,
    ID_INTERCAMBIO         varchar(33),
    NUMREGISTRO            int4      not null,
    NUM_REG_DESTINO        varchar(255),
    REINTENTOS             int4,
    SIR                    bool      not null,
    TIPO_OFICIO            int8      not null,
    LIBRO                  int8      not null,
    OFICINA                int8      not null,
    ORGANISMODEST          int8,
    USUARIO                int8      not null,
    primary key (ID)
);

create table RWE_OFIREM_REGENT
(
    IDOFIREM int8 not null,
    IDREGENT int8 not null
);

create table RWE_OFIREM_REGSAL
(
    IDOFIREM int8 not null,
    IDREGSAL int8 not null
);

create table RWE_ORGANISMO
(
    ID                  int8         not null,
    CODPOSTAL           varchar(14),
    CODIGO              varchar(9)   not null,
    DENOMINACION        varchar(300) not null,
    EDP                 bool,
    NIVELJERARQUICO     int8,
    NOMBREVIA           varchar(300),
    NUMVIA              varchar(20),
    PERMITE_USUARIOS    bool,
    CODAMBCOMUNIDAD     int8,
    CODAMBPROVINCIA     int8,
    PAIS                int8,
    EDPRINCIPAL         int8,
    ENTIDAD             int8,
    ESTADO              int8,
    LOCALIDAD           int8,
    NIVELADMINISTRACION int8,
    ORGANISMORAIZ       int8,
    ORGANISMOSUPERIOR   int8,
    TIPOVIA             int8,
    primary key (ID)
);

create table RWE_PENDIENTE
(
    ID          int8 not null,
    ESTADO      varchar(255),
    FECHA       timestamp,
    IDORGANISMO int8,
    PROCESADO   bool,
    primary key (ID)
);

create table RWE_PERMLIBUSU
(
    ID      int8 not null,
    ACTIVO  bool not null,
    PERMISO int8,
    LIBRO   int8,
    USUARIO int8,
    primary key (ID)
);

create table RWE_PERMORGUSU
(
    ID        int8 not null,
    ACTIVO    bool not null,
    PERMISO   int8,
    ORGANISMO int8,
    USUARIO   int8,
    primary key (ID)
);

create table RWE_PERSONA
(
    ID             int8 not null,
    APELLIDO1      varchar(30),
    APELLIDO2      varchar(30),
    CANALNOTIF     int8,
    CP             varchar(5),
    DIRECCION      varchar(160),
    DIRELECTRONICA varchar(160),
    DOCUMENTO      varchar(17),
    EMAIL          varchar(160),
    NOMBRE         varchar(30),
    OBSERVACIONES  varchar(160),
    RAZONSOCIAL    varchar(80),
    TELEFONO       varchar(20),
    TIPOPERSONA    int8 not null,
    TIPODOCIDENT   int8,
    ENTIDAD        int8 not null,
    LOCALIDAD      int8,
    PAIS           int8,
    PROVINCIA      int8,
    primary key (ID)
);

create table RWE_PLUGIN
(
    ID                  int8          not null,
    ACTIVO              bool          not null,
    CLASE               varchar(1000) not null,
    DESCRIPCION         varchar(2000) not null,
    ENTIDAD             int8,
    NOMBRE              varchar(255)  not null,
    PROPIEDADES_ADMIN   text,
    PROPIEDADES_ENTIDAD text,
    TIPO                int8,
    primary key (ID)
);

create table RWE_PROPIEDADGLOBAL
(
    ID          int8         not null,
    CLAVE       varchar(255) not null,
    DESCRIPCION varchar(255),
    ENTIDAD     int8,
    TIPO        int8,
    VALOR       varchar(2048),
    primary key (ID),
    unique (CLAVE, ENTIDAD)
);

create table RWE_REGISTROLOPD_MIGRADO
(
    ID         int8         not null,
    FECHA      timestamp    not null,
    TIPOACCESO varchar(10)  not null,
    USUARIO    varchar(255) not null,
    REGMIG     int8,
    primary key (ID)
);

create table RWE_REGISTRO_DETALLE
(
    ID               int8 not null,
    APLICACION       varchar(255),
    APLICACION_TELEMATICA varchar(255),
    COD_ENT_REG_DEST varchar(21),
    CODIGOSIA        int8,
    DEC_ENT_REG_DEST varchar(80),
    DEC_T_ANOTACION  varchar(80),
    EXPEDIENTE       varchar(80),
    EXPEDIENTE_JUST  varchar(256),
    EXPONE           text,
    EXTRACTO         varchar(240),
    FECHAORIGEN      timestamp,
    ID_INTERCAMBIO   varchar(33),
    IDIOMA           int8,
    INDICADOR_PRUEBA int4,
    NUMREG_ORIGEN    varchar(20),
    NUMTRANSPORTE    varchar(20),
    OBSERVACIONES    varchar(50),
    OFICINAEXTERNO   varchar(9),
    DENOMOFIORIGEXT  varchar(300),
    PRESENCIAL       bool,
    REFEXT           varchar(16),
    RESERVA          varchar(4000),
    SOLICITA         text,
    TIPO_ANOTACION   varchar(2),
    TIPODOCFISICA    int8,
    TIPOENVIODOC     varchar(255),
    TRANSPORTE       int8,
    VERSION          varchar(255),
    CODASUNTO        int8,
    OFICINAORIG      int8,
    TIPOASUNTO       int8,
    primary key (ID)
);

create table RWE_REGISTRO_ENTRADA
(
    ID               int8         not null,
    DESTEXTCOD       varchar(9),
    DESTEXTDEN       varchar(300),
    ESTADO           int8         not null,
    EVENTO           int8,
    FECHA            timestamp    not null,
    NUMREGISTRO      int4         not null,
    NUMREGFORMAT     varchar(255) not null,
    DESTINO          int8,
    LIBRO            int8         not null,
    OFICINA          int8         not null,
    REGISTRO_DETALLE int8,
    USUARIO          int8         not null,
    primary key (ID)
);

create table RWE_REGISTRO_MIGRADO
(
    ID            int8          not null,
    ANO           int4          not null,
    ANOENTSAL     int4          not null,
    ANULADO       bool,
    CODIDIDOC     int4          not null,
    CODIDIEXT     int4          not null,
    CODOFICINA    int4          not null,
    CODOFIFIS     int4          not null,
    CODORGDESEMI  int4          not null,
    DENOFICINA    varchar(255)  not null,
    DENOFIFIS     varchar(60)   not null,
    DESCDOC       varchar(60)   not null,
    DESIDIDOC     varchar(15)   not null,
    DESORGDESEMI  varchar(60),
    DESREMDES     varchar(160)  not null,
    MAILREMITENTE varchar(50),
    EXTRACTO      varchar(2000) not null,
    FECHADOC      timestamp     not null,
    FECHAREG      timestamp     not null,
    FECHAVIS      timestamp,
    infoAdicional varchar(255),
    DESIDIEXT     varchar(15)   not null,
    NUMERO        int4          not null,
    NUMCORREO     varchar(8),
    NUMDISQUET    int4,
    NUMENTSAL     int4          not null,
    OTROS         varchar(255),
    PRODESGEO     varchar(50)   not null,
    PRODESGEOBAL  int4          not null,
    PRODESGEOFUE  varchar(50),
    TIPODOC       varchar(2)    not null,
    TREGISTRO     bool,
    IDENTIDAD     int8,
    primary key (ID),
    unique (ANO, NUMERO, CODOFICINA, TREGISTRO, IDENTIDAD)
);

create table RWE_REGISTRO_SALIDA
(
    ID               int8         not null,
    ESTADO           int8         not null,
    EVENTO           int8,
    FECHA            timestamp    not null,
    NUMREGISTRO      int4         not null,
    NUMREGFORMAT     varchar(255) not null,
    DESTEXTCOD       varchar(9),
    DESTEXTDEN       varchar(300),
    LIBRO            int8         not null,
    OFICINA          int8         not null,
    ORIGEN           int8,
    REGISTRO_DETALLE int8,
    USUARIO          int8         not null,
    primary key (ID)
);

create table RWE_REGISTRO_SIR
(
    ID               int8         not null,
    APLICACION       varchar(4),
    COD_ASUNTO       varchar(16),
    COD_ENT_REG      varchar(21)  not null,
    COD_ENT_REG_DEST varchar(21)  not null,
    COD_ENT_REG_INI  varchar(21)  not null,
    COD_ENT_REG_ORI  varchar(21)  not null,
    COD_ERROR        varchar(255),
    COD_UNI_TRA_DEST varchar(21),
    COD_UNI_TRA_ORI  varchar(21),
    CONTACTO_USUARIO varchar(160),
    DEC_ENT_REG_DEST varchar(80),
    DEC_ENT_REG_INI  varchar(80),
    DEC_ENT_REG_ORI  varchar(80),
    DEC_T_ANOTACION  varchar(80),
    DEC_UNI_TRA_DEST varchar(80),
    DEC_UNI_TRA_ORI  varchar(80),
    DESC_ERROR       varchar(2000),
    DOC_FISICA       varchar(1)   not null,
    ESTADO           int4         not null,
    EXPONE           text,
    FECHA_ESTADO     timestamp,
    FECHA_RECEPCION  timestamp,
    FECHAR_EGISTRO   timestamp    not null,
    ID_INTERCAMBIO   varchar(33)  not null,
    INDICADOR_PRUEBA int4         not null,
    NOMBRE_USUARIO   varchar(80),
    NUM_EXPEDIENTE   varchar(80),
    NUMERO_REGISTRO  varchar(20)  not null,
    REINTENTOS       int4,
    NUM_TRANSPORTE   varchar(20),
    OBSERVACIONES    varchar(50),
    REF_EXTERNA      varchar(16),
    RESUMEN          varchar(240) not null,
    SOLICITA         text,
    TIMESTAMP        text,
    TIPO_ANOTACION   varchar(2)   not null,
    TIPO_REGISTRO    int4         not null,
    TIPO_TRANSPORTE  varchar(2),
    ENTIDAD          int8         not null,
    primary key (ID)
);

create table RWE_RELORGOFI
(
    IDORGANISMO int8,
    IDOFICINA   int8,
    ESTADO      int8 not null,
    primary key (IDORGANISMO, IDOFICINA)
);

create table RWE_RELSIROFI
(
    IDORGANISMO int8,
    IDOFICINA   int8,
    ESTADO      int8 not null,
    primary key (IDORGANISMO, IDOFICINA)
);

create table RWE_REPRO
(
    ID             int8         not null,
    ACTIVO         bool         not null,
    NOMBRE         varchar(255) not null,
    ORDEN          int4         not null,
    REPRO          text,
    TIPOREGISTRO   int8         not null,
    USUARIOENTIDAD int8,
    primary key (ID)
);

create table RWE_ROL
(
    ID          int8         not null,
    DESCRIPCION varchar(255) not null,
    NOMBRE      varchar(255) not null unique,
    ORDEN       int4         not null,
    primary key (ID)
);

create table RWE_SESION
(
    ID            int8 not null,
    ESTADO        int8,
    FECHA         timestamp,
    IDSESION      int8 not null,
    NUMREG        varchar(255),
    TIPO_REGISTRO int8,
    USUARIO       int8 not null,
    primary key (ID)
);

create table RWE_TIPOASUNTO
(
    ID      int8        not null,
    ACTIVO  bool        not null,
    CODIGO  varchar(16) not null,
    ENTIDAD int8        not null,
    primary key (ID)
);

create table RWE_TIPODOCUMENTAL
(
    ID        int8 not null,
    CODIGONTI varchar(255),
    ENTIDAD   int8,
    primary key (ID)
);

create table RWE_TRAZABILIDAD
(
    ID                   int8      not null,
    FECHA                timestamp not null,
    tipo                 int8      not null,
    OFICIO_REMISION      int8,
    REGENT_DESTINO       int8,
    REGENT_ORIGEN        int8,
    REGISTRO_SALIDA      int8,
    REGISTRO_SALIDA_RECT int8,
    REGISTRO_SIR         int8,
    primary key (ID)
);

create table RWE_TRAZABILIDAD_SIR
(
    ID               int8        not null,
    APLICACION       varchar(4),
    COD_ENT_REG_DEST varchar(21) not null,
    COD_ENT_REG_ORI  varchar(21) not null,
    COD_UNI_TRA_DEST varchar(21),
    CONTACTO_USUARIO varchar(160),
    DEC_ENT_REG_DEST varchar(80),
    DEC_ENT_REG_ORI  varchar(80),
    DEC_UNI_TRA_DEST varchar(80),
    FECHA            timestamp   not null,
    NOMBRE_USUARIO   varchar(80),
    OBSERVACIONES    varchar(2000),
    tipo             int8        not null,
    REGISTRO_ENTRADA int8,
    REGISTRO_SIR     int8        not null,
    primary key (ID)
);

create table RWE_TRA_CODIGOASUNTO
(
    IDCODIGOASUNTO int8         not null,
    NOMBRE         varchar(255) not null,
    LANG           varchar(2),
    primary key (IDCODIGOASUNTO, LANG)
);

create table RWE_TRA_TDOCUMENTAL
(
    IDTDOCUMENTAL int8         not null,
    NOMBRE        varchar(255) not null,
    LANG          varchar(2),
    primary key (IDTDOCUMENTAL, LANG)
);

create table RWE_TRA_TIPOASUNTO
(
    IDTIPOASUNTO int8         not null,
    NOMBRE       varchar(255) not null,
    LANG         varchar(2),
    primary key (IDTIPOASUNTO, LANG)
);

create table RWE_USUARIO
(
    ID               int8         not null,
    APELLIDO1        varchar(255),
    APELLIDO2        varchar(255),
    DIB_USER_RW         bool         not null,
    DOCUMENTO        varchar(255),
    EMAIL            varchar(255) not null,
    IDENTIFICADOR    varchar(255) not null unique,
    IDIOMA           int8,
    NOMBRE           varchar(255) not null,
    RWE_ADMIN        bool         not null,
    RWE_SUPERADMIN   bool         not null,
    RWE_USUARI       bool         not null,
    RWE_WS_CIUDADANO bool         not null,
    RWE_WS_ENTRADA   bool         not null,
    RWE_WS_SALIDA    bool         not null,
    TIPOUSUARIO      int8,
    primary key (ID)
);

create table RWE_USUARIO_ENTIDAD
(
    ID            int8 not null,
    ACTIVO        bool not null,
    ENTIDAD       int8,
    ULTIMAOFICINA int8,
    USUARIO       int8,
    primary key (ID)
);

create
index RWE_ANEXO_REGDET_FK_I on RWE_ANEXO (REGISTRODETALLE);

    create
index RWE_ANEXO_TDOCAL_FK_I on RWE_ANEXO (TDOCUMENTAL);

alter table RWE_ANEXO
    add constraint RWE_ANEXO_REGDET_FK
        foreign key (REGISTRODETALLE)
            references RWE_REGISTRO_DETALLE;

alter table RWE_ANEXO
    add constraint RWE_ANEXO_TDOCAL_FK
        foreign key (TDOCUMENTAL)
            references RWE_TIPODOCUMENTAL;

alter table RWE_ANEXO_SIR
    add constraint RWE_ANEXOSIR_ANEXO_FK
        foreign key (ANEXO)
            references RWE_ARCHIVO;

alter table RWE_ANEXO_SIR
    add constraint RWE_ANEXOSIR_REGSIR_FK
        foreign key (REGISTRO_SIR)
            references RWE_REGISTRO_SIR;

create
index RWE_CATCOM_CATPAI_FK_I on RWE_CATCOMUNIDADAUTONOMA (PAIS);

alter table RWE_CATCOMUNIDADAUTONOMA
    add constraint RWE_CATCOMUNAUT_CATPAIS_FK
        foreign key (PAIS)
            references RWE_CATPAIS;

create
index RWE_CATLOC_CATENG_FK_I on RWE_CATLOCALIDAD (ENTIDADGEOGRAFICA);

    create
index RWE_CATLOC_CATPRO_FK_I on RWE_CATLOCALIDAD (PROVINCIA);

alter table RWE_CATLOCALIDAD
    add constraint RWE_CATLOCAL_CATPROVIN_FK
        foreign key (PROVINCIA)
            references RWE_CATPROVINCIA;

alter table RWE_CATLOCALIDAD
    add constraint RWE_CATLOCAL_CATENT_FK
        foreign key (ENTIDADGEOGRAFICA)
            references RWE_CATENTIDADGEOGRAFICA;

create
index RWE_CATPRO_CATCAU_FK_I on RWE_CATPROVINCIA (COMUNIDADAUTONOMA);

alter table RWE_CATPROVINCIA
    add constraint RWE_CATPROVINC_CATCOMUNAUTO_FK
        foreign key (COMUNIDADAUTONOMA)
            references RWE_CATCOMUNIDADAUTONOMA;

alter table RWE_CODIGOASUNTO
    add constraint RWE_CODASUNTO_ENTIDAD_FK
        foreign key (ENTIDAD)
            references RWE_ENTIDAD;

alter table RWE_COLA
    add constraint RWE_COLA_USUENTI_FK
        foreign key (USUARIOENTIDAD)
            references RWE_USUARIO_ENTIDAD;

alter table RWE_CONFIGURACION
    add constraint RWE_CONFIGURACION_LOGOMENU_FK
        foreign key (LOGOMENU)
            references RWE_ARCHIVO;

alter table RWE_CONFIGURACION
    add constraint RWE_CONFIGURACION_LOGOPIE_FK
        foreign key (LOGOPIE)
            references RWE_ARCHIVO;

create
index RWE_DESCAR_ENTIDA_FK_I on RWE_DESCARGA (ENTIDAD);

alter table RWE_DESCARGA
    add constraint RWE_DESCARGA_ENTIDAD_FK
        foreign key (ENTIDAD)
            references RWE_ENTIDAD;

create
index RWE_ENTIDA_PRO_FK_I on RWE_ENTIDAD (PROPIETARIO);

alter table RWE_ENTIDAD
    add constraint RWE_ENTIDAD_CONT_SIR_FK
        foreign key (CONTADOR_SIR)
            references RWE_CONTADOR;

alter table RWE_ENTIDAD
    add constraint RWE_ENTIDAD_LOGOSELLO_FK
        foreign key (LOGOSELLO)
            references RWE_ARCHIVO;

alter table RWE_ENTIDAD
    add constraint RWE_ENTIDAD_LOGOMENU_FK
        foreign key (LOGOMENU)
            references RWE_ARCHIVO;

alter table RWE_ENTIDAD
    add constraint RWE_ENTIDAD_LOGOPIE_FK
        foreign key (LOGOPIE)
            references RWE_ARCHIVO;

alter table RWE_ENTIDAD
    add constraint RWE_ENTIDAD_LIBRO_FK
        foreign key (LIBRO)
            references RWE_LIBRO;

alter table RWE_ENTIDAD
    add constraint RWE_ENTIDAD_USU_PROP_FK
        foreign key (PROPIETARIO)
            references RWE_USUARIO;

alter table RWE_ENTIDAD_USUENT
    add constraint RWE_ENTIDAD_USU_ADM_FK
        foreign key (IDUSUENT)
            references RWE_USUARIO_ENTIDAD;

alter table RWE_ENTIDAD_USUENT
    add constraint RWE_USU_ADM_ENTIDAD_FK
        foreign key (IDENTIDAD)
            references RWE_ENTIDAD;

alter table RWE_HISTORICOUO
    add constraint RWE_ORG_ORG_HISTANTE_FK
        foreign key (CODANTERIOR)
            references RWE_ORGANISMO;

alter table RWE_HISTORICOUO
    add constraint RWE_ORG_ORG_HISTULTI_FK
        foreign key (CODULTIMA)
            references RWE_ORGANISMO;

create
index RWE_HRE_REGENT_FK_I on RWE_HISTORICO_REGISTRO_ENTRADA (REGISTRO_ENTRADA);

    create
index RWE_HRE_USUENT_FK_I on RWE_HISTORICO_REGISTRO_ENTRADA (USUARIO);

alter table RWE_HISTORICO_REGISTRO_ENTRADA
    add constraint RWE_HISTORICO_USUARIO_FK
        foreign key (USUARIO)
            references RWE_USUARIO_ENTIDAD;

alter table RWE_HISTORICO_REGISTRO_ENTRADA
    add constraint RWE_HITORICO_RE_FK
        foreign key (REGISTRO_ENTRADA)
            references RWE_REGISTRO_ENTRADA;

alter table RWE_HISTORICO_REGISTRO_SALIDA
    add constraint RWE_HITORICO_RS_FK
        foreign key (REGISTRO_SALIDA)
            references RWE_REGISTRO_SALIDA;

alter table RWE_HISTORICO_REGISTRO_SALIDA
    add constraint RWE_HISTORICO_USUARIO_RS_FK
        foreign key (USUARIO)
            references RWE_USUARIO_ENTIDAD;

create
index RWE_INT_ENTIDAD_FK_I on RWE_INTEGRACION (ENTIDAD);

alter table RWE_INTEGRACION
    add constraint RWE_INT_ENTIDAD_FK
        foreign key (ENTIDAD)
            references RWE_ENTIDAD;

create
index RWE_INTERES_CATPAI_FK_I on RWE_INTERESADO (PAIS);

    create
index RWE_INTERES_CATLOC_FK_I on RWE_INTERESADO (LOCALIDAD);

    create
index RWE_INTERES_REPADO_FK_I on RWE_INTERESADO (REPRESENTADO);

    create
index RWE_INTERES_REPANT_FK_I on RWE_INTERESADO (REPRESENTANTE);

alter table RWE_INTERESADO
    add constraint RWE_INTERESADO_REPREANTE_FK
        foreign key (REPRESENTANTE)
            references RWE_INTERESADO;

alter table RWE_INTERESADO
    add constraint RWE_INTERESADO_REPRESENT_FK
        foreign key (REPRESENTADO)
            references RWE_INTERESADO;

alter table RWE_INTERESADO
    add constraint RWE_INTERESADO_PAIS_FK
        foreign key (PAIS)
            references RWE_CATPAIS;

alter table RWE_INTERESADO
    add constraint RWE_INTERESADO_PROVINCIA_FK
        foreign key (PROVINCIA)
            references RWE_CATPROVINCIA;

alter table RWE_INTERESADO
    add constraint RWE_INTERESADO_REGDET_FK
        foreign key (REGISTRODETALLE)
            references RWE_REGISTRO_DETALLE;

alter table RWE_INTERESADO
    add constraint RWE_INTERESADO_LOCALIDAD_FK
        foreign key (LOCALIDAD)
            references RWE_CATLOCALIDAD;

alter table RWE_INTERESADO_SIR
    add constraint RWE_INTERESADOSIR_REGSIR_FK
        foreign key (REGISTRO_SIR)
            references RWE_REGISTRO_SIR;

create
index RWE_LIBRO_CONSAL_FK_I on RWE_LIBRO (CONTADOR_SALIDA);

    create
index RWE_LIBRO_ORGANI_FK_I on RWE_LIBRO (ORGANISMO);

    create
index RWE_LIBRO_CONOFI_FK_I on RWE_LIBRO (CONTADOR_OFICIO_REMISION);

    create
index RWE_LIBRO_CONENT_FK_I on RWE_LIBRO (CONTADOR_ENTRADA);

alter table RWE_LIBRO
    add constraint RWE_LIBRO_CONT_SIR_FK
        foreign key (CONTADOR_SIR)
            references RWE_CONTADOR;

alter table RWE_LIBRO
    add constraint RWE_LIBRO_CONT_SAL_FK
        foreign key (CONTADOR_SALIDA)
            references RWE_CONTADOR;

alter table RWE_LIBRO
    add constraint RWE_LIBRO_CONT_ENT_FK
        foreign key (CONTADOR_ENTRADA)
            references RWE_CONTADOR;

alter table RWE_LIBRO
    add constraint RWE_LIBRO_ORGANISMO_FK
        foreign key (ORGANISMO)
            references RWE_ORGANISMO;

alter table RWE_LIBRO
    add constraint RWE_LIBRO_CONT_ORM_FK
        foreign key (CONTADOR_OFICIO_REMISION)
            references RWE_CONTADOR;

create
index RWE_LOPD_USUENT_FK_I on RWE_LOPD (USUARIO);

    create
index RWE_LOPD_LIBRO_FK_I on RWE_LOPD (LIBRO);

alter table RWE_LOPD
    add constraint RWE_LOPD_USUENT_FK
        foreign key (USUARIO)
            references RWE_USUARIO_ENTIDAD;

alter table RWE_LOPD
    add constraint RWE_LOPD_LIBRO_FK
        foreign key (LIBRO)
            references RWE_LIBRO;

alter table RWE_MENSAJE_CONTROL
    add constraint RWE_MC_ENTIDAD_FK
        foreign key (ENTIDAD)
            references RWE_ENTIDAD;

create
index RWE_MODOFI_ENTIDA_FK_I on RWE_MODELO_OFICIO_REMISION (ENTIDAD);

    create
index RWE_MODOFI_ARCHIV_FK_I on RWE_MODELO_OFICIO_REMISION (MODELO);

alter table RWE_MODELO_OFICIO_REMISION
    add constraint RWE_MODELOFREMISION_ENTIDAD_FK
        foreign key (ENTIDAD)
            references RWE_ENTIDAD;

alter table RWE_MODELO_OFICIO_REMISION
    add constraint RWE_MODELOFREMISION_MODELO_FK
        foreign key (MODELO)
            references RWE_ARCHIVO;

create
index RWE_MODREB_ENTIDA_FK_I on RWE_MODELO_RECIBO (ENTIDAD);

    create
index RWE_MODREB_ARCHIV_FK_I on RWE_MODELO_RECIBO (MODELO);

alter table RWE_MODELO_RECIBO
    add constraint RWE_MODELRECIBO_ENTIDAD_FK
        foreign key (ENTIDAD)
            references RWE_ENTIDAD;

alter table RWE_MODELO_RECIBO
    add constraint RWE_MODELRECIBO_ARCHIVO_FK
        foreign key (MODELO)
            references RWE_ARCHIVO;

alter table RWE_MODIFICACIONLOPD_MIGRADO
    add constraint RWE_MODLOPDMIG_REGMIG_FK
        foreign key (REGMIG)
            references RWE_REGISTRO_MIGRADO;

create
index RWE_NOTIF_DEST_FK_I on RWE_NOTIFICACION (DESTINATARIO);

    create
index RWE_NOTIF_REMIT_FK_I on RWE_NOTIFICACION (REMITENTE);

alter table RWE_NOTIFICACION
    add constraint RWE_NOTIF_DEST_FK
        foreign key (DESTINATARIO)
            references RWE_USUARIO_ENTIDAD;

alter table RWE_NOTIFICACION
    add constraint RWE_NOTIF_REMIT_FK
        foreign key (REMITENTE)
            references RWE_USUARIO_ENTIDAD;

create
index RWE_OFICIN_PAIS_FK_I on RWE_OFICINA (PAIS);

    create
index RWE_OFICIN_ESTENT_FK_I on RWE_OFICINA (ESTADO);

    create
index RWE_OFICIN_COMUNI_FK_I on RWE_OFICINA (COMUNIDAD);

    create
index RWE_OFICIN_TVIA_FK_I on RWE_OFICINA (TIPOVIA);

    create
index RWE_OFICIN_LOCALI_FK_I on RWE_OFICINA (LOCALIDAD);

    create
index RWE_OFICIN_OFICIN_FK_I on RWE_OFICINA (OFICINARESPONSABLE);

    create
index RWE_OFICIN_ORGANI_FK_I on RWE_OFICINA (ORGANISMORESPONSABLE);

alter table RWE_OFICINA
    add constraint RWE_OFICINA_TIPOVIA_FK
        foreign key (TIPOVIA)
            references RWE_CATTIPOVIA;

alter table RWE_OFICINA
    add constraint RWE_OFICINA_PAIS_FK
        foreign key (PAIS)
            references RWE_CATPAIS;

alter table RWE_OFICINA
    add constraint RWE_OFICINA_ESTADO_FK
        foreign key (ESTADO)
            references RWE_CATESTADOENTIDAD;

alter table RWE_OFICINA
    add constraint RWE_OFICINA_LOCALIDAD_FK
        foreign key (LOCALIDAD)
            references RWE_CATLOCALIDAD;

alter table RWE_OFICINA
    add constraint RWE_OFICINA_ORGANISMO_FK
        foreign key (ORGANISMORESPONSABLE)
            references RWE_ORGANISMO;

alter table RWE_OFICINA
    add constraint RWE_OFICINA_OFICINA_FK
        foreign key (OFICINARESPONSABLE)
            references RWE_OFICINA;

alter table RWE_OFICINA
    add constraint RWE_OFICINA_COMUNIDAD_FK
        foreign key (COMUNIDAD)
            references RWE_CATCOMUNIDADAUTONOMA;

alter table RWE_OFICINA_SERVICIO
    add constraint RWE_SERVICIO_OFICINA_FK
        foreign key (IDOFICINA)
            references RWE_OFICINA;

alter table RWE_OFICINA_SERVICIO
    add constraint RWE_OFICINA_SERVICIO_FK
        foreign key (IDSERVICIO)
            references RWE_CATSERVICIO;

create
index RWE_OFIREM_ORGANI_FK_I on RWE_OFICIO_REMISION (ORGANISMODEST);

    create
index RWE_OFIREM_OFICIN_FK_I on RWE_OFICIO_REMISION (OFICINA);

    create
index RWE_OFIREM_LIBRO_FK_I on RWE_OFICIO_REMISION (LIBRO);

    create
index RWE_OFIREM_USUARI_FK_I on RWE_OFICIO_REMISION (USUARIO);

alter table RWE_OFICIO_REMISION
    add constraint RWE_OFIREM_USUORM_FK
        foreign key (USUARIO)
            references RWE_USUARIO_ENTIDAD;

alter table RWE_OFICIO_REMISION
    add constraint RWE_OFIREM_ORGANISMODEST_FK
        foreign key (ORGANISMODEST)
            references RWE_ORGANISMO;

alter table RWE_OFICIO_REMISION
    add constraint RWE_OFIREM_OFICINA_FK
        foreign key (OFICINA)
            references RWE_OFICINA;

alter table RWE_OFICIO_REMISION
    add constraint RWE_OFIREM_LIBRO_FK
        foreign key (LIBRO)
            references RWE_LIBRO;

alter table RWE_OFIREM_REGENT
    add constraint RWE_OFIREM_REGENT_FK
        foreign key (IDREGENT)
            references RWE_REGISTRO_ENTRADA;

alter table RWE_OFIREM_REGENT
    add constraint RWE_REGENT_OFIREM_FK
        foreign key (IDOFIREM)
            references RWE_OFICIO_REMISION;

alter table RWE_OFIREM_REGSAL
    add constraint RWE_REGSAL_OFIREM_FK
        foreign key (IDOFIREM)
            references RWE_OFICIO_REMISION;

alter table RWE_OFIREM_REGSAL
    add constraint RWE_OFIREM_REGSAL_FK
        foreign key (IDREGSAL)
            references RWE_REGISTRO_SALIDA;

create
index RWE_ORGANI_SUPERI_FK_I on RWE_ORGANISMO (ORGANISMOSUPERIOR);

    create
index RWE_ORGANI_TVIA_FK_I on RWE_ORGANISMO (TIPOVIA);

    create
index RWE_ORGANI_LOCALI_FK_I on RWE_ORGANISMO (LOCALIDAD);

    create
index RWE_ORGANI_EDP_FK_I on RWE_ORGANISMO (EDPRINCIPAL);

    create
index RWE_ORGANI_PROVIN_FK_I on RWE_ORGANISMO (CODAMBPROVINCIA);

    create
index RWE_ORGANI_ESTADO_FK_I on RWE_ORGANISMO (ESTADO);

    create
index RWE_ORGANI_ENTIDA_FK_I on RWE_ORGANISMO (ENTIDAD);

    create
index RWE_ORGANI_RAIZ_FK_I on RWE_ORGANISMO (ORGANISMORAIZ);

    create
index RWE_ORGANI_CAUTON_FK_I on RWE_ORGANISMO (CODAMBCOMUNIDAD);

    create
index RWE_ORGANI_PAIS_FK_I on RWE_ORGANISMO (PAIS);

alter table RWE_ORGANISMO
    add constraint RWE_ORGANISMO_ORGRAIZ_FK
        foreign key (ORGANISMORAIZ)
            references RWE_ORGANISMO;

alter table RWE_ORGANISMO
    add constraint RWE_ORGANISMO_TIPOVIA_FK
        foreign key (TIPOVIA)
            references RWE_CATTIPOVIA;

alter table RWE_ORGANISMO
    add constraint RWE_ORGANISMO_ENTIDAD_FK
        foreign key (ENTIDAD)
            references RWE_ENTIDAD;

alter table RWE_ORGANISMO
    add constraint RWE_ORGANISMO_CATPROVINCIA_FK
        foreign key (CODAMBPROVINCIA)
            references RWE_CATPROVINCIA;

alter table RWE_ORGANISMO
    add constraint RWE_ORGANISMO_PAIS_FK
        foreign key (PAIS)
            references RWE_CATPAIS;

alter table RWE_ORGANISMO
    add constraint RWE_ORGANISMO_CATAMBCOMAUTO_FK
        foreign key (CODAMBCOMUNIDAD)
            references RWE_CATCOMUNIDADAUTONOMA;

alter table RWE_ORGANISMO
    add constraint RWE_ORGANISMO_ORG_SUPERIOR_FK
        foreign key (ORGANISMOSUPERIOR)
            references RWE_ORGANISMO;

alter table RWE_ORGANISMO
    add constraint RWE_ORGANISMO_CATNIVELADMIN_FK
        foreign key (NIVELADMINISTRACION)
            references RWE_CATNIVELADMINISTRACION;

alter table RWE_ORGANISMO
    add constraint RWE_ORGANISMO_EDPRIN_FK
        foreign key (EDPRINCIPAL)
            references RWE_ORGANISMO;

alter table RWE_ORGANISMO
    add constraint RWE_ORGANISMO_ESTADO_FK
        foreign key (ESTADO)
            references RWE_CATESTADOENTIDAD;

alter table RWE_ORGANISMO
    add constraint RWE_ORGANISMO_LOCALIDAD_FK
        foreign key (LOCALIDAD)
            references RWE_CATLOCALIDAD;

create
index RWE_PELIUS_USUARI_FK_I on RWE_PERMLIBUSU (USUARIO);

    create
index RWE_PELIUS_LIBRO_FK_I on RWE_PERMLIBUSU (LIBRO);

alter table RWE_PERMLIBUSU
    add constraint RWE_PERMLIBUSU_USUENT_FK
        foreign key (USUARIO)
            references RWE_USUARIO_ENTIDAD;

alter table RWE_PERMLIBUSU
    add constraint RWE_PERMLIBUSU_LIBRO_FK
        foreign key (LIBRO)
            references RWE_LIBRO;

create
index RWE_POU_ORG_FK_I on RWE_PERMORGUSU (ORGANISMO);

    create
index RWE_POU_USUARI_FK_I on RWE_PERMORGUSU (USUARIO);

alter table RWE_PERMORGUSU
    add constraint RWE_POU_USUENT_FK
        foreign key (USUARIO)
            references RWE_USUARIO_ENTIDAD;

alter table RWE_PERMORGUSU
    add constraint RWE_POU_ORG_FK
        foreign key (ORGANISMO)
            references RWE_ORGANISMO;

create
index RWE_PERSONA_ENTIDAD_FK_I on RWE_PERSONA (ENTIDAD);

alter table RWE_PERSONA
    add constraint RWE_PERSONA_ENTIDAD_FK
        foreign key (ENTIDAD)
            references RWE_ENTIDAD;

alter table RWE_PERSONA
    add constraint RWE_PERSONA_PAIS_FK
        foreign key (PAIS)
            references RWE_CATPAIS;

alter table RWE_PERSONA
    add constraint RWE_PERSONA_PROVINCIA_FK
        foreign key (PROVINCIA)
            references RWE_CATPROVINCIA;

alter table RWE_PERSONA
    add constraint RWE_PERSONA_LOCALIDAD_FK
        foreign key (LOCALIDAD)
            references RWE_CATLOCALIDAD;

create
index RWE_PLUGI_ENTIDA_FK_I on RWE_PLUGIN (ENTIDAD);

    create
index RWE_PROPIE_ENTIDA_FK_I on RWE_PROPIEDADGLOBAL (ENTIDAD);

alter table RWE_REGISTROLOPD_MIGRADO
    add constraint RWE_REGLOPDMIG_REGMIG_FK
        foreign key (REGMIG)
            references RWE_REGISTRO_MIGRADO;

alter table RWE_REGISTRO_DETALLE
    add constraint RWE_REGDET_OFICINAORIG_FK
        foreign key (OFICINAORIG)
            references RWE_OFICINA;

alter table RWE_REGISTRO_DETALLE
    add constraint RWE_REGDET_CODASUNTO_FK
        foreign key (CODASUNTO)
            references RWE_CODIGOASUNTO;

alter table RWE_REGISTRO_DETALLE
    add constraint RWE_REGDET_TIPOASUNTO_FK
        foreign key (TIPOASUNTO)
            references RWE_TIPOASUNTO;

alter table RWE_REGISTRO_ENTRADA
    add constraint RWE_REGENT_DESTINO_FK
        foreign key (DESTINO)
            references RWE_ORGANISMO;

alter table RWE_REGISTRO_ENTRADA
    add constraint RWE_REGENT_USUENT_FK
        foreign key (USUARIO)
            references RWE_USUARIO_ENTIDAD;

alter table RWE_REGISTRO_ENTRADA
    add constraint RWE_REGENT_REGDET_FK
        foreign key (REGISTRO_DETALLE)
            references RWE_REGISTRO_DETALLE;

alter table RWE_REGISTRO_ENTRADA
    add constraint RWE_REGENT_OFICINA_FK
        foreign key (OFICINA)
            references RWE_OFICINA;

alter table RWE_REGISTRO_ENTRADA
    add constraint RWE_REGENT_LIBRO_FK
        foreign key (LIBRO)
            references RWE_LIBRO;

create
index RWE_REGMIG_FECREG_I on RWE_REGISTRO_MIGRADO (FECHAREG);

    create
index RWE_REGMIG_CODOF_I on RWE_REGISTRO_MIGRADO (CODOFICINA);

    create
index RWE_REGMIG_EXTR_I on RWE_REGISTRO_MIGRADO (EXTRACTO);

    create
index RWE_REGMIG_NUM_I on RWE_REGISTRO_MIGRADO (NUMERO);

    create
index RWE_REGMIG_REMDES_I on RWE_REGISTRO_MIGRADO (DESREMDES);

    create
index RWE_REGMIG_ANO_I on RWE_REGISTRO_MIGRADO (ANO);

    create
index RWE_REGMIG_TREG_I on RWE_REGISTRO_MIGRADO (TREGISTRO);

alter table RWE_REGISTRO_MIGRADO
    add constraint RWE_REGMIG_ENTIDAD_FK
        foreign key (IDENTIDAD)
            references RWE_ENTIDAD;

alter table RWE_REGISTRO_SALIDA
    add constraint RWE_REGSAL_USUSAL_FK
        foreign key (USUARIO)
            references RWE_USUARIO_ENTIDAD;

alter table RWE_REGISTRO_SALIDA
    add constraint RWE_REGSAL_REGDET_FK
        foreign key (REGISTRO_DETALLE)
            references RWE_REGISTRO_DETALLE;

alter table RWE_REGISTRO_SALIDA
    add constraint RWE_REGSAL_ORIGEN_FK
        foreign key (ORIGEN)
            references RWE_ORGANISMO;

alter table RWE_REGISTRO_SALIDA
    add constraint RWE_REGSAL_OFICINA_FK
        foreign key (OFICINA)
            references RWE_OFICINA;

alter table RWE_REGISTRO_SALIDA
    add constraint RWE_REGSAL_LIBRO_FK
        foreign key (LIBRO)
            references RWE_LIBRO;

alter table RWE_REGISTRO_SIR
    add constraint RWE_RES_ENTIDAD_FK
        foreign key (ENTIDAD)
            references RWE_ENTIDAD;

alter table RWE_RELORGOFI
    add constraint RWE_RELORGOFI_ORGANISMO_FK
        foreign key (IDORGANISMO)
            references RWE_ORGANISMO;

alter table RWE_RELORGOFI
    add constraint RWE_OFICINA_RELORGOFI_FK
        foreign key (IDOFICINA)
            references RWE_OFICINA;

alter table RWE_RELORGOFI
    add constraint RWE_RELORGANOFI_CATESTENT_FK
        foreign key (ESTADO)
            references RWE_CATESTADOENTIDAD;

alter table RWE_RELSIROFI
    add constraint RWE_RELSIROFI_ORGANISMO_FK
        foreign key (IDORGANISMO)
            references RWE_ORGANISMO;

alter table RWE_RELSIROFI
    add constraint RWE_RELSIROFI_OFICINA_FK
        foreign key (IDOFICINA)
            references RWE_OFICINA;

alter table RWE_RELSIROFI
    add constraint RWE_RELSIROFI_CATESTENTI_FK
        foreign key (ESTADO)
            references RWE_CATESTADOENTIDAD;

alter table RWE_REPRO
    add constraint RWE_REPRO_USUARIO_FK
        foreign key (USUARIOENTIDAD)
            references RWE_USUARIO_ENTIDAD;

create
index RWE_SESION_USUENT_FK_I on RWE_SESION (USUARIO);

alter table RWE_SESION
    add constraint RWE_SESION_USUENT_FK
        foreign key (USUARIO)
            references RWE_USUARIO_ENTIDAD;

alter table RWE_TIPOASUNTO
    add constraint RWE_TIPOASUNTO_ENTIDAD_FK
        foreign key (ENTIDAD)
            references RWE_ENTIDAD;

alter table RWE_TIPODOCUMENTAL
    add constraint RWE_TIPODOCUMENTAL_ENTIDAD_FK
        foreign key (ENTIDAD)
            references RWE_ENTIDAD;

alter table RWE_TRAZABILIDAD
    add constraint RWE_TRAZAB_REGSAL_FK
        foreign key (REGISTRO_SALIDA)
            references RWE_REGISTRO_SALIDA;

alter table RWE_TRAZABILIDAD
    add constraint RWE_TRAZAB_REGENTO_FK
        foreign key (REGENT_ORIGEN)
            references RWE_REGISTRO_ENTRADA;

alter table RWE_TRAZABILIDAD
    add constraint RWE_TRAZAB_OFIREM_FK
        foreign key (OFICIO_REMISION)
            references RWE_OFICIO_REMISION;

alter table RWE_TRAZABILIDAD
    add constraint RWE_TRAZAB_RGSRCT_FK
        foreign key (REGISTRO_SALIDA_RECT)
            references RWE_REGISTRO_SALIDA;

alter table RWE_TRAZABILIDAD
    add constraint RWE_TRAZAB_REGENTD_FK
        foreign key (REGENT_DESTINO)
            references RWE_REGISTRO_ENTRADA;

alter table RWE_TRAZABILIDAD
    add constraint RWE_TRAZAB_REGSIR_FK
        foreign key (REGISTRO_SIR)
            references RWE_REGISTRO_SIR;

alter table RWE_TRAZABILIDAD_SIR
    add constraint RWE_TRASIR_REGENT_FK
        foreign key (REGISTRO_ENTRADA)
            references RWE_REGISTRO_ENTRADA;

alter table RWE_TRAZABILIDAD_SIR
    add constraint RWE_TRASIR_REGSIR_FK
        foreign key (REGISTRO_SIR)
            references RWE_REGISTRO_SIR;

alter table RWE_TRA_CODIGOASUNTO
    add constraint RWE_CODASUNTO_TRACODASUNTO_FK
        foreign key (IDCODIGOASUNTO)
            references RWE_CODIGOASUNTO;

alter table RWE_TRA_TDOCUMENTAL
    add constraint RWE_TIPODOC_TRATIPODOC_FK
        foreign key (IDTDOCUMENTAL)
            references RWE_TIPODOCUMENTAL;

alter table RWE_TRA_TIPOASUNTO
    add constraint RWE_TASUNTO_TRATASUNTO_FK
        foreign key (IDTIPOASUNTO)
            references RWE_TIPOASUNTO;

alter table RWE_USUARIO_ENTIDAD
    add constraint RWE_USUENT_ENTIDAD_FK
        foreign key (ENTIDAD)
            references RWE_ENTIDAD;

alter table RWE_USUARIO_ENTIDAD
    add constraint RWE_USUENT_USUARIO_FK
        foreign key (USUARIO)
            references RWE_USUARIO;

alter table RWE_USUARIO_ENTIDAD
    add constraint RWE_USUENT_OFICINA_FK
        foreign key (ULTIMAOFICINA)
            references RWE_OFICINA;

create sequence RWE_ALL_SEQ;

create sequence RWE_POU_SEQ;

create sequence RWE_SESION_SEQ;

create sequence RWE_SIR_SEQ;

create sequence RWE_COLA_SEQ;

create sequence RWE_INT_SEQ;