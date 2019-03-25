--Adaptacio nova arquitectura SIR
alter table RWE_REGISTRO_DETALLE add CODIGOSIA int8;
alter table RWE_REGISTRO_DETALLE add PRESENCIAL bool;
alter table RWE_REGISTRO_DETALLE add TIPOENVIODOC varchar(2);

alter table RWE_INTERESADO add CODIGODIRE varchar(15);