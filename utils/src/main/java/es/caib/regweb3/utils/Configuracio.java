package es.caib.regweb3.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;


/**
 * 
 * @author anadal
 * 
 */
public class Configuracio implements RegwebConstantes {

	protected static final Logger log = LoggerFactory.getLogger(Configuracio.class);


    static {
        String propertyFileName = System.getProperty("es.caib.regweb3.properties");
        try (Reader reader = new FileReader(propertyFileName)){
            System.getProperties().load(reader);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }


    public static boolean isCAIB() {
      return Boolean.getBoolean(REGWEB3_PROPERTY_BASE + "iscaib");
    }

    public static String getUrlPreregistre() {
        return System.getProperty(REGWEB3_PROPERTY_BASE + "preregistre");
    }
  
  
    public static boolean showTimeStamp() {
    return Boolean.getBoolean(REGWEB3_PROPERTY_BASE + "showtimestamp");
  }

  
    public static boolean isDevelopment() {
    return Boolean.getBoolean(REGWEB3_PROPERTY_BASE + "development");
  }


    public static String getHibernateDialect() {
      return  System.getProperty(RegwebConstantes.REGWEB3_PROPERTY_BASE + "hibernate.dialect");
    }

    public static String getDefaultLanguage() {
      String def = System.getProperty(REGWEB3_PROPERTY_BASE + "defaultlanguage");
      return (def == null)? RegwebConstantes.IDIOMA_CATALAN_CODIGO : def;
    }

    public static String getSirServerBase() {
      return  System.getProperty(RegwebConstantes.REGWEB3_PROPERTY_BASE + "sir.serverbase");
    }

    public static String getArchivosPath() {
      return System.getProperty(REGWEB3_PROPERTY_BASE + "archivos.path");
    }

}