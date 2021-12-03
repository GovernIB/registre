--ROL
INSERT INTO RWE_ROL (id,nombre,descripcion,orden) VALUES (1,'RWE_SUPERADMIN','Administrador',1);
INSERT INTO RWE_ROL (id,nombre,descripcion,orden) VALUES (2,'RWE_ADMIN','Admin. Entitat',2);
INSERT INTO RWE_ROL (id,nombre,descripcion,orden) VALUES (3,'RWE_USUARI','Operador',3);
INSERT INTO RWE_ROL (id,nombre,descripcion,orden) VALUES (4,'RWE_WS_ENTRADA','Usuario WS entrada',4);
INSERT INTO RWE_ROL (id,nombre,descripcion,orden) VALUES (5,'RWE_WS_SALIDA','Usuario WS salida',5);
INSERT INTO RWE_ROL (id,nombre,descripcion,orden) VALUES (6,'RWE_WS_CIUDADANO','Usuario WS ciudadano',6);
INSERT INTO RWE_ROL (id,nombre,descripcion,orden) VALUES (7,'DIB_USER_RW','Copias auténticas',7);

--PROPIEDADES GLOBALES
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.dir3caib.server','url',2,'Url de Dir3Caib',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.dir3caib.username','username',2,'Usuario de conexión a Dir3Caib',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.dir3caib.password','password',2,'Password de conexión Dir3Caib',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.sir.tamanoMaximoPorAnexo','10485760',7,'Tamaño máximo permitido por anexo en sir en bytes',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.sir.numMaxAnexos','5',7,'Máximo número de anexos que se pueden adjuntar a un registro de entrada o salida que va a SIR',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.sir.formatosAnexos','.jpg, .jpeg, .odt, .odp, .ods, .odg, .docx, .xlsx, .pptx, .pdf, .png, .rtf, .svg, .tiff, .txt, .xml, .xsig, .csig,.html',7,'Formatos permitidos para los anexos en sir',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.sir.tamanoMaxTotalAnexos','15728640',7,'Tamaño máximo permitido para el total de anexos en sir en bytes',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.scanweb.absoluteurl',null,5,'URL Base absoluta para atacar los plugins de ScanWeb',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.ayuda.url',null,1,'Enlace de ayuda a los usuarios de las Oficinas de registro.',null);

--PLUGINS
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,entidad,PROPIEDADES_ADMIN) SELECT nextval('RWE_ALL_SEQ'),true, 'User Information','Información de usuarios','org.fundaciobit.plugins.userinformation.database.DataBaseUserInformationPlugin',5,null,'es.caib.regweb3.plugins.userinformation.database.jndi=java:/es.caib.seycon.db.wl
es.caib.regweb3.plugins.userinformation.database.users_table=SC_WL_USUARI
es.caib.regweb3.plugins.userinformation.database.username_column=USU_CODI
es.caib.regweb3.plugins.userinformation.database.administrationid_column=USU_NIF
es.caib.regweb3.plugins.userinformation.database.name_column=USU_NOM
es.caib.regweb3.plugins.userinformation.database.userroles_table=SC_WL_USUGRU
es.caib.regweb3.plugins.userinformation.database.userroles_rolename_column=UGR_CODGRU
es.caib.regweb3.plugins.userinformation.database.userroles_username_column=UGR_CODUSU';

INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,entidad,PROPIEDADES_ADMIN) SELECT nextval('RWE_ALL_SEQ'),true, 'Custodia','Custodia de documentos','org.fundaciobit.plugins.documentcustody.filesystem.FileSystemDocumentCustodyPlugin',0,null,'es.caib.regweb3.annex.plugins.documentcustody.filesystem.prefix=ANNEX_
es.caib.regweb3.plugins.documentcustody.filesystem.basedir=C:/xxxx/Anexos/';

INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,entidad,PROPIEDADES_ADMIN) SELECT nextval('RWE_ALL_SEQ'),true, 'Custodia-Justificante','Custodia de justificantes','org.fundaciobit.plugins.documentcustody.filesystem.FileSystemDocumentCustodyPlugin',7,null,'es.caib.regweb3.plugins.documentcustody.filesystem.prefix=JUST_
es.caib.regweb3.plugins.documentcustody.filesystem.basedir=D:/xxxx/Justificantes/';