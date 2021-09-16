package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatTipoVia;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface CatTipoViaLocal extends BaseEjb<CatTipoVia, Long> {

  CatTipoVia findByCodigo(Long codigo) throws Exception;

  CatTipoVia findByDescripcion(String descripcion) throws Exception;

}

