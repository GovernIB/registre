package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.Oficina;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface OficinaLocal extends BaseEjb<Oficina, Long> {

    /**
     * Busca una Oficina a partir de su código
     * @param codigo
     * @return
     * @throws Exception
     */
    public Oficina findByCodigo(String codigo) throws Exception;

    /**
     * Busca una Oficina válida a partir de su código
     * @param codigo
     * @return
     * @throws Exception
     */
    public Oficina getOficinaValidaByCodigo(String codigo) throws Exception;

    /**
     * Obtiene las Oficinas cuyo Organismo responsable es el indicado
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    public List<Oficina> findByOrganismoResponsable(Long idOrganismo) throws Exception;

    /**
     * Obtiene las Oficinas cuya Entidad responsable es la indicada
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public List<Oficina> findByEntidad(Long idEntidad) throws Exception;

    /**
     * Obtiene las Oficinas cuya Entidad responsable es la indicada y tienen el estado indicado
     * @param idEntidad
     * @param estado
     * @return
     * @throws Exception
     */
    public List<Oficina> findByEntidadByEstado(Long idEntidad, String estado) throws Exception;

    /**
     * Obtiene las Oficinas responsables cuya Entidad responsable es la indicada
     * @param idEntidad
     * @param estado
     * @return
     * @throws Exception
     */
    public List<Oficina> responsableByEntidadEstado(Long idEntidad, String estado) throws Exception;

    /**
     * Obtiene las Oficinas dependientes cuya Entidad responsable es la indicada
     * @param idEntidad
     * @param estado
     * @return
     * @throws Exception
     */
    public List<Oficina> dependienteByEntidadEstado(Long idEntidad, String estado) throws Exception;
}
