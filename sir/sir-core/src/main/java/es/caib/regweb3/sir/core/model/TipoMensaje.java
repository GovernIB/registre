package es.caib.regweb3.sir.core.model;


/**
 *
 */
public enum TipoMensaje {

    ACK("01", "ACK"),
    ERROR("02", "ERROR"),
    CONFIRMACION("03", "CONFIRMACION");

    private final String value;
    private final String name;


    TipoMensaje(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static TipoMensaje getTipoMensaje(String value) {

        if (value != null) {

            for (TipoMensaje e : TipoMensaje.values()) {
                if (value.equals(e.getValue())) return e;
            }

        }

        return null;
    }

}