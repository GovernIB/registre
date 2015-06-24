package es.caib.regweb3.migracio;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;

import es.caib.regweb3.logic.helper.Helper;
import es.caib.regweb3.logic.helper.ParametrosListadoRegistrosEntrada;
import es.caib.regweb3.logic.helper.ParametrosListadoRegistrosSalida;
import es.caib.regweb3.logic.helper.ParametrosRegistroEntrada;
import es.caib.regweb3.logic.helper.ParametrosRegistroSalida;
import es.caib.regweb3.logic.helper.RegistroSeleccionado;
import es.caib.regweb3.model.EntradaId;
import es.caib.regweb3.model.LogEntradaLopdId;
import es.caib.regweb3.model.LogModificacionLopdId;
import es.caib.regweb3.model.LogSalidaLopdId;
import es.caib.regweb3.model.SalidaId;

/**
 * 
 * @author anadal
 *
 */
public class MigracioDadesRegistre {
  
  private static  Logger log = Logger.getLogger(MigracioDadesRegistre.class);
  
  public static final boolean TIPOREGISTRO_ENTRADA = true;
  public static final boolean TIPOREGISTRO_SALIDA = false;
  
  
  public static boolean generateSqlForOracle = false;
  
  public static boolean isTimeWithSeconds = true;
  
  public static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
  
  public static final DateFormat dateInput=new SimpleDateFormat("dd/MM/yyyy");
  
  public static final DateFormat timestampInput=new SimpleDateFormat("dd/MM/yyyy HHmmss");
  
  public static final  DateFormat dateIntegerParser=new SimpleDateFormat("yyyyMMdd");
  
  public static String printSqlBoolean(boolean value) {
    if (generateSqlForOracle) {
      return value ? "1" : "0";
    } else {
      return String.valueOf(value);
    }
  }
  
  public static String printSqlDate(Date date) {
    if (date == null) {
      return "null";
    }
    if (generateSqlForOracle) {
      return "TO_DATE('" + dateFormat.format(date) + "', 'yyyy/mm/dd hh24:mi:ss')";
    } else {
      return "to_timestamp('" + dateFormat.format(date) + "', 'YYYY/MM/DD HH24:MI:SS')";
    }
  }
  
  
  public static String printSqlString(String value) {
    if (value == null) {
      return "null";
    } else {
      return "'" + value.replace("'", "''") + "'";
    }
  }

  

  
  
  public static Date parseDate(String strFecha) throws java.text.ParseException{
    if (strFecha == null || strFecha.equals("0")) {
      return null;
    }
    Date fechaAux = dateInput.parse(strFecha);
    return fechaAux;
  }
  
  
  
  public static Date parseDateTime(String strFecha, String hora) throws java.text.ParseException{
    if (strFecha == null) {
      return null;
    }    
    Date t = timestampInput.parse(strFecha + " " + ((isTimeWithSeconds && hora.length() == 6)? hora : (hora + "00")));
    return t;
  }
  
  public static String convertHoraToHHMMSS(int hora) {
    String horaStr = String.valueOf(hora);
    int max = isTimeWithSeconds? 6: 4;
    while(horaStr.length() < max) {
      horaStr = "0" + horaStr;
    }
    return horaStr;
  }
  
  
  public static Date parseDateTime(int fecha, int hora) throws java.text.ParseException{
    if (fecha == 0) {
      return null;
    }
    
    // Obtenir hora en format dd/mm/yyyy
    
    Date date = dateIntegerParser.parse(String.valueOf(fecha));
    String fechaStr = dateInput.format(date);
    return  parseDateTime(fechaStr, convertHoraToHHMMSS(hora));
  }
  
  
  public static final Long parseLong(String value) {
    if (value == null || value.trim().length() == 0) {
      return null;
    } else {
      return Long.parseLong(value);
    }
  }

  public static void main(String[] args) {

    try {
    
    Properties prop = new Properties();
    
    prop.load(new FileInputStream(new File("./configuration.properties")));
        
    //
    // prop.put("hibernate.dialect","org.hibernate.dialect.MySQL5InnoDBDialect");
    // prop.put("javax.persistence.jdbc.driver","com.mysql.jdbc.Driver");
    // prop.put("javax.persistence.jdbc.url",
    // "jdbc:mysql://192.168.35.151:3306/portasib");
    // prop.put("javax.persistence.jdbc.user","portasib");
    // prop.put("javax.persistence.jdbc.password","portasib");
    //

    Properties configHib = new Properties();
    String hibernateDialect = prop.getProperty("hibernate.dialect");
    configHib.put("hibernate.dialect", hibernateDialect );
    
    /*
    prop.put("javax.persistence.jdbc.driver", "org.postgresql.Driver");
    // prop.put("javax.persistence.jdbc.url","jdbc:postgresql://192.168.35.151:5432/portafib");
    prop.put("javax.persistence.jdbc.url", "jdbc:postgresql://192.168.35.42:5432/regweb3");
    prop.put("javax.persistence.jdbc.user", "regweb3");
    prop.put("javax.persistence.jdbc.password", "regweb3");

    */
    
        
    configHib.put("hibernate.connection.driver_class", prop.getProperty("hibernate.connection.driver_class"));
    configHib.put("hibernate.connection.url", prop.getProperty("hibernate.connection.url"));
    configHib.put("hibernate.connection.username", prop.getProperty("hibernate.connection.username"));
    configHib.put("hibernate.connection.password", prop.getProperty("hibernate.connection.password"));
    
    String show_sql = prop.getProperty("hibernate.show_sql");
    if (show_sql != null && "true".equals(show_sql)) {
      configHib.put("hibernate.show_sql", "true");
    }

    

      EntityManagerFactory emf;

      // Veure persistence.xml
      emf = Persistence.createEntityManagerFactory("regwebmigracioDBStandalone", configHib);

      EntityManager em = emf.createEntityManager();

      //em.setFlushMode(FlushModeType.AUTO);
      em.setFlushMode(FlushModeType.COMMIT);

      //EntityTransaction tx = em.getTransaction();

      //tx.begin();
      
      Session session = (Session)em.getDelegate();
      
       
      // Desde properties
      String minDate = prop.getProperty("minDate"); // "01/01/2012";
      String maxDate = prop.getProperty("maxDate"); //"31/12/2014";
      String usuario = prop.getProperty("usuario"); // "ADMIN";
      long idCounter = Long.parseLong(prop.getProperty("idCounter")); //1000;
      generateSqlForOracle = "true".equals(prop.getProperty("generateSqlForOracle")); //;
      isTimeWithSeconds= "true".equals(prop.getProperty("generateSqlForOracle"));
      // long entitatID = 24376; // JOAN
      long entitatID = Long.parseLong(prop.getProperty("entitatID")); // 13188;

      final int minOfi = Integer.parseInt(prop.getProperty("minOfi"));
      final int maxOfi = Integer.parseInt(prop.getProperty("maxOfi"));
      
      final int tamanyPagina = Integer.parseInt(prop.getProperty("tamanyPagina"));
      System.out.println("Tamany de pàgina = " + tamanyPagina);
      
      FileOutputStream fos = new FileOutputStream("migracio_from_" 
          + minDate.replace("/", "-") + "_to_" + maxDate.replace("/", "-") + ".sql");
      
      PrintStream ps;
      
      if (generateSqlForOracle) {
        ps= new PrintStream(new BufferedOutputStream(fos));
      } else {
        ps= new PrintStream(new BufferedOutputStream(fos), true, "UTF-8");
      }
      
      
      /**
      List<String> usuaris;
      {
        SQLQuery q=session.createSQLQuery("select distinct(fzhcusu) from bzautor where fzhcaut = 'CE' order by fzhcusu");
        usuaris = q.list();
        for (String usuari : usuaris) {
          System.out.println("Usuari -> " + usuari);
        }
      
      }
      */
      
      
      /*
      String query = "SELECT fztanoen, fztdatac, fzthorac, fztnumen, fztcagco, fzttipac, fztcusu FROM bzenlpd;";
      
      SQLQuery q=session.createSQLQuery(query);
      List<LogEntradaLopd> listLOPD =q.list();
      
      for (LogEntradaLopd logEntradaLopd : listLOPD) {
        System.out.println(logEntradaLopd.getId().getAnyo());
      }   
      */
      
      
      
      
      


      /*
      ParametrosRegistroEntrada parametro = new ParametrosRegistroEntrada();
      
      parametro.setNumeroEntrada("1");
      parametro.setAnoEntrada("2014");
      parametro.fijaUsuario("admin");
      parametro.setoficina("2");
      
      
      ParametrosRegistroEntrada registro = leer(session,parametro);
      
      registro.print();
      */
      
      /*
      Calendar data = Calendar.getInstance();
      data.set(minYear, Calendar.JANUARY, 1);
      
      
      parametros.setfechaDesde(ddmmyyyy.format(new Date(data.getTimeInMillis())));
      
      data.set(maxYear, Calendar.DECEMBER, 31);
     
      parametros.setfechaHasta(ddmmyyyy.format(new Date(data.getTimeInMillis())));
      */
      

      // Numero d'element a obtenir en cada consulta

      idCounter = migrarEntradas(em, session, minDate, maxDate, usuario, idCounter, entitatID,
          minOfi, maxOfi, ps, tamanyPagina);
      
      idCounter = migrarSalidas(em, session, minDate, maxDate, usuario, idCounter, entitatID,
          minOfi, maxOfi, ps, tamanyPagina);
      
      ps.println();
      ps.println();
      ps.println("-- Last ID generated = " + idCounter);
      
      System.out.println(" Last ID generated = " + idCounter);
      
      
      /*
      javax.persistence.Query query = em.createQuery("from Idioma");
      
      List<Idioma> idiomes = query.getResultList();
      
      for (Idioma idioma : idiomes) {
        System.out.println("Idioma -> " + idioma.getCodigo());
      }
      */
      

      //tx.commit();
      
      Thread.sleep(1500);
      
      System.err.println("<<<<<<<<<<<  Good Bye!");

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static long migrarEntradas(EntityManager em, Session session, String minDate,
      String maxDate, String usuario, long idCounter, long entitatID, final int minOfi,
      final int maxOfi, PrintStream ps, int pagina) throws Exception {
    ParametrosListadoRegistrosEntrada parametros = new ParametrosListadoRegistrosEntrada();
    
    
    //DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
    parametros.setfechaDesde(minDate);
    parametros.setfechaHasta(maxDate);

    parametros.setoficinaDesde(String.valueOf(minOfi));
    parametros.setoficinaHasta(String.valueOf(maxOfi));
    parametros.setCodiMunicipi060("000"); // Tots
    
    long timeLOPD = 0;
    long timeLOPDModif = 0;
    long start = System.currentTimeMillis();
    
    long lastStart = start;
    
    final int sizePaginaE = pagina;
    int paginaE = 1;
    
    int countE = 0;
    int countLOPD = 0;
    int countLOPDModif = 0;
    
    
    
    // XYZ 
    //Set<String> duplicats = new HashSet<String>();
    
    
    ps.println();
    ps.println("-- ========== REGISTRES ENTRADA & LOPD ENTRADA ============");
    ps.println();
    
    System.out.println();
    System.out.println(" -------------- ENTRADES ---------------");
    System.out.println();
    
    Long anoentsal,codididoc, codofifis,codorgdesemi;        
    Long  numdisquet, numentsal, prodesgeobal, identidad;
    int codidiext;
    boolean anulado, tregistro;
    String denoficina,denofifis,descdoc,desididoc,desorgdesemi,desremdes,tipodoc;
    String mailremitente,extracto,desidiext,numcorreo,otros,prodesgeo,prodesgeofue;
    Date fechadoc,fechareg,fechavis;
    
    
    // ArrayList<RegistroSeleccionado> list;
    List<EntradaId> list;
    
    final long totalEntradas = recuperarTotalEntradasNuevo(em,parametros);
    
    do {
    

      
      
/*
     
      list = recuperarEntradas(session, 
          parametros, usuario, 
          sizePaginaE, paginaE);

      for (RegistroSeleccionado registroSelect : list) {

        ParametrosRegistroEntrada parametro = new ParametrosRegistroEntrada();
        
        parametro.setNumeroEntrada(registroSelect.getNumeroEntrada());
        parametro.setAnoEntrada(registroSelect.getAnoEntrada());
        parametro.setoficina(registroSelect.getOficina());
        */

     list =  recuperarEntradasNuevo(em,
         parametros, sizePaginaE, paginaE);
         
      for (EntradaId registroSelect : list) {

        ParametrosRegistroEntrada parametro = new ParametrosRegistroEntrada();
        
        parametro.setNumeroEntrada(String.valueOf(registroSelect.getNumero()));
        parametro.setAnoEntrada(String.valueOf(registroSelect.getAnyo()));
        parametro.setoficina(String.valueOf(registroSelect.getOficina()));
        
        /*
        {
          String key = registroSelect.getNumero() + "_" + registroSelect.getAnyo()
          + "_" + registroSelect.getOficina();
          if (duplicats.contains(key) ) {
            System.out.println(" COUHNTER = " + countE);
            throw new Exception("Clau duplicada " + key);
            
          }
          duplicats.add(key);
          
        }
        */

        Long id;
        id = idCounter++;

        // Registre Entrada
        Long numero,ano, codoficina;
        {
        
        parametro.fijaUsuario(usuario);


        ParametrosRegistroEntrada registro = leerEntrada(session,parametro);
        
        if (registro == null || registro.getAnoEntrada() == null ||
            registro.getOficina() == null || registro.getNumeroEntrada() == null) {
          System.out.println("    Registre null. Revisar logs");
          
          log.error("Registre null: ");
          /*
          log.error("     NumeroEntrada : " + registroSelect.getNumeroEntrada());
          log.error("     AnoEntrada: " + registroSelect.getAnoEntrada());
          */
          log.error("     NumeroEntrada : " + registroSelect.getNumero());
          log.error("     AnoEntrada: " + registroSelect.getAnyo());
          //parametro.fijaUsuario(usuario);
          log.error("     oficina: " + registroSelect.getOficina());
          
          
          continue;
        }
        
        ano = parseLong(registro.getAnoEntrada());
        codoficina = parseLong(registro.getOficina());
        numero = parseLong(registro.getNumeroEntrada());
        
        
        anoentsal = parseLong(registro.getSalida2());
        String nulo = registro.getRegistroAnulado(); 
        anulado =  (nulo == null || nulo.trim().length() == 0)? false : true;

        codididoc = parseLong(registro.getIdioma());
        if (registro.getIdioex()== null || registro.getIdioex().trim().length() == 0) {
          //String lastEntry = registroSelect.getNumeroEntrada() + "/" + registroSelect.getAnoEntrada();
          String lastEntry = registroSelect.getNumero() + "/" + registroSelect.getAnyo();
          throw new Exception(lastEntry + ": registro.getIdioex()== null or empty");
        }
        
        codidiext = parseLong(registro.getIdioex()).intValue(); // TODO canviar per SENCER
        
        codofifis = parseLong(registro.getOficinafisica());
        codorgdesemi = parseLong(registro.getDestinatari());
        denoficina = registro.getDescripcionOficina();
        denofifis = registro.getDescripcionOficinaFisica();
        descdoc = registro.getDescripcionDocumento();
        desididoc = registro.getDescripcionIdiomaDocumento();
        desorgdesemi = registro.getDescripcionOrganismoDestinatario();
        desremdes = registro.getDescripcionRemitente();
        mailremitente = registro.getEmailRemitent();
        extracto = registro.getComentario();
        fechadoc =  parseDate(registro.getData());
        fechareg =  parseDateTime(registro.getDataEntrada(), registro.getHora());
        fechavis = parseDate(registro.getDataVisado());
        
        desidiext = registro.getIdiomaExtracto();
        
        
        
        numcorreo = registro.getCorreo();
        numdisquet = parseLong(registro.getDisquet());
        
        
        numentsal = parseLong(registro.getSalida1());
        
        otros = registro.getAltres();
        
        prodesgeo = registro.getProcedenciaGeografica();
        
        prodesgeobal = parseLong(registro.getBalears());
          
        prodesgeofue = registro.getFora();
        
        tipodoc = registro.getTipo();
        
        tregistro = TIPOREGISTRO_ENTRADA;
        
        
        identidad = entitatID;
        
        
        
        // TODO falta generar  SQL
        String sql =
        "INSERT INTO rwe_registro_migrado("
        + "id, ano, anoentsal, anulado, codididoc,"
        + "codidiext, codoficina, codofifis, codorgdesemi, denoficina,"
        + "denofifis, descdoc, desididoc, desorgdesemi, desremdes,"
        + "mailremitente, extracto, fechadoc, fechareg, fechavis,"
        + "desidiext, numero, numcorreo, numdisquet, numentsal,"
        + "otros, prodesgeo, prodesgeobal, prodesgeofue, tipodoc,"
        + "tregistro,  identidad)"
        + "VALUES (" + id + ", " + ano + ", " +anoentsal + ", " + printSqlBoolean(anulado) + ", " 
        +codididoc + ", " +codidiext + ", " +codoficina + ", " +codofifis + ", "
        +codorgdesemi + ", " + printSqlString(denoficina) + ", " +printSqlString(denofifis) 
        + ", " +printSqlString(descdoc) + ", "
        +printSqlString(desididoc) + ", " +printSqlString(desorgdesemi) 
        + ", " +printSqlString(desremdes) + ", " +printSqlString(mailremitente) + ", "
        +printSqlString(extracto) + ", " + printSqlDate(fechadoc) + ", " + printSqlDate(fechareg) 
        + ", " + printSqlDate(fechavis) + ", " +printSqlString(desidiext)
        + ", " +numero + ", " +printSqlString(numcorreo) + ", " +numdisquet + ", " +numentsal + ", " 
        +printSqlString(otros) + ", " +printSqlString(prodesgeo) + ", " +prodesgeobal 
        + ", " +printSqlString(prodesgeofue) + ", " 
        +printSqlString(tipodoc) + ", " + printSqlBoolean(tregistro) + ", " +identidad + ");";
        
        
        ps.println();
        ps.println(sql);
        
        }
        
        
        // LOPD Registres Entrada

        {
          long startL = System.currentTimeMillis();
          //final int sizePaginaL = 10;
          //int paginaL = 1;
          //int countL = 0;
          List<LogEntradaLopdId> listLOPD;
          //do {
            Query query = em.createQuery("select lopd.id from LogEntradaLopd as lopd "
                + " where lopd.id.numero = " + numero 
                + " AND lopd.id.anyo = " + ano
                + " AND lopd.id.oficina = " + codoficina
                + " order by id.fecha, id.hora");
 
            //query.setFirstResult((sizePaginaL * (paginaL-1)));
            //query.setMaxResults(sizePaginaL);
            
             listLOPD =query.getResultList();
            // System.out.println(" ----------- LOPD size " + listLOPD.size());
            countLOPD = countLOPD + listLOPD.size();
            for (LogEntradaLopdId logEntradaLopdId : listLOPD) {
              
              Date fecha = parseDateTime(logEntradaLopdId.getFecha(), logEntradaLopdId.getHora());
              long idLopd = idCounter++;
              String sql = "INSERT INTO rwe_registrolopd_migrado("
             + "id, fecha, tipoacceso, usuario, regmig)"
             + "VALUES (" +idLopd + ", " + printSqlDate(fecha)+ ", '" 
             + logEntradaLopdId.getTipoAcceso() + "', '" 
             + logEntradaLopdId.getUsuario() + "', " + id + ");";
              
              ps.println(sql);
 
            }
          //  paginaL ++;
          //} while (listLOPD.size() == sizePaginaL);
          
          long endL = System.currentTimeMillis();
          
          timeLOPD = timeLOPD + (endL - startL);
        }
        
        
        /*
        {
          long startL = System.currentTimeMillis();
          //final int sizePaginaL = 10;
          //int paginaL = 1;
          //int countL = 0;
          List<LogEntradaLopd> listLOPD;
          //do {
            Query query = em.createQuery("from LogEntradaLopd as lopd "
                + " where lopd.id.numero = " + numero 
                + " AND lopd.id.anyo = " + ano
                + " AND lopd.id.oficina = " + codoficina
                + " order by id.fecha, id.hora");
 
            //query.setFirstResult((sizePaginaL * (paginaL-1)));
            //query.setMaxResults(sizePaginaL);
            
             listLOPD =query.getResultList();
            // System.out.println(" ----------- LOPD size " + listLOPD.size());
            countLOPD = countLOPD + listLOPD.size();
            for (LogEntradaLopd logEntradaLopd : listLOPD) {
              
              Date fecha = parseDateTime(logEntradaLopd.getId().getFecha(), logEntradaLopd.getId().getHora());
              long idLopd = idCounter++;
              String sql = "INSERT INTO rwe_registrolopd_migrado("
             + "id, fecha, tipoacceso, usuario, regmig)"
             + "VALUES (" +idLopd + ", " + printSqlDate(fecha)+ ", '" + logEntradaLopd.getId().getTipoAcceso() + "', '" 
             + logEntradaLopd.getId().getUsuario() + "', " + id + ");";
              
              ps.println(sql);
 
            }
          //  paginaL ++;
          //} while (listLOPD.size() == sizePaginaL);
          
          long endL = System.currentTimeMillis();
          
          timeLOPD = timeLOPD + (endL - startL);
        }
        */
        
        
        // LODP Modificats de Registres d'Entrada
        {
          
          long startL = System.currentTimeMillis();
          //final int sizePaginaL = 10;
          //int paginaL = 1;
          //int countL = 0;
          List<LogModificacionLopdId> listLOPD;
          //do {
            Query query = em.createQuery("select lopd.id from LogModificacionLopd as lopd "
                + " where lopd.id.numero = " + numero 
                + " AND lopd.id.anyo = " + ano
                + " AND lopd.id.oficina = " + codoficina
                + " AND lopd.id.codigoEntradaSalida = 'E'"
                + " order by id.fecha, id.hora");
 
            //query.setFirstResult((sizePaginaL * (paginaL-1)));
            //query.setMaxResults(sizePaginaL);
            
             listLOPD =query.getResultList();
            // System.out.println(" ----------- LOPD size " + listLOPD.size());
            countLOPDModif = countLOPDModif + listLOPD.size();
            for (LogModificacionLopdId logEntradaLopdModif : listLOPD) {
              
              Date fecha = parseDateTime(logEntradaLopdModif.getFecha(), logEntradaLopdModif.getHora());
              Date fechaMod = parseDateTime(logEntradaLopdModif.getFechaModificacion(), logEntradaLopdModif.getHoraModificacion());
              long idLopd = idCounter++;

              String sql = "INSERT INTO rwe_modificacionlopd_migrado("
             + "id, fecha, fechamod, tipoacceso, usuario, regmig)"
             + "VALUES (" +idLopd + ", " + printSqlDate(fecha) + ", " + printSqlDate(fechaMod)+ ", '" + logEntradaLopdModif.getTipoAcceso() + "', '" 
             + logEntradaLopdModif.getUsuario() + "', " + id + ");";
              
              ps.println(sql);
 
            }
          //  paginaL ++;
          //} while (listLOPD.size() == sizePaginaL);
          
          long endL = System.currentTimeMillis();
          
          timeLOPDModif = timeLOPDModif + (endL - startL);
        }

        //System.out.println("Llegides " + count + " LogLopdEntrada en " + formatElapsedTime(end - start));

        //registroSeleccionado.print();
        //break;
        countE ++;

      }
      paginaE ++;
      
      long now = System.currentTimeMillis();
      
      System.out.println("Migracio en progres. Procesades " + countE + "/" + totalEntradas + "  Entrades en " + formatElapsedTime(now - lastStart));
      lastStart =now;
    } while(list.size() == sizePaginaE);
    

    final long totalTime = System.currentTimeMillis() - start;
    final long entradesTime = totalTime - timeLOPD;
    
    System.out.println();
    System.out.println("========== FINAL ENTRADES en " + formatElapsedTime(totalTime) + ": ");
    System.out.println("     + Llegides " + countE + " entrades en " + formatElapsedTime(entradesTime));
    System.out.println("     + Llegides " + countLOPD + " LOPD en " + formatElapsedTime(timeLOPD));
    System.out.println();
    System.out.println();
    return idCounter;
  }
  
  
  
  public static String formatElapsedTime(final long l) {
    final long hr = TimeUnit.MILLISECONDS.toHours(l);
    final long min = TimeUnit.MILLISECONDS.toMinutes(l - TimeUnit.HOURS.toMillis(hr));
    final long sec = TimeUnit.MILLISECONDS.toSeconds(l - TimeUnit.HOURS.toMillis(hr)
        - TimeUnit.MINUTES.toMillis(min));
    final long ms = TimeUnit.MILLISECONDS.toMillis(l - TimeUnit.HOURS.toMillis(hr)
        - TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec));
    return String.format("%02d:%02d:%02d.%03d", hr, min, sec, ms);
  }
  
  
  /** 
   * Lee un registro del fichero BZENTRA, para ello le
   * deberemos pasar el usuario, el codigo de oficina, el numero de registro de
   * entrada y el año de entrada.
   * @param param ParametrosRegistroEntrada
   * @return ParametrosRegistroEntrada
   * @ejb.interface-method
   * @ejb.permission unchecked="true"
   */
  public static ParametrosRegistroEntrada leerEntrada(Session session, ParametrosRegistroEntrada param) {
      
      ParametrosRegistroEntrada res = new ParametrosRegistroEntrada();
  
      String usuario=param.getUsuario();
      String anoEntrada=param.getAnoEntrada();
      String numeroEntrada=param.getNumeroEntrada();
      String oficina=param.getOficina();      
   //Session session = getSession();
      ScrollableResults rs = null;
      ScrollableResults rsHist = null;
      SQLQuery q = null;
      SQLQuery qHist = null;    
      res.setLeido(false);
      
      try {
        // XYZ
        //LocalitzadorsDocsElectronicsFacade regLocDocElect = LocalitzadorsDocsElectronicsFacadeUtil.getHome().create();
          String sentenciaHql="SELECT FZAANOEN, FZANUMEN, FZACAGCO, OFF_CODI, OFF_NOM, FZAFENTR, " +
                  " FAADAGCO, FZAFDOCU, FZAFACTU, FZAHORA, FZGCENTI, FZAREMIT, " +
                  " FZGDENT2, FZACORGA, FAXDORGT, FZIDTIPE, FZACTIPE, FZACIDI, " +
                  " FZACIDIO, FZAENULA, FZMDIDI, FZACENTI, FZANENTI, FZAREMIT, " +
                  " FZACAGGE, FZAPROCE, FABDAGGE, FZACONEN, FZACONE2, FZANDIS, " +
                  " FZANLOC, FZAALOC, FZPNCORR, EMAILREMITENT FROM BZENTRA LEFT JOIN BAGECOM ON FAACAGCO=FZACAGCO " +
                  " LEFT JOIN BZENTID ON FZACENTI=FZGCENTI AND FZGNENTI=FZANENTI " +
                  " LEFT JOIN BORGANI ON FAXCORGA=FZACORGA " +
                  " LEFT JOIN BZTDOCU ON FZICTIPE=FZACTIPE " +
                  " LEFT JOIN BZIDIOM ON FZACIDI=FZMCIDI " +
                  " LEFT JOIN BZENTOFF ON FZAANOEN=FOEANOEN AND FZANUMEN=FOENUMEN AND FZACAGCO=FOECAGCO " +
                  " LEFT JOIN BZOFIFIS ON FZACAGCO=FZOCAGCO AND OFF_CODI=OFE_CODI " +
                  " LEFT JOIN BAGRUGE ON FZACTAGG=FABCTAGG AND FZACAGGE=FABCAGGE " +
                  " LEFT JOIN BZAUTOR ON FZHCUSU=? AND FZHCAGCO=FZACAGCO " +
                  //" LEFT JOIN BZAUTOR ON FZHCAGCO=FZACAGCO " +
                  " LEFT JOIN BZNCORR ON FZPCENSA='E' AND FZPCAGCO=FZACAGCO AND FZPANOEN=FZAANOEN AND FZPNUMEN=FZANUMEN " +
                  " WHERE FZAANOEN=? AND FZANUMEN=? AND FZACAGCO=? AND FZHCAUT=?";
          q=session.createSQLQuery(sentenciaHql);
          q.addScalar("FZAANOEN", Hibernate.INTEGER);
          q.addScalar("FZANUMEN", Hibernate.INTEGER);
          q.addScalar("FZACAGCO", Hibernate.INTEGER);
          q.addScalar("OFF_CODI", Hibernate.INTEGER);
          q.addScalar("OFF_NOM" , Hibernate.STRING);
          q.addScalar("FZAFENTR", Hibernate.INTEGER);
          q.addScalar("FAADAGCO", Hibernate.STRING);
          q.addScalar("FZAFDOCU", Hibernate.INTEGER);
          q.addScalar("FZAFACTU", Hibernate.INTEGER);
          q.addScalar("FZAHORA" , Hibernate.INTEGER);
          q.addScalar("FZGCENTI", Hibernate.STRING);
          q.addScalar("FZAREMIT", Hibernate.STRING);
          q.addScalar("FZGDENT2", Hibernate.STRING);
          q.addScalar("FZACORGA", Hibernate.INTEGER);
          q.addScalar("FAXDORGT", Hibernate.STRING);
          q.addScalar("FZIDTIPE", Hibernate.STRING);
          q.addScalar("FZACTIPE", Hibernate.STRING);
          q.addScalar("FZACIDI" , Hibernate.STRING);
          q.addScalar("FZACIDIO", Hibernate.STRING);
          q.addScalar("FZAENULA", Hibernate.STRING);
          q.addScalar("FZMDIDI" , Hibernate.STRING);
          q.addScalar("FZACENTI", Hibernate.STRING);
          q.addScalar("FZANENTI", Hibernate.STRING);
          q.addScalar("FZAREMIT", Hibernate.STRING);
          q.addScalar("FZACAGGE", Hibernate.STRING);
          q.addScalar("FZAPROCE", Hibernate.STRING);
          q.addScalar("FABDAGGE", Hibernate.STRING);
          q.addScalar("FZACONEN", Hibernate.STRING);
          q.addScalar("FZACONE2", Hibernate.STRING);
          q.addScalar("FZANDIS" , Hibernate.STRING);
          q.addScalar("FZANLOC" , Hibernate.INTEGER);
          q.addScalar("FZAALOC" , Hibernate.INTEGER);
          q.addScalar("FZPNCORR", Hibernate.STRING);
          q.addScalar("EMAILREMITENT", Hibernate.STRING);
          
          q.setString(0,usuario.toUpperCase());
          q.setInteger(1,Integer.valueOf(anoEntrada));
          q.setInteger(2,Integer.valueOf(numeroEntrada));
          q.setInteger(3,Integer.valueOf(oficina));
          q.setString(4,"CE");
          //log.debug("Leyendo registro entrada...");
          //log.debug(q);
          rs=q.scroll();
          if (rs.next()) {
            /* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
            //Date fechaSystem=new Date();
            //DateFormat hhmmss=new SimpleDateFormat("HHmmss");
              //DateFormat sss=new SimpleDateFormat("S");
              //String ss=sss.format(fechaSystem);
              //DateFormat aaaammdd=new SimpleDateFormat("yyyyMMdd");
              //int fzafsis=Integer.parseInt(aaaammdd.format(fechaSystem));
 
              
              // XYZ
              //int horamili=Helper.obtenerHoraSistemaHHmmssSS();
              //logLopdBZENTRA("SELECT", usuario, fzafsis, horamili, Integer.parseInt(numeroEntrada), Integer.parseInt(anoEntrada), Integer.parseInt(oficina));
              
              res.setLeido(true);
              res.setAnoEntrada(String.valueOf(rs.getInteger(0)));
              res.setNumeroEntrada(String.valueOf(rs.getInteger(1)));
              res.setoficina(String.valueOf(rs.getInteger(2)));
      res.setoficinafisica(String.valueOf(rs.getInteger(3)));

      /* Aquí hem d'anar a la taula d'oficines fisiques. */
      String textoOficinaFisica=null;
      textoOficinaFisica=rs.getString(4);
      if (rs.getInteger(3)==null) {
        res.setoficinafisica("0");

                String sentenciaHqlOfiFis="SELECT OFF_NOM FROM BZOFIFIS WHERE FZOCAGCO=? AND OFF_CODI=0 ";
              qHist=session.createSQLQuery(sentenciaHqlOfiFis);
              qHist.addScalar("OFF_NOM", Hibernate.STRING);
              qHist.setInteger(0,Integer.valueOf(oficina));
              rsHist=qHist.scroll();
              if (rsHist.next()) {
          textoOficinaFisica=rsHist.getString(0);
              }
          if (rsHist != null)
            rsHist.close();

      }
      if (textoOficinaFisica==null) {
        textoOficinaFisica=" ";
      }

      res.setDescripcionOficinaFisica(textoOficinaFisica);

              String fechaEntra=String.valueOf(rs.getInteger(5));
              res.setdataentrada(Helper.convierteyyyymmddFechaAddmmyyyyFecha(fechaEntra));

              String textoOficina=null;
              String sentenciaHqlHistOfi="SELECT FHADAGCO FROM BHAGECO WHERE FHACAGCO=? AND FHAFALTA<=? " +
                  "AND ( (FHAFBAJA>= ? AND FHAFBAJA !=0) OR FHAFBAJA = 0)";
              qHist=session.createSQLQuery(sentenciaHqlHistOfi);
              qHist.addScalar("FHADAGCO", Hibernate.STRING);
              qHist.setInteger(0,Integer.valueOf(oficina));
              qHist.setInteger(1,Integer.valueOf(fechaEntra));
              qHist.setInteger(2,Integer.valueOf(fechaEntra));
              rsHist=qHist.scroll();
              if (rsHist.next()) {
                /* Hem trobat un històric de l'oficina sol·licitada, hem de mostrar-ne el descriptiu. */
                textoOficina=rsHist.getString(0);
              } else {
                textoOficina=rs.getString(6);
                if (textoOficina==null) {
                  textoOficina=" ";
                }
              }
              //  Tancam el preparedstatement i resultset de l'històric
          if (rsHist != null)
            rsHist.close();
          
              res.setDescripcionOficina(textoOficina);
              
              String fechaDocu=String.valueOf(rs.getInteger(7));
              try {  
                res.setdata(Helper.convierteyyyymmddFechaAddmmyyyyFecha(fechaDocu));
              } catch (Exception e) {
                  res.setdata("0");
              }

              String fechaVisado=String.valueOf(rs.getInteger(8));                
              try {               
                res.setDataVisado(Helper.convierteyyyymmddFechaAddmmyyyyFecha(fechaVisado));
              } catch (Exception e) {
                  res.setDataVisado("0");
              }
          
              res.sethora(String.valueOf(rs.getInteger(9)));
              if (rs.getString(10)==null) {
                  res.setDescripcionRemitente(rs.getString(11));
              } else {
                  res.setDescripcionRemitente(rs.getString(12));
              }
              res.setdestinatari(String.valueOf(rs.getInteger(13)));
              
              String sentenciaHqlHistOrga="SELECT FHXDORGT FROM BHORGAN WHERE FHXCORGA=? AND FHXFALTA<=? " +
                  "AND ( (FHXFBAJA>= ? AND FHXFBAJA !=0) OR FHXFBAJA = 0)";
              qHist=session.createSQLQuery(sentenciaHqlHistOrga);
              qHist.addScalar("FHXDORGT", Hibernate.STRING);
              qHist.setInteger(0,Integer.valueOf(res.getDestinatari()));
              qHist.setInteger(1,Integer.valueOf(fechaEntra));
              qHist.setInteger(2,Integer.valueOf(fechaEntra));
              rsHist=qHist.scroll();
              
              if (rsHist.next()) {
                /* Hem trobat un històric de l'organisme sol·licitat, hem de mostrar-ne el descriptiu. */
                res.setDescripcionOrganismoDestinatario(rsHist.getString(0));
                //log.debug("Org destinatari: "+descripcionOrganismoDestinatario);
              } else {
                res.setDescripcionOrganismoDestinatario(rs.getString(14));
                if (res.getDescripcionOrganismoDestinatario()==null) {
                  res.setDescripcionOrganismoDestinatario(" ");
                }
              }
              //log.debug("Org destinatari: "+this.getDescripcionOrganismoDestinatario());
              //  Tancam el preparedstatement i resultset de l'històric
          if (rsHist != null)
            rsHist.close();
              
              if (rs.getString(15)==null)
                res.setDescripcionDocumento("");
              else
                res.setDescripcionDocumento(rs.getString(15));
              
              if (rs.getString(16)==null )
                res.settipo("");
              else 
                res.settipo(rs.getString(16));
              
              res.setidioma(rs.getString(17));
              res.setidioex(rs.getString(18));
              res.setRegistroAnulado(rs.getString(19));
              res.setDescripcionIdiomaDocumento(rs.getString(20));
              res.setentidad1(Helper.convierteEntidad(rs.getString(21),session));
              res.setEntidad1Grabada(rs.getString(21));
              res.setentidad2(rs.getString(22));
              res.setaltres((rs.getString(23)==null)?null:rs.getString(23).trim());
              res.setbalears(rs.getString(24));
              res.setfora((rs.getString(25)==null)?null:rs.getString(25).trim());
             
              if (rs.getString(26)==null) {
                 res.setProcedenciaGeografica(((rs.getString(25)==null)?null:rs.getString(25).trim()));
              } else {
                 res.setProcedenciaGeografica(rs.getString(26).trim());
              }
              if (rs.getString(18).equals("1")) {
                  res.setIdiomaExtracto("CASTELLA");
                  res.setcomentario(rs.getString(27));
              } else {
                  res.setIdiomaExtracto("CATALA");
                  res.setcomentario(((rs.getString(28)==null)?null:rs.getString(28).trim()));
              }
              if (rs.getString(29).equals("0")) {
                  res.setdisquet("");
              } else {
                  res.setdisquet(rs.getString(29));
              }
              res.setsalida1(String.valueOf(rs.getInteger(30)));
              res.setsalida2(String.valueOf(rs.getInteger(31)));
              res.setCorreo(rs.getString(32));
              res.setEmailRemitent((rs.getString(33)!=null)?rs.getString(33).trim():null);
              
              //XYZ
              //res.setLocalitzadorsDocs(regLocDocElect.LeerListaLocalizadoresDocEntrada(Integer.parseInt(anoEntrada),Integer.parseInt(numeroEntrada),Integer.parseInt(oficina)));           
              
              // leer060() lee el campo de municipio 060 asociado al registro. Este dato se almacena en la tabla BZENTRA060.
              leer060(res, session);
          }
      } catch (Exception e) {
        log.error("ERROR: Leer: "+e.getMessage());
      } finally {
    if (rs!=null) rs.close();
     // XYZ
     //  close(session);
      }
      return res;
  }

  

  /** 
* Lee un registro del fichero BZENTRA060 (si existe), para ello le
* deberemos pasar el codigo de oficina, el numero de registro de
* entrada y el año de entrada.
* @param param ParametrosRegistroEntrada
* @return void
*/
private static void leer060(ParametrosRegistroEntrada param, Session session) throws Exception {
   
   String anoEntrada=param.getAnoEntrada();
   String numeroEntrada=param.getNumeroEntrada();
   String oficina=param.getOficina();
   
 ScrollableResults rs = null;
 SQLQuery q = null;

   try {

       String sentenciaHql="SELECT ENT_CODIMUN, MUN_NOM, ENT_NUMREG FROM BZENTRA060,BZMUN_060 WHERE ENT_ANY=? AND ENT_OFI=? AND ENT_NUM=? AND ENT_CODIMUN=MUN_CODI";
       q=session.createSQLQuery(sentenciaHql);
       q.addScalar("ENT_CODIMUN", Hibernate.STRING);
       q.addScalar("MUN_NOM", Hibernate.STRING);
       q.addScalar("ENT_NUMREG", Hibernate.STRING);
       q.setInteger(0,Integer.valueOf(anoEntrada));
       q.setInteger(1,Integer.valueOf(oficina));
       q.setInteger(2,Integer.valueOf(numeroEntrada));
       rs=q.scroll();
       if (rs.next()) {
         param.setMunicipi060(rs.getString(0));
         param.setDescripcionMunicipi060(rs.getString(1));
         param.setNumeroDocumentosRegistro060(rs.getString(2));
       }
       
   }catch(Exception e ){
       throw e;
   }finally{
   if (rs!=null) rs.close();
   }
}


   public static class RegistroKey {
     String numeroEntrada;
     String anoEntrada;
     String oficina;
    public String getNumeroEntrada() {
      return numeroEntrada;
    }
    public void setNumeroEntrada(String numeroEntrada) {
      this.numeroEntrada = numeroEntrada;
    }
    public String getAnoEntrada() {
      return anoEntrada;
    }
    public void setAnoEntrada(String anoEntrada) {
      this.anoEntrada = anoEntrada;
    }
    public String getOficina() {
      return oficina;
    }
    public void setOficina(String oficina) {
      this.oficina = oficina;
    }
     
   
     
   }
   
   
   
   public static long recuperarTotalEntradasNuevo(EntityManager em,
       ParametrosListadoRegistrosEntrada parametros) {

     String fechaDesde=parametros.getFechaDesde();
     String fechaHasta=parametros.getFechaHasta();
     
     
     String fechaInicio=fechaDesde.substring(6,10)+fechaDesde.substring(3,5)+fechaDesde.substring(0,2);
     String fechaFinal=fechaHasta.substring(6,10)+fechaHasta.substring(3,5)+fechaHasta.substring(0,2);
     
       //do {
         Query query = em.createQuery("select count(*) from Entrada as entrada "
             + " where entrada.fechaEntrada >= " + fechaInicio + " AND entrada.fechaEntrada <= " + fechaFinal 
             );

         
        return  Long.parseLong(query.getSingleResult().toString());
     
   }
     



   public static List<EntradaId> recuperarEntradasNuevo(EntityManager em,
       ParametrosListadoRegistrosEntrada parametros, int sizePaginaL, int paginaL) {
     

     

     String fechaDesde=parametros.getFechaDesde();
     String fechaHasta=parametros.getFechaHasta();
     
     
     String fechaInicio=fechaDesde.substring(6,10)+fechaDesde.substring(3,5)+fechaDesde.substring(0,2);
     String fechaFinal=fechaHasta.substring(6,10)+fechaHasta.substring(3,5)+fechaHasta.substring(0,2);
     
     // LOPD Registres Entrada
     {
       //long startL = System.currentTimeMillis();
       
       
       
       //int countL = 0;
       List<EntradaId> listLOPD;
       //do {
         Query query = em.createQuery("select entrada.id from Entrada as entrada "
             + " where entrada.fechaEntrada >= " + fechaInicio + " AND entrada.fechaEntrada <= " + fechaFinal 
             + " order by entrada.id.anyo, entrada.id.numero, entrada.id.oficina ");

         query.setFirstResult((sizePaginaL * (paginaL-1)));
         query.setMaxResults(sizePaginaL);
         
          listLOPD =query.getResultList();
          
          return listLOPD;
        
        
     }
     
   }



  
  
  
  /**
   * @ejb.interface-method
   * @ejb.permission unchecked="true"
   */
   public static ArrayList<RegistroSeleccionado> recuperarEntradas(Session session, 
       ParametrosListadoRegistrosEntrada parametros, String usuario, 
       int sizePagina, int pagina) throws java.rmi.RemoteException {
   //Session session = getSession();
   ScrollableResults rs = null;
   //ScrollableResults rsHist = null;
   SQLQuery q = null;
   //SQLQuery qHist = null;
   
   String oficinaDesde=parametros.getOficinaDesde();
       String oficinaHasta=parametros.getOficinaHasta();
       String fechaDesde=parametros.getFechaDesde();
       String fechaHasta=parametros.getFechaHasta();
       String oficinaFisica=parametros.getOficinaFisica();
       //Hashtable errores2=parametros.getErrores();
       //String any=parametros.getAny();
       String extracto=parametros.getExtracto();
       String tipo=parametros.getTipo();
       String remitente=parametros.getRemitente();
       String procedencia=parametros.getProcedencia();
       String destinatario=parametros.getDestinatario();
       String codiDestinatari=parametros.getCodiDestinatari();
       //String totalFiles=parametros.getTotalFiles();
       String codiMun060=parametros.getCodiMunicipi060();
       String numeroRegistroSalidaRelacionado=parametros.getNumeroRegistroSalidaRelacionado();
       String anyoRegistroSalidaRelacionado=parametros.getAnyoRegistroSalidaRelacionado();
       
   usuario=usuario.toUpperCase();
   ArrayList<RegistroSeleccionado> registrosVector=new ArrayList<RegistroSeleccionado>(sizePagina);
   // XYZ
//   DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
//   DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
//   java.util.Date fechaDocumento=null;
//   java.util.Date fechaEESS=null;
//           
//   String numero_de_registres ;
   
   
   String fechaInicio=fechaDesde.substring(6,10)+fechaDesde.substring(3,5)+fechaDesde.substring(0,2);
   String fechaFinal=fechaHasta.substring(6,10)+fechaHasta.substring(3,5)+fechaHasta.substring(0,2);
   
   try {
     
     
     String sentenciaHql = "SELECT DISTINCT * FROM BZENTRA LEFT JOIN BAGECOM ON FAACAGCO=FZACAGCO " +
     "LEFT JOIN BZENTOFF ON FOECAGCO=FZACAGCO AND FOEANOEN=FZAANOEN AND FOENUMEN=FZANUMEN " +
     "LEFT JOIN BZOFIFIS ON FZOCAGCO=FOECAGCO AND OFF_CODI=OFE_CODI " +
     "LEFT JOIN BZENTID ON FZACENTI=FZGCENTI AND FZGNENTI=FZANENTI " +
     "LEFT JOIN BORGANI ON FAXCORGA=FZACORGA " +
     "LEFT JOIN BHORGAN ON FHXCORGA=FZACORGA " +
     "LEFT JOIN BZTDOCU ON FZICTIPE=FZACTIPE " +
     "LEFT JOIN BZIDIOM ON FZACIDI=FZMCIDI " +
     "LEFT JOIN BAGRUGE ON FZACTAGG=FABCTAGG AND FZACAGGE=FABCAGGE " +
     "LEFT JOIN BZAUTOR ON FZHCUSU=? AND FZHCAGCO=FZACAGCO AND FZHCAUT=? " +
     //"LEFT JOIN BZAUTOR ON FZHCAGCO=FZACAGCO AND FZHCAUT=? " +
     (codiMun060.trim().equals("999")? "INNER JOIN BZENTRA060 ON FZAANOEN = ENT_ANY AND FZANUMEN= ENT_NUM AND FZACAGCO=ENT_OFI ":"LEFT JOIN BZENTRA060 ON FZAANOEN = ENT_ANY AND FZANUMEN= ENT_NUM AND FZACAGCO=ENT_OFI ") +
     "WHERE FZACAGCO>=? AND FZACAGCO<=? AND " +
     "FZAFENTR>=? AND FZAFENTR<=? " +
     (oficinaFisica==null || oficinaFisica.equals("")?"":" AND OFF_CODI = ? ") +
     (!extracto.trim().equals("") ? " AND (UPPER(FZACONEN) LIKE ? OR UPPER(FZACONE2) LIKE ?) " : "") +
     (!tipo.trim().equals("") ? " AND FZACTIPE=? " : "") +
     (!remitente.trim().equals("") ? " AND (UPPER(FZAREMIT) LIKE ? OR UPPER(FZGDENT2) LIKE ? ) " : "") +
     (!procedencia.trim().equals("") ? " AND (UPPER(FZAPROCE) LIKE ? OR UPPER(FABDAGGE) LIKE ? ) " : "") +
     " AND ( (FHXFALTA<=FZAFENTR AND ( (FHXFBAJA>= FZAFENTR AND FHXFBAJA !=0) OR FHXFBAJA = 0) ) ) " +
     (!destinatario.trim().equals("") ? " AND UPPER(FHXDORGT) LIKE ? " : "") +
     (!codiDestinatari.trim().equals("") ? " AND FZACORGA = ? " : "") +
     (!(numeroRegistroSalidaRelacionado.trim().equals("") )? " AND FZANLOC = ? " : "") +
     (!(anyoRegistroSalidaRelacionado.trim().equals("") )? " AND FZAALOC = ? " : "") +
     (!(codiMun060.trim().equals("000") || codiMun060.trim().equals("999")) ? " AND ENT_CODIMUN = ? " : "") +
     " ORDER BY FZACAGCO, FZAANOEN, FZANUMEN ";
     //log.debug(sentenciaHql);
     q=session.createSQLQuery(sentenciaHql); //, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
           q.addScalar("FZAANOEN", Hibernate.INTEGER);//0
           q.addScalar("FZANUMEN", Hibernate.INTEGER);//1
           q.addScalar("FZACAGCO", Hibernate.INTEGER);
           q.addScalar("FZAFENTR", Hibernate.INTEGER);
           q.addScalar("OFF_CODI", Hibernate.STRING);
           q.addScalar("OFF_NOM" , Hibernate.STRING);
           q.addScalar("FAADAGCO", Hibernate.STRING);
           q.addScalar("FZAFDOCU", Hibernate.INTEGER);
           q.addScalar("FZGCENTI", Hibernate.STRING);
           q.addScalar("FZAREMIT", Hibernate.STRING);
           q.addScalar("FZGDENT2", Hibernate.STRING);//10
           q.addScalar("FABDAGGE", Hibernate.STRING);
           q.addScalar("FZAPROCE", Hibernate.STRING);
           q.addScalar("FZACORGA", Hibernate.INTEGER);
           q.addScalar("FAXDORGT", Hibernate.STRING);
           q.addScalar("FZIDTIPE", Hibernate.STRING);
           q.addScalar("FZMDIDI" , Hibernate.STRING);
           q.addScalar("FZAENULA", Hibernate.STRING);//17
           q.addScalar("FZACIDIO", Hibernate.STRING);
           q.addScalar("FZACONEN", Hibernate.STRING);//19
           q.addScalar("FZACONE2", Hibernate.STRING);
           q.addScalar("ENT_NUMREG", Hibernate.STRING);//21
           
           
           
           
     int contador=0;
     q.setString(contador++,usuario); 
     q.setString(contador++,"CE");
     q.setInteger(contador++,Integer.parseInt(oficinaDesde));
     q.setInteger(contador++,Integer.parseInt(oficinaHasta));
     //System.out.println("Fecha Inicio = " + Integer.parseInt(fechaInicio));
     q.setInteger(contador++,Integer.parseInt(fechaInicio));
     //System.out.println("Fecha Final = " + Integer.parseInt(fechaFinal));
     q.setInteger(contador++,Integer.parseInt(fechaFinal));
     if (oficinaFisica!=null && !oficinaFisica.equals("")) {
         q.setInteger(contador++, Integer.parseInt(oficinaFisica));
     }
     if (!extracto.trim().equals("")) {
       q.setString(contador++,"%"+extracto.toUpperCase()+"%");
       q.setString(contador++,"%"+extracto.toUpperCase()+"%");
     }
     if (!tipo.trim().equals("")) {
       q.setString(contador++,tipo);
     }
     if (!remitente.trim().equals("")) {
       q.setString(contador++,"%"+remitente.toUpperCase()+"%");
       q.setString(contador++,"%"+remitente.toUpperCase()+"%");
     }
     if (!procedencia.trim().equals("")) {
       q.setString(contador++,"%"+procedencia.toUpperCase()+"%");
       q.setString(contador++,"%"+procedencia.toUpperCase()+"%");
     }
     if (!destinatario.trim().equals("")) {
       q.setString(contador++,"%"+destinatario.toUpperCase()+"%");
     }
     
     if (!codiDestinatari.trim().equals("")) {
       q.setInteger(contador++,Integer.valueOf(codiDestinatari));
     }
     
     if (!numeroRegistroSalidaRelacionado.trim().equals("")) {
       q.setInteger(contador++,Integer.valueOf(numeroRegistroSalidaRelacionado));
     }
     
     if (!anyoRegistroSalidaRelacionado.trim().equals("")) {
       q.setInteger(contador++,Integer.valueOf(anyoRegistroSalidaRelacionado));
     }
     
     if (!(codiMun060.trim().equals("000") || codiMun060.trim().equals("999"))) {
       q.setString(contador++,codiMun060);
       //log.debug("Codi  municipi 2: "+codiMun060);
     }
     
     if (pagina<=1 && parametros.isCalcularTotalRegistres()) {
       int totalRegistres060 = 0;
     // La primera vegada, calculam el nombre màxim de registres que tornarà la consulta
     rs=q.scroll(ScrollMode.SCROLL_INSENSITIVE);
           // log.debug("Nombre total de registres ="+rs.getFetchSize());
     
     while (rs.next()) {
       
       if((rs.getString(21)!=null)&&(!rs.getString(21).equals(""))&&(!rs.getString(17).equalsIgnoreCase("S")))
         totalRegistres060 += Integer.parseInt(rs.getString(21));
     }
     
//      Point to the last row in resultset.
         rs.last();
         // Get the row position which is also the number of rows in the resultset.
         int rowcount = rs.getRowNumber()+1;
         // Reposition at the beginning of the ResultSet to take up rs.next() call.
         rs.beforeFirst();
         log.debug("Total rows for the query using Scrollable ResultSet: "
                            +rowcount);
         parametros.setTotalFiles( String.valueOf(rowcount));
         
         // XYZ
         //this.totalRegistres060 = totalRegistres060;
         
     }
       
       // Numero maximo de registros a devolver
     // Código antiguo e incorrecto
     //q.setMaxResults((sizePagina*pagina)+1);
     
     q.setFirstResult((sizePagina * (pagina-1)));
     // TODO: Ver porqué setMaxResults desordena los resultados
     //
     // ZZZ
     q.setMaxResults(sizePagina);
     
     rs=q.scroll(ScrollMode.SCROLL_INSENSITIVE);
         
       
     //Controlamos el número de filas devuelta usando una variable local
     //hasta solucionar la incidencia de setMaxResults
       int i = 0;
     while (rs.next() && i<=sizePagina) {
               i++;
       RegistroSeleccionado registro=new RegistroSeleccionado();
       
       registro.setAnoEntrada(String.valueOf(rs.getInteger(0)));
       registro.setNumeroEntrada(String.valueOf(rs.getInteger(1)));
       registro.setOficina(String.valueOf(rs.getInteger(2)));
       
       /* XYZ
       String fechaES=String.valueOf(rs.getInteger(3));
       try {
         fechaEESS=yyyymmdd.parse(fechaES);
         registro.setFechaES(ddmmyyyy.format(fechaEESS));
       } catch (Exception e) {
         registro.setFechaES(fechaES);
       }
       
       registro.setOficinaFisica(rs.getString(4));
       registro.setDescripcionOficinaFisica(rs.getString(5));

       /* Aquí hem d'anar a l'històric.
        IF WSLE-BHAGECO02 AND
        W-FHACAGCO = WC-FHACAGCO AND
        W-FHAFALTA <= W-FZAFENTR-1 AND
        ((W-FZAFENTR-1 <= W-FHAFBAJA AND W-FHAFBAJA NOT = ZEROS) OR
        W-FHAFBAJA = ZEROS)
        MOVE W-FHADAGCO TO P3S-DAGCO
        ELSE
        PERFORM READR-BAGECOM01-RBAGECOM
        IF WSLE-BAGECOM01
        MOVE W-FAADAGCO   TO P3S-DAGCO.
        */
       /* XYZ
       String textoOficina=null;
       String sentenciaHqlHistOfi="SELECT FHADAGCO FROM BHAGECO WHERE FHACAGCO=? AND FHAFALTA<=? " +
       "AND ( (FHAFBAJA>= ? AND FHAFBAJA !=0) OR FHAFBAJA = 0)";
       qHist=session.createSQLQuery(sentenciaHqlHistOfi);
       qHist.addScalar("FHADAGCO", Hibernate.STRING);
       qHist.setInteger(0,rs.getInteger(2));
       qHist.setInteger(1,Integer.valueOf(fechaES));
       qHist.setInteger(2,Integer.valueOf(fechaES));
       rsHist=qHist.scroll();
       if (rsHist.next()) {
         // Hem trobat un històric de l'oficina sol·licitada, hem de mostrar-ne el descriptiu. 
         textoOficina=rsHist.getString(0);
       } else {
         textoOficina=rs.getString(6);
         if (textoOficina==null) {
           textoOficina=" ";
         }
       }
       //  Tancam el preparedstatement i resultset de l'històric
       if (rsHist != null)
         rsHist.close();
       
       registro.setDescripcionOficina(textoOficina);
       String fechaDocu=String.valueOf(rs.getInteger(7));
       try {
         fechaDocumento=yyyymmdd.parse(fechaDocu);
         registro.setData(ddmmyyyy.format(fechaDocumento));
       } catch (Exception e) {
         registro.setData(fechaDocu);
       }
       
       if (rs.getString(8)==null) {
         registro.setDescripcionRemitente(rs.getString(9));
       } else {                                          
         registro.setDescripcionRemitente(rs.getString(10));
       }                                                 
       if (rs.getString(11)==null) {             
         registro.setDescripcionGeografico(rs.getString(12));
       } else {                                          
         registro.setDescripcionGeografico(rs.getString(11));
       }
       */
       // registro.setDescripcionOrganismoDestinatario(rs.getString("faxdorgr"));
       
       /* Aquí hem d'anar a l'històric d'organismes 
        MOVE W-FZACORGA-1   TO P3S-CORGA WC-FAXCORGA WC-FHXCORGA.
        MOVE W-FZAFENTR-1   TO                       WC-FHXFALTA.
        PERFORM START-BHORGAN02-RBHORGAN.
        IF WSLE-BHORGAN02 AND
        W-FHXCORGA = WC-FHXCORGA AND
        W-FHXFALTA <= W-FZAFENTR-1 AND
        ((W-FZAFENTR-1 <= W-FHXFBAJA AND W-FHXFBAJA NOT = ZEROS) OR
        W-FHXFBAJA = ZEROS)
        MOVE W-FHXDORGT   TO P3S-DORGT
        ELSE
        PERFORM READR-BORGANI01-RBORGANI
        IF WSLE-BORGANI01
        MOVE W-FAXDORGT   TO P3S-DORGT. */
       /* XYZ
       String sentenciaHqlHistOrga="SELECT FHXDORGT FROM BHORGAN WHERE FHXCORGA=? AND FHXFALTA<=? " +
       "AND ( (FHXFBAJA>= ? AND FHXFBAJA !=0) OR FHXFBAJA = 0)";
       qHist=session.createSQLQuery(sentenciaHqlHistOrga);
       qHist.addScalar("FHXDORGT", Hibernate.STRING);
       qHist.setInteger(0,rs.getInteger(13));
       qHist.setInteger(1,Integer.valueOf(fechaES));
       qHist.setInteger(2,Integer.valueOf(fechaES));
       rsHist=qHist.scroll();
       if (rsHist.next()) {
         // Hem trobat un històric de l'organisme sol·licitat, hem de mostrar-ne el descriptiu.
         registro.setDescripcionOrganismoDestinatario(rsHist.getString(0));
         //log.debug("Org destinatari: "+descripcionOrganismoDestinatario);
       } else {
         registro.setDescripcionOrganismoDestinatario(rs.getString(14));
         if (registro.getDescripcionOrganismoDestinatario()==null) {
           registro.setDescripcionOrganismoDestinatario(" ");
         }
       }
       //log.debug("Org destinatari: "+this.getDescripcionOrganismoDestinatario());
       //  Tancam el preparedstatement i resultset de l'històric
       if (rsHist != null)
         rsHist.close();
       
       
       
       //registro.setDescripcionDocumento(rs.getString("fzidtipe"));
       if (rs.getString(15)==null)
         registro.setDescripcionDocumento("&nbsp;");
       else
         registro.setDescripcionDocumento(rs.getString(15));
       
       registro.setDescripcionIdiomaDocumento(rs.getString(16));
       registro.setRegistroAnulado(rs.getString(17));
       if (rs.getString(18).equals("1")) {
         registro.setExtracto(rs.getString(19));
       } else {
         registro.setExtracto(rs.getString(20));
       }
       
       // Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) 
             Date fechaSystem=new Date();
             DateFormat hhmmss=new SimpleDateFormat("HHmmss");
               DateFormat sss=new SimpleDateFormat("S");
               String Stringsss=sss.format(fechaSystem);
               String ss=sss.format(fechaSystem);
               DateFormat aaaammdd=new SimpleDateFormat("yyyyMMdd");
               int fzafsis=Integer.parseInt(aaaammdd.format(fechaSystem));
               if (ss.length()>2) {
                   ss=ss.substring(0,2);
               }
               //int fzahsis=Integer.parseInt(hhmmss.format(fechaSystem)+ss);
               
               switch (Stringsss.length()) {
               //Hem d'emplenar amb 0s.
               case (1):
                 Stringsss="00"+Stringsss;
               break;
               case (2):
                 Stringsss="0"+Stringsss;
               break;
               }
               int horamili=Integer.parseInt(hhmmss.format(fechaSystem)+Stringsss);
               
               // XYZ
               //logLopdBZENTRA("SELECT", usuario, fzafsis, horamili, rs.getInteger(1), rs.getInteger(0), rs.getInteger(2));
               
               registro.setNumeroDocumentosRegistro060((rs.getString(21)!= null)?Integer.parseInt(rs.getString(21)):0);
               
               */
               
               registrosVector.add(registro);
     }
     
   } catch (Exception e) {
     log.error("Error: "+e.getMessage());
     e.printStackTrace();
   } finally {
     if (rs!=null) rs.close();
     // XYZ
     //close(session);
   }
   return registrosVector;
 }

   
   
   

   public static long recuperarTotalSalidasNuevo(EntityManager em,
       ParametrosListadoRegistrosSalida parametros) {

     String fechaDesde=parametros.getFechaDesde();
     String fechaHasta=parametros.getFechaHasta();
     
     
     String fechaInicio=fechaDesde.substring(6,10)+fechaDesde.substring(3,5)+fechaDesde.substring(0,2);
     String fechaFinal=fechaHasta.substring(6,10)+fechaHasta.substring(3,5)+fechaHasta.substring(0,2);
     
       //do {
         Query query = em.createQuery("select count(*) from Salida as salida "
             + " where salida.fechaSalida >= " + fechaInicio + " AND salida.fechaSalida <= " + fechaFinal 
             );

         
        return  Long.parseLong(query.getSingleResult().toString());
     
   }
     



   public static List<SalidaId> recuperarSalidasNuevo(EntityManager em,
       ParametrosListadoRegistrosSalida parametros, int sizePaginaL, int paginaL) {
     

     String fechaDesde=parametros.getFechaDesde();
     String fechaHasta=parametros.getFechaHasta();
     
     
     String fechaInicio=fechaDesde.substring(6,10)+fechaDesde.substring(3,5)+fechaDesde.substring(0,2);
     String fechaFinal=fechaHasta.substring(6,10)+fechaHasta.substring(3,5)+fechaHasta.substring(0,2);
     
     // LOPD Registres Entrada
     {
       //long startL = System.currentTimeMillis();
       
       //int countL = 0;
       List<SalidaId> listLOPD;
       //do {
         Query query = em.createQuery("select salida.id from Salida as salida "
             + " where salida.fechaSalida >= " + fechaInicio + " AND salida.fechaSalida <= " + fechaFinal 
             + " order by salida.id.anyo, salida.id.numero, salida.id.oficina ");

         query.setFirstResult((sizePaginaL * (paginaL-1)));
         query.setMaxResults(sizePaginaL);
         
          listLOPD =query.getResultList();
          
          return listLOPD;

     }
     
   }


   
   
   
   
 
   
   /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public static ArrayList<RegistroSeleccionado> recuperarSalidas(Session session,
        ParametrosListadoRegistrosSalida parametros, String usuario, int sizePagina, int pagina) {

    ScrollableResults rs = null;
    ScrollableResults rsHist = null;
    SQLQuery q = null;
    SQLQuery qHist = null;
    
    String oficinaDesde=parametros.getOficinaDesde();
    String oficinaHasta=parametros.getOficinaHasta();
    String fechaDesde=parametros.getFechaDesde();
    String fechaHasta=parametros.getFechaHasta();
    String oficinaFisica=parametros.getOficinaFisica();
    //Hashtable errores=parametros.getErrores();
    //String any=parametros.getAny();
    String extracto=parametros.getExtracto();
    String tipo=parametros.getTipo();
    String remitente=parametros.getRemitente();
    String destinatario=parametros.getDestinatario();
    String codiRemitent=parametros.getCodiRemitent();
    String destino=parametros.getDestino();
    
    usuario=usuario.toUpperCase();
    ArrayList<RegistroSeleccionado> registrosSalidaVector=new ArrayList<RegistroSeleccionado>(sizePagina);
    DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
    DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
    java.util.Date fechaDocumento=null;
    java.util.Date fechaEESS=null;
    
    String fechaInicio=fechaDesde.substring(6,10)+fechaDesde.substring(3,5)+fechaDesde.substring(0,2);
    String fechaFinal=fechaHasta.substring(6,10)+fechaHasta.substring(3,5)+fechaHasta.substring(0,2);
    
    try {

      String sentenciaSql="SELECT DISTINCT * FROM BZSALIDA LEFT JOIN BAGECOM ON FAACAGCO=FZSCAGCO " +
      "LEFT JOIN BZSALOFF ON FOSCAGCO=FZSCAGCO AND FOSANOEN=FZSANOEN AND FOSNUMEN=FZSNUMEN " +
      "LEFT JOIN BZOFIFIS ON FZOCAGCO=FOSCAGCO AND OFF_CODI=OFS_CODI " +
      "LEFT JOIN BZENTID ON FZSCENTI=FZGCENTI AND FZGNENTI=FZSNENTI " +
      "LEFT JOIN BORGANI ON FAXCORGA=FZSCORGA " +
      "LEFT JOIN BHORGAN ON FHXCORGA=FZSCORGA " +
      "LEFT JOIN BZTDOCU ON FZICTIPE=FZSCTIPE " +
      "LEFT JOIN BZIDIOM ON FZSCIDI=FZMCIDI " +
      "LEFT JOIN BAGRUGE ON FZSCTAGG=FABCTAGG AND FZSCAGGE=FABCAGGE " +
      "LEFT JOIN BZAUTOR ON FZHCUSU=? AND FZHCAGCO=FZSCAGCO AND FZHCAUT=? " +
      //"LEFT JOIN BZAUTOR ON FZHCAGCO=FZSCAGCO AND FZHCAUT=? " +
      "WHERE " +
      "FZSCAGCO>=? AND FZSCAGCO<=? AND " +
      "FZSFENTR>=? AND FZSFENTR<=? " +
      (oficinaFisica==null || oficinaFisica.equals("")?"":" AND OFF_CODI = ? ") +
      (!extracto.trim().equals("") ? " AND (UPPER(FZSCONEN) LIKE ? OR UPPER(FZSCONE2) LIKE ?) " : "") +
      (!tipo.trim().equals("") ? " AND FZSCTIPE=? " : "") +
      (!destinatario.trim().equals("") ? " AND (UPPER(FZSREMIT) LIKE ? OR UPPER(FZGDENT2) LIKE ? ) " : "") +
      (!destino.trim().equals("") ? " AND (UPPER(FZSPROCE) LIKE ? OR UPPER(FABDAGGE) LIKE ? ) " : "") +
      (!remitente.trim().equals("") ? " AND UPPER(FHXDORGT) LIKE ? " : "") +
      " AND ( (FHXFALTA<=FZSFENTR AND ( (FHXFBAJA>= FZSFENTR AND FHXFBAJA !=0) OR FHXFBAJA = 0) ) ) " +
      //(!REMITENTE.TRIM().EQUALS("") ? " AND UPPER(FHXDORGT) LIKE ? )" : "") +
      (!codiRemitent.trim().equals("") ? " AND FZSCORGA = ? " : "") +
      "ORDER BY FZSCAGCO, FZSANOEN, FZSNUMEN";
      q=session.createSQLQuery(sentenciaSql); //, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      q.addScalar("FZSANOEN", Hibernate.INTEGER);
            q.addScalar("FZSNUMEN", Hibernate.INTEGER);
            q.addScalar("FZSCAGCO", Hibernate.INTEGER);
            q.addScalar("FZSFENTR", Hibernate.INTEGER);
            q.addScalar("OFF_CODI", Hibernate.STRING);
            q.addScalar("OFF_NOM" , Hibernate.STRING);
            q.addScalar("FAADAGCO", Hibernate.STRING);
            q.addScalar("FZSFDOCU", Hibernate.INTEGER);
            q.addScalar("FZGCENTI", Hibernate.STRING);
            q.addScalar("FZSREMIT", Hibernate.STRING);
            q.addScalar("FZGDENT2", Hibernate.STRING);
            q.addScalar("FABDAGGE", Hibernate.STRING);
            q.addScalar("FZSPROCE", Hibernate.STRING);
            q.addScalar("FZSCORGA", Hibernate.INTEGER);
            q.addScalar("FAXDORGT", Hibernate.STRING);
            q.addScalar("FZIDTIPE", Hibernate.STRING);
            q.addScalar("FZMDIDI" , Hibernate.STRING);
            q.addScalar("FZSENULA", Hibernate.STRING);
            q.addScalar("FZSCIDIO", Hibernate.STRING);
            q.addScalar("FZSCONEN", Hibernate.STRING);
            q.addScalar("FZSCONE2", Hibernate.STRING);
      
      int contador=0;
      q.setString(contador++,usuario);
      q.setString(contador++,"CS");
      q.setInteger(contador++,Integer.parseInt(oficinaDesde));
      q.setInteger(contador++,Integer.parseInt(oficinaHasta));
      q.setInteger(contador++,Integer.parseInt(fechaInicio));
      q.setInteger(contador++,Integer.parseInt(fechaFinal));
      if (oficinaFisica!=null && !oficinaFisica.equals("")) {
          q.setInteger(contador++, Integer.parseInt(oficinaFisica));
      }
      if (!extracto.trim().equals("")) {
        q.setString(contador++,"%"+extracto.toUpperCase()+"%");
        q.setString(contador++,"%"+extracto.toUpperCase()+"%");
      }
      if (!tipo.trim().equals("")) {
        q.setString(contador++,tipo);
      }
      if (!destinatario.trim().equals("")) {
        q.setString(contador++,"%"+destinatario.toUpperCase()+"%");
        q.setString(contador++,"%"+destinatario.toUpperCase()+"%");
      }
      if (!destino.trim().equals("")) {
        q.setString(contador++,"%"+destino.toUpperCase()+"%");
        q.setString(contador++,"%"+destino.toUpperCase()+"%");
      }
      
      if (!remitente.trim().equals("")) {
        q.setString(contador++,"%"+remitente.toUpperCase()+"%");
      }
      
      if (!codiRemitent.trim().equals("")) {
        q.setString(contador++,codiRemitent);
      }
      
      if (pagina<=1 && parametros.isCalcularTotalRegistres()) {
        // La primera vegada, calculam el nombre màxim de registres que tornarà la consulta
        rs=q.scroll(ScrollMode.SCROLL_INSENSITIVE);
        //log.debug("Nombre total de registres ="+rs.getFetchSize());
//         Point to the last row in resultset.
            rs.last();      
            // Get the row position which is also the number of rows in the resultset.
            int rowcount = rs.getRowNumber()+1;
            // Reposition at the beginning of the ResultSet to take up rs.next() call.
            rs.beforeFirst();
            log.debug("Total rows for the query using Scrollable ResultSet: "
                               +rowcount);
            parametros.setTotalFiles( String.valueOf(rowcount));
      }

      //Numero maximo de registros a devolver
      //q.setMaxResults((sizePagina*pagina)+1);
      q.setFirstResult(sizePagina * (pagina-1));
      
      // TODO: Ver porqué setMaxResults desordena los resultados
      //
      //q.setMaxResults((sizePagina));
      rs=q.scroll(ScrollMode.SCROLL_INSENSITIVE);
      
      //Controlamos el número de filas devuelta usando una variable local
      //hasta solucionar la incidencia de setMaxResults
        int i = 0;
      while (rs.next() && i<=sizePagina) {
                i++;
        RegistroSeleccionado registroSalida=new RegistroSeleccionado();
        
        registroSalida.setAnoEntrada(String.valueOf(rs.getInteger(0)));
        registroSalida.setNumeroEntrada(String.valueOf(rs.getInteger(1)));
        registroSalida.setOficina(String.valueOf(rs.getInteger(2)));
        
        String fechaES=String.valueOf(rs.getInteger(3));
        try {
          fechaEESS=yyyymmdd.parse(fechaES);
          registroSalida.setFechaES(ddmmyyyy.format(fechaEESS));
        } catch (Exception e) {
          registroSalida.setFechaES(fechaES);
        }
        
        
        registroSalida.setOficinaFisica(rs.getString(4));
        registroSalida.setDescripcionOficinaFisica(rs.getString(5));

        /* Aquí hem d'anar a l'històric.
         IF WSLE-BHAGECO02 AND
         W-FHACAGCO = WC-FHACAGCO AND
         W-FHAFALTA <= W-FZAFENTR-1 AND
         ((W-FZAFENTR-1 <= W-FHAFBAJA AND W-FHAFBAJA NOT = ZEROS) OR
         W-FHAFBAJA = ZEROS)
         MOVE W-FHADAGCO TO P3S-DAGCO
         ELSE
         PERFORM READR-BAGECOM01-RBAGECOM
         IF WSLE-BAGECOM01
         MOVE W-FAADAGCO   TO P3S-DAGCO.
         */
        String textoOficina=null;
        String sentenciaSqlHistOfi="SELECT FHADAGCO FROM BHAGECO WHERE FHACAGCO=? AND FHAFALTA<=? " +
        "AND ( (FHAFBAJA>= ? AND FHAFBAJA !=0) OR FHAFBAJA = 0)";
        qHist=session.createSQLQuery(sentenciaSqlHistOfi);
        qHist.addScalar("FHADAGCO", Hibernate.STRING);
        qHist.setInteger(0,rs.getInteger(2));
        qHist.setInteger(1,Integer.valueOf(fechaES));
        qHist.setInteger(2,Integer.valueOf(fechaES));
        rsHist=qHist.scroll();
        if (rsHist.next()) {
          /* Hem trobat un històric de l'oficina sol·licitada, hem de mostrar-ne el descriptiu. */
          textoOficina=rsHist.getString(0);
        } else {
          textoOficina=rs.getString(6);
          if (textoOficina==null) {
            textoOficina=" ";
          }
        }
        //  Tancam el preparedstatement i resultset de l'històric
        if (rsHist != null)
          rsHist.close();
        
        //String textoOficina=rs.getString("faadagco");
        //if (textoOficina==null) {textoOficina=" ";}
        registroSalida.setDescripcionOficina(textoOficina);
        String fechaDocu=String.valueOf(rs.getInteger(7));
        try {
          fechaDocumento=yyyymmdd.parse(fechaDocu);
          registroSalida.setData(ddmmyyyy.format(fechaDocumento));
        } catch (Exception e) {
          registroSalida.setData(fechaDocu);
        }
        
        if (rs.getString(8)==null) {
          registroSalida.setDescripcionRemitente(rs.getString(9));
        } else {
          registroSalida.setDescripcionRemitente(rs.getString(10));
        }
        
        if (rs.getString(11)==null) {
          registroSalida.setDescripcionGeografico(rs.getString(12));
        } else {
          registroSalida.setDescripcionGeografico(rs.getString(11));
        }
        
        //registroSalida.setDescripcionOrganismoDestinatario(rs.getString("faxdorgr"));
        
        /* Aquí hem d'anar a l'històric d'organismes 
         MOVE W-FZACORGA-1   TO P3S-CORGA WC-FAXCORGA WC-FHXCORGA.
         MOVE W-FZAFENTR-1   TO                       WC-FHXFALTA.
         PERFORM START-BHORGAN02-RBHORGAN.
         IF WSLE-BHORGAN02 AND
         W-FHXCORGA = WC-FHXCORGA AND
         W-FHXFALTA <= W-FZAFENTR-1 AND
         ((W-FZAFENTR-1 <= W-FHXFBAJA AND W-FHXFBAJA NOT = ZEROS) OR
         W-FHXFBAJA = ZEROS)
         MOVE W-FHXDORGT   TO P3S-DORGT
         ELSE
         PERFORM READR-BORGANI01-RBORGANI
         IF WSLE-BORGANI01
         MOVE W-FAXDORGT   TO P3S-DORGT. */
        String sentenciaSqlHistOrga="SELECT FHXDORGT FROM BHORGAN WHERE FHXCORGA=? AND FHXFALTA<=? " +
        "AND ( (FHXFBAJA>= ? AND FHXFBAJA !=0) OR FHXFBAJA = 0)";
        qHist=session.createSQLQuery(sentenciaSqlHistOrga);
        qHist.addScalar("FHXDORGT", Hibernate.STRING);
        qHist.setInteger(0,rs.getInteger(13));
        qHist.setInteger(1,Integer.valueOf(fechaES));
        qHist.setInteger(2,Integer.valueOf(fechaES));
        rsHist=qHist.scroll();
        if (rsHist.next()) {
          /* Hem trobat un històric de l'organisme sol·licitat, hem de mostrar-ne el descriptiu. */
          registroSalida.setDescripcionOrganismoDestinatario(rsHist.getString(0));
          //log.debug("Org destinatari: "+descripcionOrganismoDestinatario);
        } else {
          registroSalida.setDescripcionOrganismoDestinatario(rs.getString(14));
          if (registroSalida.getDescripcionOrganismoDestinatario()==null) {
            registroSalida.setDescripcionOrganismoDestinatario(" ");
          }
        }
        //log.debug("Org destinatari: "+this.getDescripcionOrganismoDestinatario());
        //  Tancam el preparedstatement i resultset de l'històric
        if (rsHist != null)
          rsHist.close();
        
        
        //registroSalida.setDescripcionDocumento(rs.getString("fzidtipe"));
        if (rs.getString(15)==null)
          registroSalida.setDescripcionDocumento("&nbsp;");
        else
          registroSalida.setDescripcionDocumento(rs.getString(15));
        
        registroSalida.setDescripcionIdiomaDocumento(rs.getString(16));
        registroSalida.setRegistroAnulado(rs.getString(17));
        if (rs.getString(18).equals("1")) {
          registroSalida.setExtracto(rs.getString(19));
        } else {
          registroSalida.setExtracto(rs.getString(20));
        }
        
        /* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
              Date fechaSystem=new Date();
              //DateFormat hhmmss=new SimpleDateFormat("HHmmss");
                DateFormat sss=new SimpleDateFormat("S");
                String ss=sss.format(fechaSystem);
                //DateFormat aaaammdd=new SimpleDateFormat("yyyyMMdd");
                //int fzafsis=Integer.parseInt(aaaammdd.format(fechaSystem));
                if (ss.length()>2) {
                    ss=ss.substring(0,2);
                }
                //int fzahsis=Integer.parseInt(hhmmss.format(fechaSystem)+ss);
                String Stringsss=sss.format(fechaSystem);
                switch (Stringsss.length()) {
                //Hem d'emplenar amb 0s.
                case (1):
                  Stringsss="00"+Stringsss;
                break;
                case (2):
                  Stringsss="0"+Stringsss;
                break;
                }

                //int horamili=Integer.parseInt(hhmmss.format(fechaSystem)+Stringsss);
                //logLopdBZSALIDA("SELECT", usuario, fzafsis, horamili, rs.getInteger(1), rs.getInteger(0), rs.getInteger(2));

        registrosSalidaVector.add(registroSalida);
      }
    } catch (Exception e) {
      log.error("Error: "+e.getMessage());
      e.printStackTrace();
    } finally {
      if (rs!=null) rs.close();
      // XYZ
      //close(session);
    }
    return registrosSalidaVector;
  }
  
    
    
    public static final DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
    public static final DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");


    public static final DateFormat sss=new SimpleDateFormat("S");

    /** 
     * Lee un registro de entrada del fichero BZSALIDA, para ello le
     * deberemos pasar el usuario, el codigo de oficina, el numero de registro de
     * salida y el año de salida.
     * @param param ParametrosRegistroSalida
     * @return Devuelve un objeto de la clase ParametrosRegistroSalida
      * @ejb.interface-method
      * @ejb.permission unchecked="true"
      */
      public static ParametrosRegistroSalida leerSalida(Session session, ParametrosRegistroSalida param) {    
      ParametrosRegistroSalida res = new ParametrosRegistroSalida();    
           ScrollableResults rs = null;
          ScrollableResults rsHist = null;
          SQLQuery q = null;
          SQLQuery qHist = null;
        String oficina=param.getOficina();
        String usuario=param.getUsuario();    
        String anoSalida=param.getAnoSalida();
        String numeroSalida=param.getNumeroSalida();
          res.setLeido(false);

      java.util.Date fechaDocumento=null;
      
      try {
        // XYZ LocalitzadorsDocsElectronicsFacade regLocDocElect = LocalitzadorsDocsElectronicsFacadeUtil.getHome().create();
        String sentenciaHql="SELECT FZSNUMEN, FZSANOEN, FZSCAGCO, OFF_CODI, OFF_NOM, FZSFENTR, FAADAGCO, " +
            " FZSFDOCU, FZSFACTU, FZSHORA, FZGCENTI, FZSREMIT, FZGDENT2, FZSENULA, " +
            " FZSCTIPE, FZSCIDI, FZSCIDIO, FZSCENTI, FZSNENTI, FZSCAGGE, FZSPROCE, " +
            " FZSCORGA, FAXDORGT, FZIDTIPE, FZMDIDI, FABDAGGE, FZSCONEN, FZSCONE2, " +
            " FZSNDIS, FZSNLOC, FZSALOC, FZPNCORR,EMAILREMITENT FROM BZSALIDA LEFT JOIN BAGECOM ON FAACAGCO=FZSCAGCO " +
            " LEFT JOIN BZENTID ON FZSCENTI=FZGCENTI AND FZGNENTI=FZSNENTI " +
            " LEFT JOIN BORGANI ON FAXCORGA=FZSCORGA " +
            " LEFT JOIN BZTDOCU ON FZICTIPE=FZSCTIPE " +
            " LEFT JOIN BZIDIOM ON FZSCIDI=FZMCIDI " +
            " LEFT JOIN BZSALOFF ON FZSANOEN=FOSANOEN AND FZSNUMEN=FOSNUMEN AND FZSCAGCO=FOSCAGCO " +
            " LEFT JOIN BZOFIFIS ON FZSCAGCO=FZOCAGCO AND OFF_CODI=OFS_CODI " +
            " LEFT JOIN BAGRUGE ON FZSCTAGG=FABCTAGG AND FZSCAGGE=FABCAGGE " +
            " LEFT JOIN BZAUTOR ON FZHCUSU=? AND FZHCAGCO=FZSCAGCO " +
            //" LEFT JOIN BZAUTOR ON FZHCAGCO=FZSCAGCO " +
            " LEFT JOIN BZNCORR ON FZPCENSA='S' AND FZPCAGCO=FZSCAGCO AND FZPANOEN=FZSANOEN AND FZPNUMEN=FZSNUMEN " +
            " WHERE FZSANOEN=? AND FZSNUMEN=? AND FZSCAGCO=? AND FZHCAUT=?";
        q=session.createSQLQuery(sentenciaHql);
        
        q.addScalar("FZSNUMEN", Hibernate.INTEGER);
              q.addScalar("FZSANOEN", Hibernate.INTEGER);
              q.addScalar("FZSCAGCO", Hibernate.INTEGER);
              q.addScalar("OFF_CODI", Hibernate.INTEGER);
              q.addScalar("OFF_NOM" , Hibernate.STRING);
              q.addScalar("FZSFENTR", Hibernate.INTEGER);
              q.addScalar("FAADAGCO", Hibernate.STRING);
              q.addScalar("FZSFDOCU", Hibernate.INTEGER);
              q.addScalar("FZSFACTU", Hibernate.INTEGER);
              q.addScalar("FZSHORA" , Hibernate.INTEGER);
              q.addScalar("FZGCENTI", Hibernate.STRING);
              q.addScalar("FZSREMIT", Hibernate.STRING);
              q.addScalar("FZGDENT2", Hibernate.STRING);
              q.addScalar("FZSENULA", Hibernate.STRING);
              q.addScalar("FZSCTIPE", Hibernate.STRING);
              q.addScalar("FZSCIDI" , Hibernate.STRING);
              q.addScalar("FZSCIDIO", Hibernate.STRING);
              q.addScalar("FZSCENTI", Hibernate.STRING);
              q.addScalar("FZSNENTI", Hibernate.STRING);
              q.addScalar("FZSCAGGE", Hibernate.STRING);
              q.addScalar("FZSPROCE", Hibernate.STRING);
              q.addScalar("FZSCORGA", Hibernate.INTEGER);
              q.addScalar("FAXDORGT", Hibernate.STRING);
              q.addScalar("FZIDTIPE", Hibernate.STRING);
              q.addScalar("FZMDIDI" , Hibernate.STRING);
              q.addScalar("FABDAGGE", Hibernate.STRING);
              q.addScalar("FZSCONEN", Hibernate.STRING);
              q.addScalar("FZSCONE2", Hibernate.STRING);
              q.addScalar("FZSNDIS" , Hibernate.STRING);
              q.addScalar("FZSNLOC" , Hibernate.INTEGER);
              q.addScalar("FZSALOC" , Hibernate.INTEGER);
              q.addScalar("FZPNCORR", Hibernate.STRING);
              q.addScalar("EMAILREMITENT", Hibernate.STRING);
              
        q.setString(0,usuario.toUpperCase());
        q.setInteger(1,Integer.valueOf(anoSalida));
        q.setInteger(2,Integer.valueOf(numeroSalida));
        q.setInteger(3,Integer.valueOf(oficina));
        q.setString(4,"CS");
        rs=q.scroll();
        if (rs.next()) {
          /* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
                Date fechaSystem=new Date();
                //DateFormat hhmmss=new SimpleDateFormat("HHmmss");
                  
                  String ss=sss.format(fechaSystem);
                  //DateFormat aaaammdd=new SimpleDateFormat("yyyyMMdd");
                  //int fzsfsis=Integer.parseInt(aaaammdd.format(fechaSystem));
                  if (ss.length()>2) {
                      ss=ss.substring(0,2);
                  }

                  /*
                  int horamili=Helper.obtenerHoraSistemaHHmmssSS();
                  
          logLopdBZSALIDA("SELECT", (usuario.toUpperCase().length()>10) ? usuario.toUpperCase().substring(0,10) : usuario.toUpperCase()
              , fzsfsis, horamili, rs.getInteger(0), rs.getInteger(1), rs.getInteger(2));
          */
          res.setLeido(true); 
          res.setAnoSalida(String.valueOf(rs.getInteger(1)));
          res.setNumeroSalida(String.valueOf(rs.getInteger(0)));
          res.setoficina(String.valueOf(rs.getInteger(2)));
          res.setoficinafisica(String.valueOf(rs.getInteger(3)));
          
          /* Aquí hem d'anar a la taula d'oficines fisiques. */
          String textoOficinaFisica=null;
          textoOficinaFisica=rs.getString(4);
          
          if (rs.getInteger(3)==null) {
            res.setoficinafisica("0");

                    String sentenciaHqlOfiFis="SELECT OFF_NOM FROM BZOFIFIS WHERE FZOCAGCO=? AND OFF_CODI=0 ";
                  qHist=session.createSQLQuery(sentenciaHqlOfiFis);
                  qHist.addScalar("OFF_NOM", Hibernate.STRING);
                  qHist.setInteger(0,Integer.valueOf(res.getOficina()));
                  rsHist=qHist.scroll();
                  if (rsHist.next()) {
              textoOficinaFisica=rsHist.getString(0);
                  }
              if (rsHist != null)
                rsHist.close();

          }
          if (textoOficinaFisica==null) {
            textoOficinaFisica=" ";
          }

          res.setDescripcionOficinaFisica(textoOficinaFisica);

          String fechaSalid=String.valueOf(rs.getInteger(5));
          try {
            fechaDocumento=yyyymmdd.parse(fechaSalid);
            res.setdatasalida(ddmmyyyy.format(fechaDocumento));
          } catch (Exception e) {
            res.setdatasalida(fechaSalid);
          }
          
          /* Aquí hem d'anar a l'històric d'oficines. */
          String textoOficina=null;
          String sentenciaHqlHistOfi="SELECT FHADAGCO FROM BHAGECO WHERE FHACAGCO=? AND FHAFALTA<=? " +
              "AND ( (FHAFBAJA>= ? AND FHAFBAJA !=0) OR FHAFBAJA = 0)";
          qHist=session.createSQLQuery(sentenciaHqlHistOfi);
          qHist.addScalar("FHADAGCO", Hibernate.STRING);
          qHist.setInteger(0,Integer.valueOf(res.getOficina()));
          qHist.setInteger(1,Integer.valueOf(fechaSalid));
          qHist.setInteger(2,Integer.valueOf(fechaSalid));
          rsHist=qHist.scroll();
          if (rsHist.next()) {
            /* Hem trobat un històric de l'oficina sol·licitada, hem de mostrar-ne el descriptiu. */
            textoOficina=rsHist.getString(0);
          } else {
            textoOficina=rs.getString(6);
            if (textoOficina==null) {
              textoOficina=" ";
            }
          }
          //  Tancam el preparedstatement i resultset de l'històric
          if (rsHist != null)
            rsHist.close();
          
          res.setDescripcionOficina(textoOficina);
          String fechaDocu=String.valueOf(rs.getInteger(7));
          try {
            fechaDocumento=yyyymmdd.parse(fechaDocu);
            res.setdata(ddmmyyyy.format(fechaDocumento));
          } catch (Exception e) {
            res.setdata(fechaDocu);
          }
          
          String fechaVisado=String.valueOf(rs.getInteger(8));
          try {
            fechaDocumento=yyyymmdd.parse(fechaVisado);
            res.setDataVisado(ddmmyyyy.format(fechaDocumento));
          } catch (Exception e) {
            res.setDataVisado(fechaVisado);
          }

          res.sethora(String.valueOf(rs.getInteger(9)));
          res.setDescripcionDestinatario((rs.getString(10)==null)?rs.getString(11):rs.getString(12));
          res.setRegistroAnulado(rs.getString(13));
          res.settipo(( rs.getString(14)==null)?"":rs.getString(14));
          res.settipo(rs.getString(14));
          res.setidioma(rs.getString(15));
          res.setidioex(rs.getString(16));
          res.setentidad1(Helper.convierteEntidad(rs.getString(17),session));
          res.setEntidad1Grabada(rs.getString(17));
          res.setentidad2(rs.getString(18));
          res.setaltres(rs.getString(11));
          res.setbalears(rs.getString(19));
          res.setfora((rs.getString(20)==null)?null:rs.getString(20).trim());
          res.setremitent(String.valueOf(rs.getInteger(21)));
          
          /* Aquí hem d'anar a l'històric d'organismes  */
          String sentenciaHqlHistOrga="SELECT FHXDORGT FROM BHORGAN WHERE FHXCORGA=? AND FHXFALTA<=? " +
              "AND ( (FHXFBAJA>= ? AND FHXFBAJA !=0) OR FHXFBAJA = 0)";
          qHist=session.createSQLQuery(sentenciaHqlHistOrga);
          qHist.addScalar("FHXDORGT", Hibernate.STRING);
          qHist.setInteger(0,Integer.valueOf(res.getRemitent()));
          qHist.setInteger(1,Integer.valueOf(fechaSalid));
          qHist.setInteger(2,Integer.valueOf(fechaSalid));
          rsHist=qHist.scroll();
          if (rsHist.next()) {
            /* Hem trobat un històric de l'oficina sol·licitada, hem de mostrar-ne el descriptiu. */
            res.setDescripcionOrganismoRemitente(rsHist.getString(0));
          } else {
            res.setDescripcionOrganismoRemitente(rs.getString(22));
            if (res.getDescripcionOrganismoRemitente()==null) {
              res.setDescripcionOrganismoRemitente(" ");
            }
          }
          //  Tancam el preparedstatement i resultset de l'històric
          if (rsHist != null)
            rsHist.close();
        
          res.setDescripcionDocumento((rs.getString(23)==null)?"":rs.getString(23));
          res.setDescripcionIdiomaDocumento(rs.getString(24));
          res.setDestinoGeografico((rs.getString(25)==null)?rs.getString(20):rs.getString(25));
          if (rs.getString(16).equals("1")) {
            res.setIdiomaExtracto("CASTELLA");
            res.setcomentario(((rs.getString(26)==null)?null:rs.getString(26).trim()));;
          } else {
            res.setIdiomaExtracto("CATALA");
            res.setcomentario(((rs.getString(27)==null)?null:rs.getString(27).trim()));
          }

          res.setdisquet((rs.getString(28).equals("0"))?"":rs.getString(28));
          res.setentrada1(String.valueOf(rs.getInteger(29)));
          res.setentrada2(String.valueOf(rs.getInteger(30)));
          res.setCorreo(rs.getString(31));
                  res.setEmailRemitent((rs.getString(32)!=null)?rs.getString(32).trim():null);
                  // XYZ
                  //res.setLocalitzadorsDocs(regLocDocElect.LeerListaLocalizadoresDocSalida(Integer.parseInt(res.getAnoSalida()),Integer.parseInt(res.getNumeroSalida()),Integer.parseInt(res.getOficina())));   
        }
      } catch (Exception e) {
        log.error("LEER: Error inesperat: "+e.getMessage() );
        
      } finally {        
        if (rs!=null) rs.close();
        // XYZ close(session);
      }
      return res;
    }

   
      
       
       
       public static long migrarSalidas(EntityManager em, Session session, String minDate,
           String maxDate, String usuario, long idCounter, long entitatID, final int minOfi,
           final int maxOfi, PrintStream ps, int paginacio) throws Exception {
         ParametrosListadoRegistrosSalida parametros = new ParametrosListadoRegistrosSalida();
         
         
         //DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
         parametros.setfechaDesde(minDate);
         parametros.setfechaHasta(maxDate);
         
         
         parametros.setoficinaDesde(String.valueOf(minOfi));
         parametros.setoficinaHasta(String.valueOf(maxOfi));
                  
         long timeLOPD = 0;
         long timeLOPDModif = 0;
         long start = System.currentTimeMillis();
         
         long lastStart = start;
         
         final int sizePaginaE = paginacio;
         int paginaE = 1;
         List<SalidaId> list;
         int countE = 0;
         int countLOPD = 0;
         int countLOPDModif = 0;
         
         ps.println();
         ps.println("-- ========== REGISTRES SORTIDA & LOPD SORTIDA ============");
         ps.println();
         
         
         System.out.println();
         System.out.println(" -------------- SORTIDES --------------- ");
         System.out.println();
         
         
         Long anoentsal,codididoc, codofifis,codorgdesemi;
         Long  numdisquet, numentsal, prodesgeobal,  identidad;
         int codidiext;
         boolean anulado, tregistro;
         String denoficina,denofifis,descdoc,desididoc,desorgdesemi, tipodoc,desremdes;
         String mailremitente,extracto,desidiext,numcorreo,otros,prodesgeo,prodesgeofue;
         Date fechadoc,fechareg,fechavis;
         

         final long totalSalidas = recuperarTotalSalidasNuevo(em,
             parametros);
         
         
         do {
         


      
           //System.out.println("MAX  = " + sizePagina);
           //System.out.println("SIZE = " + list.size());
           //System.out.println(" --------------SORTIDES PAGINA  " + paginaE );
           list = recuperarSalidasNuevo(em, parametros, sizePaginaE, paginaE);
                     
           
           //if (true) break;
           //String tmp;
           for (SalidaId registroSelect : list) {

             
             
             //System.out.println("--  " + (countE+1) + ".-  " + lastEntry);
             
             Long id;
             id = idCounter++;

             // Registre Entrada
             Long numero,ano, codoficina;
             {
               
               ParametrosRegistroSalida parametro = new ParametrosRegistroSalida();
               
               parametro.setNumeroSalida(String.valueOf(registroSelect.getNumero()));
               parametro.setAnoSalida(String.valueOf(registroSelect.getAnyo()));
               parametro.fijaUsuario(usuario);
               parametro.setoficina(String.valueOf(registroSelect.getOficina()));
             

             ParametrosRegistroSalida registro = leerSalida(session,parametro);
             
  
  

             
             ano = Long.parseLong(registro.getAnoSalida());
             anoentsal = Long.parseLong(registro.getEntrada2());
             String nulo = registro.getRegistroAnulado(); 
             anulado =  (nulo == null || nulo.trim().length() == 0)? false : true;

             codididoc = parseLong(registro.getIdioma());
             if (registro.getIdioex()== null || registro.getIdioex().trim().length() == 0) {
               String lastEntry = registroSelect.getNumero() + "/" + registroSelect.getAnyo();
               throw new Exception(lastEntry + ": registro.getIdioex()== null or empty");
             }
             
             codidiext = parseLong(registro.getIdioex()).intValue(); // TODO canviar per SENCER
             codoficina = parseLong(registro.getOficina());
             codofifis = parseLong(registro.getOficinafisica());
             codorgdesemi = parseLong(registro.getRemitent());
             denoficina = registro.getDescripcionOficina();
             denofifis = registro.getDescripcionOficinaFisica();
             descdoc = registro.getDescripcionDocumento();
             desididoc = registro.getDescripcionIdiomaDocumento();
             desorgdesemi = registro.getDescripcionOrganismoRemitente();
             desremdes = registro.getDescripcionDestinatario();
             mailremitente = registro.getEmailRemitent();
             extracto = registro.getComentario();
             fechadoc =  parseDate(registro.getData());
             fechareg =  parseDateTime(registro.getDataSalida(), registro.getHora());
             fechavis = parseDate(registro.getDataVisado());
             
             desidiext = registro.getIdiomaExtracto();
             
             numero = parseLong(registro.getNumeroSalida());
             
             numcorreo = registro.getCorreo();
             numdisquet = parseLong(registro.getDisquet());
             
             
             numentsal = parseLong(registro.getEntrada1());
             
             otros = registro.getAltres();
             
             prodesgeo = registro.getDestinoGeografico();
             
             prodesgeobal = parseLong(registro.getBalears());
               
             prodesgeofue = registro.getFora();
             
             tipodoc = registro.getTipo();
             
             tregistro = TIPOREGISTRO_SALIDA;
             
             
             identidad = entitatID;
             
             
             
             // TODO falta generar  SQL
             String sql =
             "INSERT INTO rwe_registro_migrado("
             + "id, ano, anoentsal, anulado, codididoc,"
             + "codidiext, codoficina, codofifis, codorgdesemi, denoficina,"
             + "denofifis, descdoc, desididoc, desorgdesemi, desremdes,"
             + "mailremitente, extracto, fechadoc, fechareg, fechavis,"
             + "desidiext, numero, numcorreo, numdisquet, numentsal,"
             + "otros, prodesgeo, prodesgeobal, prodesgeofue, tipodoc,"
             + "tregistro,  identidad)"
             + "VALUES (" + id + ", " + ano + ", " +anoentsal + ", " + printSqlBoolean(anulado) + ", " 
             +codididoc + ", " +codidiext + ", " +codoficina + ", " +codofifis + ", "
             +codorgdesemi + ", " + printSqlString(denoficina) + ", " +printSqlString(denofifis) 
             + ", " +printSqlString(descdoc) + ", "
             +printSqlString(desididoc) + ", " +printSqlString(desorgdesemi) 
             + ", " +printSqlString(desremdes) + ", " +printSqlString(mailremitente) + ", "
             +printSqlString(extracto) + ", " + printSqlDate(fechadoc) + ", " + printSqlDate(fechareg) 
             + ", " + printSqlDate(fechavis) + ", " +printSqlString(desidiext)
             + ", " +numero + ", " +printSqlString(numcorreo) + ", " +numdisquet + ", " +numentsal + ", " 
             +printSqlString(otros) + ", " +printSqlString(prodesgeo) + ", " +prodesgeobal 
             + ", " +printSqlString(prodesgeofue) + ", " 
             + printSqlString(tipodoc) + ", " + printSqlBoolean(tregistro) + ", " +identidad + ");";
             
             
             ps.println();
             ps.println(sql);
             
             }
             
             
             // LOPD Registres Entrada
             {
               long startL = System.currentTimeMillis();
               //final int sizePaginaL = 10;
               //int paginaL = 1;
               //int countL = 0;
               List<LogSalidaLopdId> listLOPD;
               //do {
                 Query query = em.createQuery("select lopd.id from LogSalidaLopd as lopd "
                     + " where lopd.id.numero = " + numero 
                     + " AND lopd.id.anyo = " + ano
                     + " AND lopd.id.oficina = " + codoficina
                     + " order by id.fecha, id.hora");
      
                 //query.setFirstResult((sizePaginaL * (paginaL-1)));
                 //query.setMaxResults(sizePaginaL);
                 
                  listLOPD =query.getResultList();
                 // System.out.println(" ----------- LOPD size " + listLOPD.size());
                 countLOPD = countLOPD + listLOPD.size();
                 for (LogSalidaLopdId logSalidaLopd : listLOPD) {
                   
                   Date fecha = parseDateTime(logSalidaLopd.getFecha(), logSalidaLopd.getHora());
                   long idLopd = idCounter++;
                   String sql = "INSERT INTO rwe_registrolopd_migrado("
                  + "id, fecha, tipoacceso, usuario, regmig)"
                  + "VALUES (" +idLopd + ", " + printSqlDate(fecha)+ ", '" + logSalidaLopd.getTipoAcceso() + "', '" 
                  + logSalidaLopd.getUsuario() + "', " + id + ");";
                   
                   ps.println(sql);
      
                 }
               //  paginaL ++;
               //} while (listLOPD.size() == sizePaginaL);
               
               long endL = System.currentTimeMillis();
               
               timeLOPD = timeLOPD + (endL - startL);
             }
             
             
             
             // LODP Modificats de Registres d'Entrada
             {
               
               long startL = System.currentTimeMillis();
               //final int sizePaginaL = 10;
               //int paginaL = 1;
               //int countL = 0;
               List<LogModificacionLopdId> listLOPD;
               //do {
                 Query query = em.createQuery("select lopd.id from LogModificacionLopd as lopd "
                     + " where lopd.id.numero = " + numero 
                     + " AND lopd.id.anyo = " + ano
                     + " AND lopd.id.oficina = " + codoficina
                     + " AND lopd.id.codigoEntradaSalida = 'S'" // SALIDA
                     + " order by id.fecha, id.hora");
      
                 //query.setFirstResult((sizePaginaL * (paginaL-1)));
                 //query.setMaxResults(sizePaginaL);
                 
                  listLOPD =query.getResultList();
                 // System.out.println(" ----------- LOPD size " + listLOPD.size());
                 countLOPDModif = countLOPDModif + listLOPD.size();
                 for (LogModificacionLopdId logEntradaLopdModif : listLOPD) {
                   
                   Date fecha = parseDateTime(logEntradaLopdModif.getFecha(), logEntradaLopdModif.getHora());
                   Date fechaMod = parseDateTime(logEntradaLopdModif.getFechaModificacion(), logEntradaLopdModif.getHoraModificacion());
                   long idLopd = idCounter++;

                   String sql = "INSERT INTO rwe_modificacionlopd_migrado("
                  + "id, fecha, fechamod, tipoacceso, usuario, regmig)"
                  + "VALUES (" +idLopd + ", " + printSqlDate(fecha) + ", " + printSqlDate(fechaMod)+ ", '" + logEntradaLopdModif.getTipoAcceso() + "', '" 
                  + logEntradaLopdModif.getUsuario() + "', " + id + ");";
                   
                   ps.println(sql);
      
                 }
               //  paginaL ++;
               //} while (listLOPD.size() == sizePaginaL);
               
               long endL = System.currentTimeMillis();
               
               timeLOPDModif = timeLOPDModif + (endL - startL);
             }

             //System.out.println("Llegides " + count + " LogLopdEntrada en " + formatElapsedTime(end - start));

             //registroSeleccionado.print();
             //break;
             countE ++;

           }
           paginaE ++;
           
           long now = System.currentTimeMillis();
           
           System.out.println("Migracio en proces: Procesades " + countE 
               + " / " + totalSalidas + " Sortides en " + formatElapsedTime(now - lastStart));
           lastStart =now;
         } while(list.size() == sizePaginaE);

         
         
         final long totalTime = System.currentTimeMillis() - start;
         final long entradesTime = totalTime - timeLOPD;
         
         System.out.println();
         System.out.println("========== FINAL SORTIDES en " + formatElapsedTime(totalTime) + ": ");
         System.out.println("     + Llegides " + countE + " sortides en " + formatElapsedTime(entradesTime));
         System.out.println("     + Llegides " + countLOPD + " LOPD en " + formatElapsedTime(timeLOPD));
         System.out.println();
         System.out.println();
         
         
         
         
         return idCounter;
       }
   
   
}
