package es.caib.regweb3.webapp.controller.registro;

import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.dir3caib.ws.api.unidad.UnidadTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.persistence.utils.RespuestaDistribucion;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.webapp.form.AnularForm;
import es.caib.regweb3.webapp.form.EnvioSirForm;
import es.caib.regweb3.webapp.form.ReenviarForm;
import es.caib.regweb3.webapp.form.RegistroEntradaBusqueda;
import es.caib.regweb3.webapp.utils.AnexoUtils;
import es.caib.regweb3.webapp.utils.JsonResponse;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.RegistroEntradaBusquedaValidator;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by Fundació BIT.
 * Controller para los listados de los Registros de Entrada
 *
 * @author earrivi
 * @author anadal
 * Date: 31/03/14
 */
@Controller
@RequestMapping(value = "/registroEntrada")
public class RegistroEntradaListController extends AbstractRegistroCommonListController {

    @Autowired
    private RegistroEntradaBusquedaValidator registroEntradaBusquedaValidator;

    @EJB(mappedName = "regweb3/HistoricoRegistroEntradaEJB/local")
    private HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;

    @EJB(mappedName = "regweb3/AnexoEJB/local")
    private AnexoLocal anexoEjb;

    @EJB(mappedName = "regweb3/SirEnvioEJB/local")
    private SirEnvioLocal sirEnvioEjb;

    @EJB(mappedName = "regweb3/JustificanteEJB/local")
    private JustificanteLocal justificanteEjb;

    @EJB(mappedName = "regweb3/DistribucionEJB/local")
    private DistribucionLocal distribucionEjb;

    @EJB(mappedName = "regweb3/PluginEJB/local")
    private PluginLocal pluginEjb;


    /**
     * Listado de todos los Registros de Entrada
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String listado() {
        return "redirect:/registroEntrada/list";
    }


    /**
     * Listado de registros de entrada
     *
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request) throws Exception {

        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

        List<Organismo> organismosConsultaEntrada = getOrganismosConsultaEntrada(request);

        RegistroEntrada registroEntrada = new RegistroEntrada();
        registroEntrada.setLibro(getLibroEntidad(request));  // Fijamos el Libro único por defecto

        RegistroEntradaBusqueda registroEntradaBusqueda = new RegistroEntradaBusqueda(registroEntrada, 1);
        registroEntradaBusqueda.setIdOrganismo(seleccionarOrganismoActivo(request, organismosConsultaEntrada));
        registroEntradaBusqueda.setFechaInicio(new Date());
        registroEntradaBusqueda.setFechaFin(new Date());

        model.addAttribute("oficinaActiva", getOficinaActiva(request));
        model.addAttribute("registroEntradaBusqueda", registroEntradaBusqueda);
        model.addAttribute("organosDestino", organismosOficinaActiva);
        model.addAttribute("organismosConsultaEntrada", organismosConsultaEntrada);

        // Obtenemos los usuarios de la Entidad
        model.addAttribute("usuariosEntidad", usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId()));

        model.addAttribute("anularForm", new AnularForm());

        return "registroEntrada/registroEntradaList";
    }

    /**
     * Realiza la busqueda de {@link es.caib.regweb3.model.RegistroEntrada} según los parametros del formulario
     */
    @RequestMapping(value = "/busqueda", method = RequestMethod.GET)
    public ModelAndView busqueda(@ModelAttribute RegistroEntradaBusqueda busqueda, BindingResult result, HttpServletRequest request, HttpServletResponse response) throws Exception {

        ModelAndView mav = new ModelAndView("registroEntrada/registroEntradaList", result.getModel());

        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));
        List<UsuarioEntidad> usuariosEntidad = usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId());
        Oficina oficinaActiva = getOficinaActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Entidad entidadActiva = getEntidadActiva(request);

        registroEntradaBusquedaValidator.validate(busqueda, result);

        // Si hay errores volvemos a la vista del formulario
        if (result.hasErrors()) {
            mav.addObject("errors", result.getAllErrors());
            mav.addObject("oficinaActiva", oficinaActiva);
            mav.addObject("usuariosEntidad", usuariosEntidad);
            mav.addObject("registroEntradaBusqueda", busqueda);
            mav.addObject("organosDestino", organismosOficinaActiva);
            mav.addObject("organismosConsultaEntrada", getOrganismosConsultaEntrada(request));
            mav.addObject("anularForm", new AnularForm());

            return mav;

        } else { // Si no hay errores realizamos la búsqueda

            RegistroEntrada registroEntrada = busqueda.getRegistroEntrada();

            // Ponemos la hora 23:59 a la fecha fin
            Date fechaFin = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin());

            /* Solución a los problemas de encoding del formulario GET */
            String nombreInteresado = new String(busqueda.getInteressatNom().getBytes("ISO-8859-1"), "UTF-8");
            String apellido1Interesado = new String(busqueda.getInteressatLli1().getBytes("ISO-8859-1"), "UTF-8");
            String apellido2Interesado = new String(busqueda.getInteressatLli2().getBytes("ISO-8859-1"), "UTF-8");

            // Organismo origen seleccionado
            List<Long> organismos = new ArrayList<>();
            if(busqueda.getIdOrganismo() == null){
                organismos =  getOrganismosConsultaEntradaId(request);
            }else{
                organismos.add(busqueda.getIdOrganismo());
            }

            //Búsqueda de registros
            Paginacion paginacion = registroEntradaConsultaEjb.busqueda(busqueda.getPageNumber(), organismos, busqueda.getFechaInicio(), fechaFin, registroEntrada, nombreInteresado, apellido1Interesado, apellido2Interesado, busqueda.getInteressatDoc(), busqueda.getOrganDestinatari(), busqueda.getObservaciones(), busqueda.getUsuario(), entidadActiva.getId());

            busqueda.setPageNumber(1);
            mav.addObject("paginacion", paginacion);
            mav.addObject("puedeEditar", permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), busqueda.getIdOrganismo(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA, true));

            // Alta en tabla LOPD
            lopdEjb.insertarRegistros(paginacion, usuarioEntidad.getId(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_LISTADO);
        }

        // Comprobamos si el Organismo destinatario es externo, para añadirlo a la lista.
        if (StringUtils.isNotEmpty(busqueda.getOrganDestinatari())) {
            Organismo org = organismoEjb.findByCodigoByEntidadMultiEntidad(busqueda.getOrganDestinatari(), usuarioEntidad.getEntidad().getId());
            if (org == null || !organismosOficinaActiva.contains(org)) { //Es organismo externo, lo añadimos a la lista
                organismosOficinaActiva.add(new Organismo(null, busqueda.getOrganDestinatari(), new String(busqueda.getOrganDestinatariNom().getBytes("ISO-8859-1"), "UTF-8")));
            }
        }

        mav.addObject("organosDestino", organismosOficinaActiva);
        mav.addObject("oficinaActiva", oficinaActiva);
        mav.addObject("usuariosEntidad", usuariosEntidad);
        mav.addObject("oficinasConsultaEntrada", getOficinasConsultaEntrada(request));
        mav.addObject("organismosConsultaEntrada", getOrganismosConsultaEntrada(request));
        mav.addObject("registroEntradaBusqueda", busqueda);
        mav.addObject("organDestinatari", busqueda.getOrganDestinatari());
        mav.addObject("anularForm", new AnularForm());

        /* Solucion a los problemas de encoding del formulario GET */
        busqueda.getRegistroEntrada().getRegistroDetalle().setExtracto(new String(busqueda.getRegistroEntrada().getRegistroDetalle().getExtracto().getBytes("ISO-8859-1"), "UTF-8"));
        busqueda.setObservaciones(new String(busqueda.getObservaciones().getBytes("ISO-8859-1"), "UTF-8"));

        busqueda.setOrganDestinatariNom(new String(busqueda.getOrganDestinatariNom().getBytes("ISO-8859-1"), "UTF-8"));

        return mav;

    }


    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/detalle", method = RequestMethod.GET)
    public String detalleRegistroEntrada(@PathVariable Long idRegistro, Model model, HttpServletRequest request) throws Exception, I18NException {

        RegistroEntrada registro = registroEntradaEjb.findByIdCompleto(idRegistro);

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
        Boolean puedeEditar = permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registro.getOficina().getOrganismoResponsable().getId(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA, true);

        model.addAttribute("isResponsableOrganismo", permisoOrganismoUsuarioEjb.isAdministradorOrganismo(usuarioEntidad.getId(),registro.getOficina().getOrganismoResponsable().getId()));
        model.addAttribute("puedeEditar", puedeEditar);
        model.addAttribute("puedeDistribuir", permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registro.getOficina().getOrganismoResponsable().getId(), RegwebConstantes.PERMISO_DISTRIBUCION_REGISTRO, true));
        model.addAttribute("tieneJustificante", tieneJustificante);
        model.addAttribute("maxReintentos", PropiedadGlobalUtil.getMaxReintentosSir(entidadActiva.getId()));

        // Solo si no es una reserva de número
        if (!registro.getEstado().equals(RegwebConstantes.REGISTRO_RESERVA)) {

            // Oficio Remision
            if (entidadActiva.getOficioRemision() && (registro.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO) || registro.getEstado().equals(RegwebConstantes.REGISTRO_PENDIENTE_VISAR))) {

                if (registro.getEvento().equals(RegwebConstantes.EVENTO_OFICIO_SIR)) { // Mensajes de limitaciones anexos si es oficio de remisión sir
                    initMensajeNotaInformativaAnexos(entidadActiva, model);
                }
            }

            // ANULAR DISTRIBUIR TEMPORAL POR MOTIVO DE FIRMA XSIG NO VALIDABLE
            if (registro.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO)) {

                boolean distribuirRipea = true;
                if (PropiedadGlobalUtil.getNoDistribuir(entidadActiva.getId())) {
                    for (Anexo anexo : registro.getRegistroDetalle().getAnexos()) {
                        //Solo miramos si la firma es valida si el anexo tiene firma
                        if (anexo.getModoFirma() != RegwebConstantes.MODO_FIRMA_ANEXO_SINFIRMA && !anexo.getTipoDocumento().equals(RegwebConstantes.TIPO_DOCUMENTO_FICHERO_TECNICO) && !anexo.isJustificante() && !anexo.getConfidencial()) {
                            if (!anexo.getFirmaValida()) { //Si la firma es invalida no se distribuye.
                                distribuirRipea = false;
                                break;
                            }
                        }
                    }
                }
                model.addAttribute("distribuirRipea", distribuirRipea);
            }


            // Anexos
            Boolean anexosCompleto = (registro.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO) || registro.getEstado().equals(RegwebConstantes.REGISTRO_PENDIENTE_VISAR)) && puedeEditar && !tieneJustificante;
            if (anexosCompleto) { // Si se muestran los anexos completo

                List<AnexoFull> anexos = anexoEjb.getByRegistroEntrada(registro); //Inicializamos los anexos del registro de entrada.
                initScanAnexos(entidadActiva, model, request, registro.getId()); // Inicializa los atributos para escanear anexos

                // Si es SIR, se validan los tamaños y tipos de anexos
                if (registro.getEvento().equals(RegwebConstantes.EVENTO_OFICIO_SIR)) {

                    model.addAttribute("erroresAnexosSir", AnexoUtils.validarAnexosSir(anexos));
                }
                model.addAttribute("anexos", anexos);
                model.addAttribute("anexoDetachedPermitido", PropiedadGlobalUtil.getPermitirAnexosDetached(entidadActiva.getId()));
            }
            model.addAttribute("anexosCompleto", anexosCompleto);

            // Interesados
            if (registro.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO) && puedeEditar && !tieneJustificante) {

                initDatosInteresados(model, organismosOficinaActiva);
                model.addAttribute("ultimosOrganismos",  registroEntradaConsultaEjb.ultimosOrganismosRegistro(usuarioEntidad));

            }

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
        }

        // Posicion sello
        if (entidadActiva.getPosXsello() != null && entidadActiva.getPosYsello() != null) {
            model.addAttribute("posXsello", entidadActiva.getPosXsello());
            model.addAttribute("posYsello", entidadActiva.getPosYsello());
        }

        // Alta en tabla LOPD
        lopdEjb.altaLopd(registro.getNumeroRegistro(), registro.getFecha(), registro.getLibro().getId(), usuarioEntidad.getId(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_CONSULTA);

        return "registroEntrada/registroEntradaDetalle";
    }

    /**
     * Enviar a SIR un Registro de Entrada
     */
    @RequestMapping(value = "/{idRegistro}/enviarSir", method = RequestMethod.GET)
    public ModelAndView enviarSir(@PathVariable Long idRegistro, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("registro/envioSir");
        Oficina oficinaActiva = getOficinaActiva(request);
        Entidad entidadActiva = getEntidadActiva(request);

        if (!oficinaActiva.getSirEnvio()) {
            log.info("La oficinaActiva no está integrada en SIR");
            Mensaje.saveMessageError(request, getMessage("aviso.oficinaActiva.sir"));
            return new ModelAndView("redirect:/registroEntrada/" + idRegistro + "/detalle");
        }

        RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);

        //Obtenemos el destino externo de dir3caib que nos han indicado para ver si está extinguido
        //String destinoExterno = registroEntradaEjb.obtenerDestinoExternoRE(idRegistro);
        String destinoExterno = registroEntrada.getDestinoExternoCodigo();

        if (destinoExterno != null) {
            UnidadTF destino = organismoEjb.obtenerDestinoExterno(destinoExterno, entidadActiva.getId());
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
                    return new ModelAndView("redirect:/registroEntrada/" + idRegistro + "/detalle");
                }
            }
            mav.addObject("oficinasSIR", oficinasSIR);
        }

        mav.addObject("tipoRegistro", RegwebConstantes.REGISTRO_ENTRADA);
        mav.addObject("envioSirForm", new EnvioSirForm());
        mav.addObject("registro", registroEntrada);

        return mav;
    }

    /**
     * Enviar a SIR un Registro de Entrada
     */
    @RequestMapping(value = "/{idRegistro}/enviarSir", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse enviarSir(@ModelAttribute EnvioSirForm envioSirForm, @PathVariable Long idRegistro, String oficinaSIRCodigo,
                                  HttpServletRequest request) throws Exception {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        JsonResponse jsonResponse = new JsonResponse();

        try {
            RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFull(idRegistro);

            // Crear el Justificante
            if (!registroEntrada.getRegistroDetalle().getTieneJustificante()) {

                // Creamos el anexo del justificante y se lo añadimos al registro
                AnexoFull anexoFull = justificanteEjb.crearJustificante(usuarioEntidad, registroEntrada, RegwebConstantes.REGISTRO_ENTRADA, Configuracio.getDefaultLanguage());
                registroEntrada.getRegistroDetalle().getAnexosFull().add(anexoFull);
            }

            // Enviar el Intercambio
            sirEnvioEjb.enviarIntercambio(RegwebConstantes.REGISTRO_ENTRADA, registroEntrada, getOficinaActiva(request), usuarioEntidad, oficinaSIRCodigo);

            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.envioSir.ok"));
            jsonResponse.setStatus("SUCCESS");

        } catch (Exception se) {
            log.info(getMessage("registroSir.error.envio"));
            jsonResponse.setStatus("FAIL");
            jsonResponse.setError(getMessage("registroSir.error.envio") + ": " + se.getMessage());
            se.printStackTrace();
        } catch (I18NException e) {
            log.info(getMessage("registroSir.error.envio"));
            jsonResponse.setStatus("FAIL");
            jsonResponse.setError(getMessage("registroSir.error.envio") + ": " + I18NUtils.getMessage(e));
            e.printStackTrace();
        } catch (I18NValidationException ve) {
            log.info(getMessage("registroSir.error.envio"));
            jsonResponse.setStatus("FAIL");
            jsonResponse.setError(getMessage("registroSir.error.envio") + ": " + I18NUtils.getMessage(ve));
            ve.printStackTrace();
        }

        return jsonResponse;
    }

    @RequestMapping(value = "/{idRegistro}/reenviar", method = RequestMethod.GET)
    public String reenviarRegistroEntrada(@PathVariable Long idRegistro, Model model, HttpServletRequest request) throws Exception {

        RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);

        if(!(registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_RECHAZADO) || registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_REENVIADO))){
            Mensaje.saveMessageAviso(request, I18NUtils.tradueix("aviso.registro.reenvioSir"));
            return "redirect:/aviso";
        }

        model.addAttribute("tipoRegistro", RegwebConstantes.REGISTRO_ENTRADA);
        model.addAttribute("comunidadesAutonomas", catComunidadAutonomaEjb.getAll());
        model.addAttribute("nivelesAdministracion", catNivelAdministracionEjb.getAll());
        model.addAttribute("registro", registroEntrada);
        model.addAttribute("reenviarForm", new ReenviarForm());

        return "registro/reenvioSir";
    }

    /**
     * Reenvia un {@link RegistroSir}
     */
    @RequestMapping(value = "/{idRegistro}/reenviar", method = RequestMethod.POST)
    public String reenviarRegistroEntrada(@PathVariable Long idRegistro, @ModelAttribute ReenviarForm reenviarForm, HttpServletRequest request)
            throws Exception, I18NException, I18NValidationException {

        //Montamos la oficina de reenvio seleccionada por el usuario
        Oficina oficinaReenvio = reenviarForm.oficinaReenvio();
        Oficina oficinaActiva = getOficinaActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        // Reenvia el RegistroSir
        try {
            if (oficinaReenvio != null) {//Si han seleccionado oficina de reenvio
                //Reenviamos
                sirEnvioEjb.reenviarIntercambio(RegwebConstantes.REGISTRO_ENTRADA, idRegistro, oficinaReenvio, oficinaActiva, usuarioEntidad, reenviarForm.getObservaciones());
            }

            Mensaje.saveMessageInfo(request, getMessage("registroSir.reenvio.ok"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("registroSir.error.reenvio"));
            e.printStackTrace();
        }

        return "redirect:/registroEntrada/" + idRegistro + "/detalle";
    }

    @RequestMapping(value = "/reservas/list/{pageNumber}")
    public ModelAndView reservas(@PathVariable Integer pageNumber, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("registroEntrada/registrosEntradaEstado");

        Oficina oficinaActiva = getOficinaActiva(request);

        if (isOperador(request) && oficinaActiva != null) {

            Paginacion paginacion = registroEntradaConsultaEjb.getByOficinaEstadoPaginado(pageNumber, oficinaActiva.getId(), RegwebConstantes.REGISTRO_RESERVA);

            mav.addObject("titulo", getMessage("registroEntrada.listado.reservas"));
            mav.addObject("url", "reservas");
            mav.addObject("paginacion", paginacion);

        }

        return mav;
    }

    @RequestMapping(value = "/pendientesDistribuir/list/{pageNumber}")
    public ModelAndView validos(@PathVariable Integer pageNumber, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("registroEntrada/registrosEntradaEstado");

        Oficina oficinaActiva = getOficinaActiva(request);

        if (isOperador(request) && oficinaActiva != null) {

            Paginacion paginacion = registroEntradaConsultaEjb.pendientesDistribuir(pageNumber, oficinaActiva.getId());

            mav.addObject("titulo", getMessage("registroEntrada.listado.pendientesDistribuir"));
            mav.addObject("url", "pendientesDistribuir");
            mav.addObject("paginacion", paginacion);

        }

        return mav;
    }

    @RequestMapping(value = "/pendientesSir/list/{pageNumber}")
    public ModelAndView pendientesSir(@PathVariable Integer pageNumber, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("registroEntrada/registrosEntradaEstado");

        Oficina oficinaActiva = getOficinaActiva(request);

        if (isOperador(request) && oficinaActiva != null) {

            Paginacion paginacion = registroEntradaConsultaEjb.getSirRechazadosReenviadosPaginado(pageNumber, oficinaActiva.getId());

            mav.addObject("titulo", getMessage("registroEntrada.listado.pendientesSir"));
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

            List<RegistroEntrada> registrosEntrada = registroEntradaConsultaEjb.getByLibrosEstado((pageNumber - 1) * BaseEjbJPA.RESULTADOS_PAGINACION, organismosResponsable, RegwebConstantes.REGISTRO_PENDIENTE_VISAR);
            Long totalVisarEntrada = registroEntradaConsultaEjb.getByLibrosEstadoCount(organismosResponsable, RegwebConstantes.REGISTRO_PENDIENTE_VISAR);
            Paginacion paginacion = new Paginacion(totalVisarEntrada.intValue(), pageNumber);

            mav.addObject("titulo", getMessage("registroEntrada.pendientesVisar"));
            mav.addObject("paginacion", paginacion);
            mav.addObject("listado", registrosEntrada);
            mav.addObject("tipoRegistro", RegwebConstantes.REGISTRO_ENTRADA);
        }

        return mav;
    }

    @RequestMapping(value = "/pendientesDistribuirSir/list/{pageNumber}", method = RequestMethod.GET)
    public ModelAndView pendientesDistribuir(@PathVariable Integer pageNumber, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("registroEntrada/registrosEntradaEstado");

        Oficina oficinaActiva = getOficinaActiva(request);
        Entidad entidadActiva = getEntidadActiva(request);

        Paginacion paginacion = trazabilidadEjb.buscarPendientesDistribuirSir(oficinaActiva.getId(), entidadActiva.getId(), pageNumber);

        mav.addObject("titulo", getMessage("registroEntrada.pendientesDistribuir.sir"));
        mav.addObject("paginacion", paginacion);
        mav.addObject("url", "pendientesDistribuirSir");
        mav.addObject("tipoRegistro", RegwebConstantes.REGISTRO_ENTRADA);

        return mav;
    }


    /**
     * Anular un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/anular", method = RequestMethod.POST)
    public String anularRegistroEntrada(@ModelAttribute AnularForm anularForm, HttpServletRequest request) {

        try {

            RegistroEntrada registroEntrada = registroEntradaEjb.findById(anularForm.getIdAnular());
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            final List<Long> estados = new ArrayList<Long>();
            estados.add(RegwebConstantes.REGISTRO_RESERVA);
            estados.add(RegwebConstantes.REGISTRO_VALIDO);
            estados.add(RegwebConstantes.REGISTRO_PENDIENTE_VISAR);

            // Comprobamos si el RegistroEntrada se puede anular según su estado.
            if (!estados.contains(registroEntrada.getEstado())) {
                Mensaje.saveMessageError(request, getMessage("registroEntrada.anulado"));
                return "redirect:/registroEntrada/list";
            }

            // Comprobamos que el usuario dispone del permiso para Modificar el Registro
            if (!permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroEntrada.getOficina().getOrganismoResponsable().getId(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA, true)) {
                log.info("Aviso: No dispone de los permisos necesarios para editar el registro");
                Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));

                return "redirect:/registroEntrada/list";
            }

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

        return "redirect:/registroEntrada/" + anularForm.getIdAnular() + "/detalle";
    }

    /**
     * Activar un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/activar")
    public String activarRegistroEntrada(@PathVariable Long idRegistro, HttpServletRequest request) {

        try {

            RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            // Comprobamos si el RegistroEntrada tiene el estado anulado
            if (!registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_ANULADO)) {

                Mensaje.saveMessageError(request, getMessage("registro.activar.error"));
                return "redirect:/registroEntrada/list";
            }

            // Comprobamos que el usuario dispone del permiso para Modificar el Registro
            if (!permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroEntrada.getOficina().getOrganismoResponsable().getId(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA, true)) {
                log.info("Aviso: No dispone de los permisos necesarios para editar el registro");
                Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));

                return "redirect:/registroEntrada/list";
            }

            // Si era una reserva de número no lo activamos, lo volvemos a poner Pendiente
            if (registroEntrada.getDestino() == null && registroEntrada.getDestinoExternoCodigo() == null) {
                registroEntradaEjb.cambiarEstadoHistorico(registroEntrada, RegwebConstantes.REGISTRO_RESERVA, usuarioEntidad);
            } else {
                // Activamos el RegistroEntrada
                registroEntradaEjb.activarRegistroEntrada(registroEntrada, usuarioEntidad);
            }

            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.activar"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            e.printStackTrace();
        }

        return "redirect:/registroEntrada/" + idRegistro + "/detalle";
    }

    /**
     * Visar un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/visar")
    public String visarRegistroEntrada(@PathVariable Long idRegistro, HttpServletRequest request) {

        try {

            RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            // Comprobamos si el RegistroEntrada tiene el estado Pendiente de Visar
            if (!registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_PENDIENTE_VISAR)) {

                Mensaje.saveMessageError(request, getMessage("registro.visar.error"));
                return "redirect:/registroEntrada/list";
            }

            // Comprobamos si el UsuarioEntidad tiene permisos para visar el Registro Entrada
            if (!permisoOrganismoUsuarioEjb.isAdministradorOrganismo(usuarioEntidad.getId(), registroEntrada.getOficina().getOrganismoResponsable().getId())) {

                Mensaje.saveMessageError(request, getMessage("aviso.usuario.visar"));
                return "redirect:/registroEntrada/list";
            }

            // Visar el RegistroEntrada
            registroEntradaEjb.visarRegistroEntrada(registroEntrada, usuarioEntidad);

            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.visar.ok"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            e.printStackTrace();
        }

        return "redirect:/registroEntrada/" + idRegistro + "/detalle";
    }

    /**
     * Función que se encarga de obtener los destinatarios a los que se debe distribuir el registro de entrada.
     * La obtención de esos destinatarios se realiza a través del plugin
     *
     * @param idRegistro identificador del registro
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idRegistro}/distribuir", method = RequestMethod.GET)
    public
    @ResponseBody
    JsonResponse distribuirRegistroEntrada(@PathVariable Long idRegistro, HttpServletRequest request) throws Exception, I18NException, I18NValidationException {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        RegistroEntrada registroEntrada;
        RespuestaDistribucion respuesta = new RespuestaDistribucion();
        JsonResponse response = new JsonResponse();

        registroEntrada = registroEntradaEjb.findById(idRegistro);

        try {
            //Distribuimos el registro
            respuesta = distribucionEjb.distribuir(registroEntrada, usuarioEntidad);

            if (respuesta.getHayPlugin()) {//
                if (respuesta.getEnviadoCola()) { //Si se ha enviado a la cola
                    response.setStatus("ENVIADO_COLA");
                    Mensaje.saveMessageInfo(request, getMessage("registroEntrada.enviocola"));
                } else if ((respuesta.getHayPlugin() && respuesta.getEnviado())) { //Cuando se ha distribuido correctamente
                    Mensaje.saveMessageInfo(request, getMessage("registroEntrada.distribuir.ok"));
                    response.setStatus("SUCCESS");
                } else if (respuesta.getHayPlugin() && !respuesta.getEnviado()) { //Cuando no se ha distribuido correctamente
                    response.setStatus("FAIL");
                    response.setError(getMessage("registroEntrada.distribuir.error.noEnviado"));
                }
            } else {

                Mensaje.saveMessageInfo(request, getMessage("registroEntrada.distribuir.ok"));
                response.setStatus("SUCCESS");
            }

            response.setResult(respuesta);

        } catch (I18NValidationException e) {
            e.printStackTrace();
            response.setStatus("FAIL");
            response.setError(I18NUtils.getMessage(e));
            response.setResult(respuesta);
            return response;
        } catch (I18NException ie) {
            ie.printStackTrace();
            response.setStatus("FAIL");
            response.setError(I18NUtils.getMessage(ie));
            response.setResult(respuesta);
            return response;
        } catch (Exception ste) {
            ste.printStackTrace();
            response.setStatus("FAIL");
            response.setError(ste.getMessage());
            response.setResult(respuesta);
            return response;
        }

        return response;
    }


    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/sello", method = RequestMethod.POST)
    public ModelAndView sello(@PathVariable Long idRegistro, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("sello");

        RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);

        //Si el registro no está Anulado ni Pendiente de Visar
        if (!registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_ANULADO) &&
                !registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_PENDIENTE_VISAR)) {

            mav.addObject("registro", registroEntrada);
            mav.addObject("x", request.getParameter("x"));
            mav.addObject("y", request.getParameter("y"));
            mav.addObject("orientacion", request.getParameter("orientacion"));
            mav.addObject("tipoRegistro", getMessage("informe.entrada"));

            return mav;

        } else {
            Mensaje.saveMessageError(request, getMessage("aviso.sello.noImprimible"));
            return new ModelAndView("redirect:/registroEntrada/" + idRegistro + "/detalle");
        }

    }


    /**
     * Método que genera el Justificante en pdf
     *
     * @param idRegistro identificador del registro de entrada
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/{idRegistro}/justificante/{idioma}", method = RequestMethod.POST)
    public JsonResponse justificante(@PathVariable Long idRegistro, @PathVariable String idioma, HttpServletRequest request)
            throws Exception {

        JsonResponse jsonResponse = new JsonResponse();

        try {

            RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFull(idRegistro);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            // Dispone de permisos para Editar el registro
            if (permisoOrganismoUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroEntrada.getOficina().getOrganismoResponsable().getId(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA, true) && !registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_ANULADO)) {

                // Creamos el anexo justificante y lo firmamos
                AnexoFull anexoFull = justificanteEjb.crearJustificante(usuarioEntidad, registroEntrada, RegwebConstantes.REGISTRO_ENTRADA, idioma);

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

    @InitBinder("registroEntradaBusqueda")
    public void registroEntradaBusqueda(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class, dateEditor);
    }

}