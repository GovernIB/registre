package es.caib.regweb3.webapp.controller.preRegistro;

import es.caib.regweb3.model.PreRegistro;
import es.caib.regweb3.persistence.ejb.PreRegistroLocal;
import es.caib.regweb3.webapp.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.ejb.EJB;

/**
 * Created 12/12/14 9:45
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb3.model.PreRegistro}
 * @author jpernia
 */
@Controller
@RequestMapping(value = "/preRegistro")
@SessionAttributes(types = PreRegistro.class)
public class PreRegistroController extends BaseController {

    @EJB(mappedName = "regweb3/PreRegistroEJB/local")
    public PreRegistroLocal preRegistroEjb;



}


