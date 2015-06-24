package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RelacionSirOfi;
import es.caib.regweb3.model.RelacionSirOfiPK;

import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 10/10/13
 */
@Local
public interface RelacionSirOfiLocal extends BaseEjb<RelacionSirOfi, RelacionSirOfiPK> {
  
  public void deleteAll() throws Exception;
  public void deleteByOficina(Long idOficina) throws Exception;

    /**
     * Busca una RelacionSirOfi a partir de la Oficina y el Organismo que la componen
     * @param idOficina
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    public RelacionSirOfi getRelacionSir(Long idOficina, Long idOrganismo) throws Exception;
}
