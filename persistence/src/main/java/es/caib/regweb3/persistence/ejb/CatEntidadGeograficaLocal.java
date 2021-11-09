package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatEntidadGeografica;

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
public interface CatEntidadGeograficaLocal extends BaseEjb<CatEntidadGeografica, Long> {

  /**
       * Retorna los {@link es.caib.regweb3.model.CatEntidadGeografica} a partir de un código
       * @param codigo
       * @return
       * @throws Exception
       */
  CatEntidadGeografica findByCodigo(String codigo) throws Exception;
}

