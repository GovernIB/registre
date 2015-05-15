package es.caib.regweb.webapp.controller.usuario;

import es.caib.regweb.model.Entidad;
import es.caib.regweb.model.Usuario;
import es.caib.regweb.model.UsuarioEntidad;
import es.caib.regweb.persistence.ejb.UsuarioLocal;
import es.caib.regweb.webapp.controller.BaseController;
import es.caib.regweb.webapp.utils.Mensaje;
import es.caib.regweb.webapp.utils.UsuarioService;
import es.caib.regweb.webapp.validator.UsuarioDocumentoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;

/**
 * Created 14/02/14 12:52
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb.model.Usuario}
 * @author mgonzalez
 */
@Controller
@RequestMapping(value = "/usuario")
@SessionAttributes("usuario")
public class UsuarioExisteController extends BaseController {

    //protected final Logger log = Logger.getLogger(getClass());

    @Autowired
    private UsuarioDocumentoValidator usuarioDocumentoValidator;

    @Autowired
    private UsuarioService usuarioService;
    
    @EJB(mappedName = "regweb/UsuarioEJB/local")
    public UsuarioLocal usuarioEjb;

    /**
     * Carga el formulario para comprobar si existe un {@link es.caib.regweb.model.Usuario}
     */
    @RequestMapping(value = "/existeUsuario", method = RequestMethod.GET)
    public String existeUsuario(Model model) throws Exception {

        Usuario usuario = new Usuario();

        model.addAttribute("usuario",usuario);

        return "usuario/usuarioIdentificadorForm";
    }

    /**
     * Guardar un nuevo {@link es.caib.regweb.model.Usuario}
     */
    @RequestMapping(value = "/existeUsuario", method = RequestMethod.POST)
    public String existeUsuario(@ModelAttribute Usuario usuario, BindingResult result,Model model, SessionStatus status,HttpServletRequest request) {

        usuarioDocumentoValidator.validate(usuario, result);

        if (result.hasErrors()) { // Si hay errores volvemos a la vista del formulario
            return "usuario/usuarioIdentificadorForm";
        }else{ // Si no hay errores guardamos el registro

            try {

                Entidad entidad = getEntidadActiva(request);
                Usuario usuarioExistente = usuarioEjb.findByIdentificador(usuario.getIdentificador());

                // Si el usuario no existe en el sistema, lo creamos a partir de Seycon
                if(usuarioExistente == null){

                    usuarioExistente = usuarioService.crearUsuario(usuario.getIdentificador());

                    if(usuarioExistente == null){
                        Mensaje.saveMessageError(request, getMessage("usuario.no.encontrado"));
                        return "redirect:/entidad/usuarios";
                    }
                }

                // Actualizamos sus Roles
                usuarioService.actualizarRoles(usuarioExistente);

                if(!usuarioExistente.getRwe_usuari() && !usuarioExistente.getRwe_admin()){
                    Mensaje.saveMessageError(request, getMessage("usuarioEntidad.rol"));
                    return "redirect:/entidad/usuarios";
                }

                //Comprobamos si el usuario no pertecene ya a la Entidad
                UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByUsuarioEntidad(usuarioExistente.getId(), entidad.getId());

                // Si no pertenecía a la entidad, lo creamos
                if (usuarioEntidad == null){

                    // Creamos el UsuarioEntidad
                    usuarioEntidad = new UsuarioEntidad();
                    usuarioEntidad.setUsuario(usuarioExistente);
                    usuarioEntidad.setEntidad(entidad);
                    UsuarioEntidad usuarioEntidadCreado = usuarioEntidadEjb.persist(usuarioEntidad);

                    // Se crean los permisos para el nuevo Usuario creado
                    permisoLibroUsuarioEjb.crearPermisosUsuarioNuevo(usuarioEntidadCreado, getEntidadActiva(request).getId());

                    Mensaje.saveMessageInfo(request, getMessage("usuarioEntidad.nuevo.ok"));
                    return "redirect:/entidad/usuarios";

                }else{ //Si ya pertenecia a la Entidad, lo volvemos a activar

                    if(usuarioEntidad.getActivo()){ // si ya está activo
                        Mensaje.saveMessageInfo(request, getMessage("usuarioEntidad.existente"));
                    }else{
                        usuarioEntidad.setActivo(true);
                        usuarioEntidadEjb.merge(usuarioEntidad);
                        Mensaje.saveMessageInfo(request, getMessage("usuarioEntidad.existente.inactivo"));
                    }
                }

            }catch (Exception e) {
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                e.printStackTrace();
            }

            return "redirect:/entidad/usuarios";
        }
    }


    @InitBinder("usuario")
    public void existeUsuario(WebDataBinder binder) {

        binder.setValidator(this.usuarioDocumentoValidator);
    }
}
