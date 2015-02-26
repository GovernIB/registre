package es.caib.regweb.webapp.form;

import es.caib.regweb.model.Libro;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created 4/04/14 16:09
 *
 * @author jpernia
 */
public class InformeLibroBusquedaForm {

    private Date fechaInicio;
    private Date fechaFin;
    private Long tipo;
    private String formato;
    private Set<String> campos;
    private List<Libro> libros;

    public InformeLibroBusquedaForm(Date fechaInicio, Date fechaFin, Long tipo, String formato, Set<String> campos, List<Libro> libros) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipo = tipo;
        this.formato = formato;
        this.campos = campos;
        this.libros = libros;
    }

    public InformeLibroBusquedaForm() {

    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public Set<String> getCampos() {
        return campos;
    }

    public void setCampos(Set<String> campos) {
        this.campos = campos;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }
}