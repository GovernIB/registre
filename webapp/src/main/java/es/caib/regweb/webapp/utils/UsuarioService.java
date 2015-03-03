package es.caib.regweb.webapp.utils;

import es.caib.regweb.model.*;
import es.caib.regweb.persistence.ejb.*;
import es.caib.regweb.utils.RegwebConstantes;
import es.caib.regweb.utils.login.RegwebLoginPluginManager;
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

        HttpSession session = request.getSession();

        // Obtenemos y asignamos los Roles del Usuario y el Usuario a la sesión
        Rol rolActivo = obtenerCredenciales(usuario, session);

        // Según el RolActivo, obtenemos sus autorizaciones
        autorizarRol(rolActivo, request);
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
     * Obtiene los Roles del usuario autenticado mediante el plugin de Login.
     * Almacenanos los Roles del usuario en la sesión
     * Almacenanos el usuario en la sesión
     * Crea la variable de sesión que contiene todos los Roles
     * Crea la variable de sesión que contiene el RolActivo
     * @param usuario
     * @param session
     * @throws Exception
     */
    public Rol obtenerCredenciales(Usuario usuario, HttpSession session) throws Exception{

        // Eliminamos las variables
        eliminarVariablesSesionCredenciales(session);

        IUserInformationPlugin loginPlugin = RegwebLoginPluginManager.getInstance();
        RolesInfo rolesInfo = loginPlugin.getRolesByUsername(usuario.getIdentificador());
        
        List<String> roles = new ArrayList<String>();
        Collections.addAll(roles, rolesInfo.getRoles());

        List<Rol> rolesUsuario = rolEjb.getByRol(roles);

        log.info("Roles en regweb de "+ usuario.getIdentificador()+": " + Arrays.toString(rolesUsuario.toArray()));

        // Actualizamos los Roles del usuario según sistema externo
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
     * Obtiene los Roles del usuario autenticado mediante el plugin de Login.
     * @param usuario
     * @return
     * @throws Exception
     */
    public List<Rol> obtenerRoles(Usuario usuario) throws Exception{

        IUserInformationPlugin loginPlugin = RegwebLoginPluginManager.getInstance();
        RolesInfo rolesInfo = loginPlugin.getRolesByUsername(usuario.getIdentificador());
        
        List<String> roles = new ArrayList<String>();
        Collections.addAll(roles, rolesInfo.getRoles());
        List<Rol> rolesUsuario = null;

        if(roles.size() > 0){
            rolesUsuario = rolEjb.getByRol(roles);
        }

        return rolesUsuario;
    }

    /**
     * Obtiene los Roles del usuario autenticado mediante el plugin de Login.
     * Almacenanos los Roles del usuario
     * @param usuario
     * @throws Exception
     */
    public void actualizarRoles(Usuario usuario) throws Exception{

        List<Rol> rolesUsuario = obtenerRoles(usuario);

        if(rolesUsuario != null) {
            //log.info("Roles " + usuario.getIdentificador() + ": " + rolesUsuario.toString());

            // Actualizamos los Roles del usuario según sistema externo
            usuario.setRoles(rolesUsuario);
            usuarioEjb.merge(usuario);
        }

    }

    /**
     * Según el RolActivo del Usuario autenticado, le autoriza.
     * @param rolActivo
     * @param request
     * @throws Exception
     */
    public void autorizarRol(Rol rolActivo, HttpServletRequest request) throws Exception{

        HttpSession session = request.getSession();

        // Limpiamos de la session las variables utilizadas
        eliminarVariablesSesion(session);

        Usuario usuarioAutenticado = (Usuario)session.getAttribute(RegwebConstantes.SESSION_USUARIO);

        log.info("RolActivo "+usuarioAutenticado.getIdentificador()+": " + rolActivo.getNombre());

        // Si el RolActivo del usuario autenticado es Administrador de Entidad
        if(rolActivo.getNombre().equals(RegwebConstantes.ROL_ADMIN)){

            asignarEntidades(usuarioAutenticado, session);
        }

        // Si RolActivo del usuario autenticado es Operador o LOPD
        if(rolActivo.getNombre().equals(RegwebConstantes.ROL_USUARI)){

            // Obtenemos todos sus UsuarioEntidad activos, de Entidades activas.
            List<UsuarioEntidad> usuariosEntidad = usuarioEntidadEjb.findByUsuario(usuarioAutenticado.getId());

            // Si el Usuario está presente en alguna Entidad
            if(usuariosEntidad.size() > 0){

                // Obtenemos y asignamos las Entidades a las que pertenece
                entidadesAsociadas(usuariosEntidad, session);

                // Obtenemos y asignamos las oficinas donde puede Registrar
                asignarOficinasRegistro(usuarioAutenticado, session);
            }

        }
    }

    /**
     * Asigna las Entidades de las que el Usuario AdministradorEntidad es propietario o administrador.
     * @param usuario
     * @param session
     * @throws Exception
     */
    public void asignarEntidades(Usuario usuario, HttpSession session) throws Exception{

        ArrayList<Entidad> entidades = new ArrayList<Entidad>();

        // Obtenemos las entidades administradas
        List<UsuarioEntidad> usuariosEntidad = usuarioEntidadEjb.findByUsuario(usuario.getId());
        List<Entidad> entidadesAdministrador = new ArrayList<Entidad>();
        for(UsuarioEntidad usuarioEntidad:usuariosEntidad){
            entidadesAdministrador.addAll(entidadEjb.getEntidadesAdministrador(usuarioEntidad.getId()));
        }

        // Obtenemos las entidades propietarias
        List<Entidad> entidadesPropietario = entidadEjb.getEntidadesPropietario(usuario.getId());
        entidades.addAll(entidadesAdministrador);
        entidades.addAll(entidadesPropietario);

        // Entidades de las cuales es Administrador
        session.setAttribute(RegwebConstantes.SESSION_ENTIDADES,entidades);

        //log.info("Entidades administradas: " + entidadesAdministrador.size());
        //log.info("Entidades propietario: " + entidadesPropietario.size());

        // Entidad Activa
        if(entidadesPropietario.size() > 0){
            session.setAttribute(RegwebConstantes.SESSION_ENTIDAD,entidadesPropietario.get(0));
        }else if(entidadesAdministrador.size() > 0){
            session.setAttribute(RegwebConstantes.SESSION_ENTIDAD,entidadesAdministrador.get(0));
        }

        session.setAttribute(RegwebConstantes.SESSION_MIGRADOS, false);

        // Eliminamos las Oficinas
        eliminarVariablesSesionOficina(session);
    }

    /**
     * Asigna las Entidades a las que el Usuario está asociado.
     * @param usuariosEntidad
     * @param session
     * @throws Exception
     */
    public void entidadesAsociadas(List<UsuarioEntidad> usuariosEntidad, HttpSession session) throws Exception{

        ArrayList<Entidad> entidades = new ArrayList<Entidad>();

        // Obtenemos las entidades a las que el Usuario está asociado
        for(UsuarioEntidad usuarioEntidad:usuariosEntidad){
            entidades.add(usuarioEntidad.getEntidad());
        }

        // Almacenamos en la sesión las entidades a las cuales pertenece el Usuario
        session.setAttribute(RegwebConstantes.SESSION_ENTIDADES,entidades);

        log.info("Entidades asociadas: " + entidades.size());
        Entidad entidadActiva = entidades.get(0);
        log.info("Entidad activa: " + entidadActiva.getNombre());

        // Entidad Activa
        session.setAttribute(RegwebConstantes.SESSION_ENTIDAD,entidadActiva);
        session.setAttribute(RegwebConstantes.SESSION_MIGRADOS, registroMigradoEjb.tieneRegistrosMigrados(entidadActiva.getId()));

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

        log.info("UsuarioEntidad activo: " + usuarioEntidad.getId());

        //Obtenemos los Libros, cuyo Organismo es Vigente y donde el UsuarioEntidad puede Registrar
        List<Libro> librosRegistro = permisoLibroUsuarioEjb.getLibrosRegistro(usuarioEntidad.getId());
        log.info("Libros registro usuario: " + Arrays.toString(librosRegistro.toArray()));

        Set<Oficina> oficinasRegistro = new HashSet<Oficina>();  // Utilizamos un Set porque no permite duplicados

        // Recorremos los Libros y a partir del Organismo al que pertenecen, obtenemos las Oficinas que pueden Registrar en el.
        for (Libro libro : librosRegistro) {
            Long idOrganismo = libro.getOrganismo().getId();
            oficinasRegistro.addAll(oficinaEjb.findByOrganismoResponsable(idOrganismo));
            oficinasRegistro.addAll(relacionOrganizativaOfiLocalEjb.getOficinasByOrganismo(idOrganismo));
        }

        log.info("Oficinas registro usuario: " + Arrays.toString(oficinasRegistro.toArray()));
        session.setAttribute(RegwebConstantes.SESSION_OFICINAS,oficinasRegistro);

        // Comprobamos la última Oficina utilizada por el usuario
        if(oficinasRegistro.contains(usuarioEntidad.getUltimaOficina())){
            session.setAttribute(RegwebConstantes.SESSION_OFICINA,oficinaEjb.findById(usuarioEntidad.getUltimaOficina().getId()));
            session.setAttribute(RegwebConstantes.SESSION_TIENEPREREGISTROS, preRegistroEjb.tienePreRegistros(usuarioEntidad.getUltimaOficina().getCodigo()));

        }else if(oficinasRegistro.size() > 0){
            session.setAttribute(RegwebConstantes.SESSION_OFICINA,oficinasRegistro.iterator().next());
            usuarioEntidadEjb.actualizarOficinaUsuario(usuarioEntidad.getId(),oficinasRegistro.iterator().next());
        }

        //Obtenemos los Libros donde que el UsuarioEntidad puede Administrar
        List<Libro> librosAdministrados = permisoLibroUsuarioEjb.getLibrosAdministrados(usuarioEntidad.getId());
        log.info("Libros administrados usuario: " + Arrays.toString(librosAdministrados.toArray()));
        session.setAttribute(RegwebConstantes.SESSION_LIBROSADMINISTRADOS,librosAdministrados);

        /*Set<Oficina> oficinasAdministradas = new HashSet<Oficina>();  // Utilizamos un Set porque no permite duplicados

        for (Libro libro : librosAdministrados) {
            oficinasAdministradas.addAll(oficinaEjb.findByOrganismoResponsable(libro.getOrganismo().getId()));
            oficinasAdministradas.addAll(relacionOrganizativaOfiLocalEjb.getOficinasByOrganismo(libro.getOrganismo().getId()));
        }

        log.info("Oficinas administrados usuario: " + Arrays.toString(oficinasAdministradas.toArray()));
        session.setAttribute(RegwebConstantes.SESSION_OFICINAS_ADMINISTRADAS,oficinasAdministradas);*/


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
                usuario.setApellido1(" ");
            }
            
            if(regwebUserInfo.getSurname2() != null){
              usuario.setApellido2(regwebUserInfo.getSurname2());
            } else {
                usuario.setApellido2(" ");
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
            List<Rol> roles = obtenerRoles(usuario);
            if(roles == null || roles.size() == 0 ){
                return null;
            }else{
                usuario.setRoles(roles);
            }

            // Guardamos el nuevo Usuario
            usuario = usuarioEjb.persist(usuario);

            return usuario;
        }else{
            return null;
        }

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
