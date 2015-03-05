package es.caib.regweb.webapp.controller.registro;

import es.caib.regweb.model.*;
import es.caib.regweb.persistence.ejb.*;
import es.caib.regweb.persistence.utils.FileSystemManager;
import es.caib.regweb.persistence.utils.Paginacion;
import es.caib.regweb.persistence.utils.RegistroUtils;
import es.caib.regweb.persistence.utils.sir.FicheroIntercambioSICRES3;
import es.caib.regweb.persistence.utils.sir.SirUtils;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.controller.BaseController;
import es.caib.regweb.webapp.form.RegistroEntradaBusqueda;
import es.caib.regweb.webapp.utils.Mensaje;
import es.caib.regweb.webapp.validator.RegistroEntradaBusquedaValidator;
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
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 * Controller para los listados de los Registros de Entrada
 * @author earrivi
 * Date: 31/03/14
 */
@Controller
@RequestMapping(value = "/registroEntrada")
@SessionAttributes(types = {RegistroEntrada.class})
public class RegistroEntradaListController extends BaseController {

    @Autowired
    private RegistroEntradaBusquedaValidator registroEntradaBusquedaValidator;

    @EJB(mappedName = "regweb/HistoricoRegistroEntradaEJB/local")
    public HistoricoRegistroEntradaLocal historicoRegistroEntradaEjb;
    
    @EJB(mappedName = "regweb/AnexoEJB/local")
    public AnexoLocal anexoEjb;
    
    @EJB(mappedName = "regweb/LopdEJB/local")
    public LopdLocal lopdEjb;
    
    @EJB(mappedName = "regweb/TrazabilidadEJB/local")
    public TrazabilidadLocal trazabilidadEjb;
    
   
    @EJB(mappedName = "regweb/ArchivoEJB/local")
    public ArchivoLocal archivoEjb;
    
    @EJB(mappedName = "regweb/ModeloReciboEJB/local")
    public ModeloReciboLocal modeloReciboEjb;
    
    @EJB(mappedName = "regweb/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;
    
    @EJB(mappedName = "regweb/TipoDocumentalEJB/local")
    public TipoDocumentalLocal tipoDocumentalEjb;
    
    @EJB(mappedName = "regweb/OrganismoEJB/local")
    public OrganismoLocal organismoEjb;

    @EJB(mappedName = "regweb/PersonaEJB/local")
    public PersonaLocal personaEjb;

    @EJB(mappedName = "regweb/CatProvinciaEJB/local")
    public CatProvinciaLocal catProvinciaEjb;

    @EJB(mappedName = "regweb/CatPaisEJB/local")
    public CatPaisLocal catPaisEjb;

    @EJB(mappedName = "regweb/CatComunidadAutonomaEJB/local")
    public CatComunidadAutonomaLocal catComunidadAutonomaEjb;

    @EJB(mappedName = "regweb/CatNivelAdministracionEJB/local")
    public CatNivelAdministracionLocal catNivelAdministracionEjb;

    @EJB(mappedName = "regweb/SirEJB/local")
    public SirLocal sirEjb;
    

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

        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        // Obtenemos los Libros donde el usuario tiene permisos de Consulta
        List<Libro> librosConsulta = permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), RegwebConstantes.PERMISO_CONSULTA_REGISTRO_ENTRADA);

        RegistroEntradaBusqueda registroEntradaBusqueda = new RegistroEntradaBusqueda(new RegistroEntrada(),1);
        registroEntradaBusqueda.setFechaFin(new Date());

        model.addAttribute("librosConsulta", librosConsulta);
        model.addAttribute("registroEntradaBusqueda", registroEntradaBusqueda);

        return "registroEntrada/registroEntradaList";
    }

    /**
    * Realiza la busqueda de {@link es.caib.regweb.model.RegistroEntrada} según los parametros del formulario
    */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ModelAndView list(@ModelAttribute RegistroEntradaBusqueda busqueda, BindingResult result, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("registroEntrada/registroEntradaList", result.getModel());
        RegistroEntrada registroEntrada = busqueda.getRegistroEntrada();

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByUsuarioEntidad(getUsuarioAutenticado(request).getId(), getEntidadActiva(request).getId());
        List<Libro> librosConsulta = permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), RegwebConstantes.PERMISO_CONSULTA_REGISTRO_ENTRADA);

        registroEntradaBusquedaValidator.validate(busqueda,result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario

            mav.addObject("errors", result.getAllErrors());
            mav.addObject("librosConsulta", librosConsulta);
            mav.addObject("registroEntradaBusqueda", busqueda);
            return mav;
        }else { // Si no hay errores realizamos la búsqueda

            // Ponemos la hora 23:59 a la fecha fin
            Date fechaFin = RegistroUtils.ajustarHoraBusqueda(busqueda.getFechaFin());

            Paginacion paginacion = registroEntradaEjb.busqueda(busqueda.getPageNumber(), busqueda.getFechaInicio(), fechaFin, registroEntrada, librosConsulta, busqueda.getAnexos());

            // Alta en tabla LOPD
            lopdEjb.insertarRegistrosEntrada(paginacion, usuarioEntidad.getId());

            busqueda.setPageNumber(1);
            mav.addObject("paginacion", paginacion);
            mav.addObject("librosConsulta", librosConsulta);
            mav.addObject("registroEntradaBusqueda", busqueda);
            mav.addObject("isAdministradorLibro", permisoLibroUsuarioEjb.isAdministradorLibro(getUsuarioEntidadActivo(request).getId(),registroEntrada.getLibro().getId()));
            mav.addObject("puedeEditar", permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(),registroEntrada.getLibro().getId(),RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA));
        }

        return mav;

    }



    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/detalle", method = RequestMethod.GET)
    public String detalleRegistroEntrada(@PathVariable Long idRegistro, Model model, HttpServletRequest request) throws Exception {

        RegistroEntrada registro = registroEntradaEjb.findById(idRegistro);
        Entidad entidad = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

        model.addAttribute("registro",registro);

        ModeloRecibo modeloRecibo = new ModeloRecibo();
        model.addAttribute("modeloRecibo", modeloRecibo);

        // Permisos
        model.addAttribute("isAdministradorLibro", permisoLibroUsuarioEjb.isAdministradorLibro(getUsuarioEntidadActivo(request).getId(),registro.getLibro().getId()));
        model.addAttribute("puedeEditar", permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(),registro.getLibro().getId(),RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA));

        // OficioRemision
        model.addAttribute("isOficioRemision",registroEntradaEjb.isOficioRemisionInterno(idRegistro));

        // Interesados, solo si el Registro en Válio o Pendiente
        if(registro.getEstado().equals(RegwebConstantes.ESTADO_VALIDO) || registro.getEstado().equals(RegwebConstantes.ESTADO_PENDIENTE)){

            model.addAttribute("personasFisicas",personaEjb.getAllbyEntidadTipo(entidad.getId(), RegwebConstantes.TIPO_PERSONA_FISICA));
            model.addAttribute("personasJuridicas",personaEjb.getAllbyEntidadTipo(entidad.getId(), RegwebConstantes.TIPO_PERSONA_JURIDICA));
            model.addAttribute("tiposInteresado", RegwebConstantes.TIPOS_INTERESADO);
            model.addAttribute("tiposPersona",RegwebConstantes.TIPOS_PERSONA);
            model.addAttribute("paises",catPaisEjb.getAll());
            model.addAttribute("provincias",catProvinciaEjb.getAll());
            model.addAttribute("canalesNotificacion",RegwebConstantes.CANALES_NOTIFICACION);
            model.addAttribute("tiposDocumento", RegwebConstantes.TIPOS_DOCUMENTOID);
            model.addAttribute("nivelesAdministracion",catNivelAdministracionEjb.getAll());
            model.addAttribute("comunidadesAutonomas",catComunidadAutonomaEjb.getAll());
        }
        // Anexos
        model.addAttribute("anexos", anexoEjb.getByRegistroEntrada(idRegistro));
        model.addAttribute("historicos", historicoRegistroEntradaEjb.getByRegistroEntrada(idRegistro));

        // Trazabilidad
        List<Trazabilidad> trazabilidades = trazabilidadEjb.getByRegistroEntrada(registro.getId());
        model.addAttribute("trazabilidades", trazabilidades);

        // Alta en tabla LOPD
        lopdEjb.insertarRegistroEntrada(idRegistro, usuarioEntidad.getId());

        return "registroEntrada/registroEntradaDetalle";
    }

    /**
     * Anular un {@link es.caib.regweb.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/anular")
    public String anularRegistroEntrada(@PathVariable Long idRegistro, HttpServletRequest request) {

        try {

            RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            final List<Long> estados = new ArrayList<Long>();
            estados.add(RegwebConstantes.ESTADO_PENDIENTE);
            estados.add(RegwebConstantes.ESTADO_VALIDO);
            estados.add(RegwebConstantes.ESTADO_PENDIENTE_VISAR);

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

        return "redirect:/registroEntrada/list";
    }

    /**
     * Activar un {@link es.caib.regweb.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/activar")
    public String activarRegistroEntrada(@PathVariable Long idRegistro, HttpServletRequest request) {

        try {

            RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            // Comprobamos si el RegistroEntrada tiene el estado anulado
            if(!registroEntrada.getEstado().equals(RegwebConstantes.ESTADO_ANULADO)){

                Mensaje.saveMessageError(request, getMessage("registroEntrada.activar.error"));
                return "redirect:/registroEntrada/list";
            }

            // Comprobamos que el usuario dispone del permiso para Modificar el Registro
            if(!permisoLibroUsuarioEjb.tienePermiso(usuarioEntidad.getId(), registroEntrada.getLibro().getId(),RegwebConstantes.PERMISO_MODIFICACION_REGISTRO_ENTRADA)){
                log.info("Aviso: No dispone de los permisos necesarios para editar el registro");
                Mensaje.saveMessageAviso(request, getMessage("aviso.registro.editar"));

                return "redirect:/registroEntrada/list";
            }

            // CREAMOS EL HISTORICO REGISTRO ENTRADA
            historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada,usuarioEntidad,RegwebConstantes.TIPO_MODIF_ESTADO,false);

            // Estado pendiente visar
            registroEntrada.setEstado(RegwebConstantes.ESTADO_PENDIENTE_VISAR);

            registroEntradaEjb.merge(registroEntrada);

            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.activar"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            e.printStackTrace();
        }

        return "redirect:/registroEntrada/list";
    }

    /**
     * Visar un {@link es.caib.regweb.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/visar")
    public String visarRegistroEntrada(@PathVariable Long idRegistro, HttpServletRequest request) {

        try {

            RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            // Comprobamos si el RegistroEntrada tiene el estado Pendiente de Visar
            if(!registroEntrada.getEstado().equals(RegwebConstantes.ESTADO_PENDIENTE_VISAR)){

                Mensaje.saveMessageError(request, getMessage("registroEntrada.visar.error"));
                return "redirect:/registroEntrada/list";
            }

            // Comprobamos si el UsuarioEntidad tiene permisos para visar el Registro Entrada
            if(!permisoLibroUsuarioEjb.isAdministradorLibro(usuarioEntidad.getId(), registroEntrada.getLibro().getId())){

                Mensaje.saveMessageError(request, getMessage("aviso.usuario.visar"));
                return "redirect:/registroEntrada/list";
            }


            // CREAMOS EL HISTORICO REGISTRO ENTRADA
            historicoRegistroEntradaEjb.crearHistoricoRegistroEntrada(registroEntrada, usuarioEntidad, RegwebConstantes.TIPO_MODIF_ESTADO,false);

            // Modificamos el estado del Registro de Entrada a Estado Válido
            registroEntrada.setEstado(RegwebConstantes.ESTADO_VALIDO);

            registroEntradaEjb.merge(registroEntrada);

            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.visar.ok"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            e.printStackTrace();
        }

        return "redirect:/registroEntrada/list";
    }

    /**
     * Tramitar un {@link es.caib.regweb.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/tramitar")
    public String tramitarRegistroEntrada(@PathVariable Long idRegistro, HttpServletRequest request) {

        try {

            RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);
            UsuarioEntidad usuarioEntidad = getUsuarioEntidadActivo(request);

            // Comprobamos si el RegistroEntrada tiene el estado Válido
            if(!registroEntrada.getEstado().equals(RegwebConstantes.ESTADO_VALIDO)){

                Mensaje.saveMessageError(request, getMessage("registroEntrada.tramitar.error"));
                return "redirect:/registroEntrada/list";
            }

            // Comprobamos que el RegistroEntrada es un OficioRemision
            if(!registroEntradaEjb.isOficioRemisionInterno(idRegistro)){
                Mensaje.saveMessageError(request, getMessage("registroEntrada.tramitar.error"));
                return "redirect:/registroEntrada/list";
            }

            registroEntradaEjb.tramitarRegistroEntrada(registroEntrada, usuarioEntidad);

            Mensaje.saveMessageInfo(request, getMessage("registroEntrada.tramitar.ok"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            e.printStackTrace();
        }

        return "redirect:/registroEntrada/list";
    }


    /**
     * Carga el formulario para ver el detalle de un {@link es.caib.regweb.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/sello", method = RequestMethod.GET)
    public ModelAndView sello(@PathVariable Long idRegistro, HttpServletRequest request) throws Exception {

        ModelAndView mav = new ModelAndView("sello");

        RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);

        mav.addObject("registro", registroEntrada);
        mav.addObject("x", request.getParameter("x"));
        mav.addObject("y", request.getParameter("y"));
        mav.addObject("orientacion", request.getParameter("orientacion"));

        return mav;
    }


    /**
     * Crea el xml de un {@link es.caib.regweb.model.RegistroEntrada}
     */
    @RequestMapping(value = "/{idRegistro}/xml", method = RequestMethod.GET)
    public String xmlRegistroEntrada(@PathVariable Long idRegistro) throws Exception {

        RegistroEntrada registroEntrada = registroEntradaEjb.findById(idRegistro);


        // TODO Check  registroEntrada != null
        
        FicheroIntercambioSICRES3 fiSICRES3 =  sirEjb.writeFicheroIntercambioSICRES3(registroEntrada);
        String xml = SirUtils.marshallObject(fiSICRES3);


        Archivo archivo = new Archivo();
        archivo.setMime("application/xml");
        archivo.setNombre(registroEntrada.getRegistroDetalle().getExtracto());
        archivo.setTamano(Long.valueOf(xml.getBytes().length));

        archivo = archivoEjb.persist(archivo);
        FileSystemManager.crearArchivo(xml.getBytes(), archivo.getId());

        return "redirect:/registroEntrada/list";
    }



    /**
     * Obtenemos los Libros de los Organismos a los que la OficinaActiva da servicio y en los que el Usuario tiene permisos
     * @param request
     * @return
     * @throws Exception
     */
    @ModelAttribute("libros")
    public List<Libro> libros(HttpServletRequest request) throws Exception {

        return getLibrosConsultaEntradas(request);
    }


    @ModelAttribute("organismosOficinaActiva")
    public Set<Organismo> organismosOficinaActiva(HttpServletRequest request) throws Exception {
      return organismoEjb.getByOficinaActiva(getOficinaActiva(request).getId());
    }

    @ModelAttribute("estados")
    public Long[] estados(HttpServletRequest request) throws Exception {
        if(getEntidadActiva(request).getSir()){
            return RegwebConstantes.ESTADOS_REGISTRO_SIR;
        }else {
            return RegwebConstantes.ESTADOS_REGISTRO;
        }
    }

    @ModelAttribute("modelosRecibo")
    public List<ModeloRecibo> modelosRecibo(HttpServletRequest request) throws Exception {
        return modeloReciboEjb.getByEntidad(getEntidadActiva(request).getId());
    }

    @ModelAttribute("tiposDocumental")
    public List<TipoDocumental> tiposDocumental(HttpServletRequest request) throws Exception {
        return tipoDocumentalEjb.getByEntidad(getEntidadActiva(request).getId());
    }

    @ModelAttribute("tiposDocumentoAnexo")
    public Long[]tiposDocumento(HttpServletRequest request) throws Exception {
        return RegwebConstantes.TIPOS_DOCUMENTO;
    }

    @ModelAttribute("tiposFirma")
    public Long[] tiposFirma(HttpServletRequest request) throws Exception {
      return RegwebConstantes.TIPOS_FIRMA;
    }

    @ModelAttribute("tiposValidezDocumento")
    public Long[] tiposValidezDocumento(HttpServletRequest request) throws Exception {
      return RegwebConstantes.TIPOS_VALIDEZDOCUMENTO;
    }



    @InitBinder("registroEntradaBusqueda")
    public void registroEntradaBusqueda(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        CustomDateEditor dateEditor = new CustomDateEditor(sdf, true);
        binder.registerCustomEditor(java.util.Date.class,dateEditor);
    }

    
   

}