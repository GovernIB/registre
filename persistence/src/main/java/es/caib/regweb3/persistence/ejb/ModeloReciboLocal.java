package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.ModeloRecibo;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface ModeloReciboLocal extends BaseEjb<ModeloRecibo, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/ModeloReciboEJB";


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
    List<ModeloRecibo> getByEntidad(Long idEntidad) throws I18NException;

    /**
     * @param inicio
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<ModeloRecibo> getPagination(int inicio, Long idEntidad) throws I18NException;

    /**
     * Elimina los {@link es.caib.regweb3.model.ModeloRecibo} de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

}
