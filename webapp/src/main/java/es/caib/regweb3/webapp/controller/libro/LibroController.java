package es.caib.regweb3.webapp.controller.libro;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.persistence.ejb.LibroLocal;
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

    //protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private LibroValidator libroValidator;
    
    @EJB(mappedName = LibroLocal.JNDI_NAME)
    private LibroLocal libroEjb;


    /**
     * Listado de libros de un Organismo
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idOrganismo}/libros", method = RequestMethod.GET)
    public String libros(Model model, @PathVariable Long idOrganismo, HttpServletRequest request)throws Exception {

        Entidad entidadActiva = getEntidadActiva(request);

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
     * Listado de libros de una Entidad
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listado(Model model, HttpServletRequest request)throws Exception {

        HttpSession session = request.getSession();
        Entidad entidadActiva = getEntidadActiva(request);

        List<Libro> librosList = libroEjb.getTodosLibrosEntidad(entidadActiva.getId());

        model.addAttribute("librosList", librosList);
        model.addAttribute("entidad", entidadActiva);

        return "libro/librosEntidadList";

    }

    /**
     * Carga el formulario para modificar un {@link es.caib.regweb3.model.Libro}
     */
    @RequestMapping(value = "{idOrganismo}/{idLibro}/edit", method = RequestMethod.GET)
    public String editarLibro(@PathVariable("idOrganismo") Long idOrganismo,@PathVariable("idLibro") Long idLibro,  Model model, HttpServletRequest request) {

        HttpSession session = request.getSession();
        Entidad entidadActiva = getEntidadActiva(request);
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

        // Comprueba que no existe otro organismo ya creado con el mismo código Dir3 y que tenga libros activos
        try {
            if(libro.getActivo()) {
                Organismo orgExistenteConLibros;
                orgExistenteConLibros = organismoEjb.findByCodigoOtraEntidadConLibros(libro.getOrganismo().getCodigo(), libro.getOrganismo().getEntidad().getId());
                if (orgExistenteConLibros != null) {
                    if (libroEjb.getLibrosActivosOrganismo(orgExistenteConLibros.getId()).size() > 0) {
                        log.info("Existe un organismo con el mismo código Dir3 " + libro.getOrganismo().getCodigo() + " con algún libro creado en la instalación");
                        Mensaje.saveMessageError(request, getMessage("aviso.organismo.existe"));
                        return "redirect:/libro/"+libro.getOrganismo().getId()+"/libros";
                    }
                }
            }

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

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        status.setComplete();
        return "redirect:/libro/"+libro.getOrganismo().getId()+"/libros";

    }

    /**
     * Inicializa los contadores del libro {@link es.caib.regweb3.model.Libro}
     */
    @RequestMapping(value = "/{libroId}/inicializar", method = RequestMethod.GET)
    public String  inicializarLibro(@PathVariable("libroId") Long libroId, HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        Entidad entidadActiva = getEntidadActiva(request);

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

        libroEjb.reiniciarContadores(libro.getId());

        Mensaje.saveMessageInfo(request, getMessage("libro.inicializar.ok"));

        return "redirect:/libro/"+libro.getOrganismo().getId()+"/libros";

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
