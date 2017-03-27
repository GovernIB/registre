package es.caib.regweb3.webapp.utils;

import es.caib.regweb3.persistence.utils.PropiedadGlobalUtil;
import es.caib.regweb3.plugins.justificante.IJustificantePlugin;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.utils.PluginsManager;


/**
 * @author jpernia
 */
public class RegwebJustificantePluginManager {

    protected static final Logger log = Logger.getLogger(RegwebJustificantePluginManager.class);

    public static IJustificantePlugin plugin = null;

    public static IJustificantePlugin getInstance(Long idEntidadActiva) throws Exception {

        if (plugin == null) {

            // Valor de la Clau
            final String propertyName = "justificante.plugin";

            // Cerca el Plugin de Justificant definit a les Propietats Globals
            String className = PropiedadGlobalUtil.getJustificanteActivoPlugin(idEntidadActiva, propertyName);

            // Si no existeix la propietat global, dóna error
            if (className == null || className.trim().length() <= 0) {
                log.info("No hi ha cap propietat " + propertyName + " definint la classe que gestiona el plugin de justificant");
                return null;
            }

            // Carregant la classe
            Object obj;
            obj = PluginsManager.instancePluginByClassName(className, propertyName);
            plugin = (IJustificantePlugin) obj;

        }

        return plugin;
    }


}