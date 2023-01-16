package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.TipoAsunto;
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
public interface TipoAsuntoLocal extends BaseEjb<TipoAsunto, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/TipoAsuntoEJB";


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
    List<TipoAsunto> getAll(Long idEntidad) throws I18NException;

    /**
     * @param inicio
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<TipoAsunto> getPagination(int inicio, Long idEntidad) throws I18NException;

    /**
     * Método que obtiene los tipos asuntos que están activos.
     * Se usa a la hora de crear un registro de entrada.
     *
     * @return
     * @throws I18NException
     */
    List<TipoAsunto> getActivosEntidad(Long idEntidad) throws I18NException;

    /**
     * Comprueba la existencia del codigo en algún TipoAsunto
     *
     * @param codigo
     * @param idTipoAsunto
     * @return
     * @throws I18NException
     */
    Boolean existeCodigoEdit(String codigo, Long idTipoAsunto, Long idEntidad) throws I18NException;

    /**
     * Retorna el {@link es.caib.regweb3.model.TipoAsunto} asociado a un codigo.
     *
     * @param codigo
     * @return
     * @throws I18NException
     */
    TipoAsunto findByCodigo(String codigo) throws I18NException;

    /**
     * Retorna el {@link es.caib.regweb3.model.TipoAsunto} asociado a un codigo y a una Entidad
     *
     * @param codigo
     * @return
     * @throws I18NException
     */
    TipoAsunto findByCodigoEntidad(String codigo, Long idEntidad) throws I18NException;

    /**
     * Obtiene el Total de {@link es.caib.regweb3.model.TipoAsunto} asociado a una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Long getTotalEntidad(Long idEntidad) throws I18NException;

    /**
     * Elimina los {@link es.caib.regweb3.model.TipoAsunto} de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;
}
