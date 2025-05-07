INSERT INTO RWE_PLUGIN (ID, ACTIVO, CLASE, DESCRIPCION, ENTIDAD, NOMBRE, PROPIEDADES_ADMIN, PROPIEDADES_ENTIDAD, TIPO) VALUES (
	RWE_ALL_SEQ.nextVal,
	1,
	'org.plugin.notib.apb.NotibApbPlugin',
	'Integració Notib',
	ID_ENTIDAD,
	'Integració Notib',
	NULL,
	'es.caib.regweb3.plugins.notib.apb.endpoint=https://notib-proves.portsdebalears.com/notibapi/interna
	es.caib.regweb3.plugins.notib.apb.username=regweb-notib
	es.caib.regweb3.plugins.notib.apb.password=
	es.caib.regweb3.plugins.notib.apb.retard.num.dies=0
	es.caib.regweb3.plugins.notib.apb.enviament.deh.activa=false
	es.caib.regweb3.plugins.notib.apb.forsar.entitat=
	es.caib.regweb3.plugins.notib.apb.caducitat.num.dies=15',
	13);
		
ALTER TABLE RWE_REMESA MODIFY IDENTIFICADOR NULL;
ALTER TABLE RWE_REMESA MODIFY CODIGO_ORIGEN NULL;
ALTER TABLE RWE_REMESA MODIFY FECHA_PUESTA_DISPOSICION NULL;
ALTER TABLE RWE_REMESA MODIFY ESTADO_NOTIFICA NULL;

ALTER TABLE RWE_REMESA ADD TIPO_REMESA VARCHAR2(64 CHAR);
ALTER TABLE RWE_REMESA ADD REFERENCIA VARCHAR2(255 CHAR);
ALTER TABLE RWE_REMESA ADD TITULAR_DIR3CODI VARCHAR2(255 CHAR);
ALTER TABLE RWE_REMESA ADD IDENTIFICADOR_INTERN VARCHAR2(255 CHAR);
ALTER TABLE RWE_REMESA ADD FECHA_CREACION TIMESTAMP;
ALTER TABLE RWE_REMESA ADD FECHA_ENVIO TIMESTAMP;
ALTER TABLE RWE_REMESA ADD FECHA_FINALIZACION TIMESTAMP;
ALTER TABLE RWE_REMESA ADD FECHA_ESTADO TIMESTAMP;
ALTER TABLE RWE_REMESA ADD MENSAJE_ERROR VARCHAR(1025 CHAR);

ALTER TABLE RWE_REMESA MODIFY FECHA_PUESTA_DISPOSICION TIMESTAMP;


UPDATE RWE_REMESA SET TIPO_REMESA = 'RECIBIDA';

UPDATE RWE_REMESA SET ESTADO_NOTIFICA = 'PENDIENTE_SEDE' WHERE ESTADO_NOTIFICA = 'PENDIENTE';
UPDATE RWE_REMESA SET ESTADO = 'PENDENT' WHERE ESTADO = 'PENDIENTE';
UPDATE RWE_REMESA SET ESTADO = 'LLEGIDA' WHERE ESTADO = 'LEIDA';
UPDATE RWE_REMESA SET ESTADO = 'FINALITZADA' WHERE ESTADO = 'FINALIZADA';
UPDATE RWE_REMESA SET ESTADO = 'PROCESSADA' WHERE ESTADO = 'PROCESADA';

INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.rolsac.jdbc.connection.url','Url JDBC de Roslac',null,1,'jdbc:oracle:thin:@//***************:1521/Test11g');
INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.rolsac.jdbc.connection.username','Usuario de conexión con la BD de Rolsac',null,1,'rolsac');
INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.rolsac.jdbc.connection.password','Contraseña de conexión con la BD de Rolsac',null,1,'rolsac');

INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.codigo.instancia.generica','Codi SIA instància genèrica',null,1,'915176');
INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.clasificar.tramite.telematico.excluir.ids','Codis dels tràmits telemàtics a excluir a l''hora de classificar registres d''instànica genèrica (separat amb |)',null,1,'PLANTILLA|PLANTILLA2');
INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.sede.procedimiento.url','Url base de la sede para acceder a un procedimiento',null,1,'http://seuelecng-proves.portsdebalears.com/seuapb/procediment/');
