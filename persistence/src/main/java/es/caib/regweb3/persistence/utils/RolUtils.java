package es.caib.regweb3.persistence.utils;

import es.caib.regweb3.model.Rol;
import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.persistence.ejb.PluginLocal;
import es.caib.regweb3.persistence.ejb.RolLocal;
import es.caib.regweb3.persistence.ejb.UsuarioEntidadLocal;
import es.caib.regweb3.persistence.ejb.UsuarioLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.pluginsib.userinformation.IUserInformationPlugin;
import org.fundaciobit.pluginsib.userinformation.RolesInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ejb.EJB;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class RolUtils {

    public final Logger log = LoggerFactory.getLogger(getClass());

    @EJB(mappedName = PluginLocal.JNDI_NAME)
    private PluginLocal pluginEjb;

    @EJB(mappedName = RolLocal.JNDI_NAME)
    private RolLocal rolEjb;

    @EJB(mappedName = UsuarioEntidadLocal.JNDI_NAME)
    private UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = UsuarioLocal.JNDI_NAME)
    private UsuarioLocal usuarioEjb;


    /**
     * Obtiene los Roles del usuario mediante el PLUGIN_USER_INFORMATION
     * @param identificador
     * @return
     * @throws Exception
     * @throws I18NException
     */
    public List<Rol> obtenerRolesUserPlugin(String identificador) throws Exception, I18NException {

        IUserInformationPlugin loginPlugin = (IUserInformationPlugin) pluginEjb.getPlugin(null,RegwebConstantes.PLUGIN_USER_INFORMATION, true);
        RolesInfo rolesInfo = loginPlugin.getRolesByUsername(identificador);

        List<String> roles = new ArrayList<String>();
        List<Rol> rolesUsuario = null;

        if(rolesInfo != null && rolesInfo.getRoles().length > 0){

            Collections.addAll(roles, rolesInfo.getRoles());
            if(roles.size() > 0){
                rolesUsuario = rolEjb.getByRol(roles);
            }
        }else{
            log.info("El usuario " + identificador + " no dispone de ningun Rol de REGWEB3 en el sistema de autentificacion");
        }

        return rolesUsuario;
    }

    /**
     * Actualiza los Roles de los usuarios RWE_ADMIN de la Entidad
     * @param idEntidad
     * @throws Exception
     * @throws I18NException
     */
    public void actualizarRolesUsuariosAdmin(Long idEntidad) throws Exception, I18NException {

        // Antes de nada, actualizamos los Roles contra Seycon de los UsuarioEntidad
        List<Usuario> usuarios = usuarioEntidadEjb.findActivosByEntidad(idEntidad);
        for (Usuario usuario : usuarios) {

            usuarioEjb.actualizarRoles(usuario, obtenerRolesUserPlugin(usuario.getIdentificador()));
        }
    }
}
