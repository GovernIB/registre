

/*
 * Created on 1-aug-2007
 *
 * @author Sebastià Matas Riera
 *  
 */
package es.caib.regweb.webapp.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import es.caib.regweb.logic.helper.ParametrosRegistroSalida;
import org.apache.log4j.Logger;

import es.caib.regweb.logic.helper.RegwebException;
import es.caib.regweb.logic.interfaces.AdminFacade;
import es.caib.regweb.logic.util.AdminFacadeUtil;
import es.caib.regweb.logic.helper.EntitatData;
import es.caib.regweb.logic.interfaces.ValoresFacade;
import es.caib.regweb.logic.util.ValoresFacadeUtil;
import es.caib.regweb.logic.interfaces.RegistroSalidaFacade;
import es.caib.regweb.logic.util.RegistroSalidaFacadeUtil;

/**
 * Servlet Class
 *
 * @web.servlet              name="UtilFormacion"
 *                           display-name="Name for UtilFormacion"
 *                           description="Description for UtilFormacion"
 * @web.servlet-mapping      url-pattern="/utilFormacio"
 * 
 * @web.security-role-ref 	role-name = "RWE_ADMIN"
 * 							role-link = "RWE_ADMIN"
 *
 */
public class UtilAdminServlet extends UtilWebServlet {
	
	/**
	 * 
	 */
	private Logger log = null;
	
	public UtilAdminServlet() {
		super();
	}
	 
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		log = Logger.getLogger(this.getClass());
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = this.getServletConfig().getServletContext();

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

		HttpSession sesion = request.getSession();
		String accion = request.getParameter("accion").trim();
		String param = "/admin/controller.do?accion=index";
		
		//log.debug("Servlet, accion="+accion);
		if (accion!=null && !accion.equals(""))
			param = request.getParameter("accion").trim();
		
		if ("altaAutUsu".equals(accion)) param = altaAutUsu(request, sesion, "alta");
		
		if ("altaOficina".equals(accion)) param = altaOficina(request, sesion);
		if ("altaOficinaFisica".equals(accion)) param = altaOficinaFisica(request, sesion);
		if ("nouNomOficina".equals(accion)) param = actualitzaOficina(request, sesion);
		if ("nouNomOficinaFisica".equals(accion)) param = actualitzaOficinaFisica(request, sesion);
		if ("baixaOficina".equals(accion)) param = actualitzaOficina(request, sesion);

		if ("altaOrganisme".equals(accion)) param = altaOrganisme(request, sesion);
		if ("nouNomOrganisme".equals(accion)) param = actualitzaOrganisme(request, sesion);
		if ("baixaOrganisme".equals(accion)) param = actualitzaOrganisme(request, sesion);
		
		if ("altaOrganismesOficina".equals(accion)) param = altaOrgsOfi(request, sesion);
		
		if ("gestionaEntitat".equals(accion)) param = gestionaEntitat(request, sesion);
		
		if ("altaComptadorOficina".equals(accion)) param = altaComptadors(request, sesion);
		
		if ("actualitzaAgruGeo".equals(accion)) param = actualitzaAgrupacioGeografica(request, sesion);
		
		if ("altaAgruGeo".equals(accion)) param = altaAgrupacioGeografica(request, sesion);
		
		if ("actualitzaTipusDoc".equals(accion)) param = actualitzaTipusDoc(request, sesion);
		
		if ("altaTipusDoc".equals(accion)) param = altaTipusDoc(request, sesion);
		
		if ("actualitzaMunicipi060".equals(accion)) param = actualitzaMunicipi060(request, sesion);
		
		if ("altaMunicipi060".equals(accion)) param = altaMunicipi060(request, sesion);
		
		if ("altaTraspas".equals(accion)) {
			//A altaTraspas tornam el fitxer de sortida amb els nombres de registre etc. ja feim el response, no hem de
			//fer el dispatch url.
			param = altaTraspas(request, sesion, response);
		} else {
			//log.debug("param="+param);
			String url = response.encodeURL(param);
			context.getRequestDispatcher(url).forward(request, response);
		}
	}
	
	private String altaTraspas(HttpServletRequest request, HttpSession sesion, HttpServletResponse response) {
		String resultado = new String("/admin/controller.do?accion=index");
		request.setAttribute("init","init"); //Posam atribut a init per a que torni a la pàgina inicial.
		//log.debug("Dins altaTraspas (servlet)");
		String fitxer = (String) request.getAttribute("fitxer"); // Fitxer a parsejar
		String fitxerSortida = "";
		String usuari = ((String) sesion.getAttribute("codi_usuari")).toUpperCase();
		fitxer = (String) request.getParameter("fitxer").trim(); // Fitxer a parsejar
		String nomFitxer = (String) request.getParameter("nomFitxer").trim(); // Nom del fitxer a parsejar
		String oficina = request.getParameter("oficinaOnRegistrar").trim();
		String orgEmissor = request.getParameter("organismeEmissor").trim(); // Organisme emissor
		String numSortida="";
		String dataSortida = "";
		String anySortida = "";
		String descripcioOficina = "";
		
		
		try{            
			if (orgEmissor==null || orgEmissor.equals("") || oficina==null || oficina.equals("") )
				throw new RegwebException("No s'ha definit una oficina o un organisme emissor."); 
			//log.debug(fitxer);
			if (fitxer!=null) {
				/* Agafam els beans */
                RegistroSalidaFacade rs = RegistroSalidaFacadeUtil.getHome().create();				
                ParametrosRegistroSalida res = new ParametrosRegistroSalida();
				
				ValoresFacade valores = ValoresFacadeUtil.getHome().create();
				
				descripcioOficina = valores.recuperaDescripcionOficina(oficina);
				// Donam d'alta els registres en base al fitxer
				String extracte="";
				String extracte1="";
				String extracte2="";
				String extracte3="";
				String remetent="";
				String destiGeografic="";
				
				try{
					BufferedReader buffReader = new BufferedReader( new StringReader(fitxer) );
					String tmp = "";
					int j=0;
					boolean validat=true;
					
					// Primer validam que poguem donar d'alta tots els registres.
					while ( (tmp = buffReader.readLine())!=null ) {
						res= new ParametrosRegistroSalida();
						extracte="";
						extracte1="";
						extracte2="";
						extracte3="";
						remetent="";
						destiGeografic="";
						j++;
						extracte2=tmp.substring(0,12);
						extracte3=": "+tmp.substring(12,39).trim();
						remetent=tmp.substring(12,39).trim();
						destiGeografic=tmp.substring(69,72);
						extracte1=tmp.substring(124).trim();
//						log.debug("Hem de donar d'alta: destiGeo="+destiGeografic+" remetent="+remetent+" extracte="+extracte1+" "+extracte2+extracte3);
						extracte = extracte1+" "+extracte2+extracte3;
						// Usuario que está accediendo
						res.fijaUsuario(usuari);
						Calendar cal = Calendar.getInstance();
						SimpleDateFormat dfdata = new SimpleDateFormat("dd/MM/yyyy");
						SimpleDateFormat dfhora = new SimpleDateFormat("HH:mm:ss");
						// Fecha de la entrada.
						res.setdatasalida(dfdata.format(cal.getTime()));
						// Hora de la entrada: Se concatena en el campo "minuto" el identificador del 
						// Thread
						res.sethora(dfhora.format(cal.getTime()));
						// El usuario de pruebas U83541 tiene permisos para registrar entradas desde
						// las oficinas 11 y 12. Tablas BAGECOM y BZAUTOR / BZAUTOR01
						res.setoficina(oficina);
						res.setdata(dfdata.format(cal.getTime()));
						// El tipo de documento es una carta. (CA = "Carta". Tabla BZTDOCU01)
						res.settipo("CA");
						// El idioma del documento es catalàn. (2 = "Catalán". Tabla BZIDIOM)
						res.setidioma("2");
						// En esta prueba, el remitente es de balears: select * from bagruge 
						// where fabctagg = 90
						// En particular, usaremos Palma de Mallorca (40)
						res.setbalears(destiGeografic);
						// Destinatari			      
						res.setaltres(remetent);
						// Remitent
						res.setremitent(orgEmissor);
						// Indicamos el idioma del extracto (Catalán = CA)
						res.setidioex("2");
						// Extracto o comentario del registro explicando de qué se trata el doc.
						res.setcomentario(extracte);
						
						// Validamos.
						if (!rs.validar(res)){
							validat=false;
							log.error("Error en la consistencia de los datos");
							Hashtable ht = res.getErrores();
							log.error(ht.toString());
						}
						
						// Mostramos los errores si los hubo.
						Hashtable ht = res.getErrores();
						if ((ht != null) && !ht.isEmpty()){
							log.error("Registro de entrada con errores:\n" + ht);
						}
						
						// Grabamos
						//res.grabar();

					}
					
					
					if (validat) {
						// Si hem validat tot el fitxer, donam d'alta els registres
						buffReader = new BufferedReader( new StringReader(fitxer) );
						tmp="";
						log.debug("Trasp\u00E0s validat, ara donarem d'alta!");
						while ( (tmp = buffReader.readLine())!=null ) {
							res= new ParametrosRegistroSalida();
							extracte="";
							extracte1="";
							extracte2="";
							extracte3="";
							remetent="";
							destiGeografic="";
							j++;
							extracte2=tmp.substring(0,12);
							extracte3=": "+tmp.substring(12,39).trim();
							remetent=tmp.substring(12,39).trim();
							destiGeografic=tmp.substring(69,72);
							extracte1=tmp.substring(124).trim();
//							log.debug("Hem de donar d'alta: destiGeo="+destiGeografic+" remetent="+remetent+" extracte="+extracte1+" "+extracte2+extracte3);
							extracte = extracte1+" "+extracte2+extracte3;
							// Usuario que está accediendo
							res.fijaUsuario(usuari);
							Calendar cal = Calendar.getInstance();
							SimpleDateFormat dfdata = new SimpleDateFormat("dd/MM/yyyy");
							SimpleDateFormat dfhora = new SimpleDateFormat("HH:mm:ss");
							// Fecha de la entrada.
							res.setdatasalida(dfdata.format(cal.getTime()));
							// Hora de la entrada: Se concatena en el campo "minuto" el identificador del 
							// Thread
							res.sethora(dfhora.format(cal.getTime()));
							// El usuario de pruebas U83541 tiene permisos para registrar entradas desde
							// las oficinas 11 y 12. Tablas BAGECOM y BZAUTOR / BZAUTOR01
							res.setoficina(oficina);
							res.setdata(dfdata.format(cal.getTime()));
							// El tipo de documento es una carta. (CA = "Carta". Tabla BZTDOCU01)
							res.settipo("CA");
							// El idioma del documento es catalàn. (2 = "Catalán". Tabla BZIDIOM)
							res.setidioma("2");
							// En esta prueba, el remitente es de balears: select * from bagruge 
							// where fabctagg = 90
							// En particular, usaremos Palma de Mallorca (40)
							res.setbalears(destiGeografic);
							// Destinatari			      
							res.setaltres(remetent);
							// Remitent
							res.setremitent(orgEmissor);
							// Indicamos el idioma del extracto (Catalán = CA)
							res.setidioex("2");
							// Extracto o comentario del registro explicando de qué se trata el doc.
							res.setcomentario(extracte);
							// Validamos.
							if (!rs.validar(res)){
								validat=true;
								log.error("Error en la consistencia de los datos");
								Hashtable ht = res.getErrores();
								log.error(ht.toString());
							}
							// Mostramos los errores si los hubo.
							Hashtable ht = res.getErrores();
							if ((ht != null) && !ht.isEmpty()){
								log.error("Registro de entrada con errores:\n" + ht);
							}
							// Grabamos
							if (rs.grabar(res)) {
								//S'ha gravat correctament REGISTRE: SORTIDES  Núm. 14554 / 2007   Data  10-AGO-2007
								DateFormat dateFddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
								DateFormat dateFddMMMyyyy= new SimpleDateFormat("dd-MMM-yyyy");
								String tmpSortida="";
								numSortida =res.getNumeroSalida();
								dataSortida = res.getDataSalida();
								Date fechaTest = dateFddMMyyyy.parse(dataSortida);
								dataSortida = dateFddMMMyyyy.format(fechaTest).toUpperCase();
								anySortida = res.getAnoSalida();
								tmpSortida=tmp.substring(0,69);
								tmpSortida=tmpSortida+tmp.substring(72,124);
								//Atenció, no tocar els espais en blanc!
								tmpSortida=tmpSortida.concat("GOVERN ILLES BALEARS"+descripcioOficina+" REGISTRE: SORTIDES  Núm. "+numSortida+" / "+anySortida.trim()+"   Data  "+dataSortida);
								//log.debug(tmpSortida);
								fitxerSortida = fitxerSortida+tmpSortida+"\r\n";
							} else {
								throw new RegwebException("No s'ha pogut desar el registre -"+tmp+"- a la bbdd!"); 
							}

						}
					}
				}catch(Exception ex){
					ex.printStackTrace();
				}
			} else {
				log.error("Error, no hi ha fitxer!");
			}
			if (fitxerSortida!=null) {
				//Hem de mostrar el fitxer de sortida a l'usuari.
				request.setAttribute("fitxerSortida",fitxerSortida); //Ficam el fitxer de sortida com atribut del request.
				resultado = new String("/admin/controller.do?accion=davallaFitxerSortidaTraspassos");
				log.debug("-----------------");
				log.debug(fitxerSortida);
				log.debug("-----------------");
				response.setContentType("text/txt");
				response.setHeader("Content-Disposition",  "inline; filename=\"sortida_"+nomFitxer+"\"");
				response.setHeader("Cache-Control",  "store");
				response.setHeader("Pragma", "cache");
				
				StringReader sr = new StringReader(fitxerSortida);
				OutputStream out=response.getOutputStream();
				response.setHeader("Content-Length", ""+fitxerSortida.getBytes().length);
				//int llegir=0;
				//while ( (llegir=sr.read())!=-1 ) 
				//	out.write(llegir);
				out.write(fitxerSortida.getBytes());
				out.flush();
			}
		} catch(RegwebException RWEex) {
			log.debug("Capturam excepci\u00f3 regweb!");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", RWEex.getMessage());
			
			StackTraceElement elements[] = RWEex.getStackTrace();
			for (int i = 0, n = elements.length; i < n; i++) {       
				String linia = elements[i].getFileName() + ":" 
				+ elements[i].getLineNumber() 
				+ ">> " 
				+ elements[i].getMethodName() + "()\n";
				request.setAttribute("mesInfoMissatge"+i,linia);
			}
			request.setAttribute("init",""); //Buidam atribut "init" per a que segueixi a la pàgina actual.
			resultado = new String("/admin/controller.do?accion=passaTraspassos");
			request.setAttribute("fitxer",fitxer); // Fitxer a parsejar
		} catch(NumberFormatException NFe) {
			log.debug("Error: camp no num\u00e8ric.");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", "Error: camp no num\u00e8ric.");
			request.setAttribute("init",""); //Buidam atribut "init" per a que segueixi a la pàgina actual.
			resultado = new String("/admin/controller.do?accion=passaTraspassos");
			NFe.printStackTrace();
		} catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		return resultado;
	}
	
	private String altaAutUsu(HttpServletRequest request, HttpSession sesion, String altaModif) {
		String resultado = new String("/admin/controller.do?accion=autoritzUsuari");
		
		try{            
			String msg="";
			String idPropAct = "";
			request.setAttribute("init","init"); //Posam atribut a init per a que torni a la pàgina inicial.
			//log.debug("Dins altaAutUsu (servlet)");
			String AE[] = request.getParameterValues("AE"); // Autorització entrades
			String CE[] = request.getParameterValues("CE"); // Autorització CONSULTA entrades
			String AS[] = request.getParameterValues("AS"); // Autorització sortides
			String CS[] = request.getParameterValues("CS"); // Autorització CONSULTA sortides
			String VE[] = request.getParameterValues("VE"); // Autorització VISAT entrades
			String VS[] = request.getParameterValues("VS"); // Autorització VISAT sortides
			String usuariAut = request.getParameter("usuariAut").trim(); // Usuari a autoritzar.
			/*    		if (AE != null ) 
			 for (int i=0 ; i<AE.length;i++ )
			 log.debug("AE["+i+"]: "+AE[i]);
			 if (CE != null ) 
			 for (int i=0 ; i<CE.length;i++ )
			 log.debug("CE["+i+"]: "+CE[i]);
			 if (AS != null ) 
			 for (int i=0 ; i<AS.length;i++ )
			 log.debug("AS["+i+"]: "+AS[i]);
			 if (CS != null ) 
			 for (int i=0 ; i<CS.length;i++ )
			 log.debug("CS["+i+"]: "+CS[i]);
			 */
			/* Agafam el bean */
			AdminFacade autoritzaUsuari = AdminFacadeUtil.getHome().create();

			//Primer esborram totes les autoritzacions de l'usuari.
			autoritzaUsuari.deleteAutoritzacionsUsuari(usuariAut);
			
			//Ara anam donant les autoritzacions.
			if (AE != null ) 
				for (int i=0 ; i<AE.length;i++ ) {   				 
					//log.debug("Insertam: AE["+i+"]: "+AE[i]);
					autoritzaUsuari.addAutoritzacioUsuari(usuariAut,"AE",AE[i]);
				}
			
			if (CE != null ) 
				for (int i=0 ; i<CE.length;i++ ){
					//log.debug("Insertam: CE["+i+"]: "+CE[i]);
					autoritzaUsuari.addAutoritzacioUsuari(usuariAut,"CE",CE[i]);
				}
			
			if (AS != null ) 
				for (int i=0 ; i<AS.length;i++ ) {
					//log.debug("Insertam: AS["+i+"]: "+AS[i]);
					autoritzaUsuari.addAutoritzacioUsuari(usuariAut,"AS",AS[i]);
				}
			
			if (CS != null ) 
				for (int i=0 ; i<CS.length;i++ ) {
					//log.debug("Insertam: CS["+i+"]: "+CS[i]);
					autoritzaUsuari.addAutoritzacioUsuari(usuariAut,"CS",CS[i]);
				}

			if (VE != null ) 
				for (int i=0 ; i<VE.length;i++ ) {
					log.debug("Insertam: VE["+i+"]: "+VE[i]);
					autoritzaUsuari.addAutoritzacioUsuari(usuariAut,"VE",VE[i]);
				}

			if (VS != null ) 
				for (int i=0 ; i<VS.length;i++ ) {
					log.debug("Insertam: VS["+i+"]: "+VS[i]);
					autoritzaUsuari.addAutoritzacioUsuari(usuariAut,"VS",VS[i]);
				}

			if (msg != null){ 
				request.setAttribute("showMSG", "true");
				request.setAttribute("alertModul", msg);
			} 
			
		} catch(NumberFormatException NFe) {
			log.debug("Error: camp no num\u00e8ric.");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", "Error: camp no num\u00e8ric.");
			request.setAttribute("init",""); //Buidam atribut "init" per a que segueixi a la pàgina actual.
			resultado = new String("/admin/controller.do?accion=autoritzUsuari");
			NFe.printStackTrace();
		} catch(RegwebException RWEex) {
			log.debug("Capturam excepci\u00f3 regweb!");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", RWEex.getMessage());
			
			StackTraceElement elements[] = RWEex.getStackTrace();
			for (int i = 0, n = elements.length; i < n; i++) {       
				String linia = elements[i].getFileName() + ":" 
				+ elements[i].getLineNumber() 
				+ ">> " 
				+ elements[i].getMethodName() + "()\n";
				request.setAttribute("mesInfoMissatge"+i,linia);
			}
			request.setAttribute("init",""); //Buidam atribut "init" per a que segueixi a la pàgina actual.
			resultado = new String("/admin/controller.do?accion=autoritzUsuari");
		} catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}
		
		return resultado;
	}
	
	private String altaOrgsOfi(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/controller.do?accion=index");
		
		try{            
			String msg="";
			String idPropAct = "";
			//log.debug("Dins altaOrgsOfi (servlet)");
			String oficinaGestionar = request.getParameter("oficinaGestionar").trim(); // Oficina a gestionar.
			String orgs[] = request.getParameterValues("org"); // Organismes a autoritzar a l'oficina
			String rems[] = request.getParameterValues("rem"); // Organismes per a remitir 
			
			/*if (orgs != null ) 
				for (int i=0 ; i<orgs.length;i++ )
					log.debug("orgs["+i+"]: "+orgs[i]);
			 if (CE != null ) 
			 for (int i=0 ; i<CE.length;i++ )
			 log.debug("CE["+i+"]: "+CE[i]);
			 if (AS != null ) 
			 for (int i=0 ; i<AS.length;i++ )
			 log.debug("AS["+i+"]: "+AS[i]);
			 if (CS != null ) 
			 for (int i=0 ; i<CS.length;i++ )
			 log.debug("CS["+i+"]: "+CS[i]);
			 */
			/* Agafam el bean */
			AdminFacade autoritzaUsuari = AdminFacadeUtil.getHome().create();
			
			//Primer esborram tots els organismes de l'oficina.
			autoritzaUsuari.deleteOrgsOfi(oficinaGestionar);
			
			/* Ara insertam tots els organimes per l-oficina */ 
			if (orgs != null ) 
				for (int i=0 ; i<orgs.length;i++ ) {   				 
					//log.debug("Insertam a l'oficina "+oficinaGestionar+" l'organisme["+i+"]: "+orgs[i]);
					autoritzaUsuari.addOrganismeOficina(oficinaGestionar,orgs[i]);
				}
			
			//Primer esborram tots els organismes per a remisio.
			autoritzaUsuari.deleteNoRemsOfi(oficinaGestionar);
			
			/* Ara insertam tots els organimes per a remisio */ 
			if (rems != null ) 
				for (int i=0 ; i<rems.length;i++ ) {   				 
					//log.debug("Insertam per a remisio a l'oficina "+oficinaGestionar+" l'organisme["+i+"]: "+rems[i]);
					autoritzaUsuari.addNoRemetreOficina(oficinaGestionar,rems[i]);
				}

		} catch(RegwebException RWEex) {
			log.debug("Capturam excepci\u00f3 regweb!");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", RWEex.getMessage());
			
			StackTraceElement elements[] = RWEex.getStackTrace();
			for (int i = 0, n = elements.length; i < n; i++) {       
				String linia = elements[i].getFileName() + ":" 
				+ elements[i].getLineNumber() 
				+ ">> " 
				+ elements[i].getMethodName() + "()\n";
				request.setAttribute("mesInfoMissatge"+i,linia);
			}
			request.setAttribute("init",""); //Buidam atribut "init" per a que segueixi a la pàgina actual.
			resultado = new String("/admin/controller.do?accion=organismesOficina");
		} catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}
		return resultado;
	}    
	
	private String altaOrganisme(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/controller.do?accion=index");
		
		try{            
			String msg="";
			String idPropAct = "";
			//log.debug("Dins altaOrganisme (servlet)");
			String organismeGestionar = request.getParameter("organismeGestionar").trim(); // Oficina a gestionar.
			String descCurtaOrganisme = request.getParameter("descCurtaOrganisme").trim(); // Descripció curta de l'organisme
			String descLlargaOrganisme = request.getParameter("descLlargaOrganisme").trim(); // Descripció llarga de l'organisme
			String dataAlta = request.getParameter("dataAlta").trim(); // Data Alta (yymmdd)
			String dataAltaHistoric = dataAlta; // Data Alta de l'històric (yyyymmdd)
			
			/* Validam l'entrada de dades */
			if ( organismeGestionar.length()>4 || descCurtaOrganisme.length()>15 || descCurtaOrganisme.length()>40)
				throw new RegwebException("Camp massa llarg");
			
			// Validam la data 
			SimpleDateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat yymmdd=new SimpleDateFormat("yyMMdd");
			SimpleDateFormat ddmmyy=new SimpleDateFormat("dd/MM/yyyy");
			ddmmyy.setLenient(false);
			ddmmyyyy.setLenient(false);  // Important! Sino deixaria passar dates errònees com: "40/01/07"!
			Date dataAlt=null;
			Date dataDarreraBaix=null;
			//log.debug("La data de baixa original és: "+dataBaixa);
			try {
				dataAlt=ddmmyy.parse(dataAlta);
				dataAlta=yymmdd.format(dataAlt);
				dataAltaHistoric=yyyymmdd.format(dataAlt);
				//log.debug("La data de baixa és vàlida i és: "+dataBaixa);
			} catch (Exception e) {
				throw new RegwebException("Data no v\u00E0lida");
			}
			/* Agafam el bean */
			AdminFacade autoritzaUsuari = AdminFacadeUtil.getHome().create();
			
			//	Aquí hem de comprovar que la nova data d'alta és posterior a la darrera data de baixa de l'històric.
    		Vector historicOrganismes = new Vector();
    		historicOrganismes = autoritzaUsuari.getHistOrganisme(organismeGestionar);
    		String dataDarreraBaixa = (String) historicOrganismes.get(4);
    		if (!dataDarreraBaixa.equals("")) {
    			//Si té històric, feim la comprovació, en cas contrari no és necessari
    			dataDarreraBaix=ddmmyyyy.parse(dataDarreraBaixa);
    			if ( dataDarreraBaix.compareTo(dataAlt)>=0)
    				throw new RegwebException("La data d'alta ha de ser posterior a la darrera data de baixa!");
    		}
    		
			autoritzaUsuari.altaOrganisme(organismeGestionar, descCurtaOrganisme, descLlargaOrganisme, dataAlta );
			//Cream l'històric corresponent
			autoritzaUsuari.altaHistOrganisme(organismeGestionar, descCurtaOrganisme, descLlargaOrganisme, dataAltaHistoric);
			
		} catch(NumberFormatException NFe) {
			log.debug("Error: camp no num\u00e8ric.");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", "Error: camp no num\u00e8ric.");
			request.setAttribute("init",""); //Buidam atribut "init" per a que segueixi a la pàgina actual.
			resultado = new String("/admin/controller.do?accion=organismes");
			NFe.printStackTrace();
		} catch(RegwebException RWEex) {
			log.debug("Capturam excepci\u00f3 regweb!");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", RWEex.getMessage());
			
			StackTraceElement elements[] = RWEex.getStackTrace();
			for (int i = 0, n = elements.length; i < n; i++) {       
				String linia = elements[i].getFileName() + ":" 
				+ elements[i].getLineNumber() 
				+ ">> " 
				+ elements[i].getMethodName() + "()\n";
				request.setAttribute("mesInfoMissatge"+i,linia);
			}
			request.setAttribute("init",""); //Buidam atribut "init" per a que segueixi a la pàgina actual.
			resultado = new String("/admin/controller.do?accion=organismes");
		} catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}
		return resultado;
	}    
	
	private String actualitzaOrganisme(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/controller.do?accion=index");
		
		try{            
			String msg="";
			String idPropAct = "";
			//log.debug("Dins actualitzaOficina (servlet)");
			
			String codOrganisme = request.getParameter("organismeGestionar").trim(); // Oficina a gestionar.
			String descCurtaOrganisme = request.getParameter("descCurtaOrganisme").trim(); // Descripció curta de l'organisme
			String descLlargaOrganisme = request.getParameter("descLlargaOrganisme").trim(); // Descripció llarga de l'organisme
			String dataBaixa = request.getParameter("dataBaixa").trim(); // Data baixa
			String dataAlta = request.getParameter("dataAlta").trim(); // Data Alta
			String accio = request.getParameter("accion").trim(); // Acció a dur a terme (nouNomOrganisme,baixaOrganisme)
			String dataBaixayymmdd = dataBaixa;
			request.setAttribute("init","init"); //Posam atribut a init per a que torni a la pàgina inicial.
			//log.debug("dataAlta="+dataAlta+" dataAltaOrgOld="+dataAltaOrgOld+" dataBaixa="+dataBaixa);
			if (dataBaixa.equals(""))
				dataBaixa="0";
			
			/* Validam l'entrada de dades */
			if ( codOrganisme.length()>4 || descCurtaOrganisme.length()>15 || descCurtaOrganisme.length()>40)
				throw new RegwebException("Camp massa llarg");
			
			// Validam la data 
			SimpleDateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat yymmdd=new SimpleDateFormat("yyMMdd");
			SimpleDateFormat ddmmyy=new SimpleDateFormat("dd/MM/yyyy");
			ddmmyy.setLenient(false);
			ddmmyyyy.setLenient(false);  // Important! Sino deixaria passar dates errònees com: "40/01/07"!
			java.util.Date data=null;
			java.util.Date dataAlt=null;
			java.util.Date dataBaix=null;
			//log.debug("La data de baixa original és: "+dataBaixa);
			try {
				data=ddmmyyyy.parse(dataAlta);
				dataAlt=data;
				dataAlta=yyyymmdd.format(data);
				
				if (!dataBaixa.equals("0")) {//Si data baixa diferent a 0, l'hem de validar!
					data=ddmmyyyy.parse(dataBaixa);
					dataBaix=data;
					dataBaixa=yyyymmdd.format(data);
					
					data=ddmmyy.parse(dataBaixayymmdd);
					dataBaixayymmdd=yymmdd.format(data);
					log.debug("dataBaixa="+dataBaixa+" dataBaixayymmdd="+dataBaixayymmdd);
				}
				//log.debug("La data de baixa és vàlida i és: "+dataBaixa);
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("init",""); //Posam atribut a init per a que torni a la pàgina inicial.
				throw new RegwebException("Data no v\u00E0lida");
			}	
			
			/* Agafam el bean */
			AdminFacade autoritzaUsuari = AdminFacadeUtil.getHome().create();

			if ( !dataBaixa.equals("0") )
				if ( dataAlt.compareTo(dataBaix)>=0 )
				throw new RegwebException("La data de baixa és anterior a la data d'alta!");

			
			if (accio.equals("nouNomOrganisme")) {
				//Hem de crear una nova entrada a l'històric d'oficines amb les dades passades. També hem de posar 
				//la data de baixa de l'oficina a "0" a la taula d'oficines.
				//	Aquí hem de comprovar que la nova data d'alta és posterior a la darrera data de baixa de l'històric.
				Vector historicOrganismes = new Vector();
				historicOrganismes = autoritzaUsuari.getHistOrganisme(codOrganisme);
				Date dataDarreraBaix=null;
				String dataDarreraBaixa = (String) historicOrganismes.get(3);
				if (!dataDarreraBaixa.equals("")) {
					//Si té històric, feim la comprovació, en cas contrari no és necessari
					dataDarreraBaix=ddmmyyyy.parse(dataDarreraBaixa);
					if ( dataDarreraBaix.compareTo(dataAlt)>=0)
						throw new RegwebException("La data d'alta ha de ser posterior a la darrera data de baixa!");
				}
				autoritzaUsuari.altaHistOrganisme(codOrganisme, descCurtaOrganisme, descLlargaOrganisme, dataAlta);
				autoritzaUsuari.actualitzaOrganisme(codOrganisme, descCurtaOrganisme, descLlargaOrganisme, "0");
			}
			if (accio.equals("baixaOrganisme")) {
				//Donam de baixa a l'històric i després a la taula d'oficines.
				autoritzaUsuari.actualitzaHistOrganisme(codOrganisme, descCurtaOrganisme, descLlargaOrganisme, dataAlta, dataBaixa);
				autoritzaUsuari.actualitzaOrganisme(codOrganisme, descCurtaOrganisme, descLlargaOrganisme, dataBaixayymmdd);
			}
		} catch(NumberFormatException NFe) {
			log.debug("Error: camp no num\u00e8ric.");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", "Error: camp no num\u00e8ric.");
			request.setAttribute("init",""); //Buidam atribut "init" per a que segueixi a la pàgina actual.
			resultado = new String("/admin/controller.do?accion=organismes");
			NFe.printStackTrace();
		} catch(RegwebException RWEex) {
			log.debug("Capturam excepci\u00f3 regweb!");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", RWEex.getMessage());
			
			StackTraceElement elements[] = RWEex.getStackTrace();
			for (int i = 0, n = elements.length; i < n; i++) {       
				String linia = elements[i].getFileName() + ":" 
				+ elements[i].getLineNumber() 
				+ ">> " 
				+ elements[i].getMethodName() + "()\n";
				request.setAttribute("mesInfoMissatge"+i,linia);
			}
			request.setAttribute("init",""); //Posam atribut a init per a que torni a la pàgina inicial.
			resultado="/admin/controller.do?accion=organismes";
		} catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}
		
		return resultado;
	}    

	
	private String gestionaEntitat(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/controller.do?accion=index");
		
		try{        
			String dataBaixa = "0";
			String msg="";
			String idPropAct = "";
			request.setAttribute("init","init"); //Posam atribut a init per a que torni a la pàgina inicial.
			//log.debug("Dins gestionaEntitat (servlet)");
			String codEntidad = request.getParameter("codEntidad").trim(); // Codi entitat a gestionar (castella)
			String codEntitat = request.getParameter("codEntitat").trim(); // Codi entitat a gestionar (catala)
			String subcodEntitat = request.getParameter("subcodEntitat").trim(); // Codi de SUBentitat a gestionar
			String descEntidad = request.getParameter("descEntidad").trim(); // Descripció llarga de l'entitat (castellà)
			String descEntitat = request.getParameter("descEntitat").trim(); // Descripció llarga de l'entitat (català)
			
			if(request.getParameter("dataBaixa")!=null && !(request.getParameter("dataBaixa").equals(""))){
				dataBaixa = request.getParameter("dataBaixa").trim(); // Data baixa
				SimpleDateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
				Date data;
				data = ddmmyyyy.parse(dataBaixa);
				dataBaixa=yyyymmdd.format(data);	
			}
			
			//log.debug("codEntidad: "+codEntidad+" subcodEntitat: "+subcodEntitat+" dataBaixa:"+dataBaixa);
			if (dataBaixa.equals(""))
				dataBaixa="0";
			
			/* Validam l'entrada de dades */
			if ( codEntidad.length()>7 || codEntitat.length()>7 || subcodEntitat.length()>3 || descEntidad.length()>30 || descEntitat.length()>30
					|| dataBaixa.length()>10 )
				throw new RegwebException("Camp massa llarg");
			/* Agafam el bean */
			AdminFacade autoritzaUsuari = AdminFacadeUtil.getHome().create();
			
			//Primer miram si el codi d'entitat ja existeix, si ja existeix, feim update, en cas contrari, insert.
			EntitatData entitats = null;
			entitats = autoritzaUsuari.getEntitat( codEntidad, subcodEntitat );
			log.debug("Miram si l'entitat ja existeix: codEntitat="+codEntitat+" subcodEntiatat="+subcodEntitat+" "+entitats.toString());
			if (entitats.getCodiEntitat().equals("")) {
				log.debug("Entidad "+codEntidad+" "+codEntitat+" "+subcodEntitat+" no existeix, hem de fer insert!");
				autoritzaUsuari.altaEntitat(codEntidad, codEntitat, subcodEntitat, descEntidad, descEntitat, dataBaixa);
			} else {
				log.debug("Entitat "+codEntidad+" "+subcodEntitat+" ja existeix, hem de fer update!");
				log.debug(entitats.toString());
				//autoritzaUsuari.delEntitat(codEntidad, codEntitat, subcodEntitat, descEntidad, descEntitat, dataBaixa);
				autoritzaUsuari.actualitzaEntitat(codEntidad, codEntitat, subcodEntitat, descEntidad, descEntitat, dataBaixa);
			}
		} catch(NumberFormatException NFe) {
			log.debug("Error: camp no num\u00e8ric.");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", "Error: camp no num\u00e8ric.");
			request.setAttribute("init",""); //Buidam atribut "init" per a que segueixi a la pàgina actual.
			resultado = new String("/admin/controller.do?accion=entitats");
			NFe.printStackTrace();
		} catch(RegwebException RWEex) {
			log.debug("Capturam excepci\u00f3 regweb!");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", RWEex.getMessage());
			
			StackTraceElement elements[] = RWEex.getStackTrace();
			for (int i = 0, n = elements.length; i < n; i++) {       
				String linia = elements[i].getFileName() + ":" 
				+ elements[i].getLineNumber() 
				+ ">> " 
				+ elements[i].getMethodName() + "()\n";
				request.setAttribute("mesInfoMissatge"+i,linia);
			}
			resultado="/admin/controller.do?accion=entitats";
			request.setAttribute("init",""); //Buidam atribut a init per a torni a la pàgina de dades.
		} catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}
		return resultado;
	}    
	
	private String altaOficina(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/controller.do?accion=index");
		
		try{            
			String msg="";
			String idPropAct = "";
			log.debug("Dins altaOficina (servlet)");
			String oficinaGestionar = request.getParameter("oficinaGestionar").trim(); // Oficina a gestionar.
			String descOficina = request.getParameter("descOficina").trim(); // Descripció de l'oficina
			String dataAlta = request.getParameter("dataAlta").trim(); // Data Alta (yymmdd)
			String dataAltaHistoric = dataAlta; // Data Alta de l'històric (yyyymmdd)
			/* Validam l'entrada de dades */
			if ( oficinaGestionar.length()>2 || descOficina.length()>20)
				throw new RegwebException("Camp massa llarg");
			
			// Validam la data 
			SimpleDateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat yymmdd=new SimpleDateFormat("yyMMdd");
			SimpleDateFormat ddmmyy=new SimpleDateFormat("dd/MM/yyyy");
			ddmmyy.setLenient(false);
			ddmmyyyy.setLenient(false);  // Important! Sino deixaria passar dates errònees com: "40/01/07"!
			Date dataAlt=null;
			Date dataDarreraBaix=null;
			//log.debug("La data de baixa original és: "+dataBaixa);
			try {
				dataAlt=ddmmyy.parse(dataAlta);
				dataAlta=yymmdd.format(dataAlt);
				dataAltaHistoric=yyyymmdd.format(dataAlt);
				//log.debug("La data de baixa és vàlida i és: "+dataBaixa);
			} catch (Exception e) {
				throw new RegwebException("Data no v\u00E0lida");
			}
			
			/* Agafam el bean */
			AdminFacade autoritzaUsuari = AdminFacadeUtil.getHome().create();
			
			//	Aquí hem de comprovar que la nova data d'alta és posterior a la darrera data de baixa de l'històric.
			Vector historicOficines = new Vector();
			historicOficines = autoritzaUsuari.getHistOficina(oficinaGestionar);
			String dataDarreraBaixa = (String) historicOficines.get(3);
			if (!dataDarreraBaixa.equals("")) {
				//Si té històric, feim la comprovació, en cas contrari no és necessari
				dataDarreraBaix=ddmmyyyy.parse(dataDarreraBaixa);
				if ( dataDarreraBaix.compareTo(dataAlt)>=0)
					throw new RegwebException("La data d'alta ha de ser posterior a la darrera data de baixa!");
			}
			//Cream l'oficina
			autoritzaUsuari.altaOficina(oficinaGestionar, descOficina, dataAlta );
			//Cream l'històric corresponent
			autoritzaUsuari.altaHistOficina(oficinaGestionar, descOficina, dataAltaHistoric);
			
		} catch(RegwebException RWEex) {
			log.debug("Capturam excepci\u00f3 regweb!");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", RWEex.getMessage());
			
			StackTraceElement elements[] = RWEex.getStackTrace();
			for (int i = 0, n = elements.length; i < n; i++) {       
				String linia = elements[i].getFileName() + ":" 
				+ elements[i].getLineNumber() 
				+ ">> " 
				+ elements[i].getMethodName() + "()\n";
				request.setAttribute("mesInfoMissatge"+i,linia);
			}
			resultado="/admin/controller.do?accion=oficines";
		} catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			
			ex.printStackTrace();
		}
		return resultado;
	}    
	
	
	private String actualitzaOficina(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/controller.do?accion=index");
		
		try{            
			String msg="";
			String idPropAct = "";
			//log.debug("Dins actualitzaOficina (servlet)");
			String codOficina = request.getParameter("oficinaGestionar").trim(); // Codi Oficina a gestionar
			String descOficina = request.getParameter("descOficina").trim(); // Descripció llarga de l'Oficina 
			String dataBaixa = request.getParameter("dataBaixa").trim(); // Data baixa
			String dataAlta = request.getParameter("dataAlta").trim(); // Data Alta
			String accio = request.getParameter("accion").trim(); // Acció a dur a terme (nouNomOficina,baixaOficina)
			String dataBaixayymmdd = dataBaixa;
			request.setAttribute("init","init"); //Posam atribut a init per a que torni a la pàgina inicial.
			log.debug("dataAlta="+dataAlta+" dataBaixa="+dataBaixa);
			if (dataBaixa.equals(""))
				dataBaixa="0";
			
			/* Validam l'entrada de dades */
			if ( codOficina.length()>2 || descOficina.length()>20)
				throw new RegwebException("Camp massa llarg");
			
			// Validam la data 
			SimpleDateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat yymmdd=new SimpleDateFormat("yyMMdd");
			SimpleDateFormat ddmmyy=new SimpleDateFormat("dd/MM/yyyy");
			ddmmyy.setLenient(false);
			ddmmyyyy.setLenient(false);  // Important! Sino deixaria passar dates errònees com: "40/01/07"!
			java.util.Date data=null;
			java.util.Date dataAlt=null;
			java.util.Date dataBaix=null;
			//log.debug("La data de baixa original és: "+dataBaixa);
			try {
				data=ddmmyyyy.parse(dataAlta);
				dataAlt=data;
				dataAlta=yyyymmdd.format(data);
				
				if (!dataBaixa.equals("0")) {
					data=ddmmyyyy.parse(dataBaixa);
					dataBaix=data;
					dataBaixa=yyyymmdd.format(data);
					
					data=ddmmyy.parse(dataBaixayymmdd);
					dataBaixayymmdd=yymmdd.format(data);
					log.debug("dataBaixa="+dataBaixa+" dataBaixayymmdd="+dataBaixayymmdd);
				}
				//log.debug("La data de baixa és vàlida i és: "+dataBaixa);
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("init",""); //Posam atribut a init per a que torni a la pàgina inicial.
				throw new RegwebException("Data no v\u00E0lida");
			}	
			
			/* Agafam el bean */
			AdminFacade autoritzaUsuari = AdminFacadeUtil.getHome().create();

			if ( !dataBaixa.equals("0") )
				if ( dataAlt.compareTo(dataBaix)>=0 )
				throw new RegwebException("La data de baixa és anterior a la data d'alta!");

			
			if (accio.equals("nouNomOficina")) {
				//Hem de crear una nova entrada a l'històric d'oficines amb les dades passades. També hem de posar 
				//la data de baixa de l'oficina a "0" a la taula d'oficines.
				//	Aquí hem de comprovar que la nova data d'alta és posterior a la darrera data de baixa de l'històric.
				Vector historicOficines = new Vector();
				historicOficines = autoritzaUsuari.getHistOficina(codOficina);
				Date dataDarreraBaix=null;
				String dataDarreraBaixa = (String) historicOficines.get(3);
				if (!dataDarreraBaixa.equals("")) {
					//Si té històric, feim la comprovació, en cas contrari no és necessari
					dataDarreraBaix=ddmmyyyy.parse(dataDarreraBaixa);
					if ( dataDarreraBaix.compareTo(dataAlt)>=0)
						throw new RegwebException("La data d'alta ha de ser posterior a la darrera data de baixa!");
				}
				autoritzaUsuari.altaHistOficina(codOficina, descOficina, dataAlta);
				autoritzaUsuari.actualitzaOficina(codOficina, descOficina, "0");
			}
			if (accio.equals("baixaOficina")) {
				//Donam de baixa a l'històric i després a la taula d'oficines.
				autoritzaUsuari.actualitzaHistOficina(codOficina, descOficina, dataAlta, dataBaixa);
				autoritzaUsuari.actualitzaOficina(codOficina, descOficina, dataBaixayymmdd);
			}
		} catch(NumberFormatException NFe) {
			log.debug("Error: camp no num\u00e8ric.");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", "Error: camp no num\u00e8ric.");
			request.setAttribute("init",""); //Buidam atribut "init" per a que segueixi a la pàgina actual.
			resultado = new String("/admin/controller.do?accion=oficines");
			NFe.printStackTrace();
		} catch(RegwebException RWEex) {
			log.debug("Capturam excepci\u00f3 regweb!");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", RWEex.getMessage());
			
			StackTraceElement elements[] = RWEex.getStackTrace();
			for (int i = 0, n = elements.length; i < n; i++) {       
				String linia = elements[i].getFileName() + ":" 
				+ elements[i].getLineNumber() 
				+ ">> " 
				+ elements[i].getMethodName() + "()\n";
				request.setAttribute("mesInfoMissatge"+i,linia);
			}
			request.setAttribute("init",""); //Posam atribut a init per a que torni a la pàgina inicial.
			resultado="/admin/controller.do?accion=oficines";
		} catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}
		
		return resultado;
	}    
	
	private String altaOficinaFisica(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/controller.do?accion=index");
		
		try{            
			String msg="";
			String idPropAct = "";
			log.debug("Dins altaOficinaFisica (servlet)");
			String oficinaGestionar = request.getParameter("oficinaGestionar").trim(); // Oficina a gestionar.
			String oficinaGestionarFisica = request.getParameter("oficinaGestionarFisica").trim(); // Oficina a gestionar.
			String descOficina = request.getParameter("descOficina").trim(); // Descripció de l'oficina
			String dataAlta = "";//request.getParameter("dataAlta").trim(); // Data Alta (yymmdd)
			String dataAltaHistoric = dataAlta; // Data Alta de l'històric (yyyymmdd)
			/* Validam l'entrada de dades */
			if ( oficinaGestionar.length()>2 || oficinaGestionarFisica.length()>4 || descOficina.length()>20)
				throw new RegwebException("Camp massa llarg");
			
			// Validam la data 
			SimpleDateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat yymmdd=new SimpleDateFormat("yyMMdd");
			SimpleDateFormat ddmmyy=new SimpleDateFormat("dd/MM/yyyy");
			ddmmyy.setLenient(false);
			ddmmyyyy.setLenient(false);  // Important! Sino deixaria passar dates errònees com: "40/01/07"!
			Date dataAlt=null;
			Date dataDarreraBaix=null;
			//log.debug("La data de baixa original és: "+dataBaixa);
//			try {
//				dataAlt=ddmmyy.parse(dataAlta);
//				dataAlta=yymmdd.format(dataAlt);
//				dataAltaHistoric=yyyymmdd.format(dataAlt);
//				//log.debug("La data de baixa és vàlida i és: "+dataBaixa);
//			} catch (Exception e) {
//				throw new RegwebException("Data no v\u00E0lida");
//			}
			
			/* Agafam el bean */
			AdminFacade autoritzaUsuari = AdminFacadeUtil.getHome().create();
			
			//	Aquí hem de comprovar que la nova data d'alta és posterior a la darrera data de baixa de l'històric.
//			Vector historicOficines = new Vector();
//			historicOficines = autoritzaUsuari.getHistOficina(oficinaGestionar);
//			String dataDarreraBaixa = (String) historicOficines.get(3);
//			if (!dataDarreraBaixa.equals("")) {
//				//Si té històric, feim la comprovació, en cas contrari no és necessari
//				dataDarreraBaix=ddmmyyyy.parse(dataDarreraBaixa);
//				if ( dataDarreraBaix.compareTo(dataAlt)>=0)
//					throw new RegwebException("La data d'alta ha de ser posterior a la darrera data de baixa!");
//			}
			//Cream l'oficina
			autoritzaUsuari.altaOficinaFisica(oficinaGestionar, oficinaGestionarFisica, descOficina, dataAlta );
			//Cream l'històric corresponent
			//autoritzaUsuari.altaHistOficina(oficinaGestionar, descOficina, dataAltaHistoric);
			
		} catch(RegwebException RWEex) {
			log.debug("Capturam excepci\u00f3 regweb!");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", RWEex.getMessage());
			
			StackTraceElement elements[] = RWEex.getStackTrace();
			for (int i = 0, n = elements.length; i < n; i++) {       
				String linia = elements[i].getFileName() + ":" 
				+ elements[i].getLineNumber() 
				+ ">> " 
				+ elements[i].getMethodName() + "()\n";
				request.setAttribute("mesInfoMissatge"+i,linia);
			}
			RWEex.printStackTrace();
			resultado="/admin/controller.do?accion=oficinesFisiques";
		} catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			
			ex.printStackTrace();
		}
		return resultado;
	}    
	
	
	private String actualitzaOficinaFisica(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/controller.do?accion=index");
		
		try{            
			//log.debug("Dins actualitzaOficina (servlet)");
			String codOficina = request.getParameter("codiOficina").trim(); // Codi Oficina a gestionar
			String codOficinaFisica = request.getParameter("codiOficinaFisica").trim(); // Codi Oficina a gestionar
			String descOficina = request.getParameter("descOficina").trim(); // Descripció llarga de l'Oficina 
			String accio = request.getParameter("accion").trim(); // Acció a dur a terme (nouNomOficina,baixaOficina)
			request.setAttribute("init","init"); //Posam atribut a init per a que torni a la pàgina inicial.
			
			/* Validam l'entrada de dades */
			if ( codOficina.length()>2 || descOficina.length()>20)
				throw new RegwebException("Camp massa llarg");
			
			
			/* Agafam el bean */
			AdminFacade autoritzaUsuari = AdminFacadeUtil.getHome().create();

			
			if (accio.equals("nouNomOficinaFisica")) {
				autoritzaUsuari.actualitzaOficinaFisica(codOficina, codOficinaFisica, descOficina, "0");
			}
		} catch(NumberFormatException NFe) {
			log.debug("Error: camp no num\u00e8ric.");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", "Error: camp no num\u00e8ric.");
			request.setAttribute("init",""); //Buidam atribut "init" per a que segueixi a la pàgina actual.
			resultado = new String("/admin/controller.do?accion=oficines");
			NFe.printStackTrace();
		} catch(RegwebException RWEex) {
			log.debug("Capturam excepci\u00f3 regweb!");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", RWEex.getMessage());
			
			StackTraceElement elements[] = RWEex.getStackTrace();
			for (int i = 0, n = elements.length; i < n; i++) {       
				String linia = elements[i].getFileName() + ":" 
				+ elements[i].getLineNumber() 
				+ ">> " 
				+ elements[i].getMethodName() + "()\n";
				request.setAttribute("mesInfoMissatge"+i,linia);
			}
			request.setAttribute("init",""); //Posam atribut a init per a que torni a la pàgina inicial.
			resultado="/admin/controller.do?accion=oficinesFisiques";
		} catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}
		
		return resultado;
	}    
	
		
	private String altaComptadors(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/controller.do?accion=index");
		
		try{            
			String msg="";
			String idPropAct = "";
			log.debug("Dins altaComptadors (servlet)");
			String oficinesAltaComptador[] = request.getParameterValues("Ini"); // Oficines on s'ha de donar l'alta del comptador
			String anyGestionar = request.getParameter("anyGestionar").trim(); // Oficina a gestionar.
			
			request.setAttribute("init","init"); //Posam atribut a init per a que torni a la pàgina inicial.
			if (oficinesAltaComptador != null ) 
				for (int i=0 ; i<oficinesAltaComptador.length;i++ )
					log.debug("oficinesAltaComptador["+i+"]: "+oficinesAltaComptador[i]);
			/* Agafam el bean */
			AdminFacade autoritzaUsuari = AdminFacadeUtil.getHome().create();
			
			//Primer esborram tots els organismes de l'organisme.
			//autoritzaUsuari.altaOrganisme(anyGestionar, descCurtaOrganisme, descLlargaOrganisme, dataAlta );
			
			//Ara anam inicialitzant els comptadors.
			if (oficinesAltaComptador != null ) 
				for (int i=0 ; i<oficinesAltaComptador.length;i++ ) {   				 
					//log.debug("Miram si hem d'insertam: oficinesAltaComptador["+i+"]: "+oficinesAltaComptador[i]);
					//Primer miram que per aquest any i oficina no estigui inicialitzat.
					if ( !autoritzaUsuari.existComptador(anyGestionar, oficinesAltaComptador[i]) ) { 
						log.debug("Insertam: oficinesAltaComptador["+i+"]: "+oficinesAltaComptador[i]);
						autoritzaUsuari.altaComptador(anyGestionar,"E",oficinesAltaComptador[i]);
						autoritzaUsuari.altaComptador(anyGestionar,"S",oficinesAltaComptador[i]);
						autoritzaUsuari.altaComptador(anyGestionar,"O",oficinesAltaComptador[i]);
					} else {
						log.debug("El comptador per l'oficina ["+oficinesAltaComptador[i]+"] ja existeix, no tractam com una excepció.");
						throw new RegwebException("El comptador per l'oficina ["+oficinesAltaComptador[i]+"] ja existeix!");
					}
				}
			
		} catch(RegwebException RWEex) {
			log.debug("Capturam excepci\u00f3 regweb!");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", RWEex.getMessage());
			
			StackTraceElement elements[] = RWEex.getStackTrace();
			for (int i = 0, n = elements.length; i < n; i++) {       
				String linia = elements[i].getFileName() + ":" 
				+ elements[i].getLineNumber() 
				+ ">> " 
				+ elements[i].getMethodName() + "()\n";
				request.setAttribute("mesInfoMissatge"+i,linia);
			}
			request.setAttribute("init",""); //Buidam atribut "init" per a que segueixi a la pàgina actual.
			resultado = new String("/admin/controller.do?accion=comptadors");
		} catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}
		return resultado;
	}    
	
	private String altaAgrupacioGeografica(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/controller.do?accion=index");
		
		try{            
			request.setAttribute("init","init"); //Posam atribut a init per a que torni a la pàgina inicial.
			String msg="";
			String idPropAct = "";
			log.debug("Dins altaAgrupacioGeografica (servlet)");
			String codTipuAgruGeo = request.getParameter("codTipuAgruGeo").trim(); // Codi tipus agrupació geogràfica a gestionar.
			String codAgruGeo = request.getParameter("codAgruGeo").trim(); // Codi agrupació geogràfica a gestionar.
			String descAgruGeo = request.getParameter("descAgruGeo").trim(); // Descripció agrupació geogràfica
			String dataBaixa = request.getParameter("dataBaixa").trim(); // Data baixa
			String codTipusAgruGeoSuperior = request.getParameter("codTipusAgruGeoSuperior").trim(); // codi tipus agrupació geogràfica superior
			String codAgruGeoSuperior = request.getParameter("codAgruGeoSuperior").trim(); // codi agrupació geogràfica superior
			String codiPostal = request.getParameter("codiPostal").trim(); // Codi postal de l'agrupació geogràfica
			
			/* Validam l'entrada de dades */
			if ( codTipuAgruGeo.length()>2 || codAgruGeo.length()>3 || descAgruGeo.length() >30 
					|| codTipusAgruGeoSuperior.length() > 2 || codAgruGeoSuperior.length() > 3 || codiPostal.length() >5 )
				throw new RegwebException("Camp massa llarg");
			
			// Validam la data 
			SimpleDateFormat yymmdd=new SimpleDateFormat("yyMMdd");
			SimpleDateFormat ddmmyy=new SimpleDateFormat("dd/MM/yy");
			ddmmyy.setLenient(false);  // Important! Sino deixaria passar dates errònees com: "40/01/07"!
			java.util.Date data=null;
			//log.debug("La data de baixa original és: "+dataBaixa);
			if (dataBaixa.equals("")) dataBaixa="0";
			if (!dataBaixa.equals("0")) {
				try {
					data=ddmmyy.parse(dataBaixa);
					dataBaixa=yymmdd.format(data);
					//log.debug("La data de baixa és vàlida i és: "+dataBaixa);
				} catch (Exception e) {
					throw new RegwebException("Data no v\u00E0lida");
				}
			}
			
			/* Agafam el bean */
			AdminFacade autoritzaUsuari = AdminFacadeUtil.getHome().create();
			
			autoritzaUsuari.altaAgrupacioGeografica(codTipuAgruGeo, codAgruGeo, descAgruGeo, 
					dataBaixa, codTipusAgruGeoSuperior, codAgruGeoSuperior, codiPostal );
			
		} catch(NumberFormatException NFe) {
			log.debug("Error: camp no num\u00e8ric.");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", "Error: camp no num\u00e8ric.");
			request.setAttribute("init",""); //Buidam atribut "init" per a que segueixi a la pàgina actual.
			resultado = new String("/admin/controller.do?accion=agrupacionsgeografiques");
			NFe.printStackTrace();
		} catch(RegwebException RWEex) {
			log.debug("Capturam excepci\u00f3 regweb!");
			RWEex.printStackTrace();
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", RWEex.getMessage());
			
			StackTraceElement elements[] = RWEex.getStackTrace();
			for (int i = 0, n = elements.length; i < n; i++) {       
				String linia = elements[i].getFileName() + ":" 
				+ elements[i].getLineNumber() 
				+ ">> " 
				+ elements[i].getMethodName() + "()\n";
				request.setAttribute("mesInfoMissatge"+i,linia);
			}
			request.setAttribute("init",""); //Buidam atribut init per a que no inicialitzi els valors de la pantalla actual.
			resultado="/admin/controller.do?accion=agrupacionsgeografiques";
		} catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}
		return resultado;
	}    
	
	
	private String actualitzaAgrupacioGeografica(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/controller.do?accion=index");
		
		try{            
			request.setAttribute("init","init"); //Posam atribut a init per a que torni a la pàgina inicial.
			String msg="";
			String idPropAct = "";
			log.debug("Dins actualitzaAgrupacioGeografica (servlet)");
			String codTipuAgruGeo = request.getParameter("codTipuAgruGeo").trim(); // Codi tipus agrupació geogràfica a gestionar.
			String codAgruGeo = request.getParameter("codAgruGeo").trim(); // Codi agrupació geogràfica a gestionar.
			String descAgruGeo = request.getParameter("descAgruGeo").trim(); // Descripció agrupació geogràfica
			String dataBaixa = request.getParameter("dataBaixa").trim(); // Data baixa
			String codTipusAgruGeoSuperior = request.getParameter("codTipusAgruGeoSuperior").trim(); // codi tipus agrupació geogràfica superior
			String codAgruGeoSuperior = request.getParameter("codAgruGeoSuperior").trim(); // codi agrupació geogràfica superior
			String codiPostal = request.getParameter("codiPostal").trim(); // Codi postal de l'agrupació geogràfica
			
			/* Validam l'entrada de dades */
			if ( codTipuAgruGeo.length()>2 || codAgruGeo.length()>3 || descAgruGeo.length() >30 
					|| codTipusAgruGeoSuperior.length() > 2 || codAgruGeoSuperior.length() > 3 || codiPostal.length() >5 )
				throw new RegwebException("Camp massa llarg");
			
			// Validam la data 
			SimpleDateFormat ddmmyy=new SimpleDateFormat("dd/MM/yy");
			SimpleDateFormat yymmdd=new SimpleDateFormat("yyMMdd");
			ddmmyy.setLenient(false);  // Important! Sino deixaria passar dates errònees com: "40/01/07"!
			java.util.Date data=null;
			//log.debug("La data de baixa original és: "+dataBaixa);
			if (dataBaixa.equals("")) dataBaixa="0";
			if (!dataBaixa.equals("0")) {
				try {
					data=ddmmyy.parse(dataBaixa);
					dataBaixa=yymmdd.format(data);
					//log.debug("La data de baixa és vàlida i és: "+dataBaixa);
				} catch (Exception e) {
					throw new RegwebException("Data no v\u00E0lida");
				}
			}
			/* Agafam el bean */
			AdminFacade autoritzaUsuari = AdminFacadeUtil.getHome().create();
			
			/* Primer eliminam el registre i després el donam d'alta. */
			autoritzaUsuari.delAgrupacioGeografica(codTipuAgruGeo, codAgruGeo);
			autoritzaUsuari.altaAgrupacioGeografica(codTipuAgruGeo, codAgruGeo, descAgruGeo, 
					dataBaixa, codTipusAgruGeoSuperior, codAgruGeoSuperior, codiPostal );
			
		} catch(NumberFormatException NFe) {
			log.debug("Error: camp no num\u00e8ric.");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", "Error: camp no num\u00e8ric.");
			request.setAttribute("init",""); //Buidam atribut "init" per a que segueixi a la pàgina actual.
			resultado = new String("/admin/controller.do?accion=agrupacionsgeografiques");
		} catch(RegwebException RWEex) {
			log.debug("Capturam excepci\u00f3 regweb!");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", RWEex.getMessage());
			
			StackTraceElement elements[] = RWEex.getStackTrace();
			for (int i = 0, n = elements.length; i < n; i++) {       
				String linia = elements[i].getFileName() + ":" 
				+ elements[i].getLineNumber() 
				+ ">> " 
				+ elements[i].getMethodName() + "()\n";
				request.setAttribute("mesInfoMissatge"+i,linia);
			}
			request.setAttribute("init",""); //Buidam atribut init per a que no inicialitzi els valors de la pantalla actual.
			resultado="/admin/controller.do?accion=agrupacionsgeografiques";
		} catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}
		return resultado;
	}    
	
	private String altaTipusDoc(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/controller.do?accion=index");
		try{ 
			request.setAttribute("init","init"); //Posam atribut a init per a que torni a la pàgina inicial.
			String msg="";
			String idPropAct = "";
			log.debug("Dins altaTipusDoc (servlet)");
			String codTipusDoc = request.getParameter("codTipusDoc").trim(); // Oficina a gestionar.
			String descTipusDoc = request.getParameter("descTipusDoc").trim(); // Descripció de l'oficina
			String dataBaixa = "0"; // Data Baixa
			/* Validam l'entrada de dades */
			if ( codTipusDoc.length()>2 || descTipusDoc.length()>30)
				throw new RegwebException("Camp massa llarg");
			
			// No validam la data, en l'alta ha de ser 0 sempre. 
			/* Agafam el bean */
			AdminFacade autoritzaUsuari = AdminFacadeUtil.getHome().create();
			//log.debug("Volem donar d'alta: "+codTipusDoc+","+descTipusDoc+","+dataBaixa);
			autoritzaUsuari.altaTipusDocument(codTipusDoc, descTipusDoc, "0" );
			
		} catch(NumberFormatException NFe) {
			log.debug("Error: camp no num\u00e8ric.");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", "Error: camp no num\u00e8ric.");
			request.setAttribute("init",""); //Buidam atribut "init" per a que segueixi a la pàgina actual.
			resultado = new String("/admin/controller.do?accion=tipusDocuments");
		} catch(RegwebException RWEex) {
			log.debug("Capturam excepci\u00f3 regweb!");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", RWEex.getMessage());
			
			StackTraceElement elements[] = RWEex.getStackTrace();
			for (int i = 0, n = elements.length; i < n; i++) {       
				String linia = elements[i].getFileName() + ":" 
				+ elements[i].getLineNumber() 
				+ ">> " 
				+ elements[i].getMethodName() + "()\n";
				request.setAttribute("mesInfoMissatge"+i,linia);
			}
			request.setAttribute("init",""); //Buidam atribut init per a que no inicialitzi els valors de la pantalla actual.
			resultado="/admin/controller.do?accion=tipusDocuments";
		} catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			
			ex.printStackTrace();
		}
		return resultado;
	}    
	
	private String actualitzaTipusDoc(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/controller.do?accion=index");
		
		try{    
			request.setAttribute("init","init"); //Posam atribut a init per a que torni a la pàgina inicial.
			String msg="";
			String idPropAct = "";
			log.debug("Dins actualitzaTipusDoc (servlet)");
			String codTipusDoc = request.getParameter("codTipusDoc").trim(); // Codi Oficina a gestionar
			String descTipusDoc = request.getParameter("descTipusDoc").trim(); // Descripció llarga de l'Oficina 
			String dataBaixa = request.getParameter("dataBaixa").trim(); // Data Baixa
			
			if (dataBaixa.equals(""))
				dataBaixa="0";
			
			/* Validam l'entrada de dades */
			if ( codTipusDoc.length()>2 || descTipusDoc.length()>30)
				throw new RegwebException("Camp massa llarg");
			// Validam la data 
			SimpleDateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
			ddmmyyyy.setLenient(false);  // Important! Sino deixaria passar dates errònees com: "40/01/07"!
			java.util.Date data=null;
			//log.debug("La data de baixa original és: "+dataBaixa);
			if (!dataBaixa.equals("0")) { //Si es "0" no l'hem de validar xq casca.
				try {
					data=ddmmyyyy.parse(dataBaixa);
					dataBaixa=yyyymmdd.format(data);
					log.debug("La data de baixa és vàlida i és: "+dataBaixa);
				} catch (Exception e) {
					throw new RegwebException("Data no v\u00E0lida");
				} 
			}
			/* Agafam el bean */
			AdminFacade autoritzaUsuari = AdminFacadeUtil.getHome().create();
			
			//log.debug("Hauriem d'esborrar el registre actual i crear-lo de nou amb els nous valors!");
			autoritzaUsuari.actualitzaTipusDocument( codTipusDoc, descTipusDoc, dataBaixa );
		} catch(NumberFormatException NFe) {
			log.debug("Error: camp no num\u00e8ric.");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", "Error: camp no num\u00e8ric.");
			request.setAttribute("init",""); //Buidam atribut "init" per a que segueixi a la pàgina actual.
			resultado = new String("/admin/controller.do?accion=tipusDocuments");
		} catch(RegwebException RWEex) {
			log.debug("Capturam excepci\u00f3 regweb!");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", RWEex.getMessage());
			
			StackTraceElement elements[] = RWEex.getStackTrace();
			for (int i = 0, n = elements.length; i < n; i++) {       
				String linia = elements[i].getFileName() + ":" 
				+ elements[i].getLineNumber() 
				+ ">> " 
				+ elements[i].getMethodName() + "()\n";
				request.setAttribute("mesInfoMissatge"+i,linia);
			}
			request.setAttribute("init",""); //Buidam atribut init per a que no inicialitzi els valors de la pantalla actual.
			resultado="/admin/controller.do?accion=tipusDocuments";
		} catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}
		return resultado;
	}    
	
	
	private String actualitzaMunicipi060(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/controller.do?accion=index");
		
		try{    
			request.setAttribute("init","init"); //Posam atribut a init per a que torni a la pàgina inicial.
			String msg="";
			String idPropAct = "";
			log.debug("Dins actualitzaMunicipi060 (servlet)");
			String codMunicipi = request.getParameter("codMunicipi").trim(); // Codi Oficina a gestionar
			String descMunicipi = request.getParameter("descMunicipi").trim(); // Descripció llarga de l'Oficina 
			String dataBaixa = request.getParameter("dataBaixa").trim(); // Data Baixa
			
			if (dataBaixa.equals(""))
				dataBaixa="0";
			
			/* Validam l'entrada de dades */
			if ( codMunicipi.length()>3 || descMunicipi.length()>30)
				throw new RegwebException("Camp massa llarg");
			// Validam la data 
			SimpleDateFormat ddmmyyyy=new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat yyyymmdd=new SimpleDateFormat("yyyyMMdd");
			ddmmyyyy.setLenient(false);  // Important! Sino deixaria passar dates errònees com: "40/01/07"!
			java.util.Date data=null;
			//log.debug("La data de baixa original és: "+dataBaixa);
			if (!dataBaixa.equals("0")) { //Si es "0" no l'hem de validar xq casca.
				try {
					data=ddmmyyyy.parse(dataBaixa);
					dataBaixa=yyyymmdd.format(data);
					log.debug("La data de baixa és v\u00E0lida i és: "+dataBaixa);
				} catch (Exception e) {
					throw new RegwebException("Data no v\u00E0lida");
				} 
			}
			/* Agafam el bean */
			AdminFacade autoritzaUsuari = AdminFacadeUtil.getHome().create();
			
			//log.debug("Hauriem d'esborrar el registre actual i crear-lo de nou amb els nous valors!");
			autoritzaUsuari.actualitzaMunicipi060( codMunicipi, descMunicipi, dataBaixa );
		} catch(NumberFormatException NFe) {
			log.debug("Error: camp no num\u00e8ric.");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", "Error: camp no num\u00e8ric.");
			request.setAttribute("init",""); //Buidam atribut "init" per a que segueixi a la pàgina actual.
			resultado = new String("/admin/controller.do?accion=tipusDocuments");
		} catch(RegwebException RWEex) {
			log.debug("Capturam excepci\u00f3 regweb!");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", RWEex.getMessage());
			
			StackTraceElement elements[] = RWEex.getStackTrace();
			for (int i = 0, n = elements.length; i < n; i++) {       
				String linia = elements[i].getFileName() + ":" 
				+ elements[i].getLineNumber() 
				+ ">> " 
				+ elements[i].getMethodName() + "()\n";
				request.setAttribute("mesInfoMissatge"+i,linia);
			}
			request.setAttribute("init",""); //Buidam atribut init per a que no inicialitzi els valors de la pantalla actual.
			resultado="/admin/controller.do?accion=municipis060";
		} catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}
		return resultado;
	}    
	
	private String altaMunicipi060(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/admin/controller.do?accion=index");
		try{ 
			request.setAttribute("init","init"); //Posam atribut a init per a que torni a la pàgina inicial.
			String msg="";
			String idPropAct = "";
			//log.debug("Dins altaMunicipi060 (servlet)");
			String codTipusDoc = request.getParameter("codMunicipi").trim(); // Oficina a gestionar.
			String descTipusDoc = request.getParameter("descMunicipi").trim(); // Descripció de l'oficina
			String dataBaixa = "0"; // Data Baixa
			/* Validam l'entrada de dades */
			if ( codTipusDoc.length()>3 || descTipusDoc.length()>30)
				throw new RegwebException("Camp massa llarg");
			
			// No validam la data, en l'alta ha de ser 0 sempre. 
			/* Agafam el bean */
			AdminFacade autoritzaUsuari = AdminFacadeUtil.getHome().create();
			//log.debug("Volem donar d'alta: "+codTipusDoc+","+descTipusDoc+","+dataBaixa);
			autoritzaUsuari.altaMunicipi060(codTipusDoc, descTipusDoc, "0" );
			
		} catch(NumberFormatException NFe) {
			log.debug("Error: camp no num\u00e8ric.");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", "Error: camp no num\u00e8ric.");
			request.setAttribute("init",""); //Buidam atribut "init" per a que segueixi a la pàgina actual.
			resultado = new String("/admin/controller.do?accion=tipusDocuments");
		} catch(RegwebException RWEex) {
			log.debug("Capturam excepci\u00f3 regweb!");
			request.setAttribute("missatge", "true");
			request.setAttribute("descMissatge", RWEex.getMessage());
			
			StackTraceElement elements[] = RWEex.getStackTrace();
			for (int i = 0, n = elements.length; i < n; i++) {       
				String linia = elements[i].getFileName() + ":" 
				+ elements[i].getLineNumber() 
				+ ">> " 
				+ elements[i].getMethodName() + "()\n";
				request.setAttribute("mesInfoMissatge"+i,linia);
			}
			request.setAttribute("init",""); //Buidam atribut init per a que no inicialitzi els valors de la pantalla actual.
			resultado="/admin/controller.do?accion=municipis060";
		} catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			
			ex.printStackTrace();
		}
		return resultado;
	}    
	
}