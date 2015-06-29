package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Repro;

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
@RolesAllowed({"RWE_USUARI"})
public interface ReproLocal extends BaseEjb<Repro, Long> {

    /**
     * Obtiene la {@link es.caib.regweb3.model.Repro} de un {@link es.caib.regweb3.model.Usuario} con un orden
     * @param idUsuario
     * @param orden
     * @return
     * @throws Exception
     */
    public Repro findByOrden(Long idUsuario, int orden) throws Exception;

    /**
     * Obtiene todas las {@link es.caib.regweb3.model.Repro} de un {@link es.caib.regweb3.model.Usuario}, paginadas.
     * @param inicio
     * @param idUsuario
     * @return
     * @throws Exception
     */
    public List<Repro> getPaginationUsuario(int inicio, Long idUsuario) throws Exception;

    /**
     * Obtiene todas las {@link es.caib.regweb3.model.Repro} de un {@link es.caib.regweb3.model.Usuario}
     * @param idUsuario
     * @return
     * @throws Exception
     */
    public List<Repro> getAllbyUsuario(Long idUsuario) throws Exception;

    /**
     * Obtiene las {@link es.caib.regweb3.model.Repro} de un {@link es.caib.regweb3.model.Usuario} Activas
     * @param idUsuario
     * @return
     * @throws Exception
     */
    public List<Repro> getActivasbyUsuario(Long idUsuario, Long tipoRegistro) throws Exception;

    /**
     * Obtiene el número total de {@link es.caib.regweb3.model.Repro} de un {@link es.caib.regweb3.model.Usuario}
     * @param idUsuario
     * @return
     * @throws Exception
     */
    public Long getTotalbyUsuario(Long idUsuario) throws Exception;

    /**
     * Obtiene el orden máximo de una {@link es.caib.regweb3.model.Repro} existente
     * @param idUsuario
     * @return
     * @throws Exception
     */
    public Integer maxOrdenRepro(Long idUsuario) throws Exception;

    /**
     * Obtener usuario de una {@link es.caib.regweb3.model.Repro}
     * @param idRepro
     * @return
     * @throws Exception
     */
    public Long obtenerUsuarioRepro(Long idRepro) throws Exception;

    /**
     * Modifica orden en una {@link es.caib.regweb3.model.Repro}
     *
     * @param idRepro
     * @param orden
     * @return
     * @throws Exception
     */
    public void modificarOrden(Long idRepro, int orden) throws Exception;

    /**
     * Sube orden en una {@link es.caib.regweb3.model.Repro}
     *
     * @param idRepro
     * @return
     * @throws Exception
     */
    public void subirOrden(Long idRepro) throws Exception;

    /**
     * Baja orden en una {@link es.caib.regweb3.model.Repro}
     * @param idRepro
     * @return
     * @throws Exception
     */
    public void bajarOrden(Long idRepro) throws Exception;

    /**
     * Cambiar estado de una {@link es.caib.regweb3.model.Repro}
     * @param idRepro
     * @return
     * @throws Exception
     */
    public void cambiarEstado(Long idRepro) throws Exception;

    /**
     * Elimina las Repros de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;

}

