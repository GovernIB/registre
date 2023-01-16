package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatTipoVia;
import org.fundaciobit.genapp.common.i18n.I18NException;

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
     * @throws I18NException
     */
    CatTipoVia findByCodigo(Long codigo) throws I18NException;

    /**
     * @param descripcion
     * @return
     * @throws I18NException
     */
    CatTipoVia findByDescripcion(String descripcion) throws I18NException;

}

