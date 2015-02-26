package es.caib.regweb.webapp.form;

import es.caib.regweb.model.PermisoLibroUsuario;
import es.caib.regweb.model.UsuarioEntidad;

import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 12/03/14
 */
public class PermisoLibroUsuarioForm {

    private UsuarioEntidad usuarioEntidad;
    private List<PermisoLibroUsuario> permisoLibroUsuarios;


    public PermisoLibroUsuarioForm() {
    }

    public UsuarioEntidad getUsuarioEntidad() {
        return usuarioEntidad;
    }

    public void setUsuarioEntidad(UsuarioEntidad usuarioEntidad) {
        this.usuarioEntidad = usuarioEntidad;
    }

    public List<PermisoLibroUsuario> getPermisoLibroUsuarios() {
        return permisoLibroUsuarios;
    }

    public void setPermisoLibroUsuarios(List<PermisoLibroUsuario> permisoLibroUsuarios) {
        this.permisoLibroUsuarios = permisoLibroUsuarios;
    }
}
