package es.caib.regweb3.webapp.controller.oficioRemision;

import es.caib.dir3caib.ws.api.oficina.Dir3CaibObtenerOficinasWs;
import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.RegistroBasico;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.Dir3CaibUtils;
import es.caib.regweb3.persistence.utils.OficiosRemisionOrganismo;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.sir.core.model.AsientoRegistralSir;
import es.caib.regweb3.sir.ws.api.manager.FicheroIntercambioManager;
import es.caib.regweb3.sir.ws.api.manager.impl.FicheroIntercambioManagerImpl;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.*;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

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

    @EJB(mappedName = "regweb3/RelacionOrganizativaOfiEJB/local")
    public RelacionOrganizativaOfiLocal relacionOrganizativaOfiLocalEjb;

    @EJB(mappedName = "regweb3/OficioRemisionEJB/local")
    public OficioRemisionLocal oficioRemisionEjb;

    @EJB(mappedName = "regweb3/ModeloOficioRemisionEJB/local")
    public ModeloOficioRemisionLocal modeloOficioRemisionEjb;

    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/RegistroSalidaEJB/local")
    public RegistroSalidaLocal registroSalidaEjb;

    @EJB(mappedName = "regweb3/EntidadEJB/local")
    public EntidadLocal entidadEjb;

    @EJB(mappedName = "regweb3/TrazabilidadEJB/local")
    public TrazabilidadLocal trazabilidadEjb;

    @EJB(mappedName = "regweb3/SirEJB/local")
    public SirLocal sirEjb;

    FicheroIntercambioManager ficheroIntercambioManager = new FicheroIntercambioManagerImpl();

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

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        List<Libro> librosConsulta = permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), RegwebConstantes.PERMISO_CONSULTA_REGISTRO_ENTRADA);

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

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        List<Libro> librosConsulta = permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), RegwebConstantes.PERMISO_CONSULTA_REGISTRO_ENTRADA);

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
        OficiosRemisionOrganismo oficiosRemisionOrganismo = oficioRemisionEntradaUtilsEjb.oficiosEntradaPendientesRemision(busqueda.getPageNumber(), busqueda.getAnyo(), oficinaActiva.getId(), registroEntrada.getLibro().getId(), registroEntrada.getDestino().getCodigo(), getOrganismosOficioRemision(request, organismosOficinaActiva), entidadActiva);

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
        model.addAttribute("organismosDestino", oficioRemisionSalidaUtilsEjb.organismosSalidaPendientesRemision(oficinaActiva.getId(), librosRegistroSalida, getOrganismosOficioRemisionSalida(request, getOrganismosOficinaActiva(request))));
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
        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

        // Obtenemos los Registros de Salida, pendientes de tramitar por medio de un Oficio de Revisión, agrupados según su Organismos destinatario.
        OficiosRemisionOrganismo oficiosRemisionOrganismo = oficioRemisionSalidaUtilsEjb.oficiosSalidaPendientesRemision(busqueda.getPageNumber(), busqueda.getAnyo(), oficinaActiva.getId(), busqueda.getRegistroSalida().getLibro().getId(), busqueda.getDestinatario().getCodigo(), entidadActiva);

        busqueda.setPageNumber(1);
        mav.addObject("oficiosRemisionOrganismo", oficiosRemisionOrganismo);
        mav.addObject("paginacion", oficiosRemisionOrganismo.getPaginacion());
        mav.addObject("librosRegistro", librosRegistroEntrada);
        mav.addObject("organismosDestino", oficioRemisionSalidaUtilsEjb.organismosSalidaPendientesRemision(oficinaActiva.getId(), librosRegistroEntrada, getOrganismosOficioRemisionSalida(request, getOrganismosOficinaActiva(request))));
        mav.addObject("registroSalidaBusqueda", busqueda);
        mav.addObject("oficioRemisionForm", new OficioRemisionForm(RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA));
        mav.addObject("anys", getAnys());

        return mav;

    }


    /**
     * Listado de oficios de remisión pendientes de Llegada
     */
    @RequestMapping(value = "/pendientesLlegada/list", method = RequestMethod.GET)
    public ModelAndView oficiosPendientesLlegada(Model model, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision/oficiosPendientesLlegadaList");

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        List<Libro> librosConsulta = permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), RegwebConstantes.PERMISO_CONSULTA_REGISTRO_ENTRADA);

        OficioRemisionBusquedaForm oficioRemisionBusquedaForm = new OficioRemisionBusquedaForm(new OficioRemision(), 1);

        model.addAttribute("librosConsulta", librosConsulta);
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

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        List<Libro> librosConsulta = permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), RegwebConstantes.PERMISO_CONSULTA_REGISTRO_ENTRADA);

        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

        Paginacion paginacion = oficioRemisionEjb.oficiosPendientesLlegadaBusqueda(organismosOficinaActiva, busqueda.getPageNumber(), oficioRemision, librosConsulta, busqueda.getTipoOficioRemision());

        busqueda.setPageNumber(1);
        mav.addObject("paginacion", paginacion);
        mav.addObject("librosConsulta", librosConsulta);
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

                    // Oficio externo
                    Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService();
                    OficinaTF oficinaSir = oficinasService.obtenerOficina(oficioRemisionForm.getOficinaSIRCodigo(), null, null);

                    final String organismoExternoCodigo = oficioRemisionForm.getOrganismoExternoCodigo();
                    final String organismoExternoDenominacion = oficioRemisionForm.getOrganismoExternoDenominacion();
                    log.info("organismoExternoCodigo = " + organismoExternoCodigo);
                    log.info("organismoExternoDenominacion = " + organismoExternoDenominacion);


                    // Para cada Registro de Entrada generamos un oficio de remision SIR.
                    for (RegistroEntrada registroEntradaAEnviar : registrosEntrada) {

                        registroEntradaAEnviar = registroEntradaEjb.getConAnexosFull(registroEntradaAEnviar.getId());

                        AsientoRegistralSir asientoRegistralSir = sirEjb.transformarRegistroEntrada(registroEntradaAEnviar, oficinaSir);

                        // Enviamos el Fichero de datos de intercambio al nodo SIR
                        String identificadorIntercambio = ficheroIntercambioManager.enviarFicheroIntercambio(asientoRegistralSir);

                        // Cream oficio remision
                        OficioRemision oficioRemision = oficioRemisionEntradaUtilsEjb.crearOficioRemisionSir(
                                registroEntradaAEnviar, getOficinaActiva(request), usuarioEntidad,
                                organismoExternoCodigo, organismoExternoDenominacion, oficioRemisionForm.getIdLibro(), identificadorIntercambio);

                        oficioRemisionList.add(oficioRemision);

                        // Crear
                        log.info("Creado oficio remisión SIR " + oficioRemision.getId()
                                + " para el registro de entrada "
                                + registroEntradaAEnviar.getNumeroRegistroFormateado());
                    }

                }

            } catch (Throwable e) {
                // TODO que fer en aquest cas
                log.error(" Error enviant a SIR: " + e.getMessage(), e);
                Mensaje.saveMessageError(request, getMessage("asientoRegistralSir.error.envio"));
                // TODO Borrar Oficio Remision

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

                    // Oficio externo
                    Dir3CaibObtenerOficinasWs oficinasService = Dir3CaibUtils.getObtenerOficinasService();
                    OficinaTF oficinaSir = oficinasService.obtenerOficina(oficioRemisionForm.getOficinaSIRCodigo(), null, null);

                    final String organismoExternoCodigo = oficioRemisionForm.getOrganismoExternoCodigo();
                    final String organismoExternoDenominacion = oficioRemisionForm.getOrganismoExternoDenominacion();
                    log.info("organismoExternoCodigo = " + organismoExternoCodigo);
                    log.info("organismoExternoDenominacion = " + organismoExternoDenominacion);


                    // Para cada Registro de Salida generamos un oficio de remision SIR.
                    for (RegistroSalida registroSalidaAEnviar : registrosSalida) {

                        registroSalidaAEnviar = registroSalidaEjb.getConAnexosFull(registroSalidaAEnviar.getId());

                        AsientoRegistralSir asientoRegistralSir = sirEjb.transformarRegistroSalida(registroSalidaAEnviar, oficinaSir);

                        // Enviamos el Fichero de datos de intercambio al nodo SIR
                        String identificadorIntercambio = ficheroIntercambioManager.enviarFicheroIntercambio(asientoRegistralSir);

                        // Cream oficio remision
                        OficioRemision oficioRemision = oficioRemisionSalidaUtilsEjb.crearOficioRemisionSir(
                                registroSalidaAEnviar, getOficinaActiva(request), usuarioEntidad,
                                organismoExternoCodigo, organismoExternoDenominacion, oficioRemisionForm.getIdLibro(), identificadorIntercambio);

                        oficioRemisionList.add(oficioRemision);

                        // Crear
                        log.info("Creado oficio remisión SIR " + oficioRemision.getId()
                                + " para el registro de salida "
                                + registroSalidaAEnviar.getNumeroRegistroFormateado());
                    }

                }


            } catch (Throwable e) {
                // TODO que fer en aquest cas
                log.error(" Error enviant a SIR: " + e.getMessage(), e);
                Mensaje.saveMessageError(request, getMessage("asientoRegistralSir.error.envio"));
                // TODO Borrar Oficio Remision

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
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.OficioRemision}
     */
    @RequestMapping(value = "/{idOficioRemision}/detalle", method = RequestMethod.GET)
    public String detalleOficioRemision(@PathVariable Long idOficioRemision, Model model, HttpServletRequest request) throws Exception {

        OficioRemision oficioRemision = oficioRemisionEjb.findById(idOficioRemision);
        //List<RegistroEntrada> registrosEntrada = oficioRemisionEjb.getByOficioRemision(oficioRemision.getId());
        List<Trazabilidad> trazabilidades = trazabilidadEjb.getByOficioRemision(oficioRemision.getId());

        model.addAttribute(oficioRemision);
        //model.addAttribute("registrosEntrada",registrosEntrada);
        model.addAttribute("trazabilidades", trazabilidades);
        model.addAttribute("isAdministradorLibro", permisoLibroUsuarioEjb.isAdministradorLibro(getUsuarioEntidadActivo(request).getId(), oficioRemision.getLibro().getId()));
        model.addAttribute("modeloOficioRemision", new ModeloOficioRemision());
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
                oficioRemision.getEstado() == RegwebConstantes.OFICIO_REMISION_INTERNO_ENVIADO || oficioRemision.getEstado() == RegwebConstantes.OFICIO_REMISION_EXTERNO_ENVIADO) {

            oficioRemisionEjb.anularOficioRemision(idOficioRemision, usuarioEntidad);
            Mensaje.saveMessageInfo(request, getMessage("aviso.oficioRemision.anulado"));

        } else {
            Mensaje.saveMessageError(request, getMessage("aviso.oficioRemision.anular"));
        }

        return "redirect:/oficioRemision/" + idOficioRemision + "/detalle";
    }

    /**
     * Seleccionar un {@link es.caib.regweb3.model.ModeloOficioRemision}
     */
    @RequestMapping(value = "/{idOficioRemision}/imprimir/{idModelo}", method = RequestMethod.GET)
    public ModelAndView imprimirModeloRecibo(@PathVariable Long idOficioRemision, @PathVariable Long idModelo, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView mav = new ModelAndView("oficioRemision");

        OficioRemision oficioRemision = oficioRemisionEjb.findById(idOficioRemision);

        if (RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA.equals(oficioRemision.getTipoOficioRemision())) {
            List<RegistroEntrada> registrosEntrada = oficioRemisionEjb.getEntradasByOficioRemision(oficioRemision.getId());
            mav.addObject("registrosEntrada", registrosEntrada);
        } else if (RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA.equals(oficioRemision.getTipoOficioRemision())) {
            List<RegistroSalida> registrosSalida = oficioRemisionEjb.getSalidasByOficioRemision(oficioRemision.getId());
            mav.addObject("registrosSalida", registrosSalida);
        }

        mav.addObject("oficioRemision", oficioRemision);
        mav.addObject("modeloOficioRemision", modeloOficioRemisionEjb.findById(idModelo));

        return mav;

    }


    /**
     * Carga el formulario para procesar un {@link es.caib.regweb3.model.OficioRemision}
     */
    @RequestMapping(value = "/{idOficioRemision}/procesar", method = RequestMethod.GET)
    public String procesarOficioRemision(@PathVariable Long idOficioRemision, Model model, HttpServletRequest request) throws Exception {

        OficioRemision oficioRemision = oficioRemisionEjb.findById(idOficioRemision);

        if (RegwebConstantes.TIPO_OFICIO_REMISION_ENTRADA.equals(oficioRemision.getTipoOficioRemision())) {
            List<RegistroEntrada> registrosEntrada = oficioRemisionEjb.getEntradasByOficioRemision(oficioRemision.getId());
            model.addAttribute("registrosEntrada", registrosEntrada);
        }

        if (RegwebConstantes.TIPO_OFICIO_REMISION_SALIDA.equals(oficioRemision.getTipoOficioRemision())) {
            List<RegistroSalida> registrosSalida = oficioRemisionEjb.getSalidasByOficioRemision(oficioRemision.getId());
            model.addAttribute("registrosSalida", registrosSalida);
        }

        model.addAttribute("oficioRemision", oficioRemision);

        // Obtenemos los libros donde el UsuarioEntidad puede registrar
        model.addAttribute("libros", getLibrosRegistroEntrada(request));

        // Obtenemos los Organismos destinatários donde la Oficina puede registrar
        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));
        model.addAttribute("organismosOficinaActiva", organismosOficinaActiva);

        model.addAttribute("oficioPendienteLlegadaForm", new OficioPendienteLlegadaForm());
        model.addAttribute("modeloOficioRemision", new ModeloOficioRemision());
        model.addAttribute("modelosOficioRemision", modeloOficioRemisionEjb.getByEntidad(getEntidadActiva(request).getId()));

        return "oficioRemision/oficioRemisionProcesar";
    }

    /**
     * Procesa {@link es.caib.regweb3.model.OficioRemision} pendiente de llegada,
     * creando tantos RegistroEntrada como contenga dicho OficioRemision.
     */
    @RequestMapping(value = "/{idOficioRemision}/procesar", method = RequestMethod.POST)
    public String procesarOficioRemision(
            @ModelAttribute OficioPendienteLlegadaForm oficioPendienteLlegadaForm,
            @PathVariable Long idOficioRemision, Model model, HttpServletRequest request)
            throws Exception, I18NException, I18NValidationException {

        OficioRemision oficioRemision = oficioRemisionEjb.findById(idOficioRemision);
        Oficina oficinaActiva = getOficinaActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        List<RegistroEntrada> registrosEntrada = new ArrayList<RegistroEntrada>();

        // Comprobamos si ya ha sido procesado
        if (oficioRemision.getEstado() != RegwebConstantes.OFICIO_REMISION_INTERNO_ENVIADO) {
            Mensaje.saveMessageError(request, getMessage("oficioRemision.error.yaprocesado"));
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("registrosEntrada", registrosEntrada);


        return "oficioRemision/oficioRemisionProcesado";
    }

}

