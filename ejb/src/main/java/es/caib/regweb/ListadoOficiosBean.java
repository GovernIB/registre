/*
 * Created on 6 de agosto de 2009, 10:56
 */

package es.caib.regweb;

import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.*;

import java.rmi.*;

import javax.ejb.*; 

/**
 * Bean que genera els llistats d'oficis de remisió 
 * @author  AROGEL
 * @version 1.0
 */

public class ListadoOficiosBean implements SessionBean {
	
	private SessionContext contextoSesion;
	
	private String oficina="";
	private String oficinaFisica="";
	private String anyo="";
	private Hashtable errores=new Hashtable();
	private boolean error=false;

	/** Creates a new instance of Valores */
	public ListadoOficiosBean() {
	}

	public void setOficina(String oficina) {
		this.oficina=oficina;
	}

	public String getOficina() {
		return this.oficina;
	}
	
	public void setOficinaFisica(String oficinaFisica) {
		this.oficinaFisica=oficinaFisica;
	}

	public String getOficinaFisica() {
		return this.oficinaFisica;
	}
	
	public void setAnyo(String anyo) {
		this.anyo=anyo;
	}

	public String getAnyo() {
		return this.anyo;
	}
	

	/**
	 * @return
	 */
	public Hashtable getErrores() {
		return errores;
	}
	
	
	/**
	 * @param fecha
	 */
	
	public void inizializar() {
		oficina="";
		oficinaFisica="";
		anyo="";
	}
	
	public Vector recuperarRegistros(String usuario)
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
			String sentenciaSql= "SELECT * FROM BZOFREM  " +
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
			//ps=conn.prepareStatement(sentenciaSql);
			ps=conn.prepareStatement(sentenciaSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//ps.setMaxRows((50)+1);

			ps.setString(1,usuario);
			// Modificado por V.Herrera 18/09/2009. CAmbio del permiso CS (Consulta Salida) a AE(Alta entradas)
			ps.setString(2,"CE");
			int contador = 3;
			if (oficina!=null && !oficina.equals("00")) {
    			ps.setInt(contador++, Integer.parseInt(oficina));
			}
			if (anyo!=null && !anyo.equals("")) {
    			ps.setInt(contador++, Integer.parseInt(anyo));
			}
			if (oficinaFisica!=null && !oficinaFisica.equals("")) {
    			ps.setInt(contador++, Integer.parseInt(oficinaFisica));
			}

			rs=ps.executeQuery();
			while (rs.next()) {
				RegistroSeleccionado registro=new RegistroSeleccionado();
				
				registro.setAnoEntrada(String.valueOf(rs.getInt("FZSANOEN")));
				registro.setNumeroEntrada(String.valueOf(rs.getInt("FZSNUMEN")));
				registro.setOficina(String.valueOf(rs.getInt("FAACAGCO")));
				String textoOficina=rs.getString("FAADAGCO");
				if (textoOficina==null) {textoOficina=" ";}
				registro.setDescripcionOficina(textoOficina);
				registro.setOficinaFisica(rs.getString("OFS_CODI"));
				registro.setDescripcionOficinaFisica(rs.getString("OFF_NOM"));

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
				if (rs.getString("FZSCIDIO").equals("1")) {
					registro.setExtracto(rs.getString("FZSCONEN"));
				} else {
					registro.setExtracto(rs.getString("FZSCONE2"));
				}
				if (rs.getString("FABDAGGE")==null) {
					registro.setDescripcionGeografico(rs.getString("FZSPROCE"));
				} else {
					registro.setDescripcionGeografico(rs.getString("FABDAGGE"));
				}
				registro.setOficio(rs.getString("REM_OFNUM"));
				registro.setDescripcionOrganismoDestinatario(rs.getString("FAXDORGR"));
				registro.setDescripcionDocumento(rs.getString("FZIDTIPE"));
				registro.setDescripcionIdiomaDocumento(rs.getString("FZMDIDI"));
				registro.setRegistroAnulado(rs.getString("REM_NULA"));
				registrosVector.addElement(registro);
			}
		} catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
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
	}
	
}