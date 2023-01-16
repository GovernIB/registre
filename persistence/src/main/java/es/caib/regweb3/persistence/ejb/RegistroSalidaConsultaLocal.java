package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Organismo;
import es.caib.regweb3.model.RegistroSalida;
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
 * Date: 16/01/14
 */
@Local
public interface RegistroSalidaConsultaLocal {

    String JNDI_NAME = "java:app/regweb3-persistence/RegistroSalidaConsultaEJB";


    /**
     * Obtiene un RegistroSalida con un mínimo de campos
     *
     * @param idRegistroSalida
     * @return
     * @throws I18NException
     */
    RegistroBasico findByIdLigero(Long idRegistroSalida) throws I18NException;


    /**
     * Busca los Registros de Salida en función de los parametros
     *
     * @param pageNumber
     * @param registroSalida
     * @return
     * @throws I18NException
     */
    Paginacion busqueda(Integer pageNumber, List<Long> organismos, Date fechaInicio, Date fechaFin, RegistroSalida registroSalida, String interesadoNom, String interesadoDoc, String interesadoLli1, String interesadoLli2, String observaciones, Long idUsuario, Long idEntidad) throws I18NException;


    /**
     * Busca los Registros de Salida en función de los parametros
     *
     * @param numero
     * @param anyo
     * @param libro
     * @return
     * @throws I18NException
     */
    RegistroSalida findByNumeroAnyoLibro(int numero, int anyo, String libro) throws I18NException;

    /**
     * Obtiene el RegistroSalida a partir de su numero de registro formateado, la entidad y el libro.
     *
     * @param numeroRegistroFormateado
     * @return
     * @throws I18NException
     */
    RegistroSalida findByNumeroRegistroFormateado(Long idEntidad, String numeroRegistroFormateado) throws I18NException;


    /**
     * Obtiene el RegistroSalida a partir de su numero de registro formateado, la entidad y el libro y carga los anexos
     *
     * @param idEntidad
     * @param numeroRegistroFormateado
     * @return
     * @throws I18NException
     * @throws I18NException
     */
    RegistroSalida findByNumeroRegistroFormateadoCompleto(Long idEntidad, String numeroRegistroFormateado) throws I18NException;


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
     * Retorna el identificador del Libro al que pertenece el RegistroSalida
     *
     * @param idRegistroSalida
     * @return
     * @throws I18NException
     */
    Long getLibro(Long idRegistroSalida) throws I18NException;

    /**
     * Retorna el Organismo al que pertenece el RegistroSalida
     *
     * @param idRegistroSalida
     * @return
     * @throws I18NException
     */
    Organismo getOrganismo(Long idRegistroSalida) throws I18NException;

    /**
     * Busca los Registros de Salida de un listado de Libros en función de su estado.
     *
     * @param organismos
     * @param idEstado
     * @return
     * @throws I18NException
     */
    Long getByLibrosEstadoCount(List<Organismo> organismos, Long idEstado) throws I18NException;

    /**
     * Busca los Registros de Salida de un listado de Organismos en función de su estado.
     *
     * @param inicio
     * @param organismos
     * @param idEstado
     * @return
     * @throws I18NException
     */
    List<RegistroSalida> getByLibrosEstado(int inicio, List<Organismo> organismos, Long idEstado) throws I18NException;

    /**
     * Busca los Registros de Salida de un Libro.
     *
     * @param idLibro
     * @return
     * @throws I18NException
     */
    Long getTotalByLibro(Long idLibro) throws I18NException;

    /**
     * Comprueba si un usuario tiene RegistroSalida
     *
     * @param idUsuarioEntidad
     * @return
     * @throws I18NException
     */
    Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws I18NException;

    /**
     * @param pageNumber
     * @param idEntidad
     * @param idOficina
     * @return
     * @throws I18NException
     */
    Paginacion getSirRechazadosReenviadosPaginado(Integer pageNumber, Long idEntidad, Long idOficina) throws I18NException;

    /**
     * Busca los Registros de Salida de una OficinaActiva Rechazados o Reenviados por SIR
     *
     * @param idOficina
     * @param total
     * @return
     * @throws I18NException
     */
    List<RegistroSalida> getSirRechazadosReenviados(Long idEntidad, Long idOficina, Integer total) throws I18NException;

    /**
     * Total de Registros de Salida de una OficinaActiva Rechazados o Reenviados por SIR
     *
     * @param idOficina
     * @return
     * @throws I18NException
     */
    Long getSirRechazadosReenviadosCount(Long idOficina) throws I18NException;

    /**
     * @param idEntidad
     * @param documento
     * @return
     * @throws I18NException
     */
    List<RegistroSalida> getByDocumento(Long idEntidad, String documento) throws I18NException;

    /**
     * @param query
     * @return
     * @throws I18NException
     */
    Long queryCount(String query) throws I18NException;

    /**
     * Obtiene los últimos organismos destinatario de los registros realizados por el usuario indicado
     *
     * @param usuarioEntidad
     * @return
     * @throws I18NException
     */
    List<Organismo> ultimosOrganismosRegistro(UsuarioEntidad usuarioEntidad) throws I18NException;

}
