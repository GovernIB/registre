--Nuevos campos RWE_ANEXO
ALTER TABLE RWE_ANEXO add CUSTODIADO bool DEFAULT false not null;

--Nuevos campos RWE_COLA
ALTER TABLE RWE_COLA ADD TIPOREGISTRO int8;
ALTER TABLE RWE_COLA ADD FECHAPROCESADO timestamp;



