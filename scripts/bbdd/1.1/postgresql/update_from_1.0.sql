
create table BZDOCLOC (
	LOC_ANY int4 not null,
	LOC_NUMDOC int4 not null,
	LOC_OFI int4 not null,
	LOC_NUMREG int4 not null,
	LOC_TIPUS varchar(1) not null,
	LOC_DOC  varchar(100) not null,
	primary key (LOC_ANY, LOC_NUMDOC, LOC_OFI, LOC_NUMREG, LOC_TIPUS)
);

comment on table BZDOCLOC is 'DocumentoCareo';

alter table BZDOCLOC add constraint LOC_TIPUS_CK check (LOC_TIPUS IN ('E','S'));

-------------------

ALTER TABLE BZENTRA ADD EMAILREMITENT CHAR(50);
ALTER TABLE BZENTRA ADD ORIGENREGISTRO CHAR(10);


create table BZHISEMAIL (
	BHE_ANY int4 not null,
	BHE_TIPUS varchar(1) not null,
	BHE_NUM int4 not null,
	BHE_NUMREG int4 not null,
	BHE_CODIOFI int4 not null,
	BHE_CODUSU varchar(10) not null,
	BHE_EMAIL varchar(50) not null,
	BHE_DATA varchar(10) not null,
	BHE_HORA varchar(5) not null,
	BHE_TIPUSMAIL varchar(1) not null,
	primary key (BHE_ANY, BHE_TIPUS, BHE_NUM, BHE_NUMREG, BHE_CODIOFI)
);

comment on table BZHISEMAIL is
	'Modificacion';

alter table BZHISEMAIL add constraint BHE_TIPUS_CK check ( BHE_TIPUS IN ('E','S'));
alter table BZHISEMAIL add constraint BHE_TIPUSMAIL_CK check ( BHE_TIPUSMAIL IN ('I','C'));



create table BZMODEMAIL (
	BME_IDIOMA  varchar(2) not null,
	BME_TIPO  varchar(2) not null,
	BME_CUERPO varchar(1000) not null,
	BME_TITULO varchar(100) not null,
	primary key (BME_IDIOMA , BME_TIPO )
);

comment on table BZMODEMAIL is 'ModeloEmail';


create table BZOFOR (
	OFO_CODIOFI int4 not null,
	OFO_CODIORG int4 not null,
	primary key (OFO_CODIOFI, OFO_CODIORG)
);

comment on table BZOFOR is'OficinaOrganismoPermetEnviarEmail';

ALTER TABLE BZSALIDA ADD EMAILREMITENT VARCHAR(50) not null;

create table BZUNIGES (
	UNI_CODIOFI int4 not null,
	UNI_CODI  int4 not null,
	UNI_BAJA  varchar(1) not null,
	UNI_EMAIL varchar(50) not null,
	UNI_NOM varchar(20) not null,
	primary key (UNI_CODIOFI, UNI_CODI )
);

comment on table BZUNIGES is 'Unidad de Gestion';
