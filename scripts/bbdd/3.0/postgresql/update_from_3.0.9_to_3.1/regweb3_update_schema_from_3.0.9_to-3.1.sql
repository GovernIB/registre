--Nueva Tabla RWE_INTEGRACION
create table RWE_INTEGRACION (
        ID int8 not null,
        DESCRIPCION varchar(400),
        ERROR varchar(2000),
        ESTADO int8 not null,
        EXCEPCION varchar(4000),
        FECHA timestamp,
        PETICION varchar(2000),
        TIEMPO int8 not null,
        TIPO int8 not null,
        ENTIDAD int8,
        primary key (ID)
    );
create index RWE_INT_ENTIDAD_FK_I on RWE_INTEGRACION (ENTIDAD);
alter table RWE_INTEGRACION add constraint RWE_INT_ENTIDAD_FK foreign key (ENTIDAD) references RWE_ENTIDAD;