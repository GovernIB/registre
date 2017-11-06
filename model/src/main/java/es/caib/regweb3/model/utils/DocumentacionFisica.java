package es.caib.regweb3.model.utils;


/**
 *
 */
public enum DocumentacionFisica {

    DOCUMENTACION_FISICA_REQUERIDA("1", "Documentación adjunta en soporte PAPEL (u otros soportes)"),
    DOCUMENTACION_FISICA_COMPLEMENTARIA("2", "Documentación adjunta digitalizada y complementariamente en papel"),
    SIN_DOCUMENTACION_FISICA("3", "Documentación adjunta digitalizada");

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

        DocumentacionFisica documentacionFisica = getDocumentacionFisica(value);

        if (documentacionFisica != null) {

            return documentacionFisica.getValue();
        }

        return null;
    }

}