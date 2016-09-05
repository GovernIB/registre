package es.caib.regweb3.utils;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.apache.log4j.Logger;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date 9/10/14.
 */
public class DocumentoUtils {

    protected final Logger log = Logger.getLogger(getClass());

    private static final String LETRAS_NIF = "TRWAGMYFPDXBNJZSQVHLCKE";

    private static final String TIPO_ORGANIZACION_CIF = "ABCDEFGHJKLMNPQRSUVW";
    private static final String CODIGO_CONTROL_CIF = "ABCDEFGHIJ";
    private static final String TIPO_ORGANIZACION_LETRA_CIF = "PQSWK";
    private static final String TIPO_ORGANIZACION_NUMERO_CIF = "ABEH";


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

                int valorNumerico = 0;

                if (documento.length() == 9) { // Comprueba largaria del dni/nif

                    try {
                        String numeroNif = documento.substring(0, documento.length() - 1); // Suprimimos la letra final
                        valorNumerico = Integer.parseInt(numeroNif); // Lo transformamos en un Integer

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return new Validacion(Boolean.FALSE, "error.dni.incorrecto", "El dni no té format correcte (8 DIGITS + LLETRA)");
                    }

                    // Comprobamos que la letra es correcta
                    if (!documento.endsWith("" + LETRAS_NIF.charAt(valorNumerico % 23))) {
                        return new Validacion(Boolean.FALSE, "error.documento.formato", "Lletra de document incorrecta");
                    }

                } else { // Largo incorrecto

                    return new Validacion(Boolean.FALSE, "error.document.largo", "Llargària de document incorrecta");
                }

            break;

            case (int) RegwebConstantes.TIPODOCUMENTOID_CIF_ID: // CIF

                int codigo_de_control;

                try {

                    if (documento.length() == 9) { // Un CIF tiene que tener nueve dígitos

			             // Obtiene la primera letra del CIF
                        char tipoOrganizacion = documento.charAt(0);

                        // Comprueba si la primera letra del CIF es válida
                        if (TIPO_ORGANIZACION_CIF.indexOf(tipoOrganizacion) >= 0) {

                            // Comprueba que el cif es numérico
                            try {
                                String cif = documento.substring(1, documento.length() - 1); // Suprimimos la letra inicial y final
                                valorNumerico = Integer.parseInt(cif); // Lo transformamos en un Integer

                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                                return new Validacion(Boolean.FALSE, "error.cif.incorrecto", "El cif no té format correcte (LLETRA + 7 DIGITS + LLETRA/DIGIT)");
                            }

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
                            return new Validacion(Boolean.FALSE, "error.cif.incorrecto", "El CIF no té format correcte (LLETRA + 8 DIGITS)");
                        }

                    } else {
                        return new Validacion(Boolean.FALSE, "error.document.largo", "Llargària de document incorrecta");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return new Validacion(Boolean.FALSE, "error.cif.incorrecto", "El CIF no té format correcte (LLETRA + 8 DIGITS)");
                }

            break;

            case (int) RegwebConstantes.TIPODOCUMENTOID_NIE_ID: // NIE

                if (documento.length() == 9) { //Comprueba la longitud del documento

                    // Si el documento no termina con Caracter
                    if (Character.isDigit(documento.charAt(documento.length() - 1))) {

                        return new Validacion(Boolean.FALSE, "error.nie.incorrecto", "El nie no té format correcte (LLETRA(X,Y,Z) + 7 DIGITS + LLETRA)");
                    }

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

                    } else {
                        return new Validacion(Boolean.FALSE, "error.nie.incorrecto", "El nie no té format correcte (LLETR(X,Y,Z) + 7 DIGITS + LLETRA)");
                    }

                } else {
                    return new Validacion(Boolean.FALSE, "error.document.largo", "Llargària de document incorrecta");

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
