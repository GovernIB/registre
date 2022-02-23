package es.caib.regweb3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal (index)
 * Date: 19/03/14
 */
@Entity
@Table(name = "RWE_CODIGOASUNTO", uniqueConstraints= @UniqueConstraint(columnNames={"CODIGO"}))
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@XmlRootElement(name = "codigoAsunto")
@XmlAccessorType(XmlAccessType.FIELD)
public class CodigoAsunto extends Traducible {

    @XmlAttribute
    private Long id;
    @XmlTransient
    private Entidad entidad;
    @XmlElement
    private String codigo;
    @XmlElement
    private Boolean activo = true;

    public CodigoAsunto() {
    }

    public CodigoAsunto(Long id) {
        this.id = id;
    }

    public CodigoAsunto(String id) {
        this.id = Long.valueOf(id);
    }


    /**
     *
     * @param ta
     */
    public CodigoAsunto(CodigoAsunto ta) {

      this.id = ta.id;
      this.entidad = ta.entidad;
      this.codigo = ta.codigo;
      this.activo = ta.activo;
      this.traducciones = new HashMap<String, Traduccion>(ta.getTraducciones());
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

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ENTIDAD")
    @ForeignKey(name = "RWE_CODASUNTO_ENTIDAD_FK")
    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    @Column(name = "CODIGO", length = 16, nullable = false)
    public String getCodigo() {
      return codigo;
    }

    public void setCodigo(String codigo) {
      this.codigo = codigo;
    }

    @Column(name = "ACTIVO", nullable = false)
    @JsonIgnore
    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @CollectionOfElements(fetch=FetchType.LAZY,targetElement = TraduccionCodigoAsunto.class)
    @Cascade(value=org.hibernate.annotations.CascadeType.ALL)
    @LazyCollection(value= LazyCollectionOption.FALSE)
    @JoinTable(name="RWE_TRA_CODIGOASUNTO",joinColumns={@JoinColumn(name="IDCODIGOASUNTO")})
    @org.hibernate.annotations.MapKey(columns={@Column(name="LANG",length=2)})
    @ForeignKey(name="RWE_CODASUNTO_TRACODASUNTO_FK", inverseName = "RWE_TRACODASUNTO_CODASUNTO_FK")
    @Override
    @XmlTransient
    public Map<String,Traduccion> getTraducciones() {
        return traducciones;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      CodigoAsunto that = (CodigoAsunto) o;

      if (id != null ? !id.equals(that.id) : that.id != null) return false;

      return true;
    }

    @Override
    public int hashCode() {
      return id != null ? id.hashCode() : 0;
    }
}
