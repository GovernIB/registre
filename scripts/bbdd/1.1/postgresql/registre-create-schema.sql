
    create table BAGECOM (
        FAACAGCO int4 not null,
        FAAFBAJA int4,
        FAADAGCO varchar(255),
        primary key (FAACAGCO)
    );

    comment on table BAGECOM is
        'Oficina';

    create table BAGRUGE (
        FABCAGGE int4 not null,
        FABCTAGG int4 not null,
        FABFBAJA int4 not null,
        FABDAGGE varchar(30) not null,
        FABCAGSU int4,
        FABCTASU int4,
        primary key (FABCAGGE, FABCTAGG)
    );

    comment on table BAGRUGE is
        'AgrupacionGeografica';

    create table BCODPOS (
        F12CPOST int4 not null,
        F12CAGGE int4 not null,
        F12CTAGG int4 not null,
        primary key (F12CAGGE, F12CTAGG)
    );

    comment on table BCODPOS is
        'CodigoPostal';

    create table BHAGECO (
        FHACAGCO int4 not null,
        FHAFALTA int4 not null,
        FHAFBAJA int4 not null,
        FHADAGCO varchar(20) not null,
        primary key (FHACAGCO, FHAFALTA)
    );

    comment on table BHAGECO is
        'OficinaHistorico';

    create table BHOFIFIS (
        FZHCAGCO int4 not null,
        OFH_CODI int4 not null,
        OFH_FECALT int4 not null,
        OFH_FECBAJ int4 not null,
        OFH_NOM varchar(20) not null,
        primary key (FZHCAGCO, OFH_CODI, OFH_FECALT)
    );

    comment on table BHOFIFIS is
        'OficinaFisicaHistorico';

    create table BHORGAN (
        FHXCORGA int4 not null,
        FHXFALTA int4 not null,
        FHXFBAJA int4 not null,
        FHXDORGR varchar(15) not null,
        FHXDORGT varchar(40) not null,
        primary key (FHXCORGA, FHXFALTA)
    );

    comment on table BHORGAN is
        'OrganismoHistorico';

    create table BORGANI (
        FAXCORGA int4 not null,
        FAXCOAGR varchar(1) not null,
        FAXFBAJA int4 not null,
        FAXDORGR varchar(15) not null,
        FAXDORGT varchar(40) not null,
        primary key (FAXCORGA)
    );

    comment on table BORGANI is
        'Organismo';

    create table BTIPAGR (
        FLDFBAJA int4 not null,
        FLDCTAGG int4 not null,
        FLDDTAGG varchar(30) not null,
        FLDCTAGS int4 not null,
        primary key (FLDFBAJA, FLDCTAGG)
    );

    comment on table BTIPAGR is
        'TipoAgrupacionGeografica';

    create table BZAUTOR (
        FZHCAUT varchar(2) not null,
        FZHCAGCO int4 not null,
        FZHCUSU varchar(10) not null,
        primary key (FZHCAUT, FZHCAGCO, FZHCUSU)
    );

    comment on table BZAUTOR is
        'Autorizacion';

    create table BZBLOQU (
        FZNAENSA int4 not null,
        FZNCENSA varchar(1) not null,
        FZNCAGCO int4 not null,
        FZNCUSU varchar(10) not null,
        primary key (FZNAENSA, FZNCENSA, FZNCAGCO)
    );

    comment on table BZBLOQU is
        'BloqueoDisquete';

    create table BZCONES (
        FZDAENSA int4 not null,
        FZDCENSA varchar(1) not null,
        FZDCAGCO int4 not null,
        FZDNUMER int4 not null,
        primary key (FZDAENSA, FZDCENSA, FZDCAGCO)
    );

    comment on table BZCONES is
        'Contador';

    create table BZDISQU (
        FZLAENSA int4 not null,
        FZLCENSA varchar(1) not null,
        FZLCAGCO int4 not null,
        FZLNDIS int4 not null,
        primary key (FZLAENSA, FZLCENSA, FZLCAGCO)
    );

    comment on table BZDISQU is
        'Disquete';

    create table BZENLPD (
        FZTANOEN int4 not null,
        FZTDATAC int4 not null,
        FZTHORAC int4 not null,
        FZTNUMEN int4 not null,
        FZTCAGCO int4 not null,
        FZTTIPAC varchar(10) not null,
        FZTCUSU varchar(10) not null,
        primary key (FZTANOEN, FZTDATAC, FZTHORAC, FZTNUMEN, FZTCAGCO, FZTTIPAC, FZTCUSU)
    );

    comment on table BZENLPD is
        'LogEntradaLopd';

    create table BZENTID (
        FZGCENTI varchar(7) not null,
        FZGFBAJA int4 not null,
        FZGNENTI int4 not null,
        FZGCENT2 varchar(7) not null,
        FZGDENTI varchar(30) not null,
        FZGDENT2 varchar(30) not null,
        primary key (FZGCENTI, FZGFBAJA, FZGNENTI)
    );

    comment on table BZENTID is
        'EntidadRemitente';

    create table BZENTOFF (
        OFE_CODI int4 not null,
        FOEANOEN int4 not null,
        FOENUMEN int4 not null,
        FOECAGCO int4 not null,
        primary key (FOEANOEN, FOENUMEN, FOECAGCO)
    );

    comment on table BZENTOFF is
        'EntradaOficinaFisica';

    create table BZENTRA (
        FZAANOEN int4 not null,
        FZANUMEN int4 not null,
        FZACAGCO int4 not null,
        FZAALOC int4 not null,
        FZACAGGE int4 not null,
        FZACENTI varchar(7) not null,
        FZACIDI varchar(1) not null,
        FZACORGA int4 not null,
        FZACONEN varchar(160) not null,
        FZACONE2 varchar(160) not null,
        FZACEDIE varchar(1) not null,
        FZAFACTU int4 not null,
        FZAFDOCU int4 not null,
        FZAFENTR int4 not null,
        FZAFSIS int4 not null,
        FZAHORA int4 not null,
        FZAHSIS int4 not null,
        FZACIDIO varchar(255) not null,
        FZAENULA varchar(1) not null,
        FZANDIS int4 not null,
        FZANENTI int4 not null,
        FZANLOC int4 not null,
        FZAPROCE varchar(25) not null,
        FZAREMIT varchar(30) not null,
        FZACTAGG int4 not null,
        FZACTIPE varchar(2) not null,
        FZACUSU varchar(10) not null,
        primary key (FZAANOEN, FZANUMEN, FZACAGCO)
    );

    comment on table BZENTRA is
        'Entrada';

    create table BZENTRA060 (
        ENT_CODIMUN varchar(3) not null,
        ENT_ANY int4 not null,
        ENT_NUM int4 not null,
        ENT_OFI int4 not null,
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
        FZCAENSA int4 not null,
        FZCCENSA varchar(1) not null,
        FZCCAGCO int4 not null,
        FZCNUMPA int4 not null,
        FZCFECED int4 not null,
        primary key (FZCAENSA, FZCCENSA, FZCCAGCO)
    );

    comment on table BZLIBRO is
        'Libro';

    create table BZMODIF (
        FZJANOEN int4 not null,
        FZJCENSA varchar(1) not null,
        FZJFMODI int4 not null,
        FZJHMODI int4 not null,
        FZJNUMEN int4 not null,
        FZJCAGCO int4 not null,
        FZJCUSMO varchar(10) not null,
        FZJCENTI varchar(7) not null,
        FZJCONEN varchar(160) not null,
        FZJFVISA int4 not null,
        FZJHVISA int4 not null,
        FZJIEXTR varchar(1) not null,
        FZJIREMI varchar(1) not null,
        FZJNENTI int4 not null,
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
        MOF_DATA oid,
        primary key (MOF_NOM)
    );

    comment on table BZMODOF is
        'ModeloOficio';

    create table BZMODREB (
        MOR_NOM varchar(25) not null unique,
        MOR_CONTYP varchar(32),
        MOR_DATA oid,
        primary key (MOR_NOM)
    );

    comment on table BZMODREB is
        'ModeloRecibo';

    create table BZMOLPD (
        FZVANOEN int4 not null,
        FZVCENSA varchar(1) not null,
        FZVDATAC int4 not null,
        FZVFMODI int4 not null,
        FZVHORAC int4 not null,
        FZVHMODI int4 not null,
        FZVNUMEN int4 not null,
        FZVCAGCO int4 not null,
        FZVTIPAC varchar(10) not null,
        FZVCUSU varchar(10) not null,
        primary key (FZVANOEN, FZVCENSA, FZVDATAC, FZVFMODI, FZVHORAC, FZVHMODI, FZVNUMEN, FZVCAGCO, FZVTIPAC, FZVCUSU)
    );

    comment on table BZMOLPD is
        'LogModificacionLopd';

    create table BZMUN_060 (
        MUN_CODI varchar(3) not null unique,
        MUN_FECBAJ int4,
        MUN_NOM varchar(30) unique,
        primary key (MUN_CODI),
        unique (MUN_NOM)
    );

    comment on table BZMUN_060 is
        'Municipio060';

    create table BZNCORR (
        FZPANOEN int4 not null,
        FZPCENSA varchar(1) not null,
        FZPNUMEN int4 not null,
        FZPCAGCO int4 not null,
        FZPNCORR varchar(8) not null,
        primary key (FZPANOEN, FZPCENSA, FZPNUMEN, FZPCAGCO)
    );

    comment on table BZNCORR is
        'NumeroCorreo';

    create table BZOFIFIS (
        OFF_CODI int4 not null,
        OFF_FECBAJ int4 not null,
        OFF_NOM varchar(25) not null,
        FZOCAGCO int4 not null,
        primary key (OFF_CODI, FZOCAGCO)
    );

    comment on table BZOFIFIS is
        'OficinaFisica';

    create table BZOFIOR (
        FZFCAGCO int4 not null,
        FZFCORGA int4 not null,
        primary key (FZFCAGCO, FZFCORGA)
    );

    comment on table BZOFIOR is
        'OficinaOrganismo';

    create table BZOFIRE (
        FZFCAGCO int4 not null,
        FZFCORGA int4 not null,
        primary key (FZFCAGCO, FZFCORGA)
    );

    comment on table BZOFIRE is
        'OficinaOrganismoNoRemision';

    create table BZOFREM (
        REM_OFANY int4 not null,
        REM_OFNUM int4 not null,
        REM_OFOFI int4 not null,
        REM_ENTANY int4,
        REM_SALANY int4,
        REM_CONT varchar(1500),
        REM_ENTDES varchar(1),
        REM_ENTFEC int4,
        REM_NULFEC int4,
        REM_OFFEC int4,
        REM_ENTMTD varchar(150),
        REM_NULMTD varchar(150),
        REM_NULA varchar(1),
        REM_ENTNUM int4,
        REM_SALNUM int4,
        REM_ENTOFI int4,
        REM_SALOFI int4,
        REM_ENTUSU varchar(10),
        REM_NULUSU varchar(10),
        primary key (REM_OFANY, REM_OFNUM, REM_OFOFI)
    );

    comment on table BZOFREM is
        'OficioRemision';

    create table BZOFRENT (
        REN_ENTANY int4 not null,
        REN_ENTNUM int4 not null,
        REN_ENTOFI int4 not null,
        REN_OFANY int4,
        REN_ENTDES varchar(1),
        REN_ENTMTD varchar(150),
        REN_OFNUM int4,
        REN_OFOFI int4,
        REN_ENTUSU varchar(10),
        primary key (REN_ENTANY, REN_ENTNUM, REN_ENTOFI)
    );

    comment on table BZOFRENT is
        'LiniaOficioRemision';

    create table BZPUBLI (
        FZEANOEN int4 not null,
        FZENUMEN int4 not null,
        FZECAGCO int4 not null,
        FZECONEN varchar(160) not null,
        FZEFPUBL int4 not null,
        FZENBOCA int4 not null,
        FZENLINE int4 not null,
        FZENPAGI int4 not null,
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
        FZSANOEN int4 not null,
        FZSNUMEN int4 not null,
        FZSCAGCO int4 not null,
        FZSALOC int4 not null,
        FZSCAGGE int4 not null,
        FZSCENTI varchar(7) not null,
        FZSCIDI varchar(1) not null,
        FZSCORGA int4 not null,
        FZSCONEN varchar(160) not null,
        FZSCONE2 varchar(160) not null,
        FZSREMIT varchar(30) not null,
        FZSPROCE varchar(25) not null,
        FZSCEDIE varchar(1) not null,
        FZSFACTU int4 not null,
        FZSFDOCU int4 not null,
        FZSFENTR int4 not null,
        FZSFSIS int4 not null,
        FZSHORA int4 not null,
        FZSHSIS int4 not null,
        FZSCIDIO varchar(1) not null,
        FZSENULA varchar(1) not null,
        FZSNDIS int4 not null,
        FZSNENTI int4 not null,
        FZSNLOC int4 not null,
        FZSCTAGG int4 not null,
        FZSCTIPE varchar(2) not null,
        FZSCUSU varchar(10) not null,
        primary key (FZSANOEN, FZSNUMEN, FZSCAGCO)
    );

    comment on table BZSALIDA is
        'Salida';

    create table BZSALOFF (
        OFS_CODI int4 not null,
        FOSANOEN int4 not null,
        FOSNUMEN int4 not null,
        FOSCAGCO int4 not null,
        primary key (FOSANOEN, FOSNUMEN, FOSCAGCO)
    );

    comment on table BZSALOFF is
        'SalidaOficinaFisica';

    create table BZSALPD (
        FZUANOEN int4 not null,
        FZUDATAC int4 not null,
        FZUHORAC int4 not null,
        FZUNUMEN int4 not null,
        FZUCAGCO int4 not null,
        FZUTIPAC varchar(10) not null,
        FZUCUSU varchar(10) not null,
        primary key (FZUANOEN, FZUDATAC, FZUHORAC, FZUNUMEN, FZUCAGCO, FZUTIPAC, FZUCUSU)
    );

    comment on table BZSALPD is
        'LogSalidaLopd';

    create table BZTDOCU (
        FZICTIPE varchar(2) not null,
        FZIFBAJA int4 not null,
        FZIDTIPE varchar(30) not null,
        primary key (FZICTIPE, FZIFBAJA)
    );

    comment on table BZTDOCU is
        'TipoDocumento';

    create table BZVISAD (
        FZKANOEN int4 not null,
        FZKCENSA varchar(1) not null,
        FZKFVISA int4 not null,
        FZKHVISA int4 not null,
        FZKNUMEN int4 not null,
        FZKCAGCO int4 not null,
        FZKCENTF varchar(7) not null,
        FZKCENTI varchar(7) not null,
        FZKCONEF varchar(160) not null,
        FZKCONEI varchar(160) not null,
        FZKFENTF int4 not null,
        FZKFENTI int4 not null,
        FZKNENTF int4 not null,
        FZKNENTI int4 not null,
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
