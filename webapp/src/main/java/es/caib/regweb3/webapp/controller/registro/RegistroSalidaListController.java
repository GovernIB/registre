package es.caib.regweb3.webapp.controller.registro;

import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.Oficio;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.sir.core.excepcion.SIRException;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.form.EnvioSirForm;
import es.caib.regweb3.webapp.form.ReenviarForm;
import es.caib.regweb3.webapp.form.RegistroSalidaBusqueda;
import es.caib.regweb3.webapp.utils.AnexoUtils;
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
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 * Controller para gestionar los Registros de Salida
 * @author earrivi
 * @author anadal
 * Date: 31/03/14
 */
@Controller
@RequestMapping(value = "/registroSalida")
public class RegistroSalidaListController extends AbstractRegistroCommonListController {


    @Autowired
    private RegistroSalidaBusquedaValidator registroSalidaBusquedaValidator;

    @EJB(mappedName = "regweb3/RegistroSalidaEJB/local")
    public RegistroSalidaLocal registroSalidaEjb;

    @EJB(mappedName = "regweb3/HistoricoRegistroSalidaEJB/local")
    private HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;

    @EJB(mappedName = "regweb3/OficioRemisionSalidaUtilsEJB/local")
    private OficioRemisionSalidaUtilsLocal oficioRemisionSalidaUtilsEjb;

    @EJB(mappedName = "regweb3/AnexoEJB/local")
    private AnexoLocal anexoEjb;

    @EJB(mappedName = "regweb3/SirEJB/local")
    private SirLocal sirEjb;


    /**
     * Listado de todos los Registros de Salida
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String listado() {
        return "redirect:/registroSalida/list";
    }


    /**
     * Listado de registros de salida
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model, HttpServletRequest request)throws Exception {

        // Obtenemos los Libros donde el usuario tiene permisos de Consulta
        List<Libro> librosConsulta = getLibrosConsultaSalidas(request);

        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

        // Fijamos un Libro por defecto
        RegistroSalida registroSalida = new RegistroSalida();
        registroSalida.setLibro(seleccionarLibroOficinaActiva(request,librosConsulta));

        RegistroSalidaBusqueda registroSalidaBusqueda = new RegistroSalidaBusqueda(registroSalida,1);
        registroSalidaBusqueda.setFechaInicio(new Date());
        registroSalidaBusqueda.setFechaFin(new Date());

        model.addAttribute(getOficinaActiva(request));
        model.addAttribute("librosConsulta", librosConsulta);
        model.addAttribute("registroSalidaBusqueda", registroSalidaBusqueda);
        model.addAttribute("organosOrigen", organismosOficinaActiva);
        model.addAttribute("oficinasRegistro",  oficinaEjb.findByEntidadByEstado(getEntidadActiva(request).getId(),RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));

        // Obtenemos los usuarios de la Entidad
        List<UsuarioEntidad> usuariosEntidad = usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId());
        model.addAttribute("usuariosEntidad", usuariosEntidad);

        return "registroSalida/registroSalidaList";

    }

    /**
     * Realiza la busqueda de {@link es.caib.regweb3.model.RegistroSalida} según los parametros del formulario
     */
    @RequestMapping(value = "/busqueda", method = RequestMethod.GET)
    public ModelAndView busqueda(@ModelAttribute RegistroSalidaBusqueda busqueda, BindingResult result,HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("registroSalida/registroSalidaList", result.getModel());

        // Obtenemos los Libros donde el usuario tiene permisos de Consulta
        List<Libro> librosConsulta = getLibrosConsultaSalidas(request);
        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));
        List<Oficina> oficinasRegistro = oficinaEjb.findByEntidadByEstado(getEntidadActiva(request).getId(),RegwebConstantes.ESTADO_ENTIDAD_VIGENTE);
        List<UsuarioEntidad> usuariosEntidad = usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId());
        Oficina oficinaActiva = getOficinaActiva(request);

        registroSalidaBusquedaValidator.validate(busqueda, result);

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        // Si hay errores volvemos a la vista del formulario
        if (result.hasErrors()) {
            mav.addObject("errors", result.getAllErrors());
            mav.addObject("organosOrigen", organismosOficinaActiva);
            mav.addObject("oficinaActiva",oficinaActiva);
            mav.addObject("usuariosEntidad",usuariosEntidad);
            mav.addObject("librosConsulta", librosConsulta);
            mav.addObject("registroSalidaBusqueda", busqueda);
            mav.addObject("oficinasRegistro",  oficinasRegistro);
            mav.addObject("organOrigen", busqueda.getOrganOrigen());

            return mav;
        }else { // Si no hay errores realizamos la búsqueda
            RegistroSalida registroSalida = busqueda.getRegistroSalida();

            // Ponemos la hora 23:59 a la fecha fin
            Date fechaFin = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin());

             /* Solución a los problemas de encoding del formulario GET */
            String nombreInteresado = new String(busqueda.getInteressatNom().getBytes("ISO-8859-1"), "UTF-8");
            String apellido1Interesado = new String(busqueda.getInteressatLli1().getBytes("ISO-8859-1"), "UTF-8");
            String apellido2Interesado = new String(busqueda.getInteressatLli2().getBytes("ISO-8859-1"), "UTF-8");
            Paginacion paginacion = registroSalidaEjb.busqueda(busqueda.getPageNumber(), busqueda.getFechaInicio(), fechaFin, registroSalida, nombreInteresado, apellido1Interesado, apellido2Interesado, busqueda.getInteressatDoc(), busqueda.getOrganOrigen(), busqueda.getAnexos(), busqueda.getObservaciones(), busqueda.getUsuario(), usuarioEntidad.getEntidad().getId());

            busqueda.setPageNumber(1);
            mav.addObject("paginacion", paginacion);
            mav.addObject("isAdministradorLibro", permisoLibroUsuarioEjb.isAdministradorLibro(getUsuarioEntidadActivo(request).getId(), registroSalida.getLibro().getId()));
            mav.addObject("puedeEditar", permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroSalida.getLibro().getId(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_SALIDA));

            // Alta en tabla LOPD
            lopdEjb.insertarRegistros(paginacion, usuarioEntidad.getId(), RegwebConstantes.REGISTRO_SALIDA, RegwebConstantes.LOPD_LISTADO);
        }


        mav.addObject("organosOrigen", organismosOficinaActiva);
        mav.addObject("oficinaActiva",oficinaActiva);
        mav.addObject("usuariosEntidad",usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId()));
        mav.addObject("librosConsulta", librosConsulta);
        mav.addObject("registroSalidaBusqueda", busqueda);
        mav.addObject("oficinasRegistro", oficinaEjb.findByEntidadByEstado(getEntidadActiva(request).getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
        mav.addObject("organOrigen", busqueda.getOrganOrigen());

          /* Solucion a los problemas de encoding del formulario GET */
        busqueda.getRegistroSalida().getRegistroDetalle().setExtracto(new String(busqueda.getRegistroSalida().getRegistroDetalle().getExtracto().getBytes("ISO-8859-1"), "UTF-8"));
        busqueda.setObservaciones(new String(busqueda.getObservaciones().getBytes("ISO-8859-1"), "UTF-8"));
        busqueda.setInteressatNom(new String(busqueda.getInteressatNom().getBytes("ISO-8859-1"), "UTF-8"));
        busqueda.setInteressatLli1(new String(busqueda.getInteressatLli1().getBytes("ISO-8859-1"), "UTF-8"));
        busqueda.setInteressatLli2(new String(busqueda.getInteressatLli2().getBytes("ISO-8859-1"), "UTF-8"));
        busqueda.setOrganOrigenNom(new String(busqueda.getOrganOrigenNom().getBytes("ISO-8859-1"), "UTF-8"));
        return mav;
    }

    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.RegistroSalida}
     */
    @RequestMapping(value = "/{idRegistro}/detalle", method = RequestMethod.GET)
    public String detalleRegistroSalida(@PathVariable Long idRegistro, Model model, HttpServletRequest request) throws Exception, I18NException {

        RegistroSalida registro = registroSalidaEjb.findById(idRegistro);
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
        model.addAttribute("oficinaRegistral", oficinaRegistral);
        model.addAttribute("isAdministradorLibro", permisoLibroUsuarioEjb.isAdministradorLibro(getUsuarioEntidadActivo(request).getId(),registro.getLibro().getId()));
        model.addAttribute("puedeEditar", permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(),registro.getLibro().getId(),RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_SALIDA));
        model.addAttribute("tieneJustificante", tieneJustificante);

        // Oficio Remision
        if(entidadActiva.getOficioRemision()){
            oficio = oficioRemisionSalidaUtilsEjb.isOficio(registro, getOrganismosOficioRemisionSalida(organismosOficinaActiva), entidadActiva);
            if(oficio.getSir()) { // Mensajes de limitaciones anexos si es oficio de remisión sir
                initMensajeNotaInformativaAnexos(entidadActiva, model);
            }
        }
        model.addAttribute("oficio", oficio);

        // Anexos completo
        if(registro.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO) || registro.getEstado().equals(RegwebConstantes.REGISTRO_PENDIENTE_VISAR)
                && oficinaRegistral && !tieneJustificante) { // Si se muestran los anexos

            List<AnexoFull> anexos = anexoEjb.getByRegistroSalida(registro); //Inicializamos los anexos del registro de entrada.
            initScanAnexos(entidadActiva, model, request, registro.getId()); // Inicializa los atributos para escanear anexos

            // Si es SIR, se validan los tamaños y tipos de anexos
            if(oficio.getSir()){

                model.addAttribute("erroresAnexosSir", AnexoUtils.validarAnexosSir(anexos));
            }

            model.addAttribute("anexos", anexos);
        }

        // Interesados, solo si el Registro en Válio
        if(registro.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO) && oficinaRegistral && !tieneJustificante){

            initDatosInteresados(model, organismosOficinaActiva);
        }

        // Justificante
        if(tieneJustificante){
            model.addAttribute("idJustificante", anexoEjb.getIdJustificante(registro.getRegistroDetalle().getId()));
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
        RegistroSalida registroSalida = registroSalidaEjb.findById(idRegistro);
        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));
        Oficina oficinaActiva = getOficinaActiva(request);

        if(!oficinaActiva.getSirEnvio()){
            log.info("La oficinaActiva no está integrada en SIR");
            Mensaje.saveMessageError(request, getMessage("aviso.oficinaActiva.sir"));
            return new ModelAndView("redirect:/registroSalida/" + idRegistro + "/detalle");
        }

        List<OficinaTF> oficinasSIR = oficioRemisionSalidaUtilsEjb.isOficioRemisionSir(registroSalida, getOrganismosOficioRemisionSalida(organismosOficinaActiva));

        if(oficinasSIR.isEmpty()){
            log.info("Este registro no se puede enviar via SIR, no tiene oficinas");
            Mensaje.saveMessageError(request, getMessage("registroSir.error.envio.oficinas"));
            return new ModelAndView("redirect:/registroSalida/" + idRegistro + "/detalle");
        }

        mav.addObject("tipoRegistro", RegwebConstantes.REGISTRO_SALIDA_ESCRITO_CASTELLANO);
        mav.addObject("envioSirForm", new EnvioSirForm());
        mav.addObject("registro", registroSalida);
        mav.addObject("oficinasSIR", oficinasSIR);
        mav.addObject("destino", organismoOficioRemision(registroSalida, getOrganismosOficioRemisionSalida(organismosOficinaActiva)));

        return mav;
    }

    /**
     * Enviar a SIR un Registro de Salida
     */
    @RequestMapping(value = "/{idRegistro}/enviarSir", method = RequestMethod.POST)
    public ModelAndView enviarSir(@ModelAttribute EnvioSirForm envioSirForm, @PathVariable Long idRegistro, HttpServletRequest request) throws Exception {

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        try {

            sirEjb.enviarFicheroIntercambio(RegwebConstantes.REGISTRO_SALIDA_ESCRITO,
                    idRegistro, getOficinaActiva(request), usuarioEntidad,envioSirForm.getOficinaSIRCodigo());
            Mensaje.saveMessageInfo(request, getMessage("registroSalida.envioSir.ok"));

        } catch (SIRException e){
            log.error("Error al cridar a sirEjb.enviarFicheroIntercambio();: "
                    + e.getMessage(), e);
            Mensaje.saveMessageError(request, getMessage("registroSir.error.envio"));
        } catch (I18NException e) {
            log.error(I18NUtils.getMessage(e), e);
            Mensaje.saveMessageError(request, getMessage("registroSir.error.envio"));
        } catch(I18NValidationException ve) {
            log.error(I18NUtils.getMessage(ve), ve);
            Mensaje.saveMessageError(request, getMessage("registroSir.error.envio"));
        }

        return new ModelAndView("redirect:/registroSalida/" + idRegistro + "/detalle");
    }

    @RequestMapping(value = "/{idRegistro}/reenviar", method = RequestMethod.GET)
    public String reenviarRegistroSalida(@PathVariable Long idRegistro, Model model, HttpServletRequest request) throws Exception {

        model.addAttribute("tipoRegistro", RegwebConstantes.REGISTRO_SALIDA_ESCRITO_CASTELLANO);
        model.addAttribute("comunidadesAutonomas", catComunidadAutonomaEjb.getAll());
        model.addAttribute("nivelesAdministracion", catNivelAdministracionEjb.getAll());
        model.addAttribute("registro", registroSalidaEjb.findById(idRegistro));
        model.addAttribute("reenviarForm", new ReenviarForm());

        return "registro/reenvioSir";
    }

    /**
     * Reenvia un {@link RegistroSir}
     */
    @RequestMapping(value = "/{idRegistro}/reenviar", method = RequestMethod.POST)
    public String reenviarRegistroSalida(@PathVariable Long idRegistro, @ModelAttribute ReenviarForm reenviarForm , HttpServletRequest request)
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
                sirEjb.reenviarRegistro(RegwebConstantes.REGISTRO_SALIDA_ESCRITO, idRegistro, oficinaReenvio, oficinaActiva,usuarioEntidad,reenviarForm.getObservaciones());
            }

            Mensaje.saveMessageInfo(request, getMessage("registroSir.reenvio.ok"));

        }catch (Exception e){
            Mensaje.saveMessageError(request, getMessage("registroSir.error.reenvio"));
            e.printStackTrace();
        }

        return "redirect:/registroSalida/"+idRegistro+"/detalle";
    }

    @RequestMapping(value = "/pendientesSir/list/{pageNumber}")
    public ModelAndView pendientesSir(@PathVariable Integer pageNumber, HttpServletRequest request) throws Exception{

        ModelAndView mav = new ModelAndView("registroSalida/registrosSalidaEstado");

        Oficina oficinaActiva = getOficinaActiva(request);

        if(isOperador(request) && oficinaActiva != null) {

            Paginacion paginacion = registroSalidaEjb.getSirRechazadosReenviadosPaginado(pageNumber,oficinaActiva.getId());

            mav.addObject("titulo", getMessage("registroSalida.listado.pendientesSir"));
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

            List<RegistroSalida> registrosSalida = registroSalidaEjb.getByLibrosEstado((pageNumber-1)* BaseEjbJPA.RESULTADOS_PAGINACION, librosAdministrados, RegwebConstantes.REGISTRO_PENDIENTE_VISAR);
            Long totalVisarSalida = registroSalidaEjb.getByLibrosEstadoCount(librosAdministrados, RegwebConstantes.REGISTRO_PENDIENTE_VISAR);
            Paginacion paginacion = new Paginacion(totalVisarSalida.intValue(), pageNumber);

            mav.addObject("titulo",getMessage("registroSalida.pendientesVisar"));
            mav.addObject("paginacion", paginacion);
            mav.addObject("listado", registrosSalida);
            mav.addObject("tipoRegistro", RegwebConstantes.REGISTRO_SALIDA_ESCRITO_CASTELLANO);
        }

        return mav;
    }


    /**
     * Anular un {@link es.caib.regweb3.model.RegistroSalida}
     */
    @RequestMapping(value = "/{idRegistro}/anular")
    public String anularRegistroSalida(@PathVariable Long idRegistro, HttpServletRequest request) {

        try {

            RegistroSalida registroSalida = registroSalidaEjb.findById(idRegistro);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            // Comprobamos si el RegistroSalida se puede anular según su estado.
            if(!registroSalida.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO)){
                Mensaje.saveMessageError(request, getMessage("registroSalida.anulado"));
                return "redirect:/registroSalida/list";
            }

            // Comprobamos que el usuario dispone del permiso para Modificar el Registro
            if(!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(),registroSalida.getLibro().getId(),RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_SALIDA)){
                log.info("Aviso: No dispone de los permisos necesarios para editar el registro");
                Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));

                return "redirect:/registroSalida/list";
            }

            // Anulamos el RegistroSalida
            registroSalidaEjb.anularRegistroSalida(registroSalida,usuarioEntidad);

            Mensaje.saveMessageInfo(request, getMessage("registroSalida.anular"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            e.printStackTrace();
        }

        return "redirect:/registroSalida/"+idRegistro+"/detalle";
    }

    /**
     * Activar un {@link es.caib.regweb3.model.RegistroSalida}
     */
    @RequestMapping(value = "/{idRegistro}/activar")
    public String activarRegistroSalida(@PathVariable Long idRegistro, HttpServletRequest request) {

        try {

            RegistroSalida registroSalida = registroSalidaEjb.findById(idRegistro);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            // Comprobamos si el RegistroSalida tiene el estado anulado
            if(!registroSalida.getEstado().equals(RegwebConstantes.REGISTRO_ANULADO)){

                Mensaje.saveMessageError(request, getMessage("registro.activar.error"));
                return "redirect:/registroSalida/list";
            }

            // Comprobamos que el usuario dispone del permiso para Modificar el Registro
            if(!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroSalida.getLibro().getId(),RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_SALIDA)){
                log.info("Aviso: No dispone de los permisos necesarios para editar el registro");
                Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));

                return "redirect:/registroSalida/list";
            }

            // Activamos el RegistroSalida
            registroSalidaEjb.activarRegistroSalida(registroSalida, usuarioEntidad);

            Mensaje.saveMessageInfo(request, getMessage("registroSalida.activar"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            e.printStackTrace();
        }

        return "redirect:/registroSalida/"+idRegistro+"/detalle";
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
            if(!registroSalida.getEstado().equals(RegwebConstantes.REGISTRO_PENDIENTE_VISAR)){

                Mensaje.saveMessageError(request, getMessage("registro.visar.error"));
                return "redirect:/registroSalida/list";
            }

            // Comprobamos si el UsuarioEntidad tiene permisos para visar el RegistroSalida
            if(!permisoLibroUsuarioEjb.isAdministradorLibro(usuarioEntidad.getId(), registroSalida.getLibro().getId())){

                Mensaje.saveMessageError(request, getMessage("aviso.usuario.visar"));
                return "redirect:/registroSalida/list";
            }

            // Visamos el RegistroSalida
            registroSalidaEjb.visarRegistroSalida(registroSalida,usuarioEntidad);

            Mensaje.saveMessageInfo(request, getMessage("registroSalida.visar.ok"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            e.printStackTrace();
        }

        return "redirect:/registroSalida/"+idRegistro+"/detalle";
    }


    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.RegistroSalida}
     */
    @RequestMapping(value = "/{idRegistro}/sello", method = RequestMethod.POST)
    public ModelAndView sello(@PathVariable Long idRegistro, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("sello");

        RegistroSalida registroSalida = registroSalidaEjb.findById(idRegistro);

        //Si el registro no está Anulado ni Pendiente de Visar
        if(!registroSalida.getEstado().equals(RegwebConstantes.REGISTRO_ANULADO) &&
                !registroSalida.getEstado().equals(RegwebConstantes.REGISTRO_PENDIENTE_VISAR)) {

            mav.addObject("registro", registroSalida);
            mav.addObject("x", request.getParameter("x"));
            mav.addObject("y", request.getParameter("y"));
            mav.addObject("orientacion", request.getParameter("orientacion"));
            mav.addObject("tipoRegistro", getMessage("informe.salida"));

            return mav;

        }else{
            Mensaje.saveMessageError(request, getMessage("aviso.sello.noImprimible"));
            return new ModelAndView("redirect:/registroSalida/"+idRegistro+"/detalle");
        }

    }


    /**
     * Método que genera el Justificante en pdf
     * @param idRegistro identificador del registro de salida
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/{idRegistro}/justificante/{idioma}", method=RequestMethod.GET)
    public ModelAndView justificante(@PathVariable Long idRegistro, @PathVariable String idioma, HttpServletRequest request)
            throws Exception {

        try {
            RegistroSalida registroSalida = registroSalidaEjb.getConAnexosFull(idRegistro);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            // Dispone de permisos para Editar el registro
            if(permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroSalida.getLibro().getId(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_SALIDA)){

                // Creamos el anexo justificante y lo firmamos
                AnexoFull anexoFull = anexoEjb.crearJustificante(usuarioEntidad, registroSalida, RegwebConstantes.REGISTRO_SALIDA_ESCRITO.toLowerCase(), idioma);

                // Alta en tabla LOPD
                if(anexoFull != null){
                    lopdEjb.altaLopd(registroSalida.getNumeroRegistro(), registroSalida.getFecha(), registroSalida.getLibro().getId(), usuarioEntidad.getId(), RegwebConstantes.REGISTRO_SALIDA, RegwebConstantes.LOPD_JUSTIFICANTE);
                }

                // Crea variable de sesión para indicar al Registro Detalle que hay que descargar el justificante
                if(anexoFull.getSignatureCustody()!=null){
                    request.getSession().setAttribute("justificante", true);
                }else {
                    request.getSession().setAttribute("justificante", false);
                }
            }else{
                Mensaje.saveMessageError(request, getMessage("aviso.registro.editar"));
            }


        } catch (I18NException e) {
            Mensaje.saveMessageError(request, I18NUtils.getMessage(e));
        } catch (I18NValidationException ve) {
            Mensaje.saveMessageError(request, I18NUtils.getMessage(ve));
        }


        return new ModelAndView("redirect:/registroSalida/"+idRegistro+"/detalle");

    }

    /**
     * Comprueba si el RegistroSalida es un Oficio de Remisión y obtiene el códigoDir3 del
     * Interesado tipo administración asociado al registro.
     * @param registroSalida
     * @param organismos
     * @return
     * @throws Exception
     */
    private String organismoOficioRemision(RegistroSalida registroSalida, Set<String> organismos) throws Exception{

        List<Interesado> interesados = registroSalida.getRegistroDetalle().getInteresados();

        for (Interesado interesado : interesados) {
            if(interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)){

                if(!organismos.contains(interesado.getCodigoDir3())){

                    return interesado.getRazonSocial();
                }
            }
        }

        return null;
    }

    @InitBinder("registroSalidaBusqueda")
    public void registroSalidaBusqueda(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class,dateEditor);
    }

}