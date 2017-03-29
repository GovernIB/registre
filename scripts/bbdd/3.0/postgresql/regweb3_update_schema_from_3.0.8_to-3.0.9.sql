--Eliminar campo de la tabla RWE_ASIENTO_REGISTRAL_SIR
ALTER TABLE RWE_ASIENTO_REGISTRAL_SIR DROP COLUMN COD_ENT_REG;

--Actualizar el nombre de la aplicación para adaptarlo a SICRES3
update RWE_REGISTRO_DETALLE set APLICACION='RWE3';

--Aumento de tamaño del campo VALOR
alter table RWE_PROPIEDADGLOBAL alter VALOR type varchar(2048);

--Nuevas propiedades en RWE_PROPIEDADGLOBAL
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT nextval('RWE_ALL_SEQ'), 'es.caib.regweb3.maxanexospermitidos','5',7,'Máximo número de anexos que se pueden adjuntar a un registro de entrada o salida',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT nextval('RWE_ALL_SEQ'), 'es.caib.regweb3.maxuploadsizeinbytes','10485760',7,'Tamaño máximo permitido por anexo en bytes',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT nextval('RWE_ALL_SEQ'), 'es.caib.regweb3.maxuploadsizetotal','15728640',7,'Tamaño máximo permitido para el total de anexos en bytes',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT nextval('RWE_ALL_SEQ'), 'es.caib.regweb3.formatospermitidos','.jpg, .jpeg, .odt, .odp, .ods, .odg, .docx, .xlsx, .pptx, .pdf, .png, .rtf, .svg, .tiff, .txt,.xml, .xsig',7,'Formatos permitidos para los anexos',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT nextval('RWE_ALL_SEQ'), 'es.caib.regweb3.mimespermitidos','image/jpeg,image/pjpeg,application/vnd.oasis.opendocument.text,application/vnd.oasis.opendocument.spreadsheet,application/vnd.oasis.opendocument.graphics,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/mspowerpoint,application/powerpoint,application/vndms-powerpoint,application/x-mspowerpoint,application/pdf,image/png,text/rtf,application/rtf,application/x-rtf,text/richtext,image/svg+xml,image/tiff,image/x-tiff,text/plain,application/xml',7,'Tipos Mime permitidos para los anexos',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT nextval('RWE_ALL_SEQ'), 'es.caib.regweb3.postproceso.plugin','es.caib.regweb3.plugins.postproceso.mock.PostProcesoMockPlugin',1,'Clase del Plugin de post-proceso',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT nextval('RWE_ALL_SEQ'), 'es.caib.regweb3.cronExpression.inicializarContadores','0 0 0 1 1 ? *',1,'Expresión del cron para la inicializacion de contadores',null;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT nextval('RWE_ALL_SEQ'), 'es.caib.regweb3.justificante.plugin','es.caib.regweb3.plugins.justificante.caib.JustificanteCaibPlugin',1,'Plugin de justificante CAIB',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT nextval('RWE_ALL_SEQ'), 'es.caib.regweb3.distribucion.plugin','es.caib.regweb3.plugins.distribucion.mock.DistribucionMockPlugin',1,'Clase del Plugin de distribución',id FROM rwe_entidad;

--SIR Anexos
alter table RWE_ANEXO add FIRMAVALIDA bool DEFAULT FALSE;
alter table RWE_ANEXO add JUSTIFICANTE bool DEFAULT FALSE;

--Nuevo permiso (SIR) en la tabla RWE_PERMLIBUSU
INSERT INTO RWE_PERMLIBUSU (id,libro,usuario,activo,permiso) SELECT nextval('RWE_ALL_SEQ'),libro,usuario,false,9 FROM RWE_PERMLIBUSU where permiso=1;

--Nuevos campos en la tabla RWE_OFICIO_REMISION
ALTER TABLE RWE_OFICIO_REMISION DROP COLUMN IDINTERCAMBIOSIR;

ALTER TABLE RWE_OFICIO_REMISION add ASIENTO_REGISTRAL_SIR int8;
alter table RWE_OFICIO_REMISION
        add constraint RWE_OFIREM_ASR_FK
        foreign key (ASIENTO_REGISTRAL_SIR)
        references RWE_ASIENTO_REGISTRAL_SIR;

ALTER table RWE_OFICIO_REMISION add SIR bool NOT NULL DEFAULT FALSE;
ALTER TABLE RWE_OFICIO_REMISION add FECHA_RECEPCION timestamp;
ALTER TABLE RWE_OFICIO_REMISION add FECHA_ENVIO timestamp;
ALTER TABLE RWE_OFICIO_REMISION add COD_ERROR varchar(255);
ALTER TABLE RWE_OFICIO_REMISION add DESC_ERROR varchar(2000);
ALTER TABLE RWE_OFICIO_REMISION add REINTENTOS int4;

--Nuevo campo en la tabla RWE_TRAZABILIDAD
ALTER TABLE RWE_TRAZABILIDAD add ASIENTO_REGISTRAL_SIR int8;
alter table RWE_TRAZABILIDAD
        add constraint RWE_TRAZAB_ASR_FK
        foreign key (ASIENTO_REGISTRAL_SIR)
        references RWE_ASIENTO_REGISTRAL_SIR;

-- Campo REGISTRO_SALIDA nulleable en la tabla RWE_TRAZABILIDAD
ALTER TABLE RWE_TRAZABILIDAD ALTER COLUMN REGISTRO_SALIDA DROP NOT NULL;
ALTER TABLE RWE_TRAZABILIDAD ALTER COLUMN OFICIO_REMISION DROP NOT NULL;