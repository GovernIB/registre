package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.Lopd;
import es.caib.regweb3.persistence.utils.Paginacion;

import javax.annotation.security.RolesAllowed;
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
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA", "RWE_WS_CIUDADANO"})
public interface LopdLocal extends BaseEjb<Lopd, Long> {

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
     * @throws Exception
     */
    Paginacion getByFechasUsuario(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuarioEntidad, List<Libro> libros, Long accion, Long tipoRegistro) throws Exception;

    /**
     * Devuelve los registros Lopd entre dos fechas para un Usuario en concreto y un Libro en concreto, con la Accion (listado/consulta) y el TipoRegistro (entrada/salida)
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuarioEntidad
     * @param idLibro
     * @param accion
     * @param tipoRegistro
     * @param pageNumber
     * @return
     * @throws Exception
     */
    Paginacion getByFechasUsuarioLibro(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuarioEntidad, Long idLibro, Long accion, Long tipoRegistro) throws Exception;

    /**
     * Devuelve los registros Lopd de un Registro concreto, pasando el Tipo de Registro y la Accion
     * @param anyoRegistro
     * @param numRegistro
     * @param idLibro
     * @param accion
     * @param tipoRegistro
     * @return
     * @throws Exception
     */
    List<Lopd> getByRegistro(String anyoRegistro, String numRegistro, Long idLibro, Long accion, Long tipoRegistro) throws Exception;

    /**
     * Realiza una Alta en las tablas de Lopd
     * @param numeroRegistro
     * @param fecha
     * @param idLibro
     * @param idUsuarioEntidad
     * @param tipoRegistro
     * @param accion
     * @throws Exception
     */
    void altaLopd(String numeroRegistro, Date fecha, Long idLibro, Long idUsuarioEntidad, Long tipoRegistro, Long accion) throws Exception;

    /**
     * Inserta la búsqueda de Registros en las tablas de Lopd
     * @param paginacion
     * @param idUsuarioEntidad
     * @throws Exception
     */
    void insertarRegistros(Paginacion paginacion, Long idUsuarioEntidad, Long tipoRegistro, Long accion) throws Exception;

    /**
     * Comprueba si un usuario tiene Lopd
     *
     * @param idUsuarioEntidad
     * @return
     * @throws Exception
     */
    Boolean obtenerPorUsuario(Long idUsuarioEntidad) throws Exception;

    /**
     * Elimina las Lopd de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Busca los Registros de Entrada que no estén pendientes en función de la una fecha inicio, una fecha fin, por Usuario y Libro
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param idLibro
     * @return
     * @throws Exception
     */
    Paginacion buscaEntradaPorUsuarioLibro(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception;

    /**
     * Busca los Registros de Entrada Modificada que no sean de 'Creación' en función de la una fecha inicio, una fecha fin, por Usuario y Libro
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param idLibro
     * @return
     * @throws Exception
     */
    Paginacion entradaModificadaPorUsuarioLibro(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception;

    /**
     * Busca los Registros de Salida que no estén pendientes en función de la una fecha inicio, una fecha fin, por Usuario y Libro
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param idLibro

     * @return
     * @throws Exception
     */
    Paginacion buscaSalidaPorUsuarioLibro(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception;

    /**
     * Busca los Registros de Salida Modificada que no sean de 'Creación' en función de la una fecha inicio, una fecha fin, por Usuario y Libro
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param idLibro
     * @return
     * @throws Exception
     */
    Paginacion salidaModificadaPorUsuarioLibro(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, Long idLibro) throws Exception;

    /**
     * Busca los Registros de Entrada en función de la una fecha inicio, una fecha fin, por Usuario en los Libros gestionados
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param libros
     * @return
     * @throws Exception
     */
    Paginacion buscaEntradaPorUsuario(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws Exception;

    /**
     * Busca los Registros de Entrada Modificada en función de la una fecha inicio, una fecha fin, por Usuario y Libros gestionados
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param libros
     * @return
     * @throws Exception
     */
    Paginacion entradaModificadaPorUsuario(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws Exception;

    /**
     * Busca los Registros de Salida en función de la una fecha inicio, una fecha fin, por Usuario y Libros gestionados
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param libros
     * @return
     * @throws Exception
     */
    Paginacion buscaSalidaPorUsuario(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws Exception;

    /**
     * Busca los Registros de Salida Modificada en función de la una fecha inicio, una fecha fin, por Usuario y Libros gestionados
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuario
     * @param libros
     * @return
     * @throws Exception
     */
    Paginacion salidaModificadaPorUsuario(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idUsuario, List<Libro> libros) throws Exception;

    /**
     * Devuelve los registros Lopd de un Registro Migrado que ha realizado un Usuario entre dos fechas, pasando la Accion
     *
     * @param pageNumber
     * @param dataInici
     * @param dataFi
     * @param usuario
     * @param accion
     * @return
     * @throws Exception
     */
    Paginacion getByUsuario(Integer pageNumber, final Integer resultsPerPage, Date dataInici, Date dataFi, String usuario, String accion) throws Exception;

    /**
     * Busca los Registros de Entrada en función de la una fecha inicio, una fecha fin, Libro y Número de registro
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param idLibro
     * @param numeroRegistro
     * @return
     * @throws Exception
     */
    Paginacion buscaEntradasPorLibroTipoNumero(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idLibro, Integer numeroRegistro) throws Exception;

    /**
     * Busca los Registros de Salida en función de la una fecha inicio, una fecha fin, Libro y Número de registro
     *
     * @param pageNumber
     * @param fechaInicio
     * @param fechaFin
     * @param idLibro
     * @param numeroRegistro
     * @return
     * @throws Exception
     */
    Paginacion buscaSalidasPorLibroTipoNumero(Integer pageNumber, final Integer resultsPerPage, Date fechaInicio, Date fechaFin, Long idLibro, Integer numeroRegistro) throws Exception;


}
