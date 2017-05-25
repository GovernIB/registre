/********** 30/9/2015 NUEVOS CAMPOS EN RWE_ENTIDAD **********/

alter table RWE_ENTIDAD add POSXSELLO int2;
alter table RWE_ENTIDAD add POSYSELLO int2;


/********** 30/9/2015 NUEVA TABLA RWE_CONFIGURACION **********/

create table RWE_CONFIGURACION (
		ID int8 not null,
		COLORMENU varchar(255),
		TEXTOPIE varchar(4000),
		LOGOMENU int8,
		LOGOPIE int8,
		primary key (ID)
);

create index RWE_ENTLOMEN_ARCHIVO_FK_I on RWE_CONFIGURACION (LOGOMENU);
create index RWE_ENTLOPIE_ARCHIVO_FK_I on RWE_CONFIGURACION (LOGOPIE);

alter table RWE_CONFIGURACION add constraint RWE_CONFIGURACION_LOGOMENU_FK foreign key (LOGOMENU) references RWE_ARCHIVO (ID);
alter table RWE_CONFIGURACION add constraint RWE_CONFIGURACION_LOGOPIE_FK foreign key (LOGOPIE) references RWE_ARCHIVO (ID);


/********** 30/9/2015 MODIFICACIONES EN CAMPOS EN RWE_REGISTRO_MIGRADO **********/

alter table RWE_REGISTRO_MIGRADO alter DENOFIFIS type varchar(60);
alter table RWE_REGISTRO_MIGRADO alter DESORGDESEMI type varchar(60);
alter table RWE_REGISTRO_MIGRADO alter DESREMDES type varchar(160);
alter table RWE_REGISTRO_MIGRADO alter EXTRACTO type varchar(2000);
alter table RWE_REGISTRO_MIGRADO alter DESCDOC type varchar(60);
alter table RWE_REGISTRO_MIGRADO alter PRODESGEOFUE type varchar(50);
alter table RWE_REGISTRO_MIGRADO alter PRODESGEO type varchar(50);


/********** 30/9/2015 NUEVA TABLA RWE_CATTIPOVIA **********/

create table RWE_CATTIPOVIA (
    ID int8 not null,
    CODIGOTIPOVIA int8 not null,
    DESCRIPCIONTIPOVIA varchar(300) not null,
    primary key (ID)
);


/********** 30/19/2015 NUEVA TABLA RWE_CATSERVICIO **********/

create table RWE_CATSERVICIO (
    ID int8 not null,
    CODIGOSERVICIO int8 not null,
    DESCRIPCIONSERVICIO varchar(300) not null,
    primary key (ID)
);


/********** 30/9/2015 NUEVOS CAMPOS EN RWE_OFICINA **********/

alter table RWE_OFICINA add PAIS int8;
alter table RWE_OFICINA add COMUNIDAD int8;
alter table RWE_OFICINA add LOCALIDAD int8;
alter table RWE_OFICINA add TIPOVIA int8;
alter table RWE_OFICINA add NOMBREVIA varchar(300);
alter table RWE_OFICINA add NUMVIA varchar(20);
alter table RWE_OFICINA add CODPOSTAL varchar(140);

alter table RWE_OFICINA add constraint RWE_OFICINA_PAIS_FK foreign key (PAIS) references RWE_CATPAIS;
alter table RWE_OFICINA add constraint RWE_OFICINA_COMUNIDAD_FK foreign key (COMUNIDAD) references RWE_CATCOMUNIDADAUTONOMA;
alter table RWE_OFICINA add constraint RWE_OFICINA_LOCALIDAD_FK foreign key (LOCALIDAD) references RWE_CATLOCALIDAD;
alter table RWE_OFICINA add constraint RWE_OFICINA_TIPOVIA_FK foreign key (TIPOVIA) references RWE_CATTIPOVIA;

create index RWE_OFICIN_PAIS_FK_I on RWE_OFICINA (PAIS);
create index RWE_OFICIN_COMUNI_FK_I on RWE_OFICINA (COMUNIDAD);
create index RWE_OFICIN_TVIA_FK_I on RWE_OFICINA (TIPOVIA);
create index RWE_OFICIN_LOCALI_FK_I on RWE_OFICINA (LOCALIDAD);


/********** 30/9/2015 NUEVA TABLA RWE_OFICINA_SERVICIO **********/

create table RWE_OFICINA_SERVICIO (
    IDOFICINA int8 not null,
    IDSERVICIO int8 not null,
    primary key (IDOFICINA, IDSERVICIO)
);

alter table RWE_OFICINA_SERVICIO add constraint RWE_SERVICIO_OFICINA_FK foreign key (IDOFICINA) references RWE_OFICINA;
alter table RWE_OFICINA_SERVICIO add constraint RWE_OFICINA_SERVICIO_FK foreign key (IDSERVICIO) references RWE_CATSERVICIO;


/********** 30/9/2015 NUEVOS CAMPOS EN RWE_ORGANISMO **********/

alter table RWE_ORGANISMO add PAIS int8;
alter table RWE_ORGANISMO add LOCALIDAD int8;
alter table RWE_ORGANISMO add TIPOVIA int8;
alter table RWE_ORGANISMO add NOMBREVIA varchar(300);
alter table RWE_ORGANISMO add NUMVIA varchar(20);
alter table RWE_ORGANISMO add CODPOSTAL varchar(140);

alter table RWE_ORGANISMO add constraint RWE_ORGANISMO_PAIS_FK foreign key (PAIS) references RWE_CATPAIS;
alter table RWE_ORGANISMO add constraint RWE_ORGANISMO_LOCALIDAD_FK foreign key (LOCALIDAD) references RWE_CATLOCALIDAD;
alter table RWE_ORGANISMO add constraint RWE_ORGANISMO_TIPOVIA_FK foreign key (TIPOVIA) references RWE_CATTIPOVIA;

create index RWE_ORGANI_PAIS_FK_I on RWE_ORGANISMO (PAIS);
create index RWE_ORGANI_TVIA_FK_I on RWE_ORGANISMO (TIPOVIA);
create index RWE_ORGANI_LOCALI_FK_I on RWE_ORGANISMO (LOCALIDAD);

