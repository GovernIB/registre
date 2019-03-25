--Adaptacio nova arquitectura SIR
alter table RWE_REGISTRO_DETALLE add (CODIGOSIA number(19,0));
alter table RWE_REGISTRO_DETALLE add (PRESENCIAL number(1,0));
alter table RWE_REGISTRO_DETALLE add (TIPOENVIODOC varchar2(2 char));

alter table RWE_INTERESADO add (CODIGODIRE varchar2(15 char));