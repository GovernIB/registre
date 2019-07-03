-- Nuevos roles
INSERT INTO RWE_ROL (id,nombre,descripcion,orden) VALUES (4,'RWE_WS_ENTRADA','Usuario WS entrada',4);
INSERT INTO RWE_ROL (id,nombre,descripcion,orden) VALUES (5,'RWE_WS_SALIDA','Usuario WS salida',5);
INSERT INTO RWE_ROL (id,nombre,descripcion,orden) VALUES (6,'RWE_WS_CIUDADANO','Usuario WS ciudadano',6);

-- Ponemos todos los registros a presenciales
update RWE_REGISTRO_DETALLE set presencial = true;

-- Marcamos los registros que se han creado via WS como Telemáticos
update RWE_REGISTRO_DETALLE set presencial=false where id in(select re.registro_detalle from RWE_REGISTRO_ENTRADA re,rwe_usuario usu, RWE_USUARIO_ENTIDAD ue where usu.tipousuario=2 and usu.id=ue.USUARIO and ue.id=re.usuario);
update RWE_REGISTRO_DETALLE set presencial=false where id in(select rs.registro_detalle from RWE_REGISTRO_SALIDA rs,rwe_usuario usu, RWE_USUARIO_ENTIDAD ue where usu.tipousuario=2 and usu.id=ue.USUARIO and ue.id=rs.usuario);

-- Marcamos los registros recibidos via SIR como Telemáticos
update RWE_REGISTRO_DETALLE set presencial=false where id in(select re.REGISTRO_DETALLE from RWE_TRAZABILIDAD_SIR tsir, RWE_REGISTRO_ENTRADA re where tsir.TIPO=4 and tsir.REGISTRO_ENTRADA=re.ID);

-- Marcamos el evento a 0 de los registros que no son Válidos ni Pendientes de Visar
update RWE_REGISTRO_ENTRADA set evento=0 where evento is null and (estado != 1 and estado != 3);
update RWE_REGISTRO_SALIDA set evento=0 where evento is null and (estado != 1 and estado != 3);