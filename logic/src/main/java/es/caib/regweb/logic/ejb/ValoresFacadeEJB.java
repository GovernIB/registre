/*
 * ValoresBean.java
 *
 * Created on 19 de junio de 2002, 18:56
 */

package es.caib.regweb.logic.ejb;

import javax.ejb.EJBException;
import java.rmi.*;

import java.util.*;
import java.text.*;

import javax.ejb.*;
import javax.servlet.http.*;

import org.apache.log4j.Logger;

import es.caib.regweb.model.BloqueoDisquete;
import es.caib.regweb.model.BloqueoDisqueteId;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.ScrollableResults;
import org.hibernate.ScrollMode;

/**
 * SessionBean per a obtenció de valors
 *
 * @ejb.bean
 *  name="logic/ValoresFacade"
 *  jndi-name="es.caib.regweb.logic.ValoresFacade"
 *  type="Stateless"
 *  view-type="remote"
 *  transaction-type="Container"
 *
 */
public abstract class ValoresFacadeEJB extends HibernateEJB {
	
	private static final long serialVersionUID = 2L;

	 String numeroRegistreVirtual = es.caib.regweb.logic.helper.Conf.get("numeroRegistreVirtual", "99");

	 boolean registreVirtualActiu = es.caib.regweb.logic.helper.Conf.get("registreVirtualActiu", "false").equalsIgnoreCase("true");

    private Logger log = Logger.getLogger(this.getClass());

    

	/** Creates a new instance of Valores */

    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public ValoresFacadeEJB() {
	}
	


    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public boolean bloquearDisquete(String oficina, String tipo, String fEntrada, String usuario) throws RemoteException  {
		
		Session session = getSession();
		ScrollableResults rs=null;
		
		
		boolean bloqueado=false;
		
		try {
			String sentenciaHql="select usuario from BloqueoDisquete where id.codigoEntradaSalida=? and id.anyoEntradaSalida=? and id.oficinaEntradaSalida=? "; // CHANGED quitado "with rs" para compatibilidad
			DateFormat dateF= new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date fechaTest=dateF.parse(fEntrada);
			Calendar cal=Calendar.getInstance();
			cal.setTime(fechaTest);
			
			
			Query query=session.createQuery(sentenciaHql);
			query.setString(0, tipo);
			query.setInteger(1, cal.get(Calendar.YEAR));
			query.setInteger(2, Integer.valueOf(oficina));
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			
			if  (rs.next()) {
				if ((rs.getString(0)==null) || (rs.getString(0).trim().equals(""))) {
					sentenciaHql="update BloqueoDisquete set usuario=? where id.codigoEntradaSalida=? and id.anyoEntradaSalida=? and id.oficinaEntradaSalida=?";
					Query q = session.createQuery(sentenciaHql);
					q.setString(0, usuario);
					q.setString(1, tipo);
					q.setInteger(2, cal.get(Calendar.YEAR));
					q.setInteger(3, Integer.valueOf(oficina));
					int cualquiera=q.executeUpdate();
					bloqueado=true;
				} else {
					bloqueado=false;
				}
			} else {

				BloqueoDisquete bloq = new BloqueoDisquete(new BloqueoDisqueteId(tipo, cal.get(Calendar.YEAR), Integer.valueOf(oficina)), usuario);
				session.save(bloq);
				
				bloqueado=true;
			}
			session.flush();
        } catch (Exception e) {
            bloqueado=false;
            log.error("bloquearDisquete ERROR: ");
            e.printStackTrace();
        } finally {
            close(session);
        }
		return bloqueado;
	}

	/**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector buscar_060() {
		return buscar_060("", false);
	}


    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector buscar_060(String valor, boolean todos) {
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector municipis=new Vector();
		
		municipis.addElement("000");
		if(valor.equals(""))
			municipis.addElement("Cap entitat seleccionada");
		else
			municipis.addElement(valor);
		
		try {
			
			String sentenciaHql = "select codigoMunicipio, nombreMunicipio from Municipio060 " + 
			((todos)?"":"where fechaBaja is null or fechaBaja = 0 ") +
			"order by nombreMunicipio asc";
			Query query=session.createQuery(sentenciaHql);
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			while (rs.next()) {
				municipis.addElement(rs.getString(0));
				municipis.addElement(rs.getString(1));
			}
			if (municipis.size()==0) {
				municipis=new Vector();
				municipis.addElement("");
				municipis.addElement("No hi ha municipis actius.");
			}
			session.flush();
        } catch (Exception e) {
         municipis.addElement("");
         municipis.addElement("Error en la SELECT");
         log.error("ERROR: ");
        } finally {
            close(session);
        }
		return municipis;
	}
	
	/**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector buscarBaleares() {
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector baleares=new Vector();
		try {
			
			String sentenciaHql="select id.codigo, nombre from AgrupacionGeografica where id.tipo=90 and fechaBaja=0 order by nombre";
			Query query=session.createQuery(sentenciaHql);
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			baleares.addElement("");
			baleares.addElement(" ");
			while (rs.next()) {
				baleares.addElement(rs.getInteger(0));
				baleares.addElement(rs.getString(1));
			}
			if (baleares.size()==1) {
				baleares.addElement("");
				baleares.addElement("No hi ha procendencies de Balears");
			}
			session.flush();
        } catch (Exception e) {
         baleares.addElement("");
         baleares.addElement("Error en la SELECT");
         log.error("ERROR: ");
        } finally {
            close(session);
        }
		return baleares;
	}


    /**
    * Devuelve la lista de organismos vinculados con una oficina
    * 
    * @param codigoOficina Codigo de la oficina de registro
    * 
    * @return Vector de Strings. Por cada organismo añade un string con su código, otro string con nombre corto y otro con su nombre largo. 
    * 		  En el caso de no encontrar nada envía el vector con tres strings: <"&nbsp;","No hi ha Organismes","&nbsp;">
    * 
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector buscarDestinatarios(String codigoOficina) {
		Vector destino=new Vector();
		Session session = getSession();
		ScrollableResults rs=null;
		
		try {
			
			String sentenciaHql="select codigo, nombreCorto, nombreLargo from Organismo where fechaBaja=0 and codigo in (select id.organismo.codigo from OficinaOrganismo where id.oficina.codigo=?) order by codigo";
			Query query=session.createQuery(sentenciaHql);
			query.setInteger(0,Integer.valueOf(codigoOficina));
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			while (rs.next()) {
				destino.addElement(rs.getInteger(0).toString());
				destino.addElement(rs.getString(1));
				destino.addElement(rs.getString(2));
			}
			if (destino.size()==0) {
				destino.addElement("&nbsp;");
				destino.addElement("No hi ha Organismes");
				destino.addElement("&nbsp;");
			}
			session.flush();
        } catch (Exception e) {
         destino.addElement("&nbsp;");
         destino.addElement("Error en la SELECT");
         destino.addElement("&nbsp;");
        } finally {
            close(session);
        }
		return destino;
	}


	/**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public String buscarDisquete(String oficina, String tipo, String fEntrada, String usuario, HttpSession httpSession) throws RemoteException {
		Session session = getSession();
		ScrollableResults rs=null;
		
		String disquete="";
		String sentenciaHql="select numero from Disquete where id.codigoEntradaSalida=? and id.oficinaEntradaSalida=? and id.anyoEntradaSalida=?";
		
		try {
			DateFormat dateF= new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date fechaTest=dateF.parse(fEntrada);
			Calendar cal=Calendar.getInstance();
			cal.setTime(fechaTest);
			
			// Averiguamos si esta bloqueado el numero de disquete
			if (estaBloqueado(oficina, tipo, fEntrada, usuario)) {
				disquete="Numero de disquet no disponible";
			} else {
				if (bloquearDisquete(oficina, tipo, fEntrada, usuario)) {
					httpSession.setAttribute("bloqueoOficina", oficina);
					httpSession.setAttribute("bloqueoTipo", tipo);
					httpSession.setAttribute("bloqueoAnyo", String.valueOf(cal.get(Calendar.YEAR)));
					httpSession.setAttribute("bloqueoUsuario", usuario);
					
					Query query=session.createQuery(sentenciaHql);
					query.setString(0, tipo);
					query.setInteger(1, Integer.valueOf(oficina));
					query.setInteger(2, cal.get(Calendar.YEAR));
					rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
					if  (rs.next()) {
						disquete=rs.getInteger(0).toString();
						httpSession.setAttribute("bloqueoDisquete", disquete);
					}
				} else {
					disquete="Numero de disquet no disponible";
				}
			}
			session.flush();
        } catch (Exception e) {
            disquete="Error en la SELECT";
            log.error("ERROR: ");
            e.printStackTrace();
        } finally {
            close(session);
        }
		return disquete;
	}


    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector buscarDocumentos() {
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector documentos=new Vector();
		try {
			
			String sentenciaHql="select id.codigo, nombre from TipoDocumento where id.fechaBaja=0 and id.codigo<>'DU' order by id.codigo";
			Query query=session.createQuery(sentenciaHql);
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			documentos.addElement("");
			documentos.addElement("");
			while (rs.next()) {
				documentos.addElement(rs.getString(0));
				documentos.addElement(rs.getString(0)+"-"+rs.getString(1));
			}
			if (documentos.size()==0) {
				documentos.addElement("");
				documentos.addElement("No hi ha tipus de documents");
			}
			session.flush();
        } catch (Exception e) {
         documentos.addElement("");
         documentos.addElement("Error en la SELECT");
         log.error("ERROR: ");
        } finally {
            close(session);
        }
		return documentos;
	}


    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector buscarIdiomas() {
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector idiomas=new Vector();
		try {
			
			String sentenciaHql="select codigo, nombre from Idioma";
			Query query=session.createQuery(sentenciaHql);
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			while (rs.next()) {
				idiomas.addElement(rs.getString(0));
				idiomas.addElement(rs.getString(1));
			}
			if (idiomas.size()==0) {
				idiomas.addElement("");
				idiomas.addElement("No hi ha idiomes");
			}
			session.flush();
        } catch (Exception e) {
         idiomas.addElement("");
         idiomas.addElement("Error en la SELECT");
         log.error("ERROR: ");
        } finally {
            close(session);
        }
		return idiomas;
	}


    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector buscarModelos(String usuario, String autorizacion) {
		Session session = getSession();
		ScrollableResults rs=null;
		
		usuario=usuario.toUpperCase();
		Vector models=new Vector();
		
		if ( usuario.equalsIgnoreCase("tots") && autorizacion.equalsIgnoreCase("totes")) {
			models=getModels();
		}
		return models;
	}


    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector buscarModelosRecibos(String usuario, String autorizacion) {
		Session session = getSession();
		ScrollableResults rs=null;
		
		usuario=usuario.toUpperCase();
		Vector models=new Vector();
		
		if ( usuario.equalsIgnoreCase("tots") && autorizacion.equalsIgnoreCase("totes")) {
			models=getModelsRebuts();
		}
		return models;
	}

    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector buscarNoRemision(String oficina) {
		Vector destino=new Vector();
		Session session = getSession();
		ScrollableResults rs=null;
		
		try {
			
			String sentenciaHql="select codigo, nombreCorto, nombreLargo from Organismo where fechaBaja=0 and codigo in (select id.organismo.codigo from OficinaOrganismoNoRemision where id.oficina.codigo=?) order by codigo";
			Query query=session.createQuery(sentenciaHql);
			query.setInteger(0,Integer.valueOf(oficina));
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			while (rs.next()) {
				destino.addElement(rs.getInteger(0).toString());
				destino.addElement(rs.getString(1));
				destino.addElement(rs.getString(2));
			}
			if (destino.size()==0) {
				destino.addElement("&nbsp;");
				destino.addElement("No hi ha Organismes");
				destino.addElement("&nbsp;");
			}
			session.flush();
        } catch (Exception e) {
         destino.addElement("&nbsp;");
         destino.addElement("Error en la SELECT");
         destino.addElement("&nbsp;");
        } finally {
            close(session);
        }
		return destino;
	}


    /**
     * Consula de las oficinas ACTIVAS que un usuario deteminado puede acceder según un tipo de autorización
     * 
     * @param usuario Usuario que realiza la consulta
     * @param autorizacion Tipo de permiso sobre la oficina
     * 
     * @return Vector con las oficinas asignadas 
     * 
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector buscarOficinas(String usuario, String autorizacion) {
		return this.buscarOficinas(usuario,autorizacion,true);
	}
	
    /**
     * Consula de las oficinas ACTIVAS que un usuario deteminado puede acceder según un tipo de autorización
     * 
     * @param usuario Usuario que realiza la consulta
     * @param autorizacion Tipo de permiso sobre la oficina
     * @param soloActivas Indicamos si solo hay que mostrar las oficinas activas o todas
     * 
     * @return Vector con las oficinas asignadas 
     * 
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector buscarOficinas(String usuario, String autorizacion, boolean soloActivas) {
		Session session = getSession();
		ScrollableResults rs=null;
		Vector oficinas=new Vector();
		
		usuario=usuario.toUpperCase();
		
		if ( usuario.equalsIgnoreCase("tots") && autorizacion.equalsIgnoreCase("totes")) {
			oficinas=getOficines();
		} else {
			
			try {
				
				String sentenciaHql="select codigo, nombre from Oficina " + 
				                    " where codigo in (select id.codigoOficina from Autorizacion where id.usuario=? and id.codigoAutorizacion=?) " +
				                    ((soloActivas)?" and fechaBaja=0 ":"") +
				                    " order by codigo";
				Query query=session.createQuery(sentenciaHql);
				query.setString(0,usuario);
				query.setString(1,autorizacion);
				rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
				
				while (rs.next()) {
					oficinas.addElement(String.valueOf(rs.getInteger(0)));
					oficinas.addElement(rs.getString(1));
				}
				
				if (oficinas.size()==0) {
					oficinas.addElement("");
					oficinas.addElement("No hi ha oficines per a l'usuari: "+usuario);
				}
				session.flush();
            } catch (Exception e) {
             oficinas.addElement("");
             oficinas.addElement("BuscarOficinas Error en la SELECT");
             log.error("ERROR dins buscarOficinas(). Usuari "+usuario,e);
            } finally {
                close(session);
            }
		}
		return oficinas;
	}


    /**
     * Devuelve la lista de oficinas físicas en las que un usuario puede acceder con una determinda autorización
     * para realizar una serie de acciones (alta de una entrada, alta de una salida ...)
     * 
     * @param codigoUsuario Codigo de usuario
     * @param autorizacion valores( AE -Alta entrada-, CE -Consulta entrada-, AS -Alta salida-, CS -Consulta salida-)
     * 
     * @return lista de string. Por cada oficina física añade tres strings: codigo oficina, codigo oficina física y nombre de la oficina.
     * 		   Si no encuentra nada, devuleve vector con dos strings: <"","","No hi ha oficines per a l'usuari: "+codigoUsuario>
     * 
    *
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector buscarOficinasFisicas(String codigoUsuario, String autorizacion) {
		Session session = getSession();
		ScrollableResults rs=null;
		
		codigoUsuario=codigoUsuario.toUpperCase();
		Vector oficinas=new Vector();
		
		if ( codigoUsuario.equalsIgnoreCase("tots") && autorizacion.equalsIgnoreCase("totes")) {
			oficinas=getOficinesFisiques();
		} else {
			
			try {
				
				String sentenciaHql="select id.oficina.codigo, id.codigoOficinaFisica, nombre " +
                        " from OficinaFisica where fechaBaja=0 and id.oficina.codigo " +
				        " in (select id.codigoOficina from Autorizacion where id.usuario=? and id.codigoAutorizacion=?) order by id.oficina.codigo,id.codigoOficinaFisica";
				Query query=session.createQuery(sentenciaHql);
				query.setString(0,codigoUsuario);
				query.setString(1,autorizacion);
				rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
				
				log.debug("Registre Virtual Actiu="+this.registreVirtualActiu+"; Codi Oficina Virtual="+this.numeroRegistreVirtual);
				
				while (rs.next()) {
					String codiOficina = String.valueOf(rs.getInteger(0));
					String codiOficinaFisica = String.valueOf(rs.getInteger(1));
					String codiOficinaFisicaVirtualEsperada = codiOficina+this.numeroRegistreVirtual;
					
					//Controlamos que la oficina física no sea una virtual y que las oficinas físicas virtuales no están activas
					if (!(this.registreVirtualActiu && 
							codiOficinaFisica.equalsIgnoreCase(codiOficinaFisicaVirtualEsperada)
							)){
					oficinas.addElement(codiOficina);
					oficinas.addElement(codiOficinaFisica);
					oficinas.addElement(rs.getString(2));
				}
				}
				
				if (oficinas.size()==0) {
					oficinas.addElement("");
					oficinas.addElement("");
					oficinas.addElement("No hi ha oficines per a l'usuari: "+codigoUsuario);
				}
				session.flush();
            } catch (Exception e) {
             oficinas.addElement("");
             oficinas.addElement("");
             oficinas.addElement("BuscarOficinas Error en la SELECT");
             log.error("ERROR: "+codigoUsuario);
            } finally {
                close(session);
            }
		}
		return oficinas;
	}
	
    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector buscarOficinasFisicasDescripcion(String usuario, String autorizacion) {
		Session session = getSession();
		ScrollableResults rs=null;
		
		usuario=usuario.toUpperCase();
		Vector oficinas=new Vector();
		
		if ( usuario.equalsIgnoreCase("tots") && autorizacion.equalsIgnoreCase("totes")) {
			oficinas=getOficinesFisiquesDescripcion();
		} else {
			
			try {
				
				String sentenciaHql="select id.oficina.codigo, id.codigoOficinaFisica, nombre, id.oficina.nombre " +
                        " from OficinaFisica  where fechaBaja=0 and id.oficina.codigo " +
				        " in (select id.codigoOficina from Autorizacion where id.usuario=? and id.codigoAutorizacion=?) order by id.oficina.codigo,id.codigoOficinaFisica";
				Query query=session.createQuery(sentenciaHql);
				query.setString(0,usuario);
				query.setString(1,autorizacion);
				rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
				
				while (rs.next()) {
					oficinas.addElement(String.valueOf(rs.getInteger(0)));
					oficinas.addElement(String.valueOf(rs.getInteger(1)));
					oficinas.addElement(rs.getString(2));
					oficinas.addElement(rs.getString(3));
				}
				
				if (oficinas.size()==0) {
					oficinas.addElement("");
					oficinas.addElement("");
					oficinas.addElement("No hi ha oficines per a l'usuari: "+usuario);
					oficinas.addElement("");
				}
				session.flush();
            } catch (Exception e) {
             oficinas.addElement("");
             oficinas.addElement("");
             oficinas.addElement("BuscarOficinas Error en la SELECT");
             log.error("ERROR: "+usuario);
            } finally {
                close(session);
            }
		}
		return oficinas;
	}

    /**
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
     public Vector buscarOrganimosEmail(String oficina) {
 		Vector destino=new Vector();
 		Session session = getSession();
 		ScrollableResults rs=null;
 		
 		try {
 			//log.debug("Buscando organismos para la oficina:"+oficina);
 			String sentenciaHql="select codigo, nombreCorto, nombreLargo from Organismo where fechaBaja=0 and codigo in (select id.organismo from OficinaOrganismoPermetEnviarEmail where id.oficina=?) order by codigo";
 			Query query=session.createQuery(sentenciaHql);
 			query.setInteger(0,Integer.valueOf(oficina));
 			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
 			while (rs.next()) {
 				destino.addElement(rs.getInteger(0).toString());
 				destino.addElement(rs.getString(1));
 				destino.addElement(rs.getString(2));
 				//log.debug("Encontrado organismo:"+rs.getInteger(0)+"-"+rs.getString(1));
 			}
 			if (destino.size()==0) {
 				destino.addElement("&nbsp;");
 				destino.addElement("No hi ha Organismes");
 				destino.addElement("&nbsp;");
 			}
 			session.flush();
         } catch (Exception e) {
	          destino.addElement("&nbsp;");
	          destino.addElement("Error en la SELECT");
	          destino.addElement("&nbsp;");
	          log.error("Error en buscarOrganimosEmail",e);
         } finally {
             close(session);
         }
 		return destino;
 	}

    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector buscarRemitentes(String subcadenaCodigo, String subcadenaTexto) {
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector remitentes=new Vector();
		String sentenciaHql="";
		try {
			
			sentenciaHql="select codigoCatalan, id.numero, nombreCatalan from EntidadRemitente where id.fechaBaja=0" +
			((!subcadenaTexto.trim().equals("") || !subcadenaCodigo.trim().equals("")) ? " and " : "") +
			((!subcadenaTexto.trim().equals("") && !subcadenaCodigo.trim().equals("")) ? "(" : "") +
			((!subcadenaTexto.trim().equals("")) ? "upper(nombreCatalan) like ?" : "") +
			((!subcadenaTexto.trim().equals("") && !subcadenaCodigo.trim().equals("")) ? " or " : "") +
			((!subcadenaCodigo.trim().equals("") && subcadenaCodigo.trim().length()!=6) ? "codigoCatalan like ?" : "") +
			((!subcadenaCodigo.trim().equals("") && subcadenaCodigo.trim().length()==6) ? "codigoCatalan like ? or codigoCatalan like ?" : "") +
			((!subcadenaTexto.trim().equals("") && !subcadenaCodigo.trim().equals("")) ? " )" : "") +
			" order by codigoCatalan, id.numero ";
			Query query=session.createQuery(sentenciaHql);
			int contador=0;
			if (!subcadenaTexto.trim().equals("")) {
				query.setString(contador++, "%"+subcadenaTexto.toUpperCase()+"%");
			}
			if (!subcadenaCodigo.trim().equals("")) {
				if (subcadenaCodigo.length()==7) {
					query.setString(contador++, subcadenaCodigo.toUpperCase());
				} else if (subcadenaCodigo.length()==6) {
					query.setString(contador++, subcadenaCodigo.toUpperCase()+"%");
					query.setString(contador++, subcadenaCodigo.toUpperCase());
				} else{
					query.setString(contador++, subcadenaCodigo.toUpperCase()+"%");
				}
				
			}
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			while (rs.next()) {
				remitentes.addElement(rs.getString(0));
				remitentes.addElement(rs.getInteger(1).toString());
				remitentes.addElement(rs.getString(2));
			}
			if (remitentes.size()==0) {
				remitentes.addElement("&nbsp;");
				remitentes.addElement("&nbsp;");
				remitentes.addElement("No hi ha Remitents");
			}
			session.flush();
        } catch (Exception e) {
         remitentes.addElement("&nbsp;");
         remitentes.addElement("&nbsp;");
         remitentes.addElement("Error en la SELECT");
         log.error("ERROR: ");
        } finally {
            close(session);
        }
		return remitentes;
	}
     
    /**
     * Devuelve la lista de todos los organismos activos de la aplicación
     * 
     * 
     * @return Vector de Strings. Por cada organismo añade un string con su código, otro string con nombre corto y otro con su nombre largo. 
     * 		  En el caso de no encontrar nada envía el vector con tres strings: <"&nbsp;","No hi ha Organismes","&nbsp;">
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
     public Vector buscarTodosDestinatarios() throws EJBException{
 		Vector destino=new Vector();
 		Session session = getSession();
 		ScrollableResults rs=null;
 		
 		try {
 			
 			String sentenciaHql="select codigo, nombreCorto, nombreLargo from Organismo where fechaBaja=0 order by codigo";
 			Query query=session.createQuery(sentenciaHql);
 			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
 			while (rs.next()) {
 				destino.addElement(rs.getInteger(0).toString());
 				destino.addElement(rs.getString(1));
 				destino.addElement(rs.getString(2));
 			}
 			if (destino.size()==0) {
 				destino.addElement("&nbsp;");
 				destino.addElement("No hi ha Organismes");
 				destino.addElement("&nbsp;");
 			}
 			session.flush();
         } catch (Exception e) {
        	 throw new EJBException(e);
         } finally {
             close(session);
         }
 		return destino;
 	}


    /**
    * Devuelve un vector con los datos de las unidades de gesión de la aplicación
    * 
    * @param soloactivas Valor booleano que indica si hay que devolver solo las unidades de gestión activas
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public Vector buscarUnitatsDeGestio(boolean soloactivas) {
    	
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector unitats=new Vector();
		
		try {
			
			String sentenciaHql="select id.codigoOficina, id.codigoUnidad, nombre from UnidadDeGestion "+((soloactivas)?" where actiu='S' ":"")+" order by id.codigoOficina, id.codigoUnidad";
			log.debug("SesentenciaHql: "+sentenciaHql );
			Query query=session.createQuery(sentenciaHql);
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			
			while (rs.next()) {
				unitats.addElement(String.valueOf(rs.getInteger(0)));
				unitats.addElement(String.valueOf(rs.getInteger(1)));
				unitats.addElement(rs.getString(2));
			}
			
			if (unitats.size()==0) {
				unitats.addElement("");
				unitats.addElement("");
				unitats.addElement("No hi ha unitats de gestió!");
			}
			session.flush();
        } catch (Exception e) {
        	unitats.addElement("");
        	unitats.addElement("");
        	unitats.addElement("buscarUnitatsDeGestio Error en la SELECT");
        	log.error(e.getMessage());
        } finally {
            close(session);
        }
		return unitats;
	}

    
    /**
     * Donat una oficina i un organisme, mostra el unitats de gestió a les que pot enviar el justificant del correu electrònic.
     * Si l'oficina es null, 
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
     public Vector buscarUnitatsGestioEmail(String oficina, String organisme) {
 		Vector destino=new Vector();
 		Session session = getSession();
 		ScrollableResults rs=null;
 		
 		try {
 			log.debug("Buscando UG para el organismo:"+organisme);
 			/*String sentenciaHql=" select UnidadDeGestion.id.codigoOficina, UnidadDeGestion.id.codigoUnidad, UnidadDeGestion.nombre, UnidadDeGestion.direccionEmail" +
 			                   " from UnidadDeGestion, OficinaOrganismo "+
 			                   " where  OficinaOrganismo.id.organismo.codigo = ? "+
 			                   "   and  OficinaOrganismo.id.oficina.codigo = UnidadDeGestion.id.codigoOficina" +			                 
 			                   "    and  UnidadDeGestion.actiu = 'S' " +
 			                   " order by UnidadDeGestion.id.codigoOficina,UnidadDeGestion.id.codigoUnidad";*/
			String sentenciaHql= " select id.codigoOficina, id.codigoUnidad, nombre, direccionEmail" +
					             "   from UnidadDeGestion "+
					             " where id.codigoOficina IN (select id.oficina from OficinaOrganismoPermetEnviarEmail where id.organismo = ? " +
					             ((oficina==null)?") ":" and id.oficina = '"+oficina+"') ") +
					             "   and  actiu = 'S' " +
					             " order by id.codigoOficina, id.codigoUnidad";

 			Query query=session.createQuery(sentenciaHql);
 			query.setInteger(0,Integer.valueOf(organisme));
 			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
 			while (rs.next()) {
 				destino.addElement(rs.getInteger(0).toString());
 				destino.addElement(rs.getInteger(1).toString());
 				destino.addElement(rs.getString(2));
 				destino.addElement(rs.getString(3));
 				log.debug("Encontrada UG:"+rs.getInteger(0)+"-"+rs.getInteger(1)+" Nombre:"+rs.getString(2)+" email:"+rs.getString(3));
 			}
 			if (destino.size()==0) {
 				destino.addElement("");
 				destino.addElement("");
 				destino.addElement("No hi ha Unitats de gestió");
 				destino.addElement("&nbsp;");
 			}
 			session.flush();
         } catch (Exception e) {
			  destino.addElement("");
 			  destino.addElement("");
	          destino.addElement("Error en la SELECT");
	          destino.addElement("&nbsp;");
	          log.error("Error en buscarOrganimosEmail",e);
         } finally {
             close(session);
         }
 		return destino;
 	}

    /**
     * Donat un organisme, mostra el unitats de gestió a les que pot enviar el justificant del correu electrònic
     * 
     * @ejb.interface-method
     * @ejb.permission unchecked="true"
     */
     public Vector buscarUnitatsGestioEmail(String organisme) {
 		return buscarUnitatsGestioEmail(null, organisme);
 	}
	
	//    public String buscarDisquete(String oficina) {

    /**
     * @ejb.create-method
     * @ejb.permission unchecked="true"
     */
    public void ejbCreate() throws CreateException {
        super.ejbCreate();
    }
	
	/** Devuelve un String con la descricpion del organismo destinatario
	 *
	 * @param Organismo
	 *
	 * @return String
	 */

    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public boolean estaBloqueado(String oficina, String tipo, String fEntrada, String usuario) throws RemoteException {
		
		Session session = getSession();
		ScrollableResults rs=null;
		
		boolean bloqueado=false;
		
		try {
			String sentenciaHql="select usuario from BloqueoDisquete where id.codigoEntradaSalida=? and id.anyoEntradaSalida=? and id.oficinaEntradaSalida=? "; // CHANGED quitado "with rs" para compatibilidad
			DateFormat dateF= new SimpleDateFormat("dd/MM/yyyy");
			java.util.Date fechaTest=dateF.parse(fEntrada);
			Calendar cal=Calendar.getInstance();
			cal.setTime(fechaTest);
			
			
			Query query=session.createQuery(sentenciaHql);
			query.setString(0, tipo);
			query.setInteger(1, cal.get(Calendar.YEAR));
			query.setInteger(2, Integer.valueOf(oficina));
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			
			if  (rs.next()) {
				if ((rs.getString(0)==null) || (rs.getString(0).trim().equals(""))) {
					bloqueado=false;
				} else {
					bloqueado=true;
				}
			} else {
				bloqueado=false;
			}
			session.flush();
        } catch (Exception e) {
            log.error("estaBloqueado: ERROR: ");
            e.printStackTrace();
        } finally {
            close(session);
        }
		return bloqueado;
	}
	
	/** Devuelve en String la descripcion de la Entidad Remitente
	 *
	 * @param String Entidad1
	 * @param String Entidad2
	 *
	 * @return String
	 */

    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public String getFecha() {
		DateFormat dateF=new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date fechaTest=new java.util.Date();
		return dateF.format(fechaTest);
	}

    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public String getHorasMinutos() {
		DateFormat dateF=new SimpleDateFormat("HH:mm");
		java.util.Date fechaTest=new java.util.Date();
		return dateF.format(fechaTest);
	}
	
	
	/** Devuelve en String la descripcion de la Oficina
	 *
	 * @param String Oficina
	 *
	 * @return String
	 */

    private Vector getModels() {
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector models=new Vector();
		
		try {
			
			String sentenciaHql="select nombre from ModeloOficio order by nombre";
			Query query=session.createQuery(sentenciaHql);
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			
			while (rs.next()) {
				models.addElement(rs.getString(0));
				//log.debug("Afegim: "+String.valueOf(rs.getInteger("codigo"))+" "+rs.getString("nombre"));
			}
			
			session.flush();
        } catch (Exception e) {
         models.addElement("");
         models.addElement("BuscarModels Error en la SELECT");
        } finally {
            close(session);
        }
		return models;
	}
	
	/** Devuelve en String la descripcion de la Oficina Fisica
	 *
	 * @param String Oficina
	 *
	 * @return String
	 */

    private Vector getModelsRebuts() {
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector models=new Vector();
		
		try {
			
			String sentenciaHql="select nombre from ModeloRecibo order by nombre";
			Query query=session.createQuery(sentenciaHql);
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			
			while (rs.next()) {
				models.addElement(rs.getString(0));
				//log.debug("Afegim: "+String.valueOf(rs.getInteger("codigo"))+" "+rs.getString("nombre"));
			}
			
			session.flush();
        } catch (Exception e) {
         models.addElement("");
         models.addElement("BuscarModels Error en la SELECT");
        } finally {
            close(session);
        }
		return models;
	}


    private Vector getOficines() {
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector oficinas=new Vector();
		
		try {
			
			String sentenciaHql="select codigo, nombre from Oficina order by codigo";
			Query query=session.createQuery(sentenciaHql);
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			
			while (rs.next()) {
				oficinas.addElement(String.valueOf(rs.getInteger(0)));
				oficinas.addElement(rs.getString(1));
				//log.debug("Afegim: "+String.valueOf(rs.getInteger("codigo"))+" "+rs.getString("nombre"));
			}
			
			if (oficinas.size()==0) {
				oficinas.addElement("");
				oficinas.addElement("No hi ha oficines!");
			}
			session.flush();
        } catch (Exception e) {
         oficinas.addElement("");
         oficinas.addElement("BuscarOficinas Error en la SELECT");
        } finally {
            close(session);
        }
		return oficinas;
	}


    /**
	 * Listado de todas las oficinas físicas.
	 * 
	 * @return
	 */
	private Vector getOficinesFisiques() {
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector oficinas=new Vector();
		
		try {
			
			String sentenciaHql="select id.oficina.codigo, id.codigoOficinaFisica, nombre from OficinaFisica order by id.oficina.codigo, id.codigoOficinaFisica";
			Query query=session.createQuery(sentenciaHql);
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			
			while (rs.next()) {
				oficinas.addElement(String.valueOf(rs.getInteger(0)));
				oficinas.addElement(rs.getString(1));
				oficinas.addElement(rs.getString(2));
				//log.debug("Afegim: "+String.valueOf(rs.getInteger("codigo"))+" "+rs.getString("nombre"));
			}
			
			if (oficinas.size()==0) {
				oficinas.addElement("");
				oficinas.addElement("");
				oficinas.addElement("No hi ha oficines!");
			}
			session.flush();
        } catch (Exception e) {
         oficinas.addElement("");
         oficinas.addElement("");
         oficinas.addElement("BuscarOficinas Error en la SELECT");
        } finally {
            close(session);
        }
		return oficinas;
	}


    private Vector getOficinesFisiquesDescripcion() {
		Session session = getSession();
		ScrollableResults rs=null;
		
		Vector oficinas=new Vector();
		
		try {
			
			String sentenciaHql="select id.oficina.codigo, id.codigoOficinaFisica, nombre, id.oficina.nombre "+
                    " from OficinaFisica order by id.oficina.codigo, id.codigoOficinaFisica";
			Query query=session.createQuery(sentenciaHql);
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			
			while (rs.next()) {
				oficinas.addElement(String.valueOf(rs.getInteger(0)));
				oficinas.addElement(String.valueOf(rs.getInteger(1)));
				oficinas.addElement(rs.getString(2));
				oficinas.addElement(rs.getString(3));
				//log.debug("Afegim: "+String.valueOf(rs.getInteger("codigo"))+" "+rs.getString("nombre"));
			}
			
			if (oficinas.size()==0) {
				oficinas.addElement("");
				oficinas.addElement("");
				oficinas.addElement("No hi ha oficines!");
				oficinas.addElement("");
			}
			session.flush();
        } catch (Exception e) {
         oficinas.addElement("");
         oficinas.addElement("");
         oficinas.addElement("BuscarOficinasFisicas Error en la SELECT");
         e.printStackTrace();
        } finally {
            close(session);
        }
		return oficinas;
	}


    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public boolean liberarDisquete(String oficina, String tipo, String anyo, String usuario) {
		
		Session session = getSession();
		ScrollableResults rs=null;
		
		boolean liberado=false;
		
		try {
			String sentenciaHql="select usuario from BloqueoDisquete where id.codigoEntradaSalida=? and id.anyoEntradaSalida=? and id.oficinaEntradaSalida=? "; // CHANGED quitado "with rs" para compatibilidad
			
			
			Query query=session.createQuery(sentenciaHql);
			query.setString(0, tipo);
			query.setInteger(1, Integer.valueOf(anyo));
			query.setInteger(2, Integer.valueOf(oficina));
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			
			if  (rs.next()) {
				if ((rs.getString(0)!=null) || (rs.getString(0).toUpperCase().equals(usuario))) {
					sentenciaHql="update BloqueoDisquete set usuario='' where id.codigoEntradaSalida=? and id.anyoEntradaSalida=? and id.oficinaEntradaSalida=?";
    				Query q = session.createQuery(sentenciaHql);
					q.setString(0,tipo);
					q.setInteger(1, Integer.valueOf(anyo));
					q.setInteger(2, Integer.valueOf(oficina));
    				int cualquiera=q.executeUpdate();
					liberado=true;
				} else {
					liberado=false;
				}
			} else {
				liberado=false;
			}
			session.flush();
		} catch (NumberFormatException e) {
			liberado=false;
	        //log.error("liberarDisquete ERROR: ",e);
        } catch (Exception e) {
         liberado=false;
         log.error("liberarDisquete ERROR: ",e);
        } finally {
            close(session);
        }
		return liberado;
	}
	

	/**
      * Comprueba si la oficina de registro puede enviar correos al organismo destinatario.
      * 
      * @param codigoOficina Código de la oficina donde se ha realizado el registro
      * @param codigoOrganismo Código del organismo destinatario del registro
      * @ejb.interface-method
      * @ejb.permission unchecked="true"
      */
      public boolean permiteEnviarEmailAlOrganismo(int codigoOficina, int codigoOrganismo) {
      	
  		Session session = getSession();
  		ScrollableResults rs=null;
  		boolean rtdo=false;
  		
  		try {
  			
  			String sentenciaHql="Select 1 "+
  								 " from  OficinaOrganismoPermetEnviarEmail " +
  								 " where id.oficina="+codigoOficina+
  								 "   and id.organismo="+codigoOrganismo;

  			Query query=session.createQuery(sentenciaHql);
  			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
  			
  			if (rs.next()) {
  				rtdo=true;
  			}

          } catch (Exception e) {
          	log.error(e.getMessage());
          } finally {
              close(session);
          }
          log.debug("PermiteEnviarEmail rtdo:"+rtdo);
  		return rtdo;
  	}


    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public String recuperaDescripcionOficina(String oficina) {
		Session session = getSession();
		ScrollableResults rs=null;
		
		String descripcionOficina=null;
		try {
			String sentenciaHql="select nombre from Oficina where codigo=? ";
			Query query=session.createQuery(sentenciaHql);
			query.setInteger(0,Integer.parseInt(oficina));
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			
			if (rs.next()) {
				descripcionOficina=rs.getString(0);
			} else {
				descripcionOficina="Oficina inexistente";
			}
			session.flush();
        } catch (Exception e) {
            descripcionOficina="Oficina inexistente";
        } finally {
            close(session);
        }
		return descripcionOficina;
	}


	/**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public String recuperaDescripcionOficinaFisica(String oficina, String oficinafisica) {
		Session session = getSession();
		ScrollableResults rs=null;
		
		String descripcionOficina=null;
		try {

			String sentenciaHql="select nombre from OficinaFisica where id.oficina.codigo=? and id.codigoOficinaFisica=? ";
			Query query=session.createQuery(sentenciaHql);
			query.setInteger(0,Integer.parseInt(oficina));
			query.setInteger(1,Integer.parseInt(oficinafisica));
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			
			if (rs.next()) {
				descripcionOficina=rs.getString(0);
			} else {
				descripcionOficina="Oficina inexistente";
			}
			session.flush();
        } catch (Exception e) {
         descripcionOficina="Oficina inexistente";
         log.error("ERROR: ");
        } finally {
            close(session);
        }
		return descripcionOficina;
	}


    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public String recuperarDestinatario(String organismo) {
		Session session = getSession();
		ScrollableResults rs=null;
		
		String descripcionDestinatario=null;
		if (organismo != null && !"".equals(organismo) ) {
			// Només anam a la BBDD si ens passen algun organisme.
			try {
				
				String sentenciaHql="select nombreLargo from Organismo where codigo=? ";
				Query query=session.createQuery(sentenciaHql);
				query.setInteger(0,Integer.valueOf(organismo));
				rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
				if (rs.next()) {
					/* IMPORTANT: La descripció del destinatari està a faxdorgT , nombreCorto és el text curt! SMR: 7/2/2007*/
					descripcionDestinatario=rs.getString(0);
				} else {
					descripcionDestinatario="Destinatari inexistent";
				}
				session.flush();
            } catch (Exception e) {
             descripcionDestinatario="Destinatari inexistent";
             if (organismo!=null)
                 log.error("recuperarDestinatario ERROR: Destinatari de l'organisme "+organismo+" inexistent ");
             else
                 log.error("recuperarDestinatario ERROR: Destinatari de l'organisme NULL inexistent ");
            } finally {
                close(session);
            }
			
		} else {
			descripcionDestinatario="Destinatari inexistent";
		}
		return descripcionDestinatario;
	}
	



    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public String recuperaRemitente(String entidad1, String entidad2) {
		Session session = getSession();
		ScrollableResults rs=null;
		
		String descripcionRemitente=null;
		
		if (entidad2.equals("")) {
			entidad2="0";
		}
		try {
			
			String sentenciaHql="select nombreCatalan from EntidadRemitente where codigoCatalan=? and id.numero=? and id.fechaBaja=0";
			Query query=session.createQuery(sentenciaHql);
			query.setString(0,entidad1.toUpperCase());
			query.setInteger(1,Integer.parseInt(entidad2));
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			if (rs.next()) {
				descripcionRemitente=rs.getString(0);
			} else {
				descripcionRemitente="Remitent inexistent";
			}
			session.flush();
        } catch (Exception e) {
         descripcionRemitente="Remitent inexistent";
         if (entidad1!=null && entidad2!=null)
             log.error("ERROR: En recuperar el remitent de l'entitat "+entidad1+"-"+entidad2+".");
         else
             log.error("ERROR: En recuperar el remitent de l'entitat NULL-NULL inexistent.");
        } finally {
            close(session);
        }
		return descripcionRemitente;
	}
    
    /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public String recuperaRemitenteCastellano(String entidad1, String entidad2) {
		Session session = getSession();
		ScrollableResults rs=null;
		
		String descripcionRemitente=null;
		
		if (entidad2.equals("")) {
			entidad2="0";
		}
		try {
			
			String sentenciaHql="select nombre from EntidadRemitente where id.codigo=? and id.numero=? and id.fechaBaja=0";
			Query query=session.createQuery(sentenciaHql);
			query.setString(0,entidad1.toUpperCase());
			query.setInteger(1,Integer.parseInt(entidad2));
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			if (rs.next()) {
				descripcionRemitente=rs.getString(0);
			} else {
				descripcionRemitente="Remitent inexistent";
			}
			session.flush();
        } catch (Exception e) {
         descripcionRemitente="Remitent inexistent";
         if (entidad1!=null && entidad2!=null)
             log.error("ERROR: En recuperar el remitent de l'entitat "+entidad1+"-"+entidad2+".");
         else
             log.error("ERROR: En recuperar el remitent de l'entitat NULL-NULL inexistent.");
        } finally {
            close(session);
        }
		return descripcionRemitente;
	}
    
     /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public String recuperarTipoDocumento(String tipo) {
		Session session = getSession();
		ScrollableResults rs=null;
		
		String descripcionDocumento="";
		try {
			
			String sentenciaHql="select id.codigo, nombre  from TipoDocumento where id.fechaBaja=0 and id.codigo=?";
			Query query=session.createQuery(sentenciaHql);
			query.setString(0, tipo);
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			if (rs.next()) {
				descripcionDocumento=rs.getString(0)+" - "+rs.getString(1);
			} else {
				descripcionDocumento=tipo+" - "+"Error en la SELECT";
			}
			session.flush();
        } catch (Exception e) {
         descripcionDocumento=tipo+" - "+"Error en la SELECT";
         log.error("ERROR: ");
        } finally {
            close(session);
        }
		return descripcionDocumento;
	}
      
      
	 /**
    * @ejb.interface-method
    * @ejb.permission unchecked="true"
    */
    public boolean usuarioAutorizadoVisar(String usuario, String programa) {
		boolean estaAutorizado=false;
		Session session = getSession();
		ScrollableResults rs=null;
		
		usuario = usuario.toUpperCase();
		try {
			
			String sentenciaHql=" from Autorizacion where id.usuario=? and id.codigoAutorizacion=? ";
			Query query=session.createQuery(sentenciaHql);
			query.setString(0,usuario);
			query.setString(1,programa);
			rs = query.scroll(ScrollMode.SCROLL_INSENSITIVE);
			if (rs.next()) {
				estaAutorizado=true;
			} else {
				estaAutorizado=false;
			}
			session.flush();
        } catch (Exception e) {
         estaAutorizado=false;
         log.debug("UsuarioAutorizadoVisar ERROR: "+usuario);
        } finally {
            close(session);
        }
		return estaAutorizado;
	}

}