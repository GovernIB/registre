package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CodigoAsunto;
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
public interface CodigoAsuntoLocal extends BaseEjb<CodigoAsunto, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/CodigoAsuntoEJB";

    /**
     * @param idTipoAsunto
     * @return
     * @throws I18NException
     */
    Long getTotal(Long idTipoAsunto) throws I18NException;

    /**
     * @param inicio
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<CodigoAsunto> getPagination(int inicio, Long idEntidad) throws I18NException;

    /**
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Long getTotalEntidad(Long idEntidad) throws I18NException;

    /**
     * Obtiene los CodigoAsunto pertenecientes a un TipoAsunto
     *
     * @param idTipoAsunto
     * @return
     * @throws I18NException
     */
    List<CodigoAsunto> getByTipoAsunto(Long idTipoAsunto) throws I18NException;

    /**
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<CodigoAsunto> getActivosEntidad(Long idEntidad) throws I18NException;

    /**
     * Obtiene los CodigoAsunto Activos pertenecientes a un TipoAsunto
     *
     * @param idTipoAsunto
     * @return
     * @throws I18NException
     */
    List<CodigoAsunto> getActivosByTipoAsunto(Long idTipoAsunto) throws I18NException;

    /**
     * Retorna el {@link es.caib.regweb3.model.CodigoAsunto} asociado a un codigo.
     *
     * @param codigo
     * @return
     * @throws I18NException
     */
    CodigoAsunto findByCodigo(String codigo) throws I18NException;

    /**
     * @param codigo
     * @param idCodigoAsunto
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Boolean existeCodigoEdit(String codigo, Long idCodigoAsunto, Long idEntidad) throws I18NException;

    /**
     * Comprueba que el {@link es.caib.regweb3.model.CodigoAsunto} codigo de asunto ya no existe para una Entidad.
     *
     * @param codigo
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    CodigoAsunto findByCodigoEntidad(String codigo, Long idEntidad) throws I18NException;

    /**
     * Elimina los CodigoAsunto de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

}
