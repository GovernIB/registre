--Eliminar campo RWE_USUARIO_ENTIDAD OAMR
ALTER TABLE RWE_USUARIO_ENTIDAD drop column OAMR;

--Nuevos campos RWE_USUARIO_ENTIDAD
ALTER TABLE RWE_USUARIO_ENTIDAD add CATEGORIA number(19,0);
ALTER TABLE RWE_USUARIO_ENTIDAD add FUNCION number(19,0);
ALTER TABLE RWE_USUARIO_ENTIDAD add TELEFONO varchar2(255 char);
ALTER TABLE RWE_USUARIO_ENTIDAD add CAI number(10,0);
ALTER TABLE RWE_USUARIO_ENTIDAD add NOMBRETRABAJO varchar2(255 char);
ALTER TABLE RWE_USUARIO_ENTIDAD add CODIGOTRABAJO varchar2(255 char);
ALTER TABLE RWE_USUARIO_ENTIDAD add OBSERVACIONES varchar2(255 char);
ALTER TABLE RWE_USUARIO_ENTIDAD add (clave number(1,0) DEFAULT 0 not null);
ALTER TABLE RWE_USUARIO_ENTIDAD add (bitcita number(1,0) DEFAULT 0 not null);
ALTER TABLE RWE_USUARIO_ENTIDAD add (asistencia number(1,0) DEFAULT 0 not null);
ALTER TABLE RWE_USUARIO_ENTIDAD add (apodera number(1,0) DEFAULT 0 not null);
ALTER TABLE RWE_USUARIO_ENTIDAD add (notificacion number(1,0) DEFAULT 0 not null);
ALTER TABLE RWE_USUARIO_ENTIDAD ADD FECHAALTA timestamp;

--Nueva tabla RWE_CATISLA
CREATE TABLE RWE_CATISLA (
                 ID number(19,0) not null,
                 CODIGOISLA number(19,0) not null,
                 DESCRIPCIONISLA varchar2(50 char) not null,
                 PROVINCIA number(19,0)
);
ALTER TABLE RWE_CATISLA add constraint RWE_CATISLA_pk primary key (ID);
ALTER TABLE RWE_CATISLA add constraint RWE_CATISLA_CATPROVIN_FK foreign key (PROVINCIA) references RWE_CATPROVINCIA;
CREATE INDEX RWE_CATISL_CATPRO_FK_I on RWE_CATISLA (PROVINCIA) TABLESPACE REGWEB_INDEX;

--Nuevos campos RWE_OFICINA
ALTER TABLE RWE_OFICINA add (OAMR number(1,0) DEFAULT 0 not null);
ALTER TABLE RWE_OFICINA add ISLA number(19,0);
ALTER TABLE RWE_OFICINA add constraint RWE_OFICINA_ISLA_FK foreign key (ISLA) references RWE_CATISLA;
CREATE INDEX RWE_OFICIN_ISLA_FK_I on RWE_OFICINA (ISLA) TABLESPACE REGWEB_INDEX;

--Nuevo campo RWE_ORGANISMO
ALTER TABLE RWE_ORGANISMO add ISLA number(19,0);
ALTER TABLE RWE_ORGANISMO add constraint RWE_ORGANISMO_ISLA_FK foreign key (ISLA) references RWE_CATISLA;
CREATE INDEX RWE_ORGANI_ISLA_FK_I on RWE_ORGANISMO (ISLA) TABLESPACE REGWEB_INDEX;

-- Índice RWE_ANEXO
CREATE INDEX RWE_ANEXO_CUSTID_FK_I on RWE_ANEXO (CUSTODIAID) TABLESPACE REGWEB_INDEX;

grant select, insert, delete, update on RWE_CATISLA to www_regweb;

--Nuevo campo RWE_ENTIDAD
ALTER TABLE RWE_ENTIDAD add (REG_SALIDAS_PERSONAS number(1,0) DEFAULT 1 not null);
--Eliminar campo RWE_ENTIDAD Oficio Remisión
ALTER TABLE RWE_ENTIDAD drop column OFICIOREMISION;


