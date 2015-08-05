package es.caib.regweb3.model;

import es.caib.regweb3.utils.RegwebConstantes;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/**
 * Created 17/02/14 13:06
 *
 * @author mgonzalez
 * @author anadal (index)
 */
@Entity
@Table(name = "RWE_OFICINA")
@org.hibernate.annotations.Table(appliesTo = "RWE_OFICINA", indexes = {
    @Index(name="RWE_OFICIN_ESTENT_FK_I", columnNames = {"ESTADO"}),
    @Index(name="RWE_OFICIN_ORGANI_FK_I", columnNames = {"ORGANISMORESPONSABLE"}),
    @Index(name="RWE_OFICIN_OFICIN_FK_I", columnNames = {"OFICINARESPONSABLE"})
})
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@XmlRootElement(name = "oficina")
@XmlAccessorType(XmlAccessType.FIELD)
public class Oficina implements Serializable{

    @XmlAttribute
    private Long id;
    @XmlElement
    private String codigo;
    @XmlTransient
    private CatEstadoEntidad estado;
    @XmlElement
    private String denominacion;
    @XmlTransient
    private Organismo organismoResponsable;
    @XmlTransient
    private Oficina oficinaResponsable;
    @XmlTransient
  	private Set<RelacionOrganizativaOfi> organizativasOfi;

    /**
     * 
     */
    public Oficina() {
      super();
    }


    /**
     * @param id
     * @param codigo
     * @param estado
     * @param denominacion
     * @param organismoResponsable
     * @param oficinaResponsable
     * @param organizativasOfi
     */
    public Oficina(Oficina o) {
      super();
      this.id = o.id;
      this.codigo = o.codigo;
      this.estado = o.estado;
      this.denominacion = o.denominacion;
      this.organismoResponsable = o.organismoResponsable;
      this.oficinaResponsable = o.oficinaResponsable;
      this.organizativasOfi = o.organizativasOfi;
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


    @Column(name = "CODIGO", nullable = false, length = 9, unique = true)
    public String getCodigo() {
      return codigo;
    }


    public void setCodigo(String codigo) {
      this.codigo = codigo;
    }


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ESTADO")
    @ForeignKey(name = "RWE_OFICINA_ESTADO_FK")
    public CatEstadoEntidad getEstado() {
        return estado;
    }

    public void setEstado(CatEstadoEntidad estado) {
        this.estado = estado;
    }


    @Column(name = "DENOMINACION", nullable = false, length = 300)
    public String getDenominacion() {
        return denominacion;
    }


    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="ORGANISMORESPONSABLE")
    @ForeignKey(name="RWE_OFICINA_ORGANISMO_FK")
    public Organismo getOrganismoResponsable() {
        return organismoResponsable;
    }

    public void setOrganismoResponsable(Organismo organismoResponsable) {
        this.organismoResponsable = organismoResponsable;
    }

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="OFICINARESPONSABLE")
    @ForeignKey(name="RWE_OFICINA_OFICINA_FK")
    public Oficina getOficinaResponsable() {
        return oficinaResponsable;
    }

    public void setOficinaResponsable(Oficina oficinaResponsable) {
        this.oficinaResponsable = oficinaResponsable;
    }


    /**
    * @return the organizativasOfi
    */
    @OneToMany(mappedBy="oficina")
    @ForeignKey(name="RWE_OFICINA_RELORGOFI_FK")
    public Set<RelacionOrganizativaOfi> getOrganizativasOfi() {
      return organizativasOfi;
    }

    /**
    * @param organizativasOfi the organizativasOfi to set
    */
    public void setOrganizativasOfi(Set<RelacionOrganizativaOfi> organizativasOfi) {
     this.organizativasOfi = organizativasOfi;
    }

    @Transient
    public String getNombreCompleto(){
        return  getCodigo() +" "+ getDenominacion();
    }

    @Transient
    public Set<Organismo> getOrganismosFuncionales(){

        Set<Organismo> organismos = new HashSet<Organismo>();

        // Añadimos el Organismo responsable de la OficinaActiva
        Organismo organismoResponsable = this.getOrganismoResponsable();
        organismos.add(organismoResponsable);

        // Añadimos los Organismos a los que la Oficina da servicio
        Set<RelacionOrganizativaOfi> organismosFuncionales = this.getOrganizativasOfi();
        for(RelacionOrganizativaOfi relacionOrganizativaOfi:organismosFuncionales){
            if(RegwebConstantes.ESTADO_ENTIDAD_VIGENTE.equals(relacionOrganizativaOfi.getEstado().getCodigoEstadoEntidad())) {
                organismos.add(relacionOrganizativaOfi.getOrganismo());
            }
        }

        return  organismos;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Oficina oficina = (Oficina) o;

        if (id != null ? !id.equals(oficina.id) : oficina.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return denominacion;
    }
}
