ALTER TABLE RWE_REGISTRO_ENTRADA ALTER COLUMN FECHA DROP NOT NULL;
ALTER TABLE RWE_REGISTRO_ENTRADA ALTER COLUMN NUMREGFORMAT DROP NOT NULL;

--CAMBIO TIPO COLUMNA NUMREGISTRO
ALTER TABLE RWE_REGISTRO_ENTRADA ADD NUMREGISTRO_TMP int4;
UPDATE RWE_REGISTRO_ENTRADA SET NUMREGISTRO_TMP = NUMREGISTRO;
ALTER TABLE RWE_REGISTRO_ENTRADA ALTER COLUMN NUMREGISTRO DROP NOT NULL;
UPDATE RWE_REGISTRO_ENTRADA SET NUMREGISTRO = NULL;

ALTER TABLE RWE_REGISTRO_ENTRADA ALTER COLUMN NUMREGISTRO TYPE varchar(20);
UPDATE RWE_REGISTRO_ENTRADA SET NUMREGISTRO = NUMREGISTRO_TMP;
ALTER TABLE RWE_REGISTRO_ENTRADA DROP COLUMN NUMREGISTRO_TMP;

ALTER TABLE RWE_REGISTRO_SALIDA ALTER COLUMN FECHA DROP NOT NULL;
ALTER TABLE RWE_REGISTRO_SALIDA ALTER COLUMN NUMREGFORMAT DROP NOT NULL;

--CAMBIO TIPO COLUMNA NUMREGISTRO
ALTER TABLE RWE_REGISTRO_SALIDA ADD NUMREGISTRO_TMP int4;
UPDATE RWE_REGISTRO_SALIDA SET NUMREGISTRO_TMP = NUMREGISTRO;
ALTER TABLE RWE_REGISTRO_SALIDA ALTER COLUMN NUMREGISTRO DROP NOT NULL;
UPDATE RWE_REGISTRO_SALIDA SET NUMREGISTRO = NULL;
ALTER TABLE RWE_REGISTRO_SALIDA ALTER COLUMN NUMREGISTRO TYPE varchar(20);
UPDATE RWE_REGISTRO_SALIDA SET NUMREGISTRO = NUMREGISTRO_TMP;
ALTER TABLE RWE_REGISTRO_SALIDA DROP COLUMN NUMREGISTRO_TMP;

--CAMBIO TIPO COLUMNA NUMREG_ORIGEN
ALTER TABLE RWE_REGISTRO_DETALLE ADD NUMREG_ORIGEN_TMP varchar(20);
UPDATE RWE_REGISTRO_DETALLE SET NUMREG_ORIGEN_TMP = NUMREG_ORIGEN;
UPDATE RWE_REGISTRO_DETALLE SET NUMREG_ORIGEN = NULL;
ALTER TABLE RWE_REGISTRO_DETALLE ALTER COLUMN NUMREG_ORIGEN TYPE varchar(20);
UPDATE RWE_REGISTRO_DETALLE SET NUMREG_ORIGEN = NUMREG_ORIGEN_TMP;
ALTER TABLE RWE_REGISTRO_DETALLE DROP COLUMN NUMREG_ORIGEN_TMP;

ALTER TABLE RWE_LOPD ADD NUMREGISTRO_TMP int4;
UPDATE RWE_LOPD SET NUMREGISTRO_TMP = NUMREGISTRO;
ALTER TABLE RWE_LOPD ALTER COLUMN NUMREGISTRO DROP NOT NULL;
UPDATE RWE_LOPD SET NUMREGISTRO = NULL;
ALTER TABLE RWE_LOPD ALTER COLUMN NUMREGISTRO TYPE varchar(255);
UPDATE RWE_LOPD SET NUMREGISTRO = NUMREGISTRO_TMP;
ALTER TABLE RWE_LOPD DROP COLUMN NUMREGISTRO_TMP;


ALTER TABLE RWE_LOPD ALTER COLUMN ANYOREGISTRO DROP NOT NULL;

ALTER TABLE RWE_REGISTRO_SIR ADD DOCUMENTO_USUARIO varchar(17);
ALTER TABLE RWE_REGISTRO_SIR ALTER COLUMN ID_INTERCAMBIO DROP NOT NULL;

-- Nuevo Contador para genera el Identificador Intercambio SIR, realizar tantos inserts como Entidades haya creadas, indicado su id
INSERT into RWE_CONTADOR (id, numero) VALUES (nextval('RWE_ALL_SEQ'),0);
UPDATE RWE_ENTIDAD set CONTADOR_SIR=CURRVAL('RWE_ALL_SEQ') where id=?;

-- Identificar documento firma dettached (no disponible identificador intercanvi fins que s'envia a GEISER)
ALTER TABLE RWE_ANEXO_SIR ADD COLUMN DOCUMENTO int8 NULL;
ALTER TABLE RWE_ANEXO_SIR
		ADD CONSTRAINT RWE_DOCUMENTOSIR_ANEXO_FK
		FOREIGN KEY (DOCUMENTO)
		REFERENCES RWE_ANEXO_SIR;
		
-- Borrar formato número registro (ahora el número se obtiene de GEISER)
UPDATE RWE_ENTIDAD SET NUMREGISTRO = NULL;

ALTER TABLE RWE_REGISTRO_SIR ALTER COLUMN APLICACION TYPE varchar(10);
ALTER TABLE RWE_TRAZABILIDAD_SIR ALTER COLUMN APLICACION TYPE varchar(10);

-- Campo necesario para consultar identificador intercambio registros recibidos
ALTER TABLE RWE_REGISTRO_SIR ADD NUMERO_REGISTRO_ORIGEN varchar(20);

ALTER TABLE RWE_ANEXO_SIR ALTER COLUMN TIPO_MIME TYPE varchar(100);

-- Cron modificable 
INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.cron.actualizacion.envio.sir.periodo','Tiempo inicial actualización registros enviados vía SIR',null,1,600000); --10min
INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.cron.actualizacion.envio.sir.retardo','Retardo entre cada actualización registros enviados vía SIR',null,1,600000); --10min

-- Cron modificable
INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.cron.actualizacion.envio.recibido.sir.periodo','Tiempo inicial actualización registros recibidos vía SIR',null,1,600000); --10min
INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.cron.actualizacion.envio.recibido.sir.retardo','Retardo entre cada actualización registros recibidos vía SIR',null,1,600000); --10min

-- Cron modificable
INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.cron.actualizacion.id.envio.recibido.sir.periodo','Tiempo inicial actualización registros enviados vía SIR',null,1,900000); --15min
INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.cron.actualizacion.id.envio.recibido.sir.retardo','Tiempo inicial actualización registros enviados vía SIR',null,1,900000); --15min

-- Identificar si descargar justificante GEISER o generar el de REGWEB
ALTER TABLE RWE_REGISTRO_DETALLE ADD JUSTIFICANTE_GEISER BOOL NOT NULL DEFAULT FALSE;

ALTER TABLE RWE_ANEXO_SIR ALTER COLUMN IDENTIFICADOR_FICHERO DROP NOT NULL;

-- Forzar una oficina para soportar envíos SIR (lógico -> GEISER)
ALTER TABLE RWE_OFICINA ADD FORZAR_ENVIO_SIR BOOL NOT NULL DEFAULT TRUE;

ALTER TABLE RWE_REGISTRO_DETALLE ADD DIRECCION_POSTAL_DEST varchar(256);

INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.cron.actualizacion.envio.sir.reintent','Máximo reintentos consulta estado envío SIR en caso de error',null,1,20); -- 20


-- Cron modificable
INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.cron.actualizacion.anexos.pendientes.verificacion.firma.periodo','Tiempo inicial verificación firma anexos recibidos por Sistra',null,1,900000); --15min
INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.cron.actualizacion.anexos.pendientes.verificacion.firma.retardo','Retardo entre cada verificación de firma de los anexos recibidos por Sistra',null,1,900000); --15min

-- Identificar con una posible firma sin verificar (anexos ya comprobados)
ALTER TABLE RWE_ANEXO ADD FIRMA_VERIFICADA BOOL DEFAULT TRUE;
UPDATE RWE_ANEXO SET FIRMA_VERIFICADA = TRUE;

ALTER TABLE RWE_INTEGRACION MODIFY PETICION VARCHAR2(4000);

-- Permitir enviar correo al producirse un error de comunicación con GEISER
INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.enviar.mail.error.geiser','Permitir enviar correo al producirse un error de comunicación con GEISER',null,1,0); -- false
INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.entorno','Indicar entorno para el envío de correos',null,1,'PRE');

ALTER TABLE RWE_COLA ADD ANEXOS_VERIFICADOS BOOL DEFAULT TRUE;