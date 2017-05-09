package es.caib.regweb3.model.utils;


/**
 *
 */
public enum EstadoRegistroSir {

    PENDIENTE_ENVIO("0", "Pendiente de envío"),
    ENVIADO("1", "Enviado"),
    ENVIADO_Y_ACK("2", "Enviado y ACK"),
    ENVIADO_Y_ERROR("3", "Enviado y ERROR"),
    DEVUELTO("4", "Devuelto"),
    ACEPTADO("5", "Aceptado"),
    REENVIADO("6", "Reenviado"),
    REENVIADO_Y_ACK("7", "Reenviado y ACK"),
    REENVIADO_Y_ERROR("8", "Reenviado y ERROR"),
    ANULADO("9", "Anulado"),
    RECIBIDO("10", "Recibido"),
    RECHAZADO("11", "Rechazado"),
    RECHAZADO_Y_ACK("12", "Rechazado y ACK"),
    RECHAZADO_Y_ERROR("13", "Rechazado y ERROR"),
    VALIDADO("14", "Validado"),
    REINTENTAR_VALIDACION("15", "Reintentar validación");

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