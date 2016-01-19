package es.caib.regweb3.sir.core.model;


/**
 *
 */
public enum DocumentacionFisica {

    DOCUMENTACION_FISICA_REQUERIDA("01", "Acompaña documentación física requerida"),
    DOCUMENTACION_FISICA_COMPLEMENTARIA("02", "Acompaña documentación física complementaria"),
    SIN_DOCUMENTACION_FISICA("03", "No acompaña documentación física");

    private final String value;
    private final String name;


    DocumentacionFisica(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static DocumentacionFisica getDocumentacionFisica(String value) {

        if (value != null) {

            for (DocumentacionFisica e : DocumentacionFisica.values()) {
                if (value.equals(e.getValue())) return e;
            }

        }

        return null;
    }

}