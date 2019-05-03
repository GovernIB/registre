package es.caib.regweb3.persistence.utils;

public class JustificanteReferencia {

    private String csv;
    private String url;

    public JustificanteReferencia() {
    }

    public JustificanteReferencia(String csv, String url) {
        this.csv = csv;
        this.url = url;
    }

    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
