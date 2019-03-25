//TODO falta probar
update RWE_REGISTRO_DETALLE set presencial = 1; --Ponemos todas presenciales
update RWE_REGISTRO_DETALLE set presencial = 0 where id_intercambio is not null; --Marcamos a 0 las que han venido via sir

-- Marcamos las que han venido via SISTRA (ENTRADA)
update RWE_REGISTRO_DETALLE set presencial = 0 where id in(select registro_detalle from RWE_REGISTRO_ENTRADA  where oficina = '<<codigo de la oficina virtual>>');
-- Marcamos las que han venido via SISTRA (SALIDA)
update RWE_REGISTRO_DETALLE set presencial = 0 where id in(select registro_detalle from RWE_REGISTRO_SALIDA  where oficina = '<<codigo de la oficina virtual>>');
