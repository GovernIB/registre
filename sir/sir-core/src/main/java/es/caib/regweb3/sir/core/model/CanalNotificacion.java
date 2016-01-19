package es.caib.regweb3.sir.core.model;


/**
 *
 */
public enum CanalNotificacion {

    DIRECCION_POSTAL("01", "Dirección Postal"),
    DIRECCION_ELECTRONICA_HABILITADA("02", "Dirección electrónica habilitada"),
    COMPARECENCIA_ELECTRONICA("03", "Comparecencia electrónica");

    private final String value;
    private final String name;


    CanalNotificacion(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static CanalNotificacion getCanalNotificacion(String value) {

        if (value != null) {

            for (CanalNotificacion e : CanalNotificacion.values()) {
                if (value.equals(e.getValue())) return e;
            }

        }

        return null;
    }

}