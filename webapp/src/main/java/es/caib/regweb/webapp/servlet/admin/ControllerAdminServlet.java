

/*
 * Created on 1-des-2009
 *
 */
package es.caib.regweb.webapp.servlet.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Vector;
import java.util.TreeMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import es.caib.regweb.logic.helper.AgrupacioGeograficaData;
import es.caib.regweb.logic.helper.AutoritzacionsOficinaData;
import es.caib.regweb.logic.helper.AutoritzacionsUsuariData;
import es.caib.regweb.logic.helper.Municipi060Data;
import es.caib.regweb.logic.helper.TipusDocumentData;
import es.caib.regweb.logic.helper.EntitatData;
import org.apache.log4j.Logger;

import es.caib.regweb.webapp.servlet.UtilWebServlet;
import es.caib.regweb.logic.interfaces.AdminFacade;
import es.caib.regweb.logic.util.AdminFacadeUtil;
import es.caib.regweb.logic.interfaces.ValoresFacade;
import es.caib.regweb.logic.util.ValoresFacadeUtil;

/**
 * Servlet Class
 *
 * 
 * @web.security-role-ref 	role-name = "RWE_ADMIN"
 * 							role-link = "RWE_ADMIN"
 *
 */
public class ControllerAdminServlet extends UtilWebServlet {
	
	/**
	 * 
	 */
	private Logger log = Logger.getLogger(this.getClass());
	
	public ControllerAdminServlet() {
		super();
	}
	 
	private String agrupacionsgeografiquesAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/agrupacionsgeografiques.jsp");
		String missatge = "";
		String descMissatge = "";
		String mesInfoMissatge = "";
        String valorAccio = ""; //Valor de l'acció a fer (alta, modificació)
        String tipusAgruGeoGestionar="";
        String codiAgruGeoGestionar="";
        String elementFocus = ""; //Element que tendrà el focus de la pàgina.

		try{            

            AdminFacade autUsu = AdminFacadeUtil.getHome().create();

			/* Gestión errores. */
			gestionMensajesError( request, missatge, descMissatge, mesInfoMissatge);

			// Buscamos el parámetro "tipusAgruGeoGestionar"
            tipusAgruGeoGestionar = obtenerParametro( request,"tipusAgruGeoGestionar",false);
			// Buscamos el parámetro "codiAgruGeoGestionar"
            codiAgruGeoGestionar = obtenerParametro( request,"codiAgruGeoGestionar",false);

            //Si l'atribut "init" és "init", buidam anyGestionar per a que ens presenti el formulari inicial.
            String ini= (request.getAttribute("init") != null ? (String) request.getAttribute("init"):"");
            if  ( ini.equals("init") ) {
            	tipusAgruGeoGestionar="";
            }


            /* Cercam l'entitat passada com a paràmetre */
            AgrupacioGeograficaData agruGeografiques = new AgrupacioGeograficaData();
            log.debug("tipusAgruGeoGestionar="+tipusAgruGeoGestionar+" codiAgruGeoGestionar="+codiAgruGeoGestionar);
            if (tipusAgruGeoGestionar!=null && !tipusAgruGeoGestionar.equals("")
            		&& codiAgruGeoGestionar!=null && !codiAgruGeoGestionar.equals("")   ) {
            	agruGeografiques = autUsu.getAgrupacioGeografica(tipusAgruGeoGestionar, codiAgruGeoGestionar );
            	if (agruGeografiques!=null)log.debug(agruGeografiques.toString());
            }

            boolean hayAgrupacion = !tipusAgruGeoGestionar.equals("");
            boolean existeAgrupacion = false;
            if (!hayAgrupacion) {
            	elementFocus="tipusAgruGeoGestionar";
            } else {
                if ( agruGeografiques!=null ) {
        		    // Si l'organisme existeix...	
        			existeAgrupacion = true;		
        			valorAccio="actualitzaAgruGeo";
        			elementFocus="descAgruGeo";
        			agruGeografiques.setDataBaixa("");
                } else {
        			elementFocus="codTipuAgruGeo";
        			valorAccio="altaAgruGeo";
        			//Si la agrupació geogràfica no existeix, l'hem de crear!
        			agruGeografiques = new AgrupacioGeograficaData();
        			agruGeografiques.setCodiTipusAgruGeo(tipusAgruGeoGestionar);
        			agruGeografiques.setCodiAgruGeo(codiAgruGeoGestionar);
        			agruGeografiques.setDescAgruGeo("");
        			agruGeografiques.setDataBaixa("");
        			agruGeografiques.setCodiTipusAgruGeoSuperior("");
        			agruGeografiques.setCodiAgruGeoSuperior("");
        	    }
            }


            request.setAttribute("hayAgrupacion", Boolean.valueOf(hayAgrupacion));
            request.setAttribute("existeAgrupacion", Boolean.valueOf(existeAgrupacion));
            request.setAttribute("codiAgruGeoGestionar", codiAgruGeoGestionar);
            request.setAttribute("tipusAgruGeoGestionar", tipusAgruGeoGestionar);
            request.setAttribute("valorAccio", valorAccio);
            request.setAttribute("elementFocus", elementFocus);
            request.setAttribute("agruGeografiques", agruGeografiques);
            request.setAttribute("missatge", missatge);
        	request.setAttribute("descMissatge", descMissatge);
        	request.setAttribute("mesInfoMissatge", mesInfoMissatge);
        } catch(Exception ex) {
			log.error("Capturam excepci\u00f3 estranya!",ex);
		}    		
		
    	return resultado;
    }
	
	private String altaModelOficiAdmin(HttpServletRequest request, HttpSession sesion, HttpServletResponse response) {
		String resultado = new String("/admin/pages/altaModelOfici.jsp");
		String missatge = "";
		String descMissatge = "";
		String mesInfoMissatge = "";
//        String modelGestionar="";
        String valorAccio = ""; //Valor de l'acció a fer (alta, modificació)
       //Vector descModel = null;
        String elementFocus = ""; //Element que tendrà el focus de la pàgina.

		try{            
			/* Gestión errores. */
			gestionMensajesError( request, missatge, descMissatge, mesInfoMissatge);

			// Buscamos el parámetro "modelGestionar"
//			modelGestionar = obtenerParametro( request,"modelGestionar",false);

            /* Cercam l'organisme */
/*            if (modelGestionar!=null &&!modelGestionar.equals("") ) {
            	descModel = autUsu.getModelOfici( modelGestionar );
            }
*/
   	   	    es.caib.regweb.webapp.servlet.ModelOficioUploader fileuploader=new es.caib.regweb.webapp.servlet.ModelOficioUploader(request, response);

            boolean borrado=false;
            if (fileuploader.getBorrado()) { 
         		borrado=true;
         	}  
            boolean grabado=false;
    		if(fileuploader.getGrabado()){		    
                grabado=true;
    	    }
 
            request.setAttribute("borrado", Boolean.valueOf(borrado));
            request.setAttribute("grabado", Boolean.valueOf(grabado));
            request.setAttribute("valorAccio", valorAccio);
            request.setAttribute("elementFocus", elementFocus);
            request.setAttribute("missatge", missatge);
        	request.setAttribute("descMissatge", descMissatge);
        	request.setAttribute("mesInfoMissatge", mesInfoMissatge);
        } catch (java.sql.SQLException e) {
     	   log.error("Excepció SQL a altaModelOficiAdmin()",e);
        } catch(Exception ex) {
        	log.error("Capturam excepci\u00f3 estranya!",ex);
		}    		
		
    	return resultado;
    }
	
	private String altaModelRebutAdmin(HttpServletRequest request, HttpSession sesion, HttpServletResponse response) {
		String resultado = new String("/admin/pages/altaModelRebut.jsp");
		String missatge = "";
		String descMissatge = "";
		String mesInfoMissatge = "";
        String valorAccio = ""; //Valor de l'acció a fer (alta, modificació)
        String elementFocus = ""; //Element que tendrà el focus de la pàgina.

		try{            
			/* Gestión errores. */
			gestionMensajesError( request, missatge, descMissatge, mesInfoMissatge);

   	   	    es.caib.regweb.webapp.servlet.ModelRebutUploader fileuploader=new es.caib.regweb.webapp.servlet.ModelRebutUploader(request, response);

            boolean borrado=false;
            if (fileuploader.getBorrado()) { 
         		borrado=true;
         	}  
            boolean grabado=false;
    		if(fileuploader.getGrabado()){		    
                grabado=true;
    	    }
 
            request.setAttribute("borrado", Boolean.valueOf(borrado));
            request.setAttribute("grabado", Boolean.valueOf(grabado));
            request.setAttribute("valorAccio", valorAccio);
            request.setAttribute("elementFocus", elementFocus);
            request.setAttribute("missatge", missatge);
        	request.setAttribute("descMissatge", descMissatge);
        	request.setAttribute("mesInfoMissatge", mesInfoMissatge);
        } catch (java.sql.SQLException e) {
     	   log.error("Excepci\u00f3 tractada",e);
        } catch(Exception ex) {
        	log.error("Capturam excepci\u00f3 estranya!",ex);
		}    		
		
    	return resultado;
    }
	
	
	private String autoritzOficinaAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/autoritzOficina.jsp");

		String oficinaConsultar="";
		String missatge = "";
		String descMissatge = "";
		String mesInfoMissatge = "";
		String elementFocus = ""; //Element que tendrà el focus de la pàgina.
		boolean tieneAutorizaciones = true; //Si la oficina tiene usuarios 
		boolean hayOficina = false;
		boolean verPermisosConsulta = false;
		String ofiInput = "";
		AutoritzacionsOficinaData autUsuData = null;

		try{  	
			AdminFacade autUsu = AdminFacadeUtil.getHome().create();
			ValoresFacade valores = ValoresFacadeUtil.getHome().create();

			/* Gestión errores. */
			gestionMensajesError( request, missatge, descMissatge, mesInfoMissatge);
			// Buscamos el parámetro "oficinaGestionar"
			oficinaConsultar = obtenerParametro( request,"oficinaGestionar",true);
			// Comprobamos si el parametro "verPermisosConsulta" esta activo
			verPermisosConsulta = (request.getParameter("verPermisosConsulta")!=null?true:false);
			

			hayOficina = !oficinaConsultar.equals("");

			if (!hayOficina) {
				// Si nos tenemos la oficina a consultar, la preguntamos
				elementFocus="OficinaConsultar";
			} else {
				try{
					autUsuData = autUsu.getUsuarisOficina(oficinaConsultar,verPermisosConsulta);
					oficinaConsultar+=" - "+valores.recuperaDescripcionOficina(oficinaConsultar);

					TreeMap autUsuAE = new TreeMap();
					TreeMap autUsuCE = new TreeMap();
					TreeMap autUsuAS = new TreeMap();
					TreeMap autUsuCS = new TreeMap();
					TreeMap autUsuVE = new TreeMap();
					TreeMap autUsuVS = new TreeMap();
					Vector usuariosOficina = new Vector();
					String codigousuarioStr;

					if (autUsuData!=null) {
						autUsuAE = autUsuData.getAutModifEntrada();
						autUsuCE = autUsuData.getAutConsultaEntrada();
						autUsuAS = autUsuData.getAutModifSortida();
						autUsuCS = autUsuData.getAutConsultaSortida();
						autUsuVE = autUsuData.getAutVisaEntrada();
						autUsuVS = autUsuData.getAutVisaSortida();
						usuariosOficina = autUsuData.getUsuariosDeLaOficina();
					}else{
						//Marcamos que no tiene datos
						tieneAutorizaciones = false;
					}
					String AEchecked="";
					String CEchecked="";
					String ASchecked="";
					String CSchecked="";
					String VEchecked="";
					String VSchecked="";

					for (int i=0;i<usuariosOficina.size();i=i+1){
						AEchecked="";
						CEchecked="";
						ASchecked="";
						CSchecked="";
						VEchecked="";
						VSchecked="";
						
						codigousuarioStr = (String)usuariosOficina.get(i);

						if (autUsuData!=null) {

							if ( autUsuAE.containsKey( codigousuarioStr ))
								AEchecked="checked=\"true\""; 
							if ( autUsuCE.containsKey( codigousuarioStr) ) 
								CEchecked="checked=\"true\""; 
							if ( autUsuAS.containsKey( codigousuarioStr) ) 
								ASchecked="checked=\"true\""; 
							if ( autUsuCS.containsKey( codigousuarioStr) ) 
								CSchecked="checked=\"true\""; 	
							if ( autUsuVE.containsKey( codigousuarioStr) ) 
								VEchecked="checked=\"true\""; 	
							if ( autUsuVS.containsKey( codigousuarioStr) ) 
								VSchecked="checked=\"true\""; 	

						}
						ofiInput = ofiInput+"<tr>\n\t<td>"+codigousuarioStr+"</td>";
						ofiInput = ofiInput+"\n\t<td><input type=\"checkbox\" disabled=\"disabled\" "+AEchecked+" style=\"width: 60px;\"/></td>";
						ofiInput = ofiInput+"\n\t<td><input type=\"checkbox\" disabled=\"disabled\" "+CEchecked+" style=\"width: 60px;\"/></td>";
						ofiInput = ofiInput+"\n\t<td><input type=\"checkbox\" disabled=\"disabled\" "+ASchecked+" style=\"width: 60px;\"/></td>";
						ofiInput = ofiInput+"\n\t<td><input type=\"checkbox\" disabled=\"disabled\" "+CSchecked+" style=\"width: 60px;\"/></td>";
						ofiInput = ofiInput+"\n\t<td><input type=\"checkbox\" disabled=\"disabled\" "+VEchecked+" style=\"width: 60px;\"/></td>";
						ofiInput = ofiInput+"\n\t<td><input type=\"checkbox\" disabled=\"disabled\" "+VSchecked+" style=\"width: 60px;\"/></td>";
						ofiInput = ofiInput+"\n</tr>\n";
					}	

				}catch(RemoteException ex){
					missatge = "Error al llegir els usuaris d'una oficina";
					descMissatge = "Error intern de l'aplicació";
					log.error(missatge, ex);
				}
			}

			request.setAttribute("hayOficina", Boolean.valueOf(hayOficina));
			request.setAttribute("tieneAutorizaciones", Boolean.valueOf(tieneAutorizaciones));
			request.setAttribute("ofiInput", ofiInput);
			request.setAttribute("oficinaConsultar", oficinaConsultar);
			request.setAttribute("elementFocus", elementFocus);
			request.setAttribute("missatge", missatge);
			request.setAttribute("descMissatge", descMissatge);
			request.setAttribute("mesInfoMissatge", mesInfoMissatge);
		} catch(Exception ex) {
			log.error("Error dins autoritzOficinaAdmin",ex);
		}    				
		return resultado;
	}


	private String autoritzUsuariAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/autoritzUsuari.jsp");
		String missatge = "";
		String descMissatge = "";
		String mesInfoMissatge = "";
		AutoritzacionsUsuariData autUsuData = null;
		String usuariAutoritzar="";
		String elementFocus = ""; //Element que tendrà el focus de la pàgina.
		String ofiInput = "";

		try{            
            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
            ValoresFacade valores = ValoresFacadeUtil.getHome().create();

            /* Cercam totes les oficines */
            Vector oficines = valores.buscarOficinas("tots","totes");

			/* Gestión errores. */
			gestionMensajesError( request, missatge, descMissatge, mesInfoMissatge);

			// Buscamos el parámetro "usuariAutoritzar"
            usuariAutoritzar = obtenerParametro( request,"usuariAutoritzar",true);
        
            boolean hayUsuario = !usuariAutoritzar.equals("");
            
    		if (!hayUsuario) {
                elementFocus="usuariAutoritzar";
            } else {
        		autUsuData = autUsu.getAutoritzacionsUsuari(usuariAutoritzar.toUpperCase());
        		TreeMap autUsuAE = new TreeMap();
            	TreeMap autUsuCE = new TreeMap();
            	TreeMap autUsuAS = new TreeMap();
            	TreeMap autUsuCS = new TreeMap();
            	TreeMap autUsuVE = new TreeMap();
            	TreeMap autUsuVS = new TreeMap();
        		if (autUsuData!=null) {
        			autUsuAE = autUsuData.getAutModifEntrada();
            		autUsuCE = autUsuData.getAutConsultaEntrada();
            		autUsuAS = autUsuData.getAutModifSortida();
            		autUsuCS = autUsuData.getAutConsultaSortida();
            		autUsuVE = autUsuData.getAutVisaEntrada();
            		autUsuVS = autUsuData.getAutVisaSortida();
        		}
        		String AEchecked="";
        		String CEchecked="";
        		String ASchecked="";
        		String CSchecked="";
        		String VEchecked="";
        		String VSchecked="";
        		for (int i=0;i<oficines.size();i=i+2){
        			AEchecked="";
        			CEchecked="";
        			ASchecked="";
        			CSchecked="";
        			VEchecked="";
        			VSchecked="";
        			String codigo=oficines.get(i).toString();
        		    String descripcion=oficines.get(i+1).toString();
        			if (autUsuData!=null) {
        				//Si l'usuari no té cap autorització, els treeMaps seran nulls i això petaría.
        				if ( autUsuAE.containsKey( new Integer(oficines.get(i).toString())) ) 
        					AEchecked="checked=\"true\""; 
        				if ( autUsuCE.containsKey( new Integer(oficines.get(i).toString())) ) 
        					CEchecked="checked=\"true\""; 
        				if ( autUsuAS.containsKey( new Integer(oficines.get(i).toString())) ) 
        					ASchecked="checked=\"true\""; 
        				if ( autUsuCS.containsKey( new Integer(oficines.get(i).toString())) ) 
        					CSchecked="checked=\"true\""; 	
        				if ( autUsuVE.containsKey( new Integer(oficines.get(i).toString())) ) 
        					VEchecked="checked=\"true\""; 	
        				if ( autUsuVS.containsKey( new Integer(oficines.get(i).toString())) ) 
        					VSchecked="checked=\"true\""; 	

        			}
        			ofiInput = ofiInput+"<tr>\n\t<td>"+codigo+"-"+descripcion+"</td>";
        		    ofiInput = ofiInput+"\n\t<td><input type=\"checkbox\" name=\"AE\" id=\""+codigo+"AE\" value=\""+codigo+"\" "+AEchecked+" style=\"width: 60px;\"/></td>";
        			ofiInput = ofiInput+"\n\t<td><input type=\"checkbox\" name=\"CE\" id=\""+codigo+"CE\" value=\""+codigo+"\" "+CEchecked+" style=\"width: 60px;\"/></td>";
        			ofiInput = ofiInput+"\n\t<td><input type=\"checkbox\" name=\"AS\" id=\""+codigo+"AS\" value=\""+codigo+"\" "+ASchecked+" style=\"width: 60px;\"/></td>";
        			ofiInput = ofiInput+"\n\t<td><input type=\"checkbox\" name=\"CS\" id=\""+codigo+"CS\" value=\""+codigo+"\" "+CSchecked+" style=\"width: 60px;\"/></td>";
        			ofiInput = ofiInput+"\n\t<td><input type=\"checkbox\" name=\"VE\" id=\""+codigo+"VE\" value=\""+codigo+"\" "+VEchecked+" style=\"width: 60px;\"/></td>";
        			ofiInput = ofiInput+"\n\t<td><input type=\"checkbox\" name=\"VS\" id=\""+codigo+"VS\" value=\""+codigo+"\" "+VSchecked+" style=\"width: 60px;\"/></td>";
        			ofiInput = ofiInput+"\n</tr>\n";
        		}		
            }
            
            boolean tieneAutorizaciones = true;
        	if (autUsuData==null) {
        		tieneAutorizaciones = false;
        	}

            request.setAttribute("hayUsuario", Boolean.valueOf(hayUsuario));
            request.setAttribute("tieneAutorizaciones", Boolean.valueOf(tieneAutorizaciones));
            request.setAttribute("ofiInput", ofiInput);
            request.setAttribute("usuariAutoritzar", usuariAutoritzar);
            request.setAttribute("elementFocus", elementFocus);
            request.setAttribute("missatge", missatge);
        	request.setAttribute("descMissatge", descMissatge);
        	request.setAttribute("mesInfoMissatge", mesInfoMissatge);
        } catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }
	
	private String comptadorsAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/comptadors.jsp");
		String missatge = "";
		String descMissatge = "";
		String mesInfoMissatge = "";
		String anyGestionar="";
        String elementFocus = ""; //Element que tendrà el focus de la pàgina.
		String ofiInput = "";
		
		try{            
            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
            ValoresFacade valores = ValoresFacadeUtil.getHome().create();

            /* Cercam totes les oficines */
            Vector oficines = valores.buscarOficinas("tots","totes");

    		/* Gestión errores. */
    		gestionMensajesError( request, missatge, descMissatge, mesInfoMissatge);

			// Buscamos el parámetro "oficinaGestionar"
            anyGestionar = obtenerParametro( request,"anyGestionar",false);

            //Si l'atribut "init" és "init", buidam anyGestionar per a que ens presenti el formulari inicial.
            //String ini= (request.getAttribute("init") != null ? (String) request.getAttribute("init"):"");
            //if  ( ini.equals("init") )
            //	anyGestionar="";

            boolean hayAny = !anyGestionar.equals("");
            if (!hayAny) {
            	elementFocus="anyGestionar";
            } else {
        		for (int j=0;j<oficines.size();j=j+2){
        			String codigo=oficines.get(j).toString();
        		    String descripcion=oficines.get(j+1).toString();
        			ofiInput = ofiInput+"<tr>\n\t<td>"+codigo+"-"+descripcion+"</td>";
        			ofiInput = ofiInput+"\n\t<td>"+autUsu.getComptadorOficina(codigo,"E",anyGestionar)+"</td>";
        			ofiInput = ofiInput+"\n\t<td>"+autUsu.getComptadorOficina(codigo,"S",anyGestionar)+"</td>";
        			ofiInput = ofiInput+"\n\t<td>"+autUsu.getComptadorOficina(codigo,"O",anyGestionar)+"</td>";
        		    ofiInput = ofiInput+"\n\t<td><input type=\"checkbox\" name=\"Ini\" id=\""+codigo+"Ini\" value=\""+codigo+"\" style=\"width: 60px;\"/></td>";
        			ofiInput = ofiInput+"\n</tr>\n";
        		}		
            }
            request.setAttribute("hayAny", Boolean.valueOf(hayAny));
            request.setAttribute("anyGestionar", anyGestionar);
            request.setAttribute("ofiInput", ofiInput);
            request.setAttribute("elementFocus", elementFocus);
            request.setAttribute("oficines", oficines);
            request.setAttribute("missatge", missatge);
        	request.setAttribute("descMissatge", descMissatge);
        	request.setAttribute("mesInfoMissatge", mesInfoMissatge);
        } catch(Exception ex) {
			log.error("Error a comptadorsAdmin",ex);
		}
    	return resultado;
    }
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String param = "/admin/controller.do?accion=index";
		ServletContext context = this.getServletConfig().getServletContext();
		HttpSession sesion = request.getSession();
		String accion;

        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");


		// Buscamos el parámetro "accion"
        accion = obtenerParametro( request,"accion",false);
		
		log.debug("Servlet ControllerAdminServlet, accion="+accion);
		// No se si esto tiene alguna función
        //if (accion!=null && !accion.equals(""))
		//	param = request.getParameter("accion").trim();
		
        
        if(!accion.equals("")){
        	// Si acción no esta vacia
        	if ("altaModelOfici".equals(accion)) param = altaModelOficiAdmin(request, sesion, response);
        	if ("altaModelRebut".equals(accion)) param = altaModelRebutAdmin(request, sesion, response);
        	if ("agrupacionsgeografiques".equals(accion)) param = agrupacionsgeografiquesAdmin(request, sesion);
        	if ("autoritzUsuari".equals(accion)) param = autoritzUsuariAdmin(request, sesion);
        	if ("autoritzOficina".equals(accion)) param = autoritzOficinaAdmin(request, sesion);
        	if ("comptadors".equals(accion)) param = comptadorsAdmin(request, sesion);
        	if ("entitats".equals(accion)) param = entitatsAdmin(request, sesion);
        	if ("index".equals(accion)) param = indexAdmin(request, sesion);
        	if ("modelsOficis".equals(accion)) param = modelsOficisAdmin(request, sesion);
        	if ("modelsRebuts".equals(accion)) param = modelsRebutsAdmin(request, sesion);
        	if ("municipis060".equals(accion)) param = municipis060Admin(request, sesion);
        	if ("oficines".equals(accion)) param = oficinesAdmin(request, sesion);
        	if ("oficinesFisiques".equals(accion)) param = oficinesFisiquesAdmin(request, sesion);
        	if ("organismes".equals(accion)) param = organismesAdmin(request, sesion);
        	if ("organismesOficina".equals(accion)) param = organismesOficinaAdmin(request, sesion);
        	if ("passaTraspassos".equals(accion)) param = passaTraspassosAdmin(request, sesion);
        	if ("tipusDocuments".equals(accion)) param = tipusDocumentsAdmin(request, sesion);
        	if ("totesAgruGeo".equals(accion)) param = totesAgruGeoAdmin(request, sesion);
        	if ("totesEntitats".equals(accion)) param = totesEntitatsAdmin(request, sesion);
        	if ("totesOficines".equals(accion)) param = totesOficinesAdmin(request, sesion);
        	if ("totesOficinesFisiques".equals(accion)) param = totesOficinesFisiquesAdmin(request, sesion);
        	if ("totsModelsOficis".equals(accion)) param = totsModelsOficisAdmin(request, sesion);
        	if ("totsModelsRebuts".equals(accion)) param = totsModelsRebutsAdmin(request, sesion);
        	if ("totsMunicipis060".equals(accion)) param = totsMunicipis060Admin(request, sesion);
        	if ("totsOrganismes".equals(accion)) param = totsOrganismesAdmin(request, sesion);
        	if ("totsTipusDoc".equals(accion)) param = totsTipusDocAdmin(request, sesion);
        	if ("traspassos".equals(accion)) param = traspassosAdmin(request, sesion);
        }
        
		String url = response.encodeURL(param);
		context.getRequestDispatcher(url).forward(request, response);
	}

	private String entitatsAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/entitats.jsp");
		String missatge = "";
		String descMissatge = "";
		String mesInfoMissatge = "";
        String entitatGestionar="";
        String subentitatGestionar="";
        String elementFocus = ""; //Element que tendrà el focus de la pàgina.

		try{                       
            AdminFacade autUsu = AdminFacadeUtil.getHome().create();

			/* Gestión errores. */
			gestionMensajesError( request, missatge, descMissatge, mesInfoMissatge);

			// Buscamos el parámetro "entitatGestionar"
			entitatGestionar = obtenerParametro( request,"entitatGestionar",false);

			// Buscamos el parámetro "subentitatGestionar"
			subentitatGestionar = obtenerParametro( request,"subentitatGestionar",false);

            //Si l'atribut "init" és "init", buidam anyGestionar per a que ens presenti el formulari inicial.
            String ini= (request.getAttribute("init") != null ? (String) request.getAttribute("init"):"");
            if  ( ini.equals("init") ) {
            	entitatGestionar="";
            	subentitatGestionar="";
            }

            /* Cercam l'entitat passada com a paràmetre */
            EntitatData entitats = new EntitatData();
            log.debug("entitatGestionar="+entitatGestionar+" subentitatGestionar="+subentitatGestionar);
            if (entitatGestionar!=null && !entitatGestionar.equals("")
            		&& subentitatGestionar!=null && !subentitatGestionar.equals("")   ) {
            	entitats = autUsu.getEntitat( entitatGestionar, subentitatGestionar );
            	if ( !entitats.getCodigoEntidad().equals("") ) { //Si el codi no és buid, agafam el codi en castellà de l'entitat com a entitatGestionar 
            		entitatGestionar = entitats.getCodigoEntidad();
            	}
            }

            boolean hayEntidad = !entitatGestionar.equals("");
            boolean existeEntidad = false;
            if (!hayEntidad) {
                elementFocus="oficinaGestionar";
            } else {
                if ( !entitats.getDataBaixa().equals("")) {     		
        		    // Si la oficina existeix...	
        			existeEntidad = true;		
        			entitats.setDataBaixa("");

                } else {
            		//Si la oficina no existeix, l'hem de crear! 
        			entitats.setCodigoEntidad(entitatGestionar);
        			entitats.setCodiEntitat(entitatGestionar);
        			entitats.setSubcodiEnt(subentitatGestionar);
        			entitats.setDataBaixa("");
        	    }
            }


            request.setAttribute("hayEntidad", Boolean.valueOf(hayEntidad));
            request.setAttribute("existeEntidad", Boolean.valueOf(existeEntidad));
            request.setAttribute("entitatGestionar", entitatGestionar);
            request.setAttribute("elementFocus", elementFocus);
            request.setAttribute("entitats", entitats);
            request.setAttribute("missatge", missatge);
        	request.setAttribute("descMissatge", descMissatge);
        	request.setAttribute("mesInfoMissatge", mesInfoMissatge);
        } catch(Exception ex) {
			log.error("Error a entitatsAdmin()",ex);
		}    		
		
    	return resultado;
    }

	/**
	 * Método para leer y procesar los atributos de error de la petición
	 */
	private void gestionMensajesError(HttpServletRequest request,String missatge,String descMissatge,String mesInfoMissatge){
       	 missatge = (String) request.getAttribute("missatge");
         descMissatge = (String) request.getAttribute("descMissatge");
    	 mesInfoMissatge = "";
    	
    	int jj=0;
    	while ( request.getAttribute("mesInfoMissatge"+jj)!=null ) {
    		mesInfoMissatge = mesInfoMissatge + "\t<p>"+((String) request.getAttribute("mesInfoMissatge"+jj)).replaceAll ("\\n", "")+ "</p>";
    		jj++;
    	}
		
	}
	
	private String indexAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/index.jsp");  	
       	String missatge = (String) request.getAttribute("missatge");
        String descMissatge = (String) request.getAttribute("descMissatge");	
    	String mesInfoMissatge = "";
    	int i=0;
    	
    	while ( request.getAttribute("mesInfoMissatge"+i)!=null ) {
    		mesInfoMissatge = mesInfoMissatge + (String) request.getAttribute("mesInfoMissatge"+i);
    		i++;
    	}
    	
    	request.setAttribute("missatge", missatge);
    	request.setAttribute("descMissatge", descMissatge);
    	request.setAttribute("mesInfoMissatge", mesInfoMissatge);
    	return resultado;
    }

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	private String modelsOficisAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/modelsOficis.jsp");
		String missatge = "";
		String descMissatge = "";
		String mesInfoMissatge = "";
		String modelGestionar="";
        String valorAccio = ""; //Valor de l'acció a fer (alta, modificació)
        Vector descModel = null;
        String elementFocus = ""; //Element que tendrà el focus de la pàgina.


		try{            
            AdminFacade autUsu = AdminFacadeUtil.getHome().create();

			/* Gestión errores. */
			gestionMensajesError( request, missatge, descMissatge, mesInfoMissatge);
			
			// Buscamos el parámetro "modelGestionar"
			modelGestionar = obtenerParametro( request,"modelGestionar",false);

            /* Cercam l'organisme */
            if (modelGestionar!=null &&!modelGestionar.equals("") ) {
            	descModel = autUsu.getModelOfici( modelGestionar );
            }

            boolean hayModel = !modelGestionar.equals("");
            boolean existeModel = false;
            if (!hayModel) {
            	elementFocus="modelGestionar";
            } else {
                if ( descModel.get(1)!=null && !descModel.get(1).toString().equals("") && descModel.get(1).toString().indexOf("no existeix")<0 ) {
        		    // Si l'organisme existeix...	
        			existeModel = true;		
        			valorAccio="actualitzaModel";
                } else {
        			valorAccio="altaModel";
        			//Si la agrupació geogràfica no existeix, l'hem de crear!

        	    }
            }

            int descModelSize=0;
            if (descModel!=null) descModelSize=descModel.size();
            
            request.setAttribute("hayModel", Boolean.valueOf(hayModel));
            request.setAttribute("existeModel", Boolean.valueOf(existeModel));
            request.setAttribute("modelGestionar", modelGestionar);
            request.setAttribute("descModel", descModel);
            request.setAttribute("descModelSize", new Integer(descModelSize));
            request.setAttribute("valorAccio", valorAccio);
            request.setAttribute("elementFocus", elementFocus);
            request.setAttribute("missatge", missatge);
        	request.setAttribute("descMissatge", descMissatge);
        	request.setAttribute("mesInfoMissatge", mesInfoMissatge);
        } catch(Exception ex) {
        	log.error("Capturam excepci\u00f3 estranya!",ex);
		}    		
		
    	return resultado;
    }

	private String modelsRebutsAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/modelsRebuts.jsp");
		String missatge = "";
		String descMissatge = "";
		String mesInfoMissatge = "";
        String modelGestionar="";
        String valorAccio = ""; //Valor de l'acció a fer (alta, modificació)
        Vector descModel = null;
        String elementFocus = ""; //Element que tendrà el focus de la pàgina.
        boolean existeModel = false;

		try{            

            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
			/* Gestión errores. */
			gestionMensajesError( request, missatge, descMissatge, mesInfoMissatge);
			// Buscamos el parámetro "modelGestionar"
			modelGestionar = obtenerParametro( request,"modelGestionar",false);


            /* Cercam l'organisme */
            if (modelGestionar!=null &&!modelGestionar.equals("") ) {
            	descModel = autUsu.getModelRebut( modelGestionar );
            }

            boolean hayModel = !modelGestionar.equals("");
            
            if (!hayModel) {
            	elementFocus="modelGestionar";
            } else {
                if ( descModel.get(1)!=null && !descModel.get(1).toString().equals("") && descModel.get(1).toString().indexOf("no existeix")<0 ) {
        		    // Si l'organisme existeix...	
        			existeModel = true;		
        			valorAccio="actualitzaModel";
                } else {
        			valorAccio="altaModel";
        			//Si la agrupació geogràfica no existeix, l'hem de crear!

        	    }
            }

            int descModelSize=0;
            if (descModel!=null) descModelSize=descModel.size();
            
            request.setAttribute("hayModel", Boolean.valueOf(hayModel));
            request.setAttribute("existeModel", Boolean.valueOf(existeModel));
            request.setAttribute("modelGestionar", modelGestionar);
            request.setAttribute("descModel", descModel);
            request.setAttribute("descModelSize", new Integer(descModelSize));
            request.setAttribute("valorAccio", valorAccio);
            request.setAttribute("elementFocus", elementFocus);
            request.setAttribute("missatge", missatge);
        	request.setAttribute("descMissatge", descMissatge);
        	request.setAttribute("mesInfoMissatge", mesInfoMissatge);
        } catch(Exception ex) {
        	log.error("Capturam excepci\u00f3 estranya!",ex);
		}    		
		
    	return resultado;
    }

	private String municipis060Admin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/municipis060.jsp");
		String valorAccio = ""; //Valor de l'acció a fer (alta, modificació)
		String missatge = "";
		String descMissatge = "";
		String mesInfoMissatge = "";
		String mun060Gestionar="";
        String elementFocus = ""; //Element que tendrà el focus de la pàgina.

		try{            
            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
			/* Gestión errores. */
			gestionMensajesError( request, missatge, descMissatge, mesInfoMissatge);
			// Buscamos el parámetro "mun060Gestionar"
			mun060Gestionar = obtenerParametro( request,"mun060Gestionar",false);

            //Si l'atribut "init" és "init", buidam mun060Gestionar per a que ens presenti el formulari inicial.
            String ini= (request.getAttribute("init") != null ? (String) request.getAttribute("init"):"");
            if  ( ini.equals("init") ) {
            	mun060Gestionar="";
            }

            /* Cercam el municipi passada com a paràmetre */
            Municipi060Data municipi060 = new Municipi060Data();

            if (mun060Gestionar!=null && !mun060Gestionar.equals("") ) {
            	municipi060 = autUsu.getMunicipi060( mun060Gestionar );
            	if ( municipi060 !=null ) log.debug(mun060Gestionar.toString());
            }

            boolean hayMunicipio = !mun060Gestionar.equals("");
            boolean existeMunicipio = false;
            if (!hayMunicipio) {
                elementFocus="mun060Gestionar";
            } else {
        		elementFocus="descMunicipi";
        		if ( municipi060!=null ) {
        			valorAccio="actualitzaMunicipi060";
        		    // Si la oficina existeix...	
        			existeMunicipio = true;		
        			municipi060.setDataBaixa(""); 
                } else {
        			valorAccio="altaMunicipi060";
        			//Si la oficina no existeix, l'hem de crear!
        			municipi060 = new Municipi060Data();
        			municipi060.setCodiMunicipi060(mun060Gestionar);
        			municipi060.setDataBaixa(""); 
        	    }
            }

            request.setAttribute("hayMunicipio", Boolean.valueOf(hayMunicipio));
            request.setAttribute("existeMunicipio", Boolean.valueOf(existeMunicipio));
            request.setAttribute("mun060Gestionar", mun060Gestionar);
            request.setAttribute("valorAccio", valorAccio);
            request.setAttribute("elementFocus", elementFocus);
            request.setAttribute("municipi060", municipi060);
            request.setAttribute("missatge", missatge);
        	request.setAttribute("descMissatge", descMissatge);
        	request.setAttribute("mesInfoMissatge", mesInfoMissatge);
        } catch(Exception ex) {
        	log.error("Capturam excepci\u00f3 estranya!",ex);
		}    			
    	return resultado;
    }

	/**
	 * Método para leer y tratar un parametro de la petición web
	 * 
	 * @param request
	 * @param nombreParametro
	 * @return
	 */
	private String obtenerParametro(HttpServletRequest request,String nombreParametro, boolean mayuscula){
		String rtdo = "";
        if ( request.getParameter(nombreParametro) != null && !request.getParameter(nombreParametro).equals("") ){
        	rtdo=request.getParameter(nombreParametro).trim();
        	if(mayuscula) rtdo=rtdo.toUpperCase();
            }
		return rtdo;
	}

	private String oficinesAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/oficines.jsp");
		String missatge = "";
		String descMissatge = "";
		String mesInfoMissatge = "";
		String oficinaGestionar="";
		String valorAccio = ""; //Valor de l'acció a fer (alta, modificació)
		Vector oficines = new Vector();
		Vector historicOficines = new Vector();
        String elementFocus = ""; //Element que tendrà el focus de la pàgina.

		try{            
            AdminFacade autUsu = AdminFacadeUtil.getHome().create();

			/* Gestión errores. */
			gestionMensajesError( request, missatge, descMissatge, mesInfoMissatge);
			
			// Buscamos el parámetro "usuariAutoritzar"
			oficinaGestionar = obtenerParametro( request,"oficinaGestionar",false);

            /* Cercam l'oficina */
            if (oficinaGestionar!=null &&!oficinaGestionar.equals("") ) {
            	oficines = autUsu.getOficina( oficinaGestionar );
            	historicOficines = autUsu.getHistOficina(oficinaGestionar);
            }

            //Si l atribut "init" és "init", buidam oficinaGestionar per a que ens presenti el formulari inicial.
            String ini= (request.getAttribute("init") != null ? (String) request.getAttribute("init"):"");
            if  ( ini.equals("init") ) {
            	oficinaGestionar="";
            }

            boolean hayOficina = !oficinaGestionar.equals("");
            boolean existeOficina = false;
            if (!hayOficina) {
                elementFocus="oficinaGestionar";
            } else {
                if ( oficines!=null && !oficines.get(0).toString().equals("")) {     		
        		    // Si la oficina existeix...	
        			existeOficina = true;		
            		valorAccio="actualitzaOficina";
            		for (int i=0;i<historicOficines.size();i=i+4){
            			String fecBaixa=historicOficines.get(i+3).toString();

            			if (!fecBaixa.equals("0") && i<4 ) {
            				//El darrer històric d'oficina té data de baixa, aleshores no hi ha cap oficina activa, donam l'opció de crear d'activar
            				//l'oficina donant un nou nom i data d'alta!
            				elementFocus="descOficina";
            				valorAccio="nouNomOficina";
            			}

            			if ( fecBaixa.equals("0") ) {
            				elementFocus="dataBaixa";
            			}

            			if ( (fecBaixa.equals("0") && i<4) ) {
            				valorAccio="baixaOficina";
            			}
            		}

                } else {
            		//Si la oficina no existeix, l'hem de crear! 
            		valorAccio="altaOficina";
        	    }
            }


            request.setAttribute("hayOficina", Boolean.valueOf(hayOficina));
            request.setAttribute("existeOficina", Boolean.valueOf(existeOficina));
            request.setAttribute("oficinaGestionar", oficinaGestionar);
            request.setAttribute("valorAccio", valorAccio);
            request.setAttribute("elementFocus", elementFocus);
            request.setAttribute("oficines", oficines);
            request.setAttribute("historicOficines", historicOficines);
            request.setAttribute("historicOficinesSize", new Integer(historicOficines.size()));
            request.setAttribute("missatge", missatge);
        	request.setAttribute("descMissatge", descMissatge);
        	request.setAttribute("mesInfoMissatge", mesInfoMissatge);
        } catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }

	private String oficinesFisiquesAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/oficinesFisiques.jsp");
		String missatge = "";
		String descMissatge = "";
		String mesInfoMissatge = "";
    	Vector oficines = new Vector();
        Vector historicOficinesFisiques = new Vector();
        String oficinaGestionar="";
        String oficinaGestionarFisica="";
        String fecBaixa="";
        String elementFocus = ""; //Element que tendra el focus de la pagina.
        String ofiInput="";
        boolean hayOficina=false;
        boolean existeOficina = false;
        String valorAccio = ""; //Valor de l'acció a fer (alta, modificació)

		try{            

            AdminFacade autUsu = AdminFacadeUtil.getHome().create();

			/* Gestión errores. */
			gestionMensajesError( request, missatge, descMissatge, mesInfoMissatge);


			// Buscamos el parámetro "oficinaGestionar"
			oficinaGestionar = obtenerParametro( request,"oficinaGestionar",false);

			// Buscamos el parámetro "oficinaGestionarFisica"
			oficinaGestionarFisica = obtenerParametro( request,"oficinaGestionarFisica",false);
            


            /* Cercam l'oficina */
            if (oficinaGestionar!=null &&!oficinaGestionar.equals("") && oficinaGestionarFisica!=null &&!oficinaGestionarFisica.equals("") ) {
            	oficines = autUsu.getOficinaFisica( oficinaGestionar, oficinaGestionarFisica );
            	historicOficinesFisiques = autUsu.getHistOficinaFisica(oficinaGestionar, oficinaGestionarFisica);
            }

            //Si l'atribut "init" és "init", buidam oficinaGestionar per a que ens presenti el formulari inicial.
            String ini= (request.getAttribute("init") != null ? (String) request.getAttribute("init"):"");
            if  ( ini.equals("init") ) {
            	oficinaGestionar="";
            	oficinaGestionarFisica="";
            }

            hayOficina = !(oficinaGestionar.equals("") || oficinaGestionarFisica.equals("") );

            if (!hayOficina) {
                elementFocus="oficinaGestionar";
            } else {
                if ( oficines!=null && !oficines.get(0).toString().equals("")) {     		
        		    // Si la oficina existeix...	
        			existeOficina = true;		
    				boolean impresoTextoOfiHist = false;
    				boolean hayOficinaHistorica = !historicOficinesFisiques.get(0).toString().equals("");

            		ofiInput="<tr><th>Oficina</th><th>Data alta<br/>(dd/mm/yyyy)</th><th>Data baixa<br/>(dd/mm/yyyy)</th></tr>";

            		for (int i=0;i<historicOficinesFisiques.size();i=i+5){
            			String descripcion=historicOficinesFisiques.get(i+2).toString();
            			String fecAlta=historicOficinesFisiques.get(i+3).toString();
            			fecBaixa=historicOficinesFisiques.get(i+4).toString();	
            			
            			if(!hayOficinaHistorica){
            				fecBaixa = "0";
                			descripcion=oficines.get(2).toString();
                			fecAlta=oficines.get(3).toString();
            			}


            			if (!fecBaixa.equals("0") && i<5 ) {
            				//El darrer històric d'oficina fisica té data de baixa, aleshores no hi ha cap oficina fisica activa, donam l'opció de crear d'activar
            				//l'oficina fisica donant un nou nom i data d'alta!
            				elementFocus="descOficina";

            				ofiInput = ofiInput+"<tr><td>"+oficinaGestionar+" - "+oficinaGestionarFisica+" - <input type=\"text\" name=\"descOficina\" id=\"descOficina\" size=\"20\" maxlength=\"20\"  value=\"\" style=\"width: 200px;\"></td>";
            				ofiInput = ofiInput+"<td><input type=\"text\" name=\"dataAlta\" id=\"dataAlta\" size=\"10\" maxlength=\"10\" value=\"\" style=\"width: 70px;\" onKeyPress=\"return goodchars(event,'0123456789/')\"></td>";
            				ofiInput = ofiInput+"<td><input type=\"text\" readonly=\"true\" name=\"dataBaixa\" id=\"dataBaixa\" size=\"10\" maxlength=\"10\" value=\"0\" style=\"width: 70px;\"></td></tr>";
            				//Botón
            				ofiInput = ofiInput +"<tr><td align=\"center\" colspan=\"3\"><input type=\"submit\" value=\"Dona d'alta\" ></td>\n</tr>";
            				//Parámetros ocultos
            				ofiInput = ofiInput +"<tr><td align=\"center\" colspan=\"3\">&nbsp;";
            				ofiInput = ofiInput+"<input type=\"hidden\" name=\"codiOficina\" id=\"codiOficina\" readonly=\"true\" value=\""+oficinaGestionar+"\" >";
            				ofiInput = ofiInput+"<input type=\"hidden\" name=\"codiOficinaFisica\" id=\"codiOficinaFisica\" readonly=\"true\" value=\""+oficinaGestionarFisica+"\" >";
            				ofiInput = ofiInput +"</td></tr>";
            				valorAccio="nouNomOficinaFisica";
            			}

            			if ( fecBaixa.equals("0") ) {
            				//El darrer històric d'oficina fisica NO té data de baixa, aleshores  hi ha una oficina fisica activa, donam l'opció de crear dar de baixa
            				//l'oficina fisica donant una data de baixa.
            				elementFocus="dataBaixa";
            				ofiInput = ofiInput+"<tr><td>"+oficinaGestionar+" - "+oficinaGestionarFisica+" "+"<input type=\"text\" readonly=\"true\" name=\"descOficina\"  id=\"descOficina\" size=\"20\" maxlength=\"20\"  value=\""+descripcion+"\" style=\"width: 200px;\"></td>";
            				ofiInput = ofiInput+"<td>"+fecAlta+"</td>";
            				ofiInput = ofiInput+"<td><input type=\"text\" name=\"dataBaixa\" id=\"dataBaixa\" size=\"10\" maxlength=\"10\"  value=\"0\" style=\"width: 70px;\" onKeyPress=\"return goodchars(event,'0123456789/')\"></td>";
            				valorAccio="baixaOficinaFisica";
            				// Añadimos el botón Actualitza
            				ofiInput = ofiInput +"<tr><td align=\"center\" colspan=\"3\"><input type=\"submit\" value=\"Actualitza\"/></td></tr>";
            				//Parámetros ocultos
            				ofiInput = ofiInput +"<tr><td align=\"center\" colspan=\"3\">&nbsp;";
            				ofiInput = ofiInput+"<input type=\"hidden\" name=\"codiOficina\" id=\"codiOficina\" readonly=\"true\" value=\""+oficinaGestionar+"\" >";
            				ofiInput = ofiInput+"<input type=\"hidden\" name=\"codiOficinaFisica\" id=\"codiOficinaFisica\" readonly=\"true\" value=\""+oficinaGestionarFisica+"\" >";
            				ofiInput = ofiInput+"<input type=\"hidden\" name=\"dataAlta\" id=\"dataAlta\" value=\""+fecAlta+"\"  onKeyPress=\"return goodchars(event,'0123456789/')\">";
            				ofiInput = ofiInput +"</td></tr>";
            			}

            			if(hayOficinaHistorica){	
            				//Si hay historico
            				if(!impresoTextoOfiHist){
            					if (!fecBaixa.equals("0")/*||(fecBaixa.equals("0") && i==5)*/){
            						ofiInput = ofiInput+"\n<tr><td colspan=3>Hist&ograve;ric de l'oficina f\u00edsica</td></tr>";
            						impresoTextoOfiHist=true;
            					}
            				}
                			
            				if ( !fecBaixa.equals("0") ) {
            					ofiInput = ofiInput+"<tr><td>"+oficinaGestionar+" - "+oficinaGestionarFisica+" - "+descripcion+"</td><td>"+fecAlta+"</td><td>"+fecBaixa+"</td></tr>";
            				}
            			}
            		} //Fin for

            		log.debug("Admin/oficinesFisiques.jsp. Formulari per modificar l'oficina f�sica amb codi:"+oficinaGestionar+"-"+oficinaGestionarFisica);
                } else {
        			//Si la oficina no existeix, l'hem de crear! 
        			valorAccio="altaOficinaFisica";
            		ofiInput="<tr><th>Codi Oficina</th><th>Codi Oficina F\u00edsica</th><th>Descripci\u00f3 Oficina</th><th>Data alta<br/>(dd/mm/yyyy)</th></tr><tr><td></td><td></td><td></td><td></td></tr>";
            		ofiInput = ofiInput+"<tr><td><input type=\"text\" name=\"oficinaGestionar\" id=\"oficinaGestionar\"  size=\"2\" maxlength=\"2\" readonly=\"true\" value=\""+oficinaGestionar+"\" style=\"width: 30px;\"/></td>";
            		ofiInput = ofiInput+"<td>" +
                            "<input type=\"text\" name=\"oficinaGestionarFisica\" id=\"oficinaGestionarFisica\"  size=\"4\" maxlength=\"4\" readonly=\"true\" value=\""+oficinaGestionarFisica+"\" style=\"width: 50px;\"/></td>";
            		ofiInput = ofiInput+"<td><input type=\"text\" name=\"descOficina\" id=\"descOficina\" size=\"20\" maxlength=\"20\"  value=\"\" style=\"width: 200px;\"/></td>";
            		ofiInput = ofiInput +"<td><input type=\"text\" name=\"dataAlta\" id=\"dataAlta\" size=\"10\" maxlength=\"10\" value=\"\" style=\"width: 100px;\" onKeyPress=\"return goodchars(event,'0123456789/')\"></td></tr>";
            		ofiInput = ofiInput +"<tr>\n\t<td align=\"center\" colspan=\"4\"><input type=\"submit\" value=\"Dona d'alta\"/></td>\n</tr>";
        	    }
            }

            request.setAttribute("hayOficina", Boolean.valueOf(hayOficina));
            request.setAttribute("existeOficina", Boolean.valueOf(existeOficina));
            request.setAttribute("oficinaGestionar", oficinaGestionar);
            request.setAttribute("oficinaGestionarFisica", oficinaGestionarFisica);
            request.setAttribute("valorAccio", valorAccio);
            request.setAttribute("elementFocus", elementFocus);
            request.setAttribute("oficines", oficines);
            request.setAttribute("ofiInput", ofiInput);
            request.setAttribute("missatge", missatge);
        	request.setAttribute("descMissatge", descMissatge);
        	request.setAttribute("mesInfoMissatge", mesInfoMissatge);
        } catch(Exception ex) {
			log.error("Capturam excepci\u00f3 estranya!",ex);
		}    		
		
    	return resultado;
    }
    
	private String organismesAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/organismes.jsp");
		String missatge = "";
		String descMissatge = "";
		String mesInfoMissatge = "";
        String organismeGestionar="";       
        String valorAccio = ""; //Valor de l'acció a fer (alta, modificació)
        String elementFocus = ""; //Element que tendrà el focus de la pàgina.
        Vector organismes = new Vector();
        Vector historicOrganismes = new Vector();

		try{
            AdminFacade autUsu = AdminFacadeUtil.getHome().create();

			/* Gestión errores. */
			gestionMensajesError( request, missatge, descMissatge, mesInfoMissatge);

			// Buscamos el parámetro "usuariAutoritzar"
            organismeGestionar = obtenerParametro( request,"organismeGestionar",false);

            /* Cercam l'organisme */
            if (organismeGestionar!=null &&!organismeGestionar.equals("") ) {
            	organismes = autUsu.getOrganisme( organismeGestionar );
            	historicOrganismes = autUsu.getHistOrganisme(organismeGestionar);
            }

            //Si l atribut "init" és "init", buidam oficinaGestionar per a que ens presenti el formulari inicial.
            String ini= (request.getAttribute("init") != null ? (String) request.getAttribute("init"):"");
            if  ( ini.equals("init") ) {
            	organismeGestionar="";
            }

            boolean hayOrganisme = !organismeGestionar.equals("");
            boolean existeOrganisme = false;
            if (!hayOrganisme) {
            	elementFocus="organismeGestionar";
            } else {
                if ( organismes!=null && !organismes.get(0).toString().equals("")) {     		
        		    // Si l'organisme existeix...	
        			existeOrganisme = true;		
            		valorAccio="actualitzaOrganisme";
            		for (int i=0;i<historicOrganismes.size();i=i+5){
            			String fecBaixa=historicOrganismes.get(i+4).toString();

            			if (!fecBaixa.equals("0") && i<5 ) {
            				//El darrer històric d'oficina té data de baixa, aleshores no hi ha cap oficina activa, donam l'opció de d'activar
            				//l'organisme donant un nou nom i data d'alta!
            				elementFocus="descOrganisme";
            				valorAccio="nouNomOrganisme";
            			}
            			if ( fecBaixa.equals("0") ) {
            				elementFocus="dataBaixa";
            			}

            			if ( (fecBaixa.equals("0") && i<5) ) {
            				valorAccio="baixaOrganisme";
            			}
            		}
                } else {
            		//Si l'Organisme no existeix, l'hem de crear! 
        			valorAccio="altaOrganisme";
        	    }
            }

            request.setAttribute("hayOrganisme", Boolean.valueOf(hayOrganisme));
            request.setAttribute("existeOrganisme", Boolean.valueOf(existeOrganisme));
            request.setAttribute("organismeGestionar", organismeGestionar);
            request.setAttribute("valorAccio", valorAccio);
            request.setAttribute("elementFocus", elementFocus);
            request.setAttribute("organismes", organismes);
            request.setAttribute("historicOrganismes", historicOrganismes);
            request.setAttribute("historicOrganismesSize", new Integer(historicOrganismes.size()));
            request.setAttribute("missatge", missatge);
        	request.setAttribute("descMissatge", descMissatge);
        	request.setAttribute("mesInfoMissatge", mesInfoMissatge);
        } catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }

	private String organismesOficinaAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/organismesOficina.jsp");
        String elementFocus = ""; //Element que tendrà el focus de la pàgina.
		String ofiInput = "";
        Vector oficines = new Vector();
        Vector organismesOficina = new Vector();
        Vector noRemetreOficina = new Vector();
        String oficinaGestionar="";

		try{
            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
            ValoresFacade valores = ValoresFacadeUtil.getHome().create();

			// Buscamos el parámetro "oficinaGestionar"
            oficinaGestionar = obtenerParametro( request,"oficinaGestionar",false);
            /* Cercam l'oficina */

            if (oficinaGestionar!=null &&!oficinaGestionar.equals("") ) {
            	oficines = autUsu.getOficina( oficinaGestionar );
            	organismesOficina = valores.buscarDestinatarios(oficinaGestionar);
            	noRemetreOficina = valores.buscarNoRemision(oficinaGestionar);
            }

            Vector organismes = autUsu.getOrganismes();

            boolean hayOficina = !oficinaGestionar.equals("");
            boolean existeOficina = false;
            if (!hayOficina) {
            	elementFocus="organismeGestionar";
            } else {

            		String Orgchecked="";
            		String Remchecked="";
            		for (int i=0;i<organismes.size();i=i+3){
            			Orgchecked="";
            			Remchecked="";
            			String codigo=organismes.get(i).toString();
            		    String descripcion=organismes.get(i+2).toString();
            			if ( organismesOficina.contains( codigo ) ) {
            					Orgchecked="checked=\"true\""; 
            			}
            			if ( noRemetreOficina.contains( codigo ) ) {
            				Remchecked="checked=\"true\""; 
            			}
            		    ofiInput = ofiInput+"<tr>";
            		    ofiInput = ofiInput+"\n\t<td><input type=\"checkbox\" name=\"org\" id=\"org"+codigo+"\" value=\""+codigo+"\" "+Orgchecked+" style=\"width: 60px;\"/></td>";
            		    ofiInput = ofiInput+"\n\t<td><input type=\"checkbox\" name=\"rem\" id=\"rem"+codigo+"\" value=\""+codigo+"\" "+Remchecked+" style=\"width: 60px;\"/></td>";
            			ofiInput = ofiInput+"\n\t<td>"+codigo+" - "+descripcion+"</td>";
            			ofiInput = ofiInput+"\n</tr>\n";
            		}		

            }

            request.setAttribute("hayOficina", Boolean.valueOf(hayOficina));
            request.setAttribute("existeOficina", Boolean.valueOf(existeOficina));
            request.setAttribute("oficinaGestionar", oficinaGestionar);
            request.setAttribute("ofiInput", ofiInput);
            request.setAttribute("elementFocus", elementFocus);
            request.setAttribute("organismes", organismes);
            request.setAttribute("oficines", oficines);

        } catch(Exception ex) {
			log.error("Error a organismesOficinaAdmin()",ex);
		}    		
		
    	return resultado;
    }

	private String passaTraspassosAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/passaTraspassos.jsp");
		String missatge = "";
		String descMissatge = "";
		String mesInfoMissatge = "";
		//String urlPath = request.getContextPath();
		int nombreLinies = 0;

		try{            
			/* Gestión errores. */
			gestionMensajesError( request, missatge, descMissatge, mesInfoMissatge);

            //Si l'atribut "fitxer" no és buid, hem de parsejar-lo i agafar un registre per línia.
            String fitxer = (request.getAttribute("fitxer") != null ? (String) request.getAttribute("fitxer"):"");

            String nomFitxer = (request.getAttribute("nomFitxer") != null ? (String) request.getAttribute("nomFitxer"):"");

            
            if ( fitxer!=null ) {
            	request.setAttribute("fitxer",fitxer);
            	log.debug("fitxer="+fitxer);
            	fitxer = fitxer.trim();

               	try{
               		BufferedReader buffReader = new BufferedReader( new StringReader(fitxer) );
               		String tmp = "";
               		int j=0;
               		while ( (tmp = buffReader.readLine())!=null ) {
            			j++;
               			log.debug("línia "+j+": "+tmp);   		
               		}
            		nombreLinies=j;
               	}catch(Exception ex){
               		log.error("Capturam excepci\u00f3 estranya!",ex);
                }
            }

            if (request.getParameter("oficinaOnRegistrar")!=null)
            	log.debug("Oficina on gestionar="+request.getParameter("oficinaOnRegistrar"));

            if (request.getParameter("organismeEmissor")!=null)
            	log.debug("organismeEmissor="+request.getParameter("organismeEmissor"));

            request.setAttribute("nombreLinies", new Integer(nombreLinies));
            request.setAttribute("fitxer", fitxer);
            request.setAttribute("nomFitxer", nomFitxer);
            request.setAttribute("missatge", missatge);
        	request.setAttribute("descMissatge", descMissatge);
        	request.setAttribute("mesInfoMissatge", mesInfoMissatge);

        } catch(Exception ex) {
        	log.error("Capturam excepci\u00f3 estranya!",ex);
		}    		
		
    	return resultado;
    }

	private String tipusDocumentsAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/tipusDocuments.jsp");
		String valorAccio = ""; //Valor de l'acció a fer (alta, modificació)
		String missatge = "";
		String descMissatge = "";
		String mesInfoMissatge = "";
		String tipDocGestionar="";
		String elementFocus = ""; //Element que tendrà el focus de la pàgina.
		String readonly="";

		try{            
            AdminFacade autUsu = AdminFacadeUtil.getHome().create();

			/* Gestión errores. */
			gestionMensajesError( request, missatge, descMissatge, mesInfoMissatge);
			// Buscamos el parámetro "tipDocGestionar"
			tipDocGestionar = obtenerParametro( request,"tipDocGestionar",false);


            //Si l'atribut "init" és "init", buidam anyGestionar per a que ens presenti el formulari inicial.
			// VHZ -> Creo que este codigo no se utiliza
            /*String ini= (request.getAttribute("init") != null ? (String) request.getAttribute("init"):"");
            if  ( ini.equals("init") ) {
            	tipDocGestionar="";
            }*/

            /* Cercam l'entitat passada com a paràmetre */
            TipusDocumentData tipusDocuments = new TipusDocumentData();
            log.debug("tipDocGestionar="+tipDocGestionar);
            if (tipDocGestionar!=null && !tipDocGestionar.equals("") ) {
            	tipusDocuments = autUsu.getTipusDocument( tipDocGestionar );
            	if ( tipusDocuments !=null ) log.debug(tipusDocuments.toString());
            }

            boolean hayTipDoc = !tipDocGestionar.equals("");
            boolean existeTipDoc = false;
            if (!hayTipDoc) {
            	elementFocus="tipDocGestionar";
            } else {
            	elementFocus="descTipusDoc";
                if ( tipusDocuments!=null ) {
        		    // Si l'organisme existeix...	
        			existeTipDoc = true;		
        			valorAccio="actualitzaTipusDoc";
        			if (tipusDocuments.getDataBaixa()==null || tipusDocuments.getDataBaixa().equals("0")) tipusDocuments.setDataBaixa("");
                } else {
        			readonly="readonly=\"true\""; //Quan es dona d'alta no es pot modificar la data de baixa!
        			valorAccio="altaTipusDoc";
        			//Si la oficina no existeix, l'hem de crear!
        			tipusDocuments = new TipusDocumentData();
        			tipusDocuments.setCodiTipusDoc(tipDocGestionar);
        			tipusDocuments.setDataBaixa("");

        	    }
            }
            
            request.setAttribute("hayTipDoc", Boolean.valueOf(hayTipDoc));
            request.setAttribute("existeTipDoc", Boolean.valueOf(existeTipDoc));
            request.setAttribute("tipDocGestionar", tipDocGestionar);
            request.setAttribute("tipusDocuments", tipusDocuments);
            request.setAttribute("readonly", readonly);
            request.setAttribute("valorAccio", valorAccio);
            request.setAttribute("elementFocus", elementFocus);
            request.setAttribute("missatge", missatge);
        	request.setAttribute("descMissatge", descMissatge);
        	request.setAttribute("mesInfoMissatge", mesInfoMissatge);
        } catch(Exception ex) {
        	log.error("Capturam excepci\u00f3 estranya!",ex);
		}    		
		
    	return resultado;
    }

	private String totesAgruGeoAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/totesAgruGeo.jsp");
		int agrupacionsGeografiquesSize=0;

		try{            
    
            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
          
            String subcadenaCodigo=(request.getParameter("subcadenaCodigo")==null) ? "" :request.getParameter("subcadenaCodigo").trim();
            String subcadenaTexto=(request.getParameter("subcadenaTexto")==null) ? "" : request.getParameter("subcadenaTexto").trim();

            Collection agrupacionsGeografiques = autUsu.getAgrupacionsGeografiques();
            if (agrupacionsGeografiques!=null) agrupacionsGeografiquesSize=agrupacionsGeografiques.size();
            
            request.setAttribute("subcadenaCodigo", subcadenaCodigo);
            request.setAttribute("subcadenaTexto", subcadenaTexto);
            request.setAttribute("agrupacionsGeografiques", agrupacionsGeografiques);
            request.setAttribute("agrupacionsGeografiquesSize",  new Integer(agrupacionsGeografiquesSize));
          
        } catch(Exception ex) {
			log.error("Error a totesAgruGeoAdmin()",ex);
		}    		
		
    	return resultado;
    }

	private String totesEntitatsAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/totesEntitats.jsp");
        int remitentesSize=0;

		try{            
 
            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
          
            String subcadenaCodigo=(request.getParameter("subcadenaCodigo")==null) ? "" :request.getParameter("subcadenaCodigo").trim();
            String subcadenaTexto=(request.getParameter("subcadenaTexto")==null) ? "" : request.getParameter("subcadenaTexto").trim();

            Vector remitentes=autUsu.getEntitats(subcadenaCodigo, subcadenaTexto);

            if (remitentes!=null) remitentesSize=remitentes.size();
            
            request.setAttribute("subcadenaCodigo", subcadenaCodigo);
            request.setAttribute("subcadenaTexto", subcadenaTexto);
            request.setAttribute("remitentes", remitentes);
            request.setAttribute("remitentesSize",  new Integer(remitentesSize));
          
        } catch(Exception ex) {
			log.error("Error a totesEntitatsAdmin()",ex);
		}    				
    	return resultado;
    }

	private String totesOficinesAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/totesOficines.jsp");

		try{            

            ValoresFacade valores = ValoresFacadeUtil.getHome().create();
          
            Vector oficines=valores.buscarOficinas("tots","totes");
            int oficinesSize=0;
            if (oficines!=null) oficinesSize=oficines.size();

            request.setAttribute("oficines", oficines);
            request.setAttribute("oficinesSize", new Integer(oficinesSize));
          
        } catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }

	private String totesOficinesFisiquesAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/totesOficinesFisiques.jsp");
		int oficinesSize=0;
		

		try{            
            ValoresFacade valores = ValoresFacadeUtil.getHome().create();        
            Vector oficines=valores.buscarOficinasFisicasDescripcion("tots","totes");
            
            if (oficines!=null) oficinesSize=oficines.size();

            request.setAttribute("oficines", oficines);
            request.setAttribute("oficinesSize", new Integer(oficinesSize));
          
        } catch(Exception ex) {
			log.error("Capturam excepci\u00f3 estranya!",ex);
		}    		
		
    	return resultado;
    }

	private String totsModelsOficisAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/totsModelsOficis.jsp");
		int modelsSize=0;
		try{            

            ValoresFacade valores = ValoresFacadeUtil.getHome().create();          
            Vector models=valores.buscarModelos("tots","totes");
            
            if (models!=null) modelsSize=models.size();

            request.setAttribute("models", models);
            request.setAttribute("modelsSize", new Integer(modelsSize));
          
        } catch(Exception ex) {
        	log.error("Capturam excepci\u00f3 estranya!",ex);
		}    		
		
    	return resultado;
    }

	private String totsModelsRebutsAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/totsModelsRebuts.jsp");
		int modelsSize=0;

		try{            
            ValoresFacade valores = ValoresFacadeUtil.getHome().create();
          
            Vector models=valores.buscarModelosRecibos("tots","totes");
            
            if (models!=null) modelsSize=models.size();

            request.setAttribute("models", models);
            request.setAttribute("modelsSize", new Integer(modelsSize));
          
        } catch(Exception ex) {
        	log.error("Capturam excepci\u00f3 estranya!",ex);
		}    		
		
    	return resultado;
    }

	private String totsMunicipis060Admin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/totsMunicipis060.jsp");
		int municipis060Size=0;

		try{            
            AdminFacade autUsu = AdminFacadeUtil.getHome().create();        
            Vector municipis060=autUsu.getMunicipis060();
            
            if (municipis060!=null) municipis060Size=municipis060.size();

            request.setAttribute("municipis060", municipis060);
            request.setAttribute("municipis060Size", new Integer(municipis060Size));
          
        } catch(Exception ex) {
        	log.error("Capturam excepci\u00f3 estranya!",ex);
		}    		
		
    	return resultado;
    }

	private String totsOrganismesAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/totsOrganismes.jsp");
		int organismesSize=0;

		try{               
            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
            Vector organismes=autUsu.getTotsOrganismes();
            
            if (organismes!=null) organismesSize=organismes.size();
            request.setAttribute("organismes", organismes);
            request.setAttribute("organismesSize", new Integer(organismesSize));
          
        } catch(Exception ex) {
			log.error("Error a totsOrganismesAdmin()",ex);
		}    				
    	return resultado;
    }

	private String totsTipusDocAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/totsTipusDoc.jsp");
		int tipusDocsSize=0;

		try{            
            AdminFacade autUsu = AdminFacadeUtil.getHome().create();         
            Vector tipusDocs=autUsu.getTipusDocuments();
            
            if (tipusDocs!=null) tipusDocsSize=tipusDocs.size();

            request.setAttribute("tipusDocs", tipusDocs);
            request.setAttribute("tipusDocsSize", new Integer(tipusDocsSize));
          
        } catch(Exception ex) {
        	log.error("Capturam excepci\u00f3 estranya!",ex);
		}    		
		
    	return resultado;
    }

	private String traspassosAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/traspassos.jsp");
    	return resultado;
    }

}