package es.caib.regweb3.logic.helper;


import org.hibernate.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;


public class Helper {

    private static Logger log = LoggerFactory.getLogger(Helper.class);

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
     * @param tipo    "E" para entradas o "S" para Salidas
     */
    private static int recogerNumero(Session session, int fzaanoe, String oficina, String tipo, Hashtable errores) throws HibernateException {
        int numero = 0;
        ScrollableResults rs = null;

        Query q = null;
        Query q2 = null;

        String consulta = "select numero from Contador where id.anyo=? and id.codigoEntradaSalida=? and id.oficina=? "; // CHANGED quitado "with rs" para compatibilidad
        String update = "update Contador set numero=numero+1 where id.anyo=? and id.codigoEntradaSalida=? and id.oficina=?";
        try {
            /* Actualizamos el numero de entrada */
            q = session.createQuery(update);
            q.setInteger(0, fzaanoe);
            q.setString(1, tipo);
            q.setInteger(2, Integer.parseInt(oficina));
            int num = q.executeUpdate();

            q2 = session.createQuery(consulta);
            q2.setInteger(0, fzaanoe);
            q2.setString(1, tipo);
            q2.setInteger(2, Integer.parseInt(oficina));
            rs = q2.scroll();
            if (rs.next()) {
                numero = rs.getInteger(0);
            } else {
                errores.put(".", "No s'ha inicialitzat el comptador d'entrades/sortides per a l'oficina - any " + oficina + "-" + fzaanoe);
                throw new HibernateException("No s'ha inicialitzat el comptador d'entrades/sortides per a l'oficina - any " + oficina + "-" + fzaanoe);
            }
            rs.close();
            session.flush();

        } catch (Exception ex) {
            ex.printStackTrace();
            errores.put("z", "No \u00e9s possible gravar el registre ara, torni a intentar-ho ");
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
     * @param session  Connection
     * @param disquete int con el numero del disquete a actualizar
     * @param oficina  Código de la oficina que solicita el número
     * @param tipo     "E"=Entrada   "S"=Salida
     * @param anyo     año a actualizar
     * @param errores  Hastable con los errores
     */
    public static void actualizaDisquete(Session session, int disquete, String oficina, String tipo, String anyo, Hashtable errores) throws HibernateException {
        ScrollableResults rs = null;
        SQLQuery ps = null;
        SQLQuery ts = null;
        try {
            String sentenciaSql = "SELECT FZLNDIS FROM BZDISQU WHERE FZLCENSA=? AND FZLCAGCO=? AND FZLAENSA=? "; // CHANGED quitado "with rs" para compatibilidad sql
            ps = session.createSQLQuery(sentenciaSql);
            ps.addScalar("FZLNDIS", Hibernate.INTEGER);
            ps.setString(0, tipo);
            ps.setString(1, oficina);
            ps.setString(2, anyo);
            rs = ps.scroll(ScrollMode.SCROLL_INSENSITIVE);
            int numeroDisquete = 0;
            if (rs.next()) {
                numeroDisquete = rs.getInteger(0);
				/* Actualizamos el numero de disquete si es mayor al leido */
                if (disquete > numeroDisquete) {
                    sentenciaSql = "UPDATE BZDISQU SET FZLNDIS=? WHERE FZLCENSA=? AND FZLCAGCO=? AND FZLAENSA=?";
                    ts = session.createSQLQuery(sentenciaSql);
                    ts.setInteger(0, disquete);
                    ts.setString(1, tipo);
                    ts.setInteger(2, Integer.parseInt(oficina));
                    ts.setInteger(3, Integer.parseInt(anyo));
                    ts.executeUpdate();
                }
            } else if (disquete > 0) {
                sentenciaSql = "INSERT INTO BZDISQU (FZLCENSA, FZLCAGCO, FZLAENSA, FZLNDIS)" +
                        " VALUES (?, ?, ?, ?)";
                ts = session.createSQLQuery(sentenciaSql);
                ts.setString(0, tipo);
                ts.setInteger(1, Integer.parseInt(oficina));
                ts.setInteger(2, Integer.parseInt(anyo));
                ts.setInteger(3, disquete);
                ts.executeUpdate();
            }

        } catch (Exception e) {
            errores.put("", "No és possible gravar el registre ara, torni a intentar-ho ");
            throw new HibernateException("No és possible gravar el registre ara, torni a intentar-ho ", e);
        } finally {
            try {
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
        String entidadCatalan = null;

        try {
            String sentenciaSql = "SELECT FZGCENT2 FROM BZENTID WHERE FZGCENTI=? AND FZGFBAJA=0";
            ps = session.createSQLQuery(sentenciaSql);
            ps.addScalar("FZGCENT2", Hibernate.STRING);
            ps.setString(0, entidadCastellano);
            rs = ps.scroll();
            if (rs.next()) {
                entidadCatalan = rs.getString(0);
            } else {
                entidadCatalan = "";
            }
            if (rs != null) {
                rs.close();
            }
        } catch (Exception e) {
            log.error("ERROR: convierteEntidad", e);
            entidadCatalan = "";
        }
        return entidadCatalan;
    }

    public static boolean estaPdteVisado(Session session, String tipo, String oficina, String ano, String numero) {

        ScrollableResults rs = null;
        SQLQuery ps = null;
        boolean pdteVisado = false;

        log.debug("estaPdteVisado (" + tipo + "," + oficina + "," + ano + "," + numero + ")");
        try {
            String sentenciaSql = "SELECT FZJFVISA FROM BZMODIF WHERE (FZJIEXTR=' ' OR FZJIREMI=' ' OR FZJIEXTR='' OR FZJIREMI='') AND FZJFVISA=0 " +
                    "AND FZJCENSA=? AND FZJCAGCO=? AND FZJANOEN=? AND FZJNUMEN=?";
            ps = session.createSQLQuery(sentenciaSql);
            ps.addScalar("FZJFVISA", Hibernate.INTEGER);
            ps.setString(0, tipo);
            ps.setInteger(1, Integer.parseInt(oficina));
            ps.setInteger(2, Integer.parseInt(ano));
            ps.setInteger(3, Integer.parseInt(numero));
            rs = ps.scroll();
            if (rs.next()) {
                pdteVisado = true;
            } else {
                pdteVisado = false;
            }
        } catch (Exception e) {
            System.out.println("ERROR: estaPdteVisado");
            System.out.println(e.getMessage());
            e.printStackTrace();
            pdteVisado = false;
        } finally {
            // XYZ
            //close(session);
        }
        return pdteVisado;
    }


    static int rellenaBytes(String cadena, byte[] buf, int offset, int longitud) {
        if (cadena == null) {
            cadena = "";
        }

        StringBuffer nuevaCadena = new StringBuffer();
        int longitudCadena = cadena.length();
        for (int n = 0; n < longitudCadena; n++) {
            nuevaCadena.append(cadena.substring(n, n + 1));
            if (cadena.substring(n, n + 1).equals("'")) {
                nuevaCadena.append("'");
                longitud++;
            }
        }
        cadena = nuevaCadena.toString();
        int cont = 0;
        while (cont < longitud && cont < cadena.length()) {
            buf[offset + cont] = (byte) cadena.charAt(cont);
            cont++;
        }
        while (cont < longitud) {
            buf[offset + cont] = (byte) ' ';
            cont++;
        }
        return longitud;
    }

    static int rellenaNumeros(int numero, byte[] buf, int offset, int longitud) {
        String numerChar = Integer.toString(numero);
        while (numerChar.length() < longitud) {
            //numerChar="0"+numerChar;
            numerChar = new StringBuffer().append("0").append(numerChar).toString();
        }

        int cont = 0;
        while (cont < longitud && cont < numerChar.length()) {
            buf[offset + cont] = (byte) numerChar.charAt(cont);
            cont++;
        }
        return longitud;
    }

    /**
     * Convierte una fecha de tipo string y formato "dd/MM/yyyy" a fecha de tipo Integer y formato "yyyyMMdd"
     *
     * @param strFecha
     * @return
     * @throws java.text.ParseException
     */
    static public int convierteStringFechaAIntFecha(String strFecha) throws java.text.ParseException {
        DateFormat dateF = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat date1 = new SimpleDateFormat("yyyyMMdd");
        Date fechaAux = null;

        log.debug("Fecha a convertir: " + strFecha);
        fechaAux = dateF.parse(strFecha);
        return Integer.parseInt(date1.format(fechaAux));
    }

    /**
     * @param strHora
     * @return
     * @throws java.text.ParseException
     */
    static public int convierteStringHoraAIntHora(String strHora) throws java.text.ParseException {
        Calendar cal = Calendar.getInstance();
        Date horaTest = null;
        DateFormat horaF = new SimpleDateFormat("HH:mm");
        horaTest = horaF.parse(strHora);
        cal.setTime(horaTest);
        DateFormat hhmm = new SimpleDateFormat("HHmm");
        return Integer.parseInt(hhmm.format(horaTest));
    }

    /**
     * Añadido MARILEN
     *
     * @param strHora
     * @return
     * @throws java.text.ParseException
     */
    static public int convierteStringHoraAIntHoraSeg(String strHora) throws java.text.ParseException {
        Calendar cal = Calendar.getInstance();
        Date horaTest = null;
        DateFormat horaF = new SimpleDateFormat("HH:mm:ss");
        horaTest = horaF.parse(strHora);
        cal.setTime(horaTest);
        DateFormat hhmm = new SimpleDateFormat("HHmmss");
        return Integer.parseInt(hhmm.format(horaTest));
    }


    public static final DateFormat yyyymmdd = new SimpleDateFormat("yyyyMMdd");
    public static final DateFormat ddmmyyyy = new SimpleDateFormat("dd/MM/yyyy");


    /**
     * Convierte una fecha de tipo string y formato "yyyyMMdd" a fecha de tipo String y formato "dd/MM/yyyy"
     *
     * @param strFecha
     * @return
     * @throws java.text.ParseException
     */
    static public String convierteyyyymmddFechaAddmmyyyyFecha(String strFecha) throws java.text.ParseException {

        java.util.Date fechaDocumento = null;

        log.debug("Fecha a convertir: " + strFecha);
        fechaDocumento = yyyymmdd.parse(strFecha);
        return ddmmyyyy.format(fechaDocumento);
    }

    /**
     * Obtiene un int con el año de una fecha de tipo string y formato "dd/MM/yyyy"
     *
     * @param strFecha
     * @return
     * @throws java.text.ParseException
     */
    static public int obtenerAnyoDeFechadd_MM_YYY(String strFecha) throws java.text.ParseException {
        //TODO Pensar optimización
        Calendar cal = Calendar.getInstance();
        DateFormat dateF = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaTest = dateF.parse(strFecha);
        cal.setTime(fechaTest);
        return cal.get(Calendar.YEAR);
    }

    /**
     * Obtiene un int con el año de una fecha de tipo string y formato "yyyymmdd"
     *
     * @param strFecha
     * @return
     * @throws java.text.ParseException
     */
    static public int obtenerAnyoDeFechayyyyMMdd(String strFecha) throws java.text.ParseException {
        //TODO Pensar optimización
        Calendar cal = Calendar.getInstance();
        DateFormat dateF = new SimpleDateFormat("yyyyMMdd");
        Date fechaTest = dateF.parse(strFecha);
        cal.setTime(fechaTest);
        return cal.get(Calendar.YEAR);
    }

    /**
     * @return devuelve fecha del sistema en formato yyyyMMdd
     */
    static public int obtenerFechaSistemayyyyMMdd() {
        Date fechaSystem = new Date();
        DateFormat aaaammdd = new SimpleDateFormat("yyyyMMdd");
        return Integer.parseInt(aaaammdd.format(fechaSystem));
    }

    /**
     * @return devuelve hora del sistema en formato HHmmssS
     */
    static public int obtenerHoraSistemaHHmmssS() {
        Date fechaSystem = new Date();
        DateFormat hhmmss = new SimpleDateFormat("HHmmss");
        DateFormat sss = new SimpleDateFormat("S");
        String ss = sss.format(fechaSystem);
        if (ss.length() > 2) {
            ss = ss.substring(0, 2);
        }
        return Integer.parseInt(hhmmss.format(fechaSystem) + ss);
    }

    /**
     * @return devuelve hora del sistema en formato HHmmssS
     */
    static public int obtenerHoraSistemaHHmmssSS() {
        Date fechaSystem = new Date();
        DateFormat hhmmss = new SimpleDateFormat("HHmmss");
        DateFormat sss = new SimpleDateFormat("S");
        String ss = sss.format(fechaSystem);
        switch (ss.length()) {
            //Hem d'emplenar amb 0s.
            case (1):
                ss = "00" + ss;
                break;
            case (2):
                ss = "0" + ss;
                break;
        }
        return Integer.parseInt(hhmmss.format(fechaSystem) + ss);
    }
	/* metodos hibernate */


    /**
     * Convierte una hora en formato "4:00" a "04:00"
     */
    public static String convierteHora_a_HHSS(String hora) {
        String hhmm = "";

        if (hora != null && !hora.equals("") && !hora.equals("0")) {
            if (hora.length() < 4)
                hora = "0" + hora;

            String hh = hora.substring(0, 2);
            String mm = hora.substring(2, 4);
            hhmm = hh + ":" + mm;
        } else {
            hhmm = hora;
        }
        return hhmm;
    }

    /**
     * Añadido Marilen
     * Convierte una hora al formato "04:00:00"
     */
    public static String convierteHora_a_HHmmss(String hora) {

        String hhmmss = "";

        if (hora != null && !hora.equals("") && !hora.equals("0")) {
            if (hora.length() < 6)
                hora = "0" + hora;

            String hh = hora.substring(0, 2);
            String mm = hora.substring(2, 4);
            String ss = hora.substring(4, 6);
            hhmmss = hh + ":" + mm + ":" + ss;
        } else {
            hhmmss = hora;
        }
        return hhmmss;
    }


    /*
     * Devuelve un String con el codigo del registro de entrada asociado a un registro de salida
     * Si no existe devuelve ""
     *
     */
    public static String obtenerCodigoRegistroEntradaAsociado(ParametrosRegistroSalida reg) {
        String entrada1 = reg.getEntrada1().equals("0") ? "" : reg.getEntrada1();
        String entrada2 = reg.getEntrada2().equals("0") ? "" : reg.getEntrada2();
        String barra = " / ";
        if (entrada1.equals("") && entrada2.equals("")) {
            barra = "";
        }
        return entrada1 + barra + entrada2;
    }
}
