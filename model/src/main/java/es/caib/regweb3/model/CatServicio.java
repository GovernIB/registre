package es.caib.regweb3.model;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Created by Fundació BIT.
 * @author earrivi
 */
@Entity
@Table(name = "RWE_CATSERVICIO")
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class CatServicio implements Serializable {

    private Long id;
    private Long codServicio;
    private String descServicio;

	public CatServicio(){}

    public CatServicio(Long codServicio, String descServicio) {
        this.codServicio = codServicio;
        this.descServicio = descServicio;
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

    @Column(name = "CODIGOSERVICIO", nullable = false)
    public Long getCodServicio() {
      return codServicio;
    }

    public void setCodServicio(Long codServicio) {
      this.codServicio = codServicio;
    }

    @Column(name = "DESCRIPCIONSERVICIO", nullable = false, length = 300)
    public String getDescServicio() {
        return descServicio;
    }

    public void setDescServicio(String descServicio) {
        this.descServicio = descServicio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CatServicio that = (CatServicio) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}