package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.CodigoAsunto;

import javax.annotation.security.RolesAllowed;
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

    /**
     *
     * @param idTipoAsunto
     * @return
     * @throws Exception
     */
    Long getTotal(Long idTipoAsunto) throws Exception;

    /**
     *
     * @param inicio
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<CodigoAsunto> getPagination(int inicio, Long idEntidad) throws Exception;

    /**
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Long getTotalEntidad(Long idEntidad) throws Exception;

    /**
     * Obtiene los CodigoAsunto pertenecientes a un TipoAsunto
     * @param idTipoAsunto
     * @return
     * @throws Exception
     */
    List<CodigoAsunto> getByTipoAsunto(Long idTipoAsunto) throws Exception;

    /**
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<CodigoAsunto> getActivosEntidad(Long idEntidad) throws Exception;

    /**
     * Obtiene los CodigoAsunto Activos pertenecientes a un TipoAsunto
     * @param idTipoAsunto
     * @return
     * @throws Exception
     */
    List<CodigoAsunto> getActivosByTipoAsunto(Long idTipoAsunto) throws Exception;

    /**
     * Retorna el {@link es.caib.regweb3.model.CodigoAsunto} asociado a un codigo.
     * @param codigo
     * @return
     * @throws Exception
     */
    CodigoAsunto findByCodigo(String codigo) throws Exception;

    /**
     *
     * @param codigo
     * @param idCodigoAsunto
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Boolean existeCodigoEdit(String codigo, Long idCodigoAsunto, Long idEntidad) throws Exception;

    /**
     * Comprueba que el {@link es.caib.regweb3.model.CodigoAsunto} codigo de asunto ya no existe para una Entidad.
     * @param codigo
     * @param idEntidad
     * @return
     * @throws Exception
     */
    CodigoAsunto findByCodigoEntidad(String codigo, Long idEntidad) throws Exception;

    /**
     * Elimina los CodigoAsunto de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

}
