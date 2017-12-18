package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.Organismo;

import java.io.Serializable;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 7/05/14
 */
public class OrganismoBusquedaForm implements Serializable {

    private Organismo organismo;
    private Long entidad;
    private Integer pageNumber;
    private Boolean libros;

    public OrganismoBusquedaForm() {
    }

    public OrganismoBusquedaForm(Organismo organismo, Integer pageNumber, Boolean libros) {
        this.organismo = organismo;
        this.pageNumber = pageNumber;
        this.libros = libros;
    }

    public Organismo getOrganismo() {
        return organismo;
    }

    public void setOrganismo(Organismo organismo) {
        this.organismo = organismo;
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

    public Boolean getLibros() { return libros; }

    public void setLibros(Boolean libros) { this.libros = libros; }
}
