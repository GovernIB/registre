--Actializar campo custodiado
update RWE_ANEXO set custodiado=1 where justificante=1 and csv IS NOT NULL;
update RWE_ANEXO set custodiado=0 where justificante=1 and csv IS NULL;