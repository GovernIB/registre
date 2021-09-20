package es.caib.regweb3.model;

import javax.persistence.*;
import java.io.Serializable;


/**
 * @version 1.0
 * @created 28-oct-2013 14:41:38
 */
@Entity
@Table(name = "RWE_CATENTIDADGEOGRAFICA")
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class CatEntidadGeografica implements Serializable {


    private Long id;
    private String codigoEntidadGeografica;
    private String descripcionEntidadGeografica;

    public CatEntidadGeografica() {

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
     * @return the codigoEntidadGeografica
     */
    @Column(name = "CODIGOENTIDADGEOGRAFICA", nullable = false, length = 2)
    public String getCodigoEntidadGeografica() {
        return codigoEntidadGeografica;
    }

    /**
     * @param codigoEntidadGeografica the codigoEntidadGeografica to set
     */
    public void setCodigoEntidadGeografica(String codigoEntidadGeografica) {
        this.codigoEntidadGeografica = codigoEntidadGeografica;
    }

    /**
     * @return the descripcionEntidadGeografica
     */
    @Column(name = "DESCRIPCIONENTIDADGEOGRAFICA", nullable = false, length = 50)
    public String getDescripcionEntidadGeografica() {
        return descripcionEntidadGeografica;
    }

    /**
     * @param descripcionEntidadGeografica the descripcionEntidadGeografica to set
     */
    public void setDescripcionEntidadGeografica(String descripcionEntidadGeografica) {
        this.descripcionEntidadGeografica = descripcionEntidadGeografica;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + (this.codigoEntidadGeografica != null ? this.codigoEntidadGeografica.hashCode() : 0);
        hash = 17 * hash + (this.descripcionEntidadGeografica != null ? this.descripcionEntidadGeografica.hashCode() : 0);
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
        final CatEntidadGeografica other = (CatEntidadGeografica) obj;
        if (this.codigoEntidadGeografica != other.codigoEntidadGeografica && (this.codigoEntidadGeografica == null || !this.codigoEntidadGeografica.equals(other.codigoEntidadGeografica))) {
            return false;
        }
        if ((this.descripcionEntidadGeografica == null) ? (other.descripcionEntidadGeografica != null) : !this.descripcionEntidadGeografica.equals(other.descripcionEntidadGeografica)) {
            return false;
        }
        return true;
    }


}