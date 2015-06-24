package es.caib.regweb3.webapp.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 11/02/14
 */
public class Mensaje {

    public static void saveMessageInfo(HttpServletRequest request, String mensaje) {
        HttpSession session = request.getSession();

        List<String> mensajes = (List<String>) session.getAttribute("infos");

        if(mensajes == null){
            mensajes = new ArrayList<String>();
            mensajes.add(mensaje);
        }else{
            mensajes.add(mensaje);
        }
        session.setAttribute("infos", mensajes);
    }

    public static void saveMessageError(HttpServletRequest request, String mensaje) {
        HttpSession session = request.getSession();
        session.setAttribute("error", mensaje);
    }

    public static void saveMessageAviso(HttpServletRequest request, String mensaje) {
        HttpSession session = request.getSession();

        session.setAttribute("aviso", mensaje);

    }
}
