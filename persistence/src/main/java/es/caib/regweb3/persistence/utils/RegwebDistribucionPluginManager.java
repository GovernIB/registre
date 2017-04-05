package es.caib.regweb3.persistence.utils;

import es.caib.regweb3.plugins.distribucion.IDistribucionPlugin;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.utils.PluginsManager;

import java.util.Properties;


/**
 * @author mgonzalez
 */
public class RegwebDistribucionPluginManager {

    protected static final Logger log = Logger.getLogger(RegwebDistribucionPluginManager.class);

    public static IDistribucionPlugin plugin = null;


    public static IDistribucionPlugin getInstance(Long idEntidad) throws Exception {


        if (plugin == null) {
            String className = PropiedadGlobalUtil.getPluginDistribucion(idEntidad);

            final String partialPropertyName = "distribucion.plugin";
            String basePlugin= RegwebConstantes.REGWEB3_PROPERTY_BASE + partialPropertyName;

            if (className == null || className.trim().length() <= 0) {
                log.debug("No hi ha cap propietat " + basePlugin + " definint la classe que gestiona el plugin de distribució");
                return null;
            }

            Properties prop = PropiedadGlobalUtil.getAllPropertiesByEntity(idEntidad);

            Object obj;
            obj = PluginsManager.instancePluginByClassName(className, basePlugin, prop);
            plugin= (IDistribucionPlugin) obj;

        }

        return plugin;
    }




}
