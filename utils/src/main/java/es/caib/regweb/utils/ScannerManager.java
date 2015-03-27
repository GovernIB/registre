package es.caib.regweb.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.fundaciobit.plugins.scanweb.IScanWebPlugin;
import org.fundaciobit.plugins.scanweb.ScanWebResource;
import org.fundaciobit.plugins.utils.PluginsManager;
/**
 * Created by Limit Tecnologies.
 * User: sandreu
 * Date: 12/03/15
 */
public class ScannerManager {


    protected static final Logger log = Logger.getLogger(ScannerManager.class);
    
    
    protected static Map<Integer, IScanWebPlugin> plugins = new HashMap<Integer, IScanWebPlugin>();
    
    protected  static IScanWebPlugin getInstance(Integer tipusScan) throws Exception {
      
      if (plugins.get(tipusScan) == null) {
        // Valor de la Clau
        final String propertyName = RegwebConstantes.REGWEB_PROPERTY_BASE + "scan.plugin." + tipusScan;
        String className = System.getProperty(propertyName);
//        String className = "es.limit.plugins.scanweb.dynamicwebtwain.DynamicWebTwainScanWebPlugin";
        if (className == null || className.trim().length()<=0) {
        	log.error("No hi ha cap propietat " + propertyName + " definint la classe que gestiona el plugin de scanner");
        	throw new Exception("No hi ha cap propietat " + propertyName + " definint la classe que gestiona el plugin de scanner");
        }
        // Carregant la classe
        Object obj;
        obj = PluginsManager.instancePluginByClassName(className, propertyName + ".");
        plugins.put(tipusScan, (IScanWebPlugin)obj);
//        Class<?> clazz = Class.forName(className);
//        IScanWebPlugin plugin = (IScanWebPlugin)clazz.newInstance();
//        plugins.put(tipusScan, plugin);
      }      

      return plugins.get(tipusScan); 
    }
    
    /**
     * Comprueba si la entidad tiene definido un tipo de escaneo vàlido
     * @param tipusScan
     * @return
     */
    public static boolean teScan(Integer tipusScan) {
    	IScanWebPlugin plugin = null;
    	if (tipusScan != null && tipusScan > 0) {
	    	try {
	    		plugin = getInstance(tipusScan);
	    	} catch (Exception e) {
	    		// En cas d'error el plugin serà null
	    		log.error("Error al obtenir el plugin d'escaneig " + tipusScan, e);
	    	}
    	}
   		return plugin != null;
    }
    
    /**
     * Obtiene el nombre del tipo de scaneo
     * @param tipusScan
     * @param locale
     * @return
     */
    public static String getName(Integer tipusScan, Locale locale) throws Exception {
   		return getInstance(tipusScan).getName(locale);
    }

    /**
     * Obtiene la cabecera con los imports necesarios para la página de escaneo
     * @param tipusScan
     * @return
     */
    public static String getHeaderJSP(HttpServletRequest request, Integer tipusScan) throws Exception {
      return getInstance(tipusScan).getHeaderJSP(request);
    }

    /**
     * Obtiene el cuerpo html necesario para crear la página de escaneo
     * @param tipusScan
     * @return
     */
    public static String getCoreJSP(HttpServletRequest request, Integer tipusScan) throws Exception {
    	 return getInstance(tipusScan).getCoreJSP(request);
    }

    /**
     * Crea un file en el sistema de archivos
     * @param file
     * @param dstId
     * @return
     * @throws Exception
     */
    public static ScanWebResource getResource(HttpServletRequest request, Integer tipusScan, String resourcename) throws Exception {
    	return getInstance(tipusScan).getResource(request, resourcename);
    }

}
