package es.caib.regweb.admin;

/*import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.*;
import java.text.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.naming.*;
*/
import java.io.BufferedReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.rmi.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.ejb.*;

import org.apache.log4j.Category;

import es.caib.regweb.EsborraRegAnticsLopdBean;
import es.caib.regweb.RegistroEntrada;
import es.caib.regweb.RegistroEntradaBean;
import es.caib.regweb.RegistroEntradaHome;
import es.caib.regweb.RegistroSalidaBean;
import es.caib.regweb.ToolsBD;
import es.caib.regweb.RegwebException;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Vector;
 

/**
 * Bean que gestiona la autorització d'usuaris.
 * @author  Sebastià Matas
 * @version 1.0
 */

public class AdminBean implements SessionBean {
    
    private SessionContext context=null;
    private static final Category log = Category.getInstance(AdminBean.class.getName());
      
    public AdminBean() {
    }

    /**
     * Retorna un vector amb les entitats similars o iguals al codi o descripció donat.
     * @param subcadenaCodigo Codi a cercar
     * @param subcadenaTexto Descripció a cercar
     * @return Vector amb la informació de les entitats trobades.
     * @throws RemoteException
     */
	public Vector getEntitats(String subcadenaCodigo, String subcadenaTexto) throws RemoteException {
		//Copy & paste de ValoresBean.buscarRemitentes però torna el codi en castellà ja que és la clau primària i sense
		// mirar si està de baixa o no.
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector remitentes=new Vector();
		String sentenciaSql="";
		try {
			conn=ToolsBD.getConn();
			sentenciaSql="SELECT * FROM BZENTID WHERE 1=1 " +
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
				remitentes.addElement(rs.getString("FZGCENTI"));
				remitentes.addElement(rs.getString("FZGNENTI").toString());
				remitentes.addElement(rs.getString("FZGDENT2"));
			}
			if (remitentes.size()==0) {
				remitentes.addElement("");
				remitentes.addElement("No hi ha Remitents");
				remitentes.addElement("");
			}
		} catch (Exception e) {
			remitentes.addElement("");
			remitentes.addElement("Error en la SELECT");
			remitentes.addElement(";");
			System.out.println("ERROR: ");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return remitentes;
	}

	
	/**
	 * Retorna la informació de l'entitat donada
	 * @param codiEntidad Codi de l'entitat 
	 * @param subcodiEntitat Subcodi de l'entitat
	 * @return Dades de l'entitat encapsulades dins la classe EntitatData
	 * @throws RemoteException
	 * @throws RegwebException
	 */
	public EntitatData getEntitat( String codiEntidad, String subcodiEntitat )  throws RemoteException, RegwebException { 
		//Important! Encara que la clau primària sigui el primer camp (FZGCENTI) les consultes es fan sobre FZGCENT2 (el mateix
		//codi però en català
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		EntitatData entitat = new EntitatData();
		
			try {
				log.debug("Cercam: "+codiEntidad+" "+subcodiEntitat);
				conn=ToolsBD.getConn();
				String sentenciaSql="SELECT * FROM BZENTID WHERE " +
						" FZGCENTI=? AND FZGNENTI=? ";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setString(1,codiEntidad);
				ps.setString(2,subcodiEntitat);
				rs=ps.executeQuery();
				
				while (rs.next()) {
					entitat.setCodigoEntidad( rs.getString("FZGCENTI") );
					entitat.setCodiEntitat(rs.getString("FZGCENT2"));
					entitat.setSubcodiEnt(String.valueOf(rs.getInt("FZGNENTI")));
					entitat.setDescEntidad(rs.getString("FZGDENTI").trim());
					entitat.setDescEntitat(rs.getString("FZGDENT2").trim());
					
			        DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
			        DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
			        java.util.Date data=null;
					String dataBaixa=String.valueOf(rs.getInt("FZGFBAJA"));
	                try {
	                	data=yyyymmdd.parse(dataBaixa);
	                	entitat.setDataBaixa(ddmmyyyy.format(data));
	                } catch (Exception e) {
	                	entitat.setDataBaixa(dataBaixa);
	                }
					log.debug("Afegim: "+entitat.getCodigoEntidad()+" "+entitat.getDescEntitat());
					entitat.toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
				ToolsBD.closeConn(conn, ps, rs);
				throw new RegwebException(e.getMessage());
			} finally {
				ToolsBD.closeConn(conn, ps, rs);
			}
		return entitat;
	}

	/**
	 * Retorna el valor del comptador de l'oficina, any i tipus d'entrada/sortida donats
	 * @param codiOficina Codi de l'oficina a cercar
	 * @param ES String que pot ser "E" per entrades i "S" per sortides
	 * @param any Any del comptador
	 * @return Valor del comptador (null si no existeix)
	 * @throws RemoteException
	 * @throws RegwebException
	 */
	public String getComptadorOficina( String codiOficina, String ES, String any )  throws RemoteException, RegwebException {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
			try {
				//log.debug("Cercam el comptador de l'oficina: "+codiOficina);
				conn=ToolsBD.getConn();
				String sentenciaSql="SELECT * FROM BZCONES WHERE " +
						" FZDAENSA=? AND FZDCENSA=? AND FZDCAGCO=? ";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setString(1,any);
				ps.setString(2,ES);
				ps.setString(3,codiOficina);
				rs=ps.executeQuery();
				
				if (rs.next()) {
					//log.debug("Comptador de l'oficina: "+codiOficina+" ES: "+ES+" i any: "+any+" és: "+rs.getInt("FZDNUMER"));
					return String.valueOf(rs.getInt("FZDNUMER"));
				} else
					return "";
			} catch (Exception e) {
				e.printStackTrace();
				ToolsBD.closeConn(conn, ps, rs);
				throw new RegwebException(e.getMessage());
			} finally {
				ToolsBD.closeConn(conn, ps, rs);
			}
	}

	/** 
	 * Torna un vector amb les dades de l'organisme
	 * @param Codi organisme Codi de l'organisme a llegir
	 * @return Vector que conté la informació de l'organisme
	 */
	public Vector getOrganisme( String codiOrganisme)  throws RemoteException{
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector organismes=new Vector();
		
			try {
				conn=ToolsBD.getConn();
				String sentenciaSql="SELECT * FROM BORGANI WHERE FAXCORGA=? " +
						"ORDER BY FAXCORGA";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setString(1,codiOrganisme);
				rs=ps.executeQuery();
				
				while (rs.next()) {
					organismes.addElement(String.valueOf(rs.getInt("FAXCORGA")));
					organismes.addElement(rs.getString("FAXDORGR").trim());
					organismes.addElement(rs.getString("FAXDORGT").trim());
					
					String dataBaixa=String.valueOf(rs.getInt("FAXFBAJA"));
					if (dataBaixa.length()==5)
						dataBaixa="0"+dataBaixa;
					
					DateFormat yymmdd=new SimpleDateFormat("yyMMdd");
			        DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
			        java.util.Date data=null;
			        
					try {
	                	data=yymmdd.parse(dataBaixa);
	                	organismes.addElement(ddmmyyyy.format(data));
	                } catch (Exception e) {
	                	organismes.addElement(dataBaixa);
	                }
					//log.debug("Afegim: "+String.valueOf(rs.getInt("FAXCORGA"))+" "+rs.getString("FAXDORGT"));
				}
				if (organismes.size()==0) {
					organismes.addElement("&nbsp;");
					organismes.addElement("");
					organismes.addElement("Error, no hi ha organismes!");
					organismes.addElement("");
				}
			} catch (Exception e) {
				organismes.addElement("&nbsp;");
				organismes.addElement("");
				organismes.addElement("getOrganismes Error en la SELECT");
				organismes.addElement("");
				e.printStackTrace();
			} finally {
				ToolsBD.closeConn(conn, ps, rs);
			}
		return organismes;
	}
	
	/**
	 * Retorna els organismes existents a la BBDD que no estan de baixa.
	 * @return Vector amb els organismes trobats
	 * @throws RemoteException
	 */
	public Vector getOrganismes( )  throws RemoteException{
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector organismes=new Vector();
		
			try {
				conn=ToolsBD.getConn();
				String sentenciaSql="SELECT * FROM BORGANI WHERE FAXFBAJA=0 ORDER BY FAXCORGA";
				ps=conn.prepareStatement(sentenciaSql);
				rs=ps.executeQuery();
				
				while (rs.next()) {
					organismes.addElement(String.valueOf(rs.getInt("FAXCORGA")));
					organismes.addElement(rs.getString("FAXDORGR").trim());
					organismes.addElement(rs.getString("FAXDORGT").trim());
					//log.debug("Afegim: "+String.valueOf(rs.getInt("FAXCORGA"))+" "+rs.getString("FAXDORGT"));
				}
				if (organismes.size()==0) {
					organismes.addElement("&nbsp;");
					organismes.addElement("");
					organismes.addElement("Error, no hi ha organismes!");
				}
			} catch (Exception e) {
				organismes.addElement("&nbsp;");
				organismes.addElement("");
				organismes.addElement("getOrganismes Error en la SELECT");
				e.printStackTrace();
			} finally {
				ToolsBD.closeConn(conn, ps, rs);
			}
		return organismes;
	}
    
	/**
	 * Retorna tots els organismes existents a la BBDD, estiguin de baixa o no.
	 * @return Vector amb tots els organismes
	 * @throws RemoteException
	 */
	public Vector getTotsOrganismes( )  throws RemoteException{
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector organismes=new Vector();
		
			try {
				conn=ToolsBD.getConn();
				String sentenciaSql="SELECT * FROM BORGANI ORDER BY FAXCORGA";
				ps=conn.prepareStatement(sentenciaSql);
				rs=ps.executeQuery();
				
				while (rs.next()) {
					organismes.addElement(String.valueOf(rs.getInt("FAXCORGA")));
					organismes.addElement(rs.getString("FAXDORGR").trim());
					organismes.addElement(rs.getString("FAXDORGT").trim());
					//log.debug("Afegim: "+String.valueOf(rs.getInt("FAXCORGA"))+" "+rs.getString("FAXDORGT"));
				}
				if (organismes.size()==0) {
					organismes.addElement("&nbsp;");
					organismes.addElement("");
					organismes.addElement("Error, no hi ha organismes!");
				}
			} catch (Exception e) {
				organismes.addElement("&nbsp;");
				organismes.addElement("");
				organismes.addElement("getOrganismes Error en la SELECT");
				e.printStackTrace();
			} finally {
				ToolsBD.closeConn(conn, ps, rs);
			}
		return organismes;
	}
    
	/**
	 * Retorna l'oficina donada
	 * @param oficina codi de l'oficina a cercar
	 * @return Vector amb la informació de l'oficina donada.
	 * @throws RemoteException
	 */
	public Vector getOficina(String oficina)  throws RemoteException{
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector oficinas=new Vector();
		
			try {
				conn=ToolsBD.getConn();
				String sentenciaSql="SELECT * FROM BAGECOM WHERE FAACAGCO = ? ";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setString(1,oficina);
				rs=ps.executeQuery();
				
				while (rs.next()) {
					oficinas.addElement(String.valueOf(rs.getInt("FAACAGCO")));
					oficinas.addElement(rs.getString("FAADAGCO").trim());
					
					String dataBaixa=String.valueOf(rs.getInt("FAAFBAJA"));
					if (dataBaixa.length()==5)
						dataBaixa="0"+dataBaixa;
					
					DateFormat yymmdd=new SimpleDateFormat("yyMMdd");
			        DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
			        java.util.Date data=null;
			        
					try {
	                	data=yymmdd.parse(dataBaixa);
	                	oficinas.addElement(ddmmyyyy.format(data));
	                } catch (Exception e) {
	                	oficinas.addElement(dataBaixa);
	                }
	                
					//log.debug("Afegim: "+String.valueOf(rs.getInt("FAACAGCO"))+" "+rs.getString("FAADAGCO"));
				}
				
				if (oficinas.size()==0) {
					oficinas.addElement("");
					oficinas.addElement("Oficina "+oficina+" no existeix");
					oficinas.addElement("");
				}
				
			} catch (Exception e) {
				oficinas.addElement("");
				oficinas.addElement("BuscarOficinas Error en la SELECT");
				oficinas.addElement("");
				e.printStackTrace();
			} finally {
				ToolsBD.closeConn(conn, ps, rs);
			}
		return oficinas;
	}
	


	/**
	 * Retorna l'oficina fisica donada
	 * @param oficina codi de l'oficina a cercar
	 * @param oficinaFisica codi de l'oficina fisica a cercar
	 * @return Vector amb la informació de l'oficina donada.
	 * @throws RemoteException
	 */
	public Vector getOficinaFisica(String oficina, String oficinaFisica)  throws RemoteException{
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector oficinas=new Vector();
		
			try {
				conn=ToolsBD.getConn();
				String sentenciaSql="SELECT * FROM BZOFIFIS WHERE FZOCAGCO = ? AND OFF_CODI = ? ";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setString(1,oficina);
				ps.setString(2,oficinaFisica);
				rs=ps.executeQuery();
				
				while (rs.next()) {
					oficinas.addElement(String.valueOf(rs.getInt("FZOCAGCO")));
					oficinas.addElement(String.valueOf(rs.getInt("OFF_CODI")));
					oficinas.addElement(rs.getString("OFF_NOM").trim());
					//oficinas.addElement(rs.getString("off_nom").trim());
					
//					String dataBaixa=String.valueOf(rs.getInt("FAAFBAJA"));
//					if (dataBaixa.length()==5)
//						dataBaixa="0"+dataBaixa;
//					
//					DateFormat yymmdd=new SimpleDateFormat("yyMMdd");
//			        DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
//			        java.util.Date data=null;
//			        
//					try {
//	                	data=yymmdd.parse(dataBaixa);
//	                	oficinas.addElement(ddmmyyyy.format(data));
//	                } catch (Exception e) {
//	                	oficinas.addElement(dataBaixa);
//	                }
	                
					//log.debug("Afegim: "+String.valueOf(rs.getInt("FAACAGCO"))+" "+rs.getString("FAADAGCO"));
				}
				
				if (oficinas.size()==0) {
					oficinas.addElement("");
					oficinas.addElement("");
					oficinas.addElement("Oficina "+oficina+" no existeix");
					//oficinas.addElement("");
				}
				
			} catch (Exception e) {
				oficinas.addElement("");
				oficinas.addElement("");
				oficinas.addElement("BuscarOficinas Error en la SELECT");
				//oficinas.addElement("");
				e.printStackTrace();
			} finally {
				ToolsBD.closeConn(conn, ps, rs);
			}
		return oficinas;
	}
	

	/**
	 * Retorna el model d'ofici donat
	 * @param oficina codi del model d'ofici a cercar
	 * @return Vector amb la informació del model d'ofici donat.
	 * @throws RemoteException
	 */
	public Vector getModelOfici(String model)  throws RemoteException{
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector models=new Vector();
		
			try {
				conn=ToolsBD.getConn();
				String sentenciaSql="SELECT MOF_NOM, MOF_CONTYP FROM BZMODOF WHERE MOF_NOM = ? ";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setString(1,model);
				rs=ps.executeQuery();
				
				while (rs.next()) {
					models.addElement(rs.getString("MOF_NOM").trim());
					models.addElement(rs.getString("MOF_CONTYP").trim());
	                
					//log.debug("Afegim: "+String.valueOf(rs.getInt("FAACAGCO"))+" "+rs.getString("FAADAGCO"));
				}
				
				if (models.size()==0) {
					models.addElement("");
					models.addElement("Model d'ofici "+model+" no existeix");
				}
				
			} catch (Exception e) {
				models.addElement("");
				models.addElement("BuscarModel Error en la SELECT");
				e.printStackTrace();
			} finally {
				ToolsBD.closeConn(conn, ps, rs);
			}
		return models;
	}

	/**
	 * Retorna el model de rebut donat
	 * @param oficina codi del model de rebut
	 * @return Vector amb la informació del model de rebut
	 * @throws RemoteException
	 */
	public Vector getModelRebut(String model)  throws RemoteException{
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector models=new Vector();
		
			try {
				conn=ToolsBD.getConn();
				String sentenciaSql="SELECT MOR_NOM, MOR_CONTYP FROM BZMODREB WHERE MOR_NOM = ? ";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setString(1,model);
				rs=ps.executeQuery();
				
				while (rs.next()) {
					models.addElement(rs.getString("MOR_NOM").trim());
					models.addElement(rs.getString("MOR_CONTYP").trim());
	                
					//log.debug("Afegim: "+String.valueOf(rs.getInt("FAACAGCO"))+" "+rs.getString("FAADAGCO"));
				}
				
				if (models.size()==0) {
					models.addElement("");
					models.addElement("Model de rebut "+model+" no existeix");
				}
				
			} catch (Exception e) {
				models.addElement("");
				models.addElement("BuscarModel Error en la SELECT");
				e.printStackTrace();
			} finally {
				ToolsBD.closeConn(conn, ps, rs);
			}
		return models;
	}
    /**
     * Retorna l'històric de l'oficina donada
     * @param oficina Oficina de la que cercar l'històric
     * @return Vector amb tota la informació de l'històric de l'oficina.
     * @throws RemoteException
     */
	public Vector getHistOficina(String oficina)  throws RemoteException{
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector oficinas=new Vector();
		
			try {
				conn=ToolsBD.getConn();
				String sentenciaSql="SELECT * FROM BHAGECO WHERE FHACAGCO = ? ";
				sentenciaSql = sentenciaSql + " ORDER BY FHAFALTA DESC";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setString(1,oficina);
				rs=ps.executeQuery();
				
				while (rs.next()) {
					oficinas.addElement(String.valueOf(rs.getInt("FHACAGCO")));
					oficinas.addElement(rs.getString("FHADAGCO").trim());
					
					DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
			        DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
			        java.util.Date data=null;
			        
					String dataAlta=String.valueOf(rs.getInt("FHAFALTA"));
	                try {
	                	data=yyyymmdd.parse(dataAlta);
	                	oficinas.addElement(ddmmyyyy.format(data));
	                } catch (Exception e) {
	                	oficinas.addElement(dataAlta);
	                }
					
			        
					String dataBaixa=String.valueOf(rs.getInt("FHAFBAJA"));
	                try {
	                	data=yyyymmdd.parse(dataBaixa);
	                	oficinas.addElement(ddmmyyyy.format(data));
	                } catch (Exception e) {
	                	oficinas.addElement(dataBaixa);
	                }
					log.debug("Llegim histOficines: "+String.valueOf(rs.getInt("FHACAGCO"))+" "+rs.getString("FHADAGCO")
							+" "+String.valueOf(rs.getInt("FHAFALTA"))+" "+String.valueOf(rs.getInt("FHAFBAJA")));
				}
				
				if (oficinas.size()==0) {
					oficinas.addElement("");
					oficinas.addElement("Oficina "+oficina+" no existeix");
					oficinas.addElement("");
					oficinas.addElement("");
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

	/**
	 * Retorna l'històric de l'organisme donat
	 * @param organisme Codi d'organisme a cercar
	 * @return Vector amb la informació de l'històric de l'organisme
	 * @throws RemoteException
	 */
	public Vector getHistOrganisme(String organisme)  throws RemoteException{
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector organismes=new Vector();
		
			try {
				conn=ToolsBD.getConn();
				String sentenciaSql="SELECT * FROM BHORGAN WHERE FHXCORGA = ? ";
				sentenciaSql = sentenciaSql + " ORDER BY FHXFALTA DESC";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setString(1,organisme);
				rs=ps.executeQuery();
				
				while (rs.next()) {
					organismes.addElement(String.valueOf(rs.getInt("FHXCORGA")));
					organismes.addElement(rs.getString("FHXDORGR").trim());
					organismes.addElement(rs.getString("FHXDORGT").trim());
					
					DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
			        DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
			        java.util.Date data=null;
			        
					String dataAlta=String.valueOf(rs.getInt("FHXFALTA"));
	                try {
	                	data=yyyymmdd.parse(dataAlta);
	                	organismes.addElement(ddmmyyyy.format(data));
	                } catch (Exception e) {
	                	organismes.addElement(dataAlta);
	                }
					
			        
					String dataBaixa=String.valueOf(rs.getInt("FHXFBAJA"));
	                try {
	                	data=yyyymmdd.parse(dataBaixa);
	                	organismes.addElement(ddmmyyyy.format(data));
	                } catch (Exception e) {
	                	organismes.addElement(dataBaixa);
	                }
	                
					log.debug("Llegim histOrganismes: "+String.valueOf(rs.getInt("FHXCORGA"))+" "+rs.getString("FHXDORGT").trim()
							+" "+String.valueOf(rs.getInt("FHXFALTA"))+" "+String.valueOf(rs.getInt("FHXFBAJA")));
				}
				
				if (organismes.size()==0) {
					organismes.addElement("");
					organismes.addElement("");
					organismes.addElement("");
					organismes.addElement("organisme "+organisme+" no existeix");
					organismes.addElement("");
				}
				
			} catch (Exception e) {
				organismes.addElement("");
				organismes.addElement("");
				organismes.addElement("");
				organismes.addElement("Buscarorganisme Error en la SELECT");
				organismes.addElement("");
				e.printStackTrace();
			} finally {
				ToolsBD.closeConn(conn, ps, rs);
			}
		return organismes;
	}

	/**
	 * Retorna les oficines autoritzades a l'usuari.
	 * @param usuari Codi d'usuari a cercar
	 * @return Informació de les oficines autoritzades a l'usuari encapsulada dins la classe AutoritzacionsUsuariData
	 * @throws RemoteException
	 */
	public AutoritzacionsUsuariData getAutoritzacionsUsuari(String usuari) throws RemoteException{
    	/* Fic a un TreeMap les oficines i les que està autoritzat. */
    	AutoritzacionsUsuariData autUsuData = new AutoritzacionsUsuariData();
    	
    	TreeMap autUsuAE = new TreeMap();
    	TreeMap autUsuCE = new TreeMap();
    	TreeMap autUsuAS = new TreeMap();
    	TreeMap autUsuCS = new TreeMap();
    	TreeMap autUsuVE = new TreeMap();
    	TreeMap autUsuVS = new TreeMap();
    	
    	String tipusAut = "";
    	String oficinaAut = "";
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector oficinas=new Vector();
		
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BZAUTOR " +
					"WHERE FZHCUSU=?" +
					"ORDER BY FZHCAUT, FZHCAGCO";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,usuari);
			rs=ps.executeQuery();
			
			int i=0;
			while (rs.next()) {
				tipusAut = rs.getString("FZHCAUT");
				oficinaAut = String.valueOf(rs.getInt("FZHCAGCO")); 
				if ( tipusAut.equals("AE")) {
					autUsuAE.put(new Integer(rs.getInt("FZHCAGCO")),"X");
				} else if ( tipusAut.equals("CE")) {
					autUsuCE.put(new Integer(rs.getInt("FZHCAGCO")),"X");
				} else if ( tipusAut.equals("AS")) {
					autUsuAS.put(new Integer(rs.getInt("FZHCAGCO")),"X");
				} else if ( tipusAut.equals("CS")) {
					autUsuCS.put(new Integer(rs.getInt("FZHCAGCO")),"X");
				} else if ( tipusAut.equals("VE")) {
					autUsuVE.put(new Integer(rs.getInt("FZHCAGCO")),"X");
				} else if ( tipusAut.equals("VS")) {
					autUsuVS.put(new Integer(rs.getInt("FZHCAGCO")),"X");
				}
				i++;
				//log.debug("Afegim: "+tipusAut+" "+oficinaAut);
			}
			if (i==0) {
				return null;
			} else {
				autUsuData.setAutModifEntrada(autUsuAE);
				autUsuData.setAutConsultaEntrada(autUsuCE);
				autUsuData.setAutModifSortida(autUsuAE);
				autUsuData.setAutConsultaSortida(autUsuCS);
				autUsuData.setAutVisaEntrada(autUsuVE);
				autUsuData.setAutVisaSortida(autUsuVS);
			}
		} catch (Exception e) {
			oficinas.addElement("");
			oficinas.addElement("BuscarOficinas Error en la SELECT");
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
    	return autUsuData;
    }

	/**
	 * Retorna la informació de l'agrupació geogràfica donada
	 * @param codiTipusAgruGeo Codi del tipus d'agrupació geogràfica
	 * @param codiAgruGeoGestionar Codi d'agrupació geogràfica
	 * @return Dades de la agrupació geogràfica encapsulada dins la classe AgrupacioGeograficaData
	 * @throws RemoteException
	 * @throws RegwebException
	 */
	public AgrupacioGeograficaData getAgrupacioGeografica(String codiTipusAgruGeo, String codiAgruGeoGestionar) throws RemoteException, RegwebException{
    	/* Torna el Data d'una agrupació geogràfica */
		AgrupacioGeograficaData agruGeoData = new AgrupacioGeograficaData();
        Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
	
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BAGRUGE WHERE FABCTAGG=? AND FABCAGGE=? ";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,codiTipusAgruGeo);
			ps.setString(2,codiAgruGeoGestionar);
			rs=ps.executeQuery();
			
			if (rs.next()) {
				agruGeoData.setCodiTipusAgruGeo( (rs.getString("FABCTAGG")!= null) ? rs.getString("FABCTAGG") : "" );
				agruGeoData.setCodiAgruGeo( (rs.getString("FABCAGGE")!= null) ? rs.getString("FABCAGGE") : "" );
				agruGeoData.setDescAgruGeo( (rs.getString("FABDAGGE")!= null) ? rs.getString("FABDAGGE").trim() : "" );
				
				DateFormat yymmdd=new SimpleDateFormat("yyMMdd");
		        DateFormat ddmmyy=new SimpleDateFormat("dd/MM/yy");
		        java.util.Date data=null;
		        
				String dataBaixa=String.valueOf(rs.getInt("FABFBAJA"));
                try {
                	data=yymmdd.parse(dataBaixa);
                	agruGeoData.setDataBaixa(ddmmyy.format(data));
                } catch (Exception e) {
                	agruGeoData.setDataBaixa(dataBaixa);
                }
                
				agruGeoData.setCodiTipusAgruGeoSuperior( (rs.getString("FABCTASU")!= null) ? rs.getString("FABCTASU") : "" );
				agruGeoData.setCodiAgruGeoSuperior( (rs.getString("FABCAGSU")!= null) ? rs.getString("FABCAGSU") : "" );
				log.debug("Afegim: "+agruGeoData.getCodiTipusAgruGeo()+" "+agruGeoData.getCodiAgruGeo()+" "+agruGeoData.getDescAgruGeo());
			
				//Ara recuperam el codi postal.
				ToolsBD.closeConn(null, ps, rs);
				sentenciaSql="SELECT * FROM BCODPOS WHERE F12CTAGG=? AND F12CAGGE=? ";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setString(1,codiTipusAgruGeo);
				ps.setString(2,codiAgruGeoGestionar);
				rs=ps.executeQuery();
				if (rs.next()) 
					agruGeoData.setCodiPostal( (rs.getString("F12CPOST")!= null) ? rs.getString("F12CPOST") : "" );
				else 
					agruGeoData.setCodiPostal( "" );
				
				// Ara recuperam el descriptor del tipus d'agrupació.
				ToolsBD.closeConn(null, ps, rs);
				sentenciaSql="SELECT * FROM BTIPAGR WHERE FLDCTAGG=? ";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setString(1,codiTipusAgruGeo);
				rs=ps.executeQuery();
				if (rs.next()) 
					agruGeoData.setDescTipusAgruGeo( (rs.getString("FLDDTAGG")!= null) ? rs.getString("FLDDTAGG").trim() : "" );
				else 
					agruGeoData.setDescTipusAgruGeo( "" );
				
//				 Ara recuperam el descriptor del tipus d'agrupació superior (de la propia taula BAGRUGE)
				ToolsBD.closeConn(null, ps, rs);
				sentenciaSql="SELECT * FROM BAGRUGE WHERE FABCTAGG=? AND FABCAGGE=? ";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setString(1,agruGeoData.getCodiTipusAgruGeoSuperior());
				ps.setString(2,agruGeoData.getCodiAgruGeoSuperior());
				rs=ps.executeQuery();
				if (rs.next()) 
					agruGeoData.setDescAgruGeoSuperior( (rs.getString("FABDAGGE")!= null) ? rs.getString("FABDAGGE").trim() : "" );
				else 
					agruGeoData.setDescAgruGeoSuperior( "" );
				
			} else 
				agruGeoData = null;
			
		} catch (Exception e) {
			e.printStackTrace();
			ToolsBD.closeConn(conn, ps, rs);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return agruGeoData;
    }

	/**
	 * Colecció de totes les agrupacions geogràfiques, estiguin o no de baixa.
	 * @return Collection amb totes les agrupacions geogràfiques
	 * @throws RemoteException
	 * @throws RegwebException
	 */
	public Collection getAgrupacionsGeografiques() throws RemoteException, RegwebException{
    	/* Fic a un TreeMap les agrupacions geogràfiques */
		Vector agrupacionsGeografiques = new Vector();

    	Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
	
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BAGRUGE " +
					"ORDER BY FABCTAGG, FABCAGGE";
			ps=conn.prepareStatement(sentenciaSql);
			rs=ps.executeQuery();
			
			int i=0;
			while (rs.next()) {
				AgrupacioGeograficaData agruGeoData = new AgrupacioGeograficaData();
				agruGeoData.setCodiTipusAgruGeo( (rs.getString("FABCTAGG")!= null) ? rs.getString("FABCTAGG") : "" );
				agruGeoData.setCodiAgruGeo( (rs.getString("FABCAGGE")!= null) ? rs.getString("FABCAGGE") : "" );
				agruGeoData.setDescAgruGeo( (rs.getString("FABDAGGE")!= null) ? rs.getString("FABDAGGE").trim() : "" );
				
				DateFormat yymmdd=new SimpleDateFormat("yyMMdd");
		        DateFormat ddmmyy=new SimpleDateFormat("dd/MM/yy");
		        java.util.Date data=null;
		        
				String dataBaixa=String.valueOf(rs.getInt("FABFBAJA"));
                try {
                	data=yymmdd.parse(dataBaixa);
                	agruGeoData.setDataBaixa(ddmmyy.format(data));
                } catch (Exception e) {
                	agruGeoData.setDataBaixa(dataBaixa);
                }
				//agruGeoData.setDataBaixa( (rs.getString("")!= null) ? rs.getString("FABFBAJA") : "" );
				agruGeoData.setCodiTipusAgruGeoSuperior( (rs.getString("FABCTASU")!= null) ? rs.getString("FABCTASU") : "" );
				agruGeoData.setCodiAgruGeoSuperior( (rs.getString("FABCAGSU")!= null) ? rs.getString("FABCAGSU") : "" );
				agrupacionsGeografiques.add(agruGeoData);
				i++;
				//log.debug("Afegim: "+agruGeoData.getCodiTipusAgruGeo()+" "+agruGeoData.getCodiAgruGeo()+" "+agruGeoData.getDescAgruGeo());
			}
			
			if (i==0) {
				return null;
			} else {
				return agrupacionsGeografiques;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			ToolsBD.closeConn(conn, ps, rs);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
    }

    /**
     * Elimina les autoritzaciós de l'usuari.
     * @param usuari Codi d'usuari
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     */
    public int deleteAutoritzacionsUsuari(String usuari) throws RemoteException, RegwebException { 
    	
    	String tipusAut = "";
    	String oficinaAut = "";
		Connection conn = null;
		boolean rs = false;
		PreparedStatement ps = null;
		
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="DELETE FROM BZAUTOR " +
					"WHERE FZHCUSU=?";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,usuari);
			int afectats=ps.executeUpdate();
			if (afectats<=0){
				log.info("L'usuari ("+usuari+") no té dades a bzautor.");
			}
			//log.debug("Eliminat ("+usuari+") (update count="+ps.getUpdateCount()+" a bzautor.");
		} catch (Exception e) {
			log.debug("Capturada excepció.");
			e.printStackTrace();
			throw new RegwebException(e.getMessage());
		} finally {
			log.debug("Tancam les connexions.");
			ToolsBD.closeConn(conn, ps, null);
		}
    	return 0;    	
    }

    /**
     * Elimina les relacions de l'oficina amb els organismes
     * @param oficinaGestionar Codi d'oficina
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     */
    public int deleteOrgsOfi(String oficinaGestionar) throws RemoteException, RegwebException { 
    	
    	String tipusAut = "";
    	String oficinaAut = "";
		Connection conn = null;
		boolean rs = false;
		PreparedStatement ps = null;
		
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="DELETE FROM BZOFIOR " +
					"WHERE FZFCAGCO=?";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,oficinaGestionar);
			int afectats=ps.executeUpdate();
			// VHZ. Pueden existir oficinas sin organismos asociados. Por ejemplo, una oficina recien creada.
			/*if (afectats<=0){
				//ToolsBD.closeConn(conn, ps, null);
			//	throw new RegwebException("ERROR: No s'han pogut esborrar els organismes de l'oficina ("+oficinaGestionar+") a bzofior.");
			}*/
			//log.debug("Eliminat ("+oficinaGestionar+") (update count="+ps.getUpdateCount()+" a bzofior.");
		} catch (Exception e) {
			e.printStackTrace();
			//ToolsBD.closeConn(conn, ps, null);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, null);
		}
    	return 0;    	
    	
    }

    /**
     * Elimina les relacions de l'oficina amb els organismes per a no remetre
     * @param oficinaGestionar Codi d'oficina
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     */
    public int deleteNoRemsOfi(String oficinaGestionar) throws RemoteException, RegwebException { 
    	
    	String tipusAut = "";
    	String oficinaAut = "";
		Connection conn = null;
		boolean rs = false;
		PreparedStatement ps = null;
		
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="DELETE FROM BZOFIRE " +
					"WHERE FZFCAGCO=?";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,oficinaGestionar);
			int afectats=ps.executeUpdate();
			// VHZ. Pueden existir oficinas sin organismos asociados. Por ejemplo, una oficina recien creada.
			/*if (afectats<=0){
				//ToolsBD.closeConn(conn, ps, null);
			//	throw new RegwebException("ERROR: No s'han pogut esborrar els organismes de remisio de l'oficina ("+oficinaGestionar+") a bzofior.");
			}*/
			//log.debug("Eliminat ("+oficinaGestionar+") (update count="+ps.getUpdateCount()+" a bzofire.");
		} catch (Exception e) {
			e.printStackTrace();
			//ToolsBD.closeConn(conn, ps, null);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, null);
		}
    	return 0;    	
    	
    }

    /**
     * Autoritza a l'usuari a l'oficina i tipus d'entrada donats
     * @param usuari Codi d'usuari a autoritzar
     * @param tipusAut Tipus d'autorització ("AE","CE","CS","AS")
     * @param oficinaAut Codi d'oficina on autoritzar
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     */
    public int addAutoritzacioUsuari(String usuari, String tipusAut, String oficinaAut) throws RemoteException, RegwebException{ 
    	
		Connection conn = null;
		boolean rs = false;
		PreparedStatement ps = null;
		
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="INSERT INTO BZAUTOR " +
					"VALUES ( ?, ?, ?)";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,usuari);
			ps.setString(2,tipusAut);
			ps.setString(3,oficinaAut);
			int afectats=ps.executeUpdate();
			if (afectats<=0){
				//ToolsBD.closeConn(conn, ps, null);
				throw new RegwebException("ERROR: No s'ha pogut insertar l'autorització ("+usuari+","+tipusAut+","+oficinaAut+") a bzautor.");
			}
			//log.debug("Insertat ("+usuari+","+tipusAut+","+oficinaAut+") (update count="+afectats+")");
		} catch (Exception e) {
			//e.printStackTrace();
			//ToolsBD.closeConn(conn, ps, null);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, null);
		}
    	return 0;    	
    }
    /**
     * Relaciona l'organisme amb l'oficina donada
     * @param oficinaGestionar Codi d'oficina
     * @param codiOrganisme Codi d'organisme
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     */
    public int addOrganismeOficina(String oficinaGestionar, String codiOrganisme) throws RemoteException, RegwebException{ 
    	
		Connection conn = null;
		boolean rs = false;
		PreparedStatement ps = null;
		
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="INSERT INTO BZOFIOR " +
					"VALUES ( ?, ?)";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,oficinaGestionar);
			ps.setString(2,codiOrganisme);
			int afectats=ps.executeUpdate();
			if (afectats<=0){
				//ToolsBD.closeConn(conn, ps, null);
				throw new RegwebException("ERROR: No s'ha pogut afegir l'organisme ("+codiOrganisme+") a l'oficina ("+oficinaGestionar+") a bzofior.");
			}
			//log.debug("Insertat ("+oficinaGestionar+","+codiOrganisme+") (update count="+afectats+")");
		} catch (Exception e) {
			e.printStackTrace();
			//ToolsBD.closeConn(conn, ps, null);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, null);
		}
    	return 0;    	
    	
    }
    
    /**
     * Relaciona per a no remetre l'oficina donada amb l'organisme 
     * @param oficinaGestionar Codi d'oficina
     * @param codiOrganisme Codi d'organisme
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     */
    public int addNoRemetreOficina(String oficinaGestionar, String codiOrganisme) throws RemoteException, RegwebException{ 
    	
		Connection conn = null;
		boolean rs = false;
		PreparedStatement ps = null;
		
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="INSERT INTO BZOFIRE " +
					"VALUES ( ?, ?)";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,oficinaGestionar);
			ps.setString(2,codiOrganisme);
			int afectats=ps.executeUpdate();
			if (afectats<=0){
				//ToolsBD.closeConn(conn, ps, null);
				throw new RegwebException("ERROR: No s'ha pogut afegir per a no remetre l'organisme ("+codiOrganisme+") a l'oficina ("+oficinaGestionar+") a bzofior.");
			}
			//log.debug("Insertat ("+oficinaGestionar+","+codiOrganisme+") (update count="+afectats+")");
		} catch (Exception e) {
			e.printStackTrace();
			//ToolsBD.closeConn(conn, ps, null);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, null);
		}
    	return 0;    	
    	
    }
    /**
     * Afageix l'organisme donat
     * @param codiOrganisme Codi d'organisme a afegir
     * @param descCurtaOrg Descripció curta de l'organimse
     * @param descLlargaOrg Descripció llarga de l'organimse
     * @param dataAltaOrg Data d'alta de l'organisme
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     */
    public int altaOrganisme(String codiOrganisme, String descCurtaOrg, String descLlargaOrg, String dataAltaOrg) throws RemoteException, RegwebException { 
		Connection conn = null;
		boolean rs = false;
		PreparedStatement ps = null;
		
		try {
			//TODO: GESTIONAR TRANSACTIONALITAT!
			conn=ToolsBD.getConn();
			String sentenciaSql="INSERT INTO BORGANI " +
					"VALUES ( ?, ?, ?, 'S', ?)";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,codiOrganisme);
			ps.setString(2,descCurtaOrg);
			ps.setString(3,descLlargaOrg);
			ps.setInt(4,0);
			int afectats=ps.executeUpdate();
			if (afectats<=0){
				//ToolsBD.closeConn(conn, ps, null);
				throw new RegwebException("ERROR: No s'ha pogut donar d'alta l'organisme ("+codiOrganisme+","+descCurtaOrg+","+descLlargaOrg+") a borgani.");
			}
			log.debug("Insertat ("+codiOrganisme+","+descLlargaOrg+") (update count="+afectats+") a borgani");			
			// No la ficam dins la taula d'històric d'organismes, d'això s'encarrega el servlet.
			//TODO: Test que ha anat bé, després de gestionar la transaccionalitat!
		} catch (Exception e) {
			e.printStackTrace();
			//ToolsBD.closeConn(conn, ps, null);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, null);
		}
    	return 0;    	
    }
    
    /**
     * Actualitza la informació de l'organisme
     * @param codiOrganisme Codi de l'organisme a modificar
     * @param descCurtaOrg Descripció curta
     * @param descLlargaOrg Descripció lalrga
     * @param dataBaixaOrg Data de baixa
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     */
    public int actualitzaOrganisme(String codiOrganisme, String descCurtaOrg, String descLlargaOrg, String dataBaixaOrg) throws RemoteException, RegwebException { 
    	Connection conn = null;
    	boolean rs = false;
    	ResultSet rsUsuaris = null;
    	PreparedStatement ps = null;
    	
    	try {
    		//TODO: Gestionar la transaccionalitat!
    		conn=ToolsBD.getConn();
    		String sentenciaSql="UPDATE BORGANI SET FAXDORGR=?, FAXDORGT=?, FAXFBAJA=?" +
    		"WHERE FAXCORGA=? ";
    		ps=conn.prepareStatement(sentenciaSql);
    		ps.setString(1,descCurtaOrg);
    		ps.setString(2,descLlargaOrg);
    		ps.setInt(3, new Integer(dataBaixaOrg).intValue() );
    		ps.setString(4,codiOrganisme);
    		
    		int afectats=ps.executeUpdate();
    		if (afectats<=0){
    			//ToolsBD.closeConn(conn, ps, null);
    			throw new RegwebException("ERROR: No s'ha pogut actualitzar l'organisme ("+codiOrganisme+") a BORGANI.");
    		}
    		//log.debug("Insertat ("+codOficina+","+descOficina+",0) (update count="+afectats+") a bagecom)");
    		log.debug("actualitzam l'organisme");	
    	} catch (Exception e) {
    		e.printStackTrace();
    		//ToolsBD.closeConn(conn, ps, null);
    		throw new RegwebException(e.getMessage());
    	} finally {
    		ToolsBD.closeConn(conn, ps, null);
    	}
    	return 0;    	
    }
    
    /**
     * Actualitza l'històric de l'organisme
     * @param codiOrganisme Codi de l'organisme a modificar (de l'històric)
     * @param descCurtaOrg Descripció curta
     * @param descLlargaOrg Descripció llarga
     * @param dataAltaOrg Data d'alta 
     * @param dataBaixaOrg Data de baixa
     * @param dataAltaOrgOld Data antiga de l'organisme (juntament amb codiOrganisme és la clau primària)
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     */
    public int actualitzaHistOrganisme(String codiOrganisme, String descCurtaOrg, String descLlargaOrg, String dataAltaOrg, String dataBaixaOrg
    		, String dataAltaOrgOld) throws RemoteException, RegwebException { 
    	Connection conn = null;
    	boolean rs = false;
    	ResultSet rsUsuaris = null;
    	PreparedStatement ps = null;
    	
    	try {
    		//TODO: Gestionar la transaccionalitat!
    		conn=ToolsBD.getConn();
    		String sentenciaSql="UPDATE BHORGAN SET FHXDORGR=?, FHXDORGT=?, FHXFALTA=?, FHXFBAJA=?" +
    		"WHERE FHXCORGA=? AND FHXFALTA=? ";
    		ps=conn.prepareStatement(sentenciaSql);
    		ps.setString(1,descCurtaOrg);
    		ps.setString(2,descLlargaOrg);
    		ps.setInt(3, new Integer(dataAltaOrg).intValue() );
    		ps.setInt(4, new Integer(dataBaixaOrg).intValue() );
    		
    		ps.setString(5,codiOrganisme);
    		ps.setInt(6, new Integer(dataAltaOrgOld).intValue() );
    		
    		int afectats=ps.executeUpdate();
    		if (afectats<=0){
    			//ToolsBD.closeConn(conn, ps, null);
    			throw new RegwebException("ERROR: No s'ha pogut actualitzar l'organisme ("+codiOrganisme+","+dataAltaOrgOld+") a BHORGAN.");
    		}
    		//log.debug("Insertat ("+codOficina+","+descOficina+",0) (update count="+afectats+") a bagecom)");
    		log.debug("actualitzam l'organisme");	
    	} catch (Exception e) {
    		//e.printStackTrace();
    		//ToolsBD.closeConn(conn, ps, null);
    		throw new RegwebException(e.getMessage());
    	} finally {
    		ToolsBD.closeConn(conn, ps, null);
    	}
    	return 0;    	
    }
    
    /**
     * Alta històric de l'organimse
     * @param codOrganisme Codi de l'organisme de qui crear l'històric
     * @param descCurtaOrganisme Descripció curta
     * @param descLlargaOrganisme Descripció llarga
     * @param dataAlta Data d'alta
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     */
    public int altaHistOrganisme(String codOrganisme, String descCurtaOrganisme, String descLlargaOrganisme, String dataAlta) throws RemoteException, RegwebException { 
		Connection conn = null;
		boolean rs = false;
		PreparedStatement ps = null;
		
		try {
			//TODO: Gestionar la transaccionalitat!
			conn=ToolsBD.getConn();
			String sentenciaSql="INSERT INTO BHORGAN  " +
					"VALUES ( ?, ?, ?, ?, ?)";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,codOrganisme);
			ps.setString(2,descCurtaOrganisme);
			ps.setString(3,descLlargaOrganisme);
			ps.setString(4,dataAlta);
			ps.setString(5,"0");
			
			int afectats=ps.executeUpdate();
			if (afectats<=0){
				//ToolsBD.closeConn(conn, ps, null);
				throw new RegwebException("ERROR: No s'ha pogut donar d'alta l'històric d'organisme ("+codOrganisme+","+descCurtaOrganisme+",) a BHORGAN.");
			}
			log.debug("Insertat ("+codOrganisme+","+descCurtaOrganisme+",0) (update count="+afectats+") a BHORGAN)");
		} catch (Exception e) {
			//e.printStackTrace();
			//ToolsBD.closeConn(conn, ps, null);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, null);
		}
    	return 0;    	
    }

    /**
     * Alta de nova oficina.
     * @param codOficina Codi d'oficina 
     * @param descOficina Descripció de l'oficina 
     * @param dataAlta Data d'alta 
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     */
    public int altaOficina(String codOficina, String descOficina, String dataAlta) throws RemoteException, RegwebException { 
		Connection conn = null;
		boolean rs = false;
		PreparedStatement ps = null;
		
		try {
			//TODO: Gestionar la transaccionalitat!
			conn=ToolsBD.getConn();
			String sentenciaSql="INSERT INTO BAGECOM " +
					"VALUES ( ?, ?, ?)";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,codOficina);
			ps.setString(2,descOficina);
			ps.setString(3,"0");
			
			int afectats=ps.executeUpdate();
			if (afectats<=0){
				//ToolsBD.closeConn(conn, ps, null);
				throw new RegwebException("ERROR: No s'ha pogut donar d'alta l'oficina ("+codOficina+","+descOficina+","+dataAlta+") a bagecom.");
			}
			log.debug("Insertat ("+codOficina+","+descOficina+",0) (update count="+afectats+") a bagecom)");
			//log.debug("insertariem l'oficina");	
			
			// NO AFEGIM LA DADA A L'HISTÒRIC, ES FA DES DEL SERVLET (QUAN CAL)
			// HEM DE DONAR PERMISOS DE LECTURA A ENTRADES I SORTIDES D'AQUESTA OFICINA A TOTHOM!!!
			autoritzaConsultaTotsUsuaris(codOficina);
		} catch (Exception e) {
			//e.printStackTrace();
			//ToolsBD.closeConn(conn, ps, null);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, null);
		}
    	return 0;    	
    }


    /**
     * Alta de nova oficina fosoca.
     * @param codOficina Codi d'oficina 
     * @param codOficinaFisica Codi d'oficina 
     * @param descOficina Descripció de l'oficina 
     * @param dataAlta Data d'alta 
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     */
    public int altaOficinaFisica(String codOficina, String codOficinaFisica, String descOficina, String dataAlta) throws RemoteException, RegwebException { 
		Connection conn = null;
		boolean rs = false;
		PreparedStatement ps = null;
		
		try {
			//TODO: Gestionar la transaccionalitat!
			conn=ToolsBD.getConn();
			String sentenciaSql="INSERT INTO BZOFIFIS " +
					"VALUES ( ?, ?, ?)";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setInt(1,Integer.parseInt(codOficina));
			ps.setInt(2,Integer.parseInt(codOficinaFisica));
			ps.setString(3,descOficina);
			
			int afectats=ps.executeUpdate();
			if (afectats<=0){
				//ToolsBD.closeConn(conn, ps, null);
				throw new RegwebException("ERROR: No s'ha pogut donar d'alta l'oficina ("+codOficina+"-"+codOficinaFisica+","+descOficina+","+dataAlta+") a bzofifis.");
			}
			log.debug("Insertat ("+codOficina+"-"+codOficinaFisica+","+descOficina+",0) (update count="+afectats+") a bzofifis)");
			//log.debug("insertariem l'oficina");	
			
			// NO AFEGIM LA DADA A L'HISTÒRIC, ES FA DES DEL SERVLET (QUAN CAL)
			// HEM DE DONAR PERMISOS DE LECTURA A ENTRADES I SORTIDES D'AQUESTA OFICINA A TOTHOM!!!
			//autoritzaConsultaTotsUsuaris(codOficina);
		} catch (Exception e) {
			//e.printStackTrace();
			//ToolsBD.closeConn(conn, ps, null);
			log.debug(e.getMessage());e.printStackTrace();
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, null);
		}
    	return 0;    	
    }

    /**
     * Alta de històric d'oficina
     * @param codOficina Codi d'oficina
     * @param descOficina Descripció de l'oficina
     * @param dataAlta Data d'alta de l'oficina (juntament amb codOficina és la clau primària)
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     */
    public int altaHistOficina(String codOficina, String descOficina, String dataAlta) throws RemoteException, RegwebException { 
		Connection conn = null;
		boolean rs = false;
		PreparedStatement ps = null;
		
		try {
			//TODO: Gestionar la transaccionalitat!
			conn=ToolsBD.getConn();
			String sentenciaSql="INSERT INTO BHAGECO " +
					"VALUES ( ?, ?, ?, ?)";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,codOficina);
			ps.setString(2,descOficina);
			ps.setString(3,dataAlta);
			ps.setString(4,"0");
			
			int afectats=ps.executeUpdate();
			if (afectats<=0){
				//ToolsBD.closeConn(conn, ps, null);
				throw new RegwebException("ERROR: No s'ha pogut donar d'alta l'oficina ("+codOficina+","+descOficina+",) a bagecom.");
			}
			log.debug("Insertat ("+codOficina+","+descOficina+",0) (update count="+afectats+") a BHAGECO)");
			//log.debug("insertariem l'històric d'oficina");	
		} catch (Exception e) {
			//e.printStackTrace();
			//ToolsBD.closeConn(conn, ps, null);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, null);
		}
    	return 0;    	
    }

    /**
     * Actualitza l'oficina
     * @param codOficina Codi d'oficina a modificar
     * @param descOficina Descripció de l'oficina 
     * @param dataBaixa Data de baixa 
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     */
    public int actualitzaOficina(String codOficina, String descOficina, String dataBaixa) throws RemoteException, RegwebException { 
    	Connection conn = null;
    	boolean rs = false;
    	ResultSet rsUsuaris = null;
    	PreparedStatement ps = null;
    	
    	try {
    		//TODO: Gestionar la transaccionalitat!
    		conn=ToolsBD.getConn();
    		String sentenciaSql="UPDATE BAGECOM SET FAADAGCO=?, FAAFBAJA=? " +
    		"WHERE FAACAGCO=?";
    		ps=conn.prepareStatement(sentenciaSql);
    		ps.setString(1,descOficina);
    		ps.setInt(2, new Integer(dataBaixa).intValue() );
    		ps.setString(3,codOficina);
    		
    		int afectats=ps.executeUpdate();
    		if (afectats<=0){
    			//ToolsBD.closeConn(conn, ps, null);
    			throw new RegwebException("ERROR: No s'ha pogut donar d'alta l'oficina ("+codOficina+","+descOficina+","+dataBaixa+") a bagecom.");
    		}
    		//log.debug("Insertat ("+codOficina+","+descOficina+",0) (update count="+afectats+") a bagecom)");
    		log.debug("actualitzam l'oficina");	
    		// Si la data de baixa és "0" HEM DE DONAR PERMISOS DE LECTURA A ENTRADES I SORTIDES D'AQUESTA OFICINA A TOTHOM!!!
    		if (dataBaixa.equals("0")) 
    		    autoritzaConsultaTotsUsuaris(codOficina);
    	} catch (Exception e) {
    		//e.printStackTrace();
    		//ToolsBD.closeConn(conn, ps, null);
    		throw new RegwebException(e.getMessage());
    	} finally {
    		ToolsBD.closeConn(conn, ps, null);
    	}
    	return 0;    	
    }


    /**
     * Actualitza l'oficina fisica
     * @param codOficina Codi d'oficina a modificar
     * @param codOficina Codi d'oficina fisica a modificar
     * @param descOficina Descripció de l'oficina 
     * @param dataBaixa Data de baixa 
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     */
    public int actualitzaOficinaFisica(String codOficina, String codOficinaFisica, String descOficina, String dataBaixa) throws RemoteException, RegwebException { 
    	Connection conn = null;
    	boolean rs = false;
    	ResultSet rsUsuaris = null;
    	PreparedStatement ps = null;
    	
    	try {
    		//TODO: Gestionar la transaccionalitat!
    		conn=ToolsBD.getConn();
    		String sentenciaSql="UPDATE BZOFIFIS SET OFF_NOM=? " +
    		"WHERE FZOCAGCO=? AND OFF_CODI=?";
    		ps=conn.prepareStatement(sentenciaSql);
    		ps.setString(1,descOficina);
    		ps.setString(2,codOficina);
    		ps.setString(3,codOficinaFisica);
    		
    		int afectats=ps.executeUpdate();
    		if (afectats<=0){
    			//ToolsBD.closeConn(conn, ps, null);
    			throw new RegwebException("ERROR: No s'ha pogut actualitzar l'oficina ("+codOficina+","+codOficinaFisica+","+descOficina+","+dataBaixa+") a bagecom.");
    		}
    		//log.debug("Insertat ("+codOficina+","+descOficina+",0) (update count="+afectats+") a bagecom)");
    		log.debug("actualitzam l'oficina fisica");	
    	} catch (Exception e) {
    		//e.printStackTrace();
    		//ToolsBD.closeConn(conn, ps, null);
    		throw new RegwebException(e.getMessage());
    	} finally {
    		ToolsBD.closeConn(conn, ps, null);
    	}
    	return 0;    	
    }

    /**
     * Autoritza tots els usuaris a consultar l'oficina donada (tant entrades com sortides)
     * Això és necessari per quan es dona d'alta/activa una nova oficina.
     * @param codOficina Oficina on autoritzar els usuaris
     * @throws RegwebException
     */
    private void autoritzaConsultaTotsUsuaris(String codOficina) throws RegwebException {
    	Connection conn = null;
    	boolean rs = false;
    	ResultSet rsUsuaris = null;
    	PreparedStatement ps = null;
    	
    	try {
    		//TODO: Gestionar la transaccionalitat!
    		conn=ToolsBD.getConn();
    		String sentenciaSql="SELECT DISTINCT FZHCUSU FROM BZAUTOR ";
    		ps=conn.prepareStatement(sentenciaSql);
    		rsUsuaris=ps.executeQuery();
    		while (rsUsuaris.next()) {
    			log.debug("Autoritzam usuari "+rsUsuaris.getString("FZHCUSU")+" a l'oficina "+codOficina);
    			try { 
    				this.addAutoritzacioUsuari(rsUsuaris.getString("FZHCUSU"), "CE", codOficina);
    				this.addAutoritzacioUsuari(rsUsuaris.getString("FZHCUSU"), "CS", codOficina);
    			} catch (RegwebException eRW) {
    				if (eRW.getMessage().indexOf("SQL0803")!=-1 ) {
    					//És clau duplicada. Se veu que l'usuari ja hi estava autoritzat, no passa rés, ignoram.
    					log.debug("Usuari "+rsUsuaris.getString("FZHCUSU")+" prèviament ja autoritzat l'oficina "+codOficina);
    				} else {
    					throw new RegwebException(eRW.getMessage());
    				}
    			}
    		}
    	} catch (Exception e) {
    		//e.printStackTrace();
    		//ToolsBD.closeConn(conn, ps, null);
    		throw new RegwebException(e.getMessage());
    	} finally {
    		ToolsBD.closeConn(conn, ps, null);
    	}
    }
    
    /**
     * Actualitza la informació de l'històric de l'oficina 
     * @param codOficina Codi d'oficina a modificar l'històric
     * @param descOficina Descripció de l'oficina 
     * @param dataAlta Data d'alta
     * @param dataBaixa Data de baixa
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     */
    public int actualitzaHistOficina(String codOficina, String descOficina, String dataAlta, String dataBaixa) throws RemoteException, RegwebException { 
		Connection conn = null;
		boolean rs = false;
		PreparedStatement ps = null;
		
		try {
			//TODO: Gestionar la transaccionalitat!
			conn=ToolsBD.getConn();
			String sentenciaSql="UPDATE BHAGECO SET FHADAGCO=?,FHAFALTA=?,FHAFBAJA=? " +
					"WHERE FHACAGCO=? AND FHAFALTA=?";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,descOficina);
			ps.setInt(2, new Integer(dataAlta).intValue() );
			ps.setInt(3, new Integer(dataBaixa).intValue() );
			ps.setString(4,codOficina);
			ps.setInt(5, new Integer(dataAlta).intValue() );
			// NO EXECUTAM FINS TENIR CLAR TOT EL QUE S'HA DE FER! 	
			
			int afectats=ps.executeUpdate();
			if (afectats<=0){
				//ToolsBD.closeConn(conn, ps, null);
				throw new RegwebException("ERROR: No s'ha pogut donar d'alta l'oficina ("+codOficina+","+descOficina+","+dataBaixa+") a bagecom.");
			}
			//log.debug("Insertat ("+codOficina+","+descOficina+",0) (update count="+afectats+") a bagecom)");
			log.debug("actualitzam l'històric d'oficina");
		} catch (Exception e) {
			//e.printStackTrace();
			//ToolsBD.closeConn(conn, ps, null);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, null);
		}
    	return 0;    	
    }

    /**
     * Alta de la agrupació geogràfica
     * @param codTipuAgruGeo Codi tipus agrupació geogràfica
     * @param codAgruGeo Codi agrupació geogràfica
     * @param descAgruGeo Descripció agrupació geogràfica 
     * @param dataBaixa Data de baixa 
     * @param codTipusAgruGeoSuperior Codi tipus agrupació geogràfica superior
     * @param codAgruGeoSuperior Codi agrupació geogràfica superior
     * @param codiPostal Codig postal
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     */
    public int altaAgrupacioGeografica(String codTipuAgruGeo, String codAgruGeo, String descAgruGeo, 
    		String dataBaixa, String codTipusAgruGeoSuperior, String codAgruGeoSuperior, String codiPostal ) throws RemoteException, RegwebException { 
		Connection conn = null;
		boolean rs = false;
		PreparedStatement ps = null;
		
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="INSERT INTO BAGRUGE " +
					"VALUES ( ?, ?, ?, ?, ?, ?)";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setInt(1,new Integer(codTipuAgruGeo).intValue() );
			ps.setInt(2,new Integer(codAgruGeo).intValue() );
			ps.setString(3,descAgruGeo);
			ps.setInt(4, new Integer(dataBaixa).intValue() );
			ps.setInt(5,new Integer(codTipusAgruGeoSuperior).intValue() );
			ps.setInt(6,new Integer(codAgruGeoSuperior).intValue() );
			// NO EXECUTAM FINS TENIR CLAR TOT EL QUE S'HA DE FER! rs=ps.execute();
			int afectats=ps.executeUpdate();
			if (afectats<=0){
				//ToolsBD.closeConn(conn, ps, null);
				throw new RegwebException("ERROR: No s'ha pogut donar d'alta l'agrupació geogràfica ("+codTipuAgruGeo+","+codAgruGeo+","
					+descAgruGeo+","+dataBaixa+","+codTipuAgruGeo+","+codAgruGeo+") (update count="+afectats+") a BAGRUGE)");
			}
			log.debug("Insertada agrupacio geogràfica  ("+codTipuAgruGeo+","+codAgruGeo+","
				+descAgruGeo+","+dataBaixa+","+codTipuAgruGeo+","+codAgruGeo+") (update count="+afectats+") a BAGRUGE)");
			/* log.debug("Hem d'insertar agrupacio geogràfica  ("+codTipuAgruGeo+","+codAgruGeo+","
					+descAgruGeo+","+dataBaixa+","+codTipuAgruGeo+","+codAgruGeo+") (update count=) a BAGRUGE)"); */
				
		    // TAMBÉ HEM D'AFEGIR LA DADA A L'HISTÒRIC!!!
			sentenciaSql = "INSERT INTO BCODPOS "+
				"VALUES (?,?,?)";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setInt(1, new Integer(codTipuAgruGeo).intValue() );
			ps.setInt(2,new Integer(codAgruGeo).intValue() );
			ps.setInt(3,new Integer(codiPostal).intValue() ); 
			afectats=ps.executeUpdate();
			if (afectats<=0){
				ToolsBD.closeConn(conn, ps, null);
				throw new RegwebException("ERROR: No s'ha pogut donar d'alta l'agrupació geogràfica ("+codTipuAgruGeo+","+codAgruGeo+","
					+descAgruGeo+","+dataBaixa+","+codTipuAgruGeo+","+codAgruGeo+","+codiPostal+") (update count="+afectats+") a bcodpos)");
			}
			log.debug("Insertada agrupacio geogràfica  ("+codTipuAgruGeo+","+codAgruGeo+","
				+descAgruGeo+","+dataBaixa+","+codTipuAgruGeo+","+codAgruGeo+","+codiPostal+") (update count="+afectats+") a bcodpos)");
			/*log.debug("Hem d'insertar agrupacio geogràfica  ("+codTipuAgruGeo+","+codAgruGeo+","
					+descAgruGeo+","+dataBaixa+","+codTipuAgruGeo+","+codAgruGeo+","+codiPostal+") (update count=) a bcodpos)"); */
		} catch (Exception e) {
			//e.printStackTrace();
			//ToolsBD.closeConn(conn, ps, null);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, null);
		}
    	return 0;    	
    }

    /**
     * Elimina l'agrupació geogràfica donada
     * @param codTipuAgruGeo Codi tipus agrupació geogràfica
     * @param codAgruGeo Codi agrupació geogràfica
     * @return torna 0 si ha anat bé (si va malament, bota la RegwebException)
     * @throws RemoteException
     * @throws RegwebException
     */
    public int delAgrupacioGeografica(String codTipuAgruGeo, String codAgruGeo) throws RemoteException, RegwebException { 
		Connection conn = null;
		boolean rs = false;
		PreparedStatement ps = null;
		
		try {
			//TODO: Gestionar transaccionalitat!
			conn=ToolsBD.getConn();
			//Primer eliminam les referències a l'agrupació geogràfica (codi postal)
			String sentenciaSql="DELETE FROM BCODPOS " +
					"WHERE F12CTAGG=? AND F12CAGGE=?";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setInt(1,new Integer(codTipuAgruGeo).intValue() );
			ps.setInt(2,new Integer(codAgruGeo).intValue() );
			log.debug("SentenciaSql="+sentenciaSql);
			int afectats=ps.executeUpdate();
			if (afectats<=0){
				//ToolsBD.closeConn(conn, ps, null);
				throw new RegwebException("ERROR: No s'ha pogut eliminar l'agrupació geogràfica ("+codTipuAgruGeo+","+codAgruGeo+") a bcodpos.");
			}
			log.debug("Eliminat ("+codTipuAgruGeo+","+codAgruGeo+" (update count="+afectats+") a bcodpos");
			//log.debug("Hem d'eliminar ("+codTipuAgruGeo+","+codAgruGeo+" (update count=) a bcodpos");
			
			sentenciaSql="DELETE FROM BAGRUGE " +
				"WHERE FABCTAGG=? AND FABCAGGE=?";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setInt(1,new Integer(codTipuAgruGeo).intValue() );
			ps.setInt(2,new Integer(codAgruGeo).intValue() );
			afectats=ps.executeUpdate();
			if (afectats<=0){
				ToolsBD.closeConn(conn, ps, null);
				throw new RegwebException("ERROR: No s'ha pogut eliminar l'agrupació geogràfica ("+codTipuAgruGeo+","+codAgruGeo+") a BAGRUGE.");
			}
			log.debug("Eliminar ("+codTipuAgruGeo+","+codAgruGeo+" (update count="+afectats+") a BAGRUGE");
			//log.debug("Eliminar ("+codTipuAgruGeo+","+codAgruGeo+" (update count= a BAGRUGE");
		} catch (Exception e) {
			//e.printStackTrace();
			//ToolsBD.closeConn(conn, ps, null);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, null);
		}
    	return 0;    	
    }
    
    /**
     * Alta entitat
     * @param codEntidad Codi d'entitat a donar d'alta (castellà)
     * @param codEntitat Codi d'entitat a donar d'alta (català)
     * @param subcodEntitat Subcodi d'entitat
     * @param descEntidad Descripció (castellà)
     * @param descEntitat Descripció (català)
     * @param dataBaixa Data de baixa
     * @return 
     * @throws RemoteException
     * @throws RegwebException
     */
    public int altaEntitat(String codEntidad, String codEntitat, String subcodEntitat, String descEntidad, String descEntitat, String dataBaixa) throws RemoteException, RegwebException { 
		Connection conn = null;
		boolean rs = false;
		PreparedStatement ps = null;
		
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="INSERT INTO BZENTID " +
					"VALUES ( ?, ?, ?, ?, ?, ?)";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,codEntidad);
			ps.setString(2,codEntitat);
			ps.setString(3,subcodEntitat);
			ps.setString(4,descEntidad);
			ps.setString(5,descEntitat);
			ps.setString(6,dataBaixa);
			int afectats=ps.executeUpdate();
			if (afectats<=0){
				//ToolsBD.closeConn(conn, ps, null);
				throw new RegwebException("ERROR: No s'ha pogut inserir l'entitat ("+codEntidad+","+subcodEntitat+") a bzentid.");
			}
			//log.debug("Insertat ("+codEntidad+","+codEntitat+","+subcodEntitat+","+descEntidad+","+descEntitat+","+dataBaixa+") (update count="+afectats+" a bzentid");
		} catch (Exception e) {
			//e.printStackTrace();
			//ToolsBD.closeConn(conn, ps, null);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, null);
		}
    	return 0;    	
    }
    
    /**
     * Actualitza la informació de l'entitat donada
     * @param codEntitat Codi d'entitat a donar d'alta (català)
     * @param subcodEntitat Subcodi d'entitat
     * @param descEntidad Descripció (castellà)
     * @param descEntitat Descripció (català)
     * @param dataBaixa Data de baixa
     * @return
     * @throws RemoteException
     * @throws RegwebException
     */
    public int actualitzaEntitat(String codEntidad, String codEntitat, String subcodEntitat, String descEntidad, String descEntitat, String dataBaixa) throws RemoteException, RegwebException { 
		Connection conn = null;
		boolean rs = false;
		PreparedStatement ps = null;
		
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="UPDATE BZENTID SET FZGCENT2=?, FZGDENTI=?, FZGDENT2=?, FZGFBAJA=?"+
					"WHERE FZGCENTI=? AND FZGNENTI=?";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1, codEntitat);
			ps.setString(2, descEntidad);
			ps.setString(3, descEntitat);
			ps.setString(4, dataBaixa);
			ps.setString(5, codEntidad);
			ps.setString(6, subcodEntitat);
			
			int afectats=ps.executeUpdate();
			if (afectats<=0){
				ToolsBD.closeConn(conn, ps, null);
				throw new RegwebException("ERROR: No s'ha pogut fer l'update a BZENTID, valors:("+codEntidad+","+codEntitat+","+subcodEntitat+","
						+descEntidad+","+descEntitat+","+dataBaixa+")");
			}
			//log.debug("Eliminat ("+codEntidad+","+subcodEntitat+","+descEntitat+") (update count="+ps.getUpdateCount()+" a bzentid");
		} catch (Exception e) {
			e.printStackTrace();
			ToolsBD.closeConn(conn, ps, null);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, null);
		}
    	return 0;    	
    }
    
    /**
     * Torna true si ja existeix un comptador donat d'alta per aquesta oficina i any.
     * @param anyGestionar Any
     * @param oficinaGestionar codi d'oficina
     * @return true si ja existeix un comptador donat d'alta per aquesta oficina i any, false en cas contrari.
     * @throws RemoteException
     * @throws RegwebException
     */
    public boolean existComptador(String anyGestionar, String oficinaGestionar) throws RemoteException, RegwebException {
    //Torna true si ja existeix un comptador donat d'alta per aquesta oficina i any.
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String resultat = "";
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT COUNT(*) FROM BZCONES " +
					"WHERE FZDAENSA=? AND FZDCAGCO=?";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,anyGestionar);
			ps.setString(2,oficinaGestionar);
			rs=ps.executeQuery();
			
			while (rs.next()) {
				resultat = rs.getString(1);
				log.debug("existComptador="+resultat);
			}
			if (Integer.parseInt(resultat)>0)
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			ToolsBD.closeConn(conn, ps, rs);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
    }
    
    /**
     * Inicialització comptador
     * @param anyGestionar any del comptador
     * @param entradaSortida tipus d'entrada/sortida del comptador
     * @param codiOficina codi de l'oficina del comptador
     * @return
     * @throws RemoteException
     * @throws RegwebException
     */
    public int altaComptador(String anyGestionar, String entradaSortida, String codiOficina) throws RemoteException, RegwebException { 
		Connection conn = null;
		PreparedStatement ps = null;
	    
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="INSERT INTO BZCONES " +
					"VALUES ( ?, ?, ?, ?)";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,anyGestionar);
			ps.setString(2,entradaSortida);
			ps.setString(3,codiOficina);
			ps.setInt(4,0 );
			int afectats=ps.executeUpdate();
			if (afectats<=0){
				ToolsBD.closeConn(conn, ps, null);
				throw new RegwebException("ERROR: No s'ha pogut donar d'alta el comptador ("+anyGestionar+","+entradaSortida+","+codiOficina+") a bzcones.");
			}
			log.debug("Insertat ("+anyGestionar+","+entradaSortida+","+codiOficina+") (update count="+afectats+") a bzcones");
		
			// Ara la hauríem de ficar també dins la taula de BZLIBRO, però hem
			// revisat amb Sebastià Soler els codis fonts de l'AS400 i no hi ha cap altre
			// referència a BZLIBRO. Aleshores, com que no s'empra, no ho implementam.
			/* sentenciaSql = "insert into bzlibro "+
					"values (?,?,?,?,?)";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,anyGestionar);
			ps.setString(2,entradaSortida);
			ps.setString(3,codiOficina);
			ps.setBigDecimal(4,new BigDecimal("0") );
			ps.setString(5, dataAlta  );
			int afectats=ps.executeUpdate();
			if (afectats<=0){
				ToolsBD.closeConn(conn, ps, null);
				throw new RegwebException("ERROR: No s'ha pogut donar d'alta el comptador ("+anyGestionar+","+entradaSortida+","+codiOficina+") a bzcones.");
			}
			log.debug("Insertat ("+anyGestionar+","+entradaSortida+","+codiOficina+",0,"+dataAlta+") (update count="+afectats+") a bzlibro");
			*/
		} catch (Exception e) {
			e.printStackTrace();
			ToolsBD.closeConn(conn, ps, null);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, null);
		}
    	return 0;    	
    }
    
    /**
     * Retorna els tipus de document
     * @param codiTipDoc Codi del tipus de document a recuperar
     * @return informació del tipus de document, encapsulat dins la classe TipusDocumentData
     * @throws RemoteException
     * @throws RegwebException
     */
    public TipusDocumentData getTipusDocument(String codiTipDoc) throws RemoteException, RegwebException{
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		TipusDocumentData tipdoc=new TipusDocumentData();
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BZTDOCU WHERE FZICTIPE=? ";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,codiTipDoc);
			rs=ps.executeQuery();
			if (rs.next()) {
				tipdoc.setCodiTipusDoc( (rs.getString("FZICTIPE")!= null) ? rs.getString("FZICTIPE") : "" );
				tipdoc.setDescTipusDoc( (rs.getString("FZIDTIPE")!= null) ? rs.getString("FZIDTIPE").trim() : "" );
				
				DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
		        DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
		        java.util.Date data=null;
		        
				String dataBaixa=String.valueOf(rs.getInt("FZIFBAJA"));
	            try {
	            	data=yyyymmdd.parse(dataBaixa);
	            	tipdoc.setDataBaixa(ddmmyyyy.format(data));
	            } catch (Exception e) {
	            	tipdoc.setDataBaixa(dataBaixa);
	            }
			} else {
				tipdoc=null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			ToolsBD.closeConn(conn, ps, rs);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return tipdoc;
	}
    
    /**
     * Retorna tots els tipus de documents de la BBDD, estiguin de baixa o no.
     * @return Vector amb tots els tipus de documents.
     * @throws RemoteException
     */
    public Vector getTipusDocuments() throws RemoteException {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector documentos=new Vector();
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BZTDOCU ORDER BY FZICTIPE";
			ps=conn.prepareStatement(sentenciaSql);
			rs=ps.executeQuery();
			documentos.addElement("");
			documentos.addElement("");
			while (rs.next()) {
				documentos.addElement(rs.getString("FZICTIPE"));
				documentos.addElement(rs.getString("FZIDTIPE").trim());
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
    
    /**
     * Creació de tipus de document
     * @param codTipDoc Codi tipus document
     * @param descTipDoc Descripció del document
     * @param dataBaixa Data de baixa del document
     * @return
     * @throws RemoteException
     * @throws RegwebException
     */
    public int altaTipusDocument(String codTipDoc, String descTipDoc, String dataBaixa) throws RemoteException, RegwebException { 
		Connection conn = null;
		boolean rs = false;
		PreparedStatement ps = null;
		
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="INSERT INTO BZTDOCU " +
					"VALUES ( ?, ?, ?)";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,codTipDoc);
			ps.setString(2,descTipDoc);
			ps.setInt(3, new Integer(dataBaixa).intValue() );
			int afectats=ps.executeUpdate();
			if (afectats<=0){
				ToolsBD.closeConn(conn, ps, null);
				throw new RegwebException("ERROR: No s'ha pogut donar d'alta el tipus de document ("+codTipDoc+","+descTipDoc+","+dataBaixa+") a BZTDOCU.");
			}
			log.debug("Insertat ("+codTipDoc+","+descTipDoc+","+dataBaixa+") (update count="+afectats+") a BZTDOCU)");
		} catch (Exception e) {
			e.printStackTrace();
			ToolsBD.closeConn(conn, ps, null);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, null);
		}
    	return 0;    	
    }

    /**
     * Actualitza la informació del tipus de document.
     * @param codTipDoc Codi tipus document
     * @param descTipDoc Descripció 
     * @param dataBaixa Data de baixa
     * @return
     * @throws RemoteException
     * @throws RegwebException
     */
    public int actualitzaTipusDocument(String codTipDoc, String descTipDoc, String dataBaixa ) throws RemoteException, RegwebException { 
		Connection conn = null;
		boolean rs = false;
		PreparedStatement ps = null;
		
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="UPDATE BZTDOCU SET FZIDTIPE=? , FZIFBAJA=? " +
					"WHERE FZICTIPE=?";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,descTipDoc);
			ps.setInt(2, new Integer(dataBaixa).intValue() );
			ps.setString(3, codTipDoc);
			int afectats=ps.executeUpdate();
			if (afectats<=0){
				ToolsBD.closeConn(conn, ps, null);
				throw new RegwebException("ERROR: No s'ha pogut modificar el tipus de document ("+codTipDoc+","+descTipDoc+","+dataBaixa+") a BZTDOCU.");
			}
			//log.debug("Actualitzat tipus de document ("+codTipDoc+","+descTipDoc+","+dataBaixa+") (update count="+afectats+") a BZTDOCU)");
		} catch (Exception e) {
			e.printStackTrace();
			ToolsBD.closeConn(conn, ps, null);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, null);
		}
    	return 0;    	
    }
	
    
    /**
     * Retorna el nom de un municipi del 060
     * @param codiMun060 Codi del tipus de document a recuperar
     * @return informació del tipus de document, encapsulat dins la classe TipusDocumentData
     * @throws RemoteException
     * @throws RegwebException
     */
    public Municipi060Data getMunicipi060(String codiMun060) throws RemoteException, RegwebException{
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Municipi060Data tipdoc=new Municipi060Data();
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM  BZMUN_060 WHERE MUN_CODI=? ";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,codiMun060);
			rs=ps.executeQuery();
			if (rs.next()) {
				tipdoc.setCodiMunicipi060( (rs.getString("MUN_CODI")!= null) ? rs.getString("MUN_CODI") : "" );
				tipdoc.setDescMunicipi060( (rs.getString("MUN_NOM")!= null) ? rs.getString("MUN_NOM").trim() : "" );
				
				DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
		        DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
		        java.util.Date data=null;
		        
				String dataBaixa=String.valueOf(rs.getInt("MUN_FECBAJ"));
	            try {
	            	data=yyyymmdd.parse(dataBaixa);
	            	tipdoc.setDataBaixa(ddmmyyyy.format(data));
	            } catch (Exception e) {
	            	tipdoc.setDataBaixa(dataBaixa);
	            }
			} else {
				tipdoc=null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			ToolsBD.closeConn(conn, ps, rs);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return tipdoc;
	}
    
    /**
     * Retorna tots els municipis del 060, estiguin de baixa o no.
     * @return Vector amb tots els municipis del 060.
     * @throws RemoteException
     */
    public Vector getTipusMunicipis060() throws RemoteException {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		Vector documentos=new Vector();
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BZMUN_060 ORDER BY MUN_CODI";
			ps=conn.prepareStatement(sentenciaSql);
			rs=ps.executeQuery();
			documentos.addElement("");
			documentos.addElement("");
			while (rs.next()) {
				documentos.addElement(rs.getString("MUN_CODI"));
				documentos.addElement(rs.getString("MUN_NOM").trim());
			}
			if (documentos.size()==0) {
				documentos.addElement("");
				documentos.addElement("No hi ha municipis del 060");
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
    
    /**
     * Creació de un municipi del 060
     * @param codMun060 Codi tipus document
     * @param descMun060 Descripció del document
     * @param dataBaixa Data de baixa del document
     * @return
     * @throws RemoteException
     * @throws RegwebException
     */
    public int altaMunicipi060(String codMun060, String descMun060, String dataBaixa) throws RemoteException, RegwebException { 
		Connection conn = null;
		boolean rs = false;
		PreparedStatement ps = null;
		
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="INSERT INTO BZMUN_060 " +
					"VALUES ( ?, ?, ?)";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,codMun060);
			ps.setString(2,descMun060);
			ps.setInt(3, new Integer(dataBaixa).intValue() );
			int afectats=ps.executeUpdate();
			if (afectats<=0){
				ToolsBD.closeConn(conn, ps, null);
				throw new RegwebException("ERROR: No s'ha pogut donar d'alta el tipus de document ("+codMun060+","+descMun060+","+dataBaixa+") a BZMUN_060.");
			}
			log.debug("Insertat ("+codMun060+","+descMun060+","+dataBaixa+") (update count="+afectats+") a BZMUN_060)");
		} catch (Exception e) {
			e.printStackTrace();
			ToolsBD.closeConn(conn, ps, null);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, null);
		}
    	return 0;    	
    }

    /**
     * Actualitza la informació del Municipi 060.
     * @param codMun060 Codi tipus document
     * @param descMun060 Descripció 
     * @param dataBaixa Data de baixa
     * @return
     * @throws RemoteException
     * @throws RegwebException
     */
    public int actualitzaMunicipi060(String codMun060, String descMun060, String dataBaixa ) throws RemoteException, RegwebException { 
		Connection conn = null;
		boolean rs = false;
		PreparedStatement ps = null;
		
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="UPDATE BZMUN_060 SET MUN_NOM=? , MUN_FECBAJ=? " +
					"WHERE MUN_CODI=?";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,descMun060);
			ps.setInt(2, new Integer(dataBaixa).intValue() );
			ps.setString(3, codMun060);
			int afectats=ps.executeUpdate();
			if (afectats<=0){
				ToolsBD.closeConn(conn, ps, null);
				throw new RegwebException("ERROR: No s'ha pogut modificar el tipus de document ("+codMun060+","+descMun060+","+dataBaixa+") a BZTDOCU.");
			}
			//log.debug("Actualitzat tipus de document ("+codMun060+","+descMun060+","+dataBaixa+") (update count="+afectats+") a BZTDOCU)");
		} catch (Exception e) {
			e.printStackTrace();
			ToolsBD.closeConn(conn, ps, null);
			throw new RegwebException(e.getMessage());
		} finally {
			ToolsBD.closeConn(conn, ps, null);
		}
    	return 0;    	
    }
    
    public void ejbCreate() throws CreateException {
    }
    
    public void ejbActivate() {
    }
    public void ejbPassivate() {
    }
    public void ejbRemove() {
        // System.out.println("EJB eliminado");
    }
    public void setSessionContext(SessionContext ctx) {
    this.context = ctx;
    }
    
   
}