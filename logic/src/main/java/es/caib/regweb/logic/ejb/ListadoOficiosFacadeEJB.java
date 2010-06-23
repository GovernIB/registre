

package es.caib.regweb.logic.ejb;

import java.util.*;
import java.text.*;

import org.apache.log4j.Logger;
import es.caib.regweb.logic.helper.RegistroSeleccionado;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.ScrollableResults;
import org.hibernate.ScrollMode;


import javax.ejb.*;

/**
 * SessionBean per a llistats d'oficis de remissió
 *
 * @ejb.bean
 *  name="logic/ListadoOficiosFacade"
 *  jndi-name="es.caib.regweb.logic.ListadoOficiosFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 */
public abstract class ListadoOficiosFacadeEJB extends HibernateEJB {
	
	private Logger log = Logger.getLogger(this.getClass());

	/** Creates a new instance of Valores */
	public ListadoOficiosFacadeEJB() {
	}

    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector recuperarRegistros(String usuario, String oficina, String oficinaFisica, String anyo)
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
			String sentenciaSql= "SELECT FZSANOEN, FZSNUMEN, FAACAGCO, FAADAGCO, OFS_CODI, " +
                " OFF_NOM, FZSFDOCU, FZSFENTR, FZGCENTI, FZSREMIT, " +
                " FZGDENT2, FZSCIDIO, FZSCONEN, FZSCONE2, FABDAGGE, " +
                " FZSPROCE, REM_OFNUM, FAXDORGR, FZIDTIPE, FZMDIDI, REM_NULA " +
                " FROM BZOFREM  " +
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
				"  AND FZSCAGCO IN (SELECT FZHCAGCO FROM BZAUTOR WHERE FZHCUSU=? AND FZHCAUT=? " + (oficina==null || oficina.equals("00")?"":" AND FZHCAGCO=? ") + ")" +
				(anyo==null || anyo.equals("")?"":" AND REM_OFANY = ? ") +
				(oficinaFisica==null || oficinaFisica.equals("")?"":" AND OFS_CODI = ? ") +
				 " ORDER BY FZSCAGCO, FZSANOEN, FZSNUMEN ";
			q=session.createSQLQuery(sentenciaSql); //, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            q.addScalar("FZSANOEN", Hibernate.INTEGER);
            q.addScalar("FZSNUMEN", Hibernate.INTEGER);
            q.addScalar("FAACAGCO", Hibernate.INTEGER);
            q.addScalar("FAADAGCO", Hibernate.STRING);
            q.addScalar("OFS_CODI", Hibernate.INTEGER);
            q.addScalar("OFF_NOM", Hibernate.STRING);
            q.addScalar("FZSFDOCU", Hibernate.INTEGER);
            q.addScalar("FZSFENTR", Hibernate.INTEGER);
            q.addScalar("FZGCENTI", Hibernate.STRING);
            q.addScalar("FZSREMIT", Hibernate.STRING);
            q.addScalar("FZGDENT2", Hibernate.STRING);
            q.addScalar("FZSCIDIO", Hibernate.STRING);
            q.addScalar("FZSCONEN", Hibernate.STRING);
            q.addScalar("FZSCONE2", Hibernate.STRING);
            q.addScalar("FABDAGGE", Hibernate.STRING);
            q.addScalar("FZSPROCE", Hibernate.STRING);
            q.addScalar("REM_OFNUM", Hibernate.STRING);
            q.addScalar("FAXDORGR", Hibernate.STRING);
            q.addScalar("FZIDTIPE", Hibernate.STRING);
            q.addScalar("FZMDIDI", Hibernate.STRING);
            q.addScalar("REM_NULA", Hibernate.STRING);
            
			q.setString(0,usuario);
			q.setString(1,"CE");
			int contador = 2;
			if (oficina!=null && !oficina.equals("00")) {
    			q.setInteger(contador++, Integer.parseInt(oficina));
			}
			if (anyo!=null && !anyo.equals("")) {
    			q.setInteger(contador++, Integer.parseInt(anyo));
			}
			if (oficinaFisica!=null && !oficinaFisica.equals("")) {
    			q.setInteger(contador++, Integer.parseInt(oficinaFisica));
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
				registro.setOficinaFisica(String.valueOf(rs.getInteger(4)));
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
	
	
	
}