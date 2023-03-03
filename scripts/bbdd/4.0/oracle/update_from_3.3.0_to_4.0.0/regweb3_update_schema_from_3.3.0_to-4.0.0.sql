-- ANEXO NUEVOS CAMPOS SICRES4
alter table RWE_ANEXO add CODFORMUL varchar2(80 char);
alter table RWE_ANEXO add RESUMEN varchar2(160 char);
alter table RWE_ANEXO add ENDPOINTRFU varchar2(255 char);
alter table RWE_ANEXO add IDENTIFRFU varchar2(255 char);

--Cambio tamanyo ANEXOSIR
ALTER TABLE RWE_ANEXO_SIR MODIFY OBSERVACIONES varchar2(160 char);

--Nuevas tablas metadatos
create table RWE_METADATO_ANEXO (
   ID number(19,0) not null,
    CAMPO varchar2(80 char),
    TIPO number(19,0),
    VALOR varchar2(4000 char),
    ANEXO number(19,0),
    primary key (ID)
);

create table RWE_METADATO_REGENT (
   ID number(19,0) not null,
    CAMPO varchar2(80 char),
    TIPO number(19,0),
    VALOR varchar2(4000 char),
    REGISTRO_ENTRADA number(19,0),
    primary key (ID)
);

create table RWE_METADATO_REGSAL (
   ID number(19,0) not null,
    CAMPO varchar2(80 char),
    TIPO number(19,0),
    VALOR varchar2(4000 char),
    REGISTRO_SALIDA number(19,0),
    primary key (ID)
);

 alter table RWE_METADATO_ANEXO
   add constraint RWE_METANEX_ANEXO_FK
   foreign key (ANEXO)
   references RWE_ANEXO;

alter table RWE_METADATO_REGENT
   add constraint RWE_METAREN_REGENT_FK
   foreign key (REGISTRO_ENTRADA)
   references RWE_REGISTRO_ENTRADA;

alter table RWE_METADATO_REGSAL
   add constraint RWE_METRSAL_REGSAL_FK
   foreign key (REGISTRO_SALIDA)
   references RWE_REGISTRO_SALIDA;


-- Cambios tamanyo RegistroDetalle
ALTER TABLE RWE_REGISTRO_DETALLE MODIFY NUMTRANSPORTE varchar2(40 char);
ALTER TABLE RWE_REGISTRO_DETALLE MODIFY OBSERVACIONES varchar2(160 char);
ALTER TABLE RWE_REGISTRO_DETALLE MODIFY DEC_T_ANOTACION varchar2(160 char);
ALTER TABLE RWE_REGISTRO_DETALLE MODIFY DEC_ENT_REG_DEST varchar2(120 char);


--Cambios tamanyo RegistroSir
ALTER TABLE RWE_REGISTRO_SIR MODIFY DEC_ENT_REG_ORI varchar2(120 char);
ALTER TABLE RWE_REGISTRO_SIR MODIFY DEC_ENT_REG_DEST varchar2(120 char);
ALTER TABLE RWE_REGISTRO_SIR MODIFY DEC_UNI_TRA_DEST varchar2(120 char);
ALTER TABLE RWE_REGISTRO_SIR MODIFY OBSERVACIONES varchar2(160 char);
ALTER TABLE RWE_REGISTRO_SIR MODIFY DEC_ENT_REG_INI varchar2(120 char);

--
ALTER TABLE RWE_TRAZABILIDAD_SIR MODIFY DEC_ENT_REG_ORI varchar2(120 char);
ALTER TABLE RWE_TRAZABILIDAD_SIR MODIFY DEC_ENT_REG_DEST varchar2(120 char);
ALTER TABLE RWE_TRAZABILIDAD_SIR MODIFY DEC_UNI_TRA_DEST varchar2(120 char);
ALTER TABLE RWE_TRAZABILIDAD_SIR MODIFY APLICACION varchar2(20 char);



--Nuevos campos Interesado
alter table RWE_INTERESADO add RECEPNOTIF number(1,0);
alter table RWE_INTERESADO add TLFMOVIL varchar2(20 char);
alter table RWE_INTERESADO add AVISONOTIFEMAIL number(1,0);
alter table RWE_INTERESADO add AVISONOTIFSMS number(1,0);

----Renombrado CODIGODIRE
alter table RWE_INTERESADO rename column CODIGODIRE TO CODDIRUNIF;
ALTER TABLE RWE_INTERESADO MODIFY CODDIRUNIF varchar2(21 char);


--Nuevos campos RegistroSIR
ALTER TABLE RWE_REGISTRO_SIR ADD  CODIGO_SIA varchar2(80 char);
ALTER TABLE RWE_REGISTRO_SIR ADD  COD_UNI_TRA_INI varchar2(21 char) ;
ALTER TABLE RWE_REGISTRO_SIR ADD  DEC_UNI_TRA_INI varchar2(120 char);
ALTER TABLE RWE_REGISTRO_SIR ADD  FECHA_REGISTRO_PRESENTACION timestamp;
update RWE_REGISTRO_SIR set FECHA_REGISTRO_PRESENTACION=FECHAR_EGISTRO;
ALTER TABLE RWE_REGISTRO_SIR MODIFY (FECHA_REGISTRO_PRESENTACION timestamp not null);
ALTER TABLE RWE_REGISTRO_SIR ADD  MODO_REGISTRO varchar2(1 char);
ALTER TABLE RWE_REGISTRO_SIR ADD  REFERENCIA_UNICA number(1,0);

--Campos Modificados RegistroSIR
ALTER TABLE RWE_REGISTRO_SIR MODIFY APLICACION varchar2(20 char);
ALTER TABLE RWE_REGISTRO_SIR MODIFY DEC_T_ANOTACION varchar2(160 char);

--Nuevos campos InteresadoSIR
ALTER TABLE RWE_INTERESADO_SIR ADD COD_DIR_UNIF_INTERESADO varchar2(21 char);
ALTER TABLE RWE_INTERESADO_SIR ADD COD_DIR_UNIF_REPRESENTANTE varchar2(21 char);
ALTER TABLE RWE_INTERESADO_SIR ADD RECEP_NOTIF_INTERESADO number(1,0);
ALTER TABLE RWE_INTERESADO_SIR ADD RECEP_NOTIF_REPRESENTANTE number(1,0);
ALTER TABLE RWE_INTERESADO_SIR ADD NOTIF_EMAIL_INTERESADO number(1,0);
ALTER TABLE RWE_INTERESADO_SIR ADD NOTIF_EMAIL_REPRESENTANTE number(1,0);
ALTER TABLE RWE_INTERESADO_SIR ADD NOTIF_SMS_INTERESADO number(1,0);
ALTER TABLE RWE_INTERESADO_SIR ADD NOTIF_SMS_REPRESENTANTE number(1,0);
ALTER TABLE RWE_INTERESADO_SIR ADD TELEFONO_MOVIL_INTERESADO varchar2(20 char);
ALTER TABLE RWE_INTERESADO_SIR ADD TELEFONO_MOVIL_REPRESENTANTE varchar2(20 char);

--Nuevos campos AnexoSIR
ALTER TABLE RWE_ANEXO_SIR ADD COD_FORMULARIO varchar2(80 char);
ALTER TABLE RWE_ANEXO_SIR ADD RESUMEN varchar2(160 char);
ALTER TABLE RWE_ANEXO_SIR ADD URL_REPOSITORIO varchar2(1000 char);

--Nuevas tablas de metadatos
create table RWE_METADATO_REGSIR (
    ID number(19,0) not null,
    CAMPO varchar2(80 char),
    TIPO number(19,0),
    VALOR varchar2(4000 char),
    REGISTRO_SIR number(19,0) not null,
    primary key (ID)
);
alter table RWE_METADATO_REGSIR
       add constraint RWE_METARSIR_REGSIR_FK
       foreign key (REGISTRO_SIR)
       references RWE_REGISTRO_SIR;

create table RWE_METADATO_ANEXO_SIR (
    ID number(19,0) not null,
    CAMPO varchar2(80 char),
    TIPO number(19,0),
    VALOR varchar2(4000 char),
    ANEXO_SIR number(19,0) not null,
    primary key (ID)
);

alter table RWE_METADATO_ANEXO_SIR
        add constraint RWE_METANEX_ANEXOSIR_FK
        foreign key (ANEXO_SIR)
        references RWE_ANEXO_SIR;


--Nuevas secuencias
create sequence RWE_MTDRESIR_SEQ start with 1 increment by  1;
create sequence RWE_MTDANSIR_SEQ start with 1 increment by  1;
create sequence RWE_MTDRE_SEQ start with 1 increment by  1;
create sequence RWE_MTDRS_SEQ start with 1 increment by  1;
create sequence RWE_MTDAN_SEQ start with 1 increment by  1;



--Transformar codigoSIA de númerico a alfanúmerico
ALTER TABLE RWE_REGISTRO_DETALLE ADD (CODIGOSIA_temp  VARCHAR2(80 char));
update RWE_REGISTRO_DETALLE set CODIGOSIA_temp = CODIGOSIA;
ALTER TABLE RWE_REGISTRO_DETALLE DROP COLUMN CODIGOSIA;
ALTER TABLE RWE_REGISTRO_DETALLE
RENAME COLUMN CODIGOSIA_temp TO CODIGOSIA;


--Nuevos campos Interesado
alter table RWE_PERSONA add RECEPNOTIF number(1,0);
alter table RWE_PERSONA add TLFMOVIL varchar2(20 char);
alter table RWE_PERSONA add AVISONOTIFEMAIL number(1,0);
alter table RWE_PERSONA add AVISONOTIFSMS number(1,0);
ALTER TABLE RWE_PERSONA add CODDIRUNIF varchar2(21 char);

