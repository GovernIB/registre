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
import es.caib.regweb.logic.interfaces.ValoresFacade;
import es.caib.regweb.logic.util.ValoresFacadeUtil;

/**
 * Automatically complete a name. Non-practical example, but what if
 * you had 50,000 names?
 * @author Bob Lee (crazybob@crazybob.org)
 */
public class RecuperaDescripcionDestinatarioServlet extends LiveScriptServlet {
    
    protected String invoke(String[] args) throws LiveScriptException {
        // if (args.length != 1) throw new LiveScriptException("Invalid argument count.");
        try {
            ValoresFacade valores = ValoresFacadeUtil.getHome().create();
            //  Valores valores=new Valores();
            String texto=null;
            texto=valores.recuperarDestinatario(args[0]);
            return java.net.URLEncoder.encode(texto.trim(),"UTF-8");
        }
        catch (Exception e) {
            LiveScriptException ls=new LiveScriptException("error de encoding");
            ls.initCause(e);
            throw ls;
        }
    }
}
