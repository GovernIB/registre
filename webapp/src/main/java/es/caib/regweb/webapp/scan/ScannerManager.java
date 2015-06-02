package es.caib.regweb.webapp.scan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.fundaciobit.plugins.scanweb.IScanWebPlugin;
import org.fundaciobit.plugins.scanweb.ScanWebResource;
import org.fundaciobit.plugins.utils.PluginsManager;

import es.caib.regweb.utils.Configuracio;
import es.caib.regweb.utils.RegwebConstantes;

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
//        log.info("SCAN: Classe del plugin " + tipusScan + " = " + className);
        if (className == null || className.trim().length()<=0) {
        	log.error("No hi ha cap propietat " + propertyName + " definint la classe que gestiona el plugin de scanner");
        	throw new Exception("No hi ha cap propietat " + propertyName + " definint la classe que gestiona el plugin de scanner");
        }
        // Carregant la classe
        Object obj;
        obj = PluginsManager.instancePluginByClassName(className, propertyName + ".");
//        log.info("SCAN: Obtinguda instància de classe -> " + obj.toString());
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
	    		log.error("SCAN: Error al obtenir el plugin d'escaneig " + tipusScan, e);
	    	}
    	}
//    	log.info("SCAN: TeScan de " + tipusScan + " = " + plugin != null);
   		return plugin != null;
    }
    
    /**
     * Obtiene el nombre del tipo de scaneo
     * @param tipusScan
     * @param locale
     * @return
     */
    public static String getName(Integer tipusScan, Locale locale) throws Exception {
//    	log.info("Obtenint nom del tipus d'escaneig " + tipusScan);
   		return getInstance(tipusScan).getName(locale);
    }

    /**
     * Obtiene la cabecera con los imports necesarios para la página de escaneo
     * @param tipusScan
     * @return
     */
    public static String getHeaderJSP(HttpServletRequest request, Integer tipusScan, String docID) throws Exception {
//    	log.info("Obtenint header del tipus d'escaneig " + tipusScan);
    	return getInstance(tipusScan).getHeaderJSP(request, docID);
    }

    /**
     * Obtiene el cuerpo html necesario para crear la página de escaneo
     * @param tipusScan
     * @return
     */
    public static String getCoreJSP(HttpServletRequest request, Integer tipusScan, String docID) throws Exception {
//    	log.info("Obtenint core del tipus d'escaneig " + tipusScan);
    	return getInstance(tipusScan).getCoreJSP(request, docID );
    }
    
    
    
    public static int getMinHeight(HttpServletRequest request, Integer tipusScan, String docID) throws Exception {
      return getInstance(tipusScan).getMinHeight(request, docID);
    }
    
    

    /**
     * Crea un file en el sistema de archivos
     * @param file
     * @param dstId
     * @return
     * @throws Exception
     */
    public static ScanWebResource getResource(HttpServletRequest request, Integer tipusScan, String resourcename, String docID) throws Exception {
//    	log.info("Obtenint recurs " + resourcename + " del tipus d'escaneig " + tipusScan);
    	return getInstance(tipusScan).getResource(request, resourcename, docID);
    }

    
    
    public static List<TipoScan> getTipusScanejat(Locale locale, String noScanName){
      String[] values = new String[] {"0"};
      try {
        String plugins = Configuracio.getScanPlugins();
        if (plugins != null && !"".equals(plugins))
          values = plugins.split(",");
        
//        log.info("SCAN: Codis de plugins d'escaneig: " + plugins);
      } catch (Exception e) {
//        log.error("SCAN: Error al obtenir els plugins definits al sistema", e);
      }

      List<TipoScan> tiposScan = new ArrayList<TipoScan>();
      for(String value: values) {
        try {
          Integer codigo = Integer.parseInt(value.trim());
          String nombre = codigo.equals(0) ? noScanName : ScannerManager.getName(codigo, locale);
          TipoScan tipoScan = new TipoScan(codigo, nombre);
          tiposScan.add(tipoScan);
//          log.info("SCAN:   " + codigo + " - " + nombre);
        } catch (Exception e){
          log.warn("SCAN: El codi " + value + " no és un codi de tipus d'escanejat válid.");
        }
      }
      return tiposScan;
      //    return Arrays.asList(values);
    }
    
}
