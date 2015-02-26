package es.caib.regweb.utils;

import org.apache.log4j.Logger;
import org.apache.commons.io.FilenameUtils;

/**
 * Created by Fundació BIT.
 * Agrupa funcionalidades comunes con {@link java.lang.String}
 * @author earrivi
 * Date 9/10/14.
 */
public class StringUtils {

    public final Logger log = Logger.getLogger(getClass());
   

    /**
     * Comprueba si una cadena está vacia ("") o es null.
     * @param cadena
     * @return
     */
    public static boolean isEmpty(final String cadena) {
        return cadena == null || cadena.length() == 0;
    }


    /**
     * Función que recorta un string a una longitud especificada
     * @param nombre nombre a recortar
     * @param length longitud a la que recortar
     * @return
     * @throws Exception
     */
    public static String recortarNombre(String nombre, int length) throws Exception{
        String nombreSinExt = FilenameUtils.removeExtension(nombre);
        String extension = FilenameUtils.getExtension(nombre);
        if(nombreSinExt.length()> length){
          nombreSinExt = nombreSinExt.substring(0,length);
        }
        return  nombreSinExt + "."+ extension;
    }
}
