package es.caib.regweb3.persistence.utils;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.RegistroSir;

public class RespuestaRecepcionSir {

    private RegistroSir registroSir ;
    private Boolean ack = true;
    private Entidad entidad;

    public RespuestaRecepcionSir() {
    }

    public RegistroSir getRegistroSir() {
        return registroSir;
    }

    public void setRegistroSir(RegistroSir registroSir) {
        this.registroSir = registroSir;
    }

    public Boolean getAck() {
        return ack;
    }

    public void setAck(Boolean ack) {
        this.ack = ack;
    }

    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }
}
