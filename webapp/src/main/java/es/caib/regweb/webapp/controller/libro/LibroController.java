package es.caib.regweb.webapp.controller.libro;

import es.caib.regweb.model.*;
import es.caib.regweb.persistence.ejb.ContadorLocal;
import es.caib.regweb.persistence.ejb.LibroLocal;
import es.caib.regweb.persistence.ejb.OrganismoLocal;
import es.caib.regweb.persistence.ejb.*;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.controller.BaseController;
import es.caib.regweb.webapp.editor.UsuarioEditor;
import es.caib.regweb.webapp.utils.Mensaje;
import es.caib.regweb.webapp.validator.LibroValidator;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb.model.Libro}
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
    
    @EJB(mappedName = "regweb/ContadorEJB/local")
    public ContadorLocal contadorEjb;
    
    @EJB(mappedName = "regweb/LibroEJB/local")
    public LibroLocal libroEjb;

    @EJB(mappedName = "regweb/OrganismoEJB/local")
    public OrganismoLocal organismoEjb;
    
    @EJB(mappedName = "regweb/RelacionOrganizativaOfiEJB/local")
    public RelacionOrganizativaOfiLocal relacionOrganizativaOfiLocalEjb;

    /**
     * Listado de libros de un Organismo
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{idOrganismo}/libros", method = RequestMethod.GET)
    public ModelAndView libros(@PathVariable Long idOrganismo, HttpServletRequest request)throws Exception {

        ModelAndView mav = new ModelAndView("libro/librosList");

        Organismo organismo = organismoEjb.findById(idOrganismo);
        mav.addObject("organismo", organismo);

        Set<Oficina> oficinas = new HashSet<Oficina>();  // Utilizamos un Set porque no permite duplicados
        oficinas.addAll(oficinaEjb.findByOrganismoResponsable(organismo.getId()));
        oficinas.addAll(relacionOrganizativaOfiLocalEjb.getOficinasByOrganismo(organismo.getId()));
        if(oficinas.size() == 0){
            log.info("El organismo no tiene Oficinas");
            Mensaje.saveMessageError(request, I18NUtils.tradueix("aviso.organismo.oficinas"));
        }

        mav.addObject("oficinas", oficinas.size() > 0);

        return mav;
    }

    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb.model.Libro}
     */
    @RequestMapping(value = "/{idOrganismo}/new", method = RequestMethod.GET)
    public String nuevoLibro(Model model, @PathVariable("idOrganismo") Long idOrganismo) throws Exception {

        Libro libro = new Libro();
        Organismo organismo = organismoEjb.findById(idOrganismo);
        libro.setOrganismo(organismo);

        model.addAttribute(libro);
        model.addAttribute("organismo", organismo);

        return "libro/libroForm";
    }

    /**
     * Guardar un nuevo {@link es.caib.regweb.model.Libro}
     */
    @RequestMapping(value = "/{idOrganismo}/new", method = RequestMethod.POST)
    public String nuevoLibro(@ModelAttribute Libro libro,BindingResult result, SessionStatus status,@PathVariable("idOrganismo") Long idOrganismo, HttpServletRequest request) {

        libroValidator.validate(libro, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            return "libro/libroForm";
        }else{ // Si no hay errores guardamos el registro

            try {
                Contador contadorEntrada = contadorEjb.persist(new Contador());
                Contador contadorSalida = contadorEjb.persist(new Contador());
                Contador contadorOficio = contadorEjb.persist(new Contador());
                libro.setContadorEntrada(contadorEntrada);
                libro.setContadorSalida(contadorSalida);
                libro.setContadorOficioRemision(contadorOficio);
                libroEjb.persist(libro);

                Mensaje.saveMessageInfo(request, getMessage("regweb.guardar.registro"));
                status.setComplete();
            }catch (Exception e) {
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                e.printStackTrace();
            }

            return "redirect:/libro/"+idOrganismo+"/libros";
        }
    }

    /**
     * Carga el formulario para modificar un {@link es.caib.regweb.model.Libro}
     */
    @RequestMapping(value = "{idOrganismo}/{idLibro}/edit", method = RequestMethod.GET)
    public String editarLibro(@PathVariable("idOrganismo") Long idOrganismo,@PathVariable("idLibro") Long idLibro,  Model model) {

        Libro libro = null;
        Organismo organismo=null;
        try {
            organismo = organismoEjb.findById(idOrganismo);
            libro = libroEjb.findById(idLibro);
        }catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute(libro);
        model.addAttribute("organismo", organismo);
        return "libro/libroForm";
    }

    /**
     * Editar un {@link es.caib.regweb.model.Libro}
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
                status.setComplete();

            }catch (Exception e) {
                e.printStackTrace();
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            }

            return "redirect:/libro/"+libro.getOrganismo().getId()+"/libros";
        }
    }

    /**
     * Inicializa los contadores del libro {@link es.caib.regweb.model.Libro}
     */
    @RequestMapping(value = "/{libroId}/inicializar", method = RequestMethod.GET)
    public String  inicializarLibro(@PathVariable("libroId") Long libroId, HttpServletRequest request) throws Exception {

        Libro libro = libroEjb.findById(libroId);

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
    public ModelAndView usuariosLibro(@PathVariable Long idLibro)throws Exception {

        ModelAndView mav = new ModelAndView("libro/usuariosLibroList");

        Libro libro = libroEjb.findById(idLibro);

        List<PermisoLibroUsuario> plu = permisoLibroUsuarioEjb.findByLibro(libro.getId());
        List<UsuarioEntidad> usuarios = permisoLibroUsuarioEjb.getUsuariosEntidadByLibro(libro.getId());

        mav.addObject("libro", libro);
        mav.addObject("plu", plu);
        mav.addObject("permisos", RegwebConstantes.PERMISOS);
        mav.addObject("usuarios", usuarios);

        return mav;
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
