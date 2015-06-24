package es.caib.regweb3.webapp.form;

import java.util.Date;

/**
 * Created 5/06/14 16:09
 * @author jpernia
 */
public class InformeIndicadoresBusquedaForm {

    private Date fechaInicio;
    private Date fechaFin;
    private Long tipo;
    private String formato;
    private Long campoCalendario;

    public InformeIndicadoresBusquedaForm(Date fechaInicio, Date fechaFin, Long tipo, String formato, Long campoCalendario) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipo = tipo;
        this.formato = formato;
        this.campoCalendario = campoCalendario;
    }

    public InformeIndicadoresBusquedaForm() {

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

    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public Long getCampoCalendario() {
        return campoCalendario;
    }

    public void setCampoCalendario(Long campoCalendario) {
        this.campoCalendario = campoCalendario;
    }
}
