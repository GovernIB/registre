update rwe_registro_detalle set RECIBIDO_SIR = 1 where id in (select re.registro_detalle from rwe_trazabilidad t, rwe_registro_entrada re where re.id=t.regent_destino and t.tipo=3 and t.registro_sir is not null);

