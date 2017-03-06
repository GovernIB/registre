--Eliminar campo de la tabla RWE_ASIENTO_REGISTRAL_SIR
ALTER TABLE RWE_ASIENTO_REGISTRAL_SIR DROP COLUMN COD_ENT_REG;

--Actualizar el nombre de la aplicación para adaptarlo a SICRES3
update RWE_REGISTRO_DETALLE set APLICACION='RWE3';
