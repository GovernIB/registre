package es.caib.regweb3.sir.core.model;


/**
 *
 */
public enum IndicadorPrueba {

    NORMAL("0", "NORMAL"),
    PRUEBA("1", "PRUEBA");

    private final String value;
    private final String name;


    IndicadorPrueba(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static IndicadorPrueba getIndicadorPrueba(String value) {

        if (value != null) {

            for (IndicadorPrueba e : IndicadorPrueba.values()) {
                if (value.equals(e.getValue())) return e;
            }

        }

        return null;
    }


}