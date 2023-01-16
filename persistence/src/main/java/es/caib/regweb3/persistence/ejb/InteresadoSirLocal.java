package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.InteresadoSir;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface InteresadoSirLocal extends BaseEjb<InteresadoSir, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/InteresadoSirEJB";

    /**
     * Guarda una interesado normalizando algunos campos
     *
     * @param interesadoSir
     * @return
     * @throws I18NException
     */
    InteresadoSir guardarInteresadoSir(InteresadoSir interesadoSir) throws I18NException;

    /**
     * Elimina los InteresadoSir de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

}

