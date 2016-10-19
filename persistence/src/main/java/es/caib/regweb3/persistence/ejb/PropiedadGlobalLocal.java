package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.PropiedadGlobal;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 05/05/16
 */
@Local
@RolesAllowed({"RWE_SUPERADMIN", "RWE_ADMIN", "RWE_USUARI"})
public interface PropiedadGlobalLocal extends BaseEjb<PropiedadGlobal, Long> {

    /**
     * Obtiene todas las {@link es.caib.regweb3.model.PropiedadGlobal} de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public List<PropiedadGlobal> findByEntidad(Long idEntidad, Long tipo) throws Exception;

    /**
     * Obtiene una {@link es.caib.regweb3.model.PropiedadGlobal} de una Entidad, según su clave
     *
     * @param clave     de la Propiedad
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public PropiedadGlobal findByClaveEntidad(String clave, Long idEntidad, Long idPropiedadGlobal) throws Exception;

    /**
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Long getTotalByEntidad(Long idEntidad, Long tipo) throws Exception;

    /**
     * @param inicio
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public List<PropiedadGlobal> getPaginationByEntidad(int inicio, Long idEntidad, Long tipo) throws Exception;

    /**
     * @return
     * @throws Exception
     */
    public Long getTotalREGWEB3(Long tipo) throws Exception;

    /**
     * @param inicio
     * @return
     * @throws Exception
     */
    public List<PropiedadGlobal> getPaginationREGWEB3(int inicio, Long tipo) throws Exception;

    /**
     * Elimina los {@link es.caib.regweb3.model.PropiedadGlobal} de una {@link es.caib.regweb3.model.Entidad}
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    public Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Obtiene el valor de una {@link es.caib.regweb3.model.PropiedadGlobal} de una {@link es.caib.regweb3.model.Entidad}
     * @param idEntidad de la Entidad
     * @param clave de la propiedad
     * @return
     * @throws Exception
     */
    public String getPropertyByEntidad(Long idEntidad, String clave) throws Exception;

    /**
     * Obtiene el valor de una {@link es.caib.regweb3.model.PropiedadGlobal}
     * @param clave de la propiedad
     * @return
     * @throws Exception
     */
    public String getProperty(String clave) throws Exception;

    /**
     * Obtiene el valor Boolean de una {@link es.caib.regweb3.model.PropiedadGlobal} de una {@link es.caib.regweb3.model.Entidad}
     * @param idEntidad de la Entidad
     * @param clave de la propiedad
     * @return
     * @throws Exception
     */
    public Boolean getBooleanPropertyByEntidad(Long idEntidad, String clave) throws Exception;

    /**
     * Obtiene el valor Boolean de una {@link es.caib.regweb3.model.PropiedadGlobal}
     * @param clave de la propiedad
     * @return
     * @throws Exception
     */
    public Boolean getBooleanProperty(String clave) throws Exception;

    /**
     * Obtiene el valor Long de una {@link es.caib.regweb3.model.PropiedadGlobal} de una {@link es.caib.regweb3.model.Entidad}
     * @param idEntidad de la Entidad
     * @param clave de la propiedad
     * @return
     * @throws Exception
     */
    public Long getLongPropertyByEntitat(Long idEntidad, String clave) throws Exception;

    /**
     * Obtiene el valor Long de una {@link es.caib.regweb3.model.PropiedadGlobal}
     * @param clave
     * @return
     * @throws Exception
     */
    public Long getLongProperty(String clave) throws Exception;
}
