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
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface CodigoAsuntoLocal extends BaseEjb<CodigoAsunto, Long> {

    /**
     *
     * @param idTipoAsunto
     * @return
     * @throws Exception
     */
    public Long getTotal(Long idTipoAsunto) throws Exception;

    /**
     *
     * @param inicio
     * @param idTipoAsunto
     * @return
     * @throws Exception
     */
    public List<CodigoAsunto> getPagination(int inicio, Long idTipoAsunto) throws Exception;

    /**
     * Obtiene los CodigoAsunto pertenecientes a un TipoAsunto
     * @param idTipoAsunto
     * @return
     * @throws Exception
     */
    public List<CodigoAsunto> getByTipoAsunto(Long idTipoAsunto) throws Exception;

    /**
     * Retorna el {@link es.caib.regweb3.model.CodigoAsunto} asociado a un codigo.
     * @param codigo
     * @return
     * @throws Exception
     */
    public CodigoAsunto findByCodigo(String codigo) throws Exception;

    /**
     * Comprueba que el {@link es.caib.regweb3.model.CodigoAsunto} codigo de asunto ya no existe para una Entidad.
     * @param codigo
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public CodigoAsunto findByCodigoEntidad(String codigo, Long idEntidad) throws Exception;

}
