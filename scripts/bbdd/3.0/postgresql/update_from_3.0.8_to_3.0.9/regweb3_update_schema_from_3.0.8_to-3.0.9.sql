--Aumento de tamaño del campo VALOR
ALTER TABLE RWE_PROPIEDADGLOBAL alter VALOR type varchar(2048);

--Nuevos campos RWE_ANEXO
alter table RWE_ANEXO add FIRMAVALIDA bool DEFAULT FALSE;
alter table RWE_ANEXO add JUSTIFICANTE bool NOT NULL DEFAULT FALSE;
ALTER TABLE RWE_ANEXO add SIGNTYPE varchar(128);
ALTER TABLE RWE_ANEXO add SIGNFORMAT varchar(128);
ALTER TABLE RWE_ANEXO add SIGNPROFILE varchar(128);

--Nuevos campos en la tabla RWE_OFICIO_REMISION
ALTER TABLE RWE_OFICIO_REMISION DROP COLUMN IDINTERCAMBIOSIR;
ALTER TABLE RWE_OFICIO_REMISION add SIR bool NOT NULL DEFAULT FALSE;
ALTER TABLE RWE_OFICIO_REMISION add ID_INTERCAMBIO varchar(33);
ALTER TABLE RWE_OFICIO_REMISION add COD_ENT_REG_DEST varchar(21);
ALTER TABLE RWE_OFICIO_REMISION add DEC_ENT_REG_DEST varchar(80);
ALTER TABLE RWE_OFICIO_REMISION add NUM_REG_DESTINO varchar(255);
ALTER TABLE RWE_OFICIO_REMISION add FECHA_DESTINO timestamp;
ALTER TABLE RWE_OFICIO_REMISION add COD_ERROR varchar(255);
ALTER TABLE RWE_OFICIO_REMISION add DESC_ERROR varchar(2000);
ALTER TABLE RWE_OFICIO_REMISION add REINTENTOS int4 DEFAULT 0;
ALTER TABLE RWE_OFICIO_REMISION add CONTACTOSDESTINO varchar(2000);
ALTER TABLE RWE_OFICIO_REMISION add COD_ENT_REG_PROC varchar(21);
ALTER TABLE RWE_OFICIO_REMISION add DEC_ENT_REG_PROC varchar(80);

--Nuevos campos en la tabla RWE_TRAZABILIDAD
ALTER TABLE RWE_TRAZABILIDAD add TIPO int8 DEFAULT 1 not null;

ALTER TABLE RWE_TRAZABILIDAD add REGISTRO_SALIDA_RECT int8;
ALTER TABLE RWE_TRAZABILIDAD
        add constraint RWE_TRAZAB_RGSRCT_FK
        foreign key (REGISTRO_SALIDA_RECT)
        references RWE_REGISTRO_SALIDA;

-- Campo REGISTRO_SALIDA nulleable en la tabla RWE_TRAZABILIDAD
ALTER TABLE RWE_TRAZABILIDAD ALTER COLUMN REGISTRO_SALIDA DROP NOT NULL;
ALTER TABLE RWE_TRAZABILIDAD ALTER COLUMN OFICIO_REMISION DROP NOT NULL;

--Nuevos campos en la tabla RWE_REGISTRO_DETALLE
ALTER TABLE RWE_REGISTRO_DETALLE add INDICADOR_PRUEBA int4;
ALTER TABLE RWE_REGISTRO_DETALLE add TIPO_ANOTACION varchar(2);
ALTER TABLE RWE_REGISTRO_DETALLE add DEC_T_ANOTACION varchar(80);
ALTER TABLE RWE_REGISTRO_DETALLE add COD_ENT_REG_DEST varchar(21);
ALTER TABLE RWE_REGISTRO_DETALLE add DEC_ENT_REG_DEST varchar(80);
ALTER TABLE RWE_REGISTRO_DETALLE add ID_INTERCAMBIO varchar(33);

--Eliminamos las tabla RWE_ASIENTO_REGISTRAL_SIR, RWE_INTERESADO_SIR y RWE_ANEXO_SIR
DROP TABLE RWE_ASIENTO_REGISTRAL_SIR CASCADE;
DROP TABLE RWE_INTERESADO_SIR CASCADE;
DROP TABLE RWE_ANEXO_SIR CASCADE;

--Creamos la Tabla RWE_REGISTRO_SIR
create table RWE_REGISTRO_SIR (
        ID int8 not null,
        APLICACION varchar(4),
        COD_ASUNTO varchar(16),
        COD_ENT_REG varchar(21) not null,
        COD_ENT_REG_DEST varchar(21) not null,
        COD_ENT_REG_INI varchar(21) not null,
        COD_ENT_REG_ORI varchar(21) not null,
        COD_ERROR varchar(255),
        COD_UNI_TRA_DEST varchar(21),
        COD_UNI_TRA_ORI varchar(21),
        CONTACTO_USUARIO varchar(160),
        DEC_ENT_REG_DEST varchar(80),
        DEC_ENT_REG_INI varchar(80),
        DEC_ENT_REG_ORI varchar(80),
        DEC_T_ANOTACION varchar(80),
        DEC_UNI_TRA_DEST varchar(80),
        DEC_UNI_TRA_ORI varchar(80),
        DESC_ERROR varchar(2000),
        DOC_FISICA varchar(1) not null,
        ESTADO int4 not null,
        EXPONE varchar(4000),
        FECHA_ESTADO timestamp,
        FECHA_RECEPCION timestamp,
        FECHAR_EGISTRO timestamp not null,
        ID_INTERCAMBIO varchar(33) not null,
        INDICADOR_PRUEBA int4 not null,
        NOMBRE_USUARIO varchar(80),
        NUM_EXPEDIENTE varchar(80),
        NUMERO_REGISTRO varchar(20) not null,
        REINTENTOS int4,
        NUM_TRANSPORTE varchar(20),
        OBSERVACIONES varchar(50),
        REF_EXTERNA varchar(16),
        RESUMEN varchar(240) not null,
        SOLICITA varchar(4000),
        TIMESTAMP text,
        TIPO_ANOTACION varchar(2) not null,
        TIPO_REGISTRO int4 not null,
        TIPO_TRANSPORTE varchar(2),
        ENTIDAD int8 not null,
        primary key (ID)
    );
ALTER TABLE RWE_REGISTRO_SIR
        add constraint RWE_RES_ENTIDAD_FK
        foreign key (ENTIDAD)
        references RWE_ENTIDAD;

ALTER TABLE RWE_TRAZABILIDAD add REGISTRO_SIR int8;

ALTER TABLE RWE_TRAZABILIDAD
        add constraint RWE_TRAZAB_REGSIR_FK
        foreign key (REGISTRO_SIR)
        references RWE_REGISTRO_SIR;

--Creamos la Tabla RWE_ANEXO_SIR
create table RWE_ANEXO_SIR (
        ID int8 not null,
        CERTIFICADO text,
        FIRMA text,
        HASH text not null,
        ID_DOCUMENTO_FIRMADO varchar(50),
        IDENTIFICADOR_FICHERO varchar(50) not null,
        NOMBRE_FICHERO varchar(80) not null,
        OBSERVACIONES varchar(50),
        TIMESTAMP text,
        TIPO_DOCUMENTO varchar(2) not null,
        TIPO_MIME varchar(20),
        VAL_OCSP_CE text,
        VALIDEZ_DOCUMENTO varchar(2),
        ANEXO int8,
        REGISTRO_SIR int8,
        primary key (ID)
    );
ALTER TABLE RWE_ANEXO_SIR
        add constraint RWE_ANEXOSIR_ANEXO_FK
        foreign key (ANEXO)
        references RWE_ARCHIVO;

ALTER TABLE RWE_ANEXO_SIR
    add constraint RWE_ANEXOSIR_REGSIR_FK
    foreign key (REGISTRO_SIR)
    references RWE_REGISTRO_SIR;

--Creamos la Tabla RWE_INTERESADO_SIR
create table RWE_INTERESADO_SIR (
        ID int8 not null,
        CANAL_NOTIF_INTERESADO varchar(2),
        CANAL_NOTIF_REPRESENTANTE varchar(2),
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
        T_DOCUMENTO_INTERESADO varchar(1),
        T_DOCUMENTO_REPRESENTANTE varchar(1),
        REGISTRO_SIR int8,
        primary key (ID)
    );

ALTER TABLE RWE_INTERESADO_SIR
        add constraint RWE_INTERESADOSIR_REGSIR_FK
        foreign key (REGISTRO_SIR)
        references RWE_REGISTRO_SIR;

--Creamos la Tabla RWE_PLUGIN
create table RWE_PLUGIN (
        ID int8 not null,
        ACTIVO bool not null,
        CLASE varchar(1000) not null,
        DESCRIPCION varchar(2000) not null,
        ENTIDAD int8,
        NOMBRE varchar(255) not null,
        PROPIEDADES_ADMIN text,
        PROPIEDADES_ENTIDAD text,
        TIPO int8,
        primary key (ID)
    );
create index RWE_PLUGI_ENTIDA_FK_I on RWE_PLUGIN (ENTIDAD);

-- Nueva Tabla RWE_TRAZABILIDAD_SIR (11/05/2017)
create table RWE_TRAZABILIDAD_SIR (
        ID int8 not null,
        APLICACION varchar(4),
        COD_ENT_REG_DEST varchar(21) not null,
        COD_ENT_REG_ORI varchar(21) not null,
        COD_UNI_TRA_DEST varchar(21),
        CONTACTO_USUARIO varchar(160),
        DEC_ENT_REG_DEST varchar(80),
        DEC_ENT_REG_ORI varchar(80),
        DEC_UNI_TRA_DEST varchar(80),
        FECHA timestamp not null,
        NOMBRE_USUARIO varchar(80),
        OBSERVACIONES varchar(2000),
        tipo int8 not null,
        REGISTRO_ENTRADA int8,
        REGISTRO_SIR int8 not null,
        primary key (ID)
    );

alter table RWE_TRAZABILIDAD_SIR
    add constraint RWE_TRASIR_REGENT_FK
    foreign key (REGISTRO_ENTRADA)
    references RWE_REGISTRO_ENTRADA;

alter table RWE_TRAZABILIDAD_SIR
    add constraint RWE_TRASIR_REGSIR_FK
    foreign key (REGISTRO_SIR)
    references RWE_REGISTRO_SIR;

-- Nuevo campo Contador para genera el Identificador Intercambio SIR
ALTER TABLE RWE_ENTIDAD add CONTADOR_SIR int8;
ALTER TABLE RWE_ENTIDAD
    add constraint RWE_ENTIDAD_CONT_SIR_FK
    foreign key (CONTADOR_SIR)
    references RWE_CONTADOR;

--Aumento tamaño del campo USUARIO
alter table RWE_REGISTROLOPD_MIGRADO alter USUARIO type varchar(255);
alter table RWE_MODIFICACIONLOPD_MIGRADO alter USUARIO type varchar(255);