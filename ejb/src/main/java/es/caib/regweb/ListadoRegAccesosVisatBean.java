/*
 * Created on 19 de junio de 2002, 18:56
 */

package es.caib.regweb;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.apache.log4j.Category;

/**
 * Bean que permet fer consultes sobre el registre d'accessos al registre de sortides 
 * @author  Sebastià Matas, basat en codi de FJMARTINEZ
 * @version 1.0
 */

public class ListadoRegAccesosVisatBean implements SessionBean {
	
	private static final Category log = Category.getInstance(ListadoRegAccesosVisatBean.class.getName());
	
	private String oficinaDesde="";
	private String oficinaHasta="";
	private String fechaDesde="";
	private String fechaHasta="";
	private Hashtable errores=new Hashtable();
	private boolean validado;
	private DateFormat dateF= new SimpleDateFormat("dd/MM/yyyy");
	private boolean error=false;
	private String RegistreES="";
	private String numRegistre="";
	private String anyRegistre="";
	private boolean calcularTotalRegistres=false;
	private String totalFiles="";
	
	
	
	/**
	 * @return Returns the calcularTotalRegistres.
	 */
	public boolean isCalcularTotalRegistres() {
		return calcularTotalRegistres;
	}

	/**
	 * @param calcularTotalRegistres The calcularTotalRegistres to set.
	 */
	public void setCalcularTotalRegistres(boolean calcularTotalRegistres) {
		this.calcularTotalRegistres = calcularTotalRegistres;
	}

	/**
	 * @return Returns the totalFiles.
	 */
	public String getTotalFiles() {
		return totalFiles;
	}

	/**
	 * @param totalFiles The totalFiles to set.
	 */
	public void setTotalFiles(String totalFiles) {
		this.totalFiles = totalFiles;
	}
	
	/** Creates a new instance of Valores */
	public ListadoRegAccesosVisatBean() {
	}
	
	
	/**
	 * @return Returns the registreES.
	 */
	public String getRegistreES() {
		return RegistreES;
	}


	/**
	 * @param registreES The registreES to set.
	 */
	public void setRegistreES(String registreES) {
		RegistreES = registreES.toUpperCase();
	}


	public String getAnyRegistre() {
		return anyRegistre;
	}


	public void setAnyRegistre(String anyRegistre) {
		this.anyRegistre = anyRegistre;
	}


	public String getNumRegistre() {
		return numRegistre;
	}
	public void setNumRegistre(String numRegistre) {
		this.numRegistre = numRegistre;
	}

	public void setoficinaDesde(String oficinaDesde) {
		this.oficinaDesde=oficinaDesde;
	}
	public void setoficinaHasta(String oficinaHasta) {
		this.oficinaHasta=oficinaHasta;
	}
	public void setfechaDesde(String fechaDesde) {
		this.fechaDesde=fechaDesde;
	}
	public void setfechaHasta(String fechaHasta) {
		this.fechaHasta=fechaHasta;
	}
	
	public String getOficinaDesde() {
		return oficinaDesde;
	}
	public String getOficinaHasta() {
		return oficinaHasta;
	}
	public String getFechaDesde() {
		return fechaDesde;
	}
	public String getFechaHasta() {
		return fechaHasta;
	}
	
	/**
	 * Validació de les dates donades per l'usuari.
	 * @param fecha1 Data inicial
	 * @param fecha2 Data final
	 * @param obligatori Indica si és obligatori que hi hagui dates
	 */
	private void validarFecha(String fecha1, String fecha2, boolean obligatori) {
		Date fechaTest1=null;
		Date fechaTest2=null;
		
		error=false;
		log.debug("validarFecha: obligatori="+obligatori);
		try {
			dateF.setLenient(false);
			fechaTest1 = dateF.parse(fecha1);
		} catch (Exception ex) {
			if (obligatori) {
				error=true;
				errores.put("2","Data inici no és valida, (dd/mm/aaaa)");
			}
		}
		try {
			dateF.setLenient(false);
			fechaTest2 = dateF.parse(fecha2);
		} catch (Exception ex) {
			if (obligatori) {
				error=true;
				errores.put("3","Data final no és valida, (dd/mm/aaaa)");
			}
		}
		if (!error && (fechaTest1!=null && fechaTest2!=null)) {
			if (fechaTest2.before(fechaTest1)) {
				if (obligatori) 
					errores.put("4","Data final és inferior a data inici");
			}
		}
	}
	
	/**
	 * Mètode per veure els errors que s'han generat en l'execució del Bean.
	 * @return Torna els errors que s'han generat.
	 */
	public Hashtable getErrores() {
		return errores;
	}

	/**
	 * Mètode per validar els camps de cerca
	 * @return Torna true si els camps son vàlids
	 */
	public boolean validarBusqueda() {
		validado=false;
		errores.clear();
		
		// Validamos que la oficina desde sea < o = a oficina Hasta
		if (oficinaDesde==null || oficinaHasta==null || oficinaDesde.equals("") || oficinaHasta.equals("")) {
			errores.put("1","Límits incorrectes per a oficines");
		} else {
			int oficinaInicio=Integer.parseInt(oficinaDesde);
			int oficinaFin=Integer.parseInt(oficinaHasta);
			if (oficinaFin<oficinaInicio) {
				errores.put("1","Límits incorrectes per a oficines");
			}
		}
		// Validamos fecha inicio
		if ( !numRegistre.equals("") ) {
			//No fa falta límit de dates, ja que hi ha un número de registre.
			log.debug("Validam dates de forma laxa ja que numRegistre="+numRegistre+" fechaDesde="
					+fechaDesde+" fechaHasta="+fechaHasta);
			validarFecha(fechaDesde, fechaHasta, false);
		} else {
			validarFecha(fechaDesde, fechaHasta, true);
		}
		if (errores.size()==0) {
			validado=true;
		} else {
			validado=false;
		}
		
		if ( numRegistre == null || !numRegistre.equals("")  ) { 
			try {
				if (Integer.parseInt(numRegistre) <= 0) {
					
					errores.put("2","Nombre de registre incorrecte");
					validado = false;
				}
			} catch (NumberFormatException nfe) {
				errores.put("2","Nombre de registre no vàlid");
				validado = false;
			}
		}
		if ( (!numRegistre.equals("") && anyRegistre.equals("")) ||
				(numRegistre.equals("") && 
						!anyRegistre.equals("") ) ) {
			log.debug("numRegistre="+numRegistre+" anyRegistre="+anyRegistre);
			errores.put("4","S'ha de posar el número de registre i l'any!");
			validado = false;
		}
		
		if ( anyRegistre == null || !anyRegistre.equals("")  ) {
			try {
				if (Integer.parseInt(anyRegistre) <= 0) {
					errores.put("3","Any de registre incorrecte");
					validado = false;
				}
			} catch (NumberFormatException nfe) {
				errores.put("2","Nombre de registre no vàlid");
				validado = false;
			}		
		}
		return validado;
	}
	
	
	/**
	 * Inicialització del Bean
	 */	
	public void inizializar() {
		oficinaDesde="";
		oficinaHasta="";
		fechaDesde="";
		fechaHasta="";
		numRegistre="";
		anyRegistre="";
	}

	/** 
	 * Parsejam d'hora agafada de BBDD
	 * @param hora En format String
	 * @return Hora parsejada amb el format: HH:MM:SS:sss
	 */
	private String parsejarHora( String hora)  {
		
		switch (hora.length()) {
		case 9:
//			Del tipus: HH:mm:yy:zzz
			return hora.substring(0,2)+":"+hora.substring(2,4)+":"+hora.substring(4,6)+":"+hora.substring(6);
		case 8:
//			Del tipus: H:mm:yy:zzz
			return "0"+hora.substring(0,1)+":"+hora.substring(1,3)+":"+hora.substring(3,5)+":"+hora.substring(5);
		case 7:
//			Del tipus: 00:mm:yy:zzz
			return "00:"+hora.substring(0,2)+":"+hora.substring(2,4)+":"+hora.substring(4);
		case 6:
			//Del tipus: 00:0X:yy:zzz
			return "00:0"+hora.substring(0,1)+":"+hora.substring(1,3)+":"+hora.substring(3);
		case 5:
			//Del tipus: 00:00:yy:zzz
			return "00:00:"+hora.substring(0,2)+":"+hora.substring(2);
		case 4: 
//			Del tipus: 00:00:01:zzz
			return "00:00:0"+hora.substring(0,1)+":"+hora.substring(1);
		case 1:
		case 2:
		case 3:
			// Només hi ha milisegons!
			return "00:00:00"+hora;
		}
		return hora;
			
	}
	
	/**
	 * Recuperam els registres segons els filtres de cerca.
	 * @param usuario Usuari que fa la consulta
	 * @param sizePagina Nombre de registres a tornar
	 * @param pagina Nombre de pàgina a tornar
	 * @return Vector que conté les classes amb la informació de cada registre.
	 */
	public Vector recuperar(String usuario, int sizePagina, int pagina) throws java.rmi.RemoteException {
		Connection conn = null;
		ResultSet rs = null;
		ResultSet rsHist = null;
		PreparedStatement ps = null;
		PreparedStatement psHist = null;
		
		usuario=usuario.toUpperCase();
		Vector registrosVector=new Vector();
		DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
		DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date fechaEESS=null;        
		java.util.Date fechaEESSModif=null;
		String fechaInicio="";
		String fechaFinal="";
		
		if ( !fechaDesde.equals("") ) {
			log.debug("fechaDesde="+fechaDesde);
			fechaInicio=fechaDesde.substring(6,10)+fechaDesde.substring(3,5)+fechaDesde.substring(0,2);
			fechaFinal=fechaHasta.substring(6,10)+fechaHasta.substring(3,5)+fechaHasta.substring(0,2);
		}
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BZMOLPD LEFT JOIN BAGECOM ON FAACAGCO=FZVCAGCO " +
			"WHERE FZVCAGCO>=? AND FZVCAGCO<=? " +
			(!fechaDesde.equals("") ? " AND FZVDATAC>=? AND FZVDATAC<=?  " : "" ) +
			(!numRegistre.equals("") ? " AND FZVNUMEN=?" : "" )+
			(!anyRegistre.equals("") ? " AND FZVANOEN=?" : "" )+
			(!RegistreES.equals("") ? " AND FZVCENSA=?" : "" )+
			( !numRegistre.equals("") || !anyRegistre.equals("") ? 
					" ORDER BY FZVANOEN, FZVNUMEN, FZVDATAC, FZVHORAC" :
					" ORDER BY FZVDATAC, FZVHORAC, FZVANOEN, FZVNUMEN ");			
			log.error(sentenciaSql);
			ps=conn.prepareStatement(sentenciaSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			int contador=1;
			ps.setInt(contador++,Integer.parseInt(oficinaDesde));
			ps.setInt(contador++,Integer.parseInt(oficinaHasta));

			if ( !fechaDesde.equals("") ) {
				ps.setInt(contador++,Integer.parseInt(fechaInicio));
				ps.setInt(contador++,Integer.parseInt(fechaFinal));
			}
			if (!numRegistre.equals("")) {
				log.debug("nombre de registre:" + numRegistre);
				ps.setInt(contador++,Integer.parseInt(numRegistre));
			}
			if (!anyRegistre.equals(""))
				ps.setInt(contador++,Integer.parseInt(anyRegistre));
			if (!RegistreES.equals(""))
				ps.setString(contador++,RegistreES);
			
			if (pagina<=1 && isCalcularTotalRegistres()) {
				// La primera vegada, calculam el nombre màxim de registres que tornarà la consulta
				rs=ps.executeQuery();
				System.out.println("Nombre total de registres ="+rs.getFetchSize());
//				 Point to the last row in resultset.
			      rs.last();      
			      // Get the row position which is also the number of rows in the resultset.
			      int rowcount = rs.getRow();
			      // Reposition at the beginning of the ResultSet to take up rs.next() call.
			      rs.beforeFirst();
			      System.out.println("Total rows for the query using Scrollable ResultSet: "
			                         +rowcount);
			      this.totalFiles= String.valueOf(rowcount);
				}
			    
			    // Numero maximo de registros a devolver
				ps.setMaxRows((sizePagina*pagina)+1);
			rs=ps.executeQuery();
			
			if (pagina>1) {
				rs.next();
				rs.relative((sizePagina*(pagina-1))-1);
			}
			
			while (rs.next()) {
				RegistroESlopdSeleccionado registro=new RegistroESlopdSeleccionado();
				registro.setTipusAcces(rs.getString("FZVTIPAC"));
				registro.setUsuCanvi(rs.getString("FZVCUSU"));			
				String fechaES=String.valueOf(rs.getInt("FZVDATAC"));
				try {
					fechaEESS=yyyymmdd.parse(fechaES);
					registro.setDataCanvi(ddmmyyyy.format(fechaEESS));
				} catch (Exception e) {
					registro.setDataCanvi(fechaES);
				}
				/**  TODO: PARSEJAR HORA */   
				registro.setHoraCanvi( parsejarHora(String.valueOf(rs.getInt("FZVHORAC"))) );
				registro.setNombreRegistre(String.valueOf(rs.getInt("FZVNUMEN")));
				registro.setAnyRegistre(String.valueOf(rs.getInt("FZVANOEN")));
				registro.setOficinaRegistre(String.valueOf(rs.getInt("FZVCAGCO")));
				registro.setTipusVisat(String.valueOf(rs.getString("FZVCENSA")));
				String fechaESModif=String.valueOf(rs.getInt("FZVDATAC"));
				try {
					fechaEESSModif=yyyymmdd.parse(fechaESModif);
					registro.setDataModif(ddmmyyyy.format(fechaEESSModif));
				} catch (Exception e) {
					registro.setDataModif(fechaESModif);
				}
				String hora=String.valueOf(rs.getInt("FZVHMODI"));
				String hhmm="";
				if (hora!=null && !hora.equals("") && !hora.equals("0") ) {
					if (hora.length()<4) {hora="0"+hora;}
					String hh=hora.substring(0,2);
					String mm=hora.substring(2,4);
					String ss=hora.substring(4,6);
					hhmm=hh+":"+mm+":"+ss;
				} else {
					hhmm=hora;
				}
				registro.setHoraModif(hhmm);
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
				String sentenciaSqlHistOfi="SELECT * FROM BHAGECO01 WHERE FHACAGCO=? AND FHAFALTA<=? " +
				"AND ( (FHAFBAJA>= ? AND FHAFBAJA !=0) OR FHAFBAJA = 0)";
				psHist=conn.prepareStatement(sentenciaSqlHistOfi);
				psHist.setString(1,String.valueOf(rs.getInt("FZVCAGCO")));
				psHist.setString(2,fechaES);
				psHist.setString(3,fechaES);
				rsHist=psHist.executeQuery();
				if (rsHist.next()) {
					/* Hem trobat un històric de l'oficina sol·licitada, hem de mostrar-ne el descriptiu. */
					textoOficina=rsHist.getString("FHADAGCO");
				} else {
					textoOficina=rs.getString("FAADAGCO");
					if (textoOficina==null) {
						textoOficina=" ";
					}
				}
				//  Tancam el preparedstatement i resultset de l'històric
				if (rsHist != null)
					rsHist.close();
				if (psHist != null)
					psHist.close();
				
				registro.setOficinaRegistreDesc(textoOficina);

				registrosVector.addElement(registro);
			}			
		} catch (Exception e) {
			errores.put("1","Error intern:"+e.getMessage());
			log.error("Error: "+e.getMessage());
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return registrosVector;
	}

	/**
	 * Recuperam els registres segons els filtres de cerca.
	 * @param usuario Usuari que fa la consulta
	 * @param maxRegistros Nombre màxim de registres a tornar
	 * @param oficina Oficina del registre a tornar
	 * @param any Any del registre a tornar
	 * @param accion ?????????????
	 * @param numero Nombre de registre a tornar
	 * En teoría només hauria de tornar un registre!!!!
	 * 
	 * @return Vector que conté les classes amb la informació de cada registre.
	 */
	public Vector recuperarRegistrosOficina(String usuario, int maxRegistros, int oficina, String any, String accion, int numero)
	throws java.rmi.RemoteException, Exception {
		/* COMENTAM FINS QUE NO L'ADAPTI A LLEGIR DE BZENLPD!!!! 
		 Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		usuario=usuario.toUpperCase();
		Vector registrosVector=new Vector();
		DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
		DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date fechaDocumento=null;
		java.util.Date fechaEESS=null;
		
		try {
			conn=ToolsBD.getConn();
			String texto="";
			if (numero>0) {
				if (accion.equals("R")) {
					texto=" and fzanumen<?";
				} else {
					texto=" and fzanumen>?";
				}
			} else {
				texto="";
			}
			String sentenciaSql="select * from bzenlpd left join bagecom on faacagco=fzacagco " +
			"left join bzentid on fzacenti=fzgcenti and fzgnenti=fzanenti " +
			"left join borgani on faxcorga=fzacorga " +
			"left join bztdocu on fzictipe=fzactipe " +
			"left join bzidiom on fzacidi=fzmcidi " +
			"left join bagruge on fzactagg=fabctagg and fzacagge=fabcagge " +
			"left join bzautor on fzhcusu=? and fzhcagco=fzacagco " +
			"where fzhcaut=? and fzacagco=?" +
			(!any.trim().equals("") ? "and fzaanoen>=?" : "") +
			texto +
			" order by fzacagco, fzaanoen, fzanumen " +
			(accion.equals("R") ? "desc" : "asc") ;
			ps=conn.prepareStatement(sentenciaSql);
			
			// Numero maximo de registros a devolver
			ps.setMaxRows(maxRegistros);
			
			ps.setString(1,usuario);
			ps.setString(2,"AS");
			// ps.setInt(3,Integer.parseInt(oficinaDesde));
			// ps.setInt(4,Integer.parseInt(oficinaHasta));
			ps.setInt(3, oficina);
			int contadorPS=4;
			if (!any.trim().equals("")) {
				ps.setInt(contadorPS++,Integer.parseInt(any));
			}
			if (numero>0) {
				ps.setInt(contadorPS,numero);
			}
			
			rs=ps.executeQuery();
			while (rs.next()) {
				RegistroSeleccionado registro=new RegistroSeleccionado();
				
				registro.setAnoEntrada(String.valueOf(rs.getInt("FZAANOEN")));
				registro.setNumeroEntrada(String.valueOf(rs.getInt("FZANUMEN")));
				registro.setOficina(String.valueOf(rs.getInt("FZACAGCO")));
				String textoOficina=rs.getString("FAADAGCO");
				if (textoOficina==null) {textoOficina=" ";}
				registro.setDescripcionOficina(textoOficina);
				String fechaDocu=String.valueOf(rs.getInt("FZAFDOCU"));
				
				try {
					fechaDocumento=yyyymmdd.parse(fechaDocu);
					registro.setData(ddmmyyyy.format(fechaDocumento));
				} catch (Exception e) {
					registro.setData(fechaDocu);
				}
				String fechaES=String.valueOf(rs.getInt("FZAFENTR"));
				try {
					fechaEESS=yyyymmdd.parse(fechaES);
					registro.setFechaES(ddmmyyyy.format(fechaEESS));
				} catch (Exception e) {
					registro.setFechaES(fechaES);
				}
				
				if (rs.getString("FZGCENTI")==null) {
					registro.setDescripcionRemitente(rs.getString("FZAREMIT"));
				} else {
					registro.setDescripcionRemitente(rs.getString("FZGDENT2"));
				}
				registro.setDescripcionOrganismoDestinatario(rs.getString("FAXDORGR"));
				registro.setDescripcionDocumento(rs.getString("FZIDTIPE"));
				registro.setDescripcionIdiomaDocumento(rs.getString("FZMDIDI"));
				registro.setRegistroAnulado(rs.getString("FZAENULA"));
				registrosVector.addElement(registro);
			}
		} catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		
		if (accion.equals("R")) {
			Collections.sort(registrosVector);
		}
		
		return registrosVector;
		*/
		return null;
	}
	
	public void ejbCreate() throws CreateException {
	}
	public void ejbActivate() {
	}
	public void ejbPassivate() {
	}
	public void ejbRemove() {
	}
	public void setSessionContext(SessionContext ctx) {
	}
	
}