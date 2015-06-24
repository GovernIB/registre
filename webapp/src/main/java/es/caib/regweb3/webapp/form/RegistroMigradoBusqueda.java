package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.RegistroMigrado;

import java.util.Date;

/**
 * Created by jpernia on 11/11/2014.
 */
public class RegistroMigradoBusqueda {

    private RegistroMigrado registroMigrado;
    private Integer pageNumber;
    private Date fechaInicio;
    private Date fechaFin;
    private Integer numeroRegistro;
    private Integer anoRegistro;


    public RegistroMigradoBusqueda() {}

    public RegistroMigradoBusqueda(RegistroMigrado registroMigrado, Integer pageNumber) {
        this.registroMigrado = registroMigrado;
        this.pageNumber = pageNumber;
    }

    public RegistroMigrado getRegistroMigrado() {
        return registroMigrado;
    }

    public void setRegistroMigrado(RegistroMigrado registroMigrado) {
        this.registroMigrado = registroMigrado;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
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

    public Integer getNumeroRegistro() { return numeroRegistro; }

    public void setNumeroRegistro(Integer numeroRegistro) { this.numeroRegistro = numeroRegistro; }

    public Integer getAnoRegistro() { return anoRegistro; }

    public void setAnoRegistro(Integer anoRegistro) { this.anoRegistro = anoRegistro; }
}
