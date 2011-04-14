package es.caib.regweb.logic.helper;

import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Query;
import org.hibernate.ScrollMode;
import org.hibernate.Session;
import org.hibernate.ScrollableResults;
import javax.ejb.SessionContext;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.hibernate.HibernateException;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import java.net.URL;

import java.util.Hashtable;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


import org.apache.log4j.Logger;

public class Helper {

	private static Logger log = Logger.getLogger(Helper.class);

    public static int recogerNumeroEntrada(Session session, int anyo, String idOficina, Hashtable errores) throws HibernateException {
        return recogerNumero(session, anyo, idOficina, "E", errores);
    }
    public static int recogerNumeroSalida(Session session, int anyo, String idOficina, Hashtable errores) throws HibernateException {
        return recogerNumero(session, anyo, idOficina, "S", errores);
    }
    public static int recogerNumeroOficio(Session session, int anyo, String idOficina, Hashtable errores) throws HibernateException {
        return recogerNumero(session, anyo, idOficina, "O", errores);
    }

    /**
     * Recoge una PK para una entrada en el registro.
     *
     * @param fzaanoe Año del registro
     * @param oficina Código de la oficina que solicita el número
     * @param tipo "E" para entradas o "S" para Salidas
     */
    private static int recogerNumero(Session session, int fzaanoe, String oficina, String tipo, Hashtable errores) throws HibernateException {
        int numero=0;
        ScrollableResults rs = null;

        Query q = null;
        Query q2 = null;

        String consulta = "select numero from Contador where id.anyo=? and id.codigoEntradaSalida=? and id.oficina=? "; // CHANGED quitado "with rs" para compatibilidad
        String update = "update Contador set numero=numero+1 where id.anyo=? and id.codigoEntradaSalida=? and id.oficina=?";
        try {
            /* Actualizamos el numero de entrada */
            q=session.createQuery(update);
            q.setInteger(0,fzaanoe);
            q.setString(1,tipo);
            q.setInteger(2,Integer.parseInt(oficina));
            int num = q.executeUpdate();			

            q2=session.createQuery(consulta);
            q2.setInteger(0,fzaanoe);
            q2.setString(1,tipo);
            q2.setInteger(2,Integer.parseInt(oficina));
            rs=q2.scroll();
            if (rs.next()) {
                numero=rs.getInteger(0); 
            } else {
                errores.put(".","No s'ha inicialitzat el comptador d'entrades/sortides per a l'oficina - any "+oficina+"-"+fzaanoe);
                throw new HibernateException("No s'ha inicialitzat el comptador d'entrades/sortides per a l'oficina - any "+oficina+"-"+fzaanoe);
            }
            rs.close();
            session.flush();

        } catch (Exception ex) {
            ex.printStackTrace();
            errores.put("z","No \u00e9s possible gravar el registre ara, torni a intentar-ho ");
            throw new HibernateException("No \u00e9s possible gravar el registre ara, torni a intentar-ho m\u00e9s tard ", ex);
        }
        return numero;
    }

    public static void actualizaDisqueteEntrada(Session session, int disquete, String oficina, String anoEntrada, Hashtable errores) throws HibernateException {
        actualizaDisquete(session, disquete, oficina, "E", anoEntrada, errores);
    }
    public static void actualizaDisqueteSalida(Session session, int disquete, String oficina, String anoSalida, Hashtable errores) throws HibernateException {
        actualizaDisquete(session, disquete, oficina, "S", anoSalida, errores);
    }
    /**
     * Actualiza el numero de Disquete. Si el numero a actualizar es mayor que el leido.
     *
     * @param session Connection
     * @param disquete int con el numero del disquete a actualizar
     * @param oficina Código de la oficina que solicita el número
     * @param tipo    "E"=Entrada   "S"=Salida
     * @param anyo  año a actualizar
     * @param errores   Hastable con los errores
     */
    public static void actualizaDisquete(Session session, int disquete, String oficina, String tipo, String anyo, Hashtable errores) throws HibernateException {
        ScrollableResults rs = null;
        SQLQuery ps = null;
        SQLQuery ts = null;
        try {
            String sentenciaSql="SELECT FZLNDIS FROM BZDISQU WHERE FZLCENSA=? AND FZLCAGCO=? AND FZLAENSA=? "; // CHANGED quitado "with rs" para compatibilidad sql
            ps=session.createSQLQuery(sentenciaSql);
            ps.addScalar("FZLNDIS", Hibernate.INTEGER);
            ps.setString(0, tipo);
            ps.setString(1, oficina);
            ps.setString(2, anyo);
            rs=ps.scroll(ScrollMode.SCROLL_INSENSITIVE);
            int numeroDisquete=0;
            if (rs.next()) {
                numeroDisquete=rs.getInteger(0);
                /* Actualizamos el numero de disquete si es mayor al leido */
                if (disquete>numeroDisquete) {
                    sentenciaSql="UPDATE BZDISQU SET FZLNDIS=? WHERE FZLCENSA=? AND FZLCAGCO=? AND FZLAENSA=?";
                    ts=session.createSQLQuery(sentenciaSql);
                    ts.setInteger(0,disquete);
                    ts.setString(1,tipo);
                    ts.setInteger(2,Integer.parseInt(oficina));
                    ts.setInteger(3,Integer.parseInt(anyo));
                    ts.executeUpdate();                   
                }
            } else if (disquete>0) {
                sentenciaSql="INSERT INTO BZDISQU (FZLCENSA, FZLCAGCO, FZLAENSA, FZLNDIS)" +
                " VALUES (?, ?, ?, ?)";
                ts=session.createSQLQuery(sentenciaSql);
                ts.setString(0,tipo);
                ts.setInteger(1,Integer.parseInt(oficina));
                ts.setInteger(2,Integer.parseInt(anyo));
                ts.setInteger(3,disquete);
                ts.executeUpdate();               
            }

        } catch (Exception e) {
            errores.put("","No és possible gravar el registre ara, torni a intentar-ho ");
            throw new HibernateException("No és possible gravar el registre ara, torni a intentar-ho ",e);
        } finally {
            try{
               // if (rs != null)
               //     rs.close();
                session.flush();
            } catch (Exception e) {
                //log.error("Error al tancar accés a BD", e);
            }
        }
    }


    public static String convierteEntidad(String entidadCastellano, Session session) {
        ScrollableResults rs = null;
        SQLQuery ps = null;
        String entidadCatalan=null;

        try {
            String sentenciaSql="SELECT FZGCENT2 FROM BZENTID WHERE FZGCENTI=? AND FZGFBAJA=0";
            ps=session.createSQLQuery(sentenciaSql);
            ps.addScalar("FZGCENT2", Hibernate.STRING);
            ps.setString(0,entidadCastellano);
            rs=ps.scroll();
            if (rs.next()) {
                entidadCatalan=rs.getString(0);
            } else {
                entidadCatalan="";
            }
            if (rs!=null) {
                rs.close();
            }
        } catch (Exception e) {
            log.error("ERROR: convierteEntidad",e);
            entidadCatalan="";
        }
        return entidadCatalan;
    }
    
    public static boolean estaPdteVisado(String tipo, String oficina, String ano, String numero) {
        Session session = getSession();
        ScrollableResults rs = null;
        SQLQuery ps = null;
        boolean pdteVisado=false;

        log.debug("estaPdteVisado ("+tipo+","+oficina+","+ano+","+numero+")");
        try {
            String sentenciaSql="SELECT FZJFVISA FROM BZMODIF WHERE (FZJIEXTR=' ' OR FZJIREMI=' ' OR FZJIEXTR='' OR FZJIREMI='') AND FZJFVISA=0 " +
            "AND FZJCENSA=? AND FZJCAGCO=? AND FZJANOEN=? AND FZJNUMEN=?";
            ps=session.createSQLQuery(sentenciaSql);
            ps.addScalar("FZJFVISA", Hibernate.INTEGER);
            ps.setString(0,tipo);
            ps.setInteger(1,Integer.parseInt(oficina));
            ps.setInteger(2,Integer.parseInt(ano));
            ps.setInteger(3,Integer.parseInt(numero));
            rs=ps.scroll();
            if (rs.next()) {
                pdteVisado=true;
            } else {
                pdteVisado=false;
            }
        } catch (Exception e) {
            System.out.println("ERROR: estaPdteVisado");
            System.out.println(e.getMessage());
            e.printStackTrace();
            pdteVisado=false;
        } finally {
            close(session);
        }
        return pdteVisado;       
    }
    
    
    static int rellenaBytes(String cadena, byte[] buf, int offset, int longitud) {
        if (cadena==null) {
            cadena="";
        }

        StringBuffer nuevaCadena=new StringBuffer();
        int longitudCadena=cadena.length();
        for (int n=0;n<longitudCadena;n++) {
            nuevaCadena.append(cadena.substring(n,n+1));
            if (cadena.substring(n,n+1).equals("'")) {
                nuevaCadena.append("'");
                longitud++;
            }
        }
        cadena=nuevaCadena.toString();
        int cont=0;
        while (cont<longitud && cont<cadena.length()) {
            buf[offset+cont]=(byte)cadena.charAt(cont);
            cont++;
        }
        while (cont<longitud) {
            buf[offset+cont]= (byte)' ';
            cont++;
        }
        return longitud;
    }

    static int rellenaNumeros(int numero, byte[] buf, int offset, int longitud) {
        String numerChar=Integer.toString(numero);
        while (numerChar.length()<longitud) {
            //numerChar="0"+numerChar;
            numerChar=new StringBuffer().append("0").append(numerChar).toString();
        }

        int cont=0;
        while (cont<longitud && cont<numerChar.length()) {
            buf[offset+cont]=(byte)numerChar.charAt(cont);
            cont++;
        }
        return longitud;
    }

    static int convierteStringFechaAIntFecha(String strFecha) throws java.text.ParseException{
    	int intFecha = 0;
    	DateFormat dateF= new SimpleDateFormat("dd/MM/yyyy");
    	DateFormat date1=new SimpleDateFormat("yyyyMMdd");
        //Calendar cal=Calendar.getInstance();
        Date fechaAux = null;
        
        fechaAux = dateF.parse(strFecha);
    	//cal.setTime(fechaAux);
    	intFecha=Integer.parseInt(date1.format(fechaAux));

    	return intFecha;
    }


    /* metodos hibernate */
    
    private static final SessionFactory sessionFactory;

    static {
        try {            
            /*
            * Build a SessionFactory object from session-factory configuration 
            * defined in the hibernate.cfg.xml file. In this file we register 
            * the JDBC connection information, connection pool, the hibernate 
            * dialect that we used and the mapping to our hbm.xml file for each 
            * POJO (Plain Old Java Object).
            * 
            */
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            URL url = cl.getResource("hibernate.cfg.xml");
            AnnotationConfiguration cfg = new AnnotationConfiguration().configure(url);
            
            sessionFactory = cfg.buildSessionFactory();
        } catch (Throwable e) {
            System.err.println("Error in creating SessionFactory object."
                + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    /*
    * A static method for other application to get SessionFactory object 
    * initialized in this helper class.
    * 
    */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    protected static Session getSession() {
       try {
           return getSessionFactory().openSession();
       } catch (HibernateException e) {
          throw e;
       }
    }

    protected static void close(Session sessio) {
       if (sessio != null && sessio.isOpen()) {
          try {
             if (sessio.isDirty()) {
                log.warn("Se ha cerrado la sessi\u00f3n sin hacer un flush.");
                sessio.flush();
             }
             sessio.close();
          } catch (HibernateException e) {
             throw e;
          }
       }
    }

	
}
