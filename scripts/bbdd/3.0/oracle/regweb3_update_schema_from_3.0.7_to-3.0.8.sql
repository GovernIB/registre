--Nueva campo en la tabla RWE_PROPIEDADGLOBAL
alter table RWE_PROPIEDADGLOBAL add TIPO number(19,0);

--Nou camp ACTIU a la taula RWE_CODIGOASUNTO
alter table RWE_CODIGOASUNTO add (ACTIVO NUMBER(1) DEFAULT 1 NOT NULL);

--Nueva campo en la tabla RWE_OFICIO_REMISION
alter table RWE_OFICIO_REMISION add (TIPO_OFICIO number(19,0) DEFAULT 1 not null);

--Nueva tabla RWE_OFIREM_REGSAL
create table RWE_OFIREM_REGSAL (
  IDOFIREM number(19,0) not null,
  IDREGSAL number(19,0) not null
);

alter table RWE_OFIREM_REGSAL add constraint RWE_OFIREM_REGSAL_FK foreign key (IDREGSAL) references RWE_REGISTRO_SALIDA;
alter table RWE_OFIREM_REGSAL add constraint RWE_REGSAL_OFIREM_FK foreign key (IDOFIREM) references RWE_OFICIO_REMISION;

--Moficación campo tabla RWE_TRAZABILIDAD
ALTER TABLE RWE_TRAZABILIDAD MODIFY REGENT_ORIGEN NULL;

