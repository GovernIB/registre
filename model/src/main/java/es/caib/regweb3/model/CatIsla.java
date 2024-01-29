package es.caib.regweb3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;


/**
 * @author earrivi
 */
@Entity
@Table(name = "RWE_CATISLA", indexes = {
        @Index(name = "RWE_CATISL_CATPRO_FK_I", columnList = "PROVINCIA")})
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@XmlRootElement(name = "isla")
@XmlAccessorType(XmlAccessType.FIELD)
public class CatIsla implements Serializable {

    @XmlAttribute
    private Long id;
    @XmlTransient
    private Long codigoIsla;
    @XmlElement
    private String descripcionIsla;
    @XmlTransient
    private CatProvincia provincia;


    public CatIsla() {

    }

    public CatIsla(Long id) {
        this.id = id;
    }

    public CatIsla(String id) {
        this.id = Long.valueOf(id);
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


    @Column(name = "CODIGOISLA", nullable = false, length = 2)
    public Long getCodigoIsla() {
        return codigoIsla;
    }

    public void setCodigoIsla(Long codigoIsla) {
        this.codigoIsla = codigoIsla;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROVINCIA", foreignKey =@ForeignKey(name = "RWE_CATISLA_CATPROVIN_FK"))
    @JsonIgnore
    public CatProvincia getProvincia() {
        return provincia;
    }


    public void setProvincia(CatProvincia provincia) {
        this.provincia = provincia;
    }


    @Column(name = "DESCRIPCIONISLA", nullable = false, length = 50)
    public String getDescripcionIsla() {
        return descripcionIsla;
    }

    public void setDescripcionIsla(String descripcionIsla) {
        this.descripcionIsla = descripcionIsla;
    }
}