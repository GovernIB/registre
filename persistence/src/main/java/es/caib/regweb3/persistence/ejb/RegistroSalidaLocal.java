package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroSalida;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.AnexoFull;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.ejb.Local;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface RegistroSalidaLocal extends RegistroSalidaCambiarEstadoLocal {

    String JNDI_NAME = "java:app/regweb3-persistence/RegistroSalidaEJB";


    /**
     * Obtiene un Registro de Salida cargardo la relación con la tabla Anexos e Interesados
     *
     * @param id
     * @return
     * @throws I18NException
     */
    RegistroSalida findByIdCompleto(Long id) throws I18NException;

    /**
     * Guarda un Registro de Salida (con anexos)
     *
     * @param registroSalida
     * @return
     * @throws I18NException
     */
    RegistroSalida registrarSalida(RegistroSalida registroSalida,Entidad entidad, UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexos, Boolean validarAnexos)
            throws I18NException, I18NValidationException;

    /**
     * Actualiza un Registro salida
     *
     * @param antiguo
     * @param registroSalida
     * @param usuarioEntidad
     * @return
     * @throws I18NException
     * @throws I18NException
     */
    RegistroSalida actualizar(RegistroSalida antiguo, RegistroSalida registroSalida, Entidad entidad,UsuarioEntidad usuarioEntidad) throws I18NException;

    /**
     * Obtiene el próximo evento que habrá que realizar con el Registro
     *
     * @param registroSalida
     * @param entidadActiva
     * @return
     * @throws I18NException
     */
    Long proximoEventoSalida(RegistroSalida registroSalida, Entidad entidadActiva) throws I18NException;


    /**
     * Obtiene el próximo evento que habrá que realizar con el Registro en un entorno multientidad
     *
     * @param registroSalida
     * @param entidadActiva
     * @return
     * @throws I18NException
     */
    Long proximoEventoSalidaMultiEntidad(RegistroSalida registroSalida, Entidad entidadActiva) throws I18NException;

    /**
     * @param idRegistro
     * @param entidadActiva
     * @throws I18NException
     */
    void actualizarEvento(Long idRegistro, Entidad entidadActiva) throws I18NException;


    /**
     * Comprueba si un RegistroSalida se considera un OficioRemision interno o no
     *
     * @param registroSalida
     * @param organismos
     * @return
     * @throws I18NException
     */
    Boolean isOficioRemisionInterno(RegistroSalida registroSalida, Set<String> organismos) throws I18NException;

    /**
     * Comprueba si un RegistroSalida se considera un OficioRemision externo o no
     *
     * @param registroSalida
     * @param organismos
     * @return
     * @throws I18NException
     */
    Boolean isOficioRemisionExterno(RegistroSalida registroSalida, Set<String> organismos) throws I18NException;

    /**
     * Comprueba si un RegistroSalida se considera un OficioRemision externo o no en un entorno multientidad
     *
     * @param registroSalida
     * @param organismos
     * @return
     * @throws I18NException
     */

    Boolean isOficioRemisionExternoMultiEntidad(RegistroSalida registroSalida, Set<String> organismos) throws I18NException;

    /**
     * Comprueba si un RegistroSalida se considera un OficioRemision SIR o no
     * @param registroSalida
     * @param organismos
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<OficinaTF> isOficioRemisionSir(RegistroSalida registroSalida, Set<String> organismos, Long idEntidad) throws I18NException;


    /**
     * Comprueba si un RegistroSalida se considera un OficioRemision SIR o no en un entorno multientidad
     * @param registroSalida
     * @param organismos
     * @return
     * @throws I18NException
     */
    List<OficinaTF> isOficioRemisionSirMultiEntidad(RegistroSalida registroSalida, Set<String> organismos, Long idEntidad) throws I18NException;


    /**
     * Anula un RegistroSalida, cambiandole el estado a anulado.
     *
     * @param registroSalida
     * @param usuarioEntidad
     * @param observacionesAnulacion
     * @throws I18NException
     */
    void anularRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad, String observacionesAnulacion) throws I18NException;

    /**
     * Activa un RegistroSalida, cambiandole el estado a anulado.
     *
     * @param registroSalida
     * @param usuarioEntidad
     * @throws I18NException
     */
    void activarRegistroSalida(RegistroSalida registroSalida, Entidad entidad, UsuarioEntidad usuarioEntidad) throws I18NException;

    /**
     * Visa un RegistroSalida, cambiandole el estado a anulado.
     *
     * @param registroSalida
     * @param usuarioEntidad
     * @throws I18NException
     */
    void visarRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws I18NException;

    /**
     * Elimina los RegistroSalida de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

    /**
     * Cambia el estado de un RegistroSalida
     *
     * @param registroSalida
     * @param idEstado
     * @param usuarioEntidad
     * @throws I18NException
     */
    void cambiarEstadoHistorico(RegistroSalida registroSalida, Long idEstado, UsuarioEntidad usuarioEntidad) throws I18NException;

    /**
     * Cambia el estado de un RegistroEntrada y el HistoricoModificación correspondiente
     *
     * @param registroSalida
     * @param idEstado
     * @param observacionesAnulacion
     * @throws I18NException
     */
    void cambiarEstadoAnuladoHistorico(RegistroSalida registroSalida, Long idEstado, UsuarioEntidad usuarioEntidad, String observacionesAnulacion) throws I18NException;


    /**
     * Rectificar Registro de Salida
     *
     * @param registroSalida
     * @param usuarioEntidad
     * @return
     * @throws I18NException
     */
    RegistroSalida rectificar(Entidad entidad, RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws I18NException;


    /**
     * Método que devuelve un registro de salida completo, con los anexos completos
     *
     * @param id
     * @return
     * @throws I18NException
     * @throws I18NException
     */
    RegistroSalida getConAnexosFull(Long id) throws I18NException;

    /**
     * Método que devuelve un registro de salida completo, con los anexosFull pero sin los documentos fisicos.
     *
     * @param id
     * @return
     * @throws I18NException
     * @throws I18NException
     */
    RegistroSalida getConAnexosFullLigero(Long id) throws I18NException;

    /**
     * Metodo que llama al plugin de postproceso cuando creamos un registro de salida.
     *
     * @param rs
     * @return
     * @throws I18NException
     */
    void postProcesoNuevoRegistro(RegistroSalida rs, Long entidadId) throws I18NException;

    /**
     * Metodo que llama al plugin de postproceso cuando actualizamos un registro de salida
     *
     * @param rs
     * @return
     * @throws I18NException
     */
    void postProcesoActualizarRegistro(RegistroSalida rs, Long entidadId) throws I18NException;


}
