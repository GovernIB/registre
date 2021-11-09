package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CatEstadoEntidad;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA"})
public interface CatEstadoEntidadLocal extends BaseEjb<CatEstadoEntidad, Long> {

    /**
     * Retorna los {@link es.caib.regweb3.model.CatEstadoEntidad} a partir de un código
     * @param codigo
     * @return
     * @throws Exception
     */
    CatEstadoEntidad findByCodigo(String codigo) throws Exception;

}
