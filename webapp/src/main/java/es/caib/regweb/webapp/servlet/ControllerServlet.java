

/*
 * Created on 1-des-2009
 *
 */
package es.caib.regweb.webapp.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import es.caib.regweb.webapp.servlet.UtilWebServlet;
import es.caib.regweb.logic.interfaces.ValoresFacade;
import es.caib.regweb.logic.util.ValoresFacadeUtil;
import es.caib.regweb.logic.helper.Conf;

/**
 * Servlet Class
 *
 */
public class ControllerServlet extends UtilWebServlet {
	
	/**
	 * 
	 */
	private Logger log = null;
	
	public ControllerServlet() {
		super();
	}
	 
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		log = Logger.getLogger(this.getClass());
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext context = this.getServletConfig().getServletContext();
		
		HttpSession sesion = request.getSession();

        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

		String accion = request.getParameter("accion");
		String param = "/controller.do?accion=index";
		
		//log.debug("Servlet, accion="+accion);
		if (accion!=null && !accion.equals(""))
			param = index(request, sesion);
		
		if ("index".equals(accion)) param = index(request, sesion);

		
		String url = response.encodeURL(param);
		context.getRequestDispatcher(url).forward(request, response);

	}
	
	
	private String index(HttpServletRequest request, HttpSession sesion) {
		String resultado = new String("/pages/index.jsp");

		try{            
            String usuario=request.getRemoteUser();

            ValoresFacade valores = ValoresFacadeUtil.getHome().create();

            boolean autorizadoVisadoEntradas = valores.usuarioAutorizadoVisar(usuario,"VE");
            boolean autorizadoVisadoSalidas = valores.usuarioAutorizadoVisar(usuario,"VS");
        	String preregistre = Conf.get("preregistre");
        	String viewPreregistre = Conf.get("viewPreregistre", "false");

            request.setAttribute("autorizadoVisadoEntradas", Boolean.valueOf(autorizadoVisadoEntradas));
            request.setAttribute("autorizadoVisadoSalidas", Boolean.valueOf(autorizadoVisadoSalidas));
            request.setAttribute("preregistre", preregistre);
            request.setAttribute("viewPreregistre", viewPreregistre);

        } catch(Exception ex) {
			log.debug("Capturam excepci\u00f3 estranya!");
			ex.printStackTrace();
		}    		
		
		return resultado;
    }


}