create table RWE_INTEGRACION (
        ID int8 not null,
        DESCRIPCION varchar(400),
        ERROR text,
        ESTADO int8 not null,
        EXCEPCION text,
        FECHA timestamp,
        NUMREGFORMAT varchar(255),
        PETICION varchar(2000),
        TIEMPO int8 not null,
        TIPO int8 not null,
        ENTIDAD int8,
        primary key (ID)
    );
create index RWE_INT_ENTIDAD_FK_I on RWE_INTEGRACION (ENTIDAD);
alter table RWE_INTEGRACION add constraint RWE_INT_ENTIDAD_FK foreign key (ENTIDAD) references RWE_ENTIDAD;

--Nueva Tabla RWE_NOTIFICACION
create table RWE_NOTIFICACION (
        ID int8 not null,
        ASUNTO varchar(200),
        ESTADO int8 not null,
        FECHA_ENVIADO timestamp,
        FECHA_LEIDO timestamp,
        MENSAJE varchar(4000),
        TIPO int8 not null,
        DESTINATARIO int8 not null,
        REMITENTE int8,
        primary key (ID)
    );
create index RWE_NOTIF_DEST_FK_I on RWE_NOTIFICACION (DESTINATARIO);
create index RWE_NOTIF_REMIT_FK_I on RWE_NOTIFICACION (REMITENTE);
alter table RWE_NOTIFICACION add constraint RWE_NOTIF_DEST_FK foreign key (DESTINATARIO) references RWE_USUARIO_ENTIDAD;
alter table RWE_NOTIFICACION add constraint RWE_NOTIF_REMIT_FK foreign key (REMITENTE) references RWE_USUARIO_ENTIDAD;

--Nueva Tabla RWE_COLA
 create table RWE_COLA (
        ID int8 not null,
        DENOMINACIONOFICINA varchar(255),
        DESCRIPCIONOBJETO varchar(255),
        ERROR text,
        ESTADO int8,
        FECHA timestamp,
        IDOBJETO int8,
        NUMMAXREINTENTOS int4,
        NUMREINTENTOS int4,
        TIPO int8,
        USUARIOENTIDAD int8 not null,
        primary key (ID)
    );

alter table RWE_COLA
        add constraint RWE_COLA_USUENTI_FK
        foreign key (USUARIOENTIDAD)
        references RWE_USUARIO_ENTIDAD;

--Nuevos campos Validacion firma Anexo
alter table rwe_anexo add FECHAVALIDACION timestamp;
alter table rwe_anexo add MOTIVONOVALID varchar(2000);
alter table rwe_anexo add ESTADOFIRMA int8  DEFAULT 0;

--Nuevos campos eliminar anexos distribuidos
alter table rwe_anexo add PURGADO bool default false;
alter table rwe_anexo_sir add PURGADO bool default false;