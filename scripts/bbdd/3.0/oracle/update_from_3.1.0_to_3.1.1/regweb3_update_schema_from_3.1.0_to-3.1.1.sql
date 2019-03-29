--Adaptacio nova arquitectura SIR
alter table RWE_REGISTRO_DETALLE add (CODIGOSIA number(19,0));
alter table RWE_REGISTRO_DETALLE add (PRESENCIAL number(1,0));
alter table RWE_REGISTRO_DETALLE add (TIPOENVIODOC varchar2(2 char));
alter table RWE_INTERESADO add (CODIGODIRE varchar2(15 char));

--Nuevo ROLES de Usuario WS
ALTER TABLE RWE_USUARIO add (RWE_WS_ENTRADA number(10,0) DEFAULT 0 not null);
ALTER TABLE RWE_USUARIO add (RWE_WS_SALIDA number(10,0) DEFAULT 0 not null);
ALTER TABLE RWE_USUARIO add (RWE_WS_CIUDADANO number(10,0) DEFAULT 0 not null);