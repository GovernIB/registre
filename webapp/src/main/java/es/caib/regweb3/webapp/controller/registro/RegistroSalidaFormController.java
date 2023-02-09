package es.caib.regweb3.webapp.controller.registro;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.PlantillaJson;
import es.caib.regweb3.persistence.ejb.CodigoAsuntoLocal;
import es.caib.regweb3.persistence.ejb.MultiEntidadLocal;
import es.caib.regweb3.persistence.ejb.PlantillaLocal;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.Dir3Caib;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.RegistroSalidaWebValidator;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Fundació BIT.
 * Controller para gestionar los Registros de Salida
 * @author earrivi
 * Date: 31/03/14
 */
@Controller
@RequestMapping(value = "/registroSalida")
@SessionAttributes({"registroSalida"})
public class RegistroSalidaFormController extends AbstractRegistroCommonFormController {

    @Autowired
    private RegistroSalidaWebValidator registroSalidaValidator;


    @EJB(mappedName = PlantillaLocal.JNDI_NAME)
    private PlantillaLocal plantillaEjb;

    @EJB(mappedName = CodigoAsuntoLocal.JNDI_NAME)
    private CodigoAsuntoLocal codigoAsuntoEjb;

    @EJB(mappedName = MultiEntidadLocal.JNDI_NAME)
    private MultiEntidadLocal multiEntidadEjb;

    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.RegistroSalida} a partir de una {@link Plantilla}
     */
    @RequestMapping(value = "/new/{idPlantilla}", method = RequestMethod.GET)
    public String nuevoRegistroSalidaPlantilla(@PathVariable("idPlantilla") Long idPlantilla, Model model, HttpServletRequest request) throws Exception {

        // Buscamos la Plantilla
        Plantilla plantilla = plantillaEjb.findById(idPlantilla);

        LinkedHashSet<Oficina> oficinasOrigen;
        if(multiEntidadEjb.isMultiEntidad()){
            oficinasOrigen = new LinkedHashSet<>(getOficinasOrigenMultiEntidad(request));
        }else{
            oficinasOrigen = new LinkedHashSet<>(getOficinasOrigen(request));
        }

        // Buscamos nuestra oficina activa
        Oficina oficinaActiva = getOficinaActiva(request);
        // Cargamos la entida activa
        Entidad entidadActiva = getEntidadActiva(request);

        //Eliminamos los posibles interesados de la Sesion
        eliminarVariableSesion(request, RegwebConstantes.SESSION_INTERESADOS_ENTRADA);

        // Cargamos los datos de la Plantilla en el Registro Entrada
        RegistroSalida registroSalida = cargarPlantillaRegistroSalida(plantilla, oficinaActiva, entidadActiva, oficinasOrigen, request);

        model.addAttribute(entidadActiva);
        model.addAttribute(getUsuarioAutenticado(request));
        model.addAttribute(oficinaActiva);
        model.addAttribute("registroSalida",registroSalida);
        model.addAttribute("organismosOficinaActiva", getOrganismosOficinaActiva(request));
        model.addAttribute("oficinasOrigen", oficinasOrigen);
        model.addAttribute("origenPlantilla", true);
        model.addAttribute("ultimosOrganismos",  registroSalidaConsultaEjb.ultimosOrganismosRegistro(getUsuarioEntidadActivo(request)));

        return "registroSalida/registroSalidaForm";
    }

    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.RegistroSalida}
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String nuevoRegistroSalida(Model model, HttpServletRequest request) throws Exception {

        Oficina oficina = getOficinaActiva(request);

        LinkedHashSet<Oficina> oficinasOrigen;
        if(multiEntidadEjb.isMultiEntidad()){
            oficinasOrigen = new LinkedHashSet<>(getOficinasOrigenMultiEntidad(request));
        }else{
            oficinasOrigen = new LinkedHashSet<>(getOficinasOrigen(request));
        }

        RegistroSalida registroSalida = new RegistroSalida();
        registroSalida.setLibro(getLibroEntidad(request));
        registroSalida.setOficina(oficina);
        registroSalida.getRegistroDetalle().setPresencial(true);

        //Eliminamos los posibles interesados de la Sesion
        eliminarVariableSesion(request, RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        model.addAttribute(getEntidadActiva(request));
        model.addAttribute(getUsuarioAutenticado(request));
        model.addAttribute(oficina);
        model.addAttribute("registroSalida",registroSalida);
        model.addAttribute("organismosOficinaActiva", getOrganismosOficinaActiva(request));
        model.addAttribute("oficinasOrigen",  oficinasOrigen);
        model.addAttribute("ultimosOrganismos",  registroSalidaConsultaEjb.ultimosOrganismosRegistro(getUsuarioEntidadActivo(request)));


        return "registroSalida/registroSalidaForm";
    }

    /**
     * Guardar un nuevo {@link es.caib.regweb3.model.RegistroSalida}
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    @SuppressWarnings("unchecked")
    public String nuevoRegistroSalida(@ModelAttribute("registroSalida") RegistroSalida registroSalida,
        BindingResult result, Model model, SessionStatus status,
        HttpServletRequest request) throws Exception, I18NException, I18NValidationException {

        HttpSession session = request.getSession();
        Entidad entidad = getEntidadActiva(request);

        registroSalidaValidator.validate(registroSalida, result);

        // Comprobamos si el usuario ha añadido algún interesado
        List<Interesado> interesadosSesion = (List<Interesado>) session.getAttribute(RegwebConstantes.SESSION_INTERESADOS_SALIDA);
        Boolean errorInteresado = true;
        Boolean errorInteresadoNotificaciones = true;
        if(interesadosSesion != null && interesadosSesion.size() > 0){
            errorInteresado = false;
            for(Interesado inter: interesadosSesion){
                if(inter.getReceptorNotificaciones()){
                    errorInteresadoNotificaciones = false;
                    break;
                }
            }
        }

        if (result.hasErrors() || errorInteresado || errorInteresadoNotificaciones) { // Si hay errores volvemos a la vista del formulario

            // Si no hay ningún interesado, generamos un error.
            if(errorInteresado){
                model.addAttribute("errorInteresado", errorInteresado);
            }

            if(errorInteresadoNotificaciones){
                model.addAttribute("errorInteresadoNotificaciones", errorInteresadoNotificaciones);
            }

            LinkedHashSet<Oficina> oficinasOrigen;
            if(multiEntidadEjb.isMultiEntidad()){
                oficinasOrigen = new LinkedHashSet<>(getOficinasOrigenMultiEntidad(request));
            }else{
                oficinasOrigen = new LinkedHashSet<>(getOficinasOrigen(request));
            }

            model.addAttribute(entidad);
            model.addAttribute(getUsuarioAutenticado(request));
            model.addAttribute(getOficinaActiva(request));
            model.addAttribute("oficinasOrigen",  oficinasOrigen);

            // Organismo origen: Select
            LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

            model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);

            // Si la Oficina Origen es Externa, la añadimos al listado.
            if (!registroSalida.getRegistroDetalle().getOficinaOrigen().getCodigo().equals("-1")) {// Han indicado oficina de origen

                Oficina oficinaOrigen = oficinaEjb.findByCodigoByEntidadMultiEntidad(registroSalida.getRegistroDetalle().getOficinaOrigen().getCodigo(), entidad.getId());
                if (oficinaOrigen == null)  { // Es externa
                    oficinasOrigen.add(new Oficina(null, registroSalida.getRegistroDetalle().getOficinaOrigen().getCodigo(), registroSalida.getRegistroDetalle().getOficinaOrigen().getDenominacion()));

                } else { // Es interna o multientidad, la añadimos a la lista por si acaso no está
                    oficinasOrigen.add(oficinaOrigen);
                }
            }
            model.addAttribute("oficinasOrigen", oficinasOrigen);
            model.addAttribute("ultimosOrganismos",  registroSalidaConsultaEjb.ultimosOrganismosRegistro(getUsuarioEntidadActivo(request)));


            return "registroSalida/registroSalidaForm";
        }else{ // Si no hay errores guardamos el registroSalida

            try {

                UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

                registroSalida.setOficina(getOficinaActiva(request));
                registroSalida.setUsuario(usuarioEntidad);

                // Estado RegistroSalida
                registroSalida.setEstado(RegwebConstantes.REGISTRO_VALIDO);

                // Procesamos las opciones comunes del RegistroSalida
                registroSalida = procesarRegistroSalida(registroSalida, entidad);

                // Guardamos el RegistroSalida
                registroSalida = registroSalidaEjb.registrarSalida(registroSalida, entidad, usuarioEntidad, interesadosSesion, null, false);


            }catch (I18NException e) {
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                e.printStackTrace();
                return "redirect:/inici";
            }finally {
                status.setComplete();
                //Eliminamos los posibles interesados de la Sesion
                try {
                    eliminarVariableSesion(request, RegwebConstantes.SESSION_INTERESADOS_SALIDA);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return "redirect:/registroSalida/"+registroSalida.getId()+"/detalle";
        }
    }


    /**
     * Carga el formulario para modificar un {@link es.caib.regweb3.model.RegistroSalida}
     */
    @RequestMapping(value = "/{idRegistro}/edit", method = RequestMethod.GET)
    public String editarRegistroSalida(@PathVariable("idRegistro") Long idRegistro,  Model model, HttpServletRequest request) throws Exception{

        //Eliminamos los posibles interesados de la Sesion
        eliminarVariableSesion(request, RegwebConstantes.SESSION_INTERESADOS_SALIDA);

        RegistroSalida registroSalida = null;

        Oficina oficina = getOficinaActiva(request);
        Usuario usuario = getUsuarioAutenticado(request);
        Entidad entidad = getEntidadActiva(request);

        try {
            registroSalida = registroSalidaEjb.findByIdCompleto(idRegistro);

            if(validarPermisosEdicion(registroSalida, request, RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_SALIDA)){
                return "redirect:/aviso";
            }

            // Organismo origen: Select
            LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));
            // Si el Organismo Origen no está en al lista lo añadimos
            if (!organismosOficinaActiva.contains(registroSalida.getOrigen())) {
                organismosOficinaActiva.add(registroSalida.getOrigen());
            }

            // Oficina Origen: Select
            Set<Oficina> oficinasOrigen;
            if(multiEntidadEjb.isMultiEntidad()){
                oficinasOrigen = new LinkedHashSet<>(getOficinasOrigenMultiEntidad(request));
            }else{
                oficinasOrigen = new LinkedHashSet<>(getOficinasOrigen(request));
            }

            // Si la Oficina Origen es Externa, la añadimos al listado.
            Oficina oficinaOrigen = registroSalida.getRegistroDetalle().getOficinaOrigen();
            if (oficinaOrigen == null && registroSalida.getRegistroDetalle().getOficinaOrigenExternoCodigo() != null) {// Si  Externa, la añadimos al listado.
                oficinasOrigen.add(new Oficina(null, registroSalida.getRegistroDetalle().getOficinaOrigenExternoCodigo(), registroSalida.getRegistroDetalle().getOficinaOrigenExternoDenominacion()));

            } else if (!oficinasOrigen.contains(oficinaOrigen)) {// Si es Interna, pero no esta relacionado con la Oficina Activa
                oficinasOrigen.add(oficinaOrigen);
            }

            model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);
            model.addAttribute("oficinasOrigen", oficinasOrigen);


        }catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute(entidad);
        model.addAttribute(usuario);
        model.addAttribute(oficina);
        model.addAttribute("registroSalida",registroSalida);

        return "registroSalida/registroSalidaForm";
    }

    /**
     * Editar un {@link es.caib.regweb3.model.RegistroSalida}
     */
    @RequestMapping(value = "/{idRegistro}/edit", method = RequestMethod.POST)
    public String editarRegistroSalida(@ModelAttribute("registroSalida") RegistroSalida registroSalida, BindingResult result,
                                        Model model, SessionStatus status,HttpServletRequest request) throws Exception{


        registroSalidaValidator.validate(registroSalida, result);
        Entidad entidad = getEntidadActiva(request);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            model.addAttribute("usuario", getUsuarioAutenticado(request));
            model.addAttribute("oficina", getOficinaActiva(request));
            model.addAttribute(entidad);

            // Organismo origen: Select
            LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));
            // Si el Organismo Origen no está en al lista lo añadimos
            if (!organismosOficinaActiva.contains(registroSalida.getOrigen())) {
                organismosOficinaActiva.add(registroSalida.getOrigen());
            }

            model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);

            // Oficina Origen: Select
            Set<Oficina> oficinasOrigen;
            if(multiEntidadEjb.isMultiEntidad()){
                oficinasOrigen = new LinkedHashSet<>(getOficinasOrigenMultiEntidad(request));
            }else{
                oficinasOrigen = new LinkedHashSet<>(getOficinasOrigen(request));
            }

            if (!registroSalida.getRegistroDetalle().getOficinaOrigen().getCodigo().equals("-1")) { // Si han indicado OficinaOrigen
                Oficina oficinaOrigen = oficinaEjb.findByCodigoByEntidadMultiEntidad(registroSalida.getRegistroDetalle().getOficinaOrigen().getCodigo(), entidad.getId());

                if (oficinaOrigen == null) { // Es externa
                    oficinasOrigen.add(new Oficina(null, registroSalida.getRegistroDetalle().getOficinaOrigen().getCodigo(), registroSalida.getRegistroDetalle().getOficinaOrigenExternoDenominacion()));

                } else { // Es interna, la añadimos a la lista por si acaso no está
                    oficinasOrigen.add(oficinaOrigen);
                }
            }
            model.addAttribute("oficinasOrigen", oficinasOrigen);

            return "registroSalida/registroSalidaForm";

        }else { // Si no hay errores actualizamos el registroSalida

            try {

                UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

                // Procesamos las opciones comunes del RegistroEnrtrada
                registroSalida = procesarRegistroSalida(registroSalida, entidad);

                // Calculamos los días transcurridos desde que se Registró para asignarle un Estado
                Long dias = RegistroUtils.obtenerDiasRegistro(registroSalida.getFecha());

                if(dias >= 1){ // Si ha pasado 1 día o mas
                    registroSalida.setEstado(RegwebConstantes.REGISTRO_PENDIENTE_VISAR);
                }

                // Obtenemos el RE antes de guardarlos, para crear el histórico
                RegistroSalida antiguo = registroSalidaEjb.findByIdCompleto(registroSalida.getId());

                // Actualizamos el RegistroSalida
                registroSalida = registroSalidaEjb.actualizar(antiguo, registroSalida, entidad, usuarioEntidad);

                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));

            } catch(I18NException i18ne) {
              log.error(I18NUtils.getMessage(i18ne), i18ne);
              Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
              return "redirect:/inici";
            } catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                return "redirect:/inici";

            } finally {
                status.setComplete();
            }

            return "redirect:/registroSalida/"+registroSalida.getId()+"/detalle";
        }
    }

    /**
     * Método que rectifica un Registro de Salida
     * @param idRegistro identificador del registro de salida
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idRegistro}/rectificar", method=RequestMethod.GET)
    public String rectificar(@PathVariable Long idRegistro, HttpServletRequest request) throws Exception {

        Entidad entidad = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        RegistroSalida registroSalidaRectificado;

        try{

            RegistroSalida registroSalida = registroSalidaEjb.getConAnexosFull(idRegistro);

            // Comprobamos si el usuario tiene permisos para registrar el registro rectificado
            if (!permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroSalida.getOficina().getOrganismoResponsable().getId(), RegwebConstantes.PERMISO_REGISTRO_SALIDA, true)) {
                Mensaje.saveMessageError(request, I18NUtils.tradueix("aviso.registro.permisos"));
                return "redirect:/registroSalida/"+idRegistro+"/detalle";
            }

            List<Long> isRectificar = new ArrayList<Long>();
            Collections.addAll(isRectificar, RegwebConstantes.REGISTRO_RECHAZADO, RegwebConstantes.REGISTRO_ANULADO);

            // Si el Registro se puede rectificar y el usuario tiene permisos sobre el libro
            if(isRectificar.contains(registroSalida.getEstado())){

                registroSalidaRectificado = registroSalidaEjb.rectificar(entidad, registroSalida, usuarioEntidad);

                Mensaje.saveMessageInfo(request, getMessage("registro.rectificar.ok"));
                return "redirect:/registroSalida/"+registroSalidaRectificado.getId()+"/detalle";
            }else{

                Mensaje.saveMessageError(request, getMessage("registro.rectificar.no"));
            }

        }catch (I18NException e){
            log.info("Error al rectificar el registro");
            e.printStackTrace();
            Mensaje.saveMessageError(request, getMessage("registro.rectificar.error"));
        }

        return "redirect:/registroSalida/"+idRegistro+"/detalle";

    }

    /**
     *
     */
    @RequestMapping(value = "/actualizarEvento", method = RequestMethod.GET)
    public @ResponseBody
    void actualizarEvento(@RequestParam Long idRegistroSalida, HttpServletRequest request) throws Exception {

        Entidad entidadActiva = getEntidadActiva(request);

        registroSalidaEjb.actualizarEvento(idRegistroSalida,entidadActiva);

    }


    /**
     * Procesa las opciones de comunes de un RegistroSalida, lo utilizamos en la creación y modificación.
     * @param registroSalida
     * @return
     * @throws Exception
     */
    private RegistroSalida procesarRegistroSalida(RegistroSalida registroSalida, Entidad entidad) throws Exception{

        Organismo organismoDestino = registroSalida.getOrigen();

        // Gestionamos el Organismo (siempre será interno porque solo se puede escoger los organismo de la oficina activa) no hace falta tener en cuenta la multientidad
        Organismo orgDestino = organismoEjb.findByCodigoEntidadLigero(organismoDestino.getCodigo(), entidad.getId());
        registroSalida.setOrigen(orgDestino);

        // Oficina origen, determinando si es Interno o Externo
        Oficina oficinaOrigen = registroSalida.getRegistroDetalle().getOficinaOrigen();

        if (oficinaOrigen.getCodigo().equals("-1")) { // No han indicado oficina de origen

            // Asignamos la Oficina donde se realiza el registro
            registroSalida.getRegistroDetalle().setOficinaOrigen(registroSalida.getOficina());
            registroSalida.getRegistroDetalle().setOficinaOrigenExternoCodigo(null);
            registroSalida.getRegistroDetalle().setOficinaOrigenExternoDenominacion(null);

        } else { // Han indicado oficina origen

            Oficina ofiOrigen = oficinaEjb.findByCodigoByEntidadMultiEntidad(oficinaOrigen.getCodigo(), entidad.getId());

            if (ofiOrigen == null || (!entidad.getId().equals(ofiOrigen.getOrganismoResponsable().getEntidad().getId())) ) { // Es externa
                registroSalida.getRegistroDetalle().setOficinaOrigenExternoCodigo(registroSalida.getRegistroDetalle().getOficinaOrigen().getCodigo());
                if (registroSalida.getId() != null) {//es una modificación
                    registroSalida.getRegistroDetalle().setOficinaOrigenExternoDenominacion(registroSalida.getRegistroDetalle().getOficinaOrigenExternoDenominacion());
                } else {
                    registroSalida.getRegistroDetalle().setOficinaOrigenExternoDenominacion(registroSalida.getRegistroDetalle().getOficinaOrigen().getDenominacion());
                }

                registroSalida.getRegistroDetalle().setOficinaOrigen(null);

            }else{ //Interna
                registroSalida.getRegistroDetalle().setOficinaOrigen(ofiOrigen);
                registroSalida.getRegistroDetalle().setOficinaOrigenExternoCodigo(null);
                registroSalida.getRegistroDetalle().setOficinaOrigenExternoDenominacion(null);
            }
        }


        // Solo se comprueba si es una modificación de RegistroSalida
        if(registroSalida.getId() != null){
            // Si no ha introducido ninguna fecha de Origen, se establece la fecha actual
            if(registroSalida.getRegistroDetalle().getFechaOrigen() == null){
                registroSalida.getRegistroDetalle().setFechaOrigen(new Date());
            }

            // Si no ha introducido ningún número de registroSalida de Origen, le ponemos el actual.
            if(registroSalida.getRegistroDetalle().getNumeroRegistroOrigen() == null || registroSalida.getRegistroDetalle().getNumeroRegistroOrigen().length() == 0){
                registroSalida.getRegistroDetalle().setNumeroRegistroOrigen(registroSalida.getNumeroRegistroFormateado());
            }
        }

        // No han especificado Codigo Asunto
        if( registroSalida.getRegistroDetalle().getCodigoAsunto().getId() == null || registroSalida.getRegistroDetalle().getCodigoAsunto().getId() == -1){
            registroSalida.getRegistroDetalle().setCodigoAsunto(null);
        }

        // No han especificadoTransporte
        if( registroSalida.getRegistroDetalle().getTransporte() == -1){
            registroSalida.getRegistroDetalle().setTransporte(null);
        }

        return registroSalida;
    }

    /**
     * Carga los valores de una Plantilla en un {@link es.caib.regweb3.model.RegistroSalida}
     * @param plantilla
     * @param oficinaActiva
     * @return
     * @throws Exception
     */
    private RegistroSalida cargarPlantillaRegistroSalida(Plantilla plantilla, Oficina oficinaActiva, Entidad entidadActiva, LinkedHashSet<Oficina> oficinasOrigen, HttpServletRequest request) throws Exception {

        Dir3Caib dir3Caib = getLoginInfo(request).getDir3Caib();
        RegistroSalida registroSalida =  new RegistroSalida();
        registroSalida.getRegistroDetalle().setPresencial(true);

        // Recuperamos los valores de la Plantilla
        PlantillaJson plantillaJson = RegistroUtils.desSerilizarPlantillaXml(plantilla.getRepro());

        // Asignamos los valores obtenidos de la Plantilla al Registro de Salida
        // ID registro Salida
        registroSalida.setId(null);
        // Libro
        registroSalida.setLibro(getLibroEntidad(request));
        // Oficina
        registroSalida.setOficina(oficinaActiva);
        // Extracto
        registroSalida.getRegistroDetalle().setExtracto(plantillaJson.getExtracto());
        // Código Asunto
        if(plantillaJson.getIdCodigoAsunto()!=null && !plantillaJson.getIdCodigoAsunto().equals("")) {
            CodigoAsunto codigoAsunto = codigoAsuntoEjb.findById(Long.parseLong(plantillaJson.getIdCodigoAsunto()));
            registroSalida.getRegistroDetalle().setCodigoAsunto(codigoAsunto);
        }
        // Idioma
        registroSalida.getRegistroDetalle().setIdioma(Long.parseLong(plantillaJson.getIdIdioma()));
        // Referencia externa
        registroSalida.getRegistroDetalle().setReferenciaExterna(plantillaJson.getReferenciaExterna());
        // Expediente
        registroSalida.getRegistroDetalle().setExpediente(plantillaJson.getExpediente());
        // Transporte
        registroSalida.getRegistroDetalle().setTransporte(Long.parseLong(plantillaJson.getIdTransporte()));
        // Número transporte
        registroSalida.getRegistroDetalle().setNumeroTransporte(plantillaJson.getNumeroTransporte());
        // Observaciones
        registroSalida.getRegistroDetalle().setObservaciones(plantillaJson.getObservaciones());
        // Número Registro Origen
        registroSalida.getRegistroDetalle().setNumeroRegistroOrigen(plantillaJson.getNumeroRegistroOrigen());
        // Código Sia
        if(plantillaJson.getCodigoSia()!=null) {
            if (!plantillaJson.getCodigoSia().isEmpty()) {
                registroSalida.getRegistroDetalle().setCodigoSia(plantillaJson.getCodigoSia());
            }
        }
        // Interesado Administración
        if(StringUtils.isNotEmpty(plantillaJson.getInteresado())) {

            String interesado = plantillaJson.getInteresado();
            String codigoDIR3 = interesado.substring(0, interesado.indexOf('+'));
            String denominacion = interesado.substring(interesado.indexOf('+') + 1);

            List<Interesado> interesados = new ArrayList<Interesado>();

            Interesado organismo = new Interesado(codigoDIR3,denominacion);

            interesados.add(organismo);
            HttpSession session = request.getSession();
            session.setAttribute(RegwebConstantes.SESSION_INTERESADOS_SALIDA, interesados);
        }

        // Fecha origen
        if(!plantillaJson.getFechaOrigen().equals("")) {
            Date fechaOrigen = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(plantillaJson.getFechaOrigen());
            registroSalida.getRegistroDetalle().setFechaOrigen(fechaOrigen);
        }

        // Comprobamos la unidad origen
        if(plantillaJson.getOrigenCodigo()!= null && !plantillaJson.isOrigenExterno()){ // Comprobamos en REGWEB3 si está vigente
            Organismo organismoOrigen = organismoEjb.findByCodigoEntidadSinEstado(plantillaJson.getOrigenCodigo(), entidadActiva.getId());
            if(organismoOrigen.getEstado().getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)){ // Es vigente
                registroSalida.setOrigen(organismoOrigen);
            }else{ // Ya no es vigente
                Set<Organismo> organismoHistoricosFinales = new HashSet<Organismo>();
                organismoEjb.obtenerHistoricosFinales(organismoOrigen.getId(), organismoHistoricosFinales);
                Organismo organismoVigente = organismoHistoricosFinales.iterator().next();
                registroSalida.setOrigen(organismoVigente);
                //Guarda el nuevo organismo en la plantilla
                plantillaJson.setOrigenCodigo(organismoVigente.getCodigo());
                plantillaJson.setOrigenDenominacion(organismoVigente.getDenominacion());
                plantilla.setRepro(RegistroUtils.serilizarXml(plantillaJson));
                plantillaEjb.merge(plantilla);
            }
        }

        // Oficina Origen
        // Externa
        if(plantillaJson.getOficinaCodigo()!= null  && !plantillaJson.getOficinaCodigo().equals("-1") && plantillaJson.isOficinaExterna()){// Preguntamos a DIR3 si está Vigente
            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(dir3Caib.getServer(), dir3Caib.getUser(), dir3Caib.getPassword());
            OficinaTF oficinaOrigen = oficinasService.obtenerOficina(plantillaJson.getOficinaCodigo(),null,null);
            if(oficinaOrigen != null) {
                if (oficinaOrigen.getEstado().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)) { //Si está vigente, asignamos la oficina al Registro de entrada
                    Oficina oficinaExterna = new Oficina();
                    oficinaExterna.setCodigo(oficinaOrigen.getCodigo());
                    oficinaExterna.setDenominacion(oficinaOrigen.getDenominacion());
                    registroSalida.getRegistroDetalle().setOficinaOrigen(oficinaExterna);
                    oficinasOrigen.add(oficinaExterna); // Añadimos la oficina al listado
                }
            }
            // Interna
        }else{// Comprobamos en REGWEB3 si está vigente
            Oficina oficinaOrigen = oficinaEjb.findByCodigoEntidad(plantillaJson.getOficinaCodigo(), entidadActiva.getId());
            if(oficinaOrigen != null){
                registroSalida.getRegistroDetalle().setOficinaOrigen(oficinaOrigen);
            }
        }

        return registroSalida;
    }


    @InitBinder("registroSalida")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");
        binder.setDisallowedFields("registroDetalle.id");
        binder.setDisallowedFields("tipoInteresado");
        binder.setDisallowedFields("organismoInteresado");
        binder.setDisallowedFields("fecha");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class, dateEditor);

        binder.setValidator(this.registroSalidaValidator);
    }

}