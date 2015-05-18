package es.caib.regweb.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;


/**
 * 
 * @author anadal
 * 
 */
public class Configuracio implements RegwebConstantes {

	protected static final Logger log = Logger.getLogger(Configuracio.class);

  public static boolean isCAIB() {
    return Boolean.getBoolean(REGWEB_PROPERTY_BASE + "iscaib");
  }

    public static String getUrlPreregistre() {
        return System.getProperty(REGWEB_PROPERTY_BASE + "preregistre");
    }
  
  
  public static boolean showTimeStamp() {
    return Boolean.getBoolean(REGWEB_PROPERTY_BASE + "showtimestamp");
  }

  
  public static boolean isDevelopment() {
    return Boolean.getBoolean(REGWEB_PROPERTY_BASE + "development");
  }

  public static String getDir3CaibUsername() {
    return System.getProperty(REGWEB_PROPERTY_BASE + "dir3caib.username");
  }

  public static String getDir3CaibPassword() {
    return System.getProperty(REGWEB_PROPERTY_BASE + "dir3caib.password");
  }

  public static String getDir3CaibServer() {
    return System.getProperty(REGWEB_PROPERTY_BASE + "dir3caib.server");
  }

  public static String getHibernateDialect() {
    return  System.getProperty(RegwebConstantes.REGWEB_PROPERTY_BASE + "hibernate.dialect");
  }
  
  public static String getDefaultLanguage() {
    String def = System.getProperty(REGWEB_PROPERTY_BASE + "defaultlanguage");
    return (def == null)? RegwebConstantes.IDIOMA_CATALAN_CODIGO : def;
  }
  
  
  public static String getSirServerBase() {
    return  System.getProperty(RegwebConstantes.REGWEB_PROPERTY_BASE + "sir.serverbase");
  }
  
  
  
  public static boolean useDirectApiSir() {
    return "true".equals(
        System.getProperty(RegwebConstantes.REGWEB_PROPERTY_BASE + "sir.usedirectapi"));
  }


	public static List<TipoScan> getTipusScanejat(Locale locale, String noScanName){
		String[] values = new String[] {"0"};
		try {
			String plugins = System.getProperty(RegwebConstantes.REGWEB_PROPERTY_BASE  + "scan.plugins");
			if (plugins != null && !"".equals(plugins))
				values = plugins.split(",");
			
//			log.info("SCAN: Codis de plugins d'escaneig: " + plugins);
		} catch (Exception e) {
//			log.error("SCAN: Error al obtenir els plugins definits al sistema", e);
		}

		List<TipoScan> tiposScan = new ArrayList<TipoScan>();
		for(String value: values) {
			try {
				Integer codigo = Integer.parseInt(value.trim());
				String nombre = codigo.equals(0) ? noScanName : ScannerManager.getName(codigo, locale);
				TipoScan tipoScan = new TipoScan(codigo, nombre);
				tiposScan.add(tipoScan);
//				log.info("SCAN:   " + codigo + " - " + nombre);
			} catch (Exception e){
				log.warn("SCAN: El codi " + value + " no és un codi de tipus d'escanejat vàlid.");
			}
		}
		return tiposScan;
		//	  return Arrays.asList(values);
	}
  

}
