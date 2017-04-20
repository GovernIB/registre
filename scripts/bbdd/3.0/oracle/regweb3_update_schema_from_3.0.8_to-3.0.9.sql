--Eliminar campo de la tabla RWE_ASIENTO_REGISTRAL_SIR
ALTER TABLE RWE_ASIENTO_REGISTRAL_SIR DROP COLUMN COD_ENT_REG;

--Actualizar el nombre de la aplicación para adaptarlo a SICRES3
update RWE_REGISTRO_DETALLE set APLICACION='RWE3';

--Aumento de tamaño del campo VALOR
ALTER TABLE RWE_PROPIEDADGLOBAL MODIFY VALOR varchar2(2048 char);

--Nuevas propiedades en RWE_PROPIEDADGLOBAL
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.maxanexospermitidos','5',7,'Máximo número de anexos que se pueden adjuntar a un registro de entrada o salida',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.maxuploadsizeinbytes','10485760',7,'Tamaño máximo permitido por anexo en bytes',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.maxuploadsizetotal','15728640',7,'Tamaño máximo permitido para el total de anexos en bytes',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.formatospermitidos','.jpg, .jpeg, .odt, .odp, .ods, .odg, .docx, .xlsx, .pptx, .pdf, .png, .rtf, .svg, .tiff, .txt, .xml, .xsig',7,'Formatos permitidos para los anexos',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.mimespermitidos','image/jpeg,image/pjpeg,application/vnd.oasis.opendocument.text,application/vnd.oasis.opendocument.spreadsheet,application/vnd.oasis.opendocument.graphics,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/mspowerpoint,application/powerpoint,application/vndms-powerpoint,application/x-mspowerpoint,application/pdf,image/png,text/rtf,application/rtf,application/x-rtf,text/richtext,image/svg+xml,image/tiff,image/x-tiff,text/plain,application/xml',7,'Tipos Mime permitidos para los anexos',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.postproceso.plugin','es.caib.regweb3.plugins.postproceso.mock.PostProcesoMockPlugin',1,'Clase del Plugin de post-proceso',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) VALUES( RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.cronExpression.inicializarContadores','0 0 0 1 1 ? *',1,'Expresión del cron para la inicializacion de contadores',null);
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.justificante.plugin','es.caib.regweb3.plugins.justificante.caib.JustificanteCaibPlugin',1,'Plugin de justificante CAIB',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.distribucion.plugin','es.caib.regweb3.plugins.distribucion.mock.DistribucionMockPlugin',1,'Clase del Plugin de distribución',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT nextval('RWE_ALL_SEQ'), 'es.caib.regweb3.scanweb.absoluteurl',null,5,'URL Base absoluta para atacar los plugins de ScanWeb',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.firmajustificante.plugin','org.fundaciobit.plugins.signatureserver.miniappletinserver.MiniAppletInServerSignatureServerPlugin',1,'Clase del Plugin de signature server',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.firmajustificante.plugins.signatureserver.miniappletinserver.base_dir','[PATH_XXX]',1,'Base del Plugin de signature server',id FROM rwe_entidad;


--SIR Anexos
alter table RWE_ANEXO add (FIRMAVALIDA NUMBER(1,0) DEFAULT 0);
alter table RWE_ANEXO add (JUSTIFICANTE NUMBER(1,0) DEFAULT 0);

--Nuevo permiso (SIR) en la tabla RWE_PERMLIBUSU
INSERT INTO RWE_PERMLIBUSU (id,libro,usuario,activo,permiso) SELECT RWE_ALL_SEQ.nextVal,libro,usuario,0,9 FROM RWE_PERMLIBUSU where permiso=1;

--Nuevos campos en la tabla RWE_OFICIO_REMISION
ALTER TABLE RWE_OFICIO_REMISION DROP COLUMN IDINTERCAMBIOSIR;
ALTER TABLE RWE_OFICIO_REMISION add (SIR NUMBER(1,0) DEFAULT 0);
ALTER TABLE RWE_OFICIO_REMISION add ID_INTERCAMBIO varchar2(33 char);
ALTER TABLE RWE_OFICIO_REMISION add NUM_REG_DESTINO varchar2(255 char);
ALTER TABLE RWE_OFICIO_REMISION add FECHA_DESTINO timestamp;
ALTER TABLE RWE_OFICIO_REMISION add COD_ERROR varchar2(255 char);
ALTER TABLE RWE_OFICIO_REMISION add DESC_ERROR varchar2(2000 char);
ALTER TABLE RWE_OFICIO_REMISION add REINTENTOS number(10,0) DEFAULT 0;

--Nuevos campos en la tabla RWE_TRAZABILIDAD
ALTER TABLE RWE_TRAZABILIDAD add (TIPO number(10,0) DEFAULT 1 not null);
UPDATE RWE_TRAZABILIDAD set TIPO=1;
ALTER TABLE RWE_TRAZABILIDAD add ASIENTO_REGISTRAL_SIR number(19,0);
ALTER table RWE_TRAZABILIDAD
        add constraint RWE_TRAZAB_ASR_FK
        foreign key (ASIENTO_REGISTRAL_SIR)
        references RWE_ASIENTO_REGISTRAL_SIR;

-- Campo REGISTRO_SALIDA nulleable en la tabla RWE_TRAZABILIDAD
ALTER TABLE RWE_TRAZABILIDAD MODIFY REGISTRO_SALIDA NULL;
ALTER TABLE RWE_TRAZABILIDAD MODIFY OFICIO_REMISION NULL;

--Nuevos campos en la tabla RWE_REGISTRO_DETALLE
ALTER TABLE RWE_REGISTRO_DETALLE add INDICADOR_PRUEBA number(10,0);
ALTER TABLE RWE_REGISTRO_DETALLE add TIPO_ANOTACION varchar2(2 char);
ALTER TABLE RWE_REGISTRO_DETALLE add DEC_T_ANOTACION varchar2(80 char);
ALTER TABLE RWE_REGISTRO_DETALLE add COD_ENT_REG_DEST varchar2(21 char);
ALTER TABLE RWE_REGISTRO_DETALLE add DEC_ENT_REG_DEST varchar2(80 char);
ALTER TABLE RWE_REGISTRO_DETALLE add ID_INTERCAMBIO varchar2(33 char);

-- Actualización código de estadod e oficio remision
update RWE_OFICIO_REMISION set ESTADO=13 where ESTADO=5;

--SOLO DESARROLLO Eliminar campos de RWE_OFICIO_REMISION
ALTER TABLE RWE_OFICIO_REMISION DROP CONSTRAINT RWE_OFIREM_ASR_FK;
ALTER TABLE RWE_OFICIO_REMISION DROP COLUMN ASIENTO_REGISTRAL_SIR;
ALTER TABLE RWE_OFICIO_REMISION DROP COLUMN FECHA_RECEPCION;
ALTER TABLE RWE_OFICIO_REMISION DROP COLUMN FECHA_ENVIO;
-- Solo desarrollo actualizar tipo trazabilidad
UPDATE RWE_TRAZABILIDAD set TIPO=3 where OFICIO_REMISION=null;
--Fin solo desarrollo