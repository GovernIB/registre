package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatEstadoEntidad;
import org.fundaciobit.genapp.common.i18n.I18NException;

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
     * @throws I18NException
     */
    CatEstadoEntidad findByCodigo(String codigo) throws I18NException;

}
