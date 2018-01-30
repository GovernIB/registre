package es.caib.regweb3.utils;

import org.apache.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date 9/10/14.
 */
public class DocumentoUtils {

    protected final Logger log = Logger.getLogger(getClass());

    private static final String LETRAS_NIF = "TRWAGMYFPDXBNJZSQVHLCKE";

    //private static final String TIPO_ORGANIZACION_CIF = "ABCDEFGHJKLMNPQRSUVW";
    private static final String CODIGO_CONTROL_CIF = "ABCDEFGHIJ";
    private static final String TIPO_ORGANIZACION_LETRA_CIF = "PQSWK";
    private static final String TIPO_ORGANIZACION_NUMERO_CIF = "ABEH";
    private static final String PATRON_CIF = "([abcdefghjklmnpqrsuvwABCDEFGHJKLMNPQRSUVW]{1})([0-9]{7})([0-9a-jA-J]{1})";
    private static final String PATRON_NIF = "([0-9]{8})([a-zA-Z]{1})";
    private static final String PATRON_NIE = "([xyzXYZ]{1})([0-9]{7})([a-zA-Z]{1})";


    /**
     * Función que recorta un string a una longitud especificada
     *
     * @param documento     a validar
     * @param tipoDocumento del documento a validar
     * @return
     * @throws Exception
     */
    public static Validacion comprobarDocumento(String documento, Long tipoDocumento) throws Exception {

        switch (tipoDocumento.intValue()) {

            case (int) RegwebConstantes.TIPODOCUMENTOID_NIF_ID: // NIF (DNI)

                // Si el document comença amb K, L, M
                if(documento.startsWith("k") || documento.startsWith("K") || documento.startsWith("m") || documento.startsWith("M") || documento.startsWith("l") || documento.startsWith("L")) {

                    int codigo_de_control;
                    Pattern pCif = Pattern.compile(PATRON_CIF);
                    Matcher mCif = pCif.matcher(documento);

                    try {

                        if (mCif.matches()) { // Comprueba que el documento tiene el patrón correcto (LETRA + 7 DIGITOS + LETRA/NUMERO)

                            // Obtiene la primera letra del CIF
                            char tipoOrganizacion = documento.charAt(0);

                            // Comprobar el código de control
                            if (Character.isDigit(documento.charAt(8))) { // Si el último carácter es un número
                                codigo_de_control = Character.getNumericValue(documento.charAt(8));  // Codigo de control es el último número

                                if (codigo_de_control == 0) { // Si código de control es 0, le pone el valor 10
                                    codigo_de_control = 10;
                                }

                                if (TIPO_ORGANIZACION_LETRA_CIF.indexOf(tipoOrganizacion) >= 0) {
                                    codigo_de_control = 100;
                                }
                            } else {  // El último carácter es una letra
                                codigo_de_control = CODIGO_CONTROL_CIF.indexOf(documento.charAt(8)) + 1; // Codigo de control es el índice de la letra

                                if (TIPO_ORGANIZACION_NUMERO_CIF.indexOf(tipoOrganizacion) >= 0) {
                                    codigo_de_control = 100;
                                }
                            }

                            // Calcula A (valores de los pares) y B (suma de los dígitos de las posiciones impares *2)
                            int a = 0, b = 0, c;
                            byte[] impares = {0, 2, 4, 6, 8, 1, 3, 5, 7, 9};

                            for (int t = 1; t <= 6; t = t + 2) { // Recorre los números del CIF para calcular A y B
                                a = a + Character.getNumericValue(documento.charAt(t + 1)); // Suma las posiciones pares de la parte numérica (2,4,6)
                                b = b + impares[Character.getNumericValue(documento.charAt(t))];  // Suma las posiciones de los impares dentro de impares[]
                            }
                            b = b + impares[Character.getNumericValue(documento.charAt(7))];  // Añade el último dígito impar a B

                            // Calcula C (para compararlo con el código de control)
                            c = 10 - ((a + b) % 10);

                            // Compara C con el dígito de control
                            if (c != codigo_de_control) {  // Si no coincide, es erróneo
                                return new Validacion(Boolean.FALSE, "error.documento.numero", "El número de nif és incorrecte");
                            }

                        } else {
                            return new Validacion(Boolean.FALSE, "error.dni.incorrecto", "El NIF no té format correcte (LLETRA + 7 NÚMEROS + LLETRA/NÚMERO)");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        return new Validacion(Boolean.FALSE, "error.dni.incorrecto", "El NIF no té format correcte (LLETRA + 7 NÚMEROS + LLETRA/NÚMERO)");
                    }


                } else{   // Si el document NO comença amb K, L, M

                    int valorNumerico = 0;
                    Pattern pNif = Pattern.compile(PATRON_NIF);
                    Matcher mNif = pNif.matcher(documento);

                    try {

                        if (mNif.matches()) { // Comprueba el patrón del dni/nif

                            String numeroNif = documento.substring(0, documento.length() - 1); // Suprimimos la letra final
                            valorNumerico = Integer.parseInt(numeroNif); // Lo transformamos en un Integer

                            // Comprobamos que la letra es correcta
                            if (!documento.endsWith("" + LETRAS_NIF.charAt(valorNumerico % 23))) {
                                return new Validacion(Boolean.FALSE, "error.documento.formato", "Lletra de document incorrecta");
                            }

                        } else { // Patrón incorrecto

                            return new Validacion(Boolean.FALSE, "error.dni.incorrecto", "El NIF no té format correcte (8 DIGITS + LLETRA)");
                        }

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return new Validacion(Boolean.FALSE, "error.dni.incorrecto", "El NIF no té format correcte (8 DIGITS + LLETRA)");
                    }

                }

            break;

            case (int) RegwebConstantes.TIPODOCUMENTOID_CIF_ID: // CIF

                int codigo_de_control;
                Pattern pCif = Pattern.compile(PATRON_CIF);
                Matcher mCif = pCif.matcher(documento);

                try {

                    if (mCif.matches()) { // Comprueba que el documento tiene el patrón correcto (LETRA + 7 DIGITOS + LETRA/NUMERO)

			             // Obtiene la primera letra del CIF
                        char tipoOrganizacion = documento.charAt(0);

                        // Comprobar el código de control
                        if (Character.isDigit(documento.charAt(8))) { // Si el último carácter es un número
                            codigo_de_control = Character.getNumericValue(documento.charAt(8));  // Codigo de control es el último número

                            if (codigo_de_control == 0) { // Si código de control es 0, le pone el valor 10
                                codigo_de_control = 10;
                            }

                            if (TIPO_ORGANIZACION_LETRA_CIF.indexOf(tipoOrganizacion) >= 0) {
                                codigo_de_control = 100;
                            }
                        } else {  // El último carácter es una letra
                            codigo_de_control = CODIGO_CONTROL_CIF.indexOf(documento.charAt(8)) + 1; // Codigo de control es el índice de la letra

                            if (TIPO_ORGANIZACION_NUMERO_CIF.indexOf(tipoOrganizacion) >= 0) {
                                codigo_de_control = 100;
                            }
                        }

                        // Calcula A (valores de los pares) y B (suma de los dígitos de las posiciones impares *2)
                        int a = 0, b = 0, c;
                        byte[] impares = {0, 2, 4, 6, 8, 1, 3, 5, 7, 9};

                        for (int t = 1; t <= 6; t = t + 2) { // Recorre los números del CIF para calcular A y B
                            a = a + Character.getNumericValue(documento.charAt(t + 1)); // Suma las posiciones pares de la parte numérica (2,4,6)
                            b = b + impares[Character.getNumericValue(documento.charAt(t))];  // Suma las posiciones de los impares dentro de impares[]
                        }
                        b = b + impares[Character.getNumericValue(documento.charAt(7))];  // Añade el último dígito impar a B

                        // Calcula C (para compararlo con el código de control)
                        c = 10 - ((a + b) % 10);

                        // Compara C con el dígito de control
                        if (c != codigo_de_control) {  // Si no coincide, es erróneo
                            return new Validacion(Boolean.FALSE, "error.cif.numeroIncorrecto", "El número de cif és incorrecte");
                        }

                    } else {
                        return new Validacion(Boolean.FALSE, "error.cif.incorrecto", "El CIF no té format correcte (LLETRA + 7 NÚMEROS + LLETRA/NÚMERO)");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return new Validacion(Boolean.FALSE, "error.cif.incorrecto", "El CIF no té format correcte (LLETRA + 7 NÚMEROS + LLETRA/NÚMERO)");
                }

            break;

            case (int) RegwebConstantes.TIPODOCUMENTOID_NIE_ID: // NIE

                Pattern pNie = Pattern.compile(PATRON_NIE);
                Matcher mNie = pNie.matcher(documento);

                int valorNumerico;
                if (mNie.matches()) { //Comprueba el formato del NIE con el patrón

                    try {
                        String numeroNie = documento.substring(1, documento.length() - 1); // Suprimimos la letra inicial y final
                        valorNumerico = Integer.parseInt(numeroNie); // Lo transformamos en un Integer

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return new Validacion(Boolean.FALSE, "error.nie.incorrecto", "El nie no té format correcte (LLETRA(X,Y,Z) + 7 DIGITS + LLETRA)");
                    }


                    if (documento.startsWith("X") || documento.startsWith("x")) { // Si el documento empieza con X


                    } else if (documento.startsWith("Y") || documento.startsWith("y")) { // Si el documento empieza con Y

                        valorNumerico = valorNumerico + 10000000;


                    } else if (documento.startsWith("Z") || documento.startsWith("z")) { // Si el documento empieza con Z

                        valorNumerico = valorNumerico + 20000000;

                    }

                } else {
                    return new Validacion(Boolean.FALSE, "error.nie.incorrecto", "El nie no té format correcte (LLETR(X,Y,Z) + 7 DIGITS + LLETRA)");

                }

                //Comprueba que la letra final sea correcta
                if (!documento.endsWith("" + LETRAS_NIF.charAt(valorNumerico % 23))) {

                    return new Validacion(Boolean.FALSE, "error.nie.numeroIncorrecto", "El número de nie és incorrecte");
                }

            break;

            case (int) RegwebConstantes.TIPODOCUMENTOID_PASSAPORT_ID: // PASAPORTE
                //return new Validacion(Boolean.TRUE, "", "");
            break;

            case (int) RegwebConstantes.TIPODOCUMENTOID_PERSONA_FISICA_ID: // OTRO DE PERSONA FISICA
                //return new Validacion(Boolean.TRUE, "", "");
            break;

            case (int) RegwebConstantes.TIPODOCUMENTOID_CODIGO_ORIGEN_ID: // CODIGO ORIGEN
                //return new Validacion(Boolean.TRUE, "", "");
            break;

        }

        return new Validacion(Boolean.TRUE, "", "");

    }
}
