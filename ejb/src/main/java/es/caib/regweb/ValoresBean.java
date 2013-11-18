/*
 * ValoresBean.java
 *
 * Created on 19 de junio de 2002, 18:56
 */

package es.caib.regweb;

import java.util.*;
import java.sql.*;
import java.text.*;

import java.rmi.*;
import javax.ejb.*;
import javax.servlet.http.*;

import org.apache.log4j.Category;

/**
 * EJB que encapsula funcions massa genèriques per tenir el seu probi EJB. 
 * 
 * @author  FJMARTINEZ
 */
public class ValoresBean implements SessionBean {
	
	private static final Category log = Category.getInstance(ValoresBean.class.getName());
	/** Creates a new instance of Valores */
	public ValoresBean() {
	}
	private SessionContext contextoSesion;
	
	
	public boolean usuarioAutorizadoVisar(String usuario, String programa) {
		boolean estaAutorizado=false;
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		usuario = usuario.toUpperCase();
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BZAUTOR WHERE FZHCUSU=? AND FZHCAUT=? ";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,usuario);
			ps.setString(2,programa);
			rs=ps.executeQuery();
			if (rs.next()) {
				estaAutorizado=true;
			} else {
				estaAutorizado=false;
			}
		} catch (Exception e) {
			estaAutorizado=false;
			System.out.println("UsuarioAutorizadoVisar ERROR: "+usuario);
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return estaAutorizado;
	}

	private Vector getOficines() {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector oficinas=new Vector();
		
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BAGECOM ORDER BY FAACAGCO";
			ps=conn.prepareStatement(sentenciaSql);
			rs=ps.executeQuery();
			
			while (rs.next()) {
				oficinas.addElement(String.valueOf(rs.getInt("FAACAGCO")));
				oficinas.addElement(rs.getString("FAADAGCO"));
				//log.debug("Afegim: "+String.valueOf(rs.getInt("FAACAGCO"))+" "+rs.getString("FAADAGCO"));
			}
			
			if (oficinas.size()==0) {
				oficinas.addElement("");
				oficinas.addElement("No hi ha oficines!");
			}
			
		} catch (Exception e) {
			oficinas.addElement("");
			oficinas.addElement("BuscarOficinas Error en la SELECT");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return oficinas;
	}
	
	public Vector BuscarOficinas(String usuario, String autorizacion) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		usuario=usuario.toUpperCase();
		Vector oficinas=new Vector();
		
		if ( usuario.equalsIgnoreCase("tots") && autorizacion.equalsIgnoreCase("totes")) {
			oficinas=getOficines();
		} else {
			
			try {
				conn=ToolsBD.getConn();
				String sentenciaSql="SELECT * FROM BAGECOM WHERE FAAFBAJA=0 AND FAACAGCO " +
				"IN (SELECT FZHCAGCO FROM BZAUTOR WHERE FZHCUSU=? AND FZHCAUT=?) ORDER BY FAACAGCO";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setString(1,usuario);
				ps.setString(2,autorizacion);
				rs=ps.executeQuery();
				
				while (rs.next()) {
					oficinas.addElement(String.valueOf(rs.getInt("FAACAGCO")));
					oficinas.addElement(rs.getString("FAADAGCO"));
				}
				
				if (oficinas.size()==0) {
					oficinas.addElement("");
					oficinas.addElement("No hi ha oficines per a l'usuari: "+usuario);
				}
				
			} catch (Exception e) {
				oficinas.addElement("");
				oficinas.addElement("BuscarOficinas Error en la SELECT");
				System.out.println("ERROR: "+usuario);
				e.printStackTrace();
			} finally {
				ToolsBD.closeConn(conn, ps, rs);
			}		
		}
		return oficinas;
	}
	
	
	private Vector getOficinesFisiques() {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector oficinas=new Vector();
		
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BZOFIFIS ORDER BY FZOCAGCO, OFF_CODI";
			ps=conn.prepareStatement(sentenciaSql);
			rs=ps.executeQuery();
			
			while (rs.next()) {
				oficinas.addElement(String.valueOf(rs.getInt("FZOCAGCO")));
				oficinas.addElement(rs.getString("OFF_CODI"));
				oficinas.addElement(rs.getString("OFF_NOM"));
				//log.debug("Afegim: "+String.valueOf(rs.getInt("FAACAGCO"))+" "+rs.getString("FAADAGCO"));
			}
			
			if (oficinas.size()==0) {
				oficinas.addElement("");
				oficinas.addElement("");
				oficinas.addElement("No hi ha oficines!");
			}
			
		} catch (Exception e) {
			oficinas.addElement("");
			oficinas.addElement("");
			oficinas.addElement("BuscarOficinas Error en la SELECT");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return oficinas;
	}
	
	public Vector BuscarOficinasFisicas(String usuario, String autorizacion) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		usuario=usuario.toUpperCase();
		Vector oficinas=new Vector();
		
		if ( usuario.equalsIgnoreCase("tots") && autorizacion.equalsIgnoreCase("totes")) {
			oficinas=getOficinesFisiques();
		} else {
			
			try {
				conn=ToolsBD.getConn();
				String sentenciaSql="SELECT BZOFIFIS.* FROM BZOFIFIS INNER JOIN BAGECOM ON BZOFIFIS.FZOCAGCO=BAGECOM.FAACAGCO WHERE FAAFBAJA=0 AND FAACAGCO " +
				"IN (SELECT FZHCAGCO FROM BZAUTOR WHERE FZHCUSU=? AND FZHCAUT=?) ORDER BY FZOCAGCO,OFF_CODI";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setString(1,usuario);
				ps.setString(2,autorizacion);
				rs=ps.executeQuery();
				
				while (rs.next()) {
					oficinas.addElement(String.valueOf(rs.getInt("FZOCAGCO")));
					oficinas.addElement(String.valueOf(rs.getInt("OFF_CODI")));
					oficinas.addElement(rs.getString("OFF_NOM"));
				}
				
				if (oficinas.size()==0) {
					oficinas.addElement("");
					oficinas.addElement("");
					oficinas.addElement("No hi ha oficines per a l'usuari: "+usuario);
				}
				
			} catch (Exception e) {
				oficinas.addElement("");
				oficinas.addElement("");
				oficinas.addElement("BuscarOficinas Error en la SELECT");
				System.out.println("ERROR: "+usuario);
				e.printStackTrace();
			} finally {
				ToolsBD.closeConn(conn, ps, rs);
			}		
		}
		return oficinas;
	}


	private Vector getOficinesFisiquesDescripcion() {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector oficinas=new Vector();
		
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BZOFIFIS LEFT JOIN BAGECOM ON BZOFIFIS.FZOCAGCO=BAGECOM.FAACAGCO ORDER BY FZOCAGCO, OFF_CODI";
			ps=conn.prepareStatement(sentenciaSql);
			rs=ps.executeQuery();
			
			while (rs.next()) {
				oficinas.addElement(String.valueOf(rs.getInt("FZOCAGCO")));
				oficinas.addElement(rs.getString("OFF_CODI"));
				oficinas.addElement(rs.getString("OFF_NOM"));
				oficinas.addElement(rs.getString("FAADAGCO"));
				//log.debug("Afegim: "+String.valueOf(rs.getInt("FAACAGCO"))+" "+rs.getString("FAADAGCO"));
			}
			
			if (oficinas.size()==0) {
				oficinas.addElement("");
				oficinas.addElement("");
				oficinas.addElement("No hi ha oficines!");
				oficinas.addElement("");
			}
			
		} catch (Exception e) {
			oficinas.addElement("");
			oficinas.addElement("");
			oficinas.addElement("BuscarOficinas Error en la SELECT");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return oficinas;
	}
	
	public Vector BuscarOficinasFisicasDescripcion(String usuario, String autorizacion) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		usuario=usuario.toUpperCase();
		Vector oficinas=new Vector();
		
		if ( usuario.equalsIgnoreCase("tots") && autorizacion.equalsIgnoreCase("totes")) {
			oficinas=getOficinesFisiquesDescripcion();
		} else {
			
			try {
				conn=ToolsBD.getConn();
				String sentenciaSql="SELECT BZOFIFIS.*, BAGECOM.FAADAGCO FROM BZOFIFIS INNER JOIN BAGECOM ON BZOFIFIS.FZOCAGCO=BAGECOM.FAACAGCO WHERE FAAFBAJA=0 AND FAACAGCO " +
				"IN (SELECT FZHCAGCO FROM BZAUTOR WHERE FZHCUSU=? AND FZHCAUT=?) ORDER BY FZOCAGCO,OFF_CODI";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setString(1,usuario);
				ps.setString(2,autorizacion);
				rs=ps.executeQuery();
				
				while (rs.next()) {
					oficinas.addElement(String.valueOf(rs.getInt("FZOCAGCO")));
					oficinas.addElement(String.valueOf(rs.getInt("OFF_CODI")));
					oficinas.addElement(rs.getString("OFF_NOM"));
					oficinas.addElement(rs.getString("FAADAGCO"));
				}
				
				if (oficinas.size()==0) {
					oficinas.addElement("");
					oficinas.addElement("");
					oficinas.addElement("No hi ha oficines per a l'usuari: "+usuario);
					oficinas.addElement("");
				}
				
			} catch (Exception e) {
				oficinas.addElement("");
				oficinas.addElement("");
				oficinas.addElement("BuscarOficinas Error en la SELECT");
				System.out.println("ERROR: "+usuario);
				e.printStackTrace();
			} finally {
				ToolsBD.closeConn(conn, ps, rs);
			}		
		}
		return oficinas;
	}

	public Vector BuscarDocumentos() {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector documentos=new Vector();
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BZTDOCU WHERE FZIFBAJA=0 AND FZICTIPE<>'DU' ORDER BY FZICTIPE";
			ps=conn.prepareStatement(sentenciaSql);
			rs=ps.executeQuery();
			documentos.addElement("");
			documentos.addElement("");
			while (rs.next()) {
				documentos.addElement(rs.getString("FZICTIPE"));
				documentos.addElement(rs.getString("FZICTIPE")+"-"+rs.getString("FZIDTIPE"));
			}
			if (documentos.size()==0) {
				documentos.addElement("");
				documentos.addElement("No hi ha tipus de documents");
			}
		} catch (Exception e) {
			documentos.addElement("");
			documentos.addElement("Error en la SELECT");
			System.out.println("ERROR: ");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return documentos;
	}
	
	public Vector BuscarIdiomas() {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector idiomas=new Vector();
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BZIDIOM";
			ps=conn.prepareStatement(sentenciaSql);
			rs=ps.executeQuery();
			while (rs.next()) {
				idiomas.addElement(rs.getString("FZMCIDI"));
				idiomas.addElement(rs.getString("FZMDIDI"));
			}
			if (idiomas.size()==0) {
				idiomas.addElement("");
				idiomas.addElement("No hi ha idiomes");
			}
		} catch (Exception e) {
			idiomas.addElement("");
			idiomas.addElement("Error en la SELECT");
			System.out.println("ERROR: ");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return idiomas;
	}
	
	public Vector Buscar_060() {
		return Buscar_060("", false);
	}
	public Vector Buscar_060(String valor, boolean todos) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector municipis=new Vector();
		
		municipis.addElement("000");
		if(valor.equals(""))
			municipis.addElement("Cap entitat seleccionada");
		else
			municipis.addElement(valor);
		
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql = "SELECT MUN_CODI, MUN_NOM FROM BZMUN_060 " + 
			((todos)?"":"WHERE MUN_FECBAJ IS NULL OR MUN_FECBAJ = 0 ") +
			"ORDER BY MUN_NOM ASC";
			ps=conn.prepareStatement(sentenciaSql);
			rs=ps.executeQuery();
			while (rs.next()) {
				municipis.addElement(rs.getString("MUN_CODI"));
				municipis.addElement(rs.getString("MUN_NOM"));
			}
			if (municipis.size()==0) {
				municipis=new Vector();
				municipis.addElement("");
				municipis.addElement("No hi ha municipis actius.");
			}
		} catch (Exception e) {
			municipis.addElement("");
			municipis.addElement("Error en la SELECT");
			System.out.println("ERROR: ");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return municipis;
	}
	
	public Vector BuscarBaleares() {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector baleares=new Vector();
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BAGRUGE WHERE FABCTAGG=90 AND FABFBAJA=0 ORDER BY FABDAGGE";
			ps=conn.prepareStatement(sentenciaSql);
			rs=ps.executeQuery();
			baleares.addElement("");
			baleares.addElement(" ");
			while (rs.next()) {
				baleares.addElement(rs.getString("FABCAGGE").toString());
				baleares.addElement(rs.getString("FABDAGGE"));
			}
			if (baleares.size()==1) {
				baleares.addElement("");
				baleares.addElement("No hi ha procendencies de Balears");
			}
		} catch (Exception e) {
			baleares.addElement("");
			baleares.addElement("Error en la SELECT");
			System.out.println("ERROR: ");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return baleares;
	}
	
	public Vector BuscarDestinatarios(String oficina) {
		Vector destino=new Vector();
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BORGANI WHERE FAXFBAJA=0 AND FAXCORGA IN (SELECT FZFCORGA FROM BZOFIOR WHERE FZFCAGCO=?) ORDER BY FAXCORGA";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,oficina);
			rs=ps.executeQuery();
			while (rs.next()) {
				destino.addElement(rs.getString("FAXCORGA").toString());
				destino.addElement(rs.getString("FAXDORGR"));
				destino.addElement(rs.getString("FAXDORGT"));
			}
			if (destino.size()==0) {
				destino.addElement("&nbsp;");
				destino.addElement("No hi ha Organismes Destinataris");
				destino.addElement("&nbsp;");
			}
		} catch (Exception e) {
			destino.addElement("&nbsp;");
			destino.addElement("Error en la SELECT");
			destino.addElement("&nbsp;");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return destino;
	}
	

	public Vector BuscarNoRemision(String oficina) {
		Vector destino=new Vector();
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BORGANI WHERE FAXFBAJA=0 AND FAXCORGA IN (SELECT FZFCORGA FROM BZOFIRE WHERE FZFCAGCO=?) ORDER BY FAXCORGA";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,oficina);
			rs=ps.executeQuery();
			while (rs.next()) {
				destino.addElement(rs.getString("FAXCORGA").toString());
				destino.addElement(rs.getString("FAXDORGR"));
				destino.addElement(rs.getString("FAXDORGT"));
			}
			if (destino.size()==0) {
				destino.addElement("&nbsp;");
				destino.addElement("No hi ha Organismes Destinataris");
				destino.addElement("&nbsp;");
			}
		} catch (Exception e) {
			destino.addElement("&nbsp;");
			destino.addElement("Error en la SELECT");
			destino.addElement("&nbsp;");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return destino;
	}
	
	public String getFecha() {
		DateFormat dateF=new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date fechaTest=new java.util.Date();
		return dateF.format(fechaTest);
	}
	
	@Deprecated
	public String getHorasMinutos() {
		DateFormat dateF=new SimpleDateFormat("HH:mm");
		java.util.Date fechaTest=new java.util.Date();
		return dateF.format(fechaTest);
	}
	
	 public String getHorasMinutosSegundos() {
	    DateFormat dateF=new SimpleDateFormat("HH:mm:ss");
	    java.util.Date fechaTest=new java.util.Date();
	    return dateF.format(fechaTest);
	  }
	
	
	public Vector buscarRemitentes(String subcadenaCodigo, String subcadenaTexto) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector remitentes=new Vector();
		String sentenciaSql="";
		try {
			conn=ToolsBD.getConn();
			sentenciaSql="SELECT * FROM BZENTID WHERE FZGFBAJA=0" +
			((!subcadenaTexto.trim().equals("") || !subcadenaCodigo.trim().equals("")) ? " AND " : "") +
			((!subcadenaTexto.trim().equals("") && !subcadenaCodigo.trim().equals("")) ? "(" : "") +
			((!subcadenaTexto.trim().equals("")) ? "FZGDENT2 LIKE ?" : "") +
			((!subcadenaTexto.trim().equals("") && !subcadenaCodigo.trim().equals("")) ? " OR " : "") +
			((!subcadenaCodigo.trim().equals("") && subcadenaCodigo.trim().length()!=6) ? "FZGCENT2 LIKE ?" : "") +
			((!subcadenaCodigo.trim().equals("") && subcadenaCodigo.trim().length()==6) ? "FZGCENT2 LIKE ? OR FZGCENT2 LIKE ?" : "") +
			((!subcadenaTexto.trim().equals("") && !subcadenaCodigo.trim().equals("")) ? " )" : "") +
			" ORDER BY FZGCENT2, FZGNENTI ";
			ps=conn.prepareStatement(sentenciaSql);
			int contador=1;
			if (!subcadenaTexto.trim().equals("")) {
				ps.setString(contador++, "%"+subcadenaTexto.toUpperCase()+"%");
			}
			if (!subcadenaCodigo.trim().equals("")) {
				if (subcadenaCodigo.length()==7) {
					ps.setString(contador++, subcadenaCodigo.toUpperCase());
				} else if (subcadenaCodigo.length()==6) {
					ps.setString(contador++, subcadenaCodigo.toUpperCase()+"%");
					ps.setString(contador++, subcadenaCodigo.toUpperCase());
				} else{
					ps.setString(contador++, subcadenaCodigo.toUpperCase()+"%");
				}
				
			}
			rs=ps.executeQuery();
			while (rs.next()) {
				remitentes.addElement(rs.getString("FZGCENT2"));
				remitentes.addElement(rs.getString("FZGNENTI").toString());
				remitentes.addElement(rs.getString("FZGDENT2"));
			}
			if (remitentes.size()==0) {
				remitentes.addElement("&nbsp;");
				remitentes.addElement("No hi ha Remitents");
				remitentes.addElement("&nbsp;");
			}
		} catch (Exception e) {
			remitentes.addElement("&nbsp;");
			remitentes.addElement("Error en la SELECT");
			remitentes.addElement("&nbsp;");
			System.out.println("ERROR: ");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return remitentes;
	}
	
	//    public String buscarDisquete(String oficina) {
	public String buscarDisquete(String oficina, String tipo, String fEntrada, String usuario, HttpSession session) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String disquete="";
		String sentenciaSql="SELECT * FROM BZDISQU WHERE FZLCENSA=? AND FZLCAGCO=? AND FZLAENSA=?";
		
		try {
			DateFormat dateF= new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date fechaTest=dateF.parse(fEntrada);
			Calendar cal=Calendar.getInstance();
			cal.setTime(fechaTest);
			
			// Averiguamos si esta bloqueado el numero de disquete
			if (estaBloqueado(oficina, tipo, fEntrada, usuario)) {
				disquete="Numero de disquet no disponible";
			} else {
				if (bloquearDisquete(oficina, tipo, fEntrada, usuario)) {
					session.setAttribute("bloqueoOficina", oficina);
					session.setAttribute("bloqueoTipo", tipo);
					session.setAttribute("bloqueoAnyo", String.valueOf(cal.get(Calendar.YEAR)));
					session.setAttribute("bloqueoUsuario", usuario);
					conn=ToolsBD.getConn();
					ps=conn.prepareStatement(sentenciaSql);
					ps.setString(1, tipo);
					ps.setString(2, oficina);
					ps.setString(3, String.valueOf(cal.get(Calendar.YEAR)));
					rs=ps.executeQuery();
					if  (rs.next()) {
						disquete=rs.getString("FZLNDIS").toString();
						session.setAttribute("bloqueoDisquete", disquete);
					}
				} else {
					disquete="Numero de disquet no disponible";
				}
			}
		} catch (Exception e) {
			disquete="Error en la SELECT";
			System.out.println("ERROR: ");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return disquete;
	}
	/** Devuelve un String con la descricpion del organismo destinatario
	 *
	 * @param Organismo
	 *
	 * @return String
	 */
	public String recuperarDestinatario(String organismo) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String descripcionDestinatario=null;
		if (organismo != null && !"".equals(organismo) ) {
			// Només anam a la BBDD si ens passen algun organisme.
			try {
				conn=ToolsBD.getConn();
				String sentenciaSql="SELECT * FROM BORGANI WHERE FAXCORGA=? ";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setString(1,organismo);
				rs=ps.executeQuery();
				if (rs.next()) {
					/* IMPORTANT: La descripció del destinatari està a faxdorgT , faxdorgr és el text curt! SMR: 7/2/2007*/
					descripcionDestinatario=rs.getString("FAXDORGT");
				} else {
					descripcionDestinatario="Destinatari inexistent";
				}
			} catch (Exception e) {
				descripcionDestinatario="Destinatari inexistent";
				if (organismo!=null)
					System.out.println("recuperarDestinatario ERROR: Destinatari de l'organisme "+organismo+" inexistent ");
				else
					System.out.println("recuperarDestinatario ERROR: Destinatari de l'organisme NULL inexistent ");
				//e.printStackTrace();
			} finally {
				ToolsBD.closeConn(conn, ps, rs);
			}
			
		} else {
			descripcionDestinatario="Destinatari inexistent";
		}
		return descripcionDestinatario;
	}
	
	/** Devuelve en String la descripcion de la Entidad Remitente
	 *
	 * @param String Entidad1
	 * @param String Entidad2
	 *
	 * @return String
	 */
	public String recuperaRemitente(String entidad1, String entidad2) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String descripcionRemitente=null;
		
		if (entidad2.equals("")) {
			entidad2="0";
		}
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BZENTID WHERE FZGCENT2=? AND FZGNENTI=? AND FZGFBAJA=0";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,entidad1.toUpperCase());
			ps.setInt(2,Integer.parseInt(entidad2));
			rs=ps.executeQuery();
			if (rs.next()) {
				descripcionRemitente=rs.getString("FZGDENT2");
			} else {
				descripcionRemitente="Remitent inexistent";
			}
		} catch (Exception e) {
			descripcionRemitente="Remitent inexistent";
			if (entidad1!=null && entidad2!=null)
				System.out.println("ERROR: En recuperar el remitent de l'entitat "+entidad1+"-"+entidad2+".");
			else
				System.out.println("ERROR: En recuperar el remitent de l'entitat NULL-NULL inexistent.");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return descripcionRemitente;
	}
	public String recuperaRemitenteCastellano(String entidad1, String entidad2) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String descripcionRemitente=null;
		
		if (entidad2.equals("")) {
			entidad2="0";
		}
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BZENTID WHERE FZGCENTI=? AND FZGNENTI=? AND FZGFBAJA=0";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,entidad1.toUpperCase());
			ps.setInt(2,Integer.parseInt(entidad2));
			rs=ps.executeQuery();
			if (rs.next()) {
				descripcionRemitente=rs.getString("FZGDENTI");
			} else {
				descripcionRemitente="Remitent inexistent";
			}
		} catch (Exception e) {
			descripcionRemitente="Remitent inexistent";
			if (entidad1!=null && entidad2!=null)
				System.out.println("ERROR: En recuperar el remitent de l'entitat "+entidad1+"-"+entidad2+".");
			else
				System.out.println("ERROR: En recuperar el remitent de l'entitat NULL-NULL inexistent.");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return descripcionRemitente;
	}
	
	
	/** Devuelve en String la descripcion de la Oficina
	 *
	 * @param String Oficina
	 *
	 * @return String
	 */
	public String recuperaDescripcionOficina(String oficina) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String descripcionOficina=null;
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BAGECOM WHERE FAACAGCO=? ";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setInt(1,Integer.parseInt(oficina));
			rs=ps.executeQuery();
			
			if (rs.next()) {
				descripcionOficina=rs.getString("FAADAGCO");
			} else {
				descripcionOficina="Oficina inexistente";
			}
		} catch (Exception e) {
			descripcionOficina="Oficina inexistente";
			System.out.println("ERROR: ");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return descripcionOficina;
	}
	
	/** Devuelve en String la descripcion de la Oficina Fisica
	 *
	 * @param String Oficina
	 *
	 * @return String
	 */
	public String recuperaDescripcionOficinaFisica(String oficina, String oficinafisica) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String descripcionOficina=null;
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BZOFIFIS WHERE FZOCAGCO=? AND OFF_CODI=? ";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setInt(1,Integer.parseInt(oficina));
			ps.setInt(2,Integer.parseInt(oficinafisica));
			rs=ps.executeQuery();
			
			if (rs.next()) {
				descripcionOficina=rs.getString("OFF_NOM");
			} else {
				descripcionOficina="Oficina inexistente";
			}
		} catch (Exception e) {
			descripcionOficina="Oficina inexistente";
			System.out.println("ERROR: ");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return descripcionOficina;
	}
	public static boolean estaBloqueado(String oficina, String tipo, String fEntrada, String usuario) {
		
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean bloqueado=false;
		
		try {
			String sentenciaSql="SELECT * FROM BZBLOQU WHERE FZNCENSA=? AND FZNAENSA=? AND FZNCAGCO=? "; // CHANGED quitado "with rs" para compatibilidad
			DateFormat dateF= new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date fechaTest=dateF.parse(fEntrada);
			Calendar cal=Calendar.getInstance();
			cal.setTime(fechaTest);
			
			conn=ToolsBD.getConn();
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1, tipo);
			ps.setString(2, String.valueOf(cal.get(Calendar.YEAR)));
			ps.setString(3, oficina);
			rs=ps.executeQuery();
			
			if  (rs.next()) {
				if ((rs.getString("FZNCUSU")==null) || (rs.getString("FZNCUSU").trim().equals(""))) {
					bloqueado=false;
				} else {
					bloqueado=true;
				}
			} else {
				bloqueado=false;
			}
			
		} catch (Exception e) {
			System.out.println("estaBloqueado: ERROR: ");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return bloqueado;
	}
	
	public static boolean bloquearDisquete(String oficina, String tipo, String fEntrada, String usuario) {
		
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		PreparedStatement ts = null;
		boolean bloqueado=false;
		
		try {
			String sentenciaSql="SELECT * FROM BZBLOQU WHERE FZNCENSA=? AND FZNAENSA=? AND FZNCAGCO=? "; // CHANGED quitado "with rs" para compatibilidad
			DateFormat dateF= new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date fechaTest=dateF.parse(fEntrada);
			Calendar cal=Calendar.getInstance();
			cal.setTime(fechaTest);
			
			conn=ToolsBD.getConn();
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1, tipo);
			ps.setString(2, String.valueOf(cal.get(Calendar.YEAR)));
			ps.setString(3, oficina);
			rs=ps.executeQuery();
			
			if  (rs.next()) {
				if ((rs.getString("FZNCUSU")==null) || (rs.getString("FZNCUSU").trim().equals(""))) {
					sentenciaSql="UPDATE BZBLOQU SET FZNCUSU=? WHERE FZNCENSA=? AND FZNAENSA=? AND FZNCAGCO=?";
					ts=conn.prepareStatement(sentenciaSql);
					ts.setString(1,usuario);
					ts.setString(2,tipo);
					ts.setString(3, String.valueOf(cal.get(Calendar.YEAR)));
					ts.setString(4, oficina);
					boolean cualquiera=ts.execute();
					ts.close();
					bloqueado=true;
				} else {
					bloqueado=false;
				}
			} else {
				sentenciaSql="INSERT INTO BZBLOQU (FZNCENSA, FZNAENSA, FZNCAGCO, FZNCUSU) VALUES(?,?,?,?)";
				ts=conn.prepareStatement(sentenciaSql);
				ts.setString(1,tipo);
				ts.setString(2, String.valueOf(cal.get(Calendar.YEAR)));
				ts.setString(3, oficina);
				ts.setString(4,usuario);
				boolean cualquiera=ts.execute();
				ts.close();
				bloqueado=true;
			}
			
		} catch (Exception e) {
			bloqueado=false;
			System.out.println("bloquearDisquete ERROR: ");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return bloqueado;
	}
	
	public static boolean liberarDisquete(String oficina, String tipo, String anyo, String usuario) {
		
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		PreparedStatement ts = null;
		boolean liberado=false;
		
		try {
			String sentenciaSql="SELECT * FROM BZBLOQU WHERE FZNCENSA=? AND FZNAENSA=? AND FZNCAGCO=? "; // CHANGED quitado "with rs" para compatibilidad
			conn=ToolsBD.getConn();
			
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1, tipo);
			ps.setString(2, anyo);
			ps.setString(3, oficina);
			rs=ps.executeQuery();
			
			if  (rs.next()) {
				if ((rs.getString("FZNCUSU")!=null) || (rs.getString("FZNCUSU").toUpperCase().equals(usuario))) {
					sentenciaSql="UPDATE BZBLOQU SET FZNCUSU='' WHERE FZNCENSA=? AND FZNAENSA=? AND FZNCAGCO=?";
					ts=conn.prepareStatement(sentenciaSql);
					ts.setString(1,tipo);
					ts.setString(2, anyo);
					ts.setString(3, oficina);
					boolean cualquiera=ts.execute();
					ts.close();
					liberado=true;
				} else {
					liberado=false;
				}
			} else {
				liberado=false;
			}
			
		} catch (Exception e) {
			liberado=false;
			System.out.println("liberarDisquete ERROR: ");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return liberado;
	}
	
	public String recuperarTipoDocumento(String tipo) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String descripcionDocumento="";
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BZTDOCU WHERE FZIFBAJA=0 AND FZICTIPE=?";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1, tipo);
			rs=ps.executeQuery();
			if (rs.next()) {
				descripcionDocumento=rs.getString("FZICTIPE")+" - "+rs.getString("FZIDTIPE");
			} else {
				descripcionDocumento=tipo+" - "+"Error en la SELECT";
			}
		} catch (Exception e) {
			descripcionDocumento=tipo+" - "+"Error en la SELECT";
			System.out.println("ERROR: ");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return descripcionDocumento;
	}
	

	private Vector getModels() {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector models=new Vector();
		
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT MOF_NOM FROM BZMODOF ORDER BY MOF_NOM";
			ps=conn.prepareStatement(sentenciaSql);
			rs=ps.executeQuery();
			
			while (rs.next()) {
				models.addElement(rs.getString("MOF_NOM"));
				//log.debug("Afegim: "+String.valueOf(rs.getInt("FAACAGCO"))+" "+rs.getString("FAADAGCO"));
			}
			
			if (models.size()==0) {
				models.addElement("No hi ha models!");
			}
			
		} catch (Exception e) {
			models.addElement("");
			models.addElement("BuscarModels Error en la SELECT");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return models;
	}
	
	public Vector BuscarModelos(String usuario, String autorizacion) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		usuario=usuario.toUpperCase();
		Vector models=new Vector();
		
		if ( usuario.equalsIgnoreCase("tots") && autorizacion.equalsIgnoreCase("totes")) {
			models=getModels();
		}
		return models;
	}
	
	
	


	private Vector getModelsRebuts() {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector models=new Vector();
		
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT MOR_NOM FROM BZMODREB ORDER BY MOR_NOM";
			ps=conn.prepareStatement(sentenciaSql);
			rs=ps.executeQuery();
			
			while (rs.next()) {
				models.addElement(rs.getString("MOR_NOM"));
				//log.debug("Afegim: "+String.valueOf(rs.getInt("FAACAGCO"))+" "+rs.getString("FAADAGCO"));
			}
			
			if (models.size()==0) {
				models.addElement("No hi ha models!");
			}
			
		} catch (Exception e) {
			models.addElement("");
			models.addElement("BuscarModels Error en la SELECT");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return models;
	}
	
	public Vector BuscarModelosRecibos(String usuario, String autorizacion) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		usuario=usuario.toUpperCase();
		Vector models=new Vector();
		
		if ( usuario.equalsIgnoreCase("tots") && autorizacion.equalsIgnoreCase("totes")) {
			models=getModelsRebuts();
		}
		return models;
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