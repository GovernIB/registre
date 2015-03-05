package es.caib.regweb.model.utils;

import java.util.Date;

/**
 * @author earrivi on 05/03/2015.
 */
public class RegistroBasico {

    private Long id;
    private String numeroRegistroFormateado;
    private Date fecha;
    private String libro;
    private String usuario;
    private String extracto;

    public RegistroBasico() {
    }

    public RegistroBasico(Long id, String numeroRegistroFormateado, Date fecha, String libro, String usuario, String extracto) {
        this.id = id;
        this.numeroRegistroFormateado = numeroRegistroFormateado;
        this.fecha = fecha;
        this.libro = libro;
        this.usuario = usuario;
        this.extracto = extracto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroRegistroFormateado() {
        return numeroRegistroFormateado;
    }

    public void setNumeroRegistroFormateado(String numeroRegistroFormateado) {
        this.numeroRegistroFormateado = numeroRegistroFormateado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getExtracto() {
        return extracto;
    }

    public void setExtracto(String extracto) {
        this.extracto = extracto;
    }
}
