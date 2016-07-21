--Nueva tabla RWE_ASIENTO_REGISTRAL_SIR

create table RWE_ASIENTO_REGISTRAL_SIR (
        ID number(19,0) not null,
        APLICACION varchar2(4 char),
        COD_ASUNTO varchar2(16 char),
        COD_ENT_REG varchar2(21 char),
        COD_ENT_REG_DEST varchar2(21 char) not null,
        COD_ENT_REG_INI varchar2(21 char) not null,
        COD_ENT_REG_ORI varchar2(21 char) not null,
        COD_UNI_TRA_DEST varchar2(21 char),
        COD_UNI_TRA_ORI varchar2(21 char),
        CONTACTO_USUARIO varchar2(160 char),
        DEC_ENT_REG_DEST varchar2(80 char),
        DEC_ENT_REG_INI varchar2(80 char),
        DEC_ENT_REG_ORI varchar2(80 char),
        DEC_T_ANOTACION varchar2(80 char),
        DEC_UNI_TRA_DEST varchar2(80 char),
        DEC_UNI_TRA_ORI varchar2(80 char),
        DOC_FISICA varchar2(1 char) not null,
        ESTADO number(10,0) not null,
        EXPONE varchar2(4000 char),
        FECHAR_EGISTRO timestamp not null,
        fechaRegistroInicial timestamp,
        ID_INTERCAMBIO varchar2(33 char) not null,
        INDICADOR_PRUEBA number(10,0) not null,
        NOMBRE_USUARIO varchar2(80 char),
        NUM_EXPEDIENTE varchar2(80 char),
        NUMERO_REGISTRO varchar2(20 char) not null,
        numeroRegistroInicial varchar2(255 char),
        NUM_TRANSPORTE varchar2(20 char),
        OBSERVACIONES varchar2(50 char),
        REF_EXTERNA varchar2(16 char),
        RESUMEN varchar2(240 char) not null,
        SOLICITA varchar2(4000 char),
        TIMESTAMP_REGISTRO raw(255),
        timestampRegistroInicial raw(255),
        TIPO_ANOTACION varchar2(2 char) not null,
        TIPO_REGISTRO number(10,0) not null,
        TIPO_TRANSPORTE varchar2(2 char),
        ENTIDAD number(19,0) not null
    );

alter table RWE_ASIENTO_REGISTRAL_SIR add constraint RWE_ASIENTO_REGISTRAL_SIR_pk primary key (ID);

alter table RWE_ASIENTO_REGISTRAL_SIR
        add constraint RWE_ARS_ENTIDAD_FK
        foreign key (ENTIDAD)
        references RWE_ENTIDAD;

--Nueva tabla RWE_INTERESADO_SIR

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
        ASIENTO_REGISTRAL number(19,0)
    );


alter table RWE_INTERESADO_SIR add constraint RWE_INTERESADO_SIR_pk primary key (ID);

alter table RWE_INTERESADO_SIR
        add constraint RWE_INTERESADOSIR_ASIREG_FK
        foreign key (ASIENTO_REGISTRAL)
        references RWE_ASIENTO_REGISTRAL_SIR;

--Nueva tabla RWE_ANEXO_SIR

create table RWE_ANEXO_SIR (
        ID number(19,0) not null,
        CERTIFICADO raw(255),
        FIRMA raw(255),
        HASH raw(255) not null,
        ID_DOCUMENTO_FIRMADO varchar2(50 char),
        IDENTIFICADOR_FICHERO varchar2(50 char) not null,
        NOMBRE_FICHERO varchar2(80 char) not null,
        OBSERVACIONES varchar2(50 char),
        TIMESTAMP raw(255),
        TIPO_DOCUMENTO varchar2(2 char) not null,
        TIPO_MIME varchar2(20 char),
        VAL_OCSP_CERTIFICADO raw(255),
        VALIDEZ_DOCUMENTO varchar2(2 char),
        ANEXO number(19,0),
        ASIENTO_REGISTRAL number(19,0)
    );

alter table RWE_ANEXO_SIR add constraint RWE_ANEXO_SIR_pk primary key (ID);

alter table RWE_ANEXO_SIR
        add constraint RWE_ANEXOSIR_ASIREG_FK
        foreign key (ASIENTO_REGISTRAL)
        references RWE_ASIENTO_REGISTRAL_SIR;

    alter table RWE_ANEXO_SIR
        add constraint RWE_ANEXOSIR_ANEXO_FK
        foreign key (ANEXO)
        references RWE_ARCHIVO;


ALTER TABLE RWE_OFICINA DROP CONSTRAINT rwe_oficina_codigo_uk;