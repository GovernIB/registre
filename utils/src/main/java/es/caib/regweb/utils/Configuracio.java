package es.caib.regweb.utils;


/**
 * 
 * @author anadal
 * 
 */
public class Configuracio implements RegwebConstantes {


  public static boolean isCAIB() {
    return Boolean.getBoolean(REGWEB_PROPERTY_BASE + "iscaib");
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
  

}
