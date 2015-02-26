package es.caib.regweb.ws.utils;

import javax.naming.InitialContext;

import org.apache.log4j.Logger;

import es.caib.regweb.persistence.ejb.UsuarioEntidadLocal;
import es.caib.regweb.persistence.ejb.UsuarioLocal;

/**
 * 
 * @author anadal
 * 
 */
public final class EjbManager {

  protected static final Logger log = Logger.getLogger(EjbManager.class);


  protected static UsuarioEntidadLocal usuariEntitatEjb;
  
  protected static UsuarioLocal usuariEjb;
  


  public static UsuarioEntidadLocal getUsuarioEntidadEJB() throws Exception {

    if (usuariEntitatEjb == null) {
      
        usuariEntitatEjb = (UsuarioEntidadLocal) new InitialContext()
            .lookup("regweb/UsuarioEntidadEJB/local");
      
    }
    return usuariEntitatEjb;
  }
  
  
  public static UsuarioLocal getUsuarioEJB() throws Exception {

    if (usuariEjb == null) {
      
        usuariEjb = (UsuarioLocal) new InitialContext()
            .lookup("regweb/UsuarioEJB/local");
      
    }
    return usuariEjb;
  }
  
  
  
}
