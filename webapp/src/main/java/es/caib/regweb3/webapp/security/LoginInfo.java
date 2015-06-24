package es.caib.regweb3.webapp.security;

import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.ObjetoBasico;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.*;

/**
 * Informació disponible durant el cicle de vida de l'aplicació en la Sessio HTTP.
 * 
 * Exemple d'us:
 *          JAVA:   LoginInfo loginInfo = LoginInfo.getInstance();
 *          JSTL:   ${loginInfo.usuarioAutenticado.nombreCompleto}
 *               
 * Des de qualsevol lloc: Controller o JSP.
 * 
 * 
 * @author anadal
 * 
 */
public class LoginInfo {

  final Usuario usuarioAutenticado;

  final boolean roleUser;
  final boolean roleAdmin;
  final boolean roleSuperAdmin;

  final Map<Long, Entidad> entidadesPorID;
  
  final List<Entidad> entidades;

  final Map<Long, UsuarioEntidad> usuarioEntidadPorEntidadId;

  Entidad entidadActiva;
  
  final User springSecurityUser;
  
  final Collection<GrantedAuthority> springRoles;
  
  List<Rol> rolesAutenticado;

  Rol rolAutenticado;
  
  Set<ObjetoBasico> oficinas;
  
  Oficina oficinaActiva;

  final boolean neededConfigAdmin;

  
  /**
   * @param usuari
   * @param entidadActiva
   * @param roles
   */
  public LoginInfo(User springSecurityUser, Usuario usuariPersona, 
      boolean roleUser, boolean roleAdmin, boolean roleSuperAdmin,
      Long entitatIDActual,  Map<Long, Entidad> entitatsPerID, 
      Map<Long, UsuarioEntidad> usuariEntitatPerEntitatID,
      Collection<GrantedAuthority> roles, 
      List<Rol> rolesAutenticado, Rol rolAutenticado,
      Set<ObjetoBasico> oficinas, Oficina oficinaActiva,
      boolean neededConfigAdmin) {
    
    this.roleUser = roleUser;
    this.roleAdmin = roleAdmin;
    this.roleSuperAdmin = roleSuperAdmin;

    this.springSecurityUser = springSecurityUser;
    this.usuarioAutenticado = usuariPersona;
    
    this.entidadesPorID = entitatsPerID;
    this.entidades =  new  ArrayList<Entidad>(entitatsPerID.values());
    
    this.springRoles = roles;
    this.rolesAutenticado = rolesAutenticado; 
    this.rolAutenticado = rolAutenticado; 
    
    this.oficinas = oficinas;
    this.oficinaActiva = oficinaActiva;
    
    this.usuarioEntidadPorEntidadId = usuariEntitatPerEntitatID;
    this.neededConfigAdmin=neededConfigAdmin;
    setEntidadActiva(entitatIDActual);
  }


  
  public Entidad getEntidadActiva() {
    return entidadActiva;
  }

  /**
   * Aquest és l'únic mètode necessari per canviar d'entitat a part
   * d'actualitzar el token
   * 
   * @param novaEntitatID
   */
  public void setEntidadActiva(Long novaEntitatID) {
    Entidad novaEntitat = this.entidadesPorID.get(novaEntitatID);
    if (novaEntitat != null) {
      // TODO Aqui s'ha de fer tot lo necessari per Canviar d'Entitat
      this.entidadActiva = novaEntitat;
    }
  }


  public Long getUsuarioAutenticadoID() {
    Usuario ue = getUsuarioAutenticado();
    if (ue == null) {
      return null;
    } else {
      return ue.getId();
    }
  }
  
  
  public String getUserName() {
    Usuario ue = getUsuarioAutenticado();
    if (ue == null) {
      return null;
    } else {
      return ue.getIdentificador();
    }
  }
  
  
  
  public UsuarioEntidad getUsuarioEntidad() {
    return this.usuarioEntidadPorEntidadId.get(this.entidadActiva.getId());
  }
  
  public Usuario getUsuarioAutenticado() {
    return this.usuarioAutenticado;
  }
  

  public Map<Long, Entidad> getEntidadesPorID() {
    return entidadesPorID;
  }

  public Map<Long, UsuarioEntidad> getUsuarioEntidadPorEntidadId() {
    return usuarioEntidadPorEntidadId;
  }



  public Set<ObjetoBasico> getOficinas() {
    return oficinas;
  }


/*
  public void setOficinas(Set<ObjetoBasico> oficinas) {
    this.oficinas = oficinas;
  }
*/


  public User getSpringSecurityUser() {
    return springSecurityUser;
  }



  public List<Entidad> getEntidades() {
    return entidades;
  }


/*
  public void setEntidadActiva(Entidad entidadActiva) {
    this.entidadActiva = entidadActiva;
  }
*/


  public Collection<GrantedAuthority> getSpringRoles() {
    return springRoles;
  }


  public boolean isNeededConfigAdmin() {
    return neededConfigAdmin;
  }

  public UsernamePasswordAuthenticationToken generateToken() {
    UsernamePasswordAuthenticationToken authToken;
    Collection<GrantedAuthority> roles = getSpringRoles();
    authToken = new UsernamePasswordAuthenticationToken(this.springSecurityUser, "",
        roles);
    authToken.setDetails(this);
    return authToken;
  }

  

  public boolean isRoleUser() {
    return roleUser;
  }


  public boolean isRoleAdmin() {
    return roleAdmin;
  }

  public boolean isRoleSuperAdmin() {
    return roleSuperAdmin;
  }



  public Oficina getOficinaActiva() {
    return oficinaActiva;
  }
  
  
  public void setOficinaActiva(Oficina oficinaActiva) {
    
    // TODO Aqui s'ha de fer tot lo necessari per Canviar d'Oficina
    
    this.oficinaActiva = oficinaActiva;
  }
  

/*
  public void setRolesAutenticado(List<Rol> rolesAutenticado) {
    this.rolesAutenticado = rolesAutenticado;
  }
*/

  public void setRolAutenticado(Rol rolAutenticado) {
    
    // TODO Aqui s'ha de fer tot lo necessari per Canviar de Rol
    
    this.rolAutenticado = rolAutenticado;
  }


  public List<Rol> getRolesAutenticado() {
    return rolesAutenticado;
  }



  public Rol getRolAutenticado() {
    return rolAutenticado;
  }



  public static LoginInfo getInstance() throws LoginException {
    Object obj;
    try {
      obj = SecurityContextHolder.getContext().getAuthentication().getDetails();
    } catch (Exception e) {
      // TODO traduccio
      throw new LoginException("Error intentant obtenir informació de Login.", e);
    }

    if (obj == null) {
      // TODO traduccio
      throw new LoginException("La informació de Login és buida");
    }

    if (obj instanceof LoginInfo) {
      return (LoginInfo) obj;
    } else {
      // TODO traduccio
      throw new LoginException("La informació de Login no és del tipus esperat."
          + " Hauria de ser de tipus " + LoginInfo.class.getName() + " i és del tipus "
          + obj.getClass().getName());
    }
  }

}
