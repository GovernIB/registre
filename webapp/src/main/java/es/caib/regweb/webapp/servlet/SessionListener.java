/*
 * SessionListener.java
 *
 * Created on 7 de septiembre de 2004, 16:40
 */

package es.caib.regweb.webapp.servlet;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSession;
import es.caib.regweb.logic.interfaces.ValoresFacade;
import es.caib.regweb.logic.util.ValoresFacadeUtil;

/**
 *
 * @author  SMART41
 */
public class SessionListener implements HttpSessionListener {
    
    /** Creates a new instance of SessionListener */
    public SessionListener() {
    }
    
    public void sessionCreated(HttpSessionEvent e) {
    }
    
    public void sessionDestroyed(HttpSessionEvent e) {
        try {
            HttpSession session=e.getSession();
            
            ValoresFacade valores = ValoresFacadeUtil.getHome().create();
            
            String bloqueoOficina=(session.getAttribute("bloqueoOficina")==null) ? "" : (String)session.getAttribute("bloqueoOficina");
            String bloqueoTipo=(session.getAttribute("bloqueoTipo")==null) ? "" : (String)session.getAttribute("bloqueoTipo");
            String bloqueoAno=(session.getAttribute("bloqueoAno")==null) ? "" : (String)session.getAttribute("bloqueoAno");
            String bloqueoUsuario=(session.getAttribute("bloqueoUsuario")==null) ? "" : (String)session.getAttribute("bloqueoUsuario");
            
            if (!bloqueoOficina.equals("") || !bloqueoTipo.equals("") || !bloqueoAno.equals("")) {
                valores.liberarDisquete(bloqueoOficina, bloqueoTipo, bloqueoAno, bloqueoUsuario);
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }
}
