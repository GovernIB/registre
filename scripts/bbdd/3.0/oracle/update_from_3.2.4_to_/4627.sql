INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.notib.codigo.emisor.forzado','Código emisor con que se realizan las notificaciones desde Notib (si no se informa, se cogerá el de la entidad)',null,1,'EA0001301');
INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.notib.sir.callback.url','Url callback de Notib (canvi estat comunicacions SIR)',null,1,'https://notib-proves.portsdebalears.com/notibapi/interna/sir/adviser/sincronitzar');
INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.cron.adviser.comunicacion.estado.envios.sir.periodo','Periodo para activar adviser comunicación cambio estado de los envíos sir pendientes',null,1,'14400000');
INSERT INTO RWE_PROPIEDADGLOBAL (ID, CLAVE, DESCRIPCION, ENTIDAD, TIPO, VALOR) VALUES (RWE_ALL_SEQ.nextVal,'es.caib.regweb3.cron.adviser.comunicacion.estado.envios.sir.retardo','Retardo entre cada adviser comunicación cambio estado de los envíos sir pendientes',null,1,'14400000');

ALTER TABLE RWE_REGISTRO_SIR ADD CAMBIO_ESTADO_COMUNICADO NUMBER(1,0) DEFAULT 0;
UPDATE RWE_REGISTRO_SIR SET CAMBIO_ESTADO_COMUNICADO = 1; -- Donam per fet que el canvi d’estat de les comunicacions antigues ja s’ha comunicat. Si en queda cap pendent, s’haurà de consultar manualment des de Notib

COMMIT;