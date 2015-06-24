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
@RolesAllowed({"RWE_ADMIN","RWE_USUARI"})
public interface LopdLocal extends BaseEjb<Lopd, Long> {

    /**
     * Devuelve los registros Lopd entre dos fechas para un Usuario en concreto y una lista de Libros, con la Accion (listado/consulta) y el TipoRegistro (entrada/salida)
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuarioEntidad
     * @param libros
     * @param accion
     * @param tipoRegistro
     * @return
     * @throws Exception
     */
    public List<Lopd> getByFechasUsuario(Date fechaInicio, Date fechaFin, Long idUsuarioEntidad, List<Libro> libros, Long accion, Long tipoRegistro) throws Exception;

    /**
     * Devuelve los registros Lopd entre dos fechas para un Usuario en concreto y un Libro en concreto, con la Accion (listado/consulta) y el TipoRegistro (entrada/salida)
     * @param fechaInicio
     * @param fechaFin
     * @param idUsuarioEntidad
     * @param idLibro
     * @param accion
     * @param tipoRegistro
     * @return
     * @throws Exception
     */
    public List<Lopd> getByFechasUsuarioLibro(Date fechaInicio, Date fechaFin, Long idUsuarioEntidad, Long idLibro, Long accion, Long tipoRegistro) throws Exception;

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
    public List<Lopd> getByRegistro(String anyoRegistro, Integer numRegistro, Long idLibro, Long accion, Long tipoRegistro) throws Exception;

    /**
     * Inserta la consulta de un Registro de Entrada en las tablas de Lopd
     * @param idRegistro
     * @param idUsuarioEntidad
     * @throws Exception
     */
    public void insertarRegistroEntrada(Long idRegistro, Long idUsuarioEntidad) throws Exception;

    /**
     * Inserta la búsqueda de un Registros de Entrada en las tablas de Lopd
     * @param paginacion
     * @param idUsuarioEntidad
     * @throws Exception
     */
    public void insertarRegistrosEntrada(Paginacion paginacion, Long idUsuarioEntidad) throws Exception;

    /**
     * Inserta la consulta de un Registro de Salida en las tablas de Lopd
     * @param idRegistro
     * @param idUsuarioEntidad
     * @throws Exception
     */
    public void insertarRegistroSalida(Long idRegistro, Long idUsuarioEntidad) throws Exception;

    /**
     * Inserta la búsqueda de un Registros de Salida en las tablas de Lopd
     * @param paginacion
     * @param idUsuarioEntidad
     * @throws Exception
     */
    public void insertarRegistrosSalida(Paginacion paginacion, Long idUsuarioEntidad) throws Exception;

    /**
     * Elimina las Lopd de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;
}
