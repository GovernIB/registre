package es.caib.regweb.webapp.form;

import es.caib.regweb.model.RegistroSalida;

import java.io.Serializable;
import java.util.Date;

/**
 * Created 4/04/14 16:09
 *
 * @author earrivi
 */
public class RegistroSalidaBusqueda implements Serializable {

    private RegistroSalida registroSalida;
    private Integer pageNumber;
    private Date fechaInicio;
    private Date fechaFin;
    private Boolean anexos;


    public RegistroSalidaBusqueda() {}

    public RegistroSalidaBusqueda(RegistroSalida registroSalida, Integer pageNumber) {
        this.registroSalida = registroSalida;
        this.pageNumber = pageNumber;
    }

    public RegistroSalida getRegistroSalida() {
        return registroSalida;
    }

    public void setRegistroSalida(RegistroSalida registroSalida) {
        this.registroSalida = registroSalida;
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

    public Boolean getAnexos() {
        return anexos;
    }

    public void setAnexos(Boolean anexos) {
        this.anexos = anexos;
    }
}
