package es.caib.regweb3.model.sir;


/**
 *
 */
public enum Errores {

    OK("Exito", "00"),
    ERROR_0037("ERROR EN LA VALIDACION FORMATO XML", "0037"),
    ERROR_0039("ERROR, NO SE PUEDE ALCANZAR EL DESTINO", "0039"),
    ERROR_0046("ERROR, OFICINA SIN URL", "0046"),
    ERROR_0057("ERROR ALMACENAR TRAZABILIDAD WS EN REGISTRO", "0057"),
    ERROR_0065("ERROR EN EL ASIENTO", "0065"),
    ERROR_0058("ERROR AL COMPROBAR LA INTEGRACION CON LA APLICACION", "0058"),

    //Errores propios
    ERROR_NO_CONTROLADO("ERROR NO CONTROLADO", "-1"),
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