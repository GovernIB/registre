package es.caib.regweb3.model.utils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author earrivi on 05/03/2015.
 */
public class RegistroBasico implements Serializable{

    private Long id;
    private String numeroRegistroFormateado;
    private Date fecha;
    private String libro;
    private String usuario;
    private String extracto;

    private Long idOficina;
    private String oficina;
    private String destinatario;
    private Long anexos;
    private Long estado;

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

    public Long getIdOficina() {
        return idOficina;
    }

    public void setIdOficina(Long idOficina) {
        this.idOficina = idOficina;
    }

    public String getOficina() {
        return oficina;
    }

    public void setOficina(String oficina) {
        this.oficina = oficina;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public Long getAnexos() {
        return anexos;
    }

    public void setAnexos(Long anexos) {
        this.anexos = anexos;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }
}
