package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.RegistroSir;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/06/16
 */
public class ErteBusquedaForm {

    private RegistroSir registroSir;
    private Integer pageNumber;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fechaInicio;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fechaFin;
    private String estado;
    private Long total;


    public ErteBusquedaForm() {}

    public ErteBusquedaForm(RegistroSir registroSir, Integer pageNumber) {
        this.registroSir = registroSir;
        this.pageNumber = pageNumber;
        this.fechaInicio =  new Date();
        this.fechaFin =  new Date();
    }

    public RegistroSir getRegistroSir() {
        return registroSir;
    }

    public void setRegistroSir(RegistroSir registroSir) {
        this.registroSir = registroSir;
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

    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }
}
