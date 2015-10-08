package es.caib.regweb3.webapp.controller.persona;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.CatLocalidadLocal;
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
 * @author mgonzalez
 */
@Controller
@RequestMapping(value = "/persona")
@SessionAttributes("persona")
public class PersonaController extends BaseController {

    //protected final Logger log = Logger.getLogger(getClass());

    @Autowired
    private PersonaWebValidator personaValidator;
    
    @EJB(mappedName = "regweb3/PersonaEJB/local")
    public PersonaLocal personaEjb;
    
    @EJB(mappedName = "regweb3/CatLocalidadEJB/local")
    public CatLocalidadLocal catLocalidadEjb;
    
    @EJB(mappedName = "regweb3/CatProvinciaEJB/local")
    public CatProvinciaLocal catProvinciaEjb;
    
    @EJB(mappedName = "regweb3/CatPaisEJB/local")
    public CatPaisLocal catPaisEjb;


     /**
      * Listado de todos los Personas
      */
     @RequestMapping(value = "/list", method = RequestMethod.GET)
     public String listado(Model model) {

         PersonaBusquedaForm personaBusqueda =  new PersonaBusquedaForm(new Persona(),1);
         model.addAttribute("personaBusqueda",personaBusqueda);

         return "persona/personaList";
     }

    /**
     * Realiza la busqueda de {@link es.caib.regweb3.model.Persona} según los parametros del formulario
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ModelAndView list(@ModelAttribute PersonaBusquedaForm busqueda, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("persona/personaList");

        Entidad entidad = getEntidadActiva(request);
        Persona persona = busqueda.getPersona();

        Paginacion paginacion = personaEjb.busqueda(busqueda.getPageNumber(),entidad.getId(), persona.getNombre(), persona.getApellido1(), persona.getApellido2(), persona.getDocumento());

        busqueda.setPageNumber(1);
        mav.addObject("paginacion", paginacion);
        mav.addObject("personaBusqueda", busqueda);

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

         return "persona/personaForm";
     }

  /**
   * Guardar un nuevo {@link es.caib.regweb3.model.Persona}
   */
  @RequestMapping(value = "/new", method = RequestMethod.POST)
  public String nuevoPersona(@ModelAttribute Persona persona, BindingResult result,
      SessionStatus status, HttpServletRequest request) {

    cleanEmptyValues(persona);

    personaValidator.validate(persona, result);

    if (result.hasErrors()) {

      return "persona/personaForm";
    } else { // Si no hay errores guardamos el registro

      try {

       if(!StringUtils.isEmpty(persona.getDocumento())){
           persona.setDocumento(persona.getDocumento().toUpperCase());
       }

        personaEjb.persist(persona);

        Mensaje.saveMessageInfo(request, getMessage("regweb.guardar.registro"));

      } catch (Exception e) {
        Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
        e.printStackTrace();
      }

        status.setComplete();
      return "redirect:/persona/list";
    }
  }

  public void cleanEmptyValues(Persona persona) {
    // Si no se ha escogido ningúna Canal, lo ponemos a null
    if (persona.getCanal() == null) {
      persona.setCanal(null);
    }

    // Si no se ha escogido ningúna Provincia, lo ponemos a null
    if (persona.getProvincia() == null || persona.getProvincia().getId() == null) {
      persona.setProvincia(null);
      persona.setLocalidad(null);
    } else {
      if (persona.getLocalidad() == null || persona.getLocalidad().getId() == null) {
        persona.setLocalidad(null);
      }
    }

    if (persona.getPais() == null || persona.getPais().getId() == null) {
      persona.setPais(null);
        persona.setProvincia(null);
        persona.setLocalidad(null);
    }

  }

     /**
      * Carga el formulario para modificar un {@link es.caib.regweb3.model.Persona}
      */
     @RequestMapping(value = "/{personaId}/edit", method = RequestMethod.GET)
     public String editarPersona(@PathVariable("personaId") Long personaId, Model model,HttpServletRequest request) {

         Persona persona = null;
         Entidad entidad = getEntidadActiva(request);

         try {
             persona = personaEjb.findById(personaId);

             if(!persona.getEntidad().getId().equals(entidad.getId())){
                 Mensaje.saveMessageError(request, getMessage("error.autorizacion"));
                 return "redirect:/persona/list";
             }

         }catch (Exception e) {
             e.printStackTrace();
         }
         model.addAttribute(persona);
         return "persona/personaForm";
     }

     /**
      * Editar un {@link es.caib.regweb3.model.Persona}
      */
     @RequestMapping(value = "/{personaId}/edit", method = RequestMethod.POST)
     public String editarPersona(@ModelAttribute @Valid Persona persona,BindingResult result,
                                 SessionStatus status, HttpServletRequest request) {
       
       cleanEmptyValues(persona);
       

         personaValidator.validate(persona, result);

         if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
             return "persona/personaForm";
         }else { // Si no hay errores actualizamos el registro

             try {
/*
                 // Si no se ha escogido ningún TipoDocumento, lo ponemos a null
                 if(persona.getTipoDocumentoIdentificacion().getId() != null && persona.getTipoDocumentoIdentificacion().getId() == -1){persona.setTipoDocumentoIdentificacion(null);}

                 // Si no se ha escogido ningúna Provincia, lo ponemos a null
                 if(persona.getCanal().getId() != null && persona.getCanal().getId() == -1){persona.setCanal(null);}

                 // Si no se ha escogido ningúna Provincia, lo ponemos a null
                 if(persona.getProvincia().getId() != null && persona.getProvincia().getId() == -1){
                     persona.setProvincia(null);
                     persona.setLocalidad(null);
                 }
*/
                 
                 persona = personaEjb.merge(persona);


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

             if(!persona.getEntidad().getId().equals(entidad.getId())){
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

    @ModelAttribute("tiposDocumento")
    public long[] tiposDocumento() throws Exception {
        return RegwebConstantes.TIPOS_DOCUMENTOID;
    }

    @ModelAttribute("tiposPersona")
    public Long[] tiposPersona() throws Exception {
        return RegwebConstantes.TIPOS_PERSONA;
    }
    
    @ModelAttribute("canales")
    public long[] canales() throws Exception {
        return RegwebConstantes.CANALES_NOTIFICACION;
    }

    @ModelAttribute("provincias")
    public List<CatProvincia> provincias() throws Exception {
        return catProvinciaEjb.getAll();
    }

    @ModelAttribute("paises")
    public List<CatPais> paises() throws Exception {
        return catPaisEjb.getAll();
    }


    /**
     * Obtiene los {@link es.caib.regweb3.model.CatLocalidad} de de la Provincia seleccionada
     */
    @RequestMapping(value = "/obtenerLocalidades", method = RequestMethod.GET)
    public @ResponseBody
    List<CatLocalidad> obtenerLocalidades(@RequestParam Long id) throws Exception {

        return catLocalidadEjb.getByProvincia(id);
    }


    /*METODOS REST CON LAS OPERACIONES NECESARIAS CON EL REGISTRO DE ENTRADA*/




    @InitBinder("persona")
    public void initBinder(WebDataBinder binder) {

        binder.setDisallowedFields("id");
        binder.setValidator(this.personaValidator);
    }
}
