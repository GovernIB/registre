--Nueva campo en la tabla RWE_PROPIEDADGLOBAL
alter table RWE_PROPIEDADGLOBAL add TIPO int8;

--Nou camp ACTIU a la taula RWE_CODIGOASUNTO
alter table RWE_CODIGOASUNTO add ACTIVO bool NOT NULL DEFAULT TRUE;
update RWE_CODIGOASUNTO set ACTIVO=true;