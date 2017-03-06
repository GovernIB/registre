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
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface RelacionOrganizativaOfiLocal extends BaseEjb<RelacionOrganizativaOfi, RelacionOrganizativaOfiPK> {
  
  public void deleteAll() throws Exception;

  public int deleteByOficinaEntidad(Long idOficina) throws Exception;

    /**
     * Obtiene las Oficinas relacionadas con el Organismo seleccionado
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    public List<Oficina> getOficinasByOrganismo(Long idOrganismo) throws Exception;

    /**
     * Obtiene las Oficinas relacionadas con el Organismo seleccionado
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    public List<Oficina> oficinasOrganizativas(Long idOrganismo, Boolean oficinaVirtual) throws Exception;

    /**
     * Obtiene los Organismosr elacionados con la Oficina seleccionada
     * @param idOficina
     * @return
     * @throws Exception
     */
    public List<Organismo> getOrganismosByOficina(Long idOficina) throws Exception;

    /**
     * Busca una RelacionOrganizativaOfi a partir de la Oficina y el Organismo que la componen
     * @param idOficina
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    public RelacionOrganizativaOfi getRelacionOrganizativa(Long idOficina, Long idOrganismo) throws Exception;

    /**
     * Obtiene las Relaciones organizativas de las Oficinas cuya Entidad responsable es la indicada
     * @param idEntidad
     * @param estado
     * @return
     * @throws Exception
     */
    public List<RelacionOrganizativaOfi> organizativaByEntidadEstado(Long idEntidad, String estado) throws Exception;

    /**
     * Elimina las RelacionOrganizativaOfi de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;

}
