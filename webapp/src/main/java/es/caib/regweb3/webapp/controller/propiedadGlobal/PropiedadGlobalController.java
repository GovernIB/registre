package es.caib.regweb3.webapp.controller.propiedadGlobal;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.PropiedadGlobal;
import es.caib.regweb3.persistence.ejb.BaseEjbJPA;
import es.caib.regweb3.persistence.ejb.PropiedadGlobalLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.PropiedadGlobalValidator;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fundació Bit
 * Date: 05/05/16
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb3.model.PropiedadGlobal}
 *
 * @author earivi
 */
@Controller
@RequestMapping(value = "/propiedadGlobal")
@SessionAttributes("propiedadGlobal")
public class PropiedadGlobalController extends BaseController {

    @Autowired
    private PropiedadGlobalValidator propiedadGlobalValidator;

    @EJB(mappedName = "regweb3/PropiedadGlobalEJB/local")
    public PropiedadGlobalLocal propiedadGlobalEjb;


    /**
     * Listado de todas las {@link es.caib.regweb3.model.PropiedadGlobal}
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listado() {
        return "redirect:/propiedadGlobal/list/1";
    }

    /**
     * Listado de todos las {@link es.caib.regweb3.model.PropiedadGlobal}
     */
    @RequestMapping(value = "/list/{pageNumber}", method = RequestMethod.GET)
    public ModelAndView listado(@PathVariable Integer pageNumber, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("propiedadGlobal/propiedadGlobalList");
        List<PropiedadGlobal> listado = new ArrayList<PropiedadGlobal>();
        Paginacion paginacion = null;

        if (isSuperAdmin(request)) { // Si es SuperAdministrador, cargamos las propiedades de REGWEB3
            listado = propiedadGlobalEjb.getPaginationREGWEB3((pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION);
            Long total = propiedadGlobalEjb.getTotalREGWEB3();

            paginacion = new Paginacion(total.intValue(), pageNumber);

        } else if (isAdminEntidad(request)) { // Si es AdminEntidad, cargamos las propiedades de la Entidad
            Entidad entidadActiva = getEntidadActiva(request);

            listado = propiedadGlobalEjb.getPaginationByEntidad((pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION, entidadActiva.getId());
            Long total = propiedadGlobalEjb.getTotalByEntidad(entidadActiva.getId());

            paginacion = new Paginacion(total.intValue(), pageNumber);
        }

        mav.addObject("paginacion", paginacion);
        mav.addObject("listado", listado);

        return mav;
    }


    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.PropiedadGlobal}
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String nuevoPropiedad(Model model, HttpServletRequest request) throws Exception {

        Entidad entidad = getEntidadActiva(request);
        PropiedadGlobal propiedadGlobal = null;

        if (isSuperAdmin(request)) { // Si es SuperAdministrador, la propiedad será para todo REGWEB3
            propiedadGlobal = new PropiedadGlobal();

        } else if (isAdminEntidad(request)) { // Si es AdminEntidad, la propiedad será solo para la Entidad
            propiedadGlobal = new PropiedadGlobal(entidad.getId());
        }

        model.addAttribute(propiedadGlobal);

        return "propiedadGlobal/propiedadGlobalForm";
    }

    /**
     * Guardar una nueva {@link es.caib.regweb3.model.PropiedadGlobal}
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String nuevoPropiedad(@ModelAttribute PropiedadGlobal propiedadGlobal, BindingResult result,
                               SessionStatus status, HttpServletRequest request) {


        propiedadGlobalValidator.validate(propiedadGlobal, result);

        if (result.hasErrors()) {

            return "propiedadGlobal/propiedadGlobalForm";
        } else { // Si no hay errores guardamos el registro

            try {
                propiedadGlobalEjb.persist(propiedadGlobal);

                Mensaje.saveMessageInfo(request, getMessage("regweb.guardar.registro"));

            } catch (Exception e) {
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                e.printStackTrace();
            }

            status.setComplete();
            return "redirect:/propiedadGlobal/list";
        }
    }


    /**
     * Carga el formulario para modificar una {@link es.caib.regweb3.model.PropiedadGlobal}
     */
    @RequestMapping(value = "/{propiedadGlobalId}/edit", method = RequestMethod.GET)
    public String editarPropiedad(@PathVariable("propiedadGlobalId") Long propiedadGlobalId, Model model, HttpServletRequest request) {

        PropiedadGlobal propiedadGlobal = null;

        try {
            propiedadGlobal = propiedadGlobalEjb.findById(propiedadGlobalId);
            model.addAttribute(propiedadGlobal);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "propiedadGlobal/propiedadGlobalForm";
    }

    /**
     * Editar una {@link es.caib.regweb3.model.PropiedadGlobal}
     */
    @RequestMapping(value = "/{propiedadGlobalId}/edit", method = RequestMethod.POST)
    public String editarPropiedad(@ModelAttribute @Valid PropiedadGlobal propiedadGlobal, BindingResult result,
                                SessionStatus status, HttpServletRequest request) {

        propiedadGlobalValidator.validate(propiedadGlobal, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            return "propiedadGlobal/propiedadGlobalForm";
        } else { // Si no hay errores actualizamos el registro

            try {

                propiedadGlobalEjb.merge(propiedadGlobal);

                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));

            } catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            }

            status.setComplete();
            return "redirect:/propiedadGlobal/list";

        }
    }

    /**
     * Eliminar una {@link es.caib.regweb3.model.PropiedadGlobal}
     */
    @RequestMapping(value = "/{propiedadGlobalId}/delete")
    public String eliminarPropiedad(@PathVariable Long propiedadGlobalId, HttpServletRequest request) {

        try {

            PropiedadGlobal propiedadGlobal = propiedadGlobalEjb.findById(propiedadGlobalId);

            propiedadGlobalEjb.remove(propiedadGlobal);

            Mensaje.saveMessageInfo(request, getMessage("regweb.eliminar.registro"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.relaciones.registro"));
            e.printStackTrace();
        }

        return "redirect:/propiedadGlobal/list";
    }


    @InitBinder("propiedadGlobal")
    public void initBinder(WebDataBinder binder) {

        binder.setDisallowedFields("id");
        binder.setValidator(this.propiedadGlobalValidator);
    }
}
