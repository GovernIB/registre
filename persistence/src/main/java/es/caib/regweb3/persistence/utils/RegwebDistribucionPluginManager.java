package es.caib.regweb3.persistence.utils;

import org.apache.log4j.Logger;
import org.fundaciobit.plugins.distribucion.IDistribucionPlugin;
import org.fundaciobit.plugins.utils.PluginsManager;


/**
 * @author mgonzalez
 */
public class RegwebDistribucionPluginManager {

    protected static final Logger log = Logger.getLogger(RegwebDistribucionPluginManager.class);

    public static IDistribucionPlugin plugin = null;


    public static IDistribucionPlugin getInstance(Long idEntidad) throws Exception {


        if (plugin == null) {
            String className = PropiedadGlobalUtil.getPluginDistribucion(idEntidad);
            String basePlugin= PropiedadGlobalUtil.getBasePluginDistribucion();

            if (className == null || className.trim().length() <= 0) {
                log.debug("No hi ha cap propietat " + basePlugin + " definint la classe que gestiona el plugin de distribució");
                return null;
            }

            Object obj;
            obj = PluginsManager.instancePluginByClassName(className, basePlugin);
            plugin= (IDistribucionPlugin) obj;

        }

        return plugin;
    }




}
