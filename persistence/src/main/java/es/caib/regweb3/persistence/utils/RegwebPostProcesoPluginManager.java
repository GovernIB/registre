package es.caib.regweb3.persistence.utils;

import es.caib.regweb3.plugins.postproceso.IPostProcesoPlugin;
import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.utils.PluginsManager;

import java.util.Properties;

/**
 * @author mgonzalez
 */
public class RegwebPostProcesoPluginManager {
    protected static final Logger log = Logger.getLogger(RegwebPostProcesoPluginManager.class);

    public static IPostProcesoPlugin plugin = null;


    public static IPostProcesoPlugin getInstance(Long idEntidad) throws Exception {

        if (plugin == null) {
            String className = PropiedadGlobalUtil.getPluginPostProceso(idEntidad);

            final String partialPropertyName = "postproceso.plugin";
            String basePlugin= RegwebConstantes.REGWEB3_PROPERTY_BASE + partialPropertyName;

            if (className == null || className.trim().length() <= 0) {
                log.debug("No hi ha cap propietat " + basePlugin + " definint la classe que gestiona el plugin de Post-Procés");
                return null;
            }

            Properties prop = PropiedadGlobalUtil.getAllPropertiesByEntity(idEntidad);

            Object obj;
            obj = PluginsManager.instancePluginByClassName(className, basePlugin, prop);
            plugin = (IPostProcesoPlugin) obj;

        }

        return plugin;
    }


}
