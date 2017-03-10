package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.RelacionSirOfi;
import es.caib.regweb3.model.RelacionSirOfiPK;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 10/10/13
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public interface RelacionSirOfiLocal extends BaseEjb<RelacionSirOfi, RelacionSirOfiPK> {

    public void deleteAll() throws Exception;

    public int deleteByOficinaEntidad(Long idOficina) throws Exception;

    /**
     * Busca una RelacionSirOfi a partir de la Oficina y el Organismo que la componen
     *
     * @param idOficina
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    public RelacionSirOfi getRelacionSir(Long idOficina, Long idOrganismo) throws Exception;

    /**
     * Obtiene las oficinas que son SIR de una Entidad según un estado
     * @param idEntidad
     * @param estado
     * @return
     * @throws Exception
     */
    public List<RelacionSirOfi> oficinasSirByEntidadEstado(Long idEntidad, String estado) throws Exception;

    /**
     * Obtiene las Oficinas SIR con el Organismo seleccionado
     *
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    public List<Oficina> oficinasSIR(Long idOrganismo, Boolean oficinaVirtual) throws Exception;

    /**
     * Elimina las RelacionSirOfi de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;
}
