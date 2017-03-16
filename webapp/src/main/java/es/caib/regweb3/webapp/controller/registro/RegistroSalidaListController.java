package es.caib.regweb3.webapp.controller.registro;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.BaseEjbJPA;
import es.caib.regweb3.persistence.ejb.HistoricoRegistroSalidaLocal;
import es.caib.regweb3.persistence.ejb.OficioRemisionSalidaUtilsLocal;
import es.caib.regweb3.persistence.ejb.RegistroSalidaLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RegistroUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.form.ModeloForm;
import es.caib.regweb3.webapp.form.RegistroSalidaBusqueda;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.RegistroSalidaBusquedaValidator;
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
    public HistoricoRegistroSalidaLocal historicoRegistroSalidaEjb;

    @EJB(mappedName = "regweb3/OficioRemisionSalidaUtilsEJB/local")
    private OficioRemisionSalidaUtilsLocal oficioRemisionSalidaUtilsEjb;



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
            lopdEjb.insertarRegistrosSalida(paginacion, usuarioEntidad.getId());
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
    public String detalleRegistroSalida(@PathVariable Long idRegistro, Model model, HttpServletRequest request) throws Exception {

        RegistroSalida registro = registroSalidaEjb.findById(idRegistro);
        Entidad entidadActiva = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);
        Oficina oficinaActiva = getOficinaActiva(request);
        LinkedHashSet<Organismo> organismosOficinaActiva = new LinkedHashSet<Organismo>(getOrganismosOficinaActiva(request));

        model.addAttribute("registro",registro);
        model.addAttribute("oficina", oficinaActiva);

        // Modelo Recibo
        model.addAttribute("modeloRecibo", new ModeloForm());
        model.addAttribute("modelosRecibo", modeloReciboEjb.getByEntidad(entidadActiva.getId()));

        // Permisos
        Boolean oficinaRegistral = registro.getOficina().getId().equals(oficinaActiva.getId()) || (registro.getOficina().getOficinaResponsable() != null && registro.getOficina().getOficinaResponsable().getId().equals(oficinaActiva.getId()));
        model.addAttribute("oficinaRegistral", oficinaRegistral);
        model.addAttribute("isAdministradorLibro", permisoLibroUsuarioEjb.isAdministradorLibro(getUsuarioEntidadActivo(request).getId(),registro.getLibro().getId()));
        model.addAttribute("puedeEditar", permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(),registro.getLibro().getId(),RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_SALIDA));

        // OficioRemision
       /* if(entidadActiva.getOficioRemision()){
            model.addAttribute("isOficioRemisionInterno", oficioRemisionSalidaUtilsEjb.isOficioRemisionInterno(registro, getOrganismosOficioRemisionSalida(request, organismosOficinaActiva)));
            model.addAttribute("isOficioRemisionExterno", oficioRemisionSalidaUtilsEjb.isOficioRemisionExterno(registro, getOrganismosOficioRemisionSalida(request, organismosOficinaActiva)));
        }*/

        // Interesados, solo si el Registro en Válio
        if(registro.getEstado().equals(RegwebConstantes.REGISTRO_VALIDO) && oficinaRegistral){

            model.addAttribute("tiposInteresado",RegwebConstantes.TIPOS_INTERESADO);
            model.addAttribute("tiposPersona", RegwebConstantes.TIPOS_PERSONA);
            model.addAttribute("paises",catPaisEjb.getAll());
            model.addAttribute("provincias",catProvinciaEjb.getAll());
            model.addAttribute("canalesNotificacion", RegwebConstantes.CANALES_NOTIFICACION);
            model.addAttribute("tiposDocumento",RegwebConstantes.TIPOS_DOCUMENTOID);
            model.addAttribute("organismosOficinaActiva",organismosOficinaActiva);

        }


        // Inicializamos si se deben mostrar los anexos o no
        Boolean showannexes = PropiedadGlobalUtil.getShowAnnexes();
        model.addAttribute("showannexes", showannexes);

        if(showannexes == null || showannexes ) {
            //TODO Mirar que carga este método para mirar de ver que mostrar en caso de solo lectura y que no cargue todo el anexo.
            model.addAttribute("anexos", anexoEjb.getByRegistroSalida(registro));
            initAnexos(entidadActiva, model, request, registro.getId());
            //Inicializamos el mensaje de las limitaciones de anexos si es oficio de remisión sir
            //TODO falta hacer el método isOficioRemisionSir (salidas)
            boolean isOficioRemisionSir = false;
            if(isOficioRemisionSir) {
                initMensajeNotaInformativaAnexos(entidadActiva, model);
                model.addAttribute("maxanexospermitidos", PropiedadGlobalUtil.getMaxAnexosPermitidos(entidadActiva.getId()));
             }
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
        lopdEjb.insertarRegistroSalida(registro.getNumeroRegistro(), registro.getFecha(), registro.getLibro().getId(), usuarioEntidad.getId());
       
        return "registroSalida/registroSalidaDetalle";
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
    @RequestMapping(value = "/{idRegistro}/sello", method = RequestMethod.GET)
    public ModelAndView sello(@PathVariable Long idRegistro, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("sello");

        RegistroSalida registroSalida = registroSalidaEjb.findById(idRegistro);

        mav.addObject("registro", registroSalida);
        mav.addObject("x", request.getParameter("x"));
        mav.addObject("y", request.getParameter("y"));
        mav.addObject("orientacion", request.getParameter("orientacion"));
        mav.addObject("tipoRegistro", getMessage("informe.salida"));

        return mav;
    }



    @InitBinder("registroSalidaBusqueda")
    public void registroSalidaBusqueda(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class,dateEditor);
    }

}