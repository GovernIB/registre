package es.caib.regweb3.persistence.ejb;

import es.caib.regweb3.model.Plugin;
import org.fundaciobit.genapp.common.i18n.I18NException;

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

    String JNDI_NAME = "java:app/regweb3-persistence/PluginEJB";


    /**
     * Obtiene un {@link Plugin} determinado de una Entidad
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    List<Plugin> findByEntidadTipo(Long idEntidad, Long tipo) throws I18NException;

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
    List<Plugin> getPaginationByEntidad(int inicio, Long idEntidad, Long tipo) throws I18NException;

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
    List<Plugin> getPaginationREGWEB3(int inicio, Long tipo) throws I18NException;

    /**
     * Elimina los {@link Plugin} de una {@link es.caib.regweb3.model.Entidad}
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Integer eliminarByEntidad(Long idEntidad) throws I18NException;

    /**
     * Obtiene en plugin determinado
     *
     * @param idEntidad
     * @param tipoPlugin
     * @return
     * @throws I18NException
     */
    Object getPlugin(Long idEntidad, Long tipoPlugin) throws I18NException;

    /**
     * Obtiene en plugin de Distribución
     *
     * @param idEntidad
     * @return
     * @throws I18NException
     */
    Object getPluginDistribucion(Long idEntidad) throws I18NException;

    /**
     * Obtiene las Propiedades del plugin determinado
     *
     * @param idEntidad
     * @param tipoPlugin
     * @return
     * @throws I18NException
     */
    Properties getPropertiesPlugin(Long idEntidad, Long tipoPlugin) throws I18NException;


    /**
     * Comprueba si el plugin está definido
     *
     * @param idEntidad
     * @param tipoPlugin
     * @return
     * @throws I18NException
     */
    boolean existPlugin(Long idEntidad, Long tipoPlugin) throws I18NException;

    /**
     * Obtiene todos los plgins del tipo espeficicado
     *
     * @param idEntidad
     * @param tipoPlugin
     * @return
     * @throws I18NException
     */
    List<Object> getPlugins(Long idEntidad, Long tipoPlugin) throws I18NException;

}
