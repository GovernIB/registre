--Nueva Tabla RWE_SESION
create table RWE_SESION (
        ID number(19,0) not null,
        ESTADO number(19,0),
        FECHA timestamp,
        IDSESION number(19,0) not null,
        NUMREG varchar2(255 char),
        TIPO_REGISTRO number(19,0),
        USUARIO number(19,0) not null
    );

create sequence RWE_SESION_SEQ;
grant select on RWE_SESION_SEQ to www_regweb;
create index RWE_SESION_USUENT_FK_I on RWE_SESION (USUARIO);
alter table RWE_SESION add constraint RWE_SESION_pk primary key (ID);
alter table RWE_SESION add constraint RWE_SESION_USUENT_FK foreign key (USUARIO) references RWE_USUARIO_ENTIDAD;