package es.caib.regweb.persistence.ejb;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

import org.fundaciobit.genapp.common.i18n.I18NException;

import es.caib.regweb.model.Oficina;
import es.caib.regweb.model.PreRegistro;
import es.caib.regweb.model.RegistroEntrada;
import es.caib.regweb.model.RegistroSalida;
import es.caib.regweb.model.UsuarioEntidad;


/**
 * 
 * @author anadal
 *
 */

@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface PreRegistroUtilsLocal  {

  public RegistroEntrada procesarPreRegistroEntrada(PreRegistro preRegistro, 
      UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro) throws Exception, I18NException ;
  
  public RegistroSalida procesarPreRegistroSalida(PreRegistro preRegistro, 
      UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro) throws Exception, I18NException ;

}
