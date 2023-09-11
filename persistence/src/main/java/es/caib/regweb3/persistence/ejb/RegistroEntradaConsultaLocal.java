package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.model.utils.RegistroBasico;
import es.caib.regweb3.persistence.utils.Paginacion;
import org.fundaciobit.genapp.common.i18n.I18NException;

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
public interface RegistroEntradaConsultaLocal {

    String JNDI_NAME = "java:app/regweb3-persistence/RegistroEntradaConsultaEJB";


    Long findByNumeroRegistroOrigen(String numeroRegistroFormateado, Long idRegistro) throws I18NException;

    /**
     * Obtiene un RegistroEntrada con un mínimo de campos
     * @param idRegistroEntrada
     * @return
     * @throws I18NException
     */
    RegistroBasico findByIdLigero(Long idRegistroEntrada) throws I18NException;

    /**
     * Busca los Registros de Entrada en función de los parámetros
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param registroEntrada
     * @return
     * @throws I18NException
     */
    Paginacion busqueda(Integer pageNumber, List<Long> organismos, Date fechaInicio, Date fechaFin, RegistroEntrada registroEntrada, String interesadoNom, String interesadoDoc, String interesadoLli1, String interesadoLli2, String organoDest, String observaciones, Long idUsuario, Long idEntidad) throws I18NException;

    /**
     * Busca los Registros de Entrada de una OficinaActiva en función de su estado.
     *
     * @param idOficina
     * @param idEstado
     * @return
     * @throws I18NException
     */
    List<RegistroBasico> getByOficinaEstado(Long idOficina, Long idEstado, Integer total) throws I18NException;

    /**
     *
     * @param pageNumber
     * @param idOficinaActiva
     * @param idEstado
     * @return
     * @throws I18NException
     */
    Paginacion getByOficinaEstadoPaginado(Integer pageNumber, Long idEntidad, Long idOficinaActiva, Long idEstado) throws I18NException;

    /**
     * Registros pendientes de distribuir
     * @param pageNumber
     * @param idEntidad
     * @param idOficinaActiva

     * @return
     * @throws I18NException
     */
    Paginacion pendientesDistribuir(Integer pageNumber, Long idEntidad, Long idOficinaActiva) throws I18NException;

    /**
     * Busca los Registros de Entrada de una OficinaActiva en función de su estado.
     *
     * @param idOficinaActiva
     * @param idEstado
     * @return
     * @throws I18NException
     */
    Long getByOficinaEstadoCount(Long idOficinaActiva, Long idEstado) throws I18NException;

    /**
     * Busca los Registros de Entrada de un listado de Libros en función de su estado.
     *
     * @param inicio
     * @param organismos
     * @param idEstado
     * @return
     * @throws I18NException
     */
    List<RegistroEntrada> getByLibrosEstado(int inicio, List<Organismo> organismos, Long idEstado) throws I18NException;

    /**
     * Busca los Registros de Entrada de un listado de Organismos en función de su estado.
     *
     * @param organismos
     * @param idEstado
     * @return
     * @throws I18NException
     */
    Long getByLibrosEstadoCount(List<Organismo> organismos, Long idEstado) throws I18NException;


    /**
     * Obtiene el RegistroEntrada a partir de su numero de registro formateado, entidad y el libro
     * @param idEntidad
     * @param numeroRegistroFormateado
     * @return
     * @throws I18NException
     */
    RegistroEntrada findByNumeroRegistroFormateado(Long idEntidad, String numeroRegistroFormateado) throws I18NException;

    /**
     * Obtiene el RegistroEntrada a partir de su numero de registro formateado, entidad y el libro cargando sus anexos.
     * @param idEntidad
     * @param numeroRegistroFormateado
     * @return
     * @throws I18NException
     * @throws I18NException
     */
    RegistroEntrada findByNumeroRegistroFormateadoCompleto(Long idEntidad, String numeroRegistroFormateado) throws I18NException;

    /**
     * Obtiene el numero de registro formateado de un RegistroEntrada
     * a partir de su numero de registro, año y libro.
     *
     * @param numero
     * @param anyo
     * @param libro
     * @return
     * @throws I18NException
     */
    RegistroEntrada findByNumeroAnyoLibro(int numero, int anyo, String libro) throws I18NException;

    /**
     * Devuelve el numero de registro formateado a partir de un registro detalle.
     * Se necesita para el plug-in postproceso para pasarselo cuando creamos interesados.
     *
     * @param idRegistroDetalle
     * @return
     * @throws I18NException
     */
    String findNumeroRegistroFormateadoByRegistroDetalle(Long idRegistroDetalle) throws I18NException;


    /**
     * Retorna el identificador del Libro al que pertenece el RegistroEntrada
     *
     * @param idRegistroEntrada
     * @return
     * @throws I18NException
     */
    Long getLibro(Long idRegistroEntrada) throws I18NException;

    /**
     * Retorna el Organismo al que pertenece el RegistroEntrada
     * @param idRegistroEntrada
     * @return
     * @throws I18NException
     */
    Organismo getOrganismo(Long idRegistroEntrada) throws I18NException;

    /**
     * Comprueba si un Registro de Entrada se puede tramitar o no
     *
     * @param idRegistro
     * @return
     * @throws I18NException
     */
    Boolean isDistribuir(Long idRegistro) throws I18NException;

    /**
     *
     * @param query
     * @return
     * @throws I18NException
     */
    Long queryCount(String query) throws I18NException;

    /**
     * Busca los Registros de Entrada de un Libro.
     *
     * @param idLibro
     * @return
     * @throws I18NException
     */
    Long getTotalByLibro(Long idLibro) throws I18NException;

    /**
     * Comprueba si un usuario tiene RegistroEntrada
     *
     * @param idUsuarioEntidad
     * @return
     * @throws I18NException
     */
    Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws I18NException;

    /**
     * Comprueba si un RegistroEntrada tiene un Estado en concreto
     *
     * @param idRegistroEntrada
     * @param idEstado
     * @return
     * @throws I18NException
     */
    Boolean tieneEstado(Long idRegistroEntrada, Long idEstado) throws I18NException;

    /**
     * Obtiene un Registro de Entrada a partir de su IdentificadorIntercambio
     *
     * @param identificadorIntercambio
     * @return
     * @throws I18NException
     */
    RegistroEntrada getByIdentificadorIntercambio(String identificadorIntercambio) throws I18NException;


    /**
     *
     * @param pageNumber
     * @param idEntidad
     * @param idOficina
     * @return
     * @throws I18NException
     */
    Paginacion getSirRechazadosReenviadosPaginado(Integer pageNumber, Long idEntidad, Long idOficina) throws I18NException;

    /**
     * Busca los Registros de Entrada de una OficinaActiva Rechazados o Reenviados por SIR
     * @param idOficina
     * @param total
     * @return
     * @throws I18NException
     */
    List<RegistroEntrada> getSirRechazadosReenviados(Long idEntidad, Long idOficina, Integer total) throws I18NException;

    /**
     * Total de Registros de Entrada de una OficinaActiva Rechazados o Reenviados por SIR
     * @param idOficina
     * @return
     * @throws I18NException
     */
    Long getSirRechazadosReenviadosCount(Long idOficina) throws I18NException;


    /**
     * Obtiene los registros de un ciudadano
     * @param idEntidad
     * @param documento
     * @return
     * @throws I18NException
     */
    List<RegistroEntrada> getByDocumento(Long idEntidad, String documento) throws I18NException;

    /**
     * Obtiene los registros de un ciudadano, paginados
     * @param idEntidad
     * @param documento
     * @param pageNumber
     * @return
     * @throws I18NException
     */
    Paginacion getByDocumento(Long idEntidad, String documento, Integer pageNumber, Date fechaInicio, Date fechaFin, String numeroRegistroFormateado, List<Integer> estados, String extracto, Integer resultPorPagina) throws I18NException;

    /**
     * Obtiene un Registro a partir de su número y el documento del Ciudadano
     * @param idEntidad
     * @param documento
     * @param numeroRegistroFormateado
     * @return
     * @throws I18NException
     */
    RegistroEntrada getByDocumentoNumeroRegistro(Long idEntidad, String documento, String numeroRegistroFormateado) throws I18NException;

    /**
     * Obtiene los últimos organismos interesado de los registros realizados por el usuario indicado
     * @param usuarioEntidad
     * @return
     * @throws I18NException
     */
    List<Organismo> ultimosOrganismosRegistro(UsuarioEntidad usuarioEntidad) throws I18NException;

    /**
     * Obtiene los Registros de Entrada válidos con una antigüedad de X días para distribuir automáticamente
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<RegistroEntrada> getDistribucionAutomatica(Long idEntidad) throws I18NException;

}
