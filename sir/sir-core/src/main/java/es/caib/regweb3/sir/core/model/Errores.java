package es.caib.regweb3.sir.core.model;


/**
 *
 */
public enum Errores {

    OK("Exito", "00"),
    ERROR_0037("ERROR EN LA VALIDACION FORMATO XML", "0037"),
    ERROR_0065("ERROR EN EL ASIENTO", "0065"),

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