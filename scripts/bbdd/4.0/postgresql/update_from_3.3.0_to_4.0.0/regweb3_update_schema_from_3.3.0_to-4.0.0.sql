-- ANEXO NUEVOS CAMPOS SICRES4
alter table RWE_ANEXO add CODFORMUL varchar(80);
alter table RWE_ANEXO add RESUMEN varchar(160);
alter table RWE_ANEXO add ENDPOINTRFU varchar(255);
alter table RWE_ANEXO add IDENTIFRFU varchar(4000);

--cambio de tamanyo RWE_ANEXO
ALTER TABLE RWE_ANEXO ALTER OBSERVACIONES type varchar(160);

--Cambio tamanyo RWE_ANEXO_SIR
ALTER TABLE RWE_ANEXO_SIR ALTER OBSERVACIONES type varchar(160);



--Cambio a  null del campo hash en RWE_ANEXO_SIR
ALTER TABLE RWE_ANEXO_SIR ALTER COLUMN HASH DROP NOT NULL;

--Nuevas tablas metadatos
create table RWE_METADATO_ANEXO (
   ID int8 not null,
    CAMPO varchar(80),
    TIPO int8,
    VALOR varchar(4000),
    ANEXO int8,
    primary key (ID)
);

create table RWE_METADATO_REGENT (
   ID int8 not null,
    CAMPO varchar(80),
    TIPO int8,
    VALOR varchar(4000),
    REGISTRO_ENTRADA int8,
    primary key (ID)
);

create table RWE_METADATO_REGSAL (
   ID int8 not null,
    CAMPO varchar(80),
    TIPO int8,
    VALOR varchar(4000),
    REGISTRO_SALIDA int8,
    primary key (ID)
);

alter table if exists RWE_METADATO_ANEXO
   add constraint RWE_METANEX_ANEXO_FK
   foreign key (ANEXO)
   references RWE_ANEXO;

alter table if exists RWE_METADATO_REGENT
   add constraint RWE_METAREN_REGENT_FK
   foreign key (REGISTRO_ENTRADA)
   references RWE_REGISTRO_ENTRADA;

alter table if exists RWE_METADATO_REGSAL
   add constraint RWE_METRSAL_REGSAL_FK
   foreign key (REGISTRO_SALIDA)
   references RWE_REGISTRO_SALIDA;

-- Cambios tamanyo RWE_REGISTRO_DETALLE
ALTER TABLE RWE_REGISTRO_DETALLE ALTER NUMTRANSPORTE type varchar(40);
ALTER TABLE RWE_REGISTRO_DETALLE ALTER OBSERVACIONES type varchar(160);
ALTER TABLE RWE_REGISTRO_DETALLE ALTER DEC_T_ANOTACION type varchar(160);
ALTER TABLE RWE_REGISTRO_DETALLE ALTER DEC_ENT_REG_DEST type varchar(120);
ALTER TABLE RWE_REGISTRO_DETALLE ALTER OFICINAEXTERNO type varchar(21);


--Cambios tamanyo RWE_REGISTRO_SIR
ALTER TABLE RWE_REGISTRO_SIR ALTER DEC_ENT_REG_ORI type varchar(120);
ALTER TABLE RWE_REGISTRO_SIR ALTER DEC_ENT_REG_DEST type varchar(120);
ALTER TABLE RWE_REGISTRO_SIR ALTER DEC_UNI_TRA_DEST type varchar(120);
ALTER TABLE RWE_REGISTRO_SIR ALTER OBSERVACIONES type varchar(160);
ALTER TABLE RWE_REGISTRO_SIR ALTER DEC_ENT_REG_INI type varchar(120);
ALTER TABLE RWE_REGISTRO_SIR ALTER DEC_UNI_TRA_ORI type varchar(120);

-- Modificaciones en RWE_TRAZABILIDAD_SIR
ALTER TABLE RWE_TRAZABILIDAD_SIR ALTER DEC_ENT_REG_ORI type varchar(120);
ALTER TABLE RWE_TRAZABILIDAD_SIR ALTER DEC_ENT_REG_DEST type varchar(120);
ALTER TABLE RWE_TRAZABILIDAD_SIR ALTER DEC_UNI_TRA_DEST type varchar(120);
ALTER TABLE RWE_TRAZABILIDAD_SIR ALTER APLICACION type varchar(20);



-- Nuevos campos RWE_INTERESADO
alter table RWE_INTERESADO add RECEPNOTIF bool;
alter table RWE_INTERESADO add TLFMOVIL varchar(20);
alter table RWE_INTERESADO add AVISONOTIFEMAIL bool;
alter table RWE_INTERESADO add AVISONOTIFSMS bool;

-- Renombrado CODIGODIRE en RWE_INTERESADO
alter table RWE_INTERESADO rename CODIGODIRE TO CODDIRUNIF;
ALTER TABLE RWE_INTERESADO ALTER CODDIRUNIF type varchar(21);

--Nuevos campos RWE_REGISTRO_SIR
ALTER TABLE RWE_REGISTRO_SIR ADD CODIGO_SIA varchar(80);
ALTER TABLE RWE_REGISTRO_SIR ADD COD_UNI_TRA_INI varchar(21);
ALTER TABLE RWE_REGISTRO_SIR ADD DEC_UNI_TRA_INI varchar(120);
ALTER TABLE RWE_REGISTRO_SIR ADD FECHA_REGISTRO_PRESENTACION timestamp;
update RWE_REGISTRO_SIR set FECHA_REGISTRO_PRESENTACION=FECHAR_EGISTRO;
ALTER TABLE RWE_REGISTRO_SIR ALTER COLUMN FECHA_REGISTRO_PRESENTACION SET NOT NULL;
ALTER TABLE RWE_REGISTRO_SIR ADD MODO_REGISTRO varchar(1);
ALTER TABLE RWE_REGISTRO_SIR ADD REFERENCIA_UNICA bool;

--Campos Modificados RWE_REGISTRO_SIR
ALTER TABLE RWE_REGISTRO_SIR ALTER APLICACION type varchar(20);
ALTER TABLE RWE_REGISTRO_SIR ALTER DEC_T_ANOTACION type varchar(160);

--Nuevos campos RWE_INTERESADO_SIR
ALTER TABLE RWE_INTERESADO_SIR ADD COD_DIR_UNIF_INTERESADO varchar(21);
ALTER TABLE RWE_INTERESADO_SIR ADD COD_DIR_UNIF_REPRESENTANTE varchar(21);
ALTER TABLE RWE_INTERESADO_SIR ADD RECEP_NOTIF_INTERESADO bool;
ALTER TABLE RWE_INTERESADO_SIR ADD RECEP_NOTIF_REPRESENTANTE bool;
ALTER TABLE RWE_INTERESADO_SIR ADD NOTIF_EMAIL_INTERESADO bool;
ALTER TABLE RWE_INTERESADO_SIR ADD NOTIF_EMAIL_REPRESENTANTE bool;
ALTER TABLE RWE_INTERESADO_SIR ADD NOTIF_SMS_INTERESADO bool;
ALTER TABLE RWE_INTERESADO_SIR ADD NOTIF_SMS_REPRESENTANTE bool;
ALTER TABLE RWE_INTERESADO_SIR ADD TELEFONO_MOVIL_INTERESADO varchar(20);
ALTER TABLE RWE_INTERESADO_SIR ADD TELEFONO_MOVIL_REPRESENTANTE varchar(20);

--Nuevos campos RWE_ANEXO_SIR
ALTER TABLE RWE_ANEXO_SIR ADD COD_FORMULARIO varchar(80);
ALTER TABLE RWE_ANEXO_SIR ADD RESUMEN varchar(160);
ALTER TABLE RWE_ANEXO_SIR ADD URL_REPOSITORIO varchar(1000);
ALTER TABLE RWE_ANEXO_SIR ADD TIPO_FIRMA varchar(5);
ALTER TABLE RWE_ANEXO_SIR ADD CSV varchar(512);
ALTER TABLE RWE_ANEXO_SIR ADD REGULACION_CSV varchar(2000);
ALTER TABLE RWE_ANEXO_SIR ADD REFERENCIA_FIRMA varchar(255);
ALTER TABLE RWE_ANEXO_SIR ADD FIRMA_BASE64 bytea;

--Nuevas tablas de metadatos
create table RWE_METADATO_REGSIR (
   ID int8 not null,
    CAMPO varchar(80),
    TIPO int8,
    VALOR varchar(4000),
    REGISTRO_SIR int8,
    primary key (ID)
);

alter table if exists RWE_METADATO_REGSIR
       add constraint RWE_METARSIR_REGSIR_FK
       foreign key (REGISTRO_SIR)
       references RWE_REGISTRO_SIR;

create table RWE_METADATO_ANEXO_SIR (
   ID int8 not null,
    CAMPO varchar(80),
    TIPO int8,
    VALOR varchar(4000),
    ANEXO_SIR int8,
    primary key (ID)
);

alter table if exists RWE_METADATO_ANEXO_SIR
   add constraint RWE_METANEX_ANEXOSIR_FK
   foreign key (ANEXO_SIR)
   references RWE_ANEXO_SIR;


--Nuevas secuencias
create sequence RWE_MTDRESIR_SEQ start 1 increment 1;
create sequence RWE_MTDANSIR_SEQ start 1 increment 1;
create sequence RWE_MTDRE_SEQ start 1 increment 1;
create sequence RWE_MTDRS_SEQ start 1 increment 1;
create sequence RWE_MTDAN_SEQ start 1 increment 1;

--Transformar codigoSIA de númerico a alfanúmerico
ALTER TABLE RWE_REGISTRO_DETALLE ADD CODIGOSIA_temp  VARCHAR(80);
update RWE_REGISTRO_DETALLE set CODIGOSIA_temp = CODIGOSIA::varchar(80);
ALTER TABLE RWE_REGISTRO_DETALLE DROP COLUMN CODIGOSIA;
ALTER TABLE RWE_REGISTRO_DETALLE RENAME COLUMN CODIGOSIA_temp TO CODIGOSIA;

--Nuevos campos RWE_PERSONA
--ALTER TABLE RWE_INTERESADO ALTER CODDIRUNIF type varchar(21);
alter table RWE_PERSONA add RECEPNOTIF bool;
alter table RWE_PERSONA add TLFMOVIL varchar(20);
alter table RWE_PERSONA add AVISONOTIFEMAIL bool;
alter table RWE_PERSONA add AVISONOTIFSMS bool;
ALTER TABLE RWE_PERSONA add CODDIRUNIF varchar(21);

--Nuevo campo en RWE_OFICINA
ALTER TABLE RWE_OFICINA add ACTIVALIBSIR bool DEFAULT false;

--Nuevo campo en RWE_REGISTRO_SIR
ALTER TABLE RWE_REGISTRO_SIR ADD LIBSIR bool DEFAULT false;

-- AUMENTO DEL TAMANYO DE LOS CAMPOS CODIGOS EXTERNOS de 9 a 21 COMO INDICA SICRES4
ALTER TABLE RWE_REGISTRO_ENTRADA ALTER DESTEXTCOD type varchar(21);
ALTER TABLE RWE_REGISTRO_SALIDA ALTER DESTEXTCOD type varchar(21);
ALTER TABLE RWE_OFICIO_REMISION ALTER DESTINOEXTERNOCODIGO type varchar(21);


