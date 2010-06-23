package es.caib.regweb;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * Bean que gestiona el registre de Repros
 * @author  VHerrera
 * @version 1.0
 */

public class ReproUsuarioBean implements SessionBean {

	private static final long serialVersionUID = 1L;
	private String repro;
	private String codUsuario;
	private String nomRepro;
	private String tipRepro;
	
	
	public void leer(String codUsuario, String nomRepro) throws SQLException, ClassNotFoundException, Exception {
		Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        String datosleidos = null;
        try {
        	conn=ToolsBD.getConn();
            String sentenciaSql="SELECT FZCDREP FROM BZREPRO " +
                            "WHERE FZCCUSU=? AND FZCNREP=? ";
            ps=conn.prepareStatement(sentenciaSql);
            ps.setString(1,codUsuario);
            ps.setString(2,nomRepro);
            rs=ps.executeQuery();
            
            if (rs.next()) { 
            	datosleidos = rs.getString(1);
            }else{
            	datosleidos = null;
            }
        } catch (Exception e) {
        	String mesError = new String("ERROR: Leer: "+e.getMessage());
        	System.err.println(mesError);
            throw new Exception(mesError, e);
        } finally {
        	ToolsBD.closeConn(conn, ps, rs);
        }
        if (datosleidos != null) {
        	//Hemos encontrado la Repro
        	this.repro = datosleidos;
        	this.codUsuario = codUsuario;
        	this.nomRepro = nomRepro;
        }else{
        	//No hemos encontrado la Repro
        	throw new Exception("No s'ha trobat la Repro. CodUsu:"+codUsuario+". NomRepro:"+nomRepro);
        }
	}
	private void eliminar() throws SQLException, ClassNotFoundException, Exception {
		this.eliminar(this.getUsuario(),this.getNombre());
	}
	public void eliminar(String codUsuario, String nomRepro) throws SQLException, ClassNotFoundException, Exception {
		Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        try {
        	conn=ToolsBD.getConn();
            String sentenciaSql= "DELETE FROM BZREPRO  " +
                                 "WHERE  ( FZCCUSU = ?  AND FZCNREP = ?)";
            ps=conn.prepareStatement(sentenciaSql);
            ps.setString(1,codUsuario);
            ps.setString(2,nomRepro);
            ps.execute();
            //conn.commit();
        } catch (Exception e) {
        	String resposta = "ERROR: No s'ha pogut esborrar la repro.";
        	System.err.println(resposta);
            //e.printStackTrace();
            throw new Exception(resposta, e);
        } finally {
        	ToolsBD.closeConn(conn, ps, rs);
        }
	}
	public boolean grabar(String codUsuario, String nomRepro, String ReproValor, String tipRepro) throws SQLException, ClassNotFoundException, Exception{
		this.setNombre(nomRepro);
		this.setCodUsuario(codUsuario);
		this.setRepro(ReproValor);
		this.setTipRepro(tipRepro);
		return this.grabar();
	}
	private boolean grabar() throws SQLException, ClassNotFoundException, Exception{
		Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        boolean ok = true;
      
        try {
        	conn=ToolsBD.getConn();
            String sentenciaSql= "INSERT INTO BZREPRO ( FZCCUSU,FZCNREP,FZCDREP,FZTIREP) " +
                                 "VALUES (?,?,?,?)";
            ps=conn.prepareStatement(sentenciaSql);
            ps.setString(1,this.codUsuario);
            ps.setString(2,this.nomRepro);
            ps.setString(3,this.repro);
            ps.setString(4,this.tipRepro);
            ps.execute();
           // System.out.println("Insert correcto");
           // conn.commit();
        } catch (Exception e) {
        	//String resposta = "ERROR: No s'ha pogut desar la repro. (ReproUsuarioBean)";
        	ok = false;
        	//System.err.println(resposta);
            //e.printStackTrace();
            //throw.  e;
        } finally {
        	ToolsBD.closeConn(conn, ps, rs);
        }
        return ok;
	}
	public Vector recuperarRepros(String usuario, String tipo){
		Vector vectorRegistrosRepro=new Vector();
		Connection conn = null;
        ResultSet rs = null;
        PreparedStatement ps = null;
        
		try {
		  	conn=ToolsBD.getConn();
            String sentenciaSql="SELECT FZCNREP,FZCDREP FROM BZREPRO " +
                                "WHERE FZCCUSU=? ";
            if(tipo != null){
            	sentenciaSql+=" AND FZTIREP=? ";
            }
            sentenciaSql+= " ORDER BY FZCNREP ASC";
            ps=conn.prepareStatement(sentenciaSql);
            ps.setString(1,usuario);
            if(tipo != null){
            	ps.setString(2,tipo);
            }
            rs=ps.executeQuery();
            while (rs.next()) {
            	RegistroRepro registro = new RegistroRepro();
            	registro.setNomRepro(rs.getString("FZCNREP").trim());
            	registro.setRepro(rs.getString("FZCDREP").trim());
            	registro.setCodUsuario(usuario);
            	vectorRegistrosRepro.addElement(registro);
            }
            
		}catch(Exception e){
        	String resposta = "ERROR: No s'ha pogut llegir les repros de l'usuari:"+usuario;
        	System.out.println(resposta);
            e.printStackTrace();
		} finally {
        	ToolsBD.closeConn(conn, ps, rs);
        }
		return vectorRegistrosRepro;
	}
	public Vector recuperarRepros(String usuario){
			return recuperarRepros(usuario,null);
	}
	private String getRepro(){
		return this.repro;
	}
	private String getUsuario(){
	    return this.codUsuario;	
	}
	private String getNombre(){
		return this.nomRepro;
	}
	private String getTipRepro(){
		return this.tipRepro;
	}
	private void setRepro(String repro){
		this.repro = repro;
	}
	private void setCodUsuario(String usuario){
		this.codUsuario = usuario;
	}
	private void setNombre(String nombre){
		this.nomRepro = nombre;
	}
	private void setTipRepro(String tipoRepro){
		this.tipRepro = tipoRepro;
	}
	public void ejbActivate() throws EJBException, RemoteException {
	}

	public void ejbPassivate() throws EJBException, RemoteException {
	}

	public void ejbRemove() throws EJBException, RemoteException {
	}
	public void ejbCreate() throws CreateException {
	}
	public void setSessionContext(SessionContext arg0) throws EJBException,
			RemoteException {
	}

}
