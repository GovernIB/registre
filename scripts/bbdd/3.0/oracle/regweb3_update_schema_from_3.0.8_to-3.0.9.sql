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
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.justificante.mensaje.estampacion','Este es un mensaje de estampación {0} {1} {2}',1,'Mensaje de estampación del justificante',id FROM rwe_entidad;

--Nuevos campos RWE_ANEXO
alter table RWE_ANEXO add (FIRMAVALIDA NUMBER(1,0) DEFAULT 0);
alter table RWE_ANEXO add (JUSTIFICANTE NUMBER(1,0) NOT NULL DEFAULT 0);
ALTER TABLE RWE_ANEXO add SIGNTYPE varchar2(128 char);
ALTER TABLE RWE_ANEXO add SIGNFORMAT varchar2(128 char);
ALTER TABLE RWE_ANEXO add SIGNPROFILE varchar2(128 char);

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
update RWE_OFICIO_REMISION set ESTADO=13 where ESTADO=5; -- Actualización código de estadod e oficio remision

--Nuevos campos en la tabla RWE_TRAZABILIDAD
ALTER TABLE RWE_TRAZABILIDAD add (TIPO number(10,0) DEFAULT 1 not null);

ALTER TABLE RWE_TRAZABILIDAD add REGISTRO_SALIDA_RECT number(19,0);--04/05/2017
ALTER TABLE RWE_TRAZABILIDAD
        add constraint RWE_TRAZAB_RGSRCT_FK
        foreign key (REGISTRO_SALIDA_RECT)
        references RWE_REGISTRO_SALIDA;--04/05/2017

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
update RWE_REGISTRO_DETALLE set APLICACION='RWE3'; --Actualizar el nombre de la aplicación para adaptarlo a SICRES3

--Eliminamos las tabla RWE_ASIENTO_REGISTRAL_SIR, RWE_INTERESADO_SIR y RWE_ANEXO_SIR
DROP TABLE RWE_ASIENTO_REGISTRAL_SIR CASCADE CONSTRAINTS;
DROP TABLE RWE_INTERESADO_SIR CASCADE CONSTRAINTS;
DROP TABLE RWE_ANEXO_SIR CASCADE CONSTRAINTS;
ALTER TABLE RWE_TRAZABILIDAD DROP COLUMN ASIENTO_REGISTRAL_SIR;

--Creamos la Tabla RWE_REGISTRO_SIR
create table RWE_REGISTRO_SIR (
        ID number(19,0) not null,
        APLICACION varchar2(4 char),
        COD_ASUNTO varchar2(16 char),
        COD_ENT_REG_DEST varchar2(21 char) not null,
        COD_ENT_REG_INI varchar2(21 char) not null,
        COD_ENT_REG_ORI varchar2(21 char) not null,
        COD_ERROR varchar2(255 char),
        COD_UNI_TRA_DEST varchar2(21 char),
        COD_UNI_TRA_ORI varchar2(21 char),
        CONTACTO_USUARIO varchar2(160 char),
        DEC_ENT_REG_DEST varchar2(80 char),
        DEC_ENT_REG_INI varchar2(80 char),
        DEC_ENT_REG_ORI varchar2(80 char),
        DEC_T_ANOTACION varchar2(80 char),
        DEC_UNI_TRA_DEST varchar2(80 char),
        DEC_UNI_TRA_ORI varchar2(80 char),
        DESC_ERROR varchar2(2000 char),
        DOC_FISICA varchar2(1 char) not null,
        ESTADO number(10,0) not null,
        EXPONE varchar2(4000 char),
        FECHA_ESTADO timestamp,
        FECHA_RECEPCION timestamp,
        FECHAR_EGISTRO timestamp not null,
        fechaRegistroInicial timestamp,
        ID_INTERCAMBIO varchar2(33 char) not null,
        INDICADOR_PRUEBA number(10,0) not null,
        NOMBRE_USUARIO varchar2(80 char),
        NUM_EXPEDIENTE varchar2(80 char),
        NUMERO_REGISTRO varchar2(20 char) not null,
        numeroRegistroInicial varchar2(255 char),
        REINTENTOS number(10,0),
        NUM_TRANSPORTE varchar2(20 char),
        OBSERVACIONES varchar2(50 char),
        REF_EXTERNA varchar2(16 char),
        RESUMEN varchar2(240 char) not null,
        SOLICITA varchar2(4000 char),
        TIMESTAMP_REGISTRO raw(2000),
        timestampRegistroInicial raw(255),
        TIPO_ANOTACION varchar2(2 char) not null,
        TIPO_REGISTRO number(10,0) not null,
        TIPO_TRANSPORTE varchar2(2 char),
        ENTIDAD number(19,0) not null
    );
ALTER TABLE RWE_REGISTRO_SIR add constraint RWE_REGISTRO_SIR_pk primary key (ID);

ALTER TABLE RWE_REGISTRO_SIR
        add constraint RWE_RES_ENTIDAD_FK
        foreign key (ENTIDAD)
        references RWE_ENTIDAD;

ALTER TABLE RWE_TRAZABILIDAD add REGISTRO_SIR number(19,0);

ALTER TABLE RWE_TRAZABILIDAD
        add constraint RWE_TRAZAB_REGSIR_FK
        foreign key (REGISTRO_SIR)
        references RWE_REGISTRO_SIR;

--Creamos la Tabla RWE_ANEXO_SIR
create table RWE_ANEXO_SIR (
        ID number(19,0) not null,
        CERTIFICADO raw(255),
        FIRMA raw(255),
        HASH raw(255) not null,
        ID_DOCUMENTO_FIRMADO varchar2(50 char),
        IDENTIFICADOR_FICHERO varchar2(50 char) not null,
        NOMBRE_FICHERO varchar2(80 char) not null,
        OBSERVACIONES varchar2(50 char),
        TIMESTAMP raw(255),
        TIPO_DOCUMENTO varchar2(2 char) not null,
        TIPO_MIME varchar2(20 char),
        VAL_OCSP_CERTIFICADO raw(255),
        VALIDEZ_DOCUMENTO varchar2(2 char),
        ANEXO number(19,0),
        REGISTRO_SIR number(19,0)
    );

ALTER TABLE RWE_ANEXO_SIR add constraint RWE_ANEXO_SIR_pk primary key (ID);

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
        ID number(19,0) not null,
        CANAL_NOTIF_INTERESADO varchar2(2 char),
        CANAL_NOTIF_REPRESENTANTE varchar2(2 char),
        COD_MUNICIPIO_INTERESADO varchar2(5 char),
        COD_MUNICIPIO_REPRESENTANTE varchar2(5 char),
        COD_PAIS_INTERESADO varchar2(4 char),
        COD_PAIS_REPRESENTANTE varchar2(4 char),
        CP_INTERESADO varchar2(5 char),
        CP_REPRESENTANTE varchar2(5 char),
        COD_PROVINCIA_INTERESADO varchar2(2 char),
        COD_PROVINCIA_REPRESENTANTE varchar2(2 char),
        EMAIL_INTERESADO varchar2(160 char),
        EMAIL_REPRESENTANTE varchar2(160 char),
        DIR_ELECTRONICA_INTERESADO varchar2(160 char),
        DIR_ELECTRONICA_REPRESENTANTE varchar2(160 char),
        DIRECCION_INTERESADO varchar2(160 char),
        DIRECCION_REPRESENTANTE varchar2(160 char),
        DOCUMENTO_INTERESADO varchar2(17 char),
        DOCUMENTO_REPRESENTANTE varchar2(17 char),
        NOMBRE_INTERESADO varchar2(30 char),
        NOMBRE_REPRESENTANTE varchar2(30 char),
        OBSERVACIONES varchar2(160 char),
        APELLIDO1_INTERESADO varchar2(30 char),
        APELLIDO1_REPRESENTANTE varchar2(30 char),
        RAZON_SOCIAL_INTERESADO varchar2(80 char),
        RAZON_SOCIAL_REPRESENTANTE varchar2(80 char),
        APELLIDO2_INTERESADO varchar2(30 char),
        APELLIDO2_REPRESENTANTE varchar2(30 char),
        TELEFONO_INTERESADO varchar2(20 char),
        TELEFONO_REPRESENTANTE varchar2(20 char),
        T_DOCUMENTO_INTERESADO varchar2(1 char),
        T_DOCUMENTO_REPRESENTANTE varchar2(1 char),
        REGISTRO_SIR number(19,0)
    );

ALTER TABLE RWE_INTERESADO_SIR add constraint RWE_INTERESADO_SIR_pk primary key (ID);

ALTER TABLE RWE_INTERESADO_SIR
        add constraint RWE_INTERESADOSIR_REGSIR_FK
        foreign key (REGISTRO_SIR)
        references RWE_REGISTRO_SIR;


--Creamos la Tabla RWE_PLUGIN 04/05/2017
create table RWE_PLUGIN (
        ID number(19,0) not null,
        ACTIVO number(1,0) not null,
        CLASE varchar2(1000 char) not null,
        DESCRIPCION varchar2(2000 char) not null,
        ENTIDAD number(19,0),
        NOMBRE varchar2(255 char) not null,
        PROPIEDADES_ADMIN varchar2(2000 char),
        PROPIEDADES_ENTIDAD varchar2(2000 char),
        TIPO number(19,0)
    );
create index RWE_PLUGI_ENTIDA_FK_I on RWE_PLUGIN (ENTIDAD);
alter table RWE_PLUGIN add constraint RWE_PLUGIN_pk primary key (ID);

INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,entidad,PROPIEDADES_ADMIN) values (RWE_ALL_SEQ.nextVal,1, 'User Information','Información de usuarios','org.fundaciobit.plugins.userinformation.database.DataBaseUserInformationPlugin',5,null,'es.caib.regweb3.userinformationplugin.plugins.userinformation.database.jndi=java:/es.caib.seycon.db.wl
es.caib.regweb3.userinformationplugin.plugins.userinformation.database.users_table=SC_WL_USUARI
es.caib.regweb3.userinformationplugin.plugins.userinformation.database.username_column=USU_CODI
es.caib.regweb3.userinformationplugin.plugins.userinformation.database.administrationid_column=USU_NIF
es.caib.regweb3.userinformationplugin.plugins.userinformation.database.name_column=USU_NOM
es.caib.regweb3.userinformationplugin.plugins.userinformation.database.userroles_table=SC_WL_USUGRU
es.caib.regweb3.userinformationplugin.plugins.userinformation.database.userroles_rolename_column=UGR_CODGRU
es.caib.regweb3.userinformationplugin.plugins.userinformation.database.userroles_username_column=UGR_CODUSU');
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,entidad, PROPIEDADES_ADMIN) values (RWE_ALL_SEQ.nextVal,1, 'Custodia','Custodia de documentos','org.fundaciobit.plugins.documentcustody.filesystem.FileSystemDocumentCustodyPlugin',0,null,'es.caib.regweb3.annex.plugins.documentcustody.filesystem.prefix=ANNEX_
es.caib.regweb3.annex.plugins.documentcustody.filesystem.basedir=C:/Users/earrivi/Documents/Proyectos/SICRES3/REGWEB/archivos/
es.caib.regweb3.annex.plugins.documentcustody.filesystem.baseurl=http://localhost:8080/annexos/index.jsp?custodyID={1}');
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,entidad) SELECT RWE_ALL_SEQ.nextVal,1, 'Justificante','Implementación base del plugin, genera el justificante SIR-CAIB de los registros','es.caib.regweb3.plugins.justificante.caib.JustificanteCaibPlugin',1,id FROM rwe_entidad;
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,entidad) SELECT RWE_ALL_SEQ.nextVal,1, 'PostProceso','Implementación base del plugin, no realiza ninguna acción','es.caib.regweb3.plugins.postproceso.mock.PostProcesoMockPlugin',3,id FROM rwe_entidad;
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,entidad) SELECT RWE_ALL_SEQ.nextVal,1, 'Distribución','Implementación base del plugin, marca como distribuido el registro de entrada','es.caib.regweb3.plugins.distribucion.mock.DistribucionMockPlugin',2,id FROM rwe_entidad;
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,PROPIEDADES_ENTIDAD,entidad) SELECT RWE_ALL_SEQ.nextVal,1, 'Firma en servidor','Firma en servidor mediante el MiniApplet','org.fundaciobit.plugins.signatureserver.miniappletinserver.MiniAppletInServerSignatureServerPlugin',4,'# Base del Plugin de signature server
es.caib.regweb3.signatureserver.plugins.signatureserver.miniappletinserver.base_dir=C:/Users/earrivi/Documents/Proyectos/OTAE/REGWEB3/',id FROM rwe_entidad;
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,entidad, PROPIEDADES_ADMIN) values (RWE_ALL_SEQ.nextVal,1, 'Custodia-Justificante','Custodia de justificantes','org.fundaciobit.plugins.documentcustody.filesystem.FileSystemDocumentCustodyPlugin',7,null,'es.caib.regweb3.custodiajustificante.plugins.documentcustody.filesystem.prefix=JUST_
es.caib.regweb3.custodiajustificante.plugins.documentcustody.filesystem.basedir=C:/Users/earrivi/Documents/Proyectos/SICRES3/REGWEB/archivos/justificantes/
es.caib.regweb3.custodiajustificante.plugins.documentcustody.filesystem.baseurl=http://localhost:8080/annexos/index.jsp?custodyID={1}');

INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,PROPIEDADES_ENTIDAD,entidad) SELECT RWE_ALL_SEQ.nextVal,1, 'CAIB Scan','Scan mediante el sistema CAIB','org.fundaciobit.plugins.scanweb.caib.CAIBScanWebPlugin',6,'',id FROM rwe_entidad;
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,PROPIEDADES_ENTIDAD,entidad) SELECT RWE_ALL_SEQ.nextVal,1, 'DynamicWebTwain','Scan mediante el sistema DynamicWebTwain','es.limit.plugins.scanweb.dynamicwebtwain.DynamicWebTwainScanWebPlugin',6,'',id FROM rwe_entidad;