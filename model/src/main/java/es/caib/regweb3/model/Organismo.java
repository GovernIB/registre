package es.caib.regweb3.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;



/**
 * Created 18/02/14 14:59
 *
 * @author mgonzalez
 * @author anadal (index)
 */

@Entity
@Table(name = "RWE_ORGANISMO")
@org.hibernate.annotations.Table(appliesTo = "RWE_ORGANISMO", indexes = {
    @Index(name="RWE_ORGANI_ENTIDA_FK_I", columnNames = {"ENTIDAD"}),
    @Index(name="RWE_ORGANI_ESTADO_FK_I", columnNames = {"ESTADO"}),
    @Index(name="RWE_ORGANI_SUPERI_FK_I", columnNames = {"ORGANISMOSUPERIOR"}),
    @Index(name="RWE_ORGANI_RAIZ_FK_I", columnNames = {"ORGANISMORAIZ"}),
    @Index(name="RWE_ORGANI_CAUTON_FK_I", columnNames = {"CODAMBCOMUNIDAD"}),
    @Index(name="RWE_ORGANI_PROVIN_FK_I", columnNames = {"CODAMBPROVINCIA"})
})
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@XmlRootElement(name = "organismo")
@XmlAccessorType(XmlAccessType.FIELD)
public class Organismo implements Serializable {

    @XmlAttribute
    private Long id;
    @XmlElement
    private String codigo;
    @XmlTransient
    private Entidad entidad;
    @XmlTransient
    private List<Libro> libros;
    @XmlTransient
    private Long nivelJerarquico;
    @XmlTransient
    private CatEstadoEntidad estado;
    @XmlTransient
    private Organismo organismoSuperior;
    @XmlTransient
    private Organismo organismoRaiz;
    @XmlElement
    private String denominacion;
     /*private List<RelacionOrganizativaOfi> organizativaOfi;
     private List<RelacionSirOfi> sirOfi;*/

    //Externos
    @XmlTransient
    private CatNivelAdministracion nivelAdministracion;
    @XmlTransient
    private CatComunidadAutonoma codAmbComunidad;
    @XmlTransient
    private CatProvincia codAmbProvincia;

    @XmlTransient
    private Set<Organismo> historicoUO; // relacion de historicos


     /*public Organismo(Long id) {
         this.id = id;
     } */

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator = "generator")
    @Column(name="ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Column(name = "CODIGO", nullable = false, length = 9)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Column(name = "DENOMINACION", nullable = false, length = 300)
    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ENTIDAD")
    @ForeignKey(name = "RWE_ORGANISMO_ENTIDAD_FK")
    @JsonIgnore
    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ESTADO")
    @ForeignKey(name = "RWE_ORGANISMO_ESTADO_FK")
    @JsonIgnore
    public CatEstadoEntidad getEstado() {
        return estado;
    }

    public void setEstado(CatEstadoEntidad estado) {
        this.estado = estado;
    }


    @Column(name = "NIVELJERARQUICO", length = 6)
    public Long getNivelJerarquico() {
        return nivelJerarquico;
    }


    public void setNivelJerarquico(Long nivelJerarquico) {
        this.nivelJerarquico = nivelJerarquico;
    }


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name="ORGANISMOSUPERIOR")
    @ForeignKey(name="RWE_ORGANISMO_ORG_SUPERIOR_FK")
    @JsonIgnore
    public Organismo getOrganismoSuperior() {
        return organismoSuperior;
    }

    public void setOrganismoSuperior(Organismo organismoSuperior) {
        this.organismoSuperior = organismoSuperior;
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name="ORGANISMORAIZ")
    @ForeignKey(name="RWE_ORGANISMO_ORGRAIZ_FK")
    @JsonIgnore
    public Organismo getOrganismoRaiz() {
        return organismoRaiz;
    }

    public void setOrganismoRaiz(Organismo organismoRaiz) {
        this.organismoRaiz = organismoRaiz;
    }


    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL, mappedBy="organismo")
     /*@LazyCollection(LazyCollectionOption.FALSE)*/
    @OrderColumn(name = "id")
    @OrderBy("id")
    @JsonIgnore
    public List<Libro> getLibros() {
        return libros;
    }



    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name="NIVELADMINISTRACION")
    @ForeignKey(name="RWE_ORGANISMO_CATNIVELADMIN_FK")
    @JsonIgnore
    public CatNivelAdministracion getNivelAdministracion() {
        return nivelAdministracion;
    }


    public void setNivelAdministracion(CatNivelAdministracion nivelAdministracion) {
        this.nivelAdministracion = nivelAdministracion;
    }

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name="CODAMBCOMUNIDAD")
    @ForeignKey(name="RWE_ORGANISMO_CATAMBCOMAUTO_FK")
    @JsonIgnore
    public CatComunidadAutonoma getCodAmbComunidad() {
        return codAmbComunidad;
    }

    public void setCodAmbComunidad(CatComunidadAutonoma codAmbComunidad) {
        this.codAmbComunidad = codAmbComunidad;
    }

    @ManyToOne(cascade=CascadeType.PERSIST)
    @JoinColumn(name="CODAMBPROVINCIA")
    @ForeignKey(name="RWE_ORGANISMO_CATPROVINCIA_FK")
    @JsonIgnore
    public CatProvincia getCodAmbProvincia() {
        return codAmbProvincia;
    }

    public void setCodAmbProvincia(CatProvincia codAmbProvincia) {
        this.codAmbProvincia = codAmbProvincia;
    }

    @ManyToMany(cascade=CascadeType.PERSIST, fetch= FetchType.EAGER)
    @JoinTable(name="RWE_HISTORICOUO",
               joinColumns=@JoinColumn(name="CODANTERIOR"),
               inverseJoinColumns=@JoinColumn(name="CODULTIMA"))
    @ForeignKey(name="RWE_ORG_ORG_HISTANTE_FK", inverseName = "RWE_ORG_ORG_HISTULTI_FK")
    @JsonIgnore
    public Set<Organismo> getHistoricoUO() {
      return historicoUO;
    }


    public void setHistoricoUO(Set<Organismo> historicoUO) {
      this.historicoUO = historicoUO;
    }


    @Transient
    public String getNombreCompleto(){
        return  getCodigo() +" "+ getDenominacion();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organismo organismo = (Organismo) o;

        if (id != null ? !id.equals(organismo.id) : organismo.id != null) return false;

        return true;
    }
    

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    @Override
    public String toString() {
        return denominacion;
    }


}
