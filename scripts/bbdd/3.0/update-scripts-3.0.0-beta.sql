

-- 13/03/2015  Unique multiple de codigo asunto (jpernia)

alter table RWE_CODIGOASUNTO drop constraint RWE_CODIGOASUNTO_CODIGO_uk;
alter table RWE_CODIGOASUNTO add constraint RWE_CODASUN_CODIGO_TIPASUN_UK unique (CODIGO, TIPOASUNTO);


--16/03/2015  Descarga fechas pasadas a Date.

alter table RWE_DESCARGA ADD FECHAINICIO2 TIMESTAMP(6);
alter table RWE_DESCARGA ADD FECHAFIN2 TIMESTAMP(6);
alter table RWE_DESCARGA ADD FECHAIMPORTACION2 TIMESTAMP(6);


UPDATE RWE_DESCARGA SET FECHAINICIO2 = TO_DATE(FECHAINICIO, 'dd/mm/yyyy');
UPDATE RWE_DESCARGA SET FECHAFIN2 = TO_DATE(FECHAFIN, 'dd/mm/yyyy');
UPDATE RWE_DESCARGA SET FECHAIMPORTACION2 = TO_DATE(FECHAIMPORTACION, 'dd/mm/yyyy');

alter table RWE_DESCARGA DROP COLUMN FECHAINICIO;
alter table RWE_DESCARGA DROP COLUMN FECHAFIN;
alter table RWE_DESCARGA DROP COLUMN FECHAIMPORTACION;

alter table RWE_DESCARGA RENAME COLUMN FECHAINICIO2 to FECHAINICIO;
alter table RWE_DESCARGA RENAME COLUMN FECHAFIN2 to FECHAFIN;
alter table RWE_DESCARGA RENAME COLUMN FECHAIMPORTACION2 to FECHAIMPORTACION;


-- 17/03/2015  Camp TipusScan dins Entitat (anadal)

alter table RWE_ENTIDAD add TIPSCAN varchar2(20 char);

-- 23/04/2015 Ampliació longitud camps (ve de dir3 madrid)   (POSTGRESQL)

ALTER TABLE rwe_catpais  alter column descripcionpais type character varying(100);

-- 23/04/2015 Ampliació longitud camps (ve de dir3 madrid)     (ORACLE)

ALTER TABLE rwe_catpais MODIFY descripcionpais VARCHAR2(100 CHAR);
