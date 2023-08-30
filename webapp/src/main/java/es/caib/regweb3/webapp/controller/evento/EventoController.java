package es.caib.regweb3.webapp.controller.evento;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.webapp.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 26/09/19
 */
@Controller
public class EventoController extends BaseController {

    protected final Logger log = LoggerFactory.getLogger(getClass());


    @RequestMapping(value = "/eventos")
    public ModelAndView eventos(HttpServletRequest request) throws Exception{

        ModelAndView mav = new ModelAndView("eventos");

        Entidad entidadActiva = getEntidadActiva(request);

        mav.addObject("totalEntradas",
                registroEntradaConsultaEjb.queryCount("Select count(id) from RegistroEntrada where entidad.id = "+entidadActiva.getId()));

        mav.addObject("totalSalidas",
                registroEntradaConsultaEjb.queryCount("Select count(id) from RegistroSalida where entidad.id = "+entidadActiva.getId()));


        return mav;
    }

}
