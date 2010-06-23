/*
 * Conf.java
 *
 * Created on 21 de diciembre de 2001, 11:32
 */

package es.caib.regweb;

import java.util.Properties;
import java.net.*;
import java.io.*;

/**
 * Classe per gestionar configuració del REGWEB.
 * @author  jjnavarro
 * @version 1.0
 */

public class Conf {
    // private static final String CONF_FILE="as400.properties";
    // private static URL confURL;
    // private static Properties confProp= new Properties();
	
	// TODO no cargar propiedades as400, usar el System.getProperty para leerlas de un sar
    // static {
    //  try {
    //      cargaFichero();
    //  } catch (IOException e) {
    //      System.out.println("Excepció en obrir el fitxer de propietats de l'AS400."+e.getMessage());
    //  }
    // }
	
    // public static void cargaFichero() throws IOException {
    //  confURL = Conf.class.getClassLoader().getResource(CONF_FILE);
    //  if (confURL == null) {
    //      confURL = ClassLoader.getSystemResource(CONF_FILE);
    //  }
    //  confProp.clear();
    //  confProp.load(confURL.openStream());
    // }
	
	public static String get(String clave) {
	    return System.getProperty("es.caib.regweb."+clave);
	}
	
	public static String get(String clave, String valorDefecto) {
	    return System.getProperty("es.caib.regweb."+clave, valorDefecto);
	}
	
    // public static String lista() throws IOException {
    //  return confProp.toString();
    // }
}
