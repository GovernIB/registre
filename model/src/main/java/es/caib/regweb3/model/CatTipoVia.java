package es.caib.regweb3.model;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Created by Fundació BIT.
 * @author earrivi
 */
@Entity
@Table(name = "RWE_CATTIPOVIA")
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class CatTipoVia implements Serializable {

    private Long id;
    private Long codigoTipoVia;
    private String descripcionTipoVia;

	public CatTipoVia(){}

    public CatTipoVia(Long codigoTipoVia, String descripcionTipoVia) {
        this.codigoTipoVia = codigoTipoVia;
        this.descripcionTipoVia = descripcionTipoVia;
    }

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "generator")
    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    @Column(name = "CODIGOTIPOVIA", nullable = false)
    public Long getCodigoTipoVia() {
        return codigoTipoVia;
    }

    public void setCodigoTipoVia(Long codigoTipoVia) {
        this.codigoTipoVia = codigoTipoVia;
    }

    @Column(name = "DESCRIPCIONTIPOVIA", nullable = false, length = 300)
    public String getDescripcionTipoVia() {
        return descripcionTipoVia;
    }

    public void setDescripcionTipoVia(String descripcionTipoVia) {
        this.descripcionTipoVia = descripcionTipoVia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CatTipoVia that = (CatTipoVia) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}