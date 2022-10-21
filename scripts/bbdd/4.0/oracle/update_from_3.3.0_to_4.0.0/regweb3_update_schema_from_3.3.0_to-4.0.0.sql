-- ANEXO NUEVOS CAMPOS SICRES4
alter table RWE_ANEXO add CODFORMUL varchar2(80 char);
alter table RWE_ANEXO add RESUMEN varchar2(160 char);
alter table RWE_ANEXO add ENDPOINTRFU varchar2(255 char);
alter table RWE_ANEXO add IDENTIFRFU varchar2(255 char);

--Cambio tamanyo ANEXOSIR
ALTER TABLE RWE_ANEXO_SIR MODIFY OBSERVACIONES varchar2(160 char);

--Nueva tabla METADATO
create table RWE_METADATO (
          ID number(19,0) not null,
          CAMPO varchar2(80 char),
          VALOR varchar2(255 char),
          REGSALPART number(19,0),
          REGSALGEN number(19,0),
          REGENTPART number(19,0),
          REGENTGEN number(19,0),
          ANEXOPART number(19,0),
          ANEXOGEN number(19,0),
          primary key (ID)
);

alter table RWE_METADATO
    add constraint RWE_METADPAR_REGENT_FK
        foreign key (REGSALPART)
            references RWE_REGISTRO_SALIDA;

alter table RWE_METADATO
    add constraint RWE_METADGEN_REGENT_FK
        foreign key (REGSALGEN)
            references RWE_REGISTRO_SALIDA;

alter table RWE_METADATO
    add constraint RWE_METADPAR_REGENT_FK
        foreign key (REGENTPART)
            references RWE_REGISTRO_ENTRADA;

alter table RWE_METADATO
    add constraint RWE_METADGEN_REGENT_FK
        foreign key (REGENTGEN)
            references RWE_REGISTRO_ENTRADA;

alter table RWE_METADATO
    add constraint RWE_METADPAR_ANEXO_FK
        foreign key (ANEXOPART)
            references RWE_ANEXO;

alter table RWE_METADATO
    add constraint RWE_METADGEN_ANEXO_FK
        foreign key (ANEXOGEN)
            references RWE_ANEXO;

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



