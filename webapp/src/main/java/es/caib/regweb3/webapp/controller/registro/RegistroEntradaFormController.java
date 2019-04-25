package es.caib.regweb3.webapp.controller.registro;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.PlantillaJson;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.I18NLogicUtils;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.RegistroEntradaWebValidator;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.i18n.LocaleContextHolder;
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
    private HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;
    
    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    private RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/PlantillaEJB/local")
    private PlantillaLocal plantillaEjb;

    @EJB(mappedName = "regweb3/LibroEJB/local")
    private LibroLocal libroEjb;

    @EJB(mappedName = "regweb3/TipoAsuntoEJB/local")
    private TipoAsuntoLocal tipoAsuntoEjb;

    @EJB(mappedName = "regweb3/CodigoAsuntoEJB/local")
    private CodigoAsuntoLocal codigoAsuntoEjb;

    @EJB(mappedName = "regweb3/OrganismoEJB/local")
    private OrganismoLocal organismoEjb;


    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.RegistroEntrada} a partir de una {@link Plantilla}
     */
    @RequestMapping(value = "/new/{idPlantilla}", method = RequestMethod.GET)
    public String nuevoRegistroEntradaPlantilla(@PathVariable("idPlantilla") Long idPlantilla, Model model, HttpServletRequest request) throws Exception {

        // Buscamos la Plantilla
        Plantilla plantilla = plantillaEjb.findById(idPlantilla);

        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));
        LinkedHashSet<Oficina> oficinasOrigen = new LinkedHashSet<Oficina>(getOficinasOrigen(request));

        // Buscamos nuestra oficina activa
        Oficina oficinaActiva = getOficinaActiva(request);
        // Cargamos la entida activa
        Entidad entidadActiva = getEntidadActiva(request);

        // Cargamos los datos de la Plantilla en el Registro Entrada
        RegistroEntrada registroEntrada = cargarPlantillaRegistroEntrada(plantilla, oficinaActiva, entidadActiva, organismosOficinaActiva, oficinasOrigen);

        //Eliminamos los posibles interesados de la Sesion
        eliminarVariableSesion(request, RegwebConstantes.SESSION_INTERESADOS_ENTRADA);

        model.addAttribute(entidadActiva);
        model.addAttribute(getUsuarioAutenticado(request));
        model.addAttribute(oficinaActiva);
        model.addAttribute("registroEntrada",registroEntrada);
        model.addAttribute("libros", getLibrosRegistroEntrada(request));
        model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);
        model.addAttribute("oficinasOrigen", oficinasOrigen);
        model.addAttribute("origenPlantilla", true);

        return "registroEntrada/registroEntradaForm";
    }



    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String nuevoRegistroEntrada(Model model, HttpServletRequest request) throws Exception {

        Oficina oficina = getOficinaActiva(request);

        RegistroEntrada registroEntrada = new RegistroEntrada();
        registroEntrada.setOficina(oficina);
        registroEntrada.getRegistroDetalle().setPresencial(true);

        //Eliminamos los posibles interesados de la Sesion
        eliminarVariableSesion(request, RegwebConstantes.SESSION_INTERESADOS_ENTRADA);

        model.addAttribute(getEntidadActiva(request));
        model.addAttribute(getUsuarioAutenticado(request));
        model.addAttribute(oficina);
        model.addAttribute("registroEntrada",registroEntrada);
        model.addAttribute("libros", getLibrosRegistroEntrada(request));
        model.addAttribute("organismosOficinaActiva", getOrganismosOficinaActiva(request));
        model.addAttribute("oficinasOrigen",  getOficinasOrigen(request));

        return "registroEntrada/registroEntradaForm";
    }


    /**
     * Guardar un nuevo {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    @SuppressWarnings("unchecked")
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

                //Guardamos el RegistroEntrada
                synchronized (this){
                    registroEntrada = registroEntradaEjb.registrarEntrada(registroEntrada, usuarioEntidad, interesadosSesion, null);
                }

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

            if(!registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_RESERVA)){ //Si no se trata de una reserva de número

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
    @SuppressWarnings("unchecked")
    public String editarRegistroEntrada(@ModelAttribute("registroEntrada") RegistroEntrada registroEntrada, BindingResult result,
                                        Model model, SessionStatus status,HttpServletRequest request) throws Exception{


        registroEntradaValidator.validate(registroEntrada, result);
        Entidad entidad = getEntidadActiva(request);

        // Actualizamos los Interesados modificados, en el caso que de un RE Pendiente.
        Boolean errorInteresado = false;
        List<Interesado> interesadosSesion = null;

        if(registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_RESERVA)){
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
                if(registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_RESERVA)){

                    registroEntrada.getRegistroDetalle().setInteresados(interesadoEjb.guardarInteresados(interesadosSesion, registroEntrada.getRegistroDetalle()) );
                }

                // Calculamos los días transcurridos desde que se Registró para asignarle un Estado
                Long dias = RegistroUtils.obtenerDiasRegistro(registroEntrada.getFecha());

                if(dias >= entidadActiva.getDiasVisado()){ // Si ha pasado los Dias de Visado establecidos por la entidad.

                    registroEntrada.setEstado(RegwebConstantes.REGISTRO_PENDIENTE_VISAR);
                }else{ // Si aún no ha pasado los días definidos

                    // Si el Registro de Entrada tiene Estado Pendiente, al editarlo pasa a ser Válido.
                    if(registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_RESERVA)){
                        registroEntrada.setEstado(RegwebConstantes.REGISTRO_VALIDO);
                    }
                }

                // Obtenemos el RE antes de guardarlos, para crear el histórico
                RegistroEntrada registroEntradaAntiguo = registroEntradaEjb.findById(registroEntrada.getId());

                // Actualizamos el RegistroEntrada
                registroEntrada = registroEntradaEjb.merge(registroEntrada);

                // Creamos el Historico RegistroEntrada
                historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntradaAntiguo, usuarioEntidad, I18NLogicUtils.tradueix(LocaleContextHolder.getLocale(),"registro.modificacion.datos" ),true);
                registroEntradaEjb.postProcesoActualizarRegistro(registroEntrada,entidadActiva.getId());


                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));
            } catch(I18NException i18ne) {
              log.error(I18NUtils.getMessage(i18ne), i18ne);
              Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
              return "redirect:/inici";
            } catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
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
     * Método que rectifica un Registro de Entrada
     * @param idRegistro identificador del registro de entrada
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idRegistro}/rectificar", method=RequestMethod.GET)
    public String rectificar(@PathVariable Long idRegistro, HttpServletRequest request) throws Exception {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);
        RegistroEntrada registroEntradaRectificado;

        // Comprobamos si el usuario tiene permisos para registrar el registro rectificado
        if (!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroEntrada.getLibro().getId(), RegwebConstantes.PERMISO_REGISTRO_ENTRADA)) {
            Mensaje.saveMessageError(request, I18NUtils.tradueix("aviso.registro.permisos"));
            return "redirect:/registroEntrada/"+idRegistro+"/detalle";
        }

        try{

            List<Long> isRectificar = new ArrayList<Long>();
            Collections.addAll(isRectificar, RegwebConstantes.REGISTRO_RECHAZADO, RegwebConstantes.REGISTRO_ANULADO);

            // Si el Registro se puede rectificar y el usuario tiene permisos sobre el libro
            if(isRectificar.contains(registroEntrada.getEstado())){

                registroEntradaRectificado = registroEntradaEjb.rectificar(idRegistro, usuarioEntidad);

                Mensaje.saveMessageInfo(request, getMessage("registro.rectificar.ok"));
                return "redirect:/registroEntrada/"+registroEntradaRectificado.getId()+"/detalle";
            }else{

                log.info("Este registro no se puede rectificar");
                Mensaje.saveMessageError(request, getMessage("registro.rectificar.no"));
            }
        }catch (I18NException e){
                log.info("Error al rectificar el registro");
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("registro.rectificar.error"));
        }catch (Exception e){
            log.info("Error al rectificar el registro");
            e.printStackTrace();
            Mensaje.saveMessageError(request, getMessage("registro.rectificar.error"));
        }

        return "redirect:/registroEntrada/"+idRegistro+"/detalle";

    }


    /**
     * Procesa las opciones de comunes de un RegistroEntrada, lo utilizamos en la creación y modificación.
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    private RegistroEntrada procesarRegistroEntrada(RegistroEntrada registroEntrada, Entidad entidad) throws Exception{

        // Organismo destinatiario, determinando si es Interno o Externo. Si es organismo interno con una Entidad creada, será externo
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

    /**
     * Carga los valores de una Plantilla en un {@link es.caib.regweb3.model.RegistroEntrada}
     * @param plantilla
     * @param oficinaActiva
     * @return
     * @throws Exception
     */
    private RegistroEntrada cargarPlantillaRegistroEntrada(Plantilla plantilla, Oficina oficinaActiva, Entidad entidadActiva,
                                                       LinkedHashSet<Organismo> organismosOficinaActiva, LinkedHashSet<Oficina> oficinasOrigen) throws Exception{

        RegistroEntrada registroEntrada = new RegistroEntrada();
        RegistroDetalle registroDetalle = new RegistroDetalle();
        registroDetalle.setPresencial(true);

        // Recuperamos los valores de la Plantilla
        PlantillaJson plantillaJson = RegistroUtils.desSerilizarPlantillaXml(plantilla.getRepro());

        // Asignamos los valores obtenidos de la Plantilla al Registro de Entrada
        // ID registro Entrada
        registroEntrada.setId(null);
        // Libro
        Libro libro = libroEjb.findByCodigo(plantillaJson.getIdLibro());
        registroEntrada.setLibro(libro);
        // Oficina
        registroEntrada.setOficina(oficinaActiva);
        // Extracto
        registroDetalle.setExtracto(plantillaJson.getExtracto());
        // Tipo Asunto
        TipoAsunto tipoAsunto = tipoAsuntoEjb.findById(Long.parseLong(plantillaJson.getIdTipoAsunto()));
        registroDetalle.setTipoAsunto(tipoAsunto);
        // Código Asunto
        if(plantillaJson.getIdCodigoAsunto()!=null && !plantillaJson.getIdCodigoAsunto().equals("")) {
            CodigoAsunto codigoAsunto = codigoAsuntoEjb.findById(Long.parseLong(plantillaJson.getIdCodigoAsunto()));
            registroDetalle.setCodigoAsunto(codigoAsunto);
        }
        // Idioma
        registroDetalle.setIdioma(Long.parseLong(plantillaJson.getIdIdioma()));
        // Referencia externa
        registroDetalle.setReferenciaExterna(plantillaJson.getReferenciaExterna());
        // Expediente
        registroDetalle.setExpediente(plantillaJson.getExpediente());
        // Transporte
        registroDetalle.setTransporte(Long.parseLong(plantillaJson.getIdTransporte()));
        // Número transporte
        registroDetalle.setNumeroTransporte(plantillaJson.getNumeroTransporte());
        // Observaciones
        registroDetalle.setObservaciones(plantillaJson.getObservaciones());
        // Número Registro Origen
        registroDetalle.setNumeroRegistroOrigen(plantillaJson.getNumeroRegistroOrigen());
        // Código Sia
        registroDetalle.setCodigoSia(Long.parseLong(plantillaJson.getCodigoSia()));
        // Fecha origen
        if(!plantillaJson.getFechaOrigen().equals("")) {
            Date fechaOrigen = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(plantillaJson.getFechaOrigen());
            registroDetalle.setFechaOrigen(fechaOrigen);
        }

        // Comprobamos la unidad destino
        // Externa
        if(plantillaJson.getDestinoCodigo()!= null && plantillaJson.isDestinoExterno()){ // Preguntamos a DIR3 si está Vigente
            Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());
            UnidadTF unidad = unidadesService.obtenerUnidad(plantillaJson.getDestinoCodigo(), null, null);
            if(unidad != null) {
                if (unidad.getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)) { //Si está vigente, asignamos la unidad al Registro de entrada
                    Organismo organismoExterno = new Organismo();
                    organismoExterno.setCodigo(unidad.getCodigo());
                    organismoExterno.setDenominacion(unidad.getDenominacion());
                    registroEntrada.setDestino(organismoExterno);
                    organismosOficinaActiva.add(organismoExterno); // Añadimos la unidad al listado
                }
            }
            // Interna
        }else{ // Comprobamos en REGWEB3 si está vigente
            Organismo organismoDestino = organismoEjb.findByCodigoEntidad(plantillaJson.getDestinoCodigo(), entidadActiva.getId());
            if(organismoDestino != null){ // Ya no es vigente
                registroEntrada.setDestino(organismoDestino);
            }
        }

        // Oficina Origen
        // Externa
        if(plantillaJson.getOficinaCodigo()!= null  && !plantillaJson.getOficinaCodigo().equals("-1") && plantillaJson.isOficinaExterna()){// Preguntamos a DIR3 si está Vigente
            Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword());
            OficinaTF oficinaOrigen = oficinasService.obtenerOficina(plantillaJson.getOficinaCodigo(),null,null);
            if(oficinaOrigen != null) {
                if (oficinaOrigen.getEstado().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)) { //Si está vigente, asignamos la oficina al Registro de entrada
                    Oficina oficinaExterna = new Oficina();
                    oficinaExterna.setCodigo(oficinaOrigen.getCodigo());
                    oficinaExterna.setDenominacion(oficinaOrigen.getDenominacion());
                    registroDetalle.setOficinaOrigen(oficinaExterna);
                    oficinasOrigen.add(oficinaExterna); // Añadimos la oficina al listado
                }
            }
            // Interna
        }else{// Comprobamos en REGWEB3 si está vigente
            Oficina oficinaOrigen = oficinaEjb.findByCodigoVigente(plantillaJson.getOficinaCodigo());
            if(oficinaOrigen != null){
                registroDetalle.setOficinaOrigen(oficinaOrigen);
            }
        }

        // Guardam el Registro Detalle
        registroEntrada.setRegistroDetalle(registroDetalle);


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