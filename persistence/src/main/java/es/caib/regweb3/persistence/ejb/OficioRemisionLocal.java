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
     * @throws Exception
     */
    Paginacion busqueda(Integer pageNumber, Long idOrganismo, Date fechaInicio, Date fechaFin, String usuario, OficioRemision oficioRemision, Long tipoOficioRemision, Integer estadoOficioRemision, Long tipoRegistro, Boolean sir) throws Exception;

    /**
     * Registra un OficioRemision asignandole número
     *
     * @param oficioRemision
     * @param estado
     * @return
     * @throws Exception
     */
    OficioRemision registrarOficioRemision(Entidad entidad, OficioRemision oficioRemision, Long estado) throws Exception, I18NException, I18NValidationException;

    /**
     * Anula un Oficio de Remisión
     *
     * @param idOficioRemision
     * @param usuarioEntidad
     * @throws Exception
     */
    void anularOficioRemision(Long idOficioRemision, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Devuelve los Oficios de Remisión pendientes de procesar de los organismos seleccionados
     *
     * @param organismos
     * @return
     * @throws Exception
     */
    List<OficioRemision> oficiosPendientesLlegada(Set<Organismo> organismos, Integer total) throws Exception;

    /**
     * Actualiza el destino extinguido de los Oficios pendientes de llegada por el Organismo que le sustituye
     *
     * @param idOrganismoExtinguido
     * @param idOrganismoSustituto
     * @throws Exception
     */
    void actualizarDestinoPendientesLlegada(Long idOrganismoExtinguido, Long idOrganismoSustituto) throws Exception;

    /**
     * Devuelve los Oficios de Remisión de los organismos seleccionados según su estado
     *
     * @param organismos
     * @param oficioRemision
     * @return
     * @throws Exception
     */
    Paginacion oficiosBusqueda(Set<Organismo> organismos, Integer pageNumber, OficioRemision oficioRemision, Long tipoOficioRemision, int estado) throws Exception;

    /**
     * Devuelve los Oficios de Remisión pendientes de procesar de los organismos seleccionados
     *
     * @param organismos
     * @return
     * @throws Exception
     */
    Long oficiosPendientesLlegadaCount(Set<Organismo> organismos) throws Exception;

    /**
     * Busca Registros de Entrada que pertenecen a un OficioRemision
     *
     * @param idOficioRemision
     * @return
     * @throws Exception
     */
    List<RegistroEntrada> getEntradasByOficioRemision(Long idOficioRemision) throws Exception;

    /**
     * Obtiene los Oficios de Remisión de un determinado Estado y Entidad
     *
     * @param idEstado
     * @param idOficina
     * @return
     * @throws Exception
     */
    List<OficioRemision> getByOficinaEstado(Long idOficina, int idEstado, int total) throws Exception;

    /**
     * Obtiene los Oficios de Remisión de un determinado Estado y Entidad
     *
     * @param idEstado
     * @param idOficina
     * @return
     * @throws Exception
     */
    Long getByOficinaEstadoCount(Long idOficina, int idEstado) throws Exception;

    /**
     * Busca los Oficios de Remisión según su oficia origen y estado
     *
     * @param pageNumber
     * @param idOficinaActiva
     * @param idEstado
     * @return
     * @throws Exception
     */
    Paginacion getByOficinaEstadoPaginado(Integer pageNumber, Long idOficinaActiva, Integer idEstado) throws Exception;

    /**
     * Obtiene los Oficios de Remisión que han de reintentar su envío al componente CIR
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Long> getEnviadosSinAck(Long idEntidad) throws Exception;

    /**
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Long> getEnviadosConError(Long idEntidad) throws Exception;

    /**
     * Oficios enviados o reenviados sin ACK y con el máx de reintentos acumulados
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<OficioRemision> getEnviadosSinAckMaxReintentos(Long idEntidad) throws Exception;

    /**
     * Oficios enviados o reenviados con ERROR y con el máx de reintentos acumulados
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<OficioRemision> getEnviadosErrorMaxReintentos(Long idEntidad) throws Exception;


    /**
     * Busca Registros de Salida que pertenecen a un OficioRemision
     *
     * @param idOficioRemision
     * @return
     * @throws Exception
     */
    List<RegistroSalida> getSalidasByOficioRemision(Long idOficioRemision) throws Exception;

    /**
     * Obtiene un OficioRemisionSir a partir del identificadorIntercambio del envío
     *
     * @param identificadorIntercambio
     * @return
     * @throws Exception
     */
    OficioRemision getByIdentificadorIntercambio(String identificadorIntercambio, String codigoEntidadRegistralDestino) throws Exception;

    /**
     * Obtiene un OficioRemisionSir a partir del identificadorIntercambio del envío en estado RECHAZADO
     *
     * @param identificadorIntercambio
     * @param codigoEntidadRegistralDestino
     * @return
     * @throws Exception
     */
    OficioRemision getBySirRechazado(String identificadorIntercambio, String codigoEntidadRegistralDestino) throws Exception;

    /**
     * Obtiene un OficioRemisionSir a partir del identificadorIntercambio del envío
     *
     * @param identificadorIntercambio
     * @return
     * @throws Exception
     */
    OficioRemision getByIdentificadorIntercambio(String identificadorIntercambio) throws Exception;

    /**
     * Modifica el Estado de un OficioRemision
     *
     * @param idOficioRemision
     * @param estado
     * @throws Exception
     */
    void modificarEstado(Long idOficioRemision, int estado) throws Exception;

    /**
     * Modifica el Estado de un {@link OficioRemision}, a uno con Error, incluyendo código y descripción del Error.
     * @param idOficioRemision
     * @param estado
     * @param codigoError
     * @param descripcionError
     * @throws Exception
     */
    void modificarEstadoError(Long idOficioRemision, int estado, String codigoError, String descripcionError) throws Exception;

    /**
     * Incrementa el contador de reintentos
     * @param idOficioRemision
     * @param reintentos
     * @throws Exception
     */
    void incrementarReintentos(Long idOficioRemision, Integer reintentos) throws Exception;

    /**
     * Reinicia el contador de reintentos SIR
     *
     * @param idOficioRemision
     * @throws Exception
     */
    void reiniciarIntentos(Long idOficioRemision) throws Exception;

    /**
     * Acepta un Oficio SIr, procesando el Mensaje de confirmación
     * @param idOficioRemision
     * @param codigoEntidadRegistralOrigen
     * @param decodificacionEntidadRegistralOrigen
     * @param numeroRegistroDestino
     * @param fechaRegistroDestino
     * @throws Exception
     */
    void aceptarOficioSir(Long idOficioRemision, String codigoEntidadRegistralOrigen, String decodificacionEntidadRegistralOrigen, String numeroRegistroDestino, Date fechaRegistroDestino) throws Exception;

    /**
     * Busca los Números de Registro Formateados de los Registros de Entrada que pertenecen a un OficioRemision
     *
     * @param idOficioRemision
     * @return
     * @throws Exception
     */
    List<String> getNumerosRegistroEntradaFormateadoByOficioRemision(Long idOficioRemision) throws Exception;

    /**
     * Busca los Números de Registro Formateados de los Registros de Salida que pertenecen a un OficioRemision
     *
     * @param idOficioRemision
     * @return
     * @throws Exception
     */
    List<String> getNumerosRegistroSalidaFormateadoByOficioRemision(Long idOficioRemision) throws Exception;

    /**
     * @param codigoOrganismo
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Oficio obtenerTipoOficio(String codigoOrganismo, Long idEntidad) throws Exception;

    /**
     * Elimina los Oficios de remisión de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Obtiene el oficio de Remisión a partir de un numero de registro formateado
     *
     * @param numeroRegistroFormateado
     * @return
     */
    OficioRemision getByNumeroRegistroFormateado(String numeroRegistroFormateado, String entidad) throws Exception;

    /**
     * Genera el pdf del oficio de remisión para obtenerlo via WS
     *
     * @param oficioRemision
     * @param modeloOficioRemision
     * @param registrosEntrada
     * @param registrosSalida
     * @return
     * @throws Exception
     */
    CombineStream generarOficioRemisionRtf(OficioRemision oficioRemision, ModeloOficioRemision modeloOficioRemision, List<String> registrosEntrada, List<String> registrosSalida) throws Exception;
}
