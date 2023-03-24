package es.caib.regweb3.webapp.controller.registro;

import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.webapp.form.AnularForm;
import es.caib.regweb3.webapp.form.EnvioSirForm;
import es.caib.regweb3.webapp.form.ReenviarForm;
import es.caib.regweb3.webapp.form.RegistroSalidaBusqueda;
import es.caib.regweb3.webapp.utils.AnexoUtils;
import es.caib.regweb3.webapp.utils.JsonResponse;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.RegistroSalidaBusquedaValidator;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by Fundació BIT.
 * Controller para gestionar los Registros de Salida
 *
 * @author earrivi
 * @author anadal
 * Date: 31/03/14
 */
@Controller
@RequestMapping(value = "/registroSalida")
public class RegistroSalidaListController extends AbstractRegistroCommonListController {


    @Autowired
    private RegistroSalidaBusquedaValidator registroSalidaBusquedaValidator;

    @EJB(mappedName = HistoricoRegistroSalidaLocal.JNDI_NAME)
    private HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;

    @EJB(mappedName = AnexoLocal.JNDI_NAME)
    private AnexoLocal anexoEjb;

    @EJB(mappedName = SirEnvioLocal.JNDI_NAME)
    private SirEnvioLocal sirEnvioEjb;

    @EJB(mappedName = JustificanteLocal.JNDI_NAME)
    private JustificanteLocal justificanteEjb;


    /**
     * Listado de todos los Registros de Salida
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String listado() {
        return "redirect:/registroSalida/list";
    }


    /**
     * Listado de registros de salida
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request) throws Exception {

        List<Organismo> organismosConsultaSalida = getOrganismosConsultaSalida(request);

        // Fijamos un Libro por defecto
        RegistroSalida registroSalida = new RegistroSalida();
        registroSalida.setLibro(getLibroEntidad(request));

        RegistroSalidaBusqueda registroSalidaBusqueda = new RegistroSalidaBusqueda(registroSalida, 1);
        registroSalidaBusqueda.setIdOrganismo(seleccionarOrganismoActivo(request, organismosConsultaSalida));
        registroSalidaBusqueda.setFechaInicio(new Date());
        registroSalidaBusqueda.setFechaFin(new Date());

        model.addAttribute(getOficinaActiva(request));
        model.addAttribute("registroSalidaBusqueda", registroSalidaBusqueda);
        model.addAttribute("organismosConsultaSalida", organismosConsultaSalida);

        // Obtenemos los usuarios de la Entidad
        model.addAttribute("usuariosEntidad", usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId()));

        model.addAttribute("anularForm", new AnularForm());

        return "registroSalida/registroSalidaList";

    }

    /**
     * Realiza la busqueda de {@link es.caib.regweb3.model.RegistroSalida} según los parametros del formulario
     */
    @RequestMapping(value = "/busqueda", method = RequestMethod.GET)
    public ModelAndView busqueda(@ModelAttribute RegistroSalidaBusqueda busqueda, BindingResult result, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("registroSalida/registroSalidaList", result.getModel());

        List<UsuarioEntidad> usuariosEntidad = usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId());
        Oficina oficinaActiva = getOficinaActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Entidad entidadActiva = getEntidadActiva(request);

        registroSalidaBusquedaValidator.validate(busqueda, result);

        // Si hay errores volvemos a la vista del formulario
        if (result.hasErrors()) {
            mav.addObject("errors", result.getAllErrors());
            mav.addObject("oficinaActiva", oficinaActiva);
            mav.addObject("usuariosEntidad", usuariosEntidad);
            mav.addObject("registroSalidaBusqueda", busqueda);
            mav.addObject("oficinasConsultaSalida", getOficinasConsultaSalida(request));
            mav.addObject("organismosConsultaSalida", getOrganismosConsultaSalida(request));
            mav.addObject("organOrigen", busqueda.getOrganOrigen());
            mav.addObject("anularForm", new AnularForm());

            return mav;
        } else { // Si no hay errores realizamos la búsqueda
            RegistroSalida registroSalida = busqueda.getRegistroSalida();

            // Ponemos la hora 23:59 a la fecha fin
            Date fechaFin = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin());

            // Organismo origen seleccionado
            List<Long> organismos = new ArrayList<>();
            if(busqueda.getIdOrganismo() == null){
                organismos =  getOrganismosConsultaSalidaId(request);
            }else{
                organismos.add(busqueda.getIdOrganismo());
            }

            //Búsqueda de registros
            Paginacion paginacion = registroSalidaConsultaEjb.busqueda(busqueda.getPageNumber(), organismos, busqueda.getFechaInicio(), fechaFin, registroSalida, busqueda.getInteressatNom(), busqueda.getInteressatLli1(), busqueda.getInteressatLli2(), busqueda.getInteressatDoc(), busqueda.getObservaciones(), busqueda.getIdUsuario(), entidadActiva.getId());

            busqueda.setPageNumber(1);
            mav.addObject("paginacion", paginacion);
            mav.addObject("permisoEditar", permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), busqueda.getIdOrganismo(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_SALIDA, true));


            // Alta en tabla LOPD
            lopdEjb.insertarRegistros(paginacion, usuarioEntidad, entidadActiva.getLibro(), RegwebConstantes.REGISTRO_SALIDA, RegwebConstantes.LOPD_LISTADO);
        }

        mav.addObject("oficinaActiva", oficinaActiva);
        mav.addObject("usuariosEntidad", usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId()));
        mav.addObject("oficinasConsultaSalida", getOficinasConsultaSalida(request));
        mav.addObject("organismosConsultaSalida", getOrganismosConsultaSalida(request));
        mav.addObject("registroSalidaBusqueda", busqueda);
        mav.addObject("organOrigen", busqueda.getOrganOrigen());
        mav.addObject("anularForm", new AnularForm());

        return mav;
    }

    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.RegistroSalida}
     */
    @RequestMapping(value = "/{idRegistro}/detalle", method = RequestMethod.GET)
    public String detalleRegistroSalida(@PathVariable Long idRegistro, Model model, HttpServletRequest request) throws Exception, I18NException {

        RegistroSalida registro = registroSalidaEjb.findByIdCompleto(idRegistro);

        if (registro.getEvento() == null) {
            Mensaje.saveMessageError(request, getMessage("aviso.registro.evento"));
            return "redirect:/inici";
        }

        Entidad entidadActiva = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Oficina oficinaActiva = getOficinaActiva(request);
        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

        model.addAttribute("registro", registro);
        model.addAttribute("oficina", oficinaActiva);
        model.addAttribute("entidadActiva", entidadActiva);
        model.addAttribute("anularForm", new AnularForm());

        // Permisos
        Boolean tieneJustificante = registro.getRegistroDetalle().getTieneJustificante();
        Boolean permisoEditar = permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registro.getOficina().getOrganismoResponsable().getId(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_SALIDA, true);

        model.addAttribute("isResponsableOrganismo", permisoOrganismoUsuarioEjb.isAdministradorOrganismo(usuarioEntidad.getId(),registro.getOficina().getOrganismoResponsable().getId()));
        model.addAttribute("permisoEditar", permisoEditar);
        model.addAttribute("tieneJustificante", tieneJustificante);
        model.addAttribute("maxReintentos", PropiedadGlobalUtil.getMaxReintentosSir(entidadActiva.getId()));

        // Oficio Remision
        if (entidadActiva.getOficioRemision() && (registro.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO) || registro.getEstado().equals(RegwebConstantes.REGISTRO_PENDIENTE_VISAR))) {

            if (registro.getEvento().equals(RegwebConstantes.EVENTO_OFICIO_SIR)) { // Mensajes de limitaciones anexos si es oficio de remisión sir
                initMensajeNotaInformativaAnexos(entidadActiva, model);
            }
        }

        // Anexos completo
        Boolean anexosEditar = (registro.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO) || registro.getEstado().equals(RegwebConstantes.REGISTRO_PENDIENTE_VISAR)) && registro.getRegistroDetalle().getPresencial() && permisoEditar && !tieneJustificante;
        if (anexosEditar) {

            List<AnexoFull> anexos = anexoEjb.getByRegistroSalida(registro); //Inicializamos los anexos del registro de salida.
            initScanAnexos(entidadActiva, model); // Inicializa los atributos para escanear anexos

            // Si es SIR, se validan los tamaños y tipos de anexos
            if (registro.getEvento().equals(RegwebConstantes.EVENTO_OFICIO_SIR)) {

                model.addAttribute("erroresAnexosSir", AnexoUtils.validarAnexosSir(anexos));
            }

            model.addAttribute("anexos", anexos);
            model.addAttribute("anexoDetachedPermitido", PropiedadGlobalUtil.getPermitirAnexosDetached(entidadActiva.getId()));
        }
        model.addAttribute("anexosEditar", anexosEditar);

        // Interesados
        Boolean interesadosEditar = registro.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO) && registro.getRegistroDetalle().getPresencial() && permisoEditar && !tieneJustificante;

        if (interesadosEditar) {

            initDatosInteresados(model, organismosOficinaActiva);
            model.addAttribute("ultimosOrganismos",  registroSalidaConsultaEjb.ultimosOrganismosRegistro(usuarioEntidad));
        }
        model.addAttribute("interesadosEditar", interesadosEditar);

        // Justificante
        if (tieneJustificante) {
            Anexo justificante = registro.getRegistroDetalle().getJustificante();

            model.addAttribute("idJustificante", justificante.getId());
            String urlValidacion = anexoEjb.getUrlValidation(justificante,entidadActiva.getId());
            model.addAttribute("tieneUrlValidacion", StringUtils.isNotEmpty(urlValidacion));

        }

        // Historicos
        model.addAttribute("historicos", historicoRegistroSalidaEjb.getByRegistroSalida(idRegistro));

        // Trazabilidad
        model.addAttribute("trazabilidades", trazabilidadEjb.getByRegistroSalida(registro.getId()));

        // Posicion sello
        if (entidadActiva.getPosXsello() != null && entidadActiva.getPosYsello() != null) {
            model.addAttribute("posXsello", entidadActiva.getPosXsello());
            model.addAttribute("posYsello", entidadActiva.getPosYsello());
        }

        // Alta en tabla LOPD
        lopdEjb.altaLopd(registro.getNumeroRegistro(), registro.getFecha(), registro.getLibro().getId(), usuarioEntidad.getId(), RegwebConstantes.REGISTRO_SALIDA, RegwebConstantes.LOPD_CONSULTA);

        return "registroSalida/registroSalidaDetalle";
    }

    /**
     * Enviar a SIR un Registro de Salida
     */
    @RequestMapping(value = "/{idRegistro}/enviarSir", method = RequestMethod.GET)
    public ModelAndView enviarSir(@PathVariable Long idRegistro, Model model, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("registro/envioSir");
        Oficina oficinaActiva = getOficinaActiva(request);
        Entidad entidadActiva = getEntidadActiva(request);

        if (!oficinaActiva.getSirEnvio()) {
            log.info("La oficinaActiva no está integrada en SIR");
            Mensaje.saveMessageError(request, getMessage("aviso.oficinaActiva.sir"));
            return new ModelAndView("redirect:/registroSalida/" + idRegistro + "/detalle");
        }

        RegistroSalida registroSalida = registroSalidaEjb.findByIdCompleto(idRegistro);

        if (!registroSalida.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO)) {
            log.info("El registro de salida no tiene el estado valido necesario para enviar a SIR");
            Mensaje.saveMessageError(request, getMessage("aviso.registro.envioSir"));
            return new ModelAndView("redirect:/registroSalida/" + idRegistro + "/detalle");
        }

        //Obtenemos el destino externo de dir3caib que nos han indicado para ver si está extinguido
        String codigoDir3 = RegistroUtils.obtenerCodigoDir3Interesado(registroSalida);

        //Consultamos el estado del destino externo
        if (codigoDir3 != null) {
            UnidadTF destino = organismoEjb.obtenerDestinoExterno(codigoDir3, entidadActiva.getId());
            mav.addObject("destino", destino);
            List<OficinaTF> oficinasSIR = new ArrayList<OficinaTF>();
            //Si está extinguido obtenemos sus sustitutos(con oficinas SIR) de dir3caib
            if (destino.getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_EXTINGUIDO)) {
                List<UnidadTF> sustitutos = organismoEjb.obtenerSustitutosExternosSIR(destino.getCodigo(), entidadActiva.getId());
                if (sustitutos.size() == 1) {
                    //obtenemos sus oficinas SIR
                    oficinasSIR = oficinaEjb.obtenerOficinasSir(sustitutos.get(0).getCodigo(), getLoginInfo(request).getDir3Caib());
                }
                mav.addObject("sustitutos", sustitutos);
            } else { //Obtenemos las oficinas SIR desde dir3caib
                oficinasSIR = oficinaEjb.obtenerOficinasSir(destino.getCodigo(), getLoginInfo(request).getDir3Caib());
                if (oficinasSIR.isEmpty()) {
                    log.info("Este registro no se puede enviar via SIR, no tiene oficinas");
                    Mensaje.saveMessageError(request, getMessage("registroSir.error.envio.oficinas"));
                    return new ModelAndView("redirect:/registroSalida/" + idRegistro + "/detalle");
                }
            }
            mav.addObject("oficinasSIR", oficinasSIR);
        }
        mav.addObject("tipoRegistro", RegwebConstantes.REGISTRO_SALIDA);
        mav.addObject("envioSirForm", new EnvioSirForm());
        mav.addObject("registro", registroSalida);

        return mav;
    }

    /**
     * Enviar a SIR un Registro de Salida
     */
    @RequestMapping(value = "/{idRegistro}/enviarSir", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse enviarSir(@ModelAttribute EnvioSirForm envioSirForm, @PathVariable Long idRegistro, String oficinaSIRCodigo,
                                  HttpServletRequest request) throws Exception {

        Entidad entidad = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        JsonResponse jsonResponse = new JsonResponse();

        try {

            RegistroSalida registroSalida = registroSalidaEjb.getConAnexosFull(idRegistro);

            // Crear el Justificante
            if (!registroSalida.getRegistroDetalle().getTieneJustificante()) {

                // Creamos el anexo del justificante y se lo añadimos al registro
                AnexoFull anexoFull = justificanteEjb.crearJustificante(entidad, usuarioEntidad, registroSalida, RegwebConstantes.REGISTRO_SALIDA, Configuracio.getDefaultLanguage());
                registroSalida.getRegistroDetalle().getAnexosFull().add(anexoFull);
            }

            sirEnvioEjb.enviarIntercambio(RegwebConstantes.REGISTRO_SALIDA, registroSalida, entidad, getOficinaActiva(request), usuarioEntidad, oficinaSIRCodigo);

            Mensaje.saveMessageInfo(request, getMessage("registroSalida.envioSir.ok"));
            jsonResponse.setStatus("SUCCESS");

        } catch (I18NException e) {
            log.info(getMessage("registroSir.error.envio"));
            jsonResponse.setStatus("FAIL");
            jsonResponse.setError(getMessage("registroSir.error.envio") + ": " + e.getMessage());
            e.printStackTrace();
        }

        return jsonResponse;
    }

    @RequestMapping(value = "/{idRegistro}/reenviar", method = RequestMethod.GET)
    public String reenviarRegistroSalida(@PathVariable Long idRegistro, Model model, HttpServletRequest request) throws Exception {

        RegistroSalida registroSalida = registroSalidaEjb.findById(idRegistro);

        if(!(registroSalida.getEstado().equals(RegwebConstantes.REGISTRO_RECHAZADO) || registroSalida.getEstado().equals(RegwebConstantes.REGISTRO_REENVIADO))){
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registro.reenvioSir"));
            return "redirect:/aviso";
        }

        model.addAttribute("tipoRegistro", RegwebConstantes.REGISTRO_SALIDA);
        model.addAttribute("registro", registroSalida);
        model.addAttribute("reenviarForm", new ReenviarForm());

        return "registro/reenvioSir";
    }

    /**
     * Reenvia un {@link RegistroSir}
     */
    @RequestMapping(value = "/{idRegistro}/reenviar", method = RequestMethod.POST)
    public String reenviarRegistroSalida(@PathVariable Long idRegistro, @ModelAttribute ReenviarForm reenviarForm, HttpServletRequest request)
            throws Exception, I18NException, I18NValidationException {

        log.info("Oficina Destino reenvio: " + reenviarForm.getCodigoOficina());

        //Montamos la oficina de reenvio seleccionada por el usuario
        Oficina oficinaReenvio = reenviarForm.oficinaReenvio();
        Oficina oficinaActiva = getOficinaActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Entidad entidad = getEntidadActiva(request);

        // Reenvia el RegistroSir
        try {
            if (oficinaReenvio != null) {//Si han seleccionado oficina de reenvio
                //Reenviamos
                sirEnvioEjb.reenviarIntercambio(RegwebConstantes.REGISTRO_SALIDA, idRegistro, entidad, oficinaReenvio, oficinaActiva, usuarioEntidad, reenviarForm.getObservaciones());
            }

            Mensaje.saveMessageInfo(request, getMessage("registroSir.reenvio.ok"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("registroSir.error.reenvio"));
            e.printStackTrace();
        }

        return "redirect:/registroSalida/" + idRegistro + "/detalle";
    }

    @RequestMapping(value = "/pendientesSir/list/{pageNumber}")
    public ModelAndView pendientesSir(@PathVariable Integer pageNumber, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("registroSalida/registrosSalidaEstado");

        Entidad entidad = getEntidadActiva(request);
        Oficina oficinaActiva = getOficinaActiva(request);

        if (isOperador(request) && oficinaActiva != null) {

            Paginacion paginacion = registroSalidaConsultaEjb.getSirRechazadosReenviadosPaginado(pageNumber, entidad.getId(), oficinaActiva.getId());

            mav.addObject("titulo", getMessage("registroSalida.listado.pendientesSir"));
            mav.addObject("url", "pendientesSir");
            mav.addObject("paginacion", paginacion);

        }

        return mav;
    }

    @RequestMapping(value = "/pendientesVisar/list/{pageNumber}", method = RequestMethod.GET)
    public ModelAndView pendientesVisar(@PathVariable Integer pageNumber, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("registro/pendientesVisarList");

        List<Organismo> organismosResponsable = getOrganismosResponsable(request);

        if ((organismosResponsable != null && organismosResponsable.size() > 0)) {

            List<RegistroSalida> registrosSalida = registroSalidaConsultaEjb.getByLibrosEstado((pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION, organismosResponsable, RegwebConstantes.REGISTRO_PENDIENTE_VISAR);
            Long totalVisarSalida = registroSalidaConsultaEjb.getByLibrosEstadoCount(organismosResponsable, RegwebConstantes.REGISTRO_PENDIENTE_VISAR);
            Paginacion paginacion = new Paginacion(totalVisarSalida.intValue(), pageNumber);

            mav.addObject("titulo", getMessage("registroSalida.pendientesVisar"));
            mav.addObject("paginacion", paginacion);
            mav.addObject("listado", registrosSalida);
            mav.addObject("tipoRegistro", RegwebConstantes.REGISTRO_SALIDA);
        }

        return mav;
    }


    /**
     * Anular un {@link es.caib.regweb3.model.RegistroSalida}
     */
    @RequestMapping(value = "/anular", method = RequestMethod.POST)
    public String anularRegistroSalida(@ModelAttribute AnularForm anularForm, HttpServletRequest request) {

        try {

            RegistroSalida registroSalida = registroSalidaEjb.findById(anularForm.getIdAnular());
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            final List<Long> estados = new ArrayList<Long>();
            estados.add(RegwebConstantes.REGISTRO_VALIDO);
            estados.add(RegwebConstantes.REGISTRO_PENDIENTE_VISAR);

            // Comprobamos si el RegistroSalida se puede anular según su estado.
            if (!estados.contains(registroSalida.getEstado())) {
                Mensaje.saveMessageError(request, getMessage("registroSalida.anulado"));
                return "redirect:/registroSalida/list";
            }

            // Comprobamos que el usuario dispone del permiso para Modificar el Registro
            if (!permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroSalida.getOficina().getOrganismoResponsable().getId(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_SALIDA, true)) {
                log.info("Aviso: No dispone de los permisos necesarios para editar el registro");
                Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));

                return "redirect:/registroSalida/list";
            }

            // Anulamos el RegistroSalida
            String motivoAnulacion;
            if (StringUtils.isEmpty(anularForm.getObservacionesAnulacion())) {
                motivoAnulacion = getMessage("registro.modificacion.anulacion");
            } else {
                motivoAnulacion = getMessage("registro.modificacion.anulacion") + ": " + anularForm.getObservacionesAnulacion();
            }
            registroSalidaEjb.anularRegistroSalida(registroSalida, usuarioEntidad, motivoAnulacion);

            Mensaje.saveMessageInfo(request, getMessage("registroSalida.anular"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            e.printStackTrace();
        }

        return "redirect:/registroSalida/" + anularForm.getIdAnular() + "/detalle";
    }

    /**
     * Activar un {@link es.caib.regweb3.model.RegistroSalida}
     */
    @RequestMapping(value = "/{idRegistro}/activar")
    public String activarRegistroSalida(@PathVariable Long idRegistro, HttpServletRequest request) {

        try {

            RegistroSalida registroSalida = registroSalidaEjb.findByIdCompleto(idRegistro);
            Entidad entidad = getEntidadActiva(request);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            // Comprobamos si el RegistroSalida tiene el estado anulado
            if (!registroSalida.getEstado().equals(RegwebConstantes.REGISTRO_ANULADO)) {

                Mensaje.saveMessageError(request, getMessage("registro.activar.error"));
                return "redirect:/registroSalida/list";
            }

            // Comprobamos que el usuario dispone del permiso para Modificar el Registro
            if (!permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroSalida.getOficina().getOrganismoResponsable().getId(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_SALIDA, true)) {
                log.info("Aviso: No dispone de los permisos necesarios para editar el registro");
                Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));

                return "redirect:/registroSalida/list";
            }

            // Activamos el RegistroSalida
            registroSalidaEjb.activarRegistroSalida(registroSalida, entidad, usuarioEntidad);

            Mensaje.saveMessageInfo(request, getMessage("registroSalida.activar"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            e.printStackTrace();
        }

        return "redirect:/registroSalida/" + idRegistro + "/detalle";
    }

    /**
     * Visar un {@link es.caib.regweb3.model.RegistroSalida}
     */
    @RequestMapping(value = "/{idRegistro}/visar")
    public String visarRegistroSalida(@PathVariable Long idRegistro, HttpServletRequest request) {

        try {

            RegistroSalida registroSalida = registroSalidaEjb.findById(idRegistro);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            // Comprobamos si el RegistroSalida tiene el estado Pendiente de Visar
            if (!registroSalida.getEstado().equals(RegwebConstantes.REGISTRO_PENDIENTE_VISAR)) {

                Mensaje.saveMessageError(request, getMessage("registro.visar.error"));
                return "redirect:/registroSalida/list";
            }

            // Comprobamos si el UsuarioEntidad tiene permisos para visar el RegistroSalida
            if (!permisoOrganismoUsuarioEjb.isAdministradorOrganismo(usuarioEntidad.getId(), registroSalida.getOficina().getOrganismoResponsable().getId())) {

                Mensaje.saveMessageError(request, getMessage("aviso.usuario.visar"));
                return "redirect:/registroSalida/list";
            }

            // Visamos el RegistroSalida
            registroSalidaEjb.visarRegistroSalida(registroSalida, usuarioEntidad);

            Mensaje.saveMessageInfo(request, getMessage("registroSalida.visar.ok"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            e.printStackTrace();
        }

        return "redirect:/registroSalida/" + idRegistro + "/detalle";
    }


    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.RegistroSalida}
     */
    @RequestMapping(value = "/{idRegistro}/sello", method = RequestMethod.POST)
    public ModelAndView sello(@PathVariable Long idRegistro, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("selloPdfView");

        RegistroSalida registroSalida = registroSalidaEjb.findById(idRegistro);

        //Si el registro no está Anulado ni Pendiente de Visar
        if (!registroSalida.getEstado().equals(RegwebConstantes.REGISTRO_ANULADO) &&
                !registroSalida.getEstado().equals(RegwebConstantes.REGISTRO_PENDIENTE_VISAR)) {

            mav.addObject("entidad", getEntidadActiva(request));
            mav.addObject("registro", registroSalida);
            mav.addObject("x", request.getParameter("x"));
            mav.addObject("y", request.getParameter("y"));
            mav.addObject("orientacion", request.getParameter("orientacion"));
            mav.addObject("tipoRegistro", getMessage("informe.salida"));

            return mav;

        } else {
            Mensaje.saveMessageError(request, getMessage("aviso.sello.noImprimible"));
            return new ModelAndView("redirect:/registroSalida/" + idRegistro + "/detalle");
        }

    }


    /**
     * Método que genera el Justificante en pdf
     *
     * @param idRegistro identificador del registro de salida
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/{idRegistro}/justificante/{idioma}", method = RequestMethod.POST)
    public JsonResponse justificante(@PathVariable Long idRegistro, @PathVariable String idioma, HttpServletRequest request)
            throws Exception {

        JsonResponse jsonResponse = new JsonResponse();

        try {

            RegistroSalida registroSalida = registroSalidaEjb.getConAnexosFull(idRegistro);
            Entidad entidad = getEntidadActiva(request);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            // Dispone de permisos para Editar el registro
            if (permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroSalida.getOficina().getOrganismoResponsable().getId(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_SALIDA, true) && !registroSalida.getEstado().equals(RegwebConstantes.REGISTRO_ANULADO)) {

                // Creamos el anexo justificante y lo firmamos
                AnexoFull anexoFull = justificanteEjb.crearJustificante(entidad, usuarioEntidad, registroSalida, RegwebConstantes.REGISTRO_SALIDA, idioma);

                // Alta en tabla LOPD
                if (anexoFull != null) {
                    lopdEjb.altaLopd(registroSalida.getNumeroRegistro(), registroSalida.getFecha(), registroSalida.getLibro().getId(), usuarioEntidad.getId(), RegwebConstantes.REGISTRO_SALIDA, RegwebConstantes.LOPD_JUSTIFICANTE);
                }

                jsonResponse.setStatus("SUCCESS");

            } else {
                jsonResponse.setStatus("FAIL");
                jsonResponse.setError(getMessage("aviso.registro.editar"));
            }

        } catch (I18NException e) {
            e.printStackTrace();
            jsonResponse.setStatus("FAIL");
            jsonResponse.setError(I18NUtils.getMessage(e));
        } catch (I18NValidationException ve) {
            ve.printStackTrace();
            jsonResponse.setStatus("FAIL");
            jsonResponse.setError(I18NUtils.getMessage(ve));
        }

        return jsonResponse;

    }

    @InitBinder("registroSalidaBusqueda")
    public void registroSalidaBusqueda(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class, dateEditor);
    }

}