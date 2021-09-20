package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatNivelAdministracion;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface CatNivelAdministracionLocal extends BaseEjb<CatNivelAdministracion, Long> {

  String JNDI_NAME = "java:app/regweb3-persistence/CatNivelAdministracionEJB";

  CatNivelAdministracion findByCodigo(Long codigo) throws Exception;

}
