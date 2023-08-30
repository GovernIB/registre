package es.caib.regweb3.webapp.controller.oficina;

import es.caib.regweb3.model.CatEstadoEntidad;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.persistence.ejb.CatEstadoEntidadLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.OficinaBusquedaForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/oficina")
public class OficinaController extends BaseController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @EJB(mappedName = CatEstadoEntidadLocal.JNDI_NAME)
    private CatEstadoEntidadLocal catEstadoEntidadEjb;

    /**
     * Listado de todos los Oficinas
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listado(Model model, HttpServletRequest request) throws Exception {

        Entidad entidad = getEntidadActiva(request);
        CatEstadoEntidad vigente = catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        Oficina oficina = new Oficina();
        oficina.setEstado(vigente);

        OficinaBusquedaForm oficinaBusqueda = new OficinaBusquedaForm(oficina, entidad.getId(), 1);

        model.addAttribute("entidad", entidad);
        model.addAttribute("oficinaBusqueda", oficinaBusqueda);
        model.addAttribute("estados",catEstadoEntidadEjb.getAll());

        return "oficina/oficinaList";
    }

    /**
     * Listado de oficinas
     *
     * @param busqueda
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ModelAndView list(@ModelAttribute OficinaBusquedaForm busqueda, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("oficina/oficinaList");
        Entidad entidad = getEntidadActiva(request);
        Oficina oficina = busqueda.getOficina();

        Paginacion paginacion = oficinaEjb.busqueda(busqueda.getPageNumber(), entidad.getId(), oficina.getCodigo(), oficina.getDenominacion(), oficina.getEstado().getId());

        busqueda.setPageNumber(1);
        mav.addObject("entidad", entidad);
        mav.addObject("paginacion", paginacion);
        mav.addObject("oficinaBusqueda", busqueda);
        mav.addObject("estados",catEstadoEntidadEjb.getAll());

        return mav;
    }

    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idOficina}/detalle", method = RequestMethod.GET)
    public String detalleOficina(@PathVariable Long idOficina, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception{

        Oficina oficina = oficinaEjb.findByIdCompleto(idOficina);

        model.addAttribute("oficina",oficina);
        model.addAttribute("organismos",organismoEjb.getOrganismosRegistro(oficina));

        return "oficina/oficinaDetalle";

    }
}
