package es.caib.regweb3.persistence.ejb;

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
     * @throws Exception
     */
    boolean isMultiEntidad() throws Exception;
}
