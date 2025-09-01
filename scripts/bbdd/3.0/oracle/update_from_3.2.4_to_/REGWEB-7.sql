ALTER TABLE RWE_REMESA ADD DOCUMENTO_DESCOMPRIMIDO NUMBER(1,0) DEFAULT 0; -- Campo para identificar si el documento legal ha sido descomprimido 
UPDATE RWE_REMESA SET DOCUMENTO_DESCOMPRIMIDO = 0;

--INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.notificaciones.dehu.forzar.emisor','Código DIR3 del emisor a forzar para las notificaciones procedentes de la DEHú',null,1,'EA0004518');