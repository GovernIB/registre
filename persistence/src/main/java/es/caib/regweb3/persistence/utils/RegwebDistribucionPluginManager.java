package es.caib.regweb3.persistence.utils;

import es.caib.regweb3.plugins.distribucion.IDistribucionPlugin;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.utils.PluginsManager;


/**
 * @author mgonzalez
 */
public class RegwebDistribucionPluginManager {

    protected static final Logger log = Logger.getLogger(RegwebDistribucionPluginManager.class);

    public static IDistribucionPlugin plugin = null;


    public static IDistribucionPlugin getInstance(Long idEntidad) throws Exception {


        // Valor de la Clau

        final String propertyName = "distribucion.plugin";

        // Cerca el Plugin de Distribució definit a les Propietats Globals
        String className = PropiedadGlobalUtil.getPluginDistribucion(idEntidad, propertyName);

        // Si no existeix la propietat global, dóna error
        if (className == null || className.trim().length() <= 0) {
            log.info("No hi ha cap propietat " + propertyName + " definint la classe que gestiona el plugin de distribució");
            return null;
        }

        // Carregant la classe
        Object obj;
        obj = PluginsManager.instancePluginByClassName(className, propertyName);
        plugin = (IDistribucionPlugin) obj;

        return plugin;
    }




}
