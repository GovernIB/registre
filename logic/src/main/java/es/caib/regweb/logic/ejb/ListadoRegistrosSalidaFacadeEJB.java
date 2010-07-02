package es.caib.regweb.logic.ejb;

import java.util.*;
import java.text.*;

import javax.ejb.*;

import es.caib.regweb.logic.helper.ParametrosListadoRegistrosSalida;

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
 * SessionBean per a llistat de registres de sortida
 *
 * @ejb.bean
 *  name="logic/ListadoRegistrosSalidaFacade"
 *  jndi-name="es.caib.regweb.logic.ListadoRegistrosSalidaFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 */
public abstract class ListadoRegistrosSalidaFacadeEJB extends HibernateEJB {
	
	private Logger log = Logger.getLogger(this.getClass());
    
	private DateFormat dateF= new SimpleDateFormat("dd/MM/yyyy");
	private Date fechaTest1=null;
	private Date fechaTest2=null; 
	
	
	/**
	 * @param fecha
     */
    private boolean validarFecha(ParametrosListadoRegistrosSalida parametros, String fecha1, String fecha2) {
        Hashtable errores = parametros.getErrores();
        boolean error=false;
        try {
            dateF.setLenient(false);
            fechaTest1 = dateF.parse(fecha1);
        } catch (Exception ex) {
            error=true;
            errores.put("2","Data inici no \u00e9s valida, (dd/mm/aaaa)");
        }
        try {
            dateF.setLenient(false);
            fechaTest2 = dateF.parse(fecha2);
        } catch (Exception ex) {
            error=true;
            errores.put("3","Data final no \u00e9s valida, (dd/mm/aaaa)");
        }
        if (!error) {
            if (fechaTest2.before(fechaTest1)) {
                errores.put("4","Data final \u00e9s inferior a data inici");
            }
        }
        return !error;
    }
	
	
    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public boolean validarBusqueda(ParametrosListadoRegistrosSalida parametros) {
		String oficinaDesde=parametros.getOficinaDesde();
		String oficinaHasta=parametros.getOficinaHasta();
		String fechaDesde=parametros.getFechaDesde();
		String fechaHasta=parametros.getFechaHasta();
		String oficinaFisica=parametros.getOficinaFisica();
		Hashtable errores=parametros.getErrores();
		String any=parametros.getAny();
		String extracto=parametros.getExtracto();
		String tipo=parametros.getTipo();
		String remitente=parametros.getRemitente();
		String destinatario=parametros.getDestinatario();
		String codiRemitent=parametros.getCodiRemitent();
		String destino=parametros.getDestino();

		boolean validado=false;
		errores.clear();
		// Validamos que la oficina desde sea < o = a oficina Hasta
		if (oficinaDesde==null || oficinaHasta==null || oficinaDesde.equals("") || oficinaHasta.equals("")) {
			errores.put("1","L\u00edmits incorrectes per a oficines");
		} else {
			int oficinaInicio=Integer.parseInt(oficinaDesde);
			int oficinaFin=Integer.parseInt(oficinaHasta);
			if (oficinaFin<oficinaInicio) {
				errores.put("1","L\u00edmits incorrectes per a oficines");
			}
		}
		// Validamos fecha inicio
		validarFecha(parametros, fechaDesde, fechaHasta);
		// Hem de validar que el codi d'organisme destinatari ficat sigui correcte, ja sigui dins BORGANI com BHORGANI
		if (codiRemitent==null)
			codiRemitent="";
		if (!codiRemitent.equals("")) {
			Session session = getSession();
			
			ScrollableResults rs = null;
			ScrollableResults rsHist = null;
			SQLQuery q = null;
			SQLQuery qHist = null;
			try {
				int codiRemitente = Integer.parseInt(codiRemitent);
				String sentenciaSql="SELECT FAXCORGA FROM BORGANI WHERE FAXCORGA=? ";
				q=session.createSQLQuery(sentenciaSql); //, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				q.setInteger(0,Integer.valueOf(codiRemitent));
				rs=q.scroll(ScrollMode.SCROLL_INSENSITIVE);
				
				if (!rs.next()) {
					sentenciaSql="SELECT FHXCORGA FROM BHORGAN WHERE FHXCORGA=? ";
					q=session.createSQLQuery(sentenciaSql); //, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					q.setInteger(0,Integer.valueOf(codiRemitent));
					rs=q.scroll(ScrollMode.SCROLL_INSENSITIVE);
					if (!rs.next())
						errores.put("1","Codi de destinatari inexistent");
					//log.error("ERROR: Codi "+CodiRemitent+" no existeix!");
				}
			} catch ( NumberFormatException ne){
				errores.put("2","Codi de destinatari no num\u00e8ric"); 	
			} catch (Exception e) {
				log.error("Error: "+e.getMessage());
				e.printStackTrace();
			} finally {
    			if (rs!=null) rs.close();
    			close(session);
			}
		}
		if (errores.size()==0) {
			validado=true;
		} else {
			validado=false;
		}
		return validado;
	}
	
    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector recuperar(ParametrosListadoRegistrosSalida parametros, String usuario, int sizePagina, int pagina) {
		Session session = getSession();
		ScrollableResults rs = null;
		ScrollableResults rsHist = null;
		SQLQuery q = null;
		SQLQuery qHist = null;
		
		String oficinaDesde=parametros.getOficinaDesde();
		String oficinaHasta=parametros.getOficinaHasta();
		String fechaDesde=parametros.getFechaDesde();
		String fechaHasta=parametros.getFechaHasta();
		String oficinaFisica=parametros.getOficinaFisica();
		Hashtable errores=parametros.getErrores();
		String any=parametros.getAny();
		String extracto=parametros.getExtracto();
		String tipo=parametros.getTipo();
		String remitente=parametros.getRemitente();
		String destinatario=parametros.getDestinatario();
		String codiRemitent=parametros.getCodiRemitent();
		String destino=parametros.getDestino();
		
		usuario=usuario.toUpperCase();
		Vector registrosSalidaVector=new Vector();
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
//				 Point to the last row in resultset.
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
			q.setMaxResults((sizePagina*pagina)+1);
			rs=q.scroll(ScrollMode.SCROLL_INSENSITIVE);
			
			if (pagina>1) {
				rs.next();
				rs.scroll((sizePagina*(pagina-1))-1);
			}
			
			while (rs.next()) {
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
            	DateFormat hhmmss=new SimpleDateFormat("HHmmss");
                DateFormat sss=new SimpleDateFormat("S");
                String ss=sss.format(fechaSystem);
                DateFormat aaaammdd=new SimpleDateFormat("yyyyMMdd");
                int fzafsis=Integer.parseInt(aaaammdd.format(fechaSystem));
                if (ss.length()>2) {
                    ss=ss.substring(0,2);
                }
                int fzahsis=Integer.parseInt(hhmmss.format(fechaSystem)+ss);
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
                int horamili=Integer.parseInt(hhmmss.format(fechaSystem)+Stringsss);
                logLopdBZSALIDA("SELECT", usuario, fzafsis, horamili, rs.getInteger(1), rs.getInteger(0), rs.getInteger(2));

				registrosSalidaVector.addElement(registroSalida);
			}
		} catch (Exception e) {
			log.error("Error: "+e.getMessage());
			e.printStackTrace();
		} finally {
			if (rs!=null) rs.close();
			close(session);
		}
		return registrosSalidaVector;
	}
	
    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector recuperarRegistrosOficina(String usuario, int maxRegistros, int oficina, String any, String accion, int numero)
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

			String texto="";
			if (numero>0) {
				if (accion.equals("R")) {
					texto=" AND FZSNUMEN<? ";
				} else {
					texto=" AND FZSNUMEN>? ";
				}
			} else {
				texto="";
			}
			
			String sentenciaSql="SELECT FZSANOEN, FZSNUMEN, FZSCAGCO, FAADAGCO, FZSFDOCU, FZSFENTR, " +
            " FZGCENTI, FZSREMIT, FZGDENT2, FAXDORGR, FZIDTIPE, FZMDIDI, FZSENULA " +
            " FROM BZSALIDA LEFT JOIN BAGECOM ON FAACAGCO=FZSCAGCO " +
			" LEFT JOIN BZENTID ON FZSCENTI=FZGCENTI AND FZGNENTI=FZSNENTI " +
			" LEFT JOIN BORGANI ON FAXCORGA=FZSCORGA " +
			" LEFT JOIN BZTDOCU ON FZICTIPE=FZSCTIPE " +
			" LEFT JOIN BZIDIOM ON FZSCIDI=FZMCIDI " +
			" LEFT JOIN BAGRUGE ON FZSCTAGG=FABCTAGG AND FZSCAGGE=FABCAGGE " +
			" LEFT JOIN BZAUTOR ON FZHCUSU=? AND FZHCAGCO=FZSCAGCO " +
			" WHERE FZHCAUT=? AND FZSCAGCO=? " +
			(!any.trim().equals("") ? " AND FZSANOEN>=? " : "") +
			texto +
			"ORDER BY FZSCAGCO, FZSANOEN, FZSNUMEN " +
			(accion.equals("R") ? "DESC" : "ASC") ;
			q=session.createSQLQuery(sentenciaSql);
			q.addScalar("FZSANOEN", Hibernate.INTEGER);
            q.addScalar("FZSNUMEN", Hibernate.INTEGER);
            q.addScalar("FZSCAGCO", Hibernate.INTEGER);
            q.addScalar("FAADAGCO", Hibernate.STRING);
            q.addScalar("FZSFDOCU", Hibernate.INTEGER);
            q.addScalar("FZSFENTR", Hibernate.INTEGER);
            q.addScalar("FZGCENTI", Hibernate.STRING);
            q.addScalar("FZSREMIT", Hibernate.STRING);
            q.addScalar("FZGDENT2", Hibernate.STRING);
            q.addScalar("FAXDORGR", Hibernate.STRING);
            q.addScalar("FZIDTIPE", Hibernate.STRING);
            q.addScalar("FZMDIDI" , Hibernate.STRING);
            q.addScalar("FZSENULA", Hibernate.STRING);
            
			// Numero maximo de registros a devolver
			q.setMaxResults(maxRegistros);
			
			q.setString(0,usuario);
			q.setString(1,"AS");
			q.setInteger(2,oficina);
			int contadorPS=3;
			if (!any.trim().equals("")) {
				q.setInteger(contadorPS++,Integer.parseInt(any));
			}
			if (numero>0) {
				q.setInteger(contadorPS,numero);
			}
			
			rs=q.scroll();
			while (rs.next()) {
				RegistroSeleccionado registro=new RegistroSeleccionado();
				
				registro.setAnoEntrada(String.valueOf(rs.getInteger(0)));
				registro.setNumeroEntrada(String.valueOf(rs.getInteger(1)));
				registro.setOficina(String.valueOf(rs.getInteger(2)));
				String textoOficina=rs.getString(3);
				if (textoOficina==null) {textoOficina=" ";}
				registro.setDescripcionOficina(textoOficina);
				String fechaDocu=String.valueOf(rs.getInteger(4));
				try {
					fechaDocumento=yyyymmdd.parse(fechaDocu);
					registro.setData(ddmmyyyy.format(fechaDocumento));
				} catch (Exception e) {
					registro.setData(fechaDocu);
				}
				String fechaES=String.valueOf(rs.getInteger(5));
				try {
					fechaEESS=yyyymmdd.parse(fechaES);
					registro.setFechaES(ddmmyyyy.format(fechaEESS));
				} catch (Exception e) {
					registro.setFechaES(fechaES);
				}
				
				if (rs.getString(6)==null) {
					registro.setDescripcionRemitente(rs.getString(7));
				} else {
					registro.setDescripcionRemitente(rs.getString(8));
				}
				registro.setDescripcionOrganismoDestinatario(rs.getString(9));
				registro.setDescripcionDocumento(rs.getString(10));
				registro.setDescripcionIdiomaDocumento(rs.getString(11));
				registro.setRegistroAnulado(rs.getString(12));
				registrosVector.addElement(registro);
			}
			
		} catch (Exception e) {
			log.error("Error: "+e.getMessage());
			e.printStackTrace();
		} finally {
			if (rs!=null) rs.close();
			close(session);
		}
		
		if (accion.equals("R")) {
			Collections.sort(registrosVector);
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
	 * Emplena la taula de control d'accés complint la llei LOPD per la taula BZSALIDA
	 * @param tipusAcces <code>String</code> tipus d'accés a la taula
	 * @param usuari <code>String</code> codi de l'usuari que fa l'acció.
	 * @param data <code>Intr</code> data d'accés en format numèric (ddmmyyyy)
	 * @param hora <code>Int</code> hora d'accés en format numèric (hhmissmis, hora (2 posicions), minut (2 posicions), segons (2 posicions), milisegons (3 posicions)
	 * @param nombreRegistre <code>Int</code> nombre de registre
	 * @param any <code>Int</code> any del registre
	 * @param oficina <code>Int</code> oficina on s'ha registrat
	 * @author Sebastià Matas Riera (bitel)
	 */
	
	private void logLopdBZSALIDA(String tipusAcces, String usuari, int data, int hora, int nombreRegistre, int any, int oficina ) {
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