/**
 * Copyright 2002 Bob Lee (http://www.crazybob.org/)
 * This product is licensed under the GPL
 * (http://www.gnu.org/licenses/gpl.txt).
 */

package es.caib.regweb.webapp.servlet;

//import org.crazybob.livescript.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;
import org.apache.log4j.Logger;
import es.caib.regweb.logic.interfaces.ValoresFacade;
import es.caib.regweb.logic.util.ValoresFacadeUtil;

/**
 * Automatically complete a name. Non-practical example, but what if
 * you had 50,000 names?
 * @author Bob Lee (crazybob@crazybob.org)
 */
public class DesbloqueaSessionServlet extends LiveScriptSessionServlet {
    
	private Logger log = null;
	
    protected String invoke(String[] args, HttpServletRequest request) throws LiveScriptException {
        try {
        	log = Logger.getLogger(this.getClass());
            
            ValoresFacade valores = ValoresFacadeUtil.getHome().create();
            
            HttpSession session = request.getSession(true);
            String bloqueoOficina=(session.getAttribute("bloqueoOficina")==null) ? "" : (String)session.getAttribute("bloqueoOficina");
            String bloqueoTipo=(session.getAttribute("bloqueoTipo")==null) ? "" : (String)session.getAttribute("bloqueoTipo");
            String bloqueoAno=(session.getAttribute("bloqueoAno")==null) ? "" : (String)session.getAttribute("bloqueoAno");
            String bloqueoUsuario=(session.getAttribute("bloqueoUsuario")==null) ? "" : (String)session.getAttribute("bloqueoUsuario");
            //log.debug("Desbloquejam");
            if (!bloqueoOficina.equals("") || !bloqueoTipo.equals("") || !bloqueoAno.equals("")) {
                valores.liberarDisquete(bloqueoOficina, bloqueoTipo, bloqueoAno, bloqueoUsuario);
                // Eliminamos de la session los atributos
                session.removeAttribute("bloqueoOficina");
                session.removeAttribute("bloqueoTipo");
                session.removeAttribute("bloqueoAno");
                session.removeAttribute("bloqueoUsuario");
                session.removeAttribute("bloqueoDisquete");
            }
            return bloqueoOficina+" - "+bloqueoTipo+" - "+bloqueoAno;
        }
        catch (Exception e) {
            LiveScriptException ls=new LiveScriptException("error de encoding");
            ls.initCause(e);
            throw ls;
        }
    }
}
