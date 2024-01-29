package es.caib.regweb3.webapp.controller.usuario;

import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.ejb.UsuarioLocal;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.persistence.utils.RolUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.UsuarioBusquedaForm;
import es.caib.regweb3.webapp.utils.LoginService;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.UsuarioValidator;
import org.fundaciobit.genapp.common.i18n.I18NException;
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


/**
 * Created 14/02/14 12:52
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb3.model.Usuario}
 * @author mgonzalez
 */
@Controller
@RequestMapping(value = "/usuario")
@SessionAttributes("usuario")
public class UsuarioController extends BaseController {

    //protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UsuarioValidator usuarioValidator;

    @Autowired
    private LoginService loginService;

    @Autowired
    private RolUtils rolUtils;
       
    @EJB(mappedName = UsuarioLocal.JNDI_NAME)
    private UsuarioLocal usuarioEjb;
    
     /**
      * Listado de todos los Usuarios
      */
     @RequestMapping(value = "/list", method = RequestMethod.GET)
     public String listado(Model model) {
         UsuarioBusquedaForm usuarioBusqueda =  new UsuarioBusquedaForm(new Usuario(),1);
         model.addAttribute("usuarioBusqueda",usuarioBusqueda);

         return "usuario/usuarioList";
     }

     /**
      * Listado de usuarios
      * @param busqueda
      * @return
      * @throws Exception
      */
     @RequestMapping(value = "/list", method = RequestMethod.POST)
     public ModelAndView list(@ModelAttribute UsuarioBusquedaForm busqueda)throws Exception {

         ModelAndView mav = new ModelAndView("usuario/usuarioList");
         Usuario usuario = busqueda.getUsuario();

         Paginacion paginacion = usuarioEjb.busqueda(busqueda.getPageNumber(),
             usuario.getIdentificador(), usuario.getNombre(), usuario.getApellido1(),
             usuario.getApellido2(), usuario.getDocumento(), usuario.getTipoUsuario());

         busqueda.setPageNumber(1);
         mav.addObject("paginacion", paginacion);
         mav.addObject("usuarioBusqueda", busqueda);

         return mav;
     }

     /**
      * Carga el formulario para un nuevo {@link es.caib.regweb3.model.Usuario}
      */
     @RequestMapping(value = "/new", method = RequestMethod.GET)
     public String nuevoUsuario(Model model) throws Exception {

         Usuario usuario = new Usuario();

         model.addAttribute(usuario);

         return "usuario/usuarioForm";
     }

     /**
      * Guardar un nuevo {@link es.caib.regweb3.model.Usuario}
      */
     @RequestMapping(value = "/new", method = RequestMethod.POST)
     public String nuevoUsuario(@ModelAttribute Usuario usuario, BindingResult result, 
         SessionStatus status, HttpServletRequest request) {

         usuarioValidator.validate(usuario, result);

         if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
             return "usuario/usuarioForm";
         }else{ // Si no hay errores guardamos el registro

             try {
                 usuario = usuarioEjb.persist(usuario);

                 // Obtiene los Roles del Usuario desde el sistema externo
                 usuarioEjb.actualizarRoles(usuario, rolUtils.obtenerRolesUserPlugin(usuario.getIdentificador()));

                 Mensaje.saveMessageInfo(request, getMessage("regweb.guardar.registro"));

             } catch(I18NException i18ne) {
               Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
               log.error(I18NUtils.getMessage(i18ne), i18ne);
               
             }catch (Exception e) {
                 Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                 e.printStackTrace();
             }

             status.setComplete();
             return "redirect:/usuario/list";
         }
     }

     /**
      * Carga el formulario para modificar un {@link es.caib.regweb3.model.Usuario}
      */
     @RequestMapping(value = "/{usuarioId}/edit", method = RequestMethod.GET)
     public String editarUsuario(@PathVariable("usuarioId") Long usuarioId, Model model,HttpServletRequest request) {

         Usuario usuario = null;
         try {
             usuario = usuarioEjb.findById(usuarioId);
         }catch (Exception e) {
             e.printStackTrace();
         }
         model.addAttribute(usuario);
         return "usuario/usuarioForm";
     }

     /**
      * Editar un {@link es.caib.regweb3.model.Usuario}
      */
     @RequestMapping(value = "/{usuarioId}/edit", method = RequestMethod.POST)
     public String editarUsuario(@ModelAttribute @Valid Usuario usuario,BindingResult result,
                                 SessionStatus status, HttpServletRequest request) {

         usuarioValidator.validate(usuario, result);

         if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
             return "usuario/usuarioForm";
         }else { // Si no hay errores actualizamos el registro

             try {
                 Usuario usuarioAutenticado = getUsuarioAutenticado(request);

                 usuario = usuarioEjb.merge(usuario);

                 // Actualizamos los Roles del Usuario desde el sistema externo y en la sesion
                 usuarioEjb.actualizarRoles(usuario, rolUtils.obtenerRolesUserPlugin(usuario.getIdentificador()));

                 //Si el usuario modificado es el mismo que el UsuarioAutenticado, lo actualizamos en la sesión
                 if(usuario.getId().equals(usuarioAutenticado.getId())){
                     loginService.setUsuarioAutenticado(usuario, getLoginInfo(request));
                 }

                 Mensaje.saveMessageInfo(request, getMessage("regweb.actualizar.registro"));
             } catch(I18NException i18ne) {
               log.error(I18NUtils.getMessage(i18ne), i18ne);
               Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
             } catch (Exception e) {
                 e.printStackTrace();
                 Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
             }

             status.setComplete();

             if (isOperador(request) || isAdminEntidad(request)) {
                 return "redirect:/inici";
             } else{
                 return "redirect:/usuario/list";
             }
         }
     }

    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.Usuario}
     */
    @RequestMapping(value = "/new/{identificador}", method = RequestMethod.GET)
    public String nuevoUsuarioDocumento(Model model,@PathVariable String identificador) throws Exception {

        Usuario usuario = new Usuario();
        usuario.setIdentificador(identificador);

        model.addAttribute(usuario);

        return "usuario/usuarioForm";
    }

    /**
     * Guardar un nuevo {@link es.caib.regweb3.model.Usuario}
     */
    @RequestMapping(value = "/new/{identificador}", method = RequestMethod.POST)
    public String nuevoUsuarioDocumento(@ModelAttribute Usuario usuario, BindingResult result, SessionStatus status, HttpServletRequest request) {

        usuarioValidator.validate(usuario, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            return "usuario/usuarioForm";
        }else{ // Si no hay errores guardamos el registro

            try {

                //Guardamos el nuevo usuario
                usuario = usuarioEjb.persist(usuario);

                if(isAdminEntidad(request)){

                    //Asociamos el usuario a la Entidad activa
                    UsuarioEntidad usuarioEntidad = new UsuarioEntidad();
                    usuarioEntidad.setUsuario(usuario);
                    usuarioEntidad.setEntidad(getEntidadActiva(request));

                    usuarioEntidadEjb.persist(usuarioEntidad);

                    Mensaje.saveMessageInfo(request, getMessage("usuarioEntidad.nuevo.ok"));
                    status.setComplete();
                    return "redirect:/usuarioEntidad/list";
                }

                Mensaje.saveMessageInfo(request, getMessage("regweb.guardar.registro"));
                status.setComplete();
            }catch (Exception e) {
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                e.printStackTrace();
            }

            return "redirect:/usuarioEntidad/list";
        }
    }


     /**
      * Eliminar un {@link es.caib.regweb3.model.Usuario}
      */
     @RequestMapping(value = "/{usuarioId}/delete")
     public String eliminarUsuario(@PathVariable Long usuarioId, HttpServletRequest request) {

         try {

             Usuario usuario = usuarioEjb.findById(usuarioId);
             usuarioEjb.remove(usuario);

             Mensaje.saveMessageInfo(request, getMessage("regweb.eliminar.registro"));

         } catch (Exception e) {
             Mensaje.saveMessageError(request, getMessage("usuario.eliminar.relaciones"));
             e.printStackTrace();
         }

         return "redirect:/usuario/list";
     }

    /**
     * Asocia el idioma por defecto de la aplicación a los usuarios sin idioma
     */
    @RequestMapping(value = "/sinIdioma")
    public String usuariosIdioma(HttpServletRequest request) {

        try {

            Integer total = usuarioEjb.asociarIdioma();

            Mensaje.saveMessageInfo(request, "Se han modificado " +total+" usuarios sin idioma.");

        } catch (Exception e) {
            Mensaje.saveMessageError(request,"Error asociendo idioma por defecto");
            e.printStackTrace();
        }

        return "redirect:/usuario/list";
    }



     @ModelAttribute("idiomas")
     public Long[] idiomas() throws Exception {
         return RegwebConstantes.IDIOMAS_UI;
     }

     @ModelAttribute("tiposUsuario")
     public Long[] tiposUsuario() throws Exception {
        return RegwebConstantes.TIPOS_USUARIO;
    }


     @InitBinder("usuario")
     public void initBinder(WebDataBinder binder) {
         binder.setDisallowedFields("id");
         binder.setDisallowedFields("roles");
     }
}
