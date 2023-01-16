package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.RegistroDetalle;
import org.fundaciobit.genapp.common.i18n.I18NException;

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

    String JNDI_NAME = "java:app/regweb3-persistence/RegistroDetalleEJB";


    /**
     * Obtiene el RegistroDetalle cargando los Interesados
     *
     * @param id
     * @return
     * @throws I18NException
     */
    RegistroDetalle findByIdConInteresados(Long id) throws I18NException;

    /**
     * Obtiene el RegistroDetalle de un RegistroEntrada
     *
     * @param idRegistroEntrada
     * @return
     * @throws I18NException
     */
    RegistroDetalle findByRegistroEntrada(Long idRegistroEntrada) throws I18NException;

    /**
     * Elimina los RegistroDetalle
     *
     * @param ids
     * @return
     * @throws I18NException
     */
    Integer eliminar(Set<Long> ids, Long idEntidad) throws I18NException;

    /**
     * Obtiene todos los RegistroDetalle de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Set<Long> getRegistrosDetalle(Long idEntidad) throws I18NException;

    /**
     * Elimina un anexo de un registroDetalle. Puesto aqui por referencias cruzadas
     *
     * @param idAnexo
     * @param idRegistroDetalle
     * @return
     * @throws I18NException
     */
    boolean eliminarAnexoRegistroDetalle(Long idAnexo, Long idRegistroDetalle, Long idEntidad) throws I18NException;

    /**
     * Obtiene todos los identificadores de los registros detalle que se han confirmado en destino (aceptado).
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Set<Long> getRegistrosDetalleConfirmados(Long idEntidad, Date fecha) throws I18NException;
}
