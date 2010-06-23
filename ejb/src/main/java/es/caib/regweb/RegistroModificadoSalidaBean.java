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

import java.lang.reflect.InvocationTargetException;

/**
 * Bean que gestiona la modificació del registre de sortida
 * @author  FJMARTINEZ
 * @version 1.0
 */


public class RegistroModificadoSalidaBean implements SessionBean {
	
	private SessionContext sessioEjb;
	
	private int anoSalida;
	private static final String TIPO_REGISTRO="S";
	private String entidad1;
	private String entidad1Catalan;
	private int entidad2;
	private String extracto;
	private String usuarioModificacion;
	private String usuarioVisado;
	private String indVisExtracto;
	private String indVisRemitente;
	private int numeroRegistro;
	private String remitente;
	private String motivo;
	private String fechaModificacion;
	private String horaModificacion;
	private boolean leido=false;
	private boolean hayVisadoRemitente=false;
	private boolean hayVisadoExtracto=false;
	private String idiomaExtracto="";
	private int fechaDocumento=0;
	private String tipoDocumento="";
	private int fechaRegistro=0;
	private int fzacagge=0;
	private int destinatario=0;
	private String idioma="";
	private String fora="";
	private String comentario="";
	private String altres="";
	private String entidad1Old="";
	private int entidad2Old=0;
	private String password="";
	
	/**
	 * Holds value of property oficina.
	 */
	private int oficina;
	
	/* Set's para la creacion del objeto RegistroModificadoEntrada */
	public void setAnoSalida(int anoSalida) {
		this.anoSalida=anoSalida;
	}
	public void setOficina(int oficina) {
		this.oficina = oficina;
	}
	public void setEntidad1(String entidad1) {
		this.entidad1=entidad1;
	}
	public void setExtracto(String extracto) {
		this.extracto=extracto;
	}
	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion=usuarioModificacion;
	}
	public void setUsuarioVisado(String usuarioVisado) {
		this.usuarioVisado=usuarioVisado;
	}
	public void setIndVisExtracto(String indVisExtracto) {
		this.indVisExtracto=indVisExtracto;
	}
	public void setIndVisRemitente(String indVisRemitente) {
		this.indVisRemitente=indVisRemitente;
	}
	public void setEntidad2(int entidad2) {
		this.entidad2=entidad2;
	}
	public void setNumeroRegistro(int numeroRegistro) {
		this.numeroRegistro=numeroRegistro;
	}
	public void setRemitente(String remitente) {
		this.remitente=remitente;
	}
	public void setMotivo(String motivo) {
		this.motivo=motivo;
	}
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion=fechaModificacion;
	}
	public void setHoraModificacion(String horaModificacion) {
		this.horaModificacion=horaModificacion;
	}
	public void setVisarRemitente(boolean hayVisadoRemitente) {
		this.hayVisadoRemitente=hayVisadoRemitente;
	}
	public void setVisarExtracto(boolean hayVisadoExtracto) {
		this.hayVisadoExtracto=hayVisadoExtracto;
	}
	public void fijaPasswordUser(String password) {
		this.password=password;
	}
	
	
	public RegistroModificadoSalidaBean() {
	}
	
	public void leer() {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BZMODIF WHERE FZJCENSA='S' AND FZJCAGCO=? AND FZJNUMEN=? AND FZJANOEN=? AND" +
			" FZJFMODI=? AND FZJHMODI=?";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setInt(1,oficina);
			ps.setInt(2,numeroRegistro);
			ps.setInt(3,anoSalida);
			ps.setString(4, fechaModificacion);
			ps.setString(5, horaModificacion);
			rs=ps.executeQuery();
			if (rs.next()) {
				leido=true;
				anoSalida=rs.getInt("FZJANOEN");
				entidad1=rs.getString("FZJCENTI");
				entidad1Catalan=ToolsBD.convierteEntidad(entidad1,conn);
				entidad2=rs.getInt("FZJNENTI");
				extracto=rs.getString("FZJCONEN");
				usuarioModificacion=rs.getString("FZJCUSMO");
				usuarioVisado=rs.getString("FZJCUSVI");
				indVisRemitente=rs.getString("FZJIREMI");
				indVisExtracto=rs.getString("FZJIEXTR");
				numeroRegistro=rs.getInt("FZJNUMEN");
				remitente=rs.getString("FZJREMIT");
				motivo=rs.getString("FZJTEXTO");
				oficina=rs.getInt("FZJCAGCO");
				if (!(rs.getString("FZJIREMI").equals(" ") || rs.getString("FZJIREMI").equals(""))) {
					entidad1="";
					entidad1Catalan="";
					entidad2=0;
					remitente="";
				}
				if (!(rs.getString("FZJIEXTR").equals(" ") || rs.getString("FZJIEXTR").equals(""))) {
					extracto="";
				}
			}
			/* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
			Date fechaSystem=new Date();
			DateFormat aaaammdd=new SimpleDateFormat("yyyyMMdd");
			DateFormat hhmmss=new SimpleDateFormat("HHmmss");
			DateFormat sss=new SimpleDateFormat("S");
			String ss=sss.format(fechaSystem);
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
            int fzafsis=Integer.parseInt(aaaammdd.format(fechaSystem));
            logLopdBZMODIF("SELECT", sessioEjb.getCallerPrincipal().getName().toUpperCase() 
					, fzafsis, horamili, 'S', numeroRegistro, anoSalida, oficina, Integer.parseInt(fechaModificacion), Integer.parseInt(horaModificacion) );
		} catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
			e.printStackTrace();
			leido=false;
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
	}
	
	public boolean visar() throws SQLException, ClassNotFoundException, Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		Date fechaSystem=new Date();
		DateFormat aaaammdd=new SimpleDateFormat("yyyyMMdd");
		DateFormat hhmmss=new SimpleDateFormat("HHmmss");
		DateFormat sss=new SimpleDateFormat("S");
		String ss=sss.format(fechaSystem);
		if (ss.length()>2) {
			ss=ss.substring(0,2);
		}
		boolean visado=false;
		try {
			conn=ToolsBD.getConn();
			conn.setAutoCommit(false);
			//String sentenciaSql="update bzmodif set fzjtexto=?, fzjcusvi=?, fzjfvisa=?, fzjhvisa=?" +
			String sentenciaSql="UPDATE BZMODIF SET FZJCUSVI=?, FZJFVISA=?, FZJHVISA=?" +
			((hayVisadoExtracto) ? ", FZJIEXTR=?" : "") +
			((hayVisadoRemitente) ? ", FZJIREMI=?" : "" )+
			" WHERE FZJCENSA='S' AND FZJCAGCO=? AND FZJNUMEN=? AND FZJANOEN=? AND FZJFMODI=? AND FZJHMODI=?";
			ps=conn.prepareStatement(sentenciaSql);
			//ps.setString(1, motivo);
			ps.setString(1, usuarioVisado);
			ps.setInt(2, Integer.parseInt(aaaammdd.format(fechaSystem)));
			ps.setInt(3, Integer.parseInt(hhmmss.format(fechaSystem)+ss));
			int contador=4;
			if (hayVisadoExtracto) {
				ps.setString(contador++, "X");
			}
			if (hayVisadoRemitente) {
				ps.setString(contador++, "X");
			}
			ps.setInt(contador++,oficina);
			ps.setInt(contador++,numeroRegistro);
			ps.setInt(contador++,anoSalida);
			ps.setString(contador++, fechaModificacion);
			ps.setString(contador++, horaModificacion);
			
			int registrosAfectados=ps.executeUpdate();
			//ps.close();
			
			if (registrosAfectados>0 && !hayVisadoExtracto && !hayVisadoRemitente) {
				visado=true;
			}
			
			if (registrosAfectados>0 && (hayVisadoExtracto || hayVisadoRemitente) ) {
				boolean generado=generarBZVISAD( conn, Integer.parseInt(aaaammdd.format(fechaSystem)),
						Integer.parseInt(hhmmss.format(fechaSystem)+ss) );
				if (generado) { // Aztualizamos el BZSALIDA
					visado=actualizarBZSALIDA(conn);
				}
				

                // desacoplamiento cobol
				String rem="";
				String com="";
				if (hayVisadoRemitente) {
					if (!remitente.trim().equals("")) {
						rem=remitente;
					} else {
						javax.naming.InitialContext contexto = new javax.naming.InitialContext();
						Object ref = contexto.lookup("es.caib.regweb.ValoresHome");
						ValoresHome home=(ValoresHome)javax.rmi.PortableRemoteObject.narrow(ref, ValoresHome.class);
						Valores valor=home.create();
						rem=valor.recuperaRemitenteCastellano(entidad1, entidad2+"");
						valor.remove();
					}
				} else {
					if (!altres.trim().equals("")) {
						rem=remitente;
					} else {
						javax.naming.InitialContext contexto = new javax.naming.InitialContext();
						Object ref = contexto.lookup("es.caib.regweb.ValoresHome");
						ValoresHome home=(ValoresHome)javax.rmi.PortableRemoteObject.narrow(ref, ValoresHome.class);
						Valores valor=home.create();
						rem=valor.recuperaRemitenteCastellano(entidad1Old, entidad2Old+"");
						valor.remove();
					}
				}
				if (hayVisadoExtracto) {
					com=extracto;
				} else {
					com=comentario;
				}
                try {
                    Class t = Class.forName("es.caib.regweb.module.PluginHook");

                    Class [] partypes = {
                        String.class , Integer.class, Integer.class, Integer.class, Integer.class, String.class,
                     	String.class, String.class, Integer.class, Integer.class, String.class, Integer.class, String.class  
                    };

                    Object [] params = {
                        "V", new Integer(anoSalida), new Integer(numeroRegistro), new Integer(oficina), new Integer(fechaDocumento), rem, 
                        com, tipoDocumento, new Integer(fechaRegistro), new Integer(fzacagge), fora, new Integer(destinatario), idioma
                    };

                    java.lang.reflect.Method metodo = t.getMethod("salida", partypes);
                    metodo.invoke(null, params);

                    //t.salida("V", anoSalida, numeroRegistro, oficina, fechaDocumento, rem,
    				//	com, tipoDocumento, fechaRegistro, fzacagge, fora, destinatario, idioma);


                // ignorar excepciones en caso de que no se encuentre la clase o el método, pero pasar las excepciones que genere el metodo invocado
                } catch (IllegalAccessException iae) { 
                } catch (IllegalArgumentException iae) { 
                } catch (InvocationTargetException ite) {
                } catch (NullPointerException npe) { 
                } catch (ExceptionInInitializerError eiie) {
                } catch (NoSuchMethodException nsme) {
                } catch (SecurityException se) {
                } catch (LinkageError le) {
                } catch (ClassNotFoundException le) {
                }
                
			}
			conn.commit();
			/* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
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
            int fzafsis=Integer.parseInt(aaaammdd.format(fechaSystem));
            logLopdBZMODIF("UPDATE", sessioEjb.getCallerPrincipal().getName().toUpperCase() 
					, fzafsis, horamili, 'S', numeroRegistro, anoSalida, oficina, Integer.parseInt(fechaModificacion), Integer.parseInt(horaModificacion) );
		} catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
			e.printStackTrace();
			visado=false;
			try {
				if (conn!=null)
					conn.rollback();
				else
					System.out.println("ERROR: No es pot fer rollback sense connexió!");
			} catch (Exception ex) {
				System.out.println("Error: "+e.getMessage());
				ex.printStackTrace();
				//throw new Exception("S'ha produ\357t un error i no s'han pogut tornar enrere els canvis efectuats", ex);
			}
		} finally {
			ToolsBD.closeConn(conn, ps, null);
		}
		return visado;
	}
	
	
	private boolean actualizarBZSALIDA(Connection conn) throws SQLException, ClassNotFoundException, Exception {
		boolean generado=false;
		PreparedStatement ps = null;
		
		Date fechaSystem=new Date();
		DateFormat aaaammdd=new SimpleDateFormat("yyyyMMdd");
		DateFormat hhmmss=new SimpleDateFormat("HHmmss");

		String entidad1Valor="";
		int entidad2Valor=0;
		String remitenteValor="";
		if (hayVisadoRemitente) {
			if (entidad1.trim().equals("")) {
				remitenteValor=remitente;
				entidad1Valor=" ";
				entidad2Valor=0;
			} else {
				remitenteValor="";
				entidad1Valor=entidad1;
				entidad2Valor=entidad2;
			}
		}
		
		String actualizaBZSALIDA="UPDATE BZSALIDA SET FZSFACTU=? " +
		((hayVisadoExtracto && idiomaExtracto.equals("1")) ? ", FZSCONEN=?" : "") +
		((hayVisadoExtracto && !idiomaExtracto.equals("1")) ? ", FZSCONE2=?" : "") +
		// ((hayVisadoExtracto && hayVisadoRemitente) ? ", " : "") +
		((hayVisadoRemitente) ? ", FZSREMIT=?, FZSCENTI=?, FZSNENTI=?" : "" ) +
		" WHERE FZSNUMEN=? AND FZSCAGCO=? AND FZSANOEN=?";
		try {
			ps=conn.prepareStatement(actualizaBZSALIDA);
			int contador=1;
			
			ps.setInt(contador++, Integer.parseInt(aaaammdd.format(fechaSystem)));
			
			if (hayVisadoExtracto) {
				ps.setString(contador++, extracto);
			}
			if (hayVisadoRemitente) {
				ps.setString(contador++, remitenteValor);
				ps.setString(contador++, entidad1Valor);
				ps.setInt(contador++, entidad2Valor);
			}
			ps.setInt(contador++, numeroRegistro);
			ps.setInt(contador++, oficina);
			ps.setInt(contador++, anoSalida);
			
			int registrosAfectados=ps.executeUpdate();
			ps.close();
			generado=(registrosAfectados>0) ? true : false;

			/* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
			DateFormat sss=new SimpleDateFormat("S");
            String ss=sss.format(fechaSystem);
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
            int fzafsis=Integer.parseInt(aaaammdd.format(fechaSystem));
            logLopdBZSALIDA("UPDATE", usuarioModificacion
    				, fzafsis, horamili, numeroRegistro, anoSalida, oficina);
		} catch (Exception e){
			generado=false;
			System.out.println("Error: "+e.getMessage());
			e.printStackTrace();
			throw new Exception("S'ha produ\357t un error actualizant BZSALIDA");
		}
		return generado;
	}
	
	private boolean generarBZVISAD(Connection conn, int fecha, int hora) throws SQLException, ClassNotFoundException, Exception {
		boolean generado=false;
		PreparedStatement ps = null;
		String insertBZVISAD="INSERT INTO BZVISAD (FZKANOEN, FZKCAGCO, FZKCENSA, FZKCENTF, FZKCENTI, FZKNENTF, FZKNENTI, " +
		"FZKREMIF, FZKREMII, FZKCONEF, FZKCONEI, FZKCUSVI, FZKFENTF, FZKFENTI, FZKFVISA, FZKHVISA,  FZKNUMEN, " +
		"FZKTEXTO) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			// Recuperamos el registro original para la recuperacion de datos
			javax.naming.InitialContext contexto = new javax.naming.InitialContext();
			Object ref = contexto.lookup("es.caib.regweb.RegistroSalidaHome");
			RegistroSalidaHome home=(RegistroSalidaHome)javax.rmi.PortableRemoteObject.narrow(ref, RegistroSalidaHome.class);
			RegistroSalida registro=home.create();
			
			registro.fijaUsuario(usuarioVisado);
			registro.setoficina(oficina+"");
			registro.setNumeroSalida(numeroRegistro+"");
			registro.setAnoSalida(anoSalida+"");
			registro.leer();
			
			if (!registro.getLeido()) {
				throw new Exception("S'ha produ\357t un error i no s'han pogut crear el objecte RegistroSalida");
			}
			
			if (registro.getData().equals("0")) {
				fechaDocumento=0;
			} else {
				java.util.Date fechaTest=null;
				DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
				DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
				fechaTest=ddmmyyyy.parse(registro.getData());
				fechaDocumento=Integer.parseInt(yyyymmdd.format(fechaTest));
			}
			
			if (registro.getDataSalida().equals("0")) {
				fechaRegistro=0;
			} else {
				java.util.Date fechaTest=null;
				DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
				DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
				fechaTest=ddmmyyyy.parse(registro.getDataSalida());
				fechaRegistro=Integer.parseInt(yyyymmdd.format(fechaTest));
			}
			
			tipoDocumento=registro.getTipo();
			fzacagge=Integer.parseInt(registro.getBalears());
			idiomaExtracto=registro.getIdioex();
			fora=registro.getFora();
			destinatario=Integer.parseInt(registro.getRemitent());
			comentario=registro.getComentario();
			idioma=registro.getIdioma();
			altres=registro.getAltres();
			entidad1Old=registro.getEntidad1();
			entidad2Old=Integer.parseInt(registro.getEntidad2());
			
			ps=conn.prepareStatement(insertBZVISAD);
			ps.setInt(1, anoSalida);
			ps.setInt(2, oficina);
			ps.setString(3, TIPO_REGISTRO);
			ps.setString(4, (hayVisadoRemitente) ? entidad1 : " ");
			ps.setString(5, (hayVisadoRemitente) ? registro.getEntidad1Grabada() : " ");
			ps.setInt(6, (hayVisadoRemitente) ? entidad2 : 0);
			ps.setInt(7, (hayVisadoRemitente) ? Integer.parseInt(registro.getEntidad2()) : 0);
			ps.setString(8, (hayVisadoRemitente) ? remitente : " ");
			ps.setString(9,(hayVisadoRemitente) ? registro.getAltres() : " ");
			ps.setString(10, (hayVisadoExtracto) ? extracto : " ");
			ps.setString(11,(hayVisadoExtracto) ? registro.getComentario() : " ");
			ps.setString(12, usuarioVisado);
			ps.setInt(13,0);
			ps.setInt(14,0);
			ps.setInt(15, fecha);
			ps.setInt(16, hora);
			ps.setInt(17, numeroRegistro);
			ps.setString(18, motivo);
			
			ps.execute();
			generado=true;
			registro.remove();
			ps.close();
			
		} catch (Exception e){
			generado=false;
			System.out.println("Error: "+e.getMessage());
			e.printStackTrace();
			throw new Exception("S'ha produ\357t un error insert BZVISAD");
		}
		return generado;
	}
	
	public List recuperarRegistros(String oficina, String usuario) {
		List registros=new ArrayList();
		Connection conn=null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		String sentenciaSql="";
		String fecha="";
		java.util.Date fechaDocumento=null;
		DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
		DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
		
		if (oficina.equals("00")) {
			sentenciaSql="SELECT * FROM BZMODIF WHERE (FZJIEXTR=' ' OR FZJIREMI=' ' OR FZJIEXTR='' OR FZJIREMI='') AND FZJFVISA=0 AND FZJCAGCO " +
			"IN (SELECT FZHCAGCO FROM BZAUTOR WHERE FZHCUSU=? AND FZHCAUT=?) AND FZJCENSA='S' ORDER BY " +
			"FZJCAGCO, FZJANOEN, FZJNUMEN, FZJFMODI, FZJHMODI";
		} else {
			sentenciaSql="SELECT * FROM BZMODIF WHERE FZJCAGCO=? AND FZJFVISA=0 AND (FZJIEXTR=' ' OR FZJIREMI=' ' OR FZJIEXTR='' OR FZJIREMI='') AND FZJCENSA='S' ORDER BY " +
			"FZJCAGCO, FZJANOEN, FZJNUMEN, FZJFMODI, FZJHMODI";
		}
		try {
			conn=ToolsBD.getConn();
			ps=conn.prepareStatement(sentenciaSql);
			if (oficina.equals("00")) {
				ps.setString(1, usuario);
				ps.setString(2, "VS");
			} else {
				ps.setInt(1, Integer.parseInt(oficina));
			}
			rs=ps.executeQuery();
			while (rs.next()) {
				RegistroModificadoSeleccionado reg=new RegistroModificadoSeleccionado();
				reg.setNumeroOficina(rs.getInt("FZJCAGCO"));
				reg.setNumeroRegistro(rs.getInt("FZJNUMEN"));
				reg.setAnoRegistro(rs.getInt("FZJANOEN"));
				if ((!rs.getString("FZJCENTI").trim().equals("") || !rs.getString("FZJREMIT").trim().equals("")) &&
						(rs.getString("FZJIREMI").equals(" ") || rs.getString("FZJIREMI").equals(""))){
					reg.setVisadoR("*");
				} else {
					reg.setVisadoR("");
				}
				if (!rs.getString("FZJCONEN").trim().equals("") && (rs.getString("FZJIEXTR").equals(" ") || rs.getString("FZJIEXTR").equals(""))) {
					reg.setVisadoC("*");
				} else {
					reg.setVisadoC("");
				}
				fecha=String.valueOf(rs.getInt("FZJFMODI"));
				reg.setFechaModif(rs.getInt("FZJFMODI"));
				reg.setHoraModif(rs.getInt("FZJHMODI"));
				try {
					fechaDocumento=yyyymmdd.parse(fecha);
					reg.setFechaModificacion(ddmmyyyy.format(fechaDocumento));
				} catch (Exception e) {
					reg.setFechaModificacion(fecha);
				}
				reg.setMotivoCambio(rs.getString("FZJTEXTO"));
				if ( (rs.getString("FZJIREMI").trim().equals("") && (!rs.getString("FZJCENTI").trim().equals("") || !rs.getString("FZJREMIT").trim().equals("")) )
						|| (rs.getString("FZJIEXTR").trim().equals("") && !rs.getString("FZJCONEN").trim().equals("")) ) {
					registros.add(reg);
				}
			}
		} catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		return registros;
	}
	
	public boolean generarModificacion(Connection conn) {
		boolean generado=false;
		PreparedStatement ps = null;
		String insertBZMODIF="INSERT INTO BZMODIF (FZJANOEN, FZJCAGCO, FZJCENSA, FZJCENTI, FZJCONEN, FZJCUSMO, " +
		"FZJCUSVI, FZJFMODI, FZJFVISA, FZJHMODI, FZJHVISA, FZJIEXTR, FZJIREMI, FZJNENTI, FZJNUMEN, FZJREMIT, " +
		"FZJTEXTO) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		Date fechaSystem=new Date();
		DateFormat aaaammdd=new SimpleDateFormat("yyyyMMdd");
		DateFormat hhmmss=new SimpleDateFormat("HHmmss");
		DateFormat sss=new SimpleDateFormat("S");
		String ss=sss.format(fechaSystem);
		if (ss.length()>2) {
			ss=ss.substring(0,2);
		}
		
		try {
			ps=conn.prepareStatement(insertBZMODIF);
			ps.setInt(1, anoSalida);
			ps.setInt(2, oficina);
			ps.setString(3, TIPO_REGISTRO);
			ps.setString(4, entidad1);
			ps.setString(5, extracto);
			ps.setString(6, usuarioModificacion);
			ps.setString(7, ""); // Usuario que visa
			ps.setInt(8, Integer.parseInt(aaaammdd.format(fechaSystem)));
			ps.setInt(9, 0);
			ps.setInt(10, Integer.parseInt(hhmmss.format(fechaSystem)+ss));
			ps.setInt(11,0);
			ps.setString(12, "");
			ps.setString(13, "");
			ps.setInt(14, entidad2);
			ps.setInt(15, numeroRegistro);
			ps.setString(16, remitente);
			ps.setString(17, motivo);
			
			ps.execute();
			generado=true;
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
			logLopdBZMODIF("INSERT", usuarioModificacion
					, Integer.parseInt(aaaammdd.format(fechaSystem)), horamili, 'S', numeroRegistro, anoSalida, oficina, Integer.parseInt(aaaammdd.format(fechaSystem)),Integer.parseInt(hhmmss.format(fechaSystem)+ss) );
			
		
		} catch (Exception e) {
			System.out.println("RegistroModificadoSalidaBean: Excepción al generar modificacion "+e.getMessage());
			e.printStackTrace();
			generado=false;
		} finally {
			ToolsBD.closeConn(null, ps, null);
		}
		return generado;
	}
	
	public boolean getLeido() {
		return leido;
	}
	public int getNumeroRegistro() {
		return numeroRegistro;
	}
	public int getAnoSalida() {
		return anoSalida;
	}
	public int getOficina() {
		return oficina;
	}
	public String getMotivo() {
		return motivo;
	}
	public String getEntidad1() {
		return entidad1;
	}
	public String getEntidad1Catalan() {
		return entidad1;
	}
	
	public int getEntidad2() {
		return entidad2;
	}
	public String getRemitente() {
		return remitente;
	}
	public String getExtracto() {
		return extracto;
	}
	public void ejbCreate() throws CreateException {
		//System.out.println("EJB creado");
	}
	public void ejbActivate() {
	}
	public void ejbPassivate() {
	}
	public void ejbRemove() {
		// System.out.println("EJB eliminado");
	}
	
	public void setSessionContext(SessionContext ctx) {
		sessioEjb = ctx;
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
			System.out.println("RegistroModificadoSalidaBean: ERROR: S'ha produ\357t un error a logLopdBZSALID");
		} finally {
				// Tancam el que pugui estar obert
				ToolsBD.closeConn(conn, ps, rs);
		}
		//System.out.println("RegistroModificadoSalidaBean: Desada informació dins BZSALPD: "+tipusAcces+" "+usuari+" "+data+" "+hora+" "+nombreRegistre+" "+any+" "+oficina);
     }

	/**
	 * Emplena la taula de control d'accés complint la llei LOPD per la taula BZMODIF 
	 * @param tipusAcces <code>String</code> tipus d'accés a la taula
	 * @param usuari <code>String</code> codi de l'usuari que fa l'acció.
	 * @param data <code>Intr</code> data d'accés en format numèric (ddmmyyyy)
	 * @param hora <code>Int</code> hora d'accés en format numèric (hhmissmis, hora (2 posicions), minut (2 posicions), segons (2 posicions), milisegons (3 posicions)
	 * @param entrsal <code>char</code> Caràcter que indica si és una entrada o una sortida.
	 * @param nombreRegistre <code>Int</code> nombre de registre
	 * @param any <code>Int</code> any del registre
	 * @param oficina <code>Int</code> oficina on s'ha registrat
	 * @author Sebastià Matas Riera (bitel)
	 */
	
	private void logLopdBZMODIF(String tipusAcces, String usuari, int data, int hora, char entrsal, int nombreRegistre, int any, int oficina
			, int dataModif, int horaModif) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			conn=ToolsBD.getConn();
				String sentenciaSql="INSERT INTO BZMOLPD (FZVTIPAC, FZVCUSU, " +
						"FZVDATAC, FZVHORAC, FZVCENSA, " +
						"FZVNUMEN, FZVANOEN, FZVCAGCO, FZVFMODI, FZVHMODI ) " +
						"VALUES (?,?,?,?,?,?,?,?,?,?)";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setString(1,tipusAcces);
				ps.setString(2,usuari);
				ps.setInt(3,data);
				ps.setInt(4,hora);
				ps.setString(5,""+entrsal);
				ps.setInt(6, nombreRegistre);
				ps.setInt(7, any);
				ps.setInt(8,oficina);
				ps.setInt(9,dataModif);
				ps.setInt(10,horaModif);
				ps.execute();
				//ps.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR: S'ha produ\357t un error a logLopdBZENTRA.");
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
		//System.out.println("RegistroModificadoEntradaBean: Desada informació dins BZMOLPD: "+tipusAcces+" "+usuari+" "+data+" "+hora+" "+entrsal+" "+nombreRegistre+" "+any+" "+oficina+" "+dataModif+" "+horaModif);
  	}
	
}