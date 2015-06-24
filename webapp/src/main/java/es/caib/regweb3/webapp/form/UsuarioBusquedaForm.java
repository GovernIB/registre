package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.Usuario;

import java.io.Serializable;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 7/05/14
 */
public class UsuarioBusquedaForm implements Serializable {

    private Usuario usuario;
    private Integer pageNumber;

    public UsuarioBusquedaForm() {
    }

    public UsuarioBusquedaForm(Usuario usuario, Integer pageNumber) {
        this.usuario = usuario;
        this.pageNumber = pageNumber;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }
}
