package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.Oficina;

import java.io.Serializable;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 *         Date: 7/05/14
 */
public class OficinaBusquedaForm implements Serializable {

    private Oficina oficina;
    private Long entidad;
    private Integer pageNumber;

    public OficinaBusquedaForm() {
    }

    public OficinaBusquedaForm(Oficina oficina, Long entidad, Integer pageNumber) {
        this.oficina = oficina;
        this.entidad = entidad;
        this.pageNumber = pageNumber;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public Long getEntidad() {
        return entidad;
    }

    public void setEntidad(Long entidad) {
        this.entidad = entidad;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
