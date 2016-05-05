package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.PropiedadGlobal;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 05/05/16
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public interface PropiedadGlobalLocal extends BaseEjb<PropiedadGlobal, Long> {

    /**
     * Obtiene todas las {@link es.caib.regweb3.model.PropiedadGlobal} de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public List<PropiedadGlobal> findByEntidad(Long idEntidad) throws Exception;

    /**
     * Obtiene una {@link es.caib.regweb3.model.PropiedadGlobal} de una Entidad, según su clave
     *
     * @param clave     de la Propiedad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public PropiedadGlobal findByClaveEntidad(String clave, Long idEntidad, Long idPropiedadGlobal) throws Exception;

    /**
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Long getTotalByEntidad(Long idEntidad) throws Exception;

    /**
     * @param inicio
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public List<PropiedadGlobal> getPaginationByEntidad(int inicio, Long idEntidad) throws Exception;

    /**
     * @return
     * @throws Exception
     */
    public Long getTotalREGWEB3() throws Exception;

    /**
     * @param inicio
     * @return
     * @throws Exception
     */
    public List<PropiedadGlobal> getPaginationREGWEB3(int inicio) throws Exception;
}
