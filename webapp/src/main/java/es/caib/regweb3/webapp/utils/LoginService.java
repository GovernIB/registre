package es.caib.regweb3.webapp.utils;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RolUtils;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.security.LoginInfo;
import org.apache.log4j.Logger;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.pluginsib.userinformation.IUserInformationPlugin;
import org.fundaciobit.pluginsib.userinformation.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by Fundació BIT.
 * Clase para gestionar los Roles, Entidades, Oficinas del Usuario autenticado
 *
 * @author earrivi
 * Date: 27/03/14
 */
@Service
public class LoginService {

    public final Logger log = Logger.getLogger(getClass());

    @Autowired
    private RolUtils rolUtils;

    @EJB(mappedName = "regweb3/UsuarioEJB/local")
    private UsuarioLocal usuarioEjb;

    @EJB(mappedName = "regweb3/RolEJB/local")
    private RolLocal rolEjb;

    @EJB(mappedName = "regweb3/EntidadEJB/local")
    private EntidadLocal entidadEjb;

    @EJB(mappedName = "regweb3/UsuarioEntidadEJB/local")
    private UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = "regweb3/PermisoLibroUsuarioEJB/local")
    private PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;

    @EJB(mappedName = "regweb3/OficinaEJB/local")
    private OficinaLocal oficinaEjb;

    @EJB(mappedName = "regweb3/RegistroMigradoEJB/local")
    private RegistroMigradoLocal registroMigradoEjb;

    @EJB(mappedName = "regweb3/ConfiguracionEJB/local")
    private ConfiguracionLocal configuracionEjb;

    @EJB(mappedName = "regweb3/OrganismoEJB/local")
    private OrganismoLocal organismoEjb;

    @EJB(mappedName = "regweb3/PluginEJB/local")
    private PluginLocal pluginEjb;


    /**
     * Dado un usuario autenticado, realiza todas las configuraciones necesarias para su funcionamiento en REGWEB3.
     * Obtiene sus roles y sus autorizaciones.
     *
     * @param usuario
     * @throws Exception
     */
    public LoginInfo configurarUsuario(Usuario usuario, HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();

        // Eliminamos de la sesion el LoginInfo con toda la información del usuario autenticado
        session.removeAttribute(RegwebConstantes.SESSION_LOGIN_INFO);

        LoginInfo loginInfo = new LoginInfo(usuario, null, null);

        // Obtenemos y asignamos los Roles del Usuario y el Usuario
        Rol rolActivo = obtenerCredenciales(usuario, loginInfo, request);

        // Según el RolActivo, obtenemos sus autorizaciones
        autorizarRol(rolActivo, loginInfo);

        // Almacenamos en la sesion el LoginInfo con toda la información del usuario autenticado
        session.setAttribute(RegwebConstantes.SESSION_LOGIN_INFO, loginInfo);

        return loginInfo;
    }


    /**
     * Obtiene los Roles del usuario autenticado mediante el plugin de Login.
     * Almacenanos los Roles del usuario
     * Almacenanos el usuario
     * Crea la variable que contiene todos los Roles
     * Crea la variable que contiene el RolActivo
     *
     * @param usuario
     * @param request
     * @throws Exception
     */
    private Rol obtenerCredenciales(Usuario usuario, LoginInfo loginInfo, HttpServletRequest request) throws Exception {

        List<Rol> rolesUsuario = obtenerRolesUsuarioAutenticado(request);

        log.info("Usuario autenticado: " + usuario.getNombreCompleto() + " - Roles: " + Arrays.toString(rolesUsuario.toArray()));

        // Actualizamos los Roles del usuario en la bbdd, según los obtenidos del sistema externo
        usuario.setRoles(rolesUsuario);
        usuario = usuarioEjb.merge(usuario);

        // Almacenamos los Roles que dispone el usuario.
        loginInfo.setRolesAutenticado(rolesUsuario);

        // Almacenamos el RolActivo del usuario.
        if (rolesUsuario.size() > 0) {
            loginInfo.setRolActivo(rolesUsuario.get(0));
        }

        return loginInfo.getRolActivo();
    }


    /**
     * Según el RolActivo del Usuario autenticado, obtiene sus entidades y oficinas de registro.
     *
     * @param rolActivo
     * @param loginInfo
     * @throws Exception
     */
    private void autorizarRol(Rol rolActivo, LoginInfo loginInfo) throws Exception {

        log.info("Autorizando: " + loginInfo.getUsuarioAutenticado().getNombreCompleto() + " como " + rolActivo.getNombre());

        // Reset de las variables
        loginInfo.resetDatos();

        // Si el RolActivo del usuario autenticado es Administrador de Entidad
        if (rolActivo.getNombre().equals(RegwebConstantes.RWE_ADMIN)) {

            asignarEntidadesAdministradas(loginInfo, loginInfo.getEntidadActiva());

            // Si RolActivo del usuario autenticado es Operador
        } else if (rolActivo.getNombre().equals(RegwebConstantes.RWE_USUARI)) {

            //Asignamos las Entidades donde tiene acceso el usuario operador
            asignarEntidadesOperador(loginInfo, loginInfo.getEntidadActiva());

            //Asignamos las oficinas donde tiene acceso el usuario operador
            asignarOficinasRegistro(loginInfo);

            // Asigna la Configuración del SuperAdministrador
        } else if (rolActivo.getNombre().equals(RegwebConstantes.RWE_SUPERADMIN)) {
            List<Configuracion> configuraciones = configuracionEjb.getAll();
            if (configuraciones.size() > 0) {
                loginInfo.setConfiguracion(configuraciones.get(0));
            }
        }

    }


    /**
     * Asigna las Entidades de las que el Usuario AdministradorEntidad es propietario o administrador.
     *
     * @param entidadActiva
     * @param loginInfo
     * @throws Exception
     */
    private void asignarEntidadesAdministradas(LoginInfo loginInfo, Entidad entidadActiva) throws Exception {

        // Obtenemos las entidades administradas
        ArrayList<Entidad> entidadesAdministradas = new ArrayList<Entidad>(entidadEjb.getEntidadesAdministrador(loginInfo.getUsuarioAutenticado().getId()));

        // Obtenemos las entidades propietarias
        ArrayList<Entidad> entidadesPropietario = new ArrayList<Entidad>(entidadEjb.getEntidadesPropietario(loginInfo.getUsuarioAutenticado().getId()));

        // Las guardamos
        loginInfo.getEntidades().addAll(entidadesAdministradas);
        loginInfo.getEntidades().addAll(entidadesPropietario);

        // Definimos la Entidad Activa
        if (entidadActiva != null && loginInfo.getEntidades().contains(entidadActiva)) {
            loginInfo.setEntidadActiva(entidadActiva);
        } else if (loginInfo.getEntidades().size() > 0) {
            entidadActiva = entidadEjb.findById(loginInfo.getEntidades().get(0).getId());
            loginInfo.setEntidadActiva(entidadActiva);
        }

        log.info("Entidades administradas: " + entidadesAdministradas.size());
        log.info("Entidades propietario: " + entidadesPropietario.size());

        if (entidadActiva != null) {
            // UsuarioEntidadActivo
            setUsuarioEntidadActivo(loginInfo, entidadActiva);

            // Registros migrados
            tieneMigrados(loginInfo);
        }

    }

    /**
     * Obtiene las Entidades a las que el Usuario pertenece
     *
     * @throws Exception
     */
    private UsuarioEntidad asignarEntidadesOperador(LoginInfo loginInfo, Entidad entidadActiva) throws Exception {

        // Obtenemos las entidades a las que el Usuario está asociado
        loginInfo.setEntidades(usuarioEntidadEjb.getEntidadesByUsuario(loginInfo.getUsuarioAutenticado().getId()));

        // Si está asociado en alguna Entidad
        if (loginInfo.getEntidades().size() > 0) {

            // Entidad Activa
            if (entidadActiva != null && loginInfo.getEntidades().contains(entidadActiva)) {
                loginInfo.setEntidadActiva(entidadActiva);
            } else if (loginInfo.getEntidades().size() > 0) {
                loginInfo.setEntidadActiva(entidadEjb.findById(loginInfo.getEntidades().get(0).getId()));
            }

            //UsuarioEntidadActivo
            return setUsuarioEntidadActivo(loginInfo, loginInfo.getEntidadActiva());
        }

        return null;

    }

    /**
     * Asigna las Oficinas a las cuales el UsuarioEntidad puede Registrar y puede Administrar
     *
     * @param loginInfo
     * @throws Exception
     */
    private void asignarOficinasRegistro(LoginInfo loginInfo) throws Exception {

        //Obtenemos los Libros donde el UsuarioEntidad tenga permisos de (Registro, Modificación o Administración)
        List<Libro> librosRegistro = permisoLibroUsuarioEjb.getLibrosRegistro(loginInfo.getUsuarioEntidadActivo().getId());
        log.info("Libros registro usuario: " + Arrays.toString(librosRegistro.toArray()));

        // Si no hay libros en los que podamos registrar, buscamos en los que podamos consultar.
        if (librosRegistro.isEmpty()) {
            librosRegistro = permisoLibroUsuarioEjb.getLibrosConsulta(loginInfo.getUsuarioEntidadActivo().getId());
            log.info("Libros registro consulta: " + Arrays.toString(librosRegistro.toArray()));
        }

        //Obtenemos los Libros donde el UsuarioEntidad puede Administrar
        loginInfo.setLibrosAdministrados(permisoLibroUsuarioEjb.getLibrosAdministrados(loginInfo.getUsuarioEntidadActivo().getId()));
        log.info("Libros administrados usuario: " + Arrays.toString(loginInfo.getLibrosAdministrados().toArray()));

        // Obtenemos las Oficinas que pueden registrar en los Libros
        loginInfo.setOficinasRegistro(oficinaEjb.oficinasRegistro(librosRegistro));
        log.info("Oficinas registro usuario: " + Arrays.toString(loginInfo.getOficinasRegistro().toArray()));

        // Si la Entidad está en SIR, obtenemos las Oficinas SIR a las que tiene acceso
        if (loginInfo.getEntidadActiva().getSir()) {

            //Obtenemos los Libros donde el UsuarioEntidad tenga permiso SIR
            List<Libro> librosSIR = permisoLibroUsuarioEjb.getLibrosPermiso(loginInfo.getUsuarioEntidadActivo().getId(), RegwebConstantes.PERMISO_SIR, false);
            log.info("Libros SIR usuario: " + Arrays.toString(librosSIR.toArray()));

            //Obtenemos las Oficinas que pueden operar con los Libros anteriores
            LinkedHashSet<Oficina> oficinasSIR = oficinaEjb.oficinasSIR(librosRegistro);  // Utilizamos un Set porque no permite duplicados
            log.info("Oficinas SIR usuario: " + Arrays.toString(oficinasSIR.toArray()));

            // Las añadimos al listado general de oficinas
            loginInfo.getOficinasRegistro().addAll(oficinasSIR);

            // Indicamos si la oficina es SIR para que aparezca en el listado
            for (Oficina oficina : loginInfo.getOficinasRegistro()) {

                oficina.setSirRecepcion(oficinaEjb.isSIRRecepcion(oficina.getId()));
                oficina.setSirEnvio(oficinaEjb.isSIREnvio(oficina.getId()));

            }
        }


        // Comprobamos si el usuario tiene última Oficina utilizada.
        if (loginInfo.getUsuarioEntidadActivo().getUltimaOficina() != null && loginInfo.getOficinasRegistro().contains(new Oficina(loginInfo.getUsuarioEntidadActivo().getUltimaOficina().getId()))) {

            asignarOficinaActiva(oficinaEjb.findById(loginInfo.getUsuarioEntidadActivo().getUltimaOficina().getId()), loginInfo);

        } else if (loginInfo.getOficinasRegistro().size() > 0) {

            asignarOficinaActiva(oficinaEjb.findById(loginInfo.getOficinasRegistro().iterator().next().getId()), loginInfo);
        }

        //RegistrosMigrados
        tieneMigrados(loginInfo);

    }

    /**
     * @param oficinaNueva
     * @param loginInfo
     * @throws Exception
     */
    public void asignarOficinaActiva(Oficina oficinaNueva, LoginInfo loginInfo) throws Exception {

        if (oficinaNueva != null) {

            // Guardamos  la nueva OficinaActiva
            loginInfo.setOficinaActiva(oficinaNueva);

            // Guardamos  los Organismos OficiaActiva
            loginInfo.setOrganismosOficinaActiva(organismoEjb.getByOficinaActiva(loginInfo.getOficinaActiva(), RegwebConstantes.ESTADO_ENTIDAD_VIGENTE));

            if (loginInfo.getEntidadActiva().getSir()) {
                // Comprobamos si la Oficina está integrada en SIR
                loginInfo.getOficinaActiva().setSirRecepcion(oficinaEjb.isSIRRecepcion(loginInfo.getOficinaActiva().getId()));
                loginInfo.getOficinaActiva().setSirEnvio(oficinaEjb.isSIREnvio(loginInfo.getOficinaActiva().getId()));
            }

            // Actualizamos la última Oficina del Usuario
            usuarioEntidadEjb.actualizarOficinaUsuario(loginInfo.getUsuarioEntidadActivo().getId(), loginInfo.getOficinaActiva().getId());
        } else {

            // Guardamos  la nueva OficinaActiva
            loginInfo.setOficinaActiva(null);
        }

    }

    /**
     * Actualiza la variable de Registros Migrados, según la entidad Activa
     *
     * @param loginInfo
     * @throws Exception
     */
    private void tieneMigrados(LoginInfo loginInfo) throws Exception {
        loginInfo.setRegistrosMigrados(registroMigradoEjb.tieneRegistrosMigrados(loginInfo.getEntidadActiva().getId()));

    }

    /**
     * @param entidadNueva
     * @throws Exception
     */
    public void cambioEntidad(Entidad entidadNueva, LoginInfo loginInfo) throws Exception {
        log.info("Cambiando Entidad activa a: " + entidadNueva.getNombre());

        // Asociamos la nueva EntidadActiva
        loginInfo.setEntidadActiva(entidadNueva);

        // Asociamos lel nuevo UsuarioEntidadActivo
        setUsuarioEntidadActivo(loginInfo, entidadNueva);

        if (loginInfo.getRolActivo().getNombre().equals(RegwebConstantes.RWE_USUARI)) { // Solo si es Operador
            asignarOficinasRegistro(loginInfo);

        } else {
            tieneMigrados(loginInfo);
        }

    }

    /**
     * Realiza el cambio de Rol para un usuario autenticado
     *
     * @param rolNuevo
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public Boolean cambioRol(Rol rolNuevo, LoginInfo loginInfo) throws Exception {
        log.info("Cambiando el rol a: " + rolNuevo.getNombre());

        if (loginInfo.getRolesAutenticado().contains(rolNuevo)) {
            loginInfo.setRolActivo(rolNuevo);
            autorizarRol(rolNuevo, loginInfo);

            return true;
        } else {
            return false;
        }
    }

    /**
     * Crea un nuevo usuario en REGWEB3, a partir del identificador de Seycon, obtiene sus
     * datos personales de la bbdd de Seycon.
     *
     * @param identificador
     * @return
     * @throws Exception
     */
    public Usuario crearUsuario(String identificador) throws Exception, I18NException {

        IUserInformationPlugin loginPlugin = (IUserInformationPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_USER_INFORMATION);
        UserInfo regwebUserInfo = loginPlugin.getUserInfoByUserName(identificador);

        if (regwebUserInfo != null) { // Si el documento no existe en REGWEB3

            Usuario usuario = new Usuario();
            usuario.setNombre(regwebUserInfo.getName());

            //Idioma por defecto
            Long idioma = RegwebConstantes.IDIOMA_ID_BY_CODIGO.get(Configuracio.getDefaultLanguage());
            usuario.setIdioma(idioma);

            if (regwebUserInfo.getSurname1() != null) {
                usuario.setApellido1(regwebUserInfo.getSurname1());
            } else {
                usuario.setApellido1("");
            }

            if (regwebUserInfo.getSurname2() != null) {
                usuario.setApellido2(regwebUserInfo.getSurname2());
            } else {
                usuario.setApellido2("");
            }

            usuario.setDocumento(regwebUserInfo.getAdministrationID());
            usuario.setIdentificador(identificador);

            // Email
            if (regwebUserInfo.getEmail() != null) {
                usuario.setEmail(regwebUserInfo.getEmail());
            } else {
                usuario.setEmail("no hi ha email");
            }

            // Tipo Usuario
            if (identificador.startsWith("$")) {
                usuario.setTipoUsuario(RegwebConstantes.TIPO_USUARIO_APLICACION);
            } else {
                usuario.setTipoUsuario(RegwebConstantes.TIPO_USUARIO_PERSONA);
            }

            // Roles
            List<Rol> roles = rolUtils.obtenerRolesUserPlugin(identificador);
            if (roles == null || roles.size() == 0) {
                log.info("El usuario " + identificador + " no dispone de ningun Rol valido para REGWEB3");
                return null;
            } else {
                usuario.setRoles(roles);
            }

            // Guardamos el nuevo Usuario
            usuario = usuarioEjb.persist(usuario);
            log.info("Usuario " + usuario.getNombreCompleto() + " creado correctamente");
            return usuario;
        } else {
            log.info("Usuario " + identificador + " no encontrado en el sistema de usuarios");
            return null;
        }

    }

    /**
     * Guardamos  el Usuario autenticado
     *
     * @param usuario
     */
    public void setUsuarioAutenticado(Usuario usuario, LoginInfo loginInfo) throws Exception {

        loginInfo.setUsuarioAutenticado(usuario);
    }

    /**
     * Guardamos  el Usuario autenticado
     *
     * @param entidad
     * @param loginInfo
     */
    private UsuarioEntidad setUsuarioEntidadActivo(LoginInfo loginInfo, Entidad entidad) throws Exception {

        loginInfo.setUsuarioEntidadActivo(usuarioEntidadEjb.findByUsuarioEntidadActivo(loginInfo.getUsuarioAutenticado().getId(), entidad.getId()));

        loginInfo.setEnlaceDir3(PropiedadGlobalUtil.getEnlaceDir3(entidad.getId()));
        loginInfo.setMostrarAvisos(PropiedadGlobalUtil.getMostrarAvisos(entidad.getId()));

        log.info("Entidad activa usuario: " + entidad.getNombre() + " - " + loginInfo.getUsuarioAutenticado().getNombreCompleto());

        return loginInfo.getUsuarioEntidadActivo();
    }

    /**
     * Comprueba si un usuario existe en el sistema de usuarios, mediante su identificador
     *
     * @param identificador
     * @return
     * @throws Exception
     */
    public Boolean existeIdentificador(String identificador) throws Exception, I18NException {
        IUserInformationPlugin loginPlugin = (IUserInformationPlugin) pluginEjb.getPlugin(null, RegwebConstantes.PLUGIN_USER_INFORMATION);
        UserInfo regwebUserInfo = loginPlugin.getUserInfoByUserName(identificador);

        return regwebUserInfo != null;
    }

    /**
     * Obtiene los Roles del usuario autenticado
     *
     * @param request
     * @return
     * @throws Exception
     */
    private List<Rol> obtenerRolesUsuarioAutenticado(HttpServletRequest request) throws Exception {

        List<Rol> rolesUsuario = null;

        List<String> roles = new ArrayList<String>();

        if (request.isUserInRole(RegwebConstantes.RWE_SUPERADMIN)) {
            roles.add(RegwebConstantes.RWE_SUPERADMIN);
        }
        if (request.isUserInRole(RegwebConstantes.RWE_ADMIN)) {
            roles.add(RegwebConstantes.RWE_ADMIN);
        }
        if (request.isUserInRole(RegwebConstantes.RWE_USUARI)) {
            roles.add(RegwebConstantes.RWE_USUARI);
        }

        if (roles.size() > 0) {
            rolesUsuario = rolEjb.getByRol(roles);
        }

        return rolesUsuario;
    }
}

