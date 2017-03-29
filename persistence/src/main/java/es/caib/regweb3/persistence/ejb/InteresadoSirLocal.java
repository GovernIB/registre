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

    /**
     * Elimina los AsientoRegistralSir de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;

}

