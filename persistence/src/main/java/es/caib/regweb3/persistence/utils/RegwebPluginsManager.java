package es.caib.regweb3.persistence.utils;

import es.caib.regweb3.utils.RegwebConstantes;
import org.apache.log4j.Logger;
import org.fundaciobit.plugins.signatureserver.api.ISignatureServerPlugin;
import org.fundaciobit.plugins.utils.PluginsManager;

import java.util.Properties;

/**
 * Created by jpernia on 30/03/2017.
 */
public class RegwebPluginsManager {

    public static ISignatureServerPlugin plugin = null;


    protected static final Logger log = Logger.getLogger(RegwebPluginsManager.class);


    public static ISignatureServerPlugin getPluginSignatureServer(Long idEntidad) {

        if(plugin==null) {

            String BASE_PACKAGE = RegwebConstantes.REGWEB3_PROPERTY_BASE + "firmajustificante.";

            final String partialPropertyName = "firmajustificante.plugin";
            String className = PropiedadGlobalUtil.getStringByEntidad(idEntidad, partialPropertyName);

            // Valor global si no existeix el de per entitat
            if (className == null) {
                className = PropiedadGlobalUtil.getString(partialPropertyName);
            }

            // Si no existeix la propietat global, dóna error
            if (className == null || className.trim().length() <= 0) {
                log.info("No s'ha pogut instanciar el plugin de Signature en Servidor");
                return null;
            }

            Properties prop = PropiedadGlobalUtil.getAllPropertiesByEntity(idEntidad);

            ISignatureServerPlugin pluginInstance = (ISignatureServerPlugin) PluginsManager.instancePluginByClassName(className, BASE_PACKAGE, prop);

            plugin = pluginInstance;
        }

        return plugin;
    }


}
