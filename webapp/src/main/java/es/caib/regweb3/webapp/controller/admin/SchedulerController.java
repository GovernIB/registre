package es.caib.regweb3.webapp.controller.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.scheduler.Regweb3Scheduler;
import es.caib.regweb3.webapp.utils.Mensaje;

@Controller
@RequestMapping(value = "/scheduler")
public class SchedulerController extends BaseController {

	protected final Logger log = Logger.getLogger(getClass());
	
	@Autowired
	private Regweb3Scheduler regweb3Scheduler;
	
	@RequestMapping(value = "/restart", method = RequestMethod.GET)
    public String list(Model model, @RequestParam String task, HttpServletRequest request)throws Exception {

		try {
			regweb3Scheduler.reiniciarTarea(task);
			
			Mensaje.saveMessageInfo(request, getMessage("regweb.schduler.restart.ok"));
		} catch (Exception e) {
			Mensaje.saveMessageError(request, getMessage("regweb.schduler.restart.ko", e.getMessage()));
		}
		
		return "redirect:/adminEntidad/anexosfirma/list";
    }
}
