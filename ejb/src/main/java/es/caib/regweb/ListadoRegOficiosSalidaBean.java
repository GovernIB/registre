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
 * Bean que genera els llistats d'oficis de remisió pendents d'arribar
 * @author  AROGEL
 * @version 1.0
 */

public class ListadoRegOficiosSalidaBean implements SessionBean {
	
	private SessionContext contextoSesion;
	
	private String oficina="";
	private Hashtable errores=new Hashtable();
	private boolean error=false;

	/** Creates a new instance of Valores */
	public ListadoRegOficiosSalidaBean() {
	}

	public void setOficina(String oficina) {
		this.oficina=oficina;
	}

	public String getOficina() {
		return this.oficina;
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
				"  AND FZSNENTI IN (SELECT FZHCAGCO FROM BZAUTOR WHERE FZHCUSU=? AND FZHCAUT=? " + (oficina==null || oficina.equals("00")?"":" AND FZHCAGCO=? ") + ")" +
				 //" AND REM_SALDES='N' " +
				 " AND (REM_ENTDES IS NULL OR REM_ENTDES='N' OR REM_ENTDES='') " +
				 " AND (REM_NULA IS NULL OR REM_NULA='N' OR REM_NULA='') " +
				 " AND REM_ENTFEC=0 " +
				 " AND NOT FZSENULA='S' " +
				 " ORDER BY FZSCAGCO, FZSANOEN, FZSNUMEN ";
			//ps=conn.prepareStatement(sentenciaSql);
			ps=conn.prepareStatement(sentenciaSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//ps.setMaxRows((50)+1);

			ps.setString(1,usuario);
			// Modificado por V.Herrera 18/09/2009. CAmbio del permiso CS (Consulta Salida) a AE(Alta entradas)
			ps.setString(2,"AE");
			if (oficina!=null && !oficina.equals("00")) {
    			ps.setInt(3, Integer.parseInt(oficina));
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
				registro.setRegistroAnulado(rs.getString("FZSENULA"));
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
    	Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			conn=ToolsBD.getConn();
				String sentenciaSql="INSERT INTO BZENLPD (FZTTIPAC, FZTCUSU, FZTDATAC, FZTHORAC, FZTNUMEN, FZTANOEN," +
					" FZTCAGCO) VALUES (?,?,?,?,?,?,?)";
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
			System.out.println("ERROR: S'ha produ\357t un error a logLopdBZENTRA");
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		//System.out.println("ListadoRegistrosEntradaBean: Desada informació dins BZENLPD: "+tipusAcces+" "+usuari+" "+data+" "+hora+" "+nombreRegistre+" "+any+" "+oficina);
     }
}