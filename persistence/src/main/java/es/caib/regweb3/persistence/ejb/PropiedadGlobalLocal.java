package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.PropiedadGlobal;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 05/05/16
 */
@Local
public interface PropiedadGlobalLocal extends BaseEjb<PropiedadGlobal, Long> {

    String JNDI_NAME = "java:app/regweb3-persistence/PropiedadGlobalEJB";


    /**
     * Obtiene todas las {@link es.caib.regweb3.model.PropiedadGlobal} de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<PropiedadGlobal> findByEntidad(Long idEntidad, Long tipo) throws I18NException;

    /**
     * Obtiene una {@link es.caib.regweb3.model.PropiedadGlobal} de una Entidad, según su clave
     *
     * @param clave     de la Propiedad
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    PropiedadGlobal findByClaveEntidad(String clave, Long idEntidad, Long idPropiedadGlobal) throws I18NException;

    /**
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Long getTotalByEntidad(Long idEntidad, Long tipo) throws I18NException;

    /**
     * @param inicio
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<PropiedadGlobal> getPaginationByEntidad(int inicio, Long idEntidad, Long tipo) throws I18NException;

    /**
     * @return
     * @throws I18NException
     */
    Long getTotalREGWEB3(Long tipo) throws I18NException;

    /**
     * @param inicio
     * @return
     * @throws I18NException
     */
    List<PropiedadGlobal> getPaginationREGWEB3(int inicio, Long tipo) throws I18NException;

    /**
     * Elimina los {@link es.caib.regweb3.model.PropiedadGlobal} de una {@link es.caib.regweb3.model.Entidad}
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

    /**
     * Obtiene el valor de una {@link es.caib.regweb3.model.PropiedadGlobal} de una {@link es.caib.regweb3.model.Entidad}
     *
     * @param idEntidad de la Entidad
     * @param clave     de la propiedad
     * @return
     * @throws I18NException
     */
    String getPropertyByEntidad(Long idEntidad, String clave) throws I18NException;

    /**
     * Obtiene el valor de una {@link es.caib.regweb3.model.PropiedadGlobal}
     *
     * @param clave de la propiedad
     * @return
     * @throws I18NException
     */
    String getProperty(String clave) throws I18NException;

    /**
     * Obtiene el valor Boolean de una {@link es.caib.regweb3.model.PropiedadGlobal} de una {@link es.caib.regweb3.model.Entidad}
     *
     * @param idEntidad de la Entidad
     * @param clave     de la propiedad
     * @return
     * @throws I18NException
     */
    Boolean getBooleanPropertyByEntidad(Long idEntidad, String clave) throws I18NException;

    /**
     * Obtiene el valor Boolean de una {@link es.caib.regweb3.model.PropiedadGlobal}
     *
     * @param clave de la propiedad
     * @return
     * @throws I18NException
     */
    Boolean getBooleanProperty(String clave) throws I18NException;

    /**
     * Obtiene el valor Long de una {@link es.caib.regweb3.model.PropiedadGlobal} de una {@link es.caib.regweb3.model.Entidad}
     *
     * @param idEntidad de la Entidad
     * @param clave     de la propiedad
     * @return
     * @throws I18NException
     */
    Long getLongPropertyByEntitat(Long idEntidad, String clave) throws I18NException;

    /**
     * Obtiene el valor Long de una {@link es.caib.regweb3.model.PropiedadGlobal}
     *
     * @param clave
     * @return
     * @throws I18NException
     */
    Long getLongProperty(String clave) throws I18NException;

    /**
     * Obtiene el valor Integer de una {@link es.caib.regweb3.model.PropiedadGlobal}
     *
     * @param idEntidad
     * @param clave
     * @return
     * @throws I18NException
     */
    Integer getIntegerPropertyByEntitat(Long idEntidad, String clave) throws I18NException;

    /**
     * Obtiene el valor Integer de una {@link es.caib.regweb3.model.PropiedadGlobal}
     *
     * @param clave
     * @return
     * @throws I18NException
     */
    Integer getIntegerProperty(String clave) throws I18NException;

    /**
     * Obtiene las {@link es.caib.regweb3.model.PropiedadGlobal} de una {@link es.caib.regweb3.model.Entidad}
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<PropiedadGlobal> getAllPropertiesByEntidad(Long idEntidad) throws I18NException;

    /**
     * Obtiene todas las {@link es.caib.regweb3.model.PropiedadGlobal} que no estan asociadas
     * a alguna {@link es.caib.regweb3.model.Entidad}
     *
     * @return
     * @throws I18NException
     */
    List<PropiedadGlobal> getAllProperties() throws I18NException;
}
