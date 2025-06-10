-- Corregeix el valor del camp 'estado' de la taula RWE_REGISTRO_SIR
-- utilitzant com a referència l'estat correcte de l'ofici associat

-- Registres de sortida
update RWE_REGISTRO_SIR rrs set estado = 8 WHERE rrs.NUMERO_REGISTRO in -- ENVIADO_RECHAZADO
(
	select rrs.numregistro from RWE_OFIREM_REGSAl ror
	inner join RWE_REGISTRO_SALIDA rrs on rrs.id  = ror.idregsal
	where ror.idofirem in (
		select id from RWE_OFICIO_REMISION ror
		where ror.estado = 11 -- OFICIO_SIR_ENVIADO_RECHAZADO
	)
);

-- Registres d'entrada
update RWE_REGISTRO_SIR rrs set estado = 8 WHERE rrs.NUMERO_REGISTRO in -- ENVIADO_RECHAZADO
(
	select rre.numregistro from RWE_OFIREM_REGENT ror
	inner join RWE_REGISTRO_ENTRADA rre on rre.id  = ror.idregent
	where ror.idofirem in (
		select id from RWE_OFICIO_REMISION ror
		where ror.estado = 11 -- OFICIO_SIR_ENVIADO_RECHAZADO
	)
);


-- Corregeix el valor del camp 'estado' de la taula RWE_REGISTRO_SIR
-- utilitzant com a referència l'estat correcte de l'ofici associat

-- Registres d'entrada
update RWE_REGISTRO_SIR rrs set estado = 6 WHERE rrs.NUMERO_REGISTRO in -- ENVIADO_CONFIRMADO
(
	select rre.numregistro from RWE_OFIREM_REGENT ror
	inner join RWE_REGISTRO_ENTRADA rre on rre.id  = ror.idregent 
	where ror.idofirem in (
		select id from RWE_OFICIO_REMISION ror 
		where ror.estado = 9 -- OFICIO_SIR_ENVIADO_CONFIRMADO
	)
);

-- Registres de sortida
update RWE_REGISTRO_SIR rrs set estado = 6 WHERE rrs.NUMERO_REGISTRO in -- ENVIADO_CONFIRMADO
(
	select rrs.numregistro from RWE_OFIREM_REGSAL ror
	inner join RWE_REGISTRO_SALIDA rrs on rrs.id  = ror.idregsal
	where ror.idofirem in (
		select id from RWE_OFICIO_REMISION ror 
		where ror.estado = 9 -- OFICIO_SIR_ENVIADO_CONFIRMADO
	)
);
