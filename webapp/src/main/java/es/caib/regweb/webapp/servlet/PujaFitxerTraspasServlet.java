

/*
 * Created on 1-aug-2007
 *
 * @author Sebastià Matas Riera
 *  
 */
package es.caib.regweb.webapp.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DefaultFileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.log4j.Logger;

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
public class PujaFitxerTraspasServlet extends UtilWebServlet {

    /**
     * 
     */
	private Logger log = null;
	
    public PujaFitxerTraspasServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        log = Logger.getLogger(this.getClass());
        // TODO Auto-generated method stub
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO Auto-generated method stub
        super.doGet(req, resp);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext context = this.getServletConfig().getServletContext();
        
        HttpSession sesion = request.getSession();
        String accion = request.getParameter("accion");
        String param = "/admin/controller.do?accion=index";
        
        log.debug("PujaFitxerTraspasServlet, accion="+accion);
    	FileUpload fu = new FileUpload(new DefaultFileItemFactory());
    	fu.setSizeMax(50960);// 40 kB
    	//fu.setSizeThreshold(50960);// 40 kB
    	StringBuffer out = null;
    	java.util.List fileItems=null;
    	Iterator i = null;
    	String fileName="";
    	try {
    		fileItems = fu.parseRequest(request);
    		i = fileItems.iterator();
    		while (i.hasNext()) {
    			//log.debug("Inici FileUpload");
    			FileItem item = (FileItem) i.next();
    			if (item.isFormField()) {
    				if (item.getFieldName().equals("accion") ) {
    					log.debug("Acci\u00f3="+item.getString());
    				}
    			} else if (item.getSize() > 0) {
    				String fieldName = item.getFieldName();
    				fileName = item.getName();
    				String contentType = item.getContentType();
    				boolean isInMemory = item.isInMemory();
    				long sizeInBytes = item.getSize();
    				InputStream uploadedStream = item.getInputStream();
    				out = new StringBuffer();
    			    byte[] b = new byte[4096];
    			    for (int n; (n = uploadedStream.read(b)) != -1;) {
    			    	String tmp = new String(b, 0, n);
    			    	//log.debug("abans:"+tmp+ "strlen="+tmp.length());
    			    	tmp=tmp.replace('\u00B4','\'');
    			        out.append(tmp);
    			        //log.debug("despres:"+tmp+ "strlen="+tmp.length());
    			    }
    			    uploadedStream.close();
    				//System.out.println("fitxer:" +data);
    				log.debug("Nom fitxer: "+fileName+" mida: "+sizeInBytes);
    				//log.debug(out.toString());
    				//Ara a "out" tenim un string amb el contingut del fitxer a tractar.
    			}
    		}
    	} catch (FileUploadException e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    	}
    	if (out!=null) {
    		//Hem llegit el fitxer, ara anam a la pàgina on mostram el que tenim previst fer-ne.
    		param = "/admin/controller.do?accion=passaTraspassos";
    		request.setAttribute("fitxer",out.toString()); //Posam atribut a init per a que torni a la pàgina inicial.
    		request.setAttribute("nomFitxer",fileName); //Posam atribut a init per a que torni a la pàgina inicial.
    	}
        log.debug("param="+param);
        String url = response.encodeURL(param);
        context.getRequestDispatcher(url).forward(request, response);
    }
    
}