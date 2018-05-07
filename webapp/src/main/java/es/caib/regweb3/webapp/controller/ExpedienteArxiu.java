package es.caib.regweb3.webapp.controller;

public class ExpedienteArxiu {

    private String id;
    private String name;
    private String custodyId;
    private String tipoRegistro;
    private String numeroRegistroFormateado;
    private String codigoLibro;
    private String csv;
    private Boolean justificante;

    public ExpedienteArxiu() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustodyId() {
        return custodyId;
    }

    public void setCustodyId(String custodyId) {
        this.custodyId = custodyId;
    }

    public String getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public String getNumeroRegistroFormateado() {
        return numeroRegistroFormateado;
    }

    public void setNumeroRegistroFormateado(String numeroRegistroFormateado) {
        this.numeroRegistroFormateado = numeroRegistroFormateado;
    }

    public String getCodigoLibro() {
        return codigoLibro;
    }

    public void setCodigoLibro(String codigoLibro) {
        this.codigoLibro = codigoLibro;
    }

    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }

    public Boolean getJustificante() {
        return justificante;
    }

    public void setJustificante(Boolean justificante) {
        this.justificante = justificante;
    }
}
