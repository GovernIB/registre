--Nuevas propiedades en RWE_PROPIEDADGLOBAL
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.dir3caib.server','https://proves.caib.es/dir3caib',2,'Url de Dir3Caib',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.dir3caib.username','$regweb_dir3caib',2,'Usuario de conexión a Dir3Caib',null);
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.dir3caib.password','password',2,'Password de conexión Dir3Caib',null);
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.maxanexospermitidos','5',7,'Máximo número de anexos que se pueden adjuntar a un registro de entrada o salida',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.maxuploadsizeinbytes','10485760',7,'Tamaño máximo permitido por anexo en bytes',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.maxuploadsizetotal','15728640',7,'Tamaño máximo permitido para el total de anexos en bytes',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.formatospermitidos','.jpg, .jpeg, .odt, .odp, .ods, .odg, .docx, .xlsx, .pptx, .pdf, .png, .rtf, .svg, .tiff, .txt, .xml, .xsig',7,'Formatos permitidos para los anexos',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.mimespermitidos','image/jpeg,image/pjpeg,application/vnd.oasis.opendocument.text,application/vnd.oasis.opendocument.spreadsheet,application/vnd.oasis.opendocument.graphics,application/vnd.openxmlformats-officedocument.wordprocessingml.document,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/mspowerpoint,application/powerpoint,application/vndms-powerpoint,application/x-mspowerpoint,application/pdf,image/png,text/rtf,application/rtf,application/x-rtf,text/richtext,image/svg+xml,image/tiff,image/x-tiff,text/plain,application/xml',7,'Tipos Mime permitidos para los anexos',id FROM rwe_entidad;
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) VALUES( RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.cronExpression.inicializarContadores','0 0 0 1 1 ? *',1,'Expresión del cron para la inicializacion de contadores',null);
INSERT INTO rwe_propiedadglobal(id,clave,valor,tipo,descripcion,entidad) SELECT RWE_ALL_SEQ.nextVal, 'es.caib.regweb3.scanweb.absoluteurl',null,5,'URL Base absoluta para atacar los plugins de ScanWeb',id FROM rwe_entidad;
INSERT INTO RWE_PROPIEDADGLOBAL (id,clave,valor,tipo,descripcion,entidad) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.oficioSalida.fecha','26/05/2017',1,'Fecha a partir de al cual se generarán los Oficios de Remisión de Salida',null);

--Nuevo permiso (SIR) en la tabla RWE_PERMLIBUSU
INSERT INTO RWE_PERMLIBUSU (id,libro,usuario,activo,permiso) SELECT RWE_ALL_SEQ.nextVal,libro,usuario,0,9 FROM RWE_PERMLIBUSU where permiso=1;

--Modificación estado Oficio Remisión
update RWE_OFICIO_REMISION set ESTADO=13 where ESTADO=5;

--Modificación nombre aplicación
update RWE_REGISTRO_DETALLE set APLICACION='RWE3';

--Plugin UserInformation
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,entidad,PROPIEDADES_ADMIN) values (RWE_ALL_SEQ.nextVal,1, 'User Information','Información de usuarios','org.fundaciobit.plugins.userinformation.database.DataBaseUserInformationPlugin',5,null,'es.caib.regweb3.plugins.userinformation.database.jndi=java:/es.caib.seycon.db.wl' ||
'es.caib.regweb3.plugins.userinformation.database.users_table=SC_WL_USUARI' ||
'es.caib.regweb3.plugins.userinformation.database.username_column=USU_CODI' ||
'es.caib.regweb3.plugins.userinformation.database.administrationid_column=USU_NIF' ||
'es.caib.regweb3.plugins.userinformation.database.name_column=USU_NOM' ||
'es.caib.regweb3.plugins.userinformation.database.userroles_table=SC_WL_USUGRU' ||
'es.caib.regweb3.plugins.userinformation.database.userroles_rolename_column=UGR_CODGRU' ||
'es.caib.regweb3.plugins.userinformation.database.userroles_username_column=UGR_CODUSU');

--Plugin Custodia Annexos
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,entidad, PROPIEDADES_ADMIN) values (RWE_ALL_SEQ.nextVal,1, 'Custodia','Custodia de documentos','org.fundaciobit.plugins.documentcustody.filesystem.FileSystemDocumentCustodyPlugin',0,null,'es.caib.regweb3.plugins.documentcustody.filesystem.prefix=ANNEX_' ||
'es.caib.regweb3.plugins.documentcustody.filesystem.basedir=/app/caib/regweb/archivos' ||
'#es.caib.regweb3.plugins.documentcustody.filesystem.baseurl=http://localhost:8080/annexos/index.jsp?custodyID={1}');

--Plugin Justificante CAIB
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo, PROPIEDADES_ENTIDAD, entidad) SELECT RWE_ALL_SEQ.nextVal,1, 'Justificante','Genera el justificante SIR-CAIB de los registros','es.caib.regweb3.plugins.justificante.caib.JustificanteCaibPlugin',1,'# Mensaje para estampación del CVS en el justificante' ||
'# {0}=url, {1}=specialValue, {2}=csv' ||
'es.caib.regweb3.plugins.justificante.caib.estampacion=Este es un mensaje de estampación url:{0} specialValue:{1} csv:{2}' ||
'# Mensaje para la declaración en el justificante' ||
'es.caib.regweb3.plugins.justificante.caib.declaracion.es=declara que las imágenes electrónicas adjuntas son imagen fiel e íntegra de los documentos en soporte físico origen, en el marco de la normativa vigente.' ||
'es.caib.regweb3.plugins.justificante.caib.declaracion.ca=declara que les imatges electròniques adjuntes són imatge feel i íntegra dels documents en soport físic origen, en el marc de la normativa vigent.' ||
'# Mensaje para la ley en el justificante' ||
'es.caib.regweb3.plugins.justificante.caib.ley.es=El registro realizado está amparado en el Artículo 16 de la Ley 39/2016.' ||
'es.caib.regweb3.plugins.justificante.caib.ley.ca=El registre realitzat està amparat a l''Article 16 de la Llei 39/2016.' ||
'# Mensaje para la validación en el justificante' ||
'es.caib.regweb3.plugins.justificante.caib.validez.es=El presente justificante tiene validez a efectos de presentación de la documentación. El inicio del cómputo de plazos para la Administración, en su caso, vendrá determinado por la fecha de la entrada de su solicitud en el registro del Organismo competente.' ||
'es.caib.regweb3.plugins.justificante.caib.validez.ca=El present justificant té validesa a efectes de presentació de la documentació. L''inici del còmput de plaços per l''Administració, en el seu cas, vendrà determinat per la data de l''entrada de la seva sol·licitud en el registre de l''Organismo competent.' ||
'# Path para el logo de la Entidad' ||
'es.caib.regweb3.plugins.justificante.caib.logoPath=/app/caib/regweb/archivos/goib.png',id FROM rwe_entidad;

--Plugin PostProceso
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,entidad) SELECT RWE_ALL_SEQ.nextVal,1, 'PostProceso','Implementación base del plugin','es.caib.regweb3.plugins.postproceso.mock.PostProcesoMockPlugin',3,id FROM rwe_entidad;

--Plugin Distribución
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,entidad) SELECT RWE_ALL_SEQ.nextVal,1, 'Distribución','Implementación base del plugin, marca como distribuido el registro de entrada','es.caib.regweb3.plugins.distribucion.mock.DistribucionMockPlugin',2,id FROM rwe_entidad;

--Plugin Signature in Server
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,PROPIEDADES_ENTIDAD,entidad) SELECT RWE_ALL_SEQ.nextVal,1, 'Firma en servidor','Firma en servidor emprant @Firma federat','org.fundaciobit.plugins.signatureserver.afirmaserver.AfirmaServerSignatureServerPlugin',4,'# Classe org.fundaciobit.plugins.signatureserver.afirmaserver.AfirmaServerSignatureServerPlugin' ||
'# Obligatiori. Aplicació definida dins [Gestión de Aplicaciones] de @firma federat, on' ||
'# en l´apartat ´Parametros de la aplicación´ en el camp [Política de TimeStamp] té' ||
'# definit el valor [Sin TimeStamp]' ||
'es.caib.regweb3.plugins.signatureserver.afirmaserver.applicationID=CAIB.REGWEB' ||
'# Opcional. Aplicació definida dins "Gestión de Aplicaciones" de @firma federat, on' ||
'# en l´apartat ´Parametros de la aplicación´ en el camp "Política de TimeStamp" té definit' ||
'# el valor "Con TimeStamp". Si aquest valor no està definit llavors no es poden' ||
'# fer firmes amb segell de temps (No suporta  PAdES-T).' ||
'es.caib.regweb3.plugins.signatureserver.afirmaserver.applicationID_TimeStamp=' ||
'# Si signaturesSet.getCommonInfoSignature().getUsername() es null, llavors' ||
'# s´utilitza aquest valor com a sistema de selecció del certificat amb el que firmar' ||
'es.caib.regweb3.plugins.signatureserver.afirmaserver.defaultAliasCertificate=dgdt-pre' ||
'# Podeu descarregar-ho des de https://github.com/GovernIB/pluginsib/tree/pluginsib-1.0/plugins-validatesignature/afirmacxf/config/transformersTemplates' ||
'es.caib.regweb3.plugins.signatureserver.afirmaserver.TransformersTemplatesPath=/app/caib/regweb/transformersTemplates' ||
'#Propiedades de comunicacion con el repositorio de servicios' ||
'es.caib.regweb3.plugins.signatureserver.afirmaserver.endpoint=https://afirmapre.caib.es/afirmaws/services/DSSAfirmaSign' ||
'# USERNAME-PASSWORD Token' ||
'es.caib.regweb3.plugins.signatureserver.afirmaserver.authorization.username=REGWEB' ||
'es.caib.regweb3.plugins.signatureserver.afirmaserver.authorization.password=REGWEB' ||
'# CERTIFICATE Token' ||
'es.caib.regweb3.plugins.signatureserver.afirmaserver.authorization.ks.path=/app/caib/regweb/afirma_connect_cert.jks' ||
'es.caib.regweb3.plugins.signatureserver.afirmaserver.authorization.ks.type=JKS' ||
'es.caib.regweb3.plugins.signatureserver.afirmaserver.authorization.ks.password=<<PASSWORD>>' ||
'es.caib.regweb3.plugins.signatureserver.afirmaserver.authorization.ks.cert.alias=<<ALIAS>>' ||
'es.caib.regweb3.plugins.signatureserver.afirmaserver.authorization.ks.cert.password=<<PASSWORD>>',id FROM rwe_entidad;

--Plugin Custodia-Justificants
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,entidad, PROPIEDADES_ADMIN) values (RWE_ALL_SEQ.nextVal,1, 'Custodia-Justificante','Custodia de Justificants emprant Arxiu Digital CAIB','org.fundaciobit.plugins.documentcustody.arxiudigitalcaib.ArxiuDigitalCAIBDocumentCustodyPlugin',7,null,'# Específiques' ||
'' ||
'# Requerit. Nom de l´expedient. Important: Ha de ser únic' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.nom_expedient_EL=Registre_JUST_<#if (registro.origen)?? >S<#else>E</#if>_${(registro.libro.codigo)}_${.now?string[\"yyyy\"]}_${registro.numeroRegistro?string[\"0000000\"]}' ||
'' ||
'# Opcional. Nom de la carpeta dins de l´expedient.' ||
'# Si carpeta no està definida llavors custodyID = {uuid_expedient}' ||
'# Si carpeta està definida llavors custodyID =  {uuid_expedient}#{uuid_carpeta}.' ||
'# es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.nom_carpeta_EL=' ||
'' ||
'# Opcional. Per defecte sempre es crea Draft' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.createDraft=false' ||
'' ||
'# Opcional. Tanca l´expedient despres del primer guardat. Requereix "createDraft=false" ' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.tancarExpedient=true' ||
'' ||
'# Opcional' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.debug=false' ||
'' ||
'# =================  DADES CONNEXIO ========================' ||
'' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.connexio.url=https://esbpre.caib.es/' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.connexio.login.username=<<USERNAME>>' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.connexio.login.password=<<PASSWORD>>' ||
'' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.connexio.ignore_server_certificates=false' ||
'' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.connexio.codi_aplicacio=<<APP_CODE>>' ||
'' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.connexio.organitzacio=CAIB' ||
'' ||
'# Ciutada (opcional)' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.connexio.solicitant.nom_EL=${ciudadano_nombre}' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.connexio.solicitant.identificador_administratiu_EL=${ciudadano_idadministrativo}' ||
'' ||
'# Treballador (opcional)' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.connexio.usuari.username_EL=${usuarioEntidad.usuario.identificador}' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.connexio.usuari.identificador_administratiu_EL=${usuarioEntidad.usuario.documento}' ||
'' ||
'# Nom procediment (opcional)' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.connexio.nom_procediment_EL=<<PROCEDIMENT>>' ||
'' ||
'# =================  METADADES OBLIGATORIES COMUNS ========================' ||
'' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.serie_documental_EL=<<FALTA SERIE>>' ||
'#  Separats per comma' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.organs_EL=A04013511' ||
'' ||
'# ADMINISTRACION=1 //  CIUDADANO=0' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.origen_document_EL=${anexo.origenCiudadanoAdmin}' ||
'' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.data_creacio_EL =<#setting time_zone=´UTC´>${.now?string[\"yyyy-MM-dd´T´HH:mm:ss.sss´Z´\"]}' ||
'' ||
'# =================  METADADES OBLIGATORIES EXPEDIENT ========================' ||
'' ||
'# També inclou "Codigo Aplicacion" definit a la propietat "connexio.codi_aplicacio"' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.codi_procediment_EL=<<CODI_PROCEDIMENT>>' ||
'' ||
'# =================  METADADES OBLIGATORIES DOCUMENT ========================' ||
'' ||
'# També inclou "Codigo Aplicacion" definit a la propietat "connexio.codi_aplicacio"' ||
'' ||
'# EE01,EE02, EE03, EE04, EE99' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.estat_elaboracio_EL=<#switch anexo.validezDocumento><#case 1>EE01<#break><#case 2>EE02<#break><#case 3>EE03<#break><#case 4>EE03<#break><#default>EE99</#switch>' ||
'' ||
'# TIPO_DOC_ENI: TD01, TD02, TD03, ...' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.tipus_documental_EL=${anexo.tipoDocumental.codigoNTI}' ||
'' ||
'# Només es processa quan s´envia una firma' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.perfil_firma_EL=<#switch anexo.signProfile><#case "AdES-BES">BES<#break><#case "AdES-EPES">EPES<#break><#case "AdES-T">T<#break><#case "AdES-C">C<#break><#case "AdES-X">X<#break><#case "AdES-X1">X<#break><#case "AdES-X2">X<#break><#case "AdES-XL">XL<#break><#case "AdES-XL1">XL<#break><#case "AdES-XL2">XL<#break><#case "AdES-A">A<#break><#case "PAdES-LTV">LTV<#break><#case "PAdES-Basic">BES<#break></#switch>' ||
'' ||
'# --------------------------------------------------' ||
'# Genèriques' ||
'' ||
'# Opcional. Configuració per generar Hash de la baseurl quan {2}' ||
'#es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.hash.password=mypass' ||
'#  MD2, MD5, SHA,SHA-256,SHA-384,SHA-512' ||
'#es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.hash.algorithm=MD5' ||
'' ||
'# Opcional. Servei Millorat per generar la URL de Custodia emprant EL.' ||
'# Variables addicionals: csv, validationUrl_custodyID, validationUrl_custodyID_URLEncode i validationUrl_custodyID_Hash  ' ||
'# es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.baseurl_EL=http://regweb3.fundaciobit.org/custody/${csv}' ||
'' ||
'# Opcional. Si no definit retorna el custodyID' ||
'# Variables addicionals: csv' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.specialValue_EL=${registro.id}' ||
'' ||
'# ----------------------------------------------' ||
'# Opcional. Metadades Automàtiques per Expedient' ||
'# Nota: Només es permeten les definides en la classe es.caib.arxiudigital.apirest.constantes.MetadatosExpediente' ||
'' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.expedient.automaticmetadata_items=1,2' ||
'' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.expedient.automatic_metadata.1.name=eni:descripcion' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.expedient.automatic_metadata.1.valueEL=${registro.numeroRegistro?c} / ${registro.numeroRegistroFormateado}' ||
'' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.expedient.automatic_metadata.2.name=eni:termino_punto_acceso' ||
'es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.expedient.automatic_metadata.2.valueEL=Justificant' ||
'' ||
'# ----------------------------------------------' ||
'# Opcional. Metadades Automàtiques per Document Electrònic' ||
'# Nota: Només es permeten les definides en la classe es.caib.arxiudigital.apirest.constantes.MetadatosDocumento' ||
'' ||
'#es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.document.automaticmetadata_items=1,2,3' ||
'' ||
'#es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.document.automatic_metadata.1.name=eni:tipo_asiento_registral' ||
'#es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.document.automatic_metadata.1.valueEL=${registro.???????????}' ||
'' ||
'#es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.document.automatic_metadata.2.name=eni:origen' ||
'#es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.document.automatic_metadata.2.valueEL=${registro.origen}' ||
'' ||
'#es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.document.automatic_metadata.3.name=eni:codigo_oficina_registro' ||
'#es.caib.regweb3.plugins.documentcustody.arxiudigitalcaib.document.automatic_metadata.3.valueEL=${registro.???????????????}');

--Plugin Scan
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,PROPIEDADES_ENTIDAD,entidad) SELECT RWE_ALL_SEQ.nextVal,1, 'Applet/JNLP Scan','Scan emprant Applet/JNLP','org.fundaciobit.plugins.scanweb.iecisa.IECISAScanWebPlugin',6,'es.caib.regweb3.plugins.scanweb.iecisa.debug=false' ||
'es.caib.regweb3.plugins.scanweb.iecisa.forcejnlp=false' ||
'es.caib.regweb3.plugins.scanweb.iecisa.forcesign=false' ||
'es.caib.regweb3.plugins.scanweb.iecisa.closewindowwhenfinish=true',id FROM rwe_entidad;

-- Validate Signature Plugin (08/05/2017)
INSERT INTO RWE_PLUGIN(id,activo,nombre,descripcion,clase,tipo,propiedades_admin,entidad) VALUES(RWE_ALL_SEQ.nextval,1, 'Validar Firma - @Firma','Información y Validación de Firmas Mediante @firma','org.fundaciobit.plugins.validatesignature.afirmacxf.AfirmaCxfValidateSignaturePlugin',8,'# Obligatiori. Aplicació definida dins "Gestión de Aplicaciones" de @firma federat' ||
'es.caib.regweb3.plugins.validatesignature.afirmacxf.applicationID=CAIB.REGWEB' ||
'# Podeu descarregar-ho des de https://github.com/GovernIB/pluginsib/tree/pluginsib-1.0/plugins-validatesignature/afirmacxf/config/transformersTemplates' ||
'es.caib.regweb3.plugins.validatesignature.afirmacxf.TransformersTemplatesPath=/app/caib/regweb/transformersTemplates' ||
'es.caib.regweb3.plugins.validatesignature.afirmacxf.endpoint=https://afirmapre.caib.es/afirmaws/services/DSSAfirmaVerify' ||
'es.caib.regweb3.plugins.validatesignature.afirmacxf.printxml=false' ||
'# USERNAME-PASSWORD Token' ||
'es.caib.regweb3.plugins.validatesignature.afirmacxf.authorization.username=REGWEB' ||
'es.caib.regweb3.plugins.validatesignature.afirmacxf.authorization.password=REGWEB' ||
'# CERTIFICATE Token' ||
'es.caib.regweb3.plugins.validatesignature.afirmacxf.authorization.ks.path=/app/caib/regweb/afirma_connect_cert.jks' ||
'es.caib.regweb3.plugins.validatesignature.afirmacxf.authorization.ks.type=JKS' ||
'es.caib.regweb3.plugins.validatesignature.afirmacxf.authorization.ks.password=<<PASSWORD>>' ||
'es.caib.regweb3.plugins.validatesignature.afirmacxf.authorization.ks.cert.alias=<<ALIAS>>' ||
'es.caib.regweb3.plugins.validatesignature.afirmacxf.authorization.ks.cert.password=<<PASSWORD>>',null);

