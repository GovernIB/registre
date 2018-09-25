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
) TABLESPACE REGWEB_DADES;
create index RWE_INT_ENTIDAD_FK_I on RWE_INTEGRACION (ENTIDAD) TABLESPACE REGWEB_INDEX;
alter table RWE_INTEGRACION add constraint RWE_INTEGRACION_pk primary key (ID);
alter table RWE_INTEGRACION add constraint RWE_INT_ENTIDAD_FK foreign key (ENTIDAD) references RWE_ENTIDAD;
grant select,insert,delete,update on RWE_INTEGRACION to www_regweb;
alter table RWE_INTEGRACION move lob (ERROR) store as RWE_INTEGR_ERROR_lob (tablespace regweb_lob index RWE_INTEGR_ERROR_lob_i);
alter table RWE_INTEGRACION move lob (EXCEPCION) store as RWE_INTEGR_EXCP_lob (tablespace regweb_lob index RWE_INTEGR_EXCP_lob_i);

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

create index RWE_NOTIF_DEST_FK_I on RWE_NOTIFICACION (DESTINATARIO) TABLESPACE REGWEB_INDEX;
create index RWE_NOTIF_REMIT_FK_I on RWE_NOTIFICACION (REMITENTE) TABLESPACE REGWEB_INDEX;
alter table RWE_NOTIFICACION add constraint RWE_NOTIFICACION_pk primary key (ID);
alter table RWE_NOTIFICACION add constraint RWE_NOTIF_DEST_FK foreign key (DESTINATARIO) references RWE_USUARIO_ENTIDAD;
alter table RWE_NOTIFICACION add constraint RWE_NOTIF_REMIT_FK foreign key (REMITENTE) references RWE_USUARIO_ENTIDAD;
grant select,insert,delete,update on RWE_NOTIFICACION to www_regweb;


-- Nueva Tabla RWE_COLA
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
    ) TABLESPACE REGWEB_DADES;

alter table RWE_COLA add constraint RWE_COLA_pk primary key (ID);
alter table RWE_COLA
        add constraint RWE_COLA_USUENTI_FK
        foreign key (USUARIOENTIDAD)
        references RWE_USUARIO_ENTIDAD;

grant select,insert,delete,update on RWE_COLA to www_regweb;
alter table RWE_COLA move lob (ERROR) store as RWE_COLA_ERROR_lob (tablespace regweb_lob index RWE_COLA_ERROR_lob_i);

--Nuevos campos Validacion firma Anexo
alter table rwe_anexo add FECHAVALIDACION timestamp;
alter table rwe_anexo add MOTIVONOVALID varchar2(2000 char);
alter table rwe_anexo add (ESTADOFIRMA number(19,0) DEFAULT 0);

--Nuevos campos eliminar anexos
alter table rwe_anexo add (PURGADO number(1,0) default 0);
alter table rwe_anexo_sir add (PURGADO number(1,0) default 0);

--Nueva Tabla RWE_MENSAJE_CONTROL
create table RWE_MENSAJE_CONTROL (
        ID number(19,0) not null,
        COD_ENT_REG_DEST varchar2(21 char) not null,
        COD_ENT_REG_ORI varchar2(21 char) not null,
        COD_ERROR varchar2(4 char),
        DESCRIPCION varchar2(1024 char),
        FECHA timestamp,
        FECHA_DESTINO timestamp,
        ID_INTERCAMBIO varchar2(33 char) not null,
        INDICADOR_PRUEBA number(10,0) not null,
        NUM_REG_DESTINO varchar2(20 char),
        TIPO_COMUNICACION number(19,0) not null,
        TIPO_MENSAJE varchar2(2 char) not null,
        ENTIDAD number(19,0) not null
    );
alter table RWE_MENSAJE_CONTROL add constraint RWE_MENSAJE_CONTROL_pk primary key (ID);
alter table RWE_MENSAJE_CONTROL
        add constraint RWE_MC_ENTIDAD_FK
        foreign key (ENTIDAD)
        references RWE_ENTIDAD;
create sequence RWE_SIR_SEQ;
grant select,insert,delete,update on RWE_MENSAJE_CONTROL to www_regweb;
