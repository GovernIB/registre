
update RWE_REGISTRO_DETALLE set presencial = true; --Ponemos todas presenciales
update RWE_REGISTRO_DETALLE set presencial = false where id_intercambio is not null; --Marcamos a 0 las que han venido via sir

-- Marcamos las que han venido via SISTRA (ENTRADA)
update RWE_REGISTRO_DETALLE set presencial = false where id in(select registro_detalle from RWE_REGISTRO_ENTRADA  where oficina = '<<codigo de la oficina virtual>>');
-- Marcamos las que han venido via SISTRA (SALIDA)
update RWE_REGISTRO_DETALLE set presencial = false where id in(select registro_detalle from RWE_REGISTRO_SALIDA  where oficina = '<<codigo de la oficina virtual>>');