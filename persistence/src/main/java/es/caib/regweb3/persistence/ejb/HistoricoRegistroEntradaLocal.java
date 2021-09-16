package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.HistoricoRegistroEntrada;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.UsuarioEntidad;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 16/01/14
 */
@Local
public interface HistoricoRegistroEntradaLocal extends BaseEjb<HistoricoRegistroEntrada, Long> {

    List<HistoricoRegistroEntrada> getByRegistroEntrada(Long idRegistro) throws Exception;

    /**
     * Crea un HistoricoRegistroEntrada según los parámetros
     *
     * @param registroEntrada
     * @param usuarioEntidad
     * @param modificacion
     * @param serializar
     * @return
     * @throws Exception
     */
    HistoricoRegistroEntrada crearHistoricoRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad, String modificacion, boolean serializar) throws Exception;

    /**
     * Comprueba si un usuario tiene HistoricoRegistroEntrada
     *
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws Exception;

    /**
     * Elimina las HistoricoRegistroSalida de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;


}
