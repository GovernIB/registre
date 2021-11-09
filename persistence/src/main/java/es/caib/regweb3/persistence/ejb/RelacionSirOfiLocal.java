package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
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
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA"})
public interface RelacionSirOfiLocal extends BaseEjb<RelacionSirOfi, RelacionSirOfiPK> {

    void deleteAll() throws Exception;

    int deleteByOficinaEntidad(Long idOficina) throws Exception;

    /**
     * Busca una RelacionSirOfi a partir de la Oficina y el Organismo que la componen
     *
     * @param idOficina
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    RelacionSirOfi getRelacionSir(Long idOficina, Long idOrganismo) throws Exception;

    /**
     * Obtiene las oficinas que son SIR de una Entidad según un estado
     * @param idEntidad
     * @param estado
     * @return
     * @throws Exception
     */
    List<RelacionSirOfi> relacionesSirOfiByEntidadEstado(Long idEntidad, String estado) throws Exception;

    /**
     * Obtiene las Oficinas SIR con el Organismo seleccionado
     *
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    List<Oficina> oficinasSIR(Long idOrganismo) throws Exception;

    /**
     * Obtiene las Oficinas SIR de una Enitdad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public List<Oficina> oficinasSIREntidad(Long idEntidad) throws Exception;

    /**
     * Obtiene los Organismos a los que da Servicio Sir una Oficina  integrada en SIR
     * @param idOficina
     * @return
     * @throws Exception
     */
    List<Organismo> organimosServicioSIR(Long idOficina) throws Exception;

    /**
     * Elimina las RelacionSirOfi de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;
}
