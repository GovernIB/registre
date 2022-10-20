-- ANEXO NUEVOS CAMPOS SICRES4
alter table RWE_ANEXO add CODFORMUL varchar2(80 char);
alter table RWE_ANEXO add RESUMEN varchar2(160 char);
alter table RWE_ANEXO add ENDPOINTRFU varchar2(255 char);
alter table RWE_ANEXO add IDENTIFRFU varchar2(255 char);

--Cambio tamanyo ANEXOSIR
ALTER TABLE RWE_ANEXO_SIR MODIFY OBSERVACIONES varchar2(160 char);

--Nueva tabla METADATO
create table RWE_METADATO (
      ID int8 not null,
      CAMPO varchar(80),
      VALOR varchar(255),
      REGSALPART int8,
      REGSALGEN int8,
      REGENTPART int8,
      REGENTGEN int8,
      ANEXOPART int8,
      ANEXOGEN int8,
      primary key (ID)
);

alter table if exists RWE_METADATO
    add constraint RWE_METADPAR_REGENT_FK
    foreign key (REGSALPART)
    references RWE_REGISTRO_SALIDA;

alter table if exists RWE_METADATO
    add constraint RWE_METADGEN_REGENT_FK
    foreign key (REGSALGEN)
    references RWE_REGISTRO_SALIDA;

alter table if exists RWE_METADATO
    add constraint RWE_METADPAR_REGENT_FK
    foreign key (REGENTPART)
    references RWE_REGISTRO_ENTRADA;

alter table if exists RWE_METADATO
    add constraint RWE_METADGEN_REGENT_FK
    foreign key (REGENTGEN)
    references RWE_REGISTRO_ENTRADA;

alter table if exists RWE_METADATO
    add constraint RWE_METADPAR_ANEXO_FK
    foreign key (ANEXOPART)
    references RWE_ANEXO;

alter table if exists RWE_METADATO
    add constraint RWE_METADGEN_ANEXO_FK
    foreign key (ANEXOGEN)
    references RWE_ANEXO;

-- Cambios tamanyo RegistroDetalle
ALTER TABLE RWE_REGISTRO_DETALLE MODIFY NUMTRANSPORTE varchar2(40 char);
ALTER TABLE RWE_REGISTRO_DETALLE MODIFY OBSERVACIONES varchar2(160 char);
ALTER TABLE RWE_REGISTRO_DETALLE MODIFY DEC_T_ANOTACION varchar2(160 char);
ALTER TABLE RWE_REGISTRO_DETALLE MODIFY DEC_ENT_REG_DEST varchar2(120 char);

-- Paso a not null
update RWE_REGISTRO_DETALLE set INDICADOR_PRUEBA=0 where INDICADOR_PRUEBA is null;
ALTER TABLE RWE_REGISTRO_DETALLE MODIFY (INDICADOR_PRUEBA number(10,0) not null);

-- Campos nuevos RegistroDetalle
-- Actualización de campo nuevo not null;
UPDATE RWE_REGISTRO_DETALLE
SET FECHA_PRES_INTER = (
    SELECT fecha
    FROM rwe_registro_entrada
    WHERE rwe_registro_entrada.REGISTRO_DETALLE = RWE_REGISTRO_DETALLE.ID
);

alter table RWE_REGISTRO_DETALLE add FECHA_PRES_INTER timestamp;
UPDATE RWE_REGISTRO_DETALLE
SET FECHA_PRES_INTER = (
    SELECT fecha
    FROM rwe_registro_entrada
    WHERE rwe_registro_entrada.REGISTRO_DETALLE = RWE_REGISTRO_DETALLE.ID
);
ALTER TABLE RWE_REGISTRO_DETALLE MODIFY ( FECHA_PRES_INTER NOT NULL);
alter table RWE_REGISTRO_DETALLE add TIMESTAMP_PRES_INTER varchar2(255 char);
alter table RWE_REGISTRO_DETALLE add IDINTERCAMBIO_PREVIO varchar2(33 char);
alter table RWE_REGISTRO_DETALLE add COD_UNITRAM_INICIO varchar2(21 char);
alter table RWE_REGISTRO_DETALLE add DEC_UNITRAM_INICIO varchar2(120 char);

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



