package es.caib.regweb3.webapp.controller.adminEntidad;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.utils.TimeUtils;
import es.caib.regweb3.webapp.controller.registro.AbstractRegistroCommonListController;
import es.caib.regweb3.webapp.form.AnularForm;
import es.caib.regweb3.webapp.form.BasicForm;
import es.caib.regweb3.webapp.form.RegistroEntradaBusqueda;
import es.caib.regweb3.webapp.form.RegistroSalidaBusqueda;
import es.caib.regweb3.webapp.utils.JsonResponse;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.RegistroEntradaBusquedaValidator;
import es.caib.regweb3.webapp.validator.RegistroSalidaBusquedaValidator;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

@Controller
@RequestMapping(value = "/adminEntidad")
public class AdminEntidadController extends AbstractRegistroCommonListController {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RegistroEntradaBusquedaValidator registroEntradaBusquedaValidator;

    @Autowired
    private RegistroSalidaBusquedaValidator registroSalidaBusquedaValidator;

    @EJB(mappedName = HistoricoRegistroEntradaLocal.JNDI_NAME)
    private HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;

    @EJB(mappedName = HistoricoRegistroSalidaLocal.JNDI_NAME)
    private HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;

    @EJB(mappedName = AnexoLocal.JNDI_NAME)
    private AnexoLocal anexoEjb;

    @EJB(mappedName = JustificanteLocal.JNDI_NAME)
    private JustificanteLocal justificanteEjb;

    @EJB(mappedName = PersonaLocal.JNDI_NAME)
    private PersonaLocal personaEjb;

    @EJB(mappedName = InteresadoLocal.JNDI_NAME)
    private InteresadoLocal interesadoEjb;

    @EJB(mappedName = MultiEntidadLocal.JNDI_NAME)
    private MultiEntidadLocal multiEntidadEjb;

    @EJB(mappedName = DistribucionLocal.JNDI_NAME)
    private DistribucionLocal distribucionEjb;

    @EJB(mappedName = IntegracionLocal.JNDI_NAME)
    private IntegracionLocal integracionEjb;

    @EJB(mappedName = AnexoSirLocal.JNDI_NAME)
    private AnexoSirLocal anexoSirEjb;


    /**
     * Listado de registros de entrada
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/registroEntrada/list", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request)throws Exception {

        Entidad entidadActiva = getEntidadActiva(request);

        RegistroEntradaBusqueda registroEntradaBusqueda = new RegistroEntradaBusqueda(new RegistroEntrada(),1);
        registroEntradaBusqueda.setFechaInicio(new Date());
        registroEntradaBusqueda.setFechaFin(new Date());

        model.addAttribute("registroEntradaBusqueda", registroEntradaBusqueda);
        model.addAttribute("organosOrigen", organismoEjb.getPermitirUsuarios(entidadActiva.getId()));
        if(multiEntidadEjb.isMultiEntidad()) {
            model.addAttribute("organosDestino", organismoEjb.getAllByEntidadMultiEntidad(entidadActiva.getId()));
        }else{
            model.addAttribute("organosDestino", organismoEjb.getAllByEntidad(entidadActiva.getId()));
        }
        model.addAttribute("oficinasRegistro",  oficinaEjb.findByEntidadLigero(entidadActiva.getId()));

        // Obtenemos los usuarios de la Entidad
        model.addAttribute("usuariosEntidad", usuarioEntidadEjb.findByEntidad(entidadActiva.getId()));

        return "registroEntrada/registroEntradaListAdmin";
    }

    /**
     * Realiza la busqueda de {@link es.caib.regweb3.model.RegistroEntrada} según los parametros del formulario
     */
    @RequestMapping(value = "/registroEntrada/busqueda", method = RequestMethod.GET)
    public ModelAndView busqueda(@ModelAttribute RegistroEntradaBusqueda busqueda, BindingResult result, HttpServletRequest request, HttpServletResponse response)throws Exception {

        ModelAndView mav = new ModelAndView("registroEntrada/registroEntradaListAdmin", result.getModel());

        Entidad entidadActiva = getEntidadActiva(request);

        List<Organismo> organosOrigen = organismoEjb.getPermitirUsuarios(entidadActiva.getId());
        List<Organismo> organosDestino;
        if(multiEntidadEjb.isMultiEntidad()) {

            organosDestino = organismoEjb.getAllByEntidadMultiEntidad(entidadActiva.getId());
        }else{
            organosDestino = organismoEjb.getAllByEntidad(entidadActiva.getId());
        }
        List<Oficina> oficinasRegistro = oficinaEjb.findByEntidadLigero(entidadActiva.getId());
        List<UsuarioEntidad> usuariosEntidad = usuarioEntidadEjb.findByEntidad(entidadActiva.getId());
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        registroEntradaBusquedaValidator.validate(busqueda, result);

        // Si hay errores volvemos a la vista del formulario
        if (result.hasErrors()) {
            mav.addObject("errors", result.getAllErrors());
            mav.addObject("usuariosEntidad",usuariosEntidad);
            mav.addObject("registroEntradaBusqueda", busqueda);
            mav.addObject("organosDestino", organosDestino);
            mav.addObject("organosOrigen", organosOrigen);
            mav.addObject("oficinasRegistro",  oficinasRegistro);

            return mav;

        }else { // Si no hay errores realizamos la búsqueda

            RegistroEntrada registroEntrada = busqueda.getRegistroEntrada();

            // Ponemos la hora 23:59 a la fecha fin
            Date fechaFin = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin());

            // Organismo origen seleccionado
            List<Long> organismos = new ArrayList<>();
            if(busqueda.getIdOrganismo() == null){
                organismos = getIds(organosOrigen);
            }else{
                organismos.add(busqueda.getIdOrganismo());
            }

            //Búsqueda de registros
            Paginacion paginacion = registroEntradaConsultaEjb.busqueda(busqueda.getPageNumber(), organismos,busqueda.getFechaInicio(), fechaFin, registroEntrada, busqueda.getInteressatNom(), busqueda.getInteressatLli1(), busqueda.getInteressatLli2(), busqueda.getInteressatDoc(), busqueda.getOrganDestinatari(), null, busqueda.getIdUsuario(), entidadActiva.getId());

            busqueda.setPageNumber(1);
            mav.addObject("paginacion", paginacion);

            // Alta en tabla LOPD
            lopdEjb.insertarRegistros(paginacion, usuarioEntidad,entidadActiva.getLibro(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_LISTADO);
        }

        // Comprobamos si el Organismo destinatario es externo, para añadirlo a la lista.
        if (StringUtils.isNotEmpty(busqueda.getOrganDestinatari())) {
            Organismo org = organismoEjb.findByCodigoByEntidadMultiEntidad(busqueda.getOrganDestinatari(), entidadActiva.getId());
            if(org== null || !organosDestino.contains(org)){ //Es organismo externo, lo añadimos a la lista
                organosDestino.add(new Organismo(null,busqueda.getOrganDestinatari(),busqueda.getOrganDestinatariNom()));
            }
        }

        mav.addObject("organosDestino", organosDestino);
        mav.addObject("organosOrigen", organosOrigen);
        mav.addObject("usuariosEntidad",usuariosEntidad);
        mav.addObject("oficinasRegistro", oficinasRegistro);
        mav.addObject("registroEntradaBusqueda", busqueda);
        mav.addObject("organDestinatari", busqueda.getOrganDestinatari());

        return mav;
    }

    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/registroEntrada/{idRegistro}/detalle", method = RequestMethod.GET)
    public String detalleRegistroEntrada(@PathVariable Long idRegistro, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception, I18NException, I18NValidationException {

        RegistroEntrada registro = registroEntradaEjb.findByIdCompleto(idRegistro);

        Entidad entidadActiva = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        model.addAttribute("registro",registro);
        model.addAttribute("entidadActiva", entidadActiva);
        model.addAttribute("anularForm", new AnularForm());
        model.addAttribute("integracion", new BasicForm());

        // Permisos
        Boolean tieneJustificante = registro.getRegistroDetalle().getTieneJustificante();
        model.addAttribute("tieneJustificante", tieneJustificante);
        model.addAttribute("tieneJustificanteCustodiado", registro.getRegistroDetalle().getTieneJustificanteCustodiado());

        // Solo si no es una reserva de número
        if(!registro.getEstado().equals(RegwebConstantes.REGISTRO_RESERVA)){

            // Justificante
            if (tieneJustificante) {
                Anexo justificante = registro.getRegistroDetalle().getJustificante();

                model.addAttribute("idJustificante", justificante.getId());
                String urlValidacion = anexoEjb.getUrlValidation(justificante,entidadActiva.getId());
                model.addAttribute("tieneUrlValidacion", StringUtils.isNotEmpty(urlValidacion));
            }

            // Historicos
            model.addAttribute("historicos", historicoRegistroEntradaEjb.getByRegistroEntrada(idRegistro));

            // Trazabilidad
            model.addAttribute("trazabilidades", trazabilidadEjb.getByRegistroEntrada(registro.getId()));

            model.addAttribute("isResponsableOrganismo", permisoOrganismoUsuarioEjb.isAdministradorOrganismo(usuarioEntidad.getId(),registro.getOficina().getOrganismoResponsable().getId()));

        }

        // Alta en tabla LOPD
        lopdEjb.altaLopd(registro.getNumeroRegistro(), registro.getFecha(), registro.getLibro().getId(), usuarioEntidad.getId(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_CONSULTA);

        return "registroEntrada/registroEntradaDetalleAdmin";
    }

    /**
     * Marca como distribuido un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/registroEntrada/{idRegistro}/marcarDistribuido", method = RequestMethod.GET)
    public String marcarDistribuido(@PathVariable Long idRegistro, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        RegistroEntrada registro = registroEntradaEjb.findByIdCompleto(idRegistro);

        // Justificante
        if (registro.getRegistroDetalle().getTieneJustificanteCustodiado()) {
            registroEntradaEjb.marcarDistribuido(registro, usuarioEntidad, I18NUtils.tradueix("distribucion.cola"));
            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.distribuir.ok"));
        }

        return "redirect:/adminEntidad/registroEntrada/" + idRegistro + "/detalle";
    }

    /**
     * Método que genera el Justificante en pdf
     *
     * @param idRegistro identificador del registro de entrada
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/registroEntrada/{idRegistro}/reDistribuir", method = RequestMethod.POST)
    public JsonResponse reDistribuir(@PathVariable Long idRegistro, HttpServletRequest request) throws Exception {

        JsonResponse jsonResponse = new JsonResponse();

        try {

            Boolean distribuido = distribucionEjb.reDistribuirRegistro(idRegistro, getEntidadActiva(request));

            if(distribuido){
                jsonResponse.setStatus("SUCCESS");
            }else{
                jsonResponse.setStatus("FAIL");
            }

        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.setStatus("FAIL");
        }

        return jsonResponse;
    }

    /**
     * Método que genera el Justificante en pdf
     *
     * @param idRegistro identificador del registro de entrada
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/registroEntrada/{idRegistro}/justificante/{idioma}", method = RequestMethod.POST)
    public JsonResponse justificanteEntrada(@PathVariable Long idRegistro, @PathVariable String idioma, HttpServletRequest request)
            throws Exception {

        JsonResponse jsonResponse = new JsonResponse();

        try {

            RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFull(idRegistro);
            Entidad entidad = getEntidadActiva(request);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            // Dispone de permisos para Editar el registro
            if (permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroEntrada.getOficina().getOrganismoResponsable().getId(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA, true) && !registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_ANULADO)) {

                // Creamos el anexo justificante y lo firmamos
                AnexoFull anexoFull = justificanteEjb.crearJustificante(entidad, usuarioEntidad, registroEntrada, RegwebConstantes.REGISTRO_ENTRADA, idioma);

                // Alta en tabla LOPD
                if (anexoFull != null) {
                    lopdEjb.altaLopd(registroEntrada.getNumeroRegistro(), registroEntrada.getFecha(), registroEntrada.getLibro().getId(), usuarioEntidad.getId(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_JUSTIFICANTE);
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

    /**
     * Anular un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/registroEntrada/anular", method = RequestMethod.POST)
    public String anularRegistroEntrada(@ModelAttribute AnularForm anularForm, HttpServletRequest request) {

        try {

            RegistroEntrada registroEntrada = registroEntradaEjb.findById(anularForm.getIdAnular());
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            // Anulamos el RegistroEntrada
            String motivoAnulacion;
            if (StringUtils.isEmpty(anularForm.getObservacionesAnulacion())) {
                motivoAnulacion = getMessage("registro.modificacion.anulacion");
            } else {
                motivoAnulacion = getMessage("registro.modificacion.anulacion") + ": " + anularForm.getObservacionesAnulacion();
            }
            registroEntradaEjb.anularRegistroEntrada(registroEntrada, usuarioEntidad, motivoAnulacion);

            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.anular"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            e.printStackTrace();
        }

        return "redirect:/adminEntidad/registroEntrada/" + anularForm.getIdAnular() + "/detalle";
    }

    /**
     * Listado de registros de salida
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/registroSalida/list", method = RequestMethod.GET)
    public String listRegistroSalida(Model model, HttpServletRequest request)throws Exception {

        Entidad entidadActiva = getEntidadActiva(request);

        RegistroSalidaBusqueda registroSalidaBusqueda = new RegistroSalidaBusqueda(new RegistroSalida(),1);
        registroSalidaBusqueda.setFechaInicio(new Date());
        registroSalidaBusqueda.setFechaFin(new Date());

        model.addAttribute("organosOrigen", organismoEjb.getPermitirUsuarios(entidadActiva.getId()));
        model.addAttribute("registroSalidaBusqueda", registroSalidaBusqueda);
        model.addAttribute("oficinasRegistro",  oficinaEjb.findByEntidadLigero(entidadActiva.getId()));

        // Obtenemos los usuarios de la Entidad
        model.addAttribute("usuariosEntidad", usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId()));

        return "registroSalida/registroSalidaListAdmin";
    }

    /**
     * Realiza la busqueda de {@link es.caib.regweb3.model.RegistroSalida} según los parametros del formulario
     */
    @RequestMapping(value = "/registroSalida/busqueda", method = RequestMethod.GET)
    public ModelAndView busquedaRegistroSalida(@ModelAttribute RegistroSalidaBusqueda busqueda, BindingResult result, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("registroSalida/registroSalidaListAdmin", result.getModel());

        Entidad entidadActiva = getEntidadActiva(request);

        List<Organismo> organosOrigen = organismoEjb.getPermitirUsuarios(entidadActiva.getId());
        List<Oficina> oficinasRegistro = oficinaEjb.findByEntidadLigero(entidadActiva.getId());
        List<UsuarioEntidad> usuariosEntidad = usuarioEntidadEjb.findByEntidad(entidadActiva.getId());
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        registroSalidaBusquedaValidator.validate(busqueda, result);

        // Si hay errores volvemos a la vista del formulario
        if (result.hasErrors()) {
            mav.addObject("errors", result.getAllErrors());
            mav.addObject("usuariosEntidad",usuariosEntidad);
            mav.addObject("registroSalidaBusqueda", busqueda);
            mav.addObject("organosOrigen", organosOrigen);
            mav.addObject("oficinasRegistro",  oficinasRegistro);

            return mav;

        }else { // Si no hay errores realizamos la búsqueda

            RegistroSalida registroSalida = busqueda.getRegistroSalida();

            // Ponemos la hora 23:59 a la fecha fin
            Date fechaFin = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin());

            // Organismo origen seleccionado
            List<Long> organismos = new ArrayList<>();
            if(busqueda.getIdOrganismo() == null){
                organismos = getIds(organosOrigen);
            }else{
                organismos.add(busqueda.getIdOrganismo());
            }

            //Búsqueda de registros
            Paginacion paginacion = registroSalidaConsultaEjb.busqueda(busqueda.getPageNumber(),organismos, busqueda.getFechaInicio(), fechaFin, registroSalida, busqueda.getInteressatNom(), busqueda.getInteressatLli1(), busqueda.getInteressatLli2(), busqueda.getInteressatDoc(),null, busqueda.getIdUsuario(), entidadActiva.getId());

            busqueda.setPageNumber(1);
            mav.addObject("paginacion", paginacion);

            // Alta en tabla LOPD
            lopdEjb.insertarRegistros(paginacion, usuarioEntidad, entidadActiva.getLibro(), RegwebConstantes.REGISTRO_SALIDA, RegwebConstantes.LOPD_LISTADO);
        }

        mav.addObject("organosOrigen",  organosOrigen);
        mav.addObject("usuariosEntidad",usuariosEntidad);
        mav.addObject("oficinasRegistro", oficinasRegistro);
        mav.addObject("registroEntradaBusqueda", busqueda);

        return mav;
    }

    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.RegistroSalida}
     */
    @RequestMapping(value = "/registroSalida/{idRegistro}/detalle", method = RequestMethod.GET)
    public String detalleRegistroSalida(@PathVariable Long idRegistro, Model model, HttpServletRequest request) throws Exception, I18NException, I18NValidationException {

        RegistroSalida registro = registroSalidaEjb.findByIdCompleto(idRegistro);

        Entidad entidadActiva = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        model.addAttribute("registro",registro);
        model.addAttribute("entidadActiva", entidadActiva);
        model.addAttribute("anularForm", new AnularForm());
        model.addAttribute("integracion", new BasicForm());

        // Permisos
        Boolean tieneJustificante = registro.getRegistroDetalle().getTieneJustificante();
        model.addAttribute("tieneJustificante", tieneJustificante);

        // Justificante
        if(tieneJustificante){
            Anexo justificante = registro.getRegistroDetalle().getJustificante();

            model.addAttribute("idJustificante", justificante.getId());
            String urlValidacion = anexoEjb.getUrlValidation(justificante,entidadActiva.getId());
            model.addAttribute("tieneUrlValidacion", StringUtils.isNotEmpty(urlValidacion));
        }

        // Historicos
        model.addAttribute("historicos", historicoRegistroSalidaEjb.getByRegistroSalida(idRegistro));

        // Trazabilidad
        model.addAttribute("trazabilidades", trazabilidadEjb.getByRegistroSalida(registro.getId()));

        model.addAttribute("isResponsableOrganismo", permisoOrganismoUsuarioEjb.isAdministradorOrganismo(usuarioEntidad.getId(),registro.getOficina().getOrganismoResponsable().getId()));

        // Alta en tabla LOPD
        lopdEjb.altaLopd(registro.getNumeroRegistro(), registro.getFecha(), registro.getLibro().getId(), usuarioEntidad.getId(), RegwebConstantes.REGISTRO_SALIDA, RegwebConstantes.LOPD_CONSULTA);

        return "registroSalida/registroSalidaDetalleAdmin";
    }

    /**
     * Método que genera el Justificante en pdf
     *
     * @param idRegistro identificador del registro de salida
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/registroSalida/{idRegistro}/justificante/{idioma}", method = RequestMethod.POST)
    public JsonResponse justificanteSalida(@PathVariable Long idRegistro, @PathVariable String idioma, HttpServletRequest request)
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

    /**
     * Anular un {@link es.caib.regweb3.model.RegistroSalida}
     */
    @RequestMapping(value = "/registroSalida/anular", method = RequestMethod.POST)
    public String anularRegistroSalida(@ModelAttribute AnularForm anularForm, HttpServletRequest request) {

        try {

            RegistroSalida registroSalida = registroSalidaEjb.findById(anularForm.getIdAnular());
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            // Anulamos el RegistroEntrada
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

        return "redirect:/adminEntidad/registroSalida/" + anularForm.getIdAnular() + "/detalle";
    }

    /**
     * Capitalizar todas las {@link es.caib.regweb3.model.Persona} de un tipo
     */
    @RequestMapping(value = "/capitalizarPersonas/{tipoPersona}")
    public String capitalizarPersonas(@PathVariable Long tipoPersona, HttpServletRequest request) {

        try {

            long inicio = System.currentTimeMillis();

            Entidad entidad = getEntidadActiva(request);

            if(tipoPersona.equals(RegwebConstantes.TIPO_PERSONA_JURIDICA)){
                personaEjb.capitalizarPersonasJuridicas(entidad.getId());

            }else if(tipoPersona.equals(RegwebConstantes.TIPO_PERSONA_FISICA)){
                personaEjb.capitalizarPersonasFisicas(entidad.getId());
            }

            Mensaje.saveMessageInfo(request, "Se han capitalizado las personas correctamente en " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - inicio));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, "Error al capitalizar personas");
            e.printStackTrace();
        }

        return "redirect:/inici";
    }

    /**
     * Capitalizar todas las {@link es.caib.regweb3.model.Interesado} de un tipo
     */
    @RequestMapping(value = "/capitalizarInteresados/{tipoInteresado}")
    public String capitalizarInteresados(@PathVariable Long tipoInteresado, HttpServletRequest request) {

        try {

            long inicio = System.currentTimeMillis();

            if(tipoInteresado.equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_JURIDICA)){
                interesadoEjb.capitalizarInteresadosJuridicos();

            }else if(tipoInteresado.equals(RegwebConstantes.TIPO_INTERESADO_PERSONA_FISICA)){
                interesadoEjb.capitalizarInteresadosFisicas();
            }

            Mensaje.saveMessageInfo(request, "Se han capitalizado los interesados correctamente en " + TimeUtils.formatElapsedTime(System.currentTimeMillis() - inicio));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, "Error al capitalizar interesados");
            e.printStackTrace();
        }

        return "redirect:/inici";
    }

    /**
     * Purga los anexos distribuidos
     */
    @RequestMapping(value = "/purgarAnexosDistribuidos")
    public String purgarAnexosDistribuidos(HttpServletRequest request) throws Exception{

        Entidad entidad =  getEntidadActiva(request);

        //Integración
        long tiempo = System.currentTimeMillis();
        StringBuilder peticion = new StringBuilder();
        String descripcion = "Purgar Anexos distribuidos";
        Date inicio = new Date();
        peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

        try {

            //Purgamos los anexos de registros distribuidos
            int total = anexoEjb.purgarAnexosRegistrosDistribuidos(entidad.getId());
            peticion.append("total anexos purgados: ").append(total).append(System.getProperty("line.separator"));

            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");

            Mensaje.saveMessageInfo(request, "Se han purgado " + total + " anexos distribuidos.");

        } catch (I18NException e) {
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidad.getId(), "");
            Mensaje.saveMessageError(request, "Error purgando anexos distribuidos");
            e.printStackTrace();
        }

        return "redirect:/inici";
    }

    /**
     * Purga los anexos sir aceptados
     */
    @RequestMapping(value = "/purgarAnexosSir")
    public String purgarAnexosSir(HttpServletRequest request) throws Exception{

        Entidad entidad =  getEntidadActiva(request);

        //Integración
        long tiempo = System.currentTimeMillis();
        StringBuilder peticion = new StringBuilder();
        String descripcion = "Purgar AnexosSir";
        peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

        Date inicio = new Date();

        try {

            //Purgamos los anexos de registros distribuidos
            int total = anexoSirEjb.purgarAnexosAceptados(entidad.getId());
            peticion.append("total anexos: ").append(total).append(System.getProperty("line.separator"));

            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");

            Mensaje.saveMessageInfo(request, "Se han purgado " + total + " anexos sir.");

        } catch (Exception e) {
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidad.getId(), "");
            Mensaje.saveMessageError(request, "Error purgando anexos sir");
            e.printStackTrace();
        }

        return "redirect:/inici";
    }

    /**
     * Purga los anexos aceptados en destino SIR
     */
    @RequestMapping(value = "/purgarAnexosAceptados")
    public String purgarAnexosAceptados(HttpServletRequest request) throws Exception{

        Entidad entidad =  getEntidadActiva(request);

        //Integración
        long tiempo = System.currentTimeMillis();
        StringBuilder peticion = new StringBuilder();
        String descripcion = "Purgar Anexos de registros recibidos SIR Confirmados";
        peticion.append("entidad: ").append(entidad.getNombre()).append(System.getProperty("line.separator"));

        Date inicio = new Date();

        try {

            //Purgamos los anexos de registros recibidos SIR Confirmados
            int total = anexoEjb.purgarAnexosRegistrosAceptados(entidad.getId());
            peticion.append("total anexos: ").append(total).append(System.getProperty("line.separator"));

            integracionEjb.addIntegracionOk(inicio, RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), System.currentTimeMillis() - tiempo, entidad.getId(), "");

            Mensaje.saveMessageInfo(request, "Se han purgado " + total + " anexos aceptados en destino SIR.");

        } catch (I18NException e) {
            integracionEjb.addIntegracionError(RegwebConstantes.INTEGRACION_SCHEDULERS, descripcion, peticion.toString(), e, null, System.currentTimeMillis() - tiempo, entidad.getId(), "");
            Mensaje.saveMessageError(request, "Error purgando anexos aceptados en destino SIR");
            e.printStackTrace();
        }

        return "redirect:/inici";
    }

    /**
     * Obtiene las {@link es.caib.regweb3.model.Oficina} del Organismo seleccionado
     */
    @RequestMapping(value = "/obtenerOficinas", method = RequestMethod.GET)
    public @ResponseBody
    LinkedHashSet<Oficina> obtenerOficinas(@RequestParam Long id) throws Exception {

        if(id != null){
            return oficinaEjb.oficinasServicioCompleto(id, RegwebConstantes.OFICINA_VIRTUAL_SI);
        }else{
            return null;
        }
    }


    /**
     * Retorna una lista con los id's de un List<Organismo>
     * @param organosDestino
     * @return
     */
    private List<Long> getIds(List<Organismo> organosDestino) {

        List<Long> organismosId = new ArrayList<>();

        for(Organismo organismo:organosDestino){
            organismosId.add(organismo.getId());
        }
        return organismosId;
    }


    @InitBinder("registroEntradaBusqueda")
    public void registroEntradaBusqueda(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class,dateEditor);
    }

    @InitBinder("registroSalidaBusqueda")
    public void registroSalidaBusqueda(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class,dateEditor);
    }
}
