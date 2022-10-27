package es.caib.regweb3.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author mgonzalez
 * @version 1
 * 02/09/2022
 */

@MappedSuperclass
public abstract class Metadato implements Serializable {

    protected String tipo;
    protected String campo;
    protected String valor;


    public Metadato() {
    }

    public Metadato(String tipo, String campo, String valor) {
        this.tipo = tipo;
        this.campo = campo;
        this.valor = valor;
    }

    @Column(name = "TIPO", length = 1)
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Column(name = "CAMPO", length = 80)
    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    @Column(
            name = "VALOR",
            length = 4000
    )
    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
