package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.Libro;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/01/14
 */
@Local
public interface LibroLocal extends BaseEjb<Libro, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/LibroEJB";

    /**
     * Devuelve los Libros activos relacionados con algún Organismos de la Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Libro> getLibrosEntidad(Long idEntidad) throws I18NException;


    /**
     * Retorna true o false en función de si ya existe algún Libro con ese código
     *
     * @param codigo
     * @param idLibro
     * @return
     * @throws I18NException
     */
    Boolean existeCodigoEdit(String codigo, Long idLibro, Long idEntidad) throws I18NException;

    /**
     * Retorna un Libro a partir de su código
     *
     * @param codigo
     * @return
     * @throws I18NException
     */
    Libro findByCodigo(String codigo) throws I18NException;

    /**
     * Retorna un Libro a partir de su código y la Entidad a la que pertenece
     *
     * @param codigo
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Libro findByCodigoEntidad(String codigo, Long idEntidad) throws I18NException;

    /**
     * Comprueba si un Organimo tiene un Libro activo
     *
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    public Boolean tieneLibro(Long idOrganismo) throws I18NException;

    /**
     * Lista los Libros activos de un Organismo
     *
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    List<Libro> getLibrosActivosOrganismo(Long idOrganismo) throws I18NException;


    /**
     * Lista los Libros activos de un Organismo de otra entidad
     *
     * @param codigoOrganismo
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Libro> getLibrosActivosOrganismoDiferente(String codigoOrganismo, Long idEntidad) throws I18NException;

    /**
     * Lista los Libros de un Organismo
     *
     * @param idOrganismo
     * @return
     * @throws I18NException
     */
    List<Libro> getLibrosOrganismo(Long idOrganismo) throws I18NException;

    /**
     * Retorna todos los Libros de relacionados con algún Organismos de la Entidad, activo o no
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Libro> getTodosLibrosEntidad(Long idEntidad) throws I18NException;

    /**
     * Pone a 0 todos los contadores de un Libro
     *
     * @param idLibro
     * @throws I18NException
     */
    void reiniciarContadores(Long idLibro) throws I18NException;

    /**
     * Crea un libro y le asocia sus contadores
     *
     * @param libro
     * @return
     * @throws I18NException
     */
    Libro crearLibro(Libro libro) throws I18NException;

    /**
     * Elimina los Libros de una Entidad
     * @param entidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Entidad entidad) throws I18NException;

}
