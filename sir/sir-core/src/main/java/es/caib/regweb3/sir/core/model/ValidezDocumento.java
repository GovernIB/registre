package es.caib.regweb3.sir.core.model;


/**
 *
 */
public enum ValidezDocumento {

    COPIA("01", "Copia"),
    COPIA_COMPULSADA("02", "Copia compulsada"),
    COPIA_ORIGINAL("03", "Copia original"),
    ORIGINAL("04", "Original");

    private final String value;
    private final String name;


    ValidezDocumento(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static ValidezDocumento getValidezDocumento(String value) {

        if (value != null) {

            for (ValidezDocumento validezDocumento : ValidezDocumento.values()) {
                if (value.equals(validezDocumento.getValue())) return validezDocumento;
            }

        }

        return null;
    }

    public static String getValidezDocumentoValue(String value) {

        ValidezDocumento validezDocumento = getValidezDocumento(value);

        if (validezDocumento != null) {
            return validezDocumento.getValue();

        }

        return null;
    }

}