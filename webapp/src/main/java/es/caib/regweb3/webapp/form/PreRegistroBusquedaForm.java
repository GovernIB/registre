package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.PreRegistro;

/**
 * Created 12/12/14 11:11
 *
 * @author jpernia
 */
public class PreRegistroBusquedaForm {

    private PreRegistro preRegistro;
    private Integer pageNumber;
    private Integer anyo;
    private Long estado;


    public PreRegistroBusquedaForm() {}

    public PreRegistroBusquedaForm(PreRegistro preRegistro, Integer pageNumber) {
        this.preRegistro = preRegistro;
        this.pageNumber = pageNumber;
    }

    public PreRegistro getPreRegistro() {
        return preRegistro;
    }

    public void setPreRegistro(PreRegistro preRegistro) {
        this.preRegistro = preRegistro;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getAnyo() {
        return anyo;
    }

    public void setAnyo(Integer anyo) {
        this.anyo = anyo;
    }

    public Long getEstado() { return estado; }

    public void setEstado(Long estado) { this.estado = estado; }
}
