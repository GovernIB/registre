package es.caib.regweb3.webapp.security;

import es.caib.regweb3.model.Rol;
import es.caib.regweb3.model.Usuario;
import es.caib.regweb3.persistence.ejb.RolLocal;
import es.caib.regweb3.persistence.ejb.UsuarioLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.utils.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.naming.InitialContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * @author anadal
 */

@Component
public class AuthenticationSuccessListener implements
        ApplicationListener<InteractiveAuthenticationSuccessEvent> {

    protected final Logger log = LoggerFactory.getLogger(getClass());


    private UsuarioLocal usuarioEjb = null;

    @Autowired
    private LoginService loginService;


    @Override
    public synchronized void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {

        SecurityContext sc = SecurityContextHolder.getContext();
        Authentication au = sc.getAuthentication();
        LoginInfo loginInfo = null;

        if (au == null) {
            // TODO traduccio
            throw new LoginException("NO PUC ACCEDIR A LA INFORMACIO de AUTENTICACIO");
        }

        User user = (User) au.getPrincipal();

        String identificador = user.getUsername();
        log.info(" =================================================================");
        log.info(" ============ Login Usuari: " + identificador);

        // Cercam si té el ROLE_USER o ROLE_ADMIN
        Collection<GrantedAuthority> authorities = user.getAuthorities();

        List<Rol> rolesUsuario = obtenerRolesUsuarioAutenticado(authorities);

        // Info Usuari
        Usuario usuario = null;

        if (usuarioEjb == null) {

            try {
                usuarioEjb = (UsuarioLocal) new InitialContext().lookup(UsuarioLocal.JNDI_NAME);

                usuario = usuarioEjb.findByIdentificador(identificador);

            } catch (Exception e) {
               log.info("No puc accedir al gestor de BBDD d´obtenció de informació de usuari per " + identificador);
                throw new LoginException("No puc accedir al gestor de BBDD d´obtenció de" +
                        " informació de usuari per " + identificador + ": " + e.getMessage(), e);
            }
        }

        if (usuario != null) { // El usuario existe en REGWEB3

            log.info("Usuario: " + usuario.getNombreCompleto());
            // Si el usuario es de tipo aplicación, no puede acceder.
            if (usuario.getTipoUsuario().equals(RegwebConstantes.TIPO_USUARIO_APLICACION)) {
                log.info(usuario.getNombreCompleto() + " es un usuario de tipo aplicación y no tiene acceso a REGWEB3.");

                throw new LoginException("El usuario " + identificador + " es de tipo aplicacion y no tiene acceso a la web");
            }

            // Configuramos en la sesion el usuario, sus roles, oficinas, etc..
            try {
                //loginInfo = loginService.configurarUsuario(user, authorities, usuario, rolesUsuario);
            } catch (Exception e) {
                throw new LoginException("Ha ocurrido un error configurando el acceso a REGWEB3: " + e.getMessage());
            }

        } else { // El usuario NO existe en REGWEB3

            log.info("El usuario " + identificador + " no existe en REGWEB3, lo crearemos.");

            Usuario nuevoUsuario = null;
            try {
                nuevoUsuario = loginService.crearUsuario(identificador);
            } catch (Exception e) {
                throw new LoginException("Ha ocurrido un error configurando el acceso a REGWEB3: " + e.getMessage());
            }

            if (nuevoUsuario != null) {
                log.info("Usuario creado: " + nuevoUsuario.getNombreCompleto());

                // Si el usuario es de tipo aplicación, no puede acceder.
                if (nuevoUsuario.getTipoUsuario().equals(RegwebConstantes.TIPO_USUARIO_APLICACION)) {
                    log.info(nuevoUsuario.getNombreCompleto() + " es un usuario de tipo aplicación y no tiene acceso a REGWEB3.");

                    throw new LoginException("El usuario " + identificador + " es de tipo aplicacion y no tiene acceso a la web.");
                }

                // Configuramos en la sesion el usuario, sus roles, oficinas, etc..
                try {
                    //loginInfo = loginService.configurarUsuario(user, authorities, nuevoUsuario, rolesUsuario);
                } catch (Exception e) {

                    e.printStackTrace();
                    throw new LoginException("Ha ocurrido un error configurando el acceso a REGWEB3");
                }
            } else {

                throw new LoginException("El usuario " + identificador + " no esta autorizado.");
            }

        }


        // and set the authentication of the current Session context
        SecurityContextHolder.getContext().setAuthentication(loginInfo.generateToken());
        log.info("LoginInfo: " + loginInfo.toString());
        log.info(">>>>>> Final del Process d'autenticació.");
        log.info(" =================================================================");

    }


    /**
     * Obtiene los Roles del usuario autenticado
     * @return
     * @throws Exception
     */
    private List<Rol> obtenerRolesUsuarioAutenticado(Collection<GrantedAuthority> authorities){

        List<Rol> rolesUsuario = null;

        List<String> roles = new ArrayList<String>();

        for (GrantedAuthority grantedAuthority : authorities) {
            String rol = grantedAuthority.getAuthority();
            log.info("Rol SEYCON : " + rol);
            if (RegwebConstantes.RWE_USUARI.equals(rol)) {
                roles.add(RegwebConstantes.RWE_USUARI);
            }
            if (RegwebConstantes.RWE_ADMIN.equals(rol)) {
                roles.add(RegwebConstantes.RWE_ADMIN);
            }
            if (RegwebConstantes.RWE_SUPERADMIN.equals(rol)) {
                roles.add(RegwebConstantes.RWE_SUPERADMIN);
            }
        }

        if(roles.size() > 0){

            try {
                RolLocal rolEjb = (RolLocal) new InitialContext().lookup(RolLocal.JNDI_NAME);

                rolesUsuario = rolEjb.getByRol(roles);

            } catch (Exception e) {
                // TODO traduccio
                throw new LoginException("No puc accedir al gestor de BBDD d´obtenció de" +
                        " informació de rols de usuari "  + e.getMessage(), e);
            }


        }

        return rolesUsuario;
    }

}
