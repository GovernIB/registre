package es.caib.regweb.logic.ejb;

import java.util.*;
import java.text.*;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.ScrollableResults;
import org.hibernate.ScrollMode;
import es.caib.regweb.logic.helper.Helper;

import java.rmi.*;

import javax.ejb.*;

import java.lang.reflect.InvocationTargetException;

import es.caib.regweb.logic.helper.ParametrosRegistroModificado;
import es.caib.regweb.logic.interfaces.RegistroModificadoSalidaFacade;
import es.caib.regweb.logic.util.RegistroModificadoSalidaFacadeUtil;
import es.caib.regweb.logic.interfaces.ValoresFacade;
import es.caib.regweb.logic.util.ValoresFacadeUtil;
import es.caib.regweb.logic.interfaces.AdminFacade;
import es.caib.regweb.logic.util.AdminFacadeUtil;
import es.caib.regweb.logic.interfaces.LocalitzadorsDocsElectronicsFacade;
import es.caib.regweb.logic.util.LocalitzadorsDocsElectronicsFacadeUtil;
import org.apache.log4j.Logger;

import es.caib.regweb.model.LogSalidaLopd;
import es.caib.regweb.model.LogSalidaLopdId;

import es.caib.regweb.logic.helper.ParametrosRegistroSalida;

/**
 * SessionBean per a manteniment de registres de sortida
 *
 * @ejb.bean
 *  name="logic/RegistroSalidaFacade"
 *  jndi-name="es.caib.regweb.logic.RegistroSalidaFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 */
public abstract class RegistroSalidaFacadeEJB extends HibernateEJB {
	
	private static final long serialVersionUID = 3L;
	private static int CORRECTE = 1;
	private static int FECHA_NO_LOGICA = 2;
	private static int FECHA_POSTERIOR_ACTUAL = 3;
	private static int FECHA_DEMASIADO_ANTIGUA = 4;
	
	private Logger log = Logger.getLogger(this.getClass());
	private DateFormat dateF= new SimpleDateFormat("dd/MM/yyyy");
	private Date fechaTest=null;
	private DateFormat horaF=new SimpleDateFormat("HH:mm:ss");
	private Date horaTest=null;

	private String SENTENCIA="INSERT INTO BZSALIDA (" +
	"FZSANOEN, FZSNUMEN, FZSCAGCO, FZSFDOCU, FZSREMIT, FZSCONEN, FZSCTIPE, FZSCEDIE, FZSENULA,"+
	"FZSPROCE, FZSFENTR, FZSCTAGG, FZSCAGGE, FZSCORGA, FZSFACTU, FZSCENTI, FZSNENTI, FZSHORA,"+
	"FZSCIDIO, FZSCONE2, FZSNLOC, FZSALOC, FZSNDIS, FZSFSIS, FZSHSIS, FZSCUSU, FZSCIDI,EMAILREMITENT"+
	") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
		
	
	/* Validaciones del registro de entrada*/
	
	/**
	 * @return
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public ParametrosRegistroSalida validar(ParametrosRegistroSalida param) {
        Session session = getSession();
        ScrollableResults rs=null;
        Query q = null;
    	String datasalida=param.getDataSalida();
    	String hora=param.getHora();
    	String oficina=param.getOficina();
    	String oficinafisica=param.getOficinafisica();
    	String data=param.getData();
    	String tipo=param.getTipo();
    	String idioma=param.getIdioma();
    	String entidad1=param.getEntidad1();
    	String entidad2=param.getEntidad2();
    	String altres=param.getAltres();
    	String balears=param.getBalears();
    	String fora=param.getFora();
    	String entrada1=param.getEntrada1();
    	String entrada2=param.getEntrada2();
    	String remitent=param.getRemitent();
    	String idioex=param.getIdioex();
    	String disquet=param.getDisquet();
    	String comentario=param.getComentario();
    	String usuario=param.getUsuario();
    	boolean actualizacion=param.getActualizacion();
    	String motivo=param.getMotivo();
    	String entidad1Nuevo=param.getEntidad1Nuevo();
    	String entidad2Nuevo=param.getEntidad2Nuevo();
    	String altresNuevo=param.getAltresNuevo();
    	String comentarioNuevo=param.getComentarioNuevo();      
    	boolean error=param.getError();
    	boolean validado=param.getValidado();
    	Hashtable errores=param.getErrores();
    	int error_fecha = CORRECTE;
    	
		validado=false;
		errores.clear();
		
		try {
			/* Validamos la fecha del registro */
			error_fecha = validarFecha(datasalida);
			if (error_fecha==FECHA_NO_LOGICA) {
				errores.put("datasalida","Data de sortida no \u00e9s l\u00f2gica.");
			}

			if (error_fecha==FECHA_POSTERIOR_ACTUAL) {
				errores.put("datasalida","Data de sortida \u00e9s posterior a la del dia.");
			}
			
			if (error_fecha==FECHA_DEMASIADO_ANTIGUA) {
				errores.put("datasalida","Data de sortida \u00e9s massa antiga.");
			}
			
			/* Validamos Fecha del documento */
			if (data==null) {
				//Si no tenemos fecha de documento, copiamos la del registro
				data=datasalida;
				param.setdata(data);
			}else{
				error_fecha = validarFecha(data);
				
				if (error_fecha==FECHA_NO_LOGICA) {
					errores.put("data","Data de document \u00e9s no \u00e9s l\u00f2gica.");
				}

				if (error_fecha==FECHA_POSTERIOR_ACTUAL) {
					errores.put("data","Data de document \u00e9s posterior a la del dia.");
				}
				
				if (error_fecha==FECHA_DEMASIADO_ANTIGUA) {
					errores.put("data","Data de document \u00e9s massa antiga.");
				}
			}
		
			/* Validamos Hora */
			if (hora==null) {
				errores.put("hora","Hora de sortida no \u00e9s l\u00f2gica Valor:"+hora);
			} else {
				try {
					horaF.setLenient(false);
					horaTest=horaF.parse(hora);
				} catch (ParseException ex) {
					errores.put("hora","Hora de sortida no \u00e9s l\u00f2gica");
				}
			}
			
			/* Validamos la oficina */
			if (!oficina.equals("")) {
				try {
					String sentenciaHql=" FROM Autorizacion WHERE id.usuario=? AND id.codigoAutorizacion=? AND id.codigoOficina IN " +
					"(SELECT codigo FROM Oficina WHERE fechaBaja=0 AND codigo=?)";
					q=session.createQuery(sentenciaHql);
					q.setString(0,usuario.toUpperCase());
					q.setString(1,"AS");
					q.setInteger(2,Integer.parseInt(oficina));
					rs=q.scroll();
					
					if (rs.next()) {
					} else {
						errores.put("oficina","Oficina: "+oficina+" no v\u00e0lida per a l'usuari: "+usuario);
					}
				} catch (Exception e) {
					log.error(usuario+": Error en validar l'oficina "+e.getMessage() );
					errores.put("oficina","Error en validar l'oficina: "+oficina+" de l'usuari: "+usuario+": "+e.getClass()+"->"+e.getMessage());
				} finally {
					rs.close();
				}
			} else {
				errores.put("oficina","Oficina: "+oficina+" no v\u00e0lida per a l'usuari: "+usuario);
			}

            /* Validamos la oficina fisica */
            if (oficinafisica==null || oficinafisica.equals("") || oficinafisica.equals("null")) {
                errores.put("oficinafisica","Oficina f\u00EDsica no v\u00e0lida.");
            }

            /* Validamos el contador de la oficina */
            if (!oficina.equals("")) {
    			
                int fzsanoe=Helper.obtenerAnyoDeFechadd_MM_YYY(datasalida);
                
                AdminFacade admin=AdminFacadeUtil.getHome().create();
                if(!admin.existComptador(String.valueOf(fzsanoe),oficina)) {
                    errores.put("oficina","Oficina: "+oficina+" no t\u00E9 inicialitzat el comptador.");
                }
            }

			/* Validamos Tipo de documento */
			try {
				String sentenciaHql=" FROM TipoDocumento WHERE id.codigo=? AND id.fechaBaja=0";
				q=session.createQuery(sentenciaHql);
				q.setString(0,tipo);
				rs=q.scroll();
				
				if (rs.next()) {
				} else {
					errores.put("tipo","Tipus de document : "+tipo+" no v\u00e0lid");
				}
			} catch (Exception e) {
				log.error(usuario+": Error en validar el tipus de document"+e.getMessage() );
				errores.put("tipo","Error en validar el tipus de document : "+tipo+": "+e.getClass()+"->"+e.getMessage());
			} finally {
					//	Tancam el que pugui estar obert
					rs.close();
			}
			
			/* Validamos el idioma del documento */
			try {
				String sentenciaHql=" FROM Idioma WHERE codigo=?";
				q=session.createQuery(sentenciaHql);
				q.setString(0,idioma);
				rs=q.scroll();
				
				if (rs.next()) {
				} else {
					errores.put("idioma","Idioma del document : "+idioma+" no v\u00e0lid");
				}
			} catch (Exception e) {
				log.error(usuario+": Error en validar l'idioma del document"+e.getMessage() );
				errores.put("idioma","Error en validar l'idioma del document: "+idioma+": "+e.getClass()+"->"+e.getMessage());
			} finally {
				rs.close();
			}
			
			/* Validamos destinatario */
			if (entidad1.equals("") && altres.equals("")) {
				errores.put("entidad1","Obligatori introduir destinatari");
			} else if(!entidad1.equals("") && !altres.equals("")) {
				errores.put("entidad1","Heu d'introduir: Entitat o Altres");
			} else if (!entidad1.equals("")) {
				if (entidad2.equals("")) {entidad2="0";param.setentidad2(entidad2);}
				try {
					String sentenciaHql="SELECT id.codigo FROM EntidadRemitente WHERE codigoCatalan=? AND id.numero=? AND id.fechaBaja=0";
					q=session.createQuery(sentenciaHql);
					q.setString(0,entidad1);
					q.setInteger(1,Integer.parseInt(entidad2));
					rs=q.scroll();
					
					if (rs.next()) {
						param.setEntidadCastellano(rs.getString(0));
					} else {
						errores.put("entidad1","Entitat Destinat\u00e0ria : "+entidad1+"-"+entidad2+" no v\u00e0lida");
					}
				} catch (Exception e) {
					log.error(usuario+": Error en validar l'entitat destinat\u00e0ria "+e.getClass()+"->"+e.getMessage() );
					errores.put("entidad1","Error en validar l'entitat destinat\u00e0ria: "+entidad1+"-"+entidad2+": "+e.getClass()+"->"+e.getMessage());
				} finally {
						//	Tancam el que pugui estar obert
						rs.close();
				}
			}
			
			/* Validamos la procedencia geografica */
			if (balears.equals("") && fora.equals("")) {
				errores.put("balears","Obligatori introduir dest\u00ed Geogr\u00e0fic");
			} else if (!balears.equals("") && !fora.equals("")) {
				errores.put("balears","Heu d'introduir Balears o Fora de Balears");
			} else if (!balears.equals("")) {
				try {
					String sentenciaHql=" SELECT id.tipo FROM AgrupacionGeografica WHERE id.tipo=90 AND id.codigo=? AND fechaBaja=0";
					q=session.createQuery(sentenciaHql);
					q.setInteger(0,Integer.parseInt(balears));
					rs=q.scroll();
					
					if (rs.next()) {
					} else {
						errores.put("balears","Desti geogr\u00e0fic de Balears : "+balears+" no v\u00e0lid");
					}
				} catch (Exception e) {
					log.error(usuario+": Error en validar En comprovar el dest\u00ed geogr\u00e0fic "+e.getMessage() );
					errores.put("balears","Error en validar En comprovar el dest\u00ed geogr\u00e0fic: "+balears+": "+e.getClass()+"->"+e.getMessage());
				} finally {
					rs.close();
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
					errores.put("entrada1","Ambd\u00f3s camps de numero d'entrada han de ser num\u00e8rics");
				}
				if (chk2<1990 || chk2>2050) {
					errores.put("entrada2","Any d'entrada, incorrecte. Valor:"+chk2);
				}
			}
			
			/* Validamos el Organismo emisor */
			try {
				String sentenciaHql=" FROM OficinaOrganismo WHERE id.oficina.codigo=? AND id.organismo.codigo=?";
				q=session.createQuery(sentenciaHql);
				q.setInteger(0,Integer.parseInt(oficina));
				q.setInteger(1,Integer.parseInt(remitent));
				rs=q.scroll();
				
				if (rs.next()) {
				} else {
					errores.put("remitent","Organisme emisor: "+remitent+" no v\u00e0lid");
				}
			} catch (NumberFormatException e1) {
				errores.put("remitent","Organisme emisor: "+remitent+" codi no num\u00e8ric");
			} catch (Exception e) {
				log.error(usuario+": Error en validar l'organisme emisor "+e.getMessage() );
				errores.put("remitent","Error en validar l'organisme emisor: "+remitent+": "+e.getClass()+"->"+e.getMessage());
			} finally {
					// Tancam el que pugui estar obert
					rs.close();
			}
			
			/* Validamos el idioma del extracto */
			
			if (!idioex.equals("1") && !idioex.equals("2")) {
				errores.put("idioex","L'idioma ha de ser 1 \u00f2 2, idioma="+idioex);
			}			
			
			/* Comprobamos que la ultima fecha introducida en el fichero sea inferior o igual
			 * que la fecha de entrada del registro */
			if (!oficina.equals("")  && !actualizacion) {
				try {
					String sentenciaHql="SELECT MAX(fechaSalida) as maximo FROM Salida WHERE id.anyo=? AND id.oficina=?";
					//fechaTest = dateF.parse(datasalida);
					//Calendar cal=Calendar.getInstance();
					//cal.setTime(fechaTest);
					//DateFormat date1=new SimpleDateFormat("yyyyMMdd");
					
					q=session.createQuery(sentenciaHql);
					
					//q.setInteger(0,cal.get(Calendar.YEAR));
					q.setInteger(0,Helper.obtenerAnyoDeFechadd_MM_YYY(datasalida));
					q.setInteger(1,Integer.parseInt(oficina));
					
					rs=q.scroll();
					int ultimaFecha=0;
					
                    if (rs!=null && rs.next()) {
                        Object result = rs.get(0);
					    if (result!=null) ultimaFecha=(Integer)result;
					    
					    //if (ultimaFecha>Integer.parseInt(date1.format(fechaTest))) {
					    if (ultimaFecha>Helper.convierteStringFechaAIntFecha(datasalida)) {
    						errores.put("datasalida","Data inferior a la darrera sortida");
    					}
    				}
				} catch (Exception e) {
					log.error(usuario+": Error inesperat en la data de sortida "+e.getMessage() );
					errores.put("datasalida","Error inesperat en la data de sortida: "+datasalida+": "+e.getClass()+"->"+e.getMessage());
				} finally {
					if (rs!=null) rs.close();
				}
			}
			
			/* Validamos el numero de disquete */
			try {
				if (!disquet.equals("")) {
					Integer.parseInt(disquet);
				}
			} catch (Exception e) {
				errores.put("disquet","Numero de disquet no v\u00e0lid");
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
					if (entidad2Nuevo.equals("")) {entidad2Nuevo="0";param.setEntidad2Nuevo(entidad2Nuevo);}
					try {
						String sentenciaHql=" FROM EntidadRemitente WHERE codigoCatalan=? AND id.numero=? AND id.fechaBaja=0";
						q=session.createQuery(sentenciaHql);
						q.setString(0,entidad1Nuevo);
						q.setInteger(1,Integer.parseInt(entidad2Nuevo));
						rs=q.scroll();
						
						if (rs.next()) {
						} else {
							errores.put("entidad1","Entitat Destinat\u00e0ria : "+entidad1+"-"+entidad2+" no v\u00e0lid");
						}
					} catch (Exception e) {
						log.error(usuario+": Error inesperat en l'entitat destinat\u00e0ria "+e.getMessage() );
						
						errores.put("entidad1","Error inesperat en l'entitat destinat\u00e0ria : "+entidad1+"-"+entidad2+": "+e.getClass()+"->"+e.getMessage());
					} finally {
							//Tancam el que pugui estar obert
							rs.close();
					}
				}
			}
			
			/* Fin de validaciones de campos */
		} catch (Exception e) {
			
			validado=false;
		} finally {
			close(session);
		}
		if (errores.size()==0) {
			validado=true;
		} else {
			validado=false;
		}

        param.setValidado(validado);
		return param;
	}
	
	
	/* Grabamos registro de salida si las validaciones son correctas */
	
	/**
	 * @throws EJBException
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public ParametrosRegistroSalida grabar(ParametrosRegistroSalida param) throws EJBException {	
		//String dataVisado=param.getDataVisado();
    	String datasalida=param.getDataSalida();
    	String hora=param.getHora();
    	String oficina=param.getOficina();
    	String oficinafisica=param.getOficinafisica();
    	String data=param.getData();
    	String tipo=param.getTipo();
    	String idioma=param.getIdioma();
    	String entidad2=param.getEntidad2();
    	String altres=param.getAltres();
    	String balears=param.getBalears();
    	String fora=param.getFora();
    	String entrada1=param.getEntrada1();
    	String entrada2=param.getEntrada2();
    	String remitent=param.getRemitent();
    	String idioex=param.getIdioex();
    	String disquet=param.getDisquet();
    	String comentario=param.getComentario();
    	int fzsnume=param.getFzsnume();
    	String usuario=param.getUsuario();
    	String correo=param.getCorreo();
    	boolean validado=param.getValidado();
    	boolean registroSalidaGrabado=param.getGrabado();
    	Hashtable errores=param.getErrores();
    	String entidadCastellano=param.getEntidadCastellano();
        String emailRemitente=param.getEmailRemitent();
        String locDocs=param.getLocalitzadorsDocs(); 

		int fzsanoe;
		int fzsfent;
		int fzsfsis;
		int horamili;
		int fzscagc;
		int off_codi;
		
		Session session = getSession();
		
		if (!validado) {
            param=validar(param);
			validado=param.getValidado();
		}
		if (!validado) {
			throw new EJBException("No s'ha realitzat la validaci\u00f3 de les dades del registre. ");
		}

		registroSalidaGrabado=false;
		try {
			LocalitzadorsDocsElectronicsFacade regLocDocElect = LocalitzadorsDocsElectronicsFacadeUtil.getHome().create();
			SQLQuery ms = null;
            
			/* Descomponemos el año de la data de entrada, FZAANOEN y preparamos campo
			 FZSFENT en formato aaaammdd */			

			//fechaTest = dateF.parse(datasalida);
			Calendar cal=Calendar.getInstance();
			cal.setTime(dateF.parse(datasalida));
			//
			//fzsanoe=cal.get(Calendar.YEAR);
			fzsanoe=Helper.obtenerAnyoDeFechadd_MM_YYY(datasalida);
			param.setAnoSalida(String.valueOf(fzsanoe));
			fzsfent=Helper.convierteStringFechaAIntFecha(datasalida);
			
			/* Recuperamos numero de entrada */
			fzsnume=Helper.recogerNumeroSalida(session, fzsanoe, oficina, errores);
			param.setNumeroSalida(Integer.toString(fzsnume));
			
			/* Oficina, fzscagc */
			fzscagc=Integer.parseInt(oficina);
			off_codi=Integer.parseInt(oficinafisica);
			
			/* Fecha documento en un campo en formato aaaammdd, fzsfdoc */
			//fechaTest = dateF.parse(data);
			//cal.setTime(fechaTest);
			//int fzsfdoc=Integer.parseInt(date1.format(fechaTest));
			int fzsfdoc=Helper.convierteStringFechaAIntFecha(data);
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
			
			/* Hora del documento, fzshora hhmmss */
			horaTest=horaF.parse(hora);
			cal.setTime(horaTest);
			int fzshora=Helper.convierteStringHoraAIntHoraSeg(hora);

                        log.error(" HORAAAAAA: " + fzshora);
			
			/* Numero localizador y año localizador, fzsnloc y fzsaloc */
			int fzsnloc=Integer.parseInt((entrada1.equals(""))?"0":entrada1);
			int fzsaloc=Integer.parseInt((entrada2.equals(""))?"0":entrada2);
			
			/* Numero de disquette, fzsndis */
			int fzsndis=Integer.parseInt((disquet.equals(""))?"0":disquet);
			
			/* Actualizamos el numero de disquete */
			if (fzsndis>0){Helper.actualizaDisqueteSalida(session, fzsndis, oficina, param.getAnoSalida(), errores);}
			
			/* Recuperamos la fecha y la hora del sistema, fzsfsis(aaaammdd) y
			 fzshsis (hhMMssmm) */
			int fzshsis=Helper.obtenerHoraSistemaHHmmssS();
			fzsfsis=Helper.obtenerFechaSistemayyyyMMdd();

			/* Ejecutamos sentencias SQL */
			ms=session.createSQLQuery(SENTENCIA);
			
			ms.setInteger(0,fzsanoe);
			ms.setInteger(1,fzsnume);
			ms.setInteger(2,fzscagc);
			ms.setInteger(3,fzsfdoc);
			ms.setString(4,(altres.length()>30) ? altres.substring(0,30) : altres); // 30 pos.
			ms.setString(5,(fzscone.length()>160) ? fzscone.substring(0,160) : fzscone); // 160 pos.
			ms.setString(6,(tipo.length()>2) ? tipo.substring(0,2) : tipo); // 2 pos.
			ms.setString(7,"N");
			ms.setString(8,"");
			ms.setString(9,(fzsproce.length()>25) ? fzsproce.substring(0,25) : fzsproce); // 25 pos.
			ms.setInteger(10,fzsfent);
			ms.setInteger(11,fzsctagg);
			ms.setInteger(12,fzscagge);
			ms.setInteger(13,fzscorg);
			ms.setInteger(14,ceros);
			ms.setString(15,(fzscent.length()>7) ? fzscent.substring(0,7) : fzscent); // 7 pos.
			ms.setInteger(16,fzsnent);
			ms.setInteger(17,fzshora);
			ms.setInteger(18,fzscidi);
			ms.setString(19,(fzscone2.length()>160) ? fzscone2.substring(0,160) : fzscone2); // 160 pos.
			ms.setInteger(20,fzsnloc);
			ms.setInteger(21,fzsaloc);
			ms.setInteger(22,fzsndis);
			ms.setInteger(23,fzsfsis);
			ms.setInteger(24,fzshsis);
			ms.setString(25,(usuario.toUpperCase().length()>10) ? usuario.toUpperCase().substring(0,10) : usuario.toUpperCase()); // 10 pos.
			ms.setString(26,idioma);
			ms.setString(27,emailRemitente);
			
			ms.executeUpdate();
			registroSalidaGrabado=true;

            /* Grabamos numero de correo si tuviera */
            if (correo!=null && !correo.equals("")) {
                String insertBZNCORR="INSERT INTO BZNCORR (FZPCENSA, FZPCAGCO, FZPANOEN, FZPNUMEN, FZPNCORR)" +
                "VALUES (?,?,?,?,?)";
                ms=session.createSQLQuery(insertBZNCORR);
                ms.setString(0, "S");
                ms.setInteger(1,fzscagc);
                ms.setInteger(2,fzsanoe);
                ms.setInteger(3,fzsnume);
                ms.setString(4, correo);
                ms.executeUpdate();
            }

            String insertOfifis="INSERT INTO BZSALOFF (FOSANOEN, FOSNUMEN, FOSCAGCO, OFS_CODI)" +
            "VALUES (?,?,?,?)";
            ms=session.createSQLQuery(insertOfifis);
            ms.setInteger(0,fzsanoe);
            ms.setInteger(1,fzsnume);
            ms.setInteger(2,fzscagc);
            ms.setInteger(3,off_codi);
            ms.executeUpdate();
            
            /* Guardamos los documentos electrónicos, si tiene. */
            if (locDocs!=null && !locDocs.equals("")) {
            	log.debug("locDocs"+locDocs);
            	regLocDocElect.GrabarLocalitzadorDocsSalida(session, fzsanoe, fzsnume, fzscagc,locDocs);
            }
            
            // desacoplamiento cobol
            String remitente="";
            if (!altres.trim().equals("")) {
                remitente=altres;
            } else {
                ValoresFacade valor = ValoresFacadeUtil.getHome().create();
                remitente=valor.recuperaRemitenteCastellano(fzscent, fzsnent+"");
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
            
            horamili=Helper.obtenerHoraSistemaHHmmssSS();
            session.flush();
		}catch(HibernateException he){
			// Tratamos las excepciones generadas por Hibernate
			log.error("Error: S'ha generat una Hibernate Exception " + he.getMessage());
			registroSalidaGrabado=false;
			errores.put("","Error inesperat, no s'ha desat el registre"+": "+he.getClass()+"->"+he.getMessage());

			throw new EJBException("Error: S'ha generat una Hibernate Exception " + he.getMessage());                
		} catch (Exception ex) {
			 log.error(usuario+": Excepci\u00f3: "+ex.getMessage());
			registroSalidaGrabado=false;
			errores.put("","Error inesperat, no s'ha desat el registre"+": "+ex.getClass()+"->"+ex.getMessage());
			
			throw new EJBException("Error inesperat, no s'ha desat el registre", ex);
		} finally {
			close(session);
		}
		
		try { 
			logLopdBZSALIDA("INSERT", (usuario.toUpperCase().length()>10) ? usuario.toUpperCase().substring(0,10) : usuario.toUpperCase(), fzsfsis, horamili, fzsnume, fzsanoe, fzscagc);
		} catch (Exception e){
			log.error(usuario+": Error inesperat al guardar el LogLopd."+e.getMessage() );
			
		}
        param.setregistroSalidaGrabado(registroSalidaGrabado);
		return param;
	}
	
	/**
	 * @param fecha
	 */
	private int validarFecha(String fecha) {
		int ok=CORRECTE;
		SimpleDateFormat  dateF= new SimpleDateFormat("dd/MM/yyyy");
		Date fechaTest = null;
		Date fechaHoy = new Date();
		Date fechaMinimo = null;

		try {
			fechaMinimo = dateF.parse("01/01/1900");
			dateF.setLenient(false);
			fechaTest = dateF.parse(fecha);
		} catch (Exception ex) {
			log.error("Error al validar la fecha: "+fecha);
			ok=FECHA_NO_LOGICA;
		}
		
		if(ok==CORRECTE){
			//Comprobamos que la fecha no sea posterior a la actual
			if(fechaTest.after(fechaHoy)){
				log.error("Error al validar la fecha: "+fecha+". Fecha posterior a la actual.");
				ok=FECHA_POSTERIOR_ACTUAL;
			}
			//Comprobamos que la fecha no sea demasiado antigua
			if(fechaTest.before(fechaMinimo)){
				log.error("Error al validar la fecha: "+fecha+". No puede anterior a la fecha 01/01/1950.");
				ok=FECHA_DEMASIADO_ANTIGUA;
			}
		}
		return ok;
	}
	
	/** 
	 * Lee un registro de entrada del fichero BZSALIDA, para ello le
	 * deberemos pasar el usuario, el codigo de oficina, el numero de registro de
	 * salida y el año de salida.
	 * @param param ParametrosRegistroSalida
	 * @return Devuelve un objeto de la clase ParametrosRegistroSalida
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public ParametrosRegistroSalida leer(ParametrosRegistroSalida param) {		
		ParametrosRegistroSalida res = new ParametrosRegistroSalida();		
		Session session = getSession();
        ScrollableResults rs = null;
        ScrollableResults rsHist = null;
        SQLQuery q = null;
        SQLQuery qHist = null;
    	String oficina=param.getOficina();
    	String usuario=param.getUsuario();    
    	String anoSalida=param.getAnoSalida();
    	String numeroSalida=param.getNumeroSalida();
        res.setLeido(false);
		DateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
		DateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date fechaDocumento=null;
		
		try {
			LocalitzadorsDocsElectronicsFacade regLocDocElect = LocalitzadorsDocsElectronicsFacadeUtil.getHome().create();
			String sentenciaHql="SELECT FZSNUMEN, FZSANOEN, FZSCAGCO, OFF_CODI, OFF_NOM, FZSFENTR, FAADAGCO, " +
    			" FZSFDOCU, FZSFACTU, FZSHORA, FZGCENTI, FZSREMIT, FZGDENT2, FZSENULA, " +
    			" FZSCTIPE, FZSCIDI, FZSCIDIO, FZSCENTI, FZSNENTI, FZSCAGGE, FZSPROCE, " +
    			" FZSCORGA, FAXDORGT, FZIDTIPE, FZMDIDI, FABDAGGE, FZSCONEN, FZSCONE2, " +
    			" FZSNDIS, FZSNLOC, FZSALOC, FZPNCORR,EMAILREMITENT FROM BZSALIDA LEFT JOIN BAGECOM ON FAACAGCO=FZSCAGCO " +
    			" LEFT JOIN BZENTID ON FZSCENTI=FZGCENTI AND FZGNENTI=FZSNENTI " +
    			" LEFT JOIN BORGANI ON FAXCORGA=FZSCORGA " +
    			" LEFT JOIN BZTDOCU ON FZICTIPE=FZSCTIPE " +
    			" LEFT JOIN BZIDIOM ON FZSCIDI=FZMCIDI " +
    			" LEFT JOIN BZSALOFF ON FZSANOEN=FOSANOEN AND FZSNUMEN=FOSNUMEN AND FZSCAGCO=FOSCAGCO " +
    			" LEFT JOIN BZOFIFIS ON FZSCAGCO=FZOCAGCO AND OFF_CODI=OFS_CODI " +
    			" LEFT JOIN BAGRUGE ON FZSCTAGG=FABCTAGG AND FZSCAGGE=FABCAGGE " +
    			" LEFT JOIN BZAUTOR ON FZHCUSU=? AND FZHCAGCO=FZSCAGCO " +
    			" LEFT JOIN BZNCORR ON FZPCENSA='S' AND FZPCAGCO=FZSCAGCO AND FZPANOEN=FZSANOEN AND FZPNUMEN=FZSNUMEN " +
    			" WHERE FZSANOEN=? AND FZSNUMEN=? AND FZSCAGCO=? AND FZHCAUT=?";
			q=session.createSQLQuery(sentenciaHql);
			
			q.addScalar("FZSNUMEN", Hibernate.INTEGER);
            q.addScalar("FZSANOEN", Hibernate.INTEGER);
            q.addScalar("FZSCAGCO", Hibernate.INTEGER);
            q.addScalar("OFF_CODI", Hibernate.INTEGER);
            q.addScalar("OFF_NOM" , Hibernate.STRING);
            q.addScalar("FZSFENTR", Hibernate.INTEGER);
            q.addScalar("FAADAGCO", Hibernate.STRING);
            q.addScalar("FZSFDOCU", Hibernate.INTEGER);
            q.addScalar("FZSFACTU", Hibernate.INTEGER);
            q.addScalar("FZSHORA" , Hibernate.INTEGER);
            q.addScalar("FZGCENTI", Hibernate.STRING);
            q.addScalar("FZSREMIT", Hibernate.STRING);
            q.addScalar("FZGDENT2", Hibernate.STRING);
            q.addScalar("FZSENULA", Hibernate.STRING);
            q.addScalar("FZSCTIPE", Hibernate.STRING);
            q.addScalar("FZSCIDI" , Hibernate.STRING);
            q.addScalar("FZSCIDIO", Hibernate.STRING);
            q.addScalar("FZSCENTI", Hibernate.STRING);
            q.addScalar("FZSNENTI", Hibernate.STRING);
            q.addScalar("FZSCAGGE", Hibernate.STRING);
            q.addScalar("FZSPROCE", Hibernate.STRING);
            q.addScalar("FZSCORGA", Hibernate.INTEGER);
            q.addScalar("FAXDORGT", Hibernate.STRING);
            q.addScalar("FZIDTIPE", Hibernate.STRING);
            q.addScalar("FZMDIDI" , Hibernate.STRING);
            q.addScalar("FABDAGGE", Hibernate.STRING);
            q.addScalar("FZSCONEN", Hibernate.STRING);
            q.addScalar("FZSCONE2", Hibernate.STRING);
            q.addScalar("FZSNDIS" , Hibernate.STRING);
            q.addScalar("FZSNLOC" , Hibernate.INTEGER);
            q.addScalar("FZSALOC" , Hibernate.INTEGER);
            q.addScalar("FZPNCORR", Hibernate.STRING);
            q.addScalar("EMAILREMITENT", Hibernate.STRING);
            
			q.setString(0,usuario.toUpperCase());
			q.setInteger(1,Integer.valueOf(anoSalida));
			q.setInteger(2,Integer.valueOf(numeroSalida));
			q.setInteger(3,Integer.valueOf(oficina));
			q.setString(4,"CS");
			rs=q.scroll();
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

                int horamili=Helper.obtenerHoraSistemaHHmmssSS();
				logLopdBZSALIDA("SELECT", (usuario.toUpperCase().length()>10) ? usuario.toUpperCase().substring(0,10) : usuario.toUpperCase()
						, fzsfsis, horamili, rs.getInteger(0), rs.getInteger(1), rs.getInteger(2));
				
				res.setLeido(true);	
				res.setAnoSalida(String.valueOf(rs.getInteger(1)));
				res.setNumeroSalida(String.valueOf(rs.getInteger(0)));
				res.setoficina(String.valueOf(rs.getInteger(2)));
				res.setoficinafisica(String.valueOf(rs.getInteger(3)));
				
				/* Aquí hem d'anar a la taula d'oficines fisiques. */
				String textoOficinaFisica=null;
				textoOficinaFisica=rs.getString(4);
				
				if (rs.getInteger(3)==null) {
					res.setoficinafisica("0");

	                String sentenciaHqlOfiFis="SELECT OFF_NOM FROM BZOFIFIS WHERE FZOCAGCO=? AND OFF_CODI=0 ";
		            qHist=session.createSQLQuery(sentenciaHqlOfiFis);
		            qHist.addScalar("OFF_NOM", Hibernate.STRING);
		            qHist.setInteger(0,Integer.valueOf(res.getOficina()));
		            rsHist=qHist.scroll();
		            if (rsHist.next()) {
						textoOficinaFisica=rsHist.getString(0);
		            }
		    		if (rsHist != null)
		    			rsHist.close();

				}
				if (textoOficinaFisica==null) {
					textoOficinaFisica=" ";
				}

				res.setDescripcionOficinaFisica(textoOficinaFisica);

				String fechaSalid=String.valueOf(rs.getInteger(5));
				try {
					fechaDocumento=yyyymmdd.parse(fechaSalid);
					res.setdatasalida(ddmmyyyy.format(fechaDocumento));
				} catch (Exception e) {
					res.setdatasalida(fechaSalid);
				}
				
				/* Aquí hem d'anar a l'històric d'oficines. */
				String textoOficina=null;
				String sentenciaHqlHistOfi="SELECT FHADAGCO FROM BHAGECO WHERE FHACAGCO=? AND FHAFALTA<=? " +
				    "AND ( (FHAFBAJA>= ? AND FHAFBAJA !=0) OR FHAFBAJA = 0)";
				qHist=session.createSQLQuery(sentenciaHqlHistOfi);
				qHist.addScalar("FHADAGCO", Hibernate.STRING);
				qHist.setInteger(0,Integer.valueOf(res.getOficina()));
				qHist.setInteger(1,Integer.valueOf(fechaSalid));
				qHist.setInteger(2,Integer.valueOf(fechaSalid));
				rsHist=qHist.scroll();
				if (rsHist.next()) {
					/* Hem trobat un històric de l'oficina sol·licitada, hem de mostrar-ne el descriptiu. */
					textoOficina=rsHist.getString(0);
				} else {
					textoOficina=rs.getString(6);
					if (textoOficina==null) {
						textoOficina=" ";
					}
				}
				//  Tancam el preparedstatement i resultset de l'històric
				if (rsHist != null)
					rsHist.close();
				
				res.setDescripcionOficina(textoOficina);
				String fechaDocu=String.valueOf(rs.getInteger(7));
				try {
					fechaDocumento=yyyymmdd.parse(fechaDocu);
					res.setdata(ddmmyyyy.format(fechaDocumento));
				} catch (Exception e) {
					res.setdata(fechaDocu);
				}
				
				String fechaVisado=String.valueOf(rs.getInteger(8));
				try {
					fechaDocumento=yyyymmdd.parse(fechaVisado);
					res.setDataVisado(ddmmyyyy.format(fechaDocumento));
				} catch (Exception e) {
					res.setDataVisado(fechaVisado);
				}

				res.sethora(String.valueOf(rs.getInteger(9)));
				res.setDescripcionDestinatario((rs.getString(10)==null)?rs.getString(11):rs.getString(12));
				res.setRegistroAnulado(rs.getString(13));
				res.settipo(( rs.getString(14)==null)?"":rs.getString(14));
				res.settipo(rs.getString(14));
				res.setidioma(rs.getString(15));
				res.setidioex(rs.getString(16));
				res.setentidad1(Helper.convierteEntidad(rs.getString(17),session));
				res.setEntidad1Grabada(rs.getString(17));
				res.setentidad2(rs.getString(18));
				res.setaltres(rs.getString(11));
				res.setbalears(rs.getString(19));
				res.setfora((rs.getString(20)==null)?null:rs.getString(20).trim());
				res.setremitent(String.valueOf(rs.getInteger(21)));
				
				/* Aquí hem d'anar a l'històric d'organismes  */
				String sentenciaHqlHistOrga="SELECT FHXDORGT FROM BHORGAN WHERE FHXCORGA=? AND FHXFALTA<=? " +
				    "AND ( (FHXFBAJA>= ? AND FHXFBAJA !=0) OR FHXFBAJA = 0)";
				qHist=session.createSQLQuery(sentenciaHqlHistOrga);
				qHist.addScalar("FHXDORGT", Hibernate.STRING);
				qHist.setInteger(0,Integer.valueOf(res.getRemitent()));
				qHist.setInteger(1,Integer.valueOf(fechaSalid));
				qHist.setInteger(2,Integer.valueOf(fechaSalid));
				rsHist=qHist.scroll();
				if (rsHist.next()) {
					/* Hem trobat un històric de l'oficina sol·licitada, hem de mostrar-ne el descriptiu. */
					res.setDescripcionOrganismoRemitente(rsHist.getString(0));
				} else {
					res.setDescripcionOrganismoRemitente(rs.getString(22));
					if (res.getDescripcionOrganismoRemitente()==null) {
						res.setDescripcionOrganismoRemitente(" ");
					}
				}
				//  Tancam el preparedstatement i resultset de l'històric
				if (rsHist != null)
					rsHist.close();
			
				res.setDescripcionDocumento((rs.getString(23)==null)?"":rs.getString(23));
				res.setDescripcionIdiomaDocumento(rs.getString(24));
				res.setDestinoGeografico((rs.getString(25)==null)?rs.getString(20):rs.getString(25));
				if (rs.getString(16).equals("1")) {
					res.setIdiomaExtracto("CASTELLA");
					res.setcomentario(((rs.getString(26)==null)?null:rs.getString(26).trim()));;
				} else {
					res.setIdiomaExtracto("CATALA");
					res.setcomentario(((rs.getString(27)==null)?null:rs.getString(27).trim()));
				}

				res.setdisquet((rs.getString(28).equals("0"))?"":rs.getString(28));
				res.setentrada1(String.valueOf(rs.getInteger(29)));
				res.setentrada2(String.valueOf(rs.getInteger(30)));
				res.setCorreo(rs.getString(31));
                res.setEmailRemitent((rs.getString(32)!=null)?rs.getString(32).trim():null);
                res.setLocalitzadorsDocs(regLocDocElect.LeerListaLocalizadoresDocSalida(Integer.parseInt(res.getAnoSalida()),Integer.parseInt(res.getNumeroSalida()),Integer.parseInt(res.getOficina())));   
			}
		} catch (Exception e) {
			log.error("LEER: Error inesperat: "+e.getMessage() );
			
		} finally {
			if (rs!=null) rs.close();
			close(session);
		}
		return res;
	}


    /**
     * Actualitza el registre de sortida
     * @throws EJBException
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
    public ParametrosRegistroSalida actualizar(ParametrosRegistroSalida param) throws EJBException {
    	String datasalida=param.getDataSalida();
    	String hora=param.getHora();
    	String oficina=param.getOficina();
    	String oficinafisica=param.getOficinafisica();
    	String data=param.getData();
    	String tipo=param.getTipo();
    	String idioma=param.getIdioma();
    	String entidad2=param.getEntidad2();
    	String altres=param.getAltres();
    	String balears=param.getBalears();
    	String fora=param.getFora();
    	String entrada1=param.getEntrada1();
    	String entrada2=param.getEntrada2();
    	String remitent=param.getRemitent();
    	String idioex=param.getIdioex();
    	String disquet=param.getDisquet();
    	String comentario=param.getComentario();
    	String usuario=param.getUsuario();
    	String correo=param.getCorreo();
    	String registroAnulado=param.getRegistroAnulado();
    	boolean registroActualizado=param.getActualizado();
    	String motivo=param.getMotivo();
    	String entidad1Nuevo=param.getEntidad1Nuevo();
    	String entidad2Nuevo=param.getEntidad2Nuevo();
    	String altresNuevo=param.getAltresNuevo();
    	String comentarioNuevo=param.getComentarioNuevo();       
    	boolean validado=param.getValidado();
    	Hashtable errores=param.getErrores();
    	String entidadCastellano=param.getEntidadCastellano();    
    	String anoSalida=param.getAnoSalida();
    	String numeroSalida=param.getNumeroSalida();
    	String emailRemitent = param.getEmailRemitent();    
		Session session = getSession();
		SQLQuery ms = null;
	
		if (!validado) {
            param=validar(param);
			validado=param.getValidado();
		}
		if (!validado) {
			throw new EJBException("No s'ha realitzat la validaci\u00f3 de les dades del registre ");
		}
		
		registroActualizado=false;
		try {
			/* Descomponemos el año de la data de entrada, FZAANOEN y preparamos campo
			 FZAFENT en formato aaaammdd */
			int fzaanoe;
			
			fechaTest = dateF.parse(datasalida);
			Calendar cal=Calendar.getInstance();
			cal.setTime(fechaTest);
			
			fzaanoe=Integer.parseInt(anoSalida);
			int fzafent=Helper.convierteStringFechaAIntFecha(datasalida);
			
			/* Recuperamos numero de salida */		
			int fzanume=Integer.parseInt(numeroSalida);
			
			/* Oficina, fzacagc */
			int fzacagc=Integer.parseInt(oficina);
            int off_codi=0;
            try {
            	off_codi=Integer.parseInt(oficinafisica);
            } catch (Exception e) {}
			
			/* Fecha documento en un campo en formato aaaammdd, fzafdoc */
			int fzafdoc=Helper.convierteStringFechaAIntFecha(data);

			
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
			int fzahora=Helper.convierteStringHoraAIntHoraSeg(hora);
			
			/* Numero localizador y año localizador, fzanloc y fzaaloc */
			int fzanloc=Integer.parseInt((entrada1.equals(""))?"0":entrada1);
			int fzaaloc=Integer.parseInt((entrada2.equals(""))?"0":entrada2);
			
			/* Numero de disquette, fzandis */
			int fzandis=Integer.parseInt((disquet.equals(""))?"0":disquet);
			
			/* Actualizamos el numero de disquete */
			if (fzandis>0){Helper.actualizaDisqueteEntrada(session, fzandis, oficina, anoSalida, errores);}
			
			/* Recuperamos la fecha y la hora del sistema, fzafsis(aaaammdd) y fzahsis (hhMMssmm) */
			Date fechaSystem=new Date();
			//DateFormat aaaammdd=new SimpleDateFormat("yyyyMMdd");
			//int fzafsis=Integer.parseInt(aaaammdd.format(fechaSystem));
			
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
					ms=session.createSQLQuery(updateBZNCORR);
					ms.setString(0, correo);
					ms.setString(1, "S");
					ms.setInteger(2,fzacagc);
					ms.setInteger(3,fzaanoe); 
					ms.setInteger(4,fzanume);
					actualizados=ms.executeUpdate();

					if (actualizados==0) {
						/* Generamos alta con el numero de correo */
						ms=session.createSQLQuery(insertBZNCORR);
						ms.setString(0, "S");
						ms.setInteger(1,fzacagc);
						ms.setInteger(2,fzaanoe);
						ms.setInteger(3,fzanume);
						ms.setString(4, correo);
						ms.executeUpdate();

					}
				} else {
					ms=session.createSQLQuery(deleteBZNCORR);
					ms.setString(0, "S");
					ms.setInteger(1,fzacagc);
					ms.setInteger(2,fzaanoe);
					ms.setInteger(3,fzanume);
					ms.executeUpdate();
				}
			}
			
			
            String deleteOfifis="DELETE FROM BZSALOFF WHERE FOSANOEN=? AND FOSNUMEN=? AND FOSCAGCO=?";
            ms=session.createSQLQuery(deleteOfifis);
            ms.setInteger(0,fzaanoe);
            ms.setInteger(1,fzanume);
            ms.setInteger(2,fzacagc);
    		ms.executeUpdate();

    		String insertOfifis="INSERT INTO BZSALOFF (FOSANOEN, FOSNUMEN, FOSCAGCO, OFS_CODI)" +
            "VALUES (?,?,?,?)";
            ms=session.createSQLQuery(insertOfifis);
            ms.setInteger(0,fzaanoe);
            ms.setInteger(1,fzanume);
            ms.setInteger(2,fzacagc);
    		ms.setInteger(3,off_codi);
    		ms.executeUpdate();

			/* Ejecutamos sentencias SQL */
			ms=session.createSQLQuery("UPDATE BZSALIDA SET FZSFDOCU=?, FZSREMIT=?, FZSCONEN=?, FZSCTIPE=?, " +
					"FZSCEDIE=?, FZSENULA=?, FZSPROCE=?, FZSFENTR=?, FZSCTAGG=?, FZSCAGGE=?, FZSCORGA=?, " +
					"FZSCENTI=?, FZSNENTI=?, FZSHORA=?, FZSCIDIO=?, FZSCONE2=?, FZSNLOC=?, FZSALOC=?, FZSNDIS=?, " +
			        "FZSCUSU=?, FZSCIDI=?, EMAILREMITENT  = ? WHERE FZSANOEN=? AND FZSNUMEN=? AND FZSCAGCO=? ");
			ms.setInteger(0,fzafdoc);
			ms.setString(1,(altres.length()>30) ? altres.substring(0,30) : altres); // 30 pos.
			ms.setString(2,(fzacone.length()>160) ? fzacone.substring(0,160) : fzacone); // 160 pos.
			ms.setString(3,(tipo.length()>2) ? tipo.substring(0,1) : tipo);  // 2 pos.
			ms.setString(4,"N");
			ms.setString(5,(registroAnulado.length()>1) ? registroAnulado.substring(0,1) : registroAnulado);
			ms.setString(6,(fzaproce.length()>25) ? fzaproce.substring(0,25) : fzaproce); // 25 pos.
			ms.setInteger(7,fzafent);
			ms.setInteger(8,fzactagg);
			ms.setInteger(9,fzacagge);
			ms.setInteger(10,fzacorg);
			ms.setString(11,(fzacent.length()>7) ? fzacent.substring(0,8) : fzacent); // 7 pos.
			ms.setInteger(12,fzanent);
			ms.setInteger(13,fzahora);
			ms.setInteger(14,fzacidi);
			ms.setString(15,(fzacone2.length()>160) ? fzacone2.substring(0,160) : fzacone2); // 160 pos.
			ms.setInteger(16,fzanloc);
			ms.setInteger(17,fzaaloc);
			ms.setInteger(18,fzandis);
			ms.setString(19,(usuario.toUpperCase().length()>10) ? usuario.toUpperCase().substring(0,10) : usuario.toUpperCase()); // 10 pos.
			ms.setString(20,idioma);
			ms.setString(21,emailRemitent);
			// Clave del fichero
			ms.setInteger(22,fzaanoe);
			ms.setInteger(23,fzanume);
			ms.setInteger(24,fzacagc);
			
			
			// Si hay motivo, generamos objeto Modificacion
			boolean modificado=false;
			if (!motivo.equals("")) {
                RegistroModificadoSalidaFacade regmod = RegistroModificadoSalidaFacadeUtil.getHome().create();
                ParametrosRegistroModificado registroModificado = new ParametrosRegistroModificado();
                
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
					fzacentNuevo=convierteEntidadCastellano(entidad1Nuevo, entidad2Nuevo);
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
				modificado=regmod.generarModificacion(registroModificado, session);
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
                    ValoresFacade valor = ValoresFacadeUtil.getHome().create();
                    remitente=valor.recuperaRemitenteCastellano(fzacent, fzanent+"");
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
	            int horamili=Helper.obtenerHoraSistemaHHmmssSS();
				logLopdBZSALIDA("UPDATE", (usuario.toUpperCase().length()>10) ? usuario.toUpperCase().substring(0,10) : usuario.toUpperCase()
						, fzahsis, horamili, fzanume, fzaanoe, fzacagc);
			} else {
				registroActualizado=false;
				errores.put("","Error inesperat, no s'ha modificat el registre");
				throw new RemoteException("Error inesperat, no s'ha modifcat el registre");
			}	
			session.flush();  
		}catch(HibernateException he){
			// Tratamos las excepciones generadas por Hibernate
			log.error("Error: S'ha generat una Hibernate Exception " + he.getMessage());
			errores.put("","Error inesperat, no s'ha actualitzat el registre"+": "+he.getClass()+"->"+he.getMessage());
			throw new EJBException("Error: S'ha generat una Hibernate Exception. No s'ha desat el registre. " + he.getMessage());
		} catch (Exception ex) {
			log.error("Error inesperat "+ex.getMessage() );
			registroActualizado=false;
			errores.put("","Error inesperat, no s'ha modificat el registre"+": "+ex.getClass()+"->"+ex.getMessage());
			throw new EJBException("Error inesperat, no s'ha modifcat el registre", ex);
		} finally {
			param.setregistroActualizado(registroActualizado);
			close(session);
		}
		return param;
	}
	
	private String convierteEntidadCastellano(String entidadCatalan, String entidad2Nuevo) {
		String eCastellano="";
        ScrollableResults rs = null;
        SQLQuery q1 = null;
        Session session = getSession();
        
		try {
			String sentenciaHql="SELECT FZGCENTI FROM BZENTID WHERE FZGCENT2=? AND FZGNENTI=? AND FZGFBAJA=0";
			q1=session.createSQLQuery(sentenciaHql);
			q1.addScalar("FZGCENTI", Hibernate.STRING);
			q1.setString(0,entidadCatalan);
			q1.setInteger(1,Integer.parseInt(entidad2Nuevo));
			rs=q1.scroll();
			
			if (rs.next()) {
				eCastellano=rs.getString(0);
			} else {
				eCastellano="";
			}
		} catch (Exception e) {
			eCastellano="";
		} finally {
            if (rs!=null) rs.close();
            close(session);
		}
		return eCastellano;
	}
	
	 /**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
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
        Session session = getSession();
	    try {
            LogSalidaLopd log = new LogSalidaLopd(new LogSalidaLopdId( tipusAcces, usuari, Integer.valueOf(data), Integer.valueOf(hora), 
                                     Integer.valueOf(nombreRegistre), Integer.valueOf(any), Integer.valueOf(oficina)));

            session.save(log);
            session.flush();

        } catch (HibernateException he) {
            throw new EJBException(he);
        } finally {
            close(session);
        }

    }

    /**
     * @throws EJBException
     * 
     * @return
     * @ejb.interface-method
     * @ejb.permission unchecked="true" 
     */
    public boolean ExisteListaLocalizadoresDoc(int anoEntrada, int numeroEntrada, int oficina) throws EJBException {
    	LocalitzadorsDocsElectronicsFacade regLocDocElect;
    	boolean rdto = false;
    	try{
    		regLocDocElect = LocalitzadorsDocsElectronicsFacadeUtil.getHome().create();
    		rdto = regLocDocElect.ExisteListaLocalizadoresDocSalida(anoEntrada,numeroEntrada,oficina);
    	}catch(Exception e){
    		throw new  EJBException(e);
    	}
    	return  rdto;
    }
	/**
	* Anula el registro de salida si las validaciones son correctas
	* 
	* @param usuario Identificador del usuario que realiza la acción
	* @param oficina Oficina de salida que queremos comprobar si tiene permisos
	* @param session Session de base de datos
	* 
	* @return verdadero si tiene permiso para modificar, falso en caso contrario.
	* 
    */
    private boolean tienePermisoSalida(String usuario, String oficina, Session session ) throws Exception{
    	boolean rtdo = false;
    	ScrollableResults rs=null;
    	Query q = null;

    	String sentenciaHql=" FROM Autorizacion WHERE id.usuario=? AND id.codigoAutorizacion=? AND id.codigoOficina IN " +
    	"(SELECT codigo FROM Oficina WHERE fechaBaja=0 AND codigo=?)";
    	q=session.createQuery(sentenciaHql);
    	q.setString(0,usuario.toUpperCase());
    	q.setString(1,"AS");
    	q.setInteger(2,Integer.parseInt(oficina));
    	rs=q.scroll();

    	if (rs.next()) {
    		rtdo = true;
    	}

    	return rtdo;
    }

	/**
	* Anula el registro de salida si las validaciones son correctas
	* 
	* @param registro Identificador del registro afectado
	* @param hay_que_anular Si es verdadero anulamos el registro, si es falso quitamos la anulación
	* 
	* @return verdadero si todo va bien, falso en caso contrario.
	* 
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public boolean anular(ParametrosRegistroSalida registro, boolean hay_que_anular) throws EJBException{
    	Session session = getSession();
    	ScrollableResults rs=null;
    	Query q = null;

    	boolean rtdo= false;
    	String estadoAnulacionRegistro = ((hay_que_anular)?"S":"");
    	String usuario = registro.getUsuario();
    	String oficina = registro.getOficina();
    	String numeroRegistro = registro.getNumeroSalida();
    	String anyo = registro.getAnoSalida();

    	try {
    		if ( tienePermisoSalida( usuario,  oficina,  session )){

    			String sentenciaHql= "update Salida " +
    								 "set nula=?" +
    								 "where  ( id.anyo = ?  and id.numero = ? and id.oficina = ?)";
    			q=session.createQuery(sentenciaHql);
    			//Campo a modificar
    			q.setString(0,estadoAnulacionRegistro);
    			//Claves primarias
    			q.setString(1,anyo);
    			q.setString(2,numeroRegistro);
    			q.setString(3,oficina);

    			if(q.executeUpdate()>0) {   			
    				log.debug("Registro de salida:"+registro.getReferenciaRegistro()+((hay_que_anular)?". Anulado correctamente.":". Quitada la anulación correctamente."));
    				rtdo= true;
    			}else{
    				log.debug("Registro de salida:"+registro.getReferenciaRegistro()+" no encontrado.");
    			}

    			session.flush();
    		}else{
    			log.info("INFO: L'usuari " +usuario+" no te permisos per anular registres de l'oficina "+ oficina);
    		}
    	}catch(HibernateException he){
    		// Tratamos las excepciones generadas por Hibernate
    		log.error("Error: S'ha generat una Hibernate Exception " + he.getMessage());
    		throw new EJBException("Error: S'ha generat una Hibernate Exception. No s'ha anulat el registre "+registro.getReferenciaRegistro()+". " + he.getMessage());
    	} catch (Exception ex) {
    		log.error(usuario+": Excepci\u00f3: "+ex.getMessage());
    		throw new EJBException("Error inesperat: No s'ha anulat el registre "+registro.getReferenciaRegistro(), ex);
    	} finally {
    		close(session);
    	}
    	return rtdo;
    }

    
}
