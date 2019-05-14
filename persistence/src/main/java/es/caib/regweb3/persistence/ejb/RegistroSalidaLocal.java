package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.AnexoFull;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

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
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
public interface RegistroSalidaLocal extends RegistroSalidaCambiarEstadoLocal {

    /**
     * Guarda un Registro de Salida (con anexos)
     * @param registroSalida
     * @return
     * @throws Exception
     */
    RegistroSalida registrarSalida(RegistroSalida registroSalida,
                                   UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexos)
            throws Exception, I18NException, I18NValidationException;


    /**
     * Anula un RegistroSalida, cambiandole el estado a anulado.
     * @param registroSalida
     * @param usuarioEntidad
     * @param observacionesAnulacion
     * @throws Exception
     */
    void anularRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad, String observacionesAnulacion) throws Exception;

    /**
     * Activa un RegistroSalida, cambiandole el estado a anulado.
     * @param registroSalida
     * @param usuarioEntidad
     * @throws Exception
     */
    void activarRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Visa un RegistroSalida, cambiandole el estado a anulado.
     * @param registroSalida
     * @param usuarioEntidad
     * @throws Exception
     */
    void visarRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Elimina los RegistroSalida de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Cambia el estado de un RegistroSalida
     * @param registroSalida
     * @param idEstado
     * @param usuarioEntidad
     * @throws Exception
     */
    void cambiarEstadoHistorico(RegistroSalida registroSalida, Long idEstado, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Cambia el estado de un RegistroEntrada y el HistoricoModificación correspondiente
     *
     * @param registroSalida
     * @param idEstado
     * @param observacionesAnulacion
     * @throws Exception
     */
    void cambiarEstadoAnuladoHistorico(RegistroSalida registroSalida, Long idEstado, UsuarioEntidad usuarioEntidad, String observacionesAnulacion) throws Exception;


    /**
     * Rectificar Registro de Salida
     * @param registroSalida
     * @param usuarioEntidad
     * @return
     * @throws Exception
     */
    RegistroSalida rectificar(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Metodo que llama al plugin de postproceso cuando creamos un registro de salida.
     * @param rs
     * @return
     * @throws Exception
     */
    void postProcesoNuevoRegistro(RegistroSalida rs, Long entidadId) throws Exception, I18NException;

    /**
     * Metodo que llama al plugin de postproceso cuando actualizamos un registro de salida
     * @param rs
     * @return
     * @throws Exception
     */
    void postProcesoActualizarRegistro(RegistroSalida rs, Long entidadId) throws Exception, I18NException;

}
