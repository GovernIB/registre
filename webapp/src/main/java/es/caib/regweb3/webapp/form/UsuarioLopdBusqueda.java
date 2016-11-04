package es.caib.regweb3.webapp.form;

import java.util.Date;

/**
 * Created 15/09/14 14:37
 * @author jpernia
 */
public class UsuarioLopdBusqueda {

    private Date fechaInicio;
    private Date fechaFin;
    private Long usuario;
    private Long libro;
    private Long tipo;
    private Long accion;
    private Integer pageNumber;

    public UsuarioLopdBusqueda(Date fechaInicio, Date fechaFin, Long usuario, Long libro, Integer pageNumber) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.usuario = usuario;
        this.libro = libro;
        this.pageNumber = pageNumber;
    }

    public UsuarioLopdBusqueda(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public UsuarioLopdBusqueda() {

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

    public Long getTipo() { return tipo; }

    public void setTipo(Long tipo) { this.tipo = tipo; }

    public Long getAccion() { return accion; }

    public void setAccion(Long accion) { this.accion = accion; }

    public Integer getPageNumber() { return pageNumber; }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
