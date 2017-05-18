package es.caib.regweb3.model.utils;


/**
 *
 */
public enum EstadoRegistroSir {

    RECIBIDO("0", "Recibido"),
    ACEPTADO("1", "Aceptado"),
    REENVIADO("2", "Reenviado"),
    REENVIADO_Y_ACK("3", "Reenviado y ACK"),
    REENVIADO_Y_ERROR("4", "Reenviado y ERROR"),
    RECHAZADO("5", "Rechazado"),
    RECHAZADO_Y_ACK("6", "Rechazado y ACK"),
    RECHAZADO_Y_ERROR("7", "Rechazado y ERROR");

    private final String value;
    private final String name;


    EstadoRegistroSir(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static EstadoRegistroSir getEstadoRegistroSir(String value) {

        if (value != null) {

            for (EstadoRegistroSir e : EstadoRegistroSir.values()) {
                if (value.equals(e.getValue())) return e;
            }

        }

        return null;
    }

}