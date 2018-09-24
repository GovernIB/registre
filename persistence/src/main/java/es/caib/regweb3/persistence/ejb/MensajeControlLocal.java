package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.sir.MensajeControl;

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
public interface MensajeControlLocal extends BaseEjb<MensajeControl, Long> {

    /**
     * Obtiene las mensajeControles de una entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<MensajeControl> getByEntidad(Long idEntidad) throws Exception;

    /**
     * Obtiene los Mensajes de control de un IdentificadorIntercambio
     * @param identificadorIntercambio
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<MensajeControl> getByIdentificadorIntercambio(String identificadorIntercambio, Long idEntidad) throws Exception;

    /**
     * Realiza las acciones pertinentes cuando se recibie un mensaje de control
     * @param mensaje
     * @throws Exception
     */
    void procesarMensajeDatosControl(MensajeControl mensaje) throws Exception;

    /**
     * Elimina las mensajeControles de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

}
