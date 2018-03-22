package es.caib.regweb3.webapp.controller.integracion;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Integracion;
import es.caib.regweb3.persistence.ejb.IntegracionLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by Fundació Bit
 * Date: 06/03/18
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb3.model.Integracion}
 *
 * @author earivi
 */
@Controller
@RequestMapping(value = "/integracion")
public class IntegracionController extends BaseController {

    @EJB(mappedName = "regweb3/IntegracionEJB/local")
    private IntegracionLocal integracionEjb;


    /**
     * Listado de todas las {@link Integracion}
     */
    @RequestMapping(value = "/list/{tipo}", method = RequestMethod.GET)
    public String listado(@PathVariable("tipo") Long tipo, Model model, HttpServletRequest request) throws Exception{

        Integracion busqueda = new Integracion(tipo);

        Entidad entidadActiva = getEntidadActiva(request);

        Paginacion paginacion = integracionEjb.busqueda(busqueda.getPageNumber(), entidadActiva.getId(), busqueda.getTipo());

        model.addAttribute("paginacion", paginacion);
        model.addAttribute("integracionBusqueda", busqueda);
        model.addAttribute("tipo", tipo);

        return "integracion/integracionList";
    }

    /**
     * Listado de todos las {@link Integracion}
     */
    @RequestMapping(value = "/list/{tipo}", method = RequestMethod.POST)
    public ModelAndView listado(@PathVariable("tipo") Long tipo, @ModelAttribute Integracion busqueda, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("integracion/integracionList");

        Entidad entidadActiva = getEntidadActiva(request);

        Paginacion paginacion = integracionEjb.busqueda(busqueda.getPageNumber(), entidadActiva.getId(), busqueda.getTipo());

        mav.addObject("paginacion", paginacion);
        mav.addObject("integracionBusqueda", busqueda);
        mav.addObject("tipo", busqueda.getTipo());

        return mav;
    }
    

    @ModelAttribute("tipos")
    public
    Long[] configuraciones() {
        return RegwebConstantes.INTEGRACION_TIPOS;
    }
    
}
