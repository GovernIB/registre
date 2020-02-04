package es.caib.regweb3.webapp.controller.oficioRemision;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.RegistroBasico;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.OficiosRemisionOrganismo;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.sir.core.excepcion.SIRException;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.*;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

    @EJB(mappedName = "regweb3/OficioRemisionEntradaUtilsEJB/local")
    private OficioRemisionEntradaUtilsLocal oficioRemisionEntradaUtilsEjb;

    @EJB(mappedName = "regweb3/OficioRemisionSalidaUtilsEJB/local")
    private OficioRemisionSalidaUtilsLocal oficioRemisionSalidaUtilsEjb;

    @EJB(mappedName = "regweb3/OficioRemisionEJB/local")
    private OficioRemisionLocal oficioRemisionEjb;

    @EJB(mappedName = "regweb3/ModeloOficioRemisionEJB/local")
    private ModeloOficioRemisionLocal modeloOficioRemisionEjb;

    @EJB(mappedName = "regweb3/TrazabilidadEJB/local")
    private TrazabilidadLocal trazabilidadEjb;

    @EJB(mappedName = "regweb3/SirEnvioEJB/local")
    private SirEnvioLocal sirEnvioEjb;


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

                Long re = registroEntradaConsultaEjb.findByNumeroRegistroOrigen(t.getRegistroEntradaOrigen().getNumeroRegistroFormateado(), t.getRegistroEntradaOrigen().getId());
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

                Long re = registroEntradaConsultaEjb.findByNumeroRegistroOrigen(t.getRegistroSalida().getNumeroRegistroFormateado(), t.getRegistroSalida().getId());

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

        // Obtenemos los Libros donde el usuario tiene permisos de Consulta
        List<Libro> librosConsulta = getLibrosConsultaEntradas(request);

        // Fijamos un libro por defecto
        OficioRemision oficioRemision = new OficioRemision();
        oficioRemision.setLibro(seleccionarLibroOficinaActiva(request, librosConsulta));
        OficioRemisionBusquedaForm oficioRemisionBusqueda = new OficioRemisionBusquedaForm(oficioRemision, 1);
        oficioRemisionBusqueda.setFechaInicio(new Date());
        oficioRemisionBusqueda.setFechaFin(new Date());

        model.addAttribute("estadosOficioRemision", RegwebConstantes.ESTADOS_OFICIO_REMISION);
        model.addAttribute("destinosOficioRemision", RegwebConstantes.DESTINOS_OFICIO_REMISION);
        model.addAttribute("tiposOficioRemision", RegwebConstantes.TIPOS_OFICIO_REMISION);
        model.addAttribute("librosConsulta", librosConsulta);
        model.addAttribute("oficioRemisionBusqueda", oficioRemisionBusqueda);
        model.addAttribute("usuariosEntidad", usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId()));

        return mav;
    }

    /**
     * Realiza la busqueda de {@link es.caib.regweb3.model.RegistroEntrada} según los parametros del formulario
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ModelAndView list(@ModelAttribute OficioRemisionBusquedaForm busqueda, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficioRemisionList");

        OficioRemision oficioRemision = busqueda.getOficioRemision();

        // Obtenemos los Libros donde el usuario tiene permisos de Consulta
        List<Libro> librosConsulta = getLibrosConsultaEntradas(request);

        // Ponemos la hora 23:59 a la fecha fin
        Date fechaFin = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin());

        Paginacion paginacion = oficioRemisionEjb.busqueda(busqueda.getPageNumber(), busqueda.getFechaInicio(), fechaFin, busqueda.getUsuario(), oficioRemision, librosConsulta, busqueda.getDestinoOficioRemision(), busqueda.getEstadoOficioRemision(), busqueda.getTipoOficioRemision(), false);

        busqueda.setPageNumber(1);
        mav.addObject("paginacion", paginacion);
        mav.addObject("destinosOficioRemision", RegwebConstantes.DESTINOS_OFICIO_REMISION);
        mav.addObject("tiposOficioRemision", RegwebConstantes.TIPOS_OFICIO_REMISION);
        mav.addObject("estadosOficioRemision", RegwebConstantes.ESTADOS_OFICIO_REMISION);
        mav.addObject("librosConsulta", librosConsulta);
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
        List<Libro> librosRegistroEntrada = getLibrosRegistroEntrada(request); // Obtenemos los Libros donde el Usuario puede Registrar de la Oficina Activa
        Oficina oficinaActiva = getOficinaActiva(request);

        // Fijamos la oficina activa como la seleccionada por defecto
        RegistroEntrada registroEntrada = new RegistroEntrada();
        registroEntrada.setLibro(seleccionarLibroOficinaActiva(request, librosRegistroEntrada));
        registroEntrada.setOficina(oficinaActiva);

        OficioPendienteBusquedaForm oficioPendienteBusquedaForm = new OficioPendienteBusquedaForm(registroEntrada, 1);

        List<Organismo> organismosDestino = new ArrayList<Organismo>();

        if (tipoEvento.equals(RegwebConstantes.EVENTO_OFICIO_INTERNO)) {
            organismosDestino = oficioRemisionEntradaUtilsEjb.organismosEntradaPendientesRemisionInternos(oficinaActiva.getId(), librosRegistroEntrada, null);

        } else {
            organismosDestino = oficioRemisionEntradaUtilsEjb.organismosEntradaPendientesRemisionExternosTipo(oficinaActiva.getId(), librosRegistroEntrada, tipoEvento, null);
        }

        model.addAttribute("librosRegistro", librosRegistroEntrada);
        model.addAttribute("organismosDestino", organismosDestino);
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
        List<Libro> librosRegistroEntrada = getLibrosRegistroEntrada(request); // Obtenemos los Libros donde el Usuario puede Registrar de la Oficina Activa
        Oficina oficinaActiva = getOficinaActiva(request);
        Entidad entidadActiva = getEntidadActiva(request);

        RegistroEntrada registroEntrada = busqueda.getRegistroEntrada();
        registroEntrada.setEstado(RegwebConstantes.REGISTRO_VALIDO); // Fijamos el Estado válido por defecto

        // Obtenemos los Registros de Entrada, pendientes de tramitar por medio de un Oficio de Remisión, agrupados según su Organismos destinatario.
        OficiosRemisionOrganismo oficiosRemisionOrganismo = oficioRemisionEntradaUtilsEjb.oficiosEntradaPendientesRemision(tipoEvento, busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageOficios(entidadActiva.getId()), busqueda.getAnyo(), oficinaActiva, registroEntrada.getLibro().getId(), registroEntrada.getDestino().getCodigo(), entidadActiva);

        List<Organismo> organismosDestino = new ArrayList<Organismo>();

        if (tipoEvento.equals(RegwebConstantes.EVENTO_OFICIO_INTERNO)) {
            organismosDestino = oficioRemisionEntradaUtilsEjb.organismosEntradaPendientesRemisionInternos(oficinaActiva.getId(), librosRegistroEntrada, null);

        } else {
            organismosDestino = oficioRemisionEntradaUtilsEjb.organismosEntradaPendientesRemisionExternosTipo(oficinaActiva.getId(), librosRegistroEntrada, tipoEvento, null);
        }

        busqueda.setPageNumber(1);
        mav.addObject("oficiosRemisionOrganismo", oficiosRemisionOrganismo);
        mav.addObject("paginacion", oficiosRemisionOrganismo.getPaginacion());
        mav.addObject("librosRegistro", librosRegistroEntrada);
        mav.addObject("organismosDestino", organismosDestino);
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
        List<Libro> librosRegistroSalida = getLibrosRegistroSalida(request); // Obtenemos los Libros donde el Usuario puede Registrar de la Oficina Activa
        Oficina oficinaActiva = getOficinaActiva(request);

        // Fijamos la oficina activa como la seleccionada por defecto
        RegistroSalida registroSalida = new RegistroSalida();
        registroSalida.setLibro(seleccionarLibroOficinaActiva(request, librosRegistroSalida));
        registroSalida.setOficina(oficinaActiva);

        model.addAttribute("librosRegistro", librosRegistroSalida);
        model.addAttribute("organismosDestino", oficioRemisionSalidaUtilsEjb.organismosSalidaPendientesRemisionTipo(oficinaActiva.getId(), librosRegistroSalida, tipoEvento, null));
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
        List<Libro> librosRegistroSalida = getLibrosRegistroSalida(request); // Obtenemos los Libros donde el Usuario puede Registrar de la Oficina Activa
        Oficina oficinaActiva = getOficinaActiva(request);
        Entidad entidadActiva = getEntidadActiva(request);

        RegistroSalida registroSalida = new RegistroSalida();
        registroSalida.setEstado(RegwebConstantes.REGISTRO_VALIDO); // Fijamos el Estado válido por defecto

        // Obtenemos los Registros de Salida, pendientes de tramitar por medio de un Oficio de Revisión, agrupados según su Organismos destinatario.
        OficiosRemisionOrganismo oficiosRemisionOrganismo = oficioRemisionSalidaUtilsEjb.oficiosSalidaPendientesRemision(busqueda.getPageNumber(), busqueda.getAnyo(), oficinaActiva, busqueda.getRegistroSalida().getLibro().getId(), busqueda.getDestinatario().getCodigo(), entidadActiva, tipoEvento);

        busqueda.setPageNumber(1);
        mav.addObject("oficiosRemisionOrganismo", oficiosRemisionOrganismo);
        mav.addObject("paginacion", oficiosRemisionOrganismo.getPaginacion());
        mav.addObject("librosRegistro", librosRegistroSalida);
        mav.addObject("organismosDestino", oficioRemisionSalidaUtilsEjb.organismosSalidaPendientesRemisionTipo(oficinaActiva.getId(), librosRegistroSalida, tipoEvento, null));
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
            throws Exception, I18NException, I18NValidationException {

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
            if (!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), oficioRemisionForm.getIdLibro(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA, true)) {
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
            correctos = oficioRemisionEntradaUtilsEjb.crearJustificantesRegistros(registrosEntrada, usuarioEntidad);

            // Creamos el OficioRemisión con los registros que se ha generado su Justificante
            if (correctos.size() > 0) {
                if (interno) { //Oficio interno
                    log.info("Nuevo organismos sustituto: " + oficioRemisionForm.getIdOrganismo());
                    oficioRemision = oficioRemisionEntradaUtilsEjb.crearOficioRemisionInterno(correctos,
                            getOficinaActiva(request), usuarioEntidad, oficioRemisionForm.getIdOrganismo(),
                            oficioRemisionForm.getIdLibro());

                } else {//Oficio externo
                    oficioRemision = oficioRemisionEntradaUtilsEjb.crearOficioRemisionExterno(correctos,
                            getOficinaActiva(request), usuarioEntidad, oficioRemisionForm.getOrganismoExternoCodigo(),
                            oficioRemisionForm.getOrganismoExternoDenominacion(), oficioRemisionForm.getIdLibro());
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
            if (!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), oficioRemisionForm.getIdLibro(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_SALIDA, true)) {
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
            correctos = oficioRemisionSalidaUtilsEjb.crearJustificantesRegistros(registrosSalida, usuarioEntidad);

            // Creamos el OficioRemisión con los registros que se ha generado su Justificante
            if (correctos.size() > 0) {
                if (interno) { //Oficio interno
                    oficioRemision = oficioRemisionSalidaUtilsEjb.crearOficioRemisionInterno(correctos,
                            getOficinaActiva(request), usuarioEntidad, oficioRemisionForm.getIdOrganismo(),
                            oficioRemisionForm.getIdLibro());

                } else {//Oficio externo
                    oficioRemision = oficioRemisionSalidaUtilsEjb.crearOficioRemisionExterno(correctos,
                            getOficinaActiva(request), usuarioEntidad, oficioRemisionForm.getOrganismoExternoCodigo(),
                            oficioRemisionForm.getOrganismoExternoDenominacion(), oficioRemisionForm.getIdLibro());
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

        List<OficioRemision> oficioRemisionList = new ArrayList<OficioRemision>();
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

                redirect = "redirect:/oficioRemision/entradasPendientesRemision";

                // Comprobamos que el UsuarioActivo pueda crear un Oficio de Remisión
                if (!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), oficioRemisionForm.getIdLibro(),
                        RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA, true)) {
                    log.info("Aviso: No dispone de los permisos necesarios para crear el oficio de remisión de entrada");
                    Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));
                    return new ModelAndView("redirect:/oficioRemision/entradasPendientesRemision/4");
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
                    return new ModelAndView("redirect:/oficioRemision/entradasPendientesRemision/4");

                } else { // Creamos el OficioRemisión a partir de los registros de entrada seleccionados.

                    // Para cada Registro de Entrada generamos un oficio de remision SIR.
                    for (RegistroEntrada registroEntradaAEnviar : registrosEntrada) {

                        // Enviamos el Fichero de datos de intercambio al nodo SIR
                        OficioRemision oficioRemision = sirEnvioEjb.enviarFicheroIntercambio(
                                RegwebConstantes.REGISTRO_ENTRADA, registroEntradaAEnviar.getId(),
                                getOficinaActiva(request), usuarioEntidad, oficioRemisionForm.getOficinaSIRCodigo());

                        oficioRemisionList.add(oficioRemision);
                    }
                }

                // OFICIO DE REMISION SALIDA
            } else if (RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA.equals(oficioRemisionForm.getTipoOficioRemision())) {

                redirect = "redirect:/oficioRemision/salidasPendientesRemision";

                // Comprobamos que el UsuarioActivo pueda crear un Oficio de Remisión
                if (!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), oficioRemisionForm.getIdLibro(),
                        RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_SALIDA, true)) {
                    log.info("Aviso: No dispone de los permisos necesarios para crear el oficio de remisión de salida");
                    Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));
                    return new ModelAndView("redirect:/oficioRemision/salidasPendientesRemision/4");
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
                    return new ModelAndView("redirect:/oficioRemision/salidasPendientesRemision/4");

                } else { // Creamos el OficioRemisión a partir de los registros de salida seleccionados.

                    // Para cada Registro de Salida generamos un oficio de remision SIR.
                    for (RegistroSalida registroSalidaAEnviar : registrosSalida) {

                        // Enviamos el Fichero de datos de intercambio al nodo SIR
                        OficioRemision oficioRemision = sirEnvioEjb.enviarFicheroIntercambio(
                                RegwebConstantes.REGISTRO_SALIDA, registroSalidaAEnviar.getId(),
                                getOficinaActiva(request), usuarioEntidad, oficioRemisionForm.getOficinaSIRCodigo());

                        oficioRemisionList.add(oficioRemision);
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

        // TODO Missatge
        if (oficioRemisionList.size() == 0) {
            return new ModelAndView(redirect);
        } else {

            if (oficioRemisionList.size() == 1) {
                return new ModelAndView("redirect:/oficioRemision/" + oficioRemisionList.get(0).getId() + "/detalle");
            } else {
                //Model model, HttpServletRequest request)throws Exception {

                ModelAndView mav = new ModelAndView("oficioRemision/oficioRemisionListSir");

                Paginacion paginacion = new Paginacion(oficioRemisionList.size(), 1);

                paginacion.setListado(new ArrayList<Object>(oficioRemisionList));

                mav.addObject("paginacion", paginacion);
                model.addAttribute("paginacion", paginacion);
                //model.addAttribute("oficioRemisionBusqueda", new OficioRemisionBusquedaForm());

                return mav;
            }
        }
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
        model.addAttribute("isAdministradorLibro", permisoLibroUsuarioEjb.isAdministradorLibro(getUsuarioEntidadActivo(request).getId(), oficioRemision.getLibro().getId()));
        model.addAttribute("modeloOficioRemision", new ModeloForm());
        model.addAttribute("modelosOficioRemision", modeloOficioRemisionEjb.getByEntidad(getEntidadActiva(request).getId()));

        return "oficioRemision/oficioRemisionDetalle";
    }

    /**
     * Anula un {@link es.caib.regweb3.model.OficioRemision}
     */
    @RequestMapping(value = "/{idOficioRemision}/anular", method = RequestMethod.GET)
    public String anularOficioRemision(@PathVariable Long idOficioRemision, Model model, HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
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
        if (permisoLibroUsuarioEjb.isAdministradorLibro(getUsuarioEntidadActivo(request).getId(), oficioRemision.getLibro().getId()) &&
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
        model.addAttribute("anys", getAnys());

        return mav;
    }

    /**
     * Listado de oficios de remisión pendientes de Llegada
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pendientesLlegada/list", method = RequestMethod.POST)
    public ModelAndView oficiosPendientesLlegada(@ModelAttribute OficioRemisionBusquedaForm busqueda, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficiosPendientesLlegadaList");

        OficioRemision oficioRemision = busqueda.getOficioRemision();

        Paginacion paginacion = oficioRemisionEjb.oficiosBusqueda(getOrganismosOficinaActiva(request), busqueda.getPageNumber(), oficioRemision, busqueda.getTipoOficioRemision(), RegwebConstantes.OFICIO_INTERNO_ENVIADO);

        busqueda.setPageNumber(1);
        mav.addObject("paginacion", paginacion);
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
    public ModelAndView oficiosAceptados(@ModelAttribute OficioRemisionBusquedaForm busqueda, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficiosAceptadosList");

        OficioRemision oficioRemision = busqueda.getOficioRemision();

        Paginacion paginacion = oficioRemisionEjb.oficiosBusqueda(getOrganismosOficinaActiva(request), busqueda.getPageNumber(), oficioRemision, busqueda.getTipoOficioRemision(), RegwebConstantes.OFICIO_ACEPTADO);

        busqueda.setPageNumber(1);
        mav.addObject("paginacion", paginacion);
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

        ModelAndView mav = new ModelAndView("oficioRemision");

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

        OficioRemision oficioRemision = oficioRemisionEjb.findById(idOficioRemision);
        model.addAttribute("oficioRemision", oficioRemision);

        // Obtenemos los libros donde el UsuarioEntidad puede registrar
        model.addAttribute("libros", getLibrosRegistroEntrada(request));

        // Obtenemos los Organismos destinatários donde la Oficina puede registrar
        model.addAttribute("organismosOficinaActiva", getOrganismosOficinaActiva(request));

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
    public String procesarOficioRemision(
            @ModelAttribute OficioPendienteLlegadaForm oficioPendienteLlegadaForm,
            @PathVariable Long idOficioRemision, Model model, HttpServletRequest request)
            throws Exception, I18NException, I18NValidationException {

        OficioRemision oficioRemision = oficioRemisionEjb.findById(idOficioRemision);
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

                registrosEntrada = oficioRemisionEntradaUtilsEjb.aceptarOficioRemision(oficioRemision,
                        usuarioEntidad, oficinaActiva, oficioPendienteLlegadaForm.getOficios());

            } else if (RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA.equals(oficioRemision.getTipoOficioRemision())) {

                registrosEntrada = oficioRemisionSalidaUtilsEjb.aceptarOficioRemision(oficioRemision,
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
    }

}

