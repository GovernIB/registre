
alter table bzhisemail alter column  bhe_hora type character varying(8);

update bzhisemail set bhe_hora = bhe_hora || ':00';


