package es.caib.regweb3.webapp.utils;

public class TipoCola {

    private Long id;
    private Boolean activa;
    private Long pendientes;

    public TipoCola(Long id, Boolean activa, Long pendientes) {
        this.id = id;
        this.activa = activa;
        this.pendientes = pendientes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public Long getPendientes() {
        return pendientes;
    }

    public void setPendientes(Long pendientes) {
        this.pendientes = pendientes;
    }
}
