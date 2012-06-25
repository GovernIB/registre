

/*
 * Created on 1-des-2009
 *
 */
package es.caib.regweb.webapp.servlet;

import es.caib.regweb.logic.interfaces.ValoresFacade;
import es.caib.regweb.logic.util.ValoresFacadeUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Vector;

/**
 * Servlet Class
 *
 */
public class ControllerPopupServlet extends UtilWebServlet {

	private Logger log = null;

	public ControllerPopupServlet() {
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


        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

		String accion = request.getParameter("accion");
		String param = "/popup.do";

		log.debug("Servlet ControllerPopupServlet, accion="+accion);
		if (accion!=null && !accion.equals(""))
			param = "/popup/pages/index.jsp";

		if ("remitentes".equals(accion)) param = remitentes(request);


		String url = response.encodeURL(param);
		context.getRequestDispatcher(url).forward(request, response);

	}


	private String remitentes(HttpServletRequest request) {
		String resultado = "/popup/pages/remitentes.jsp";

		try{
            String subcadenaCodigo=(request.getParameter("subcadenaCodigo")==null) ? "" :request.getParameter("subcadenaCodigo").trim();
            String subcadenaTexto=(request.getParameter("subcadenaTexto")==null) ? "" : request.getParameter("subcadenaTexto").trim();

            ValoresFacade valores = ValoresFacadeUtil.getHome().create();

            Vector remitentes=valores.buscarRemitentes(subcadenaCodigo, subcadenaTexto);

            request.setAttribute("subcadenaCodigo", subcadenaCodigo);
            request.setAttribute("subcadenaTexto", subcadenaTexto);
            request.setAttribute("remitentes", remitentes);
            request.setAttribute("remitentesSize", remitentes.size());

        } catch(Exception ex) {
			log.error("Error a remitentes()",ex);
		}

		return resultado;
    }


}