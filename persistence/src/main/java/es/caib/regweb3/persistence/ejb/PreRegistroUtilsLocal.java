package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;


/**
 * 
 * @author anadal
 *
 */

@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface PreRegistroUtilsLocal  {

  public RegistroEntrada procesarPreRegistroEntrada(PreRegistro preRegistro, 
      UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro)
          throws Exception, I18NException, I18NValidationException;
  
  public RegistroSalida procesarPreRegistroSalida(PreRegistro preRegistro, 
      UsuarioEntidad usuario, Oficina oficinaActiva, Long idLibro)
          throws Exception, I18NException, I18NValidationException;

}
