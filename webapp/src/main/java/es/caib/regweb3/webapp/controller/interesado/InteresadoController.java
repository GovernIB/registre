package es.caib.regweb3.webapp.controller.interesado;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.Persona;
import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.persistence.ejb.CatPaisLocal;
import es.caib.regweb3.persistence.ejb.PersonaLocal;
import es.caib.regweb3.persistence.ejb.RegistroDetalleLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.JsonResponse;
import es.caib.regweb3.webapp.utils.PersonaJson;
import es.caib.regweb3.webapp.validator.InteresadoWebValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fundació BIT.
 * Controller para gestionar los Interesados de un Registro de entrada o salida
 * @author earrivi
 * @author anadal
 * Date: 28/04/14
 */
@Controller
@RequestMapping(value = "/interesado")
public class InteresadoController extends BaseController{
    
    @Autowired
    InteresadoWebValidator interesadoValidator;

    @EJB(mappedName = "regweb3/CatPaisEJB/local")
    public CatPaisLocal catPaisEjb;

    @EJB(mappedName = "regweb3/PersonaEJB/local")
    public PersonaLocal personaEjb;

    @EJB(mappedName = "regweb3/RegistroDetalleEJB/local")
    public RegistroDetalleLocal registroDetalleEjb;
    

    /**
     * Crea un Interesado y la añade a la variable de sesion que almacena los interesados que son de tipo Persona
     * @param interesado
     * @param request
     * @param result
     * @return
     */
    @RequestMapping(value="/gestionar/{tipoRegistro}/nuevo/{idRegistroDetalle}", method= RequestMethod.POST)
    @ResponseBody
    public JsonResponse nuevoInteresado(@PathVariable String tipoRegistro,@PathVariable String idRegistroDetalle, @RequestBody Interesado interesado, HttpServletRequest request, BindingResult result) {
        log.info("Tipo Registro: " + tipoRegistro);
        JsonResponse jsonResponse = new JsonResponse();

        String variable = (tipoRegistro.equals("entrada") ? RegwebConstantes.SESSION_INTERESADOS_ENTRADA:RegwebConstantes.SESSION_INTERESADOS_SALIDA);
        log.info("Variable: " + variable);

        Boolean isRepresentante = false;
        String idRepresentado = null;

        log.info("Dentro de nuevo interesado");
        log.info("idRegistroDetalle: " + idRegistroDetalle);
        log.info("Representante?: " + interesado.getIsRepresentante());

        // Comprobamos si es se trata de un representante
        if(interesado.getIsRepresentante()){
            log.info("Representado: " + interesado.getRepresentado().getId());
            isRepresentante = true;
            idRepresentado = interesado.getRepresentado().getId().toString();
        }

        // Validamos el nuevo Interesado
        interesado.setEntidad(getEntidadActiva(request).getId());
        interesadoValidator.validate(interesado,result);

        if (result.hasErrors()){ // Si hay errores, preparamos la respuesta.

            List<FieldError> errores = setDefaultMessageToErrors(result.getFieldErrors(), "Interesado");
            jsonResponse.setStatus("FAIL");
            jsonResponse.setErrores(errores);
            
        }else{ // Si no hay errores, guardamos la Persona

            jsonResponse.setStatus("SUCCESS");

            HttpSession session = request.getSession();
            Entidad entidad = getEntidadActiva(request);

            try {

                // Procesamos los datos de un interesado
                interesado = procesarInteresado(interesado);

                // Creamos la respuesta especifica
                PersonaJson personaJson = new PersonaJson();
                personaJson.setIsRepresentante(interesado.getIsRepresentante());

                // Según la configuración de la Entidad, guardamos o no la Persona.
                // Asignamos la Entidad a la que pertenece esta persona
                Persona persona = new Persona(interesado);
                persona.setEntidad(entidad);

                switch (entidad.getConfiguracionPersona().intValue()){

                    case (int)RegwebConstantes.CONFIGURACION_PERSONA_SIN_GUARDAR: // No se guardan
                        persona.setId((long)(Math.random()*10000));
                        //log.info("Id generado: " + persona.getId());
                        interesado.setId(persona.getId());
                    break;

                    case (int)RegwebConstantes.CONFIGURACION_PERSONA_GUARDAR_TODOS: // Se guardan las Personas

                        if(StringUtils.isEmpty(persona.getDocumento())){ // Si no hay documento, no se guarda en la bbdd
                            persona.setId((long)(Math.random()*10000));
                        }else{
                            persona = personaEjb.persist(persona);
                        }

                        interesado.setId(persona.getId());
                    break;

                    case (int)RegwebConstantes.CONFIGURACION_PERSONA_CONFIRMAR_NUEVA_PERSONA: // Se pregunta antes de Guardar

                        if(persona.isGuardarInteresado() && !StringUtils.isEmpty(persona.getDocumento())){
                            persona = personaEjb.persist(persona);
                        }else{
                            persona.setId((long)(Math.random()*10000));
                        }

                        interesado.setId(persona.getId());
                    break;

                    case 4: break; // todo:bbdd de terceros

                }


                // Se trata de un nuevo Registro, utilizamos la sesion.
                if(idRegistroDetalle.equals("null")) {

                    // Gestionamos en caso de que sea Representante
                    if(isRepresentante){ // Si se trata de un representante lo indicamos

                        personaJson.setRepresentado(new PersonaJson(idRepresentado));

                        // Asociamos el representante a la Persona representada
                        Interesado representado = obtenerInteresadoSesion(Long.valueOf(idRepresentado), session, variable);
                        interesado.setRepresentado(representado);
                        representado.setRepresentante(interesado);

                        añadirInteresadoSesion(interesado,session, variable);
                        actualizarInteresadoSesion(representado, session, variable);

                    }else{ // Si no lo es
                        // Lo añadimos a la sesion
                        añadirInteresadoSesion(interesado,session, variable);
                    }

                }else{ // Edición de un registro, lo añadimos a la bbdd

                    if(isRepresentante) { // Si se trata de un representante lo indicamos
                        interesado.setRegistroDetalle(new RegistroDetalle(Long.valueOf(idRegistroDetalle)));
                        personaJson.setRepresentado(new PersonaJson(idRepresentado));

                        // Guardamos el Nuevo representante
                        interesado = interesadoEjb.persist(interesado);

                        // Lo asociamos al represenatado
                        Interesado representado = interesadoEjb.findById(Long.valueOf(idRepresentado));
                        representado.setRepresentante(interesado);
                        interesadoEjb.merge(representado);
                    }else{
                        interesado.setRegistroDetalle(new RegistroDetalle(Long.valueOf(idRegistroDetalle)));
                        interesado = interesadoEjb.persist(interesado);
                    }

                }

                // Almacenamos el Id de la nueva persona/representante creado
                personaJson.setId(interesado.getId().toString());

                // Generamos el nombre a mostrar según el tipo de persona
                if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)){
                    personaJson.setNombre(interesado.getNombrePersonaFisica());
                    log.info("Nombre fisica asignado: " + personaJson.getNombre());
                }else if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)){
                    personaJson.setNombre(interesado.getNombrePersonaJuridica());
                    log.info("Nombre Juridica asignado: " + personaJson.getNombre());
                }

                jsonResponse.setResult(personaJson);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return jsonResponse;
    }


    /**
     * Crea o modifica una Interesado y la añade a la variable de sesion que almacena los interesados que son de tipo Persona
     * @param interesado
     * @param request
     * @param result
     * @return
     */
    @RequestMapping(value="/gestionar/{tipoRegistro}/editar/{idRegistroDetalle}", method= RequestMethod.POST)
    @ResponseBody
    public JsonResponse editarInteresado(@PathVariable String tipoRegistro,@PathVariable String idRegistroDetalle,
        @RequestBody Interesado interesado, HttpServletRequest request, BindingResult result) {
        log.info("Tipo Registro: " + tipoRegistro);

        String variable = (tipoRegistro.equals("entrada") ? RegwebConstantes.SESSION_INTERESADOS_ENTRADA:RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        // TODO
        //Boolean isRepresentante = false;
        //String idRepresentado = null;

        log.info("idInteresado editar: " + interesado.getId());
        log.info("idRegistroDetalle: " + idRegistroDetalle);
        log.info("Representante?: " + interesado.getIsRepresentante());

        // Comprobamos si es se trata de un representante
        if(interesado.getIsRepresentante()){
            log.info("Representado: " + interesado.getRepresentado().getId());
            // TODO
            //isRepresentante = true;
            //idRepresentado = interesado.getRepresentado().getId().toString();
        }

        JsonResponse jsonResponse = new JsonResponse();

        // Validamos la nueva Persona
        interesado.setEntidad(getEntidadActiva(request).getId());
        interesadoValidator.validate(interesado,result);

        if (result.hasErrors()) { // Si hay errores, preparamos la respuesta.

            List<FieldError> errores = setDefaultMessageToErrors(result.getFieldErrors(), "interesado");

            jsonResponse.setStatus("FAIL");
            jsonResponse.setErrores(errores);
            
            
        } else { // Si no hay errores, guardamos la Persona

            jsonResponse.setStatus("SUCCESS");

            HttpSession session = request.getSession();

            //Entidad entidad = getEntidadActiva(request);

            try {

                // Procesamos los datos de un interesado
                interesado = procesarInteresado(interesado);

                // Creamos la respuesta
                PersonaJson personaJson = new PersonaJson();
                personaJson.setIsRepresentante(interesado.getIsRepresentante());

                // Se trata de un nuevo Registro, utilizamos la sesion.
                if(idRegistroDetalle.equals("null")) {

                    actualizarInteresadoSesion(interesado, session, variable);
                }else{ // Edición de un registro, lo añadimos a la bbdd

                    interesado.setRegistroDetalle(new RegistroDetalle(Long.valueOf(idRegistroDetalle)));
                    interesadoEjb.merge(interesado);
                }

                // Almacenamos el Id de la nueva persona/representante creado
                personaJson.setId(interesado.getId().toString());

                // Generamos el nombre a mostrar según el tipo de persona
                if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)){
                    personaJson.setNombre(interesado.getNombrePersonaFisica());
                    log.info("Nombre fisica asignado: " + personaJson.getNombre());
                }else if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)){
                    personaJson.setNombre(interesado.getNombrePersonaJuridica());
                    log.info("Nombre Juridica asignado: " + personaJson.getNombre());
                }

                jsonResponse.setResult(personaJson);


            } catch (Exception e) {
              // TODO NO!!!!!!!!!!!!!!!!!!!!
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
            log.info("Buscar Persona fisica Total: " + personas.size());
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
     * Añade un representante existente a la Sesion y lo relaciona con el representado
     * @param idRepresentante
     * @param idRepresentado
     * @param request
     * @return
     */
    @RequestMapping(value = "/{tipoRegistro}/addRepresentante", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse addRepresentante(@PathVariable String tipoRegistro,@RequestParam Long idRepresentante,@RequestParam Long idRepresentado,@RequestParam Long idRegistroDetalle, HttpServletRequest request) {

        String variable = (tipoRegistro.equals("entrada") ? RegwebConstantes.SESSION_INTERESADOS_ENTRADA:RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        log.info("Tipo Registro: " + tipoRegistro);
        log.info("Dentro de añadir representante: " + idRepresentante + " - " + idRepresentado);
        log.info("idRegistroDetalle: " + idRegistroDetalle);

        HttpSession session = request.getSession();
        JsonResponse jsonResponse = new JsonResponse();

        try {

            Interesado representante = new Interesado(personaEjb.findById(idRepresentante));
            representante.setIsRepresentante(true);
            log.info("Representante seleccionado: " + representante.getNombreCompleto());

            //Creamos la respuesta

            PersonaJson personaJson = new PersonaJson();
            personaJson.setId(representante.getId().toString());
            personaJson.setNombre(representante.getNombreCompleto());
            jsonResponse.setResult(personaJson);

            if(idRegistroDetalle == null){ // Sesion

                Interesado representado = obtenerInteresadoSesion(idRepresentado,session, variable);

                // Añadimos el representante a la sesion
                representante.setRepresentado(representado);
                añadirInteresadoSesion(representante, session, variable);

                //Actualizamos el representado
                representado.setRepresentante(representante);
                actualizarInteresadoSesion(representado, session, variable);


            }else{ // bbdd
                Interesado representado = interesadoEjb.findById(idRepresentado);
                // Creamos el Representante
                representante.setRegistroDetalle(new RegistroDetalle(idRegistroDetalle));
                representante.setRepresentado(representado);
                representante = interesadoEjb.persist(representante);

                // Actualizamos el representado
                representado.setRepresentante(representante);
                interesadoEjb.merge(representado);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }

    /**
     * Elimina un representante de la Sesion y lo desvincula de su representado.
     * @param idRepresentante
     * @param idRepresentado
     * @param request
     * @return
     */
    @RequestMapping(value = "/{tipoRegistro}/eliminarRepresentante", method = RequestMethod.GET)
    @ResponseBody
    public Boolean eliminarRepresentante(@PathVariable String tipoRegistro,@RequestParam Long idRepresentante,@RequestParam Long idRepresentado,@RequestParam String idRegistroDetalle, HttpServletRequest request) {

        String variable = (tipoRegistro.equals("entrada") ? RegwebConstantes.SESSION_INTERESADOS_ENTRADA:RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        log.info("Tipo Registro: " + tipoRegistro);
        log.info("Dentro de eliminar representante: " + idRepresentante);
        log.info("idRegistroDetalle: " + idRegistroDetalle);

        HttpSession session = request.getSession();

        try {

            if(idRegistroDetalle.equals("null")){ // Trabajamos en la sesión

                Interesado representante = new Interesado(idRepresentante);
                Interesado representado = obtenerInteresadoSesion(idRepresentado,session, variable);

                representado.setRepresentante(null);

                eliminarInteresadoSesion(representante, session, variable);
                actualizarInteresadoSesion(representado,session, variable);
                return true;

            }else{ //Trabajamos en la bbdd
                Interesado representado = interesadoEjb.findById(idRepresentado);
                representado.setRepresentante(null);
                interesadoEjb.merge(representado);
                interesadoEjb.eliminarInteresadoRegistroDetalle(idRepresentante, Long.valueOf(idRegistroDetalle));
                return true;

            }



        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Añade un Organismo existente a la variable de sesion que almacena los interesados.
     * @param codigoDir3
     * @param denominacion
     * @param request
     * @return
     */
    @RequestMapping(value = "/{tipoRegistro}/addOrganismo", method = RequestMethod.GET)
    @ResponseBody
    public Boolean addOrganismoInteresado(@PathVariable String tipoRegistro, @RequestParam String codigoDir3, @RequestParam String denominacion, @RequestParam String idRegistroDetalle, HttpServletRequest request) {

        String variable = (tipoRegistro.equals("entrada") ? RegwebConstantes.SESSION_INTERESADOS_ENTRADA:RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        log.info("Tipo Registro: " + tipoRegistro);
        HttpSession session = request.getSession();

        try {
            String converted = URLDecoder.decode(denominacion, "UTF-8");

            log.info("Dentro de organismoInteresado: " + codigoDir3 + " " + denominacion);
            log.info("RegistroDetalle: " + idRegistroDetalle);

            Interesado organismo = new Interesado(codigoDir3,converted);

            if(StringUtils.isEmpty(idRegistroDetalle)){ // Se trata de un nuevo Registro, utilizamos la sesion.

                añadirInteresadoSesion(organismo, session, variable);

            }else{ // Edición de un registro, lo añadimos a la bbdd
                organismo.setRegistroDetalle(new RegistroDetalle(Long.valueOf(idRegistroDetalle)));
                interesadoEjb.persist(organismo);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


    /**
     *  Añade una Persona existente a la variable de sesion que almacena los interesados que son de tipo Persona
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/{tipoRegistro}/addPersona", method = RequestMethod.GET)
    @ResponseBody
    public Integer addPersonaInteresado(@PathVariable String tipoRegistro,@RequestParam Long id,@RequestParam String idRegistroDetalle, HttpServletRequest request) {

        String variable = (tipoRegistro.equals("entrada") ? RegwebConstantes.SESSION_INTERESADOS_ENTRADA:RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        log.info("Tipo Registro: " + tipoRegistro);
        log.info("Dentro de personaInteresado: " + id);
        log.info("RegistroDetalle: " + idRegistroDetalle);

        HttpSession session = request.getSession();
        Interesado interesado = null;

        try {
            Persona persona = personaEjb.findById(id);

            if(persona != null) { // Si existe la persona en la bbdd
                interesado = new Interesado(persona);

                if(StringUtils.isEmpty(idRegistroDetalle)) { // Se trata de un nuevo Registro, utilizamos la sesion.

                    if(persona != null){ // Si existe la persona en la bbdd

                        añadirInteresadoSesion(interesado,session, variable);
                    }

                }else{ // Edición de un registro, lo añadimos a la bbdd
                    interesado.setRegistroDetalle(new RegistroDetalle(Long.valueOf(idRegistroDetalle)));
                    interesado = interesadoEjb.persist(interesado);
                    return interesado.getId().intValue();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Elimina una Persona de la variable de sesion que almacena los interesados
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/{tipoRegistro}/eliminarPersona", method = RequestMethod.GET)
    @ResponseBody
    public Boolean eliminarPersonaInteresado(@PathVariable String tipoRegistro,@RequestParam Long id,@RequestParam String idRegistroDetalle, HttpServletRequest request) {

        String variable = (tipoRegistro.equals("entrada") ? RegwebConstantes.SESSION_INTERESADOS_ENTRADA:RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        log.info("Tipo Registro: " + tipoRegistro);
        log.info("Dentro de eliminar personaInteresado: " + id);
        log.info("RegistroDetalle: " + idRegistroDetalle);

        HttpSession session = request.getSession();

        try {

            if(idRegistroDetalle.equals("null") || StringUtils.isEmpty(idRegistroDetalle)) { // Se trata de un nuevo Registro, utilizamos la sesion.

                Interesado persona = obtenerInteresadoSesion(id, session, variable);

                // Si tiene Representate, también lo eliminamos.
                if(persona.getRepresentante() != null){
                    log.info("Tiene representante y lo eliminamos");
                    Interesado representante = obtenerInteresadoSesion(persona.getRepresentante().getId(), session, variable);
                    eliminarInteresadoSesion(representante,session, variable);
                }

                // Eliminamos la Persona
                return eliminarInteresadoSesion(persona,session, variable);


            }else{// Edición de un registro, lo eliminanos de la bbdd
                RegistroDetalle registroDetalle = registroDetalleEjb.findById(Long.valueOf(idRegistroDetalle));
                if(registroDetalle != null && registroDetalle.getInteresados().size()>1 ) { // Si solo hay un Interesado, no permitimos eliminarlo.
                    interesadoEjb.eliminarInteresadoRegistroDetalle(id,Long.valueOf(idRegistroDetalle));
                    return true;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Elimina un Organismo de la variable de sesion que almacena los interesados
     * @param codigoDir3
     * @param request
     * @return
     */
    @RequestMapping(value = "/{tipoRegistro}/eliminarOrganismo", method = RequestMethod.GET)
    @ResponseBody
    public Boolean eliminarOrganismoInteresado(@PathVariable String tipoRegistro, @RequestParam String codigoDir3,@RequestParam String idRegistroDetalle, HttpServletRequest request) {

        String variable = (tipoRegistro.equals("entrada") ? RegwebConstantes.SESSION_INTERESADOS_ENTRADA:RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        log.info("Tipo Registro: " + tipoRegistro);
        log.info("Dentro de eliminarOrganismo: " + codigoDir3);
        log.info("RegistroDetalle: " + idRegistroDetalle);

        HttpSession session = request.getSession();

        try {

            if(StringUtils.isEmpty(idRegistroDetalle)) { // Se trata de un nuevo Registro, lo eliminamos de la sesion

               return eliminarOrganimoSesion(codigoDir3, session, variable);

            }else{// Edición de un registro, lo eliminanos de la bbdd
                RegistroDetalle registroDetalle = registroDetalleEjb.findById(Long.valueOf(idRegistroDetalle));

                if(registroDetalle != null && registroDetalle.getInteresados().size()>1 ){ // Si solo hay un Interesado, no permitimos eliminarlo.
                    Interesado interesado = interesadoEjb.findByCodigoDir3RegistroDetalle(codigoDir3, Long.valueOf(idRegistroDetalle));
                    if(interesado != null){
                        interesadoEjb.eliminarInteresadoRegistroDetalle(interesado.getId(),Long.valueOf(idRegistroDetalle));
                        return true;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }



    /**
     * Busca una Interesado en la variable de Interesados almacenada en la Sesion
     * @param idInteresado del Interesado a buscar
     * @param session donde buscar
     * @return
     */
    public Interesado obtenerInteresadoSesion(Long idInteresado, HttpSession session, String variable){
        log.info("Buscando en: " + variable);
        List<Interesado> interesados = (List<Interesado>) session.getAttribute(variable);

        Interesado interesado = new Interesado(idInteresado);

        if(interesados.contains(interesado)){
            int posicion = interesados.indexOf(interesado);

            if (posicion != -1){
                return interesados.get(posicion);
            }else{
                return  null;
            }

        }else{
            return null;
        }
    }

    /**
     * Elimina una Persona de la sesion
     * @param interesado
     * @param session
     */
    public Boolean eliminarInteresadoSesion(Interesado interesado, HttpSession session, String variable)throws Exception{

        List<Interesado> interesados = (List<Interesado>) session.getAttribute(variable);

        if(interesados.contains(interesado)){
            log.info("Eliminado!");
            interesados.remove(interesado);
            session.setAttribute(variable, interesados);
            return true;
        }
        return false;

    }

    /**
     * Elimina un Organismo del listado de Interesados de la sesion
     * @param codigoDir3
     * @param session
     * @throws Exception
     */
    public Boolean eliminarOrganimoSesion(String codigoDir3, HttpSession session, String variable) throws Exception{

        List<Interesado> interesados = (List<Interesado>) session.getAttribute(variable);

        if(interesados != null){

            for(Interesado interesado:interesados){
                if(!StringUtils.isEmpty(interesado.getCodigoDir3()) && interesado.getCodigoDir3().equals(codigoDir3)){
                    interesados.remove(interesado);
                    session.setAttribute(variable, interesados);
                    log.info("Organismo eliminado");
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Actualiza un Interesado por otro en la sesion
     * @param interesado
     * @param session
     */
    public void actualizarInteresadoSesion(Interesado interesado, HttpSession session, String variable){

        List<Interesado> interesados = (List<Interesado>) session.getAttribute(variable);

        if(interesados.contains(interesado)){
            log.info("Actualizamos el interesado en la sesion");
            interesados.remove(interesado);

            interesados.add(interesado);
            session.setAttribute(variable, interesados);
        }

    }

    /**
     * Añade un Interesado a la sesion
     * @param interesado
     * @param session
     */
    public void añadirInteresadoSesion(Interesado interesado, HttpSession session, String variable){

        List<Interesado> interesados = (List<Interesado>) session.getAttribute(variable);

        if(interesados != null){
            interesados.add(interesado);
        }else{
            interesados = new ArrayList<Interesado>();
            interesados.add(interesado);
        }

        session.setAttribute(variable, interesados);
    }



    /**
     * Obtiene la {@link es.caib.regweb3.model.Interesado} según su identificador.
     * Si no la encuentra la busca en la Sesion.
     */
    @RequestMapping(value = "/{tipoRegistro}/obtenerInteresado", method = RequestMethod.GET)
    @ResponseBody
    public Interesado obtenerInteresado(@PathVariable String tipoRegistro,@RequestParam Long id, HttpServletRequest request) throws Exception {

        String variable = (tipoRegistro.equals("entrada") ? RegwebConstantes.SESSION_INTERESADOS_ENTRADA:RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        log.info("Tipo Registro: " + tipoRegistro);
        log.info("ObtenerInteresado: " + id);

        Interesado interesado = interesadoEjb.findById(id);

        if(interesado == null){
            log.info("Está en la sesion");
            HttpSession session = request.getSession();
            interesado = obtenerInteresadoSesion(id, session, variable);
        }

        return  interesado;
    }

    /**
     * Procesa un interesado
     * @param interesado
     * @return
     * @throws Exception
     */
    private Interesado procesarInteresado(Interesado interesado) throws Exception{

        // Si no se ha escogido ningún TipoDocumento, lo ponemos a null
        if(interesado.getTipoDocumentoIdentificacion() != null 
            && interesado.getTipoDocumentoIdentificacion() == -1)    { 
          interesado.setTipoDocumentoIdentificacion(null);
        }

        // Si no se ha escogido ningún Canal de Notificación, lo ponemos a null
        if(interesado.getCanal() != null && interesado.getCanal() == -1){interesado.setCanal(null);}

        // Si no se ha escogido ningúna Provincia, lo ponemos a null
        if(interesado.getPais().getId() != null && interesado.getPais().getId() == -1){interesado.setPais(null);}

        // Si no se ha escogido ningúna Provincia, lo ponemos a null
        if(interesado.getProvincia().getId() != null && interesado.getProvincia().getId() == -1){
            interesado.setProvincia(null);
            interesado.setLocalidad(null);
        }

        // Si tiene documento ponemos las letras en mayúsculas
        if(interesado.getDocumento() != null){
            interesado.setDocumento(interesado.getDocumento().toUpperCase());
        }

        return interesado;
    }

    /**
     * Procesa un interesado
     * @param persona
     * @return
     * @throws Exception
     */
    public Persona procesarPersona(Persona persona) throws Exception{

        // Si no se ha escogido ningún TipoDocumento, lo ponemos a null
        if(persona.getTipoDocumentoIdentificacion()!= null && persona.getTipoDocumentoIdentificacion() == -1) { 
          persona.setTipoDocumentoIdentificacion(null);
        }

        // Si no se ha escogido ningún Canal de Notificación, lo ponemos a null
        if(persona.getCanal() != null && persona.getCanal() == -1){persona.setCanal(null);}

        // Si no se ha escogido ningúna Provincia, lo ponemos a null
        if(persona.getPais().getId() != null && persona.getPais().getId() == -1){persona.setPais(null);}

        // Si no se ha escogido ningúna Provincia, lo ponemos a null
        if(persona.getProvincia().getId() != null && persona.getProvincia().getId() == -1){
            persona.setProvincia(null);
            persona.setLocalidad(null);
        }

        return persona;
    }

}
