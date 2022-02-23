package es.caib.regweb3.webapp.controller.permiso;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.LibroLocal;
import es.caib.regweb3.persistence.ejb.UsuarioLocal;
import es.caib.regweb3.persistence.utils.RolUtils;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.controller.BaseController;
import es.caib.regweb3.webapp.form.PermisoOrganismoUsuarioForm;
import es.caib.regweb3.webapp.utils.Mensaje;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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

    @Autowired
    private RolUtils rolUtils;

    @EJB(mappedName = UsuarioLocal.JNDI_NAME)
    private UsuarioLocal usuarioEjb;

    @EJB(mappedName = LibroLocal.JNDI_NAME)
    private LibroLocal libroEjb;


    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.PermisoLibroUsuario}
     */
    @RequestMapping(value = "/{idUsuarioEntidad}", method = RequestMethod.GET)
    public String gestionarPermisos(@PathVariable Long idUsuarioEntidad, Model model,
                                 HttpServletRequest request) throws Exception, I18NException {

        Entidad entidad = getEntidadActiva(request);
        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findById(idUsuarioEntidad);

        // Obtenemos los Roles del usuario mediante plugin correspondiento
        List<Rol> roles = rolUtils.obtenerRolesUserPlugin(usuarioEntidad.getUsuario().getIdentificador());

        // Actualizamos los Roles
        usuarioEjb.actualizarRoles(usuarioEntidad.getUsuario(), roles);

        // Si no dispone de algún ROL, no se le pueden asignar permisos
        if (roles.contains(new Rol("RWE_USUARI")) || roles.contains(new Rol("RWE_WS_SALIDA")) || roles.contains(new Rol("RWE_WS_ENTRADA")) || !usuarioEntidad.getActivo()) {

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

    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.PermisoLibroUsuario}
     */
    @RequestMapping(value = "/{idUsuarioEntidad}/{idOrganismo}/asignar", method = RequestMethod.GET)
    public String asignarOrganismo(@PathVariable Long idUsuarioEntidad,@PathVariable Long idOrganismo, HttpServletRequest request) throws Exception {


        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findById(idUsuarioEntidad);
        Organismo organismo = organismoEjb.findByIdLigero(idOrganismo);

        permisoOrganismoUsuarioEjb.crearPermisosUsuarioOrganismo(usuarioEntidad, organismo);

        Mensaje.saveMessageInfo(request, getMessage("usuario.asignar.permisos.ok"));

        return "redirect:/permisos/"+idUsuarioEntidad;

    }

    /**
     * Carga el formulario para un nuevo {@link es.caib.regweb3.model.PermisoLibroUsuario}
     */
    @RequestMapping(value = "/{idUsuarioEntidad}/asignarTodos", method = RequestMethod.GET)
    public String asignarOrganismosTodos(@PathVariable Long idUsuarioEntidad, HttpServletRequest request) throws Exception {

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.getReference(idUsuarioEntidad);

        List<Organismo> organismosActivos = organismoEjb.getPermitirUsuarios(getEntidadActiva(request).getId());
        List<Organismo> organismosAsignados = permisoOrganismoUsuarioEjb.getOrganismosByUsuario(usuarioEntidad.getId());

        // Eliminamos los que ya estén asociados
        for(Organismo organismoAsignado:organismosAsignados){
            organismosActivos.remove(organismoAsignado);
        }

        // Asignamos todos los Organismos al usuario
        for(Organismo organismo: organismosActivos){
            permisoOrganismoUsuarioEjb.crearPermisosUsuarioOrganismo(usuarioEntidad, organismo);
        }

        Mensaje.saveMessageInfo(request, getMessage("usuario.asignar.permisos.ok"));

        return "redirect:/permisos/"+idUsuarioEntidad;

    }

    /**
     * Elimina {@link es.caib.regweb3.model.PermisoLibroUsuario} de la relación de uin Usuario y un Organismo
     */
    @RequestMapping(value = "/{idUsuarioEntidad}/{idOrganismo}/eliminar", method = RequestMethod.GET)
    public String eliminarPermisos(@PathVariable Long idUsuarioEntidad,@PathVariable Long idOrganismo) throws Exception {


        permisoOrganismoUsuarioEjb.eliminarPermisosUsuarioOrganismo(idUsuarioEntidad, idOrganismo);

        return "redirect:/permisos/"+idUsuarioEntidad;

    }

    /**
     * Eliminar la asignación de un Usuario a una Entidad
     */
    @RequestMapping(value = "/{idUsuarioEntidad}/delete", method = RequestMethod.GET)
    public String eliminarAsignacion(@PathVariable Long idUsuarioEntidad, HttpServletRequest request) {

        try {
            UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findById(idUsuarioEntidad);

            if (entidadEjb.esAdministrador(usuarioEntidad)) {
                Mensaje.saveMessageError(request, getMessage("usuarioEntidad.administrador"));
                return "redirect:/entidad/usuarios";
            }

            // Eliminamos todos sus PermisoLibroUsuario
            permisoOrganismoUsuarioEjb.eliminarByUsuario(idUsuarioEntidad);

            // Comprobar si el usuario tiene Registros en la Entidad
            if (entidadEjb.puedoEliminarlo(idUsuarioEntidad)) {
                // Eliminamos las notificaciones
                notificacionEjb.eliminarByUsuario(usuarioEntidad.getId());
                // Si no tiene registros relacinados, lo eliminamos definitivamente.
                usuarioEntidadEjb.remove(usuarioEntidad);

                Mensaje.saveMessageInfo(request, getMessage("usuario.eliminado"));
            } else {
                // Desactivamos este usuario de la Entidad
                usuarioEntidad.setActivo(false);
                usuarioEntidadEjb.merge(usuarioEntidad);

                Mensaje.saveMessageInfo(request, getMessage("usuario.desactivado"));
            }

        } catch (Exception e) {
            Mensaje.saveMessageError(request, "No s'ha eliminat el registre perque està relacionat amb un altra entitat.");
            e.printStackTrace();
        }

        return "redirect:/entidad/usuarios";
    }

    /**
     * Migrar permisos existentes
     */
    @RequestMapping(value = "/migrarPermisos/{idLibro}", method = RequestMethod.GET)
    public String migrarPermisos(@PathVariable Long idLibro, HttpServletRequest request) throws Exception {

        Libro libro = libroEjb.findById(idLibro);

        if(libro != null && libro.getActivo()){

            Integer permisos = permisoOrganismoUsuarioEjb.migrarPermisos(libro);

            Mensaje.saveMessageInfo(request,"Se han creado " + permisos + " permisos");
        }


        return "redirect:/libro/list";
    }


    @InitBinder({"permisoOrganismoUsuarioForm"})
    public void initBinder2(WebDataBinder binder) {
        // Per resoldre el problema dels 256 objectes dins un form
        binder.setAutoGrowCollectionLimit(500);
    }
}
