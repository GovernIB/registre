--Nuevos campos en RWE_ORGANISMO

alter table RWE_ORGANISMO add EDP number(1,0);
alter table RWE_ORGANISMO add EDPRINCIPAL number(19,0);

alter table RWE_ORGANISMO
        add constraint RWE_ORGANISMO_EDPRIN_FK
        foreign key (EDPRINCIPAL)
        references RWE_ORGANISMO;

create index RWE_ORGANI_EDP_FK_I on RWE_ORGANISMO (EDPRINCIPAL);
update RWE_ORGANISMO set EDP=0;

--Nueva tabla RWE_PROPIEDADGLOBAL

create table RWE_PROPIEDADGLOBAL (
        ID number(19,0) not null,
        CLAVE varchar2(255 char) not null,
        DESCRIPCION varchar2(255 char),
        ENTIDAD number(19,0),
        VALOR varchar2(255 char)
    );

alter table RWE_PROPIEDADGLOBAL add constraint RWE_PROPIEDADGLOBAL_pk primary key (ID);
create index RWE_PROPIE_ENTIDA_FK_I on RWE_PROPIEDADGLOBAL (ENTIDAD);
alter table RWE_PROPIEDADGLOBAL add constraint RWE_propiedad_clave_entidad_uk unique (CLAVE, ENTIDAD);
