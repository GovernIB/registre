--Eliminar campo de la tabla RWE_ASIENTO_REGISTRAL_SIR
ALTER TABLE RWE_ASIENTO_REGISTRAL_SIR DROP COLUMN COD_ENT_REG;

--Actualizar el nombre de la aplicación para adaptarlo a SICRES3
update RWE_REGISTRO_DETALLE set APLICACION='RWE3';

--Aumento de tamaño del campo VALOR
ALTER TABLE RWE_PROPIEDADGLOBAL MODIFY VALOR varchar2(1024 char);

--Nuevas propiedades en RWE_PROPIEDADGLOBAL
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.maxanexospermitidos','5',7,'Máximo número de anexos que se pueden adjuntar a un registro de entrada o salida',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.maxuploadsizeinbytes','10485760',7,'Tamaño máximo permitido por anexo en bytes',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.maxuploadsizetotal','15728640',7,'Tamaño máximo permitido para el total de anexos en bytes',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.formatospermitidos','.jpg, .jpeg, .odt, .odp, .ods, .odg, .docx, .xlsx, .pptx, .pdf, .png, .rtf, .svg, .tiff, .txt, .xml, .xsig',7,'Formatos permitidos para los anexos',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.mimespermitidos','image/jpeg,image/pjpeg,application/vnd.oasis.opendocument.text,application/vnd.oasis.opendocument.spreadsheet,application/vnd.oasis.opendocument.graphics,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/mspowerpoint,application/powerpoint,application/vndms-powerpoint,application/x-mspowerpoint,application/pdf,image/png,text/rtf,application/rtf,application/x-rtf,text/richtext,image/svg+xml,image/tiff,image/x-tiff,text/plain,application/xml',7,'Tipos Mime permitidos para los anexos',id FROM rwe_entidad;

--SIR Anexos
alter table RWE_ANEXO add (FIRMAVALIDA NUMBER(1,0) DEFAULT 0);
alter table RWE_ANEXO add (JUSTIFICANTE NUMBER(1,0) DEFAULT 0);

--Nuevo permiso (SIR) en la tabla RWE_PERMLIBUSU
INSERT INTO RWE_PERMLIBUSU (id,libro,usuario,activo,permiso) SELECT RWE_ALL_SEQ.nextVal,libro,usuario,0,9 FROM RWE_PERMLIBUSU where permiso=1;

--Nuevos campos en la tabla RWE_OFICIO_REMISION
ALTER TABLE RWE_OFICIO_REMISION DROP COLUMN IDINTERCAMBIOSIR;

ALTER TABLE RWE_OFICIO_REMISION add ASIENTO_REGISTRAL_SIR number(19,0);
alter table RWE_OFICIO_REMISION
        add constraint RWE_OFIREM_ASR_FK
        foreign key (ASIENTO_REGISTRAL_SIR)
        references RWE_ASIENTO_REGISTRAL_SIR;

ALTER TABLE RWE_OFICIO_REMISION add (SIR NUMBER(1,0) DEFAULT 0);
ALTER TABLE RWE_OFICIO_REMISION add FECHA_RECEPCION timestamp;
ALTER TABLE RWE_OFICIO_REMISION add FECHA_ENVIO timestamp;
ALTER TABLE RWE_OFICIO_REMISION add COD_ERROR varchar2(255 char);
ALTER TABLE RWE_OFICIO_REMISION add DESC_ERROR varchar2(2000 char);
ALTER TABLE RWE_OFICIO_REMISION add REINTENTOS number(10,0);

--Nuevo campo en la tabla RWE_TRAZABILIDAD
ALTER TABLE RWE_TRAZABILIDAD add ASIENTO_REGISTRAL_SIR number(19,0);
alter table RWE_TRAZABILIDAD
        add constraint RWE_TRAZAB_ASR_FK
        foreign key (ASIENTO_REGISTRAL_SIR)
        references RWE_ASIENTO_REGISTRAL_SIR;

-- Campo REGISTRO_SALIDA nulleable en la tabla RWE_TRAZABILIDAD
ALTER TABLE RWE_TRAZABILIDAD MODIFY REGISTRO_SALIDA NULL;