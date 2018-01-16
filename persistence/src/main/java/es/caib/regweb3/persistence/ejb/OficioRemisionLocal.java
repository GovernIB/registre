package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.*;
import es.caib.regweb3.persistence.utils.Paginacion;
import org.fundaciobit.genapp.common.i18n.I18NException;
import org.fundaciobit.genapp.common.i18n.I18NValidationException;

import javax.annotation.security.RolesAllowed;
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
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface OficioRemisionLocal extends BaseEjb<OficioRemision, Long> {

    /**
     * Realizada una Búsqueda según los parámetros
     * @param pageNumber
     * @param usuario
     * @param fechaInicio
     * @param fechaFin
     * @param oficioRemision
     * @param libros
     * @param tipoOficioRemision
     * @return
     * @throws Exception
     */
    Paginacion busqueda(Integer pageNumber, Date fechaInicio, Date fechaFin, String usuario, OficioRemision oficioRemision, List<Libro> libros, Long tipoOficioRemision, Integer estadoOficioRemision, Long tipoRegistro, Boolean sir) throws Exception;

    /**
     * Registra un OficioRemision asignandole número
     * @param oficioRemision
     * @param estado
     * @return
     * @throws Exception
     */
    OficioRemision registrarOficioRemision(OficioRemision oficioRemision, Long estado)
        throws Exception, I18NException, I18NValidationException;

    /**
     * Anula un Oficio de Remisión
     * @param idOficioRemision
     * @param usuarioEntidad
     * @throws Exception
     */
    void anularOficioRemision(Long idOficioRemision, UsuarioEntidad usuarioEntidad) throws Exception;

    /**
     * Devuelve los Oficios de Remisión pendientes de procesar de los organismos seleccionados
     * @param organismos
     * @return
     * @throws Exception
     */
    List<OficioRemision> oficiosPendientesLlegada(Set<Organismo> organismos, Integer total) throws Exception;

    /**
     * Devuelve los Oficios de Remisión de los organismos seleccionados según su estado
     * @param organismos
     * @param oficioRemision
     * @return
     * @throws Exception
     */
    Paginacion oficiosBusqueda(Set<Organismo> organismos, Integer pageNumber, OficioRemision oficioRemision, Long tipoOficioRemision, int estado) throws Exception;

    /**
     * Devuelve los Oficios de Remisión pendientes de procesar de los organismos seleccionados
     * @param organismos
     * @return
     * @throws Exception
     */
    Long oficiosPendientesLlegadaCount(Set<Organismo> organismos) throws Exception;

    /**
     * Busca Registros de Entrada que pertenecen a un OficioRemision
     * @param idOficioRemision
     * @return
     * @throws Exception
     */
    List<RegistroEntrada> getEntradasByOficioRemision(Long idOficioRemision) throws Exception;

    /**
     * Obtiene los Oficios de Remisión de un determinado Estado y Entidad
     * @param idEstado
     * @param idOficina
     * @return
     * @throws Exception
     */
    List<OficioRemision> getByOficinaEstado(Long idOficina, int idEstado, int total) throws Exception;

    /**
     * Obtiene los Oficios de Remisión de un determinado Estado y Entidad
     * @param idEstado
     * @param idOficina
     * @return
     * @throws Exception
     */
    Long getByOficinaEstadoCount(Long idOficina, int idEstado) throws Exception;

    /**
     * Busca los Oficios de Remisión según su oficia origen y estado
     * @param pageNumber
     * @param idOficinaActiva
     * @param idEstado
     * @return
     * @throws Exception
     */
    Paginacion getByOficinaEstadoPaginado(Integer pageNumber, Long idOficinaActiva, Integer idEstado) throws Exception;

    /**
     * Obtiene los Oficios de Remisión que han de reintentar su envío al componente CIR
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<OficioRemision> getEnviadosSinAck(Long idEntidad) throws Exception;

    /**
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<OficioRemision> getEnviadosConError(Long idEntidad) throws Exception;

    /**
     * Busca Registros de Salida que pertenecen a un OficioRemision
     * @param idOficioRemision
     * @return
     * @throws Exception
     */
    List<RegistroSalida> getSalidasByOficioRemision(Long idOficioRemision) throws Exception;

    /**
     * Obtiene un OficioRemisionSir a partir del identificadorIntercambio del envío
     * @param identificadorIntercambio
     * @return
     * @throws Exception
     */
    OficioRemision getByIdentificadorIntercambio(String identificadorIntercambio, String codigoEntidadRegistralDestino) throws Exception;

    /**
     * Modifica el Estado de un OficioRemision
     * @param idOficioRemision
     * @param estado
     * @throws Exception
     */
    void modificarEstado(Long idOficioRemision, int estado) throws Exception;

    /**
     * Busca los Números de Registro Formateados de los Registros de Entrada que pertenecen a un OficioRemision
     * @param idOficioRemision
     * @return
     * @throws Exception
     */
    List<String> getNumerosRegistroEntradaFormateadoByOficioRemision(Long idOficioRemision) throws Exception;

    /**
     * Busca los Números de Registro Formateados de los Registros de Salida que pertenecen a un OficioRemision
     * @param idOficioRemision
     * @return
     * @throws Exception
     */
    List<String> getNumerosRegistroSalidaFormateadoByOficioRemision(Long idOficioRemision) throws Exception;

    /**
     * Elimina los Oficios de remisión de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;
}
