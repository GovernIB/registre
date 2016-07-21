package es.caib.regweb3.sir.core.model;


/**
 *
 */
public enum DocumentacionFisica {

    DOCUMENTACION_FISICA_REQUERIDA("1", "Acompaña documentación física requerida"),
    DOCUMENTACION_FISICA_COMPLEMENTARIA("2", "Acompaña documentación física complementaria"),
    SIN_DOCUMENTACION_FISICA("3", "No acompaña documentación física");

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

            for (DocumentacionFisica documentacionFisica : DocumentacionFisica.values()) {
                if (value.equals(documentacionFisica.getValue())) return documentacionFisica;
            }

        }

        return null;
    }

    public static String getDocumentacionFisicaValue(String value) {

        if (value != null) {

            for (DocumentacionFisica documentacionFisica : DocumentacionFisica.values()) {
                if (value.equals(documentacionFisica.getValue())) return documentacionFisica.getValue();
            }

        }

        return null;
    }

}