package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.Organismo;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created 4/04/14 16:09
 *
 * @author jpernia
 */
public class InformeOrganismoBusquedaForm {

    private Date fechaInicio;
    private Date fechaFin;
    private Long tipo;
    private String formato;
    private Set<String> campos;
    private Long idOrganismo;
    private Boolean anexos;
    private String interessatNom;
    private String interessatLli1;
    private String interessatLli2;
    private String interessatDoc;
    private String organDestinatari;
    private String organDestinatariNom;
    private String observaciones;
    private String usuario;
    private String extracto;
    private String numeroRegistroFormateado;
    private Long estado;
    private Long idOficina;

    public InformeOrganismoBusquedaForm(Date fechaInicio, Date fechaFin, Long tipo, String formato, Set<String> campos, String usuario,
                                        List<Organismo> organismos, Boolean anexos, String interessatNom, String interessatLli1, String interessatLli2,
                                        String interessatDoc, String organDestinatari, String organDestinatariNom, String observaciones,
                                        String extracto, String numeroRegistroFormateado, Long estado, Long idOficina) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipo = tipo;
        this.formato = formato;
        this.campos = campos;
        this.idOrganismo = idOrganismo;
        this.anexos = anexos;
        this.interessatNom = interessatNom;
        this.interessatLli1 = interessatLli1;
        this.interessatLli2 = interessatLli2;
        this.interessatDoc = interessatDoc;
        this.organDestinatari = organDestinatari;
        this.organDestinatariNom = organDestinatariNom;
        this.observaciones = observaciones;
        this.usuario = usuario;
        this.extracto = extracto;
        this.numeroRegistroFormateado = numeroRegistroFormateado;
        this.estado = estado;
        this.idOficina = idOficina;
    }

    public InformeOrganismoBusquedaForm() {

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

    public Long getIdOrganismo() {
        return idOrganismo;
    }

    public void setIdOrganismo(Long idOrganismo) {
        this.idOrganismo = idOrganismo;
    }

    public Boolean getAnexos() {return anexos; }

    public void setAnexos(Boolean anexos) { this.anexos = anexos; }

    public String getInteressatNom() { return interessatNom; }

    public void setInteressatNom(String interessatNom) { this.interessatNom = interessatNom; }

    public String getInteressatLli1() { return interessatLli1; }

    public void setInteressatLli1(String interessatLli1) { this.interessatLli1 = interessatLli1; }

    public String getInteressatLli2() { return interessatLli2; }

    public void setInteressatLli2(String interessatLli2) { this.interessatLli2 = interessatLli2; }

    public String getInteressatDoc() { return interessatDoc; }

    public void setInteressatDoc(String interessatDoc) { this.interessatDoc = interessatDoc; }

    public String getOrganDestinatari() { return organDestinatari; }

    public void setOrganDestinatari(String organDestinatari) { this.organDestinatari = organDestinatari; }

    public String getOrganDestinatariNom() { return organDestinatariNom; }

    public void setOrganDestinatariNom(String organDestinatariNom) { this.organDestinatariNom = organDestinatariNom; }

    public String getObservaciones() { return observaciones; }

    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getUsuario() { return usuario; }

    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getExtracto() { return extracto; }

    public void setExtracto(String extracto) { this.extracto = extracto; }

    public String getNumeroRegistroFormateado() { return numeroRegistroFormateado; }

    public void setNumeroRegistroFormateado(String numeroRegistroFormateado) { this.numeroRegistroFormateado = numeroRegistroFormateado; }

    public Long getEstado() { return estado; }

    public void setEstado(Long estado) { this.estado = estado; }

    public Long getIdOficina() { return idOficina; }

    public void setIdOficina(Long idOficina) { this.idOficina = idOficina; }

}