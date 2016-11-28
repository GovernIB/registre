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

--Nuevo campo en la tabla RWE_ENTIDAD
alter table RWE_ENTIDAD add (OFICIOREMISION NUMBER(1) DEFAULT 1 NOT NULL);

--Contenido para cada Entidad de la tabla RWE_PROPIEDADGLOBAL (Es necesario especificar el id de la Entidad a la que pertenecen)
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.resultsperpage','10',1,'Resultados por página en los listados paginados',?);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.resultsperpage.oficios','20',1,'Resultados por página en los Oficios pendientes de remisión',?);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.resultsperpage.lopd','20',1,'Resultados por página en los informes LOPD',?);