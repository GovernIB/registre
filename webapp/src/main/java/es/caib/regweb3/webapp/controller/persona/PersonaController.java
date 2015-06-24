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
import es.caib.regweb3.webapp.utils.JsonResponse;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.utils.PersonaJson;
import es.caib.regweb3.webapp.validator.PersonaWebValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
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
      // Si hay errores volvemos a la vista del formulario
      /*
      List<FieldError> errors = result.getFieldErrors();
      for (FieldError error : errors) {
        log.error("Error: " + error.getField() + " - " + error.getCode() + " - " + error.getDefaultMessage());
      }
      */
      return "persona/personaForm";
    } else { // Si no hay errores guardamos el registro

      try {

       if(!StringUtils.isEmpty(persona.getDocumento())){
           persona.setDocumento(persona.getDocumento().toUpperCase());
       }

        personaEjb.persist(persona);

        Mensaje.saveMessageInfo(request, getMessage("regweb.guardar.registro"));
        status.setComplete();
      } catch (Exception e) {
        Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
        e.printStackTrace();
      }

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
                 status.setComplete();

             } catch (Exception e) {
                 e.printStackTrace();
                 Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
             }


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

    /**
     *  Añade una Persona existente a la variable de sesion que almacena los interesados que son de tipo Persona
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/addPersonaSesion", method = RequestMethod.GET)
    @ResponseBody
    public Boolean addPersonaSesion(@RequestParam Long id, HttpServletRequest request) {

        log.info("Dentro de personaSesion: " + id);

        HttpSession session = request.getSession();

        try {
            Persona persona = personaEjb.findById(id);

            List<Persona> personas = (List<Persona>) session.getAttribute("personas");

            if(personas != null){
                personas.add(persona);
                session.setAttribute("personas", personas);
            }else{
                personas = new ArrayList<Persona>();
                personas.add(persona);
                session.setAttribute("personas", personas);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Elimina una Persona de la variable de sesion que almacena los interesados que son de tipo Persona
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/eliminarPersonaSesion", method = RequestMethod.GET)
    @ResponseBody
    public Boolean eliminarPersonaSesion(@RequestParam Long id, HttpServletRequest request) {

        log.info("Dentro de eliminarPersona: " + id);

        HttpSession session = request.getSession();

        try {
            Persona persona = obtenerPersonaSesion(id,session);

            // Si tiene Representate, también lo eliminamos.
           /* if(persona.getRepresentante() != null){
                log.info("Tiene representante y lo eliminamos");
                Persona representante = obtenerPersonaSesion(persona.getRepresentante().getId(), session);
                eliminarPersonaSesion(representante,session);
            }*/

            // Eliminamos la Persona
            eliminarPersonaSesion(persona,session);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }



    /**
     * Elimina un representante de la Sesion y lo desvincula de su representado.
     * @param idRepresentante
     * @param idRepresentado
     * @param request
     * @return
     */
    @RequestMapping(value = "/eliminarRepresentanteSesion", method = RequestMethod.GET)
    @ResponseBody
    public Boolean eliminarRepresentante(@RequestParam Long idRepresentante,@RequestParam Long idRepresentado, HttpServletRequest request) {

        log.info("Dentro de eliminar representante: " + idRepresentante);

        HttpSession session = request.getSession();

        try {
            Persona representante = new Persona(idRepresentante);
            Persona representado = obtenerPersonaSesion(idRepresentado,session);

          //  representado.setRepresentante(null);

            eliminarPersonaSesion(representante,session);
            actualizarPersonaSesion(representado,session);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * Crea o modifica una Persona y la añade a la variable de sesion que almacena los interesados que son de tipo Persona
     * @param persona
     * @param request
     * @param result
     * @return
     */
    @RequestMapping(value="/gestionar/{accion}", method= RequestMethod.POST)
    @ResponseBody
    public JsonResponse gestionarPersona(@PathVariable String accion, @RequestBody Persona persona, HttpServletRequest request, BindingResult result) {

        Boolean isRepresentante = false;
        String idRepresentado = null;

        log.info("Accion: " + accion);
       // log.info("Representante?: " + persona.getIsRepresentante());

       /* if(persona.getIsRepresentante()){
            log.info("Representado: " + persona.getRepresentado().getId());
            isRepresentante = true;
            idRepresentado = persona.getRepresentado().getId().toString();
        }*/

        JsonResponse jsonResponse = new JsonResponse();

        // Validamos la nueva Persona
        personaValidator.validate(persona,result);


        if (result.hasErrors()) { // Si hay errores, preparamos la respuesta.
            
            List<FieldError> errores = setDefaultMessageToErrors(result.getFieldErrors(), "Persona");
            jsonResponse.setStatus("FAIL");
            jsonResponse.setErrores(errores);
            
        } else { // Si no hay errores, guardamos la Persona

            jsonResponse.setStatus("SUCCESS");

            HttpSession session = request.getSession();

            Entidad entidad = getEntidadActiva(request);

            try {

                // Si no se ha escogido ningún TipoDocumento, lo ponemos a null
                if(persona.getTipoDocumentoIdentificacion() != null && persona.getTipoDocumentoIdentificacion() == -1){persona.setTipoDocumentoIdentificacion(null);}

                // Si no se ha escogido ningún Canal de Notificación, lo ponemos a null
                if(persona.getCanal() != null && persona.getCanal() == -1){persona.setCanal(null);}

                // Si no se ha escogido ningúna Provincia, lo ponemos a null
                if(persona.getPais().getId() != null && persona.getPais().getId() == -1){persona.setPais(null);}

                // Si no se ha escogido ningúna Provincia, lo ponemos a null
                if(persona.getProvincia().getId() != null && persona.getProvincia().getId() == -1){
                    persona.setProvincia(null);
                    persona.setLocalidad(null);
                }

                // Asignamos la Entidad a la que pertenece esta persona
                persona.setEntidad(entidad);

                // Creamos la respuesta
                PersonaJson personaJson = new PersonaJson();
              //  personaJson.setIsRepresentante(persona.getIsRepresentante());


                // Según la configuración de la Entidad, guardamos o no la Persona.
                switch (entidad.getConfiguracionPersona().intValue()){

                    case (int)RegwebConstantes.CONFIGURACION_PERSONA_SIN_GUARDAR: // No se guardan
                        if(accion.equals("nuevo")){
                            persona.setId((long)(Math.random()*10000));
                            log.info("Id generado: " + persona.getId());
                        }

                        break;

                    case (int)RegwebConstantes.CONFIGURACION_PERSONA_GUARDAR_TODOS: // Se guardan las Personas
                        if(accion.equals("nuevo")){
                            persona = personaEjb.persist(persona);
                        }else if(accion.equals("editar")){
                            persona = personaEjb.merge(persona);
                        }

                        break;

                    case (int)RegwebConstantes.CONFIGURACION_PERSONA_CONFIRMAR_NUEVA_PERSONA: // Se pregunta antes de Guardar

                        if(accion.equals("nuevo")){

                            if(persona.isGuardarInteresado()){
                                persona = personaEjb.persist(persona);
                            }else{
                                persona.setId((long)(Math.random()*10000));
                                log.info("Id generado: " + persona.getId());
                            }

                        }else if(accion.equals("editar")){
                            if(persona.isGuardarInteresado()){
                                persona = personaEjb.merge(persona);
                            }

                        }


                        break;

                    case (int)RegwebConstantes.CONFIGURACION_PERSONA_SISTEMA_EXTERNO: 
                    break; // todo:bbdd de terceros

                }

                //Almacenamos el Id de la nueva persona/representante creado
                personaJson.setId(persona.getId().toString());

                // Gestionamos en caso de que sea Representante
                if(isRepresentante){ // Si se trata de un representante lo indicamos

                    personaJson.setRepresentado(new PersonaJson(idRepresentado));

                    // Asociamos el representante a la Persona representada
                    Persona representado = obtenerPersonaSesion(Long.valueOf(idRepresentado),session);
                    //persona.setRepresentado(representado);
                  //  representado.setRepresentante(persona);

                    actualizarPersonaSesion(representado,session);
                }

                // Generamos el nombre a mostrar según el tipo de persona
                if(persona.getTipo().equals(RegwebConstantes.TIPO_PERSONA_FISICA)){
                    personaJson.setNombre(persona.getNombrePersonaFisica());
                    log.info("Nombre fisica asignado: " + personaJson.getNombre());
                }else if(persona.getTipo().equals(RegwebConstantes.TIPO_PERSONA_JURIDICA)){
                    personaJson.setNombre(persona.getNombrePersonaJuridica());
                    log.info("Nombre Juridica asignado: " + personaJson.getNombre());
                }

                jsonResponse.setResult(personaJson);

                // Añadimos o actualizamos la persona/representante en la Sesion
                List<Persona> personas = (List<Persona>) session.getAttribute("personas");

                if(accion.equals("nuevo")){ // Si es nuevo

                    if(personas != null){

                        personas.add(persona);
                        session.setAttribute("personas", personas);
                    }else{

                        personas = new ArrayList<Persona>();
                        personas.add(persona);
                        session.setAttribute("personas", personas);
                    }

                }else if(accion.equals("editar")){ // si es una edición

                    if(personas.contains(persona)){
                        log.info("Actualizamos persona en la sesion");
                        personas.remove(persona);
                        personas.add(persona);
                    }
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return jsonResponse;
    }


    /**
     * Busca Persona según los parámetros recibidos
     * @param persona
     * @param request
     * @param result
     * @return
     */
    @RequestMapping(value="/busquedaPersona", method= RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Persona> busquedaPersona(@RequestBody Persona persona, HttpServletRequest request, BindingResult result) throws Exception {

        Entidad entidad = getEntidadActiva(request);
        List<Persona> personas = new ArrayList<Persona>();

        if(persona.getTipo().equals(Long.valueOf(RegwebConstantes.TIPO_PERSONA_FISICA))){ // Personas Físicas
            personas = personaEjb.busquedaFisicas(entidad.getId(), persona.getNombre(), persona.getApellido1(), persona.getApellido2(), persona.getDocumento(), persona.getTipo());
            log.info("Buscar Persona juridica Total: " + personas.size());
        }else if(persona.getTipo().equals(Long.valueOf(RegwebConstantes.TIPO_PERSONA_JURIDICA))){ // Personas Jurídicas
            personas = personaEjb.busquedaJuridicas(entidad.getId(), persona.getRazonSocial(), persona.getDocumento(), persona.getTipo());
            log.info("Buscar Persona juridica Total: " + personas.size());
        }else if(persona.getTipo().equals(Long.valueOf(0))) { // Todas las Personas
            personas = personaEjb.busquedaPersonas(entidad.getId(), persona.getNombre(), persona.getApellido1(),persona.getApellido2(), persona.getDocumento(), persona.getRazonSocial());
            log.info("Buscar Personas Total: " + personas.size());
        }

        return personas;
    }

    /**
     * Obtiene la {@link es.caib.regweb3.model.Persona} según su identificador.
     * Si no la encuentra la busca en la Sesion.
     */
    @RequestMapping(value = "/obtenerPersona", method = RequestMethod.GET)
    public @ResponseBody
    Persona obtenerPersona(@RequestParam Long id, HttpServletRequest request) throws Exception {
        log.info("ObtenerPersona: " + id);

        Persona persona = personaEjb.findById(id);

        if(persona == null){
            //La obtenemos de la sesion, porqué se trata de una Persona que no se ha almacenado en la bbdd
            HttpSession session = request.getSession();
            List<Persona> personas = (List<Persona>) session.getAttribute("personas");

            Persona personaEditar =  new Persona(id);
            int posicion = personas.indexOf(personaEditar);

            if (posicion != -1){
                //Persona persona1 = personas.get(posicion);
                return personas.get(posicion);
            }else{
                return  null;
            }

        }else{
            return persona;
        }

    }

    /**
     * Busca una Persona en la lista de Personas almacenada en la Sesion
     * @param idPersona de la Persona a buscar
     * @param session donde buscar
     * @return
     */
    public Persona obtenerPersonaSesion(Long idPersona, HttpSession session){

        Persona persona = new Persona(idPersona);

        List<Persona> personas = (List<Persona>) session.getAttribute("personas");

        if(personas.contains(persona)){
            int posicion = personas.indexOf(persona);

            if (posicion != -1){
                return personas.get(posicion);
            }else{
                return  null;
            }

        }else{
            return null;
        }
    }

    /**
     * Actualiza una Persona por otra en la sesion
     * @param persona
     * @param session
     */
    public void actualizarPersonaSesion(Persona persona, HttpSession session){

        List<Persona> personas = (List<Persona>) session.getAttribute("personas");

        if(personas.contains(persona)){
            personas.remove(persona);

            personas.add(persona);
            session.setAttribute("personas", personas);
        }

    }

    /**
     * Elimina una Persona de la sesion
     * @param persona
     * @param session
     */
    public void eliminarPersonaSesion(Persona persona, HttpSession session){

        List<Persona> personas = (List<Persona>) session.getAttribute("personas");

        if(personas.contains(persona)){
            log.info("Eliminado!");
            personas.remove(persona);
            session.setAttribute("personas", personas);
        }

    }

    @InitBinder("persona")
    public void initBinder(WebDataBinder binder) {

        binder.setDisallowedFields("id");
        binder.setValidator(this.personaValidator);
    }
}
