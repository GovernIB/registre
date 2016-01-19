package es.caib.regweb3.sir.core.model;


/**
 *
 */
public enum TipoAnotacion {

    PENDIENTE("01", "Pendiente"),
    ENVIO("02", "Envío"),
    REENVIO("03", "Reenvío"),
    RECHAZO("04", "Rechazo");

    private final String value;
    private final String name;


    TipoAnotacion(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static TipoAnotacion getTipoAnotacion(String value) {

        if (value != null) {

            for (TipoAnotacion e : TipoAnotacion.values()) {
                if (value.equals(e.getValue())) return e;
            }

        }

        return null;
    }

}