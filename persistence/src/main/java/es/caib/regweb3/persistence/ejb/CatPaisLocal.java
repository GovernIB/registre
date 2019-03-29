package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatPais;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA"})
public interface CatPaisLocal extends BaseEjb<CatPais, Long> {

  CatPais findByCodigo(Long codigo) throws Exception;

}

