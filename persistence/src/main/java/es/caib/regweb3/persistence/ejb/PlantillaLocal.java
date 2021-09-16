package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Plantilla;
import es.caib.regweb3.model.utils.PlantillaJson;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 *         Date: 16/07/14
 */
@Local
public interface PlantillaLocal extends BaseEjb<Plantilla, Long> {

    /**
     * Obtiene la {@link Plantilla} de un {@link es.caib.regweb3.model.Usuario} con un orden
     * @param idUsuario
     * @param orden
     * @return
     * @throws Exception
     */
    Plantilla findByOrden(Long idUsuario, int orden) throws Exception;

    /**
     * Obtiene todas las {@link Plantilla} de un {@link es.caib.regweb3.model.Usuario}, paginadas.
     * @param inicio
     * @param idUsuario
     * @return
     * @throws Exception
     */
    List<Plantilla> getPaginationUsuario(int inicio, Long idUsuario) throws Exception;

    /**
     * Obtiene todas las {@link Plantilla} de un {@link es.caib.regweb3.model.Usuario}
     * @param idUsuario
     * @return
     * @throws Exception
     */
    List<Plantilla> getAllbyUsuario(Long idUsuario) throws Exception;

    /**
     * Obtiene las {@link Plantilla} de un {@link es.caib.regweb3.model.Usuario} Activas
     * @param idUsuario
     * @return
     * @throws Exception
     */
    List<Plantilla> getActivasbyUsuario(Long idUsuario, Long tipoRegistro) throws Exception;

    /**
     * Obtiene el número total de {@link Plantilla} de un {@link es.caib.regweb3.model.Usuario}
     * @param idUsuario
     * @return
     * @throws Exception
     */
    Long getTotalbyUsuario(Long idUsuario) throws Exception;

    /**
     * Obtiene el orden máximo de una {@link Plantilla} existente
     * @param idUsuario
     * @return
     * @throws Exception
     */
    Integer maxOrdenPlantilla(Long idUsuario) throws Exception;

    /**
     * Obtener usuario de una {@link Plantilla}
     * @param idPlantilla
     * @return
     * @throws Exception
     */
    Long obtenerUsuarioPlantilla(Long idPlantilla) throws Exception;

    /**
     * Modifica orden en una {@link Plantilla}
     *
     * @param idPlantilla
     * @param orden
     * @return
     * @throws Exception
     */
    void modificarOrden(Long idPlantilla, int orden) throws Exception;

    /**
     * Sube orden en una {@link Plantilla}
     *
     * @param idPlantilla
     * @return
     * @throws Exception
     */
    Boolean subirOrden(Long idPlantilla) throws Exception;

    /**
     * Baja orden en una {@link Plantilla}
     * @param idPlantilla
     * @return
     * @throws Exception
     */
    Boolean bajarOrden(Long idPlantilla) throws Exception;

    /**
     * Cambiar estado de una {@link Plantilla}
     * @param idPlantilla
     * @return
     * @throws Exception
     */
    Boolean cambiarEstado(Long idPlantilla) throws Exception;

    /**
     * Elimina las Plantillas de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Comprueba si un usuario tiene Plantilla
     *
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws Exception;

    /**
     *
     * @param idPlantilla
     * @return
     * @throws Exception
     */
    PlantillaJson obtenerPlantilla(Long idPlantilla, Entidad entidad) throws Exception;

}

