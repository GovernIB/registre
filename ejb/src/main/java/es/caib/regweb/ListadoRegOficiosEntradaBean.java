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
 * Bean que genera els llistats de registres d'entrada pendents de remisio
 * @author  AROGEL
 * @version 1.0
 */

public class ListadoRegOficiosEntradaBean implements SessionBean {
	
	private SessionContext contextoSesion;
	
	private String oficina="";
	private String oficinaFisica="";
	
	/** Creates a new instance of Valores */
	public ListadoRegOficiosEntradaBean() {
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
			String sentenciaSql="SELECT * FROM BZENTRA LEFT JOIN BAGECOM ON FAACAGCO=FZACAGCO " +
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

			//ps=conn.prepareStatement(sentenciaSql);
			ps=conn.prepareStatement(sentenciaSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			//ps.setMaxRows((50)+1);

			ps.setString(1,usuario);
			ps.setString(2,"AS");
			if (oficina!=null && !oficina.equals("00")) {
    			ps.setInt(3, Integer.parseInt(oficina));
    			if (oficinaFisica!=null && !oficinaFisica.equals("")) {
        			ps.setInt(4, Integer.parseInt(oficinaFisica));
    			}
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

                registro.setOficinaFisica(String.valueOf(rs.getInt("OFE_CODI")));
                String textoOficinaFisica=rs.getString("OFF_NOM");
                if (textoOficinaFisica==null) {textoOficinaFisica=" ";}
                registro.setDescripcionOficinaFisica(textoOficinaFisica);
				
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
				if (rs.getString("FZACIDIO").equals("1")) {
					registro.setExtracto(rs.getString("FZACONEN"));
				} else {
					registro.setExtracto(rs.getString("FZACONE2"));
				}
				if (rs.getString("FABDAGGE")==null) {
					registro.setDescripcionGeografico(rs.getString("FZAPROCE"));
				} else {
					registro.setDescripcionGeografico(rs.getString("FABDAGGE"));
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