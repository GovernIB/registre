--Nuevo campo ENTIDAD en RWE_REGISTRO_ENTRADA
alter table RWE_REGISTRO_ENTRADA add ENTIDAD int8;
alter table RWE_REGISTRO_ENTRADA add constraint RWE_REGENT_ENTIDAD_FK foreign key (ENTIDAD) references RWE_ENTIDAD;

--Nuevo campo ENTIDAD en RWE_REGISTRO_SALIDA
alter table RWE_REGISTRO_SALIDA add ENTIDAD int8;
alter table RWE_REGISTRO_SALIDA add constraint RWE_REGSAL_ENTIDAD_FK foreign key (ENTIDAD) references RWE_ENTIDAD;

--Nuevo campo ENTIDAD en RWE_OFICIO_REMISION
alter table RWE_OFICIO_REMISION add ENTIDAD int8;
alter table RWE_OFICIO_REMISION add constraint RWE_OFIREM_ENTIDAD_FK foreign key (ENTIDAD) references RWE_ENTIDAD;

--Nuevo campo ENTIDAD en RWE_ANEXO
alter table RWE_ANEXO add ENTIDAD int8;
alter table RWE_ANEXO add constraint RWE_ANEXO_ENTIDAD_FK foreign key (ENTIDAD) references RWE_ENTIDAD;

--Nuevo campo ENTIDAD en RWE_ANEXO_SIR
alter table RWE_ANEXO_SIR add ENTIDAD int8;
alter table RWE_ANEXO_SIR add constraint RWE_ANEXOSIR_ENTIDAD_FK foreign key (ENTIDAD) references RWE_ENTIDAD;

--Nuevo campo usuario OAMR
ALTER TABLE RWE_USUARIO_ENTIDAD add OAMR boolean DEFAULT false not null;