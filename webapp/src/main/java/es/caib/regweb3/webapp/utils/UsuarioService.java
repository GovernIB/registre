package es.caib.regweb3.webapp.utils;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.ejb.*;
import es.caib.regweb3.utils.Configuracio;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.userinformation.IUserInformationPlugin;
import org.fundaciobit.plugins.userinformation.RolesInfo;
import org.fundaciobit.plugins.userinformation.UserInfo;
import org.springframework.stereotype.Component;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by Fundació BIT.
 * Clase para gestionar los Roles, Entidades, Oficinas del Usuario autenticado
 * @author earrivi
 * Date: 27/03/14
 */
@Component
public class UsuarioService {

    public final Logger log = Logger.getLogger(getClass());

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
     * Lo guarda en la sesion, obtiene sus roles y sus autorizaciones.
     * @param usuario
     * @param request
     * @throws Exception
     */
    public void configurarUsuario(Usuario usuario, HttpServletRequest request) throws Exception {

        // Obtenemos y asignamos los Roles del Usuario y el Usuario a la sesión
        Rol rolActivo = obtenerCredenciales(usuario, request);

        // Según el RolActivo, obtenemos sus autorizaciones
        autorizarRol(rolActivo,null, request);
    }


    /**
     * Obtiene los Roles del usuario autenticado mediante el plugin de Login.
     * Almacenanos los Roles del usuario en la sesión
     * Almacenanos el usuario en la sesión
     * Crea la variable de sesión que contiene todos los Roles
     * Crea la variable de sesión que contiene el RolActivo
     * @param usuario
     * @param request
     * @throws Exception
     */
    private Rol obtenerCredenciales(Usuario usuario, HttpServletRequest request) throws Exception{

        HttpSession session = request.getSession();

        log.info("Usuario autenticado: " + usuario.getNombreCompleto());

        // Eliminamos las variables
        eliminarVariablesSesionCredenciales(session);

        List<Rol> rolesUsuario = obtenerRolesUsuarioAutenticado(request);

        log.info("Roles en regweb3: " + Arrays.toString(rolesUsuario.toArray()));

        // Actualizamos los Roles del usuario en la bbdd, según los obtenidos del sistema externo
        usuario.setRoles(rolesUsuario);
        usuario = usuarioEjb.merge(usuario);

        // Almacenamos en la sesión el Usuario autenticado
        session.setAttribute(RegwebConstantes.SESSION_USUARIO, usuario);

        // Almacenamos en la sesion los Roles que dispone el usuario.
        session.setAttribute(RegwebConstantes.SESSION_ROLES, rolesUsuario);

        // Almacenamos en la sesion el RolActivo del usuario.
        Rol rolActivo = null;
        if(rolesUsuario.size() > 0){
            rolActivo = rolesUsuario.get(0);
            session.setAttribute(RegwebConstantes.SESSION_ROL, rolActivo);
        }

        return rolActivo;
    }



    /**
     * Según el RolActivo del Usuario autenticado, obtiene sus entidades y oficinas de registro.
     * @param rolActivo
     * @param request
     * @throws Exception
     */
    private void autorizarRol(Rol rolActivo,Entidad entidadActiva, HttpServletRequest request) throws Exception{


        HttpSession session = request.getSession();
        Usuario usuarioAutenticado = (Usuario) session.getAttribute(RegwebConstantes.SESSION_USUARIO);

        log.info("Autorizando: " + usuarioAutenticado.getNombreCompleto() +" como "+ rolActivo.getNombre());

        // Limpiamos de la session las variables utilizadas
        eliminarVariablesSesion(session);

        // Si el RolActivo del usuario autenticado es Administrador de Entidad
        if(rolActivo.getNombre().equals(RegwebConstantes.ROL_ADMIN)){

            asignarEntidadesAdministradas(usuarioAutenticado,entidadActiva, session);

        // Si RolActivo del usuario autenticado es Operador
        }else if(rolActivo.getNombre().equals(RegwebConstantes.ROL_USUARI)){

            //Asignamos las Entidades donde tiene acceso el usuario operador
            UsuarioEntidad usuarioEntidadActivo = asignarEntidadesOperador(usuarioAutenticado, entidadActiva, session);

            //Asignamos las oficinas donde tiene acceso el usuario operador
            asignarOficinasRegistro(usuarioEntidadActivo, session);

        // Asigna la Configuración del SuperAdministrador
        }else if(rolActivo.getNombre().equals(RegwebConstantes.ROL_SUPERADMIN)){
            List<Configuracion> configuraciones = configuracionEjb.getAll();
            if(configuraciones.size()>0) {
                Configuracion configuracion = configuraciones.get(0);
                asignarConfiguracionAdministrador(configuracion, request);
            }
        }

    }


    /**
     * Asigna las Entidades de las que el Usuario AdministradorEntidad es propietario o administrador.
     * @param usuario
     * @param session
     * @throws Exception
     */
    private void asignarEntidadesAdministradas(Usuario usuario,Entidad entidadActiva, HttpSession session) throws Exception{

        ArrayList<Entidad> entidadesAdministradas = new ArrayList<Entidad>();
        ArrayList<Entidad> entidadesPropietario = new ArrayList<Entidad>();
        ArrayList<Entidad> entidades = new ArrayList<Entidad>();

        // Obtenemos las entidades administradas
        entidadesAdministradas.addAll(entidadEjb.getEntidadesAdministrador(usuario.getId()));

        // Obtenemos las entidades propietarias
        entidadesPropietario.addAll(entidadEjb.getEntidadesPropietario(usuario.getId()));

        // Las guardamos en la sesion
        entidades.addAll(entidadesAdministradas);
        entidades.addAll(entidadesPropietario);
        session.setAttribute(RegwebConstantes.SESSION_ENTIDADES, entidades);

        // Definimos la Entidad Activa
        if(entidadActiva != null && entidades.contains(entidadActiva)){
            session.setAttribute(RegwebConstantes.SESSION_ENTIDAD,entidadActiva);
        }else if(entidades.size() > 0){
            entidadActiva = entidadEjb.findById(entidades.get(0).getId());
            session.setAttribute(RegwebConstantes.SESSION_ENTIDAD, entidadActiva);
        }

        log.info("Entidades administradas: " + entidadesAdministradas.size());
        log.info("Entidades propietario: " + entidadesPropietario.size());

        if(entidadActiva != null){
            //UsuarioEntidadActivo
            setUsuarioEntidadActivo(usuario,entidadActiva,session);

            // Registros migrados
            tieneMigrados(entidadActiva,session);
        }

        // Eliminamos las Oficinas
        eliminarVariablesSesionOficina(session);

    }

    /**
     * Obtiene las Entidades a las que el Usuario pertenece y las guarda en la sesion
     * @param session
     * @throws Exception
     */
    private UsuarioEntidad asignarEntidadesOperador(Usuario usuarioAutenticado,Entidad entidadActiva, HttpSession session) throws Exception{

        // Obtenemos las entidades a las que el Usuario está asociado
        List<Entidad> entidades = usuarioEntidadEjb.getEntidadesByUsuario(usuarioAutenticado.getId());

        // Si está asociado en alguna Entidad
        if(entidades.size() > 0){

            // Almacenamos en la sesión las entidades a las cuales pertenece el Usuario
            session.setAttribute(RegwebConstantes.SESSION_ENTIDADES, entidades);

            // Entidad Activa
            if(entidadActiva != null && entidades.contains(entidadActiva)){
                session.setAttribute(RegwebConstantes.SESSION_ENTIDAD,entidadActiva);
            }else if(entidades.size() > 0){
                entidadActiva = entidadEjb.findById(entidades.get(0).getId());
                session.setAttribute(RegwebConstantes.SESSION_ENTIDAD,entidadActiva);
            }

            log.info("Entidades asociadas operador: " + entidades.size());

            //UsuarioEntidadActivo
            return setUsuarioEntidadActivo(usuarioAutenticado, entidadActiva, session);
        }

        return null;

    }

    /**
     *  Asigna las Oficinas a las cuales el UsuarioEntidad puede Registrar y puede Administrar
     * @param usuarioEntidad
     * @param session
     * @throws Exception
     */
    private void asignarOficinasRegistro(UsuarioEntidad usuarioEntidad, HttpSession session) throws Exception{

        // Antes de nada, eliminamos las variables de sesión que continen información de las oficinas
        eliminarVariablesSesionOficina(session);

        Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);

        //Obtenemos los Libros donde el UsuarioEntidad tenga permisos de (Registro, Modificación o Administración)
        List<Libro> librosRegistro = permisoLibroUsuarioEjb.getLibrosRegistro(usuarioEntidad.getId());
        log.info("Libros registro usuario: " + Arrays.toString(librosRegistro.toArray()));

        // Si no hay libros en los que podamos registrar, buscamos en los que podamos consultar.
        if(librosRegistro.isEmpty()){
            librosRegistro = permisoLibroUsuarioEjb.getLibrosConsulta(usuarioEntidad.getId());
            log.info("Libros registro consulta: " + Arrays.toString(librosRegistro.toArray()));
        }

        //Obtenemos los Libros donde el UsuarioEntidad puede Administrar
        List<Libro> librosAdministrados = permisoLibroUsuarioEjb.getLibrosAdministrados(usuarioEntidad.getId());
        session.setAttribute(RegwebConstantes.SESSION_LIBROSADMINISTRADOS, librosAdministrados);
        log.info("Libros administrados usuario: " + Arrays.toString(librosAdministrados.toArray()));

        // Obtenemos las Oficinas que pueden registrar en los Libros
        LinkedHashSet<Oficina> oficinasRegistro = oficinaEjb.oficinasRegistro(librosRegistro);  // Utilizamos un Set porque no permite duplicados
        log.info("Oficinas registro usuario: " + Arrays.toString(oficinasRegistro.toArray()));

        // Si la Entidad está en SIR, obtenemos las Oficinas SIR a las que tiene acceso
        if(entidadActiva.getSir()){

            //Obtenemos los Libros donde el UsuarioEntidad tenga permiso SIR
            List<Libro> librosSIR = permisoLibroUsuarioEjb.getLibrosPermiso(usuarioEntidad.getId(), RegwebConstantes.PERMISO_SIR);
            log.info("Libros SIR usuario: " + Arrays.toString(librosSIR.toArray()));

            //Obtenemos las Oficinas que pueden operar con los Libros anteriores
            LinkedHashSet<Oficina> oficinasSIR = oficinaEjb.oficinasSIR(librosRegistro);  // Utilizamos un Set porque no permite duplicados
            log.info("Oficinas SIR usuario: " + Arrays.toString(oficinasSIR.toArray()));

            // Las añadimos al listado general de oficinas
            oficinasRegistro.addAll(oficinasSIR);
        }

        // Creamos la variable de sesión con las Oficinas a las que tiene acceso el usuario
        session.setAttribute(RegwebConstantes.SESSION_OFICINAS,oficinasRegistro);

        // Comprobamos si el usuario tiene última Oficina utilizada.
        if(usuarioEntidad.getUltimaOficina()!= null && oficinasRegistro.contains(new Oficina(usuarioEntidad.getUltimaOficina().getId()))){

            asignarOficinaActiva(oficinaEjb.findById(usuarioEntidad.getUltimaOficina().getId()) ,session);

        }else if(oficinasRegistro.size() > 0){

            asignarOficinaActiva(oficinaEjb.findById(oficinasRegistro.iterator().next().getId()) ,session);
        }

        //RegistrosMigrados
        tieneMigrados(entidadActiva,session);

    }

    /**
     *
     * @param oficinaNueva
     * @param session
     * @throws Exception
     */
    public void asignarOficinaActiva(Oficina oficinaNueva, HttpSession session) throws Exception{

        if(oficinaNueva != null){

            UsuarioEntidad usuarioEntidad = (UsuarioEntidad)session.getAttribute(RegwebConstantes.SESSION_USUARIO_ENTIDAD);

            // Guardamos en la sesión la nueva OficinaActiva
            session.setAttribute(RegwebConstantes.SESSION_OFICINA, oficinaNueva);

            // Guardamos en la sesión los Organismos OficiaActiva
            session.setAttribute(RegwebConstantes.SESSION_ORGANISMOS_OFICINA, organismoEjb.getByOficinaActiva(oficinaNueva));

            // Comprobamos si la Oficina está integrada en SIR
            oficinaNueva.setSirRecepcion(oficinaEjb.isSIRRecepcion(oficinaNueva.getId()));
            oficinaNueva.setSirEnvio(oficinaEjb.isSIRRecepcion(oficinaNueva.getId()));

            // Actualizamos la última Oficina del Usuario
            usuarioEntidadEjb.actualizarOficinaUsuario(usuarioEntidad.getId(), oficinaNueva.getId());
        }else{
            // Guardamos en la sesión la nueva OficinaActiva
            session.setAttribute(RegwebConstantes.SESSION_OFICINA, null);
        }

    }

    /**
     * Actualiza la variable de sesion de Registros Migrados, según la entidad Activa
     * @param entidadActiva
     * @param session
     * @throws Exception
     */
    private void tieneMigrados(Entidad entidadActiva, HttpSession session) throws Exception{
        session.setAttribute(RegwebConstantes.SESSION_MIGRADOS, registroMigradoEjb.tieneRegistrosMigrados(entidadActiva.getId()));
    }

    /**
     *
     * @param entidadNueva
     * @param request
     * @throws Exception
     */
    public void cambioEntidad(Entidad entidadNueva, HttpServletRequest request) throws Exception{
        log.info("Cambiando Entidad activa a: " + entidadNueva.getNombre());

        HttpSession session = request.getSession();
        Usuario usuarioAutenticado = (Usuario) session.getAttribute(RegwebConstantes.SESSION_USUARIO);
        Rol rolActivo = (Rol) session.getAttribute(RegwebConstantes.SESSION_ROL);

        session.removeAttribute(RegwebConstantes.SESSION_ENTIDAD);
        session.removeAttribute(RegwebConstantes.SESSION_USUARIO_ENTIDAD);
        session.setAttribute(RegwebConstantes.SESSION_ENTIDAD, entidadNueva);

        UsuarioEntidad usuarioEntidad = setUsuarioEntidadActivo(usuarioAutenticado, entidadNueva, session);

        if(rolActivo.getNombre().equals(RegwebConstantes.ROL_USUARI)){ // Solo si es Operador
            asignarOficinasRegistro(usuarioEntidad,session);

        } else {
            tieneMigrados(entidadNueva, session);
        }

    }

    /**
     * Realiza el cambio de Rol para un usuario autenticado
     * @param rolNuevo
     * @param request
     * @throws Exception
     */
    public Boolean cambioRol(Rol rolNuevo, HttpServletRequest request) throws Exception{
        log.info("Cambiando el rol a: " + rolNuevo.getNombre());
        HttpSession session = request.getSession();
        List<Rol> rolesAutentido = (List<Rol>) session.getAttribute(RegwebConstantes.SESSION_ROLES);
        Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);

        if(rolesAutentido.contains(rolNuevo)){
            session.setAttribute(RegwebConstantes.SESSION_ROL, rolNuevo);
            autorizarRol(rolNuevo, entidadActiva,request);

            return true;
        }else{
           return false;
        }

    }

    /**
     * Obtiene los Roles del usuario autenticado
     * @param request
     * @return
     * @throws Exception
     */
    private List<Rol> obtenerRolesUsuarioAutenticado(HttpServletRequest request) throws Exception{

        List<Rol> rolesUsuario = null;

        List<String> roles = new ArrayList<String>();

        if(request.isUserInRole(RegwebConstantes.ROL_SUPERADMIN)){roles.add(RegwebConstantes.ROL_SUPERADMIN);}
        if(request.isUserInRole(RegwebConstantes.ROL_ADMIN)){roles.add(RegwebConstantes.ROL_ADMIN);}
        if(request.isUserInRole(RegwebConstantes.ROL_USUARI)){roles.add(RegwebConstantes.ROL_USUARI);}

        if(roles.size() > 0){
            rolesUsuario = rolEjb.getByRol(roles);
        }

        return rolesUsuario;
    }

    /**
     * Obtiene los Roles del usuario mediante el plugin de Login.
     * @param identificador
     * @return
     * @throws Exception
     */
    private List<Rol> obtenerRolesUserPlugin(String identificador) throws Exception{

        IUserInformationPlugin loginPlugin = (IUserInformationPlugin) pluginEjb.getPlugin(null,RegwebConstantes.PLUGIN_USER_INFORMATION);
        RolesInfo rolesInfo = loginPlugin.getRolesByUsername(identificador);

        List<String> roles = new ArrayList<String>();
        List<Rol> rolesUsuario = null;

        if(rolesInfo.getRoles().length > 0){

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
     * Obtiene los Roles del usuario autenticado mediante el plugin de Login.
     * Actualiza los Roles del usuario en la bbdd de REGWEB3
     * @param usuario
     * @throws Exception
     */
    public void actualizarRoles(Usuario usuario) throws Exception{

        List<Rol> rolesUsuario = obtenerRolesUserPlugin(usuario.getIdentificador());

        if(rolesUsuario != null) {
            //log.info("Roles " + usuario.getIdentificador() + ": " + rolesUsuario.toString());

            // Actualizamos los Roles del usuario según sistema externo
            usuario.setRoles(rolesUsuario);
            usuarioEjb.merge(usuario);
        }

    }

    /**
     * Crea un nuevo usuario en REGWEB3, a partir del identificador de Seycon, obtiene sus
     * datos personales de la bbdd de Seycon.
     * @param identificador
     * @return
     * @throws Exception
     */
    public Usuario crearUsuario(String identificador) throws Exception{

        IUserInformationPlugin loginPlugin = (IUserInformationPlugin) pluginEjb.getPlugin(null,RegwebConstantes.PLUGIN_USER_INFORMATION);
        UserInfo regwebUserInfo = loginPlugin.getUserInfoByUserName(identificador);

        if(regwebUserInfo != null){ // Si el documento no existe en REGWEB3

            Usuario usuario = new Usuario();
            usuario.setNombre(regwebUserInfo.getName());

            //Idioma por defecto
            Long idioma  = RegwebConstantes.IDIOMA_ID_BY_CODIGO.get(Configuracio.getDefaultLanguage());
            usuario.setIdioma(idioma);

            if(regwebUserInfo.getSurname1() != null){
                usuario.setApellido1(regwebUserInfo.getSurname1());
            }else{
                usuario.setApellido1("");
            }
            
            if(regwebUserInfo.getSurname2() != null){
              usuario.setApellido2(regwebUserInfo.getSurname2());
            } else {
                usuario.setApellido2("");
            }

            usuario.setDocumento(regwebUserInfo.getAdministrationID());
            usuario.setIdentificador(identificador);

            // Email
            if(regwebUserInfo.getEmail() != null){
                usuario.setEmail(regwebUserInfo.getEmail());
            }else{
                usuario.setEmail("no hi ha email");
            }

            // Tipo Usuario
            if(identificador.startsWith("$")){
                usuario.setTipoUsuario(RegwebConstantes.TIPO_USUARIO_APLICACION);
            }else{
                usuario.setTipoUsuario(RegwebConstantes.TIPO_USUARIO_PERSONA);
            }

            // Roles
            List<Rol> roles = obtenerRolesUserPlugin(identificador);
            if(roles == null || roles.size() == 0 ){
                log.info("El usuario " + identificador + " no dispone de ningun Rol valido paa REGWEB3");
                return null;
            }else{
                usuario.setRoles(roles);
            }

            // Guardamos el nuevo Usuario
            usuario = usuarioEjb.persist(usuario);
            log.info("Usuario " + usuario.getNombreCompleto() + " creado correctamente");
            return usuario;
        }else{
            log.info("Usuario " + identificador + " no encontrado en el sistema de usuarios");
            return null;
        }

    }

    /**
     * Guardamos en la sesión el Usuario autenticado
     * @param usuario
     * @param request
     */
    public void setUsuarioAutenticado(Usuario usuario, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        session.setAttribute(RegwebConstantes.SESSION_USUARIO, usuario);
    }

    /**
     * Guardamos en la sesión el Usuario autenticado
     * @param usuario
     * @param session
     */
    private UsuarioEntidad setUsuarioEntidadActivo(Usuario usuario, Entidad entidad, HttpSession session) throws Exception{

        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByUsuarioEntidadActivo(usuario.getId(), entidad.getId());

        session.setAttribute(RegwebConstantes.SESSION_USUARIO_ENTIDAD, usuarioEntidad);

        log.info("Entidad activa usuario: " + entidad.getNombre() + " - " + usuario.getNombreCompleto());

        return usuarioEntidad;
    }

    /**
     * Comprueba si un usuario existe en el sistema de usuarios, mediante su identificador
     * @param identificador
     * @return
     * @throws Exception
     */
    public Boolean existeIdentificador(String identificador) throws  Exception{
        IUserInformationPlugin loginPlugin = (IUserInformationPlugin) pluginEjb.getPlugin(null,RegwebConstantes.PLUGIN_USER_INFORMATION);
        UserInfo regwebUserInfo = loginPlugin.getUserInfoByUserName(identificador);

        return regwebUserInfo != null;
    }

    /**
     * Limpia de la {@link javax.servlet.http.HttpSession} las variables utilizadas
     * @param session
     * @throws Exception
     */
    private void eliminarVariablesSesion(HttpSession session) throws Exception{

        session.removeAttribute(RegwebConstantes.SESSION_ENTIDADES);
        session.removeAttribute(RegwebConstantes.SESSION_ENTIDAD);
        session.removeAttribute(RegwebConstantes.SESSION_MIGRADOS);

        eliminarVariablesSesionOficina(session);
    }

    /**
     * Limpia de la {@link javax.servlet.http.HttpSession} las variables referentes a las Oficinas
     * @param session
     * @throws Exception
     */
    private void eliminarVariablesSesionOficina(HttpSession session) throws Exception{

        session.removeAttribute(RegwebConstantes.SESSION_OFICINAS);
        session.removeAttribute(RegwebConstantes.SESSION_OFICINA);
        session.removeAttribute(RegwebConstantes.SESSION_OFICINAS_ADMINISTRADAS);
        session.removeAttribute(RegwebConstantes.SESSION_LIBROSADMINISTRADOS);
        session.removeAttribute(RegwebConstantes.SESSION_ORGANISMOS_OFICINA);
    }

    /**
     * Limpia de la {@link javax.servlet.http.HttpSession} las variables referentes a las Oficinas
     * @param session
     * @throws Exception
     */
    private void eliminarVariablesSesionCredenciales(HttpSession session) throws Exception{

        session.removeAttribute(RegwebConstantes.SESSION_USUARIO);
        session.removeAttribute(RegwebConstantes.SESSION_USUARIO_ENTIDAD);
        session.removeAttribute(RegwebConstantes.SESSION_ROLES);
        session.removeAttribute(RegwebConstantes.SESSION_ROL);
    }

    /**
     * Guardamos en la sesión la Configuracion del SuperAdministrador
     * @param configuracion
     * @param request
     */
    private void asignarConfiguracionAdministrador(Configuracion configuracion, HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        session.setAttribute(RegwebConstantes.SESSION_CONFIGURACION, configuracion);
    }


}
