/*
 * FileUploader.java
 *
 * Created on 5 de marzo de 2003, 20:04
 */

package es.caib.regweb.webapp.servlet;

import es.caib.regweb.logic.helper.RegistroRepro;
import org.apache.commons.fileupload.*;

import java.util.*;

import org.apache.log4j.Logger;
import java.sql.SQLException;
import es.caib.regweb.logic.interfaces.ReproUsuarioFacade;
import es.caib.regweb.logic.util.ReproUsuarioFacadeUtil;

/**
 *
 * @author  SNAVA05
 */
public class FileUploader {
	
	private boolean borrarCookies=false;
	private int cookiesImportadas = 0;
	private int reprosDescartades = 0;
	private int cookiesNoGrabadas = 0;
	private Logger log = null;
	
	public static int MAX_MB=1;
	private static int MAX_FILE_SIZE=MAX_MB*1024*1024;
	
	/** Creates a new instance of FileUploader */
	public FileUploader(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, String tipusRepro) throws SQLException, Exception {      
		log = Logger.getLogger(this.getClass());
		int nombreRepros = 0;
		FileUpload fu = new FileUpload();
		fu.setSizeMax(50960);// 40 kB
		fu.setSizeThreshold(50960);// 40 kB
		
		Vector vectorRepros = new Vector();
		javax.naming.InitialContext contexto = new javax.naming.InitialContext();
		
		//Cercam el EJB d'accés al repositori de Repros
        ReproUsuarioFacade repro = ReproUsuarioFacadeUtil.getHome().create();				
	
		//Cercam l'usuari
		String usuario=request.getRemoteUser();	
		
		//REcuperam les repros de l'usuari segons el tipus de Repro amb el que feim feina
		vectorRepros=repro.recuperarRepros(usuario,tipusRepro);

		//Cercam el número màxim de repros per usuari
		javax.naming.Context myenv = (javax.naming.Context) contexto.lookup("java:comp/env");
	    int maxRepros = ((Integer)myenv.lookup("Repros.max")).intValue();
		
		java.util.List fileItems = fu.parseRequest(request);
		java.util.Iterator i = fileItems.iterator();		
		
		while (i.hasNext()) {
			FileItem item = (FileItem) i.next();
			if (item.getFieldName().equals("borra_repro") && item.getString().equals("s")){ 
			//if((borra_repro != null) && (borra_repro.equalsIgnoreCase("s"))){
				try {
					for (int j=0;j<vectorRepros.size();j=j+1){
						RegistroRepro regRepro = (RegistroRepro) vectorRepros.get(j);
						repro.eliminar(usuario,regRepro.getNomRepro());
					}
				//	System.out.println("Eliminado Repros");
				} catch (SQLException e) {
					e.printStackTrace();
				}
				vectorRepros.clear();
				borrarCookies=true;
			}
		}
		
		 i = fileItems.iterator();
		cookiesImportadas=0;
		cookiesNoGrabadas=0;
		reprosDescartades=0;
		nombreRepros = vectorRepros.size();
		
		while (i.hasNext()) {
			log.debug("Inici FileUpload");
			FileItem item = (FileItem) i.next();
			if (item.isFormField()) {
				//if (item.getFieldName().equals("borra_repro") && item.getString().equals("s")) {
					//Borramos las cookies
				//}
			} else if (item.getSize() > 0) {
				Properties cookieFile=new Properties();
				cookieFile.load(item.getInputStream());
				Enumeration nombres=cookieFile.propertyNames();
				
				while (nombres.hasMoreElements() && ((cookiesImportadas+nombreRepros)< maxRepros)) {
					String ReproNombre=(String)nombres.nextElement();
					String ReproValor=cookieFile.getProperty(ReproNombre);
					//System.out.println("Repro a importar:"+ReproNombre+" mida="+ReproValor.length()+" Tipus:"+tipusRepro);
					if (repro.grabar(usuario, ReproNombre, ReproValor, tipusRepro)){
							cookiesImportadas++;
						}else{
							cookiesNoGrabadas++;
						}
					
				} // Fin while (nombres.has
				
				while (nombres.hasMoreElements()) {
					String ReproNombre=(String)nombres.nextElement();
					//String ReproValor=cookieFile.getProperty(ReproNombre);
					reprosDescartades++;
				} // Fin while (nombres.has
				log.debug("Nombre total repros importades="+cookiesImportadas);
			} // Fin else if (item.getSize() > 0) 
		} // Fin if (item.isFormField()) 
	
	} //Fin public FileUploader
	
	public boolean getBorradas() {
		return borrarCookies;
	}
	
	public int getImportadas() {
		return cookiesImportadas;
	}
	public int getDescartades() {
		return reprosDescartades;
	}
	public int getNoGravades() {
		return cookiesNoGrabadas;
	}
}
