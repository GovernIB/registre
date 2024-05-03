package es.caib.regweb3.webapp.utils;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.persistence.utils.RolUtils;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.Dir3Caib;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.StringUtils;
import es.caib.regweb3.webapp.security.LoginInfo;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.pluginsib.userinformation.IUserInformationPlugin;
import org.fundaciobit.pluginsib.userinformation.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private RolUtils rolUtils;

    @EJB(mappedName = UsuarioLocal.JNDI_NAME)
    private UsuarioLocal usuarioEjb;

    @EJB(mappedName = RolLocal.JNDI_NAME)
    private RolLocal rolEjb;

    @EJB(mappedName = EntidadLocal.JNDI_NAME)
    private EntidadLocal entidadEjb;

    @EJB(mappedName = UsuarioEntidadLocal.JNDI_NAME)
    private UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = PermisoOrganismoUsuarioLocal.JNDI_NAME)
    private PermisoOrganismoUsuarioLocal permisoOrganismoUsuarioEjb;

    @EJB(mappedName = OficinaLocal.JNDI_NAME)
    private OficinaLocal oficinaEjb;

    @EJB(mappedName = RegistroMigradoLocal.JNDI_NAME)
    private RegistroMigradoLocal registroMigradoEjb;

    @EJB(mappedName =ConfiguracionLocal.JNDI_NAME)
    private ConfiguracionLocal configuracionEjb;

    @EJB(mappedName = OrganismoLocal.JNDI_NAME)
    private OrganismoLocal organismoEjb;

    @EJB(mappedName = PluginLocal.JNDI_NAME)
    private PluginLocal pluginEjb;

    @EJB(mappedName = PlantillaLocal.JNDI_NAME)
    private PlantillaLocal plantillaEjb;


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
     * Actualizamos los Roles del usuario en bbdd
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

        List<Rol> rolesUsuario = obtenerRolesWebUsuarioAutenticado(request);
        if(rolesUsuario != null){
            log.info("Usuario autenticado: " + usuario.getNombreCompleto() + " - Roles: " + Arrays.toString(rolesUsuario.toArray()));
        }else{
            log.info("Usuario autenticado: " + usuario.getNombreCompleto() + " - Sin Roles Web ");
        }

        // Actualizamos los Roles del usuario en la bbdd, según los obtenidos del sistema externo
        try {
            List<Rol> roles = rolUtils.obtenerRolesUserPlugin(usuario.getIdentificador());
            usuarioEjb.actualizarRoles(usuario, roles);
        } catch (I18NException e) {
            e.printStackTrace();
            log.info("Ha ocurrido un error actualizando los roles del usuario: " + usuario.getIdentificador());
            throw new Exception("a ocurrido un error actualizando los roles del usuario");
        }

        // Almacenamos los Roles Web que dispone el usuario.
        loginInfo.setRolesAutenticado(rolesUsuario);

        // Almacenamos el RolActivo del usuario.
        if (rolesUsuario.size() > 0) {

            if(rolesUsuario.contains(new Rol(RegwebConstantes.RWE_ADMIN))){
                loginInfo.setRolActivo(rolesUsuario.get(rolesUsuario.indexOf(new Rol(RegwebConstantes.RWE_ADMIN))));
            }else{
                loginInfo.setRolActivo(rolesUsuario.get(0));
            }

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


        switch (rolActivo.getNombre()) {
            case RegwebConstantes.RWE_ADMIN: // Si el RolActivo del usuario autenticado es Administrador de Entidad

                asignarEntidadesAdministradas(loginInfo);
            break;
            case RegwebConstantes.RWE_USUARI: // Si RolActivo del usuario autenticado es Operador

                //Asignamos las Entidades donde tiene acceso el usuario operador
                UsuarioEntidad usuarioEntidad = asignarEntidadesOperador(loginInfo);

                if(usuarioEntidad == null){
                    throw new Exception("El usuario no esta relacionado con ninguna Entidad, contacte con el Administrador.");
                }

                //Asignamos las oficinas donde tiene acceso el usuario operador
                asignarOficinas(loginInfo);

                //Asignamos las plantillas
                asignarPlantillas(loginInfo);
            break;
            case RegwebConstantes.RWE_SUPERADMIN: // Asigna la Configuración del SuperAdministrador
                List<Configuracion> configuraciones = configuracionEjb.getAll();
                if (!configuraciones.isEmpty()) {
                    loginInfo.setConfiguracion(configuraciones.get(0));
                }
                loginInfo.setDir3Caib(new Dir3Caib(PropiedadGlobalUtil.getDir3CaibServer(), PropiedadGlobalUtil.getDir3CaibUsername(), PropiedadGlobalUtil.getDir3CaibPassword()));

            break;
        }

    }


    /**
     * Asigna las Entidades de las que el Usuario AdministradorEntidad es propietario o administrador.
     *
     * @param loginInfo
     * @throws Exception
     */
    private void asignarEntidadesAdministradas(LoginInfo loginInfo) throws Exception {

        Entidad entidadActiva = null;

        // Obtenemos las entidades administradas
        ArrayList<Entidad> entidadesAdministradas = new ArrayList<Entidad>(entidadEjb.getEntidadesAdministrador(loginInfo.getUsuarioAutenticado().getId()));

        // Obtenemos las entidades propietarias
        ArrayList<Entidad> entidadesPropietario = new ArrayList<Entidad>(entidadEjb.getEntidadesPropietario(loginInfo.getUsuarioAutenticado().getId()));

        // Las guardamos
        loginInfo.getEntidades().addAll(entidadesAdministradas);
        loginInfo.getEntidades().addAll(entidadesPropietario);

        // Definimos la Entidad Activa
        if (!loginInfo.getEntidades().isEmpty()) {
            entidadActiva = entidadEjb.findByIdLigero(loginInfo.getEntidades().get(0).getId());
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
    private UsuarioEntidad asignarEntidadesOperador(LoginInfo loginInfo) throws Exception {

        // Obtenemos las entidades a las que el Usuario está asociado
        loginInfo.setEntidades(usuarioEntidadEjb.getEntidadesByUsuario(loginInfo.getUsuarioAutenticado().getId()));

        // Si está asociado en alguna Entidad
        if (!loginInfo.getEntidades().isEmpty()) {

            // Entidad Activa
            loginInfo.setEntidadActiva(entidadEjb.findByIdLigero(loginInfo.getEntidades().get(0).getId()));

            //UsuarioEntidadActivo
            return setUsuarioEntidadActivo(loginInfo, loginInfo.getEntidadActiva());
        }

        return null;

    }

    /**
     * Asigna las Oficinas del UsuarioEntidad autenticado
     *
     * @param loginInfo
     * @throws Exception
     */
    private void asignarOficinas(LoginInfo loginInfo) throws Exception {

        // Obtenemos los Organismos donde el usuario puede Registrar entradas y de ahí las oficinas que dan servicio
        List<Organismo> organismosRegistroEntrada = permisoOrganismoUsuarioEjb.getOrganismosPermiso(loginInfo.getUsuarioEntidadActivo().getId(), RegwebConstantes.PERMISO_REGISTRO_ENTRADA);
        LinkedHashSet<Oficina> oficinasRegistroEntrada = oficinaEjb.oficinasServicio(organismosRegistroEntrada, false);

        loginInfo.setOficinasRegistroEntrada(oficinasRegistroEntrada);

        // Obtenemos los Organismos donde el usuario puede Registrar salidas y de ahí las oficinas que dan servicio
        List<Organismo> organismosRegistroSalida = permisoOrganismoUsuarioEjb.getOrganismosPermiso(loginInfo.getUsuarioEntidadActivo().getId(), RegwebConstantes.PERMISO_REGISTRO_SALIDA);
        LinkedHashSet<Oficina> oficinasRegistroSalida = oficinaEjb.oficinasServicio(organismosRegistroSalida, false);

        loginInfo.setOficinasRegistroSalida(oficinasRegistroSalida);

        // Obtenemos los Organismos donde el usuario puede consultar entradas y de ahí las oficinas que dan servicio
        List<Organismo> organismosConsultaEntrada = permisoOrganismoUsuarioEjb.getOrganismosPermiso(loginInfo.getUsuarioEntidadActivo().getId(), RegwebConstantes.PERMISO_CONSULTA_REGISTRO_ENTRADA);
        LinkedHashSet<Oficina> oficinasConsultaEntrada = oficinaEjb.oficinasServicio(organismosConsultaEntrada, false);

        loginInfo.setOrganismosConsultaEntrada(organismosConsultaEntrada);
        loginInfo.setOficinasConsultaEntrada(oficinasConsultaEntrada);

        // Obtenemos los Organismos donde el usuario puede consultar salidas y de ahí las oficinas que dan servicio
        List<Organismo> organismosConsultaSalida = permisoOrganismoUsuarioEjb.getOrganismosPermiso(loginInfo.getUsuarioEntidadActivo().getId(), RegwebConstantes.PERMISO_CONSULTA_REGISTRO_SALIDA);
        LinkedHashSet<Oficina> oficinasConsultaSalida = oficinaEjb.oficinasServicio(organismosConsultaSalida, false);

        loginInfo.setOrganismosConsultaSalida(organismosConsultaSalida);
        loginInfo.setOficinasConsultaSalida(oficinasConsultaSalida);


        // Creamos la lista de Oficinas en las que el usuario puede situarse
        loginInfo.getOficinasAcceso().addAll(oficinasRegistroEntrada);
        loginInfo.getOficinasAcceso().addAll(oficinasRegistroSalida);

        // Si el usuario no puede registrar, añadimos las oficinas donde pueda consultar
        if(loginInfo.getOficinasAcceso().size() == 0){
            loginInfo.getOficinasAcceso().addAll(oficinasConsultaEntrada);
            loginInfo.getOficinasAcceso().addAll(oficinasConsultaSalida);
        }

        // Obtenemos los Organismos donde el UsuarioEntidad es responsable
        loginInfo.setOrganismosResponsable(permisoOrganismoUsuarioEjb.getOrganismosAdministrados(loginInfo.getUsuarioEntidadActivo().getId()));

        // Si la Entidad está en SIR, obtenemos las Oficinas SIR a las que tiene acceso
        if (loginInfo.getEntidadActiva().getSir()) {

            //Obtenemos los Organismos donde el UsuarioEntidad tenga permiso SIR
            LinkedHashSet<Oficina> oficinasSIR = permisoOrganismoUsuarioEjb.getOficinasSir(loginInfo.getUsuarioEntidadActivo().getId());

            // Las añadimos al listado general de oficinas
            loginInfo.getOficinasAcceso().addAll(oficinasSIR);

            // Indicamos si la oficina es SIR para que aparezca en el listado
            for (Oficina oficina : loginInfo.getOficinasAcceso()) {

                oficina.setSirRecepcion(oficinaEjb.isSIRRecepcion(oficina.getId()));
                oficina.setSirEnvio(oficinaEjb.isSIREnvio(oficina.getId()));
            }
        }

        // Comprobamos si el usuario tiene última Oficina utilizada.
        if (loginInfo.getUsuarioEntidadActivo().getUltimaOficina() != null && loginInfo.getOficinasAcceso().contains(oficinaEjb.findById(loginInfo.getUsuarioEntidadActivo().getUltimaOficina().getId()))) {

            asignarOficinaActiva(oficinaEjb.findById(loginInfo.getUsuarioEntidadActivo().getUltimaOficina().getId()), loginInfo);

        } else if (loginInfo.getOficinasAcceso().size() > 0) {

            asignarOficinaActiva(oficinaEjb.findById(loginInfo.getOficinasAcceso().iterator().next().getId()), loginInfo);
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
            loginInfo.setOrganismosOficinaActiva(organismoEjb.getOrganismosRegistro(loginInfo.getOficinaActiva()));

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
     * Asigna las plantillas que el usuario tiene guardadas
     * @param loginInfo
     */
    public void asignarPlantillas(LoginInfo loginInfo) throws Exception{

        loginInfo.setPlantillasEntrada(plantillaEjb.getActivasbyUsuario(loginInfo.getUsuarioEntidadActivo().getId(), RegwebConstantes.REGISTRO_ENTRADA));
        loginInfo.setPlantillasSalida(plantillaEjb.getActivasbyUsuario(loginInfo.getUsuarioEntidadActivo().getId(), RegwebConstantes.REGISTRO_SALIDA));

    }

    /**
     * @param entidadNueva
     * @throws Exception
     */
    public void cambioEntidad(Entidad entidadNueva, LoginInfo loginInfo) throws Exception {
        log.info("Cambiando Entidad activa a: " + entidadNueva.getNombre());

        // Asociamos la nueva EntidadActiva
        loginInfo.setEntidadActiva(entidadNueva);

        // Asociamos el nuevo UsuarioEntidadActivo
        setUsuarioEntidadActivo(loginInfo, entidadNueva);

        if (loginInfo.getRolActivo().getNombre().equals(RegwebConstantes.RWE_USUARI)) { // Solo si es Operador

            // Asignamos oficinas
            loginInfo.resetOficinas();
            asignarOficinas(loginInfo);

            //Asignamos las plantillas
            asignarPlantillas(loginInfo);

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

            // Nombre
            if(StringUtils.isNotEmpty(regwebUserInfo.getName())){
                usuario.setNombre(regwebUserInfo.getName());
            }else{
                usuario.setNombre(identificador);
            }

            //Idioma por defecto
            Long idioma = RegwebConstantes.IDIOMA_ID_BY_CODIGO.get(Configuracio.getDefaultLanguage());
            usuario.setIdioma(idioma);

            // Apellido 1
            if (StringUtils.isNotEmpty(regwebUserInfo.getSurname1())) {
                usuario.setApellido1(regwebUserInfo.getSurname1());
            } else {
                usuario.setApellido1("");
            }

            // Apellido 2
            if (StringUtils.isNotEmpty(regwebUserInfo.getSurname2())) {
                usuario.setApellido2(regwebUserInfo.getSurname2());
            } else {
                usuario.setApellido2("");
            }

            usuario.setDocumento(regwebUserInfo.getAdministrationID());
            usuario.setIdentificador(identificador);

            // Email
            if (StringUtils.isNotEmpty(regwebUserInfo.getEmail())) {
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
        loginInfo.setAyudaUrl(PropiedadGlobalUtil.getAyudaUrl(entidad.getId()));
        loginInfo.setDir3Caib(new Dir3Caib(PropiedadGlobalUtil.getDir3CaibServer(entidad.getId()), PropiedadGlobalUtil.getDir3CaibUsername(entidad.getId()), PropiedadGlobalUtil.getDir3CaibPassword(entidad.getId())));

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
     * Obtiene los Roles Web del usuario autenticado
     *
     * @param request
     * @return
     * @throws Exception
     */
    private List<Rol> obtenerRolesWebUsuarioAutenticado(HttpServletRequest request) throws Exception {

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

