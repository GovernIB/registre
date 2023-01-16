package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.sir.MensajeControl;
import es.caib.regweb3.persistence.utils.Paginacion;
import org.fundaciobit.genapp.common.i18n.I18NException;

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
public interface MensajeControlLocal extends BaseEjb<MensajeControl, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/MensajeControlEJB";


    /**
     * Obtiene las mensajeControles de una entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<MensajeControl> getByEntidad(Long idEntidad) throws I18NException;

    /**
     * Realiza la búsque de Mensaje de Control según los datos del formulario
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param mensajeControl
     * @param entidad
     * @return
     * @throws I18NException
     */
    Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, MensajeControl mensajeControl, Entidad entidad) throws I18NException;

    /**
     * Obtiene los Mensajes de control de un IdentificadorIntercambio
     *
     * @param identificadorIntercambio
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<MensajeControl> getByIdentificadorIntercambio(String identificadorIntercambio, Long idEntidad) throws I18NException;

    /**
     * Realiza las acciones pertinentes cuando se recibie un mensaje de control
     *
     * @param mensaje
     * @throws I18NException
     */
    void procesarMensajeDatosControl(MensajeControl mensaje) throws I18NException;

    /**
     * Elimina las mensajeControles de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

}
