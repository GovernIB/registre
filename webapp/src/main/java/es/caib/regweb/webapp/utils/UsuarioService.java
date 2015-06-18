package es.caib.regweb.webapp.utils;

import es.caib.regweb.model.*;
import es.caib.regweb.model.utils.ObjetoBasico;
import es.caib.regweb.persistence.ejb.*;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.webapp.login.RegwebLoginPluginManager;
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

    @EJB(mappedName = "regweb/UsuarioEJB/local")
    public UsuarioLocal usuarioEjb;

    @EJB(mappedName = "regweb/RolEJB/local")
    public RolLocal rolEjb;

    @EJB(mappedName = "regweb/EntidadEJB/local")
    public EntidadLocal entidadEjb;

    @EJB(mappedName = "regweb/UsuarioEntidadEJB/local")
    public UsuarioEntidadLocal usuarioEntidadEjb;

    @EJB(mappedName = "regweb/PermisoLibroUsuarioEJB/local")
    public PermisoLibroUsuarioLocal permisoLibroUsuarioEjb;

    @EJB(mappedName = "regweb/OficinaEJB/local")
    public OficinaLocal oficinaEjb;

    @EJB(mappedName = "regweb/RelacionOrganizativaOfiEJB/local")
    public RelacionOrganizativaOfiLocal relacionOrganizativaOfiLocalEjb;

    @EJB(mappedName = "regweb/RegistroMigradoEJB/local")
    public RegistroMigradoLocal registroMigradoEjb;

    @EJB(mappedName = "regweb/PreRegistroEJB/local")
    public PreRegistroLocal preRegistroEjb;


    /**
     * Dado un usuario autenticado, realiza todas las configuraciones necesarias para su funcionamiento en REGWEB.
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
    public Rol obtenerCredenciales(Usuario usuario, HttpServletRequest request) throws Exception{

        HttpSession session = request.getSession();

        log.info("Usuario autenticado: " + usuario.getNombreCompleto());

        // Eliminamos las variables
        eliminarVariablesSesionCredenciales(session);

        List<Rol> rolesUsuario = obtenerRolesUsuarioAutenticado(request);

        log.info("Roles en regweb: " + Arrays.toString(rolesUsuario.toArray()));

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
    public void autorizarRol(Rol rolActivo,Entidad entidadActiva, HttpServletRequest request) throws Exception{


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

            asignarEntidadesOperador(usuarioAutenticado, entidadActiva, session);
        }

    }


    /**
     * Asigna las Entidades de las que el Usuario AdministradorEntidad es propietario o administrador.
     * @param usuario
     * @param session
     * @throws Exception
     */
    public void asignarEntidadesAdministradas(Usuario usuario,Entidad entidadActiva, HttpSession session) throws Exception{

        ArrayList<Entidad> entidades = new ArrayList<Entidad>();

        // Obtenemos las entidades administradas
        List<UsuarioEntidad> usuariosEntidad = usuarioEntidadEjb.findByUsuario(usuario.getId());
        List<Entidad> entidadesAdministrador = new ArrayList<Entidad>();
        for(UsuarioEntidad usuarioEntidad:usuariosEntidad){
            entidadesAdministrador.addAll(entidadEjb.getEntidadesAdministrador(usuarioEntidad.getId()));
        }

        // Obtenemos las entidades propietarias
        List<Entidad> entidadesPropietario = entidadEjb.getEntidadesPropietario(usuario.getId());

        // Las guardamos en la sesion
        entidades.addAll(entidadesAdministrador);
        entidades.addAll(entidadesPropietario);
        session.setAttribute(RegwebConstantes.SESSION_ENTIDADES,entidades);

        // Entidad Activa
        if(entidades.contains(entidadActiva)){
            session.setAttribute(RegwebConstantes.SESSION_ENTIDAD,entidadActiva);
        }else if(entidadesPropietario.size() > 0){
            session.setAttribute(RegwebConstantes.SESSION_ENTIDAD,entidadesPropietario.get(0));
        }else if(entidadesAdministrador.size() > 0){
            session.setAttribute(RegwebConstantes.SESSION_ENTIDAD,entidadesAdministrador.get(0));
        }

        // Registros migrados
        Entidad entidad = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);
        tieneMigrados(entidad,session);

        // Eliminamos las Oficinas
        eliminarVariablesSesionOficina(session);

        log.info("Entidades asociadas: " + entidades.size());
        log.info("Entidades activa: " + entidad.getNombre());
    }

    /**
     * Obtiene las Entidades a las que el Usuario pertenece y las guarda en la sesion
     * @param session
     * @throws Exception
     */
    public void asignarEntidadesOperador(Usuario usuarioAutenticado,Entidad entidadActiva, HttpSession session) throws Exception{

        ArrayList<Entidad> entidades = new ArrayList<Entidad>();

        // Obtenemos todos sus UsuarioEntidad activos, de Entidades activas.
        List<UsuarioEntidad> usuariosEntidad = usuarioEntidadEjb.findByUsuario(usuarioAutenticado.getId());

        // Si el Usuario está presente en alguna Entidad
        if(usuariosEntidad.size() > 0){

            // Obtenemos las entidades a las que el Usuario está asociado
            for(UsuarioEntidad usuarioEntidad:usuariosEntidad){
                entidades.add(usuarioEntidad.getEntidad());
            }

            // Almacenamos en la sesión las entidades a las cuales pertenece el Usuario
            session.setAttribute(RegwebConstantes.SESSION_ENTIDADES, entidades);

            // Entidad Activa
            if(entidades.contains(entidadActiva)){
                session.setAttribute(RegwebConstantes.SESSION_ENTIDAD,entidadActiva);
            }else if(entidades.size() > 0){
                session.setAttribute(RegwebConstantes.SESSION_ENTIDAD,entidades.get(0));
            }


            Entidad entidad = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);

            log.info("Entidades asociadas: " + entidades.size());
            log.info("Entidades activa: " + entidad.getNombre());

            //Asignamos las oficinas donde tiene acceso
            asignarOficinasRegistro(usuarioAutenticado, session);
        }


    }

    /**
     *  Asigna las Oficinas a las cuales el Usuario autenticado puede Registrar y puede Administrar
     * @param usuarioAutenticado
     * @param session
     * @throws Exception
     */
    public void asignarOficinasRegistro(Usuario usuarioAutenticado, HttpSession session) throws Exception{

        // Antes de nada, eliminamos las variables de sesión que continen información de las oficinas
        eliminarVariablesSesionOficina(session);

        Entidad entidadActiva = (Entidad) session.getAttribute(RegwebConstantes.SESSION_ENTIDAD);
        UsuarioEntidad usuarioEntidad = usuarioEntidadEjb.findByUsuarioEntidad(usuarioAutenticado.getId(), entidadActiva.getId());

        //Obtenemos los Libros, cuyo Organismo es Vigente y donde el UsuarioEntidad tenga algún permiso
        List<Libro> librosRegistro = permisoLibroUsuarioEjb.getLibrosRegistro(usuarioEntidad.getId());
        log.info("Libros usuario: " + Arrays.toString(librosRegistro.toArray()));

        // Obtenemos las Oficinas que pueden registrar en los Libros
        Set<ObjetoBasico> oficinasRegistro = oficinaEjb.oficinasRegistro(librosRegistro);  // Utilizamos un Set porque no permite duplicados

        log.info("Oficinas registro usuario: " + Arrays.toString(oficinasRegistro.toArray()));
        session.setAttribute(RegwebConstantes.SESSION_OFICINAS,oficinasRegistro);

        // Comprobamos la última Oficina utilizada por el usuario
        if(usuarioEntidad.getUltimaOficina()!= null && oficinasRegistro.contains(new ObjetoBasico(usuarioEntidad.getUltimaOficina().getId()))){
            session.setAttribute(RegwebConstantes.SESSION_OFICINA,oficinaEjb.findById(usuarioEntidad.getUltimaOficina().getId()));

        }else if(oficinasRegistro.size() > 0){
            session.setAttribute(RegwebConstantes.SESSION_OFICINA,oficinaEjb.findById(oficinasRegistro.iterator().next().getId()));
            usuarioEntidadEjb.actualizarOficinaUsuario(usuarioEntidad.getId(), oficinasRegistro.iterator().next().getId());
        }

        //Obtenemos los Libros donde que el UsuarioEntidad puede Administrar
        List<Libro> librosAdministrados = permisoLibroUsuarioEjb.getLibrosAdministrados(usuarioEntidad.getId());
        log.info("Libros administrados usuario: " + Arrays.toString(librosAdministrados.toArray()));
        session.setAttribute(RegwebConstantes.SESSION_LIBROSADMINISTRADOS, librosAdministrados);

        //RegistrosMigrados
        tieneMigrados(entidadActiva,session);

        //PreRegistros
        Oficina oficinaActiva = (Oficina) session.getAttribute(RegwebConstantes.SESSION_OFICINA);
        if(oficinaActiva != null) {
            tienePreRegistros(oficinaActiva,session);
        }



    }


    /**
     * Actualiza la variable de sesion de Registros Migrados, según la entidad Activa
     * @param entidadActiva
     * @param session
     * @throws Exception
     */
    public void tieneMigrados(Entidad entidadActiva, HttpSession session) throws Exception{
        session.setAttribute(RegwebConstantes.SESSION_MIGRADOS, registroMigradoEjb.tieneRegistrosMigrados(entidadActiva.getId()));
    }

    /**
     * Actualiza la variable de sesion de PreRegistros, según la oficina Activa
     * @param oficinaActiva
     * @param session
     * @throws Exception
     */
    public void tienePreRegistros(Oficina oficinaActiva, HttpSession session) throws Exception{
        session.setAttribute(RegwebConstantes.SESSION_TIENEPREREGISTROS, preRegistroEjb.tienePreRegistros(oficinaActiva.getCodigo()));
    }

    /**
     * Realiza el cambio de Rol para un usuario autenticado
     * @param rolNuevo
     * @param request
     * @throws Exception
     */
    public Boolean cambioRol(Rol rolNuevo, HttpServletRequest request) throws Exception{
        log.info("Cambiando el rol a:" + rolNuevo.getNombre());
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
    public List<Rol> obtenerRolesUsuarioAutenticado(HttpServletRequest request) throws Exception{

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
    public List<Rol> obtenerRolesUserPlugin(String identificador) throws Exception{

        IUserInformationPlugin loginPlugin = RegwebLoginPluginManager.getInstance();
        RolesInfo rolesInfo = loginPlugin.getRolesByUsername(identificador);

        List<String> roles = new ArrayList<String>();
        List<Rol> rolesUsuario = null;

        if(rolesInfo.getRoles().length > 0){

            Collections.addAll(roles, rolesInfo.getRoles());
            if(roles.size() > 0){
                rolesUsuario = rolEjb.getByRol(roles);
            }
        }else{
            log.info("El usuario " + identificador + " no dispone de ningun Rol de REGWEB en el sistema de autentificacion");
        }

        return rolesUsuario;
    }

    /**
     * Obtiene los Roles del usuario autenticado mediante el plugin de Login.
     * Actualiza los Roles del usuario en la bbdd de REGWEB
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
     * Crea un nuevo usuario en REGWEB, a partir del identificador de Seycon, obtiene sus
     * datos personales de la bbdd de Seycon.
     * @param identificador
     * @return
     * @throws Exception
     */
    public Usuario crearUsuario(String identificador) throws Exception{

        IUserInformationPlugin loginPlugin = RegwebLoginPluginManager.getInstance();
        UserInfo regwebUserInfo = loginPlugin.getUserInfoByUserName(identificador);

        if(regwebUserInfo != null){ // Si el documento no existe en REGWEB

            Usuario usuario = new Usuario();
            usuario.setNombre(regwebUserInfo.getName());

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
                log.info("El usuario " + identificador + " no dispone de ningun Rol valido paa REGWEB");
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
     * Comprueba si un usuario existe en el sistema de usuarios, mediante su identificador
     * @param identificador
     * @return
     * @throws Exception
     */
    public Boolean existeIdentificador(String identificador) throws  Exception{
        IUserInformationPlugin loginPlugin = RegwebLoginPluginManager.getInstance();
        UserInfo regwebUserInfo = loginPlugin.getUserInfoByUserName(identificador);

        return regwebUserInfo != null;
    }

    /**
     * Limpia de la {@link javax.servlet.http.HttpSession} las variables utilizadas
     * @param session
     * @throws Exception
     */
    public void eliminarVariablesSesion(HttpSession session) throws Exception{

        session.removeAttribute(RegwebConstantes.SESSION_ENTIDADES);
        session.removeAttribute(RegwebConstantes.SESSION_ENTIDAD);
        session.removeAttribute(RegwebConstantes.SESSION_OFICINAS);
        session.removeAttribute(RegwebConstantes.SESSION_OFICINA);
        session.removeAttribute(RegwebConstantes.SESSION_OFICINAS_ADMINISTRADAS);
        session.removeAttribute(RegwebConstantes.SESSION_MIGRADOS);
        session.removeAttribute(RegwebConstantes.SESSION_TIENEPREREGISTROS);

    }

    /**
     * Limpia de la {@link javax.servlet.http.HttpSession} las variables referentes a las Oficinas
     * @param session
     * @throws Exception
     */
    public void eliminarVariablesSesionOficina(HttpSession session) throws Exception{

        session.removeAttribute(RegwebConstantes.SESSION_OFICINAS);
        session.removeAttribute(RegwebConstantes.SESSION_OFICINA);
        session.removeAttribute(RegwebConstantes.SESSION_OFICINAS_ADMINISTRADAS);
        session.removeAttribute(RegwebConstantes.SESSION_LIBROSADMINISTRADOS);

    }

    /**
     * Limpia de la {@link javax.servlet.http.HttpSession} las variables referentes a las Oficinas
     * @param session
     * @throws Exception
     */
    public void eliminarVariablesSesionCredenciales(HttpSession session) throws Exception{

        session.removeAttribute(RegwebConstantes.SESSION_USUARIO);
        session.removeAttribute(RegwebConstantes.SESSION_ROLES);
        session.removeAttribute(RegwebConstantes.SESSION_ROL);

    }


}
