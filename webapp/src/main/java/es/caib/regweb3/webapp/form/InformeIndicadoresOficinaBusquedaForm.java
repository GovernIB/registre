package es.caib.regweb3.webapp.form;

import java.util.Date;

/**
 * Created 5/06/14 16:09
 * @author jpernia
 */
public class InformeIndicadoresOficinaBusquedaForm {

    private Date fechaInicio;
    private Date fechaFin;
    private String formato;
    private Long oficina;

    public InformeIndicadoresOficinaBusquedaForm(Date fechaInicio, Date fechaFin, String formato, Long oficina) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.formato = formato;
        this.oficina = oficina;
    }

    public InformeIndicadoresOficinaBusquedaForm() {

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

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public Long getOficina() { return oficina; }

    public void setOficina(Long oficina) { this.oficina = oficina; }
}
