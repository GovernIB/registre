package es.caib.regweb;

import java.util.*;
import java.text.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.rmi.*;

import javax.ejb.*;
import javax.naming.Context;
import javax.naming.InitialContext;

import java.lang.reflect.InvocationTargetException;

/**
 * Bean que gestiona el registre de sortida
 * @author  FJMARTINEZ
 * @version 1.0
 */

public class RegistroSalidaBean implements SessionBean {
	
	private String dataVisado="";
	private String datasalida="";
	private String hora="";
	private String oficina="";
	private String oficinafisica="0";
	private String data="";
	private String tipo="";
	private String idioma="";
	private String entidad1="";
	private String entidad1Grabada="";
	private String entidad2="";
	private String altres="";
	private String balears="";
	private String fora="";
	private String entrada1="";
	private String entrada2="";
	private String remitent="";
	private String idioex="";
	private String disquet="";
	private String comentario="";
	private String usuario;
	private int fzsnume=0;
	private String correo="";
	private String registroAnulado="";
	private boolean actualizacion=false;
	private boolean registroActualizado=false;
	private boolean leidos=false;
	private String motivo="";
	private String entidad1Nuevo="";
	private String entidad2Nuevo="";
	private String altresNuevo="";
	private String comentarioNuevo="";
	private String password="";
	
	private SessionContext ctx;
	
	private boolean error=false;
	private boolean validado=false;
	private boolean registroSalidaGrabado=false;
	private Hashtable errores=new Hashtable();
	private DateFormat dateF= new SimpleDateFormat("dd/MM/yyyy");
	private Date fechaTest=null;
	private DateFormat horaF=new SimpleDateFormat("HH:mm");
	private Date horaTest=null;
	private String entidadCastellano;
	private String SENTENCIA="INSERT INTO BZSALIDA (" +
	"FZSANOEN, FZSNUMEN, FZSCAGCO, FZSFDOCU, FZSREMIT, FZSCONEN, FZSCTIPE, FZSCEDIE, FZSENULA,"+
	"FZSPROCE, FZSFENTR, FZSCTAGG, FZSCAGGE, FZSCORGA, FZSFACTU, FZSCENTI, FZSNENTI, FZSHORA,"+
	"FZSCIDIO, FZSCONE2, FZSNLOC, FZSALOC, FZSNDIS, FZSFSIS, FZSHSIS, FZSCUSU, FZSCIDI"+
	") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private String anoSalida=null;
	private String numeroSalida=null;
	private String descripcionOficina=null;
	private String descripcionOficinaFisica=null;
	private String descripcionDestinatario=null;
	private String descripcionOrganismoRemitente=null;
	private String descripcionDocumento=null;
	private String descripcionIdiomaDocumento=null;
	private String destinoGeografico=null;
	private String idiomaExtracto=null;
	
	public RegistroSalidaBean() {
	}
	
	/** 
	 * @param anoSalida
	 */
	public void setAnoSalida(String anoSalida) {
		this.anoSalida=anoSalida;
	}
	public void setActualizacion(boolean actualizacion) {
		this.actualizacion=actualizacion;
	}
	
	public void fijaPasswordUser(String password) {
		this.password=password;
	}
	
	/**
	 * @param anoSalida
	 */
	public void setRegistroAnulado(String registroAnulado) {
		this.registroAnulado=registroAnulado;
	}
	
	/**
	 * @param numeroEntrada
	 */
	public void setNumeroSalida(String numeroSalida) {
		this.numeroSalida=numeroSalida;
	}
	/**
	 * @param descripcionOficina
	 */
	public void setDescripcionOficina(String descripcionOficina) {
		this.descripcionOficina=descripcionOficina;
	}
	/**
	 * @param descripcionOficinaFisica
	 */
	public void setDescripcionOficinaFisica(String descripcionOficinaFisica) {
		this.descripcionOficinaFisica=descripcionOficinaFisica;
	}
	/**
	 * @param descripcionDestinatario
	 */
	public void setDescripcionDestinatario(String descripcionDestinatario) {
		this.descripcionDestinatario=descripcionDestinatario;
	}
	/**
	 * @param correo
	 */
	public void setCorreo(String correo) {
		this.correo=correo;
	}
	/**
	 * @param descripcionOrganismoRemitente
	 */
	public void setDescripcionOrganismoRemitente(String descripcionOrganismoRemitente) {
		this.descripcionOrganismoRemitente=descripcionOrganismoRemitente;
	}
	/**
	 * @param descripcionDocumento
	 */
	public void setDescripcionDocumento(String descripcionDocumento) {
		this.descripcionDocumento=descripcionDocumento;
	}
	/**
	 * @param descripcionIdiomaDocumento
	 */
	public void setDescripcionIdiomaDocumento(String descripcionIdiomaDocumento) {
		this.descripcionIdiomaDocumento=descripcionIdiomaDocumento;
	}
	/**
	 * @param destinoGeografico
	 */
	public void setDestinoGeografico(String destinoGeografico) {
		this.destinoGeografico=destinoGeografico;
	}
	/**
	 * @param idiomaExtracto
	 */
	public void setIdiomaExtracto(String idiomaExtracto) {
		this.idiomaExtracto=idiomaExtracto;
	}
	
	/**
	 * @param datasalida
	 */
	public void setdatasalida(String datasalida) {
		this.datasalida=datasalida;
	}
	/**
	 * @param hora
	 */
	public void sethora(String hora) {
		this.hora=hora;
	}
	/**
	 * @param oficina
	 */
	public void setoficina(String oficina) {
		this.oficina=oficina;
	}
	/**
	 * @param oficina
	 */
	public void setoficinafisica(String oficinafisica) {
		this.oficinafisica=oficinafisica;
	}
	/**
	 * @param data
	 */
	public void setdata(String data) {
		this.data=data;
	}
	/**
	 * @param tipo
	 */
	public void settipo(String tipo) {
		this.tipo=tipo.toUpperCase();
	}
	/**
	 * @param idioma
	 */
	public void setidioma(String idioma) {
		this.idioma=idioma;
	}
	/**
	 * @param entidad1
	 */
	public void setentidad1(String entidad1) {
		this.entidad1=entidad1.toUpperCase().trim();
	}
	/**
	 * @param entidad2
	 */
	public void setentidad2(String entidad2) {
		this.entidad2=entidad2.trim();
	}
	/**
	 * @param altres
	 */
	public void setaltres(String altres) {
		this.altres=altres.trim();
	}
	/**
	 * @param balears
	 */
	public void setbalears(String balears) {
		this.balears=balears.trim();
	}
	/**
	 * @param fora
	 */
	public void setfora(String fora) {
		this.fora=fora.trim();
	}
	/**
	 * @param entrada1
	 */
	public void setentrada1(String entrada1) {
		this.entrada1=entrada1.trim();
	}
	/**
	 * @param entrada2
	 */
	public void setentrada2(String entrada2) {
		this.entrada2=entrada2.trim();
	}
	/**
	 * @param destinatari
	 */
	public void setremitent(String remitent) {
		this.remitent=remitent.trim();
	}
	/**
	 * @param idioex
	 */
	public void setidioex(String idioex) {
		this.idioex=idioex;
	}
	/**
	 * @param disquet
	 */
	public void setdisquet(String disquet) {
		this.disquet=disquet;
	}
	/**
	 * @param comentario
	 */
	public void setcomentario(String comentarioEntero) {
		if (comentarioEntero.length()>160) {
			comentarioEntero=comentarioEntero.substring(0, 160);
		}
		this.comentario=comentarioEntero;
	}
	/**
	 * @param motivo
	 */
	public void setMotivo(String motivo) {
		this.motivo=motivo;
	}
	/**
	 * @param entidad1Nuevo
	 */
	public void setEntidad1Nuevo(String entidad1Nuevo) {
		this.entidad1Nuevo=entidad1Nuevo;
	}
	/**
	 * @param entidad1Nuevo
	 */
	public void setEntidad2Nuevo(String entidad2Nuevo) {
		this.entidad2Nuevo=entidad2Nuevo;
	}
	/**
	 * @param altresNuevo
	 */
	public void setAltresNuevo(String altresNuevo) {
		this.altresNuevo=altresNuevo;
	}
	/**
	 * @param comentarioNuevo
	 */
	public void setComentarioNuevo(String comentarioNuevoEntero) {
		if (comentarioNuevoEntero.length()>160) {
			comentarioNuevoEntero=comentarioNuevoEntero.substring(0, 160);
		}
		this.comentarioNuevo=comentarioNuevoEntero;
	}
	
	/**
	 * @param usuario
	 */
	public void fijaUsuario(String usuario) {
		this.usuario=usuario.toUpperCase();
	}
	
	/**
	 * @param dataVisado
	 */
	public void setDataVisado(String dataVisado) {
		this.dataVisado=dataVisado;
	}
	
	
	
	/* Validaciones del registro de entrada*/
	
	/**
	 * @return
	 */
	public boolean validar() {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		validado=false;
		entidadCastellano=null;
		errores.clear();
		try {
			conn=ToolsBD.getConn();
			/* Validamos la fecha de entrada */
			validarFecha(datasalida);
			if (error) {
				errores.put("datasalida","Data de sortida no és lògica");
			}
			/* La fecha de salida sera <= que la fecha del dia */
			Date fechaHoy=new Date();
			fechaTest = dateF.parse(datasalida);
			if (fechaTest.after(fechaHoy)) {
				errores.put("datasalida","Data de sortida posterior a la del dia");
			}
			
			/* Validamos Hora */
			if (hora==null) {
				errores.put("hora","Hora de sortida no és lògica");
			} else {
				try {
					horaF.setLenient(false);
					horaTest=horaF.parse(hora);
				} catch (ParseException ex) {
					errores.put("hora","Hora de sortida no és lògica");
				}
			}
			
			/* Validamos la oficina */
			if (!oficina.equals("")) {
				try {
					String sentenciaSql="SELECT * FROM BZAUTOR WHERE FZHCUSU=? AND FZHCAUT=? AND FZHCAGCO IN " +
					"(SELECT FAACAGCO FROM BAGECOM WHERE FAAFBAJA=0 AND FAACAGCO=?)";
					ps=conn.prepareStatement(sentenciaSql);
					ps.setString(1,usuario.toUpperCase());
					ps.setString(2,"AS");
					ps.setInt(3,Integer.parseInt(oficina));
					rs=ps.executeQuery();
					
					if (rs.next()) {
					} else {
						errores.put("oficina","Oficina: "+oficina+" no vàlida per a l'usuari: "+usuario);
					}
				} catch (Exception e) {
					System.out.println(usuario+": Error en validar l'oficina "+e.getMessage() );
					e.printStackTrace();
					errores.put("oficina","Error en validar l'oficina: "+oficina+" de l'usuari: "+usuario+": "+e.getClass()+"->"+e.getMessage());
				} finally {
					ToolsBD.closeConn(null, ps, rs);
				}
			} else {
				errores.put("oficina","Oficina: "+oficina+" no vàlida per a l'usuari: "+usuario);
			}
			/* Validamos Fecha del documento */
			if (data==null) {
				data=datasalida;
			}
			validarFecha(data);
			if (error) {
				errores.put("data","Data document, no es lògica");
			}
			
			/* Validamos Tipo de documento */
			try {
				String sentenciaSql="SELECT * FROM BZTDOCU WHERE FZICTIPE=? AND FZIFBAJA=0";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setString(1,tipo);
				rs=ps.executeQuery();
				
				if (rs.next()) {
				} else {
					errores.put("tipo","Tipus de document : "+tipo+" no vàlid");
				}
			} catch (Exception e) {
				System.out.println(usuario+": Error en validar el tipus de document"+e.getMessage() );
				e.printStackTrace();
				errores.put("tipo","Error en validar el tipus de document : "+tipo+": "+e.getClass()+"->"+e.getMessage());
			} finally {
					//	Tancam el que pugui estar obert
					ToolsBD.closeConn(null, ps, rs);
			}
			
			/* Validamos el idioma del documento */
			try {
				String sentenciaSql="SELECT * FROM BZIDIOM WHERE FZMCIDI=?";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setString(1,idioma);
				rs=ps.executeQuery();
				
				if (rs.next()) {
				} else {
					errores.put("idioma","Idioma del document : "+idioma+" no vàlid");
				}
			} catch (Exception e) {
				System.out.println(usuario+": Error en validar l'idioma del document"+e.getMessage() );
				e.printStackTrace();
				errores.put("idioma","Error en validar l'idioma del document: "+idioma+": "+e.getClass()+"->"+e.getMessage());
			} finally {
				ToolsBD.closeConn(null, ps, rs);
			}
			
			/* Validamos destinatario */
			if (entidad1.equals("") && altres.equals("")) {
				errores.put("entidad1","Obligatori introduir destinatari");
			} else if(!entidad1.equals("") && !altres.equals("")) {
				errores.put("entidad1","Heu d'introduir: Entitat o Altres");
			} else if (!entidad1.equals("")) {
				if (entidad2.equals("")) {entidad2="0";}
				try {
					String sentenciaSql="SELECT * FROM BZENTID WHERE FZGCENT2=? AND FZGNENTI=? AND FZGFBAJA=0";
					ps=conn.prepareStatement(sentenciaSql);
					ps.setString(1,entidad1);
					ps.setInt(2,Integer.parseInt(entidad2));
					rs=ps.executeQuery();
					
					if (rs.next()) {
						entidadCastellano=rs.getString("FZGCENTI");
					} else {
						errores.put("entidad1","Entitat Destinatària : "+entidad1+"-"+entidad2+" no vàlida");
					}
				} catch (Exception e) {
					System.out.println(usuario+": Error en validar l'entitat destinatària "+e.getClass()+"->"+e.getMessage() );
					errores.put("entidad1","Error en validar l'entitat destinatària: "+entidad1+"-"+entidad2+": "+e.getClass()+"->"+e.getMessage());
					e.printStackTrace();
				} finally {
						//	Tancam el que pugui estar obert
						ToolsBD.closeConn(null, ps, rs);
				}
			}
			
			/* Validamos la procedencia geografica */
			if (balears.equals("") && fora.equals("")) {
				errores.put("balears","Obligatori introduir destí Geogràfic");
			} else if (!balears.equals("") && !fora.equals("")) {
				errores.put("balears","Heu d'introduir Balears o Fora de Balears");
			} else if (!balears.equals("")) {
				try {
					String sentenciaSql="SELECT * FROM BAGRUGE WHERE FABCTAGG=90 AND FABCAGGE=? AND FABFBAJA=0";
					ps=conn.prepareStatement(sentenciaSql);
					ps.setInt(1,Integer.parseInt(balears));
					rs=ps.executeQuery();
					
					if (rs.next()) {
					} else {
						errores.put("balears","Desti geogràfic de Balears : "+balears+" no vàlid");
					}
				} catch (Exception e) {
					System.out.println(usuario+": Error en validar En comprovar el destí geogràfic "+e.getMessage() );
					e.printStackTrace();
					errores.put("balears","Error en validar En comprovar el destí geogràfic: "+balears+": "+e.getClass()+"->"+e.getMessage());
				} finally {
					ToolsBD.closeConn(null, ps, rs);
				}
			}
			
			/* No hay validacion del numero de entrada del documento */
			
			if (entrada1.equals("") && entrada2.equals("")) {
			} else {
				int chk1=0;
				int chk2=0;
				try {
					chk1=Integer.parseInt(entrada1);
					chk2=Integer.parseInt(entrada2);
				} catch (Exception e) {
					errores.put("entrada1","Ambdós camps de numero d'entrada han de ser numèrics");
				}
				if (chk2<1990 || chk2>2050) {
					errores.put("entrada1","Any d'entrada, incorrecte");
				}
			}
			
			/* Validamos el Organismo emisor */
			try {
				String sentenciaSql="SELECT * FROM BZOFIOR WHERE FZFCAGCO=? AND FZFCORGA=?";
				ps=conn.prepareStatement(sentenciaSql);
				ps.setInt(1,Integer.parseInt(oficina));
				ps.setInt(2,Integer.parseInt(remitent));
				rs=ps.executeQuery();
				
				if (rs.next()) {
				} else {
					errores.put("remitent","Organisme emisor: "+remitent+" no vàlid");
				}
			} catch (NumberFormatException e1) {
				errores.put("remitent","Organisme emisor: "+remitent+" codi no numèric");
			} catch (Exception e) {
				System.out.println(usuario+": Error en validar l'organisme emisor "+e.getMessage() );
				e.printStackTrace();
				errores.put("remitent","Error en validar l'organisme emisor: "+remitent+": "+e.getClass()+"->"+e.getMessage());
			} finally {
					// Tancam el que pugui estar obert
					ToolsBD.closeConn(null, ps, rs);
			}
			
			/* Validamos el idioma del extracto */
			
			if (!idioex.equals("1") && !idioex.equals("2")) {
				errores.put("idioex","L'idioma ha de ser 1 ò 2, idioma="+idioex);
			}
			
			
			/* Comprobamos que la ultima fecha introducida en el fichero sea inferior o igual
			 * que la fecha de entrada del registro */
			if (!oficina.equals("")  && !actualizacion) {
				try {
					String sentenciaSql="SELECT MAX(FZSFENTR) FROM BZSALIDA WHERE FZSANOEN=? AND FZSCAGCO=?";
					fechaTest = dateF.parse(datasalida);
					Calendar cal=Calendar.getInstance();
					cal.setTime(fechaTest);
					DateFormat date1=new SimpleDateFormat("yyyyMMdd");
					
					ps=conn.prepareStatement(sentenciaSql);
					ps.setInt(1,cal.get(Calendar.YEAR));
					ps.setInt(2,Integer.parseInt(oficina));
					rs=ps.executeQuery();
					int ultimaFecha=0;
					
					if (rs.next()) {ultimaFecha=rs.getInt(1);}
					if (ultimaFecha>Integer.parseInt(date1.format(fechaTest))) {
						errores.put("datasalida","Data inferior a la darrera sortida");
					}
				} catch (Exception e) {
					System.out.println(usuario+": Error inesperat en la data de sortida "+e.getMessage() );
					e.printStackTrace();
					errores.put("datasalida","Error inesperat en la data de sortida: "+remitent+": "+e.getClass()+"->"+e.getMessage());
				} finally {
					ToolsBD.closeConn(null, ps, rs);
				}
			}
			
			/* Solamente se podra introducir numero de correo para la oficina 32 
			 if (!oficina.equals("32") && !correo.trim().equals("")) {
			 errores.put("correo","El valor número de correu només introduïble per Oficina 32 (BOIB)");
			 }
			 */
			/* Validamos el numero de disquete */
			try {
				if (!disquet.equals("")) {
					int chk1=Integer.parseInt(disquet);
				}
			} catch (Exception e) {
				errores.put("disquet","Numero de disquet no vàlid");
			}
			
			/* Validamos el extracto del documento */
			if (comentario.equals("")) {
				errores.put("comentario","Heu d'introduir un extracte del document ");
			}
			
			/* Validaciones solamente validas para el proceso de actualizacion con modificacion de
			 * campos criticos */
			if (actualizacion && !motivo.trim().equals("")) {
				if (comentarioNuevo.equals("")) {
					errores.put("comentario","Heu d'introduir un extracte del document ");
				}
				/* Validamos remitente */
				if (entidad1Nuevo.equals("") && altresNuevo.equals("")) {
					errores.put("entidad1","Obligatori introduir remitent");
				} else if(!entidad1Nuevo.equals("") && !altresNuevo.equals("")) {
					errores.put("entidad1","Heu d'introduir: Entitat o Altres");
				} else if (!entidad1Nuevo.equals("")) {
					if (entidad2Nuevo.equals("")) {entidad2Nuevo="0";}
					try {
						String sentenciaSql="SELECT * FROM BZENTID WHERE FZGCENT2=? AND FZGNENTI=? AND FZGFBAJA=0";
						ps=conn.prepareStatement(sentenciaSql);
						ps.setString(1,entidad1Nuevo);
						ps.setInt(2,Integer.parseInt(entidad2Nuevo));
						rs=ps.executeQuery();
						
						if (rs.next()) {
						} else {
							errores.put("entidad1","Entitat Destinatària : "+entidad1+"-"+entidad2+" no vàlid");
						}
					} catch (Exception e) {
						System.out.println(usuario+": Error inesperat en l'entitat destinatària "+e.getMessage() );
						e.printStackTrace();
						errores.put("entidad1","Error inesperat en l'entitat destinatària : "+entidad1+"-"+entidad2+": "+e.getClass()+"->"+e.getMessage());
					} finally {
							//Tancam el que pugui estar obert
							ToolsBD.closeConn(null, ps, rs);
					}
				}
			}
			
			/* Fin de validaciones de campos */
		} catch (Exception e) {
			e.printStackTrace();
			validado=false;
		} finally {
			ToolsBD.closeConn(conn, null, null);
		}
		if (errores.size()==0) {
			validado=true;
		} else {
			validado=false;
		}
		return validado;
	}
	
	
	/* Grabamos registro de salida si las validaciones son correctas */
	
	/**
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public void grabar() throws SQLException, ClassNotFoundException, Exception {
		Connection conn = null;
		PreparedStatement ms = null;
		int fzsanoe;
		int fzsfent;
		int fzsfsis;
		int horamili;
		int fzscagc;
		int off_codi;
		
		if (!validado) {
			validado=validar();
		}
		if (!validado) {
			throw new Exception("No s'ha realitzat la validació de les dades del registre ");
		}
		
		
		registroSalidaGrabado=false;
		try {
			
			/* Descomponemos el año de la data de entrada, FZAANOEN y preparamos campo
			 FZSFENT en formato aaaammdd */
			
			String campo;
			
			fechaTest = dateF.parse(datasalida);
			Calendar cal=Calendar.getInstance();
			cal.setTime(fechaTest);
			DateFormat date1=new SimpleDateFormat("yyyyMMdd");
			
			fzsanoe=cal.get(Calendar.YEAR);
			anoSalida=String.valueOf(fzsanoe);
			
			fzsfent=Integer.parseInt(date1.format(fechaTest));
			
			/* Recuperamos numero de entrada */
			conn=ToolsBD.getConn();
			conn.setAutoCommit(false);
			int fzsnume=ToolsBD.RecogerNumeroSalida(conn, fzsanoe, oficina, errores);
			setNumeroSalida(Integer.toString(fzsnume));
			
			/* Oficina, fzscagc */
			fzscagc=Integer.parseInt(oficina);
			off_codi=Integer.parseInt(oficinafisica);
			
			/* Fecha documento en un campo en formato aaaammdd, fzsfdoc */
			fechaTest = dateF.parse(data);
			cal.setTime(fechaTest);
			int fzsfdoc=Integer.parseInt(date1.format(fechaTest));
			
			/* Si el idioma del extracte es 1=castellano entonces el extracte lo guardamos
			 en el campo FZACONE, si es 2=catalan lo guardamos en FZACONE2 */
			String fzscone, fzscone2;
			if (idioex.equals("1")) {
				fzscone=comentario;
				fzscone2="";
			} else {
				fzscone="";
				fzscone2=comentario;
			}
			/* Preparamos los campos de Procedencia geografica, tipo de agrupacion geografica
			 y codigo de agrupacion geografica */
			String fzsproce;
			int fzsctagg, fzscagge;
			if (fora.equals("")) {
				fzsctagg=90;
				fzscagge=Integer.parseInt(balears);
				fzsproce="";
			} else {
				fzsproce=fora;
				fzsctagg=0;
				fzscagge=0;
			}
			/* Fecha de actualizacion a ceros */
			int ceros=0;
			
			/* Codigo de Organismo */
			int fzscorg=Integer.parseInt(remitent);
			
			/* Numero de entidad, fzsnent */
			int fzsnent;
			String fzscent;
			if (altres.equals("")) {
				altres="";
				fzsnent=Integer.parseInt(entidad2);
				fzscent=entidadCastellano;
			} else {
				fzsnent=0;
				fzscent="";
			}
			
			/* Idioma del extracto, fzscidi */
			int fzscidi=Integer.parseInt(idioex);
			
			/* Hora del documento, fzshora mmss */
			horaTest=horaF.parse(hora);
			cal.setTime(horaTest);
			DateFormat hhmm=new SimpleDateFormat("HHmm");
			int fzshora=Integer.parseInt(hhmm.format(horaTest));
			
			/* Numero localizador y año localizador, fzsnloc y fzsaloc */
			if (entrada1.equals("")) {entrada1="0";}
			if (entrada2.equals("")) {entrada2="0";}
			int fzsnloc=Integer.parseInt(entrada1);
			int fzsaloc=Integer.parseInt(entrada2);
			
			/* Numero de disquette, fzsndis */
			if (disquet.equals("")) {disquet="0";}
			int fzsndis=Integer.parseInt(disquet);
			/* Actualizamos el numero de disquete */
			if (fzsndis>0){ToolsBD.actualizaDisqueteSalida(conn, fzsndis, oficina, anoSalida, errores);}
			
			/* Recuperamos la fecha y la hora del sistema, fzsfsis(aaaammdd) y
			 fzshsis (hhMMssmm) */
			Date fechaSystem=new Date();
			DateFormat aaaammdd=new SimpleDateFormat("yyyyMMdd");
			fzsfsis=Integer.parseInt(aaaammdd.format(fechaSystem));
			
			DateFormat hhmmss=new SimpleDateFormat("HHmmss");
			DateFormat sss=new SimpleDateFormat("S");
			String ss=sss.format(fechaSystem);
			if (ss.length()>2) {
				ss=ss.substring(0,2);
			}
			int fzshsis=Integer.parseInt(hhmmss.format(fechaSystem)+ss);
			
			/* Grabamos numero de correo si tuviera */
			if (correo!=null && !correo.equals("")) {
				String insertBZNCORR="INSERT INTO BZNCORR (FZPCENSA, FZPCAGCO, FZPANOEN, FZPNUMEN, FZPNCORR)" +
				"VALUES (?,?,?,?,?)";
				ms=conn.prepareStatement(insertBZNCORR);
				ms.setString(1, "S");
				ms.setInt(2,fzscagc);
				ms.setInt(3,fzsanoe);
				ms.setInt(4,fzsnume);
				ms.setString(5, correo);
				ms.execute();
				ms.close();
			}
			
            String insertOfifis="INSERT INTO BZSALOFF (FOSANOEN, FOSNUMEN, FOSCAGCO, OFS_CODI)" +
            "VALUES (?,?,?,?)";
            ms=conn.prepareStatement(insertOfifis);
            ms.setInt(1,fzsanoe);
            ms.setInt(2,fzsnume);
            ms.setInt(3,fzscagc);
    		ms.setInt(4,off_codi);
    		ms.execute();
    		ms.close();

			/* Ejecutamos sentencias SQL */
			ms=conn.prepareStatement(SENTENCIA);
			
			ms.setInt(1,fzsanoe);
			ms.setInt(2,fzsnume);
			ms.setInt(3,fzscagc);
			ms.setInt(4,fzsfdoc);
			ms.setString(5,(altres.length()>30) ? altres.substring(0,30) : altres); // 30 pos.
			ms.setString(6,(fzscone.length()>160) ? fzscone.substring(0,160) : fzscone); // 160 pos.
			ms.setString(7,(tipo.length()>2) ? tipo.substring(0,2) : tipo); // 2 pos.
			ms.setString(8,"N");
			ms.setString(9,"");
			ms.setString(10,(fzsproce.length()>25) ? fzsproce.substring(0,25) : fzsproce); // 25 pos.
			ms.setInt(11,fzsfent);
			ms.setInt(12,fzsctagg);
			ms.setInt(13,fzscagge);
			ms.setInt(14,fzscorg);
			ms.setInt(15,ceros);
			ms.setString(16,(fzscent.length()>7) ? fzscent.substring(0,7) : fzscent); // 7 pos.
			ms.setInt(17,fzsnent);
			ms.setInt(18,fzshora);
			ms.setInt(19,fzscidi);
			ms.setString(20,(fzscone2.length()>160) ? fzscone2.substring(0,160) : fzscone2); // 160 pos.
			ms.setInt(21,fzsnloc);
			ms.setInt(22,fzsaloc);
			ms.setInt(23,fzsndis);
			ms.setInt(24,fzsfsis);
			ms.setInt(25,fzshsis);
			ms.setString(26,(usuario.toUpperCase().length()>10) ? usuario.toUpperCase().substring(0,10) : usuario.toUpperCase()); // 10 pos.
			ms.setString(27,idioma);
			
			registroSalidaGrabado=ms.execute();
			registroSalidaGrabado=true;
            

            // desacoplamiento cobol
            String remitente="";
            if (!altres.trim().equals("")) {
                remitente=altres;
            } else {
                javax.naming.InitialContext contexto = new javax.naming.InitialContext();
                Object ref = contexto.lookup("es.caib.regweb.ValoresHome");
                ValoresHome home=(ValoresHome)javax.rmi.PortableRemoteObject.narrow(ref, ValoresHome.class);
                Valores valor=home.create();
                remitente=valor.recuperaRemitenteCastellano(fzscent, fzsnent+"");
                valor.remove();
            }
            try {
                Class t = Class.forName("es.caib.regweb.module.PluginHook");
                
                Class [] partypes = {
                    String.class , Integer.class, Integer.class, Integer.class, Integer.class, String.class,
                 	String.class, String.class, Integer.class, Integer.class, String.class, Integer.class, String.class  
                };
                
                Object [] params = {
                    "A", new Integer(fzsanoe), new Integer(fzsnume), new Integer(fzscagc), new Integer(fzsfdoc), remitente, 
                    comentario, tipo, new Integer(fzsfent), new Integer(fzscagge), fzsproce, new Integer(fzscorg), idioma
                };
                
                java.lang.reflect.Method metodo = t.getMethod("salida", partypes);
                metodo.invoke(null, params);
                
                //t.salida("A", fzsanoe, fzsnume, fzscagc, fzsfdoc, remitente, comentario, tipo, fzsfent, fzscagge, fzsproce, fzscorg, idioma);
                
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
            

			//ms.close();
			conn.commit();
			/* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
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
            horamili=Integer.parseInt(hhmmss.format(fechaSystem)+Stringsss);
			
			
		} catch (Exception ex) {
			System.out.println(usuario+": Error inesperat: "+ex.getMessage() );
			ex.printStackTrace();
			registroSalidaGrabado=false;
			errores.put("","Error inesperat, no s'ha desat el registre"+": "+ex.getClass()+"->"+ex.getMessage());
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException sqle) {
				throw new RemoteException(usuario+": S'ha produït un error i no s'han pogut tornar enrere els canvis efectuats", sqle);
			}
			throw new RemoteException("Error inesperat, no s'ha desat el registre", ex);
		} finally {
			ToolsBD.closeConn(conn, ms, null);
		}
		
		try { 
			logLopdBZSALIDA("INSERT", (usuario.toUpperCase().length()>10) ? usuario.toUpperCase().substring(0,10) : usuario.toUpperCase(), fzsfsis, horamili, fzsnume, fzsanoe, fzscagc);
		} catch (Exception e){
			System.out.println(usuario+": Error inesperat al guardar el LogLopd."+e.getMessage() );
			e.printStackTrace();
		}
	}
	
	/**
	 * @param fecha
	 */
	private void validarFecha(String fecha) {
		try {
			dateF.setLenient(false);
			fechaTest = dateF.parse(fecha);
			error=false;
		} catch (Exception ex) {
			error=true;
		}
	}
	
	/** Lee un registro de entrada del fichero BZSALIDA, para ello le
	 * deberemos pasar el usuario, el codigo de oficina, el numero de registro de
	 * salida y el año de salida.
	 * @param usuario String
	 * @param oficina String
	 * @param numeroEntrada String
	 * @param anoEntrada String
	 * @return Devuelve un objeto de la clase RegistroEntrada
	 */
	public void leer() {
		Connection conn = null;
		ResultSet rs = null;
		ResultSet rsHist = null;
		PreparedStatement ps = null;
		PreparedStatement psHist = null;
		DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
		DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date fechaDocumento=null;
		try {
			conn=ToolsBD.getConn();
			String sentenciaSql="SELECT * FROM BZSALIDA LEFT JOIN BAGECOM ON FAACAGCO=FZSCAGCO " +
			"LEFT JOIN BZENTID ON FZSCENTI=FZGCENTI AND FZGNENTI=FZSNENTI " +
			"LEFT JOIN BORGANI ON FAXCORGA=FZSCORGA " +
			"LEFT JOIN BZTDOCU ON FZICTIPE=FZSCTIPE " +
			"LEFT JOIN BZIDIOM ON FZSCIDI=FZMCIDI " +
			"LEFT JOIN BZSALOFF ON FZSANOEN=FOSANOEN AND FZSNUMEN=FOSNUMEN AND FZSCAGCO=FOSCAGCO " +
			"LEFT JOIN BZOFIFIS ON FZSCAGCO=FZOCAGCO AND OFF_CODI=OFS_CODI " +
			"LEFT JOIN BAGRUGE ON FZSCTAGG=FABCTAGG AND FZSCAGGE=FABCAGGE " +
			"LEFT JOIN BZAUTOR ON FZHCUSU=? AND FZHCAGCO=FZSCAGCO " +
			"LEFT JOIN BZNCORR ON FZPCENSA='S' AND FZPCAGCO=FZSCAGCO AND FZPANOEN=FZSANOEN AND FZPNUMEN=FZSNUMEN " +
			"WHERE FZSANOEN=? AND FZSNUMEN=? AND FZSCAGCO=? AND FZHCAUT=?";
			ps=conn.prepareStatement(sentenciaSql);
			ps.setString(1,usuario.toUpperCase());
			ps.setString(2,anoSalida);
			ps.setString(3,numeroSalida);
			ps.setString(4,oficina);
			ps.setString(5,"CS");
			rs=ps.executeQuery();
			if (rs.next()) {
				/* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
            	Date fechaSystem=new Date();
            	DateFormat hhmmss=new SimpleDateFormat("HHmmss");
                DateFormat sss=new SimpleDateFormat("S");
                String ss=sss.format(fechaSystem);
                DateFormat aaaammdd=new SimpleDateFormat("yyyyMMdd");
                int fzsfsis=Integer.parseInt(aaaammdd.format(fechaSystem));
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
				logLopdBZSALIDA("SELECT", (usuario.toUpperCase().length()>10) ? usuario.toUpperCase().substring(0,10) : usuario.toUpperCase()
						, fzsfsis, horamili, rs.getInt("FZSNUMEN"), rs.getInt("FZSANOEN"), rs.getInt("FZSCAGCO"));
				
				leidos=true;
				anoSalida=String.valueOf(rs.getInt("FZSANOEN"));
				numeroSalida=String.valueOf(rs.getInt("FZSNUMEN"));
				oficina=String.valueOf(rs.getInt("FZSCAGCO"));
				oficinafisica=String.valueOf(rs.getInt("OFF_CODI"));
				
				/* Aquí hem d'anar a la taula d'oficines fisiques. */
				String textoOficinaFisica=null;
				textoOficinaFisica=rs.getString("OFF_NOM");
				
				if (rs.getString("OFF_CODI")==null) {
					oficinafisica="0";

	                String sentenciaSqlOfiFis="SELECT * FROM BZOFIFIS WHERE FZOCAGCO=? AND OFF_CODI=0 ";
		            psHist=conn.prepareStatement(sentenciaSqlOfiFis);
		            psHist.setString(1,oficina);
		            rsHist=psHist.executeQuery();
		            if (rsHist.next()) {
						textoOficinaFisica=rsHist.getString("OFF_NOM");
		            }
		    		if (rsHist != null)
		    			rsHist.close();
		    		if (psHist != null)
		    			psHist.close();

				}
				if (textoOficinaFisica==null) {
					textoOficinaFisica=" ";
				}

				descripcionOficinaFisica=textoOficinaFisica;

				String fechaSalid=String.valueOf(rs.getInt("FZSFENTR"));
				try {
					fechaDocumento=yyyymmdd.parse(fechaSalid);
					datasalida=(ddmmyyyy.format(fechaDocumento));
				} catch (Exception e) {
					datasalida=fechaSalid;
				}
				
				/* Aquí hem d'anar a l'històric d'oficines. */
				String textoOficina=null;
				String sentenciaSqlHistOfi="SELECT * FROM BHAGECO01 WHERE FHACAGCO=? AND FHAFALTA<=? " +
				"AND ( (FHAFBAJA>= ? AND FHAFBAJA !=0) OR FHAFBAJA = 0)";
				psHist=conn.prepareStatement(sentenciaSqlHistOfi);
				psHist.setString(1,oficina);
				psHist.setString(2,fechaSalid);
				psHist.setString(3,fechaSalid);
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
				
				descripcionOficina=textoOficina;
				String fechaDocu=String.valueOf(rs.getInt("FZSFDOCU"));
				try {
					fechaDocumento=yyyymmdd.parse(fechaDocu);
					data=(ddmmyyyy.format(fechaDocumento));
				} catch (Exception e) {
					data=fechaDocu;
				}
				
				String fechaVisado=String.valueOf(rs.getInt("FZSFACTU"));
				try {
					fechaDocumento=yyyymmdd.parse(fechaVisado);
					dataVisado=(ddmmyyyy.format(fechaDocumento));
				} catch (Exception e) {
					dataVisado=fechaVisado;
				}
				
				
				hora=String.valueOf(rs.getInt("FZSHORA"));
				if (rs.getString("FZGCENTI")==null) {
					descripcionDestinatario=rs.getString("FZSREMIT");
				} else {
					descripcionDestinatario=rs.getString("FZGDENT2");
				}
				registroAnulado=rs.getString("FZSENULA");
				
				if ( rs.getString("FZSCTIPE")==null)
					tipo="";
				else 
					tipo=rs.getString("FZSCTIPE");
				
				idioma=rs.getString("FZSCIDI");
				idioex=rs.getString("FZSCIDIO");
				entidad1=ToolsBD.convierteEntidad(rs.getString("FZSCENTI"),conn);
				entidad1Grabada=rs.getString("FZSCENTI");
				entidad2=rs.getString("FZSNENTI");
				altres=rs.getString("FZSREMIT");
				balears=rs.getString("FZSCAGGE");
				fora=rs.getString("FZSPROCE");
				
				remitent=String.valueOf(rs.getInt("FZSCORGA"));
				
				/* Aquí hem d'anar a l'històric d'organismes  */
				String sentenciaSqlHistOrga="SELECT * FROM BHORGAN01 WHERE FHXCORGA=? AND FHXFALTA<=? " +
				"AND ( (FHXFBAJA>= ? AND FHXFBAJA !=0) OR FHXFBAJA = 0)";
				psHist=conn.prepareStatement(sentenciaSqlHistOrga);
				psHist.setString(1,remitent);
				psHist.setString(2,fechaSalid);
				psHist.setString(3,fechaSalid);
				rsHist=psHist.executeQuery();
				if (rsHist.next()) {
					/* Hem trobat un històric de l'oficina sol·licitada, hem de mostrar-ne el descriptiu. */
					descripcionOrganismoRemitente=rsHist.getString("FHXDORGT");
				} else {
					descripcionOrganismoRemitente=(rs.getString("FAXDORGT"));
					if (descripcionOrganismoRemitente==null) {
						descripcionOrganismoRemitente=" ";
					}
				}
				//  Tancam el preparedstatement i resultset de l'històric
				if (rsHist != null)
					rsHist.close();
				if (psHist != null)
					psHist.close();
				
				
				if (rs.getString("FZIDTIPE")==null)
					descripcionDocumento="";
				else
					descripcionDocumento=rs.getString("FZIDTIPE");
				
				descripcionIdiomaDocumento=rs.getString("FZMDIDI");
				
				if (rs.getString("FABDAGGE")==null) {
					destinoGeografico=rs.getString("FZSPROCE");
				} else {
					destinoGeografico=rs.getString("FABDAGGE");
				}
				if (rs.getString("FZSCIDIO").equals("1")) {
					idiomaExtracto="CASTELLÀ";
					comentario=rs.getString("FZSCONEN");
				} else {
					idiomaExtracto="CATALÀ";
					comentario=rs.getString("FZSCONE2");
				}
				if (rs.getString("FZSNDIS").equals("0")) {
					disquet="";
				} else {
					disquet=rs.getString("FZSNDIS");
				}
				entrada1=String.valueOf(rs.getInt("FZSNLOC"));
				entrada2=String.valueOf(rs.getInt("FZSALOC"));
				correo=rs.getString("FZPNCORR");
			}
		} catch (Exception e) {
			System.out.println("LEER: Error inesperat: "+e.getMessage() );
			e.printStackTrace();
		} finally {
			ToolsBD.closeConn(conn, ps, rs);
		}
	}
	
	public void actualizar() throws SQLException, ClassNotFoundException, Exception {
		Connection conn = null;
		PreparedStatement ms = null;
		if (!validado) {
			validado=validar();
		}
		if (!validado) {
			throw new Exception("No s'ha realitzat la validació de les dades del registre ");
		}
		
		registroActualizado=false;
		try {
			/* Descomponemos el año de la data de entrada, FZAANOEN y preparamos campo
			 FZAFENT en formato aaaammdd */
			int fzaanoe;
			String campo;
			
			fechaTest = dateF.parse(datasalida);
			Calendar cal=Calendar.getInstance();
			cal.setTime(fechaTest);
			DateFormat date1=new SimpleDateFormat("yyyyMMdd");
			
			fzaanoe=Integer.parseInt(anoSalida);
			
			int fzafent=Integer.parseInt(date1.format(fechaTest));
			
			/* Recuperamos numero de entrada */
			conn=ToolsBD.getConn();
			conn.setAutoCommit(false);
			
			int fzanume=Integer.parseInt(numeroSalida);
			
			/* Oficina, fzacagc */
			int fzacagc=Integer.parseInt(oficina);
            int off_codi=0;
            try {
            	off_codi=Integer.parseInt(oficinafisica);
            } catch (Exception e) {}
			
			/* Fecha documento en un campo en formato aaaammdd, fzafdoc */
			fechaTest = dateF.parse(data);
			cal.setTime(fechaTest);
			int fzafdoc=Integer.parseInt(date1.format(fechaTest));
			
			/* Si el idioma del extracte es 1=castellano entonces el extracte lo guardamos
			 en el campo FZACONE, si es 2=catalan lo guardamos en FZACONE2 */
			String fzacone, fzacone2;
			if (idioex.equals("1")) {
				fzacone=comentario;
				fzacone2="";
			} else {
				fzacone="";
				fzacone2=comentario;
			}
			
			/* Preparamos los campos de Procedencia geografica, tipo de agrupacion geografica
			 y codigo de agrupacion geografica */
			String fzaproce;
			int fzactagg, fzacagge;
			if (fora.equals("")) {
				fzactagg=90;
				fzacagge=Integer.parseInt(balears);
				fzaproce="";
			} else {
				fzaproce=fora;
				fzactagg=0;
				fzacagge=0;
			}
			/* Fecha de actualizacion a ceros */
			int ceros=0;
			
			/* Codigo de Organismo */
			int fzacorg=Integer.parseInt(remitent);
			
			/* Numero de entidad, fzanent */
			int fzanent;
			String fzacent;
			if (altres.equals("")) {
				altres="";
				fzanent=Integer.parseInt(entidad2);
				fzacent=entidadCastellano;
			} else {
				fzanent=0;
				fzacent="";
			}
			
			/* Idioma del extracto, fzacidi */
			int fzacidi=Integer.parseInt(idioex);
			
			/* Hora del documento, fzahora mmss */
			horaTest=horaF.parse(hora);
			cal.setTime(horaTest);
			DateFormat hhmm=new SimpleDateFormat("HHmm");
			int fzahora=Integer.parseInt(hhmm.format(horaTest));
			
			/* Numero localizador y año localizador, fzanloc y fzaaloc */
			if (entrada1.equals("")) {entrada1="0";}
			if (entrada2.equals("")) {entrada2="0";}
			int fzanloc=Integer.parseInt(entrada1);
			int fzaaloc=Integer.parseInt(entrada2);
			
			/* Numero de disquette, fzandis */
			if (disquet.equals("")) {disquet="0";}
			int fzandis=Integer.parseInt(disquet);
			/* Actualizamos el numero de disquete */
			if (fzandis>0){ToolsBD.actualizaDisqueteEntrada(conn, fzandis, oficina, anoSalida, errores);}
			
			/* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
			Date fechaSystem=new Date();
			DateFormat aaaammdd=new SimpleDateFormat("yyyyMMdd");
			int fzafsis=Integer.parseInt(aaaammdd.format(fechaSystem));
			
			DateFormat hhmmss=new SimpleDateFormat("HHmmss");
			DateFormat sss=new SimpleDateFormat("S");
			String ss=sss.format(fechaSystem);
			if (ss.length()>2) {
				ss=ss.substring(0,2);
			}
			int fzahsis=Integer.parseInt(hhmmss.format(fechaSystem)+ss);
			
			/* Grabamos numero de correo si tuviera */
			if (correo!=null) {
				String insertBZNCORR="INSERT INTO BZNCORR (FZPCENSA, FZPCAGCO, FZPANOEN, FZPNUMEN, FZPNCORR)" +
				"VALUES (?,?,?,?,?)";
				String updateBZNCORR="UPDATE BZNCORR SET FZPNCORR=? WHERE FZPCENSA=? AND FZPCAGCO=? AND FZPANOEN=? AND FZPNUMEN=?";
				String deleteBZNCORR="DELETE FROM BZNCORR WHERE FZPCENSA=? AND FZPCAGCO=? AND FZPANOEN=? AND FZPNUMEN=?";
				int actualizados=0;
				
				if (!correo.trim().equals("")) {
					/* Actualizamos registro */
					ms=conn.prepareStatement(updateBZNCORR);
					ms.setString(1, correo);
					ms.setString(2, "S");
					ms.setInt(3,fzacagc);
					ms.setInt(4,fzaanoe); 
					ms.setInt(5,fzanume);
					actualizados=ms.executeUpdate();
					ms.close();
					if (actualizados==0) {
						/* Generamos alta con el numero de correo */
						ms=conn.prepareStatement(insertBZNCORR);
						ms.setString(1, "S");
						ms.setInt(2,fzacagc);
						ms.setInt(3,fzaanoe);
						ms.setInt(4,fzanume);
						ms.setString(5, correo);
						ms.execute();
						ms.close();
					}
				} else {
					ms=conn.prepareStatement(deleteBZNCORR);
					ms.setString(1, "S");
					ms.setInt(2,fzacagc);
					ms.setInt(3,fzaanoe);
					ms.setInt(4,fzanume);
					ms.execute();
				}
			}
			
			
            String deleteOfifis="DELETE FROM BZSALOFF WHERE FOSANOEN=? AND FOSNUMEN=? AND FOSCAGCO=?";
            ms=conn.prepareStatement(deleteOfifis);
            ms.setInt(1,fzaanoe);
            ms.setInt(2,fzanume);
            ms.setInt(3,fzacagc);
    		ms.execute();
    		ms.close();

    		String insertOfifis="INSERT INTO BZSALOFF (FOSANOEN, FOSNUMEN, FOSCAGCO, OFS_CODI)" +
            "VALUES (?,?,?,?)";
            ms=conn.prepareStatement(insertOfifis);
            ms.setInt(1,fzaanoe);
            ms.setInt(2,fzanume);
            ms.setInt(3,fzacagc);
    		ms.setInt(4,off_codi);
    		ms.execute();
    		ms.close();

			/* Ejecutamos sentencias SQL */
			ms=conn.prepareStatement("UPDATE BZSALIDA SET FZSFDOCU=?, FZSREMIT=?, FZSCONEN=?, FZSCTIPE=?, " +
					"FZSCEDIE=?, FZSENULA=?, FZSPROCE=?, FZSFENTR=?, FZSCTAGG=?, FZSCAGGE=?, FZSCORGA=?, " +
					"FZSCENTI=?, FZSNENTI=?, FZSHORA=?, FZSCIDIO=?, FZSCONE2=?, FZSNLOC=?, FZSALOC=?, FZSNDIS=?, " +
			"FZSCUSU=?, FZSCIDI=? WHERE FZSANOEN=? AND FZSNUMEN=? AND FZSCAGCO=? ");
			ms.setInt(1,fzafdoc);
			ms.setString(2,(altres.length()>30) ? altres.substring(0,30) : altres); // 30 pos.
			ms.setString(3,(fzacone.length()>160) ? fzacone.substring(0,160) : fzacone); // 160 pos.
			ms.setString(4,(tipo.length()>2) ? tipo.substring(0,1) : tipo);  // 2 pos.
			ms.setString(5,"N");
			ms.setString(6,(registroAnulado.length()>1) ? registroAnulado.substring(0,1) : registroAnulado);
			ms.setString(7,(fzaproce.length()>25) ? fzaproce.substring(0,25) : fzaproce); // 25 pos.
			ms.setInt(8,fzafent);
			ms.setInt(9,fzactagg);
			ms.setInt(10,fzacagge);
			ms.setInt(11,fzacorg);
			ms.setString(12,(fzacent.length()>7) ? fzacent.substring(0,8) : fzacent); // 7 pos.
			ms.setInt(13,fzanent);
			ms.setInt(14,fzahora);
			ms.setInt(15,fzacidi);
			ms.setString(16,(fzacone2.length()>160) ? fzacone2.substring(0,160) : fzacone2); // 160 pos.
			ms.setInt(17,fzanloc);
			ms.setInt(18,fzaaloc);
			ms.setInt(19,fzandis);
			ms.setString(20,(usuario.toUpperCase().length()>10) ? usuario.toUpperCase().substring(0,10) : usuario.toUpperCase()); // 10 pos.
			ms.setString(21,idioma);
			// Clave del fichero
			ms.setInt(22,fzaanoe);
			ms.setInt(23,fzanume);
			ms.setInt(24,fzacagc);
			
			// Si hay motivo, generamos objeto Modificacion
			boolean modificado=false;
			if (!motivo.equals("")) {
				javax.naming.InitialContext contexto = new javax.naming.InitialContext();
				Object ref = contexto.lookup("es.caib.regweb.RegistroModificadoSalidaHome");
				RegistroModificadoSalidaHome home=(RegistroModificadoSalidaHome)javax.rmi.PortableRemoteObject.narrow(ref, RegistroModificadoSalidaHome.class);
				RegistroModificadoSalida registroModificado=home.create();
				registroModificado.setAnoSalida(fzaanoe);
				registroModificado.setOficina(fzacagc);
				if (!entidad1Nuevo.trim().equals("")) {
					if (entidad2Nuevo.equals("")) {entidad2Nuevo="0";}
				}
				int fzanentNuevo;
				String fzacentNuevo;
				if (altresNuevo.trim().equals("")) {
					altresNuevo="";
					fzanentNuevo=Integer.parseInt(entidad2Nuevo);
					fzacentNuevo=convierteEntidadCastellano(entidad1Nuevo, conn);
				} else {
					fzanentNuevo=0;
					fzacentNuevo="";
				}
				if (!fzacentNuevo.equals(fzacent) || fzanentNuevo!=fzanent) {
					registroModificado.setEntidad2(fzanentNuevo);
					registroModificado.setEntidad1(fzacentNuevo);
				} else {
					registroModificado.setEntidad2(0);
					registroModificado.setEntidad1("");
				}
				if (!comentarioNuevo.trim().equals(comentario.trim())) {
					registroModificado.setExtracto(comentarioNuevo);
				} else {
					registroModificado.setExtracto("");
				}
				registroModificado.setUsuarioModificacion(usuario.toUpperCase());
				registroModificado.setNumeroRegistro(fzanume);
				if (altresNuevo.equals(altres)) {
					registroModificado.setRemitente("");
				} else {
					registroModificado.setRemitente(altresNuevo);
				}
				registroModificado.setMotivo(motivo);
				modificado=registroModificado.generarModificacion(conn);
				registroModificado.remove();
			}
			if ((modificado && !motivo.equals("")) || motivo.equals(""))  {
				int afectados=ms.executeUpdate();
				if (afectados>0){
					registroActualizado=true;
				} else {
					registroActualizado=false;
				}

                // desacoplamiento cobol
                String remitente="";
                if (!altres.trim().equals("")) {
                    remitente=altres;
                } else {
                    javax.naming.InitialContext contexto = new javax.naming.InitialContext();
                    Object ref = contexto.lookup("es.caib.regweb.ValoresHome");
                    ValoresHome home=(ValoresHome)javax.rmi.PortableRemoteObject.narrow(ref, ValoresHome.class);
                    Valores valor=home.create();
                    remitente=valor.recuperaRemitenteCastellano(fzacent, fzanent+"");
                    valor.remove();
                }
                try {
                    Class t = Class.forName("es.caib.regweb.module.PluginHook");

                    Class [] partypes = {
                        String.class , Integer.class, Integer.class, Integer.class, Integer.class, String.class,
                     	String.class, String.class, Integer.class, Integer.class, String.class, Integer.class, String.class  
                    };

                    Object [] params = {
                        "M", new Integer(fzaanoe), new Integer(fzanume), new Integer(fzacagc), new Integer(fzafdoc), remitente, 
                        comentario, tipo, new Integer(fzafent), new Integer(fzacagge), fzaproce, new Integer(fzacorg), idioma
                    };

                    java.lang.reflect.Method metodo = t.getMethod("salida", partypes);
                    metodo.invoke(null, params);

                    //t.salida("M", fzaanoe, fzanume, fzacagc, fzafdoc, remitente, comentario, tipo, fzafent, fzacagge, fzaproce, fzacorg, idioma);

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
				
				
				/* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
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
				logLopdBZSALIDA("UPDATE", (usuario.toUpperCase().length()>10) ? usuario.toUpperCase().substring(0,10) : usuario.toUpperCase()
						, fzahsis, horamili, fzanume, fzaanoe, fzacagc);
				
				//ms.close();
				conn.commit();
				
			} else {
				registroActualizado=false;
				errores.put("","Error inesperat, no s'ha modificat el registre");
				throw new RemoteException("Error inesperat, no s'ha modifcat el registre");
			}
		} catch (Exception ex) {
			System.out.println("Error inesperat "+ex.getMessage() );
			ex.printStackTrace();
			registroActualizado=false;
			errores.put("","Error inesperat, no s'ha modificat el registre"+": "+ex.getClass()+"->"+ex.getMessage());
			try {
				if (conn!=null)
					conn.rollback();
			} catch (SQLException sqle) {
				throw new RemoteException("S'ha produ\357t un error i no s'han pogut tornar enrere els canvis efectuats", sqle);
			}
			throw new RemoteException("Error inesperat, no s'ha modifcat el registre", ex);
		} finally {
			ToolsBD.closeConn(conn, ms, null);
		}
	}
	
	private String convierteEntidadCastellano(String entidadCatalan, Connection conn) {
		String eCastellano="";
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		try {
			String sentenciaSql="SELECT * FROM BZENTID WHERE FZGCENT2=? AND FZGNENTI=? AND FZGFBAJA=0";
			ps1=conn.prepareStatement(sentenciaSql);
			ps1.setString(1,entidadCatalan);
			ps1.setInt(2,Integer.parseInt(entidad2Nuevo));
			rs=ps1.executeQuery();
			
			if (rs.next()) {
				eCastellano=rs.getString("FZGCENTI");
			} else {
				eCastellano="";
			}
		} catch (Exception e) {
			eCastellano="";
		} finally {
			ToolsBD.closeConn(null, ps1, rs);
		}
		return eCastellano;
	}
	
	
	/**
	 * @return
	 */
	public boolean getValidado() {
		return validado;
	}
	
	/* Devolvemos la serie de errores */
	
	/**
	 * @return
	 */
	public Hashtable getErrores() {
		return errores;
	}
	
	/* Devolvemos si el registro de entrada se ha grabado bien */
	/**
	 * @return
	 */
	public boolean getGrabado() {
		return registroSalidaGrabado;
	}
	
	/* Devolvemos valores para mostrarlos */
	
	/**
	 * @return
	 */
	public String getOficina() {
		return oficina;
	}
	/**
	 * @return
	 */
	public String getOficinafisica() {
		return oficinafisica;
	}
	/**
	 * @return
	 */
	public String getCorreo() {
		return correo;
	}
	/**
	 * @return
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @return
	 */
	public String getRegistroAnulado() {
		return registroAnulado;
	}
	/**
	 * @return
	 */
	public String getIdioma() {
		return idioma;
	}
	/**
	 * @return
	 */
	public String getEntidad1() {
		return entidad1;
	}
	/**
	 * @return
	 */
	public String getEntidad2() {
		return entidad2;
	}
	/**
	 * @return
	 */
	public String getAltres() {
		return altres;
	}
	/**
	 * @return
	 */
	public String getBalears() {
		return balears;
	}
	/**
	 * @return
	 */
	public String getFora() {
		return fora;
	}
	/**
	 * @return
	 */
	public String getEntrada1() {
		return entrada1;
	}
	/**
	 * @return
	 */
	public String getEntrada2() {
		return entrada2;
	}
	/**
	 * @return
	 */
	public String getRemitent() {
		return remitent;
	}
	/**
	 * @return
	 */
	public String getIdioex() {
		return idioex;
	}
	/**
	 * @return
	 */
	public String getDisquet() {
		return disquet;
	}
	/**
	 * @return
	 */
	public String getComentario() {
		return comentario;
	}
	/**
	 * @return
	 */
	public String getHora() {
		return hora;
	}
	/**
	 * @return
	 */
	public String getDataSalida() {
		return datasalida;
	}
	/**
	 * @return
	 */
	public String getData() {
		return data;
	}
	/**
	 * @return
	 */
	public String getNumero() {
		return String.valueOf(fzsnume);
	}
	
	/**
	 * @return
	 */
	public String getAnoSalida() {
		return anoSalida;
	}
	/**
	 * @return
	 */
	public String getNumeroSalida() {
		return numeroSalida;
	}
	/**
	 * @return
	 */
	public String getDescripcionOficina() {
		return descripcionOficina;
	}
	/**
	 * @return
	 */
	public String getDescripcionOficinaFisica() {
		return descripcionOficinaFisica;
	}
	/**
	 * @return
	 */
	public String getDescripcionDestinatario() {
		return descripcionDestinatario;
	}
	/**
	 * @return
	 */
	public String getDescripcionOrganismoRemitente() {
		return descripcionOrganismoRemitente;
	}
	/**
	 * @return
	 */
	public String getDescripcionDocumento() {
		return descripcionDocumento;
	}
	/**
	 * @return
	 */
	public String getDescripcionIdiomaDocumento() {
		return descripcionIdiomaDocumento;
	}
	/**
	 * @return
	 */
	public String getDestinoGeografico() {
		return destinoGeografico;
	}
	/**
	 * @return
	 */
	public String getIdiomaExtracto() {
		return idiomaExtracto;
	}
	public String getEntidad1Nuevo() {
		return entidad1Nuevo;
	}
	public String getEntidad2Nuevo() {
		return entidad2Nuevo;
	}
	public String getAltresNuevo() {
		return altresNuevo;
	}
	public String getComentarioNuevo() {
		return comentarioNuevo;
	}
	public boolean getLeido() {
		return leidos;
	}
	public String getDataVisado() {
		return dataVisado;
	}
	
	
	public String getMotivo() {
		return motivo;
	}
	public String getEntidad1Grabada() {
		return entidad1Grabada;
	}
	public boolean getActualizado() {
		return registroActualizado;
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
		this.ctx = ctx;
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
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR: S'ha produït un error a logLopdBZSALID");
		} finally {
			try {
				ToolsBD.closeConn(conn, ps, null);
			} catch (Exception e){
				System.out.println("ERROR: S'ha produït un error a logLopdBZSALID en tancar la connexió. ");
			}
		}
		//System.out.println("RegistroSalidaBean: Desada informació dins BZSALPD: "+tipusAcces+" "+usuari+" "+data+" "+hora+" "+nombreRegistre+" "+any+" "+oficina);
     }
}