--Nueva tabla RWE_PERMORGUSU
create table RWE_PERMORGUSU (
        ID number(19,0) not null,
        ACTIVO number(1,0) not null,
        PERMISO number(19,0),
        ORGANISMO number(19,0),
        USUARIO number(19,0)
);
alter table RWE_PERMORGUSU add constraint RWE_PERMORGUSU_pk primary key (ID);
alter table RWE_PERMORGUSU add constraint RWE_POU_USUENT_FK foreign key (USUARIO)references RWE_USUARIO_ENTIDAD;

alter table RWE_PERMORGUSU add constraint RWE_POU_ORG_FK foreign key (ORGANISMO) references RWE_ORGANISMO;
create index RWE_POU_ORG_FK_I on RWE_PERMORGUSU (ORGANISMO);
create index RWE_POU_USUARI_FK_I on RWE_PERMORGUSU (USUARIO);
create sequence RWE_POU_SEQ;

--Libro único
ALTER TABLE RWE_ENTIDAD ADD LIBRO number(19,0);
ALTER TABLE RWE_LIBRO ADD CONTADOR_SIR number(19,0);
ALTER TABLE RWE_ENTIDAD add constraint RWE_ENTIDAD_LIBRO_FK foreign key (LIBRO) references RWE_LIBRO;
ALTER TABLE RWE_LIBRO add constraint RWE_LIBRO_CONT_SIR_FK foreign key (CONTADOR_SIR) references RWE_CONTADOR;

--Nuevos campos
ALTER TABLE RWE_ORGANISMO ADD PERMITE_USUARIOS number(1,0) DEFAULT 0;
ALTER TABLE RWE_USUARIO add (DIB_USER number(10,0) DEFAULT 0 not null);
ALTER TABLE RWE_ANEXO add (SCAN number(10,0) DEFAULT 0 not null);
ALTER TABLE RWE_REGISTRO_DETALLE add (EXPEDIENTE_JUST varchar2(256 char));

--Modificar tamaño campos tipo RAW Tabla RWE_ANEXO
ALTER TABLE RWE_ANEXO MODIFY HASH raw(2000);
ALTER TABLE RWE_ANEXO MODIFY CERTIFICADO raw(2000);
ALTER TABLE RWE_ANEXO MODIFY VAL_OCSP_CERTIFICADO raw(2000);
ALTER TABLE RWE_ANEXO MODIFY TIMESTAMP raw(2000);

--RWE_ANEXO: Cambio de tipo de datos para el campo firma a CLOB
ALTER TABLE RWE_ANEXO RENAME COLUMN firma TO firma2;
ALTER TABLE RWE_ANEXO ADD (firma CLOB);
alter table RWE_ANEXO move lob (firma) store as RWE_ANEXO_FIRMA_lob (tablespace regweb_lob index RWE_ANEXO_FIRMA_lob_i);
UPDATE RWE_ANEXO SET firma=firma2;
ALTER TABLE RWE_ANEXO DROP COLUMN firma2;
ALTER INDEX RWE_ANEXO_PK REBUILD;