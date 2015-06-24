package es.caib.regweb3.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created 19/02/14 11:47
 *
 * @author mgonzalez
 */
@Entity
@Table(name = "RWE_CATESTADOENTIDAD")
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class CatEstadoEntidad implements Serializable {

    private Long id;
    private String codigoEstadoEntidad;
    private String descripcionEstadoEntidad;

  	public CatEstadoEntidad(){

  	}


    public CatEstadoEntidad(Long id) {
        this.id = id;
    }

    public void finalize() throws Throwable {

  	}

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "generator")
    @Column(name="ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "CODIGOESTADOENTIDAD", nullable = false, length = 2, unique = true)
    public String getCodigoEstadoEntidad() {
      return codigoEstadoEntidad;
    }


    public void setCodigoEstadoEntidad(String codigoEstadoEntidad) {
      this.codigoEstadoEntidad = codigoEstadoEntidad;
    }


    @Column(name = "DESCRIPCIONESTADOENTIDAD", nullable = false, length = 50)
    public String getDescripcionEstadoEntidad() {
      return descripcionEstadoEntidad;
    }


    public void setDescripcionEstadoEntidad(String descripcionEstadoEntidad) {
      this.descripcionEstadoEntidad = descripcionEstadoEntidad;
    }
}
