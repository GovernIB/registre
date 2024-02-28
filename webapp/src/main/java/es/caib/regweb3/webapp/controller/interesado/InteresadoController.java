package es.caib.regweb3.webapp.controller.interesado;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.Persona;
import es.caib.regweb3.model.RegistroDetalle;
import es.caib.regweb3.persistence.ejb.InteresadoLocal;
import es.caib.regweb3.persistence.ejb.PersonaLocal;
import es.caib.regweb3.persistence.ejb.RegistroDetalleLocal;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.JsonResponse;
import es.caib.regweb3.webapp.utils.PersonaJson;
import es.caib.regweb3.webapp.validator.InteresadoWebValidator;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
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

import static es.caib.regweb3.utils.RegwebConstantes.REGISTRO_ENTRADA;
import static es.caib.regweb3.utils.RegwebConstantes.REGISTRO_SALIDA;

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

    @EJB(mappedName = PersonaLocal.JNDI_NAME)
    private PersonaLocal personaEjb;

    @EJB(mappedName = RegistroDetalleLocal.JNDI_NAME)
    private RegistroDetalleLocal registroDetalleEjb;

    @EJB(mappedName = InteresadoLocal.JNDI_NAME)
    private InteresadoLocal interesadoEjb;


    /**
     * Añade un Organismo existente a la variable de sesion que almacena los interesados.
     * @param codigoDir3
     * @param denominacion
     * @param request
     * @return
     */
    @RequestMapping(value = "/{tipoRegistro}/addOrganismo", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse addOrganismoInteresado(@PathVariable Long tipoRegistro, @RequestParam String codigoDir3, @RequestParam String denominacion, @RequestParam String idRegistroDetalle, HttpServletRequest request) {

        JsonResponse jsonResponse = new JsonResponse();

        String variableSesion = (tipoRegistro.equals(REGISTRO_ENTRADA) ? RegwebConstantes.SESSION_INTERESADOS_ENTRADA:RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        HttpSession session = request.getSession();
        Entidad entidadActiva = getEntidadActiva(request);

        try {

            if(Configuracio.isCAIB()){ // si la instalación es CAIB, no permitimos realizar registros con interesado administración de la propia entidad

                if(organismoEjb.isOrganismoInterno(codigoDir3, entidadActiva.getId())){ // Comprobamos si el Organismo indicado pruede ser Interesado o Destinatario

                    jsonResponse.setStatus("FAIL");

                    if(tipoRegistro.equals(REGISTRO_ENTRADA)){
                        jsonResponse.setError(getMessage("interesado.organismo.interno"));
                    }else if(tipoRegistro.equals(REGISTRO_SALIDA)){
                        jsonResponse.setError(getMessage("destinatario.organismo.interno"));
                    }

                    return jsonResponse;
                }
            }

            denominacion = URLDecoder.decode(denominacion, "UTF-8");

            Interesado organismo = new Interesado(codigoDir3,denominacion);

            if(StringUtils.isEmpty(idRegistroDetalle)){ // Se trata de un nuevo Registro, utilizamos la sesion.

                // Si es una Salida solo puede haber un Interesado Administración
                if(tipoRegistro.equals(REGISTRO_SALIDA)){

                    String organismoCodigo = hayOrganismoInteresado(session);
                    if(StringUtils.isNotEmpty(organismoCodigo)){
                        eliminarOrganismoSesion(organismoCodigo, session, variableSesion);
                    }
                }

                addInteresadoSesion(organismo, session, variableSesion);

            }else{ // Edición de un registro, lo añadimos a la bbdd

                // Si es una Salida solo puede haber un Interesado Administración
                if(tipoRegistro.equals(REGISTRO_SALIDA)){

                    String organismoCodigo = interesadoEjb.existeInteresadoAdministracion(Long.valueOf(idRegistroDetalle));
                    if(StringUtils.isNotEmpty(organismoCodigo)){
                        eliminarOrganismoBbdd(organismoCodigo, Long.valueOf(idRegistroDetalle),tipoRegistro, entidadActiva.getId());
                    }
                }

                organismo.setRegistroDetalle(registroDetalleEjb.findById(Long.valueOf(idRegistroDetalle)));
                interesadoEjb.persist(organismo);

                // Plug-in postproceso
                interesadoEjb.postProcesoNuevoInteresado(organismo, Long.valueOf(idRegistroDetalle),tipoRegistro, entidadActiva.getId());
            }

        } catch(I18NException i18ne) {
            log.error(I18NUtils.getMessage(i18ne), i18ne);
            jsonResponse.setStatus("FAIL");
            jsonResponse.setError(getMessage("regweb.error.general"));
            return jsonResponse;
        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.setStatus("FAIL");
            jsonResponse.setError(getMessage("regweb.error.general"));
            return jsonResponse;
        }

        jsonResponse.setStatus("SUCCESS");
        return jsonResponse;
    }


    /**
     *  Añade una Persona existente a la variable de sesion que almacena los interesados que son de tipo Persona
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/{tipoRegistro}/addPersona", method = RequestMethod.GET)
    @ResponseBody
    public Integer addPersonaInteresado(@PathVariable Long tipoRegistro,@RequestParam Long id,@RequestParam String idRegistroDetalle, HttpServletRequest request) {

        String variableSesion = (tipoRegistro.equals(REGISTRO_ENTRADA) ? RegwebConstantes.SESSION_INTERESADOS_ENTRADA:RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        HttpSession session = request.getSession();
        Interesado interesado;

        try {
            Persona persona = personaEjb.findById(id);

            if(persona != null) { // Si existe la persona en la bbdd
                interesado = new Interesado(persona);

                if(StringUtils.isEmpty(idRegistroDetalle)) { // Se trata de un nuevo Registro, utilizamos la sesion.

                    addInteresadoSesion(interesado,session, variableSesion);

                }else{ // Edición de un registro, lo añadimos a la bbdd
                    interesado.setId(null);
                    interesado.setRegistroDetalle(registroDetalleEjb.getReference(Long.valueOf(idRegistroDetalle)));
                    interesado = interesadoEjb.guardarInteresado(interesado);
                    Entidad entidadActiva = getEntidadActiva(request);

                    // Plug-in Post-Proceso
                    interesadoEjb.postProcesoNuevoInteresado(interesado, Long.valueOf(idRegistroDetalle), tipoRegistro,entidadActiva.getId());
                    return interesado.getId().intValue();
                }
            }

        } catch(I18NException i18ne) {
          log.error(I18NUtils.getMessage(i18ne), i18ne);
          return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return 0;
    }

    /**
     * Crea un Interesado y la añade a la variable de sesion que almacena los interesados que son de tipo Persona
     * @param interesado
     * @param request
     * @param result
     * @return
     */
    @RequestMapping(value="/gestionar/{tipoRegistro}/nuevo/{idRegistroDetalle}", method= RequestMethod.POST)
    @ResponseBody
    public JsonResponse nuevoInteresado(@PathVariable Long tipoRegistro,@PathVariable String idRegistroDetalle, @RequestBody Interesado interesado, HttpServletRequest request, BindingResult result) {

        JsonResponse jsonResponse = new JsonResponse();

        String variableSesion = (tipoRegistro.equals(REGISTRO_ENTRADA) ? RegwebConstantes.SESSION_INTERESADOS_ENTRADA:RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        Boolean isRepresentante = interesado.getIsRepresentante();
        String idRepresentado = null;

        // Comprobamos si es se trata de un representante
        if (isRepresentante) {
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
                //Marcamos por defecto como receptor de notificicaciones si es el primer interesado.
                List<Interesado> interesadosSesion = (List<Interesado>) session.getAttribute(variableSesion);
                if (interesadosSesion == null) {
                    interesado.setReceptorNotificaciones(true);
                }

                // Procesamos los datos de un interesado
                interesado = procesarInteresado(interesado);

                // Creamos la respuesta especifica
                PersonaJson personaJson = new PersonaJson();
                personaJson.setIsRepresentante(isRepresentante);

                // Según la configuración de la Entidad, guardamos o no la Persona.
                // Asignamos la Entidad a la que pertenece esta persona
                Persona persona = new Persona(interesado);
                persona.setEntidad(entidad);

                switch (entidad.getConfiguracionPersona().intValue()){

                    case (int)RegwebConstantes.CONFIGURACION_PERSONA_SIN_GUARDAR: // No se guardan
                        persona.setId((long)(Math.random()*10000));
                        interesado.setId(persona.getId());
                    break;

                    case (int)RegwebConstantes.CONFIGURACION_PERSONA_GUARDAR_TODOS: // Se guardan las Personas

                        if(StringUtils.isEmpty(persona.getDocumento())){ // Si no hay documento, no se guarda en la bbdd
                            persona.setId((long)(Math.random()*10000));
                        }else{
                            persona = personaEjb.guardarPersona(persona);
                        }

                        interesado.setId(persona.getId());
                    break;

                }

                // Se trata de un nuevo Registro, utilizamos la sesion.
                if(idRegistroDetalle.equals("null")) {

                    // Gestionamos en caso de que sea Representante
                    if(isRepresentante){ // Si se trata de un representante lo indicamos

                        personaJson.setRepresentado(new PersonaJson(idRepresentado));

                        // Asociamos el representante a la Persona representada
                        Interesado representado = obtenerInteresadoSesion(Long.valueOf(idRepresentado), session, variableSesion);
                        interesado.setRepresentado(representado);
                        representado.setRepresentante(interesado);

                        addRepresentanteSesion(representado, interesado, session, variableSesion);

                    } else {
                        addInteresadoSesion(interesado,session, variableSesion);
                    }

                }else{ // Edición de un registro, lo añadimos a la bbdd
                    Entidad entidadActiva = getEntidadActiva(request);

                    if(isRepresentante) { // Si se trata de un representante lo indicamos
                        interesado.setId(null);
                        interesado.setRegistroDetalle(registroDetalleEjb.getReference(Long.valueOf(idRegistroDetalle)));
                        interesado.setRepresentado(interesadoEjb.getReference(Long.valueOf(idRepresentado)));
                        personaJson.setRepresentado(new PersonaJson(idRepresentado));

                        // Guardamos el Nuevo representante
                        interesado = interesadoEjb.guardarInteresado(interesado);
                        // Plugin Post-Proceso
                        interesadoEjb.postProcesoNuevoInteresado(interesado, Long.valueOf(idRegistroDetalle), tipoRegistro,entidadActiva.getId());

                        // Lo asociamos al represenatado
                        Interesado representado = interesadoEjb.findById(Long.valueOf(idRepresentado));
                        representado.setRepresentante(interesado);
                        interesadoEjb.merge(representado);
                        // Plugin Post-Proceso
                        interesadoEjb.postProcesoActualizarInteresado(representado, Long.valueOf(idRegistroDetalle), tipoRegistro,entidadActiva.getId());
                    }else{
                        interesado.setId(null);
                        interesado.setRegistroDetalle(registroDetalleEjb.getReference(Long.valueOf(idRegistroDetalle)));
                        interesado = interesadoEjb.guardarInteresado(interesado);
                        // Plugin Post-Proceso
                        interesadoEjb.postProcesoNuevoInteresado(interesado, Long.valueOf(idRegistroDetalle), tipoRegistro,entidadActiva.getId());
                    }

                }

                // Almacenamos el Id de la nueva persona/representante creado
                personaJson.setId(interesado.getId().toString());

                // Generamos el nombre a mostrar según el tipo de persona
                personaJson.setNombre(interesado.getNombreCompletoInforme());

                jsonResponse.setResult(personaJson);

            } catch(I18NException i18ne) {
              log.error(I18NUtils.getMessage(i18ne), i18ne);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return jsonResponse;
    }


    /**
     * Crea o modifica una Interesado y la añade a la variable de sesion que almacena los interesados que son de tipo Persona
     *
     * @param interesado
     * @param request
     * @param result
     * @return
     */
    @RequestMapping(value = "/gestionar/{tipoRegistro}/editar/{idRegistroDetalle}", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse editarInteresado(@PathVariable Long tipoRegistro, @PathVariable String idRegistroDetalle,
                                         @RequestBody Interesado interesado, HttpServletRequest request, BindingResult result) throws I18NException {

        String variableSesion = (tipoRegistro.equals(REGISTRO_ENTRADA) ? RegwebConstantes.SESSION_INTERESADOS_ENTRADA : RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        Boolean isRepresentante = interesado.getIsRepresentante();
        JsonResponse jsonResponse = new JsonResponse();
        Entidad entidadActiva = getEntidadActiva(request);

        // Validamos el interesado
        interesado.setEntidad(entidadActiva.getId());
        interesadoValidator.validate(interesado, result);

        //Validamos que haya al menos un interesado como receptor de Notificaciones (obligatorio en Sicres 4)
        if(!idRegistroDetalle.equals("null")) {
            RegistroDetalle registroDetalle = registroDetalleEjb.findByIdConInteresados(Long.valueOf(idRegistroDetalle));
            boolean tieneReceptor = false;
            if (registroDetalle != null && registroDetalle.getInteresados().size() > 0) {
                for (Interesado interesado1 : registroDetalle.getInteresados()) {
                    if (interesado1.getReceptorNotificaciones()) {
                        tieneReceptor = true;
                        break;
                    }
                }
                //Si no hay ninguno marcado como receptor, marcamos el primero por defecto.
                if (!tieneReceptor) {
                    registroDetalle.getInteresados().get(0).setReceptorNotificaciones(true);
                }
            }
        }

        if (result.hasErrors()) { // Si hay errores, preparamos la respuesta.

            List<FieldError> errores = setDefaultMessageToErrors(result.getFieldErrors(), "interesado");

            jsonResponse.setStatus("FAIL");
            jsonResponse.setErrores(errores);


        } else { // Si no hay errores, actualizamos el Interesado

            jsonResponse.setStatus("SUCCESS");

            HttpSession session = request.getSession();

            try {

                // Procesamos los datos de un interesado
                interesado = procesarInteresado(interesado);

                // Creamos la respuesta
                PersonaJson personaJson = new PersonaJson();
                personaJson.setIsRepresentante(isRepresentante);

                // Se trata de un nuevo Registro, utilizamos la sesion.
                if(idRegistroDetalle.equals("null")) {

                    // Comprobamos si es se trata de un representante
                    if (isRepresentante) {
                        interesado.setRepresentado(obtenerInteresadoSesion(interesado.getRepresentado().getId(), session, variableSesion));
                    }

                    actualizarInteresadoSesion(interesado, session, variableSesion);
                }else{ // Edición de un registro, lo actualizamos en la bbdd
                    interesado.setRegistroDetalle(registroDetalleEjb.getReference(Long.valueOf(idRegistroDetalle)));
                    interesado = interesadoEjb.merge(interesado);

                    // Plug-in de Post-Proceso
                    interesadoEjb.postProcesoActualizarInteresado(interesado, Long.valueOf(idRegistroDetalle),tipoRegistro,entidadActiva.getId());
                }

                // Almacenamos el Id de la nueva persona/representante creado
                personaJson.setId(interesado.getId().toString());

                // Generamos el nombre a mostrar según el tipo de persona
                personaJson.setNombre(interesado.getNombreCompletoInforme());

                // Actualizamos los datos modificados en la bbdd de Personas
                personaEjb.actualizarPersona(interesado, entidadActiva.getId());

                jsonResponse.setResult(personaJson);

            } catch(I18NException i18ne) {
              log.error(I18NUtils.getMessage(i18ne), i18ne);
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

        if(persona.getTipo().equals(RegwebConstantes.TIPO_PERSONA_FISICA)){ // Personas Físicas
            personas = personaEjb.busquedaFisicas(entidad.getId(), persona.getNombre(), persona.getApellido1(), persona.getApellido2(), persona.getDocumento(), persona.getTipo());

        }else if(persona.getTipo().equals(RegwebConstantes.TIPO_PERSONA_JURIDICA)){ // Personas Jurídicas
            personas = personaEjb.busquedaJuridicas(entidad.getId(), persona.getRazonSocial(), persona.getDocumento(), persona.getTipo());

        }else if(persona.getTipo().equals(0L)) { // Todas las Personas

            if(StringUtils.isNotEmpty(persona.getNombre()) || StringUtils.isNotEmpty(persona.getApellido1()) || StringUtils.isNotEmpty(persona.getApellido2()) || StringUtils.isNotEmpty(persona.getDocumento())){
                personas.addAll(personaEjb.busquedaFisicas(entidad.getId(), persona.getNombre(), persona.getApellido1(), persona.getApellido2(), persona.getDocumento(), RegwebConstantes.TIPO_PERSONA_FISICA));
            }

            if(StringUtils.isNotEmpty(persona.getRazonSocial()) || StringUtils.isNotEmpty(persona.getDocumento()) ){
                personas.addAll(personaEjb.busquedaJuridicas(entidad.getId(), persona.getRazonSocial(), persona.getDocumento(), RegwebConstantes.TIPO_PERSONA_JURIDICA));
            }

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
    public JsonResponse addRepresentante(@PathVariable Long tipoRegistro,@RequestParam Long idRepresentante,@RequestParam Long idRepresentado,@RequestParam Long idRegistroDetalle, HttpServletRequest request) {

        String variableSesion = (tipoRegistro.equals(REGISTRO_ENTRADA) ? RegwebConstantes.SESSION_INTERESADOS_ENTRADA:RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        HttpSession session = request.getSession();
        JsonResponse jsonResponse = new JsonResponse();

        try {

            // Creamos el representante a partir de la Persona seleccionada
            Interesado representante = new Interesado(personaEjb.findById(idRepresentante));
            representante.setIsRepresentante(true);

            if(idRegistroDetalle == null){ // Sesion

                Interesado representado = obtenerInteresadoSesion(idRepresentado,session, variableSesion);

                // Añadimos el representante a la sesion
                representante.setRepresentado(representado);
                addInteresadoSesion(representante, session, variableSesion);

                //Actualizamos el representado
                representado.setRepresentante(representante);
                actualizarInteresadoSesion(representado, session, variableSesion);


            }else{ // bbdd
                Interesado representado = interesadoEjb.findById(idRepresentado);
                // Creamos el Representante
                representante.setId(null);
                representante.setRegistroDetalle(registroDetalleEjb.getReference(idRegistroDetalle));
                representante.setRepresentado(representado);
                representante = interesadoEjb.guardarInteresado(representante);
                Entidad entidadActiva= getEntidadActiva(request);

                // Plug-in de Post-Proceso
                interesadoEjb.postProcesoNuevoInteresado(representante, idRegistroDetalle, tipoRegistro, entidadActiva.getId());

                // Actualizamos el representado
                representado.setRepresentante(representante);
                interesadoEjb.merge(representado);

                // Plug-in de Post-Proceso
                interesadoEjb.postProcesoActualizarInteresado(representado, idRegistroDetalle, tipoRegistro,entidadActiva.getId());
            }

            //Creamos la respuesta
            PersonaJson personaJson = new PersonaJson();
            personaJson.setId(representante.getId().toString());
            personaJson.setNombre(representante.getNombreCompleto());
            jsonResponse.setResult(personaJson);

        } catch(I18NException i18ne) {
          log.error(I18NUtils.getMessage(i18ne), i18ne);
          return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
    public Boolean eliminarRepresentante(@PathVariable Long tipoRegistro,@RequestParam Long idRepresentante,@RequestParam Long idRepresentado,@RequestParam String idRegistroDetalle, HttpServletRequest request) {

        String variableSesion = (tipoRegistro.equals(REGISTRO_ENTRADA) ? RegwebConstantes.SESSION_INTERESADOS_ENTRADA:RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        HttpSession session = request.getSession();

        try {

            if(idRegistroDetalle.equals("null")){ // Trabajamos en la sesión

                Interesado representante = new Interesado(idRepresentante);
                Interesado representado = obtenerInteresadoSesion(idRepresentado,session, variableSesion);

                representado.setRepresentante(null);

                eliminarInteresadoSesion(representante, session, variableSesion);
                actualizarInteresadoSesion(representado,session, variableSesion);
                return true;

            }else{ //Trabajamos en la bbdd

                Interesado representado = interesadoEjb.findById(idRepresentado);
                representado.setRepresentante(null);
                interesadoEjb.merge(representado);
                Entidad entidadActiva= getEntidadActiva(request);
                // Plug-in de Post-Proceso
                interesadoEjb.postProcesoActualizarInteresado(representado, Long.valueOf(idRegistroDetalle), tipoRegistro,entidadActiva.getId());
                interesadoEjb.eliminarInteresadoRegistroDetalle(idRepresentante, Long.valueOf(idRegistroDetalle));
                interesadoEjb.postProcesoEliminarInteresado(idRepresentante, Long.valueOf(idRegistroDetalle),tipoRegistro,getEntidadActiva(request).getId());
                return true;
            }
        } catch(I18NException i18ne) {
          log.error(I18NUtils.getMessage(i18ne), i18ne);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Elimina una Persona de la variable de sesion que almacena los interesados
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/{tipoRegistro}/eliminarPersona", method = RequestMethod.GET)
    @ResponseBody
    public Boolean eliminarPersonaInteresado(@PathVariable Long tipoRegistro,@RequestParam Long id,@RequestParam String idRegistroDetalle, HttpServletRequest request) {

        String variableSesion = (tipoRegistro.equals(REGISTRO_ENTRADA) ? RegwebConstantes.SESSION_INTERESADOS_ENTRADA:RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        HttpSession session = request.getSession();

        try {

            // Se trata de un nuevo Registro, utilizamos la sesion.
            if(idRegistroDetalle.equals("null") || StringUtils.isEmpty(idRegistroDetalle)) {

                Interesado persona = obtenerInteresadoSesion(id, session, variableSesion);

                // Si tiene Representate, también lo eliminamos.
                if(persona.getRepresentante() != null){
                    Interesado representante = obtenerInteresadoSesion(persona.getRepresentante().getId(), session, variableSesion);
                    eliminarInteresadoSesion(representante,session, variableSesion);
                }

                // Eliminamos la Persona
                return eliminarInteresadoSesion(persona,session, variableSesion);


            }else{// Edición de un registro, lo eliminanos de la bbdd
                RegistroDetalle registroDetalle = registroDetalleEjb.findByIdConInteresados(Long.valueOf(idRegistroDetalle));
                if(registroDetalle != null && registroDetalle.getInteresados().size()>1 ) { // Si solo hay un Interesado, no permitimos eliminarlo.
                    Boolean hayNotificaciones = false;
                    for(Interesado inter: registroDetalle.getInteresados()) {
                        if (inter.getReceptorNotificaciones() && !inter.getId().equals(id)) {
                            hayNotificaciones = true;
                            break;
                        }
                    }
                    if(hayNotificaciones) {
                        interesadoEjb.eliminarInteresadoRegistroDetalle(id, Long.valueOf(idRegistroDetalle));

                        // Plug-in de Post-Proceso
                        interesadoEjb.postProcesoEliminarInteresado(id, Long.valueOf(idRegistroDetalle), tipoRegistro, getEntidadActiva(request).getId());
                        return true;
                    }else{
                        return false;
                    }

                }

            }
        } catch(I18NException i18ne) {
          log.error(I18NUtils.getMessage(i18ne), i18ne);
          return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
    public Boolean eliminarOrganismoInteresado(@PathVariable Long tipoRegistro, @RequestParam String codigoDir3,@RequestParam String idRegistroDetalle, HttpServletRequest request) {

        String variableSesion = (tipoRegistro.equals(REGISTRO_ENTRADA) ? RegwebConstantes.SESSION_INTERESADOS_ENTRADA:RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        HttpSession session = request.getSession();

        try {

            if(StringUtils.isEmpty(idRegistroDetalle)) { // Se trata de un nuevo Registro, lo eliminamos de la sesion

               return eliminarOrganismoSesion(codigoDir3, session, variableSesion);

            }else{// Edición de un registro, lo eliminanos de la bbdd

                RegistroDetalle registroDetalle = registroDetalleEjb.findByIdConInteresados(Long.valueOf(idRegistroDetalle));

                if(registroDetalle != null && registroDetalle.getInteresados().size()>1 ){ // Si solo hay un Interesado, no permitimos eliminarlo.

                    return eliminarOrganismoBbdd(codigoDir3,Long.valueOf(idRegistroDetalle),tipoRegistro, getEntidadActiva(request).getId());

                }
            }
        } catch(I18NException i18ne) {
          log.error(I18NUtils.getMessage(i18ne), i18ne);
          return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Elimina todos los Organismos de la variable de sesion que almacena los interesados
     * @param request
     * @return
     */
    @RequestMapping(value = "/{tipoRegistro}/eliminarInteresados", method = RequestMethod.GET)
    @ResponseBody
    public Boolean eliminarInteresados(@PathVariable Long tipoRegistro, @RequestParam String idRegistroDetalle, HttpServletRequest request) {

        String variableSesion = (tipoRegistro.equals(REGISTRO_ENTRADA) ? RegwebConstantes.SESSION_INTERESADOS_ENTRADA:RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        HttpSession session = request.getSession();

        try {
            if(StringUtils.isEmpty(idRegistroDetalle)) { // Se trata de un nuevo Registro, lo eliminamos de la sesion
                return eliminarInteresadosSesion(session, variableSesion);
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
    @SuppressWarnings("unchecked")
    private Interesado obtenerInteresadoSesion(Long idInteresado, HttpSession session, String variableSesion){

        List<Interesado> interesados = (List<Interesado>) session.getAttribute(variableSesion);

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
    @SuppressWarnings("unchecked")
    private Boolean eliminarInteresadoSesion(Interesado interesado, HttpSession session, String variableSesion)throws Exception{

        List<Interesado> interesados = (List<Interesado>) session.getAttribute(variableSesion);

        if(interesados.contains(interesado)){
            interesados.remove(interesado);
            session.setAttribute(variableSesion, interesados);
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
    @SuppressWarnings("unchecked")
    private Boolean eliminarOrganismoSesion(String codigoDir3, HttpSession session, String variableSesion) throws Exception{

        List<Interesado> interesados = (List<Interesado>) session.getAttribute(variableSesion);

        if(interesados != null){
            for(Interesado interesado:interesados){
                if(StringUtils.isNotEmpty(interesado.getCodigoDir3()) && interesado.getCodigoDir3().equals(codigoDir3)){
                    interesados.remove(interesado);
                    session.setAttribute(variableSesion, interesados);

                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Elimina todos los Organismos del listado de Interesados de la sesion
     * @param session
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private Boolean eliminarInteresadosSesion(HttpSession session, String variableSesion) throws Exception{

        List<Interesado> interesados = (List<Interesado>) session.getAttribute(variableSesion);

        if(interesados!=null){
            session.setAttribute(variableSesion, null);
        }

        return true;
    }


    /**
     * Elimina un Organismo del listado de Interesados de la bbdd
     * @param codigoDir3
     * @param idRegistroDetalle
     * @param tipoRegistro
     * @throws Exception
     */
    private Boolean eliminarOrganismoBbdd(String codigoDir3, Long idRegistroDetalle,
        Long tipoRegistro, Long idEntidad) throws Exception, I18NException{

        Interesado interesado = interesadoEjb.findByCodigoDir3RegistroDetalle(codigoDir3, idRegistroDetalle);
        if(interesado != null){
            interesadoEjb.eliminarInteresadoRegistroDetalle(interesado.getId(),idRegistroDetalle);

            // Plug-in de Post-Proceso
            interesadoEjb.postProcesoEliminarInteresado(interesado.getId(), idRegistroDetalle, tipoRegistro, idEntidad);
            return true;
        }

        return false;
    }


    /**
     * Actualiza un Interesado por otro en la sesion
     * @param interesado
     * @param session
     */
    @SuppressWarnings("unchecked")
    private void actualizarInteresadoSesion(Interesado interesado, HttpSession session, String variableSesion){

        List<Interesado> interesados = (List<Interesado>) session.getAttribute(variableSesion);

        if(interesados.contains(interesado)){
            interesados.remove(interesado);

            interesados.add(interesado);
            session.setAttribute(variableSesion, interesados);
        }

    }

    /**
     * Añade un Interesado a la sesion
     * @param interesado
     * @param session
     */
    @SuppressWarnings("unchecked")
    private void addInteresadoSesion(Interesado interesado, HttpSession session, String variableSesion){

        List<Interesado> interesados = (List<Interesado>) session.getAttribute(variableSesion);

        if(interesados != null){
            interesados.add(interesado);
        }else{
            interesados = new ArrayList<Interesado>();
            interesados.add(interesado);
        }

        session.setAttribute(variableSesion, interesados);
    }

    /**
     * Añade el nuevo Representante a la sesión y lo relaciona con su Representado
     *
     * @param representado
     * @param interesado
     * @param session
     * @param variableSesion
     */
    @SuppressWarnings("unchecked")
    private void addRepresentanteSesion(Interesado representado, Interesado interesado, HttpSession session, String variableSesion) {
        List<Interesado> interesados = (List<Interesado>) session.getAttribute(variableSesion);

        // Añadimos el onuevo representante a la sesión
        interesados.add(interesado);

        // Actualizamos el representado de la sesión
        if (interesados.contains(representado)) {
            interesados.remove(representado);
            interesados.add(representado);

        }
        session.setAttribute(variableSesion, interesados);
    }



    /**
     * Obtiene la {@link es.caib.regweb3.model.Interesado} según su identificador.
     * Si no la encuentra la busca en la Sesion.
     */
    @RequestMapping(value = "/{tipoRegistro}/obtenerInteresado", method = RequestMethod.GET)
    @ResponseBody
    public Interesado obtenerInteresado(@PathVariable Long tipoRegistro,@RequestParam Long id, HttpServletRequest request) throws Exception {

        String variableSesion = (tipoRegistro.equals(REGISTRO_ENTRADA) ? RegwebConstantes.SESSION_INTERESADOS_ENTRADA:RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        Interesado interesado = interesadoEjb.findById(id);

        if(interesado == null) {
            HttpSession session = request.getSession();
            interesado = obtenerInteresadoSesion(id, session, variableSesion);

        }

        return interesado;
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
        if (interesado.getCanal() != null && interesado.getCanal() != 1) {
            //interesado.setCanal(null);
            interesado.setPais(null);
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
     * Comprueba si hay algún OrganismoInteresado ya añadido
     * @param session
     */
    @SuppressWarnings("unchecked")
    private String hayOrganismoInteresado(HttpSession session){

        List<Interesado> interesados = (List<Interesado>) session.getAttribute(RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        if (interesados != null) {

            for(Interesado interesado:interesados){
                if(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION.equals(interesado.getTipo())){
                    return interesado.getCodigoDir3();
                }
            }
        }

        return null;
    }
}
