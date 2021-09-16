package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.*;
import es.caib.regweb3.model.utils.AnexoFull;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 16/01/14
 */
@Local
public interface RegistroEntradaLocal extends RegistroEntradaCambiarEstadoLocal {


    /**
     * Obtiene un Registro de Entrada cargardo la relación con la tabla Anexos e Interesados
     * @param id
     * @return
     * @throws Exception
     */
    RegistroEntrada findByIdCompleto(Long id) throws Exception;


    /**
     * Guarda un Registro de Entrada y le asocia un número de registro (con anexos)
     *
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    RegistroEntrada registrarEntrada(RegistroEntrada registroEntrada,
                                     UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexosFull, Boolean validarAnexos)
            throws Exception, I18NException, I18NValidationException;

    /**
     * Actualiza un Registro de entrada
     * @param registroEntrada
     * @param usuarioEntidad
     * @return
     * @throws Exception
     * @throws I18NException
     */
    RegistroEntrada actualizar(RegistroEntrada antiguo, RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws Exception, I18NException;

    /**
     * Comprueba si un RegistroEntrada se considera un OficioRemision interno o no
     *
     * @param idRegistro
     * @param organismos Lista con los Destinatarios que no se consideran Oficio de Remisión
     * @return
     * @throws Exception
     */
    Boolean isOficioRemisionInterno(Long idRegistro, Set<Long> organismos) throws Exception;

    /**
     * Identifica todos los registros de una oficina que son Oficios Internos
     * @param oficina
     * @throws Exception
     */
    Integer actualizarEventoOficioInterno(Oficina oficina) throws Exception;

    /**
     *
     * @param oficina
     * @return
     * @throws Exception
     */
    Integer actualizarEventoDistribuir(Oficina oficina) throws Exception;

    /**
     *
     * @param oficina
     * @return
     * @throws Exception
     */
    Integer actualizarEventoOficioExterno(Oficina oficina) throws Exception;

    /**
     * Comprueba si un RegistroEntrada se considera un OficioRemision externo o no
     * @param idRegistro
     * @return
     * @throws Exception
     */
    Boolean isOficioRemisionExterno(Long idRegistro) throws Exception;

    /**
     * Comprueba si un RegistroEntrada se considera un OficioRemision SIR o no
     * @param idRegistro
     * @return
     * @throws Exception
     */
    List<OficinaTF> isOficioRemisionSir(Long idRegistro) throws Exception;


    /**
     * Comprueba si un RegistroEntrada se considera un OficioRemision SIR o no en un entorno multientidad
     * @param idRegistro
     * @return
     * @throws Exception
     */
    List<OficinaTF> isOficioRemisionSirMultiEntidad(Long idRegistro) throws Exception;


    /**
     * Obtiene el destino externo del registro de entrada de dir3caib
     * @param idRegistro
     * @return
     * @throws Exception
     */
    String obtenerDestinoExternoRE(Long idRegistro) throws Exception;

    /**
     * Obtiene el próximo evento que habrá que realizar con el Registro
     * @param registroEntrada
     * @param entidadActiva
     * @return
     * @throws Exception
     */
    Long proximoEventoEntrada(RegistroEntrada registroEntrada, Entidad entidadActiva, Long idOficina) throws Exception;


    /**
     * Obtiene el próximo evento que habrá que realizar con el Registro en un entorno multientidad
     * @param registroEntrada
     * @param entidadActiva
     * @return
     * @throws Exception
     */
    Long proximoEventoEntradaMultiEntidad(RegistroEntrada registroEntrada, Entidad entidadActiva, Long idOficina) throws Exception;


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
     * Tramita un RegistroEntrada, creando el HistoricoEstado y Trazabilidad
     *
     * @param registroEntrada
     * @throws Exception
     */
    void marcarDistribuido(RegistroEntrada registroEntrada) throws Exception;


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
     * Método que devuelve un registro de entrada completo, con anexoFull pero sin los documentos fisicos.
     *
     * @param id
     * @return
     * @throws Exception
     * @throws I18NException
     */
    RegistroEntrada getConAnexosFullLigero(Long id) throws Exception, I18NException;

    /**
     * Obtiene el Registro para Distribuir, sin cargar el Justificante, porque solo se envía el custodyID
     * @param id
     * @return
     * @throws Exception
     * @throws I18NException
     */
    RegistroEntrada getConAnexosFullDistribuir(Long id) throws Exception, I18NException;

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
