package es.caib.regweb3.sir.core.model;


/**
 *
 */
public enum TipoDocumentoIdentificacion {

    NIF("N", "NIF"),
    CIF("C", "CIF"),
    PASAPORTE("P", "Pasaporte"),
    NIE("E", "NIE"),
    OTROS_PERSONA_FISICA("X", "Otros de persona física"),
    CODIGO_ORIGEN_VALUE("O", "Código de origen");

    private final String value;
    private final String name;


    TipoDocumentoIdentificacion(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static TipoDocumentoIdentificacion getTipoDocumentoIdentificacion(String value) {

        if (value != null) {

            for (TipoDocumentoIdentificacion e : TipoDocumentoIdentificacion.values()) {
                if (value.equals(e.getValue())) return e;
            }

        }

        return null;
    }

}