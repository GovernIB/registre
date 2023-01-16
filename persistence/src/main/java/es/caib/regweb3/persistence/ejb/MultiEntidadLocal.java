package es.caib.regweb3.persistence.ejb;

import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author mgonzalez
 * Date: 07/07/2021
 */

@Local
public interface MultiEntidadLocal {

    String JNDI_NAME = "java:app/regweb3-persistence/MultiEntidadEJB";


    /**
     * Determina si una implementación es multientidad (más de una entidad con sir activado)
     *
     * @throws I18NException
     */
    boolean isMultiEntidad() throws I18NException;
}
