package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Plantilla;
import es.caib.regweb3.model.utils.PlantillaJson;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 16/07/14
 */
@Local
public interface PlantillaLocal extends BaseEjb<Plantilla, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/PlantillaEJB";


    /**
     * Obtiene la {@link Plantilla} de un {@link es.caib.regweb3.model.Usuario} con un orden
     *
     * @param idUsuario
     * @param orden
     * @return
     * @throws I18NException
     */
    Plantilla findByOrden(Long idUsuario, int orden) throws I18NException;

    /**
     * Obtiene todas las {@link Plantilla} de un {@link es.caib.regweb3.model.Usuario}, paginadas.
     *
     * @param inicio
     * @param idUsuario
     * @return
     * @throws I18NException
     */
    List<Plantilla> getPaginationUsuario(int inicio, Long idUsuario) throws I18NException;

    /**
     * Obtiene todas las {@link Plantilla} de un {@link es.caib.regweb3.model.Usuario}
     *
     * @param idUsuario
     * @return
     * @throws I18NException
     */
    List<Plantilla> getAllbyUsuario(Long idUsuario) throws I18NException;

    /**
     * Obtiene las {@link Plantilla} de un {@link es.caib.regweb3.model.Usuario} Activas
     *
     * @param idUsuario
     * @return
     * @throws I18NException
     */
    List<Plantilla> getActivasbyUsuario(Long idUsuario, Long tipoRegistro) throws I18NException;

    /**
     * Obtiene el número total de {@link Plantilla} de un {@link es.caib.regweb3.model.Usuario}
     *
     * @param idUsuario
     * @return
     * @throws I18NException
     */
    Long getTotalbyUsuario(Long idUsuario) throws I18NException;

    /**
     * Obtiene el orden máximo de una {@link Plantilla} existente
     *
     * @param idUsuario
     * @return
     * @throws I18NException
     */
    Integer maxOrdenPlantilla(Long idUsuario) throws I18NException;

    /**
     * Obtener usuario de una {@link Plantilla}
     *
     * @param idPlantilla
     * @return
     * @throws I18NException
     */
    Long obtenerUsuarioPlantilla(Long idPlantilla) throws I18NException;

    /**
     * Modifica orden en una {@link Plantilla}
     *
     * @param idPlantilla
     * @param orden
     * @return
     * @throws I18NException
     */
    void modificarOrden(Long idPlantilla, int orden) throws I18NException;

    /**
     * Sube orden en una {@link Plantilla}
     *
     * @param idPlantilla
     * @return
     * @throws I18NException
     */
    Boolean subirOrden(Long idPlantilla) throws I18NException;

    /**
     * Baja orden en una {@link Plantilla}
     *
     * @param idPlantilla
     * @return
     * @throws I18NException
     */
    Boolean bajarOrden(Long idPlantilla) throws I18NException;

    /**
     * Cambiar estado de una {@link Plantilla}
     *
     * @param idPlantilla
     * @return
     * @throws I18NException
     */
    Boolean cambiarEstado(Long idPlantilla) throws I18NException;

    /**
     * Elimina las Plantillas de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

    /**
     * Comprueba si un usuario tiene Plantilla
     *
     * @param idUsuarioEntidad
     * @return
     * @throws I18NException
     */
    Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws I18NException;

    /**
     * @param idPlantilla
     * @return
     * @throws I18NException
     */
    PlantillaJson obtenerPlantilla(Long idPlantilla, Entidad entidad) throws I18NException;

}

