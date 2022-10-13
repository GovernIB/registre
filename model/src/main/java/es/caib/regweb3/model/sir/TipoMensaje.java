package es.caib.regweb3.model.sir;


/**
 *
 */
public enum TipoMensaje {

    ACK("01", "ACK"),
    ERROR("02", "ERROR"),
    CONFIRMACION("03", "CONFIRMACION"),
    ACK_CONFIRMACION("04", "ACK a CONFIRMACION"),
    RECHAZO("05", "RECHAZO"),
    ACK_RECHAZO("06", "ACK a RECHAZO");


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