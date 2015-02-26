package es.caib.regweb.model;

import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 19/03/14
 */
@Entity
@Table(name = "RWE_TIPOASUNTO")
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@XmlRootElement(name = "tipoAsunto")
@XmlAccessorType(XmlAccessType.FIELD)
public class TipoAsunto extends Traducible {

    @XmlElement
    private Long id;
    @XmlTransient
    private Entidad entidad;
    @XmlElement
    private String codigo;
    @XmlElement
    private List<CodigoAsunto> codigosAsunto;
    @XmlElement
    private Boolean activo = true;

    public TipoAsunto() {
    }

    public TipoAsunto(Long id) {
        this.id = id;
    }

    public TipoAsunto(String id) {
        this.id = Long.valueOf(id);
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

    @ManyToOne(cascade = CascadeType.PERSIST, optional = false)
    @JoinColumn(name = "ENTIDAD")
    @ForeignKey(name = "RWE_TIPOASUNTO_ENTIDAD_FK")
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoAsunto")
    @LazyCollection(LazyCollectionOption.FALSE)
    @OrderColumn(name = "id")
    @OrderBy("id")
    public List<CodigoAsunto> getCodigosAsunto() {
        return codigosAsunto;
    }

    public void setCodigosAsunto(List<CodigoAsunto> codigosAsunto) {
        this.codigosAsunto = codigosAsunto;
    }

    @Column(name="ACTIVO", nullable= false)
    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

  @CollectionOfElements(fetch=FetchType.LAZY,targetElement = TraduccionTipoAsunto.class)
    @Cascade(value=org.hibernate.annotations.CascadeType.ALL)
    @LazyCollection(value= LazyCollectionOption.FALSE)
    @JoinTable(name="RWE_TRA_TIPOASUNTO",joinColumns={@JoinColumn(name="IDTIPOASUNTO")})
    @org.hibernate.annotations.MapKey(columns={@Column(name="LANG",length=2)})
    @ForeignKey(name="RWE_TASUNTO_TRATASUNTO_FK", inverseName = "RWE_TRATASUNTO_TASUNTO_FK")
    @Override
    @XmlTransient
    public Map<String,Traduccion> getTraducciones() {
        return traducciones;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      TipoAsunto that = (TipoAsunto) o;

      if (id != null ? !id.equals(that.id) : that.id != null) return false;

      return true;
    }

    @Override
    public int hashCode() {
      return id != null ? id.hashCode() : 0;
    }
}
