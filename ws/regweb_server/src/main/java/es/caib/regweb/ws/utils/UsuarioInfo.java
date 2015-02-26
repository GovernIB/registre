package es.caib.regweb.ws.utils;

import java.util.ArrayList;
import java.util.List;

import org.fundaciobit.genapp.common.i18n.I18NException;

import es.caib.regweb.model.Entidad;
import es.caib.regweb.model.Usuario;
import es.caib.regweb.model.UsuarioEntidad;


/**
 * 
 * @author anadal
 * 
 */
public class UsuarioInfo {

  final Usuario usuario;

  final List<Entidad> entidades = new ArrayList<Entidad>();

  final List<Long> entidadesIDs = new ArrayList<Long>();
  
  final List<String> entidadesCodigoDir3 = new ArrayList<String>();

  /**
   * @param usuario
   * @param entidades
   */
  public UsuarioInfo(Usuario usuario, List<UsuarioEntidad> entidades) {
    super();
    this.usuario = usuario;

    for (UsuarioEntidad entidad : entidades) {
      this.entidades.add(entidad.getEntidad());
      this.entidadesIDs.add(entidad.getEntidad().getId());
      this.entidadesCodigoDir3.add(entidad.getEntidad().getCodigoDir3());
    }

  }

  public Usuario getUsuario() {
    return usuario;
  }

  public List<Entidad> getEntidades() {
    return entidades;
  }

  public List<Long> getEntidadesIDs() {
    return entidadesIDs;
  }
  
  
  public List<String> getEntidadesCodigoDir3() {
    return entidadesCodigoDir3;
  }

  public void checkPerteneceAEntidad(String entidadCodigoDir3) throws I18NException {
    
    if (!entidadesCodigoDir3.contains(entidadCodigoDir3)) {
      
      throw new I18NException("error.ws.usrappnoestadentroentidad",
          usuario.getIdentificador(), entidadCodigoDir3);
      
      /*
      String msg = "L´aplicació " + usuario.getIdentificador() 
        + " intenta obtenir informació de l´entitat amb ID=" +
        entidadID + ", pero l´aplicació no pertany a aquesta entitat.";
      WsUtils.throwException(msg);
      */
    }
  }

}
