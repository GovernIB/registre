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


--Nueva Tabla COLA
create table RWE_COLA (
        ID number(19,0) not null,
        DENOMINACIONOFICINA varchar2(255 char),
        DESCRIPCIONOBJETO varchar2(255 char),
        ERROR clob,
        ESTADO number(19,0),
        FECHA timestamp,
        IDOBJETO number(19,0),
        NUMMAXREINTENTOS number(10,0),
        NUMREINTENTOS number(10,0),
        TIPO number(19,0),
        USUARIOENTIDAD number(19,0) not null
    );

alter table RWE_COLA add constraint RWE_COLA_pk primary key (ID);
alter table RWE_COLA
        add constraint RWE_COLA_USUENTI_FK
        foreign key (USUARIOENTIDAD)
        references RWE_USUARIO_ENTIDAD;


--Nuevos campos Validacion firma Anexo
alter table rwe_anexo add FECHAVALIDACION timestamp;
alter table rwe_anexo add MOTIVONOVALID varchar2(2000 char);
alter table rwe_anexo add (ESTADOFIRMA number(19,0) DEFAULT 0);

--Nuevos campos eliminar anexos
alter table rwe_anexo add PURGADO number(1,0) default false;
alter table rwe_anexo_sir add (PURGADO number(1,0) default 0);