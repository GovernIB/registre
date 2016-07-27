package es.caib.regweb3.sir.core.model;


/**
 *
 */
public enum Errores {

    OK("Exito", "00"),
    ERROR_INESPERADO("Error inesperado", "-1"),
    ERROR_0037("ERROR_VALIDACION_FORMATO_XML", "0037"),
    ERROR_0038("ERROR_ALMACENAR_TRAZABILIDAD_WS", "0038"),
    ERROR_0041("ERROR_CAMPO_TIPO_REGISTRO", "0041"),
    ERROR_0043("ERROR_CONSULTAR_CERTIFICADO", "0043"),
    ERROR_0044("ERROR_AL_RECIBIR_MENSAJE", "0044"),
    ERROR_0045("ERROR_FICHEROS_ADJUNTOS", "0045"),
    ERROR_0047("ERROR_NOMBRE_ZIP", "0047"),
    ERROR_0048("ERROR_DESCOMPRIMIR_FICHERO", "0048"),
    ERROR_0049("ERROR_DESCOMPRIMIR_MENSAJE", "0049"),
    ERROR_0055("ERROR_GESTIONAR_REINTENTOS_REGISTRO", "0055"),
    ERROR_0056("ERROR_GESTIONAR_REINTENTOS_MENSAJE", "0056"),
    ERROR_0057("ERROR_ALMACENAR_TRAZABILIDAD_REGISTRO", "0057"),
    ERROR_0059("ERROR_GESTION_MENSAJES_ERROR", "0059"),
    ERROR_0063("ERROR_AL_RECIBIR_ASIENTO", "0063"),
    ERROR_0064("ERROR_EN_EL_MENSAJE", "0064"),
    ERROR_0065("ERROR_EN_EL_ASIENTO", "0065"),
    ERROR_0066("ERROR_AL_EXTRAER", "0066"),
    ERROR_0205("ERROR, DUPLICIDAD DE FICHERO DE INTERCAMBIO EN DESTINO", "0205"),
    ERROR_0206("ERROR, DUPLICIDAD DE FICHERO DE MENSAJES EN DESTINO", "0206"),

    //Errores propios
    ERROR_COD_ENTIDAD_INVALIDO("CODIGO_ENTIDAD_INVALIDO", "-2");

    private final String value;
    private final String name;


    Errores(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }


}