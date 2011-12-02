

/*
 * Created on 1-des-2009
 *
 */
package es.caib.regweb.webapp.servlet;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import es.caib.regweb.webapp.servlet.UtilWebServlet;
import es.caib.regweb.logic.interfaces.RegistroEntradaFacade;
import es.caib.regweb.logic.interfaces.ValoresFacade;
import es.caib.regweb.logic.interfaces.HistoricoEmailsFacade;
import es.caib.regweb.logic.util.RegistroEntradaFacadeUtil;
import es.caib.regweb.logic.util.ValoresFacadeUtil;
import es.caib.regweb.logic.util.HistoricoEmailsFacadeUtil;
import es.caib.regweb.logic.helper.Conf;
import es.caib.regweb.logic.helper.ParametrosRegistroEntrada;
import es.caib.regweb.logic.helper.ParametrosHistoricoEmails;
import es.caib.regweb.logic.helper.Mail;

/**
 * Servlet Class
 *
 */
public class HistoricEmailsServlet extends UtilWebServlet {
	

	private Logger log = null;

	public HistoricEmailsServlet() {
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

		String tipoRegistro = request.getParameter("tipus");
		String codOficina = request.getParameter("oficina");
		String numeroEntrada = request.getParameter("numero");
		String ano = request.getParameter("ano");


		
		String param = "/popup/historicEmails.jsp";
		param+="?oficina="+codOficina+"&numero="+numeroEntrada+"&ano="+ano+"&tipoRegistro="+tipoRegistro;


		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		String url = response.encodeURL(param);
		context.getRequestDispatcher(url).forward(request, response);
	}


	

}