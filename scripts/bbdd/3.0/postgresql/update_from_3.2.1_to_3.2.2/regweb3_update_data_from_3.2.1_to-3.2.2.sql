--Actializar campo custodiado
update RWE_ANEXO set custodiado=true where justificante=true and csv IS NOT NULL;
update RWE_ANEXO set custodiado=false where justificante=true and csv IS NULL;