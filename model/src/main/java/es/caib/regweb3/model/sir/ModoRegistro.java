package es.caib.regweb3.model.sir;

public enum ModoRegistro {

    PRESENCIAL("01", "Presencial"),
    ELECTRONICO("02", "Electrónico");

    private final String value;
    private final String name;


    ModoRegistro(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static ModoRegistro getModoRegistro(String value) {

        if (value != null) {

            for (ModoRegistro modoRegistro : ModoRegistro.values()) {
                if (value.equals(modoRegistro.getValue())) return modoRegistro;
            }

        }

        return null;
    }

    public static String getModoRegistroValue(String value) {

        ModoRegistro modoRegistro = getModoRegistro(value);

        if (modoRegistro != null) {
            return modoRegistro.getValue();
        }

        return null;
    }
}
