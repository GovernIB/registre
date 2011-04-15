
    create table BAGECOM (
        FAACAGCO number(10,0) not null,
        FAAFBAJA number(10,0),
        FAADAGCO varchar2(255 char),
        primary key (FAACAGCO)
    );

    comment on table BAGECOM is
        'Oficina';

    create table BAGRUGE (
        FABCAGGE number(10,0) not null,
        FABCTAGG number(10,0) not null,
        FABFBAJA number(10,0) not null,
        FABDAGGE varchar2(30 char) not null,
        FABCAGSU number(10,0),
        FABCTASU number(10,0),
        primary key (FABCAGGE, FABCTAGG)
    );

    comment on table BAGRUGE is
        'AgrupacionGeografica';

    create table BCODPOS (
        F12CPOST number(10,0) not null,
        F12CAGGE number(10,0) not null,
        F12CTAGG number(10,0) not null,
        primary key (F12CAGGE, F12CTAGG)
    );

    comment on table BCODPOS is
        'CodigoPostal';

    create table BHAGECO (
        FHACAGCO number(10,0) not null,
        FHAFALTA number(10,0) not null,
        FHAFBAJA number(10,0) not null,
        FHADAGCO varchar2(20 char) not null,
        primary key (FHACAGCO, FHAFALTA)
    );

    comment on table BHAGECO is
        'OficinaHistorico';

    create table BHOFIFIS (
        FZHCAGCO number(10,0) not null,
        OFH_CODI number(10,0) not null,
        OFH_FECALT number(10,0) not null,
        OFH_FECBAJ number(10,0) not null,
        OFH_NOM varchar2(20 char) not null,
        primary key (FZHCAGCO, OFH_CODI, OFH_FECALT)
    );

    comment on table BHOFIFIS is
        'OficinaFisicaHistorico';

    create table BHORGAN (
        FHXCORGA number(10,0) not null,
        FHXFALTA number(10,0) not null,
        FHXFBAJA number(10,0) not null,
        FHXDORGR varchar2(15 char) not null,
        FHXDORGT varchar2(40 char) not null,
        primary key (FHXCORGA, FHXFALTA)
    );

    comment on table BHORGAN is
        'OrganismoHistorico';

    create table BORGANI (
        FAXCORGA number(10,0) not null,
        FAXCOAGR varchar2(1 char) not null,
        FAXFBAJA number(10,0) not null,
        FAXDORGR varchar2(15 char) not null,
        FAXDORGT varchar2(40 char) not null,
        primary key (FAXCORGA)
    );

    comment on table BORGANI is
        'Organismo';

    create table BTIPAGR (
        FLDFBAJA number(10,0) not null,
        FLDCTAGG number(10,0) not null,
        FLDDTAGG varchar2(30 char) not null,
        FLDCTAGS number(10,0) not null,
        primary key (FLDFBAJA, FLDCTAGG)
    );

    comment on table BTIPAGR is
        'TipoAgrupacionGeografica';

    create table BZAUTOR (
        FZHCAUT varchar2(2 char) not null,
        FZHCAGCO number(10,0) not null,
        FZHCUSU varchar2(10 char) not null,
        primary key (FZHCAUT, FZHCAGCO, FZHCUSU)
    );

    comment on table BZAUTOR is
        'Autorizacion';

    create table BZBLOQU (
        FZNAENSA number(10,0) not null,
        FZNCENSA varchar2(1 char) not null,
        FZNCAGCO number(10,0) not null,
        FZNCUSU varchar2(10 char) not null,
        primary key (FZNAENSA, FZNCENSA, FZNCAGCO)
    );

    comment on table BZBLOQU is
        'BloqueoDisquete';

    create table BZCONES (
        FZDAENSA number(10,0) not null,
        FZDCENSA varchar2(1 char) not null,
        FZDCAGCO number(10,0) not null,
        FZDNUMER number(10,0) not null,
        primary key (FZDAENSA, FZDCENSA, FZDCAGCO)
    );

    comment on table BZCONES is
        'Contador';

    create table BZDISQU (
        FZLAENSA number(10,0) not null,
        FZLCENSA varchar2(1 char) not null,
        FZLCAGCO number(10,0) not null,
        FZLNDIS number(10,0) not null,
        primary key (FZLAENSA, FZLCENSA, FZLCAGCO)
    );

    comment on table BZDISQU is
        'Disquete';

    create table BZENLPD (
        FZTANOEN number(10,0) not null,
        FZTDATAC number(10,0) not null,
        FZTHORAC number(10,0) not null,
        FZTNUMEN number(10,0) not null,
        FZTCAGCO number(10,0) not null,
        FZTTIPAC varchar2(10 char) not null,
        FZTCUSU varchar2(10 char) not null,
        primary key (FZTANOEN, FZTDATAC, FZTHORAC, FZTNUMEN, FZTCAGCO, FZTTIPAC, FZTCUSU)
    );

    comment on table BZENLPD is
        'LogEntradaLopd';

    create table BZENTID (
        FZGCENTI varchar2(7 char) not null,
        FZGFBAJA number(10,0) not null,
        FZGNENTI number(10,0) not null,
        FZGCENT2 varchar2(7 char) not null,
        FZGDENTI varchar2(30 char) not null,
        FZGDENT2 varchar2(30 char) not null,
        primary key (FZGCENTI, FZGFBAJA, FZGNENTI)
    );

    comment on table BZENTID is
        'EntidadRemitente';

    create table BZENTOFF (
        OFE_CODI number(10,0) not null,
        FOEANOEN number(10,0) not null,
        FOENUMEN number(10,0) not null,
        FOECAGCO number(10,0) not null,
        primary key (FOEANOEN, FOENUMEN, FOECAGCO)
    );

    comment on table BZENTOFF is
        'EntradaOficinaFisica';

    create table BZENTRA (
        FZAANOEN number(10,0) not null,
        FZANUMEN number(10,0) not null,
        FZACAGCO number(10,0) not null,
        FZAALOC number(10,0) not null,
        FZACAGGE number(10,0) not null,
        FZACENTI varchar2(7 char) not null,
        FZACIDI varchar2(1 char) not null,
        FZACORGA number(10,0) not null,
        FZACONEN varchar2(160 char) not null,
        FZACONE2 varchar2(160 char) not null,
        FZACEDIE varchar2(1 char) not null,
        FZAFACTU number(10,0) not null,
        FZAFDOCU number(10,0) not null,
        FZAFENTR number(10,0) not null,
        FZAFSIS number(10,0) not null,
        FZAHORA number(10,0) not null,
        FZAHSIS number(10,0) not null,
        FZACIDIO varchar2(255 char) not null,
        FZAENULA varchar2(1 char) not null,
        FZANDIS number(10,0) not null,
        FZANENTI number(10,0) not null,
        FZANLOC number(10,0) not null,
        FZAPROCE varchar2(25 char) not null,
        FZAREMIT varchar2(30 char) not null,
        FZACTAGG number(10,0) not null,
        FZACTIPE varchar2(2 char) not null,
        FZACUSU varchar2(10 char) not null,
        primary key (FZAANOEN, FZANUMEN, FZACAGCO)
    );

    comment on table BZENTRA is
        'Entrada';

    create table BZENTRA060 (
        ENT_CODIMUN varchar2(3 char) not null,
        ENT_ANY number(10,0) not null,
        ENT_NUM number(10,0) not null,
        ENT_OFI number(10,0) not null,
        primary key (ENT_ANY, ENT_NUM, ENT_OFI)
    );

    comment on table BZENTRA060 is
        'Entrada060';

    create table BZIDIOM (
        FZMCIDI varchar2(1 char) not null,
        FZMDIDI varchar2(15 char) not null,
        primary key (FZMCIDI)
    );

    comment on table BZIDIOM is
        'Idioma';

    create table BZLIBRO (
        FZCAENSA number(10,0) not null,
        FZCCENSA varchar2(1 char) not null,
        FZCCAGCO number(10,0) not null,
        FZCNUMPA number(10,0) not null,
        FZCFECED number(10,0) not null,
        primary key (FZCAENSA, FZCCENSA, FZCCAGCO)
    );

    comment on table BZLIBRO is
        'Libro';

    create table BZMODIF (
        FZJANOEN number(10,0) not null,
        FZJCENSA varchar2(1 char) not null,
        FZJFMODI number(10,0) not null,
        FZJHMODI number(10,0) not null,
        FZJNUMEN number(10,0) not null,
        FZJCAGCO number(10,0) not null,
        FZJCUSMO varchar2(10 char) not null,
        FZJCENTI varchar2(7 char) not null,
        FZJCONEN varchar2(160 char) not null,
        FZJFVISA number(10,0) not null,
        FZJHVISA number(10,0) not null,
        FZJIEXTR varchar2(1 char) not null,
        FZJIREMI varchar2(1 char) not null,
        FZJNENTI number(10,0) not null,
        FZJREMIT varchar2(30 char) not null,
        FZJTEXTO varchar2(150 char) not null,
        FZJCUSVI varchar2(10 char) not null,
        primary key (FZJANOEN, FZJCENSA, FZJFMODI, FZJHMODI, FZJNUMEN, FZJCAGCO, FZJCUSMO)
    );

    comment on table BZMODIF is
        'Modificacion';

    create table BZMODOF (
        MOF_NOM varchar2(25 char) not null unique,
        MOF_CONTYP varchar2(32 char),
        MOF_DATA blob,
        primary key (MOF_NOM)
    );

    comment on table BZMODOF is
        'ModeloOficio';

    create table BZMODREB (
        MOR_NOM varchar2(25 char) not null unique,
        MOR_CONTYP varchar2(32 char),
        MOR_DATA blob,
        primary key (MOR_NOM)
    );

    comment on table BZMODREB is
        'ModeloRecibo';

    create table BZMOLPD (
        FZVANOEN number(10,0) not null,
        FZVCENSA varchar2(1 char) not null,
        FZVDATAC number(10,0) not null,
        FZVFMODI number(10,0) not null,
        FZVHORAC number(10,0) not null,
        FZVHMODI number(10,0) not null,
        FZVNUMEN number(10,0) not null,
        FZVCAGCO number(10,0) not null,
        FZVTIPAC varchar2(10 char) not null,
        FZVCUSU varchar2(10 char) not null,
        primary key (FZVANOEN, FZVCENSA, FZVDATAC, FZVFMODI, FZVHORAC, FZVHMODI, FZVNUMEN, FZVCAGCO, FZVTIPAC, FZVCUSU)
    );

    comment on table BZMOLPD is
        'LogModificacionLopd';

    create table BZMUN_060 (
        MUN_CODI varchar2(3 char) not null unique,
        MUN_FECBAJ number(10,0),
        MUN_NOM varchar2(30 char) unique,
        primary key (MUN_CODI),
        unique (MUN_NOM)
    );

    comment on table BZMUN_060 is
        'Municipio060';

    create table BZNCORR (
        FZPANOEN number(10,0) not null,
        FZPCENSA varchar2(1 char) not null,
        FZPNUMEN number(10,0) not null,
        FZPCAGCO number(10,0) not null,
        FZPNCORR varchar2(8 char) not null,
        primary key (FZPANOEN, FZPCENSA, FZPNUMEN, FZPCAGCO)
    );

    comment on table BZNCORR is
        'NumeroCorreo';

    create table BZOFIFIS (
        OFF_CODI number(10,0) not null,
        OFF_FECBAJ number(10,0) not null,
        OFF_NOM varchar2(25 char) not null,
        FZOCAGCO number(10,0) not null,
        primary key (OFF_CODI, FZOCAGCO)
    );

    comment on table BZOFIFIS is
        'OficinaFisica';

    create table BZOFIOR (
        FZFCAGCO number(10,0) not null,
        FZFCORGA number(10,0) not null,
        primary key (FZFCAGCO, FZFCORGA)
    );

    comment on table BZOFIOR is
        'OficinaOrganismo';

    create table BZOFIRE (
        FZFCAGCO number(10,0) not null,
        FZFCORGA number(10,0) not null,
        primary key (FZFCAGCO, FZFCORGA)
    );

    comment on table BZOFIRE is
        'OficinaOrganismoNoRemision';

    create table BZOFREM (
        REM_OFANY number(10,0) not null,
        REM_OFNUM number(10,0) not null,
        REM_OFOFI number(10,0) not null,
        REM_ENTANY number(10,0),
        REM_SALANY number(10,0),
        REM_CONT varchar2(1500 char),
        REM_ENTDES varchar2(1 char),
        REM_ENTFEC number(10,0),
        REM_NULFEC number(10,0),
        REM_OFFEC number(10,0),
        REM_ENTMTD varchar2(150 char),
        REM_NULMTD varchar2(150 char),
        REM_NULA varchar2(1 char),
        REM_ENTNUM number(10,0),
        REM_SALNUM number(10,0),
        REM_ENTOFI number(10,0),
        REM_SALOFI number(10,0),
        REM_ENTUSU varchar2(10 char),
        REM_NULUSU varchar2(10 char),
        primary key (REM_OFANY, REM_OFNUM, REM_OFOFI)
    );

    comment on table BZOFREM is
        'OficioRemision';

    create table BZOFRENT (
        REN_ENTANY number(10,0) not null,
        REN_ENTNUM number(10,0) not null,
        REN_ENTOFI number(10,0) not null,
        REN_OFANY number(10,0),
        REN_ENTDES varchar2(1 char),
        REN_ENTMTD varchar2(150 char),
        REN_OFNUM number(10,0),
        REN_OFOFI number(10,0),
        REN_ENTUSU varchar2(10 char),
        primary key (REN_ENTANY, REN_ENTNUM, REN_ENTOFI)
    );

    comment on table BZOFRENT is
        'LiniaOficioRemision';

    create table BZPUBLI (
        FZEANOEN number(10,0) not null,
        FZENUMEN number(10,0) not null,
        FZECAGCO number(10,0) not null,
        FZECONEN varchar2(160 char) not null,
        FZEFPUBL number(10,0) not null,
        FZENBOCA number(10,0) not null,
        FZENLINE number(10,0) not null,
        FZENPAGI number(10,0) not null,
        FZEOBSER varchar2(50 char) not null,
        primary key (FZEANOEN, FZENUMEN, FZECAGCO)
    );

    comment on table BZPUBLI is
        'Publicacion';

    create table BZREPRO (
        FZCNREP varchar2(50 char) not null,
        FZTIREP varchar2(10 char) not null,
        FZCCUSU varchar2(10 char) not null,
        FZCDREP long,
        primary key (FZCNREP, FZTIREP, FZCCUSU)
    );

    comment on table BZREPRO is
        'Repro';

    create table BZSALIDA (
        FZSANOEN number(10,0) not null,
        FZSNUMEN number(10,0) not null,
        FZSCAGCO number(10,0) not null,
        FZSALOC number(10,0) not null,
        FZSCAGGE number(10,0) not null,
        FZSCENTI varchar2(7 char) not null,
        FZSCIDI varchar2(1 char) not null,
        FZSCORGA number(10,0) not null,
        FZSCONEN varchar2(160 char) not null,
        FZSCONE2 varchar2(160 char) not null,
        FZSREMIT varchar2(30 char) not null,
        FZSPROCE varchar2(25 char) not null,
        FZSCEDIE varchar2(1 char) not null,
        FZSFACTU number(10,0) not null,
        FZSFDOCU number(10,0) not null,
        FZSFENTR number(10,0) not null,
        FZSFSIS number(10,0) not null,
        FZSHORA number(10,0) not null,
        FZSHSIS number(10,0) not null,
        FZSCIDIO varchar2(1 char) not null,
        FZSENULA varchar2(1 char) not null,
        FZSNDIS number(10,0) not null,
        FZSNENTI number(10,0) not null,
        FZSNLOC number(10,0) not null,
        FZSCTAGG number(10,0) not null,
        FZSCTIPE varchar2(2 char) not null,
        FZSCUSU varchar2(10 char) not null,
        primary key (FZSANOEN, FZSNUMEN, FZSCAGCO)
    );

    comment on table BZSALIDA is
        'Salida';

    create table BZSALOFF (
        OFS_CODI number(10,0) not null,
        FOSANOEN number(10,0) not null,
        FOSNUMEN number(10,0) not null,
        FOSCAGCO number(10,0) not null,
        primary key (FOSANOEN, FOSNUMEN, FOSCAGCO)
    );

    comment on table BZSALOFF is
        'SalidaOficinaFisica';

    create table BZSALPD (
        FZUANOEN number(10,0) not null,
        FZUDATAC number(10,0) not null,
        FZUHORAC number(10,0) not null,
        FZUNUMEN number(10,0) not null,
        FZUCAGCO number(10,0) not null,
        FZUTIPAC varchar2(10 char) not null,
        FZUCUSU varchar2(10 char) not null,
        primary key (FZUANOEN, FZUDATAC, FZUHORAC, FZUNUMEN, FZUCAGCO, FZUTIPAC, FZUCUSU)
    );

    comment on table BZSALPD is
        'LogSalidaLopd';

    create table BZTDOCU (
        FZICTIPE varchar2(2 char) not null,
        FZIFBAJA number(10,0) not null,
        FZIDTIPE varchar2(30 char) not null,
        primary key (FZICTIPE, FZIFBAJA)
    );

    comment on table BZTDOCU is
        'TipoDocumento';

    create table BZVISAD (
        FZKANOEN number(10,0) not null,
        FZKCENSA varchar2(1 char) not null,
        FZKFVISA number(10,0) not null,
        FZKHVISA number(10,0) not null,
        FZKNUMEN number(10,0) not null,
        FZKCAGCO number(10,0) not null,
        FZKCENTF varchar2(7 char) not null,
        FZKCENTI varchar2(7 char) not null,
        FZKCONEF varchar2(160 char) not null,
        FZKCONEI varchar2(160 char) not null,
        FZKFENTF number(10,0) not null,
        FZKFENTI number(10,0) not null,
        FZKNENTF number(10,0) not null,
        FZKNENTI number(10,0) not null,
        FZKREMIF varchar2(30 char) not null,
        FZKREMII varchar2(30 char) not null,
        FZKTEXTO varchar2(150 char) not null,
        FZKCUSVI varchar2(10 char) not null,
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
