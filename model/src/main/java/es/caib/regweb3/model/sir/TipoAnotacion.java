package es.caib.regweb3.model.sir;


/**
 *
 */
public enum TipoAnotacion {

    PENDIENTE("01", "Pendiente"),
    ENVIO("02", "Envío"),
    REENVIO("03", "Reenvío");
   // RECHAZO("04", "Rechazo");  //TODO SICRES4

    private final String value;
    private final String name;


    TipoAnotacion(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static TipoAnotacion getTipoAnotacion(String value) {

        if (value != null) {

            for (TipoAnotacion tipoAnotacion : TipoAnotacion.values()) {
                if (value.equals(tipoAnotacion.getValue())) return tipoAnotacion;
            }

        }

        return null;
    }

    public static String getTipoAnotacionValue(String value) {

        TipoAnotacion tipoAnotacion = getTipoAnotacion(value);

        if(tipoAnotacion != null){
            return  tipoAnotacion.getValue();
        }

        return null;
    }

}