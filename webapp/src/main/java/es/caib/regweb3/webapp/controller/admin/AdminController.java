package es.caib.regweb3.webapp.controller.admin;

import es.caib.regweb3.model.CatEstadoEntidad;
import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.persistence.ejb.CatEstadoEntidadLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.OficinaBusquedaForm;
import es.caib.regweb3.webapp.form.OrganismoBusquedaForm;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


/**
 * Created 14/02/14 12:52
 * Controller que gestiona todas las operaciones delicadas con {@link Oficina} y {@link Organismo}
 *
 * @author earrivi
 */
@Controller
@RequestMapping(value = "/admin")
@SessionAttributes({"oficina", "organismo"})
public class AdminController extends BaseController {


    @EJB(mappedName = "regweb3/CatEstadoEntidadEJB/local")
    private CatEstadoEntidadLocal catEstadoEntidadEjb;


    /**
     * Listado de todos los Organismos
     */
    @RequestMapping(value = "/organismo/list", method = RequestMethod.GET)
    public String listado(Model model, HttpServletRequest request) throws Exception {

        CatEstadoEntidad vigente = catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

        Organismo organismo = new Organismo();
        organismo.setEstado(vigente);

        OrganismoBusquedaForm organismoBusqueda = new OrganismoBusquedaForm(organismo, 1);

        model.addAttribute("organismoBusqueda", organismoBusqueda);
        model.addAttribute("entidades", entidadEjb.getAll());

        return "admin/organismoList";
    }

    /**
     * Listado de organismos
     *
     * @param busqueda
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/organismo/list", method = RequestMethod.POST)
    public ModelAndView list(@ModelAttribute OrganismoBusquedaForm busqueda, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("admin/organismoList");

        Organismo organismo = busqueda.getOrganismo();

        Paginacion paginacion = organismoEjb.busqueda(busqueda.getPageNumber(), busqueda.getEntidad(), organismo);

        mav.addObject("paginacion", paginacion);
        mav.addObject("organismoBusqueda", busqueda);
        mav.addObject("entidades", entidadEjb.getAll());

        return mav;
    }

    /**
     * Carga el formulario para modificar un {@link Organismo}
     */
    @RequestMapping(value = "/organismo/{organismoId}/edit", method = RequestMethod.GET)
    public String editarOrganismo(@PathVariable("organismoId") Long organismoId, Model model, HttpServletRequest request) throws Exception {

        Organismo organismo = null;
        try {
            organismo = organismoEjb.findByIdCompleto(organismoId);

            model.addAttribute("organismos", organismoEjb.findByEntidadByEstado(organismo.getEntidad().getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(organismo);


        return "admin/organismoForm";
    }

    /**
     * Editar un {@link Organismo}
     */
    @RequestMapping(value = "/organismo/{organismoId}/edit", method = RequestMethod.POST)
    public String editarOrganismo(@ModelAttribute @Valid Organismo organismo, Model model, @PathVariable("organismoId") Long organismoId, BindingResult result, SessionStatus status, HttpServletRequest request) throws Exception {


        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            model.addAttribute("organismos", organismoEjb.findByEntidadByEstado(organismo.getEntidad().getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));

            return "admin/organismoForm";
        } else { // Si no hay errores actualizamos el registro
            organismo.setId(organismoId);
            try {
                if (organismo.getEdpPrincipal().getId() == null) {
                    organismo.setEdpPrincipal(null);
                }

                organismoEjb.merge(organismo);

                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));

            } catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            }

            status.setComplete();

            return "redirect:/admin/organismo/list";

        }
    }

    /**
     * Listado de todos los Oficinas
     */
    @RequestMapping(value = "/oficina/list", method = RequestMethod.GET)
    public String listado(Model model) throws Exception {

        CatEstadoEntidad vigente = catEstadoEntidadEjb.findByCodigo(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        Oficina oficina = new Oficina();
        oficina.setEstado(vigente);

        OficinaBusquedaForm oficinaBusqueda = new OficinaBusquedaForm(oficina, null, 1);

        model.addAttribute("oficinaBusqueda", oficinaBusqueda);
        model.addAttribute("entidades", entidadEjb.getAll());

        return "admin/oficinaList";
    }

    /**
     * Listado de oficinas
     *
     * @param busqueda
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/oficina/list", method = RequestMethod.POST)
    public ModelAndView list(@ModelAttribute OficinaBusquedaForm busqueda) throws Exception {

        ModelAndView mav = new ModelAndView("admin/oficinaList");
        Oficina oficina = busqueda.getOficina();

        Paginacion paginacion = oficinaEjb.busqueda(busqueda.getPageNumber(), busqueda.getEntidad(), oficina.getCodigo(), oficina.getDenominacion(), oficina.getEstado().getId());

        busqueda.setPageNumber(1);
        mav.addObject("paginacion", paginacion);
        mav.addObject("oficinaBusqueda", busqueda);
        mav.addObject("entidades", entidadEjb.getAll());

        return mav;
    }


    /**
     * Carga el formulario para modificar un {@link Oficina}
     */
    @RequestMapping(value = "/oficina/{oficinaId}/edit", method = RequestMethod.GET)
    public String editarOficina(@PathVariable("oficinaId") Long oficinaId, Model model, HttpServletRequest request) throws Exception {

        Oficina oficina = null;
        try {
            oficina = oficinaEjb.findById(oficinaId);

            model.addAttribute("oficinas", oficinaEjb.findByEntidadByEstado(oficina.getOrganismoResponsable().getEntidad().getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
            model.addAttribute("organismos", organismoEjb.findByEntidadByEstado(oficina.getOrganismoResponsable().getEntidad().getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(oficina);


        return "admin/oficinaForm";
    }

    /**
     * Editar un {@link Oficina}
     */
    @RequestMapping(value = "/oficina/{oficinaId}/edit", method = RequestMethod.POST)
    public String editarOficina(@ModelAttribute @Valid Oficina oficina, Model model, @PathVariable("oficinaId") Long oficinaId,BindingResult result, SessionStatus status, HttpServletRequest request) throws Exception {


        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            model.addAttribute("oficinas", oficinaEjb.findByEntidadByEstado(oficina.getOrganismoResponsable().getEntidad().getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
            model.addAttribute("organismos", organismoEjb.findByEntidadByEstado(oficina.getOrganismoResponsable().getEntidad().getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));

            return "admin/oficinaForm";
        } else { // Si no hay errores actualizamos el registro
            oficina.setId(oficinaId);
            try {

                if (oficina.getOficinaResponsable().getId() == null) {
                    oficina.setOficinaResponsable(null);
                }

                oficinaEjb.merge(oficina);

                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));

            } catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            }

            status.setComplete();

            return "redirect:/admin/oficina/list";

        }
    }

    @ModelAttribute("estados")
    public List<CatEstadoEntidad> estados() throws Exception {
        return catEstadoEntidadEjb.getAll();
    }

    @InitBinder("oficina")
    public void oficinaBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");

    }

    @InitBinder("organismo")
    public void organismoBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");

    }
}
