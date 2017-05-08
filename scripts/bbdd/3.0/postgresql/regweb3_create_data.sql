BEGIN;

--ROL
INSERT INTO RWE_ROL (id,nombre,descripcion,orden) VALUES (1,'RWE_SUPERADMIN','Administrador',1);
INSERT INTO RWE_ROL (id,nombre,descripcion,orden) VALUES (2,'RWE_ADMIN','Admin. Entitat',2);
INSERT INTO RWE_ROL (id,nombre,descripcion,orden) VALUES (3,'RWE_USUARI','Operador',3);

--PROPIEDADES GLOBALES
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.defaultlanguage','ca',1,'Idioma por defecto',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.archivos.path','ca',1,'Directorio base para los archivos generales',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.showtimestamp','false',1,'Muestra la fecha/hora de compilación de la aplicación',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (nextval('RWE_ALL_SEQ'),'es.caib.regweb3.iscaib','false',1,'Indica si es una instalación en la CAIB',null);

--PLUGINS
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,entidad,PROPIEDADES_ADMIN) SELECT nextval('RWE_ALL_SEQ'),true, 'User Information','Información de usuarios','org.fundaciobit.plugins.userinformation.database.DataBaseUserInformationPlugin',5,null,'es.caib.regweb3.userinformationplugin.plugins.userinformation.database.jndi=java:/es.caib.seycon.db.wl
es.caib.regweb3.userinformationplugin.plugins.userinformation.database.users_table=SC_WL_USUARI
es.caib.regweb3.userinformationplugin.plugins.userinformation.database.username_column=USU_CODI
es.caib.regweb3.userinformationplugin.plugins.userinformation.database.administrationid_column=USU_NIF
es.caib.regweb3.userinformationplugin.plugins.userinformation.database.name_column=USU_NOM
es.caib.regweb3.userinformationplugin.plugins.userinformation.database.userroles_table=SC_WL_USUGRU
es.caib.regweb3.userinformationplugin.plugins.userinformation.database.userroles_rolename_column=UGR_CODGRU
es.caib.regweb3.userinformationplugin.plugins.userinformation.database.userroles_username_column=UGR_CODUSU';

COMMIT;