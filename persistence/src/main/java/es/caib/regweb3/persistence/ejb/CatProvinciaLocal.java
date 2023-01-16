package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatProvincia;
import es.caib.regweb3.model.utils.ObjetoBasico;
import org.fundaciobit.genapp.common.i18n.I18NException;

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

    String JNDI_NAME = "java:app/regweb3-persistence/CatProvinciaEJB";

    /**
     * @param codigo
     * @return
     * @throws I18NException
     */
    CatProvincia findByCodigo(Long codigo) throws I18NException;

    /**
     * @param codigoComunidad
     * @return
     * @throws I18NException
     */
    List<ObjetoBasico> getByComunidadObject(Long codigoComunidad) throws I18NException;

    /**
     * @param codigoComunidad
     * @return
     * @throws I18NException
     */
    List<CatProvincia> getByComunidad(Long codigoComunidad) throws I18NException;

}

