package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Libro;

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
@RolesAllowed({"RWE_SUPERADMIN","RWE_ADMIN","RWE_USUARI","RWE_WS_ENTRADA","RWE_WS_SALIDA"})
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
     * Lista los Libros activos de un Organismo de otra entidad
     * @param codigoOrganismo
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Libro> getLibrosActivosOrganismoDiferente(String codigoOrganismo, Long idEntidad) throws Exception;

    /**
     * Lista los Libros de un Organismo
     * @param idOrganismo
     * @return
     * @throws Exception
     */
    List<Libro> getLibrosOrganismo(Long idOrganismo) throws Exception;

    /**
     * Retorna todos los Libros de relacionados con algún Organismos de la Entidad, activo o no
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Libro> getTodosLibrosEntidad(Long idEntidad) throws Exception;

    /**
     * Pone a 0 todos los contadores de un Libro
     * @param idLibro
     * @throws Exception
     */
    void reiniciarContadores(Long idLibro) throws Exception;

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

}
