

package es.caib.regweb.logic.ejb;

import java.util.*;
import java.text.*;

import javax.ejb.*;

import es.caib.regweb.logic.helper.RegistroSeleccionado;
import org.apache.log4j.Logger;

import es.caib.regweb.model.LogEntradaLopd;
import es.caib.regweb.model.LogEntradaLopdId;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.ScrollableResults;
import org.hibernate.ScrollMode;

/**
 * SessionBean per a llistats d'oficis (entrada)
 *
 * @ejb.bean
 *  name="logic/ListadoRegOficiosEntradaFacade"
 *  jndi-name="es.caib.regweb.logic.ListadoRegOficiosEntradaFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 */
public abstract class ListadoRegOficiosEntradaFacadeEJB extends HibernateEJB {
	
	private Logger log = Logger.getLogger(this.getClass());
    
	
    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector recuperarRegistros(String usuario, String oficina, String oficinaFisica) throws java.rmi.RemoteException, Exception {

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
			String sentenciaSql="SELECT FZAANOEN, FZANUMEN, FZACAGCO, FAADAGCO, OFE_CODI, " +
            " OFF_NOM, FZAFDOCU, FZAFENTR, FZGCENTI, FZAREMIT, " +
            " FZGDENT2, FZACIDIO, FZACONEN, FZACONE2, FABDAGGE, " +
            " FZAPROCE, FAXDORGR, FZIDTIPE, FZMDIDI, FZAENULA " +
            " FROM BZENTRA LEFT JOIN BAGECOM ON FAACAGCO=FZACAGCO " +
			" LEFT JOIN BZENTID ON FZACENTI=FZGCENTI AND FZGNENTI=FZANENTI " +
			" LEFT JOIN BORGANI ON FAXCORGA=FZACORGA " +
			" LEFT JOIN BZTDOCU ON FZICTIPE=FZACTIPE " +
			" LEFT JOIN BZIDIOM ON FZACIDI=FZMCIDI " +
			" LEFT JOIN BAGRUGE ON FZACTAGG=FABCTAGG AND FZACAGGE=FABCAGGE " +
			" LEFT JOIN BZOFIRE ON FZACAGCO=FZFCAGCO AND FZACORGA=FZFCORGA " +
			" LEFT JOIN BZENTOFF ON FZACAGCO=FOECAGCO AND FZAANOEN=FOEANOEN AND FZANUMEN=FOENUMEN " +
			" LEFT JOIN BZOFIFIS ON FOECAGCO=FZOCAGCO AND OFE_CODI=OFF_CODI " +
			" LEFT JOIN BZOFRENT ON FZAANOEN=REN_ENTANY AND FZANUMEN=REN_ENTNUM AND FZACAGCO=REN_ENTOFI " +
			" WHERE FZAFENTR>20090913 AND FZACAGCO IN (SELECT FZHCAGCO FROM BZAUTOR WHERE FZHCUSU=? AND FZHCAUT=? " + (oficina==null || oficina.equals("00")?"":(oficinaFisica==null || oficinaFisica.equals("")?" AND FZHCAGCO=? ":" AND FZHCAGCO=? AND OFE_CODI=? ")) + ")" +
			" AND FZFCAGCO IS NULL" +
			" AND FZFCORGA IS NULL" +
			" AND REN_ENTANY IS NULL" +
			" AND REN_ENTNUM IS NULL" +
			" AND REN_ENTOFI IS NULL" +
			" AND NOT FLOOR(FZACORGA/100)=FZACAGCO " +
			" AND NOT FZAENULA='S' " +
			" ORDER BY FZACAGCO, FZAANOEN, FZANUMEN";


			q=session.createSQLQuery(sentenciaSql); //, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            q.addScalar("FZAANOEN",Hibernate.INTEGER);
            q.addScalar("FZANUMEN",Hibernate.INTEGER);
            q.addScalar("FZACAGCO",Hibernate.INTEGER);
            q.addScalar("FAADAGCO",Hibernate.STRING);
            q.addScalar("OFE_CODI",Hibernate.INTEGER);
            q.addScalar("OFF_NOM",Hibernate.STRING);
            q.addScalar("FZAFDOCU",Hibernate.INTEGER);
            q.addScalar("FZAFENTR",Hibernate.INTEGER);
            q.addScalar("FZGCENTI",Hibernate.STRING);
            q.addScalar("FZAREMIT",Hibernate.STRING);
            q.addScalar("FZGDENT2",Hibernate.STRING);
            q.addScalar("FZACIDIO",Hibernate.STRING);
            q.addScalar("FZACONEN",Hibernate.STRING);
            q.addScalar("FZACONE2",Hibernate.STRING);
            q.addScalar("FABDAGGE",Hibernate.STRING);
            q.addScalar("FZAPROCE",Hibernate.STRING);
            q.addScalar("FAXDORGR",Hibernate.STRING);
            q.addScalar("FZIDTIPE",Hibernate.STRING);
            q.addScalar("FZMDIDI",Hibernate.STRING);
            q.addScalar("FZAENULA",Hibernate.STRING);
			q.setString(0,usuario);
			q.setString(1,"AS");
			if (oficina!=null && !oficina.equals("00")) {
    			q.setInteger(2, Integer.parseInt(oficina));
    			if (oficinaFisica!=null && !oficinaFisica.equals("")) {
        			q.setInteger(3, Integer.parseInt(oficinaFisica));
    			}
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
                String textoOficinaFisica=rs.getString(5);
                if (textoOficinaFisica==null) {textoOficinaFisica=" ";}
                registro.setDescripcionOficinaFisica(textoOficinaFisica);
				
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

				registro.setDescripcionOrganismoDestinatario(rs.getString(16));
				registro.setDescripcionDocumento(rs.getString(17));
				registro.setDescripcionIdiomaDocumento(rs.getString(18));
				registro.setRegistroAnulado(rs.getString(19));
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
     * Emplena la taula de control d'accés complint la llei LOPD per la taula Entrada
     * @param tipusAcces <code>String</code> tipus d'accés a la taula
     * @param usuari <code>String</code> codi de l'usuari que fa l'acció.
     * @param data <code>Intr</code> data d'accés en format numèric (ddmmyyyy)
     * @param hora <code>Int</code> hora d'accés en format numèric (hhmissmis, hora (2 posicions), minut (2 posicions), segons (2 posicions), milisegons (3 posicions)
     * @param nombreRegistre <code>Int</code> nombre de registre
     * @param any <code>Int</code> any del registre
     * @param oficina <code>Int</code> oficina on s'ha registrat
     * @author Sebastià Matas Riera (bitel)
     */

	private void logLopdEntrada(String tipusAcces, String usuari, int data, int hora, int nombreRegistre, int any, int oficina ) {
        Session session = getSession();
		try {
            LogEntradaLopd log = new LogEntradaLopd(new LogEntradaLopdId( tipusAcces, usuari, Integer.valueOf(data), Integer.valueOf(hora), 
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