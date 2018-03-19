--Nuevas propiedades en RWE_PROPIEDADGLOBAL
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.dir3caib.server','url',2,'Url de Dir3Caib',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.dir3caib.username','username',2,'Usuario de conexión a Dir3Caib',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.dir3caib.password','password',2,'Password de conexión Dir3Caib',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.sir.tamanoMaximoPorAnexo','10485760',7,'Tamaño máximo permitido por anexo en sir en bytes',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.sir.numMaxAnexos','5',7,'Máximo número de anexos que se pueden adjuntar a un registro de entrada o salida que va a SIR',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.sir.formatosAnexos','.jpg, .jpeg, .odt, .odp, .ods, .odg, .docx, .xlsx, .pptx, .pdf, .png, .rtf, .svg, .tiff, .txt, .xml, .xsig, .csig,.html',7,'Formatos permitidos para los anexos en sir',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.sir.tamanoMaxTotalAnexos','15728640',7,'Tamaño máximo permitido para el total de anexos en sir en bytes',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.maxuploadsizeinbytes','10485760',7,'Tamaño máximo permitido por anexo en bytes',id FROM rwe_entidad;
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.scanweb.absoluteurl',null,5,'URL Base absoluta para atacar los plugins de ScanWeb',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.oficioSalida.fecha','26/05/2019',1,'Fecha a partir de al cual se generarán los Oficios de Remisión de Salida',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.sir.reintentos','10',7,'Número máximo de reintentos para los envios SIR',id FROM rwe_entidad;

--Nuevo permiso (SIR) en la tabla RWE_PERMLIBUSU
INSERT INTO RWE_PERMLIBUSU (id,libro,usuario,activo,permiso) SELECT RWE_ALL_SEQ.nextVal,libro,usuario,0,9 FROM RWE_PERMLIBUSU where permiso=1;

--Modificación estado Oficio Remisión
update RWE_OFICIO_REMISION set ESTADO=13 where ESTADO=5;

--Modificación nombre aplicación
update RWE_REGISTRO_DETALLE set APLICACION='RWE3';

--Plugin UserInformation
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,entidad,PROPIEDADES_ADMIN) values (RWE_ALL_SEQ.nextVal,1, 'User Information','Información de usuarios','org.fundaciobit.plugins.userinformation.database.DataBaseUserInformationPlugin',5,null,'es.caib.regweb3.plugins.userinformation.database.jndi=java:/es.caib.seycon.db.wl
es.caib.regweb3.plugins.userinformation.database.users_table=SC_WL_USUARI
es.caib.regweb3.plugins.userinformation.database.username_column=USU_CODI
es.caib.regweb3.plugins.userinformation.database.administrationid_column=USU_NIF
es.caib.regweb3.plugins.userinformation.database.name_column=USU_NOM
es.caib.regweb3.plugins.userinformation.database.userroles_table=SC_WL_USUGRU
es.caib.regweb3.plugins.userinformation.database.userroles_rolename_column=UGR_CODGRU
es.caib.regweb3.plugins.userinformation.database.userroles_username_column=UGR_CODUSU');

--Plugin Custodia
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,PROPIEDADES_ENTIDAD, entidad) SELECT RWE_ALL_SEQ.nextVal,1, 'Custodia','Custodia de documentos','org.fundaciobit.plugins.documentcustody.filesystem.FileSystemDocumentCustodyPlugin',0,'es.caib.regweb3.plugins.documentcustody.filesystem.prefix=ANNEX_
es.caib.regweb3.plugins.documentcustody.filesystem.basedir=C:/xxxx/Anexos/',id FROM rwe_entidad;

--Plugin Custodia-Justificante
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,entidad,PROPIEDADES_ENTIDAD, entidad) SELECT RWE_ALL_SEQ.nextVal,1, 'Custodia-Justificante','Custodia de justificantes','org.fundaciobit.plugins.documentcustody.filesystem.FileSystemDocumentCustodyPlugin',7,'es.caib.regweb3.plugins.documentcustody.filesystem.prefix=JUST_
es.caib.regweb3.plugins.documentcustody.filesystem.basedir=D:/xxxx/Justificantes/',id FROM rwe_entidad;

--Plugin JustificanteMock
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,PROPIEDADES_ENTIDAD,entidad) SELECT RWE_ALL_SEQ.nextVal,1, 'Justificante','Genera el justificante SIR de los registros','es.caib.regweb3.plugins.justificante.mock.JustificanteMockPlugin',1,'# Mensaje para la declaración en el justificante
es.caib.regweb3.plugins.justificante.mock.declaracion.es=declara que las imágenes electrónicas adjuntas son imagen fiel e íntegra de los documentos en soporte físico origen, en el marco de la normativa vigente.
es.caib.regweb3.plugins.justificante.mock.declaracion.ca=declara que les imatges electròniques adjuntes són imatge feel i íntegra dels documents en soport físic origen, en el marc de la normativa vigent.
# Mensaje para la ley en el justificante
es.caib.regweb3.plugins.justificante.mock.ley.es=El registro realizado está amparado en el Artículo 16 de la Ley 39/2015.
es.caib.regweb3.plugins.justificante.mock.ley.ca=El registre realitzat està amparat a l''Article 16 de la Llei 39/2015.
# Mensaje para la validez en el justificante
es.caib.regweb3.plugins.justificante.mock.validez.es=El presente justificante tiene validez a efectos de presentación de la documentación. El inicio del cómputo de plazos para la Administración, en su caso, vendrá determinado por la fecha de la entrada de su solicitud en el registro del Organismo competente.
es.caib.regweb3.plugins.justificante.mock.validez.ca=El present justificant té validesa a efectes de presentació de la documentació. L''inici del còmput de plaços per l''Administració, en el seu cas, vendrà determinat per la data de l''entrada de la seva sol·licitud en el registre de l''Organismo competent.
# Path para el logo de la Entidad
es.caib.regweb3.plugins.justificante.mock.logoPath=xxxx/logo_entitat.jpg',id FROM rwe_entidad;

--Plugin Distribución
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,entidad) SELECT RWE_ALL_SEQ.nextVal,1, 'Distribución','Implementación base del plugin, cambia el estado de un Registro a Distribuido','es.caib.regweb3.plugins.distribucion.mock.DistribucionMockPlugin',2,id FROM rwe_entidad;

--Plugin Signature in Server
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,PROPIEDADES_ENTIDAD,entidad) SELECT RWE_ALL_SEQ.nextVal,1, 'Firma en servidor','Firma en servidor mediante el MiniApplet','org.fundaciobit.plugins.signatureserver.miniappletinserver.MiniAppletInServerSignatureServerPlugin',4,'# Base del Plugin de signature server
es.caib.regweb3.plugins.signatureserver.miniappletinserver.base_dir=xxxxxxxx/
# Si signaturesSet.getCommonInfoSignature().getUsername() es null, llavors
# s´utilitza aquest valor com a sistema de selecció del certificat amb el que firmar
es.caib.regweb3.plugins.signatureserver.miniappletinserver.defaultAliasCertificate=regweb3',id FROM rwe_entidad;

--Aumento tamaño del campo USUARIO
alter table RWE_REGISTROLOPD_MIGRADO MODIFY USUARIO varchar2(255 char);
alter table RWE_MODIFICACIONLOPD_MIGRADO MODIFY USUARIO varchar2(255 char);

--Deshabilitar SIR
UPDATE RWE_ENTIDAD set sir=0;

-- Realizar tantos inserts y updates como Entidades haya creadas, indicado su id
INSERT into RWE_CONTADOR (id, numero) VALUES (RWE_ALL_SEQ.nextVal,0);
UPDATE RWE_ENTIDAD set CONTADOR_SIR=RWE_ALL_SEQ.currval where id=?;