- Substituir :entitatID per l'identificador de l'entitat



-- Documentos de decisión

-- TD01 - Resolución.
INSERT INTO rwe_tipodocumental(id, codigonti, entidad) VALUES (1001, 'TD01', :entitatID);
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1001, 'Resolució', 'ca');
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1001, 'Resolución', 'es');
 
-- TD02 - Acuerdo.
INSERT INTO rwe_tipodocumental(id, codigonti, entidad) VALUES (1002, 'TD02', :entitatID);
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1002, 'Acord', 'ca');
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1002, 'Acuerdo', 'es');
 
-- TD03 - Contrato. 
INSERT INTO rwe_tipodocumental(id, codigonti, entidad) VALUES (1003, 'TD03', :entitatID);
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1003, 'Contracte', 'ca');
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1003, 'Contrato', 'es');

-- TD04 - Convenio. 
INSERT INTO rwe_tipodocumental(id, codigonti, entidad) VALUES (1004, 'TD04', :entitatID);
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1004, 'Conveni', 'ca');
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1004, 'Convenio', 'es');

-- TD05 - Declaración.
INSERT INTO rwe_tipodocumental(id, codigonti, entidad) VALUES (1005, 'TD05', :entitatID);
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1005, 'Declaració', 'ca');
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1005, 'Declaración', 'es');

--   /*Documentos de transmisi�n*/ 

-- TD06 - Comunicación.
INSERT INTO rwe_tipodocumental(id, codigonti, entidad) VALUES (1006, 'TD06', :entitatID);
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1006, 'Comunicació', 'ca');
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1006, 'Comunicación', 'es');

-- TD07 - Notificación. 
INSERT INTO rwe_tipodocumental(id, codigonti, entidad) VALUES (1007, 'TD07', :entitatID);
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1007, 'Notificació', 'ca');
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1007, 'Notificación', 'es');

-- TD08 - Publicación. 
INSERT INTO rwe_tipodocumental(id, codigonti, entidad) VALUES (1008, 'TD08', :entitatID);
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1008, 'Publicació', 'ca');
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1008, 'Publicación', 'es');

-- TD09 - Acuse de recibo. 
INSERT INTO rwe_tipodocumental(id, codigonti, entidad) VALUES (1009, 'TD09', :entitatID);
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1009, 'Justificant de recepció', 'ca');
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1009, 'Acuse de recibo', 'es');


--	  /*Documentos de constancia*/ 

-- TD10 - Acta.
INSERT INTO rwe_tipodocumental(id, codigonti, entidad) VALUES (1010, 'TD10', :entitatID);
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1010, 'Acta', 'ca');
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1010, 'Acta', 'es');

-- TD11 - Certificado. 
INSERT INTO rwe_tipodocumental(id, codigonti, entidad) VALUES (1011, 'TD11', :entitatID);
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1011, 'Certificat', 'ca');
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1011, 'Certificado', 'es');

-- TD12 - Diligencia.
INSERT INTO rwe_tipodocumental(id, codigonti, entidad) VALUES (1012, 'TD12', :entitatID);
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1012, 'Diligència', 'ca');
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1012, 'Diligencia', 'es');
 
--  /*Documentos de juicio*/ 

-- TD13 - Informe. 
INSERT INTO rwe_tipodocumental(id, codigonti, entidad) VALUES (1013, 'TD13', :entitatID);
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1013, 'Informe', 'ca');
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1013, 'Informe', 'es');

-- /*Documentos de ciudadano*/ 

 -- TD14 - Solicitud.
INSERT INTO rwe_tipodocumental(id, codigonti, entidad) VALUES (1014, 'TD14', :entitatID);
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1014, 'Sol·licitud', 'ca');
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1014, 'Solicitud', 'es');

 -- TD15 - Denuncia.
INSERT INTO rwe_tipodocumental(id, codigonti, entidad) VALUES (1015, 'TD15', :entitatID);
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1015, 'Denúncia', 'ca');
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1015, 'Denuncia', 'es');
 
 -- TD16 - Alegación
INSERT INTO rwe_tipodocumental(id, codigonti, entidad) VALUES (1016, 'TD16', :entitatID);
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1016, 'Al·legació', 'ca');
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1016, 'Alegación', 'es');

 -- TD17 - Recurso
 INSERT INTO rwe_tipodocumental(id, codigonti, entidad) VALUES (1017, 'TD17', :entitatID);
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1017, 'Recurs', 'ca');
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1017, 'Recurso', 'es');

 -- TD18 - Comunicación al ciudadano. 
 INSERT INTO rwe_tipodocumental(id, codigonti, entidad) VALUES (1018, 'TD18', :entitatID);
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1018, 'Comunicació al ciutadà', 'ca');
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1018, 'Comunicación al ciudadano', 'es');

 -- TD19 - Factura. 
 INSERT INTO rwe_tipodocumental(id, codigonti, entidad) VALUES (1019, 'TD19', :entitatID);
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1019, 'Factura', 'ca');
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1019, 'Factura', 'es');

 -- TD20 - Otros incautados
 INSERT INTO rwe_tipodocumental(id, codigonti, entidad) VALUES (1020, 'TD20', :entitatID);
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1020, 'Altres confiscats', 'ca');
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1020, 'Diligencia', 'es');


-- /*  Altres tipus de documents */
--TD99
 INSERT INTO rwe_tipodocumental(id, codigonti, entidad) VALUES (1099, 'TD99', :entitatID);
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1099, 'Altres tipus de documents', 'ca');
INSERT INTO rwe_tra_tdocumental(idtdocumental, nombre, lang) VALUES (1099, 'Otros tipos de documentos', 'es')
