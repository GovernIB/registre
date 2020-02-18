package es.caib.regweb3.utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Fundació BIT.
 * Agrupa funcionalidades comunes con {@link java.lang.String}
 * @author earrivi
 * Date 9/10/14.
 */
public class StringUtils {

    public final Logger log = Logger.getLogger(getClass());
    private static final List<String> excepciones = Arrays.asList("de", "la", "y", "del", "el", "los", "las");


    /**
     * Comprueba si una cadena está vacia ("") o es null.
     * @param cadena
     * @return
     */
    public static boolean isEmpty(final String cadena) {
        return cadena == null || cadena.length() == 0;
    }

    /**
     * Comprueba si una cadena no está vacia ("") o es null.
     * @param cadena
     * @return
     */
    public static boolean isNotEmpty(final String cadena) {
        return !isEmpty(cadena);
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

    /**
     * Función que convierte la primera letra de cada palabra en mayuscula
     * @param texto
     * @return
     */
    public static String capitailizeWord(String texto) {

        if(isNotEmpty(texto)){

            StringBuilder s = new StringBuilder();

            // Separamos las palabras
            String[] palabras = texto.split("\\s");

            // Recorremos cada palabra
            for (String palabra : palabras) {

                palabra = palabra.toLowerCase();

                if(!excepciones.contains(palabra)){ // Si no es una excepción la capitalizamos

                    // Declare a character of space
                    // To identify that the next character is the starting
                    // of a new word
                    char ch = ' ';
                    for (int i = 0; i < palabra.length(); i++) {

                        // If previous character is space and current
                        // character is not space then it shows that
                        // current letter is the starting of the word
                        if (ch == ' ' && palabra.charAt(i) != ' ')
                            s.append(Character.toUpperCase(palabra.charAt(i)));
                        else
                            s.append(palabra.charAt(i));
                        ch = palabra.charAt(i);
                    }
                    s.append(" ");
                }else{
                    s.append(palabra).append(" ");
                }

            }

            // Return the string with trimming
            return s.toString().trim();
        }

        return null;
    }
}
