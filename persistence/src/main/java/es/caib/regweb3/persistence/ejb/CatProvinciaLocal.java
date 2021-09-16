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
public interface CatProvinciaLocal extends BaseEjb<CatProvincia, Long> {

  CatProvincia findByCodigo(Long codigo) throws Exception;

  List<ObjetoBasico> getByComunidadObject(Long codigoComunidad) throws Exception;

  List<CatProvincia> getByComunidad(Long codigoComunidad) throws Exception;

}

