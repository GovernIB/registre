-- Actualizar la entidad de cada RegistroEntrada
update rwe_registro_entrada set entidad = ? where usuario in (select id from rwe_usuario_entidad where entidad = ?);

-- Actualizar la entidad de cada RegistroSalida
update rwe_registro_salida set entidad = ? where usuario in (select id from rwe_usuario_entidad where entidad = ?);

-- Actualizar la entidad de cada OficioRemision
update rwe_oficio_remision set entidad = ? where usuario in (select id from rwe_usuario_entidad where entidad = ?);

-- Actualizar la entidad de cada Anexo
update rwe_anexo set entidad = ? where registrodetalle in (select registro_detalle from rwe_registro_entrada where usuario in (select id from rwe_usuario_entidad where entidad = ?));
update rwe_anexo set entidad = ? where registrodetalle in (select registro_detalle from rwe_registro_salida where usuario in (select id from rwe_usuario_entidad where entidad = ?));

-- Actualizar la entidad de cada AnexoSir
update rwe_anexo_sir set entidad=? where registro_sir in (select id from rwe_registro_sir where entidad = ?);