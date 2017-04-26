--Actualizar el nombre de la aplicación para adaptarlo a SICRES3
update RWE_REGISTRO_DETALLE set APLICACION='RWE3';

--Aumento de tamaño del campo VALOR
ALTER TABLE RWE_PROPIEDADGLOBAL alter VALOR type varchar(2048);

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
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT nextval('RWE_ALL_SEQ'), 'es.caib.regweb3.scanweb.absoluteurl',null,5,'URL Base absoluta para atacar los plugins de ScanWeb',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT nextval('RWE_ALL_SEQ'), 'es.caib.regweb3.firmajustificante.plugin','org.fundaciobit.plugins.signatureserver.miniappletinserver.MiniAppletInServerSignatureServerPlugin',1,'Clase del Plugin de signature server',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT nextval('RWE_ALL_SEQ'), 'es.caib.regweb3.firmajustificante.plugins.signatureserver.miniappletinserver.base_dir','[PATH_XXX]',1,'Base del Plugin de signature server',id FROM rwe_entidad;

--SIR Anexos
ALTER TABLE RWE_ANEXO add FIRMAVALIDA bool DEFAULT FALSE;
ALTER TABLE RWE_ANEXO add JUSTIFICANTE bool DEFAULT FALSE;

--Nuevo permiso (SIR) en la tabla RWE_PERMLIBUSU
INSERT INTO RWE_PERMLIBUSU (id,libro,usuario,activo,permiso) SELECT nextval('RWE_ALL_SEQ'),libro,usuario,false,9 FROM RWE_PERMLIBUSU where permiso=1;

--Nuevos campos en la tabla RWE_OFICIO_REMISION
ALTER TABLE RWE_OFICIO_REMISION DROP COLUMN IDINTERCAMBIOSIR;
ALTER TABLE RWE_OFICIO_REMISION add SIR bool NOT NULL DEFAULT FALSE;
ALTER TABLE RWE_OFICIO_REMISION add ID_INTERCAMBIO varchar(33);
ALTER TABLE RWE_OFICIO_REMISION add NUM_REG_DESTINO varchar(255);
ALTER TABLE RWE_OFICIO_REMISION add FECHA_DESTINO timestamp;
ALTER TABLE RWE_OFICIO_REMISION add COD_ERROR varchar(255);
ALTER TABLE RWE_OFICIO_REMISION add DESC_ERROR varchar(2000);
ALTER TABLE RWE_OFICIO_REMISION add REINTENTOS int4 DEFAULT 0;

--Nuevo campo en la tabla RWE_TRAZABILIDAD
ALTER TABLE RWE_TRAZABILIDAD add TIPO int8 DEFAULT 1 not null;

-- Campo REGISTRO_SALIDA nulleable en la tabla RWE_TRAZABILIDAD
ALTER TABLE RWE_TRAZABILIDAD ALTER COLUMN REGISTRO_SALIDA DROP NOT NULL;
ALTER TABLE RWE_TRAZABILIDAD ALTER COLUMN OFICIO_REMISION DROP NOT NULL;

--Nuevos campos en la tabla RWE_REGISTRO_DETALLE
ALTER TABLE RWE_REGISTRO_DETALLE add INDICADOR_PRUEBA int4;
ALTER TABLE RWE_REGISTRO_DETALLE add TIPO_ANOTACION varchar(2);
ALTER TABLE RWE_REGISTRO_DETALLE add DEC_T_ANOTACION varchar(80);
ALTER TABLE RWE_REGISTRO_DETALLE add COD_ENT_REG_DEST varchar(21);
ALTER TABLE RWE_REGISTRO_DETALLE add DEC_ENT_REG_DEST varchar(80);
ALTER TABLE RWE_REGISTRO_DETALLE add ID_INTERCAMBIO varchar(33);

-- Actualización código de estadod e oficio remision
update RWE_OFICIO_REMISION set ESTADO=13 where ESTADO=5;

--Eliminamos las tabla RWE_ASIENTO_REGISTRAL_SIR, RWE_INTERESADO_SIR y RWE_ANEXO_SIR
DROP TABLE RWE_ASIENTO_REGISTRAL_SIR CASCADE;
DROP TABLE RWE_INTERESADO_SIR CASCADE;
DROP TABLE RWE_ANEXO_SIR CASCADE;
ALTER TABLE RWE_TRAZABILIDAD DROP COLUMN ASIENTO_REGISTRAL_SIR;

--Creamos la Tabla RWE_REGISTRO_SIR
create table RWE_REGISTRO_SIR (
        ID int8 not null,
        APLICACION varchar(4),
        COD_ASUNTO varchar(16),
        COD_ENT_REG_DEST varchar(21) not null,
        COD_ENT_REG_INI varchar(21) not null,
        COD_ENT_REG_ORI varchar(21) not null,
        COD_ERROR varchar(255),
        COD_UNI_TRA_DEST varchar(21),
        COD_UNI_TRA_ORI varchar(21),
        CONTACTO_USUARIO varchar(160),
        DEC_ENT_REG_DEST varchar(80),
        DEC_ENT_REG_INI varchar(80),
        DEC_ENT_REG_ORI varchar(80),
        DEC_T_ANOTACION varchar(80),
        DEC_UNI_TRA_DEST varchar(80),
        DEC_UNI_TRA_ORI varchar(80),
        DESC_ERROR varchar(2000),
        DOC_FISICA varchar(1) not null,
        ESTADO int4 not null,
        EXPONE varchar(4000),
        FECHA_ESTADO timestamp,
        FECHA_RECEPCION timestamp,
        FECHAR_EGISTRO timestamp not null,
        fechaRegistroInicial timestamp,
        ID_INTERCAMBIO varchar(33) not null,
        INDICADOR_PRUEBA int4 not null,
        NOMBRE_USUARIO varchar(80),
        NUM_EXPEDIENTE varchar(80),
        NUMERO_REGISTRO varchar(20) not null,
        numeroRegistroInicial varchar(255),
        REINTENTOS int4,
        NUM_TRANSPORTE varchar(20),
        OBSERVACIONES varchar(50),
        REF_EXTERNA varchar(16),
        RESUMEN varchar(240) not null,
        SOLICITA varchar(4000),
        TIMESTAMP_REGISTRO bytea,
        timestampRegistroInicial bytea,
        TIPO_ANOTACION varchar(2) not null,
        TIPO_REGISTRO int4 not null,
        TIPO_TRANSPORTE varchar(2),
        ENTIDAD int8 not null,
        primary key (ID)
    );
ALTER TABLE RWE_REGISTRO_SIR
        add constraint RWE_RES_ENTIDAD_FK
        foreign key (ENTIDAD)
        references RWE_ENTIDAD;

ALTER TABLE RWE_TRAZABILIDAD add REGISTRO_SIR int8;

ALTER TABLE RWE_TRAZABILIDAD
        add constraint RWE_TRAZAB_REGSIR_FK
        foreign key (REGISTRO_SIR)
        references RWE_REGISTRO_SIR;

--Creamos la Tabla RWE_ANEXO_SIR
create table RWE_ANEXO_SIR (
        ID int8 not null,
        CERTIFICADO bytea,
        FIRMA bytea,
        HASH bytea not null,
        ID_DOCUMENTO_FIRMADO varchar(50),
        IDENTIFICADOR_FICHERO varchar(50) not null,
        NOMBRE_FICHERO varchar(80) not null,
        OBSERVACIONES varchar(50),
        TIMESTAMP bytea,
        TIPO_DOCUMENTO varchar(2) not null,
        TIPO_MIME varchar(20),
        VAL_OCSP_CERTIFICADO bytea,
        VALIDEZ_DOCUMENTO varchar(2),
        ANEXO int8,
        REGISTRO_SIR int8,
        primary key (ID)
    );
ALTER TABLE RWE_ANEXO_SIR
        add constraint RWE_ANEXOSIR_ANEXO_FK
        foreign key (ANEXO)
        references RWE_ARCHIVO;

ALTER TABLE RWE_ANEXO_SIR
    add constraint RWE_ANEXOSIR_REGSIR_FK
    foreign key (REGISTRO_SIR)
    references RWE_REGISTRO_SIR;

--Creamos la Tabla RWE_INTERESADO_SIR
create table RWE_INTERESADO_SIR (
        ID int8 not null,
        CANAL_NOTIF_INTERESADO varchar(2),
        CANAL_NOTIF_REPRESENTANTE varchar(2),
        COD_MUNICIPIO_INTERESADO varchar(5),
        COD_MUNICIPIO_REPRESENTANTE varchar(5),
        COD_PAIS_INTERESADO varchar(4),
        COD_PAIS_REPRESENTANTE varchar(4),
        CP_INTERESADO varchar(5),
        CP_REPRESENTANTE varchar(5),
        COD_PROVINCIA_INTERESADO varchar(2),
        COD_PROVINCIA_REPRESENTANTE varchar(2),
        EMAIL_INTERESADO varchar(160),
        EMAIL_REPRESENTANTE varchar(160),
        DIR_ELECTRONICA_INTERESADO varchar(160),
        DIR_ELECTRONICA_REPRESENTANTE varchar(160),
        DIRECCION_INTERESADO varchar(160),
        DIRECCION_REPRESENTANTE varchar(160),
        DOCUMENTO_INTERESADO varchar(17),
        DOCUMENTO_REPRESENTANTE varchar(17),
        NOMBRE_INTERESADO varchar(30),
        NOMBRE_REPRESENTANTE varchar(30),
        OBSERVACIONES varchar(160),
        APELLIDO1_INTERESADO varchar(30),
        APELLIDO1_REPRESENTANTE varchar(30),
        RAZON_SOCIAL_INTERESADO varchar(80),
        RAZON_SOCIAL_REPRESENTANTE varchar(80),
        APELLIDO2_INTERESADO varchar(30),
        APELLIDO2_REPRESENTANTE varchar(30),
        TELEFONO_INTERESADO varchar(20),
        TELEFONO_REPRESENTANTE varchar(20),
        T_DOCUMENTO_INTERESADO varchar(1),
        T_DOCUMENTO_REPRESENTANTE varchar(1),
        REGISTRO_SIR int8,
        primary key (ID)
    );

ALTER TABLE RWE_INTERESADO_SIR
        add constraint RWE_INTERESADOSIR_REGSIR_FK
        foreign key (REGISTRO_SIR)
        references RWE_REGISTRO_SIR;

--SOLO DESARROLLO Eliminar campos de RWE_OFICIO_REMISION
ALTER TABLE RWE_OFICIO_REMISION DROP CONSTRAINT RWE_OFIREM_ASR_FK;
ALTER TABLE RWE_OFICIO_REMISION DROP COLUMN ASIENTO_REGISTRAL_SIR;
ALTER TABLE RWE_OFICIO_REMISION DROP COLUMN FECHA_RECEPCION;
ALTER TABLE RWE_OFICIO_REMISION DROP COLUMN FECHA_ENVIO;
-- Solo desarrollo actualizar tipo trazabilidad
UPDATE RWE_TRAZABILIDAD set TIPO=3 where OFICIO_REMISION=null;
--Fin solo desarrollo