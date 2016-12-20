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
    public List<Libro> getLibrosEntidad(Long idEntidad) throws Exception;


    /**
     * Retorna true o false en función de si ya existe algún Libro con ese código
     * @param codigo
     * @param idLibro
     * @return
     * @throws Exception
     */
    public Boolean existeCodigoEdit(String codigo, Long idLibro, Long idEntidad) throws Exception;

    /**
     * Retorna un Libro a partir de su código
     * @param codigo
     * @return
     * @throws Exception
     */
    public Libro findByCodigo(String codigo) throws Exception;

    /**
     * Retorna un Libro a partir de su código y la Entidad a la que pertenece
     * @param codigo
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Libro findByCodigoEntidad(String codigo, Long idEntidad) throws Exception;

    /**
     * Lista los Libros activos de un Organismo
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    public List<Libro> getLibrosActivosOrganismo(Long idOrganismo) throws Exception;

    /**
     * Lista los Libros de un Organismo
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    public List<Libro> getLibrosOrganismo(Long idOrganismo) throws Exception;

    /**
     * Obtiene los libros de un organismo solo cargando el id, la denominación y el estado
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    public List<Libro> getLibrosOrganismoLigero(Long idOrganismo) throws Exception;

    /**
     * Retorna todos los Libros de relacionados con algún Organismos de la Entidad, activo o no
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public List<Libro> getTodosLibrosEntidad(Long idEntidad) throws Exception;

    /**
     * Retorna si la entidad indicada tiene libros.
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Boolean tieneLibrosEntidad(Long idEntidad) throws Exception;

    /**
     * Pone a 0 todos los contadores de un Libro
     * @param idLibro
     * @throws Exception
     */
    public void reiniciarContadores(Long idLibro) throws Exception;

    /**
     * Reinicia los Contadores de todos los Libros de la Entidad
     * @param idEntidad
     * @throws Exception
     */
    public void reiniciarContadoresEntidad(Long idEntidad) throws Exception;

    /**
     * Obtiene los Organismos vigentes que tienen Libro activo de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public List<Organismo> organismosConLibro(Long idEntidad) throws Exception;

    /**
     * Crea un libro y le asocia sus contadores
     * @param libro
     * @return
     * @throws Exception
     */
    public Libro crearLibro(Libro libro) throws Exception;

    /**
     * Elimina los Libros de una Entidad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Elimina un Libro
     * @param idLibro
     * @return
     * @throws Exception
     */
    public Long eliminarLibro(Long idLibro) throws Exception;

}
