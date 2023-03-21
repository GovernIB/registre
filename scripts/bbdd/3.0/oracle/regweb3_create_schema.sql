create sequence RWE_ALL_SEQ start with 1 increment by  1;
create sequence RWE_COLA_SEQ start with 1 increment by  1;
create sequence RWE_INT_SEQ start with 1 increment by  1;
create sequence RWE_POU_SEQ start with 1 increment by  1;
create sequence RWE_SESION_SEQ start with 1 increment by  1;
create sequence RWE_SIR_SEQ start with 1 increment by  1;

    create table RWE_ANEXO (
       ID number(19,0) not null,
        CERTIFICADO raw(2000),
        CONFIDENCIAL number(1,0),
        CSV varchar2(255 char),
        CUSTODIAID varchar2(256 char),
        CUSTODIADO number(1,0),
        ESTADOFIRMA number(10,0),
        EXPEDIENTEID varchar2(256 char),
        FECHACAPTURA timestamp not null,
        FECHAVALIDACION timestamp,
        FIRMA CLOB,
        FIRMAVALIDA number(1,0),
        HASH raw(2000),
        JUSTIFICANTE number(1,0) not null,
        MODOFIRMA number(10,0),
        MOTIVONOVALID varchar2(255 char),
        NOMBRE_FICHERO varchar2(200 char),
        OBSERVACIONES varchar2(50 char),
        ORIGEN number(10,0),
        PERFIL_CUSTODIA number(19,0),
        PURGADO number(1,0),
        SCAN number(1,0),
        SIGNFORMAT varchar2(255 char),
        SIGNPROFILE varchar2(255 char),
        SIGNTYPE varchar2(255 char),
        TAMANO_FICHERO number(10,0),
        TIMESTAMP raw(2000),
        TIPODOC number(19,0),
        TITULO varchar2(200 char) not null,
        VAL_OCSP_CERTIFICADO raw(2000),
        TVALDOC number(19,0),
        ENTIDAD number(19,0),
        REGISTRODETALLE number(19,0),
        TDOCUMENTAL number(19,0),
        primary key (ID)
    );

    create table RWE_ANEXO_SIR (
       ID number(19,0) not null,
        CERTIFICADO CLOB,
        FIRMA CLOB,
        HASH CLOB not null,
        ID_DOCUMENTO_FIRMADO varchar2(50 char),
        IDENTIFICADOR_FICHERO varchar2(50 char) not null,
        NOMBRE_FICHERO varchar2(80 char) not null,
        OBSERVACIONES varchar2(50 char),
        PURGADO number(1,0) not null,
        TIMESTAMP CLOB,
        TIPO_DOCUMENTO varchar2(2 char) not null,
        TIPO_MIME varchar2(20 char),
        VAL_OCSP_CE CLOB,
        VALIDEZ_DOCUMENTO varchar2(2 char),
        ANEXO number(19,0),
        ENTIDAD number(19,0),
        REGISTRO_SIR number(19,0),
        primary key (ID)
    );

    create table RWE_ARCHIVO (
       ID number(19,0) not null,
        MIME varchar2(255 char) not null,
        NOMBRE varchar2(255 char) not null,
        TAMANO number(19,0) not null,
        primary key (ID)
    );

    create table RWE_CATCOMUNIDADAUTONOMA (
       ID number(19,0) not null,
        C_CODIGO_DIR2 number(19,0),
        C_COMUNIDAD_RPC varchar2(2 char),
        CODIGOCOMUNIDAD number(19,0) not null,
        DESCRIPCIONCOMUNIDAD varchar2(50 char) not null,
        PAIS number(19,0),
        primary key (ID)
    );

    create table RWE_CATENTIDADGEOGRAFICA (
       ID number(19,0) not null,
        CODIGOENTIDADGEOGRAFICA varchar2(2 char) not null,
        DESCRIPCIONENTIDADGEOGRAFICA varchar2(50 char) not null,
        primary key (ID)
    );

    create table RWE_CATESTADOENTIDAD (
       ID number(19,0) not null,
        CODIGOESTADOENTIDAD varchar2(2 char) not null,
        DESCRIPCIONESTADOENTIDAD varchar2(50 char) not null,
        primary key (ID)
    );

    create table RWE_CATLOCALIDAD (
       ID number(19,0) not null,
        CODIGOLOCALIDAD number(19,0) not null,
        DESCRIPCIONLOCALIDAD varchar2(50 char) not null,
        ENTIDADGEOGRAFICA number(19,0),
        PROVINCIA number(19,0),
        primary key (ID)
    );

    create table RWE_CATNIVELADMINISTRACION (
       ID number(19,0) not null,
        CODIGONIVELADMINISTRACION number(19,0) not null,
        DESCRIPCIONNIVELADMINISTRACION varchar2(300 char) not null,
        primary key (ID)
    );

    create table RWE_CATPAIS (
       ID number(19,0) not null,
        ALFA2PAIS varchar2(2 char),
        ALFA3PAIS varchar2(3 char),
        CODIGOPAIS number(19,0) not null,
        DESCRIPCIONPAIS varchar2(100 char) not null,
        primary key (ID)
    );

    create table RWE_CATPROVINCIA (
       ID number(19,0) not null,
        CODIGOPROVINCIA number(19,0) not null,
        DESCRIPCIONPROVINCIA varchar2(50 char) not null,
        COMUNIDADAUTONOMA number(19,0),
        primary key (ID)
    );

    create table RWE_CATSERVICIO (
       ID number(19,0) not null,
        CODIGOSERVICIO number(19,0) not null,
        DESCRIPCIONSERVICIO varchar2(300 char) not null,
        primary key (ID)
    );

    create table RWE_CATTIPOVIA (
       ID number(19,0) not null,
        CODIGOTIPOVIA number(19,0) not null,
        DESCRIPCIONTIPOVIA varchar2(300 char) not null,
        primary key (ID)
    );

    create table RWE_CODIGOASUNTO (
       ID number(19,0) not null,
        ACTIVO number(1,0) not null,
        CODIGO varchar2(16 char) not null,
        ENTIDAD number(19,0) not null,
        primary key (ID)
    );

    create table RWE_COLA (
       ID number(19,0) not null,
        DENOMINACIONOFICINA varchar2(255 char),
        DESCRIPCIONOBJETO varchar2(255 char),
        ERROR CLOB,
        ESTADO number(19,0),
        FECHA timestamp,
        FECHAPROCESADO timestamp,
        IDOBJETO number(19,0),
        NUMMAXREINTENTOS number(10,0),
        NUMREINTENTOS number(10,0),
        TIPO number(19,0),
        TIPOREGISTRO number(19,0),
        USUARIOENTIDAD number(19,0) not null,
        primary key (ID)
    );

    create table RWE_CONFIGURACION (
       ID number(19,0) not null,
        COLORMENU varchar2(255 char),
        TEXTOPIE varchar2(4000 char),
        LOGOMENU number(19,0),
        LOGOPIE number(19,0),
        primary key (ID)
    );

    create table RWE_CONTADOR (
       ID number(19,0) not null,
        NUMERO number(10,0) not null,
        primary key (ID)
    );

    create table RWE_DESCARGA (
       ID number(19,0) not null,
        FECHAFIN timestamp,
        FECHAIMPORTACION timestamp,
        FECHAINICIO timestamp,
        TIPO varchar2(255 char),
        ENTIDAD number(19,0),
        primary key (ID)
    );

    create table RWE_ENTIDAD (
       ID number(19,0) not null,
        ACTIVO number(1,0) not null,
        CODIGODIR3 varchar2(255 char) not null,
        COLORMENU varchar2(255 char),
        CONFIGPERSONA number(19,0),
        DESCRIPCION varchar2(4000 char) not null,
        DIASVISADO number(10,0),
        MANTENIMIENTO number(1,0) not null,
        NOMBRE varchar2(255 char) not null,
        NUMREGISTRO varchar2(4000 char),
        OFICIOREMISION number(1,0) not null,
        PERFIL_CUSTODIA number(19,0),
        POSXSELLO number(10,0),
        POSYSELLO number(10,0),
        SELLO varchar2(4000 char),
        SIR number(1,0) not null,
        TEXTOPIE varchar2(4000 char),
        TIPSCAN varchar2(20 char),
        CONTADOR_SIR number(19,0),
        LIBRO number(19,0),
        LOGOMENU number(19,0),
        LOGOPIE number(19,0),
        LOGOSELLO number(19,0),
        PROPIETARIO number(19,0),
        primary key (ID)
    );

    create table RWE_ENTIDAD_USUENT (
       IDENTIDAD number(19,0) not null,
        IDUSUENT number(19,0) not null,
        primary key (IDENTIDAD, IDUSUENT)
    );

    create table RWE_HISTORICO_REGISTRO_ENTRADA (
       ID number(19,0) not null,
        ESTADO number(19,0),
        FECHA timestamp not null,
        MODIFICACION varchar2(255 char) not null,
        RE_ORIGINAL CLOB,
        REGISTRO_ENTRADA number(19,0),
        USUARIO number(19,0),
        primary key (ID)
    );

    create table RWE_HISTORICO_REGISTRO_SALIDA (
       ID number(19,0) not null,
        ESTADO number(19,0),
        FECHA timestamp not null,
        MODIFICACION varchar2(255 char) not null,
        RS_ORIGINAL CLOB,
        REGISTRO_SALIDA number(19,0),
        USUARIO number(19,0),
        primary key (ID)
    );

    create table RWE_HISTORICOUO (
       CODANTERIOR number(19,0) not null,
        CODULTIMA number(19,0) not null,
        primary key (CODANTERIOR, CODULTIMA)
    );

    create table RWE_INTEGRACION (
       ID number(19,0) not null,
        DESCRIPCION varchar2(400 char),
        ERROR CLOB,
        ESTADO number(19,0) not null,
        EXCEPCION CLOB,
        FECHA timestamp,
        NUMREGFORMAT varchar2(255 char),
        PETICION varchar2(2000 char),
        TIEMPO number(19,0) not null,
        TIPO number(19,0) not null,
        ENTIDAD number(19,0),
        primary key (ID)
    );

    create table RWE_INTERESADO (
       ID number(19,0) not null,
        APELLIDO1 varchar2(255 char),
        APELLIDO2 varchar2(255 char),
        CANALNOTIF number(19,0),
        CODIGODIR3 varchar2(255 char),
        CODIGODIRE varchar2(15 char),
        CP varchar2(5 char),
        DIRECCION varchar2(160 char),
        DIRELECTRONICA varchar2(160 char),
        DOCUMENTO varchar2(17 char),
        EMAIL varchar2(160 char),
        ISREPRESENTANTE number(1,0),
        NOMBRE varchar2(255 char),
        OBSERVACIONES varchar2(160 char),
        RAZONSOCIAL varchar2(2000 char),
        TELEFONO varchar2(20 char),
        TIPOINTERESADO number(19,0),
        TIPODOCIDENT number(19,0),
        LOCALIDAD number(19,0),
        PAIS number(19,0),
        PROVINCIA number(19,0),
        REGISTRODETALLE number(19,0),
        REPRESENTADO number(19,0),
        REPRESENTANTE number(19,0),
        primary key (ID)
    );

    create table RWE_INTERESADO_SIR (
       ID number(19,0) not null,
        CANAL_NOTIF_INTERESADO varchar2(2 char),
        CANAL_NOTIF_REPRESENTANTE varchar2(2 char),
        COD_MUNICIPIO_INTERESADO varchar2(5 char),
        COD_MUNICIPIO_REPRESENTANTE varchar2(5 char),
        COD_PAIS_INTERESADO varchar2(4 char),
        COD_PAIS_REPRESENTANTE varchar2(4 char),
        CP_INTERESADO varchar2(5 char),
        CP_REPRESENTANTE varchar2(5 char),
        COD_PROVINCIA_INTERESADO varchar2(2 char),
        COD_PROVINCIA_REPRESENTANTE varchar2(2 char),
        EMAIL_INTERESADO varchar2(160 char),
        EMAIL_REPRESENTANTE varchar2(160 char),
        DIR_ELECTRONICA_INTERESADO varchar2(160 char),
        DIR_ELECTRONICA_REPRESENTANTE varchar2(160 char),
        DIRECCION_INTERESADO varchar2(160 char),
        DIRECCION_REPRESENTANTE varchar2(160 char),
        DOCUMENTO_INTERESADO varchar2(17 char),
        DOCUMENTO_REPRESENTANTE varchar2(17 char),
        NOMBRE_INTERESADO varchar2(30 char),
        NOMBRE_REPRESENTANTE varchar2(30 char),
        OBSERVACIONES varchar2(160 char),
        APELLIDO1_INTERESADO varchar2(30 char),
        APELLIDO1_REPRESENTANTE varchar2(30 char),
        RAZON_SOCIAL_INTERESADO varchar2(80 char),
        RAZON_SOCIAL_REPRESENTANTE varchar2(80 char),
        APELLIDO2_INTERESADO varchar2(30 char),
        APELLIDO2_REPRESENTANTE varchar2(30 char),
        TELEFONO_INTERESADO varchar2(20 char),
        TELEFONO_REPRESENTANTE varchar2(20 char),
        T_DOCUMENTO_INTERESADO varchar2(1 char),
        T_DOCUMENTO_REPRESENTANTE varchar2(1 char),
        REGISTRO_SIR number(19,0),
        primary key (ID)
    );

    create table RWE_LIBRO (
       ID number(19,0) not null,
        ACTIVO number(1,0) not null,
        CODIGO varchar2(4 char) not null,
        NOMBRE varchar2(255 char) not null,
        CONTADOR_ENTRADA number(19,0),
        CONTADOR_OFICIO_REMISION number(19,0),
        CONTADOR_SALIDA number(19,0),
        CONTADOR_SIR number(19,0),
        ORGANISMO number(19,0),
        primary key (ID)
    );

    create table RWE_LOPD (
       ID number(19,0) not null,
        ACCION number(19,0) not null,
        ANYOREGISTRO varchar2(255 char) not null,
        FECHA timestamp not null,
        NUMREGISTRO number(10,0) not null,
        TIPOREGISTRO number(19,0) not null,
        LIBRO number(19,0) not null,
        USUARIO number(19,0) not null,
        primary key (ID)
    );

    create table RWE_MENSAJE_CONTROL (
       ID number(19,0) not null,
        COD_ENT_REG_DEST varchar2(21 char) not null,
        COD_ENT_REG_ORI varchar2(21 char) not null,
        COD_ERROR varchar2(4 char),
        DESCRIPCION varchar2(1024 char),
        FECHA timestamp,
        FECHA_DESTINO timestamp,
        ID_INTERCAMBIO varchar2(33 char) not null,
        INDICADOR_PRUEBA number(10,0) not null,
        NUM_REG_DESTINO varchar2(20 char),
        TIPO_COMUNICACION number(19,0) not null,
        TIPO_MENSAJE varchar2(2 char) not null,
        ENTIDAD number(19,0) not null,
        primary key (ID)
    );

    create table RWE_MODELO_OFICIO_REMISION (
       ID number(19,0) not null,
        NOMBRE varchar2(255 char) not null,
        ENTIDAD number(19,0),
        MODELO number(19,0),
        primary key (ID)
    );

    create table RWE_MODELO_RECIBO (
       ID number(19,0) not null,
        NOMBRE varchar2(255 char) not null,
        ENTIDAD number(19,0),
        MODELO number(19,0),
        primary key (ID)
    );

    create table RWE_MODIFICACIONLOPD_MIGRADO (
       ID number(19,0) not null,
        FECHA timestamp not null,
        FECHAMOD timestamp not null,
        TIPOACCESO varchar2(10 char) not null,
        USUARIO varchar2(255 char) not null,
        REGMIG number(19,0),
        primary key (ID)
    );

    create table RWE_NOTIFICACION (
       ID number(19,0) not null,
        ASUNTO varchar2(200 char),
        ESTADO number(19,0) not null,
        FECHA_ENVIADO timestamp,
        FECHA_LEIDO timestamp,
        MENSAJE varchar2(4000 char),
        TIPO number(19,0) not null,
        DESTINATARIO number(19,0) not null,
        REMITENTE number(19,0),
        primary key (ID)
    );

    create table RWE_OFICINA (
       ID number(19,0) not null,
        CODPOSTAL varchar2(14 char),
        CODIGO varchar2(9 char) not null,
        DENOMINACION varchar2(300 char) not null,
        NOMBREVIA varchar2(300 char),
        NUMVIA varchar2(20 char),
        COMUNIDAD number(19,0),
        PAIS number(19,0),
        ESTADO number(19,0),
        LOCALIDAD number(19,0),
        OFICINARESPONSABLE number(19,0),
        ORGANISMORESPONSABLE number(19,0),
        TIPOVIA number(19,0),
        primary key (ID)
    );

    create table RWE_OFICINA_SERVICIO (
       IDOFICINA number(19,0) not null,
        IDSERVICIO number(19,0) not null,
        primary key (IDOFICINA, IDSERVICIO)
    );

    create table RWE_OFICIO_REMISION (
       ID number(19,0) not null,
        COD_ENT_REG_DEST varchar2(21 char),
        COD_ENT_REG_PROC varchar2(21 char),
        COD_ERROR varchar2(255 char),
        CONTACTOSDESTINO varchar2(2000 char),
        DEC_ENT_REG_DEST varchar2(80 char),
        DEC_ENT_REG_PROC varchar2(80 char),
        DEC_T_ANOTACION varchar2(80 char),
        DESC_ERROR varchar2(2000 char),
        DESTINOEXTERNOCODIGO varchar2(9 char),
        DESTINOEXTERNODENOMINA varchar2(300 char),
        ESTADO number(10,0) not null,
        FECHA timestamp not null,
        FECHA_DESTINO timestamp,
        FECHA_ESTADO timestamp,
        ID_INTERCAMBIO varchar2(33 char),
        NUMREGISTRO number(10,0) not null,
        NUM_REG_DESTINO varchar2(255 char),
        REINTENTOS number(10,0),
        SIR number(1,0) not null,
        TIPO_ANOTACION varchar2(2 char),
        TIPO_OFICIO number(19,0) not null,
        ENTIDAD number(19,0),
        LIBRO number(19,0) not null,
        OFICINA number(19,0) not null,
        ORGANISMODEST number(19,0),
        USUARIO number(19,0) not null,
        primary key (ID)
    );

    create table RWE_OFIREM_REGENT (
       IDOFIREM number(19,0) not null,
        IDREGENT number(19,0) not null
    );

    create table RWE_OFIREM_REGSAL (
       IDOFIREM number(19,0) not null,
        IDREGSAL number(19,0) not null
    );

    create table RWE_ORGANISMO (
       ID number(19,0) not null,
        CODPOSTAL varchar2(14 char),
        CODIGO varchar2(9 char) not null,
        DENOMINACION varchar2(300 char) not null,
        EDP number(1,0),
        NIVELJERARQUICO number(19,0),
        NOMBREVIA varchar2(300 char),
        NUMVIA varchar2(20 char),
        PERMITE_USUARIOS number(1,0),
        CODAMBCOMUNIDAD number(19,0),
        CODAMBPROVINCIA number(19,0),
        PAIS number(19,0),
        EDPRINCIPAL number(19,0),
        ENTIDAD number(19,0),
        ESTADO number(19,0),
        LOCALIDAD number(19,0),
        NIVELADMINISTRACION number(19,0),
        ORGANISMORAIZ number(19,0),
        ORGANISMOSUPERIOR number(19,0),
        TIPOVIA number(19,0),
        primary key (ID)
    );

    create table RWE_PENDIENTE (
       ID number(19,0) not null,
        ESTADO varchar2(255 char),
        FECHA timestamp,
        IDORGANISMO number(19,0),
        PROCESADO number(1,0),
        primary key (ID)
    );

    create table RWE_PERMLIBUSU (
       ID number(19,0) not null,
        ACTIVO number(1,0) not null,
        PERMISO number(19,0),
        LIBRO number(19,0),
        USUARIO number(19,0),
        primary key (ID)
    );

    create table RWE_PERMORGUSU (
       ID number(19,0) not null,
        ACTIVO number(1,0) not null,
        PERMISO number(19,0),
        ORGANISMO number(19,0),
        USUARIO number(19,0),
        primary key (ID)
    );

    create table RWE_PERSONA (
       ID number(19,0) not null,
        APELLIDO1 varchar2(30 char),
        APELLIDO2 varchar2(30 char),
        CANALNOTIF number(19,0),
        CP varchar2(5 char),
        DIRECCION varchar2(160 char),
        DIRELECTRONICA varchar2(160 char),
        DOCUMENTO varchar2(17 char),
        EMAIL varchar2(160 char),
        NOMBRE varchar2(30 char),
        OBSERVACIONES varchar2(160 char),
        RAZONSOCIAL varchar2(80 char),
        TELEFONO varchar2(20 char),
        TIPOPERSONA number(19,0) not null,
        TIPODOCIDENT number(19,0),
        ENTIDAD number(19,0) not null,
        LOCALIDAD number(19,0),
        PAIS number(19,0),
        PROVINCIA number(19,0),
        primary key (ID)
    );

    create table RWE_PLUGIN (
       ID number(19,0) not null,
        ACTIVO number(1,0) not null,
        CLASE varchar2(1000 char) not null,
        DESCRIPCION varchar2(2000 char) not null,
        ENTIDAD number(19,0),
        NOMBRE varchar2(255 char) not null,
        PROPIEDADES_ADMIN CLOB,
        PROPIEDADES_ENTIDAD CLOB,
        TIPO number(19,0),
        primary key (ID)
    );

    create table RWE_PROPIEDADGLOBAL (
       ID number(19,0) not null,
        CLAVE varchar2(255 char) not null,
        DESCRIPCION varchar2(255 char),
        ENTIDAD number(19,0),
        TIPO number(19,0),
        VALOR varchar2(2048 char),
        primary key (ID)
    );

    create table RWE_REGISTRO_DETALLE (
       ID number(19,0) not null,
        APLICACION varchar2(255 char),
        APLICACION_TELEMATICA varchar2(255 char),
        COD_ENT_REG_DEST varchar2(21 char),
        CODIGOSIA number(19,0),
        DEC_ENT_REG_DEST varchar2(80 char),
        DEC_T_ANOTACION varchar2(80 char),
        EXPEDIENTE varchar2(80 char),
        EXPEDIENTE_JUST varchar2(256 char),
        EXPONE CLOB,
        EXTRACTO varchar2(240 char),
        FECHAORIGEN timestamp,
        ID_INTERCAMBIO varchar2(33 char),
        IDIOMA number(19,0),
        INDICADOR_PRUEBA number(10,0),
        NUMREG_ORIGEN varchar2(20 char),
        NUMTRANSPORTE varchar2(20 char),
        OBSERVACIONES varchar2(50 char),
        OFICINAEXTERNO varchar2(9 char),
        DENOMOFIORIGEXT varchar2(300 char),
        PRESENCIAL number(1,0),
        RECIBIDO_SIR number(1,0),
        REFEXT varchar2(16 char),
        RESERVA varchar2(4000 char),
        SOLICITA CLOB,
        TIPO_ANOTACION varchar2(2 char),
        TIPODOCFISICA number(19,0),
        TIPOENVIODOC varchar2(255 char),
        TRANSPORTE number(19,0),
        VERSION varchar2(255 char),
        CODASUNTO number(19,0),
        OFICINAORIG number(19,0),
        TIPOASUNTO number(19,0),
        primary key (ID)
    );

    create table RWE_REGISTRO_ENTRADA (
       ID number(19,0) not null,
        DESTEXTCOD varchar2(9 char),
        DESTEXTDEN varchar2(300 char),
        ESTADO number(19,0) not null,
        EVENTO number(19,0),
        FECHA timestamp not null,
        NUMREGISTRO number(10,0) not null,
        NUMREGFORMAT varchar2(255 char) not null,
        DESTINO number(19,0),
        ENTIDAD number(19,0),
        LIBRO number(19,0) not null,
        OFICINA number(19,0) not null,
        REGISTRO_DETALLE number(19,0),
        USUARIO number(19,0) not null,
        primary key (ID)
    );

    create table RWE_REGISTRO_MIGRADO (
       ID number(19,0) not null,
        ANO number(10,0) not null,
        ANOENTSAL number(10,0) not null,
        ANULADO number(1,0),
        CODIDIDOC number(10,0) not null,
        CODIDIEXT number(10,0) not null,
        CODOFICINA number(10,0) not null,
        CODOFIFIS number(10,0) not null,
        CODORGDESEMI number(10,0) not null,
        DENOFICINA varchar2(255 char) not null,
        DENOFIFIS varchar2(60 char) not null,
        DESCDOC varchar2(60 char) not null,
        DESIDIDOC varchar2(15 char) not null,
        DESORGDESEMI varchar2(60 char),
        DESREMDES varchar2(160 char) not null,
        MAILREMITENTE varchar2(50 char),
        EXTRACTO varchar2(2000 char) not null,
        FECHADOC timestamp not null,
        FECHAREG timestamp not null,
        FECHAVIS timestamp,
        infoAdicional varchar2(255 char),
        DESIDIEXT varchar2(15 char) not null,
        NUMERO number(10,0) not null,
        NUMCORREO varchar2(8 char),
        NUMDISQUET number(10,0),
        NUMENTSAL number(10,0) not null,
        OTROS varchar2(255 char),
        PRODESGEO varchar2(50 char) not null,
        PRODESGEOBAL number(10,0) not null,
        PRODESGEOFUE varchar2(50 char),
        TIPODOC varchar2(2 char) not null,
        TREGISTRO number(1,0),
        IDENTIDAD number(19,0),
        primary key (ID)
    );

    create table RWE_REGISTRO_SALIDA (
       ID number(19,0) not null,
        ESTADO number(19,0) not null,
        EVENTO number(19,0),
        FECHA timestamp not null,
        NUMREGISTRO number(10,0) not null,
        NUMREGFORMAT varchar2(255 char) not null,
        DESTEXTCOD varchar2(9 char),
        DESTEXTDEN varchar2(300 char),
        ENTIDAD number(19,0),
        LIBRO number(19,0) not null,
        OFICINA number(19,0) not null,
        ORIGEN number(19,0),
        REGISTRO_DETALLE number(19,0),
        USUARIO number(19,0) not null,
        primary key (ID)
    );

    create table RWE_REGISTRO_SIR (
       ID number(19,0) not null,
        APLICACION varchar2(4 char),
        COD_ASUNTO varchar2(16 char),
        COD_ENT_REG varchar2(21 char) not null,
        COD_ENT_REG_DEST varchar2(21 char) not null,
        COD_ENT_REG_INI varchar2(21 char) not null,
        COD_ENT_REG_ORI varchar2(21 char) not null,
        COD_ERROR varchar2(255 char),
        COD_UNI_TRA_DEST varchar2(21 char),
        COD_UNI_TRA_ORI varchar2(21 char),
        CONTACTO_USUARIO varchar2(160 char),
        DEC_ENT_REG_DEST varchar2(80 char),
        DEC_ENT_REG_INI varchar2(80 char),
        DEC_ENT_REG_ORI varchar2(80 char),
        DEC_T_ANOTACION varchar2(80 char),
        DEC_UNI_TRA_DEST varchar2(80 char),
        DEC_UNI_TRA_ORI varchar2(80 char),
        DESC_ERROR varchar2(2000 char),
        DOC_FISICA varchar2(1 char) not null,
        ESTADO number(10,0) not null,
        EXPONE CLOB,
        FECHA_ESTADO timestamp,
        FECHA_RECEPCION timestamp,
        FECHAR_EGISTRO timestamp not null,
        ID_INTERCAMBIO varchar2(33 char) not null,
        INDICADOR_PRUEBA number(10,0) not null,
        NOMBRE_USUARIO varchar2(80 char),
        NUM_EXPEDIENTE varchar2(80 char),
        NUMERO_REGISTRO varchar2(20 char) not null,
        REINTENTOS number(10,0),
        NUM_TRANSPORTE varchar2(20 char),
        OBSERVACIONES varchar2(50 char),
        REF_EXTERNA varchar2(16 char),
        RESUMEN varchar2(240 char) not null,
        SOLICITA CLOB,
        TIMESTAMP CLOB,
        TIPO_ANOTACION varchar2(2 char) not null,
        TIPO_REGISTRO number(10,0) not null,
        TIPO_TRANSPORTE varchar2(2 char),
        ENTIDAD number(19,0) not null,
        primary key (ID)
    );

    create table RWE_REGISTROLOPD_MIGRADO (
       ID number(19,0) not null,
        FECHA timestamp not null,
        TIPOACCESO varchar2(10 char) not null,
        USUARIO varchar2(255 char) not null,
        REGMIG number(19,0),
        primary key (ID)
    );

    create table RWE_RELORGOFI (
       IDOFICINA number(19,0) not null,
        IDORGANISMO number(19,0) not null,
        ESTADO number(19,0) not null,
        primary key (IDORGANISMO, IDOFICINA)
    );

    create table RWE_RELSIROFI (
       IDOFICINA number(19,0) not null,
        IDORGANISMO number(19,0) not null,
        ESTADO number(19,0) not null,
        primary key (IDORGANISMO, IDOFICINA)
    );

    create table RWE_REPRO (
       ID number(19,0) not null,
        ACTIVO number(1,0) not null,
        NOMBRE varchar2(255 char) not null,
        ORDEN number(10,0) not null,
        REPRO CLOB,
        TIPOREGISTRO number(19,0) not null,
        USUARIOENTIDAD number(19,0),
        primary key (ID)
    );

    create table RWE_ROL (
       ID number(19,0) not null,
        DESCRIPCION varchar2(255 char) not null,
        NOMBRE varchar2(255 char) not null,
        ORDEN number(10,0) not null,
        primary key (ID)
    );

    create table RWE_SESION (
       ID number(19,0) not null,
        ESTADO number(19,0),
        FECHA timestamp,
        IDSESION number(19,0) not null,
        NUMREG varchar2(255 char),
        TIPO_REGISTRO number(19,0),
        USUARIO number(19,0) not null,
        primary key (ID)
    );

    create table RWE_TIPOASUNTO (
       ID number(19,0) not null,
        ACTIVO number(1,0) not null,
        CODIGO varchar2(16 char) not null,
        ENTIDAD number(19,0) not null,
        primary key (ID)
    );

    create table RWE_TIPODOCUMENTAL (
       ID number(19,0) not null,
        CODIGONTI varchar2(255 char),
        ENTIDAD number(19,0),
        primary key (ID)
    );

    create table RWE_TRA_CODIGOASUNTO (
       IDCODIGOASUNTO number(19,0) not null,
        NOMBRE varchar2(255 char) not null,
        LANG varchar2(2 char) not null,
        primary key (IDCODIGOASUNTO, LANG)
    );

    create table RWE_TRA_TDOCUMENTAL (
       IDTDOCUMENTAL number(19,0) not null,
        NOMBRE varchar2(255 char) not null,
        LANG varchar2(2 char) not null,
        primary key (IDTDOCUMENTAL, LANG)
    );

    create table RWE_TRA_TIPOASUNTO (
       IDTIPOASUNTO number(19,0) not null,
        NOMBRE varchar2(255 char) not null,
        LANG varchar2(2 char) not null,
        primary key (IDTIPOASUNTO, LANG)
    );

    create table RWE_TRAZABILIDAD (
       ID number(19,0) not null,
        FECHA timestamp not null,
        tipo number(19,0) not null,
        OFICIO_REMISION number(19,0),
        REGENT_DESTINO number(19,0),
        REGENT_ORIGEN number(19,0),
        REGISTRO_SALIDA number(19,0),
        REGISTRO_SALIDA_RECT number(19,0),
        REGISTRO_SIR number(19,0),
        primary key (ID)
    );

    create table RWE_TRAZABILIDAD_SIR (
       ID number(19,0) not null,
        APLICACION varchar2(4 char),
        COD_ENT_REG_DEST varchar2(21 char) not null,
        COD_ENT_REG_ORI varchar2(21 char) not null,
        COD_UNI_TRA_DEST varchar2(21 char),
        CONTACTO_USUARIO varchar2(160 char),
        DEC_ENT_REG_DEST varchar2(80 char),
        DEC_ENT_REG_ORI varchar2(80 char),
        DEC_UNI_TRA_DEST varchar2(80 char),
        FECHA timestamp not null,
        NOMBRE_USUARIO varchar2(80 char),
        OBSERVACIONES varchar2(2000 char),
        tipo number(19,0) not null,
        REGISTRO_ENTRADA number(19,0),
        REGISTRO_SIR number(19,0) not null,
        primary key (ID)
    );

    create table RWE_USUARIO (
       ID number(19,0) not null,
        APELLIDO1 varchar2(255 char),
        APELLIDO2 varchar2(255 char),
        DIB_USER_RW number(1,0) not null,
        DOCUMENTO varchar2(255 char),
        EMAIL varchar2(255 char) not null,
        IDENTIFICADOR varchar2(255 char) not null,
        IDIOMA number(19,0),
        NOMBRE varchar2(255 char) not null,
        RWE_ADMIN number(1,0) not null,
        RWE_SUPERADMIN number(1,0) not null,
        RWE_USUARI number(1,0) not null,
        RWE_WS_CIUDADANO number(1,0) not null,
        RWE_WS_ENTRADA number(1,0) not null,
        RWE_WS_SALIDA number(1,0) not null,
        TIPOUSUARIO number(19,0),
        primary key (ID)
    );

    create table RWE_USUARIO_ENTIDAD (
       ID number(19,0) not null,
        ACTIVO number(1,0) not null,
        OAMR number(1,0) not null,
        ENTIDAD number(19,0),
        ULTIMAOFICINA number(19,0),
        USUARIO number(19,0),
        primary key (ID)
    );
create index RWE_ANEXO_TDOCAL_FK_I on RWE_ANEXO (TDOCUMENTAL);
create index RWE_ANEXO_REGDET_FK_I on RWE_ANEXO (REGISTRODETALLE);
create index RWE_CATCOM_CATPAI_FK_I on RWE_CATCOMUNIDADAUTONOMA (PAIS);
create index RWE_CATLOC_CATPRO_FK_I on RWE_CATLOCALIDAD (PROVINCIA);
create index RWE_CATLOC_CATENG_FK_I on RWE_CATLOCALIDAD (ENTIDADGEOGRAFICA);
create index RWE_CATPRO_CATCAU_FK_I on RWE_CATPROVINCIA (COMUNIDADAUTONOMA);
create index RWE_DESCAR_ENTIDA_FK_I on RWE_DESCARGA (ENTIDAD);
create index RWE_ENTIDA_PRO_FK_I on RWE_ENTIDAD (PROPIETARIO);
create index RWE_HRE_REGENT_FK_I on RWE_HISTORICO_REGISTRO_ENTRADA (REGISTRO_ENTRADA);
create index RWE_HRE_USUENT_FK_I on RWE_HISTORICO_REGISTRO_ENTRADA (USUARIO);
create index RWE_INT_ENTIDAD_FK_I on RWE_INTEGRACION (ENTIDAD);
create index RWE_INTERES_CATPAI_FK_I on RWE_INTERESADO (PAIS);
create index RWE_INTERES_CATLOC_FK_I on RWE_INTERESADO (LOCALIDAD);
create index RWE_INTERES_REPADO_FK_I on RWE_INTERESADO (REPRESENTADO);
create index RWE_INTERES_REPANT_FK_I on RWE_INTERESADO (REPRESENTANTE);
create index RWE_LIBRO_CONENT_FK_I on RWE_LIBRO (CONTADOR_ENTRADA);
create index RWE_LIBRO_CONSAL_FK_I on RWE_LIBRO (CONTADOR_SALIDA);
create index RWE_LIBRO_CONOFI_FK_I on RWE_LIBRO (CONTADOR_OFICIO_REMISION);
create index RWE_LIBRO_ORGANI_FK_I on RWE_LIBRO (ORGANISMO);
create index RWE_LOPD_LIBRO_FK_I on RWE_LOPD (LIBRO);
create index RWE_LOPD_USUENT_FK_I on RWE_LOPD (USUARIO);
create index RWE_MODOFI_ENTIDA_FK_I on RWE_MODELO_OFICIO_REMISION (ENTIDAD);
create index RWE_MODOFI_ARCHIV_FK_I on RWE_MODELO_OFICIO_REMISION (MODELO);
create index RWE_MODREB_ENTIDA_FK_I on RWE_MODELO_RECIBO (ENTIDAD);
create index RWE_MODREB_ARCHIV_FK_I on RWE_MODELO_RECIBO (MODELO);
create index RWE_NOTIF_REMIT_FK_I on RWE_NOTIFICACION (REMITENTE);
create index RWE_NOTIF_DEST_FK_I on RWE_NOTIFICACION (DESTINATARIO);
create index RWE_OFICIN_ESTENT_FK_I on RWE_OFICINA (ESTADO);
create index RWE_OFICIN_PAIS_FK_I on RWE_OFICINA (PAIS);
create index RWE_OFICIN_COMUNI_FK_I on RWE_OFICINA (COMUNIDAD);
create index RWE_OFICIN_LOCALI_FK_I on RWE_OFICINA (LOCALIDAD);
create index RWE_OFICIN_TVIA_FK_I on RWE_OFICINA (TIPOVIA);
create index RWE_OFICIN_ORGANI_FK_I on RWE_OFICINA (ORGANISMORESPONSABLE);
create index RWE_OFICIN_OFICIN_FK_I on RWE_OFICINA (OFICINARESPONSABLE);
create index RWE_OFIREM_ORGANI_FK_I on RWE_OFICIO_REMISION (ORGANISMODEST);
create index RWE_OFIREM_LIBRO_FK_I on RWE_OFICIO_REMISION (LIBRO);
create index RWE_OFIREM_OFICIN_FK_I on RWE_OFICIO_REMISION (OFICINA);
create index RWE_OFIREM_USUARI_FK_I on RWE_OFICIO_REMISION (USUARIO);
create index RWE_ORGANI_ENTIDA_FK_I on RWE_ORGANISMO (ENTIDAD);
create index RWE_ORGANI_ESTADO_FK_I on RWE_ORGANISMO (ESTADO);
create index RWE_ORGANI_SUPERI_FK_I on RWE_ORGANISMO (ORGANISMOSUPERIOR);
create index RWE_ORGANI_RAIZ_FK_I on RWE_ORGANISMO (ORGANISMORAIZ);
create index RWE_ORGANI_EDP_FK_I on RWE_ORGANISMO (EDPRINCIPAL);
create index RWE_ORGANI_CAUTON_FK_I on RWE_ORGANISMO (CODAMBCOMUNIDAD);
create index RWE_ORGANI_PAIS_FK_I on RWE_ORGANISMO (PAIS);
create index RWE_ORGANI_LOCALI_FK_I on RWE_ORGANISMO (LOCALIDAD);
create index RWE_ORGANI_TVIA_FK_I on RWE_ORGANISMO (TIPOVIA);
create index RWE_ORGANI_PROVIN_FK_I on RWE_ORGANISMO (CODAMBPROVINCIA);
create index RWE_PELIUS_LIBRO_FK_I on RWE_PERMLIBUSU (LIBRO);
create index RWE_PELIUS_USUARI_FK_I on RWE_PERMLIBUSU (USUARIO);
create index RWE_POU_ORG_FK_I on RWE_PERMORGUSU (ORGANISMO);
create index RWE_POU_USUARI_FK_I on RWE_PERMORGUSU (USUARIO);
create index RWE_PERSONA_ENTIDAD_FK_I on RWE_PERSONA (ENTIDAD);
create index RWE_PLUGI_ENTIDA_FK_I on RWE_PLUGIN (ENTIDAD);
create index RWE_PROPIE_ENTIDA_FK_I on RWE_PROPIEDADGLOBAL (ENTIDAD);
create index RWE_REGMIG_ANO_I on RWE_REGISTRO_MIGRADO (ANO);
create index RWE_REGMIG_NUM_I on RWE_REGISTRO_MIGRADO (NUMERO);
create index RWE_REGMIG_TREG_I on RWE_REGISTRO_MIGRADO (TREGISTRO);
create index RWE_REGMIG_CODOF_I on RWE_REGISTRO_MIGRADO (CODOFICINA);
create index RWE_REGMIG_EXTR_I on RWE_REGISTRO_MIGRADO (EXTRACTO);
create index RWE_REGMIG_FECREG_I on RWE_REGISTRO_MIGRADO (FECHAREG);
create index RWE_REGMIG_REMDES_I on RWE_REGISTRO_MIGRADO (DESREMDES);
create index RWE_SESION_USUENT_FK_I on RWE_SESION (USUARIO);

alter table RWE_ANEXO
   add constraint RWE_ANEXO_ENTIDAD_FK
   foreign key (ENTIDAD)
   references RWE_ENTIDAD;

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
   add constraint RWE_ANEXOSIR_ENTIDAD_FK
   foreign key (ENTIDAD)
   references RWE_ENTIDAD;

alter table RWE_ANEXO_SIR
   add constraint RWE_ANEXOSIR_REGSIR_FK
   foreign key (REGISTRO_SIR)
   references RWE_REGISTRO_SIR;

alter table RWE_CATCOMUNIDADAUTONOMA
   add constraint RWE_CATCOMUNAUT_CATPAIS_FK
   foreign key (PAIS)
   references RWE_CATPAIS;

alter table RWE_CATLOCALIDAD
   add constraint RWE_CATLOCAL_CATENT_FK
   foreign key (ENTIDADGEOGRAFICA)
   references RWE_CATENTIDADGEOGRAFICA;

alter table RWE_CATLOCALIDAD
   add constraint RWE_CATLOCAL_CATPROVIN_FK
   foreign key (PROVINCIA)
   references RWE_CATPROVINCIA;

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

alter table RWE_DESCARGA
   add constraint RWE_DESCARGA_ENTIDAD_FK
   foreign key (ENTIDAD)
   references RWE_ENTIDAD;

alter table RWE_ENTIDAD
   add constraint RWE_ENTIDAD_CONT_SIR_FK
   foreign key (CONTADOR_SIR)
   references RWE_CONTADOR;

alter table RWE_ENTIDAD
   add constraint RWE_ENTIDAD_LIBRO_FK
   foreign key (LIBRO)
   references RWE_LIBRO;

alter table RWE_ENTIDAD
   add constraint RWE_ENTIDAD_LOGOMENU_FK
   foreign key (LOGOMENU)
   references RWE_ARCHIVO;

alter table RWE_ENTIDAD
   add constraint RWE_ENTIDAD_LOGOPIE_FK
   foreign key (LOGOPIE)
   references RWE_ARCHIVO;

alter table RWE_ENTIDAD
   add constraint RWE_ENTIDAD_LOGOSELLO_FK
   foreign key (LOGOSELLO)
   references RWE_ARCHIVO;

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

alter table RWE_HISTORICO_REGISTRO_ENTRADA
   add constraint RWE_HITORICO_RE_FK
   foreign key (REGISTRO_ENTRADA)
   references RWE_REGISTRO_ENTRADA;

alter table RWE_HISTORICO_REGISTRO_ENTRADA
   add constraint RWE_HISTORICO_USUARIO_FK
   foreign key (USUARIO)
   references RWE_USUARIO_ENTIDAD;

alter table RWE_HISTORICO_REGISTRO_SALIDA
   add constraint RWE_HITORICO_RS_FK
   foreign key (REGISTRO_SALIDA)
   references RWE_REGISTRO_SALIDA;

alter table RWE_HISTORICO_REGISTRO_SALIDA
   add constraint RWE_HISTORICO_USUARIO_RS_FK
   foreign key (USUARIO)
   references RWE_USUARIO_ENTIDAD;

alter table RWE_HISTORICOUO
   add constraint RWE_ORG_ORG_HISTULTI_FK
   foreign key (CODULTIMA)
   references RWE_ORGANISMO;

alter table RWE_HISTORICOUO
   add constraint RWE_ORG_ORG_HISTANTE_FK
   foreign key (CODANTERIOR)
   references RWE_ORGANISMO;

alter table RWE_INTEGRACION
   add constraint RWE_INT_ENTIDAD_FK
   foreign key (ENTIDAD)
   references RWE_ENTIDAD;

alter table RWE_INTERESADO
   add constraint RWE_INTERESADO_LOCALIDAD_FK
   foreign key (LOCALIDAD)
   references RWE_CATLOCALIDAD;

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
   add constraint RWE_INTERESADO_REPRESENT_FK
   foreign key (REPRESENTADO)
   references RWE_INTERESADO;

alter table RWE_INTERESADO
   add constraint RWE_INTERESADO_REPREANTE_FK
   foreign key (REPRESENTANTE)
   references RWE_INTERESADO;

alter table RWE_INTERESADO_SIR
   add constraint RWE_INTERESADOSIR_REGSIR_FK
   foreign key (REGISTRO_SIR)
   references RWE_REGISTRO_SIR;

alter table RWE_LIBRO
   add constraint RWE_LIBRO_CONT_ENT_FK
   foreign key (CONTADOR_ENTRADA)
   references RWE_CONTADOR;

alter table RWE_LIBRO
   add constraint RWE_LIBRO_CONT_ORM_FK
   foreign key (CONTADOR_OFICIO_REMISION)
   references RWE_CONTADOR;

alter table RWE_LIBRO
   add constraint RWE_LIBRO_CONT_SAL_FK
   foreign key (CONTADOR_SALIDA)
   references RWE_CONTADOR;

alter table RWE_LIBRO
   add constraint RWE_LIBRO_CONT_SIR_FK
   foreign key (CONTADOR_SIR)
   references RWE_CONTADOR;

alter table RWE_LIBRO
   add constraint RWE_LIBRO_ORGANISMO_FK
   foreign key (ORGANISMO)
   references RWE_ORGANISMO;

alter table RWE_LOPD
   add constraint RWE_LOPD_LIBRO_FK
   foreign key (LIBRO)
   references RWE_LIBRO;

alter table RWE_LOPD
   add constraint RWE_LOPD_USUENT_FK
   foreign key (USUARIO)
   references RWE_USUARIO_ENTIDAD;

alter table RWE_MENSAJE_CONTROL
   add constraint RWE_MC_ENTIDAD_FK
   foreign key (ENTIDAD)
   references RWE_ENTIDAD;

alter table RWE_MODELO_OFICIO_REMISION
   add constraint RWE_MODELOFREMISION_ENTIDAD_FK
   foreign key (ENTIDAD)
   references RWE_ENTIDAD;

alter table RWE_MODELO_OFICIO_REMISION
   add constraint RWE_MODELOFREMISION_MODELO_FK
   foreign key (MODELO)
   references RWE_ARCHIVO;

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

alter table RWE_NOTIFICACION
   add constraint RWE_NOTIF_DEST_FK
   foreign key (DESTINATARIO)
   references RWE_USUARIO_ENTIDAD;

alter table RWE_NOTIFICACION
   add constraint RWE_NOTIF_REMIT_FK
   foreign key (REMITENTE)
   references RWE_USUARIO_ENTIDAD;

alter table RWE_OFICINA
   add constraint RWE_OFICINA_COMUNIDAD_FK
   foreign key (COMUNIDAD)
   references RWE_CATCOMUNIDADAUTONOMA;

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
   add constraint RWE_OFICINA_OFICINA_FK
   foreign key (OFICINARESPONSABLE)
   references RWE_OFICINA;

alter table RWE_OFICINA
   add constraint RWE_OFICINA_ORGANISMO_FK
   foreign key (ORGANISMORESPONSABLE)
   references RWE_ORGANISMO;

alter table RWE_OFICINA
   add constraint RWE_OFICINA_TIPOVIA_FK
   foreign key (TIPOVIA)
   references RWE_CATTIPOVIA;

alter table RWE_OFICINA_SERVICIO
   add constraint RWE_OFICINA_SERVICIO_FK
   foreign key (IDSERVICIO)
   references RWE_CATSERVICIO;

alter table RWE_OFICINA_SERVICIO
   add constraint RWE_SERVICIO_OFICINA_FK
   foreign key (IDOFICINA)
   references RWE_OFICINA;

alter table RWE_OFICIO_REMISION
   add constraint RWE_OFIREM_ENTIDAD_FK
   foreign key (ENTIDAD)
   references RWE_ENTIDAD;

alter table RWE_OFICIO_REMISION
   add constraint RWE_OFIREM_LIBRO_FK
   foreign key (LIBRO)
   references RWE_LIBRO;

alter table RWE_OFICIO_REMISION
   add constraint RWE_OFIREM_OFICINA_FK
   foreign key (OFICINA)
   references RWE_OFICINA;

alter table RWE_OFICIO_REMISION
   add constraint RWE_OFIREM_ORGANISMODEST_FK
   foreign key (ORGANISMODEST)
   references RWE_ORGANISMO;

alter table RWE_OFICIO_REMISION
   add constraint RWE_OFIREM_USUORM_FK
   foreign key (USUARIO)
   references RWE_USUARIO_ENTIDAD;

alter table RWE_OFIREM_REGENT
   add constraint RWE_OFIREM_REGENT_FK
   foreign key (IDREGENT)
   references RWE_REGISTRO_ENTRADA;

alter table RWE_OFIREM_REGENT
   add constraint RWE_REGENT_OFIREM_FK
   foreign key (IDOFIREM)
   references RWE_OFICIO_REMISION;

alter table RWE_OFIREM_REGSAL
   add constraint RWE_OFIREM_REGSAL_FK
   foreign key (IDREGSAL)
   references RWE_REGISTRO_SALIDA;

alter table RWE_OFIREM_REGSAL
   add constraint RWE_REGSAL_OFIREM_FK
   foreign key (IDOFIREM)
   references RWE_OFICIO_REMISION;

alter table RWE_ORGANISMO
   add constraint RWE_ORGANISMO_CATAMBCOMAUTO_FK
   foreign key (CODAMBCOMUNIDAD)
   references RWE_CATCOMUNIDADAUTONOMA;

alter table RWE_ORGANISMO
   add constraint RWE_ORGANISMO_CATPROVINCIA_FK
   foreign key (CODAMBPROVINCIA)
   references RWE_CATPROVINCIA;

alter table RWE_ORGANISMO
   add constraint RWE_ORGANISMO_PAIS_FK
   foreign key (PAIS)
   references RWE_CATPAIS;

alter table RWE_ORGANISMO
   add constraint RWE_ORGANISMO_EDPRIN_FK
   foreign key (EDPRINCIPAL)
   references RWE_ORGANISMO;

alter table RWE_ORGANISMO
   add constraint RWE_ORGANISMO_ENTIDAD_FK
   foreign key (ENTIDAD)
   references RWE_ENTIDAD;

alter table RWE_ORGANISMO
   add constraint RWE_ORGANISMO_ESTADO_FK
   foreign key (ESTADO)
   references RWE_CATESTADOENTIDAD;

alter table RWE_ORGANISMO
   add constraint RWE_ORGANISMO_LOCALIDAD_FK
   foreign key (LOCALIDAD)
   references RWE_CATLOCALIDAD;

alter table RWE_ORGANISMO
   add constraint RWE_ORGANISMO_CATNIVELADMIN_FK
   foreign key (NIVELADMINISTRACION)
   references RWE_CATNIVELADMINISTRACION;

alter table RWE_ORGANISMO
   add constraint RWE_ORGANISMO_ORGRAIZ_FK
   foreign key (ORGANISMORAIZ)
   references RWE_ORGANISMO;

alter table RWE_ORGANISMO
   add constraint RWE_ORGANISMO_ORG_SUPERIOR_FK
   foreign key (ORGANISMOSUPERIOR)
   references RWE_ORGANISMO;

alter table RWE_ORGANISMO
   add constraint RWE_ORGANISMO_TIPOVIA_FK
   foreign key (TIPOVIA)
   references RWE_CATTIPOVIA;

alter table RWE_PERMLIBUSU
   add constraint RWE_PERMLIBUSU_LIBRO_FK
   foreign key (LIBRO)
   references RWE_LIBRO;

alter table RWE_PERMLIBUSU
   add constraint RWE_PERMLIBUSU_USUENT_FK
   foreign key (USUARIO)
   references RWE_USUARIO_ENTIDAD;

alter table RWE_PERMORGUSU
   add constraint RWE_POU_ORG_FK
   foreign key (ORGANISMO)
   references RWE_ORGANISMO;

alter table RWE_PERMORGUSU
   add constraint RWE_POU_USUENT_FK
   foreign key (USUARIO)
   references RWE_USUARIO_ENTIDAD;

alter table RWE_PERSONA
   add constraint RWE_PERSONA_ENTIDAD_FK
   foreign key (ENTIDAD)
   references RWE_ENTIDAD;

alter table RWE_PERSONA
   add constraint RWE_PERSONA_LOCALIDAD_FK
   foreign key (LOCALIDAD)
   references RWE_CATLOCALIDAD;

alter table RWE_PERSONA
   add constraint RWE_PERSONA_PAIS_FK
   foreign key (PAIS)
   references RWE_CATPAIS;

alter table RWE_PERSONA
   add constraint RWE_PERSONA_PROVINCIA_FK
   foreign key (PROVINCIA)
   references RWE_CATPROVINCIA;

alter table RWE_REGISTRO_DETALLE
   add constraint RWE_REGDET_CODASUNTO_FK
   foreign key (CODASUNTO)
   references RWE_CODIGOASUNTO;

alter table RWE_REGISTRO_DETALLE
   add constraint RWE_REGDET_OFICINAORIG_FK
   foreign key (OFICINAORIG)
   references RWE_OFICINA;

alter table RWE_REGISTRO_DETALLE
   add constraint RWE_REGDET_TIPOASUNTO_FK
   foreign key (TIPOASUNTO)
   references RWE_TIPOASUNTO;

alter table RWE_REGISTRO_ENTRADA
   add constraint RWE_REGENT_DESTINO_FK
   foreign key (DESTINO)
   references RWE_ORGANISMO;

alter table RWE_REGISTRO_ENTRADA
   add constraint RWE_REGENT_ENTIDAD_FK
   foreign key (ENTIDAD)
   references RWE_ENTIDAD;

alter table RWE_REGISTRO_ENTRADA
   add constraint RWE_REGENT_LIBRO_FK
   foreign key (LIBRO)
   references RWE_LIBRO;

alter table RWE_REGISTRO_ENTRADA
   add constraint RWE_REGENT_OFICINA_FK
   foreign key (OFICINA)
   references RWE_OFICINA;

alter table RWE_REGISTRO_ENTRADA
   add constraint RWE_REGENT_REGDET_FK
   foreign key (REGISTRO_DETALLE)
   references RWE_REGISTRO_DETALLE;

alter table RWE_REGISTRO_ENTRADA
   add constraint RWE_REGENT_USUENT_FK
   foreign key (USUARIO)
   references RWE_USUARIO_ENTIDAD;

alter table RWE_REGISTRO_MIGRADO
   add constraint RWE_REGMIG_ENTIDAD_FK
   foreign key (IDENTIDAD)
   references RWE_ENTIDAD;

alter table RWE_REGISTRO_SALIDA
   add constraint RWE_REGSAL_ENTIDAD_FK
   foreign key (ENTIDAD)
   references RWE_ENTIDAD;

alter table RWE_REGISTRO_SALIDA
   add constraint RWE_REGSAL_LIBRO_FK
   foreign key (LIBRO)
   references RWE_LIBRO;

alter table RWE_REGISTRO_SALIDA
   add constraint RWE_REGSAL_OFICINA_FK
   foreign key (OFICINA)
   references RWE_OFICINA;

alter table RWE_REGISTRO_SALIDA
   add constraint RWE_REGSAL_ORIGEN_FK
   foreign key (ORIGEN)
   references RWE_ORGANISMO;

alter table RWE_REGISTRO_SALIDA
   add constraint RWE_REGSAL_REGDET_FK
   foreign key (REGISTRO_DETALLE)
   references RWE_REGISTRO_DETALLE;

alter table RWE_REGISTRO_SALIDA
   add constraint RWE_REGSAL_USUSAL_FK
   foreign key (USUARIO)
   references RWE_USUARIO_ENTIDAD;

alter table RWE_REGISTRO_SIR
   add constraint RWE_RES_ENTIDAD_FK
   foreign key (ENTIDAD)
   references RWE_ENTIDAD;

alter table RWE_REGISTROLOPD_MIGRADO
   add constraint RWE_REGLOPDMIG_REGMIG_FK
   foreign key (REGMIG)
   references RWE_REGISTRO_MIGRADO;

alter table RWE_RELORGOFI
   add constraint RWE_RELORGOFI_OFICINA_FK
   foreign key (IDOFICINA)
   references RWE_OFICINA;

alter table RWE_RELORGOFI
   add constraint RWE_RELORGOFI_ORGANISMO_FK
   foreign key (IDORGANISMO)
   references RWE_ORGANISMO;

alter table RWE_RELORGOFI
   add constraint RWE_RELORGANOFI_CATESTENT_FK
   foreign key (ESTADO)
   references RWE_CATESTADOENTIDAD;

alter table RWE_RELSIROFI
   add constraint RWE_RELSIROFI_OFICINA_FK
   foreign key (IDOFICINA)
   references RWE_OFICINA;

alter table RWE_RELSIROFI
   add constraint RWE_RELSIROFI_ORGANISMO_FK
   foreign key (IDORGANISMO)
   references RWE_ORGANISMO;

alter table RWE_RELSIROFI
   add constraint RWE_RELSIROFI_CATESTENTI_FK
   foreign key (ESTADO)
   references RWE_CATESTADOENTIDAD;

alter table RWE_REPRO
   add constraint RWE_REPRO_USUARIO_FK
   foreign key (USUARIOENTIDAD)
   references RWE_USUARIO_ENTIDAD;

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

alter table RWE_TRAZABILIDAD
   add constraint RWE_TRAZAB_OFIREM_FK
   foreign key (OFICIO_REMISION)
   references RWE_OFICIO_REMISION;

alter table RWE_TRAZABILIDAD
   add constraint RWE_TRAZAB_REGENTD_FK
   foreign key (REGENT_DESTINO)
   references RWE_REGISTRO_ENTRADA;

alter table RWE_TRAZABILIDAD
   add constraint RWE_TRAZAB_REGENTO_FK
   foreign key (REGENT_ORIGEN)
   references RWE_REGISTRO_ENTRADA;

alter table RWE_TRAZABILIDAD
   add constraint RWE_TRAZAB_REGSAL_FK
   foreign key (REGISTRO_SALIDA)
   references RWE_REGISTRO_SALIDA;

alter table RWE_TRAZABILIDAD
   add constraint RWE_TRAZAB_RGSRCT_FK
   foreign key (REGISTRO_SALIDA_RECT)
   references RWE_REGISTRO_SALIDA;

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

alter table RWE_USUARIO_ENTIDAD
   add constraint RWE_USUENT_ENTIDAD_FK
   foreign key (ENTIDAD)
   references RWE_ENTIDAD;

alter table RWE_USUARIO_ENTIDAD
   add constraint RWE_USUENT_OFICINA_FK
   foreign key (ULTIMAOFICINA)
   references RWE_OFICINA;

alter table RWE_USUARIO_ENTIDAD
   add constraint RWE_USUENT_USUARIO_FK
   foreign key (USUARIO)
   references RWE_USUARIO;



-- INICI UNIQUES
alter table RWE_CATESTADOENTIDAD
    add constraint RWE_CATESTENT_CODESTENT_UK unique (CODIGOESTADOENTIDAD);
alter table RWE_CODIGOASUNTO
    add constraint RWE_CODASUN_CODIGO_UK unique (CODIGO);
alter table RWE_ENTIDAD
    add constraint RWE_ENTIDAD_CODIGODIR3_uk unique (CODIGODIR3);
alter table RWE_PROPIEDADGLOBAL
    add constraint RWE_propiedad_clave_entidad_uk unique (CLAVE, ENTIDAD);
alter table RWE_REGISTRO_MIGRADO
    add constraint RWE_REGMIGRADO_AN_NUM_OF_UK unique (ANO, NUMERO, CODOFICINA, TREGISTRO, IDENTIDAD);
alter table RWE_ROL
    add constraint RWE_ROL_NOMBRE_uk unique (NOMBRE);
alter table RWE_USUARIO
    add constraint RWE_USUARIO_IDENTIFICADOR_uk unique (IDENTIFICADOR);
-- FINAL UNIQUES