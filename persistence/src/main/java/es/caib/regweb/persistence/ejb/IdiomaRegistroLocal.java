package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.IdiomaRegistro;

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
public interface IdiomaRegistroLocal extends BaseEjb<IdiomaRegistro, Long> {

    /**
     * Retorna el {@link es.caib.regweb.model.IdiomaRegistro} asociado a un codigo.
     * @param codigo
     * @return
     * @throws Exception
     */
    public IdiomaRegistro findByCodigo(String codigo) throws Exception;

}
