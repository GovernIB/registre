package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatEstadoEntidad;

import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface CatEstadoEntidadLocal extends BaseEjb<CatEstadoEntidad, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/CatEstadoEntidadEJB";

    /**
     * Retorna los {@link es.caib.regweb3.model.CatEstadoEntidad} a partir de un código
     * @param codigo
     * @return
     * @throws Exception
     */
    CatEstadoEntidad findByCodigo(String codigo) throws Exception;

}
