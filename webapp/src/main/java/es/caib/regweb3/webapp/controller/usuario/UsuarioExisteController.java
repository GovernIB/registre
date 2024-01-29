package es.caib.regweb3.webapp.controller.usuario;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.ejb.UsuarioLocal;
import es.caib.regweb3.persistence.utils.RolUtils;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.utils.LoginService;
import es.caib.regweb3.webapp.utils.Mensaje;
import es.caib.regweb3.webapp.validator.UsuarioDocumentoValidator;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.web.i18n.I18NUtils;
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
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb3.model.Usuario}
 * @author mgonzalez
 */
@Controller
@RequestMapping(value = "/usuario")
@SessionAttributes("usuario")
public class UsuarioExisteController extends BaseController {

    //protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UsuarioDocumentoValidator usuarioDocumentoValidator;

    @Autowired
    private LoginService loginService;

    @Autowired
    private RolUtils rolUtils;
    
    @EJB(mappedName = UsuarioLocal.JNDI_NAME)
    private UsuarioLocal usuarioEjb;

    /**
     * Carga el formulario para comprobar si existe un {@link es.caib.regweb3.model.Usuario}
     */
    @RequestMapping(value = "/existeUsuario", method = RequestMethod.GET)
    public String existeUsuario(Model model) throws Exception {

        Usuario usuario = new Usuario();

        model.addAttribute("usuario",usuario);

        return "usuario/usuarioIdentificadorForm";
    }

    /**
     * Guardar un nuevo {@link es.caib.regweb3.model.Usuario}
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

                    usuarioExistente = loginService.crearUsuario(usuario.getIdentificador());

                    if(usuarioExistente == null){
                        Mensaje.saveMessageError(request, getMessage("usuario.no.encontrado"));
                        return "redirect:/usuarioEntidad/list";
                    }
                }

                // Actualizamos sus Roles
                usuarioEjb.actualizarRoles(usuarioExistente, rolUtils.obtenerRolesUserPlugin(usuario.getIdentificador()));

                if(!usuarioExistente.getRwe_usuari() && !usuarioExistente.getRwe_admin() && !usuarioExistente.getRwe_ws_ciudadano()
                        && !usuarioExistente.getRwe_ws_entrada() && !usuarioExistente.getRwe_ws_salida()){

                    Mensaje.saveMessageError(request, getMessage("usuarioEntidad.rol"));
                    return "redirect:/usuarioEntidad/list";
                }

                //Comprobamos si el usuario no pertecene ya a la Entidad
                UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByUsuarioEntidad(usuarioExistente.getId(), entidad.getId());

                // Si no pertenecía a la entidad, lo creamos
                if (usuarioEntidad == null){

                    // Creamos el UsuarioEntidad
                    usuarioEntidad = new UsuarioEntidad();
                    usuarioEntidad.setUsuario(usuarioExistente);
                    usuarioEntidad.setEntidad(entidad);
                    usuarioEntidadEjb.persist(usuarioEntidad);

                    Mensaje.saveMessageInfo(request, getMessage("usuarioEntidad.nuevo.ok"));
                    return "redirect:/usuarioEntidad/list";

                }else{ //Si ya pertenecia a la Entidad, lo volvemos a activar

                    if(usuarioEntidad.getActivo()){ // si ya está activo
                        Mensaje.saveMessageInfo(request, getMessage("usuarioEntidad.existente"));
                    }else{
                        usuarioEntidad.setActivo(true);
                        usuarioEntidadEjb.merge(usuarioEntidad);
                        Mensaje.saveMessageInfo(request, getMessage("usuarioEntidad.existente.inactivo"));
                    }
                }

            } catch(I18NException i18ne) {
              log.error(I18NUtils.getMessage(i18ne), i18ne);
              Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
            }catch (Exception e) {
                Mensaje.saveMessageError(request, getMessage("regweb.error.registro"));
                e.printStackTrace();
            }

            return "redirect:/usuarioEntidad/list";
        }
    }


    @InitBinder("usuario")
    public void existeUsuario(WebDataBinder binder) {

        binder.setValidator(this.usuarioDocumentoValidator);
    }
}
