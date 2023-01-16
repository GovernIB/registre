package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.ModeloOficioRemision;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 2/09/14
 */
@Local
public interface ModeloOficioRemisionLocal extends BaseEjb<ModeloOficioRemision, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/ModeloOficioRemisionEJB";


    /**
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Long getTotal(Long idEntidad) throws I18NException;

    /**
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<ModeloOficioRemision> getByEntidad(Long idEntidad) throws I18NException;

    /**
     * @param inicio
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<ModeloOficioRemision> getPagination(int inicio, Long idEntidad) throws I18NException;

    /**
     * Elimina los {@link es.caib.regweb3.model.ModeloOficioRemision} de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

}