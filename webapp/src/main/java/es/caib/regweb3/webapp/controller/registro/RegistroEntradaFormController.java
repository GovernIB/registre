package es.caib.regweb3.webapp.controller.registro;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.unidad.Dir3CaibObtenerUnidadesWs;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.PlantillaJson;
import es.caib.regweb3.persistence.ejb.CodigoAsuntoLocal;
import es.caib.regweb3.persistence.ejb.InteresadoLocal;
import es.caib.regweb3.persistence.ejb.MultiEntidadLocal;
import es.caib.regweb3.persistence.ejb.PlantillaLocal;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.Dir3Caib;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.RegistroEntradaWebValidator;
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

    @EJB(mappedName = PlantillaLocal.JNDI_NAME)
    private PlantillaLocal plantillaEjb;

    @EJB(mappedName = CodigoAsuntoLocal.JNDI_NAME)
    private CodigoAsuntoLocal codigoAsuntoEjb;

    @EJB(mappedName = InteresadoLocal.JNDI_NAME)
    private InteresadoLocal interesadoEjb;

    @EJB(mappedName = MultiEntidadLocal.JNDI_NAME)
    private MultiEntidadLocal multiEntidadEjb;


    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.RegistroEntrada} a partir de una {@link Plantilla}
     */
    @RequestMapping(value = "/new/{idPlantilla}", method = RequestMethod.GET)
    public String nuevoRegistroEntradaPlantilla(@PathVariable("idPlantilla") Long idPlantilla, Model model, HttpServletRequest request) throws Exception {

        // Buscamos la Plantilla
        Plantilla plantilla = plantillaEjb.findById(idPlantilla);

        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

        LinkedHashSet<Oficina> oficinasOrigen = new LinkedHashSet<>(getOficinasOrigen(request));

        // Buscamos nuestra oficina activa
        Oficina oficinaActiva = getOficinaActiva(request);
        // Cargamos la entida activa
        Entidad entidadActiva = getEntidadActiva(request);

        //Eliminamos los posibles interesados de la Sesion
        eliminarVariableSesion(request, RegwebConstantes.SESSION_INTERESADOS_ENTRADA);

        // Cargamos los datos de la Plantilla en el Registro Entrada
        RegistroEntrada registroEntrada = cargarPlantillaRegistroEntrada(plantilla, oficinaActiva, entidadActiva, organismosOficinaActiva, oficinasOrigen, request);

        model.addAttribute(entidadActiva);
        model.addAttribute(getUsuarioAutenticado(request));
        model.addAttribute(oficinaActiva);
        model.addAttribute("registroEntrada",registroEntrada);
        organismosOficinaActiva.add(registroEntrada.getDestino());
        model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);
        model.addAttribute("oficinasOrigen", oficinasOrigen);
        model.addAttribute("origenPlantilla", true);
        model.addAttribute("ultimosOrganismos",  registroEntradaConsultaEjb.ultimosOrganismosRegistro(getUsuarioEntidadActivo(request)));

        return "registroEntrada/registroEntradaForm";
    }



    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/new", method = RequestMethod.GET)
    public String nuevoRegistroEntrada(Model model, HttpServletRequest request) throws Exception {

        Oficina oficina = getOficinaActiva(request);

        LinkedHashSet<Oficina> oficinasOrigen = new LinkedHashSet<>(getOficinasOrigen(request));

        RegistroEntrada registroEntrada = new RegistroEntrada();
        registroEntrada.setOficina(oficina);
        registroEntrada.setLibro(getLibroEntidad(request));
        registroEntrada.getRegistroDetalle().setPresencial(true);
        registroEntrada.getRegistroDetalle().setTipoDocumentacionFisica(RegwebConstantes.TIPO_DOCFISICA_NO_ACOMPANYA_DOC);

        //Eliminamos los posibles interesados de la Sesion
        eliminarVariableSesion(request, RegwebConstantes.SESSION_INTERESADOS_ENTRADA);

        model.addAttribute(getEntidadActiva(request));
        model.addAttribute(getUsuarioAutenticado(request));
        model.addAttribute(oficina);
        model.addAttribute("registroEntrada",registroEntrada);
        model.addAttribute("organismosOficinaActiva", getOrganismosOficinaActiva(request));
        model.addAttribute("oficinasOrigen",  oficinasOrigen);
        model.addAttribute("ultimosOrganismos",  registroEntradaConsultaEjb.ultimosOrganismosRegistro(getUsuarioEntidadActivo(request)));

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
        Boolean errorInteresado = true;
        Boolean errorOrganismoExterno = false;

        registroEntradaValidator.validate(registroEntrada, result);
        
        // Comprobamos si el usuario ha añadido algún interesado
        List<Interesado> interesadosSesion = (List<Interesado>) session.getAttribute(RegwebConstantes.SESSION_INTERESADOS_ENTRADA);
        if(interesadosSesion != null && interesadosSesion.size() > 0){
            errorInteresado = false;
        }

        // Comprobamos que el organismo escogido no esté marcado como "externo"
        if(registroEntrada.getDestino() != null && organismoEjb.isExterno(registroEntrada.getDestino().getCodigo(), entidad.getId())){
            errorOrganismoExterno = true;
        }

        if (result.hasErrors() || errorInteresado || errorOrganismoExterno) { // Si hay errores volvemos a la vista del formulario

            // Si no hay ningún interesado, generamos un error.
            if(errorInteresado){
                model.addAttribute("errorInteresado", errorInteresado);
            }

            // Si se ha seleccionado un organismo externo
            if(errorOrganismoExterno){
                model.addAttribute("errorOrganismoExterno", errorOrganismoExterno);
            }

            model.addAttribute(entidad);
            model.addAttribute(getUsuarioAutenticado(request));
            model.addAttribute(getOficinaActiva(request));
            model.addAttribute("ultimosOrganismos",  registroEntradaConsultaEjb.ultimosOrganismosRegistro(getUsuarioEntidadActivo(request)));

            // Organismo destino: Select
            LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

            if (registroEntrada.getDestino() != null) { // Si se ha escogido un Organismo destino

                Organismo organismo = organismoEjb.findByCodigoByEntidadMultiEntidad(registroEntrada.getDestino().getCodigo(), entidad.getId());

                if (organismo == null) {// Si es externo, lo creamos nuevo y lo añadimos a la lista del select
                    organismosOficinaActiva.add(new Organismo(null, registroEntrada.getDestino().getCodigo(), registroEntrada.getDestino().getDenominacion()));
                } else { // si es interno o multientidad  lo añadimos
                    organismosOficinaActiva.add(organismo);
                }
            }

            model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);

            // Oficina Origen: Select
            LinkedHashSet<Oficina> oficinasOrigen = new LinkedHashSet<>(getOficinasOrigen(request));

            if (!registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo().equals("-1")) {// Han indicado oficina de origen

                Oficina oficinaOrigen = oficinaEjb.findByCodigoByEntidadMultiEntidad(registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo(),entidad.getId());
                if (oficinaOrigen == null) { // Es externa
                    oficinasOrigen.add(new Oficina(null, registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo(), registroEntrada.getRegistroDetalle().getOficinaOrigen().getDenominacion()));
                } else { // Es interna o multientidad, la añadimos a la lista por si acaso no está
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
                registroEntrada = registroEntradaEjb.registrarEntrada(registroEntrada, entidad, usuarioEntidad, interesadosSesion, null, false);

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
            registroEntrada = registroEntradaEjb.findByIdCompleto(idRegistro);

            if(validarPermisosEdicion(registroEntrada, request, RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA)){
                return "redirect:/aviso";
            }

            LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

            LinkedHashSet<Oficina> oficinasOrigen = new LinkedHashSet<>(getOficinasOrigen(request));

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

                Organismo organismo;
                if(multiEntidadEjb.isMultiEntidadSir()) {
                    organismo = organismoEjb.findByCodigoMultiEntidad(registroEntrada.getDestino().getCodigo());
                }else{
                    organismo = organismoEjb.findByCodigoEntidadLigero(registroEntrada.getDestino().getCodigo(), entidad.getId());
                }

                if (organismo == null) { // Si es externo, lo creamos nuevo y lo añadimos a la lista del select
                    organismosOficinaActiva.add(new Organismo(null, registroEntrada.getDestino().getCodigo(), registroEntrada.getDestinoExternoDenominacion()));
                    // si es interno o multientidad, miramos si ya esta en la lista, si no, lo añadimos
                } else {
                    organismosOficinaActiva.add(organismo);
                }
            }

            model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);

            // Oficina Origen: Select
            LinkedHashSet<Oficina> oficinasOrigen = new LinkedHashSet<>(getOficinasOrigen(request));

            if (!registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo().equals("-1")) { // Si han indicado OficinaOrigen
                Oficina oficinaOrigen = oficinaEjb.findByCodigoByEntidadMultiEntidad(registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo(), entidad.getId());

                if (oficinaOrigen == null) { // Es externa
                    oficinasOrigen.add(new Oficina(null, registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo(), registroEntrada.getRegistroDetalle().getOficinaOrigenExternoDenominacion()));

                } else { // Es interna o multientidad, la añadimos a la lista por si acaso no está
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

                }else if(registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_RESERVA)){ // Si el Registro de Entrada tiene Estado Pendiente, al editarlo pasa a ser Válido.

                    registroEntrada.setEstado(RegwebConstantes.REGISTRO_VALIDO);
                }

                // Obtenemos el RE antes de guardarlos, para crear el histórico
                RegistroEntrada antiguo = registroEntradaEjb.findByIdCompleto(registroEntrada.getId());

                // Actualizamos el RegistroEntrada
                registroEntrada = registroEntradaEjb.actualizar(antiguo, registroEntrada, entidad, usuarioEntidad);

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

        Entidad entidad = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        RegistroEntrada registroEntradaRectificado;

        try{

            RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFull(idRegistro);

            // Comprobamos si el usuario tiene permisos para registrar el registro rectificado
            if (!permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroEntrada.getOficina().getOrganismoResponsable().getId(), RegwebConstantes.PERMISO_REGISTRO_ENTRADA, true)) {
                Mensaje.saveMessageError(request, I18NUtils.tradueix("aviso.registro.permisos"));
                return "redirect:/registroEntrada/"+idRegistro+"/detalle";
            }

            List<Long> isRectificar = new ArrayList<Long>();
            Collections.addAll(isRectificar, RegwebConstantes.REGISTRO_RECHAZADO, RegwebConstantes.REGISTRO_ANULADO);

            // Si el Registro se puede rectificar y el usuario tiene permisos sobre el libro
            if(isRectificar.contains(registroEntrada.getEstado())){

                registroEntradaRectificado = registroEntradaEjb.rectificar(registroEntrada,entidad, usuarioEntidad);

                Mensaje.saveMessageInfo(request, getMessage("registro.rectificar.ok"));
                return "redirect:/registroEntrada/"+registroEntradaRectificado.getId()+"/detalle";
            }else{

                Mensaje.saveMessageError(request, getMessage("registro.rectificar.no"));
            }
        }catch (I18NException e){
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

        // Organismo destinatario, lo buscamos en función de si es multientidad o no
        Organismo orgDestino = organismoEjb.findByCodigoByEntidadMultiEntidad(registroEntrada.getDestino().getCodigo(),entidad.getId());

        //Organismo destinatario, determinamos si es interno o externo teniendo en cuenta la multientidad
        if( orgDestino == null || (!entidad.getId().equals(orgDestino.getEntidad().getId()))){ //Externo

            registroEntrada.setDestinoExternoCodigo(registroEntrada.getDestino().getCodigo());
            if(registroEntrada.getId()!= null){//es una modificación
                registroEntrada.setDestinoExternoDenominacion(registroEntrada.getDestinoExternoDenominacion());
            }else{
                registroEntrada.setDestinoExternoDenominacion(registroEntrada.getDestino().getDenominacion());
            }

            registroEntrada.setDestino(null);

        }else{//Interno

            registroEntrada.setDestino(orgDestino);
            registroEntrada.setDestinoExternoCodigo(null);
            registroEntrada.setDestinoExternoDenominacion(null);
        }

        // Oficina origen, determinando si es Interno o Externo
        Oficina oficinaOrigen = registroEntrada.getRegistroDetalle().getOficinaOrigen();

        if (oficinaOrigen.getCodigo().equals("-1")) { // No han indicado oficina de origen

            // Asignamos la Oficina donde se realiza el registro
            registroEntrada.getRegistroDetalle().setOficinaOrigen(registroEntrada.getOficina());
            registroEntrada.getRegistroDetalle().setOficinaOrigenExternoCodigo(null);
            registroEntrada.getRegistroDetalle().setOficinaOrigenExternoDenominacion(null);

        } else { // Han indicado oficina origen

            //Oficina ofiOrigen = oficinaEjb.findByCodigoEntidad(oficinaOrigen.getCodigo(), entidad.getId());
            Oficina ofiOrigen =oficinaEjb.findByCodigoByEntidadMultiEntidad(oficinaOrigen.getCodigo(), entidad.getId());

            if (ofiOrigen == null || (!entidad.getId().equals(ofiOrigen.getEntidad().getId())) ) { // Es externa

                registroEntrada.getRegistroDetalle().setOficinaOrigenExternoCodigo(registroEntrada.getRegistroDetalle().getOficinaOrigen().getCodigo());
                if (registroEntrada.getId() != null) {//es una modificación
                    registroEntrada.getRegistroDetalle().setOficinaOrigenExternoDenominacion(registroEntrada.getRegistroDetalle().getOficinaOrigenExternoDenominacion());
                } else {
                    registroEntrada.getRegistroDetalle().setOficinaOrigenExternoDenominacion(registroEntrada.getRegistroDetalle().getOficinaOrigen().getDenominacion());
                }

                registroEntrada.getRegistroDetalle().setOficinaOrigen(null);

            } else {  // es interna

                registroEntrada.getRegistroDetalle().setOficinaOrigen(ofiOrigen);
                registroEntrada.getRegistroDetalle().setOficinaOrigenExternoCodigo(null);
                registroEntrada.getRegistroDetalle().setOficinaOrigenExternoDenominacion(null);
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
    private RegistroEntrada cargarPlantillaRegistroEntrada(Plantilla plantilla, Oficina oficinaActiva, Entidad entidadActiva, LinkedHashSet<Organismo> organismosOficinaActiva, LinkedHashSet<Oficina> oficinasOrigen, HttpServletRequest request) throws Exception{

        Dir3Caib dir3Caib = getLoginInfo(request).getDir3Caib();
        RegistroEntrada registroEntrada = new RegistroEntrada();
        registroEntrada.getRegistroDetalle().setPresencial(true);

        // Recuperamos los valores de la Plantilla
        PlantillaJson plantillaJson = RegistroUtils.desSerilizarPlantillaXml(plantilla.getRepro());

        // Asignamos los valores obtenidos de la Plantilla al Registro de Entrada
        // ID registro Entrada
        registroEntrada.setId(null);
        // Libro
        registroEntrada.setLibro(getLibroEntidad(request));
        // Oficina
        registroEntrada.setOficina(oficinaActiva);
        // Extracto
        registroEntrada.getRegistroDetalle().setExtracto(plantillaJson.getExtracto());
        // Código Asunto
        if(plantillaJson.getIdCodigoAsunto()!=null && !plantillaJson.getIdCodigoAsunto().equals("")) {
            CodigoAsunto codigoAsunto = codigoAsuntoEjb.findById(Long.parseLong(plantillaJson.getIdCodigoAsunto()));
            registroEntrada.getRegistroDetalle().setCodigoAsunto(codigoAsunto);
        }
        // Idioma
        registroEntrada.getRegistroDetalle().setIdioma(Long.parseLong(plantillaJson.getIdIdioma()));
        // Referencia externa
        registroEntrada.getRegistroDetalle().setReferenciaExterna(plantillaJson.getReferenciaExterna());
        // Expediente
        registroEntrada.getRegistroDetalle().setExpediente(plantillaJson.getExpediente());
        // Transporte
        registroEntrada.getRegistroDetalle().setTransporte(Long.parseLong(plantillaJson.getIdTransporte()));
        // Número transporte
        registroEntrada.getRegistroDetalle().setNumeroTransporte(plantillaJson.getNumeroTransporte());
        // Observaciones
        registroEntrada.getRegistroDetalle().setObservaciones(plantillaJson.getObservaciones());
        // Número Registro Origen
        registroEntrada.getRegistroDetalle().setNumeroRegistroOrigen(plantillaJson.getNumeroRegistroOrigen());
        // Código Sia
        if(plantillaJson.getCodigoSia()!=null) {
            if (!plantillaJson.getCodigoSia().isEmpty()) {
                registroEntrada.getRegistroDetalle().setCodigoSia(Long.parseLong(plantillaJson.getCodigoSia()));
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
            session.setAttribute(RegwebConstantes.SESSION_INTERESADOS_ENTRADA, interesados);
        }

        // Fecha origen
        if(!plantillaJson.getFechaOrigen().equals("")) {
            Date fechaOrigen = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(plantillaJson.getFechaOrigen());
            registroEntrada.getRegistroDetalle().setFechaOrigen(fechaOrigen);
        }

        // Comprobamos la unidad destino
        // Externa
        if(plantillaJson.getDestinoCodigo()!= null && plantillaJson.isDestinoExterno()){ // Preguntamos a DIR3 si está Vigente
            Dir3CaibObtenerUnidadesWs unidadesService = Dir3CaibUtils.getObtenerUnidadesService(dir3Caib.getServer(), dir3Caib.getUser(), dir3Caib.getPassword());
            UnidadTF unidad = unidadesService.obtenerUnidad(plantillaJson.getDestinoCodigo(), null, null);
            if(unidad != null) {
                if (unidad.getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)) { //Si está vigente, asignamos la unidad al Registro de entrada
                    Organismo organismoExterno = new Organismo(null,unidad.getCodigo(),unidad.getDenominacion());
                    registroEntrada.setDestino(organismoExterno);
                    organismosOficinaActiva.add(organismoExterno); // Añadimos la unidad al listado
                }
            }
         // Interna
        }else{ // Comprobamos en REGWEB3 si está vigente
            Organismo organismoDestino = organismoEjb.findByCodigoEntidadSinEstado(plantillaJson.getDestinoCodigo(), entidadActiva.getId());
            if(organismoDestino.getEstado().getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)){ // Es vigente
                registroEntrada.setDestino(organismoDestino);
            }else{ // Ya no es vigente
                Set<Organismo> organismoHistoricosFinales = new HashSet<Organismo>();
                organismoEjb.obtenerHistoricosFinales(organismoDestino.getId(), organismoHistoricosFinales);
                Organismo organismoVigente = organismoHistoricosFinales.iterator().next();
                registroEntrada.setDestino(organismoVigente);
                //Guarda el nuevo organismo en la plantilla
                plantillaJson.setDestinoCodigo(organismoVigente.getCodigo());
                plantillaJson.setDestinoDenominacion(organismoVigente.getDenominacion());
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
                    registroEntrada.getRegistroDetalle().setOficinaOrigen(oficinaExterna);
                    oficinasOrigen.add(oficinaExterna); // Añadimos la oficina al listado
                }
            }
            // Interna
        }else{// Comprobamos en REGWEB3 si está vigente
            Oficina oficinaOrigen = oficinaEjb.findByCodigoEntidad(plantillaJson.getOficinaCodigo(),entidadActiva.getId());
            if(oficinaOrigen != null){
                registroEntrada.getRegistroDetalle().setOficinaOrigen(oficinaOrigen);
            }
        }


        return registroEntrada;
    }


    @InitBinder("registroEntrada")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");
        binder.setDisallowedFields("tipoInteresado");
        binder.setDisallowedFields("organismoInteresado");
        binder.setDisallowedFields("fecha");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class, dateEditor);

        binder.setValidator(this.registroEntradaValidator);
    }


}