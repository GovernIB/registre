--Nueva Tabla RWE_INTEGRACION
create table RWE_INTEGRACION (
        ID number(19,0) not null,
        DESCRIPCION varchar2(400 char),
        ERROR varchar2(2000 char),
        ESTADO number(19,0) not null,
        EXCEPCION varchar2(4000 char),
        FECHA timestamp,
        PETICION varchar2(2000 char),
        TIEMPO number(19,0) not null,
        TIPO number(19,0) not null,
        ENTIDAD number(19,0)
    );
create index RWE_INT_ENTIDAD_FK_I on RWE_INTEGRACION (ENTIDAD) TABLESPACE REGWEB_INDEX;
alter table RWE_INTEGRACION add constraint RWE_INTEGRACION_pk primary key (ID);
alter table RWE_INTEGRACION add constraint RWE_INT_ENTIDAD_FK foreign key (ENTIDAD) references RWE_ENTIDAD;
grant select,insert,delete,update on RWE_INTEGRACION to www_regweb;