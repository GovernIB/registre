package es.caib.regweb3.webapp.controller.registro;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.HistoricoRegistroEntradaLocal;
import es.caib.regweb3.persistence.ejb.RegistroEntradaLocal;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.RegistroEntradaWebValidator;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
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
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 * Controller para gestionar los Registros de Entrada
 * @author earrivi
 * Date: 31/03/14
 */
@Controller
@RequestMapping(value = "/registroEntrada")
@SessionAttributes({"registroEntrada"})
public class RegistroEntradaFormController extends AbstractRegistroCommonFormController {

    @Autowired
    private RegistroEntradaWebValidator registroEntradaValidator;
    

    @EJB(mappedName = "regweb3/HistoricoRegistroEntradaEJB/local")
    public HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;
    
    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;


    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String nuevoRegistroEntrada(Model model, HttpServletRequest request) throws Exception {

        Oficina oficina = getOficinaActiva(request);

        RegistroEntrada registroEntrada = new RegistroEntrada();
        registroEntrada.setRegistroDetalle(new RegistroDetalle());
        registroEntrada.setOficina(oficina);

        //Eliminamos los posibles interesados de la Sesion
        eliminarVariableSesion(request, RegwebConstantes.SESSION_INTERESADOS_ENTRADA);

        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

        model.addAttribute(getEntidadActiva(request));
        model.addAttribute(getUsuarioAutenticado(request));
        model.addAttribute(oficina);
        model.addAttribute("registroEntrada",registroEntrada);
        model.addAttribute("libros", getLibrosRegistroEntrada(request));
        model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);
        model.addAttribute("oficinasOrigen",  getOficinasOrigen(request));

        return "registroEntrada/registroEntradaForm";
    }


    /**
     * Guardar un nuevo {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String nuevoRegistroEntrada(@ModelAttribute("registroEntrada") RegistroEntrada registroEntrada,
        BindingResult result, Model model, SessionStatus status,
        HttpServletRequest request) throws Exception, I18NException, I18NValidationException {

        HttpSession session = request.getSession();
        Entidad entidad = getEntidadActiva(request);

        registroEntradaValidator.validate(registroEntrada, result);
        
        // Comprobamos si el usuario ha añadido algún interesado
        List<Interesado> interesadosSesion = (List<Interesado>) session.getAttribute(RegwebConstantes.SESSION_INTERESADOS_ENTRADA);
        Boolean errorInteresado = true;
        if(interesadosSesion != null && interesadosSesion.size() > 0){
            errorInteresado = false;
        }

        if (result.hasErrors() || errorInteresado) { // Si hay errores volvemos a la vista del formulario

            // Si no hay ningún interesado, generamos un error.
            if(errorInteresado){
                model.addAttribute("errorInteresado", errorInteresado);
            }

            model.addAttribute(entidad);
            model.addAttribute(getUsuarioAutenticado(request));
            model.addAttribute(getOficinaActiva(request));
            model.addAttribute("oficinasOrigen",  getOficinasOrigen(request));
            model.addAttribute("libros", getLibrosRegistroEntrada(request));

            // Organismo destino: Select
            LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

            if (registroEntrada.getDestino() != null) { // Si se ha escogido un Organismo destino

                Organismo organismo = organismoEjb.findByCodigoEntidad(registroEntrada.getDestino().getCodigo(), entidad.getId());

                if (organismo == null) {// Si es externo, lo creamos nuevo y lo añadimos a la lista del select

                    organismosOficinaActiva.add(new Organismo(null, registroEntrada.getDestino().getCodigo(), registroEntrada.getDestino().getDenominacion()));

                } else { // si es interno, miramos si ya esta en la lista, si no, lo añadimos
                    organismosOficinaActiva.add(organismo);
                }


            }
            model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);

            // Oficina Origen: Select
            Set<Oficina> oficinasOrigen = getOficinasOrigen(request);

            if (!registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo().equals("-1")) {// Han indicado oficina de origen

                Oficina oficinaOrigen = oficinaEjb.findByCodigoEntidad(registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo(), entidad.getId());

                if (oficinaOrigen == null) { // Es externa

                    oficinasOrigen.add(new Oficina(null, registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo(), registroEntrada.getRegistroDetalle().getOficinaOrigen().getDenominacion()));

                } else { // Es interna, la añadimos a la lista por si acaso no está
                    oficinasOrigen.add(oficinaOrigen);
                }
            }
            model.addAttribute("oficinasOrigen", oficinasOrigen);

            return "registroEntrada/registroEntradaForm";
        }else{ // Si no hay errores guardamos el registro

            try {

                UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

                registroEntrada.setOficina(getOficinaActiva(request));
                registroEntrada.setUsuario(usuarioEntidad);

                // Estado Registro entrada
                registroEntrada.setEstado(RegwebConstantes.REGISTRO_VALIDO);

                // Procesamos las opciones comunes del RegistroEntrada
                registroEntrada = procesarRegistroEntrada(registroEntrada, entidad);

                // Procesamos lo Interesados de la session
                List<Interesado> interesados = procesarInteresados(interesadosSesion, null);

                registroEntrada.getRegistroDetalle().setInteresados(interesados);

                //Guardamos el RegistroEntrada
                synchronized (this){
                    registroEntrada = registroEntradaEjb.registrarEntrada(registroEntrada);
                }

                //Guardamos el HistorioRegistroEntrada
                historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada, usuarioEntidad, RegwebConstantes.TIPO_MODIF_ALTA,false);


            }catch (Exception e) {
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                e.printStackTrace();
                return "redirect:/inici";
            }finally {
                status.setComplete();
                //Eliminamos los posibles interesados de la Sesion
                try {
                    eliminarVariableSesion(request, RegwebConstantes.SESSION_INTERESADOS_ENTRADA);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            return "redirect:/registroEntrada/"+registroEntrada.getId()+"/detalle";
        }
    }


    /**
     * Carga el formulario para modificar un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/edit", method = RequestMethod.GET)
    public String editarRegistroEntrada(@PathVariable("idRegistro") Long idRegistro,  Model model, HttpServletRequest request) throws Exception{

        if(!isOperador(request)){
            Mensaje.saveMessageError(request, getMessage("error.rol.operador"));
            return "redirect:/inici";
        }

        //Eliminamos los posibles interesados de la Sesion
        eliminarVariableSesion(request, RegwebConstantes.SESSION_INTERESADOS_ENTRADA);

        RegistroEntrada registroEntrada = null;

        Oficina oficina = getOficinaActiva(request);
        Usuario usuario = getUsuarioAutenticado(request);
        Entidad entidad = getEntidadActiva(request);

        try {
            registroEntrada = registroEntradaEjb.findById(idRegistro);

            LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));
            Set<Oficina> oficinasOrigen = getOficinasOrigen(request);

            if(!registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_PENDIENTE)){ //Si no se trata de una reserva de número

                // Organismo destino: Select
                if (registroEntrada.getDestino() == null) {// Es  Externo, lo añadimos al listado.

                    organismosOficinaActiva.add(new Organismo(null, registroEntrada.getDestinoExternoCodigo(), registroEntrada.getDestinoExternoDenominacion()));

                } else if (!organismosOficinaActiva.contains(registroEntrada.getDestino())) {// Si es Interno, pero no esta relacionado con la Oficina Activa
                    organismosOficinaActiva.add(registroEntrada.getDestino());
                }

                // Oficina Origen: Select
                Oficina oficinaOrigen = registroEntrada.getRegistroDetalle().getOficinaOrigen();
                if (oficinaOrigen == null && registroEntrada.getRegistroDetalle().getOficinaOrigenExternoCodigo() != null) {// Si  Externa, la añadimos al listado.

                    oficinasOrigen.add(new Oficina(null, registroEntrada.getRegistroDetalle().getOficinaOrigenExternoCodigo(), registroEntrada.getRegistroDetalle().getOficinaOrigenExternoDenominacion()));

                } else if (!oficinasOrigen.contains(oficinaOrigen)) {// Si es Interna, pero no esta relacionado con la Oficina Activa
                    oficinasOrigen.add(oficinaOrigen);
                }
            }

            model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);
            model.addAttribute("oficinasOrigen", oficinasOrigen);

        }catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute(entidad);
        model.addAttribute(usuario);
        model.addAttribute(oficina);
        model.addAttribute("registroEntrada",registroEntrada);

        return "registroEntrada/registroEntradaForm";
    }


    /**
     * Editar un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/edit", method = RequestMethod.POST)
    public String editarRegistroEntrada(@ModelAttribute("registroEntrada") RegistroEntrada registroEntrada, BindingResult result,
                                        Model model, SessionStatus status,HttpServletRequest request) throws Exception{


        registroEntradaValidator.validate(registroEntrada, result);
        Entidad entidad = getEntidadActiva(request);

        // Actualizamos los Interesados modificados, en el caso que de un RE Pendiente.
        Boolean errorInteresado = false;
        List<Interesado> interesadosSesion = null;

        if(registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_PENDIENTE)){
            HttpSession session = request.getSession();

            interesadosSesion = (List<Interesado>) session.getAttribute(RegwebConstantes.SESSION_INTERESADOS_ENTRADA);

            if(interesadosSesion == null || interesadosSesion.size() == 0){
                errorInteresado = true;
            }
        }

        if (result.hasErrors() || errorInteresado) { // Si hay errores volvemos a la vista del formulario

            // Si no hay ningún interesado, generamos un error.
            if(errorInteresado){
                model.addAttribute("errorInteresado", errorInteresado);
            }

            model.addAttribute(entidad);
            model.addAttribute("usuario", getUsuarioAutenticado(request));
            model.addAttribute("oficina", getOficinaActiva(request));

            // Organismo destino: Select
            LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));
            if (registroEntrada.getDestino() != null) { // Si se ha escogido un Organismo destino

                Organismo organismo = organismoEjb.findByCodigoEntidad(registroEntrada.getDestino().getCodigo(), entidad.getId());
                if (organismo == null) { // Si es externo, lo creamos nuevo y lo añadimos a la lista del select
                    log.info("Es organismo externo: " + registroEntrada.getDestino().getCodigo() + " - " + registroEntrada.getDestinoExternoDenominacion());
                    organismosOficinaActiva.add(new Organismo(null, registroEntrada.getDestino().getCodigo(), registroEntrada.getDestinoExternoDenominacion()));

                    // si es interno, miramos si ya esta en la lista, si no, lo añadimos
                } else {
                    log.info("Es organismo interno: " + organismo.getDenominacion());
                    organismosOficinaActiva.add(organismo);
                }
            }

            model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);

            // Oficina Origen: Select
            Set<Oficina> oficinasOrigen = getOficinasOrigen(request);

            if (!registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo().equals("-1")) { // Si han indicado OficinaOrigen
                Oficina oficinaOrigen = oficinaEjb.findByCodigoEntidad(registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo(), entidad.getId());

                if (oficinaOrigen == null) { // Es externa
                    log.info("Es oficina externa: " + registroEntrada.getRegistroDetalle().getOficinaOrigenExternoDenominacion());
                    oficinasOrigen.add(new Oficina(null, registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo(), registroEntrada.getRegistroDetalle().getOficinaOrigenExternoDenominacion()));

                } else { // Es interna, la añadimos a la lista por si acaso no está
                    log.info("Es oficina interna: " + oficinaOrigen.getDenominacion());
                    oficinasOrigen.add(oficinaOrigen);
                }
            }
            model.addAttribute("oficinasOrigen", oficinasOrigen);

            return "registroEntrada/registroEntradaForm";
        }else { // Si no hay errores actualizamos el registro

            try {
                Entidad entidadActiva = getEntidadActiva(request);
                UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

                // Procesamos las opciones comunes del RegistroEnrtrada
                registroEntrada = procesarRegistroEntrada(registroEntrada, entidad);

                // Si es PENDIENTE, Procesamos lo Interesados de la session
                if(registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_PENDIENTE)){

                    registroEntrada.getRegistroDetalle().setInteresados(procesarInteresados(interesadosSesion, registroEntrada.getRegistroDetalle().getId()));
                }

                // Calculamos los días transcurridos desde que se Registró para asignarle un Estado
                Long dias = RegistroUtils.obtenerDiasRegistro(registroEntrada.getFecha());

                if(dias >= entidadActiva.getDiasVisado()){ // Si ha pasado los Dias de Visado establecidos por la entidad.

                    registroEntrada.setEstado(RegwebConstantes.REGISTRO_PENDIENTE_VISAR);
                }else{ // Si aún no ha pasado los días definidos

                    // Si el Registro de Entrada tiene Estado Pendiente, al editarlo pasa a ser Válido.
                    if(registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_PENDIENTE)){
                        registroEntrada.setEstado(RegwebConstantes.REGISTRO_VALIDO);
                    }
                }

                // Obtenemos el RE antes de guardarlos, para crear el histórico
                RegistroEntrada registroEntradaAntiguo = registroEntradaEjb.findById(registroEntrada.getId());

                // Actualizamos el RegistroEntrada
                registroEntrada = registroEntradaEjb.merge(registroEntrada);

                // Creamos el Historico RegistroEntrada
                historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntradaAntiguo, usuarioEntidad, RegwebConstantes.TIPO_MODIF_DATOS,true);

                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));

            }catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                return "redirect:/inici";
            }finally {
                status.setComplete();
            }

            return "redirect:/registroEntrada/"+registroEntrada.getId()+"/detalle";
        }
    }



    /**
     * Procesa las opciones de comunes de un RegistroEntrada, lo utilizamos en la creación y modificación.
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    private RegistroEntrada procesarRegistroEntrada(RegistroEntrada registroEntrada, Entidad entidad) throws Exception{

        // Organismo destinatiario, determinando si es Interno o Externo
        Organismo orgDestino = organismoEjb.findByCodigoEntidad(registroEntrada.getDestino().getCodigo(), entidad.getId());

        if(orgDestino != null){ // es interno
            registroEntrada.setDestino(orgDestino);
            registroEntrada.setDestinoExternoCodigo(null);
            registroEntrada.setDestinoExternoDenominacion(null);

        } else { // es externo
            registroEntrada.setDestinoExternoCodigo(registroEntrada.getDestino().getCodigo());
            if(registroEntrada.getId()!= null){//es una modificación
                registroEntrada.setDestinoExternoDenominacion(registroEntrada.getDestinoExternoDenominacion());
            }else{
                registroEntrada.setDestinoExternoDenominacion(registroEntrada.getDestino().getDenominacion());
            }

            registroEntrada.setDestino(null);
        }

        // Oficina origen, determinando si es Interno o Externo
        Oficina oficinaOrigen = registroEntrada.getRegistroDetalle().getOficinaOrigen();

        if (oficinaOrigen.getCodigo().equals("-1")) { // No han indicado oficina de origen

            // Asignamos la Oficina donde se realiza el registro
            registroEntrada.getRegistroDetalle().setOficinaOrigen(registroEntrada.getOficina());
            registroEntrada.getRegistroDetalle().setOficinaOrigenExternoCodigo(null);
            registroEntrada.getRegistroDetalle().setOficinaOrigenExternoDenominacion(null);

        } else { // Han indicado oficina origen

            Oficina ofiOrigen = oficinaEjb.findByCodigoEntidad(oficinaOrigen.getCodigo(), entidad.getId());
            if (ofiOrigen != null) { // Es interna

                registroEntrada.getRegistroDetalle().setOficinaOrigen(ofiOrigen);
                registroEntrada.getRegistroDetalle().setOficinaOrigenExternoCodigo(null);
                registroEntrada.getRegistroDetalle().setOficinaOrigenExternoDenominacion(null);

            } else {  // es externa
                registroEntrada.getRegistroDetalle().setOficinaOrigenExternoCodigo(registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo());
                if (registroEntrada.getId() != null) {//es una modificación
                    registroEntrada.getRegistroDetalle().setOficinaOrigenExternoDenominacion(registroEntrada.getRegistroDetalle().getOficinaOrigenExternoDenominacion());
                } else {
                    registroEntrada.getRegistroDetalle().setOficinaOrigenExternoDenominacion(registroEntrada.getRegistroDetalle().getOficinaOrigen().getDenominacion());
                }

                registroEntrada.getRegistroDetalle().setOficinaOrigen(null);
            }
        }

        // Solo se comprueba si es una modificación de RegistroEntrada
        if(registroEntrada.getId() != null){
            // Si no ha introducido ninguna fecha de Origen, se establece la fecha actual
            if(registroEntrada.getRegistroDetalle().getFechaOrigen() == null){
                registroEntrada.getRegistroDetalle().setFechaOrigen(new Date());
            }

            // Si no ha introducido ningún número de registro de Origen, le ponemos el actual.
            if(registroEntrada.getRegistroDetalle().getNumeroRegistroOrigen() == null || registroEntrada.getRegistroDetalle().getNumeroRegistroOrigen().length() == 0){
                registroEntrada.getRegistroDetalle().setNumeroRegistroOrigen(registroEntrada.getNumeroRegistroFormateado());
            }
        }

        // No han especificado Codigo Asunto
        if( registroEntrada.getRegistroDetalle().getCodigoAsunto().getId() == null || registroEntrada.getRegistroDetalle().getCodigoAsunto().getId() == -1){
            registroEntrada.getRegistroDetalle().setCodigoAsunto(null);
        }

        // No han especificadoTransporte
        if( registroEntrada.getRegistroDetalle().getTransporte() == -1){
            registroEntrada.getRegistroDetalle().setTransporte(null);
        }


        return registroEntrada;
    }


    @InitBinder("registroEntrada")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");
        //binder.setDisallowedFields("registroDetalle.id");
        binder.setDisallowedFields("tipoInteresado");
        binder.setDisallowedFields("organismoInteresado");
        binder.setDisallowedFields("fecha");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class, dateEditor);

        binder.setValidator(this.registroEntradaValidator);
    }


}