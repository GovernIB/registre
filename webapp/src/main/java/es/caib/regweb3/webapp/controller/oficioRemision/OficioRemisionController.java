package es.caib.regweb3.webapp.controller.oficioRemision;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.RegistroBasico;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.OficiosRemisionOrganismo;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.sir.core.excepcion.SIRException;
import es.caib.regweb3.utils.Dir3CaibUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.*;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

    @EJB(mappedName = "regweb3/SirEJB/local")
    private SirLocal sirEjb;



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
        OficioRemisionBusquedaForm oficioRemisionBusquedaForm = new OficioRemisionBusquedaForm(oficioRemision, 1);

        model.addAttribute("estadosOficioRemision", RegwebConstantes.ESTADOS_OFICIO_REMISION);
        model.addAttribute("destinosOficioRemision", RegwebConstantes.DESTINOS_OFICIO_REMISION);
        model.addAttribute("tiposOficioRemision", RegwebConstantes.TIPOS_OFICIO_REMISION);
        model.addAttribute("librosConsulta", librosConsulta);
        model.addAttribute("oficioRemisionBusqueda", oficioRemisionBusquedaForm);
        model.addAttribute("anys", getAnys());

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

        Paginacion paginacion = oficioRemisionEjb.busqueda(busqueda.getPageNumber(), busqueda.getAnyo(), oficioRemision, librosConsulta, busqueda.getDestinoOficioRemision(), busqueda.getEstadoOficioRemision(), busqueda.getTipoOficioRemision());

        busqueda.setPageNumber(1);
        mav.addObject("paginacion", paginacion);
        mav.addObject("destinosOficioRemision", RegwebConstantes.DESTINOS_OFICIO_REMISION);
        mav.addObject("tiposOficioRemision", RegwebConstantes.TIPOS_OFICIO_REMISION);
        mav.addObject("estadosOficioRemision", RegwebConstantes.ESTADOS_OFICIO_REMISION);
        mav.addObject("librosConsulta", librosConsulta);
        mav.addObject("oficioRemisionBusqueda", busqueda);
        mav.addObject("anys", getAnys());

        return mav;

    }

    /**
     * Listado de Registro de Entrada para realizar un Oficio de Remisión Interno
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/entradasPendientesRemision", method = RequestMethod.GET)
    public ModelAndView entradasPendientesRemision(Model model, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficiosEntradaPendientesRemisionList");
        List<Libro> librosRegistroEntrada = getLibrosRegistroEntrada(request); // Obtenemos los Libros donde el Usuario puede Registrar de la Oficina Activa
        Oficina oficinaActiva = getOficinaActiva(request);

        // Fijamos la oficina activa como la seleccionada por defecto
        RegistroEntrada registroEntrada = new RegistroEntrada();
        registroEntrada.setLibro(seleccionarLibroOficinaActiva(request, librosRegistroEntrada));
        registroEntrada.setOficina(oficinaActiva);

        OficioPendienteBusquedaForm oficioPendienteBusquedaForm = new OficioPendienteBusquedaForm(registroEntrada, 1);

        model.addAttribute("librosRegistro", librosRegistroEntrada);
        model.addAttribute("organismosDestino", oficioRemisionEntradaUtilsEjb.organismosEntradaPendientesRemision(oficinaActiva.getId(), librosRegistroEntrada, getOrganismosOficioRemision(request, getOrganismosOficinaActiva(request))));
        model.addAttribute("registroEntradaBusqueda", oficioPendienteBusquedaForm);
        model.addAttribute("anys", getAnys());


        return mav;
    }

    /**
     * Realiza la busqueda de Listado de Registro de Entrada para realizar un Oficio de Remisión Interno según los parametros del formulario
     */
    @RequestMapping(value = "/entradasPendientesRemision", method = RequestMethod.POST)
    public ModelAndView entradasPendientesRemision(@ModelAttribute OficioPendienteBusquedaForm busqueda, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficiosEntradaPendientesRemisionList");
        List<Libro> librosRegistroEntrada = getLibrosRegistroEntrada(request); // Obtenemos los Libros donde el Usuario puede Registrar de la Oficina Activa
        Oficina oficinaActiva = getOficinaActiva(request);
        Entidad entidadActiva = getEntidadActiva(request);

        RegistroEntrada registroEntrada = busqueda.getRegistroEntrada();
        registroEntrada.setEstado(RegwebConstantes.REGISTRO_VALIDO); // Fijamos el Estado válido por defecto
        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

        // Obtenemos los Registros de Entrada, pendientes de tramitar por medio de un Oficio de Revisión, agrupados según su Organismos destinatario.
        OficiosRemisionOrganismo oficiosRemisionOrganismo = oficioRemisionEntradaUtilsEjb.oficiosEntradaPendientesRemision(busqueda.getPageNumber(), PropiedadGlobalUtil.getResultsPerPageOficios(entidadActiva.getId()), busqueda.getAnyo(), oficinaActiva, registroEntrada.getLibro().getId(), registroEntrada.getDestino().getCodigo(), getOrganismosOficioRemision(request, organismosOficinaActiva), entidadActiva);

        busqueda.setPageNumber(1);
        mav.addObject("oficiosRemisionOrganismo", oficiosRemisionOrganismo);
        mav.addObject("paginacion", oficiosRemisionOrganismo.getPaginacion());
        mav.addObject("librosRegistro", librosRegistroEntrada);
        mav.addObject("organismosDestino", oficioRemisionEntradaUtilsEjb.organismosEntradaPendientesRemision(oficinaActiva.getId(), librosRegistroEntrada, getOrganismosOficioRemision(request, getOrganismosOficinaActiva(request))));
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
    @RequestMapping(value = "/salidasPendientesRemision", method = RequestMethod.GET)
    public ModelAndView oficiosSalidaPendientesRemision(Model model, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficiosSalidaPendientesRemisionList");
        List<Libro> librosRegistroSalida = getLibrosRegistroSalida(request); // Obtenemos los Libros donde el Usuario puede Registrar de la Oficina Activa
        Oficina oficinaActiva = getOficinaActiva(request);

        // Fijamos la oficina activa como la seleccionada por defecto
        RegistroSalida registroSalida = new RegistroSalida();
        registroSalida.setLibro(seleccionarLibroOficinaActiva(request, librosRegistroSalida));
        registroSalida.setOficina(oficinaActiva);

        model.addAttribute("librosRegistro", librosRegistroSalida);
        model.addAttribute("organismosDestino", oficioRemisionSalidaUtilsEjb.organismosSalidaPendientesRemision(oficinaActiva.getId(), librosRegistroSalida, getOrganismosOficioRemisionSalida(getOrganismosOficinaActiva(request))));
        model.addAttribute("registroSalidaBusqueda", new OficioSalidaPendienteBusquedaForm(registroSalida, 1));
        model.addAttribute("anys", getAnys());


        return mav;
    }

    /**
     * Realiza la busqueda de Listado de Registro de Salida para realizar un Oficio de Remisión según los parametros del formulario
     */
    @RequestMapping(value = "/salidasPendientesRemision", method = RequestMethod.POST)
    public ModelAndView oficiosSalidaPendientesRemision(@ModelAttribute OficioSalidaPendienteBusquedaForm busqueda, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficiosSalidaPendientesRemisionList");
        List<Libro> librosRegistroEntrada = getLibrosRegistroSalida(request); // Obtenemos los Libros donde el Usuario puede Registrar de la Oficina Activa
        Oficina oficinaActiva = getOficinaActiva(request);
        Entidad entidadActiva = getEntidadActiva(request);

        RegistroSalida registroSalida = new RegistroSalida();
        registroSalida.setEstado(RegwebConstantes.REGISTRO_VALIDO); // Fijamos el Estado válido por defecto
        //LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

        // Obtenemos los Registros de Salida, pendientes de tramitar por medio de un Oficio de Revisión, agrupados según su Organismos destinatario.
        OficiosRemisionOrganismo oficiosRemisionOrganismo = oficioRemisionSalidaUtilsEjb.oficiosSalidaPendientesRemision(busqueda.getPageNumber(), busqueda.getAnyo(), oficinaActiva, busqueda.getRegistroSalida().getLibro().getId(), busqueda.getDestinatario().getCodigo(), entidadActiva);

        busqueda.setPageNumber(1);
        mav.addObject("oficiosRemisionOrganismo", oficiosRemisionOrganismo);
        mav.addObject("paginacion", oficiosRemisionOrganismo.getPaginacion());
        mav.addObject("librosRegistro", librosRegistroEntrada);
        mav.addObject("organismosDestino", oficioRemisionSalidaUtilsEjb.organismosSalidaPendientesRemision(oficinaActiva.getId(), librosRegistroEntrada, getOrganismosOficioRemisionSalida(getOrganismosOficinaActiva(request))));
        mav.addObject("registroSalidaBusqueda", busqueda);
        mav.addObject("oficioRemisionForm", new OficioRemisionForm(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA));
        mav.addObject("anys", getAnys());

        return mav;

    }

    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.OficioRemision}
     */
    @RequestMapping(value = "/{idOficioRemision}/detalle", method = RequestMethod.GET)
    public String detalleOficioRemision(@PathVariable Long idOficioRemision, Model model, HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        Oficina oficinaActiva = (Oficina) session.getAttribute(RegwebConstantes.SESSION_OFICINA);

        OficioRemision oficioRemision = oficioRemisionEjb.findById(idOficioRemision);

        // Si no es la Oficina Activa no se puede consultar
        if(!oficioRemision.getOficina().equals(oficinaActiva)){
            log.info("Este OficioRemision no se puede consultar: No se encuentra en la Oficina donde se genero");
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.oficioRemision.oficinaActiva"));
            return "redirect:/oficioRemision/list";
        }


        //List<RegistroEntrada> registrosEntrada = oficioRemisionEjb.getByOficioRemision(oficioRemision.getId());
        List<Trazabilidad> trazabilidades = trazabilidadEjb.getByOficioRemision(oficioRemision.getId());

        model.addAttribute(oficioRemision);
        //model.addAttribute("registrosEntrada",registrosEntrada);
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

        OficioRemision oficioRemision = oficioRemisionEjb.findById(idOficioRemision);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        if (permisoLibroUsuarioEjb.isAdministradorLibro(getUsuarioEntidadActivo(request).getId(), oficioRemision.getLibro().getId()) &&
                oficioRemision.getEstado() == RegwebConstantes.OFICIO_INTERNO || oficioRemision.getEstado() == RegwebConstantes.OFICIO_EXTERNO) {

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

        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

        Paginacion paginacion = oficioRemisionEjb.oficiosPendientesLlegadaBusqueda(organismosOficinaActiva, busqueda.getPageNumber(), oficioRemision, busqueda.getTipoOficioRemision());

        busqueda.setPageNumber(1);
        mav.addObject("paginacion", paginacion);
        mav.addObject("tiposOficioRemision", RegwebConstantes.TIPOS_OFICIO_REMISION);
        mav.addObject("oficioRemisionBusqueda", busqueda);

        return mav;
    }


    /**
     * Crea un nuevo {@link es.caib.regweb3.model.OficioRemision} a partir de los Registros seleccionados.
     */
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public String oficioRemision(@ModelAttribute OficioRemisionForm oficioRemisionForm, HttpServletRequest request)
            throws Exception, I18NException, I18NValidationException {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Boolean interno = oficioRemisionForm.getIdOrganismo() != null;
        List<RegistroEntrada> registrosEntrada = new ArrayList<RegistroEntrada>();
        List<RegistroSalida> registrosSalida = new ArrayList<RegistroSalida>();
        OficioRemision oficioRemision = null;

        // OFICIO DE REMISION ENTRADA
        if (RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA.equals(oficioRemisionForm.getTipoOficioRemision())) {

            // Comprobamos que el UsuarioActivo pueda crear un Oficio de Remisión
            if (!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), oficioRemisionForm.getIdLibro(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA)) {
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
                return "redirect:/oficioRemision/entradasPendientesRemision";
            }

            // Creamos el OficioRemisión a partir de los registros de entrada seleccionados.
            if (interno) { //Oficio interno
                oficioRemision = oficioRemisionEntradaUtilsEjb.crearOficioRemisionInterno(registrosEntrada,
                        getOficinaActiva(request), usuarioEntidad, oficioRemisionForm.getIdOrganismo(),
                        oficioRemisionForm.getIdLibro());

            } else {//Oficio externo todo: Acabar oficio remisión externo

                oficioRemision = oficioRemisionEntradaUtilsEjb.crearOficioRemisionExterno(registrosEntrada,
                        getOficinaActiva(request), usuarioEntidad, oficioRemisionForm.getOrganismoExternoCodigo(),
                        oficioRemisionForm.getOrganismoExternoDenominacion(), oficioRemisionForm.getIdLibro());
            }

            // OFICIO DE REMISION SALIDA
        } else if (RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA.equals(oficioRemisionForm.getTipoOficioRemision())) {

            // Comprobamos que el UsuarioActivo pueda crear un Oficio de Remisión
            if (!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), oficioRemisionForm.getIdLibro(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_SALIDA)) {
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
                return "redirect:/oficioRemision/salidasPendientesRemision";
            }

            // Creamos el OficioRemisión a partir de los registros de entrada seleccionados.
            if (interno) { //Oficio interno
                oficioRemision = oficioRemisionSalidaUtilsEjb.crearOficioRemisionInterno(registrosSalida,
                        getOficinaActiva(request), usuarioEntidad, oficioRemisionForm.getIdOrganismo(),
                        oficioRemisionForm.getIdLibro());

            } else {//Oficio externo todo: Acabar oficio remisión externo

                oficioRemision = oficioRemisionSalidaUtilsEjb.crearOficioRemisionExterno(registrosSalida,
                        getOficinaActiva(request), usuarioEntidad, oficioRemisionForm.getOrganismoExternoCodigo(),
                        oficioRemisionForm.getOrganismoExternoDenominacion(), oficioRemisionForm.getIdLibro());
            }

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

        // Comprobamos que la Entidad que envía está en SIR
        Entidad entidadActual = getEntidadActiva(request);
        if (!entidadActual.getSir()) {
            log.error("Aviso: La entidad no está en SIR");
            Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));
            return new ModelAndView("redirect:/oficioRemision/entradasPendientesRemision");
        }

        // OficinaSir destino
        Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService();
        OficinaTF oficinaSir = oficinasService.obtenerOficina(oficioRemisionForm.getOficinaSIRCodigo(), null, null);

        // OFICIO DE REMISION ENTRADA
        if (RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA.equals(oficioRemisionForm.getTipoOficioRemision())) {

            log.debug("Entra Dins oficioRemisionSir ENTRADA");

            try {

                // Comprobamos que el UsuarioActivo pueda crear un Oficio de Remisión
                if (!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), oficioRemisionForm.getIdLibro(),
                        RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA)) {
                    log.info("Aviso: No dispone de los permisos necesarios para crear el oficio de remisión de entrada");
                    Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));
                    return new ModelAndView("redirect:/oficioRemision/entradasPendientesRemision");
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
                    return new ModelAndView("redirect:/oficioRemision/entradasPendientesRemision");

                } else { // Creamos el OficioRemisión a partir de los registros de entrada seleccionados.

                    // Para cada Registro de Entrada generamos un oficio de remision SIR.
                    for (RegistroEntrada registroEntradaAEnviar : registrosEntrada) {

                        // Enviamos el Fichero de datos de intercambio al nodo SIR
                        OficioRemision oficioRemision = sirEjb.enviarFicheroIntercambio(RegwebConstantes.REGISTRO_ENTRADA_ESCRITO,registroEntradaAEnviar.getId(), oficinaSir.getCodigo(),oficinaSir.getDenominacion(), getOficinaActiva(request), usuarioEntidad, oficioRemisionForm.getIdLibro());

                        oficioRemisionList.add(oficioRemision);
                    }
                }

            } catch (SIRException s) {
                log.error(" Error enviant a SIR: " + s.getMessage(), s);
                Mensaje.saveMessageError(request, getMessage("registroSir.error.envio"));
                return new ModelAndView("redirect:/oficioRemision/entradasPendientesRemision");

            } catch (I18NException e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("registroSir.error.envio"));
                return new ModelAndView("redirect:/oficioRemision/entradasPendientesRemision");
            }

            // OFICIO DE REMISION SALIDA
        } else if (RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA.equals(oficioRemisionForm.getTipoOficioRemision())) {

            log.debug("Entra Dins oficioRemisionSir SALIDA");

            try {

                // Comprobamos que el UsuarioActivo pueda crear un Oficio de Remisión
                if (!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), oficioRemisionForm.getIdLibro(),
                        RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_SALIDA)) {
                    log.info("Aviso: No dispone de los permisos necesarios para crear el oficio de remisión de salida");
                    Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));
                    return new ModelAndView("redirect:/oficioRemision/salidasPendientesRemision");
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
                    return new ModelAndView("redirect:/oficioRemision/salidasPendientesRemision");

                } else { // Creamos el OficioRemisión a partir de los registros de salida seleccionados.


                    // Para cada Registro de Salida generamos un oficio de remision SIR.
                    for (RegistroSalida registroSalidaAEnviar : registrosSalida) {

                        // Enviamos el Fichero de datos de intercambio al nodo SIR
                        OficioRemision oficioRemision = sirEjb.enviarFicheroIntercambio(RegwebConstantes.REGISTRO_SALIDA_ESCRITO,registroSalidaAEnviar.getId(), oficinaSir.getCodigo(),oficinaSir.getDenominacion(), getOficinaActiva(request), usuarioEntidad, oficioRemisionForm.getIdLibro());

                        oficioRemisionList.add(oficioRemision);
                    }
                }


            } catch (SIRException s) {
                log.error(" Error enviant a SIR: " + s.getMessage(), s);
                Mensaje.saveMessageError(request, getMessage("registroSir.error.envio"));
                return new ModelAndView("redirect:/oficioRemision/salidasPendientesRemision");

            } catch (I18NException e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("registroSir.error.envio"));
                return new ModelAndView("redirect:/oficioRemision/salidasPendientesRemision");
            }
        }


        log.debug("Final de oficioRemisionSir.");
        // TODO Missatge
        if (oficioRemisionList.size() == 0) {
            return new ModelAndView("redirect:/oficioRemision/entradasPendientesRemision");
        } else {

            if (oficioRemisionList.size() == 1) {
                String redirect = "redirect:/oficioRemision/" + oficioRemisionList.get(0).getId() + "/detalle";
                log.info("Redirecting to: " + redirect);
                return new ModelAndView(redirect);
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
     * Seleccionar un {@link es.caib.regweb3.model.ModeloOficioRemision}
     */
    @RequestMapping(value = "/{idOficioRemision}/imprimir/{idModelo}", method = RequestMethod.GET)
    public ModelAndView imprimirModeloRecibo(@PathVariable Long idOficioRemision, @PathVariable Long idModelo, HttpServletRequest request, HttpServletResponse response) throws Exception {

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

        /*if (RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA.equals(oficioRemision.getTipoOficioRemision())) {
            List<RegistroEntrada> registrosEntrada = oficioRemisionEjb.getEntradasByOficioRemision(oficioRemision.getId());
            model.addAttribute("registrosEntrada", registrosEntrada);
        }

        if (RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA.equals(oficioRemision.getTipoOficioRemision())) {
            List<RegistroSalida> registrosSalida = oficioRemisionEjb.getSalidasByOficioRemision(oficioRemision.getId());
            model.addAttribute("registrosSalida", registrosSalida);
        }*/

        model.addAttribute("oficioRemision", oficioRemision);

        // Obtenemos los libros donde el UsuarioEntidad puede registrar
        model.addAttribute("libros", getLibrosRegistroEntrada(request));

        // Obtenemos los Organismos destinatários donde la Oficina puede registrar
        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));
        model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);

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
                oficioRemision.getEstado() != RegwebConstantes.OFICIO_INTERNO) {
            Mensaje.saveMessageError(request, getMessage("oficioRemision.error.aceptado"));
            return "redirect:/oficioRemision/pendientesLlegada/list";
        }

        // Procesa el Oficio de Remisión
        try {
            if (RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA.equals(oficioRemision.getTipoOficioRemision())) {

                registrosEntrada = oficioRemisionEntradaUtilsEjb.procesarOficioRemision(oficioRemision,
                        usuarioEntidad, oficinaActiva, oficioPendienteLlegadaForm.getOficios());

            } else if (RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA.equals(oficioRemision.getTipoOficioRemision())) {

                registrosEntrada = oficioRemisionSalidaUtilsEjb.procesarOficioRemision(oficioRemision,
                        usuarioEntidad, oficinaActiva, oficioPendienteLlegadaForm.getOficios());
            }

            model.addAttribute("registrosEntrada", registrosEntrada);

        } catch (Exception e) {
            e.printStackTrace();
            Mensaje.saveMessageError(request, getMessage("oficioRemision.error.aceptando"));
            return "redirect:/oficioRemision/pendientesLlegada/list";
        }

        return "oficioRemision/oficioRemisionAceptado";
    }

}

