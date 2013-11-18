
update bzentra set fzahora = fzahora * 100;
update BZSALIDA set FZSHORA = FZSHORA * 100;

alter table bzhisemail alter column  bhe_hora type character varying(8);

update bzhisemail set bhe_hora = bhe_hora || ':00';

