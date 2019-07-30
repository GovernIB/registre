--Adaptacio nova arquitectura SIR
alter table RWE_REGISTRO_DETALLE add CODIGOSIA int8;
alter table RWE_REGISTRO_DETALLE add PRESENCIAL bool;
alter table RWE_REGISTRO_DETALLE add TIPOENVIODOC varchar(2);
alter table RWE_INTERESADO add CODIGODIRE varchar(15);

--Nuevo ROLES de Usuario WS
ALTER TABLE RWE_USUARIO add RWE_WS_ENTRADA bool DEFAULT FALSE;
ALTER TABLE RWE_USUARIO add RWE_WS_SALIDA bool DEFAULT FALSE;
ALTER TABLE RWE_USUARIO add RWE_WS_CIUDADANO bool DEFAULT FALSE;

--Nueva propiedad para indicar que la entidad está en mantenimiento
ALTER TABLE RWE_ENTIDAD add MANTENIMIENTO bool DEFAULT FALSE;

--Nuevo campo "evento" en Registro entrada y salida
alter table RWE_REGISTRO_ENTRADA add EVENTO int8;
alter table RWE_REGISTRO_SALIDA add EVENTO int8;

--Añadir campo entidad a codigo asunto
alter table RWE_CODIGOASUNTO add entidad bigint;

--Eliminar tabla tipoasunto T
--drop table RWE_TIPOASUNTO CASCADE CONSTRAINTS;

--Eliminar tipoAsunto de registro detalle (OJO DE MOMENTO NO LO ELIMINAMOS).
--drop column tipoasunto from RWE_REGISTRO_DETALLE CASCADE CONSTRAINTS;
