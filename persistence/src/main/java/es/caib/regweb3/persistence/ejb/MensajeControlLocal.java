package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.sir.MensajeControl;
import es.caib.regweb3.persistence.utils.Paginacion;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.Date;
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
     * Realiza la búsque de Mensaje de Control según los datos del formulario
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param mensajeControl
     * @param entidad
     * @return
     * @throws Exception
     */
    Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, MensajeControl mensajeControl, Entidad entidad) throws Exception;

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
