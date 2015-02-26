package es.caib.regweb.persistence.ejb;

import es.caib.regweb.model.CodigoAsunto;

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
     * Retorna el {@link es.caib.regweb.model.CodigoAsunto} asociado a un codigo.
     * @param codigo
     * @return
     * @throws Exception
     */
    public CodigoAsunto findByCodigo(String codigo) throws Exception;

}
