package es.caib.regweb3.persistence.ejb;

import es.caib.dir3caib.ws.api.oficina.OficinaTF;
import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroEntrada;
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
public interface RegistroEntradaLocal extends RegistroEntradaCambiarEstadoLocal {

    String JNDI_NAME = "java:app/regweb3-persistence/RegistroEntradaEJB";


    /**
     * Obtiene un Registro de Entrada cargardo la relación con la tabla Anexos e Interesados
     *
     * @param id
     * @return
     * @throws I18NException
     */
    RegistroEntrada findByIdCompleto(Long id) throws I18NException;


    /**
     * Guarda un Registro de Entrada y le asocia un número de registro (con anexos)
     *
     * @param registroEntrada
     * @return
     * @throws I18NException
     */
    RegistroEntrada registrarEntrada(RegistroEntrada registroEntrada, Entidad entidad, UsuarioEntidad usuarioEntidad, List<Interesado> interesados, List<AnexoFull> anexosFull, Boolean validarAnexos) throws I18NException, I18NValidationException;


    /**
     * Actualiza un Registro de entrada
     *
     * @param registroEntrada
     * @param usuarioEntidad
     * @return
     * @throws I18NException
     * @throws I18NException
     */
    RegistroEntrada actualizar(RegistroEntrada antiguo, RegistroEntrada registroEntrada, Entidad entidad, UsuarioEntidad usuarioEntidad) throws I18NException;

    /**
     * Comprueba si un RegistroEntrada se considera un OficioRemision interno o no
     *
     * @param idRegistro
     * @param organismos Lista con los Destinatarios que no se consideran Oficio de Remisión
     * @return
     * @throws I18NException
     */
    Boolean isOficioRemisionInterno(Long idRegistro, Set<Long> organismos) throws I18NException;

    /**
     * Comprueba si un RegistroEntrada se considera un OficioRemision externo o no
     *
     * @param idRegistro
     * @return
     * @throws I18NException
     */
    Boolean isOficioRemisionExterno(Long idRegistro) throws I18NException;

    /**
     * Comprueba si un RegistroEntrada se considera un OficioRemision SIR o no
     * @param idRegistro
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<OficinaTF> isOficioRemisionSir(Long idRegistro, Long idEntidad) throws I18NException;


    /**
     * Comprueba si un RegistroEntrada se considera un OficioRemision SIR o no en un entorno multientidad
     * @param idRegistro
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<OficinaTF> isOficioRemisionSirMultiEntidad(Long idRegistro, Long idEntidad) throws I18NException;


    /**
     * Obtiene el destino externo del registro de entrada de dir3caib
     *
     * @param idRegistro
     * @return
     * @throws I18NException
     */
    String obtenerDestinoExternoRE(Long idRegistro) throws I18NException;

    /**
     * Obtiene el próximo evento que habrá que realizar con el Registro
     *
     * @param registroEntrada
     * @param entidadActiva
     * @return
     * @throws I18NException
     */
    Long proximoEventoEntrada(RegistroEntrada registroEntrada, Entidad entidadActiva, Long idOficina) throws I18NException;


    /**
     * Obtiene el próximo evento que habrá que realizar con el Registro en un entorno multientidad
     *
     * @param registroEntrada
     * @param entidadActiva
     * @return
     * @throws I18NException
     */
    Long proximoEventoEntradaMultiEntidad(RegistroEntrada registroEntrada, Entidad entidadActiva, Long idOficina) throws I18NException;


    /**
     * Cambia el estado de un RegistroEntrada y el HistoricoModificación correspondiente
     *
     * @param registroEntrada
     * @param idEstado
     * @throws I18NException
     */
    void cambiarEstadoHistorico(RegistroEntrada registroEntrada, Long idEstado, UsuarioEntidad usuarioEntidad) throws I18NException;

    /**
     * Cambia el estado de un RegistroEntrada y el HistoricoModificación correspondiente
     *
     * @param registroEntrada
     * @param idEstado
     * @param observacionesAnulacion
     * @throws I18NException
     */
    void cambiarEstadoAnuladoHistorico(RegistroEntrada registroEntrada, Long idEstado, UsuarioEntidad usuarioEntidad, String observacionesAnulacion) throws I18NException;

    /**
     * Anula un RegistroEntrada, cambiandole el estado a anulado.
     *
     * @param registroEntrada
     * @param usuarioEntidad
     * @param observacionesAnulacion
     * @throws I18NException
     */
    void anularRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad, String observacionesAnulacion) throws I18NException;

    /**
     * Activa un RegistroEntrada, cambiandole el estado a anulado.
     *
     * @param registroEntrada
     * @param usuarioEntidad
     * @throws I18NException
     */
    void activarRegistroEntrada(RegistroEntrada registroEntrada, UsuarioEntidad usuarioEntidad) throws I18NException;

    /**
     * Visa un RegistroEntrada, cambiandole el estado a anulado.
     *
     * @param registroEntrada
     * @param usuarioEntidad
     * @throws I18NException
     */
    void visarRegistroEntrada(RegistroEntrada registroEntrada, Entidad entidad, UsuarioEntidad usuarioEntidad) throws I18NException;

    /**
     * Tramita un RegistroEntrada, creando el HistoricoEstado y Trazabilidad
     *
     * @param registroEntrada
     * @param descripcion
     * @throws I18NException
     */
    void marcarDistribuido(RegistroEntrada registroEntrada, String descripcion) throws I18NException;


    /**
     * Elimina los RegistroEntrada de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

    /**
     * Rectificar un Registro de entrada, creando una nuevo informando de ello
     *
     * @param registroEntrada
     * @param usuarioEntidad
     * @return
     * @throws I18NException
     */
    RegistroEntrada rectificar(RegistroEntrada registroEntrada, Entidad entidad, UsuarioEntidad usuarioEntidad) throws I18NException;

    /**
     * Actualiza el Destino extinguido por el que le sustituye
     *
     * @param idOrganismoExtinguido
     * @param idOrganismo
     * @throws I18NException
     */
    void actualizarDestinoExtinguido(Long idOrganismoExtinguido, Long idOrganismo) throws I18NException;

    /**
     * Actualiza el Destino externo extinguido de un Registro por el sustituto
     * @param idRegistro
     * @param destinoExternoCodigo
     * @param destinoExternoDenominacion
     * @throws I18NException
     */
    void actualizarDestinoExternoExtinguido(Long idRegistro, String destinoExternoCodigo, String destinoExternoDenominacion) throws I18NException;

    /**
     * Método que devuelve un registro de entrada completo, con los anexos completos
     *
     * @param id
     * @return
     * @throws I18NException
     * @throws I18NException
     */
    RegistroEntrada getConAnexosFull(Long id) throws I18NException;

    /**
     * Método que devuelve un registro de entrada completo, con anexoFull pero sin los documentos fisicos.
     *
     * @param id
     * @return
     * @throws I18NException
     * @throws I18NException
     */
    RegistroEntrada getConAnexosFullLigero(Long id) throws I18NException;

    /**
     * Obtiene el Registro para Distribuir, sin cargar el Justificante, porque solo se envía el custodyID
     *
     * @param id
     * @return
     * @throws I18NException
     * @throws I18NException
     */
    RegistroEntrada getConAnexosFullDistribuir(Long id) throws I18NException;

    /**
     * Método que invoca al plugin post proceso al actualizar un registro entrada.
     *
     * @param re
     * @return
     * @throws I18NException
     */
    void postProcesoActualizarRegistro(RegistroEntrada re, Long entidadId) throws I18NException;

    /**
     * @param re
     * @return
     * @throws I18NException
     */
    void postProcesoNuevoRegistro(RegistroEntrada re, Long entidadId) throws I18NException;

}
