package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.RelacionOrganizativaOfi;
import es.caib.regweb3.model.RelacionOrganizativaOfiPK;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 10/10/13
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA"})
public interface RelacionOrganizativaOfiLocal extends BaseEjb<RelacionOrganizativaOfi, RelacionOrganizativaOfiPK> {
  
  void deleteAll() throws Exception;

  int deleteByOficinaEntidad(Long idOficina) throws Exception;

    /**
     * Obtiene las Oficinas relacionadas con el Organismo seleccionado
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    List<Oficina> getOficinasByOrganismo(Long idOrganismo) throws Exception;

    /**
     * Obtiene las Oficinas relacionadas con el Organismo seleccionado
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    List<Oficina> oficinasOrganizativas(Long idOrganismo, Boolean oficinaVirtual) throws Exception;

    /**
     * Obtiene los Organismosr elacionados con la Oficina seleccionada
     * @param idOficina
     * @return
     * @throws Exception
     */
    List<Organismo> getOrganismosByOficina(Long idOficina) throws Exception;

    /**
     * Busca una RelacionOrganizativaOfi a partir de la Oficina y el Organismo que la componen
     * @param idOficina
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    RelacionOrganizativaOfi getRelacionOrganizativa(Long idOficina, Long idOrganismo) throws Exception;

    /**
     * Obtiene las Relaciones organizativas de las Oficinas cuya Entidad responsable es la indicada
     * @param idEntidad
     * @param estado
     * @return
     * @throws Exception
     */
    List<RelacionOrganizativaOfi> organizativaByEntidadEstado(Long idEntidad, String estado) throws Exception;

    /**
     * Elimina las RelacionOrganizativaOfi de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

}
