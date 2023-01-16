package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.HistoricoRegistroSalida;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.UsuarioEntidad;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 30/10/14
 */
@Local
public interface HistoricoRegistroSalidaLocal extends BaseEjb<HistoricoRegistroSalida, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/HistoricoRegistroSalidaEJB";


    List<HistoricoRegistroSalida> getByRegistroSalida(Long idRegistro) throws I18NException;

    /**
     * Crea un HistoricoRegistroSalida según los parámetros
     *
     * @param registroSalida
     * @param usuarioEntidad
     * @param modificacion
     * @param serializar
     * @return
     * @throws I18NException
     */
    HistoricoRegistroSalida crearHistoricoRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad, String modificacion, boolean serializar) throws I18NException;


    /**
     * Comprueba si un usuario tiene HistoricoRegistroSalida
     *
     * @param idUsuarioEntidad
     * @return
     * @throws I18NException
     */
    Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws I18NException;

    /**
     * Elimina las HistoricoRegistroEntrada de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;


}
