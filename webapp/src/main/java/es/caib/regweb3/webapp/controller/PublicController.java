package es.caib.regweb3.webapp.controller;

import es.caib.regweb3.utils.Versio;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class PublicController {
	
	protected final Logger log = Logger.getLogger(getClass());

	@RequestMapping(value = "/public/versio")
	public void versio(HttpServletResponse response)
			throws Exception {
		response.getWriter().write(Versio.VERSIO);
		response.getWriter().flush();
		response.getWriter().close();

	}

}
