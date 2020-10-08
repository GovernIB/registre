--Nueva tabla RWE_PERMORGUSU
create table RWE_PERMORGUSU (
        ID int8 not null,
        ACTIVO bool not null,
        PERMISO int8,
        ORGANISMO int8,
        USUARIO int8
);
alter table RWE_PERMORGUSU add constraint RWE_PERMORGUSU_pk primary key (ID);
alter table RWE_PERMORGUSU add constraint RWE_POU_USUENT_FK foreign key (USUARIO)references RWE_USUARIO_ENTIDAD;

alter table RWE_PERMORGUSU add constraint RWE_POU_ORG_FK foreign key (ORGANISMO) references RWE_ORGANISMO;
create index RWE_POU_ORG_FK_I on RWE_PERMORGUSU (ORGANISMO);
create index RWE_POU_USUARI_FK_I on RWE_PERMORGUSU (USUARIO);
create sequence RWE_POU_SEQ;

--Libro único
ALTER TABLE RWE_ENTIDAD ADD LIBRO int8;
ALTER TABLE RWE_LIBRO ADD CONTADOR_SIR int8;
ALTER TABLE RWE_ENTIDAD add constraint RWE_ENTIDAD_LIBRO_FK foreign key (LIBRO) references RWE_LIBRO;
ALTER TABLE RWE_LIBRO add constraint RWE_LIBRO_CONT_SIR_FK foreign key (CONTADOR_SIR) references RWE_CONTADOR;

--Nuevos campos
ALTER TABLE RWE_ORGANISMO ADD PERMITE_USUARIOS bool DEFAULT false;
ALTER TABLE RWE_USUARIO add DIB_USER bool DEFAULT false not null;
ALTER TABLE RWE_ANEXO add SCAN bool DEFAULT false not null;