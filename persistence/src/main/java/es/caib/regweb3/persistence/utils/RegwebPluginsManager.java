package es.caib.regweb3.persistence.utils;

import es.caib.regweb3.utils.RegwebConstantes;

import org.apache.log4j.Logger;
import org.fundaciobit.plugins.signatureserver.api.ISignatureServerPlugin;
import org.fundaciobit.plugins.utils.PluginsManager;
import org.fundaciobit.plugins.validatesignature.api.IValidateSignaturePlugin;

import java.util.Properties;

/**
 * Created by jpernia on 30/03/2017.
 * 
 * @author anadal (plugin firma annexos)
 */
public class RegwebPluginsManager {
  

   protected static final Logger log = Logger.getLogger(RegwebPluginsManager.class);

   // ======================================================================
   // ======================================================================
   // ================ FIRMA JUSTIFICANT & FIRMA ANEXOS SIR  ===============
   // ======================================================================
   // ======================================================================

    protected static ISignatureServerPlugin pluginSignatureServer = null;

    // TODO XYZ ZZZ Renombrar aquest plugin a getPluginSignatureServerForJustificante
    public static ISignatureServerPlugin getPluginSignatureServer(Long idEntidad) {

        if(pluginSignatureServer==null) {
          
            String base = "firmajustificante.";

            String BASE_PACKAGE = RegwebConstantes.REGWEB3_PROPERTY_BASE + base;

            final String partialPropertyName = base + ".plugin";
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

            pluginSignatureServer = (ISignatureServerPlugin) PluginsManager.instancePluginByClassName(className, BASE_PACKAGE, prop);
        }

        return pluginSignatureServer;
    }

    
    
    
    // ======================================================================
    // ======================================================================
    // ======================= INFORMACIO DE FIRMES  ========================
    // ======================================================================
    // ======================================================================

    
    protected static IValidateSignaturePlugin pluginValidateSignature = null;

    
    public static IValidateSignaturePlugin getPluginValidateSignature(Long idEntidad) throws Exception {

        if(pluginValidateSignature==null) {
          
            final String base = "validatesignature.";
            final String desc = "Información y Validación de Firmas";

            Object obj = instantiatePlugin(idEntidad, base, desc); 

            pluginValidateSignature = (IValidateSignaturePlugin) obj;
        }

        return pluginValidateSignature;
    }
    

    
    // ======================================================================
    // ======================================================================
    // ================================ CODI COMU  ==========================
    // ======================================================================
    // ======================================================================
    
    protected static Object instantiatePlugin(Long idEntidad, final String base,
        final String desc) throws Exception {
      final String pluginPropertyName = RegwebConstantes.REGWEB3_PROPERTY_BASE + base + ".plugin";
      String className = PropiedadGlobalUtil.getStringByEntidad(idEntidad, pluginPropertyName);

      
      // Valor global si no existeix el de per entitat
      boolean isGlobal = false;
      if (className == null) {
        className = PropiedadGlobalUtil.getString(pluginPropertyName);
        isGlobal = true;
      }

      // Si no existeix la propietat global, dóna error
      if (className == null || className.trim().length() <= 0) {
          throw new Exception("No s'ha pogut instanciar el plugin de " + desc
              + ": No s'ha trobat cap valor per la propietat " + pluginPropertyName);
      }

      // Properties del Plugin
      Properties prop;
      if (isGlobal) {
        prop = PropiedadGlobalUtil.getAllProperties();
      } else {
        prop = PropiedadGlobalUtil.getAllPropertiesByEntity(idEntidad);
      }
      

      String basePluginProperties = RegwebConstantes.REGWEB3_PROPERTY_BASE + base + "properties.";
      
      Object obj = PluginsManager.instancePluginByClassName(className, basePluginProperties, prop);
      return obj;
    }

}
