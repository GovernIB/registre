package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.HistoricoRegistroEntrada;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.UsuarioEntidad;
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
public interface HistoricoRegistroEntradaLocal extends BaseEjb<HistoricoRegistroEntrada, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/HistoricoRegistroEntradaEJB";


    List<HistoricoRegistroEntrada> getByRegistroEntrada(Long idRegistro) throws I18NException;

    /**
     * Crea un HistoricoRegistroEntrada según los parámetros
     *
     * @param registroEntrada
     * @param usuarioEntidad
     * @param modificacion
     * @param serializar
     * @return
     * @throws I18NException
     */
    HistoricoRegistroEntrada crearHistoricoRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad, String modificacion, boolean serializar) throws I18NException;

    /**
     * Comprueba si un usuario tiene HistoricoRegistroEntrada
     *
     * @param idUsuarioEntidad
     * @return
     * @throws I18NException
     */
    Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws I18NException;

    /**
     * Elimina las HistoricoRegistroSalida de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;


}
