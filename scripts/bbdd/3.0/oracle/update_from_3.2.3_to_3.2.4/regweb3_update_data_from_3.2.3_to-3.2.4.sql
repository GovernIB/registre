-- Marcar como recibido SIR los REGISTROS de ENTRADA
update rwe_registro_detalle set RECIBIDO_SIR = 1 where id in (select re.registro_detalle from rwe_trazabilidad t, rwe_registro_entrada re where re.id=t.regent_destino and t.tipo=3 and t.registro_sir is not null);

--Actualizar nombre Rol para realizar copias autenticas
update RWE_ROL set nombre='DIB_USER_RW' where nombre='DIB_USER';
