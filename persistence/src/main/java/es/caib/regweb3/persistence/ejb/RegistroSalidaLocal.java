package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
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
     * @throws Exception
     */
    RegistroSalida findByIdCompleto(Long id) throws Exception;

    /**
     * Guarda un Registro de Salida (con anexos)
     *
     * @param registroSalida
     * @return
     * @throws Exception
     */
    RegistroSalida registrarSalida(RegistroSalida registroSalida,
                                   UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexos, Boolean validarAnexos)
            throws Exception, I18NException, I18NValidationException;

    /**
     * Actualiza un Registro salida
     *
     * @param antiguo
     * @param registroSalida
     * @param usuarioEntidad
     * @return
     * @throws Exception
     * @throws I18NException
     */
    RegistroSalida actualizar(RegistroSalida antiguo, RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception, I18NException;

    /**
     * Obtiene el próximo evento que habrá que realizar con el Registro
     *
     * @param registroSalida
     * @param entidadActiva
     * @return
     * @throws Exception
     */
    Long proximoEventoSalida(RegistroSalida registroSalida, Entidad entidadActiva) throws Exception;


    /**
     * Obtiene el próximo evento que habrá que realizar con el Registro en un entorno multientidad
     *
     * @param registroSalida
     * @param entidadActiva
     * @return
     * @throws Exception
     */
    Long proximoEventoSalidaMultiEntidad(RegistroSalida registroSalida, Entidad entidadActiva) throws Exception;

    /**
     * @param idRegistro
     * @param entidadActiva
     * @throws Exception
     */
    void actualizarEvento(Long idRegistro, Entidad entidadActiva) throws Exception;

    /**
     * Actualiza los Registros de Salida que con de Evento Distribuir
     *
     * @param oficina
     * @param entidad
     * @return
     * @throws Exception
     */
    Integer actualizarEventoDistribuirSalidas(Oficina oficina, Entidad entidad) throws Exception;

    /**
     * Actualiza los Registros de Salida que con de Evento Distribuir cuyo destinatario es una Persona
     *
     * @param oficina
     * @param entidad
     * @return
     * @throws Exception
     */
    Integer actualizarEventoDistribuirSalidasPersona(Oficina oficina, Entidad entidad) throws Exception;

    /**
     * Comprueba si un RegistroSalida se considera un OficioRemision interno o no
     *
     * @param registroSalida
     * @param organismos
     * @return
     * @throws Exception
     */
    Boolean isOficioRemisionInterno(RegistroSalida registroSalida, Set<String> organismos) throws Exception;

    /**
     * Comprueba si un RegistroSalida se considera un OficioRemision externo o no
     *
     * @param registroSalida
     * @param organismos
     * @return
     * @throws Exception
     */
    Boolean isOficioRemisionExterno(RegistroSalida registroSalida, Set<String> organismos) throws Exception;

    /**
     * Comprueba si un RegistroSalida se considera un OficioRemision externo o no en un entorno multientidad
     *
     * @param registroSalida
     * @param organismos
     * @return
     * @throws Exception
     */

    Boolean isOficioRemisionExternoMultiEntidad(RegistroSalida registroSalida, Set<String> organismos) throws Exception;

    /**
     * Comprueba si un RegistroSalida se considera un OficioRemision SIR o no
     * @param registroSalida
     * @param organismos
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<OficinaTF> isOficioRemisionSir(RegistroSalida registroSalida, Set<String> organismos, Long idEntidad) throws Exception;


    /**
     * Comprueba si un RegistroSalida se considera un OficioRemision SIR o no en un entorno multientidad
     * @param registroSalida
     * @param organismos
     * @return
     * @throws Exception
     */
    List<OficinaTF> isOficioRemisionSirMultiEntidad(RegistroSalida registroSalida, Set<String> organismos, Long idEntidad) throws Exception;


    /**
     * Anula un RegistroSalida, cambiandole el estado a anulado.
     *
     * @param registroSalida
     * @param usuarioEntidad
     * @param observacionesAnulacion
     * @throws Exception
     */
    void anularRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad, String observacionesAnulacion) throws Exception;

    /**
     * Activa un RegistroSalida, cambiandole el estado a anulado.
     *
     * @param registroSalida
     * @param usuarioEntidad
     * @throws Exception
     */
    void activarRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Visa un RegistroSalida, cambiandole el estado a anulado.
     *
     * @param registroSalida
     * @param usuarioEntidad
     * @throws Exception
     */
    void visarRegistroSalida(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Elimina los RegistroSalida de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Cambia el estado de un RegistroSalida
     *
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
     *
     * @param registroSalida
     * @param usuarioEntidad
     * @return
     * @throws Exception
     */
    RegistroSalida rectificar(RegistroSalida registroSalida, UsuarioEntidad usuarioEntidad) throws Exception;


    /**
     * Método que devuelve un registro de salida completo, con los anexos completos
     *
     * @param id
     * @return
     * @throws Exception
     * @throws I18NException
     */
    RegistroSalida getConAnexosFull(Long id) throws Exception, I18NException;

    /**
     * Método que devuelve un registro de salida completo, con los anexosFull pero sin los documentos fisicos.
     *
     * @param id
     * @return
     * @throws Exception
     * @throws I18NException
     */
    RegistroSalida getConAnexosFullLigero(Long id) throws Exception, I18NException;

    /**
     * Metodo que llama al plugin de postproceso cuando creamos un registro de salida.
     *
     * @param rs
     * @return
     * @throws Exception
     */
    void postProcesoNuevoRegistro(RegistroSalida rs, Long entidadId) throws Exception, I18NException;

    /**
     * Metodo que llama al plugin de postproceso cuando actualizamos un registro de salida
     *
     * @param rs
     * @return
     * @throws Exception
     */
    void postProcesoActualizarRegistro(RegistroSalida rs, Long entidadId) throws Exception, I18NException;


}
