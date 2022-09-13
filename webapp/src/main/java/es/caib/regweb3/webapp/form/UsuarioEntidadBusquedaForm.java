package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.Organismo;
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
    private Organismo organismo;
    private Long permiso;
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

    public Organismo getOrganismo() {
        return organismo;
    }

    public void setOrganismo(Organismo organismo) {
        this.organismo = organismo;
    }

    public Long getPermiso() {
        return permiso;
    }

    public void setPermiso(Long permiso) {
        this.permiso = permiso;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
