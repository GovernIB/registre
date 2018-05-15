package es.caib.regweb3.webapp.form;


import java.util.List;

/**
 * Created by Fundació Bit
 * @author earrivi
 */
public class NotificacionForm {

    private List<Long> destinatarios;
    private Long tipo;
    private String asunto;
    private String mensaje;


    public NotificacionForm() {
    }

    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    public List<Long> getDestinatarios() {
        return destinatarios;
    }

    public void setDestinatarios(List<Long> destinatarios) {
        this.destinatarios = destinatarios;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
