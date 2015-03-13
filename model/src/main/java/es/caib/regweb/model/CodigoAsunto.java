package es.caib.regweb.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.*;
import java.util.Map;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal (index)
 * Date: 19/03/14
 */
@Entity
@Table(name = "RWE_CODIGOASUNTO",
        uniqueConstraints= @UniqueConstraint(columnNames={"CODIGO", "TIPOASUNTO"}))
@org.hibernate.annotations.Table(appliesTo = "RWE_CODIGOASUNTO", indexes = {
    @Index(name="RWE_CODASU_TASUN_FK_I", columnNames = {"TIPOASUNTO"})
})
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@XmlRootElement(name = "codigoAsunto")
@XmlAccessorType(XmlAccessType.FIELD)
public class CodigoAsunto extends Traducible {

    @XmlAttribute
    private Long id;
    @XmlTransient
    private TipoAsunto tipoAsunto;
    @XmlElement
    private String codigo;

    public CodigoAsunto() {
    }

    public CodigoAsunto(Long id) {
        this.id = id;
    }

    public CodigoAsunto(String id) {
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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "TIPOASUNTO")
    @ForeignKey(name = "RWE_CODASUNTO_TIPOASUNTO_FK")
    @JsonIgnore
    public TipoAsunto getTipoAsunto() {
        return tipoAsunto;
    }

    public void setTipoAsunto(TipoAsunto tipoAsunto) {
        this.tipoAsunto = tipoAsunto;
    }

    @Column(name = "CODIGO", length = 16, nullable = false)
    public String getCodigo() {
      return codigo;
    }

    public void setCodigo(String codigo) {
      this.codigo = codigo;
    }

    @CollectionOfElements(fetch=FetchType.LAZY,targetElement = TraduccionCodigoAsunto.class)
    @Cascade(value=org.hibernate.annotations.CascadeType.ALL)
    @LazyCollection(value= LazyCollectionOption.FALSE)
    @JoinTable(name="RWE_TRA_CODIGOASUNTO",joinColumns={@JoinColumn(name="IDCODIGOASUNTO")})
    @org.hibernate.annotations.MapKey(columns={@Column(name="LANG",length=2)})
    @ForeignKey(name="RWE_CODASUNTO_TRACODASUNTO_FK", inverseName = "RWE_TRACODASUNTO_COD   ASUNTO_FK")
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
