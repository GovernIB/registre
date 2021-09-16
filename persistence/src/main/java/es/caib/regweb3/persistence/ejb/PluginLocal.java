package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Plugin;
import org.fundaciobit.genapp.common.i18n.I18NException;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Local;
import java.util.List;
import java.util.Properties;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 05/05/16
 */
@Local
public interface PluginLocal extends BaseEjb<Plugin, Long> {

    /**
     * Obtiene un {@link Plugin} determinado de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Plugin> findByEntidadTipo(Long idEntidad, Long tipo) throws Exception;

    /**
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Long getTotalByEntidad(Long idEntidad, Long tipo) throws Exception;

    /**
     * @param inicio
     * @param idEntidad
     * @return
     * @throws Exception
     */
    List<Plugin> getPaginationByEntidad(int inicio, Long idEntidad, Long tipo) throws Exception;

    /**
     * @return
     * @throws Exception
     */
    Long getTotalREGWEB3(Long tipo) throws Exception;

    /**
     * @param inicio
     * @return
     * @throws Exception
     */
    List<Plugin> getPaginationREGWEB3(int inicio, Long tipo) throws Exception;

    /**
     * Elimina los {@link Plugin} de una {@link es.caib.regweb3.model.Entidad}
     *
     * @param idEntidad
     * @return
     * @throws Exception
     */
    Integer eliminarByEntidad(Long idEntidad) throws Exception;

    /**
     * Obtiene en plugin determinado
     * @param idEntidad
     * @param tipoPlugin
     * @return
     * @throws Exception
     */
    Object getPlugin(Long idEntidad, Long tipoPlugin) throws I18NException;

    /**
     * Obtiene las Propiedades del plugin determinado
     * @param idEntidad
     * @param tipoPlugin
     * @return
     * @throws Exception
     */
    Properties getPropertiesPlugin(Long idEntidad, Long tipoPlugin) throws I18NException;


    /**
     * Comprueba si el plugin está definido
     * @param idEntidad
     * @param tipoPlugin
     * @return
     * @throws I18NException
     */
    boolean existPlugin(Long idEntidad, Long tipoPlugin) throws I18NException;

    /**
     * Obtiene todos los plgins del tipo espeficicado
     * @param idEntidad
     * @param tipoPlugin
     * @return
     * @throws Exception
     */
    List<Object> getPlugins(Long idEntidad, Long tipoPlugin) throws Exception;

}
