--Nuevo campo ENTIDAD en RWE_PENDIENTE
alter table RWE_PENDIENTE add ENTIDAD number(19,0);
alter table RWE_PENDIENTE add constraint RWE_PENDIE_ENTIDAD_FK foreign key (ENTIDAD) references RWE_ENTIDAD;
