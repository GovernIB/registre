package es.caib.regweb3.ws.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class AsientoRegistralBean {

    private String numeroRegistro;
    private String extracto;

    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public String getExtracto() {
        return extracto;
    }

    public void setExtracto(String extracto) {
        this.extracto = extracto;
    }
}
