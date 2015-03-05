package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.Oficina;
import es.caib.regweb.model.Organismo;
import es.caib.regweb.model.RelacionOrganizativaOfi;
import es.caib.regweb.model.RelacionOrganizativaOfiPK;
import es.caib.regweb.model.utils.ObjetoBasico;

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

  public void deleteByOficina(Long idOficina) throws Exception;

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
    public List<ObjetoBasico> getOficinasByOrganismoVO(Long idOrganismo) throws Exception;

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
     * Obtiene las Relaciones funcinales de las Oficinas cuya Entidad responsable es la indicada
     * @param idEntidad
     * @param estado
     * @return
     * @throws Exception
     */
    public List<RelacionOrganizativaOfi> funcionalByEntidadEstado(Long idEntidad, String estado) throws Exception;

}
