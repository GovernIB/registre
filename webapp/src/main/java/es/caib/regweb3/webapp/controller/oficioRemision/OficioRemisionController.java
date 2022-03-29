package es.caib.regweb3.webapp.controller.oficioRemision;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.RegistroBasico;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.OficiosRemisionOrganismo;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.sir.core.excepcion.SIRException;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.*;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.OficioRemisionBusquedaValidator;
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
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created 16/07/14 12:52
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb3.model.OficioRemision}
 *
 * @author earrivi
 * @author anadal
 */
@Controller
@RequestMapping(value = "/oficioRemision")
@SessionAttributes(types = OficioRemision.class)
public class OficioRemisionController extends BaseController {

    @Autowired
    private OficioRemisionBusquedaValidator oficioRemisionValidator;

    @EJB(mappedName = OficioRemisionEntradaUtilsLocal.JNDI_NAME)
    private OficioRemisionEntradaUtilsLocal oficioRemisionEntradaUtilsEjb;

    @EJB(mappedName =OficioRemisionSalidaUtilsLocal.JNDI_NAME)
    private OficioRemisionSalidaUtilsLocal oficioRemisionSalidaUtilsEjb;

    @EJB(mappedName = OficioRemisionLocal.JNDI_NAME)
    private OficioRemisionLocal oficioRemisionEjb;

    @EJB(mappedName = ModeloOficioRemisionLocal.JNDI_NAME)
    private ModeloOficioRemisionLocal modeloOficioRemisionEjb;

    @EJB(mappedName = TrazabilidadLocal.JNDI_NAME)
    private TrazabilidadLocal trazabilidadEjb;

    @EJB(mappedName = SirEnvioLocal.JNDI_NAME)
    private SirEnvioLocal sirEnvioEjb;

    @EJB(mappedName = JustificanteLocal.JNDI_NAME)
    private JustificanteLocal justificanteEjb;


    /**
     * Listado de todos los Oficios de Remision
     */
    @RequestMapping(value = "/sinDestino/{tipoOficio}", method = RequestMethod.GET)
    public String oficiosSinREDestino(@PathVariable Long tipoOficio, HttpServletRequest request) throws Exception {

        int contador = 0;

        if(tipoOficio.equals(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA)){

            List<Trazabilidad> trazabilidadesEntrada = trazabilidadEjb.oficiosSinREDestino(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA);

            log.info("Total entradas: " + trazabilidadesEntrada.size());

            for (Trazabilidad t:trazabilidadesEntrada) {

                Long re = registroEntradaConsultaEjb.findByNumeroRegistroOrigen(t.getRegistroEntradaOrigen().getRegistroDetalle().getNumeroRegistroOrigen(), t.getRegistroEntradaOrigen().getId());
                if(re != null){
                    log.info("Registro entrada destino: " + re);
                    trazabilidadEjb.actualizarTrazabilidad(t.getId(),re);
                    contador = contador + 1;
                }

            }

            Mensaje.saveMessageInfo(request,"Se han encontrado "+trazabilidadesEntrada.size()+" trazabilidades y se han solucionado "+contador+" entradas.");

        }else  if(tipoOficio.equals(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA)){

            List<Trazabilidad> trazabilidadesSalida = trazabilidadEjb.oficiosSinREDestino(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA);

            log.info("Total salidas: " + trazabilidadesSalida.size());
            for (Trazabilidad t:trazabilidadesSalida) {

                Long re = registroEntradaConsultaEjb.findByNumeroRegistroOrigen(t.getRegistroSalida().getRegistroDetalle().getNumeroRegistroOrigen(), t.getRegistroSalida().getId());

                if(re != null){
                    log.info("Registro entrada destino: " + re);
                    trazabilidadEjb.actualizarTrazabilidad(t.getId(),re);
                    contador = contador + 1;
                }

            }

            Mensaje.saveMessageInfo(request,"Se han encontrado "+trazabilidadesSalida.size()+" trazabilidades y se han solucionado "+contador+" salidas.");
        }


        return "redirect:/oficioRemision/list";
    }

    /**
     * Listado de todos los Oficios de Remision
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String listado() {
        return "redirect:/oficioRemision/list";
    }

    /**
     * Listado de oficios de remisión
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView list(Model model, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficioRemisionList");

        List<Organismo> organismosConsultaEntrada = getOrganismosConsultaEntrada(request);

        OficioRemisionBusquedaForm oficioRemisionBusqueda = new OficioRemisionBusquedaForm(new OficioRemision(), 1);
        oficioRemisionBusqueda.setIdOrganismo(seleccionarOrganismoActivo(request, organismosConsultaEntrada));
        oficioRemisionBusqueda.setFechaInicio(new Date());
        oficioRemisionBusqueda.setFechaFin(new Date());

        model.addAttribute("estadosOficioRemision", RegwebConstantes.ESTADOS_OFICIO_REMISION);
        model.addAttribute("destinosOficioRemision", RegwebConstantes.DESTINOS_OFICIO_REMISION);
        model.addAttribute("tiposOficioRemision", RegwebConstantes.TIPOS_OFICIO_REMISION);
        model.addAttribute("organismosConsultaEntrada", organismosConsultaEntrada);
        model.addAttribute("oficioRemisionBusqueda", oficioRemisionBusqueda);
        model.addAttribute("usuariosEntidad", usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId()));

        return mav;
    }

    /**
     * Realiza la busqueda de {@link es.caib.regweb3.model.RegistroEntrada} según los parametros del formulario
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ModelAndView list(@ModelAttribute("oficioRemisionBusqueda") OficioRemisionBusquedaForm busqueda, BindingResult result, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficioRemisionList");

        oficioRemisionValidator.validate(busqueda,result);

        OficioRemision oficioRemision = busqueda.getOficioRemision();

        if (result.hasErrors()) {
            mav.addObject("errors", result.getAllErrors());

        } else {
            // Ponemos la hora 23:59 a la fecha fin
            busqueda.setFechaFin(RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin()));

            Paginacion paginacion = oficioRemisionEjb.busqueda(busqueda.getPageNumber(),busqueda.getIdOrganismo(), busqueda.getFechaInicio(), busqueda.getFechaFin(), busqueda.getUsuario(), oficioRemision, busqueda.getDestinoOficioRemision(), busqueda.getEstadoOficioRemision(), busqueda.getTipoOficioRemision(), false);

            busqueda.setPageNumber(1);
            mav.addObject("paginacion", paginacion);

        }

        mav.addObject("organismosConsultaEntrada", getOrganismosConsultaEntrada(request));
        mav.addObject("destinosOficioRemision", RegwebConstantes.DESTINOS_OFICIO_REMISION);
        mav.addObject("tiposOficioRemision", RegwebConstantes.TIPOS_OFICIO_REMISION);
        mav.addObject("estadosOficioRemision", RegwebConstantes.ESTADOS_OFICIO_REMISION);
        mav.addObject("oficioRemisionBusqueda", busqueda);
        mav.addObject("usuariosEntidad", usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId()));
        return mav;

    }

    /**
     * Listado de Registro de Entrada para realizar un Oficio de Remisión Interno
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/entradasPendientesRemision/{tipoEvento}", method = RequestMethod.GET)
    public ModelAndView entradasPendientesRemision(@PathVariable Long tipoEvento, Model model, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficiosEntradaPendientesRemisionList");
        Oficina oficinaActiva = getOficinaActiva(request);

        // Fijamos la oficina activa como la seleccionada por defecto
        RegistroEntrada registroEntrada = new RegistroEntrada();
        registroEntrada.setOficina(oficinaActiva);

        OficioPendienteBusquedaForm oficioPendienteBusquedaForm = new OficioPendienteBusquedaForm(registroEntrada, 1);

        model.addAttribute("organismosDestino", oficioRemisionEntradaUtilsEjb.organismosEntradaPendientesRemisionExternosTipo(oficinaActiva.getId(), tipoEvento, null));
        model.addAttribute("registroEntradaBusqueda", oficioPendienteBusquedaForm);
        model.addAttribute("anys", getAnys());

        return mav;
    }

    /**
     * Realiza la busqueda de Listado de Registro de Entrada para realizar un Oficio de Remisión Interno según los parametros del formulario
     */
    @RequestMapping(value = "/entradasPendientesRemision/{tipoEvento}", method = RequestMethod.POST)
    public ModelAndView entradasPendientesRemision(@ModelAttribute OficioPendienteBusquedaForm busqueda, @PathVariable Long tipoEvento, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficiosEntradaPendientesRemisionList");
        Oficina oficinaActiva = getOficinaActiva(request);
        Entidad entidadActiva = getEntidadActiva(request);

        RegistroEntrada registroEntrada = busqueda.getRegistroEntrada();

        // Obtenemos los Registros de Entrada, pendientes de tramitar por medio de un Oficio de Remisión, agrupados según su Organismos destinatario.
        OficiosRemisionOrganismo oficiosRemisionOrganismo = oficioRemisionEntradaUtilsEjb.oficiosEntradaPendientesRemision(tipoEvento, busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageOficios(entidadActiva.getId()), busqueda.getAnyo(), oficinaActiva,registroEntrada.getDestino().getCodigo(), entidadActiva);

        busqueda.setPageNumber(1);
        mav.addObject("oficiosRemisionOrganismo", oficiosRemisionOrganismo);
        mav.addObject("paginacion", oficiosRemisionOrganismo.getPaginacion());
        mav.addObject("organismosDestino", oficioRemisionEntradaUtilsEjb.organismosEntradaPendientesRemisionExternosTipo(oficinaActiva.getId(), tipoEvento, null));
        mav.addObject("registroEntradaBusqueda", busqueda);
        mav.addObject("oficioRemisionForm", new OficioRemisionForm(RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA));
        mav.addObject("anys", getAnys());

        return mav;
    }

    /**
     * Listado de Registro de Salida para realizar un Oficio de Remisión Interno
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/salidasPendientesRemision/{tipoEvento}", method = RequestMethod.GET)
    public ModelAndView oficiosSalidaPendientesRemision(@PathVariable Long tipoEvento, Model model, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficiosSalidaPendientesRemisionList");
        Oficina oficinaActiva = getOficinaActiva(request);

        // Fijamos la oficina activa como la seleccionada por defecto
        RegistroSalida registroSalida = new RegistroSalida();
        registroSalida.setOficina(oficinaActiva);

        model.addAttribute("organismosDestino", oficioRemisionSalidaUtilsEjb.organismosSalidaPendientesRemisionTipo(oficinaActiva.getId(), tipoEvento, null));
        model.addAttribute("registroSalidaBusqueda", new OficioSalidaPendienteBusquedaForm(registroSalida, 1));
        model.addAttribute("anys", getAnys());


        return mav;
    }

    /**
     * Realiza la busqueda de Listado de Registro de Salida para realizar un Oficio de Remisión según los parametros del formulario
     */
    @RequestMapping(value = "/salidasPendientesRemision/{tipoEvento}", method = RequestMethod.POST)
    public ModelAndView oficiosSalidaPendientesRemision(@ModelAttribute OficioSalidaPendienteBusquedaForm busqueda, @PathVariable Long tipoEvento, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficiosSalidaPendientesRemisionList");
        Oficina oficinaActiva = getOficinaActiva(request);
        Entidad entidadActiva = getEntidadActiva(request);

        RegistroSalida registroSalida = new RegistroSalida();

        // Obtenemos los Registros de Salida, pendientes de tramitar por medio de un Oficio de Revisión, agrupados según su Organismos destinatario.
        OficiosRemisionOrganismo oficiosRemisionOrganismo = oficioRemisionSalidaUtilsEjb.oficiosSalidaPendientesRemision(busqueda.getPageNumber(), busqueda.getAnyo(), oficinaActiva, busqueda.getIdOrganismoOrigen(), busqueda.getDestinatario().getCodigo(), entidadActiva, tipoEvento);

        busqueda.setPageNumber(1);
        mav.addObject("oficiosRemisionOrganismo", oficiosRemisionOrganismo);
        mav.addObject("paginacion", oficiosRemisionOrganismo.getPaginacion());
        mav.addObject("organismosDestino", oficioRemisionSalidaUtilsEjb.organismosSalidaPendientesRemisionTipo(oficinaActiva.getId(), tipoEvento, null));
        mav.addObject("registroSalidaBusqueda", busqueda);
        mav.addObject("oficioRemisionForm", new OficioRemisionForm(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA));
        mav.addObject("anys", getAnys());

        return mav;

    }

    /**
     * Crea un nuevo {@link es.caib.regweb3.model.OficioRemision} a partir de los Registros seleccionados.
     */
    @RequestMapping(value = "/newEntrada", method = RequestMethod.POST)
    public String oficioRemisionEntrada(@ModelAttribute OficioRemisionForm oficioRemisionForm, HttpServletRequest request)
            throws Exception {

        Entidad entidad = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Boolean interno = oficioRemisionForm.getIdOrganismo() != null;
        List<RegistroEntrada> registrosEntrada = new ArrayList<RegistroEntrada>();
        OficioRemision oficioRemision = null;
        List<RegistroEntrada> correctos;

        //Calculamos el evento
        Long evento = interno ? RegwebConstantes.EVENTO_OFICIO_INTERNO : RegwebConstantes.EVENTO_OFICIO_EXTERNO;

        log.info(" ");
        log.info("-------------------------------------------");
        log.info("Registrando Oficio Remision de Entrada");
        log.info(" ");

        try {
            // Comprobamos que el UsuarioActivo pueda crear un Oficio de Remisión
            if (!permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), getOficinaActiva(request).getOrganismoResponsable().getId(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA, true)) {
                Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));
                return "redirect:/oficioRemision/entradasPendientesRemision";
            }

            // Buscamos los registros de entrada seleccionados para crear un Oficio de Remisión
            for (int i = 0; i < oficioRemisionForm.getRegistros().size(); i++) {
                RegistroBasico registro = oficioRemisionForm.getRegistros().get(i);

                if (registro.getId() != null) { //Si se ha seleccionado
                    registrosEntrada.add(new RegistroEntrada(registro.getId()));
                }
            }

            // Comprobamos que al menos haya seleccionado algún RegistroEntrada
            if (registrosEntrada.size() == 0) {
                Mensaje.saveMessageError(request, getMessage("oficioRemision.seleccion"));
                return "redirect:/oficioRemision/entradasPendientesRemision/" + evento;
            }

            // Generamos los Justificantes de todos los Registros seleccionados
            correctos = oficioRemisionEntradaUtilsEjb.crearJustificantesRegistros(entidad, registrosEntrada, usuarioEntidad);

            // Creamos el OficioRemisión con los registros que se ha generado su Justificante
            if (correctos.size() > 0) {
                if (interno) { //Oficio interno
                    log.info("Nuevo organismos sustituto: " + oficioRemisionForm.getIdOrganismo());
                    oficioRemision = oficioRemisionEntradaUtilsEjb.crearOficioRemisionInterno(correctos, entidad,
                            getOficinaActiva(request), usuarioEntidad, oficioRemisionForm.getIdOrganismo(),
                            getLibroEntidad(request).getId());

                } else {//Oficio externo
                    oficioRemision = oficioRemisionEntradaUtilsEjb.crearOficioRemisionExterno(correctos, entidad,
                            getOficinaActiva(request), usuarioEntidad, oficioRemisionForm.getOrganismoExternoCodigo(),
                            oficioRemisionForm.getOrganismoExternoDenominacion(), getLibroEntidad(request).getId());
                }

            } else {
                Mensaje.saveMessageError(request, getMessage("oficioRemision.error.nuevo.justificante"));
                return "redirect:/oficioRemision/entradasPendientesRemision/" + evento;
            }


        } catch (I18NException e) {
            log.error(I18NUtils.getMessage(e), e);
            Mensaje.saveMessageError(request, getMessage("oficioRemision.error.nuevo") + ": " + I18NUtils.getMessage(e));
            return "redirect:/oficioRemision/entradasPendientesRemision/" + evento;
        } catch (I18NValidationException ve) {
            log.error(I18NUtils.getMessage(ve), ve);
            Mensaje.saveMessageError(request, getMessage("oficioRemision.error.nuevo") + ": " + I18NUtils.getMessage(ve));
            return "redirect:/oficioRemision/entradasPendientesRemision/" + evento;
        }

        log.info("");
        log.info("Fin Registrando Oficio Remision de Entrada: " + oficioRemision.getNumeroOficio());
        log.info("-------------------------------------------");
        log.info(" ");

        Mensaje.saveMessageInfo(request, getMessage("oficioRemision.generar.ok"));

        if (correctos.size() != registrosEntrada.size()) {
            Mensaje.saveMessageError(request, getMessage("oficioRemision.generar.incompleto"));
        }

        return "redirect:/oficioRemision/" + oficioRemision.getId() + "/detalle";
    }

    /**
     * Crea un nuevo {@link es.caib.regweb3.model.OficioRemision} a partir de los Registros seleccionados.
     */
    @RequestMapping(value = "/newSalida", method = RequestMethod.POST)
    public String oficioRemisionSalida(@ModelAttribute OficioRemisionForm oficioRemisionForm, HttpServletRequest request)
            throws Exception, I18NException, I18NValidationException {

        Entidad entidad = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Boolean interno = oficioRemisionForm.getIdOrganismo() != null;
        List<RegistroSalida> registrosSalida = new ArrayList<RegistroSalida>();
        OficioRemision oficioRemision = null;
        List<RegistroSalida> correctos;

        //Calculamos el evento
        Long evento = interno ? RegwebConstantes.EVENTO_OFICIO_INTERNO : RegwebConstantes.EVENTO_OFICIO_EXTERNO;

        log.info(" ");
        log.info("-------------------------------------------");
        log.info("Registrando Oficio Remision de Salida");
        log.info(" ");


        try {
            // Comprobamos que el UsuarioActivo pueda crear un Oficio de Remisión
            if (!permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), getOficinaActiva(request).getOrganismoResponsable().getId(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_SALIDA, true)) {
                Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));
                return "redirect:/oficioRemision/salidasPendientesRemision";
            }

            // Buscamos los registros de salida seleccionados para crear un Oficio de Remisión
            for (int i = 0; i < oficioRemisionForm.getRegistros().size(); i++) {
                RegistroBasico registro = oficioRemisionForm.getRegistros().get(i);

                if (registro.getId() != null) { //Si se ha seleccionado
                    registrosSalida.add(new RegistroSalida(registro.getId()));
                }
            }

            // Comprobamos que al menos haya seleccionado algún RegistroSalida
            if (registrosSalida.size() == 0) {
                Mensaje.saveMessageError(request, getMessage("oficioRemision.seleccion"));
                return "redirect:/oficioRemision/salidasPendientesRemision/" + evento;
            }

            // Generamos los Justificantes de todos los Registros seleccionados
            correctos = oficioRemisionSalidaUtilsEjb.crearJustificantesRegistros(entidad, registrosSalida, usuarioEntidad);

            // Creamos el OficioRemisión con los registros que se ha generado su Justificante
            if (correctos.size() > 0) {
                if (interno) { //Oficio interno
                    oficioRemision = oficioRemisionSalidaUtilsEjb.crearOficioRemisionInterno(correctos, entidad,
                            getOficinaActiva(request), usuarioEntidad, oficioRemisionForm.getIdOrganismo(),
                            getLibroEntidad(request).getId());

                } else {//Oficio externo
                    oficioRemision = oficioRemisionSalidaUtilsEjb.crearOficioRemisionExterno(correctos, entidad,
                            getOficinaActiva(request), usuarioEntidad, oficioRemisionForm.getOrganismoExternoCodigo(),
                            oficioRemisionForm.getOrganismoExternoDenominacion(), getLibroEntidad(request).getId());
                }

            } else {
                Mensaje.saveMessageError(request, getMessage("oficioRemision.error.nuevo"));
                return "redirect:/oficioRemision/salidasPendientesRemision/" + evento;
            }


        } catch (I18NException e) {
            log.error(I18NUtils.getMessage(e), e);
            Mensaje.saveMessageError(request, getMessage("oficioRemision.error.nuevo") + ": " + I18NUtils.getMessage(e));
            return "redirect:/oficioRemision/salidasPendientesRemision/" + evento;
        } catch (I18NValidationException ve) {
            log.error(I18NUtils.getMessage(ve), ve);
            Mensaje.saveMessageError(request, getMessage("oficioRemision.error.nuevo") + ": " + I18NUtils.getMessage(ve));
            return "redirect:/oficioRemision/salidasPendientesRemision/" + evento;
        }

        log.info("");
        log.info("Fin Registrando Oficio Remision de Salida: " + oficioRemision.getNumeroOficio());
        log.info("-------------------------------------------");
        log.info(" ");

        Mensaje.saveMessageInfo(request, getMessage("oficioRemision.generar.ok"));

        if (correctos.size() != registrosSalida.size()) {
            Mensaje.saveMessageError(request, getMessage("oficioRemision.generar.incompleto"));
        }

        return "redirect:/oficioRemision/" + oficioRemision.getId() + "/detalle";
    }


    /**
     * Crea un nuevo {@link es.caib.regweb3.model.OficioRemision} hacia SIR a partir de los Registros seleccionados.
     */
    @RequestMapping(value = "/sir", method = RequestMethod.POST)
    public ModelAndView oficioRemisionSir(@ModelAttribute OficioRemisionForm oficioRemisionForm, HttpServletRequest request, Model model) throws Exception {

        Entidad entidad = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        String redirect = "/inici";

        // Comprobamos que la Entidad que envía está en SIR
        Entidad entidadActual = getEntidadActiva(request);
        if (!entidadActual.getSir()) {
            log.error("Aviso: La entidad no está en SIR");
            Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));
            return new ModelAndView("redirect:/oficioRemision/entradasPendientesRemision/4");
        }

        try {

            // OFICIO DE REMISION ENTRADA
            if (RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA.equals(oficioRemisionForm.getTipoOficioRemision())) {

                redirect = "redirect:/oficioRemision/entradasPendientesRemision/4";

                // Comprobamos que el UsuarioActivo pueda crear un Oficio de Remisión
                if (!permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), getOficinaActiva(request).getOrganismoResponsable().getId(),
                        RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA, true)) {
                    log.info("Aviso: No dispone de los permisos necesarios para crear el oficio de remisión de entrada");
                    Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));
                    return new ModelAndView(redirect);
                }

                // Buscamos todos los registros de entrada seleccionados para crear un Oficio de Remisión
                List<RegistroEntrada> registrosEntrada = new ArrayList<RegistroEntrada>();
                for (int i = 0; i < oficioRemisionForm.getRegistros().size(); i++) {
                    RegistroBasico registro = oficioRemisionForm.getRegistros().get(i);

                    if (registro.getId() != null) { // Si se ha seleccionado
                        registrosEntrada.add(new RegistroEntrada(registro.getId()));
                    }
                }

                // Comprobamos que al menos haya seleccionado algún RegistroEntrada
                if (registrosEntrada.size() == 0) {
                    Mensaje.saveMessageError(request, getMessage("oficioRemision.seleccion"));
                    return new ModelAndView(redirect);

                } else { // Creamos el OficioRemisión a partir de los registros de entrada seleccionados.

                    // Para cada Registro de Entrada generamos un oficio de remision SIR.
                    for (RegistroEntrada registroEntradaAEnviar : registrosEntrada) {

                        RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFull(registroEntradaAEnviar.getId());

                        // Crear el Justificante
                        if (!registroEntrada.getRegistroDetalle().getTieneJustificante()) {

                            // Creamos el anexo del justificante y se lo añadimos al registro
                            AnexoFull anexoFull = justificanteEjb.crearJustificante(entidad, usuarioEntidad, registroEntrada, RegwebConstantes.REGISTRO_ENTRADA, Configuracio.getDefaultLanguage());
                            registroEntrada.getRegistroDetalle().getAnexosFull().add(anexoFull);
                        }

                        // Enviamos el Fichero de datos de intercambio al nodo SIR
                        sirEnvioEjb.enviarIntercambio(RegwebConstantes.REGISTRO_ENTRADA, registroEntrada, entidad, getOficinaActiva(request), usuarioEntidad,
                                oficioRemisionForm.getOficinaSIRCodigo());

                    }
                }

                // OFICIO DE REMISION SALIDA
            } else if (RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA.equals(oficioRemisionForm.getTipoOficioRemision())) {

                redirect = "redirect:/oficioRemision/salidasPendientesRemision/4";

                // Comprobamos que el UsuarioActivo pueda crear un Oficio de Remisión
                if (!permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), getOficinaActiva(request).getOrganismoResponsable().getId(),
                        RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_SALIDA, true)) {
                    log.info("Aviso: No dispone de los permisos necesarios para crear el oficio de remisión de salida");
                    Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));
                    return new ModelAndView(redirect);
                }

                // Buscamos todos los registros de salida seleccionados para crear un Oficio de Remisión
                List<RegistroSalida> registrosSalida = new ArrayList<RegistroSalida>();
                for (int i = 0; i < oficioRemisionForm.getRegistros().size(); i++) {
                    RegistroBasico registro = oficioRemisionForm.getRegistros().get(i);

                    if (registro.getId() != null) { // Si se ha seleccionado
                        registrosSalida.add(new RegistroSalida(registro.getId()));
                    }
                }

                // Comprobamos que al menos haya seleccionado algún RegistroSalida
                if (registrosSalida.size() == 0) {
                    Mensaje.saveMessageError(request, getMessage("oficioRemision.seleccion"));
                    return new ModelAndView(redirect);

                } else { // Creamos el OficioRemisión a partir de los registros de salida seleccionados.

                    // Para cada Registro de Salida generamos un oficio de remision SIR.
                    for (RegistroSalida registroSalidaAEnviar : registrosSalida) {

                        RegistroSalida registroSalida = registroSalidaEjb.getConAnexosFull(registroSalidaAEnviar.getId());

                        // Crear el Justificante
                        if (!registroSalida.getRegistroDetalle().getTieneJustificante()) {

                            // Creamos el anexo del justificante y se lo añadimos al registro
                            AnexoFull anexoFull = justificanteEjb.crearJustificante(entidad, usuarioEntidad, registroSalida, RegwebConstantes.REGISTRO_SALIDA, Configuracio.getDefaultLanguage());
                            registroSalida.getRegistroDetalle().getAnexosFull().add(anexoFull);
                        }

                        // Enviamos el Fichero de datos de intercambio al nodo SIR
                        sirEnvioEjb.enviarIntercambio(RegwebConstantes.REGISTRO_SALIDA, registroSalida, entidad, getOficinaActiva(request), usuarioEntidad,
                                oficioRemisionForm.getOficinaSIRCodigo());

                    }
                }
            }

        } catch (SIRException s) {
            log.info(" Error enviant a SIR: " + s.getMessage(), s);
            Mensaje.saveMessageError(request, getMessage("registroSir.error.envio") + ": " + s.getMessage());
            return new ModelAndView(redirect);
        } catch (I18NException e) {
            log.info(" Error enviant a SIR: " + I18NUtils.getMessage(e), e);
            Mensaje.saveMessageError(request, getMessage("registroSir.error.envio") + ": " + e.getMessage());
            return new ModelAndView(redirect);
        } catch (I18NValidationException ve) {
            log.info(" Error enviant a SIR: " + I18NUtils.getMessage(ve), ve);
            Mensaje.saveMessageError(request, getMessage("registroSir.error.envio") + ": " + ve.getMessage());
            return new ModelAndView(redirect);
        }

        Mensaje.saveMessageInfo(request, getMessage("oficioRemision.generar.ok"));

        return new ModelAndView(redirect);
    }

    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.OficioRemision}
     */
    @RequestMapping(value = "/{idOficioRemision}/detalle", method = RequestMethod.GET)
    public String detalleOficioRemision(@PathVariable Long idOficioRemision, Model model, HttpServletRequest request) throws Exception {

        Oficina oficinaActiva = getOficinaActiva(request);
        Entidad entidadActiva = getEntidadActiva(request);

        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

        OficioRemision oficioRemision = oficioRemisionEjb.findById(idOficioRemision);

        // Interno
        if (oficioRemision.getOrganismoDestinatario() != null) {

            // Solo si es la oficna que lo generó o alguna que depende del organismo destinatario del Oficio
            if (!oficioRemision.getOficina().equals(oficinaActiva) && !organismosOficinaActiva.contains(oficioRemision.getOrganismoDestinatario())) {
                log.info("Este OficioRemision no se puede consultar: No se encuentra en la Oficina donde se genero");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.oficioRemision.oficinaActiva"));
                return "redirect:/oficioRemision/list";
            }

        } else { // Externo

            // Si no es la Oficina Activa no se puede consultar
            if (!oficioRemision.getOficina().equals(oficinaActiva)) {
                log.info("Este OficioRemision no se puede consultar: No se encuentra en la Oficina donde se genero");
                Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.oficioRemision.oficinaActiva"));
                return "redirect:/oficioRemision/list";
            }
        }

        //List<RegistroEntrada> registrosEntrada = oficioRemisionEjb.getByOficioRemision(oficioRemision.getId());
        List<Trazabilidad> trazabilidades = trazabilidadEjb.getByOficioRemision(oficioRemision.getId());

        model.addAttribute(oficioRemision);
        model.addAttribute("maxReintentos", PropiedadGlobalUtil.getMaxReintentosSir(entidadActiva.getId()));
        model.addAttribute("trazabilidades", trazabilidades);
        model.addAttribute("isResponsableOrganismo", permisoOrganismoUsuarioEjb.isAdministradorOrganismo(getUsuarioEntidadActivo(request).getId(), oficioRemision.getOficina().getOrganismoResponsable().getId()));
        model.addAttribute("modeloOficioRemision", new ModeloForm());
        model.addAttribute("modelosOficioRemision", modeloOficioRemisionEjb.getByEntidad(getEntidadActiva(request).getId()));

        return "oficioRemision/oficioRemisionDetalle";
    }

    /**
     * Anula un {@link es.caib.regweb3.model.OficioRemision}
     */
    @RequestMapping(value = "/{idOficioRemision}/anular", method = RequestMethod.GET)
    public String anularOficioRemision(@PathVariable Long idOficioRemision, Model model, HttpServletRequest request) throws Exception {

        OficioRemision oficioRemision = oficioRemisionEjb.findById(idOficioRemision);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Oficina oficinaActiva = getOficinaActiva(request);

        // Si no es la Oficina Activa no se puede consultar
        if (!oficioRemision.getOficina().equals(oficinaActiva)) {
            log.info("Este OficioRemision no se puede consultar: No se encuentra en la Oficina donde se genero");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.oficioRemision.oficinaActiva"));
            return "redirect:/oficioRemision/" + idOficioRemision + "/detalle";
        }

        // Comprobación de permisos
        if (permisoOrganismoUsuarioEjb.isAdministradorOrganismo(getUsuarioEntidadActivo(request).getId(), oficioRemision.getOficina().getOrganismoResponsable().getId()) &&
                oficioRemision.getEstado() == RegwebConstantes.OFICIO_INTERNO_ENVIADO || oficioRemision.getEstado() == RegwebConstantes.OFICIO_EXTERNO_ENVIADO) {

            oficioRemisionEjb.anularOficioRemision(idOficioRemision, usuarioEntidad);
            Mensaje.saveMessageInfo(request, getMessage("aviso.oficioRemision.anulado"));

        } else {
            Mensaje.saveMessageError(request, getMessage("aviso.oficioRemision.anular"));
        }

        return "redirect:/oficioRemision/" + idOficioRemision + "/detalle";
    }


    /**
     * Listado de oficios de remisión pendientes de Llegada
     */
    @RequestMapping(value = "/pendientesLlegada/list", method = RequestMethod.GET)
    public ModelAndView oficiosPendientesLlegada(Model model, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficiosPendientesLlegadaList");

        OficioRemisionBusquedaForm oficioRemisionBusquedaForm = new OficioRemisionBusquedaForm(new OficioRemision(), 1);

        model.addAttribute("tiposOficioRemision", RegwebConstantes.TIPOS_OFICIO_REMISION);
        model.addAttribute("oficioRemisionBusqueda", oficioRemisionBusquedaForm);

        return mav;
    }

    /**
     * Listado de oficios de remisión pendientes de Llegada
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pendientesLlegada/list", method = RequestMethod.POST)
    public ModelAndView oficiosPendientesLlegada(@ModelAttribute("oficioRemisionBusqueda") OficioRemisionBusquedaForm busqueda, BindingResult result, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficiosPendientesLlegadaList");

        oficioRemisionValidator.validate(busqueda,result);
        OficioRemision oficioRemision = busqueda.getOficioRemision();

        // Si hay errores volvemos a la vista del formulario
        if (result.hasErrors()) {
            mav.addObject("errors", result.getAllErrors());
        } else {
            Paginacion paginacion = oficioRemisionEjb.oficiosBusqueda(getOrganismosOficinaActiva(request), busqueda.getPageNumber(), oficioRemision, busqueda.getTipoOficioRemision(), RegwebConstantes.OFICIO_INTERNO_ENVIADO);

            busqueda.setPageNumber(1);
            mav.addObject("paginacion", paginacion);
        }

        mav.addObject("tiposOficioRemision", RegwebConstantes.TIPOS_OFICIO_REMISION);
        mav.addObject("oficioRemisionBusqueda", busqueda);

        return mav;
    }

    /**
     * Listado de oficios de remisión aceptados
     */
    @RequestMapping(value = "/aceptados/list", method = RequestMethod.GET)
    public ModelAndView oficiosAceptados(Model model, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficiosAceptadosList");

        OficioRemisionBusquedaForm oficioRemisionBusquedaForm = new OficioRemisionBusquedaForm(new OficioRemision(), 1);

        model.addAttribute("tiposOficioRemision", RegwebConstantes.TIPOS_OFICIO_REMISION);
        model.addAttribute("oficioRemisionBusqueda", oficioRemisionBusquedaForm);

        return mav;
    }

    /**
     * Listado de oficios de remisión aceptados
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/aceptados/list", method = RequestMethod.POST)
    public ModelAndView oficiosAceptados(@ModelAttribute("oficioRemisionBusqueda") OficioRemisionBusquedaForm busqueda, BindingResult result, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficiosAceptadosList");

        oficioRemisionValidator.validate(busqueda,result);
        OficioRemision oficioRemision = busqueda.getOficioRemision();

        // Si hay errores volvemos a la vista del formulario
        if (result.hasErrors()) {
            mav.addObject("errors", result.getAllErrors());
        } else { // Si no hay errores realizamos la búsqueda
            Paginacion paginacion = oficioRemisionEjb.oficiosBusqueda(getOrganismosOficinaActiva(request), busqueda.getPageNumber(), oficioRemision, busqueda.getTipoOficioRemision(), RegwebConstantes.OFICIO_ACEPTADO);

            busqueda.setPageNumber(1);
            mav.addObject("paginacion", paginacion);
        }
        mav.addObject("tiposOficioRemision", RegwebConstantes.TIPOS_OFICIO_REMISION);
        mav.addObject("oficioRemisionBusqueda", busqueda);

        return mav;
    }


    @RequestMapping(value = "/rechazados/list/{pageNumber}")
    public ModelAndView rechazados(@PathVariable Integer pageNumber, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficiosRemisionEstado");

        Oficina oficinaActiva = getOficinaActiva(request);

        if (isOperador(request) && oficinaActiva != null) {

            Paginacion paginacion = oficioRemisionEjb.getByOficinaEstadoPaginado(pageNumber, oficinaActiva.getId(), RegwebConstantes.OFICIO_SIR_RECHAZADO);

            mav.addObject("estado", RegwebConstantes.OFICIO_SIR_RECHAZADO);
            mav.addObject("url", "rechazados");
            mav.addObject("paginacion", paginacion);

        }

        return mav;
    }

    @RequestMapping(value = "/devueltos/list/{pageNumber}")
    public ModelAndView devueltos(@PathVariable Integer pageNumber, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficiosRemisionEstado");

        Oficina oficinaActiva = getOficinaActiva(request);

        if (isOperador(request) && oficinaActiva != null) {

            Paginacion paginacion = oficioRemisionEjb.getByOficinaEstadoPaginado(pageNumber, oficinaActiva.getId(), RegwebConstantes.OFICIO_SIR_DEVUELTO);

            mav.addObject("estado", RegwebConstantes.OFICIO_SIR_DEVUELTO);
            mav.addObject("url", "devueltos");
            mav.addObject("paginacion", paginacion);

        }

        return mav;
    }


    /**
     * Seleccionar un {@link es.caib.regweb3.model.ModeloOficioRemision}
     */
    @RequestMapping(value = "/{idOficioRemision}/imprimir/{idModelo}", method = RequestMethod.GET)
    public ModelAndView imprimirModeloOficio(@PathVariable Long idOficioRemision, @PathVariable Long idModelo, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemisionRtfView");

        OficioRemision oficioRemision = oficioRemisionEjb.findById(idOficioRemision);

        if (RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA.equals(oficioRemision.getTipoOficioRemision())) {
            List<String> registrosEntrada = oficioRemisionEjb.getNumerosRegistroEntradaFormateadoByOficioRemision(oficioRemision.getId());
            mav.addObject("registrosEntrada", registrosEntrada);
        } else if (RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA.equals(oficioRemision.getTipoOficioRemision())) {
            List<String> registrosSalida = oficioRemisionEjb.getNumerosRegistroSalidaFormateadoByOficioRemision(oficioRemision.getId());
            mav.addObject("registrosSalida", registrosSalida);
        }

        mav.addObject("oficioRemision", oficioRemision);
        mav.addObject("modeloOficioRemision", modeloOficioRemisionEjb.findById(idModelo));

        return mav;

    }


    /**
     * Carga el formulario para procesar un {@link es.caib.regweb3.model.OficioRemision}
     */
    @RequestMapping(value = "/{idOficioRemision}/aceptar", method = RequestMethod.GET)
    public String procesarOficioRemision(@PathVariable Long idOficioRemision, Model model, HttpServletRequest request) throws Exception {

        // Libro único
        List<Libro> libros = new ArrayList<>();
        model.addAttribute("libros", libros);

        // Obtenemos los Organismos destinatários donde la Oficina puede registrar
        model.addAttribute("organismosOficinaActiva", getOrganismosOficinaActiva(request));
        model.addAttribute("oficioRemision", oficioRemisionEjb.findById(idOficioRemision));
        model.addAttribute("oficioPendienteLlegadaForm", new OficioPendienteLlegadaForm());
        model.addAttribute("modeloOficioRemision", new ModeloForm());
        model.addAttribute("modelosOficioRemision", modeloOficioRemisionEjb.getByEntidad(getEntidadActiva(request).getId()));

        return "oficioRemision/oficioRemisionAceptar";
    }

    /**
     * Procesa {@link es.caib.regweb3.model.OficioRemision} pendiente de llegada,
     * creando tantos RegistroEntrada como contenga dicho OficioRemision.
     */
    @RequestMapping(value = "/{idOficioRemision}/aceptar", method = RequestMethod.POST)
    public String procesarOficioRemision(@ModelAttribute OficioPendienteLlegadaForm oficioPendienteLlegadaForm, @PathVariable Long idOficioRemision, Model model, HttpServletRequest request)
            throws Exception, I18NException, I18NValidationException {

        OficioRemision oficioRemision = oficioRemisionEjb.findById(idOficioRemision);
        Entidad entidad = getEntidadActiva(request);
        Oficina oficinaActiva = getOficinaActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        List<RegistroEntrada> registrosEntrada = new ArrayList<RegistroEntrada>();

        Set<Organismo> organismosOficinaActiva = getOrganismosOficinaActiva(request);

        // Comprobamos si ya ha sido procesado
        if (!organismosOficinaActiva.contains(oficioRemision.getOrganismoDestinatario()) ||
                oficioRemision.getEstado() != RegwebConstantes.OFICIO_INTERNO_ENVIADO) {
            Mensaje.saveMessageError(request, getMessage("oficioRemision.error.aceptado"));
            return "redirect:/oficioRemision/pendientesLlegada/list";
        }

        // Procesa el Oficio de Remisión
        try {
            if (RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA.equals(oficioRemision.getTipoOficioRemision())) {

                registrosEntrada = oficioRemisionEntradaUtilsEjb.aceptarOficioRemision(oficioRemision,entidad,
                        usuarioEntidad, oficinaActiva, oficioPendienteLlegadaForm.getOficios());

            } else if (RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA.equals(oficioRemision.getTipoOficioRemision())) {

                registrosEntrada = oficioRemisionSalidaUtilsEjb.aceptarOficioRemision(oficioRemision,entidad,
                        usuarioEntidad, oficinaActiva, oficioPendienteLlegadaForm.getOficios());
            }

            model.addAttribute("registrosEntrada", registrosEntrada);

        } catch (Exception e) {
            e.printStackTrace();
            Mensaje.saveMessageError(request, getMessage("oficioRemision.error.aceptando"));
            return "redirect:/oficioRemision/pendientesLlegada/list";
        } catch (I18NException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (I18NValidationException ve) {
            ve.printStackTrace();
            throw ve;
        }

        return "oficioRemision/oficioRemisionAceptado";
    }

    @InitBinder("oficioRemisionBusqueda")
    public void oficioRemisionBusqueda(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        binder.registerCustomEditor(java.util.Date.class, new CustomDateEditor(sdf, true));
        binder.setValidator(this.oficioRemisionValidator);
    }
}

