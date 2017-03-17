package es.caib.regweb3.persistence.utils;

import org.apache.log4j.Logger;
import org.fundaciobit.plugins.postproceso.IPostProcesoPlugin;
import org.fundaciobit.plugins.utils.PluginsManager;

/**
 * @author mgonzalez
 */
public class RegwebPostProcesoPluginManager {
    protected static final Logger log = Logger.getLogger(RegwebPostProcesoPluginManager.class);

    public static IPostProcesoPlugin plugin = null;


    public static IPostProcesoPlugin getInstance(Long idEntidad) throws Exception {

        if (plugin == null) {
            String className = PropiedadGlobalUtil.getPluginPostProceso(idEntidad);
            String basePlugin= PropiedadGlobalUtil.getBasePluginPostProceso();

            if (className == null || className.trim().length() <= 0) {
                log.debug("No hi ha cap propietat " + basePlugin + " definint la classe que gestiona el plugin de Post-Procés");
                return null;
            }

            Object obj;
            obj = PluginsManager.instancePluginByClassName(className, basePlugin);
            plugin = (IPostProcesoPlugin) obj;

        }

        return plugin;
    }


}
