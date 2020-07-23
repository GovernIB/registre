package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.PermisoOrganismoUsuario;
import es.caib.regweb3.model.UsuarioEntidad;

import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 */
public class PermisoOrganismoUsuarioForm {

    private UsuarioEntidad usuarioEntidad;
    private List<PermisoOrganismoUsuario> permisoOrganismoUsuarios;


    public PermisoOrganismoUsuarioForm() {
    }

    public UsuarioEntidad getUsuarioEntidad() {
        return usuarioEntidad;
    }

    public void setUsuarioEntidad(UsuarioEntidad usuarioEntidad) {
        this.usuarioEntidad = usuarioEntidad;
    }

    public List<PermisoOrganismoUsuario> getPermisoOrganismoUsuarios() {
        return permisoOrganismoUsuarios;
    }

    public void setPermisoOrganismoUsuarios(List<PermisoOrganismoUsuario> permisoOrganismoUsuarios) {
        this.permisoOrganismoUsuarios = permisoOrganismoUsuarios;
    }
}
