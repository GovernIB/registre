

package es.caib.regweb.logic.ejb;

import java.util.*;
import java.text.*;

import javax.ejb.*;

import org.apache.log4j.Logger;

import es.caib.regweb.logic.helper.RegistroSeleccionado;

import es.caib.regweb.model.LogSalidaLopd;
import es.caib.regweb.model.LogSalidaLopdId;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.ScrollableResults;
import org.hibernate.ScrollMode;

/**
 * SessionBean per a llistat d'oficis de sortida
 *
 * @ejb.bean
 *  name="logic/ListadoRegOficiosSalidaFacade"
 *  jndi-name="es.caib.regweb.logic.ListadoRegOficiosSalidaFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 */
public abstract class ListadoRegOficiosSalidaFacadeEJB extends HibernateEJB {
	
	private static final long serialVersionUID = 1L;
	private Logger log = Logger.getLogger(this.getClass());
    

    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector recuperarRegistros(String usuario, String oficina, boolean mostrarRegElec)
	throws java.rmi.RemoteException, Exception {

		Session session = getSession();
		ScrollableResults rs = null;
		SQLQuery q = null;
		
		usuario=usuario.toUpperCase();
		Vector registrosVector=new Vector();
		DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
		DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date fechaDocumento=null;
		java.util.Date fechaEESS=null;
		try {
			String sentenciaSql= "SELECT FZSANOEN, FZSNUMEN, FAACAGCO, FAADAGCO, OFS_CODI, OFF_NOM, " +
				" FZSFDOCU, FZSFENTR, FZGCENTI, FZSREMIT, FZGDENT2, FZSCIDIO, " +
				" FZSCONEN, FZSCONE2, FABDAGGE, FZSPROCE, REM_OFNUM, FAXDORGR, " +
				" FZIDTIPE, FZMDIDI, FZSENULA FROM BZOFREM  " +
				" INNER JOIN BZSALIDA ON REM_SALANY=FZSANOEN AND REM_SALNUM=FZSNUMEN AND REM_SALOFI=FZSCAGCO " +
				" LEFT JOIN BAGECOM ON FAACAGCO=FZSCAGCO  " +
				" LEFT JOIN BZSALOFF ON FOSCAGCO=FZSCAGCO AND FOSANOEN=FZSANOEN AND FOSNUMEN=FZSNUMEN " +
				" LEFT JOIN BZOFIFIS ON FZOCAGCO=FOSCAGCO AND OFF_CODI=OFS_CODI " +
				" LEFT JOIN BZENTID ON FZSCENTI=FZGCENTI AND FZGNENTI=FZSNENTI  " +
				" LEFT JOIN BORGANI ON FAXCORGA=FZSCORGA  " +
				" LEFT JOIN BZTDOCU ON FZICTIPE=FZSCTIPE  " +
				" LEFT JOIN BZIDIOM ON FZSCIDI=FZMCIDI  " +
				" LEFT JOIN BAGRUGE ON FZSCTAGG=FABCTAGG AND FZSCAGGE=FABCAGGE  " +
				" WHERE REM_SALANY>2008 " +
				"  AND FZSNENTI IN (SELECT FZHCAGCO FROM BZAUTOR WHERE FZHCUSU=? AND FZHCAUT=? " + (oficina==null || oficina.equals("00")?"":" AND FZHCAGCO=? ") + ")" +
				 //" AND REM_SALDES='N' " +
				 " AND (REM_ENTDES IS NULL OR REM_ENTDES='N' OR REM_ENTDES='') " +
				 " AND (REM_NULA IS NULL OR REM_NULA='N' OR REM_NULA='') " +
				 " AND REM_ENTFEC=0 " +
				 " AND NOT FZSENULA='S' " +
				 ((mostrarRegElec)?"  AND EXISTS (SELECT * FROM BZDOCLOC WHERE LOC_ANY = REM_SALANY AND LOC_NUMREG= REM_SALNUM AND LOC_OFI=REM_SALOFI AND LOC_TIPUS='S' ) ":"") +
				 " ORDER BY FZSCAGCO, FZSANOEN, FZSNUMEN ";

 			q=session.createSQLQuery(sentenciaSql); //, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            q.addScalar("FZSANOEN" , Hibernate.INTEGER);
            q.addScalar("FZSNUMEN" , Hibernate.INTEGER);
            q.addScalar("FAACAGCO" , Hibernate.INTEGER);
            q.addScalar("FAADAGCO" , Hibernate.STRING);
            q.addScalar("OFS_CODI" , Hibernate.STRING);
            q.addScalar("OFF_NOM"  , Hibernate.STRING);
            q.addScalar("FZSFDOCU" , Hibernate.INTEGER);
            q.addScalar("FZSFENTR" , Hibernate.INTEGER);
            q.addScalar("FZGCENTI" , Hibernate.STRING);
            q.addScalar("FZSREMIT" , Hibernate.STRING);
            q.addScalar("FZGDENT2" , Hibernate.STRING);
            q.addScalar("FZSCIDIO" , Hibernate.STRING);
            q.addScalar("FZSCONEN" , Hibernate.STRING);
            q.addScalar("FZSCONE2" , Hibernate.STRING);
            q.addScalar("FABDAGGE" , Hibernate.STRING);
            q.addScalar("FZSPROCE" , Hibernate.STRING);
            q.addScalar("REM_OFNUM", Hibernate.STRING);
            q.addScalar("FAXDORGR" , Hibernate.STRING);
            q.addScalar("FZIDTIPE" , Hibernate.STRING);
            q.addScalar("FZMDIDI"  , Hibernate.STRING);
            q.addScalar("FZSENULA" , Hibernate.STRING);


			q.setString(0,usuario);
			// Modificado por V.Herrera 18/09/2009. CAmbio del permiso CS (Consulta Salida) a AE(Alta entradas)
			q.setString(1,"AE");
			if (oficina!=null && !oficina.equals("00")) {
    			q.setInteger(2, Integer.parseInt(oficina));
			}
			
			rs=q.scroll(ScrollMode.SCROLL_INSENSITIVE);
			while (rs.next()) {
				RegistroSeleccionado registro=new RegistroSeleccionado();
				
				registro.setAnoEntrada(String.valueOf(rs.getInteger(0)));
				registro.setNumeroEntrada(String.valueOf(rs.getInteger(1)));
				registro.setOficina(String.valueOf(rs.getInteger(2)));
				String textoOficina=rs.getString(3);
				if (textoOficina==null) {textoOficina=" ";}
				registro.setDescripcionOficina(textoOficina);
				registro.setOficinaFisica(rs.getString(4));
				registro.setDescripcionOficinaFisica(rs.getString(5));
				String fechaDocu=String.valueOf(rs.getInteger(6));
				
				try {
					fechaDocumento=yyyymmdd.parse(fechaDocu);
					registro.setData(ddmmyyyy.format(fechaDocumento));
				} catch (Exception e) {
					registro.setData(fechaDocu);
				}
				String fechaES=String.valueOf(rs.getInteger(7));
				try {
					fechaEESS=yyyymmdd.parse(fechaES);
					registro.setFechaES(ddmmyyyy.format(fechaEESS));
				} catch (Exception e) {
					registro.setFechaES(fechaES);
				}
				
				if (rs.getString(8)==null) {
					registro.setDescripcionRemitente(rs.getString(9));
				} else {
					registro.setDescripcionRemitente(rs.getString(10));
				}
				if (rs.getString(11).equals("1")) {
					registro.setExtracto(rs.getString(12));
				} else {
					registro.setExtracto(rs.getString(13));
				}
				if (rs.getString(14)==null) {
					registro.setDescripcionGeografico(rs.getString(15));
				} else {
					registro.setDescripcionGeografico(rs.getString(14));
				}
				registro.setOficio(rs.getString(16));
				registro.setDescripcionOrganismoDestinatario(rs.getString(17));
				registro.setDescripcionDocumento(rs.getString(18));
				registro.setDescripcionIdiomaDocumento(rs.getString(19));
				registro.setRegistroAnulado(rs.getString(20));
				registrosVector.addElement(registro);
			}
		} catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
			e.printStackTrace();
		} finally {
			if (rs!=null) rs.close();
			close(session);
		}
		
		
		return registrosVector;
	}
	
	 /**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }
	
	
	/**
	 * Emplena la taula de control d'accés complint la llei LOPD per la taula Salida
	 * @param tipusAcces <code>String</code> tipus d'accés a la taula
	 * @param usuari <code>String</code> codi de l'usuari que fa l'acció.
	 * @param data <code>Intr</code> data d'accés en format numèric (ddmmyyyy)
	 * @param hora <code>Int</code> hora d'accés en format numèric (hhmissmis, hora (2 posicions), minut (2 posicions), segons (2 posicions), milisegons (3 posicions)
	 * @param nombreRegistre <code>Int</code> nombre de registre
	 * @param any <code>Int</code> any del registre
	 * @param oficina <code>Int</code> oficina on s'ha registrat
	 * @author Sebastià Matas Riera (bitel)
	 */
	
    private void logLopdSalida(String tipusAcces, String usuari, int data, int hora, int nombreRegistre, int any, int oficina ) {
        Session session = getSession();
	    try {
            LogSalidaLopd log = new LogSalidaLopd(new LogSalidaLopdId( tipusAcces, usuari, Integer.valueOf(data), Integer.valueOf(hora), 
                                     Integer.valueOf(nombreRegistre), Integer.valueOf(any), Integer.valueOf(oficina)));

            session.save(log);
            session.flush();

        // } catch (Exception e) {
        //          log.error("ERROR: S'ha produ\357t un error a logLopdSalida: " + e.getMessage());
        // }
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
        
    }
}