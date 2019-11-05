--Nuevo Table RWE_SESION
create table RWE_SESION (
        ID int8 not null,
        ESTADO int8,
        FECHA timestamp,
        IDSESION int8 not null,
        NUMREG varchar(255),
        TIPO_REGISTRO int8,
        USUARIO int8 not null,
        primary key (ID)
    );

create index RWE_SESION_USUENT_FK_I on RWE_SESION (USUARIO);

alter table RWE_SESION
    add constraint RWE_SESION_USUENT_FK
    foreign key (USUARIO)
    references RWE_USUARIO_ENTIDAD;

create sequence RWE_SESION_SEQ;