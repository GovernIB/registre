package es.caib.regweb3.webapp.controller.permiso;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.PermisoOrganismoUsuario;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.ejb.PluginLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.PermisoOrganismoUsuarioForm;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.pluginsib.userinformation.IUserInformationPlugin;
import org.fundaciobit.pluginsib.userinformation.RolesInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Fundació BIT.
 * Controller que gestiona todas las operaciones con {@link es.caib.regweb3.model.PermisoOrganismoUsuario}
 *
 * @author earrivi
 */
@Controller
@RequestMapping(value = "/permisos")
public class PermisosController extends BaseController {

    @EJB(mappedName = "regweb3/PluginEJB/local")
    private PluginLocal pluginEjb;

    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.PermisoLibroUsuario}
     */
    @RequestMapping(value = "/{idUsuarioEntidad}/{idOrganismo}/asignar", method = RequestMethod.GET)
    public String asignarOrganismo(@PathVariable Long idUsuarioEntidad,@PathVariable Long idOrganismo, Model model,
                                 HttpServletRequest request) throws Exception, I18NException {


        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findById(idUsuarioEntidad);
        Organismo organismo = organismoEjb.findByIdLigero(idOrganismo);

        permisoOrganismoUsuarioEjb.crearPermisosUsuarioOrganismo(usuarioEntidad, organismo);

        return "redirect:/permisos/"+idUsuarioEntidad;

    }

    /**
     * Elimina {@link es.caib.regweb3.model.PermisoLibroUsuario} de la relación de uin Usuario y un Organismo
     */
    @RequestMapping(value = "/{idUsuarioEntidad}/{idOrganismo}/eliminar", method = RequestMethod.GET)
    public String eliminarPermisos(@PathVariable Long idUsuarioEntidad,@PathVariable Long idOrganismo, Model model,
                                   HttpServletRequest request) throws Exception, I18NException {


        permisoOrganismoUsuarioEjb.eliminarPermisosUsuarioOrganismo(idUsuarioEntidad, idOrganismo);

        return "redirect:/permisos/"+idUsuarioEntidad;

    }

    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.PermisoLibroUsuario}
     */
    @RequestMapping(value = "/{idUsuarioEntidad}", method = RequestMethod.GET)
    public String gestionarPermisos(@PathVariable Long idUsuarioEntidad, Model model,
                                 HttpServletRequest request) throws Exception, I18NException {

        Entidad entidad = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findById(idUsuarioEntidad);

        IUserInformationPlugin loginPlugin = (IUserInformationPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_USER_INFORMATION);
        RolesInfo rolesInfo = loginPlugin.getRolesByUsername(usuarioEntidad.getUsuario().getIdentificador());

        List<String> roles = new ArrayList<String>();
        Collections.addAll(roles, rolesInfo.getRoles());

        // Si no dispone de algún ROL, no se le pueden asignar permisos
        if (roles.contains("RWE_USUARI") || roles.contains("RWE_WS_SALIDA") || roles.contains("RWE_WS_ENTRADA") || !usuarioEntidad.getActivo()) {

            PermisoOrganismoUsuarioForm permisoOrganismoUsuarioForm = new PermisoOrganismoUsuarioForm();
            permisoOrganismoUsuarioForm.setUsuarioEntidad(usuarioEntidad);

            List<PermisoOrganismoUsuario> permisos = permisoOrganismoUsuarioEjb.findByUsuario(usuarioEntidad.getId());
            permisoOrganismoUsuarioForm.setPermisoOrganismoUsuarios(permisos);

            List<Organismo> organismosActivos = organismoEjb.getPermitirUsuarios(entidad.getId());
            List<Organismo> organismos = new ArrayList<Organismo>();
            if (permisos.size() > 0) {
                organismos = permisoOrganismoUsuarioEjb.getOrganismosByUsuario(usuarioEntidad.getId());
            }

            // Eliminamos los que ya estén asociados
            for(Organismo organismo:organismos){
                organismosActivos.remove(organismo);
            }

            model.addAttribute(permisoOrganismoUsuarioForm);
            model.addAttribute("entidad", entidad);
            model.addAttribute("permisos", RegwebConstantes.PERMISOS);
            model.addAttribute("organismos", organismos);
            model.addAttribute("organismosActivos", organismosActivos);

            return "permiso/permisoOrganismoUsuarioForm";
        } else {
            Mensaje.saveMessageError(request, getMessage("usuario.asignar.permisos.denegado"));
            return "redirect:/entidad/usuarios";
        }
    }

    /**
     * Guardar un nuevo {@link es.caib.regweb3.model.PermisoLibroUsuario}
     */
    @RequestMapping(value = "/{idUsuarioEntidad}", method = RequestMethod.POST)
    public String gestionarPermisos(@ModelAttribute PermisoOrganismoUsuarioForm permisoOrganismoUsuarioForm,
                                 @PathVariable Integer idUsuarioEntidad, SessionStatus status, HttpServletRequest request) {

        try {

            UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findById(Long.valueOf(idUsuarioEntidad));

            for (PermisoOrganismoUsuario plu : permisoOrganismoUsuarioForm.getPermisoOrganismoUsuarios()) {
                plu.setUsuario(usuarioEntidad);

                // Si ya existe el Permiso, actualiza el valor de ctivo. Si no existe, crea el Permiso en BBDD
                if (plu.getId() != null) {
                    permisoOrganismoUsuarioEjb.actualizarPermiso(plu.getId(), plu.getActivo());
                } else {
                    permisoOrganismoUsuarioEjb.merge(plu);
                }

            }

            Mensaje.saveMessageInfo(request, getMessage("usuario.asignar.permisos.ok"));

        } catch (Exception e) {
            Mensaje.saveMessageError(request, getMessage("organismo.usuario.asignar.error"));
            e.printStackTrace();
        }

        status.setComplete();
        return "redirect:/entidad/usuarios";
    }


    @InitBinder({"permisoOrganismoUsuarioForm"})
    public void initBinder2(WebDataBinder binder) {
        // Per resoldre el problema dels 256 objectes dins un form
        binder.setAutoGrowCollectionLimit(500);
    }
}
