--Eliminar campo de la tabla RWE_ASIENTO_REGISTRAL_SIR
ALTER TABLE RWE_ASIENTO_REGISTRAL_SIR DROP COLUMN COD_ENT_REG;

--Actualizar el nombre de la aplicación para adaptarlo a SICRES3
update RWE_REGISTRO_DETALLE set APLICACION='RWE3';

--Se debe indicar el id de la entidad
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.maxanexospermitidos','5',7,'Máximo número de anexos que se pueden adjuntar a un registro de entrada o salida',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.maxuploadsizeinbytes','10485760',7,'Tamaño máximo permitido por anexo en bytes',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.maxuploadsizetotal','15728640',7,'Tamaño máximo permitido para el total de anexos en bytes',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.formatospermitidos','.jpg, .jpeg, .odt, .odp, .ods, .odg, .docx, .xlsx, .pptx, .pdf, .png, .rtf, .svg, .tiff, .txt',7,'Formatos permitidos para los anexos',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.mimespermitidos','image/jpeg,image/pjpeg,application/vnd.oasis.opendocument.text,application/vnd.oasis.opendocument.spreadsheet,application/vnd.oasis.opendocument.graphics,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/mspowerpoint,application/powerpoint,application/vndms-powerpoint,application/x-mspowerpoint,application/pdf,image/png,text/rtf,application/rtf,application/x-rtf,text/richtext,image/svg+xml,image/tiff,image/x-tiff,text/plain',7,'Tipos Mime permitidos para los anexos',null);


--Aumento de tamaño del campo VALOR
ALTER TABLE RWE_PROPIEDADGLOBAL MODIFY VALOR varchar2(1024 char);

--TODO probar esta query
INSERT INTO rwe_propiedadglobal(id, clave, valor,tipo,descripcio,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.resultsperpage.oficios','20',1,'Resultados por página en los Oficios pendientes de remisión',entidad FROM rwe_entidad;

--SIR Anexos
alter table RWE_ANEXO add  FIRMAVALIDA NUMBER(1,0);
alter table RWE_ANEXO add  JUSTIFICANTE NUMBER(1,0);

update RWE_ANEXO set FIRMAVALIDA=0;
update RWE_ANEXO set JUSTIFICANTE=0;

