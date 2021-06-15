--Actualizar el estado a 'VÁLIDO', de las Salidas generadas por Notib.
UPDATE RWE_REGISTRO_SALIDA
SET ESTADO=1
WHERE ESTADO=7
  AND REGISTRO_DETALLE IN (select rd.id
                           from RWE_REGISTRO_DETALLE rd
                           where upper(rd.observaciones) LIKE upper('%notib%'));