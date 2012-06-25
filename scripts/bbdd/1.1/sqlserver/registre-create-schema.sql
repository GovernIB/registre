
    create table BAGECOM (
        FAACAGCO int not null,
        FAAFBAJA int null,
        FAADAGCO varchar(255) null,
        primary key (FAACAGCO)
    );

    create table BAGRUGE (
        FABCAGGE int not null,
        FABCTAGG int not null,
        FABFBAJA int not null,
        FABDAGGE varchar(30) not null,
        FABCAGSU int null,
        FABCTASU int null,
        primary key (FABCAGGE, FABCTAGG)
    );

    create table BCODPOS (
        F12CPOST int not null,
        F12CAGGE int not null,
        F12CTAGG int not null,
        primary key (F12CAGGE, F12CTAGG)
    );

    create table BHAGECO (
        FHACAGCO int not null,
        FHAFALTA int not null,
        FHAFBAJA int not null,
        FHADAGCO varchar(20) not null,
        primary key (FHACAGCO, FHAFALTA)
    );

    create table BHOFIFIS (
        FZHCAGCO int not null,
        OFH_CODI int not null,
        OFH_FECALT int not null,
        OFH_FECBAJ int not null,
        OFH_NOM varchar(20) not null,
        primary key (FZHCAGCO, OFH_CODI, OFH_FECALT)
    );

    create table BHORGAN (
        FHXCORGA int not null,
        FHXFALTA int not null,
        FHXFBAJA int not null,
        FHXDORGR varchar(15) not null,
        FHXDORGT varchar(40) not null,
        primary key (FHXCORGA, FHXFALTA)
    );

    create table BORGANI (
        FAXCORGA int not null,
        FAXCOAGR varchar(1) not null,
        FAXFBAJA int not null,
        FAXDORGR varchar(15) not null,
        FAXDORGT varchar(40) not null,
        primary key (FAXCORGA)
    );

    create table BTIPAGR (
        FLDFBAJA int not null,
        FLDCTAGG int not null,
        FLDDTAGG varchar(30) not null,
        FLDCTAGS int not null,
        primary key (FLDFBAJA, FLDCTAGG)
    );

    create table BZAUTOR (
        FZHCAUT varchar(2) not null,
        FZHCAGCO int not null,
        FZHCUSU varchar(10) not null,
        primary key (FZHCAUT, FZHCAGCO, FZHCUSU)
    );

    create table BZBLOQU (
        FZNAENSA int not null,
        FZNCENSA varchar(1) not null,
        FZNCAGCO int not null,
        FZNCUSU varchar(10) not null,
        primary key (FZNAENSA, FZNCENSA, FZNCAGCO)
    );

    create table BZCONES (
        FZDAENSA int not null,
        FZDCENSA varchar(1) not null,
        FZDCAGCO int not null,
        FZDNUMER int not null,
        primary key (FZDAENSA, FZDCENSA, FZDCAGCO)
    );

    create table BZDISQU (
        FZLAENSA int not null,
        FZLCENSA varchar(1) not null,
        FZLCAGCO int not null,
        FZLNDIS int not null,
        primary key (FZLAENSA, FZLCENSA, FZLCAGCO)
    );

    create table BZENLPD (
        FZTANOEN int not null,
        FZTDATAC int not null,
        FZTHORAC int not null,
        FZTNUMEN int not null,
        FZTCAGCO int not null,
        FZTTIPAC varchar(10) not null,
        FZTCUSU varchar(10) not null,
        primary key (FZTANOEN, FZTDATAC, FZTHORAC, FZTNUMEN, FZTCAGCO, FZTTIPAC, FZTCUSU)
    );

    create table BZENTID (
        FZGCENTI varchar(7) not null,
        FZGFBAJA int not null,
        FZGNENTI int not null,
        FZGCENT2 varchar(7) not null,
        FZGDENTI varchar(30) not null,
        FZGDENT2 varchar(30) not null,
        primary key (FZGCENTI, FZGFBAJA, FZGNENTI)
    );

    create table BZENTOFF (
        OFE_CODI int not null,
        FOEANOEN int not null,
        FOENUMEN int not null,
        FOECAGCO int not null,
        primary key (FOEANOEN, FOENUMEN, FOECAGCO)
    );

    create table BZENTRA (
        FZAANOEN int not null,
        FZANUMEN int not null,
        FZACAGCO int not null,
        FZAALOC int not null,
        FZACAGGE int not null,
        FZACENTI varchar(7) not null,
        FZACIDI varchar(1) not null,
        FZACORGA int not null,
        FZACONEN varchar(160) not null,
        FZACONE2 varchar(160) not null,
        FZACEDIE varchar(1) not null,
        FZAFACTU int not null,
        FZAFDOCU int not null,
        FZAFENTR int not null,
        FZAFSIS int not null,
        FZAHORA int not null,
        FZAHSIS int not null,
        FZACIDIO varchar(255) not null,
        FZAENULA varchar(1) not null,
        FZANDIS int not null,
        FZANENTI int not null,
        FZANLOC int not null,
        FZAPROCE varchar(25) not null,
        FZAREMIT varchar(30) not null,
        FZACTAGG int not null,
        FZACTIPE varchar(2) not null,
        FZACUSU varchar(10) not null,
        primary key (FZAANOEN, FZANUMEN, FZACAGCO)
    );

    create table BZENTRA060 (
        ENT_CODIMUN varchar(3) not null,
        ENT_ANY int not null,
        ENT_NUM int not null,
        ENT_OFI int not null,
		ENT_NUMREG VARCHAR(5) NOT NULL,
        primary key (ENT_ANY, ENT_NUM, ENT_OFI)
    );

    create table BZIDIOM (
        FZMCIDI varchar(1) not null,
        FZMDIDI varchar(15) not null,
        primary key (FZMCIDI)
    );

    create table BZLIBRO (
        FZCAENSA int not null,
        FZCCENSA varchar(1) not null,
        FZCCAGCO int not null,
        FZCNUMPA int not null,
        FZCFECED int not null,
        primary key (FZCAENSA, FZCCENSA, FZCCAGCO)
    );

    create table BZMODIF (
        FZJANOEN int not null,
        FZJCENSA varchar(1) not null,
        FZJFMODI int not null,
        FZJHMODI int not null,
        FZJNUMEN int not null,
        FZJCAGCO int not null,
        FZJCUSMO varchar(10) not null,
        FZJCENTI varchar(7) not null,
        FZJCONEN varchar(160) not null,
        FZJFVISA int not null,
        FZJHVISA int not null,
        FZJIEXTR varchar(1) not null,
        FZJIREMI varchar(1) not null,
        FZJNENTI int not null,
        FZJREMIT varchar(30) not null,
        FZJTEXTO varchar(150) not null,
        FZJCUSVI varchar(10) not null,
        primary key (FZJANOEN, FZJCENSA, FZJFMODI, FZJHMODI, FZJNUMEN, FZJCAGCO, FZJCUSMO)
    );

    create table BZMODOF (
        MOF_NOM varchar(25) not null unique,
        MOF_CONTYP varchar(32) null,
        MOF_DATA image null,
        primary key (MOF_NOM)
    );

    create table BZMODREB (
        MOR_NOM varchar(25) not null unique,
        MOR_CONTYP varchar(32) null,
        MOR_DATA image null,
        primary key (MOR_NOM)
    );

    create table BZMOLPD (
        FZVANOEN int not null,
        FZVCENSA varchar(1) not null,
        FZVDATAC int not null,
        FZVFMODI int not null,
        FZVHORAC int not null,
        FZVHMODI int not null,
        FZVNUMEN int not null,
        FZVCAGCO int not null,
        FZVTIPAC varchar(10) not null,
        FZVCUSU varchar(10) not null,
        primary key (FZVANOEN, FZVCENSA, FZVDATAC, FZVFMODI, FZVHORAC, FZVHMODI, FZVNUMEN, FZVCAGCO, FZVTIPAC, FZVCUSU)
    );

    create table BZMUN_060 (
        MUN_CODI varchar(3) not null unique,
        MUN_FECBAJ int null,
        MUN_NOM varchar(30) null unique,
        primary key (MUN_CODI),
        unique (MUN_NOM)
    );

    create table BZNCORR (
        FZPANOEN int not null,
        FZPCENSA varchar(1) not null,
        FZPNUMEN int not null,
        FZPCAGCO int not null,
        FZPNCORR varchar(8) not null,
        primary key (FZPANOEN, FZPCENSA, FZPNUMEN, FZPCAGCO)
    );

    create table BZOFIFIS (
        OFF_CODI int not null,
        OFF_FECBAJ int not null,
        OFF_NOM varchar(25) not null,
        FZOCAGCO int not null,
        primary key (OFF_CODI, FZOCAGCO)
    );

    create table BZOFIOR (
        FZFCAGCO int not null,
        FZFCORGA int not null,
        primary key (FZFCAGCO, FZFCORGA)
    );

    create table BZOFIRE (
        FZFCAGCO int not null,
        FZFCORGA int not null,
        primary key (FZFCAGCO, FZFCORGA)
    );

    create table BZOFREM (
        REM_OFANY int not null,
        REM_OFNUM int not null,
        REM_OFOFI int not null,
        REM_ENTANY int null,
        REM_SALANY int null,
        REM_CONT varchar(1500) null,
        REM_ENTDES varchar(1) null,
        REM_ENTFEC int null,
        REM_NULFEC int null,
        REM_OFFEC int null,
        REM_ENTMTD varchar(150) null,
        REM_NULMTD varchar(150) null,
        REM_NULA varchar(1) null,
        REM_ENTNUM int null,
        REM_SALNUM int null,
        REM_ENTOFI int null,
        REM_SALOFI int null,
        REM_ENTUSU varchar(10) null,
        REM_NULUSU varchar(10) null,
        primary key (REM_OFANY, REM_OFNUM, REM_OFOFI)
    );

    create table BZOFRENT (
        REN_ENTANY int not null,
        REN_ENTNUM int not null,
        REN_ENTOFI int not null,
        REN_OFANY int null,
        REN_ENTDES varchar(1) null,
        REN_ENTMTD varchar(150) null,
        REN_OFNUM int null,
        REN_OFOFI int null,
        REN_ENTUSU varchar(10) null,
        primary key (REN_ENTANY, REN_ENTNUM, REN_ENTOFI)
    );

    create table BZPUBLI (
        FZEANOEN int not null,
        FZENUMEN int not null,
        FZECAGCO int not null,
        FZECONEN varchar(160) not null,
        FZEFPUBL int not null,
        FZENBOCA int not null,
        FZENLINE int not null,
        FZENPAGI int not null,
        FZEOBSER varchar(50) not null,
        primary key (FZEANOEN, FZENUMEN, FZECAGCO)
    );

    create table BZREPRO (
        FZCNREP varchar(50) not null,
        FZTIREP varchar(10) not null,
        FZCCUSU varchar(10) not null,
        FZCDREP varchar(65535) null,
        primary key (FZCNREP, FZTIREP, FZCCUSU)
    );

    create table BZSALIDA (
        FZSANOEN int not null,
        FZSNUMEN int not null,
        FZSCAGCO int not null,
        FZSALOC int not null,
        FZSCAGGE int not null,
        FZSCENTI varchar(7) not null,
        FZSCIDI varchar(1) not null,
        FZSCORGA int not null,
        FZSCONEN varchar(160) not null,
        FZSCONE2 varchar(160) not null,
        FZSREMIT varchar(30) not null,
        FZSPROCE varchar(25) not null,
        FZSCEDIE varchar(1) not null,
        FZSFACTU int not null,
        FZSFDOCU int not null,
        FZSFENTR int not null,
        FZSFSIS int not null,
        FZSHORA int not null,
        FZSHSIS int not null,
        FZSCIDIO varchar(1) not null,
        FZSENULA varchar(1) not null,
        FZSNDIS int not null,
        FZSNENTI int not null,
        FZSNLOC int not null,
        FZSCTAGG int not null,
        FZSCTIPE varchar(2) not null,
        FZSCUSU varchar(10) not null,
        primary key (FZSANOEN, FZSNUMEN, FZSCAGCO)
    );

    create table BZSALOFF (
        OFS_CODI int not null,
        FOSANOEN int not null,
        FOSNUMEN int not null,
        FOSCAGCO int not null,
        primary key (FOSANOEN, FOSNUMEN, FOSCAGCO)
    );

    create table BZSALPD (
        FZUANOEN int not null,
        FZUDATAC int not null,
        FZUHORAC int not null,
        FZUNUMEN int not null,
        FZUCAGCO int not null,
        FZUTIPAC varchar(10) not null,
        FZUCUSU varchar(10) not null,
        primary key (FZUANOEN, FZUDATAC, FZUHORAC, FZUNUMEN, FZUCAGCO, FZUTIPAC, FZUCUSU)
    );

    create table BZTDOCU (
        FZICTIPE varchar(2) not null,
        FZIFBAJA int not null,
        FZIDTIPE varchar(30) not null,
        primary key (FZICTIPE, FZIFBAJA)
    );

    create table BZVISAD (
        FZKANOEN int not null,
        FZKCENSA varchar(1) not null,
        FZKFVISA int not null,
        FZKHVISA int not null,
        FZKNUMEN int not null,
        FZKCAGCO int not null,
        FZKCENTF varchar(7) not null,
        FZKCENTI varchar(7) not null,
        FZKCONEF varchar(160) not null,
        FZKCONEI varchar(160) not null,
        FZKFENTF int not null,
        FZKFENTI int not null,
        FZKNENTF int not null,
        FZKNENTI int not null,
        FZKREMIF varchar(30) not null,
        FZKREMII varchar(30) not null,
        FZKTEXTO varchar(150) not null,
        FZKCUSVI varchar(10) not null,
        primary key (FZKANOEN, FZKCENSA, FZKFVISA, FZKHVISA, FZKNUMEN, FZKCAGCO)
    );

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
