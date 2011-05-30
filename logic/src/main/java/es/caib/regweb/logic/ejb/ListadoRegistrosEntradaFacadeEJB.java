

package es.caib.regweb.logic.ejb;

import java.util.*;
import java.text.*;

import javax.ejb.*;

import es.caib.regweb.logic.helper.ParametrosListadoRegistrosEntrada;
import es.caib.regweb.logic.helper.RegistroSeleccionado;

import es.caib.regweb.model.LogEntradaLopd;
import es.caib.regweb.model.LogEntradaLopdId;

import org.apache.log4j.Logger;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.ScrollableResults;
import org.hibernate.ScrollMode;

/**
 * SessionBean per a llistat de registres d'entrada 
 *
 * @ejb.bean
 *  name="logic/ListadoRegistrosEntradaFacade"
 *  jndi-name="es.caib.regweb.logic.ListadoRegistrosEntradaFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 */
public abstract class ListadoRegistrosEntradaFacadeEJB extends HibernateEJB {
	
	private Logger log = Logger.getLogger(this.getClass());
    
	private DateFormat dateF= new SimpleDateFormat("dd/MM/yyyy");
	private Date fechaTest1=null;
	private Date fechaTest2=null; 
	
	private int totalRegistres060=0; 
	

	
	/**
	 * @param fecha1
	 * @param fecha2
     */
	private boolean validarFecha(ParametrosListadoRegistrosEntrada parametros, String fecha1, String fecha2) {
		boolean error=false;
		Hashtable errores=parametros.getErrores();
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
    public ParametrosListadoRegistrosEntrada validarBusqueda(ParametrosListadoRegistrosEntrada parametros) {
        
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
         String procedencia=parametros.getProcedencia();
         String destinatario=parametros.getDestinatario();
         String codiDestinatari=parametros.getCodiDestinatari();
         String totalFiles=parametros.getTotalFiles();
         String codiMun060=parametros.getCodiMunicipi060();
         String numeroRegistroSalidaRelacionado=parametros.getNumeroRegistroSalidaRelacionado();
         String anyoRegistroSalidaRelacionado=parametros.getAnyoRegistroSalidaRelacionado();
        
		boolean validado=false;
		errores.clear();
		
		
		// Validamos que la oficina desde sea < o = a oficina Hasta
		if (oficinaDesde==null || oficinaHasta==null || oficinaDesde.equals("") || oficinaHasta.equals("")) {
			errores.put("1","L\u00edmits incorrectes per a oficines. Falten dates.");
		} else {
			int oficinaInicio=Integer.parseInt(oficinaDesde);
			int oficinaFin=Integer.parseInt(oficinaHasta);
			if (oficinaFin<oficinaInicio) {
				errores.put("1","L\u00edmits incorrectes per a oficines");
			}
		}
		// Validamos fecha inicio
		validarFecha(parametros, fechaDesde, fechaHasta);
		
		// Validamos que los datos del registro de salida vinculado al registro de entrada est�n completos
		if ( (numeroRegistroSalidaRelacionado.equals("") && !anyoRegistroSalidaRelacionado.equals("") ) || (!numeroRegistroSalidaRelacionado.equals("") && anyoRegistroSalidaRelacionado.equals("") )){
				errores.put("5","El codi del registre de sortida vinculat no est\u00e0 complet.");
			}
		
		
		// Hem de validar que el codi d'organisme destinatari ficat sigui correcte, ja sigui dins BORGANI com BHORGANI
		//log.debug("Codidestinatari="+CodiDestinatari);
		if (codiDestinatari == null)
			codiDestinatari="";
		if ( !codiDestinatari.equals("") ) {            
			Session session = getSession();
			
			ScrollableResults rs = null;
			ScrollableResults rsHist = null;
			SQLQuery q = null;
			SQLQuery qHist = null;
			try {
				int codigoDestinatari = Integer.parseInt(codiDestinatari);

				String sentenciaHql="SELECT FAXCORGA FROM BORGANI WHERE FAXCORGA=? ";
				q=session.createSQLQuery(sentenciaHql); //, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				q.setInteger(0,Integer.valueOf(codiDestinatari));
				rs=q.scroll(ScrollMode.SCROLL_INSENSITIVE);
				
				if (!rs.next()) {
					sentenciaHql="SELECT FHXCORGA FROM BHORGAN WHERE FHXCORGA=? ";
					q=session.createSQLQuery(sentenciaHql); //, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					q.setInteger(0,Integer.valueOf(codiDestinatari));
					rs=q.scroll(ScrollMode.SCROLL_INSENSITIVE);
					if (!rs.next())
						errores.put("3","Codi de destinatari inexistent");
					//log.error("ERROR: Codi "+CodiDestinatari+" no existeix!");
				}
			} catch ( NumberFormatException ne){
				errores.put("6","Codi de destinatari no num\u00e8ric"); 	
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
        parametros.setValidado(validado);
		return parametros;
	}
	
	
	
    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector recuperar(ParametrosListadoRegistrosEntrada parametros, String usuario, int sizePagina, int pagina) throws java.rmi.RemoteException {
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
        String procedencia=parametros.getProcedencia();
        String destinatario=parametros.getDestinatario();
        String codiDestinatari=parametros.getCodiDestinatari();
        String totalFiles=parametros.getTotalFiles();
        String codiMun060=parametros.getCodiMunicipi060();
        String numeroRegistroSalidaRelacionado=parametros.getNumeroRegistroSalidaRelacionado();
        String anyoRegistroSalidaRelacionado=parametros.getAnyoRegistroSalidaRelacionado();
        
		usuario=usuario.toUpperCase();
		Vector registrosVector=new Vector();
		DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
		DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date fechaDocumento=null;
		java.util.Date fechaEESS=null;        
		String numero_de_registres ;
		
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
			
//			 Point to the last row in resultset.
		      rs.last();
		      // Get the row position which is also the number of rows in the resultset.
		      int rowcount = rs.getRowNumber()+1;
		      // Reposition at the beginning of the ResultSet to take up rs.next() call.
		      rs.beforeFirst();
		      log.debug("Total rows for the query using Scrollable ResultSet: "
		                         +rowcount);
		      parametros.setTotalFiles( String.valueOf(rowcount));
		      this.totalRegistres060 = totalRegistres060;
		      
			}
		    
		    // Numero maximo de registros a devolver
			// Código antiguo e incorrecto
			//q.setMaxResults((sizePagina*pagina)+1);
			
			q.setFirstResult(sizePagina * (pagina-1));
			// TODO: Ver porqué setMaxResults desordena los resultados
			//
			//q.setMaxResults((sizePagina+1));
			
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
				String sentenciaHqlHistOrga="SELECT FHXDORGT FROM BHORGAN WHERE FHXCORGA=? AND FHXFALTA<=? " +
				"AND ( (FHXFBAJA>= ? AND FHXFBAJA !=0) OR FHXFBAJA = 0)";
				qHist=session.createSQLQuery(sentenciaHqlHistOrga);
				qHist.addScalar("FHXDORGT", Hibernate.STRING);
				qHist.setInteger(0,rs.getInteger(13));
				qHist.setInteger(1,Integer.valueOf(fechaES));
				qHist.setInteger(2,Integer.valueOf(fechaES));
				rsHist=qHist.scroll();
				if (rsHist.next()) {
					/* Hem trobat un històric de l'organisme sol·licitat, hem de mostrar-ne el descriptiu. */
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
				
				/* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
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
                logLopdBZENTRA("SELECT", usuario, fzafsis, horamili, rs.getInteger(1), rs.getInteger(0), rs.getInteger(2));
                
                registro.setNumeroDocumentosRegistro060((rs.getString(21)!= null)?Integer.parseInt(rs.getString(21)):0);
                registrosVector.addElement(registro);
			}
			
		} catch (Exception e) {
			log.error("Error: "+e.getMessage());
			e.printStackTrace();
		} finally {
			if (rs!=null) rs.close();
			close(session);
		}
		return registrosVector;
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
					texto=" AND FZANUMEN<? ";
				} else {
					texto=" AND FZANUMEN>? ";
				}
			} else {
				texto="";
			}
			String sentenciaHql="SELECT FZAANOEN, FZANUMEN, FZACAGCO, FAADAGCO, FZAFDOCU, FZAFENTR, " +
            " FZGCENTI, FZAREMIT, FZGDENT2, FAXDORGR, FZIDTIPE, FZMDIDI, FZAENULA " +
            " FROM BZENTRA LEFT JOIN BAGECOM ON FAACAGCO=FZACAGCO " +
			" LEFT JOIN BZENTID ON FZACENTI=FZGCENTI AND FZGNENTI=FZANENTI " +
			" LEFT JOIN BORGANI ON FAXCORGA=FZACORGA " +
			" LEFT JOIN BZTDOCU ON FZICTIPE=FZACTIPE " +
			" LEFT JOIN BZIDIOM ON FZACIDI=FZMCIDI " +
			" LEFT JOIN BAGRUGE ON FZACTAGG=FABCTAGG AND FZACAGGE=FABCAGGE " +
			" LEFT JOIN BZAUTOR ON FZHCUSU=? AND FZHCAGCO=FZACAGCO " +
			" WHERE FZHCAUT=? AND FZACAGCO=? " +
			(!any.trim().equals("") ? " AND FZAANOEN>=? " : "") +
			texto +
			" ORDER BY FZACAGCO, FZAANOEN, FZANUMEN " +
			(accion.equals("R") ? "DESC" : "ASC") ;
			q=session.createSQLQuery(sentenciaHql);
			q.addScalar("FZAANOEN", Hibernate.INTEGER);
            q.addScalar("FZANUMEN", Hibernate.INTEGER);
            q.addScalar("FZACAGCO", Hibernate.INTEGER);
            q.addScalar("FAADAGCO", Hibernate.STRING);
            q.addScalar("FZAFDOCU", Hibernate.INTEGER);
            q.addScalar("FZAFENTR", Hibernate.INTEGER);
            q.addScalar("FZGCENTI", Hibernate.STRING);
            q.addScalar("FZAREMIT", Hibernate.STRING);
            q.addScalar("FZGDENT2", Hibernate.STRING);
            q.addScalar("FAXDORGR", Hibernate.STRING);
            q.addScalar("FZIDTIPE", Hibernate.STRING);
            q.addScalar("FZMDIDI" , Hibernate.STRING);
            q.addScalar("FZAENULA", Hibernate.STRING);
            
			// Numero maximo de registros a devolver
			q.setMaxResults(maxRegistros);
			
			q.setString(0,usuario);
			q.setString(1,"AS");
			// q.setInt(3,Integer.parseInt(oficinaDesde));
			// q.setInt(4,Integer.parseInt(oficinaHasta));
			q.setInteger(2, oficina);
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
     * Emplena la taula de control d'accés complint la llei LOPD per la taula BZENTRA
     * @param tipusAcces <code>String</code> tipus d'accés a la taula
     * @param usuari <code>String</code> codi de l'usuari que fa l'acció.
     * @param data <code>Intr</code> data d'accés en format numèric (ddmmyyyy)
     * @param hora <code>Int</code> hora d'accés en format numèric (hhmissmis, hora (2 posicions), minut (2 posicions), segons (2 posicions), milisegons (3 posicions)
     * @param nombreRegistre <code>Int</code> nombre de registre
     * @param any <code>Int</code> any del registre
     * @param oficina <code>Int</code> oficina on s'ha registrat
     * @author Sebastià Matas Riera (bitel)
     */

	private void logLopdBZENTRA(String tipusAcces, String usuari, int data, int hora, int nombreRegistre, int any, int oficina ) {
        Session session = getSession();
		try {
            LogEntradaLopd log = new LogEntradaLopd(new LogEntradaLopdId( tipusAcces, usuari, Integer.valueOf(data), Integer.valueOf(hora), 
                                        Integer.valueOf(nombreRegistre), Integer.valueOf(any), Integer.valueOf(oficina)));

            session.save(log);
            session.flush();

        // } catch (Exception e) {
        //  log.error("ERROR: S'ha produ\357t un error a logLopdEntrada: " + e.getMessage());
        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }
    }

    /**
	    * @ejb.interface-method
	    * @ejb.permission unchecked="true"
	    */
	public int getTotalRegistres060() {
		return totalRegistres060;
	}

    /**
	    * @ejb.interface-method
	    * @ejb.permission unchecked="true"
	    */
	public void setTotalRegistres060(int totalRegistres060) {
		this.totalRegistres060 = totalRegistres060;
	}
}
