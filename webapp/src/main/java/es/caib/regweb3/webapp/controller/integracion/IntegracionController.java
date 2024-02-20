package es.caib.regweb3.webapp.controller.integracion;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Integracion;
import es.caib.regweb3.persistence.ejb.IntegracionLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.BasicForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @EJB(mappedName = IntegracionLocal.JNDI_NAME)
    private IntegracionLocal integracionEjb;


    /**
     * Listado de todas las {@link Integracion}
     */
    @RequestMapping(value = "/list/{tipo}", method = RequestMethod.GET)
    public String listado(@PathVariable("tipo") Long tipo, Model model, HttpServletRequest request) throws Exception{

        Integracion busqueda = new Integracion(tipo);
        busqueda.setEstado(RegwebConstantes.INTEGRACION_ESTADO_ERROR);
        busqueda.setNumRegFormat("");

        Entidad entidadActiva = getEntidadActiva(request);

        Paginacion paginacion = integracionEjb.busqueda(busqueda, entidadActiva.getId());

        model.addAttribute("paginacion", paginacion);
        model.addAttribute("integracionBusqueda", busqueda);
        model.addAttribute("tipo", tipo);
        model.addAttribute("integracion", new BasicForm());

        return "integracion/integracionList";
    }

    /**
     * Listado de todos las {@link Integracion}
     */
    @RequestMapping(value = "/list/{tipo}", method = RequestMethod.POST)
    public ModelAndView listado(@PathVariable("tipo") Long tipo, @ModelAttribute Integracion busqueda, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("integracion/integracionList");

        Entidad entidadActiva = getEntidadActiva(request);

        Paginacion paginacion = integracionEjb.busqueda(busqueda, entidadActiva.getId());

        busqueda.setPageNumber(1);
        mav.addObject("paginacion", paginacion);
        mav.addObject("integracionBusqueda", busqueda);
        mav.addObject("tipo", busqueda.getTipo());
        mav.addObject("integracion", new BasicForm());

        return mav;
    }

    /**
     * Búsqueda de integraciones {@link Integracion}
     */
    @RequestMapping(value = "/busqueda", method = RequestMethod.POST)
    public ModelAndView busqueda(@ModelAttribute BasicForm busqueda, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("integracion/integracionBusqueda");

        Entidad entidadActiva = getEntidadActiva(request);

        List<Integracion> integraciones = integracionEjb.getByEntidadNumReg(entidadActiva.getId(),busqueda.getTexto());

        mav.addObject("integraciones", integraciones);
        mav.addObject("busqueda", busqueda);

        return mav;
    }
    

    @ModelAttribute("tiposIntegracion")
    public Long[] tipos() {
        if(Configuracio.isCAIB()){
            return RegwebConstantes.INTEGRACION_TIPOS_CAIB;
        }else{
            return RegwebConstantes.INTEGRACION_TIPOS;
        }
    }

    @ModelAttribute("estados")
    public Long[] estados() {
        return RegwebConstantes.INTEGRACION_ESTADOS;
    }
    
}
