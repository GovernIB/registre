package es.caib.regweb3.webapp.controller.notificacio;

import javax.ejb.EJB;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import es.caib.notib.client.domini.NotificacioCanviClient;
import es.caib.regweb3.model.Remesa;
import es.caib.regweb3.persistence.ejb.RemesaConsultaLocal;
import es.caib.regweb3.persistence.ejb.RemesaLocal;

@Controller
@RequestMapping("/rest/notib")
public class NotibWsController {

	@EJB(mappedName = "regweb3/RemesaConsultaEJB/local")
    public RemesaConsultaLocal remesaConsultaEjb;
    @EJB(mappedName = "regweb3/RemesaEJB/local")
    private RemesaLocal remesaEjb;
    
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
			Remesa remesa = remesaConsultaEjb.getByIdentificadorAndReferencia(identificador, referencia);
			
			if (remesa != null) {
				remesaEjb.notificacionActualitzarEstado(
						identificador, 
						referencia,
						remesa.getEntidad());
			}
		
		}
	}
	


}