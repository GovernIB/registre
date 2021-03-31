package es.caib.regweb3.ws.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AsientoWs implements Serializable {

    private String numeroRegistro;
    private Long tipoRegistro;
    private Date fechaRegistro;
    private String codigoOficinaOrigen;
    private String denominacionOficinaOrigen;
    private String codigoUnidadOrigen;
    private String denominacionUnidadOrigen;
    private String codigoDestino;
    private String denominacionDestino;
    private String extracto;
    private String tipoDocumetacionFisica;
    private String expone;
    private String solicita;
    private Long codigoSia;
    private Boolean presencial;
    private Long idioma;
    private Long estado;
    private String descripcionEstado;

    private List<InteresadoWs> interesados;
    private List<FileInfoWs> anexos;
    private FileInfoWs justificante;

    public AsientoWs() {
    }

    public AsientoWs(Long tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public Long getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(Long tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getCodigoOficinaOrigen() {
        return codigoOficinaOrigen;
    }

    public void setCodigoOficinaOrigen(String codigoOficinaOrigen) {
        this.codigoOficinaOrigen = codigoOficinaOrigen;
    }

    public String getDenominacionOficinaOrigen() {
        return denominacionOficinaOrigen;
    }

    public void setDenominacionOficinaOrigen(String denominacionOficinaOrigen) {
        this.denominacionOficinaOrigen = denominacionOficinaOrigen;
    }

    public String getCodigoUnidadOrigen() {
        return codigoUnidadOrigen;
    }

    public void setCodigoUnidadOrigen(String codigoUnidadOrigen) {
        this.codigoUnidadOrigen = codigoUnidadOrigen;
    }

    public String getDenominacionUnidadOrigen() {
        return denominacionUnidadOrigen;
    }

    public void setDenominacionUnidadOrigen(String denominacionUnidadOrigen) {
        this.denominacionUnidadOrigen = denominacionUnidadOrigen;
    }

    public String getCodigoDestino() {
        return codigoDestino;
    }

    public void setCodigoDestino(String codigoDestino) {
        this.codigoDestino = codigoDestino;
    }

    public String getDenominacionDestino() {
        return denominacionDestino;
    }

    public void setDenominacionDestino(String denominacionDestino) {
        this.denominacionDestino = denominacionDestino;
    }

    public String getExtracto() {
        return extracto;
    }

    public void setExtracto(String extracto) {
        this.extracto = extracto;
    }

    public String getTipoDocumetacionFisica() {
        return tipoDocumetacionFisica;
    }

    public void setTipoDocumetacionFisica(String tipoDocumetacionFisica) {
        this.tipoDocumetacionFisica = tipoDocumetacionFisica;
    }

    public String getExpone() {
        return expone;
    }

    public void setExpone(String expone) {
        this.expone = expone;
    }

    public String getSolicita() {
        return solicita;
    }

    public void setSolicita(String solicita) {
        this.solicita = solicita;
    }

    public Long getCodigoSia() {
        return codigoSia;
    }

    public void setCodigoSia(Long codigoSia) {
        this.codigoSia = codigoSia;
    }

    public List<InteresadoWs> getInteresados() {
        return interesados;
    }

    public void setInteresados(List<InteresadoWs> interesados) {
        this.interesados = interesados;
    }

    public List<FileInfoWs> getAnexos() {
        return anexos;
    }

    public void setAnexos(List<FileInfoWs> anexos) {
        this.anexos = anexos;
    }

    public FileInfoWs getJustificante() {
        return justificante;
    }

    public void setJustificante(FileInfoWs justificante) {
        this.justificante = justificante;
    }

    public Boolean getPresencial() {
        return presencial;
    }

    public void setPresencial(Boolean presencial) {
        this.presencial = presencial;
    }

    public Long getIdioma() {
        return idioma;
    }

    public void setIdioma(Long idioma) {
        this.idioma = idioma;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    public String getDescripcionEstado() {
        return descripcionEstado;
    }

    public void setDescripcionEstado(String descripcionEstado) {
        this.descripcionEstado = descripcionEstado;
    }
}
