package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.OficioRemision;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created 4/04/14 16:09
 *
 * @author earrivi
 */
public class OficioRemisionBusquedaForm {

    private OficioRemision oficioRemision;
    private Long idOrganismo;
    private Integer pageNumber;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fechaInicio;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fechaFin;
    private String usuario;
    private Long destinoOficioRemision;
    private Long tipoOficioRemision;
    private Integer estadoOficioRemision;


    public OficioRemisionBusquedaForm() {
    }

    public OficioRemisionBusquedaForm(OficioRemision oficioRemision, Integer pageNumber) {
        this.oficioRemision = oficioRemision;
        this.pageNumber = pageNumber;
        this.fechaInicio = new Date();
        this.fechaFin = new Date();
    }

    public OficioRemision getOficioRemision() {
        return oficioRemision;
    }

    public void setOficioRemision(OficioRemision oficioRemision) {
        this.oficioRemision = oficioRemision;
    }

    public Long getIdOrganismo() {
        return idOrganismo;
    }

    public void setIdOrganismo(Long idOrganismo) {
        this.idOrganismo = idOrganismo;
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

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Long getDestinoOficioRemision() {
        return destinoOficioRemision;
    }

    public void setDestinoOficioRemision(Long destinoOficioRemision) {
        this.destinoOficioRemision = destinoOficioRemision;
    }

    public Long getTipoOficioRemision() {
        return tipoOficioRemision;
    }

    public void setTipoOficioRemision(Long tipoOficioRemision) {
        this.tipoOficioRemision = tipoOficioRemision;
    }

    public Integer getEstadoOficioRemision() {
        return estadoOficioRemision;
    }

    public void setEstadoOficioRemision(Integer estadoOficioRemision) {
        this.estadoOficioRemision = estadoOficioRemision;
    }
}
