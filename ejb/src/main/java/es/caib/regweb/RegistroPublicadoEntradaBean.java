package es.caib.regweb;

import java.util.*;
import java.text.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.naming.*;

import java.rmi.*;
import javax.ejb.*;

/**
 * Bean que gestiona publicació del registre d'entrada
 * @author  FJMARTINEZ
 * @version 1.0
 *
 * Created on 13 de junio de 2002, 18:41
 */

public class RegistroPublicadoEntradaBean implements SessionBean {
	
	private SessionContext contextoSesion;
	
	private int anoEntrada;
	private int numero;
	private int oficina;
	private int numeroBOCAIB;
	private int fecha;
	private int pagina;
	private int lineas;
	private String contenido;
	private String observaciones;
	
	public RegistroPublicadoEntradaBean() {
	}
	
	
	public int getAnoEntrada() {
		return this.anoEntrada;
	}
	
	public void setAnoEntrada(int anoEntrada) {
		this.anoEntrada = anoEntrada;
	}
	
	public int getNumero() {
		return this.numero;
	}
	
	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	public int getOficina() {
		return this.oficina;
	}
	
	public void setOficina(int oficina) {
		this.oficina = oficina;
	}
	
	public int getNumeroBOCAIB() {
		return this.numeroBOCAIB;
	}
	
	public void setNumeroBOCAIB(int numeroBOCAIB) {
		this.numeroBOCAIB = numeroBOCAIB;
	}
	
	public String getFecha() {
		DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
		DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date fechaBOIB=null;
		String fechaC1=String.valueOf(fecha);
		String data;
		try {
			fechaBOIB=yyyymmdd.parse(fechaC1);
			data=(ddmmyyyy.format(fechaBOIB));
		} catch (Exception e) {
			data=fechaC1;
		}
		return data;
	}
	
	public void setFecha(int fecha) {
		this.fecha = fecha;
	}
	
	public int getPagina() {
		return this.pagina;
	}
	
	public void setPagina(int pagina) {
		this.pagina = pagina;
	}
	
	public int getLineas() {
		return this.lineas;
	}
	
	public void setLineas(int lineas) {
		this.lineas = lineas;
	}
	
	public String getContenido() {
		return this.contenido;
	}
	
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	
	public String getObservaciones() {
		return this.observaciones;
	}
	
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	
	
	public boolean leer() throws Exception{
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean leido=false;
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BZPUBLI WHERE FZEANOEN=? AND FZENUMEN=? AND FZECAGCO=?";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setInt(1,anoEntrada);
			ps.setInt(2,numero);
			ps.setInt(3,oficina);
			rs=ps.executeQuery();
			if (rs.next()) {
				leido=true;
				anoEntrada=rs.getInt("FZEANOEN");
				numero=rs.getInt("FZENUMEN");
				oficina=rs.getInt("FZECAGCO");
				numeroBOCAIB=rs.getInt("FZENBOCA");
				fecha=rs.getInt("FZEFPUBL");
				pagina=rs.getInt("FZENPAGI");
				lineas=rs.getInt("FZENLINE");
				contenido=rs.getString("FZECONEN");
				observaciones=rs.getString("FZEOBSER");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("S'ha produït un error select BZPUBLI",e);
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return leido;
	}
	
	public void borrar() throws Exception{
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="DELETE FROM BZPUBLI WHERE FZEANOEN=? AND FZENUMEN=? AND FZECAGCO=?";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setInt(1,anoEntrada);
			ps.setInt(2,numero);
			ps.setInt(3,oficina);
			ps.execute();
			//ps.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("S'ha produït un error delete BZPUBLI", e);
		} finally {
				// Tancam el que pugui estar obert
				ToolsBD.closeConn(conn, ps, rs);
		}
	}
	
	public void grabar() throws Exception{
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="UPDATE BZPUBLI SET FZENBOCA=?, FZEFPUBL=?, FZENPAGI=?, FZENLINE=?, FZECONEN=?, FZEOBSER=?" +
			" WHERE FZEANOEN=? AND FZENUMEN=? AND FZECAGCO=?";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setInt(1,numeroBOCAIB);
			ps.setInt(2,fecha);
			ps.setInt(3,pagina);
			ps.setInt(4,lineas);
			ps.setString(5, contenido);
			ps.setString(6, observaciones);
			
			ps.setInt(7,anoEntrada);
			ps.setInt(8,numero);
			ps.setInt(9,oficina);
			int afectados=ps.executeUpdate();
			ps.close();
			
			if (afectados==0) {
				sentenciaSql="INSERT INTO BZPUBLI (FZENBOCA, FZEFPUBL, FZENPAGI, FZENLINE, FZECONEN, FZEOBSER," +
				" FZEANOEN, FZENUMEN, FZECAGCO) VALUES (?,?,?,?,?,?,?,?,?)";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setInt(1,numeroBOCAIB);
				ps.setInt(2,fecha);
				ps.setInt(3,pagina);
				ps.setInt(4,lineas);
				ps.setString(5, contenido);
				ps.setString(6, observaciones);
				
				ps.setInt(7,anoEntrada);
				ps.setInt(8,numero);
				ps.setInt(9,oficina);
				ps.execute();
				ps.close();
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("S'ha produ\357t un error delete BZPUBLI", e);
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
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
	}
	
	
}