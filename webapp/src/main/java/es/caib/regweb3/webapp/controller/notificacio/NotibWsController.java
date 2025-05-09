package es.caib.regweb3.webapp.controller.notificacio;

import javax.ejb.EJB;

import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.caib.notib.client.domini.NotificacioCanviClient;
import es.caib.regweb3.model.Remesa;
import es.caib.regweb3.persistence.ejb.PublicLocal;

@Controller
@RequestMapping("/public/rest/notib")
public class NotibWsController {

	protected final Logger log = Logger.getLogger(getClass());
	
	@EJB(mappedName = "regweb3/PublicEJB/local")
    public PublicLocal publicEjb;
    
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public String get() {
		return "restNotib";
	}

	@RequestMapping(value = "/notificaCanvi", method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void notificaCanvi(
			@RequestBody NotificacioCanviClient notificacioCanvi) {
		String identificador = notificacioCanvi.getIdentificador();
		String referencia = notificacioCanvi.getReferenciaEnviament();
		
		if (identificador != null && referencia != null) {
			Remesa remesa = publicEjb.getByIdentificadorAndReferencia(identificador, referencia);
			
			if (remesa != null) {
				try {
					publicEjb.notificacionActualitzarEstado(
							identificador, 
							referencia,
							remesa);
				} catch (I18NException e) {
					log.error("Ha habido un error consultando la notificación (identificador=" + identificador + ")");
				} catch (Exception e) {
					log.error("Ha habido un error consultando la notificación (identificador=" + identificador + ")");
				}
			}
		
		}
	}
	


}