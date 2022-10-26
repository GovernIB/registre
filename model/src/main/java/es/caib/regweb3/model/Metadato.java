package es.caib.regweb3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * @author mgonzalez
 * @version 1
 * 02/09/2022
 */
@Entity
@SequenceGenerator(name = "generator", sequenceName = "RWE_MTD_SEQ", allocationSize = 1)
@Table(name = "RWE_METADATO")
@XmlRootElement(name = "metadato")
@XmlAccessorType(XmlAccessType.FIELD)
public class Metadato implements Serializable {


    private Long id;
    private String campo;
    private String valor;

    public Metadato(String campo, String valor) {
        this.campo = campo;
        this.valor = valor;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Metadato() {
    }


    @Column(name = "CAMPO" , length = 80)
    public String getCampo() {
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    @Column(name = "VALOR")
    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

}
