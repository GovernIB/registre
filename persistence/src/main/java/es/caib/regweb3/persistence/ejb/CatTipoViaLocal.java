package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatTipoVia;

import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface CatTipoViaLocal extends BaseEjb<CatTipoVia, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/CatTipoViaEJB";

    /**
     * @param codigo
     * @return
     * @throws Exception
     */
    CatTipoVia findByCodigo(Long codigo) throws Exception;

    /**
     * @param descripcion
     * @return
     * @throws Exception
     */
    CatTipoVia findByDescripcion(String descripcion) throws Exception;

}

