--Nuevos campos en RWE_ORGANISMO

alter table RWE_ORGANISMO add EDP bool;
alter table RWE_ORGANISMO add EDPRINCIPAL int8;

alter table RWE_ORGANISMO
        add constraint RWE_ORGANISMO_EDPRIN_FK
        foreign key (EDPRINCIPAL)
        references RWE_ORGANISMO;

create index RWE_ORGANI_EDP_FK_I on RWE_ORGANISMO (EDPRINCIPAL);
update RWE_ORGANISMO set EDP=false;

--Nueva tabla RWE_PROPIEDADGLOBAL

create table RWE_PROPIEDADGLOBAL (
        ID int8 not null,
        CLAVE varchar(255) not null,
        DESCRIPCION varchar(255),
        ENTIDAD int8,
        VALOR varchar(255),
        primary key (ID),
        unique (CLAVE, ENTIDAD)
    );

create index RWE_PROPIE_ENTIDA_FK_I on RWE_PROPIEDADGLOBAL (ENTIDAD);

--Nuevos Índices en RWE_PERSONA
create index RWE_PERSONA_ENTIDAD_FK_I on RWE_PERSONA (ENTIDAD);

