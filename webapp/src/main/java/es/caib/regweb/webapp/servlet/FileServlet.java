/*
 * PdfServlet.java
 *
 * Created on 26 de marzo de 2003, 16:13
 */

package es.caib.regweb.webapp.servlet;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.Logger;

/**
 *
 * @author  SNAVA05
 * @version
 */
public class FileServlet extends HttpServlet {
    
    private Logger log = Logger.getLogger(this.getClass());
	
    /** Initializes the servlet.
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
    }
    
    /** Destroys the servlet.
     */
    public void destroy() {
        
    }
    
    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
//        System.out.println("Comienza");
        String nomfitxer=request.getParameter("nomfitxer");
        String extensio="xxx";
        if (nomfitxer.indexOf("..")!=-1 || nomfitxer.indexOf("/")!=-1){
        	ServletOutputStream out = response.getOutputStream();
        	out.print("<html><body><p>ERROR: No es permeten ni el car\u00E0cter '/' ni dos punts seguits al nom del fitxer "+nomfitxer+"</p></body></html>");
        	out.flush();
        	return;
        }
        
        if (nomfitxer.lastIndexOf('.')!=-1) {
        	extensio=nomfitxer.substring(nomfitxer.lastIndexOf('.')+1);
        	//System.out.println("Extensió del fitxer "+nomfitxer+" és: "+extensio);
        	if (extensio.compareTo("pdf")==0) {
        		response.setContentType("application/pdf");
        	}
        }
       
        if ( nomfitxer.compareTo("manualusuari")==0 ) {
        	nomfitxer="REGISTRE WEB - MANUAL USUARI.pdf";
        }
        if ( nomfitxer.compareTo("requisits")==0 ) {
        	nomfitxer="Requisits_tecnologics_REGWEB.pdf";
        }
        if ( nomfitxer.compareTo("Solicitud1")==0 ) {
        	nomfitxer="Sol autoritzacio registre web usuari que ja empra AS400.pdf";
        } 
        if ( nomfitxer.compareTo("Solicitud2")==0 ) {
        	nomfitxer="Sol autoritzacio registre web NOU USUARI.pdf";
        }
        if ( nomfitxer.compareTo("Solicitud3")==0 ) {
        	nomfitxer="Sol autoritzacio registre NOU USUARI NOMES AS400.pdf";
        }
        response.setHeader("Content-Disposition",  "inline; filename=\""+nomfitxer+"\"");
        response.setHeader("Cache-Control",  "store");
        response.setHeader("Pragma", "cache");
        //
        // Stream Image
        //
        BufferedInputStream in = null;
        try{
            in = new BufferedInputStream(
		    getServletContext().getResourceAsStream("documentacio/"+nomfitxer));

            ServletOutputStream out = response.getOutputStream();
            
            byte[] buffer = new byte[4 * 1024];

            int data;
            while((data = in.read(buffer)) != -1){
                out.write(buffer, 0, data);
        }
        out.flush();
            
        }catch(IOException ioe) {
        	log.error("Error:");
        	log.error(ioe.getMessage());
        	ioe.printStackTrace();
        	ServletOutputStream out = response.getOutputStream();
        	out.print("<html><body><p>ERROR: Fitxer "+nomfitxer+" NO trobat!</p></body></html>");
        	out.flush();
        }
        catch (Exception e) {
            log.error("Error:");
            log.error(e.getMessage());
            e.printStackTrace();
//            System.out.println("Error:");
        }
    }
    
    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        processRequest(request, response);
    }
    
    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Servlet que permet davallar els fitxers de docroot. Per evitar el problema" +
        		"de l'internet explorer amb davallar fitxers de MS-Office i PDFs.";
    }
    
}
