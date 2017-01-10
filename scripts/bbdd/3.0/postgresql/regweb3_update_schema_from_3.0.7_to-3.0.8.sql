--Nueva campo en la tabla RWE_PROPIEDADGLOBAL
alter table RWE_PROPIEDADGLOBAL add TIPO int8;

--Nou camp ACTIU a la taula RWE_CODIGOASUNTO
alter table RWE_CODIGOASUNTO add ACTIVO bool NOT NULL DEFAULT TRUE;

--Nueva campo en la tabla RWE_OFICIO_REMISION
alter table RWE_OFICIO_REMISION add TIPO_OFICIO int8 not null DEFAULT 1;

--Nueva tabla RWE_OFIREM_REGSAL
create table RWE_OFIREM_REGSAL (
    IDOFIREM int8 not null,
    IDREGSAL int8 not null
);

alter table RWE_OFIREM_REGSAL add constraint RWE_OFIREM_REGSAL_FK foreign key (IDREGSAL) references RWE_REGISTRO_SALIDA;
alter table RWE_OFIREM_REGSAL add constraint RWE_REGSAL_OFIREM_FK foreign key (IDOFIREM) references RWE_OFICIO_REMISION;

--Moficación campo tabla RWE_TRAZABILIDAD
ALTER TABLE RWE_TRAZABILIDAD ALTER COLUMN REGENT_ORIGEN DROP NOT NULL;

--Nuevo campo en la tabla RWE_ENTIDAD
alter table RWE_ENTIDAD add OFICIOREMISION bool NOT NULL DEFAULT TRUE;

--Nuevo permiso (Distribuir registros) en la tabla RWE_PERMLIBUSU
INSERT INTO RWE_PERMLIBUSU (id,libro,usuario,activo,permiso) SELECT nextval('RWE_ALL_SEQ'),libro,usuario,false,8 FROM RWE_PERMLIBUSU where permiso=1;

--Contenido para cada Entidad de la tabla RWE_PROPIEDADGLOBAL (Es necesario especificar el id de la Entidad a la que pertenecen)
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.resultsperpage.oficios','20',1,'Resultados por página en los Oficios pendientes de remisión',?);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.resultsperpage.lopd','20',1,'Resultados por página en los informes LOPD',?);