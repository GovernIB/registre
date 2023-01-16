package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Descarga;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 10/10/13
 */
@Local
public interface DescargaLocal extends BaseEjb<Descarga, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/DescargaEJB";


    /**
     * Busca una descarga solo por el tipo.
     *
     * @param tipo
     * @return
     * @throws I18NException
     */
    Descarga findByTipo(String tipo) throws I18NException;

    /**
     * Obtiene el valor de la última descarga de un tipo y de una entidad
     *
     * @param tipo indica el tipo (UNIDAD, OFICINA)
     * @return la descarga encontrada
     * @throws I18NException
     */
    Descarga ultimaDescarga(String tipo, Long idEntidad) throws I18NException;

    /**
     * Obtiene el valor de la primera descarga de un tipo y de una entidad
     * Nos sirve para determinar la fecha de la primera sincronizacion
     *
     * @param tipo indica el tipo (UNIDAD, OFICINA)
     * @return la descarga encontrada
     * @throws I18NException
     */
    Descarga findByTipoEntidadInverse(String tipo, Long idEntidad) throws I18NException;

    /**
     * Calcula el total por entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Long getTotalByEntidad(Long idEntidad) throws I18NException;

    /**
     * Obtiene la paginación por entidad
     *
     * @param inicio
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Descarga> getPaginationByEntidad(int inicio, Long idEntidad) throws I18NException;

    void deleteByTipo(String tipo) throws I18NException;

    /**
     * Obtiene las descargas de una entidad ordenadas por código;
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Descarga> findByEntidad(Long idEntidad) throws I18NException;

    /**
     * Eimina todas las Descargas de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

    /**
     * @param inicio
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Descarga> getPagination(int inicio, Long idEntidad) throws I18NException;

    /**
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Long getTotalEntidad(Long idEntidad) throws I18NException;
}
