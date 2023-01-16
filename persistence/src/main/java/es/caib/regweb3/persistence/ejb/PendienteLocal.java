package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Pendiente;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.List;

/**
 * Created 14/10/14 9:56
 *
 * @author mgonzalez
 */
@Local
public interface PendienteLocal extends BaseEjb<Pendiente, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/PendienteEJB";


    /**
     * Obtiene el registro pendiente del organismo en cuestión
     *
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    Pendiente findByIdOrganismo(Long idOrganismo) throws I18NException;

    /**
     * Función que devuelve los pendientes en función de su estado
     *
     * @param estado
     * @return
     * @throws I18NException
     */
    List<Pendiente> findByEstadoProcesado(String estado, Boolean procesado) throws I18NException;

    /**
     * Función que devuelve los pendientes de procesar
     *
     * @return
     * @throws I18NException
     */
    List<Pendiente> findPendientesProcesar(Long idEntidad) throws I18NException;


}
