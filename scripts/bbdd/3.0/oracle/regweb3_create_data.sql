
--ROL
INSERT INTO RWE_ROL (id,nombre,descripcion,orden) VALUES (1,'RWE_SUPERADMIN','Administrador',1);
INSERT INTO RWE_ROL (id,nombre,descripcion,orden) VALUES (2,'RWE_ADMIN','Admin. Entitat',2);
INSERT INTO RWE_ROL (id,nombre,descripcion,orden) VALUES (3,'RWE_USUARI','Operador',3);

--PROPIEDADES GLOBALES
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.dir3caib.server','',2,'Url de Dir3Caib',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.dir3caib.username','',2,'Usuario de conexión a Dir3Caib',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.dir3caib.password','',2,'Password de conexión Dir3Caib',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.defaultlanguage','ca',1,'Idioma por defecto',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.archivos.path',' ',1,'Directorio base para los archivos generales',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.showtimestamp','false',1,'Muestra la fecha/hora de compilación de la aplicación',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.iscaib','false',1,'Indica si es una instalación en la CAIB',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.preregistre','https://intranet.caib.es/zonaperback',1,'Url del PreRegistre de la CAIB',null);

--PLUGINS
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,entidad,PROPIEDADES_ADMIN) values (RWE_ALL_SEQ.nextVal,1, 'User Information','Información de usuarios','org.fundaciobit.plugins.userinformation.database.DataBaseUserInformationPlugin',5,null,'es.caib.regweb3.plugins.userinformation.database.jndi=java:/es.caib.seycon.db.wl
es.caib.regweb3.plugins.userinformation.database.users_table=SC_WL_USUARI
es.caib.regweb3.plugins.userinformation.database.username_column=USU_CODI
es.caib.regweb3.plugins.userinformation.database.administrationid_column=USU_NIF
es.caib.regweb3.plugins.userinformation.database.name_column=USU_NOM
es.caib.regweb3.plugins.userinformation.database.userroles_table=SC_WL_USUGRU
es.caib.regweb3.plugins.userinformation.database.userroles_rolename_column=UGR_CODGRU
es.caib.regweb3.plugins.userinformation.database.userroles_username_column=UGR_CODUSU');

INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,entidad, PROPIEDADES_ADMIN) values (RWE_ALL_SEQ.nextVal,1, 'Custodia','Custodia de documentos','org.fundaciobit.plugins.documentcustody.filesystem.FileSystemDocumentCustodyPlugin',0,null,'es.caib.regweb3.annex.plugins.documentcustody.filesystem.prefix=ANNEX_
es.caib.regweb3.plugins.documentcustody.filesystem.basedir=C:/Users/earrivi/Documents/Proyectos/SICRES3/REGWEB/archivos/
es.caib.regweb3.plugins.documentcustody.filesystem.baseurl=http://localhost:8080/annexos/index.jsp?custodyID={1}
es.caib.regweb3.annex.plugins.documentcustody.filesystem.prefix=ANNEX_');

INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,entidad, PROPIEDADES_ADMIN) values (RWE_ALL_SEQ.nextVal,1, 'Custodia-Justificante','Custodia de justificantes','org.fundaciobit.plugins.documentcustody.filesystem.FileSystemDocumentCustodyPlugin',7,null,'es.caib.regweb3.plugins.documentcustody.filesystem.prefix=JUST_
es.caib.regweb3.plugins.documentcustody.custodiajustificante.filesystem.basedir=C:/Users/earrivi/Documents/Proyectos/SICRES3/REGWEB/archivos/justificantes/
es.caib.regweb3.plugins.documentcustody.custodiajustificante.filesystem.baseurl=http://localhost:8080/annexos/index.jsp?custodyID={1}
es.caib.regweb3.annex.plugins.documentcustody.filesystem.prefix=JUST_');

INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,propiedades_admin,entidad) VALUES(RWE_ALL_SEQ.nextval,1, 'Validar Firma - @Firma','Información y Validación de Firmas Mediante @firma','org.fundaciobit.plugins.validatesignature.afirmacxf.AfirmaCxfValidateSignaturePlugin',8,'# Obligatiori. Aplicació definida dins "Gestión de Aplicaciones" de @firma federat
es.caib.regweb3.plugins.validatesignature.afirmacxf.applicationID=appPrueba
# Podeu descarregar-ho des de https://github.com/GovernIB/pluginsib/tree/pluginsib-1.0/plugins-validatesignature/afirmacxf/config/transformersTemplates
es.caib.regweb3.plugins.validatesignature.afirmacxf.TransformersTemplatesPath=D:/dades/dades/transformersTemplates
#http://afirma.redsara.es/afirmaws/services/DSSAfirmaVerify
#http://des-afirma.redsara.es/afirmaws/services/DSSAfirmaVerify
#http://localhost:9090/afirmaws/services/DSSAfirmaVerify
es.caib.regweb3.plugins.validatesignature.afirmacxf.endpoint=http://des-afirma.redsara.es/afirmaws/services/DSSAfirmaVerify
es.caib.regweb3.plugins.validatesignature.afirmacxf.printxml=false
# CERTIFICATE Token

es.caib.regweb3.plugins.validatesignature.afirmacxf.authorization.ks.path=D:/dades/dades/proves-dgidt.jks
es.caib.regweb3.plugins.validatesignature.afirmacxf.authorization.ks.type=JKS
es.caib.regweb3.plugins.validatesignature.afirmacxf.authorization.ks.password=<<KEYSTORE_PASSWORD>>
es.caib.regweb3.plugins.validatesignature.afirmacxf.authorization.ks.cert.alias=<<ALIAS>>
es.caib.regweb3.plugins.validatesignature.afirmacxf.authorization.ks.cert.password=<<CERTIFICATE_PASSWORD>>',null);

