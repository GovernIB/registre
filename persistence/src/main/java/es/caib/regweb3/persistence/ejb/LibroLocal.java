package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.Organismo;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI"})
public interface LibroLocal extends BaseEjb<Libro, Long> {

    /**
     * Devuelve los Libros activos relacionados con algún Organismos de la Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Libro> getLibrosEntidad(Long idEntidad) throws Exception;


    /**
     * Retorna true o false en función de si ya existe algún Libro con ese código
     * @param codigo
     * @param idLibro
     * @return
     * @throws Exception
     */
    Boolean existeCodigoEdit(String codigo, Long idLibro, Long idEntidad) throws Exception;

    /**
     * Retorna un Libro a partir de su código
     * @param codigo
     * @return
     * @throws Exception
     */
    Libro findByCodigo(String codigo) throws Exception;

    /**
     * Retorna un Libro a partir de su código y la Entidad a la que pertenece
     * @param codigo
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Libro findByCodigoEntidad(String codigo, Long idEntidad) throws Exception;

    /**
     * Comprueba si un Organimo tiene un Libro activo
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    public Boolean tieneLibro(Long idOrganismo) throws Exception;

    /**
     * Lista los Libros activos de un Organismo
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    List<Libro> getLibrosActivosOrganismo(Long idOrganismo) throws Exception;

    /**
     * Lista los Libros activos de un Organismo
     * @param codigoOrganismo
     * @return
     * @throws Exception
     */
    List<Libro> getLibrosActivosOrganismo(String codigoOrganismo) throws Exception;

    /**
     * Lista los Libros de un Organismo
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    List<Libro> getLibrosOrganismo(Long idOrganismo) throws Exception;

    /**
     * Obtiene los libros de un organismo solo cargando el id, la denominación y el estado
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    List<Libro> getLibrosOrganismoLigero(Long idOrganismo) throws Exception;

    /**
     * Retorna todos los Libros de relacionados con algún Organismos de la Entidad, activo o no
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Libro> getTodosLibrosEntidad(Long idEntidad) throws Exception;

    /**
     * Retorna si la entidad indicada tiene libros.
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Boolean tieneLibrosEntidad(Long idEntidad) throws Exception;

    /**
     * Pone a 0 todos los contadores de un Libro
     * @param idLibro
     * @throws Exception
     */
    void reiniciarContadores(Long idLibro) throws Exception;

    /**
     * Reinicia los Contadores de todos los Libros de la Entidad
     * @param idEntidad
     * @throws Exception
     */
    void reiniciarContadoresEntidad(Long idEntidad) throws Exception;

    /**
     * Obtiene los Organismos vigentes que tienen Libro activo de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Organismo> organismosConLibro(Long idEntidad) throws Exception;

    /**
     * Crea un libro y le asocia sus contadores
     * @param libro
     * @return
     * @throws Exception
     */
    Libro crearLibro(Libro libro) throws Exception;

    /**
     * Elimina los Libros de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Elimina un Libro
     * @param idLibro
     * @return
     * @throws Exception
     */
    Long eliminarLibro(Long idLibro) throws Exception;

    /**
     * Tarea que reinicia los contadores de los libros de una entidad
     * @param idEntidad
     */
    void reiniciarContadoresEntidadTask(Long idEntidad);

}
