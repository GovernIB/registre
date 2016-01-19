package es.caib.regweb3.sir.core.model;


/**
 *
 */
public enum TipoRegistro {

    ENTRADA("0", "Registro de Entrada"),
    SALIDA("1", "Registro de Salida");

    private final String value;
    private final String name;


    TipoRegistro(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static TipoRegistro getTipoRegistro(String value) {

        if (value != null) {

            for (TipoRegistro e : TipoRegistro.values()) {
                if (value.equals(e.getValue())) return e;
            }

        }

        return null;
    }

}