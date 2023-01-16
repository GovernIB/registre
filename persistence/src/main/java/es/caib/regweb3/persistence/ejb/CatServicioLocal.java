package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatServicio;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface CatServicioLocal extends BaseEjb<CatServicio, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/CatServicioEJB";

    /**
     * @param codigo
     * @return
     * @throws I18NException
     */
    CatServicio findByCodigo(Long codigo) throws I18NException;

}

