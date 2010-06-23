/*
 * Conf.java
 *
 * Created on 21 de diciembre de 2001, 11:32
 */

package es.caib.regweb.logic.helper;

/**
 * Classe per gestionar configuració del REGWEB.
 * @author  jjnavarro
 * @version 1.0
 */

public class Conf {

	public static String get(String clave) {
	    return System.getProperty("es.caib.regweb."+clave);
	}

	public static String get(String clave, String valorDefecto) {
	    return System.getProperty("es.caib.regweb."+clave, valorDefecto);
	}

}