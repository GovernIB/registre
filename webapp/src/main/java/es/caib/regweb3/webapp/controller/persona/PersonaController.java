package es.caib.regweb3.webapp.controller.persona;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Persona;
import es.caib.regweb3.persistence.ejb.CatPaisLocal;
import es.caib.regweb3.persistence.ejb.CatProvinciaLocal;
import es.caib.regweb3.persistence.ejb.PersonaLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.PersonaBusquedaForm;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.PersonaWebValidator;
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
import java.util.List;

/**
 * Created 14/02/14 12:52
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb3.model.Persona}
 *
 * @author mgonzalez
 */
@Controller
@RequestMapping(value = "/persona")
@SessionAttributes("persona")
public class PersonaController extends BaseController {

    //protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PersonaWebValidator personaValidator;

    @EJB(mappedName = PersonaLocal.JNDI_NAME)
    private PersonaLocal personaEjb;

    @EJB(mappedName = CatProvinciaLocal.JNDI_NAME)
    private CatProvinciaLocal catProvinciaEjb;

    @EJB(mappedName = CatPaisLocal.JNDI_NAME)
    private CatPaisLocal catPaisEjb;


    /**
     * Listado de todos los Personas
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listado(Model model) {

        PersonaBusquedaForm personaBusqueda = new PersonaBusquedaForm(new Persona(), 1);
        model.addAttribute("personaBusqueda", personaBusqueda);
        model.addAttribute("tiposPersona", RegwebConstantes.TIPOS_PERSONA);

        return "persona/personaList";
    }

    /**
     * Realiza la busqueda de {@link es.caib.regweb3.model.Persona} según los parametros del formulario
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ModelAndView list(@ModelAttribute PersonaBusquedaForm busqueda, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("persona/personaList");

        Entidad entidad = getEntidadActiva(request);
        Persona persona = busqueda.getPersona();

        Paginacion paginacion = personaEjb.busqueda(busqueda.getPageNumber(), entidad.getId(), persona.getNombre(), persona.getApellido1(), persona.getApellido2(), persona.getDocumento(), persona.getTipo());

        busqueda.setPageNumber(1);
        mav.addObject("paginacion", paginacion);
        mav.addObject("personaBusqueda", busqueda);
        mav.addObject("tiposPersona", RegwebConstantes.TIPOS_PERSONA);

        return mav;
    }

    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.Persona}
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String nuevoPersona(Model model, HttpServletRequest request) throws Exception {

        Entidad entidad = getEntidadActiva(request);

        Persona persona = new Persona();
        persona.setEntidad(entidad);

        model.addAttribute(persona);

        model.addAttribute("tiposDocumento", RegwebConstantes.TIPOS_DOCUMENTOID);
        model.addAttribute("tiposPersona", RegwebConstantes.TIPOS_PERSONA);
        model.addAttribute("canales", RegwebConstantes.CANALES_NOTIFICACION);
        model.addAttribute("provincias", catProvinciaEjb.getAll());
        model.addAttribute("paises", catPaisEjb.getAll());

        return "persona/personaForm";
    }

    /**
     * Guardar un nuevo {@link es.caib.regweb3.model.Persona}
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String nuevoPersona(@ModelAttribute Persona persona, BindingResult result,Model model,
                               SessionStatus status, HttpServletRequest request) throws Exception {

        cleanEmptyValues(persona);

        personaValidator.validate(persona, result);

        if (result.hasErrors()) {
            model.addAttribute("tiposDocumento", RegwebConstantes.TIPOS_DOCUMENTOID);
            model.addAttribute("tiposPersona", RegwebConstantes.TIPOS_PERSONA);
            model.addAttribute("canales", RegwebConstantes.CANALES_NOTIFICACION);
            model.addAttribute("provincias", catProvinciaEjb.getAll());
            model.addAttribute("paises", catPaisEjb.getAll());

            return "persona/personaForm";
        } else { // Si no hay errores guardamos el registro

            try {

                if (StringUtils.isNotEmpty(persona.getDocumento())) {
                    persona.setDocumento(persona.getDocumento().toUpperCase());
                }

                personaEjb.guardarPersona(persona);

                Mensaje.saveMessageInfo(request, getMessage("regweb.guardar.registro"));

            } catch (Exception e) {
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                e.printStackTrace();
            }

            status.setComplete();
            return "redirect:/persona/list";
        }
    }


    /**
     * Carga el formulario para modificar un {@link es.caib.regweb3.model.Persona}
     */
    @RequestMapping(value = "/{personaId}/edit", method = RequestMethod.GET)
    public String editarPersona(@PathVariable("personaId") Long personaId, Model model, HttpServletRequest request) throws Exception {

        Persona persona = null;
        Entidad entidad = getEntidadActiva(request);

        try {
            persona = personaEjb.findById(personaId);

            if (!persona.getEntidad().getId().equals(entidad.getId())) {
                Mensaje.saveMessageError(request, getMessage("error.autorizacion"));
                return "redirect:/persona/list";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("tiposDocumento", RegwebConstantes.TIPOS_DOCUMENTOID);
        model.addAttribute("tiposPersona", RegwebConstantes.TIPOS_PERSONA);
        model.addAttribute("canales", RegwebConstantes.CANALES_NOTIFICACION);
        model.addAttribute("provincias", catProvinciaEjb.getAll());
        model.addAttribute("paises", catPaisEjb.getAll());
        model.addAttribute(persona);
        return "persona/personaForm";
    }

    /**
     * Editar un {@link es.caib.regweb3.model.Persona}
     */
    @RequestMapping(value = "/{personaId}/edit", method = RequestMethod.POST)
    public String editarPersona(@ModelAttribute @Valid Persona persona, BindingResult result,
                                SessionStatus status, HttpServletRequest request) {

        cleanEmptyValues(persona);


        personaValidator.validate(persona, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            return "persona/personaForm";
        } else { // Si no hay errores actualizamos el registro

            try {

                personaEjb.merge(persona);

                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));

            } catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            }

            status.setComplete();
            return "redirect:/persona/list";

        }
    }

    /**
     * Eliminar un {@link es.caib.regweb3.model.Persona}
     */
    @RequestMapping(value = "/{personaId}/delete")
    public String eliminarPersona(@PathVariable Long personaId, HttpServletRequest request) {

        try {

            Persona persona = personaEjb.findById(personaId);
            Entidad entidad = getEntidadActiva(request);

            if (!persona.getEntidad().getId().equals(entidad.getId())) {
                Mensaje.saveMessageError(request, getMessage("error.autorizacion"));
                return "redirect:/persona/list";
            }

            personaEjb.remove(persona);

            Mensaje.saveMessageInfo(request, getMessage("regweb.eliminar.registro"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.relaciones.registro"));
            e.printStackTrace();
        }

        return "redirect:/persona/list";
    }

    /**
     * Listado de todas las Personas duplicadas
     */
    @RequestMapping(value = "/personasDuplicadas", method = RequestMethod.GET)
    public String duplicadas() {
        return "redirect:/persona/personasDuplicadas/1";
    }

    /**
     * Listado de todas las Personas duplicadas
     *
     * @param pageNumber
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/personasDuplicadas/{pageNumber}", method = RequestMethod.GET)
    public ModelAndView duplicadas(@PathVariable Integer pageNumber, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("persona/personasDuplicadas");

        Entidad entidad = getEntidadActiva(request);

        List<Persona> listado = personaEjb.buscarDuplicados(entidad.getId());
        //Long total = tipoAsuntoEjb.getTotalEntidad(entidad.getId());

        Paginacion paginacion = new Paginacion(listado.size(), pageNumber);

        mav.addObject("paginacion", paginacion);
        mav.addObject("listado", listado);

        return mav;
    }


    /**
     * Export de {@link es.caib.regweb3.model.Persona} a Excel
     */
    @RequestMapping(value = "/exportarPersonas", method = RequestMethod.GET)
    public ModelAndView exportar(HttpServletRequest request) throws Exception {

        String nombre = request.getParameter("nombre");

        ModelAndView mav = new ModelAndView("exportarPersonasExcel");

        Entidad entidad = getEntidadActiva(request);

        String apellido1 = request.getParameter("apellido1");
        String apellido2 = request.getParameter("apellido2");
        String documento = request.getParameter("documento");
        Long tipo = Long.valueOf(request.getParameter("tipo"));

        List<Persona> personas = personaEjb.getExportarExcel(entidad.getId(),nombre,apellido1,apellido2, documento,tipo);

        mav.addObject("personas", personas);

        return mav;
    }

    /**
     * Pone a null ciertos campos en función del valor de otros
     *
     * @param persona
     */
    private void cleanEmptyValues(Persona persona) {
        // Si no se ha escogido ningúna Canal, lo ponemos a null
        if (persona.getCanal() == null || persona.getCanal() != 1) {
            //persona.setCanal(null);
            persona.setPais(null);
            persona.setProvincia(null);
            persona.setLocalidad(null);
        }
    }

    @InitBinder("persona")
    public void initBinder(WebDataBinder binder) {

        binder.setDisallowedFields("id");
        binder.setValidator(this.personaValidator);
    }
}
