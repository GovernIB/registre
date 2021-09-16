package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatComunidadAutonoma;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface CatComunidadAutonomaLocal extends BaseEjb<CatComunidadAutonoma, Long> {

  CatComunidadAutonoma findByCodigo(Long codigo) throws Exception;
}

