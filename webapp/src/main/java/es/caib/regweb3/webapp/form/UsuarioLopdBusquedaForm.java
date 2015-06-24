package es.caib.regweb3.webapp.form;

import java.util.Date;

/**
 * Created 15/09/14 14:37
 * @author jpernia
 */
public class UsuarioLopdBusquedaForm {

    private Date fechaInicio;
    private Date fechaFin;
    private Long usuario;
    private Long libro;

    public UsuarioLopdBusquedaForm(Date fechaInicio, Date fechaFin, Long usuario, Long libro) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.usuario = usuario;
        this.libro = libro;
    }

    public UsuarioLopdBusquedaForm() {

    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(Long usuario) {
        this.usuario = usuario;
    }

    public Long getLibro() {
        return libro;
    }

    public void setLibro(Long libro) {
        this.libro = libro;
    }
}
