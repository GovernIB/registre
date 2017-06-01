--Aumento de tamaño del campo VALOR
ALTER TABLE RWE_PROPIEDADGLOBAL MODIFY VALOR varchar2(2048 char);

--Nuevos campos RWE_ANEXO
alter table RWE_ANEXO add (FIRMAVALIDA NUMBER(1,0) DEFAULT 0);
alter table RWE_ANEXO add (JUSTIFICANTE NUMBER(1,0) DEFAULT 0 NOT NULL);
ALTER TABLE RWE_ANEXO add SIGNTYPE varchar2(128 char);
ALTER TABLE RWE_ANEXO add SIGNFORMAT varchar2(128 char);
ALTER TABLE RWE_ANEXO add SIGNPROFILE varchar2(128 char);

--Nuevos campos en la tabla RWE_OFICIO_REMISION
ALTER TABLE RWE_OFICIO_REMISION DROP COLUMN IDINTERCAMBIOSIR;
ALTER TABLE RWE_OFICIO_REMISION add (SIR NUMBER(1,0) DEFAULT 0);
ALTER TABLE RWE_OFICIO_REMISION add ID_INTERCAMBIO varchar2(33 char);
ALTER TABLE RWE_OFICIO_REMISION add COD_ENT_REG_DEST varchar2(21 char);
ALTER TABLE RWE_OFICIO_REMISION add DEC_ENT_REG_DEST varchar2(80 char);
ALTER TABLE RWE_OFICIO_REMISION add NUM_REG_DESTINO varchar2(255 char);
ALTER TABLE RWE_OFICIO_REMISION add FECHA_DESTINO timestamp;
ALTER TABLE RWE_OFICIO_REMISION add COD_ERROR varchar2(255 char);
ALTER TABLE RWE_OFICIO_REMISION add DESC_ERROR varchar2(2000 char);
ALTER TABLE RWE_OFICIO_REMISION add REINTENTOS number(10,0) DEFAULT 0;

--Nuevos campos en la tabla RWE_TRAZABILIDAD
ALTER TABLE RWE_TRAZABILIDAD add (TIPO number(10,0) DEFAULT 1 not null);

ALTER TABLE RWE_TRAZABILIDAD add REGISTRO_SALIDA_RECT number(19,0);
ALTER TABLE RWE_TRAZABILIDAD
        add constraint RWE_TRAZAB_RGSRCT_FK
        foreign key (REGISTRO_SALIDA_RECT)
        references RWE_REGISTRO_SALIDA;

-- Campo REGISTRO_SALIDA nulleable en la tabla RWE_TRAZABILIDAD
ALTER TABLE RWE_TRAZABILIDAD MODIFY REGISTRO_SALIDA NULL;
ALTER TABLE RWE_TRAZABILIDAD MODIFY OFICIO_REMISION NULL;

--Nuevos campos en la tabla RWE_REGISTRO_DETALLE
ALTER TABLE RWE_REGISTRO_DETALLE add INDICADOR_PRUEBA number(10,0);
ALTER TABLE RWE_REGISTRO_DETALLE add TIPO_ANOTACION varchar2(2 char);
ALTER TABLE RWE_REGISTRO_DETALLE add DEC_T_ANOTACION varchar2(80 char);
ALTER TABLE RWE_REGISTRO_DETALLE add COD_ENT_REG_DEST varchar2(21 char);
ALTER TABLE RWE_REGISTRO_DETALLE add DEC_ENT_REG_DEST varchar2(80 char);
ALTER TABLE RWE_REGISTRO_DETALLE add ID_INTERCAMBIO varchar2(33 char);

--Eliminamos las tabla RWE_ASIENTO_REGISTRAL_SIR, RWE_INTERESADO_SIR y RWE_ANEXO_SIR
DROP TABLE RWE_ASIENTO_REGISTRAL_SIR CASCADE CONSTRAINTS;
DROP TABLE RWE_INTERESADO_SIR CASCADE CONSTRAINTS;
DROP TABLE RWE_ANEXO_SIR CASCADE CONSTRAINTS;

--Creamos la Tabla RWE_REGISTRO_SIR
create table RWE_REGISTRO_SIR (
    ID number(19,0) not null,
    APLICACION varchar2(4 char),
    COD_ASUNTO varchar2(16 char),
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
    EXPONE varchar2(4000 char),
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
    SOLICITA varchar2(4000 char),
    TIMESTAMP clob,
    TIPO_ANOTACION varchar2(2 char) not null,
    TIPO_REGISTRO number(10,0) not null,
    TIPO_TRANSPORTE varchar2(2 char),
    ENTIDAD number(19,0) not null
) TABLESPACE REGWEB_DADES;
ALTER TABLE RWE_REGISTRO_SIR add constraint RWE_REGISTRO_SIR_pk primary key (ID);

ALTER TABLE RWE_REGISTRO_SIR
        add constraint RWE_RES_ENTIDAD_FK
        foreign key (ENTIDAD)
        references RWE_ENTIDAD;

grant select,insert,delete,update on RWE_REGISTRO_SIR to www_regweb;
alter table RWE_REGISTRO_SIR move lob (TIMESTAMP) store as RWE_REG_SIR_TMST_lob (tablespace regweb_lob index RWE_REG_SIR_TMST_lob_i);

ALTER TABLE RWE_TRAZABILIDAD add REGISTRO_SIR number(19,0);

ALTER TABLE RWE_TRAZABILIDAD
        add constraint RWE_TRAZAB_REGSIR_FK
        foreign key (REGISTRO_SIR)
        references RWE_REGISTRO_SIR;

--Creamos la Tabla RWE_ANEXO_SIR
create table RWE_ANEXO_SIR (
    ID number(19,0) not null,
    CERTIFICADO clob,
    FIRMA clob,
    HASH clob not null,
    ID_DOCUMENTO_FIRMADO varchar2(50 char),
    IDENTIFICADOR_FICHERO varchar2(50 char) not null,
    NOMBRE_FICHERO varchar2(80 char) not null,
    OBSERVACIONES varchar2(50 char),
    TIMESTAMP clob,
    TIPO_DOCUMENTO varchar2(2 char) not null,
    TIPO_MIME varchar2(20 char),
    VAL_OCSP_CE clob,
    VALIDEZ_DOCUMENTO varchar2(2 char),
    ANEXO number(19,0),
    REGISTRO_SIR number(19,0)
) TABLESPACE REGWEB_DADES;

ALTER TABLE RWE_ANEXO_SIR add constraint RWE_ANEXO_SIR_pk primary key (ID);

ALTER TABLE RWE_ANEXO_SIR
        add constraint RWE_ANEXOSIR_ANEXO_FK
        foreign key (ANEXO)
        references RWE_ARCHIVO;

ALTER TABLE RWE_ANEXO_SIR
    add constraint RWE_ANEXOSIR_REGSIR_FK
    foreign key (REGISTRO_SIR)
    references RWE_REGISTRO_SIR;

grant select,insert,delete,update on RWE_ANEXO_SIR to www_regweb;
alter table RWE_ANEXO_SIR move lob (CERTIFICADO) store as RWE_ANX_SIR_CERT_lob (tablespace regweb_lob index RWE_ANX_SIR_CERT_lob_i);
alter table RWE_ANEXO_SIR move lob (FIRMA) store as RWE_ANEXO_SIR_FIRMA_lob (tablespace regweb_lob index RWE_ANEXO_SIR_FIRMA_lob_i);
alter table RWE_ANEXO_SIR move lob (HASH) store as RWE_ANEXO_SIR_HASH_lob (tablespace regweb_lob index RWE_ANEXO_SIR_HASH_lob_i);
alter table RWE_ANEXO_SIR move lob (TIMESTAMP) store as RWE_ANX_SIR_TMST_lob (tablespace regweb_lob index RWE_ANX_SIR_TMST_lob_i);
alter table RWE_ANEXO_SIR move lob (VAL_OCSP_CE) store as RWE_ANX_SIR_VALOCSP_lob (tablespace regweb_lob index RWE_ANX_SIR_VALOCSP_lob_i);

--Creamos la Tabla RWE_INTERESADO_SIR
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
        REGISTRO_SIR number(19,0)
    );

ALTER TABLE RWE_INTERESADO_SIR add constraint RWE_INTERESADO_SIR_pk primary key (ID);

ALTER TABLE RWE_INTERESADO_SIR
        add constraint RWE_INTERESADOSIR_REGSIR_FK
        foreign key (REGISTRO_SIR)
        references RWE_REGISTRO_SIR;

grant select,insert,delete,update on RWE_INTERESADO_SIR to www_regweb;

--Creamos la Tabla RWE_PLUGIN 04/05/2017
create table RWE_PLUGIN (
    ID number(19,0) not null,
    ACTIVO number(1,0) not null,
    CLASE varchar2(1000 char) not null,
    DESCRIPCION varchar2(2000 char) not null,
    ENTIDAD number(19,0),
    NOMBRE varchar2(255 char) not null,
    PROPIEDADES_ADMIN clob,
    PROPIEDADES_ENTIDAD clob,
    TIPO number(19,0)
) TABLESPACE REGWEB_DADES;
create index RWE_PLUGI_ENTIDA_FK_I on RWE_PLUGIN (ENTIDAD);
alter table RWE_PLUGIN add constraint RWE_PLUGIN_pk primary key (ID);
grant select,insert,delete,update on RWE_PLUGIN to www_regweb;
alter table RWE_PLUGIN move lob (PROPIEDADES_ADMIN) store as RWE_PLUGIN_PROP_ADM_lob (tablespace regweb_lob index RWE_PLUGIN_PROP_ADM_lob_i);
alter table RWE_PLUGIN move lob (PROPIEDADES_ENTIDAD) store as RWE_PLUGIN_PROP_ENT_lob (tablespace regweb_lob index RWE_PLUGIN_PROP_ENT_lob_i);

-- Nueva Tabla RWE_TRAZABILIDAD_SIR (11/05/2017)
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
        REGISTRO_SIR number(19,0) not null
    );

alter table RWE_TRAZABILIDAD_SIR add constraint RWE_TRAZABILIDAD_SIR_pk primary key (ID);

alter table RWE_TRAZABILIDAD_SIR
        add constraint RWE_TRASIR_REGENT_FK
        foreign key (REGISTRO_ENTRADA)
        references RWE_REGISTRO_ENTRADA;

alter table RWE_TRAZABILIDAD_SIR
    add constraint RWE_TRASIR_REGSIR_FK
    foreign key (REGISTRO_SIR)
    references RWE_REGISTRO_SIR;
grant select,insert,delete,update on RWE_TRAZABILIDAD_SIR to www_regweb;