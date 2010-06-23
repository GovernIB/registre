package es.caib.regweb;

import javax.sql.DataSource;
import javax.naming.*;
import java.sql.*;
import java.util.*;
import java.rmi.RemoteException;
import java.text.*;
import javax.servlet.http.*;

/**
 * Classe d'utilitat de la connexió amb la BBDD
 * @author  FJMARTINEZ
 * @version 1.0
 */

public class ToolsBD {
	/**
	 * Devuelve una conexión con la BD obtenida vía JNDI.
	 */
	public static Connection getConn() throws RemoteException {
		try{
			Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup("java:/es.caib.regweb.db");
			return ds.getConnection();
		} catch(Exception e){
			throw new RemoteException("Error recuperant la connexió a la BD", e);
		}
	}
	
	public static void closeConn(Connection co, PreparedStatement ps, ResultSet rs ) {
		try {
			// Tancam el que pugui estar obert
			if (rs != null)
				rs.close();
			if (ps != null) 
				ps.close();
			rs=null;
			ps=null;
		} catch (Exception e){
			System.out.println("Excepció tancant ResultsSet i PreparedStatement.");
			e.printStackTrace();
		}finally{
			try{
			//Si la connexió no està tancada, la tancam
				if (co != null && !co.isClosed()) 
					co.close();
				co=null;
			}catch (Exception e){
				System.out.println("Excepció en tancar la connexió: ");
				e.printStackTrace();
			}
		}
	}
	
	public static int RecogerNumeroEntrada(Connection con, int anyo, String idOficina, Hashtable errores) throws RemoteException {
		return RecogerNumero(con, anyo, idOficina, "E", errores);
	}
	public static int RecogerNumeroSalida(Connection con, int anyo, String idOficina, Hashtable errores) throws RemoteException {
		return RecogerNumero(con, anyo, idOficina, "S", errores);
	}
	public static int RecogerNumeroOficio(Connection con, int anyo, String idOficina, Hashtable errores) throws RemoteException {
		return RecogerNumero(con, anyo, idOficina, "O", errores);
	}

	/**
	 * Recoge una PK para una entrada en el registro.
	 *
	 * @param fzaanoe Año del registro
	 * @param oficina Código de la oficina que solicita el número
	 * @param tipo "E" para entradas o "S" para Salidas
	 */
	private static int RecogerNumero(Connection con, int fzaanoe, String oficina, String tipo, Hashtable errores) throws RemoteException {
		int numero=0;
		ResultSet rs = null;
		
		PreparedStatement ps = null;
		PreparedStatement ps2 = null;
		
		String consulta = "SELECT * FROM BZCONES WHERE FZDAENSA=? AND FZDCENSA=? AND FZDCAGCO=? "; // CHANGED quitado "with rs" para compatibilidad
		String update = "UPDATE BZCONES SET FZDNUMER=FZDNUMER+1 WHERE FZDAENSA=? AND FZDCENSA=? AND FZDCAGCO=?";
		try {
			/* Actualizamos el numero de entrada */
			ps=con.prepareStatement(update);
			ps.setInt(1,fzaanoe);
			ps.setString(2,tipo);
			ps.setInt(3,Integer.parseInt(oficina));
			int num = ps.executeUpdate();
			ps.close(); //Nuevo
			
			
			ps2=con.prepareStatement(consulta);
			ps2.setInt(1,fzaanoe);
			ps2.setString(2,tipo);
			ps2.setInt(3,Integer.parseInt(oficina));
			rs=ps2.executeQuery();
			if (rs.next())  numero=rs.getInt("FZDNUMER");
			else {
				errores.put(".","No s'ha inicialitzat el comptador d'entrades/sortides per a l'oficina - any "+oficina+"-"+fzaanoe);
				throw new RemoteException("No s'ha inicialitzat el comptador d'entrades/sortides per a l'oficina - any "+oficina+"-"+fzaanoe);
			}
			rs.close();
			ps2.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			errores.put("z","No és possible gravar el registre ara, torni a intentar-ho ");
			throw new RemoteException("No és possible gravar el registre ara, torni a intentar-ho més tard ", ex);
		}
		return numero;
	}
	
	public static void actualizaDisqueteEntrada(Connection conn, int disquete, String oficina, String anoEntrada, Hashtable errores) throws RemoteException {
		actualizaDisquete(conn, disquete, oficina, "E", anoEntrada, errores);
	}
	public static void actualizaDisqueteSalida(Connection conn, int disquete, String oficina, String anoSalida, Hashtable errores) throws RemoteException {
		actualizaDisquete(conn, disquete, oficina, "S", anoSalida, errores);
	}
	/**
	 * Actualiza el numero de Disquete. Si el numero a actualizar es mayor que el leido.
	 *
	 * @param con Connection
	 * @param disquete int con el numero del disquete a actualizar
	 * @param oficina Código de la oficina que solicita el número
	 * @param tipo    "E"=Entrada   "S"=Salida
	 * @param anyo  año a actualizar
	 * @param errores   Hastable con los errores
	 */
	public static void actualizaDisquete(Connection conn, int disquete, String oficina, String tipo, String anyo, Hashtable errores) throws RemoteException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		PreparedStatement ts = null;
		try {
			String sentenciaSql="SELECT * FROM BZDISQU WHERE FZLCENSA=? AND FZLCAGCO=? AND FZLAENSA=? "; // CHANGED quitado "with rs" para compatibilidad sql
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1, tipo);
			ps.setString(2, oficina);
			ps.setString(3, anyo);
			rs=ps.executeQuery();
			int numeroDisquete=0;
			if (rs.next()) {
				numeroDisquete=rs.getInt("FZLNDIS");
				//rs.close();
				//ps.close();
				/* Actualizamos el numero de disquete si es mayor al leido */
				if (disquete>numeroDisquete) {
					sentenciaSql="UPDATE BZDISQU SET FZLNDIS=? WHERE FZLCENSA=? AND FZLCAGCO=? AND FZLAENSA=?";
					ts=conn.prepareStatement(sentenciaSql);
					ts.setInt(1,disquete);
					ts.setString(2,tipo);
					ts.setInt(3,Integer.parseInt(oficina));
					ts.setInt(4,Integer.parseInt(anyo));
					boolean cualquiera=ts.execute();
					//ts.close();
				}
			} else if (disquete>0) {
				sentenciaSql="INSERT INTO BZDISQU (FZLCENSA, FZLCAGCO, FZLAENSA, FZLNDIS)" +
				" VALUES (?, ?, ?, ?)";
				ts=conn.prepareStatement(sentenciaSql);
				ts.setString(1,tipo);
				ts.setInt(2,Integer.parseInt(oficina));
				ts.setInt(3,Integer.parseInt(anyo));
				ts.setInt(4,disquete);
				boolean cualquiera=ts.execute();
				//ts.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			errores.put("","No és possible gravar el registre ara, torni a intentar-ho ");
			throw new RemoteException("No és possible gravar el registre ara, torni a intentar-ho ",e);
		} finally {
			try{
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (ts != null)
					ts.close();
			} catch (Exception e) {
				System.out.println("Excepció tancant ResultsSet i PreparedStatement.");
				e.printStackTrace();
			}
			
		}
	}
	
	public static String convierteEntidad(String entidadCastellano, Connection con) {
		Connection conn = con;
		ResultSet rs = null;
		PreparedStatement ps = null;
		String entidadCatalan=null;
		
		try {
			//conn=getConn();
			String sentenciaSql="SELECT * FROM BZENTID WHERE FZGCENTI=? AND FZGFBAJA=0";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,entidadCastellano);
			rs=ps.executeQuery();
			if (rs.next()) {
				entidadCatalan=rs.getString("FZGCENT2");
			} else {
				entidadCatalan="";
			}
			if (rs!=null) {
				rs.close();
			}
			if (ps!=null) {
				ps.close();
			}
		} catch (Exception e) {
			System.out.println("ERROR: convierteEntidad");
			System.out.println(e.getMessage());
			e.printStackTrace();
			entidadCatalan="";
			
		} /*finally {
		closeConn(conn, ps, rs);
		}*/
		return entidadCatalan;
	}
	
	public static boolean estaPdteVisado(String tipo, String oficina, String ano, String numero) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		boolean pdteVisado=false;
		try {
			conn=getConn();
			String sentenciaSql="SELECT * FROM BZMODIF WHERE (FZJIEXTR=' ' OR FZJIREMI=' ' OR FZJIEXTR='' OR FZJIREMI='') AND FZJFVISA=0 " +
			"AND FZJCENSA=? AND FZJCAGCO=? AND FZJANOEN=? AND FZJNUMEN=?";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,tipo);
			ps.setString(2,oficina);
			ps.setString(3,ano);
			ps.setString(4,numero);
			rs=ps.executeQuery();
			if (rs.next()) {
				pdteVisado=true;
			} else {
				pdteVisado=false;
			}
		} catch (Exception e) {
			System.out.println("ERROR: convierteEntidad");
			System.out.println(e.getMessage());
			e.printStackTrace();
			pdteVisado=false;
		} finally {
			closeConn(conn, ps, rs);
		}
		return pdteVisado;       
	}
	
	
	static int rellenaBytes(String cadena, byte[] buf, int offset, int longitud) {
		if (cadena==null) {
			cadena="";
		}
		
		StringBuffer nuevaCadena=new StringBuffer();
		int longitudCadena=cadena.length();
		for (int n=0;n<longitudCadena;n++) {
			nuevaCadena.append(cadena.substring(n,n+1));
			if (cadena.substring(n,n+1).equals("'")) {
				nuevaCadena.append("'");
				longitud++;
			}
		}
		cadena=nuevaCadena.toString();
		int cont=0;
		while (cont<longitud && cont<cadena.length()) {
			buf[offset+cont]=(byte)cadena.charAt(cont);
			cont++;
		}
		while (cont<longitud) {
			buf[offset+cont]= (byte)' ';
			cont++;
		}
		return longitud;
	}
	
	static int rellenaNumeros(int numero, byte[] buf, int offset, int longitud) {
		String numerChar=Integer.toString(numero);
		while (numerChar.length()<longitud) {
			//numerChar="0"+numerChar;
			numerChar=new StringBuffer().append("0").append(numerChar).toString();
		}
		
		int cont=0;
		while (cont<longitud && cont<numerChar.length()) {
			buf[offset+cont]=(byte)numerChar.charAt(cont);
			cont++;
		}
		return longitud;
	}
	
	public static String recuperarPassword(HttpServletRequest request) {
		String cadena=request.getHeader("authorization");
		String userPasswordEncry=cadena.substring(6);
		Base64 base = new Base64();
		String userPassword=base.decode(userPasswordEncry);
		return userPassword.substring(userPassword.indexOf(":")+1);
	}
	
}
