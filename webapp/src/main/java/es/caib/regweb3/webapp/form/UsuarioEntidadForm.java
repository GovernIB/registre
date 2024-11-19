package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.UsuarioEntidad;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class UsuarioEntidadForm {

    private UsuarioEntidad usuarioEntidad;
    private CommonsMultipartFile certificadoCurso;
    private boolean borrarCertificado;

    public UsuarioEntidadForm() {
    }

    public UsuarioEntidad getUsuarioEntidad() {
        return usuarioEntidad;
    }

    public void setUsuarioEntidad(UsuarioEntidad usuarioEntidad) {
        this.usuarioEntidad = usuarioEntidad;
    }

    public CommonsMultipartFile getCertificadoCurso() {
        return certificadoCurso;
    }

    public void setCertificadoCurso(CommonsMultipartFile certificadoCurso) {
        this.certificadoCurso = certificadoCurso;
    }

    public boolean isBorrarCertificado() {
        return borrarCertificado;
    }

    public void setBorrarCertificado(boolean borrarCertificado) {
        this.borrarCertificado = borrarCertificado;
    }
}
