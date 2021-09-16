package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.PermisoLibroUsuario;

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
public interface PermisoLibroUsuarioLocal extends BaseEjb<PermisoLibroUsuario, Long> {


    /**
     * Retorna los {@link es.caib.regweb3.model.PermisoLibroUsuario} de un {@link es.caib.regweb3.model.Libro}
     * @param idLibro
     * @return
     * @throws Exception
     */
    List<PermisoLibroUsuario> findByLibro(Long idLibro) throws Exception;


    /**
     * Elimina las PermisoLibroUsuario de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

}
