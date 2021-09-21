package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatComunidadAutonoma;

import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface CatComunidadAutonomaLocal extends BaseEjb<CatComunidadAutonoma, Long> {

  String JNDI_NAME = "java:app/regweb3-persistence/CatComunidadAutonomaEJB";

  CatComunidadAutonoma findByCodigo(Long codigo) throws Exception;
}

