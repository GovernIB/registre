package es.caib.regweb3.sir.core.model;


/**
 *
 */
public enum TipoTransporte {

    SERVICIO_MENSAJEROS("01", "Servicio de mensajeros"),
    CORREO_POSTAL("02", "Correo postal"),
    CORREO_POSTAL_CERTIFICADO("03", "Correo postal certificado"),
    BUROFAX("04", "Burofax"),
    EN_MANO("05", "En mano"),
    FAX("06", "Fax"),
    OTROS("07", "Otros");

    private final String value;
    private final String name;


    TipoTransporte(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static TipoTransporte getTipoTransporte(String value) {

        if (value != null) {

            for (TipoTransporte e : TipoTransporte.values()) {
                if (value.equals(e.getValue())) return e;
            }

        }

        return null;
    }

}