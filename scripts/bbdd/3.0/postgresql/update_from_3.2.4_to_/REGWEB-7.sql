ALTER TABLE RWE_REMESA ADD DOCUMENTO_DESCOMPRIMIDO BOOLEAN DEFAULT false; -- Campo para identificar si el documento legal ha sido descomprimido 
UPDATE RWE_REMESA SET DOCUMENTO_DESCOMPRIMIDO = false;

--INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.notificaciones.dehu.forzar.emisor','Código DIR3 del emisor a forzar para las notificaciones procedentes de la DEHú',null,1,'EA0004518');