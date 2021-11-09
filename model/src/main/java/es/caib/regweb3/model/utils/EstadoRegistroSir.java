package es.caib.regweb3.model.utils;


/**
 *
 */
public enum EstadoRegistroSir {

//    RECIBIDO("0", "Recibido"),
//    ACEPTADO("1", "Aceptado"),
//    REENVIADO("2", "Reenviado"),
//    REENVIADO_Y_ACK("3", "Reenviado y ACK"),
//    REENVIADO_Y_ERROR("4", "Reenviado y ERROR"),
//    RECHAZADO("5", "Rechazado"),
//    RECHAZADO_Y_ACK("6", "Rechazado y ACK"),
//    RECHAZADO_Y_ERROR("7", "Rechazado y ERROR"),
//    ELIMINADO("8", "ELIMINADO");
	SIN_DATOS("0", "Sin datos"),
	PENDIENTE_ENVIO("1", "Pendiente de envio al destino"),
	ENVIADO_PENDIENTE_CONFIRMACION("2", "Enviado pendiente de confirmación"),
	ENVIADO_PENDIENTE_CONFIRMACION_MANUAL("3", "Enviado pendiente de confirmación el destino es desconectado"),
	RECIBIDO_PENDIENTE_CONFIRMACION("4", "Recibido pendiente de confirmación"),
	RECIBIDO_PENDIENTE_CONFIRMACION_MANUAL("5", "Recibido pendiente de confirmación el destino es desconectado"),
	ENVIADO_CONFIRMADO("6", "Confirmado en destino (estado solo visible desde origen)"),
	RECIBIDO_CONFIRMADO("7", "Confirmado en destino (estado solo visible desde destino)"),
	ENVIADO_RECHAZADO("8", "Rechazado en destino (estado solo visible desde origen)"),
	RECIBIDO_RECHAZADO("9", "Rechazado en destino (estado solo visible desde destino)"),
	ANULADO("10", "Anulado"),
	REENVIADO("11", "Reenviado"),
	EN_TRAMITE("12", "Confirmado por unidad tramitadora"),
	ASIGNADO("13", "Asignado a un subórgano"),
	FINALIZADO("14", "Último estado de un asiento"),
	REENVIADO_RECHAZADO("15", "Reenviado y rechazado por destino a la oficina del último reenvío"),
	RECTIFICADO("16", "Modificado para su envío por SIR"),
	ENVIO_PROCESO("17", "Estado temporal pendiente de ser enviado"),
	RECIBIDO_RECHAZADO_CIUDADANO("18", "Estado rechazado de una unidad a su oficina de registro con destino a un interesado"),
	ELIMINADO("19", "ELIMINADO");
	
	
    private final String value;
    private final String name;


    EstadoRegistroSir(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static EstadoRegistroSir getEstadoRegistroSir(String value) {

        if (value != null) {

            for (EstadoRegistroSir e : EstadoRegistroSir.values()) {
                if (value.equals(e.getValue())) return e;
            }

        }

        return null;
    }
    
    public static EstadoRegistroSir getEstadoRegistroSirByName(String name) {
        if (name != null) {

            for (EstadoRegistroSir e : EstadoRegistroSir.values()) {
                if (name.equals(e.name())) return e;
            }

        }
        return null;
    }

}