/********** 30/9/2015 NUEVOS CAMPOS EN RWE_ENTIDAD **********/

alter table RWE_ENTIDAD add POSXSELLO number(19,0);
alter table RWE_ENTIDAD add POSYSELLO number(19,0);


/********** 30/9/2015 NUEVA TABLA RWE_CONFIGURACION **********/

create table RWE_CONFIGURACION (
		ID number(19,0) not null,
		COLORMENU varchar2(255 char),
		TEXTOPIE varchar2(4000 char),
		LOGOMENU number(19,0),
		LOGOPIE number(19,0)
	);
alter table RWE_CONFIGURACION add constraint RWE_CONFIGURACION_PK primary key (ID);
alter table RWE_CONFIGURACION add constraint RWE_CONFIGURACION_LOGOMENU_FK foreign key (LOGOMENU) references RWE_ARCHIVO (ID);
alter table RWE_CONFIGURACION add constraint RWE_CONFIGURACION_LOGOPIE_FK foreign key (LOGOPIE) references RWE_ARCHIVO (ID);

create index RWE_ENTLOMEN_ARCHIVO_FK_I on RWE_CONFIGURACION (LOGOMENU);
create index RWE_ENTLOPIE_ARCHIVO_FK_I on RWE_CONFIGURACION (LOGOPIE);


/********** 30/9/2015 MODIFICACIONES EN CAMPOS EN RWE_REGISTRO_MIGRADO **********/

alter table RWE_REGISTRO_MIGRADO MODIFY DENOFIFIS varchar2(60 char);
alter table RWE_REGISTRO_MIGRADO MODIFY DESORGDESEMI varchar2(60 char);
alter table RWE_REGISTRO_MIGRADO MODIFY DESREMDES varchar2(160 char);
alter table RWE_REGISTRO_MIGRADO MODIFY EXTRACTO varchar2(2000 char);
alter table RWE_REGISTRO_MIGRADO MODIFY DESCDOC varchar2(60 char);
alter table RWE_REGISTRO_MIGRADO MODIFY PRODESGEOFUE varchar2(50 char);
alter table RWE_REGISTRO_MIGRADO MODIFY PRODESGEO varchar2(50 char);


/********** 30/19/2015 NUEVA TABLA RWE_CATTIPOVIA **********/

create table RWE_CATTIPOVIA (
    ID number(19,0) not null,
    CODIGOTIPOVIA number(19,0) not null,
    DESCRIPCIONTIPOVIA varchar2(300 char) not null
);
alter table RWE_CATTIPOVIA add constraint RWE_CATTIPOVIA_pk primary key (ID);


/********** 30/19/2015 NUEVA TABLA RWE_CATSERVICIO **********/

create table RWE_CATSERVICIO (
    ID number(19,0) not null,
    CODIGOSERVICIO number(19,0) not null,
    DESCRIPCIONSERVICIO varchar2(300 char) not null
);
alter table RWE_CATSERVICIO add constraint RWE_CATSERVICIO_pk primary key (ID);


/********** 30/19/2015 NUEVOS CAMPOS EN RWE_OFICINA **********/

alter table RWE_OFICINA add PAIS number(19,0);
alter table RWE_OFICINA add COMUNIDAD number(19,0);
alter table RWE_OFICINA add LOCALIDAD number(19,0);
alter table RWE_OFICINA add TIPOVIA number(19,0);
alter table RWE_OFICINA add NOMBREVIA varchar2(300 char);
alter table RWE_OFICINA add NUMVIA varchar2(20 char);
alter table RWE_OFICINA add CODPOSTAL varchar2(140 char);

alter table RWE_OFICINA add constraint RWE_OFICINA_PAIS_FK foreign key (PAIS) references RWE_CATPAIS;
alter table RWE_OFICINA add constraint RWE_OFICINA_COMUNIDAD_FK foreign key (COMUNIDAD) references RWE_CATCOMUNIDADAUTONOMA;
alter table RWE_OFICINA add constraint RWE_OFICINA_LOCALIDAD_FK foreign key (LOCALIDAD) references RWE_CATLOCALIDAD;
alter table RWE_OFICINA add constraint RWE_OFICINA_TIPOVIA_FK foreign key (TIPOVIA) references RWE_CATTIPOVIA;

create index RWE_OFICIN_PAIS_FK_I on RWE_OFICINA (PAIS);
create index RWE_OFICIN_COMUNI_FK_I on RWE_OFICINA (COMUNIDAD);
create index RWE_OFICIN_TVIA_FK_I on RWE_OFICINA (TIPOVIA);
create index RWE_OFICIN_LOCALI_FK_I on RWE_OFICINA (LOCALIDAD);


/********** 30/19/2015 NUEVA TABLA RWE_OFICINA_SERVICIO **********/

create table RWE_OFICINA_SERVICIO (
    IDOFICINA number(19,0) not null,
    IDSERVICIO number(19,0) not null
);
alter table RWE_OFICINA_SERVICIO add constraint RWE_OFICINA_SERVICIO_pk primary key (IDOFICINA, IDSERVICIO);
alter table RWE_OFICINA_SERVICIO add constraint RWE_SERVICIO_OFICINA_FK foreign key (IDOFICINA) references RWE_OFICINA;
alter table RWE_OFICINA_SERVICIO add constraint RWE_OFICINA_SERVICIO_FK foreign key (IDSERVICIO) references RWE_CATSERVICIO;


/********** 30/19/2015 NUEVOS CAMPOS EN RWE_ORGANISMO **********/

alter table RWE_ORGANISMO add PAIS number(19,0);
alter table RWE_ORGANISMO add LOCALIDAD number(19,0);
alter table RWE_ORGANISMO add TIPOVIA number(19,0);
alter table RWE_ORGANISMO add NOMBREVIA varchar2(300 char);
alter table RWE_ORGANISMO add NUMVIA varchar2(20 char);
alter table RWE_ORGANISMO add CODPOSTAL varchar2(140 char);

alter table RWE_ORGANISMO add constraint RWE_ORGANISMO_PAIS_FK foreign key (PAIS) references RWE_CATPAIS;
alter table RWE_ORGANISMO add constraint RWE_ORGANISMO_LOCALIDAD_FK foreign key (LOCALIDAD) references RWE_CATLOCALIDAD;
alter table RWE_ORGANISMO add constraint RWE_ORGANISMO_TIPOVIA_FK foreign key (TIPOVIA) references RWE_CATTIPOVIA;

create index RWE_ORGANI_PAIS_FK_I on RWE_ORGANISMO (PAIS);
create index RWE_ORGANI_TVIA_FK_I on RWE_ORGANISMO (TIPOVIA);
create index RWE_ORGANI_LOCALI_FK_I on RWE_ORGANISMO (LOCALIDAD);
