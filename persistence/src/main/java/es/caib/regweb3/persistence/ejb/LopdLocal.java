package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.Lopd;
import es.caib.regweb3.model.UsuarioEntidad;
import es.caib.regweb3.persistence.utils.Paginacion;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 02/10/14
 */
@Local
public interface LopdLocal extends BaseEjb<Lopd, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/LopdEJB";


    /**
     * Devuelve los registros Lopd entre dos fechas para un Usuario en concreto y una lista de Libros, con la Accion (listado/consulta) y el TipoRegistro (entrada/salida)
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuarioEntidad
     * @param libros
     * @param accion
     * @param tipoRegistro
     * @return
     * @throws I18NException
     */
    Paginacion getByFechasUsuario(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuarioEntidad, List<Libro> libros, Long accion, Long tipoRegistro) throws I18NException;

    /**
     * Devuelve los registros Lopd entre dos fechas para un Usuario en concreto y un Libro en concreto, con la Accion (listado/consulta) y el TipoRegistro (entrada/salida)
     *
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuarioEntidad
     * @param idLibro
     * @param accion
     * @param tipoRegistro
     * @param pageNumber
     * @return
     * @throws I18NException
     */
    Paginacion getByFechasUsuarioLibro(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuarioEntidad, Long idLibro, Long accion, Long tipoRegistro) throws I18NException;

    /**
     * Devuelve los registros Lopd de un Registro concreto, pasando el Tipo de Registro y la Accion
     *
     * @param anyoRegistro
     * @param numRegistro
     * @param idLibro
     * @param accion
     * @param tipoRegistro
     * @return
     * @throws I18NException
     */
    List<Lopd> getByRegistro(String anyoRegistro, Integer numRegistro, Long idLibro, Long accion, Long tipoRegistro) throws I18NException;

    /**
     * Realiza una Alta en las tablas de Lopd
     *
     * @param numeroRegistro
     * @param fecha
     * @param idLibro
     * @param idUsuarioEntidad
     * @param tipoRegistro
     * @param accion
     * @throws I18NException
     */
    void altaLopd(Integer numeroRegistro, Date fecha, Long idLibro, Long idUsuarioEntidad, Long tipoRegistro, Long accion) throws I18NException;

    /**
     * Inserta la búsqueda de Registros en las tablas de Lopd
     * @param paginacion
     * @param usuarioEntidad
     * @throws I18NException
     */
    void insertarRegistros(Paginacion paginacion, UsuarioEntidad usuarioEntidad, Libro libro, Long tipoRegistro, Long accion) throws I18NException;

    /**
     * Comprueba si un usuario tiene Lopd
     *
     * @param idUsuarioEntidad
     * @return
     * @throws I18NException
     */
    Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws I18NException;

    /**
     * Elimina las Lopd de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

    /**
     * Busca los Registros de Entrada que no estén pendientes en función de la una fecha inicio, una fecha fin, por Usuario y Libro
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param idLibro
     * @return
     * @throws I18NException
     */
    Paginacion buscaEntradaPorUsuarioLibro(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws I18NException;

    /**
     * Busca los Registros de Entrada Modificada que no sean de 'Creación' en función de la una fecha inicio, una fecha fin, por Usuario y Libro
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param idLibro
     * @return
     * @throws I18NException
     */
    Paginacion entradaModificadaPorUsuarioLibro(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws I18NException;

    /**
     * Busca los Registros de Salida que no estén pendientes en función de la una fecha inicio, una fecha fin, por Usuario y Libro
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param idLibro
     * @return
     * @throws I18NException
     */
    Paginacion buscaSalidaPorUsuarioLibro(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws I18NException;

    /**
     * Busca los Registros de Salida Modificada que no sean de 'Creación' en función de la una fecha inicio, una fecha fin, por Usuario y Libro
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param idLibro
     * @return
     * @throws I18NException
     */
    Paginacion salidaModificadaPorUsuarioLibro(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws I18NException;

    /**
     * Busca los Registros de Entrada en función de la una fecha inicio, una fecha fin, por Usuario en los Libros gestionados
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param libros
     * @return
     * @throws I18NException
     */
    Paginacion buscaEntradaPorUsuario(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws I18NException;

    /**
     * Busca los Registros de Entrada Modificada en función de la una fecha inicio, una fecha fin, por Usuario y Libros gestionados
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param libros
     * @return
     * @throws I18NException
     */
    Paginacion entradaModificadaPorUsuario(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws I18NException;

    /**
     * Busca los Registros de Salida en función de la una fecha inicio, una fecha fin, por Usuario y Libros gestionados
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param libros
     * @return
     * @throws I18NException
     */
    Paginacion buscaSalidaPorUsuario(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws I18NException;

    /**
     * Busca los Registros de Salida Modificada en función de la una fecha inicio, una fecha fin, por Usuario y Libros gestionados
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param libros
     * @return
     * @throws I18NException
     */
    Paginacion salidaModificadaPorUsuario(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws I18NException;

    /**
     * Devuelve los registros Lopd de un Registro Migrado que ha realizado un Usuario entre dos fechas, pasando la Accion
     *
     * @param pageNumber
     * @param dataInici
     * @param dataFi
     * @param usuario
     * @param accion
     * @return
     * @throws I18NException
     */
    Paginacion getByUsuario(Integer pageNumber, final Integer resultsPerPage, Date dataInici, Date dataFi, String usuario, String accion) throws I18NException;

    /**
     * Busca los Registros de Entrada en función de la una fecha inicio, una fecha fin, Libro y Número de registro
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param idLibro
     * @param numeroRegistro
     * @return
     * @throws I18NException
     */
    Paginacion buscaEntradasPorLibroTipoNumero(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idLibro, Integer numeroRegistro) throws I18NException;

    /**
     * Busca los Registros de Salida en función de la una fecha inicio, una fecha fin, Libro y Número de registro
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param idLibro
     * @param numeroRegistro
     * @return
     * @throws I18NException
     */
    Paginacion buscaSalidasPorLibroTipoNumero(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idLibro, Integer numeroRegistro) throws I18NException;


}
