package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.ModeloOficioRemision;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 2/09/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface ModeloOficioRemisionLocal extends BaseEjb<ModeloOficioRemision, Long> {

    /**
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Long getTotal(Long idEntidad) throws Exception;

    /**
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public List<ModeloOficioRemision> getByEntidad(Long idEntidad) throws Exception;

    /**
     *
     * @param inicio
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public List<ModeloOficioRemision> getPagination(int inicio, Long idEntidad) throws Exception;

}