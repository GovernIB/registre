--Adaptacio nova arquitectura SIR
alter table RWE_REGISTRO_DETALLE add (CODIGOSIA number(19,0));
alter table RWE_REGISTRO_DETALLE add (PRESENCIAL number(1,0));
alter table RWE_REGISTRO_DETALLE add (TIPOENVIODOC varchar2(2 char));
alter table RWE_INTERESADO add (CODIGODIRE varchar2(15 char));

--Nuevo ROLES de Usuario WS
ALTER TABLE RWE_USUARIO add (RWE_WS_ENTRADA number(10,0) DEFAULT 0 not null);
ALTER TABLE RWE_USUARIO add (RWE_WS_SALIDA number(10,0) DEFAULT 0 not null);
ALTER TABLE RWE_USUARIO add (RWE_WS_CIUDADANO number(10,0) DEFAULT 0 not null);

--Nueva propiedad para indicar que la entidad está en mantenimiento
alter table RWE_ENTIDAD add (MANTENIMIENTO number(1,0) DEFAULT 0);

--Nuevo campo "evento" en Registro entrada y salida
alter table RWE_REGISTRO_ENTRADA add (EVENTO number(19,0));
alter table RWE_REGISTRO_SALIDA add (EVENTO number(19,0));

--Nuevo campo entidad en codigo asunto
alter table RWE_CODIGOASUNTO add entidad number(19,0);


--TODO PENDIENTE PROBAR
--Eliminar tabla tipoasunto
--drop table RWE_TIPOASUNTO CASCADE CONSTRAINTS;

--Eliminar tipoAsunto de registro detalle (OJO DE MOMENTO NO LO ELIMINAMOS).
--drop column tipoasunto from RWE_REGISTRO_DETALLE CASCADE CONSTRAINTS;

--Eliminar campo tipoasunto de la tabla RWE_CODIGOASUNTO
--drop column tipoasunto from RWE_CODIGOASUNTO cascade constraints;