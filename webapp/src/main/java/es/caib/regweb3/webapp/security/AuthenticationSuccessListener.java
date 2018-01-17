package es.caib.regweb3.webapp.security;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.ObjetoBasico;
import es.caib.regweb3.persistence.ejb.UsuarioEntidadLocal;
import es.caib.regweb3.persistence.ejb.UsuarioLocal;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.webapp.login.RegwebLoginPluginManager;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.userinformation.IUserInformationPlugin;
import org.fundaciobit.plugins.userinformation.UserInfo;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.naming.InitialContext;
import java.util.*;


/**
 *
 * @author anadal
 *
 */
//TODO AQUESTA CLASE HA DE SUBSTITUIR L'INTERCEPTOR  es.caib.regweb3.webapp.interceptor.InicioInterceptor
@Component
public class AuthenticationSuccessListener implements
    ApplicationListener<InteractiveAuthenticationSuccessEvent> {

  protected final Logger log = Logger.getLogger(getClass());
  
  
  protected UsuarioLocal usuarioEjb = null;
  
  
  protected UsuarioEntidadLocal usuarioEntidadEjb = null;
  

  @Override
  public synchronized void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {

    SecurityContext sc = SecurityContextHolder.getContext();
    Authentication au = sc.getAuthentication();

    if (au == null) {
      // TODO traduccio
      throw new LoginException("NO PUC ACCEDIR A LA INFORMACIO de AUTENTICACIO");
    }

    User user = (User) au.getPrincipal();
    
    String name = user.getUsername();
    log.info(" =================================================================");
    log.info(" ============ Login Usuari: " + name);

    // Cercam si té el ROLE_USER o ROLE_ADMIN
    Collection<GrantedAuthority> seyconAuthorities = user.getAuthorities();
    boolean containsRoleUser = false;
    boolean containsRoleAdmin = false;
    boolean containsRoleSuperAdmin = false;
    for (GrantedAuthority grantedAuthority : seyconAuthorities) {
      String rol = grantedAuthority.getAuthority();
      log.info("Rol SEYCON : " + rol);
      if (RegwebConstantes.ROL_USUARI.equals(rol)) {
        containsRoleUser = true;
      }
      if (RegwebConstantes.ROL_ADMIN.equals(rol)) {
        containsRoleAdmin = true;
      }
      if (RegwebConstantes.ROL_SUPERADMIN.equals(rol)) {
        containsRoleSuperAdmin = true;
      }
    }

    // Info Usuari

    if (usuarioEjb == null) {
      try {
        usuarioEjb = (UsuarioLocal) new InitialContext()
            .lookup("regweb3/UsuarioEJB/local");
      } catch (Exception e) {
        // TODO traduccio
        throw new LoginException("No puc accedir al gestor de BBDD d´obtenció de" +
            		" informació de usuari per " + name + ": " + e.getMessage(), e);
      }
    }

    Usuario usuariPersona;
    boolean necesitaConfigurarAdmin = false;
    try {
      usuariPersona = usuarioEjb.findByIdentificador(name);
    } catch(Throwable e) {
      throw new LoginException("Error cercant l´usuari " + name + " a la BBDD: " + e.getMessage(), e);
    }
      
    if (usuariPersona == null) {
      // Revisar si és un usuari que entra per primera vegada
      Throwable unknownError = null;  
      try {
         usuariPersona = crearUsuario(name, usuarioEjb);
      } catch(LoginException le) {
          throw le;              
      } catch(Throwable e) {
        unknownError  =e;
      }

      if(usuariPersona == null) {
        // TODO Traduir
        String msg = "No se ha podido dar de alta el usuario " + name + " en de Registro";
        if (unknownError == null) {
          throw new LoginException(msg);
        } else {
          throw new LoginException(msg + ":" + unknownError.getMessage(), unknownError);
        }
      }

    }

 
    //log.info(" Tipo Usuario =  " + usuariPersona.getTipoUsuario() + " ("  + RegwebConstantes.TIPO_USUARIO_APLICACION + ")");
    if (usuariPersona.getTipoUsuario() == (long)RegwebConstantes.TIPO_USUARIO_APLICACION) {
      throw new LoginException("L'usuari " + name
          + " és un usuari de tipus aplicació.Per favor contacti amb l´Administrador.");
    }


    // Obtener todos los usuario-entidad de todas las entidades
    List<UsuarioEntidad> usuariEntitats;
    if (usuarioEntidadEjb == null) {
      try {
        usuarioEntidadEjb = (UsuarioEntidadLocal) new InitialContext()
            .lookup("regweb3/UsuarioEntidadEJB/local");
      } catch (Exception e) {
        // TODO traduccio
        throw new LoginException("No puc accedir al gestor de BBDD d´obtenció de" +
                " informació d´usuari-entitat per " + name + ": " + e.getMessage(), e);
      }
    }
    
    try {
      usuariEntitats = usuarioEntidadEjb.findByUsuario(usuariPersona.getId());
    } catch (Exception e) {
      // TODO hauria de llançar un LoginException
      log.error("Error cercant usuario-entidad: " + usuariPersona.getId() + ": "
      + e.getMessage(), e);
      usuariEntitats = null;
    }
    
 

    log.debug("POST getUsuariEntitats() = " + usuariEntitats);

    if (usuariEntitats != null && log.isDebugEnabled()) {
      log.debug("POST getUsuariEntitats()[SIZE] = " + usuariEntitats.size());
    }

    if (usuariEntitats == null) {
        usuariEntitats = new ArrayList<UsuarioEntidad>();
    }
   
    
    // Seleccionam l'entitat per defecte i verificam que les entitats disponibles siguin correctes
    Map<Long, Entidad> entitats = new HashMap<Long, Entidad>();
    Map<String, Set<GrantedAuthority>> rolesPerEntitat = new HashMap<String, Set<GrantedAuthority>>();
    rolesPerEntitat.put((String)null, new HashSet<GrantedAuthority>(seyconAuthorities));
    Map<Long, UsuarioEntidad> usuariEntitatPerEntitatID = new HashMap<Long, UsuarioEntidad>();
    Entidad entitatPredeterminada = null;
    for (UsuarioEntidad usuariEntitat : usuariEntitats) {
      
      Entidad entitat = usuariEntitat.getEntidad();
      Long entitatID = entitat.getId();
      log.info("--------------- Entitat " + entitat.getNombre());
      // Check deshabilitada
      if (entitat.getActivo() == false) {        
        log.info("L'entitat " + entitat.getNombre() +  " esta deshabilitada.");
        continue;
      }
      
      if (!usuariEntitat.getActivo() == false) {
        log.info("L'entitat " + entitat.getNombre() +  " esta deshabilitatda per a l'usuari "
            + usuariPersona.getIdentificador());
        continue;
      }

      // Per si no n'hi ha cap per defecte
      if (entitatPredeterminada == null) {
        entitatPredeterminada = entitat;
      }
            
      
      // TODO EDUARDO Entitat per defecte
      /*
      if (usuariEntitat.isPredeterminat()) {
        entitatPredeterminada = entitat;
      }
      */
      
      
      // Entitats
      entitats.put(entitatID, entitat);
      // Usuari Entitat          
      usuariEntitatPerEntitatID.put(entitatID, usuariEntitat);
    }

    
    // TODO Eduardo que passa si un usuari-operador o usuari-admin(adm. entitats) no té 
    // entitats associades o a les que està associades estan desactivades
    /*
    if (entitats.size() == 0 && !containsRoleAdmin) {
      
      if (usuariEntitats.size() == 0) {
        // Miram si només és Administrador.
        // Només permetem llista d'entitats buida si és ADMIN
        // TODO traduccio
        throw new LoginException("L'usuari " + name + " no té cap entitat associada");
         
      } else {
        // Les entitats a les que pertany estan desactivades
        // TODO traduccio
        throw new LoginException("L'usuari " + name + " no té cap entitat vàlida associada");
      }
    }
    */

    Long entitatIDActual = null;
    if (entitatPredeterminada != null) {
      entitatIDActual = entitatPredeterminada.getId();
      log.info(">>>>>> Entitat predeterminada " + entitatIDActual);
    }
    
    
    // TODO EDUARDO falta cercar ROL i ROLES
    List<Rol> rolesAutenticado = null;
    Rol rolAutenticado = null;


    // TODO EDUARDO falta cercar oficinas i l'oficina activa
    Oficina oficinaActiva = null;
    Set<ObjetoBasico> oficinas = null;
    
    

    LoginInfo loginInfo;
    // create a new authentication token
    loginInfo = new LoginInfo(user, usuariPersona, 
        containsRoleUser, containsRoleAdmin, containsRoleSuperAdmin, 
        entitatIDActual, entitats, usuariEntitatPerEntitatID,
        seyconAuthorities, rolesAutenticado, rolAutenticado,
        oficinas, oficinaActiva, necesitaConfigurarAdmin);

    // and set the authentication of the current Session context
    SecurityContextHolder.getContext().setAuthentication(loginInfo.generateToken());
    
    log.info(">>>>>> Final del Process d'autenticació.");
    log.info(" =================================================================");

  }
  
  
  
  // TODO COPIA DEL METODE crearUsuario de la classe UsuarioService
  /**
   * 
   * 
   * 
   * Crea un nuevo usuario en REGWEB3, a partir del identificador de Seycon, obtiene sus
   * datos personales de la bbdd de Seycon.
   * @param identificador
   * @return
   * @throws Exception
   */
  public static Usuario crearUsuario(String identificador, UsuarioLocal usuariPersonaEjb) throws Exception {

      IUserInformationPlugin loginPlugin = RegwebLoginPluginManager.getInstance();
      UserInfo regwebUserInfo = loginPlugin.getUserInfoByUserName(identificador);

      if(regwebUserInfo == null){ // Si el documento no existe en REGWEB3
        // TODO traduccio
        throw new LoginException("No puc accedir al gestor d´obtenció de" +
                " informació d´usuari per " + identificador);
      }

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



      // Guardamos el nuevo Usuario
      usuario = usuariPersonaEjb.persist(usuario);

      return usuario;
      

  }
  
}
