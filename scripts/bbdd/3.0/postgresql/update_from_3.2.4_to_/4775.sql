-- Registres de sortida
-- Corregeix el valor del camp 'estado' de la taula RWE_REGISTRO_SIR
-- utilitzant com a referència l'estat correcte de l'ofici associat
update RWE_REGISTRO_SIR rrs set estado = 8 WHERE rrs.NUMERO_REGISTRO in -- ENVIADO_RECHAZADO
(
	select rrs.numregistro from RWE_OFIREM_REGSAl ror
	inner join RWE_REGISTRO_SALIDA rrs on rrs.id  = ror.idregsal
	where ror.idofirem in (
		select id from rwe_oficio_remision ror 
		where ror.estado = 11 -- OFICIO_SIR_ENVIADO_RECHAZADO
	)
)

-- Registres d'entrada
-- Corregeix el valor del camp 'estado' de la taula RWE_REGISTRO_SIR
-- utilitzant com a referència l'estat correcte de l'ofici associat
update RWE_REGISTRO_SIR rrs set estado = 6 WHERE rrs.NUMERO_REGISTRO in -- ENVIADO_CONFIRMADO
(
	select rre.numregistro from rwe_ofirem_regent ror
	inner join rwe_registro_entrada rre on rre.id  = ror.idregent 
	where ror.idofirem in (
		select id from rwe_oficio_remision ror 
		where ror.estado = 9 -- OFICIO_SIR_ENVIADO_CONFIRMADO
	)
)