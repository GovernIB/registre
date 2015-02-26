package es.caib.regweb.webapp.form;

import es.caib.regweb.model.RegistroEntrada;

import java.io.Serializable;
import java.util.Date;

/**
 * Created 4/04/14 16:09
 *
 * @author mgonzalez
 */
public class RegistroEntradaBusqueda implements Serializable {

    private RegistroEntrada registroEntrada;
    private Integer pageNumber;
    private Date fechaInicio;
    private Date fechaFin;


    public RegistroEntradaBusqueda() {}

    public RegistroEntradaBusqueda(RegistroEntrada registroEntrada, Integer pageNumber) {
        this.registroEntrada = registroEntrada;
        this.pageNumber = pageNumber;
    }

    public RegistroEntrada getRegistroEntrada() {
        return registroEntrada;
    }

    public void setRegistroEntrada(RegistroEntrada registroEntrada) {
        this.registroEntrada = registroEntrada;
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
}
