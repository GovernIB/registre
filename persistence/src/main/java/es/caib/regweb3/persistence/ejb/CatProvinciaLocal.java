package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatProvincia;
import es.caib.regweb3.model.utils.ObjetoBasico;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface CatProvinciaLocal extends BaseEjb<CatProvincia, Long> {

  public CatProvincia findByCodigo(Long codigo) throws Exception;

  public List<ObjetoBasico> getByComunidadObject(Long codigoComunidad) throws Exception;

  public List<CatProvincia> getByComunidad(Long codigoComunidad) throws Exception;

}

