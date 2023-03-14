package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.RegistroBasico;
import es.caib.regweb3.persistence.utils.Paginacion;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 14/05/19
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
public interface RegistroEntradaConsultaLocal {


    Long findByNumeroRegistroOrigen(String numeroRegistroFormateado, Long idRegistro) throws Exception;

    /**
     * Obtiene un RegistroEntrada con un mínimo de campos
     * @param idRegistroEntrada
     * @return
     * @throws Exception
     */
    RegistroBasico findByIdLigero(Long idRegistroEntrada) throws Exception;

    /**
     * Busca los Registros de Entrada en función de los parámetros
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param registroEntrada
     * @return
     * @throws Exception
     */
    Paginacion busqueda(Integer pageNumber, List<Long> organismos, Date fechaInicio, Date fechaFin, RegistroEntrada registroEntrada, String interesadoNom, String interesadoDoc, String interesadoLli1, String interesadoLli2, String organoDest, String observaciones, String usuario, Long idEntidad, boolean incluirPendientesGeiser) throws Exception;

    /**
     * Busca los Registros de Entrada de una OficinaActiva en función de su estado.
     *
     * @param idOficina
     * @param idEstado
     * @return
     * @throws Exception
     */
    List<RegistroBasico> getByOficinaEstado(Long idOficina, Long idEstado, Integer total) throws Exception;

    /**
     *
     * @param pageNumber
     * @param idOficinaActiva
     * @param idEstado
     * @return
     * @throws Exception
     */
    Paginacion getByOficinaEstadoPaginado(Integer pageNumber, Long idOficinaActiva, Long idEstado) throws Exception;

    /**
     * Registros pendientes de distribuir
     * @param pageNumber
     * @param idOficinaActiva

     * @return
     * @throws Exception
     */
    Paginacion pendientesDistribuir(Integer pageNumber, Long idOficinaActiva) throws Exception;

    /**
     * Busca los Registros de Entrada de una OficinaActiva en función de su estado.
     *
     * @param idOficinaActiva
     * @param idEstado
     * @return
     * @throws Exception
     */
    Long getByOficinaEstadoCount(Long idOficinaActiva, Long idEstado) throws Exception;

    /**
     * Busca los Registros de Entrada de un listado de Libros en función de su estado.
     *
     * @param inicio
     * @param organismos
     * @param idEstado
     * @return
     * @throws Exception
     */
    List<RegistroEntrada> getByLibrosEstado(int inicio, List<Organismo> organismos, Long idEstado) throws Exception;

    /**
     * Busca los Registros de Entrada de un listado de Organismos en función de su estado.
     *
     * @param organismos
     * @param idEstado
     * @return
     * @throws Exception
     */
    Long getByLibrosEstadoCount(List<Organismo> organismos, Long idEstado) throws Exception;


    /**
     * Obtiene el RegistroEntrada a partir de su numero de registro formateado, entidad y el libro
     * @param codigoEntidad
     * @param numeroRegistroFormateado
     * @return
     * @throws Exception
     */
    RegistroEntrada findByNumeroRegistroFormateado(String codigoEntidad, String numeroRegistroFormateado) throws Exception;
    

    /**
     * Obtiene el RegistroEntrada a partir de su numero de registro y código entidad
     * @param codigoEntidad
     * @param numeroRegistroFormateado
     * @return
     * @throws Exception
     */
	RegistroEntrada findByNumeroRegistro(String codigoEntidad, String numeroRegistro) throws Exception;

    /**
     * Obtiene el RegistroEntrada a partir de su numero de registro formateado, entidad y el libro cargando sus anexos.
     * @param codigoEntidad
     * @param numeroRegistroFormateado
     * @return
     * @throws Exception
     * @throws I18NException
     */
    RegistroEntrada findByNumeroRegistroFormateadoCompleto(String codigoEntidad, String numeroRegistroFormateado) throws Exception, I18NException;

    /**
     * Obtiene el numero de registro formateado de un RegistroEntrada
     * a partir de su numero de registro, año y libro.
     *
     * @param numero
     * @param anyo
     * @param libro
     * @return
     * @throws Exception
     */
    RegistroEntrada findByNumeroAnyoLibro(String numero, int anyo, String libro) throws Exception;

    /**
     * Devuelve el numero de registro formateado a partir de un registro detalle.
     * Se necesita para el plug-in postproceso para pasarselo cuando creamos interesados.
     *
     * @param idRegistroDetalle
     * @return
     * @throws Exception
     */
    String findNumeroRegistroFormateadoByRegistroDetalle(Long idRegistroDetalle) throws Exception;

    /**
     * Devuelve el id del registro de entrada a partir de un registro detalle.
     *
     * @param idRegistroDetalle
     * @return
     * @throws Exception
     */
    Long findIdByRegistroDetalle(Long idRegistroDetalle) throws Exception;
    

    /**
     * Retorna el identificador del Libro al que pertenece el RegistroEntrada
     *
     * @param idRegistroEntrada
     * @return
     * @throws Exception
     */
    Long getLibro(Long idRegistroEntrada) throws Exception;

    /**
     * Retorna el Organismo al que pertenece el RegistroEntrada
     * @param idRegistroEntrada
     * @return
     * @throws Exception
     */
    Organismo getOrganismo(Long idRegistroEntrada) throws Exception;

    /**
     * Comprueba si un Registro de Entrada se puede tramitar o no
     *
     * @param idRegistro
     * @return
     * @throws Exception
     */
    Boolean isDistribuir(Long idRegistro) throws Exception;

    /**
     *
     * @param query
     * @return
     * @throws Exception
     */
    Long queryCount(String query) throws Exception;

    /**
     * Busca los Registros de Entrada de un Libro.
     *
     * @param idLibro
     * @return
     * @throws Exception
     */
    Long getTotalByLibro(Long idLibro) throws Exception;

    /**
     * Comprueba si un usuario tiene RegistroEntrada
     *
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws Exception;

    /**
     * Comprueba si un RegistroEntrada tiene un Estado en concreto
     *
     * @param idRegistroEntrada
     * @param idEstado
     * @return
     * @throws Exception
     */
    Boolean tieneEstado(Long idRegistroEntrada, Long idEstado) throws Exception;

    /**
     * Obtiene un Registro de Entrada a partir de su IdentificadorIntercambio
     *
     * @param identificadorIntercambio
     * @return
     * @throws Exception
     */
    RegistroEntrada getByIdentificadorIntercambio(String identificadorIntercambio) throws Exception;


    /**
     *
     * @param pageNumber
     * @param idOficina
     * @return
     * @throws Exception
     */
    Paginacion getSirRechazadosReenviadosPaginado(Integer pageNumber, Long idOficina) throws Exception;

    /**
     * Busca los Registros de Entrada de una OficinaActiva Rechazados o Reenviados por SIR
     * @param idOficina
     * @param total
     * @return
     * @throws Exception
     */
    List<RegistroEntrada> getSirRechazadosReenviados(Long idOficina, Integer total) throws Exception;

    /**
     * Total de Registros de Entrada de una OficinaActiva Rechazados o Reenviados por SIR
     * @param idOficina
     * @return
     * @throws Exception
     */
    Long getSirRechazadosReenviadosCount(Long idOficina) throws Exception;


    /**
     * Obtiene los registros de un ciudadano
     * @param idEntidad
     * @param documento
     * @return
     * @throws Exception
     */
    List<RegistroEntrada> getByDocumento(Long idEntidad, String documento) throws Exception;

    /**
     * Obtiene los registros de un ciudadano, paginados
     * @param idEntidad
     * @param documento
     * @param pageNumber
     * @return
     * @throws Exception
     */
    Paginacion getByDocumento(Long idEntidad, String documento, Integer pageNumber, Date fechaInicio, Date fechaFin, String numeroRegistroFormateado, List<Integer> estados, String extracto, Integer resultPorPagina) throws Exception;

    /**
     * Obtiene un Registro a partir de su número y el documento del Ciudadano
     * @param idEntidad
     * @param documento
     * @param numeroRegistroFormateado
     * @return
     * @throws Exception
     */
    RegistroEntrada getByDocumentoNumeroRegistro(Long idEntidad, String documento, String numeroRegistroFormateado) throws Exception, I18NException;

    /**
     * Obtiene los últimos organismos interesado de los registros realizados por el usuario indicado
     * @param usuarioEntidad
     * @return
     * @throws Exception
     */
    List<Organismo> ultimosOrganismosRegistro(UsuarioEntidad usuarioEntidad) throws Exception;

	List<String> obtenerRegistrosSirFinalizados(Long idEntidad);

}
