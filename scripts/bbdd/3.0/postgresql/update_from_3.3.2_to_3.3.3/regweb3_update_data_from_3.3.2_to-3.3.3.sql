-- Indicar la Entidad de cada Oficina
update rwe_oficina set entidad = ? where organismoresponsable in (select id from rwe_organismo where entidad = ?);