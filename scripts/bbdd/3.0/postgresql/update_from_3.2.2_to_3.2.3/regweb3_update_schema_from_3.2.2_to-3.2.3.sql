--Nuevo campo en RWE_REGISTRO_DETALLE
ALTER TABLE RWE_REGISTRO_DETALLE ADD APLICACION_TELEMATICA varchar(255);

--Nuevos campos en RWE_ANEXO
ALTER TABLE RWE_ANEXO add confidencial bool DEFAULT false not null;
ALTER TABLE RWE_ANEXO add nombre_fichero varchar(255);
ALTER TABLE RWE_ANEXO add tamano_fichero int4;

--Nuevas secuencias
create sequence RWE_COLA_SEQ;
create sequence RWE_INT_SEQ;

