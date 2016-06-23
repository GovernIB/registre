--Nueva tabla RWE_ASIENTO_REGISTRAL_SIR

create table RWE_ASIENTO_REGISTRAL_SIR (
        ID int8 not null,
        APLICACION varchar(4),
        COD_ASUNTO varchar(16),
        COD_ENT_REG varchar(21),
        COD_ENT_REG_DEST varchar(21) not null,
        COD_ENT_REG_INI varchar(21) not null,
        COD_ENT_REG_ORI varchar(21) not null,
        COD_UNI_TRA_DEST varchar(21),
        COD_UNI_TRA_ORI varchar(21),
        CONTACTO_USUARIO varchar(160),
        DEC_ENT_REG_DEST varchar(80),
        DEC_ENT_REG_INI varchar(80),
        DEC_ENT_REG_ORI varchar(80),
        DEC_T_ANOTACION varchar(80),
        DEC_UNI_TRA_DEST varchar(80),
        DEC_UNI_TRA_ORI varchar(80),
        DOC_FISICA int4 not null,
        ESTADO int4 not null,
        EXPONE varchar(4000),
        FECHAR_EGISTRO timestamp not null,
        fechaRegistroInicial timestamp,
        ID_INTERCAMBIO varchar(33) not null,
        INDICADOR_PRUEBA int4 not null,
        NOMBRE_USUARIO varchar(80),
        NUM_EXPEDIENTE varchar(80),
        NUMERO_REGISTRO varchar(20) not null,
        numeroRegistroInicial varchar(255),
        NUM_TRANSPORTE varchar(20),
        OBSERVACIONES varchar(50),
        REF_EXTERNA varchar(16),
        RESUMEN varchar(240) not null,
        SOLICITA varchar(4000),
        TIMESTAMP_REGISTRO bytea,
        timestampRegistroInicial bytea,
        TIPO_ANOTACION int4 not null,
        TIPO_REGISTRO int4 not null,
        TIPO_TRANSPORTE int4,
        ENTIDAD int8 not null,
        primary key (ID)
    );

alter table RWE_ASIENTO_REGISTRAL_SIR
        add constraint RWE_ARS_ENTIDAD_FK
        foreign key (ENTIDAD)
        references RWE_ENTIDAD;

--Nueva tabla RWE_ANEXO_SIR

create table RWE_ANEXO_SIR (
        ID int8 not null,
        CERTIFICADO bytea,
        FIRMA bytea,
        HASH bytea not null,
        ID_DOCUMENTO_FIRMADO varchar(50),
        IDENTIFICADOR_FICHERO varchar(50) not null,
        NOMBRE_FICHERO varchar(80) not null,
        OBSERVACIONES varchar(50),
        TIMESTAMP bytea,
        TIPO_DOCUMENTO int4 not null,
        TIPO_MIME varchar(20),
        VAL_OCSP_CERTIFICADO bytea,
        VALIDEZ_DOCUMENTO int4,
        ANEXO int8,
        ASIENTO_REGISTRAL int8,
        primary key (ID)
);

alter table RWE_ANEXO_SIR
        add constraint RWE_ANEXOSIR_ASIREG_FK
        foreign key (ASIENTO_REGISTRAL)
        references RWE_ASIENTO_REGISTRAL_SIR;

alter table RWE_ANEXO_SIR
        add constraint RWE_ANEXOSIR_ANEXO_FK
        foreign key (ANEXO)
        references RWE_ARCHIVO;

--Nueva tabla RWE_INTERESADO_SIR

create table RWE_INTERESADO_SIR (
        ID int8 not null,
        CANAL_NOTIF_INTERESADO int4,
        CANAL_NOTIF_REPRESENTANTE int4,
        COD_MUNICIPIO_INTERESADO varchar(5),
        COD_MUNICIPIO_REPRESENTANTE varchar(5),
        COD_PAIS_INTERESADO varchar(4),
        COD_PAIS_REPRESENTANTE varchar(4),
        CP_INTERESADO varchar(5),
        CP_REPRESENTANTE varchar(5),
        COD_PROVINCIA_INTERESADO varchar(2),
        COD_PROVINCIA_REPRESENTANTE varchar(2),
        EMAIL_INTERESADO varchar(160),
        EMAIL_REPRESENTANTE varchar(160),
        DIR_ELECTRONICA_INTERESADO varchar(160),
        DIR_ELECTRONICA_REPRESENTANTE varchar(160),
        DIRECCION_INTERESADO varchar(160),
        DIRECCION_REPRESENTANTE varchar(160),
        DOCUMENTO_INTERESADO varchar(17),
        DOCUMENTO_REPRESENTANTE varchar(17),
        NOMBRE_INTERESADO varchar(30),
        NOMBRE_REPRESENTANTE varchar(30),
        OBSERVACIONES varchar(160),
        APELLIDO1_INTERESADO varchar(30),
        APELLIDO1_REPRESENTANTE varchar(30),
        RAZON_SOCIAL_INTERESADO varchar(80),
        RAZON_SOCIAL_REPRESENTANTE varchar(80),
        APELLIDO2_INTERESADO varchar(30),
        APELLIDO2_REPRESENTANTE varchar(30),
        TELEFONO_INTERESADO varchar(20),
        TELEFONO_REPRESENTANTE varchar(20),
        T_DOCUMENTO_INTERESADO int4,
        T_DOCUMENTO_REPRESENTANTE int4,
        ASIENTO_REGISTRAL int8,
        primary key (ID)
);

alter table RWE_INTERESADO_SIR
        add constraint RWE_INTERESADOSIR_ASIREG_FK
        foreign key (ASIENTO_REGISTRAL)
        references RWE_ASIENTO_REGISTRAL_SIR;