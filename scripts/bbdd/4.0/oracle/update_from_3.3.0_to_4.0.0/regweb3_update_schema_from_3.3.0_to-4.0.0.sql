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
    TIPO varchar2(1 char),
    VALOR varchar2(4000 char),
    ANEXO number(19,0) not null,
    primary key (ID)
);

create table RWE_METADATO_REGENT (
   ID number(19,0) not null,
    CAMPO varchar2(80 char),
    TIPO varchar2(1 char),
    VALOR varchar2(4000 char),
    REGISTRO_ENTRADA number(19,0) not null,
    primary key (ID)
);

create table RWE_METADATO_REGSAL (
   ID number(19,0) not null,
    CAMPO varchar2(80 char),
    TIPO varchar2(1 char),
    VALOR varchar2(4000 char),
    REGISTRO_SALIDA number(19,0) not null,
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
alter table RWE_INTERESADO add DIRUNIFICADO varchar2(21 char);
alter table RWE_INTERESADO add AVISONOTIFEMAIL number(1,0);
alter table RWE_INTERESADO add AVISONOTIFSMS number(1,0);



