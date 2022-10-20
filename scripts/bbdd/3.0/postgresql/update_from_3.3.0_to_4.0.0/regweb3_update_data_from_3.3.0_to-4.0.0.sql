--Actualitzar a un valor por defecto los campos que son null
update RWE_INTERESADO SET RECEPNOTIF= 0 where RECEPNOTIF is null;
update RWE_INTERESADO SET AVISONOTIFEMAIL= 0 where AVISONOTIFEMAIL is null;
update RWE_INTERESADO SET AVISONOTIFSMS= 0 where AVISONOTIFSMS is null;


