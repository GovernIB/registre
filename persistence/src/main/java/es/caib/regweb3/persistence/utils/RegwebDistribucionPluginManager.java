package es.caib.regweb3.persistence.utils;

import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.distribucion.IDistribucionPlugin;
import org.fundaciobit.plugins.utils.PluginsManager;


/**
 * @author mgonzalez
 */
public class RegwebDistribucionPluginManager {

    protected static final Logger log = Logger.getLogger(RegwebDistribucionPluginManager.class);

    public static IDistribucionPlugin plugin = null;


    public static IDistribucionPlugin getInstance() throws Exception {

        if (plugin == null) {
            // Valor de la Clau
            final String propertyName = RegwebConstantes.REGWEB3_PROPERTY_BASE + "distribucion.plugin";
            final String tipusStr = System.getProperty(propertyName);

            if (tipusStr == null || tipusStr.trim().length() == 0) {
                return null;
            }

            String basePlugin = propertyName + "." + tipusStr;
            String className = System.getProperty(basePlugin);

            if (className == null || className.trim().length() <= 0) {
                log.info("No hi ha cap propietat " + propertyName + " definint la classe que gestiona el plugin de scanner");
                return null;
            }
            // Carregant la classe
            Object obj;
            obj = PluginsManager.instancePluginByClassName(className, basePlugin);


            plugin = (IDistribucionPlugin) obj;

        }

        return plugin;
    }


}
