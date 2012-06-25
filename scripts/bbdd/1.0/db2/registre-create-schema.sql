
    create table BAGECOM (
        FAACAGCO integer not null,
        FAAFBAJA integer,
        FAADAGCO varchar(255),
        primary key (FAACAGCO)
    );

    comment on table BAGECOM is
        'Oficina';

    create table BAGRUGE (
        FABCAGGE integer not null,
        FABCTAGG integer not null,
        FABFBAJA integer not null,
        FABDAGGE varchar(30) not null,
        FABCAGSU integer,
        FABCTASU integer,
        primary key (FABCAGGE, FABCTAGG)
    );

    comment on table BAGRUGE is
        'AgrupacionGeografica';

    create table BCODPOS (
        F12CPOST integer not null,
        F12CAGGE integer not null,
        F12CTAGG integer not null,
        primary key (F12CAGGE, F12CTAGG)
    );

    comment on table BCODPOS is
        'CodigoPostal';

    create table BHAGECO (
        FHACAGCO integer not null,
        FHAFALTA integer not null,
        FHAFBAJA integer not null,
        FHADAGCO varchar(20) not null,
        primary key (FHACAGCO, FHAFALTA)
    );

    comment on table BHAGECO is
        'OficinaHistorico';

    create table BHOFIFIS (
        FZHCAGCO integer not null,
        OFH_CODI integer not null,
        OFH_FECALT integer not null,
        OFH_FECBAJ integer not null,
        OFH_NOM varchar(20) not null,
        primary key (FZHCAGCO, OFH_CODI, OFH_FECALT)
    );

    comment on table BHOFIFIS is
        'OficinaFisicaHistorico';

    create table BHORGAN (
        FHXCORGA integer not null,
        FHXFALTA integer not null,
        FHXFBAJA integer not null,
        FHXDORGR varchar(15) not null,
        FHXDORGT varchar(40) not null,
        primary key (FHXCORGA, FHXFALTA)
    );

    comment on table BHORGAN is
        'OrganismoHistorico';

    create table BORGANI (
        FAXCORGA integer not null,
        FAXCOAGR varchar(1) not null,
        FAXFBAJA integer not null,
        FAXDORGR varchar(15) not null,
        FAXDORGT varchar(40) not null,
        primary key (FAXCORGA)
    );

    comment on table BORGANI is
        'Organismo';

    create table BTIPAGR (
        FLDFBAJA integer not null,
        FLDCTAGG integer not null,
        FLDDTAGG varchar(30) not null,
        FLDCTAGS integer not null,
        primary key (FLDFBAJA, FLDCTAGG)
    );

    comment on table BTIPAGR is
        'TipoAgrupacionGeografica';

    create table BZAUTOR (
        FZHCAUT varchar(2) not null,
        FZHCAGCO integer not null,
        FZHCUSU varchar(10) not null,
        primary key (FZHCAUT, FZHCAGCO, FZHCUSU)
    );

    comment on table BZAUTOR is
        'Autorizacion';

    create table BZBLOQU (
        FZNAENSA integer not null,
        FZNCENSA varchar(1) not null,
        FZNCAGCO integer not null,
        FZNCUSU varchar(10) not null,
        primary key (FZNAENSA, FZNCENSA, FZNCAGCO)
    );

    comment on table BZBLOQU is
        'BloqueoDisquete';

    create table BZCONES (
        FZDAENSA integer not null,
        FZDCENSA varchar(1) not null,
        FZDCAGCO integer not null,
        FZDNUMER integer not null,
        primary key (FZDAENSA, FZDCENSA, FZDCAGCO)
    );

    comment on table BZCONES is
        'Contador';

    create table BZDISQU (
        FZLAENSA integer not null,
        FZLCENSA varchar(1) not null,
        FZLCAGCO integer not null,
        FZLNDIS integer not null,
        primary key (FZLAENSA, FZLCENSA, FZLCAGCO)
    );

    comment on table BZDISQU is
        'Disquete';

    create table BZENLPD (
        FZTANOEN integer not null,
        FZTDATAC integer not null,
        FZTHORAC integer not null,
        FZTNUMEN integer not null,
        FZTCAGCO integer not null,
        FZTTIPAC varchar(10) not null,
        FZTCUSU varchar(10) not null,
        primary key (FZTANOEN, FZTDATAC, FZTHORAC, FZTNUMEN, FZTCAGCO, FZTTIPAC, FZTCUSU)
    );

    comment on table BZENLPD is
        'LogEntradaLopd';

    create table BZENTID (
        FZGCENTI varchar(7) not null,
        FZGFBAJA integer not null,
        FZGNENTI integer not null,
        FZGCENT2 varchar(7) not null,
        FZGDENTI varchar(30) not null,
        FZGDENT2 varchar(30) not null,
        primary key (FZGCENTI, FZGFBAJA, FZGNENTI)
    );

    comment on table BZENTID is
        'EntidadRemitente';

    create table BZENTOFF (
        OFE_CODI integer not null,
        FOEANOEN integer not null,
        FOENUMEN integer not null,
        FOECAGCO integer not null,
        primary key (FOEANOEN, FOENUMEN, FOECAGCO)
    );

    comment on table BZENTOFF is
        'EntradaOficinaFisica';

    create table BZENTRA (
        FZAANOEN integer not null,
        FZANUMEN integer not null,
        FZACAGCO integer not null,
        FZAALOC integer not null,
        FZACAGGE integer not null,
        FZACENTI varchar(7) not null,
        FZACIDI varchar(1) not null,
        FZACORGA integer not null,
        FZACONEN varchar(160) not null,
        FZACONE2 varchar(160) not null,
        FZACEDIE varchar(1) not null,
        FZAFACTU integer not null,
        FZAFDOCU integer not null,
        FZAFENTR integer not null,
        FZAFSIS integer not null,
        FZAHORA integer not null,
        FZAHSIS integer not null,
        FZACIDIO varchar(255) not null,
        FZAENULA varchar(1) not null,
        FZANDIS integer not null,
        FZANENTI integer not null,
        FZANLOC integer not null,
        FZAPROCE varchar(25) not null,
        FZAREMIT varchar(30) not null,
        FZACTAGG integer not null,
        FZACTIPE varchar(2) not null,
        FZACUSU varchar(10) not null,
        primary key (FZAANOEN, FZANUMEN, FZACAGCO)
    );

    comment on table BZENTRA is
        'Entrada';

    create table BZENTRA060 (
        ENT_CODIMUN varchar(3) not null,
        ENT_ANY integer not null,
        ENT_NUM integer not null,
        ENT_OFI integer not null,
		ENT_NUMREG VARCHAR(5) NOT NULL,
        primary key (ENT_ANY, ENT_NUM, ENT_OFI)
    );

    comment on table BZENTRA060 is
        'Entrada060';

    create table BZIDIOM (
        FZMCIDI varchar(1) not null,
        FZMDIDI varchar(15) not null,
        primary key (FZMCIDI)
    );

    comment on table BZIDIOM is
        'Idioma';

    create table BZLIBRO (
        FZCAENSA integer not null,
        FZCCENSA varchar(1) not null,
        FZCCAGCO integer not null,
        FZCNUMPA integer not null,
        FZCFECED integer not null,
        primary key (FZCAENSA, FZCCENSA, FZCCAGCO)
    );

    comment on table BZLIBRO is
        'Libro';

    create table BZMODIF (
        FZJANOEN integer not null,
        FZJCENSA varchar(1) not null,
        FZJFMODI integer not null,
        FZJHMODI integer not null,
        FZJNUMEN integer not null,
        FZJCAGCO integer not null,
        FZJCUSMO varchar(10) not null,
        FZJCENTI varchar(7) not null,
        FZJCONEN varchar(160) not null,
        FZJFVISA integer not null,
        FZJHVISA integer not null,
        FZJIEXTR varchar(1) not null,
        FZJIREMI varchar(1) not null,
        FZJNENTI integer not null,
        FZJREMIT varchar(30) not null,
        FZJTEXTO varchar(150) not null,
        FZJCUSVI varchar(10) not null,
        primary key (FZJANOEN, FZJCENSA, FZJFMODI, FZJHMODI, FZJNUMEN, FZJCAGCO, FZJCUSMO)
    );

    comment on table BZMODIF is
        'Modificacion';

    create table BZMODOF (
        MOF_NOM varchar(25) not null unique,
        MOF_CONTYP varchar(32),
        MOF_DATA blob(1048576),
        primary key (MOF_NOM)
    );

    comment on table BZMODOF is
        'ModeloOficio';

    create table BZMODREB (
        MOR_NOM varchar(25) not null unique,
        MOR_CONTYP varchar(32),
        MOR_DATA blob(1048576),
        primary key (MOR_NOM)
    );

    comment on table BZMODREB is
        'ModeloRecibo';

    create table BZMOLPD (
        FZVANOEN integer not null,
        FZVCENSA varchar(1) not null,
        FZVDATAC integer not null,
        FZVFMODI integer not null,
        FZVHORAC integer not null,
        FZVHMODI integer not null,
        FZVNUMEN integer not null,
        FZVCAGCO integer not null,
        FZVTIPAC varchar(10) not null,
        FZVCUSU varchar(10) not null,
        primary key (FZVANOEN, FZVCENSA, FZVDATAC, FZVFMODI, FZVHORAC, FZVHMODI, FZVNUMEN, FZVCAGCO, FZVTIPAC, FZVCUSU)
    );

    comment on table BZMOLPD is
        'LogModificacionLopd';

    create table BZMUN_060 (
        MUN_CODI varchar(3) not null unique,
        MUN_FECBAJ integer,
        MUN_NOM varchar(30),
        primary key (MUN_CODI)
    );

    comment on table BZMUN_060 is
        'Municipio060';

    create table BZNCORR (
        FZPANOEN integer not null,
        FZPCENSA varchar(1) not null,
        FZPNUMEN integer not null,
        FZPCAGCO integer not null,
        FZPNCORR varchar(8) not null,
        primary key (FZPANOEN, FZPCENSA, FZPNUMEN, FZPCAGCO)
    );

    comment on table BZNCORR is
        'NumeroCorreo';

    create table BZOFIFIS (
        OFF_CODI integer not null,
        OFF_FECBAJ integer not null,
        OFF_NOM varchar(25) not null,
        FZOCAGCO integer not null,
        primary key (OFF_CODI, FZOCAGCO)
    );

    comment on table BZOFIFIS is
        'OficinaFisica';

    create table BZOFIOR (
        FZFCAGCO integer not null,
        FZFCORGA integer not null,
        primary key (FZFCAGCO, FZFCORGA)
    );

    comment on table BZOFIOR is
        'OficinaOrganismo';

    create table BZOFIRE (
        FZFCAGCO integer not null,
        FZFCORGA integer not null,
        primary key (FZFCAGCO, FZFCORGA)
    );

    comment on table BZOFIRE is
        'OficinaOrganismoNoRemision';

    create table BZOFREM (
        REM_OFANY integer not null,
        REM_OFNUM integer not null,
        REM_OFOFI integer not null,
        REM_ENTANY integer,
        REM_SALANY integer,
        REM_CONT varchar(1500),
        REM_ENTDES varchar(1),
        REM_ENTFEC integer,
        REM_NULFEC integer,
        REM_OFFEC integer,
        REM_ENTMTD varchar(150),
        REM_NULMTD varchar(150),
        REM_NULA varchar(1),
        REM_ENTNUM integer,
        REM_SALNUM integer,
        REM_ENTOFI integer,
        REM_SALOFI integer,
        REM_ENTUSU varchar(10),
        REM_NULUSU varchar(10),
        primary key (REM_OFANY, REM_OFNUM, REM_OFOFI)
    );

    comment on table BZOFREM is
        'OficioRemision';

    create table BZOFRENT (
        REN_ENTANY integer not null,
        REN_ENTNUM integer not null,
        REN_ENTOFI integer not null,
        REN_OFANY integer,
        REN_ENTDES varchar(1),
        REN_ENTMTD varchar(150),
        REN_OFNUM integer,
        REN_OFOFI integer,
        REN_ENTUSU varchar(10),
        primary key (REN_ENTANY, REN_ENTNUM, REN_ENTOFI)
    );

    comment on table BZOFRENT is
        'LiniaOficioRemision';

    create table BZPUBLI (
        FZEANOEN integer not null,
        FZENUMEN integer not null,
        FZECAGCO integer not null,
        FZECONEN varchar(160) not null,
        FZEFPUBL integer not null,
        FZENBOCA integer not null,
        FZENLINE integer not null,
        FZENPAGI integer not null,
        FZEOBSER varchar(50) not null,
        primary key (FZEANOEN, FZENUMEN, FZECAGCO)
    );

    comment on table BZPUBLI is
        'Publicacion';

    create table BZREPRO (
        FZCNREP varchar(50) not null,
        FZTIREP varchar(10) not null,
        FZCCUSU varchar(10) not null,
        FZCDREP varchar(65535),
        primary key (FZCNREP, FZTIREP, FZCCUSU)
    );

    comment on table BZREPRO is
        'Repro';

    create table BZSALIDA (
        FZSANOEN integer not null,
        FZSNUMEN integer not null,
        FZSCAGCO integer not null,
        FZSALOC integer not null,
        FZSCAGGE integer not null,
        FZSCENTI varchar(7) not null,
        FZSCIDI varchar(1) not null,
        FZSCORGA integer not null,
        FZSCONEN varchar(160) not null,
        FZSCONE2 varchar(160) not null,
        FZSREMIT varchar(30) not null,
        FZSPROCE varchar(25) not null,
        FZSCEDIE varchar(1) not null,
        FZSFACTU integer not null,
        FZSFDOCU integer not null,
        FZSFENTR integer not null,
        FZSFSIS integer not null,
        FZSHORA integer not null,
        FZSHSIS integer not null,
        FZSCIDIO varchar(1) not null,
        FZSENULA varchar(1) not null,
        FZSNDIS integer not null,
        FZSNENTI integer not null,
        FZSNLOC integer not null,
        FZSCTAGG integer not null,
        FZSCTIPE varchar(2) not null,
        FZSCUSU varchar(10) not null,
        primary key (FZSANOEN, FZSNUMEN, FZSCAGCO)
    );

    comment on table BZSALIDA is
        'Salida';

    create table BZSALOFF (
        OFS_CODI integer not null,
        FOSANOEN integer not null,
        FOSNUMEN integer not null,
        FOSCAGCO integer not null,
        primary key (FOSANOEN, FOSNUMEN, FOSCAGCO)
    );

    comment on table BZSALOFF is
        'SalidaOficinaFisica';

    create table BZSALPD (
        FZUANOEN integer not null,
        FZUDATAC integer not null,
        FZUHORAC integer not null,
        FZUNUMEN integer not null,
        FZUCAGCO integer not null,
        FZUTIPAC varchar(10) not null,
        FZUCUSU varchar(10) not null,
        primary key (FZUANOEN, FZUDATAC, FZUHORAC, FZUNUMEN, FZUCAGCO, FZUTIPAC, FZUCUSU)
    );

    comment on table BZSALPD is
        'LogSalidaLopd';

    create table BZTDOCU (
        FZICTIPE varchar(2) not null,
        FZIFBAJA integer not null,
        FZIDTIPE varchar(30) not null,
        primary key (FZICTIPE, FZIFBAJA)
    );

    comment on table BZTDOCU is
        'TipoDocumento';

    create table BZVISAD (
        FZKANOEN integer not null,
        FZKCENSA varchar(1) not null,
        FZKFVISA integer not null,
        FZKHVISA integer not null,
        FZKNUMEN integer not null,
        FZKCAGCO integer not null,
        FZKCENTF varchar(7) not null,
        FZKCENTI varchar(7) not null,
        FZKCONEF varchar(160) not null,
        FZKCONEI varchar(160) not null,
        FZKFENTF integer not null,
        FZKFENTI integer not null,
        FZKNENTF integer not null,
        FZKNENTI integer not null,
        FZKREMIF varchar(30) not null,
        FZKREMII varchar(30) not null,
        FZKTEXTO varchar(150) not null,
        FZKCUSVI varchar(10) not null,
        primary key (FZKANOEN, FZKCENSA, FZKFVISA, FZKHVISA, FZKNUMEN, FZKCAGCO)
    );

    comment on table BZVISAD is
        'Visado';

    alter table BAGRUGE 
        add constraint FK16555A49B801F7D0 
        foreign key (FABCAGSU, FABCTASU) 
        references BAGRUGE;

    alter table BCODPOS 
        add constraint FK1A29589E3DDA80CA 
        foreign key (F12CAGGE, F12CTAGG) 
        references BAGRUGE;

    alter table BZENTOFF 
        add constraint FKDB2F52FC78808496 
        foreign key (FOEANOEN, FOENUMEN, FOECAGCO) 
        references BZENTRA;

    alter table BZENTRA060 
        add constraint FKCCCE38C84AAEDA83 
        foreign key (ENT_ANY, ENT_NUM, ENT_OFI) 
        references BZENTRA;

    alter table BZOFIFIS 
        add constraint FKEBC9EBB6C691C709 
        foreign key (FZOCAGCO) 
        references BAGECOM;

    alter table BZOFIOR 
        add constraint FK41699D5DB7362972 
        foreign key (FZFCAGCO) 
        references BAGECOM;

    alter table BZOFIOR 
        add constraint FK41699D5DFF12CC2F 
        foreign key (FZFCORGA) 
        references BORGANI;

    alter table BZOFIRE 
        add constraint FK41699DADB7362972 
        foreign key (FZFCAGCO) 
        references BAGECOM;

    alter table BZOFIRE 
        add constraint FK41699DADFF12CC2F 
        foreign key (FZFCORGA) 
        references BORGANI;

    alter table BZSALOFF 
        add constraint FKF25857299C93D355 
        foreign key (FOSANOEN, FOSNUMEN, FOSCAGCO) 
        references BZSALIDA;
