package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Repro;
import es.caib.regweb3.model.utils.ReproJson;

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
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public interface ReproLocal extends BaseEjb<Repro, Long> {

    /**
     * Obtiene la {@link es.caib.regweb3.model.Repro} de un {@link es.caib.regweb3.model.Usuario} con un orden
     * @param idUsuario
     * @param orden
     * @return
     * @throws Exception
     */
    Repro findByOrden(Long idUsuario, int orden) throws Exception;

    /**
     * Obtiene todas las {@link es.caib.regweb3.model.Repro} de un {@link es.caib.regweb3.model.Usuario}, paginadas.
     * @param inicio
     * @param idUsuario
     * @return
     * @throws Exception
     */
    List<Repro> getPaginationUsuario(int inicio, Long idUsuario) throws Exception;

    /**
     * Obtiene todas las {@link es.caib.regweb3.model.Repro} de un {@link es.caib.regweb3.model.Usuario}
     * @param idUsuario
     * @return
     * @throws Exception
     */
    List<Repro> getAllbyUsuario(Long idUsuario) throws Exception;

    /**
     * Obtiene las {@link es.caib.regweb3.model.Repro} de un {@link es.caib.regweb3.model.Usuario} Activas
     * @param idUsuario
     * @return
     * @throws Exception
     */
    List<Repro> getActivasbyUsuario(Long idUsuario, Long tipoRegistro) throws Exception;

    /**
     * Obtiene el número total de {@link es.caib.regweb3.model.Repro} de un {@link es.caib.regweb3.model.Usuario}
     * @param idUsuario
     * @return
     * @throws Exception
     */
    Long getTotalbyUsuario(Long idUsuario) throws Exception;

    /**
     * Obtiene el orden máximo de una {@link es.caib.regweb3.model.Repro} existente
     * @param idUsuario
     * @return
     * @throws Exception
     */
    Integer maxOrdenRepro(Long idUsuario) throws Exception;

    /**
     * Obtener usuario de una {@link es.caib.regweb3.model.Repro}
     * @param idRepro
     * @return
     * @throws Exception
     */
    Long obtenerUsuarioRepro(Long idRepro) throws Exception;

    /**
     * Modifica orden en una {@link es.caib.regweb3.model.Repro}
     *
     * @param idRepro
     * @param orden
     * @return
     * @throws Exception
     */
    void modificarOrden(Long idRepro, int orden) throws Exception;

    /**
     * Sube orden en una {@link es.caib.regweb3.model.Repro}
     *
     * @param idRepro
     * @return
     * @throws Exception
     */
    Boolean subirOrden(Long idRepro) throws Exception;

    /**
     * Baja orden en una {@link es.caib.regweb3.model.Repro}
     * @param idRepro
     * @return
     * @throws Exception
     */
    Boolean bajarOrden(Long idRepro) throws Exception;

    /**
     * Cambiar estado de una {@link es.caib.regweb3.model.Repro}
     * @param idRepro
     * @return
     * @throws Exception
     */
    Boolean cambiarEstado(Long idRepro) throws Exception;

    /**
     * Elimina las Repros de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Comprueba si un usuario tiene Repro
     *
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws Exception;

    /**
     *
     * @param idRepro
     * @return
     * @throws Exception
     */
    ReproJson obtenerRepro(Long idRepro, Entidad entidad) throws Exception;

}

