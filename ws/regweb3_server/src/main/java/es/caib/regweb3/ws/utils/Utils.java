package es.caib.regweb3.ws.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Limit Tecnologies
 * 
 */
public class Utils {

	public static int formatNumeroRegistro(String numeroRegistroFormateado) {
		int numeroRegistro = 0;
		if (numeroRegistroFormateado != null) {
			Pattern pattern = Pattern.compile("(\\d+)$");
			Matcher m = pattern.matcher(numeroRegistroFormateado);
			m.find();
			numeroRegistro = Integer.valueOf(m.group(1));
		}
		return numeroRegistro;
	}
}
