package es.caib.regweb3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;


/**
 * @author anadal (index)
 * @version 1.0
 * @created 28-oct-2013 14:41:38
 */
@Table(name = "RWE_CATPROVINCIA", indexes =
@Index(name = "RWE_CATPRO_CATCAU_FK_I", columnList = "COMUNIDADAUTONOMA"))
@Entity
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@XmlRootElement(name = "provincia")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CatProvincia implements Serializable {

    @XmlAttribute
    private Long id;
    @XmlTransient
    private Long codigoProvincia;
    @XmlTransient
    private CatComunidadAutonoma comunidadAutonoma;
    @XmlElement
    private String descripcionProvincia;

    public CatProvincia() {
    }

    public CatProvincia(String id) {
        this.id = Long.valueOf(id);
    }


    /**
     * @param cp
     */
    public CatProvincia(CatProvincia cp) {
        super();
        this.id = cp.id;
        this.codigoProvincia = cp.codigoProvincia;
        this.comunidadAutonoma = cp.comunidadAutonoma;
        this.descripcionProvincia = cp.descripcionProvincia;
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
     * @return the codigoProvincia
     */
    @Column(name = "CODIGOPROVINCIA", nullable = false, length = 2)
    public Long getCodigoProvincia() {
        return codigoProvincia;
    }

    /**
     * @param codigoProvincia the codigoProvincia to set
     */
    public void setCodigoProvincia(Long codigoProvincia) {
        this.codigoProvincia = codigoProvincia;
    }

    /**
     * @return the descripcionProvincia
     */
    @Column(name = "DESCRIPCIONPROVINCIA", nullable = false, length = 50)
    public String getDescripcionProvincia() {
        return descripcionProvincia;
    }

    /**
     * @param descripcionProvincia the descripcionProvincia to set
     */
    public void setDescripcionProvincia(String descripcionProvincia) {
        this.descripcionProvincia = descripcionProvincia;
    }

    /**
     * @return the codigoComunidad
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMUNIDADAUTONOMA", foreignKey = @ForeignKey(name = "RWE_CATPROVINC_CATCOMUNAUTO_FK"))
    @JsonIgnore
    public CatComunidadAutonoma getComunidadAutonoma() {
        return comunidadAutonoma;
    }

    /**
     * @param comunidadAutonoma the codigoComunidad to set
     */
    public void setComunidadAutonoma(CatComunidadAutonoma comunidadAutonoma) {
        this.comunidadAutonoma = comunidadAutonoma;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (this.codigoProvincia != null ? this.codigoProvincia.hashCode() : 0);
        hash = 59 * hash + (this.comunidadAutonoma != null ? this.comunidadAutonoma.hashCode() : 0);
        hash = 59 * hash + (this.descripcionProvincia != null ? this.descripcionProvincia.hashCode() : 0);
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
        final CatProvincia other = (CatProvincia) obj;
        if (this.codigoProvincia != other.codigoProvincia && (this.codigoProvincia == null || !this.codigoProvincia.equals(other.codigoProvincia))) {
            return false;
        }
        if (this.comunidadAutonoma != other.comunidadAutonoma && (this.comunidadAutonoma == null || !this.comunidadAutonoma.equals(other.comunidadAutonoma))) {
            return false;
        }
        if ((this.descripcionProvincia == null) ? (other.descripcionProvincia != null) : !this.descripcionProvincia.equals(other.descripcionProvincia)) {
            return false;
        }
        return true;
    }

}