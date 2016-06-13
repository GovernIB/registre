package es.caib.regweb3.webapp.controller.libro;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.editor.UsuarioEditor;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.LibroValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by Fundació BIT.
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb3.model.Libro}
 * @author earrivi
 * Date: 11/02/14
 */
@Controller
@RequestMapping(value = "/libro")
@SessionAttributes(types = Libro.class)
public class LibroController extends BaseController {

    //protected final Logger log = Logger.getLogger(getClass());

    @Autowired
    private LibroValidator libroValidator;
    
    @EJB(mappedName = "regweb3/ContadorEJB/local")
    public ContadorLocal contadorEjb;
    
    @EJB(mappedName = "regweb3/LibroEJB/local")
    public LibroLocal libroEjb;
    
    @EJB(mappedName = "regweb3/RelacionOrganizativaOfiEJB/local")
    public RelacionOrganizativaOfiLocal relacionOrganizativaOfiLocalEjb;

    @EJB(mappedName = "regweb3/RegistroEntradaEJB/local")
    public RegistroEntradaLocal registroEntradaEjb;

    @EJB(mappedName = "regweb3/RegistroSalidaEJB/local")
    public RegistroSalidaLocal registroSalidaEjb;

    /**
     * Listado de libros de un Organismo
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idOrganismo}/libros", method = RequestMethod.GET)
    public String libros(Model model, @PathVariable Long idOrganismo, HttpServletRequest request)throws Exception {

        HttpSession session = request.getSession();
        Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);

        Organismo organismo = organismoEjb.findById(idOrganismo);

        // Comprueba que el Organismo existe
        if(organismo == null) {
            log.info("No existe el organismo");
            Mensaje.saveMessageError(request, getMessage("aviso.organismo.noExiste"));
            return "redirect:/organismo/list";
        }

        // Comprueba que el usuario es administrador del Organismo
        if(!organismo.getEntidad().equals(entidadActiva)) {
            log.info("No es administrador de este organismo");
            Mensaje.saveMessageError(request, getMessage("aviso.organismo.noAdministrador"));
            return "redirect:/organismo/list";
        }

        // Comprueba que la entidad está vigente
        if(!organismo.getEstado().getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)){
            log.info("El Organismo no está vigente");
            Mensaje.saveMessageError(request, getMessage("aviso.organismo.vigente"));
            return "redirect:/organismo/list";
        }

        model.addAttribute("organismo", organismo);

        Boolean tieneOficinas = oficinaEjb.tieneOficinasServicio(organismo.getId(), RegwebConstantes.OFICINA_VIRTUAL_SI);

        if (!tieneOficinas) {
            log.info("El organismo no tiene Oficinas");
            Mensaje.saveMessageError(request, getMessage("aviso.organismo.oficinas"));
        }

        model.addAttribute("oficinas", tieneOficinas);

        return "libro/librosList";
    }

    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.Libro}
     */
    @RequestMapping(value = "/{idOrganismo}/new", method = RequestMethod.GET)
    public String nuevoLibro(Model model, @PathVariable("idOrganismo") Long idOrganismo, HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);

        Libro libro = new Libro();
        Organismo organismo = organismoEjb.findById(idOrganismo);

        // Comprueba que el Organismo existe y está vigente
        if(organismo == null || !organismo.getEstado().getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)) {
            log.info("No existe el organismo");
            Mensaje.saveMessageError(request, getMessage("aviso.organismo.noExiste"));
            return "redirect:/organismo/list";
        }

        // Comprueba que el Usuario es Administrador de la Entidad
        if(organismo.getEntidad().equals(entidadActiva)) {

            // Mira que tenga Oficinas
            if (!oficinaEjb.tieneOficinasServicio(organismo.getId(),  RegwebConstantes.OFICINA_VIRTUAL_SI)) {
                log.info("El organismo no tiene Oficinas");
                Mensaje.saveMessageError(request, getMessage("aviso.organismo.oficinas"));
                return "redirect:/organismo/list";
            }
        }else{ // No es Administrador de la Entidad
            log.info("No es administrador de este organismo");
            Mensaje.saveMessageError(request, getMessage("aviso.organismo.noAdministrador"));
            return "redirect:/organismo/list";
        }

        libro.setOrganismo(organismo);

        model.addAttribute(libro);
        model.addAttribute("organismo", organismo);

        return "libro/libroForm";
    }

    /**
     * Guardar un nuevo {@link es.caib.regweb3.model.Libro}
     */
    @RequestMapping(value = "/{idOrganismo}/new", method = RequestMethod.POST)
    public String nuevoLibro(@ModelAttribute Libro libro,BindingResult result, SessionStatus status,@PathVariable("idOrganismo") Long idOrganismo, HttpServletRequest request) {

        libroValidator.validate(libro, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            return "libro/libroForm";
        }else{ // Si no hay errores guardamos el registro

            try {
                Libro libroCreado =libroEjb.crearLibro(libro);

                // Se crean los permisos para el nuevo Libro creado
                permisoLibroUsuarioEjb.crearPermisosLibroNuevo(libroCreado, getEntidadActiva(request).getId());

                Mensaje.saveMessageInfo(request, getMessage("regweb.guardar.registro"));
            }catch (Exception e) {
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                e.printStackTrace();
            }

            status.setComplete();
            return "redirect:/libro/"+idOrganismo+"/libros";
        }
    }

    /**
     * Carga el formulario para modificar un {@link es.caib.regweb3.model.Libro}
     */
    @RequestMapping(value = "{idOrganismo}/{idLibro}/edit", method = RequestMethod.GET)
    public String editarLibro(@PathVariable("idOrganismo") Long idOrganismo,@PathVariable("idLibro") Long idLibro,  Model model, HttpServletRequest request) {

        HttpSession session = request.getSession();
        Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);
        Libro libro = null;
        Organismo organismo=null;
        try {
            organismo = organismoEjb.findById(idOrganismo);
            libro = libroEjb.findById(idLibro);

            // Comprueba que el Organismo existe y está vigente
            if(organismo == null || !organismo.getEstado().getCodigoEstadoEntidad().equals(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE)) {
                log.info("No existe el organismo");
                Mensaje.saveMessageError(request, getMessage("aviso.organismo.noExiste"));
                return "redirect:/organismo/list";
            }

            // Comprueba que el Libro existe
            if(libro == null) {
                log.info("No existe el libro o no está activo");
                Mensaje.saveMessageError(request, getMessage("aviso.libro.noExiste"));
                return "redirect:/organismo/list";
            }

            // Comprueba que el organismo pertenece a la Entidad Activa
            if(!organismo.getEntidad().equals(entidadActiva)) {
                log.info("No es administrador de este organismo");
                Mensaje.saveMessageError(request, getMessage("aviso.organismo.noAdministrador"));
                return "redirect:/organismo/list";
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute(libro);
        model.addAttribute("organismo", organismo);
        return "libro/libroForm";
    }

    /**
     * Editar un {@link es.caib.regweb3.model.Libro}
     */
    @RequestMapping(value = "{idOrganismo}/{idLibro}/edit", method = RequestMethod.POST)
    public String editarLibro(@ModelAttribute @Valid Libro libro, BindingResult result,
                              SessionStatus status, HttpServletRequest request) {

        libroValidator.validate(libro, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            return "libro/libroForm";
        }else { // Si no hay errores actualizamos el registro

            try {
                libroEjb.merge(libro);
                Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));

            }catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            }

            status.setComplete();
            return "redirect:/libro/"+libro.getOrganismo().getId()+"/libros";
        }
    }

    /**
     * Eliminar un {@link es.caib.regweb3.model.Libro}
     */
    @RequestMapping(value = "/{idLibro}/{idOrganismo}/delete")
    public String eliminarLibro(@PathVariable("idLibro") Long idLibro, @PathVariable("idOrganismo") Long idOrganismo, HttpServletRequest request) {

        try {

            Long registrosEntrada = registroEntradaEjb.getTotalByLibro(idLibro);
            Long registrosSalida = registroSalidaEjb.getTotalByLibro(idLibro);

            // Comprueba que los contadores del libro están a cero y no tiene registros de entrada ni de salida
            if(registrosEntrada==0 && registrosSalida==0){

                // Elimina el Libro
                /********* LIBRO *********/
                log.info("Libro eliminado: " + libroEjb.eliminarLibro(idLibro));

                Mensaje.saveMessageInfo(request, getMessage("regweb.eliminar.registro"));

            } else{

                Mensaje.saveMessageError(request, getMessage("error.libro.eliminar"));

            }

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("regweb.relaciones.registro"));
            e.printStackTrace();
        }

        return "redirect:/libro/"+idOrganismo+"/libros";
    }

    /**
     * Inicializa los contadores del libro {@link es.caib.regweb3.model.Libro}
     */
    @RequestMapping(value = "/{libroId}/inicializar", method = RequestMethod.GET)
    public String  inicializarLibro(@PathVariable("libroId") Long libroId, HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);

        Libro libro = libroEjb.findById(libroId);

        // Comprueba que el Libro existe
        if(libro == null) {
            log.info("No existe el libro o no está activo");
            Mensaje.saveMessageError(request, getMessage("aviso.libro.noExiste"));
            return "redirect:/organismo/list";
        }

        // Comprueba que el Libro pertenece a la Entidad Activa
        if(!libro.getOrganismo().getEntidad().equals(entidadActiva)) {
            log.info("No es administrador de este libro");
            Mensaje.saveMessageError(request, getMessage("aviso.libro.noAdministrador"));
            return "redirect:/organismo/list";
        }

        Contador contadorEntrada = contadorEjb.persist(new Contador());
        Contador contadorSalida = contadorEjb.persist(new Contador());
        Contador contadorOficioRemision = contadorEjb.persist(new Contador());

        libro.setContadorEntrada(contadorEntrada);
        libro.setContadorSalida(contadorSalida);
        libro.setContadorOficioRemision(contadorOficioRemision);

        libroEjb.merge(libro);

        Mensaje.saveMessageInfo(request, getMessage("libro.inicializar.ok"));

        return "redirect:/libro/"+libro.getOrganismo().getId()+"/libros";

    }

    /**
     * Listado de usuarios y sus permisos de un libro
     * @param idLibro
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idLibro}/usuarios", method = RequestMethod.GET)
    public String usuariosLibro(Model model, @PathVariable Long idLibro, HttpServletRequest request)throws Exception {

        HttpSession session = request.getSession();
        Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);

//        ModelAndView mav = new ModelAndView("libro/usuariosLibroList");

        Libro libro = libroEjb.findById(idLibro);

        // Comprueba que el Libro existe
        if(libro == null) {
            log.info("No existe este libro");
            Mensaje.saveMessageError(request, getMessage("aviso.libro.noExiste"));
            return "redirect:/organismo/list";
        }

        // Comprueba que el Libro pertenece a la Entida Activa
        if(!libro.getOrganismo().getEntidad().equals(entidadActiva)) {
            log.info("No es administrador de este libro");
            Mensaje.saveMessageError(request, getMessage("aviso.libro.noAdministrador"));
            return "redirect:/organismo/list";
        }

        List<PermisoLibroUsuario> plu = permisoLibroUsuarioEjb.findByLibro(libro.getId());
        List<UsuarioEntidad> usuarios = permisoLibroUsuarioEjb.getUsuariosEntidadByLibro(libro.getId());

        model.addAttribute("libro", libro);
        model.addAttribute("plu", plu);
        model.addAttribute("permisos", RegwebConstantes.PERMISOS);
        model.addAttribute("usuarios", usuarios);

        return "libro/usuariosLibroList";
    }


    @ModelAttribute("idiomas")
    public Long[] idiomas() throws Exception {
        return RegwebConstantes.IDIOMAS_UI;
    }



    @InitBinder("libro")
    public void initBinder(WebDataBinder binder) {
        //binder.setDisallowedFields("id");

        binder.registerCustomEditor(Usuario.class, "administradores",new UsuarioEditor());
        binder.setValidator(this.libroValidator);
    }

}
