-- ANEXO NUEVOS CAMPOS SICRES4
alter table RWE_ANEXO add CODFORMUL varchar2(80 char);
alter table RWE_ANEXO add RESUMEN varchar2(160 char);
alter table RWE_ANEXO add ENDPOINTRFU varchar2(255 char);
alter table RWE_ANEXO add IDENTIFRFU varchar2(255 char);

--Cambio tamanyo ANEXOSIR
ALTER TABLE RWE_ANEXO_SIR MODIFY OBSERVACIONES varchar2(160 char);

create table RWE_METADATO_ANEXO (
   ID int8 not null,
    CAMPO varchar(80),
    TIPO int8,
    VALOR varchar(4000),
    ANEXO int8 not null,
    primary key (ID)
);

create table RWE_METADATO_REGENT (
   ID int8 not null,
    CAMPO varchar(80),
    TIPO int8,
    VALOR varchar(4000),
    REGISTRO_ENTRADA int8 not null,
    primary key (ID)
);

create table RWE_METADATO_REGSAL (
   ID int8 not null,
    CAMPO varchar(80),
    TIPO int8,
    VALOR varchar(4000),
    REGISTRO_SALIDA int8 not null,
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
alter table RWE_INTERESADO add DIRUNIFICADO varchar2(21 char);
alter table RWE_INTERESADO add AVISONOTIFEMAIL number(1,0);
alter table RWE_INTERESADO add AVISONOTIFSMS number(1,0);



