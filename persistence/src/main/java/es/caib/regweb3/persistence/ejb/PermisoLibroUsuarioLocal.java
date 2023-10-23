package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.PermisoLibroUsuario;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface PermisoLibroUsuarioLocal extends BaseEjb<PermisoLibroUsuario, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/PermisoLibroUsuarioEJB";


    /**
     * Retorna los {@link es.caib.regweb3.model.PermisoLibroUsuario} de un {@link es.caib.regweb3.model.Libro}
     *
     * @param idLibro
     * @return
     * @throws I18NException
     */
    List<PermisoLibroUsuario> findByLibro(Long idLibro) throws I18NException;

    /**
     *
     * @param idUsuarioEntidad
     * @throws Exception
     */
    void eliminarByUsuario(Long idUsuarioEntidad) throws Exception;

    /**
     * Elimina las PermisoLibroUsuario de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

}
