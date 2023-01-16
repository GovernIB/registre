package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Oficina;
import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.RelacionSirOfi;
import es.caib.regweb3.model.RelacionSirOfiPK;
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
public interface RelacionSirOfiLocal extends BaseEjb<RelacionSirOfi, RelacionSirOfiPK> {

    String JNDI_NAME = "java:app/regweb3-persistence/RelacionSirOfiEJB";


    /**
     * @throws I18NException
     */
    void deleteAll() throws I18NException;

    /**
     * @param idOficina
     * @return
     * @throws I18NException
     */
    int deleteByOficinaEntidad(Long idOficina) throws I18NException;

    /**
     * Busca una RelacionSirOfi a partir de la Oficina y el Organismo que la componen
     *
     * @param idOficina
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    RelacionSirOfi getRelacionSir(Long idOficina, Long idOrganismo) throws I18NException;

    /**
     * Obtiene las oficinas que son SIR de una Entidad según un estado
     *
     * @param idEntidad
     * @param estado
     * @return
     * @throws I18NException
     */
    List<RelacionSirOfi> relacionesSirOfiByEntidadEstado(Long idEntidad, String estado) throws I18NException;

    /**
     * Obtiene las Oficinas SIR con el Organismo seleccionado
     *
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    List<Oficina> oficinasSIR(Long idOrganismo) throws I18NException;

    /**
     * Obtiene las Oficinas SIR de una Enitdad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    public List<Oficina> oficinasSIREntidad(Long idEntidad) throws I18NException;

    /**
     * Obtiene los Organismos a los que da Servicio Sir una Oficina  integrada en SIR
     *
     * @param idOficina
     * @return
     * @throws I18NException
     */
    List<Organismo> organimosServicioSIR(Long idOficina) throws I18NException;

    /**
     * Elimina las RelacionSirOfi de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;
}
