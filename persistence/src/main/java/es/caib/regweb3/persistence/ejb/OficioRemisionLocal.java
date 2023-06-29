package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.utils.Oficio;
import es.caib.regweb3.persistence.utils.Paginacion;
import es.caib.regweb3.utils.CombineStream;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.ejb.Local;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface OficioRemisionLocal extends BaseEjb<OficioRemision, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/OficioRemisionEJB";


    /**
     * Realizada una Búsqueda según los parámetros
     *
     * @param pageNumber
     * @param idOrganismo
     * @param fechaInicio
     * @param fechaFin
     * @param oficioRemision
     * @param tipoOficioRemision
     * @return
     * @throws I18NException
     */
    Paginacion busqueda(Integer pageNumber, Long idOrganismo, Date fechaInicio, Date fechaFin, String usuario, OficioRemision oficioRemision, Long tipoOficioRemision, Integer estadoOficioRemision, Long tipoRegistro, Boolean sir, Long idEntidad) throws I18NException;

    /**
     * Registra un OficioRemision asignandole número
     *
     * @param oficioRemision
     * @param estado
     * @return
     * @throws I18NException
     */
    OficioRemision registrarOficioRemision(Entidad entidad, OficioRemision oficioRemision, Long estado) throws I18NException, I18NValidationException;

    /**
     * Anula un Oficio de Remisión
     *
     * @param idOficioRemision
     * @param usuarioEntidad
     * @throws I18NException
     */
    void anularOficioRemision(Long idOficioRemision, UsuarioEntidad usuarioEntidad) throws I18NException;

    /**
     * Devuelve los Oficios de Remisión pendientes de procesar de los organismos seleccionados
     *
     * @param organismos
     * @return
     * @throws I18NException
     */
    List<OficioRemision> oficiosPendientesLlegada(Set<Organismo> organismos, Integer total) throws I18NException;

    /**
     * Actualiza el destino extinguido de los Oficios pendientes de llegada por el Organismo que le sustituye
     *
     * @param idOrganismoExtinguido
     * @param idOrganismoSustituto
     * @throws I18NException
     */
    void actualizarDestinoPendientesLlegada(Long idOrganismoExtinguido, Long idOrganismoSustituto) throws I18NException;

    /**
     * Devuelve los Oficios de Remisión de los organismos seleccionados según su estado
     *
     * @param organismos
     * @param oficioRemision
     * @return
     * @throws I18NException
     */
    Paginacion oficiosBusqueda(Set<Organismo> organismos, Integer pageNumber, OficioRemision oficioRemision, Long tipoOficioRemision, int estado) throws I18NException;

    /**
     * Devuelve los Oficios de Remisión pendientes de procesar de los organismos seleccionados
     *
     * @param organismos
     * @return
     * @throws I18NException
     */
    Long oficiosPendientesLlegadaCount(Set<Organismo> organismos) throws I18NException;

    /**
     * Busca Registros de Entrada que pertenecen a un OficioRemision
     *
     * @param idOficioRemision
     * @return
     * @throws I18NException
     */
    List<RegistroEntrada> getEntradasByOficioRemision(Long idOficioRemision) throws I18NException;

    /**
     * Obtiene los Oficios de Remisión de un determinado Estado y Entidad
     *
     * @param idEstado
     * @param idOficina
     * @return
     * @throws I18NException
     */
    List<OficioRemision> getByOficinaEstado(Long idOficina, int idEstado, int total) throws I18NException;

    /**
     * Obtiene los Oficios de Remisión de un determinado Estado y Entidad
     *
     * @param idEstado
     * @param idOficina
     * @return
     * @throws I18NException
     */
    Long getByOficinaEstadoCount(Long idOficina, int idEstado) throws I18NException;

    /**
     * Busca los Oficios de Remisión según su oficia origen y estado
     *
     * @param pageNumber
     * @param idOficinaActiva
     * @param idEstado
     * @return
     * @throws I18NException
     */
    Paginacion getByOficinaEstadoPaginado(Integer pageNumber, Long idOficinaActiva, Integer idEstado) throws I18NException;

    /**
     * Obtiene los Oficios de Remisión que han de reintentar su envío al componente CIR
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Long> getEnviadosSinAck(Long idEntidad) throws I18NException;

    /**
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Long> getEnviadosConError(Long idEntidad) throws I18NException;

    /**
     * Oficios enviados o reenviados sin ACK y con el máx de reintentos acumulados
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<OficioRemision> getEnviadosSinAckMaxReintentos(Long idEntidad) throws I18NException;

    /**
     * Oficios enviados o reenviados con ERROR y con el máx de reintentos acumulados
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<OficioRemision> getEnviadosErrorMaxReintentos(Long idEntidad) throws I18NException;


    /**
     * Busca Registros de Salida que pertenecen a un OficioRemision
     *
     * @param idOficioRemision
     * @return
     * @throws I18NException
     */
    List<RegistroSalida> getSalidasByOficioRemision(Long idOficioRemision) throws I18NException;

    /**
     * Obtiene un OficioRemisionSir a partir del identificadorIntercambio del envío
     *
     * @param identificadorIntercambio
     * @return
     * @throws I18NException
     */
    OficioRemision getByIdentificadorIntercambio(String identificadorIntercambio, String codigoEntidadRegistralDestino) throws I18NException;

    /**
     * Obtiene un OficioRemisionSir a partir del identificadorIntercambio del envío en estado RECHAZADO
     *
     * @param identificadorIntercambio
     * @param codigoEntidadRegistralDestino
     * @return
     * @throws I18NException
     */
    OficioRemision getBySirRechazado(String identificadorIntercambio, String codigoEntidadRegistralDestino) throws I18NException;

    /**
     * Obtiene un OficioRemisionSir a partir del identificadorIntercambio del envío
     *
     * @param identificadorIntercambio
     * @return
     * @throws I18NException
     */
    OficioRemision getByIdentificadorIntercambio(String identificadorIntercambio) throws I18NException;

    /**
     * Modifica el Estado de un OficioRemision
     *
     * @param idOficioRemision
     * @param estado
     * @throws I18NException
     */
    void modificarEstado(Long idOficioRemision, int estado) throws I18NException;

    /**
     * Modifica el Estado de un {@link OficioRemision}, a uno con Error, incluyendo código y descripción del Error.
     * @param idOficioRemision
     * @param estado
     * @param codigoError
     * @param descripcionError
     * @throws I18NException
     */
    void modificarEstadoError(Long idOficioRemision, int estado, String codigoError, String descripcionError) throws I18NException;

    /**
     * Incrementa el contador de reintentos
     * @param idOficioRemision
     * @param reintentos
     * @throws I18NException
     */
    void incrementarReintentos(Long idOficioRemision, Integer reintentos) throws I18NException;

    /**
     * Reinicia el contador de reintentos SIR
     *
     * @param idOficioRemision
     * @throws I18NException
     */
    void reiniciarIntentos(Long idOficioRemision) throws I18NException;

    /**
     * Acepta un Oficio SIr, procesando el Mensaje de confirmación
     * @param oficio
     * @param codigoEntidadRegistralOrigen
     * @param decodificacionEntidadRegistralOrigen
     * @param numeroRegistroDestino
     * @param fechaRegistroDestino
     * @throws I18NException
     */
    void aceptarOficioSir(OficioRemision oficio, String codigoEntidadRegistralOrigen, String decodificacionEntidadRegistralOrigen, String numeroRegistroDestino, Date fechaRegistroDestino) throws I18NException;

    /**
     * Busca los Números de Registro Formateados de los Registros de Entrada que pertenecen a un OficioRemision
     *
     * @param idOficioRemision
     * @return
     * @throws I18NException
     */
    List<String> getNumerosRegistroEntradaFormateadoByOficioRemision(Long idOficioRemision) throws I18NException;

    /**
     * Busca los Números de Registro Formateados de los Registros de Salida que pertenecen a un OficioRemision
     *
     * @param idOficioRemision
     * @return
     * @throws I18NException
     */
    List<String> getNumerosRegistroSalidaFormateadoByOficioRemision(Long idOficioRemision) throws I18NException;

    /**
     * @param codigoOrganismo
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Oficio obtenerTipoOficio(String codigoOrganismo, Long idEntidad) throws I18NException;

    /**
     * Elimina los Oficios de remisión de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

    /**
     * Obtiene el oficio de Remisión a partir de un numero de registro formateado
     *
     * @param numeroRegistroFormateado
     * @param idEntidad
     * @return
     */
    OficioRemision getByNumeroRegistroFormateado(String numeroRegistroFormateado, Long idEntidad) throws I18NException;

    /**
     * Genera el pdf del oficio de remisión para obtenerlo via WS
     *
     * @param oficioRemision
     * @param modeloOficioRemision
     * @param registrosEntrada
     * @param registrosSalida
     * @return
     * @throws I18NException
     */
    CombineStream generarOficioRemisionRtf(OficioRemision oficioRemision, ModeloOficioRemision modeloOficioRemision, List<String> registrosEntrada, List<String> registrosSalida) throws I18NException;
}
