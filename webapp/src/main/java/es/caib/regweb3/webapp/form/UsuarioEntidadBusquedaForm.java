package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.Libro;
import es.caib.regweb3.model.UsuarioEntidad;

import java.io.Serializable;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 7/05/14
 */
public class UsuarioEntidadBusquedaForm implements Serializable {

    private UsuarioEntidad usuarioEntidad;
    private Libro libro;
    private Integer pageNumber;

    public UsuarioEntidadBusquedaForm() {
    }

    public UsuarioEntidadBusquedaForm(UsuarioEntidad usuarioEntidad, Integer pageNumber) {
        this.usuarioEntidad = usuarioEntidad;
        this.pageNumber = pageNumber;
    }

    public UsuarioEntidad getUsuarioEntidad() {
        return usuarioEntidad;
    }

    public void setUsuarioEntidad(UsuarioEntidad usuarioEntidad) {
        this.usuarioEntidad = usuarioEntidad;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
