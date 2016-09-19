package es.caib.regweb3.webapp.controller.registro;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.AnexoLocal;
import es.caib.regweb3.persistence.ejb.HistoricoRegistroEntradaLocal;
import es.caib.regweb3.persistence.ejb.OficioRemisionUtilsLocal;
import es.caib.regweb3.persistence.ejb.RegistroEntradaLocal;
import es.caib.regweb3.persistence.utils.DestinatarioWrapper;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.persistence.utils.RespuestaDistribucion;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.webapp.form.RegistroEntradaBusqueda;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.RegistroEntradaBusquedaValidator;
import org.fundaciobit.genapp.common.i18n.I18NException;
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
    public HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;
    
    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/AnexoEJB/local")
    public AnexoLocal anexoEjb;

    @EJB(mappedName = "regweb3/OficioRemisionUtilsEJB/local")
    public OficioRemisionUtilsLocal oficioRemisionUtilsEjb;
    

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

        model.addAttribute(getOficinaActiva(request));
        model.addAttribute("librosConsulta", librosConsulta);
        model.addAttribute("registroEntradaBusqueda", registroEntradaBusqueda);
        model.addAttribute("organosDestino", organismosOficinaActiva);
        model.addAttribute("oficinasRegistro",  oficinaEjb.findByEntidadByEstado(getEntidadActiva(request).getId(),RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));

        // Obtenemos los usuarios de la Entidad
        List<UsuarioEntidad> usuariosEntidad = usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId());
        model.addAttribute("usuariosEntidad", usuariosEntidad);

        return "registroEntrada/registroEntradaList";
    }

    /**
    * Realiza la busqueda de {@link es.caib.regweb3.model.RegistroEntrada} según los parametros del formulario
    */
    @RequestMapping(value = "/busqueda", method = RequestMethod.GET)
    public ModelAndView busqueda(@ModelAttribute RegistroEntradaBusqueda busqueda, BindingResult result, HttpServletRequest request, HttpServletResponse response)throws Exception {

        ModelAndView mav = new ModelAndView("registroEntrada/registroEntradaList", result.getModel());

        registroEntradaBusquedaValidator.validate(busqueda, result);

        RegistroEntrada registroEntrada = busqueda.getRegistroEntrada();
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        
        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            mav.addObject("errors", result.getAllErrors());
            return mav;
        }else { // Si no hay errores realizamos la búsqueda
            // Ponemos la hora 23:59 a la fecha fin
            Date fechaFin = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin());

            // Quitamos acentos al string del Nombre Interesado
            String nombreInteresado = limpiarCaracteresEspeciales(busqueda.getInteressatNom());
            String apellido1Interesado = limpiarCaracteresEspeciales(busqueda.getInteressatLli1());
            String apellido2Interesado = limpiarCaracteresEspeciales(busqueda.getInteressatLli2());
            Paginacion paginacion = registroEntradaEjb.busqueda(busqueda.getPageNumber(), busqueda.getFechaInicio(), fechaFin, registroEntrada, nombreInteresado, apellido1Interesado, apellido2Interesado, busqueda.getInteressatDoc(), busqueda.getOrganDestinatari(), busqueda.getAnexos(), busqueda.getObservaciones(), busqueda.getUsuario(), usuarioEntidad.getEntidad().getId());

            busqueda.setPageNumber(1);
            mav.addObject("paginacion", paginacion);
            mav.addObject("isAdministradorLibro", permisoLibroUsuarioEjb.isAdministradorLibro(getUsuarioEntidadActivo(request).getId(), registroEntrada.getLibro().getId()));
            mav.addObject("puedeEditar", permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroEntrada.getLibro().getId(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA));

            // Alta en tabla LOPD
            lopdEjb.insertarRegistrosEntrada(paginacion, usuarioEntidad.getId());
        }

        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

        // Comprobamos si el Organismo destinatario es externo, para añadirlo a la lista.
        if (!StringUtils.isEmpty(busqueda.getOrganDestinatari())) {
            Organismo org = organismoEjb.findByCodigoEntidad(busqueda.getOrganDestinatari(), usuarioEntidad.getEntidad().getId());
            if(org== null || !organismosOficinaActiva.contains(org)){ //Es organismo externo, lo añadimos a la lista
                organismosOficinaActiva.add(new Organismo(null,busqueda.getOrganDestinatari(),new String(busqueda.getOrganDestinatariNom().getBytes("ISO-8859-1"), "UTF-8") ));
            }
        }

        mav.addObject("organosDestino",  organismosOficinaActiva);
        mav.addObject(getOficinaActiva(request));
        mav.addObject("usuariosEntidad",usuarioEntidadEjb.findByEntidad(getEntidadActiva(request).getId()));
        mav.addObject("librosConsulta", getLibrosConsultaEntradas(request));
        mav.addObject("oficinasRegistro", oficinaEjb.findByEntidadByEstado(getEntidadActiva(request).getId(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));
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
    public String detalleRegistroEntrada(@PathVariable Long idRegistro, Model model, HttpServletRequest request) throws Exception {

        RegistroEntrada registro = registroEntradaEjb.findById(idRegistro);
        Entidad entidadActiva = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Oficina oficinaActiva = getOficinaActiva(request);
        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

        model.addAttribute("registro",registro);
        model.addAttribute("oficina", oficinaActiva);

        // Modelo Recibo
        model.addAttribute("modeloRecibo", new ModeloRecibo());
        model.addAttribute("modelosRecibo", modeloReciboEjb.getByEntidad(entidadActiva.getId()));

        // Permisos
        Boolean oficinaRegistral = registro.getOficina().getId().equals(oficinaActiva.getId()) || (registro.getOficina().getOficinaResponsable() != null && registro.getOficina().getOficinaResponsable().getId().equals(oficinaActiva.getId()));
        model.addAttribute("oficinaRegistral", oficinaRegistral);
        model.addAttribute("isAdministradorLibro", permisoLibroUsuarioEjb.isAdministradorLibro(getUsuarioEntidadActivo(request).getId(), registro.getLibro().getId()));
        model.addAttribute("puedeEditar", permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registro.getLibro().getId(), RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA));

        // OficioRemision
        model.addAttribute("isOficioRemision", oficioRemisionUtilsEjb.isOficioRemisionInterno(idRegistro, getOrganismosOficioRemision(request, organismosOficinaActiva)));

        // Interesados, solo si el Registro en Válido o Estamos en la Oficina donde se registró, o en su Oficina Responsable
        if(registro.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO) && oficinaRegistral){

            model.addAttribute("tiposInteresado", RegwebConstantes.TIPOS_INTERESADO);
            model.addAttribute("tiposPersona",RegwebConstantes.TIPOS_PERSONA);
            model.addAttribute("paises",catPaisEjb.getAll());
            model.addAttribute("provincias",catProvinciaEjb.getAll());
            model.addAttribute("canalesNotificacion",RegwebConstantes.CANALES_NOTIFICACION);
            model.addAttribute("tiposDocumento", RegwebConstantes.TIPOS_DOCUMENTOID);
            model.addAttribute("organismosOficinaActiva",organismosOficinaActiva);
        }

        // Anexos
        model.addAttribute("anexos", anexoEjb.getByRegistroEntrada(idRegistro));
        initAnexos(entidadActiva, model, request, registro.getId());

        // Historicos
        model.addAttribute("historicos", historicoRegistroEntradaEjb.getByRegistroEntrada(idRegistro));

        // Trazabilidad
        model.addAttribute("trazabilidades", trazabilidadEjb.getByRegistroEntrada(registro.getId()));

        // Posicion sello
        if (entidadActiva.getPosXsello() != null && entidadActiva.getPosYsello() != null) {
            model.addAttribute("posXsello", entidadActiva.getPosXsello());
            model.addAttribute("posYsello", entidadActiva.getPosYsello());
        }

        // Alta en tabla LOPD
        lopdEjb.insertarRegistroEntrada(registro.getNumeroRegistro(), registro.getFecha(), registro.getLibro().getId(), usuarioEntidad.getId());

        return "registroEntrada/registroEntradaDetalle";
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
            estados.add(RegwebConstantes.REGISTRO_PENDIENTE);
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
                registroEntradaEjb.cambiarEstado(registroEntrada,RegwebConstantes.REGISTRO_PENDIENTE,usuarioEntidad);
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
     * Tramitar un {@link es.caib.regweb3.model.RegistroEntrada}
     * OLD
     */
    @RequestMapping(value = "/{idRegistro}/tramitar")
    public String tramitarRegistroEntrada(@PathVariable Long idRegistro, HttpServletRequest request) {

        log.info("Llegamos a tramitar");
        try {

            RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
            LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));


            // Comprobamos si el RegistroEntrada tiene el estado Válido
            if(!registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO)){

                Mensaje.saveMessageError(request, getMessage("registroEntrada.tramitar.error"));
                return "redirect:/registroEntrada/list";
            }

            // Comprobamos que el RegistroEntrada es un OficioRemision
            if (oficioRemisionUtilsEjb.isOficioRemisionInterno(idRegistro, getOrganismosOficioRemision(request,organismosOficinaActiva))) {
                Mensaje.saveMessageError(request, getMessage("registroEntrada.tramitar.error"));
                return "redirect:/registroEntrada/list";
            }


            registroEntradaEjb.tramitarRegistroEntrada(registroEntrada, usuarioEntidad);
            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.tramitar.ok"));

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
    RespuestaDistribucion distribuirRegistroEntrada(@PathVariable Long idRegistro, HttpServletRequest request) throws Exception, I18NException {

        log.info("Entramos en distribuirRegistroEntrada");

        RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);
        RespuestaDistribucion respuestaDistribucion = new RespuestaDistribucion();
        respuestaDistribucion.setDestinatarios(null);

        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

        // Comprobamos si el RegistroEntrada tiene el estado Válido
        if (!registroEntrada.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO)) {

            Mensaje.saveMessageError(request, getMessage("registroEntrada.distribuir.error"));
            return respuestaDistribucion;
        }

        // Comprobamos que el RegistroEntrada es un OficioRemision
        if (oficioRemisionUtilsEjb.isOficioRemisionInterno(idRegistro, getOrganismosOficioRemision(request, organismosOficinaActiva))) {
            Mensaje.saveMessageError(request, getMessage("registroEntrada.distribuir.error"));
            return respuestaDistribucion;
        }

        //Obtenemos los destinatarios a través del plugin de distribución
        respuestaDistribucion = registroEntradaEjb.distribuir(registroEntrada);
        return respuestaDistribucion;
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
    @RequestMapping(value = "/{idRegistro}/enviardestinatarios", method = RequestMethod.POST)
    public
    @ResponseBody
    Boolean enviarDestinatariosRegistroEntrada(@PathVariable Long idRegistro, @RequestBody DestinatarioWrapper wrapper, HttpServletRequest request) throws Exception, I18NException {

        log.info("Entramos en enviarDestinatariosRegistroEntrada");

        RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        log.info("Destinatarios obtenidos: " + wrapper.getDestinatarios().size());
        log.info(" Observaciones obtenidas:" + wrapper.getObservaciones());

        // Enviamos el registro de entrada a los destinatarios indicados en la variable wrapper
        Boolean enviado = registroEntradaEjb.enviar(registroEntrada, wrapper);

        if (enviado) { //Mostramos mensaje en funcion de si se ha enviado o ha habido un error.
            // Marcamos el registro como tramitado, solo si se ha enviado bien
            registroEntradaEjb.tramitarRegistroEntrada(registroEntrada, usuarioEntidad);
            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.distribuir.ok"));

        } else {
            Mensaje.saveMessageError(request, getMessage("registroEntrada.distribuir.noenviado"));
        }
        return enviado;

    }


    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb3.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/sello", method = RequestMethod.GET)
    public ModelAndView sello(@PathVariable Long idRegistro, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("sello");

        RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);

        mav.addObject("registro", registroEntrada);
        mav.addObject("x", request.getParameter("x"));
        mav.addObject("y", request.getParameter("y"));
        mav.addObject("orientacion", request.getParameter("orientacion"));
        mav.addObject("tipoRegistro", getMessage("informe.entrada"));

        return mav;
    }


    @InitBinder("registroEntradaBusqueda")
    public void registroEntradaBusqueda(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class,dateEditor);
    }



    
   

}