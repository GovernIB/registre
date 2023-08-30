package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.RelacionOrganizativaOfi;
import es.caib.regweb3.model.RelacionOrganizativaOfiPK;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 10/10/13
 */
@Local
public interface RelacionOrganizativaOfiLocal extends BaseEjb<RelacionOrganizativaOfi, RelacionOrganizativaOfiPK> {

    String JNDI_NAME = "java:app/regweb3-persistence/RelacionOrganizativaOfiEJB";


    /**
     * @throws I18NException
     */
    void deleteAll() throws I18NException;

    /**
     *
     * @param idOficina
     * @return
     * @throws I18NException
     */
    int deleteByOficinaEntidad(Long idOficina) throws I18NException;

    /**
     * Obtiene las Oficinas relacionadas con el Organismo seleccionado
     *
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    List<Oficina> getOficinasByOrganismo(Long idOrganismo) throws I18NException;

    /**
     * Obtiene las Oficinas relacionadas con el Organismo seleccionado
     *
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    List<Oficina> oficinasOrganizativas(Long idOrganismo, Boolean oficinaVirtual) throws I18NException;

    /**
     * Obtiene los Organismosr elacionados con la Oficina seleccionada
     *
     * @param idOficina
     * @return
     * @throws I18NException
     */
    List<Organismo> getOrganismosByOficina(Long idOficina) throws I18NException;

    /**
     * Busca una RelacionOrganizativaOfi a partir de la Oficina y el Organismo que la componen
     *
     * @param idOficina
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    RelacionOrganizativaOfi getRelacionOrganizativa(Long idOficina, Long idOrganismo) throws I18NException;

    /**
     * Obtiene las Relaciones organizativas de las Oficinas cuya Entidad responsable es la indicada
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<RelacionOrganizativaOfi> organizativaByEntidadEstado(Long idEntidad) throws I18NException;

    /**
     * Elimina las RelacionOrganizativaOfi de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

}
