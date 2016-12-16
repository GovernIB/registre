package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroDetalle;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface RegistroDetalleLocal extends BaseEjb<RegistroDetalle, Long> {

    /**
     * Obtiene el RegistroDetalle de un RegistroEntrada
     * @param idRegistroEntrada
     * @return
     * @throws Exception
     */
    public RegistroDetalle findByRegistroEntrada(Long idRegistroEntrada) throws Exception;

    /**
     * Elimina los RegistroDetalle
     * @param ids
     * @return
     * @throws Exception
     */
    public Integer eliminar(Set<Long> ids) throws Exception;

    /**
     * Obtiene todos los RegistroDetalle de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Set<Long> getRegistrosDetalle(Long idEntidad) throws Exception;

    /**
     * Elimina un anexo de un registroDetalle. Puesto aqui por referencias cruzadas
     *
     * @param idAnexo
     * @param idRegistroDetalle
     * @return
     * @throws Exception
     */
    public boolean eliminarAnexoRegistroDetalle(Long idAnexo, Long idRegistroDetalle) throws Exception;
}
