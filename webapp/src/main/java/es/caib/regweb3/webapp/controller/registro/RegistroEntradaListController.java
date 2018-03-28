package es.caib.regweb3.webapp.controller.registro;

import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.*;
import es.caib.regweb3.sir.core.excepcion.SIRException;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
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
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by Fundació BIT.
 * Controller para los listados de los Registros de Entrada
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
    
    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    private RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/AnexoEJB/local")
    private AnexoLocal anexoEjb;

    @EJB(mappedName = "regweb3/OficioRemisionEntradaUtilsEJB/local")
    private OficioRemisionEntradaUtilsLocal oficioRemisionEntradaUtilsEjb;

    @EJB(mappedName = "regweb3/SirEJB/local")
    private SirLocal sirEjb;

    @EJB(mappedName = "regweb3/JustificanteEJB/local")
    private JustificanteLocal justificanteEjb;


    /**
    * Listado de todos los Registros de Entrada
    */
   @RequestMapping(value = "", method = RequestMethod.GET)
    public String listado() {
       return "redirect:/registroEntrada/list";
    }


    /**
    * Listado de registros de entrada
    * @return
    * @throws Exception
    */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request)throws Exception {

        // Obtenemos los Libros donde el usuario tiene permisos de Consulta
        List<Libro> librosConsulta = getLibrosConsultaEntradas(request);

        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

        // Fijamos un Libro por defecto
        RegistroEntrada registroEntrada = new RegistroEntrada();
        registroEntrada.setLibro(seleccionarLibroOficinaActiva(request,librosConsulta));

        RegistroEntradaBusqueda registroEntradaBusqueda = new RegistroEntradaBusqueda(registroEntrada,1);
        registroEntradaBusqueda.setFechaInicio(new Date());
        registroEntradaBusqueda.setFechaFin(new Date());

        model.addAttribute("oficinaActiva", getOficinaActiva(request));
        model.addAttribute("librosConsulta", librosConsulta);
        model.addAttribute("registroEntradaBusqueda", registroEntradaBusqueda);
        model.addAttribute("organosDestino", organismosOficinaActiva);
        model.addAttribute("oficinasRegistro",  oficinaEjb.findByEntidadByEstado(getEntidadActiva(request).getId(),RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));

        // Obtenemos los usuarios de la Entidad
        model.addAttribute("usuariosEntidad", usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId()));

        return "registroEntrada/registroEntradaList";
    }

    /**
    * Realiza la busqueda de {@link es.caib.regweb3.model.RegistroEntrada} según los parametros del formulario
    */
    @RequestMapping(value = "/busqueda", method = RequestMethod.GET)
    public ModelAndView busqueda(@ModelAttribute RegistroEntradaBusqueda busqueda, BindingResult result, HttpServletRequest request, HttpServletResponse response)throws Exception {

        ModelAndView mav = new ModelAndView("registroEntrada/registroEntradaList", result.getModel());

        // Obtenemos los Libros donde el usuario tiene permisos de Consulta
        List<Libro> librosConsulta = getLibrosConsultaEntradas(request);
        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));
        List<Oficina> oficinasRegistro = oficinaEjb.findByEntidadByEstado(getEntidadActiva(request).getId(),RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        List<UsuarioEntidad> usuariosEntidad = usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId());
        Oficina oficinaActiva = getOficinaActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        registroEntradaBusquedaValidator.validate(busqueda, result);

        // Si hay errores volvemos a la vista del formulario
        if (result.hasErrors()) {
            mav.addObject("errors", result.getAllErrors());
            mav.addObject("oficinaActiva", oficinaActiva);
            mav.addObject("usuariosEntidad",usuariosEntidad);
            mav.addObject("librosConsulta", librosConsulta);
            mav.addObject("registroEntradaBusqueda", busqueda);
            mav.addObject("organosDestino", organismosOficinaActiva);
            mav.addObject("oficinasRegistro",  oficinasRegistro);

            return mav;

        }else { // Si no hay errores realizamos la búsqueda

            RegistroEntrada registroEntrada = busqueda.getRegistroEntrada();

            // Ponemos la hora 23:59 a la fecha fin
            Date fechaFin = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin());

             /* Solución a los problemas de encoding del formulario GET */
            String nombreInteresado = new String(busqueda.getInteressatNom().getBytes("ISO-8859-1"), "UTF-8");
            String apellido1Interesado = new String(busqueda.getInteressatLli1().getBytes("ISO-8859-1"), "UTF-8");
            String apellido2Interesado = new String(busqueda.getInteressatLli2().getBytes("ISO-8859-1"), "UTF-8");
            Paginacion paginacion = registroEntradaEjb.busqueda(busqueda.getPageNumber(), busqueda.getFechaInicio(), fechaFin, registroEntrada, nombreInteresado, apellido1Interesado, apellido2Interesado, busqueda.getInteressatDoc(), busqueda.getOrganDestinatari(), busqueda.getAnexos(), busqueda.getObservaciones(), busqueda.getUsuario(), usuarioEntidad.getEntidad().getId());

            busqueda.setPageNumber(1);
            mav.addObject("paginacion", paginacion);
            mav.addObject("isAdministradorLibro", permisoLibroUsuarioEjb.isAdministradorLibro(getUsuarioEntidadActivo(request).getId(), registroEntrada.getLibro().getId()));
            mav.addObject("puedeEditar", permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroEntrada.getLibro().getId(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA));

            // Alta en tabla LOPD
            lopdEjb.insertarRegistros(paginacion, usuarioEntidad.getId(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_LISTADO);
        }

        // Comprobamos si el Organismo destinatario es externo, para añadirlo a la lista.
        if (StringUtils.isNotEmpty(busqueda.getOrganDestinatari())) {
            Organismo org = organismoEjb.findByCodigoEntidad(busqueda.getOrganDestinatari(), usuarioEntidad.getEntidad().getId());
            if(org== null || !organismosOficinaActiva.contains(org)){ //Es organismo externo, lo añadimos a la lista
                organismosOficinaActiva.add(new Organismo(null,busqueda.getOrganDestinatari(),new String(busqueda.getOrganDestinatariNom().getBytes("ISO-8859-1"), "UTF-8") ));
            }
        }

        mav.addObject("organosDestino",  organismosOficinaActiva);
        mav.addObject("oficinaActiva",oficinaActiva);
        mav.addObject("usuariosEntidad",usuariosEntidad);
        mav.addObject("librosConsulta", librosConsulta);
        mav.addObject("oficinasRegistro", oficinasRegistro);
        mav.addObject("registroEntradaBusqueda", busqueda);
        mav.addObject("organDestinatari", busqueda.getOrganDestinatari());

        /* Solucion a los problemas de encoding del formulario GET */
        busqueda.getRegistroEntrada().getRegistroDetalle().setExtracto(new String(busqueda.getRegistroEntrada().getRegistroDetalle().getExtracto().getBytes("ISO-8859-1"), "UTF-8"));
        busqueda.setObservaciones(new String(busqueda.getObservaciones().getBytes("ISO-8859-1"), "UTF-8"));
        busqueda.setInteressatNom(new String(busqueda.getInteressatNom().getBytes("ISO-8859-1"), "UTF-8"));
        busqueda.setInteressatLli1(new String(busqueda.getInteressatLli1().getBytes("ISO-8859-1"), "UTF-8"));
        busqueda.setInteressatLli2(new String(busqueda.getInteressatLli2().getBytes("ISO-8859-1"), "UTF-8"));
        busqueda.setOrganDestinatariNom(new String(busqueda.getOrganDestinatariNom().getBytes("ISO-8859-1"), "UTF-8"));

        return mav;

    }



    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/detalle", method = RequestMethod.GET)
    public String detalleRegistroEntrada(@PathVariable Long idRegistro, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception, I18NException {

        RegistroEntrada registro = registroEntradaEjb.findById(idRegistro);
        Entidad entidadActiva = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Oficina oficinaActiva = getOficinaActiva(request);
        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));
        Oficio oficio = new Oficio(false,false, false, false);

        model.addAttribute("registro",registro);
        model.addAttribute("oficina", oficinaActiva);
        model.addAttribute("entidadActiva", entidadActiva);

        // Modelo Recibo
        //model.addAttribute("modeloRecibo", new ModeloForm());
        //model.addAttribute("modelosRecibo", modeloReciboEjb.getByEntidad(entidadActiva.getId()));

        // Permisos
        Boolean oficinaRegistral = registro.getOficina().getId().equals(oficinaActiva.getId()) || (registro.getOficina().getOficinaResponsable() != null && registro.getOficina().getOficinaResponsable().getId().equals(oficinaActiva.getId()));
        Boolean tieneJustificante = registro.getRegistroDetalle().getTieneJustificante();
        Boolean puedeEditar = permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registro.getLibro().getId(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA);
        model.addAttribute("oficinaRegistral", oficinaRegistral);
        model.addAttribute("isAdministradorLibro", permisoLibroUsuarioEjb.isAdministradorLibro(getUsuarioEntidadActivo(request).getId(), registro.getLibro().getId()));
        model.addAttribute("puedeEditar", puedeEditar);
        model.addAttribute("puedeDistribuir", permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registro.getLibro().getId(), RegwebConstantes.PERMISO_DISTRIBUCION_REGISTRO));
        model.addAttribute("isDistribuir", registroEntradaEjb.isDistribuir(idRegistro, getOrganismosOficioRemision(request,organismosOficinaActiva)));
        model.addAttribute("tieneJustificante", tieneJustificante);
        model.addAttribute("maxReintentos", PropiedadGlobalUtil.getMaxReintentosSir(entidadActiva.getId()));

        // Solo si no es una reserva de número
        if(!registro.getEstado().equals(RegwebConstantes.REGISTRO_RESERVA)){

            // Oficio Remision
            if(entidadActiva.getOficioRemision()){
                oficio = oficioRemisionEntradaUtilsEjb.isOficio(idRegistro, getOrganismosOficioRemision(request, organismosOficinaActiva), entidadActiva);
                if(oficio.getSir()) { // Mensajes de limitaciones anexos si es oficio de remisión sir
                    initMensajeNotaInformativaAnexos(entidadActiva, model);
                }
            }
            model.addAttribute("oficio", oficio);

            // Anexos
            Boolean anexosCompleto = (registro.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO) || registro.getEstado().equals(RegwebConstantes.REGISTRO_PENDIENTE_VISAR))&& oficinaRegistral && puedeEditar && !tieneJustificante;
            if(anexosCompleto) { // Si se muestran los anexos completo

                List<AnexoFull> anexos = anexoEjb.getByRegistroEntrada(registro); //Inicializamos los anexos del registro de entrada.
                initScanAnexos(entidadActiva, model, request, registro.getId()); // Inicializa los atributos para escanear anexos

                // Si es SIR, se validan los tamaños y tipos de anexos
                if(oficio.getSir()){

                    model.addAttribute("erroresAnexosSir", AnexoUtils.validarAnexosSir(anexos));
                }
                model.addAttribute("anexos", anexos);
                model.addAttribute("anexoDetachedPermitido", PropiedadGlobalUtil.getPermitirAnexosDetached(entidadActiva.getId()));
            }
            model.addAttribute("anexosCompleto" , anexosCompleto);

            // Interesados
            if(registro.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO) && oficinaRegistral && !tieneJustificante){

                initDatosInteresados(model, organismosOficinaActiva);
            }

            // Justificante
            if(tieneJustificante){

                model.addAttribute("idJustificante", anexoEjb.getIdJustificante(registro.getRegistroDetalle().getId()));
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
        RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);
        Oficina oficinaActiva = getOficinaActiva(request);

        if(!oficinaActiva.getSirEnvio()){
            log.info("La oficinaActiva no está integrada en SIR");
            Mensaje.saveMessageError(request, getMessage("aviso.oficinaActiva.sir"));
            return new ModelAndView("redirect:/registroEntrada/" + idRegistro + "/detalle");
        }

        List<OficinaTF> oficinasSIR = oficioRemisionEntradaUtilsEjb.isOficioRemisionSir(idRegistro);

        if(oficinasSIR.isEmpty()){
            log.info("Este registro no se puede enviar via SIR, no tiene oficinas");
            Mensaje.saveMessageError(request, getMessage("registroSir.error.envio.oficinas"));
            return new ModelAndView("redirect:/registroEntrada/" + idRegistro + "/detalle");
        }

        mav.addObject("tipoRegistro", RegwebConstantes.REGISTRO_ENTRADA_ESCRITO_CASTELLANO);
        mav.addObject("envioSirForm", new EnvioSirForm());
        mav.addObject("registro", registroEntrada);
        mav.addObject("oficinasSIR", oficinasSIR);
        mav.addObject("destino", registroEntrada.getDestinoExternoDenominacion());

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

            sirEjb.enviarFicheroIntercambio(RegwebConstantes.REGISTRO_ENTRADA_ESCRITO, idRegistro, getOficinaActiva(request), usuarioEntidad, oficinaSIRCodigo);
            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.envioSir.ok"));
            jsonResponse.setStatus("SUCCESS");

        } catch (SIRException se){
            log.info(getMessage("registroSir.error.envio"));
            jsonResponse.setStatus("FAIL");
            jsonResponse.setError(getMessage("registroSir.error.envio") + ": " + se.getMessage());
            se.printStackTrace();
        } catch (Exception e) {
            log.info(getMessage("registroSir.error.envio"));
            jsonResponse.setStatus("FAIL");
            jsonResponse.setError(getMessage("registroSir.error.envio") + ": " + e.getMessage());
            e.printStackTrace();
        } catch (I18NException e) {
            log.info(getMessage("registroSir.error.envio"));
            jsonResponse.setStatus("FAIL");
            jsonResponse.setError(getMessage("registroSir.error.envio") + ": " + I18NUtils.getMessage(e));
            e.printStackTrace();
        }  catch (I18NValidationException ve) {
            log.info(getMessage("registroSir.error.envio"));
            jsonResponse.setStatus("FAIL");
            jsonResponse.setError(getMessage("registroSir.error.envio") + ": " + I18NUtils.getMessage(ve));
            ve.printStackTrace();
        }

        return jsonResponse;
    }

    @RequestMapping(value = "/{idRegistro}/reenviar", method = RequestMethod.GET)
    public String reenviarRegistroEntrada(@PathVariable Long idRegistro, Model model, HttpServletRequest request) throws Exception {

        model.addAttribute("tipoRegistro", RegwebConstantes.REGISTRO_ENTRADA_ESCRITO_CASTELLANO);
        model.addAttribute("comunidadesAutonomas", catComunidadAutonomaEjb.getAll());
        model.addAttribute("nivelesAdministracion", catNivelAdministracionEjb.getAll());
        model.addAttribute("registro", registroEntradaEjb.findById(idRegistro));
        model.addAttribute("reenviarForm", new ReenviarForm());

        return "registro/reenvioSir";
    }

    /**
     * Reenvia un {@link RegistroSir}
     */
    @RequestMapping(value = "/{idRegistro}/reenviar", method = RequestMethod.POST)
    public String reenviarRegistroEntrada(@PathVariable Long idRegistro, @ModelAttribute ReenviarForm reenviarForm , HttpServletRequest request)
            throws Exception, I18NException, I18NValidationException {

        log.info("Oficina Destino reenvio: " + reenviarForm.getCodigoOficina());

        //Montamos la oficina de reenvio seleccionada por el usuario
        Oficina oficinaReenvio = reenviarForm.oficinaReenvio();
        Oficina oficinaActiva = getOficinaActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        // Reenvia el RegistroSir
        try{
            if(oficinaReenvio != null){//Si han seleccionado oficina de reenvio
                //Reenviamos
                sirEjb.reenviarRegistro(RegwebConstantes.REGISTRO_ENTRADA_ESCRITO, idRegistro, oficinaReenvio, oficinaActiva,usuarioEntidad,reenviarForm.getObservaciones());
            }

            Mensaje.saveMessageInfo(request, getMessage("registroSir.reenvio.ok"));

        }catch (Exception e){
            Mensaje.saveMessageError(request, getMessage("registroSir.error.reenvio"));
            e.printStackTrace();
        }

        return "redirect:/registroEntrada/"+idRegistro+"/detalle";
    }

    @RequestMapping(value = "/reservas/list/{pageNumber}")
    public ModelAndView reservas(@PathVariable Integer pageNumber, HttpServletRequest request) throws Exception{

        ModelAndView mav = new ModelAndView("registroEntrada/registrosEntradaEstado");

        Oficina oficinaActiva = getOficinaActiva(request);

        if(isOperador(request) && oficinaActiva != null) {

            Paginacion paginacion = registroEntradaEjb.getByOficinaEstadoPaginado(pageNumber,oficinaActiva.getId(),RegwebConstantes.REGISTRO_RESERVA);

            mav.addObject("titulo", getMessage("registroEntrada.listado.reservas"));
            mav.addObject("url", "reservas");
            mav.addObject("paginacion", paginacion);

        }

        return mav;
    }

    @RequestMapping(value = "/validos/list/{pageNumber}")
    public ModelAndView validos(@PathVariable Integer pageNumber, HttpServletRequest request) throws Exception{

        ModelAndView mav = new ModelAndView("registroEntrada/registrosEntradaEstado");

        Oficina oficinaActiva = getOficinaActiva(request);

        if(isOperador(request) && oficinaActiva != null) {

            Paginacion paginacion = registroEntradaEjb.getByOficinaEstadoPaginado(pageNumber,oficinaActiva.getId(),RegwebConstantes.REGISTRO_VALIDO);

            mav.addObject("titulo", getMessage("registroEntrada.listado.pendientesDistribuir"));
            mav.addObject("url", "validos");
            mav.addObject("paginacion", paginacion);

        }

        return mav;
    }

    @RequestMapping(value = "/pendientesSir/list/{pageNumber}")
    public ModelAndView pendientesSir(@PathVariable Integer pageNumber, HttpServletRequest request) throws Exception{

        ModelAndView mav = new ModelAndView("registroEntrada/registrosEntradaEstado");

        Oficina oficinaActiva = getOficinaActiva(request);

        if(isOperador(request) && oficinaActiva != null) {

            Paginacion paginacion = registroEntradaEjb.getSirRechazadosReenviadosPaginado(pageNumber,oficinaActiva.getId());

            mav.addObject("titulo", getMessage("registroEntrada.listado.pendientesSir"));
            mav.addObject("url", "pendientesSir");
            mav.addObject("paginacion", paginacion);

        }

        return mav;
    }


    @RequestMapping(value = "/pendientesVisar/list/{pageNumber}", method = RequestMethod.GET)
    public ModelAndView pendientesVisar(@PathVariable Integer pageNumber, HttpServletRequest request) throws Exception{

        ModelAndView mav = new ModelAndView("registro/pendientesVisarList");

        List<Libro> librosAdministrados = getLibrosAdministrados(request);

        if((librosAdministrados!= null && librosAdministrados.size() > 0)) {

            List<RegistroEntrada> registrosEntrada = registroEntradaEjb.getByLibrosEstado((pageNumber-1)* BaseEjbJPA.RESULTADOS_PAGINACION, librosAdministrados, RegwebConstantes.REGISTRO_PENDIENTE_VISAR);
            Long totalVisarEntrada = registroEntradaEjb.getByLibrosEstadoCount(librosAdministrados, RegwebConstantes.REGISTRO_PENDIENTE_VISAR);
            Paginacion paginacion = new Paginacion(totalVisarEntrada.intValue(), pageNumber);

            mav.addObject("titulo",getMessage("registroEntrada.pendientesVisar"));
            mav.addObject("paginacion", paginacion);
            mav.addObject("listado", registrosEntrada);
            mav.addObject("tipoRegistro", RegwebConstantes.REGISTRO_ENTRADA_ESCRITO_CASTELLANO);
        }

        return mav;
    }


    /**
     * Anular un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/anular")
    public String anularRegistroEntrada(@PathVariable Long idRegistro, HttpServletRequest request) {

        try {

            RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            final List<Long> estados = new ArrayList<Long>();
            estados.add(RegwebConstantes.REGISTRO_RESERVA);
            estados.add(RegwebConstantes.REGISTRO_VALIDO);
            estados.add(RegwebConstantes.REGISTRO_PENDIENTE_VISAR);

            // Comprobamos si el RegistroEntrada se puede anular según su estado.
            if(!estados.contains(registroEntrada.getEstado())){
                Mensaje.saveMessageError(request, getMessage("registroEntrada.anulado"));
                return "redirect:/registroEntrada/list";
            }

            // Comprobamos que el usuario dispone del permiso para Modificar el Registro
            if(!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(),registroEntrada.getLibro().getId(),RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA)){
                log.info("Aviso: No dispone de los permisos necesarios para editar el registro");
                Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));

                return "redirect:/registroEntrada/list";
            }

            // Anulamos el RegistroEntrada
            registroEntradaEjb.anularRegistroEntrada(registroEntrada,usuarioEntidad);

            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.anular"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            e.printStackTrace();
        }

        return "redirect:/registroEntrada/"+idRegistro+"/detalle";
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
            if(!registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_ANULADO)){

                Mensaje.saveMessageError(request, getMessage("registro.activar.error"));
                return "redirect:/registroEntrada/list";
            }

            // Comprobamos que el usuario dispone del permiso para Modificar el Registro
            if(!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroEntrada.getLibro().getId(),RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA)){
                log.info("Aviso: No dispone de los permisos necesarios para editar el registro");
                Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));

                return "redirect:/registroEntrada/list";
            }

            // Si era una reserva de número no lo activamos, lo volvemos a poner Pendiente
            if(registroEntrada.getDestino() == null && registroEntrada.getDestinoExternoCodigo() == null){
                registroEntradaEjb.cambiarEstadoHistorico(registroEntrada,RegwebConstantes.REGISTRO_RESERVA,usuarioEntidad);
            }else{
                // Activamos el RegistroEntrada
                registroEntradaEjb.activarRegistroEntrada(registroEntrada, usuarioEntidad);
            }

            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.activar"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            e.printStackTrace();
        }

        return "redirect:/registroEntrada/"+idRegistro+"/detalle";
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
            if(!registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_PENDIENTE_VISAR)){

                Mensaje.saveMessageError(request, getMessage("registro.visar.error"));
                return "redirect:/registroEntrada/list";
            }

            // Comprobamos si el UsuarioEntidad tiene permisos para visar el Registro Entrada
            if(!permisoLibroUsuarioEjb.isAdministradorLibro(usuarioEntidad.getId(), registroEntrada.getLibro().getId())){

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

        return "redirect:/registroEntrada/"+idRegistro+"/detalle";
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
    JsonResponse distribuirRegistroEntrada(@PathVariable Long idRegistro, HttpServletRequest request) throws Exception, I18NException,I18NValidationException {

        RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFull(idRegistro);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        RespuestaDistribucion respuestaDistribucion = new RespuestaDistribucion();

        JsonResponse respuesta = new JsonResponse();

        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

        // Comprobamos si el RegistroEntrada tiene el estado Válido
        if (!registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO)) {
            respuesta.setStatus("FAIL_NOVALIDO");
            respuesta.setError(getMessage("registroEntrada.distribuir.error.novalido"));
            respuesta.setResult(respuestaDistribucion);
        }

        // Comprobamos que el usuario tiene permisos para Distribuir el registro
        if(!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroEntrada.getLibro().getId(), RegwebConstantes.PERMISO_DISTRIBUCION_REGISTRO)){
            respuesta.setStatus("FAIL_NOPERMISOS");
            respuesta.setError(getMessage("registroEntrada.distribuir.error.nopermisos"));
            respuesta.setResult(respuestaDistribucion);
        }

        // Comprobamos que el RegistroEntrada se puede Distribuir
        if (!registroEntradaEjb.isDistribuir(idRegistro, getOrganismosOficioRemision(request, organismosOficinaActiva))) {
            respuesta.setStatus("FAIL_NOISDISTRIBUIR");
            respuesta.setError(getMessage("registroEntrada.distribuir.error.noIsdistribuir"));
            respuesta.setResult(respuestaDistribucion);
        }

        //Obtenemos los destinatarios a través del plugin de distribución
        try {
            // Miramos si debemos generar el justificante
            AnexoFull justificante = null;
            if(!registroEntrada.getRegistroDetalle().getTieneJustificante()) {
                justificante = justificanteEjb.crearJustificante(registroEntrada.getUsuario(), registroEntrada, RegwebConstantes.REGISTRO_ENTRADA_ESCRITO.toLowerCase(), RegwebConstantes.IDIOMA_CATALAN_CODIGO);
                registroEntrada.getRegistroDetalle().getAnexosFull().add(justificante);
            }
            respuestaDistribucion = registroEntradaEjb.distribuir(registroEntrada, usuarioEntidad);

            //Definimos los distintos estados de la respuesta para enviar al distribuir.js
            //No hay plugin configurado
            if(!respuestaDistribucion.getHayPlugin()){
                respuesta.setStatus("NO_HAY_PLUGIN");
            }
            //Miramos si se ha enviado
            if(respuestaDistribucion.getEnviado()){
                respuesta.setStatus("ENVIADO");
            }
            //Miramos si la lista de destinatarios es modificable
            if(!respuestaDistribucion.getListadoDestinatariosModificable()){
                respuesta.setStatus("DESTIN_NO_MODIFICABLE");
                respuesta.setError(getMessage("registroEntrada.distribuir.error.noModificable"));
            }
            //Miramos si no se ha enviado
            if(!respuestaDistribucion.getEnviado()){
                respuesta.setStatus("NO_ENVIADO");
                respuesta.setError(getMessage("registroEntrada.distribuir.error.noEnviado"));
            }
            respuesta.setResult(respuestaDistribucion);

        } catch (I18NValidationException e) {
            e.printStackTrace();
            respuesta.setStatus("FAIL");
            respuesta.setError(I18NUtils.getMessage(e));
            respuesta.setResult(respuestaDistribucion);
            return respuesta;
        } catch (I18NException ie) {
            ie.printStackTrace();
            respuesta.setStatus("FAIL");
            respuesta.setError(I18NUtils.getMessage(ie));
            respuesta.setResult(respuestaDistribucion);
            return respuesta;
        } catch(SocketTimeoutException ste){
            ste.printStackTrace();
            respuesta.setStatus("FAIL");
            respuesta.setError(ste.getMessage());
            respuesta.setResult(respuestaDistribucion);
            return respuesta;
        } catch (Exception iie){
            iie.printStackTrace();
            respuesta.setStatus("FAIL");
            respuesta.setError(iie.getMessage());
            respuesta.setResult(respuestaDistribucion);
            return respuesta;
        }

        //Si ha ido bien, marcamos como SUCCESS
        if(!respuestaDistribucion.getHayPlugin() || respuestaDistribucion.getEnviado()){
            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.distribuir.ok"));
            respuesta.setStatus("SUCCESS");
        }

        // TODO eliminar referencia de custodia en los anexos si plugin arxiu digital

        return respuesta;
    }

    /**
     * Método que envia el registro de entrada a los destinatarios indicados y modifica el estado del registro
     * a tramitado
     * @param idRegistro identificador del registro de entrada
     * @param wrapper contendrá los destinatarios seleccionados
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idRegistro}/enviarDestinatarios", method = RequestMethod.POST)
    public
    @ResponseBody
    Boolean enviarDestinatariosRegistroEntrada(@PathVariable Long idRegistro, 
        @RequestBody DestinatarioWrapper wrapper, HttpServletRequest request)
            throws Exception, I18NException, I18NValidationException {

        log.info("Entramos en enviarDestinatariosRegistroEntrada");

        RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        log.info("Destinatarios obtenidos: " + wrapper.getDestinatarios().size());
        log.info(" Observaciones obtenidas:" + wrapper.getObservaciones());

        // Enviamos el registro de entrada a los destinatarios indicados en la variable wrapper
        Boolean enviado = registroEntradaEjb.enviar(registroEntrada, wrapper,
            usuarioEntidad.getEntidad().getId(), 
            RegwebConstantes.CODIGO_BY_IDIOMA_ID.get(usuarioEntidad.getUsuario().getIdioma()));

        if (enviado) { //Mostramos mensaje en funcion de si se ha enviado o ha habido un error.
            // Marcamos el registro como tramitado, solo si se ha enviado bien
            try {
                registroEntradaEjb.tramitarRegistroEntrada(registroEntrada, usuarioEntidad);
            } catch (I18NValidationException e) {
                Mensaje.saveMessageInfo(request, getMessage("registroEntrada.distribuir.error.noEnviado")+": "+I18NUtils.getMessage(e));
            }
            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.distribuir.ok"));

        } else {
            Mensaje.saveMessageError(request, getMessage("registroEntrada.distribuir.error.noEnviado"));
        }
        return enviado;

    }


    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/sello", method = RequestMethod.POST)
    public ModelAndView sello(@PathVariable Long idRegistro, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("sello");

        RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);

        //Si el registro no está Anulado ni Pendiente de Visar
        if(!registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_ANULADO) &&
                !registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_PENDIENTE_VISAR)) {

            mav.addObject("registro", registroEntrada);
            mav.addObject("x", request.getParameter("x"));
            mav.addObject("y", request.getParameter("y"));
            mav.addObject("orientacion", request.getParameter("orientacion"));
            mav.addObject("tipoRegistro", getMessage("informe.entrada"));

            return mav;

        }else{
            Mensaje.saveMessageError(request, getMessage("aviso.sello.noImprimible"));
            return new ModelAndView("redirect:/registroEntrada/"+idRegistro+"/detalle");
        }

    }


    /**
     * Método que genera el Justificante en pdf
     * @param idRegistro identificador del registro de entrada
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/{idRegistro}/justificante/{idioma}", method=RequestMethod.POST)
    public JsonResponse justificante(@PathVariable Long idRegistro, @PathVariable String idioma, HttpServletRequest request)
            throws Exception {

        JsonResponse jsonResponse = new JsonResponse();

        try {

            synchronized (this) {

                RegistroEntrada registroEntrada = registroEntradaEjb.getConAnexosFull(idRegistro);
                UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

                // Dispone de permisos para Editar el registro
                if (permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroEntrada.getLibro().getId(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA) && !registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_ANULADO)) {

                    // Creamos el anexo justificante y lo firmamos
                    AnexoFull anexoFull = justificanteEjb.crearJustificante(usuarioEntidad, registroEntrada, RegwebConstantes.REGISTRO_ENTRADA_ESCRITO.toLowerCase(), idioma);

                    // Alta en tabla LOPD
                    if (anexoFull != null) {
                        lopdEjb.altaLopd(registroEntrada.getNumeroRegistro(), registroEntrada.getFecha(), registroEntrada.getLibro().getId(), usuarioEntidad.getId(), RegwebConstantes.REGISTRO_ENTRADA, RegwebConstantes.LOPD_JUSTIFICANTE);
                    }

                    jsonResponse.setStatus("SUCCESS");

                } else {
                    jsonResponse.setStatus("FAIL");
                    jsonResponse.setError(getMessage("aviso.registro.editar"));
                }
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
        binder.registerCustomEditor(java.util.Date.class,dateEditor);
    }

}