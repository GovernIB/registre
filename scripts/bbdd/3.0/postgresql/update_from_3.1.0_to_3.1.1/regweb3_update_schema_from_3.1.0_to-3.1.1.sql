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