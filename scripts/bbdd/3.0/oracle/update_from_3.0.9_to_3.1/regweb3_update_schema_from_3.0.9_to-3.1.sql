--Nueva Tabla RWE_INTEGRACION
create table RWE_INTEGRACION (
  ID number(19,0) not null,
  DESCRIPCION varchar2(400 char),
  ERROR clob,
  ESTADO number(19,0) not null,
  EXCEPCION clob,
  FECHA timestamp,
  NUMREGFORMAT varchar2(255 char),
  PETICION varchar2(2000 char),
  TIEMPO number(19,0) not null,
  TIPO number(19,0) not null,
  ENTIDAD number(19,0)
);

create index RWE_INT_ENTIDAD_FK_I on RWE_INTEGRACION (ENTIDAD);
alter table RWE_INTEGRACION add constraint RWE_INTEGRACION_pk primary key (ID);
alter table RWE_INTEGRACION add constraint RWE_INT_ENTIDAD_FK foreign key (ENTIDAD) references RWE_ENTIDAD;

--Nueva Tabla RWE_NOTIFICACION
create table RWE_NOTIFICACION (
        ID number(19,0) not null,
        ASUNTO varchar2(200 char),
        ESTADO number(19,0) not null,
        FECHA_ENVIADO timestamp,
        FECHA_LEIDO timestamp,
        MENSAJE varchar2(4000 char),
        TIPO number(19,0) not null,
        DESTINATARIO number(19,0) not null,
        REMITENTE number(19,0)
    );
create index RWE_NOTIF_DEST_FK_I on RWE_NOTIFICACION (DESTINATARIO);
create index RWE_NOTIF_REMIT_FK_I on RWE_NOTIFICACION (REMITENTE);
alter table RWE_NOTIFICACION add constraint RWE_NOTIFICACION_pk primary key (ID);
alter table RWE_NOTIFICACION add constraint RWE_NOTIF_DEST_FK foreign key (DESTINATARIO) references RWE_USUARIO_ENTIDAD;
alter table RWE_NOTIFICACION add constraint RWE_NOTIF_REMIT_FK foreign key (REMITENTE) references RWE_USUARIO_ENTIDAD;