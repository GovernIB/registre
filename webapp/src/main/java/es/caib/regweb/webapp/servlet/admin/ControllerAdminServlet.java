

/*
 * Created on 1-des-2009
 *
 */
package es.caib.regweb.webapp.servlet.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
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
		// TODO Auto-generated constructor stub
	}
	 
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// TODO Auto-generated method stub
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = this.getServletConfig().getServletContext();
		
		HttpSession sesion = request.getSession();

        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

		String accion = request.getParameter("accion");
		if (accion!=null) accion = accion.trim();
		String param = "/admin/controller.do?accion=index";
		
		//log.debug("Servlet, accion="+accion);
		if (accion!=null && !accion.equals(""))
			param = request.getParameter("accion").trim();
		
		if ("index".equals(accion)) param = indexAdmin(request, sesion);
		if ("comptadors".equals(accion)) param = comptadorsAdmin(request, sesion);
		if ("autoritzUsuari".equals(accion)) param = autoritzUsuariAdmin(request, sesion);
		if ("oficines".equals(accion)) param = oficinesAdmin(request, sesion);
		if ("totesOficines".equals(accion)) param = totesOficinesAdmin(request, sesion);
		if ("organismesOficina".equals(accion)) param = organismesOficinaAdmin(request, sesion);
		if ("organismes".equals(accion)) param = organismesAdmin(request, sesion);
		if ("totsOrganismes".equals(accion)) param = totsOrganismesAdmin(request, sesion);
		if ("agrupacionsgeografiques".equals(accion)) param = agrupacionsgeografiquesAdmin(request, sesion);
		if ("totesAgruGeo".equals(accion)) param = totesAgruGeoAdmin(request, sesion);
		if ("entitats".equals(accion)) param = entitatsAdmin(request, sesion);
		if ("totesEntitats".equals(accion)) param = totesEntitatsAdmin(request, sesion);
		if ("oficinesFisiques".equals(accion)) param = oficinesFisiquesAdmin(request, sesion);
		if ("totesOficinesFisiques".equals(accion)) param = totesOficinesFisiquesAdmin(request, sesion);
		if ("modelsOficis".equals(accion)) param = modelsOficisAdmin(request, sesion);
		if ("totsModelsOficis".equals(accion)) param = totsModelsOficisAdmin(request, sesion);
		if ("altaModelOfici".equals(accion)) param = altaModelOficiAdmin(request, sesion, response);
		if ("modelsRebuts".equals(accion)) param = modelsRebutsAdmin(request, sesion);
		if ("totsModelsRebuts".equals(accion)) param = totsModelsRebutsAdmin(request, sesion);
		if ("altaModelRebut".equals(accion)) param = altaModelRebutAdmin(request, sesion, response);
		if ("municipis060".equals(accion)) param = municipis060Admin(request, sesion);
		if ("totsMunicipis060".equals(accion)) param = totsMunicipis060Admin(request, sesion);
		if ("totsTipusDoc".equals(accion)) param = totsTipusDocAdmin(request, sesion);
		if ("passaTraspassos".equals(accion)) param = passaTraspassosAdmin(request, sesion);
		if ("traspassos".equals(accion)) param = traspassosAdmin(request, sesion);
		if ("tipusDocuments".equals(accion)) param = tipusDocumentsAdmin(request, sesion);
		
		
		String url = response.encodeURL(param);
		context.getRequestDispatcher(url).forward(request, response);

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


	private String comptadorsAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/comptadors.jsp");

		try{            

            AutoritzacionsUsuariData autUsuData = null;

            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
            ValoresFacade valores = ValoresFacadeUtil.getHome().create();

            /* Cercam totes les oficines */
            Vector oficines = valores.buscarOficinas("tots","totes");

            String usuario=request.getRemoteUser();

            /* Gestió errors. */
               	String missatge = (String) request.getAttribute("missatge");
                String descMissatge = (String) request.getAttribute("descMissatge");
            	String mesInfoMissatge = "";
            	String indexInfoMissatge = "";
            	int i=0;
            	while ( request.getAttribute("mesInfoMissatge"+i)!=null ) {
            		mesInfoMissatge = mesInfoMissatge + "\t<p>"+((String) request.getAttribute("mesInfoMissatge"+i)).replaceAll ("\\n", "")+ "</p>";
            		i++;
            	}

            String anyGestionar="";


            if ( request.getParameter("anyGestionar") != null
            	&& !request.getParameter("anyGestionar").equals("") ){
                anyGestionar=request.getParameter("anyGestionar").trim();
            }

            //Si l'atribut "init" és "init", buidam anyGestionar per a que ens presenti el formulari inicial.
            String ini= (request.getAttribute("init") != null ? (String) request.getAttribute("init"):"");
            if  ( ini.equals("init") )
            	anyGestionar="";

            String elementFocus = ""; //Element que tendrà el focus de la pàgina.
            
            
            
            


    		String ofiInput = "";
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
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}

    	return resultado;
    }

	private String autoritzUsuariAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/autoritzUsuari.jsp");

		try{            

            
            
            
            
            

            AutoritzacionsUsuariData autUsuData = null;

            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
            ValoresFacade valores = ValoresFacadeUtil.getHome().create();


            /* Cercam totes les oficines */
            Vector oficines = valores.buscarOficinas("tots","totes");

            /* Gestió errors. */
               	String missatge = (String) request.getAttribute("missatge");
                String descMissatge = (String) request.getAttribute("descMissatge");
            	String mesInfoMissatge = "";
            	String indexInfoMissatge = "";
            	int jj=0;
            	while ( request.getAttribute("mesInfoMissatge"+jj)!=null ) {
            		mesInfoMissatge = mesInfoMissatge + "\t<p>"+((String) request.getAttribute("mesInfoMissatge"+jj)).replaceAll ("\\n", "")+ "</p>";
            		jj++;
            	}


            String usuario=request.getRemoteUser();
            String usuariAutoritzar="";

            if ( request.getParameter("usuariAutoritzar") != null
            	&& !request.getParameter("usuariAutoritzar").equals("") ){
                usuariAutoritzar=request.getParameter("usuariAutoritzar").trim().toUpperCase();
            }

            //Si l'atribut "init" és "init", buidam usuariAutoritzarr per a que ens presenti el formulari inicial.
            String ini= (request.getAttribute("init") != null ? (String) request.getAttribute("init"):"");
            if  ( ini.equals("init") )
            	usuariAutoritzar="";

            String elementFocus = ""; //Element que tendrà el focus de la pàgina.
            
            
            




            boolean hayUsuario = !usuariAutoritzar.equals("");
            boolean existeOficina = false;
            String ofiInput = "";
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

	private String oficinesAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/oficines.jsp");

		try{            

            
            
            
            
            

            AutoritzacionsUsuariData autUsuData = null;

            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
            ValoresFacade valores = ValoresFacadeUtil.getHome().create();

            /* Gestió errors. */
            String missatge = (String) request.getAttribute("missatge");
            String descMissatge = (String) request.getAttribute("descMissatge");
            String mesInfoMissatge = "";
            int j=0;
            while ( request.getAttribute("mesInfoMissatge"+j)!=null ) {
                mesInfoMissatge = mesInfoMissatge + "\t<p>"+((String) request.getAttribute("mesInfoMissatge"+j)).replaceAll ("\\n", "")+ "</p>";
                j++;
            }

            String usuario=request.getRemoteUser();
            String oficinaGestionar="";

            String valorAccio = ""; //Valor de l'acció a fer (alta, modificació)

            if ( request.getParameter("oficinaGestionar") != null
            	&& !request.getParameter("oficinaGestionar").equals("") ){
                oficinaGestionar=request.getParameter("oficinaGestionar").trim();
            }

            /* Cercam l'oficina */
            Vector oficines = new Vector();
            Vector historicOficines = new Vector();

            if (oficinaGestionar!=null &&!oficinaGestionar.equals("") ) {
            	oficines = autUsu.getOficina( oficinaGestionar );
            	historicOficines = autUsu.getHistOficina(oficinaGestionar);
            }

            //Si l atribut "init" és "init", buidam oficinaGestionar per a que ens presenti el formulari inicial.
            String ini= (request.getAttribute("init") != null ? (String) request.getAttribute("init"):"");
            if  ( ini.equals("init") ) {
            	oficinaGestionar="";
            }

            String elementFocus = ""; //Element que tendrà el focus de la pàgina.



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

	private String organismesOficinaAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/organismesOficina.jsp");

		try{

            
            
            
            
            

            AutoritzacionsUsuariData autUsuData = null;

            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
            ValoresFacade valores = ValoresFacadeUtil.getHome().create();



            String usuario=request.getRemoteUser();
            String oficinaGestionar="";

            if ( request.getParameter("oficinaGestionar") != null
            	&& !request.getParameter("oficinaGestionar").equals("") ){
                oficinaGestionar=request.getParameter("oficinaGestionar").trim();
            }

            /* Cercam l'oficina */
            Vector oficines = new Vector();
            Vector organismesOficina = new Vector();
            Vector noRemetreOficina = new Vector();

            if (oficinaGestionar!=null &&!oficinaGestionar.equals("") ) {
            	oficines = autUsu.getOficina( oficinaGestionar );
            	organismesOficina = valores.buscarDestinatarios(oficinaGestionar);
            	noRemetreOficina = valores.buscarNoRemision(oficinaGestionar);
            }


            Vector organismes = autUsu.getOrganismes();

            String elementFocus = ""; //Element que tendrà el focus de la pàgina.




    		String ofiInput = "";

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
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }

	private String organismesAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/organismes.jsp");

		try{

            
            
            

            AutoritzacionsUsuariData autUsuData = null;

            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
            ValoresFacade valores = ValoresFacadeUtil.getHome().create();

            String usuario=request.getRemoteUser();

            /* Gestió errors. */
               	String missatge = (String) request.getAttribute("missatge");
                String descMissatge = (String) request.getAttribute("descMissatge");
            	String mesInfoMissatge = "";
            	String indexInfoMissatge = "";
            	int j=0;
            	while ( request.getAttribute("mesInfoMissatge"+j)!=null ) {
            		mesInfoMissatge = mesInfoMissatge + "\t<p>"+((String) request.getAttribute("mesInfoMissatge"+j)).replaceAll ("\\n", "")+ "</p>";
            		j++;
            	}

            String organismeGestionar="";
            
            String valorAccio = ""; //Valor de l'acció a fer (alta, modificació)
            
            if ( request.getParameter("organismeGestionar") != null
            	&& !request.getParameter("organismeGestionar").equals("") ){
                organismeGestionar=request.getParameter("organismeGestionar").trim();
            }

            /* Cercam l'organisme */
            Vector organismes = new Vector();
            Vector historicOrganismes = new Vector();

            if (organismeGestionar!=null &&!organismeGestionar.equals("") ) {
            	organismes = autUsu.getOrganisme( organismeGestionar );
            	historicOrganismes = autUsu.getHistOrganisme(organismeGestionar);
            }

            //Si l atribut "init" és "init", buidam oficinaGestionar per a que ens presenti el formulari inicial.
            String ini= (request.getAttribute("init") != null ? (String) request.getAttribute("init"):"");
            if  ( ini.equals("init") ) {
            	organismeGestionar="";
            }

            String elementFocus = ""; //Element que tendrà el focus de la pàgina.




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

	private String totsOrganismesAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/totsOrganismes.jsp");

		try{            

            
            
            
            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
          
            Vector organismes=autUsu.getTotsOrganismes();
            int organismesSize=0;
            if (organismes!=null) organismesSize=organismes.size();
            request.setAttribute("organismes", organismes);
            request.setAttribute("organismesSize", new Integer(organismesSize));
          
        } catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }

	private String agrupacionsgeografiquesAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/agrupacionsgeografiques.jsp");

		try{            

            
            
            
            
            

            AutoritzacionsUsuariData autUsuData = null;

            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
            ValoresFacade valores = ValoresFacadeUtil.getHome().create();

            String usuario=request.getRemoteUser();

            /* Gestió errors. */
               	String missatge = (String) request.getAttribute("missatge");
                String descMissatge = (String) request.getAttribute("descMissatge");
            	String mesInfoMissatge = "";
            	String indexInfoMissatge = "";
            	int i=0;
            	while ( request.getAttribute("mesInfoMissatge"+i)!=null ) {
            		mesInfoMissatge = mesInfoMissatge + "\t<p>"+((String) request.getAttribute("mesInfoMissatge"+i)).replaceAll ("\\n", "")+ "</p>";
            		i++;
            	}


            String valorAccio = ""; //Valor de l'acció a fer (alta, modificació)

            String tipusAgruGeoGestionar="";
            String codiAgruGeoGestionar="";

            if ( request.getParameter("tipusAgruGeoGestionar") != null
            	&& !request.getParameter("tipusAgruGeoGestionar").equals("") ){
                tipusAgruGeoGestionar=request.getParameter("tipusAgruGeoGestionar").trim();
            }

            if ( request.getParameter("codiAgruGeoGestionar") != null
            	&& !request.getParameter("codiAgruGeoGestionar").equals("") ){
                codiAgruGeoGestionar=request.getParameter("codiAgruGeoGestionar").trim();
            }

            //Si l'atribut "init" és "init", buidam anyGestionar per a que ens presenti el formulari inicial.
            String ini= (request.getAttribute("init") != null ? (String) request.getAttribute("init"):"");
            if  ( ini.equals("init") ) {
            	tipusAgruGeoGestionar="";
            }


            /* Cercam l'entitat passada com a paràmetre */
            AgrupacioGeograficaData agruGeografiques = new AgrupacioGeograficaData();
            log.info("tipusAgruGeoGestionar="+tipusAgruGeoGestionar+" codiAgruGeoGestionar="+codiAgruGeoGestionar);
            if (tipusAgruGeoGestionar!=null && !tipusAgruGeoGestionar.equals("")
            		&& codiAgruGeoGestionar!=null && !codiAgruGeoGestionar.equals("")   ) {
            	agruGeografiques = autUsu.getAgrupacioGeografica(tipusAgruGeoGestionar, codiAgruGeoGestionar );
            	if (agruGeografiques!=null)log.info(agruGeografiques.toString());
            }

            String elementFocus = ""; //Element que tendrà el focus de la pàgina.
            
            



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
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }

	private String totesAgruGeoAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/totesAgruGeo.jsp");

		try{            

            
            
            
            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
          
            String subcadenaCodigo=(request.getParameter("subcadenaCodigo")==null) ? "" :request.getParameter("subcadenaCodigo").trim();
            String subcadenaTexto=(request.getParameter("subcadenaTexto")==null) ? "" : request.getParameter("subcadenaTexto").trim();

            Collection agrupacionsGeografiques = autUsu.getAgrupacionsGeografiques();
            int agrupacionsGeografiquesSize=0;
            if (agrupacionsGeografiques!=null) agrupacionsGeografiquesSize=agrupacionsGeografiques.size();
            
            request.setAttribute("subcadenaCodigo", subcadenaCodigo);
            request.setAttribute("subcadenaTexto", subcadenaTexto);
            request.setAttribute("agrupacionsGeografiques", agrupacionsGeografiques);
            request.setAttribute("agrupacionsGeografiquesSize",  new Integer(agrupacionsGeografiquesSize));
          
        } catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }

	private String entitatsAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/entitats.jsp");

		try{            

            
            
            
            
            

            AutoritzacionsUsuariData autUsuData = null;

            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
            ValoresFacade valores = ValoresFacadeUtil.getHome().create();

            String usuario=request.getRemoteUser();

            /* Gestió errors. */
               	String missatge = (String) request.getAttribute("missatge");
                String descMissatge = (String) request.getAttribute("descMissatge");
            	String mesInfoMissatge = "";
            	String indexInfoMissatge = "";
            	int i=0;
            	while ( request.getAttribute("mesInfoMissatge"+i)!=null ) {
            		mesInfoMissatge = mesInfoMissatge + "\t<p>"+((String) request.getAttribute("mesInfoMissatge"+i)).replaceAll ("\\n", "")+ "</p>";
            		i++;
            	}

            String entitatGestionar="";
            String subentitatGestionar="";

            if ( request.getParameter("entitatGestionar") != null
            	&& !request.getParameter("entitatGestionar").equals("") ){
                entitatGestionar=request.getParameter("entitatGestionar").trim();
            }

            if ( request.getParameter("subentitatGestionar") != null
            	&& !request.getParameter("subentitatGestionar").equals("") ){
                subentitatGestionar=request.getParameter("subentitatGestionar").trim();
            }

            //Si l'atribut "init" és "init", buidam anyGestionar per a que ens presenti el formulari inicial.
            String ini= (request.getAttribute("init") != null ? (String) request.getAttribute("init"):"");
            if  ( ini.equals("init") ) {
            	entitatGestionar="";
            	subentitatGestionar="";
            }


            /* Cercam l'entitat passada com a paràmetre */
            EntitatData entitats = new EntitatData();
            log.info("entitatGestionar="+entitatGestionar+" subentitatGestionar="+subentitatGestionar);
            if (entitatGestionar!=null && !entitatGestionar.equals("")
            		&& subentitatGestionar!=null && !subentitatGestionar.equals("")   ) {
            	entitats = autUsu.getEntitat( entitatGestionar, subentitatGestionar );
            	if ( !entitats.getCodigoEntidad().equals("") ) { //Si el codi no és buid, agafam el codi en castellà de l'entitat com a entitatGestionar 
            		entitatGestionar = entitats.getCodigoEntidad();
            	}
            }
            String elementFocus = ""; //Element que tendrà el focus de la pàgina.
            
            
            


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
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }

	private String totesEntitatsAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/totesEntitats.jsp");

		try{            

            
            
            
            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
          
            String subcadenaCodigo=(request.getParameter("subcadenaCodigo")==null) ? "" :request.getParameter("subcadenaCodigo").trim();
            String subcadenaTexto=(request.getParameter("subcadenaTexto")==null) ? "" : request.getParameter("subcadenaTexto").trim();

            Vector remitentes=autUsu.getEntitats(subcadenaCodigo, subcadenaTexto);
            int remitentesSize=0;
            if (remitentes!=null) remitentesSize=remitentes.size();
            
            request.setAttribute("subcadenaCodigo", subcadenaCodigo);
            request.setAttribute("subcadenaTexto", subcadenaTexto);
            request.setAttribute("remitentes", remitentes);
            request.setAttribute("remitentesSize",  new Integer(remitentesSize));
          
        } catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }
    
	private String oficinesFisiquesAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/oficinesFisiques.jsp");

		try{            

            
            
            
            
            

            AutoritzacionsUsuariData autUsuData = null;

            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
            ValoresFacade valores = ValoresFacadeUtil.getHome().create();

            /* Gestió errors. */
               	String missatge = (String) request.getAttribute("missatge");
                String descMissatge = (String) request.getAttribute("descMissatge");
            	String mesInfoMissatge = "";
            	String indexInfoMissatge = "";
            	int j=0;
            	while ( request.getAttribute("mesInfoMissatge"+j)!=null ) {
            		mesInfoMissatge = mesInfoMissatge + "\t<p>"+((String) request.getAttribute("mesInfoMissatge"+j)).replaceAll ("\\n", "")+ "</p>";
            		j++;
            	}

            String usuario=request.getRemoteUser();
            String oficinaGestionar="";
            String oficinaGestionarFisica="";

            String valorAccio = ""; //Valor de l'acció a fer (alta, modificació)

            if ( request.getParameter("oficinaGestionar") != null
            	&& !request.getParameter("oficinaGestionar").equals("") ){
                oficinaGestionar=request.getParameter("oficinaGestionar").trim();
            }
            if ( request.getParameter("oficinaGestionarFisica") != null
            	&& !request.getParameter("oficinaGestionarFisica").equals("") ){
                oficinaGestionarFisica=request.getParameter("oficinaGestionarFisica").trim();
            }

            /* Cercam l'oficina */
            Vector oficines = new Vector();
            Vector historicOficines = new Vector();

            if (oficinaGestionar!=null &&!oficinaGestionar.equals("") && oficinaGestionarFisica!=null &&!oficinaGestionarFisica.equals("") ) {
            	oficines = autUsu.getOficinaFisica( oficinaGestionar, oficinaGestionarFisica );
            	//historicOficines = autUsu.getHistOficina(oficinaGestionar);
            }

            //Si l'atribut "init" és "init", buidam oficinaGestionar per a que ens presenti el formulari inicial.
            String ini= (request.getAttribute("init") != null ? (String) request.getAttribute("init"):"");
            if  ( ini.equals("init") ) {
            	oficinaGestionar="";
            	oficinaGestionarFisica="";
            }

            String elementFocus = ""; //Element que tendrà el focus de la pàgina.

            String ofiInput="";

            boolean hayOficina = !(oficinaGestionar.equals("") || oficinaGestionarFisica.equals("") );
            boolean existeOficina = false;
            if (!hayOficina) {
                elementFocus="oficinaGestionar";
            } else {
                if ( oficines!=null && !oficines.get(0).toString().equals("")) {     		
        		    // Si la oficina existeix...	
        			existeOficina = true;		
            		valorAccio="nouNomOficinaFisica";
            		String Orgchecked="";

            		ofiInput="\n<tr>\n\t<th>Codi Oficina</th>\n\t<th>Codi Oficina F\u00edsica</th>\n\t<th>Descripci\u00f3 Oficina</th>\n\t</tr>\n\t<tr>\n\t\t<td></td>\n\t\t<td></td>\n\t</tr>";
            		ofiInput = ofiInput+"\n<tr>\n\t<td><input type=\"text\" name=\"codiOficina\" id=\"codiOficina\"  size=\"2\" maxlength=\"2\" readonly=\"true\" value=\""+oficinaGestionar+"\" style=\"width: 30px;\"</td>";
            		ofiInput = ofiInput+"\n\t<td><input type=\"text\" name=\"codiOficinaFisica\" id=\"codiOficinaFisica\"  size=\"4\" maxlength=\"4\" readonly=\"true\" value=\""+oficinaGestionarFisica+"\" style=\"width: 50px;\"/></td>";
            		ofiInput = ofiInput+"\n\t<td><input type=\"text\" name=\"descOficina\" id=\"descOficina\" size=\"20\" maxlength=\"20\"  value=\""+oficines.get(2).toString()+"\" style=\"width: 200px;\"/></td>\n\t</tr>";
            		ofiInput = ofiInput +"\n<tr>\n\t<td align=\"center\" colspan=\"2\"><input type=\"submit\" value=\"Actualitzar\"/></td>\n</tr>";
            		
            		/* ofiInput=ofiInput+"<tr>\n\t\t<th>Oficina</th>\n\t\t<th>Data alta<br/>(dd/mm/yyyy)</th>\n\t\t<th>Data baixa<br/>(dd/mm/yyyy)</th>\n\t</tr>";
            		if ( historicOficines.size()>4)
            			ofiInput = ofiInput+"\n<tr><td colspan=3>Hist\u00f2ric de l'oficina</td></tr>";
            		for (int i=0;i<historicOficines.size();i=i+4){
            			Orgchecked="";
            			String codigo=historicOficines.get(i).toString();
            		    String descripcion=historicOficines.get(i+1).toString();
            			String fecAlta=historicOficines.get(i+2).toString();
            			String fecBaixa=historicOficines.get(i+3).toString();

            			if (!fecBaixa.equals("0") && i<4 ) {
            				//El darrer històric d'oficina té data de baixa, aleshores no hi ha cap oficina activa, donam l'opció de crear d'activar
            				//l'oficina donant un nou nom i data d'alta!
            				elementFocus="descOficina";
            				ofiInput = ofiInput+"\n<tr>\n\t<td><input type=\"hidden\" name=\"codiOficina\" id=\"codiOficina\" readonly=\"true\" value=\""+oficinaGestionar+"\" />";
            				ofiInput = ofiInput+oficinaGestionar+" - <input type=\"text\" name=\"descOficina\" id=\"descOficina\" size=\"20\" maxlength=\"20\"  value=\"\" style=\"width: 200px;\"/></td>\n\t";
            				ofiInput = ofiInput+"\n\t<td><input type=\"text\" name=\"dataAlta\" id=\"dataAlta\" size=\"10\" maxlength=\"10\" value=\"\" style=\"width: 70px;\"/></td>\n\t";
            				ofiInput = ofiInput+"\n\t<td><input type=\"text\" readonly=\"true\" name=\"dataBaixa\" id=\"dataBaixa\" size=\"10\" maxlength=\"10\" value=\"0\" style=\"width: 70px;\"/></td>\n\t</tr>";
            				ofiInput = ofiInput +"\n<tr>\n\t<td align=\"center\" colspan=\"3\"><input type=\"submit\" value=\"Dona d'alta\"/></td>\n</tr>";
            				ofiInput = ofiInput +"\n<tr>\n\t<td align=\"center\" colspan=\"3\">&nbsp;</td>\n</tr>";
            				valorAccio="nouNomOficinaFisica";
            			}

            			if ( fecBaixa.equals("0") ) {
            				elementFocus="descOficina";
            				ofiInput = ofiInput+"\n\t<tr><td>"+codigo+" - "+"<input type=\"text\" readonly=\"true\" name=\"descOficina\"  id=\"descOficina\" size=\"20\" maxlength=\"20\"  value=\""+descripcion+"\" style=\"width: 200px;\"/></td>";
            			} else
            				ofiInput = ofiInput+"\n\t<tr><td>"+codigo+" - "+descripcion+"</td>";

            			ofiInput = ofiInput+"\n\t<td>"+fecAlta+"</td>";
            			ofiInput = ofiInput+"<input type=\"hidden\" name=\"dataAlta\" id=\"dataAlta\" value=\""+fecAlta+"\"/>";

            			if ( fecBaixa.equals("0") ) {
            				elementFocus="dataBaixa";
            				ofiInput = ofiInput+"\n\t<td><input type=\"text\" name=\"dataBaixa\" id=\"dataBaixa\" size=\"10\" maxlength=\"10\"  value=\"0\" style=\"width: 70px;\"/></td>";
            			} else {
            				ofiInput = ofiInput+"\n\t<td>"+fecBaixa+"</td>";
            			}

            			ofiInput = ofiInput+"\n</tr>\n";
            			if ( (fecBaixa.equals("0") && i<4) ) {
            				valorAccio="baixaOficinaFisica";
            				ofiInput = ofiInput +"<tr>\n\t<td align=\"center\" colspan=\"4\"><input type=\"submit\" value=\"Actualitza\"/></td></tr>";
            			}
            		} */

                } else {
        			//Si la oficina no existeix, l'hem de crear! 
        			valorAccio="altaOficinaFisica";
        			ofiInput="\n<tr>\n\t<th>Codi Oficina</th>\n\t<th>Codi Oficina F\u00edsica</th>\n\t<th>Descripci\u00f3 Oficina</th>\n\t</tr>\n\t<tr>\n\t\t<td></td>\n\t\t<td></td>\n\t</tr>";
        			ofiInput = ofiInput+"\n<tr>\n\t<td><input type=\"text\" name=\"codiOficina\" id=\"codiOficina\"  size=\"2\" maxlength=\"2\" readonly=\"true\" value=\""+oficinaGestionar+"\" style=\"width: 30px;\"/></td>";
        			ofiInput = ofiInput+"\n\t<td><input type=\"text\" name=\"codiOficinaFisica\" id=\"codiOficinaFisica\"  size=\"4\" maxlength=\"4\" readonly=\"true\" value=\""+oficinaGestionarFisica+"\" style=\"width: 50px;\"/></td>";
        			ofiInput = ofiInput+"\n\t<td><input type=\"text\" name=\"descOficina\" id=\"descOficina\" size=\"20\" maxlength=\"20\"  value=\"\" style=\"width: 200px;\"/></td>\n\t</tr>";
        			ofiInput = ofiInput +"\n<tr>\n\t<td align=\"center\" colspan=\"2\"><input type=\"submit\" value=\"Dona d'alta\"/></td>\n</tr>";
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
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }

	private String totesOficinesFisiquesAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/totesOficinesFisiques.jsp");

		try{            

            
            //
            
            //
            
            //AutoritzacionsUsuariData autUsuData = null;
            //AdminFacade autUsu = AdminFacadeUtil.getHome().create();
            ValoresFacade valores = ValoresFacadeUtil.getHome().create();
          
            Vector oficines=valores.buscarOficinasFisicasDescripcion("tots","totes");
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

	private String modelsOficisAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/modelsOficis.jsp");

		try{            

            
            
            

            AutoritzacionsUsuariData autUsuData = null;

            AdminFacade autUsu = AdminFacadeUtil.getHome().create();

            String usuario=request.getRemoteUser();

            /* Gestió errors. */
               	String missatge = (String) request.getAttribute("missatge");
                String descMissatge = (String) request.getAttribute("descMissatge");
            	String mesInfoMissatge = "";
            	String indexInfoMissatge = "";
            	int j=0;
            	while ( request.getAttribute("mesInfoMissatge"+j)!=null ) {
            		mesInfoMissatge = mesInfoMissatge + "\t<p>"+((String) request.getAttribute("mesInfoMissatge"+j)).replaceAll ("\\n", "")+ "</p>";
            		j++;
            	}


            String modelGestionar="";
            String valorAccio = ""; //Valor de l'acció a fer (alta, modificació)
            if ( request.getParameter("modelGestionar") != null
            	&& !request.getParameter("modelGestionar").equals("") ){
            	modelGestionar=request.getParameter("modelGestionar").trim();
            }

            /* Cercam l'organisme */
            Vector descModel = null;

            if (modelGestionar!=null &&!modelGestionar.equals("") ) {
            	descModel = autUsu.getModelOfici( modelGestionar );
            }

            String elementFocus = ""; //Element que tendrà el focus de la pàgina.



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
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }

	private String totsModelsOficisAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/totsModelsOficis.jsp");

		try{            

            
            //
            
            //
            
            //AutoritzacionsUsuariData autUsuData = null;
            //AdminFacade autUsu = AdminFacadeUtil.getHome().create();
            ValoresFacade valores = ValoresFacadeUtil.getHome().create();
          
            Vector models=valores.buscarModelos("tots","totes");
            int modelsSize=0;
            if (models!=null) modelsSize=models.size();

            request.setAttribute("models", models);
            request.setAttribute("modelsSize", new Integer(modelsSize));
          
        } catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }

	private String altaModelOficiAdmin(HttpServletRequest request, HttpSession sesion, HttpServletResponse response) {
		String resultado = new String("/admin/pages/altaModelOfici.jsp");

		try{            

            
            
            

            AutoritzacionsUsuariData autUsuData = null;

            AdminFacade autUsu = AdminFacadeUtil.getHome().create();

            String usuario=request.getRemoteUser();

            /* Gestió errors. */
               	String missatge = (String) request.getAttribute("missatge");
                String descMissatge = (String) request.getAttribute("descMissatge");
            	String mesInfoMissatge = "";
            	String indexInfoMissatge = "";
            	int j=0;
            	while ( request.getAttribute("mesInfoMissatge"+j)!=null ) {
            		mesInfoMissatge = mesInfoMissatge + "\t<p>"+((String) request.getAttribute("mesInfoMissatge"+j)).replaceAll ("\\n", "")+ "</p>";
            		j++;
            	}


            String modelGestionar="";
            String valorAccio = ""; //Valor de l'acció a fer (alta, modificació)
            if ( request.getParameter("modelGestionar") != null
            	&& !request.getParameter("modelGestionar").equals("") ){
            	modelGestionar=request.getParameter("modelGestionar").trim();
            }

            /* Cercam l'organisme */
            Vector descModel = null;

            if (modelGestionar!=null &&!modelGestionar.equals("") ) {
            	descModel = autUsu.getModelOfici( modelGestionar );
            }

            String elementFocus = ""; //Element que tendrà el focus de la pàgina.


    	    Logger log = Logger.getLogger(this.getClass());
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
     	   log.debug("Excepci\u00f3 tractada");
     	   log.debug(e);
        } catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }

	private String modelsRebutsAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/modelsRebuts.jsp");

		try{            

            
            
            

            AutoritzacionsUsuariData autUsuData = null;

            AdminFacade autUsu = AdminFacadeUtil.getHome().create();

            String usuario=request.getRemoteUser();

            /* Gestió errors. */
               	String missatge = (String) request.getAttribute("missatge");
                String descMissatge = (String) request.getAttribute("descMissatge");
            	String mesInfoMissatge = "";
            	String indexInfoMissatge = "";
            	int j=0;
            	while ( request.getAttribute("mesInfoMissatge"+j)!=null ) {
            		mesInfoMissatge = mesInfoMissatge + "\t<p>"+((String) request.getAttribute("mesInfoMissatge"+j)).replaceAll ("\\n", "")+ "</p>";
            		j++;
            	}


            String modelGestionar="";
            String valorAccio = ""; //Valor de l'acció a fer (alta, modificació)
            if ( request.getParameter("modelGestionar") != null
            	&& !request.getParameter("modelGestionar").equals("") ){
            	modelGestionar=request.getParameter("modelGestionar").trim();
            }

            /* Cercam l'organisme */
            Vector descModel = null;

            if (modelGestionar!=null &&!modelGestionar.equals("") ) {
            	descModel = autUsu.getModelRebut( modelGestionar );
            }

            String elementFocus = ""; //Element que tendrà el focus de la pàgina.






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
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }

	private String totsModelsRebutsAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/totsModelsRebuts.jsp");

		try{            

            
            //
            
            //
            
            //AutoritzacionsUsuariData autUsuData = null;
            //AdminFacade autUsu = AdminFacadeUtil.getHome().create();
            ValoresFacade valores = ValoresFacadeUtil.getHome().create();
          
            Vector models=valores.buscarModelosRecibos("tots","totes");
            int modelsSize=0;
            if (models!=null) modelsSize=models.size();

            request.setAttribute("models", models);
            request.setAttribute("modelsSize", new Integer(modelsSize));
          
        } catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }

	private String altaModelRebutAdmin(HttpServletRequest request, HttpSession sesion, HttpServletResponse response) {
		String resultado = new String("/admin/pages/altaModelRebut.jsp");

		try{            

            
            
            

            AutoritzacionsUsuariData autUsuData = null;

            AdminFacade autUsu = AdminFacadeUtil.getHome().create();

            String usuario=request.getRemoteUser();

            /* Gestió errors. */
               	String missatge = (String) request.getAttribute("missatge");
                String descMissatge = (String) request.getAttribute("descMissatge");
            	String mesInfoMissatge = "";
            	String indexInfoMissatge = "";
            	int j=0;
            	while ( request.getAttribute("mesInfoMissatge"+j)!=null ) {
            		mesInfoMissatge = mesInfoMissatge + "\t<p>"+((String) request.getAttribute("mesInfoMissatge"+j)).replaceAll ("\\n", "")+ "</p>";
            		j++;
            	}


            String modelGestionar="";
            String valorAccio = ""; //Valor de l'acció a fer (alta, modificació)
            if ( request.getParameter("modelGestionar") != null
            	&& !request.getParameter("modelGestionar").equals("") ){
            	modelGestionar=request.getParameter("modelGestionar").trim();
            }

            /* Cercam l'organisme */
            Vector descModel = null;

            if (modelGestionar!=null &&!modelGestionar.equals("") ) {
            	descModel = autUsu.getModelRebut( modelGestionar );
            }

            String elementFocus = ""; //Element que tendrà el focus de la pàgina.


    	    Logger log = Logger.getLogger(this.getClass());
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
     	   log.debug("Excepci\u00f3 tractada");
     	   log.debug(e);
        } catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }

	private String municipis060Admin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/municipis060.jsp");

		try{            

            
            
            
            
            

            AutoritzacionsUsuariData autUsuData = null;

            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
            ValoresFacade valores = ValoresFacadeUtil.getHome().create();

            String valorAccio = ""; //Valor de l'acció a fer (alta, modificació)

            String usuario=request.getRemoteUser();

            /* Gestió errors. */
               	String missatge = (String) request.getAttribute("missatge");
                String descMissatge = (String) request.getAttribute("descMissatge");
            	String mesInfoMissatge = "";
            	String indexInfoMissatge = "";
            	int i=0;
            	while ( request.getAttribute("mesInfoMissatge"+i)!=null ) {
            		mesInfoMissatge = mesInfoMissatge + "\t<p>"+((String) request.getAttribute("mesInfoMissatge"+i)).replaceAll ("\\n", "")+ "</p>";
            		i++;
            	}

            String mun060Gestionar="";

            if ( request.getParameter("mun060Gestionar") != null
            	&& !request.getParameter("mun060Gestionar").equals("") ){
                mun060Gestionar=request.getParameter("mun060Gestionar").toUpperCase().trim(); //IMPORTANT: Sempre empram majúscules pel codi de tipus de Municipi!
            }


            //Si l'atribut "init" és "init", buidam mun060Gestionar per a que ens presenti el formulari inicial.
            String ini= (request.getAttribute("init") != null ? (String) request.getAttribute("init"):"");
            if  ( ini.equals("init") ) {
            	mun060Gestionar="";
            }


            /* Cercam el municipi passada com a paràmetre */
            Municipi060Data municipi060 = new Municipi060Data();

            if (mun060Gestionar!=null && !mun060Gestionar.equals("") ) {
            	municipi060 = autUsu.getMunicipi060( mun060Gestionar );
            	if ( municipi060 !=null ) log.info(mun060Gestionar.toString());
            }

            String elementFocus = ""; //Element que tendrà el focus de la pàgina.
            
            
            



    		String readonly="";
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
        			readonly="readonly=\"true\""; //Quan es dona d'alta no es pot modificar la data de baixa!
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
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }

	private String totsMunicipis060Admin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/totsMunicipis060.jsp");

		try{            

            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
          
            Vector municipis060=autUsu.getMunicipis060();
            int municipis060Size=0;
            if (municipis060!=null) municipis060Size=municipis060.size();

            request.setAttribute("municipis060", municipis060);
            request.setAttribute("municipis060Size", new Integer(municipis060Size));
          
        } catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }

	private String totsTipusDocAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/totsTipusDoc.jsp");

		try{            

            
            
            
            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
          
            Vector tipusDocs=autUsu.getTipusDocuments();
            int tipusDocsSize=0;
            if (tipusDocs!=null) tipusDocsSize=tipusDocs.size();

            request.setAttribute("tipusDocs", tipusDocs);
            request.setAttribute("tipusDocsSize", new Integer(tipusDocsSize));
          
        } catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }

	private String traspassosAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/traspassos.jsp");

		try{            

        } catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }

	private String passaTraspassosAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/passaTraspassos.jsp");

		try{            

            	String urlPath = request.getContextPath();
            	Logger log = Logger.getLogger(this.getClass());

            /* Gestió errors. */
               	String missatge = (String) request.getAttribute("missatge");
                String descMissatge = (String) request.getAttribute("descMissatge");
            	String mesInfoMissatge = "";
            	String indexInfoMissatge = "";
            	int k=0;
            	while ( request.getAttribute("mesInfoMissatge"+k)!=null ) {
            		mesInfoMissatge = mesInfoMissatge + "\t<p>"+((String) request.getAttribute("mesInfoMissatge"+k)).replaceAll ("\\n", "")+ "</p>";
            		k++;
            	}

            //Si l'atribut "fitxer" no és buid, hem de parsejar-lo i agafar un registre per línia.
            String fitxer = (request.getAttribute("fitxer") != null ? (String) request.getAttribute("fitxer"):"");

            String nomFitxer = (request.getAttribute("nomFitxer") != null ? (String) request.getAttribute("nomFitxer"):"");

            int nombreLinies = 0;
            if ( fitxer!=null ) {
            	request.setAttribute("fitxer",fitxer);
            	log.info("fitxer="+fitxer);
            	fitxer = fitxer.trim();

               	try{
               		BufferedReader buffReader = new BufferedReader( new StringReader(fitxer) );
               		String tmp = "";
               		int j=0;
               		while ( (tmp = buffReader.readLine())!=null ) {
            			j++;
               			log.info("línia "+j+": "+tmp);   		
               		}
            		nombreLinies=j;
               	}catch(Exception ex){
               		ex.printStackTrace();
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
			log.debug("Capturam excepció estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }

	private String tipusDocumentsAdmin(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/pages/tipusDocuments.jsp");

		try{            

            
            
            
            
            

            AutoritzacionsUsuariData autUsuData = null;

            AdminFacade autUsu = AdminFacadeUtil.getHome().create();
            ValoresFacade valores = ValoresFacadeUtil.getHome().create();

            String valorAccio = ""; //Valor de l'acció a fer (alta, modificació)

            String usuario=request.getRemoteUser();

            /* Gestió errors. */
               	String missatge = (String) request.getAttribute("missatge");
                String descMissatge = (String) request.getAttribute("descMissatge");
            	String mesInfoMissatge = "";
            	String indexInfoMissatge = "";
            	int i=0;
            	while ( request.getAttribute("mesInfoMissatge"+i)!=null ) {
            		mesInfoMissatge = mesInfoMissatge + "\t<p>"+((String) request.getAttribute("mesInfoMissatge"+i)).replaceAll ("\\n", "")+ "</p>";
            		i++;
            	}

            String tipDocGestionar="";

            if ( request.getParameter("tipDocGestionar") != null
            	&& !request.getParameter("tipDocGestionar").equals("") ){
                tipDocGestionar=request.getParameter("tipDocGestionar").toUpperCase().trim(); //IMPORTANT: Sempre empram majúscules pel codi de tipus document!
            }


            //Si l'atribut "init" és "init", buidam anyGestionar per a que ens presenti el formulari inicial.
            String ini= (request.getAttribute("init") != null ? (String) request.getAttribute("init"):"");
            if  ( ini.equals("init") ) {
            	tipDocGestionar="";
            }


            /* Cercam l'entitat passada com a paràmetre */
            TipusDocumentData tipusDocuments = new TipusDocumentData();
            log.info("tipDocGestionar="+tipDocGestionar);
            if (tipDocGestionar!=null && !tipDocGestionar.equals("") ) {
            	tipusDocuments = autUsu.getTipusDocument( tipDocGestionar );
            	if ( tipusDocuments !=null ) log.info(tipusDocuments.toString());
            }

            String elementFocus = ""; //Element que tendrà el focus de la pàgina.



            String readonly="";
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
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
    	return resultado;
    }

}