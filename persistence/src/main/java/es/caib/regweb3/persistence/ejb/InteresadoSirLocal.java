package es.caib.regweb3.persistence.ejb;


import es.caib.regweb3.model.InteresadoSir;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface InteresadoSirLocal extends BaseEjb<InteresadoSir, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/InteresadoSirEJB";

    /**
     * Guarda una interesado normalizando algunos campos
     * @param interesadoSir
     * @return
     * @throws Exception
     */
    InteresadoSir guardarInteresadoSir(InteresadoSir interesadoSir) throws Exception;

    /**
     * Elimina los InteresadoSir de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

}

