package es.caib.regweb3.ws.model;

import java.io.Serializable;

/**
 * @author mgonzalez
 * @version 1
 * 22/09/2023
 */
public class MetadatoWs implements Serializable {

    private Long tipo;
    private String campo;
    private String valor;

    public MetadatoWs() {
    }

    public MetadatoWs(Long tipo, String campo, String valor) {
        this.tipo = tipo;
        this.campo = campo;
        this.valor = valor;
    }

    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
