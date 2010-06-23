package es.caib.regweb;

import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.*;

import java.rmi.*;
import javax.ejb.*;

import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

/**
 * Bean que genera els llistats de registres de sortida
 * @author  FJMARTINEZ
 * @version 1.0
 */

public class ListadoRegistrosSalidaBean implements SessionBean {
	
	private SessionContext context;
	private SessionContext contextoSesion;
	
	private String oficinaDesde="";
	private String oficinaHasta="";
	private String fechaDesde="";
	private String fechaHasta="";
	private String oficinaFisica="";
	private Hashtable errores=new Hashtable();
	private boolean validado;
	private DateFormat dateF= new SimpleDateFormat("dd/MM/yyyy");
	private Date fechaTest1=null;
	private Date fechaTest2=null;
	private boolean error=false;
	private String any="";
	private String extracto="";
	private String tipo="";
	private String remitente="";
	private String destinatario="";
	private String CodiRemitent="";
	private String destino="";
	
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
	public ListadoRegistrosSalidaBean() {
	}
	
	public void setoficinaDesde(String oficinaDesde) {
		this.oficinaDesde=oficinaDesde;
	}
	public void setoficinaHasta(String oficinaHasta) {
		this.oficinaHasta=oficinaHasta;
	}
	public void setoficinaFisica(String oficinaFisica) {
		this.oficinaFisica=oficinaFisica;
	}
	public void setfechaDesde(String fechaDesde) {
		this.fechaDesde=fechaDesde;
	}
	public void setfechaHasta(String fechaHasta) {
		this.fechaHasta=fechaHasta;
	}
	public void setExtracto(String extracto) {
		this.extracto=extracto;
	}
	public void setTipo(String tipo) {
		this.tipo=tipo;
	}
	public void setRemitente(String remitente) {
		this.remitente=remitente;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario=destinatario;
	}
	public void setDestino(String destino) {
		this.destino=destino;
	}
	
	public void setCodiRemitent(String codiremitent) {
		this.CodiRemitent=codiremitent;
	}
	
	public String getOficinaDesde() {
		return oficinaDesde;
	}
	public String getOficinaHasta() {
		return oficinaHasta;
	}
	public String getOficinaFisica() {
		return oficinaFisica;
	}
	public String getFechaDesde() {
		return fechaDesde;
	}
	public String getFechaHasta() {
		return fechaHasta;
	}
	public void setAny(String any) {
		this.any=any;
	}
	public String getExtracto() {
		return extracto;
	}
	public String getTipo() {
		return tipo;
	}
	public String getRemitente() {
		return remitente;
	}
	public String getDestinatario() {
		return destinatario;
	}
	public String getCodiRemitent() {
		return CodiRemitent;
	}
	
	public String getDestino() {
		return destino;
	}
	
	/**
	 * @param fecha
	 */
	private void validarFecha(String fecha1, String fecha2) {
		error=false;
		try {
			dateF.setLenient(false);
			fechaTest1 = dateF.parse(fecha1);
		} catch (Exception ex) {
			error=true;
			errores.put("2","Data inici no és valida, (dd/mm/aaaa)");
		}
		try {
			dateF.setLenient(false);
			fechaTest2 = dateF.parse(fecha2);
		} catch (Exception ex) {
			error=true;
			errores.put("3","Data final no és valida, (dd/mm/aaaa)");
		}
		if (!error) {
			if (fechaTest2.before(fechaTest1)) {
				errores.put("4","Data final és inferior a data inici");
			}
		}
	}
	
	/**
	 * @return
	 */
	public Hashtable getErrores() {
		return errores;
	}
	
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
		validarFecha(fechaDesde, fechaHasta);
		// Hem de validar que el codi d'organisme destinatari ficat sigui correcte, ja sigui dins BORGANI com BHORGANI
		if (CodiRemitent==null)
			CodiRemitent="";
		if (!CodiRemitent.equals("")) {
			Connection conn = null;
			ResultSet rs = null;
			ResultSet rsHist = null;
			PreparedStatement ps = null;
			PreparedStatement psHist = null;
			try {
				int codiRemitent = Integer.parseInt(CodiRemitent);
				conn=ToolsBD.getConn();
				String sentenciaSql="SELECT * FROM BORGANI WHERE FAXCORGA=? ";
				ps=conn.prepareStatement(sentenciaSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				ps.setString(1,CodiRemitent);
				rs=ps.executeQuery();
				
				if (!rs.next()) {
					sentenciaSql="SELECT * FROM BHORGAN WHERE FHXCORGA=? ";
					ps=conn.prepareStatement(sentenciaSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					ps.setString(1,CodiRemitent);
					rs=ps.executeQuery();
					if (!rs.next())
						errores.put("1","Codi de destinatari inexistent");
					//System.out.println("ERROR: Codi "+CodiRemitent+" no existeix!");
				}
			} catch ( NumberFormatException ne){
				errores.put("2","Codi de destinatari no numèric"); 	
			} catch (Exception e) {
				System.out.println("Error: "+e.getMessage());
				e.printStackTrace();
			} finally {
				ToolsBD.closeConn(conn, ps, rs);
			}
		}
		if (errores.size()==0) {
			validado=true;
		} else {
			validado=false;
		}
		return validado;
	}
	
	public void inizializar() {
		oficinaDesde="";
		oficinaHasta="";
		fechaDesde="";
		fechaHasta="";
		extracto="";
		tipo="";
		remitente="";
		destinatario="";
		destino="";
	}
	
	public Vector recuperar(String usuario, int sizePagina, int pagina) {
		Connection conn = null;
		ResultSet rs = null;
		ResultSet rsHist = null;
		PreparedStatement ps = null;
		PreparedStatement psHist = null;
		
		usuario=usuario.toUpperCase();
		Vector registrosSalidaVector=new Vector();
		DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
		DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date fechaDocumento=null;
		java.util.Date fechaEESS=null;
		
		String fechaInicio=fechaDesde.substring(6,10)+fechaDesde.substring(3,5)+fechaDesde.substring(0,2);
		String fechaFinal=fechaHasta.substring(6,10)+fechaHasta.substring(3,5)+fechaHasta.substring(0,2);
		
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BZSALIDA LEFT JOIN BAGECOM ON FAACAGCO=FZSCAGCO " +
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
			//(!remitente.trim().equals("") ? " and upper(fhxdorgt) like ? )" : "") +
			(!CodiRemitent.trim().equals("") ? " AND FZSCORGA = ? " : "") +
			"ORDER BY FZSCAGCO, FZSANOEN, FZSNUMEN";
			ps=conn.prepareStatement(sentenciaSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			
			int contador=1;
			ps.setString(contador++,usuario);
			ps.setString(contador++,"CS");
			ps.setInt(contador++,Integer.parseInt(oficinaDesde));
			ps.setInt(contador++,Integer.parseInt(oficinaHasta));
			ps.setInt(contador++,Integer.parseInt(fechaInicio));
			ps.setInt(contador++,Integer.parseInt(fechaFinal));
			if (oficinaFisica!=null && !oficinaFisica.equals("")) {
    			ps.setInt(contador++, Integer.parseInt(oficinaFisica));
			}
			if (!extracto.trim().equals("")) {
				ps.setString(contador++,"%"+extracto.toUpperCase()+"%");
				ps.setString(contador++,"%"+extracto.toUpperCase()+"%");
			}
			if (!tipo.trim().equals("")) {
				ps.setString(contador++,tipo);
			}
			if (!destinatario.trim().equals("")) {
				ps.setString(contador++,"%"+destinatario.toUpperCase()+"%");
				ps.setString(contador++,"%"+destinatario.toUpperCase()+"%");
			}
			if (!destino.trim().equals("")) {
				ps.setString(contador++,"%"+destino.toUpperCase()+"%");
				ps.setString(contador++,"%"+destino.toUpperCase()+"%");
			}
			
			if (!remitente.trim().equals("")) {
				ps.setString(contador++,"%"+remitente.toUpperCase()+"%");
			}
			
			if (!CodiRemitent.trim().equals("")) {
				ps.setString(contador++,CodiRemitent);
			}
			
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

			//Numero maximo de registros a devolver
			ps.setMaxRows((sizePagina*pagina)+1);
			rs=ps.executeQuery();
			
			if (pagina>1) {
				rs.next();
				rs.relative((sizePagina*(pagina-1))-1);
			}
			
			while (rs.next()) {
				RegistroSeleccionado registroSalida=new RegistroSeleccionado();
				
				registroSalida.setAnoEntrada(String.valueOf(rs.getInt("FZSANOEN")));
				registroSalida.setNumeroEntrada(String.valueOf(rs.getInt("FZSNUMEN")));
				registroSalida.setOficina(String.valueOf(rs.getInt("FZSCAGCO")));
				
				String fechaES=String.valueOf(rs.getInt("FZSFENTR"));
				try {
					fechaEESS=yyyymmdd.parse(fechaES);
					registroSalida.setFechaES(ddmmyyyy.format(fechaEESS));
				} catch (Exception e) {
					registroSalida.setFechaES(fechaES);
				}
				
				
				registroSalida.setOficinaFisica(rs.getString("OFF_CODI"));
				registroSalida.setDescripcionOficinaFisica(rs.getString("OFF_NOM"));

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
				psHist.setString(1,String.valueOf(rs.getInt("FZSCAGCO")));
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
				
				//String textoOficina=rs.getString("FAADAGCO");
				//if (textoOficina==null) {textoOficina=" ";}
				registroSalida.setDescripcionOficina(textoOficina);
				String fechaDocu=String.valueOf(rs.getInt("FZSFDOCU"));
				try {
					fechaDocumento=yyyymmdd.parse(fechaDocu);
					registroSalida.setData(ddmmyyyy.format(fechaDocumento));
				} catch (Exception e) {
					registroSalida.setData(fechaDocu);
				}
				
				if (rs.getString("FZGCENTI")==null) {
					registroSalida.setDescripcionRemitente(rs.getString("FZSREMIT"));
				} else {
					registroSalida.setDescripcionRemitente(rs.getString("FZGDENT2"));
				}
				
				if (rs.getString("FABDAGGE")==null) {
					registroSalida.setDescripcionGeografico(rs.getString("FZSPROCE"));
				} else {
					registroSalida.setDescripcionGeografico(rs.getString("FABDAGGE"));
				}
				
				//registroSalida.setDescripcionOrganismoDestinatario(rs.getString("FAXDORGR"));
				
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
				String sentenciaSqlHistOrga="SELECT * FROM BHORGAN01 WHERE FHXCORGA=? AND FHXFALTA<=? " +
				"AND ( (FHXFBAJA>= ? AND FHXFBAJA !=0) OR FHXFBAJA = 0)";
				psHist=conn.prepareStatement(sentenciaSqlHistOrga);
				psHist.setString(1,String.valueOf(rs.getInt("FZSCORGA")));
				psHist.setString(2,fechaES);
				psHist.setString(3,fechaES);
				rsHist=psHist.executeQuery();
				if (rsHist.next()) {
					/* Hem trobat un històric de l'organisme sol·licitat, hem de mostrar-ne el descriptiu. */
					registroSalida.setDescripcionOrganismoDestinatario(rsHist.getString("FHXDORGT"));
					//System.out.println("Org destinatari: "+descripcionOrganismoDestinatario);
				} else {
					registroSalida.setDescripcionOrganismoDestinatario(rs.getString("FAXDORGT"));
					if (registroSalida.getDescripcionOrganismoDestinatario()==null) {
						registroSalida.setDescripcionOrganismoDestinatario(" ");
					}
				}
				//System.out.println("Org destinatari: "+this.getDescripcionOrganismoDestinatario());
				//  Tancam el preparedstatement i resultset de l'històric
				if (rsHist != null)
					rsHist.close();
				if (psHist != null)
					psHist.close();
				
				
				//registroSalida.setDescripcionDocumento(rs.getString("FZIDTIPE"));
				if (rs.getString("FZIDTIPE")==null)
					registroSalida.setDescripcionDocumento("&nbsp;");
				else
					registroSalida.setDescripcionDocumento(rs.getString("FZIDTIPE"));
				
				registroSalida.setDescripcionIdiomaDocumento(rs.getString("FZMDIDI"));
				registroSalida.setRegistroAnulado(rs.getString("FZSENULA"));
				if (rs.getString("FZSCIDIO").equals("1")) {
					registroSalida.setExtracto(rs.getString("FZSCONEN"));
				} else {
					registroSalida.setExtracto(rs.getString("FZSCONE2"));
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
                logLopdBZSALIDA("SELECT", usuario, fzafsis, horamili, rs.getInt("FZSNUMEN"), rs.getInt("FZSANOEN"), rs.getInt("FZSCAGCO"));

				registrosSalidaVector.addElement(registroSalida);
			}
		} catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return registrosSalidaVector;
	}
	
	public Vector recuperarRegistrosOficina(String usuario, int maxRegistros, int oficina, String any, String accion, int numero)
	throws java.rmi.RemoteException, Exception {
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
					texto=" AND FZSNUMEN<?";
				} else {
					texto=" AND FZSNUMEN>?";
				}
			} else {
				texto="";
			}
			
			String sentenciaSql="SELECT * FROM BZSALIDA LEFT JOIN BAGECOM ON FAACAGCO=FZSCAGCO " +
			"LEFT JOIN BZENTID ON FZSCENTI=FZGCENTI AND FZGNENTI=FZSNENTI " +
			"LEFT JOIN BORGANI ON FAXCORGA=FZSCORGA " +
			"LEFT JOIN BZTDOCU ON FZICTIPE=FZSCTIPE " +
			"LEFT JOIN BZIDIOM ON FZSCIDI=FZMCIDI " +
			"LEFT JOIN BAGRUGE ON FZSCTAGG=FABCTAGG AND FZSCAGGE=FABCAGGE " +
			"LEFT JOIN BZAUTOR ON FZHCUSU=? AND FZHCAGCO=FZSCAGCO " +
			"WHERE FZHCAUT=? AND FZSCAGCO=?" +
			(!any.trim().equals("") ? "AND FZSANOEN>=?" : "") +
			texto +
			"ORDER BY FZSCAGCO, FZSANOEN, FZSNUMEN " +
			(accion.equals("R") ? "DESC" : "ASC") ;
			ps=conn.prepareStatement(sentenciaSql);
			
			// Numero maximo de registros a devolver
			ps.setMaxRows(maxRegistros);
			
			ps.setString(1,usuario);
			ps.setString(2,"AS");
			ps.setInt(3,oficina);
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
				
				registro.setAnoEntrada(String.valueOf(rs.getInt("FZSANOEN")));
				registro.setNumeroEntrada(String.valueOf(rs.getInt("FZSNUMEN")));
				registro.setOficina(String.valueOf(rs.getInt("FZSCAGCO")));
				String textoOficina=rs.getString("FAADAGCO");
				if (textoOficina==null) {textoOficina=" ";}
				registro.setDescripcionOficina(textoOficina);
				String fechaDocu=String.valueOf(rs.getInt("FZSFDOCU"));
				try {
					fechaDocumento=yyyymmdd.parse(fechaDocu);
					registro.setData(ddmmyyyy.format(fechaDocumento));
				} catch (Exception e) {
					registro.setData(fechaDocu);
				}
				String fechaES=String.valueOf(rs.getInt("FZSFENTR"));
				try {
					fechaEESS=yyyymmdd.parse(fechaES);
					registro.setFechaES(ddmmyyyy.format(fechaEESS));
				} catch (Exception e) {
					registro.setFechaES(fechaES);
				}
				
				if (rs.getString("FZGCENTI")==null) {
					registro.setDescripcionRemitente(rs.getString("FZSREMIT"));
				} else {
					registro.setDescripcionRemitente(rs.getString("FZGDENT2"));
				}
				registro.setDescripcionOrganismoDestinatario(rs.getString("FAXDORGR"));
				registro.setDescripcionDocumento(rs.getString("FZIDTIPE"));
				registro.setDescripcionIdiomaDocumento(rs.getString("FZMDIDI"));
				registro.setRegistroAnulado(rs.getString("FZSENULA"));
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
		this.context = ctx;
	}
	
	private RegistroSalidaHome getRegistroSalidaHome() throws NamingException {
		final InitialContext context = new InitialContext();
		return (RegistroSalidaHome)PortableRemoteObject.narrow(context.lookup("es.caib.regweb.RegistroSalidaHome"), RegistroSalidaHome.class);
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
			Connection conn = null;
			ResultSet rs = null;
			PreparedStatement ps = null;
			try {
				conn=ToolsBD.getConn();
				String sentenciaSql="INSERT INTO BZSALPD (FZUTIPAC, FZUCUSU, FZUDATAC, FZUHORAC, FZUNUMEN, FZUANOEN," +
					" FZUCAGCO) VALUES (?,?,?,?,?,?,?)";
					ps=conn.prepareStatement(sentenciaSql);
					ps.setString(1,tipusAcces);
					ps.setString(2,usuari);
					ps.setInt(3,data);
					ps.setInt(4,hora);
					ps.setInt(5, nombreRegistre);
					ps.setInt(6, any);
					ps.setInt(7,oficina);
					ps.execute(); 
					//ps.close();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("ERROR: S'ha produ\357t un error a logLopdBZSALID");
			} finally {
				ToolsBD.closeConn(conn, ps, rs);
			}
			//System.out.println("ListadoRegistrosSalidaBean: Desada informació dins BZSALPD: "+tipusAcces+" "+usuari+" "+data+" "+hora+" "+nombreRegistre+" "+any+" "+oficina);
	     }
}