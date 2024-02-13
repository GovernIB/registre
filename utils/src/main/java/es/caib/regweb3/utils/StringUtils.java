package es.caib.regweb3.utils;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Fundació BIT.
 * Agrupa funcionalidades comunes con {@link java.lang.String}
 * @author earrivi
 * Date 9/10/14.
 */
public class StringUtils {

    public final Logger log = LoggerFactory.getLogger(getClass());
    private static final List<String> excepciones = Arrays.asList("de", "la", "y", "del", "el", "los", "las", "SL");
    private static final List<String> mayusculas = Arrays.asList("sl","s.l.","sa","s.a.", "cb", "c.b.");


    /**
     * Comprueba si una cadena está vacia ("") o es null.
     * @param cadena
     * @return
     */
    public static boolean isEmpty(final String cadena) {
        return cadena == null || cadena.isEmpty();
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
     * Dada una cadena, busca si existe alguna ocurrencia de los caracteres prohibidos por ARXIU {@link es.caib.regweb3.utils.RegwebConstantes} y los elimina
     * @param cadena
     * @return
     */
    public static String eliminarCaracteresProhibidosArxiu(String cadena){


        for (String s: RegwebConstantes.CARACTERES_NO_PERMITIDOS_ARXIU) {

            cadena = cadena.replace(s, "");

        }

        return cadena;
    }

    /**
     * Dada una cadena, busca si existe alguna ocurrencia de los caracteres prohibidos por ARXIU {@link es.caib.regweb3.utils.RegwebConstantes} y los sustituye por el caracter indicado
     * @param cadena
     * @return
     */
    public static String sustituirCaracteresProhibidosSIR(String cadena, char sustituto){

        for (char s: RegwebConstantes.CARACTERES_NO_PERMITIDOS_SIR.toCharArray()) {

            cadena = cadena.replace(s, sustituto);
        }

        return cadena;
    }

    /**
     * Dada una cadena, busca si existe alguna ocurrencia de los caracteres prohibidos por SIR {@link es.caib.regweb3.utils.RegwebConstantes} y los elimina
     * @param cadena
     * @return
     */
    public static String eliminarCaracteresProhibidosSIR(String cadena){

        for (char s: RegwebConstantes.CARACTERES_NO_PERMITIDOS_SIR.toCharArray()) {

            cadena = cadena.replace(String.valueOf(s), "");

        }

        return cadena;
    }

    /**
     * Función que recorta un string a una longitud especificada
     * @param cadena cadena a recortar
     * @param length longitud a la que recortar
     * @return
     * @throws Exception
     */
    public static String recortarCadena(String cadena, int length) {
        if(cadena.length() > length){
            return cadena.substring(0,length);
        }
        return cadena;
    }

    /**
     * Función que recorta un string a una longitud especificada
     * @param nombre nombre a recortar
     * @param length longitud a la que recortar
     * @return
     * @throws Exception
     */
    public static String recortarNombre(String nombre, int length)  {
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
    public static String capitailizeWord(String texto, Boolean uppercase) {

        if(isNotEmpty(texto)){

            StringBuilder s = new StringBuilder();

            // Separamos las palabras
            String[] palabras = texto.split("\\s");

            // Recorremos cada palabra
            for (String palabra : palabras) {

                palabra = palabra.toLowerCase();

                if(uppercase && mayusculas.contains(palabra)){
                    s.append(palabra.toUpperCase()).append(" ");

                }else if(!excepciones.contains(palabra)){ // Si no es una excepción la capitalizamos

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

    /**
     * Retorna el literal de un Booleano
     * @param activo
     * @return
     */
    public static String toStringSiNo(Boolean activo){
        return activo ? "Sí" : "No";
    }
}
