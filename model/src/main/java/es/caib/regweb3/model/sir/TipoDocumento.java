package es.caib.regweb3.model.sir;


/**
 *
 */
public enum TipoDocumento {

    FORMULARIO("01", "Formulario"),
    DOCUMENTO_ADJUNTO("02", "Documento adjunto al formulario"),
    OTRO("03", "Otro"); //SICRES4

    private final String value;
    private final String name;


    TipoDocumento(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static TipoDocumento getTipoDocumento(String value) {

        if (value != null) {

            for (TipoDocumento tipoDocumento : TipoDocumento.values()) {
                if (value.equals(tipoDocumento.getValue())) return tipoDocumento;
            }

        }

        return null;
    }

    public static String getTipoDocumentoValue(String value) {

        TipoDocumento tipoDocumento = getTipoDocumento(value);

        if (tipoDocumento != null) {

            return tipoDocumento.getValue();
        }

        return null;
    }

}