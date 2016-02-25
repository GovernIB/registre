package es.caib.regweb3.webapp.controller.oficina;

import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.persistence.ejb.OficinaLocal;
import es.caib.regweb3.persistence.ejb.OrganismoLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.OficinaBusquedaForm;
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
 * Controller que gestiona todas las operaciones con {@link Oficina}
 *
 * @author earrivi
 */
@Controller
@RequestMapping(value = "/oficina")
@SessionAttributes("oficina")
public class OficinaController extends BaseController {

    //protected final Logger log = Logger.getLogger(getClass());

    @EJB(mappedName = "regweb3/OficinaEJB/local")
    public OficinaLocal oficinaEjb;

    @EJB(mappedName = "regweb3/OrganismoEJB/local")
    public OrganismoLocal organismoEjb;

    /**
     * Listado de todos los Oficinas
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listado(Model model) {
        OficinaBusquedaForm oficinaBusqueda = new OficinaBusquedaForm(new Oficina(), 1);
        model.addAttribute("oficinaBusqueda", oficinaBusqueda);

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
    public ModelAndView list(@ModelAttribute OficinaBusquedaForm busqueda) throws Exception {

        ModelAndView mav = new ModelAndView("oficina/oficinaList");
        Oficina oficina = busqueda.getOficina();

        Paginacion paginacion = oficinaEjb.busqueda(busqueda.getPageNumber(), oficina.getCodigo(), oficina.getDenominacion());

        busqueda.setPageNumber(1);
        mav.addObject("paginacion", paginacion);
        mav.addObject("oficinaBusqueda", busqueda);

        return mav;
    }


    /**
     * Carga el formulario para modificar un {@link Oficina}
     */
    @RequestMapping(value = "/{oficinaId}/edit", method = RequestMethod.GET)
    public String editarOficina(@PathVariable("oficinaId") Long oficinaId, Model model, HttpServletRequest request) {

        if (!isAdminEntidad(request)) { // Solo si es Administrador Entidad
            Mensaje.saveMessageError(request, getMessage("error.autorizacion"));
            return "redirect:/inici";
        }

        Oficina oficina = null;
        try {
            oficina = oficinaEjb.findById(oficinaId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute(oficina);

        return "oficina/oficinaForm";
    }

    /**
     * Editar un {@link Oficina}
     */
    @RequestMapping(value = "/{oficinaId}/edit", method = RequestMethod.POST)
    public String editarOficina(@ModelAttribute @Valid Oficina oficina, BindingResult result, SessionStatus status, HttpServletRequest request) {


        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            return "oficina/oficinaForm";
        } else { // Si no hay errores actualizamos el registro

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

            return "redirect:/oficina/list";

        }
    }

    @ModelAttribute("organismos")
    public List<Organismo> organismos(HttpServletRequest request) throws Exception {

        return organismoEjb.findByEntidadByEstado(getEntidadActiva(request).getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

    }

    @ModelAttribute("oficinas")
    public List<Oficina> oficinas(HttpServletRequest request) throws Exception {

        return oficinaEjb.findByEntidadByEstado(getEntidadActiva(request).getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);

    }


    @InitBinder("oficina")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");
        //binder.setDisallowedFields("roles");

        // binder.setValidator(this.oficinaValidator);
    }
}
