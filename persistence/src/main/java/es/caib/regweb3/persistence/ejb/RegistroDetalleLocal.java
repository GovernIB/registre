package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroDetalle;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.Date;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface RegistroDetalleLocal extends BaseEjb<RegistroDetalle, Long> {

    /**
     * Obtiene el RegistroDetalle cargando los Interesados
     * @param id
     * @return
     * @throws Exception
     */
    RegistroDetalle findByIdConInteresados(Long id) throws Exception;

    /**
     * Obtiene el RegistroDetalle de un RegistroEntrada
     * @param idRegistroEntrada
     * @return
     * @throws Exception
     */
    RegistroDetalle findByRegistroEntrada(Long idRegistroEntrada) throws Exception;

    /**
     * Elimina los RegistroDetalle
     * @param ids
     * @return
     * @throws Exception
     */
    Integer eliminar(Set<Long> ids, Long idEntidad) throws Exception, I18NException;

    /**
     * Obtiene todos los RegistroDetalle de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Set<Long> getRegistrosDetalle(Long idEntidad) throws Exception;

    /**
     * Elimina un anexo de un registroDetalle. Puesto aqui por referencias cruzadas
     *
     * @param idAnexo
     * @param idRegistroDetalle
     * @return
     * @throws Exception
     */
    boolean eliminarAnexoRegistroDetalle(Long idAnexo, Long idRegistroDetalle, Long idEntidad) throws Exception, I18NException;

    /**
     * Obtiene todos los identificadores de los registros detalle que se han confirmado en destino (aceptado).
     * @param idEntidad
     * @return
     * @throws Exception
     */
     Set<Long> getRegistrosDetalleConfirmados(Long idEntidad, Date fecha) throws Exception;
}
