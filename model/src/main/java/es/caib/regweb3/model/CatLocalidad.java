package es.caib.regweb3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;


/**
 * @version 1.0
 * @created 28-oct-2013 14:41:38
 * @author anadal (index)
 */
@Entity
@Table(name = "RWE_CATLOCALIDAD")
@org.hibernate.annotations.Table(appliesTo = "RWE_CATLOCALIDAD", indexes = {
    @Index(name="RWE_CATLOC_CATPRO_FK_I", columnNames = {"PROVINCIA"}),
    @Index(name="RWE_CATLOC_CATENG_FK_I", columnNames = {"ENTIDADGEOGRAFICA"})
})
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@XmlRootElement(name = "localidad")
@XmlAccessorType(XmlAccessType.FIELD)
public class CatLocalidad implements Serializable {

    @XmlAttribute
    private Long id;
    @XmlTransient
    private Long codigoLocalidad;
    @XmlTransient
    private CatProvincia provincia;
    @XmlElement
    private String nombre;
    @XmlTransient
    private CatEntidadGeografica entidadGeografica;

    public CatLocalidad(){

    }

    public CatLocalidad(Long id){
        this.id= id;
    }

    public CatLocalidad(String id){
        this.id= Long.valueOf(id);
    }


  /**
   *
   * @param cl
   */
  public CatLocalidad(CatLocalidad cl) {
      super();
      this.id = cl.id;
      this.codigoLocalidad = cl.codigoLocalidad;
      this.provincia = cl.provincia == null ? null : new CatProvincia(cl.provincia);
      this.nombre = cl.nombre;
      this.entidadGeografica = cl.entidadGeografica;
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

  /**
   * @return the codigoLocalidad
   */

  @Column(name = "CODIGOLOCALIDAD", nullable = false, length = 4)
  public Long getCodigoLocalidad() {
    return codigoLocalidad;
  }

  /**
   * @param codigoLocalidad the codigoLocalidad to set
   */
  public void setCodigoLocalidad(Long codigoLocalidad) {
    this.codigoLocalidad = codigoLocalidad;
  }

  /**
   * @return the provincia
   */

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn (name="PROVINCIA")
  @ForeignKey(name="RWE_CATLOCAL_CATPROVIN_FK")
  @JsonIgnore
  public CatProvincia getProvincia() {
    return provincia;
  }

  /**
   * @param provincia the provincia to set
   */
  public void setProvincia(CatProvincia provincia) {
    this.provincia = provincia;
  }

  /**
   * @return the descripcionLocalidad
   */
  @Column(name = "DESCRIPCIONLOCALIDAD", nullable = false, length = 50)
  public String getNombre() {
    return nombre;
  }

  /**
   * @param nombre the nombre to set
   */
  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  /**
   * @return the entidadGeografica
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn (name="ENTIDADGEOGRAFICA")
  @ForeignKey(name = "RWE_CATLOCAL_CATENT_FK")
  @JsonIgnore
  public CatEntidadGeografica getEntidadGeografica() {
    return entidadGeografica;
  }

  /**
   * @param entidadGeografica the entidadGeografica to set
   */
  public void setEntidadGeografica(CatEntidadGeografica entidadGeografica) {
    this.entidadGeografica = entidadGeografica;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 67 * hash + (this.codigoLocalidad != null ? this.codigoLocalidad.hashCode() : 0);
    hash = 67 * hash + (this.provincia != null ? this.provincia.hashCode() : 0);
    hash = 67 * hash + (this.nombre != null ? this.nombre.hashCode() : 0);
    hash = 67 * hash + (this.entidadGeografica != null ? this.entidadGeografica.hashCode() : 0);
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
    final CatLocalidad other = (CatLocalidad) obj;
    if (this.codigoLocalidad != other.codigoLocalidad && (this.codigoLocalidad == null || !this.codigoLocalidad.equals(other.codigoLocalidad))) {
      return false;
    }
    if (this.provincia != other.provincia && (this.provincia == null || !this.provincia.equals(other.provincia))) {
      return false;
    }
    if ((this.nombre == null) ? (other.nombre != null) : !this.nombre.equals(other.nombre)) {
      return false;
    }
    if (this.entidadGeografica != other.entidadGeografica && (this.entidadGeografica == null || !this.entidadGeografica.equals(other.entidadGeografica))) {
      return false;
    }
    return true;
  }
  
  

}