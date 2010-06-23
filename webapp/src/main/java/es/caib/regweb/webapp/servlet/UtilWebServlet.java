package es.caib.regweb.webapp.servlet;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;


/**
 * Servlet Class
 * 
 */
public class UtilWebServlet extends HttpServlet{

    ServletContext context = null;
    private Logger log = null;
	public UtilWebServlet() {
		super();
	}

	public void init(ServletConfig config) throws ServletException {
	    super.init(config);
	    log = Logger.getLogger(this.getClass());
	    context = config.getServletContext();
	}


    /**
     * Método genérico para instanciar a otros EJB desde JSP o Servlets
     */
    public Object getInterface(String name, String pack) {

        Object home = null;
        
        try {
            if (name != null){
	            if (pack != null && pack.length() > 0){
	                pack += ".";
	            } else {
	                pack = "";
	            }

	            String jndiName = "java:comp/env/ejb/" + name;
	            String homeName = "es.caib.regweb." + pack + name + "Home";
                
	            InitialContext jndiContext = new InitialContext();	
	            Object obj = jndiContext.lookup(jndiName);

	            home = PortableRemoteObject.narrow(obj, Class.forName(homeName));	
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } 

        return home;
    }    

   
    /**
     * Método para obtener el nombre del contexto definido para la aplicación 
     */
    public String getContext(HttpServletRequest request){
        String resultado = new String("");
        
        //resultado += "http://" + request.getServerName();
        //resultado += ":" + request.getServerPort();
        resultado += request.getContextPath();
        log.info("Contexte="+resultado);
        return resultado;
    }
    
    /**
     * Método para escribir mensajes en la consola 
     */
    public void logMsg(String msg){
        log.debug(msg);
    }
    
}
