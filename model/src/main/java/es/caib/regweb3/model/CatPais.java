package es.caib.regweb3.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;


/**
 * @version 1.0
 * @created 28-oct-2013 14:41:38
 */
@Table(name = "RWE_CATPAIS")
@Entity
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@XmlRootElement(name = "pais")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CatPais implements Serializable {

    @XmlAttribute
    private Long id;
    @XmlTransient
    private Long codigoPais;
    @XmlElement
    private String descripcionPais;
    @XmlTransient
    private String alfa3Pais;
    @XmlTransient
    private String alfa2Pais;

    public CatPais() {

    }

    public CatPais(String id) {
        this.id = Long.valueOf(id);
    }


    /**
     * @param cp
     */
    public CatPais(CatPais cp) {
        this.id = cp.id;
        this.codigoPais = cp.codigoPais;
        this.descripcionPais = cp.descripcionPais;
        this.alfa3Pais = cp.alfa3Pais;
        this.alfa2Pais = cp.alfa2Pais;
    }

    @Column(name = "ID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the codigoPais
     */
    @Column(name = "CODIGOPAIS", nullable = false, length = 3)
    public Long getCodigoPais() {
        return codigoPais;
    }

    /**
     * @param codigoPais the codigoPais to set
     */
    public void setCodigoPais(Long codigoPais) {
        this.codigoPais = codigoPais;
    }

    /**
     * @return the descripcionPais
     */
    @Column(name = "DESCRIPCIONPAIS", nullable = false, length = 100)
    public String getDescripcionPais() {
        return descripcionPais;
    }

    /**
     * @param descripcionPais the descripcionPais to set
     */
    public void setDescripcionPais(String descripcionPais) {
        this.descripcionPais = descripcionPais;
    }

    /**
     * @return the alfa3Pais
     */
    @Column(name = "ALFA3PAIS", length = 3)
    public String getAlfa3Pais() {
        return alfa3Pais;
    }

    /**
     * @param alfa3Pais the alfa3Pais to set
     */
    public void setAlfa3Pais(String alfa3Pais) {
        this.alfa3Pais = alfa3Pais;
    }

    /**
     * @return the alfa2Pais
     */
    @Column(name = "ALFA2PAIS", length = 2)
    public String getAlfa2Pais() {
        return alfa2Pais;
    }

    /**
     * @param alfa2Pais the alfa2Pais to set
     */
    public void setAlfa2Pais(String alfa2Pais) {
        this.alfa2Pais = alfa2Pais;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.codigoPais != null ? this.codigoPais.hashCode() : 0);
        hash = 61 * hash + (this.descripcionPais != null ? this.descripcionPais.hashCode() : 0);
        hash = 61 * hash + (this.alfa3Pais != null ? this.alfa3Pais.hashCode() : 0);
        hash = 61 * hash + (this.alfa2Pais != null ? this.alfa2Pais.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CatPais other = (CatPais) obj;
        if (this.codigoPais != other.codigoPais && (this.codigoPais == null || !this.codigoPais.equals(other.codigoPais))) {
            return false;
        }
        if ((this.descripcionPais == null) ? (other.descripcionPais != null) : !this.descripcionPais.equals(other.descripcionPais)) {
            return false;
        }
        if ((this.alfa3Pais == null) ? (other.alfa3Pais != null) : !this.alfa3Pais.equals(other.alfa3Pais)) {
            return false;
        }
        if ((this.alfa2Pais == null) ? (other.alfa2Pais != null) : !this.alfa2Pais.equals(other.alfa2Pais)) {
            return false;
        }
        return true;
    }

}