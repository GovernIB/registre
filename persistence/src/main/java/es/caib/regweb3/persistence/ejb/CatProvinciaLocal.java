package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatProvincia;
import es.caib.regweb3.model.utils.ObjetoBasico;

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
     * @throws Exception
     */
    CatProvincia findByCodigo(Long codigo) throws Exception;

    /**
     * @param codigoComunidad
     * @return
     * @throws Exception
     */
    List<ObjetoBasico> getByComunidadObject(Long codigoComunidad) throws Exception;

    /**
     * @param codigoComunidad
     * @return
     * @throws Exception
     */
    List<CatProvincia> getByComunidad(Long codigoComunidad) throws Exception;

}

