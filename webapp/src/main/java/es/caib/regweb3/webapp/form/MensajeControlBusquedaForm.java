package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.sir.MensajeControl;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created 4/04/14 16:09
 *
 * @author earrivi
 */
public class MensajeControlBusquedaForm {

    private MensajeControl mensajeControl;
    private Integer pageNumber;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fechaInicio;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fechaFin;


    public MensajeControlBusquedaForm() { }

    public MensajeControlBusquedaForm(MensajeControl mensajeControl, Integer pageNumber) {
        this.mensajeControl = mensajeControl;
        this.pageNumber = pageNumber;
        this.fechaInicio =  new Date();
        this.fechaFin =  new Date();
    }

    public MensajeControl getMensajeControl() {
        return mensajeControl;
    }

    public void setMensajeControl(MensajeControl mensajeControl) {
        this.mensajeControl = mensajeControl;
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
