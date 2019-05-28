package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroEntrada;
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
 *         Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
public interface RegistroEntradaLocal extends RegistroEntradaCambiarEstadoLocal {


    /**
     * Guarda un Registro de Entrada y le asocia un número de registro (con anexos)
     *
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    RegistroEntrada registrarEntrada(RegistroEntrada registroEntrada,
                                     UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexosFull)
            throws Exception, I18NException, I18NValidationException;


    /**
     * Cambia el estado de un RegistroEntrada y el HistoricoModificación correspondiente
     *
     * @param registroEntrada
     * @param idEstado
     * @throws Exception
     */
    void cambiarEstadoHistorico(RegistroEntrada registroEntrada, Long idEstado, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Cambia el estado de un RegistroEntrada y el HistoricoModificación correspondiente
     *
     * @param registroEntrada
     * @param idEstado
     * @param observacionesAnulacion
     * @throws Exception
     */
    void cambiarEstadoAnuladoHistorico(RegistroEntrada registroEntrada, Long idEstado, UsuarioEntidad usuarioEntidad, String observacionesAnulacion) throws Exception;

    /**
     * Anula un RegistroEntrada, cambiandole el estado a anulado.
     *
     * @param registroEntrada
     * @param usuarioEntidad
     * @param observacionesAnulacion
     * @throws Exception
     */
    void anularRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad, String observacionesAnulacion) throws Exception;

    /**
     * Activa un RegistroEntrada, cambiandole el estado a anulado.
     *
     * @param registroEntrada
     * @param usuarioEntidad
     * @throws Exception
     */
    void activarRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Visa un RegistroEntrada, cambiandole el estado a anulado.
     *
     * @param registroEntrada
     * @param usuarioEntidad
     * @throws Exception
     */
    void visarRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Tramita un RegistroEntrada, cambiandole el estado a tramitado.
     *
     * @param registroEntrada
     * @param usuarioEntidad
     * @throws Exception
     */
    void tramitarRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws Exception, I18NValidationException, I18NException;


    /**
     * Elimina los RegistroEntrada de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Rectificar un Registro de entrada, creando una nuevo informando de ello
     *
     * @param registroEntrada
     * @param usuarioEntidad
     * @return
     * @throws Exception
     */
    RegistroEntrada rectificar(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws Exception, I18NException;

    /**
     * Actualiza el Destino extinguido por el que le sustituye
     * @param idOrganismoExtinguido
     * @param idOrganismo
     * @throws Exception
     */
    void actualizarDestinoExtinguido(Long idOrganismoExtinguido, Long idOrganismo) throws Exception;

    /**
     * Método que devuelve un registro de entrada completo, con los anexos completos
     *
     * @param id
     * @return
     * @throws Exception
     * @throws I18NException
     */
    RegistroEntrada getConAnexosFull(Long id) throws Exception, I18NException;

    /**
     * Método que invoca al plugin post proceso al actualizar un registro entrada.
     *
     * @param re
     * @return
     * @throws Exception
     */
    void postProcesoActualizarRegistro(RegistroEntrada re, Long entidadId) throws Exception, I18NException;

    /**
     * @param re
     * @return
     * @throws Exception
     */
    void postProcesoNuevoRegistro(RegistroEntrada re, Long entidadId) throws Exception, I18NException;

}
