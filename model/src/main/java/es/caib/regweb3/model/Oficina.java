package es.caib.regweb3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.caib.regweb3.utils.RegwebConstantes;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;


/**
 * Created 17/02/14 13:06
 *
 * @author mgonzalez
 * @author anadal (index)
 */
@Entity
@Table(name = "RWE_OFICINA", indexes = {
        @Index(name = "RWE_OFICIN_ESTENT_FK_I", columnList = "ESTADO"),
        @Index(name = "RWE_OFICIN_PAIS_FK_I", columnList = "PAIS"),
        @Index(name = "RWE_OFICIN_ISLA_FK_I", columnList = "ISLA"),
        @Index(name = "RWE_OFICIN_COMUNI_FK_I", columnList = "COMUNIDAD"),
        @Index(name = "RWE_OFICIN_LOCALI_FK_I", columnList = "LOCALIDAD"),
        @Index(name = "RWE_OFICIN_TVIA_FK_I", columnList = "TIPOVIA"),
        @Index(name = "RWE_OFICIN_ORGANI_FK_I", columnList = "ORGANISMORESPONSABLE"),
        @Index(name = "RWE_OFICIN_OFICIN_FK_I", columnList = "OFICINARESPONSABLE")
})
@SequenceGenerator(name = "generator", sequenceName = "RWE_OFICINA_SEQ", allocationSize = 1)
@XmlRootElement(name = "oficina")
@XmlAccessorType(XmlAccessType.FIELD)
public class Oficina implements Serializable {

    @XmlAttribute
    private Long id;
    @XmlElement
    private String codigo;
    @XmlTransient
    private Entidad entidad;
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
    @XmlTransient
    private CatPais codPais;
    @XmlTransient
    private CatComunidadAutonoma codComunidad;
    @XmlTransient
    private CatLocalidad localidad;
    @XmlTransient
    private CatIsla isla;
    @XmlTransient
    private CatTipoVia tipoVia;
    @XmlTransient
    private String nombreVia;
    @XmlTransient
    private String numVia;
    @XmlTransient
    private String codPostal;
    @XmlTransient
    private Set<CatServicio> servicios;
    @XmlTransient
    private Boolean oamr = false;
    @Transient
    private Boolean isSirRecepcion = false;
    @Transient
    private Boolean isSirEnvio = false;
    @Transient
    private Boolean isSir = false;
    @Transient
    private Boolean isOficinaSir = false;


    public Oficina() {
        super();
    }

    public Oficina(Long id) {
        this.id = id;
    }

    public Oficina(Long idEntidad, String codigo) {
        this.entidad = new Entidad(idEntidad);
        this.codigo = codigo;
    }

    public Oficina(Long id, String codigo, String denominacion) {
        this.id = id;
        this.codigo = codigo;
        this.denominacion = denominacion;
    }

    public Oficina(Long id, String codigo, String denominacion, Long organismoResponsable) {
        this.id = id;
        this.codigo = codigo;
        this.denominacion = denominacion;
        this.organismoResponsable = new Organismo(organismoResponsable);
    }

    public Oficina(Long id, String codigo, String denominacion, Long oficinaResponsable, Long organismoResponsable) {
        this.id = id;
        this.codigo = codigo;
        this.denominacion = denominacion;
        this.oficinaResponsable = new Oficina(oficinaResponsable);
        this.organismoResponsable = new Organismo(organismoResponsable);
    }

    public Oficina(Long id, String codigo, String denominacion, Long oficinaResponsable, Long organismoResponsable, Set<CatServicio> servicios) {
        this.id = id;
        this.codigo = codigo;
        this.denominacion = denominacion;
        this.oficinaResponsable = new Oficina(oficinaResponsable);
        this.organismoResponsable = new Organismo(organismoResponsable);
        this.servicios = servicios;
    }

    public Oficina(Long id, String codigo, String denominacion, Long organismoResponsable, String organismoResponsableCodigo, String organismoResponsableDenominacion) {
        this.id = id;
        this.codigo = codigo;
        this.denominacion = denominacion;
        this.organismoResponsable = new Organismo(organismoResponsable, organismoResponsableCodigo, organismoResponsableDenominacion);
    }

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    @Column(name = "ID")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENTIDAD", foreignKey = @ForeignKey(name = "RWE_OFICINA_ENTIDAD_FK"))
    @JsonIgnore
    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    @ManyToOne()
    @JoinColumn(name = "ESTADO", foreignKey = @ForeignKey(name = "RWE_OFICINA_ESTADO_FK"))
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


    @ManyToOne()
    @JoinColumn(name = "ORGANISMORESPONSABLE", foreignKey = @ForeignKey(name = "RWE_OFICINA_ORGANISMO_FK"))
    @JsonIgnore
    public Organismo getOrganismoResponsable() {
        return organismoResponsable;
    }

    public void setOrganismoResponsable(Organismo organismoResponsable) {
        this.organismoResponsable = organismoResponsable;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OFICINARESPONSABLE", foreignKey = @ForeignKey(name = "RWE_OFICINA_OFICINA_FK"))
    @JsonIgnore
    public Oficina getOficinaResponsable() {
        return oficinaResponsable;
    }

    public void setOficinaResponsable(Oficina oficinaResponsable) {
        this.oficinaResponsable = oficinaResponsable;
    }


    /**
     * @return the organizativasOfi
     */
    @OneToMany(mappedBy = "oficina",fetch = FetchType.LAZY)
    /*@ForeignKey(name = "RWE_OFICINA_RELORGOFI_FK")*/
    @JsonIgnore
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
    public String getNombreCompleto() {
        return getCodigo() + " " + getDenominacion();
    }

    /**
     * Retorna todos los Organismos a los que la Oficina da servicio
     */
    @Transient
    @JsonIgnore
    public LinkedHashSet<Organismo> getOrganismosFuncionales() {

        LinkedHashSet<Organismo> organismos = new LinkedHashSet<Organismo>();

        // Añadimos el Organismo responsable de la OficinaActiva
        organismos.add(this.getOrganismoResponsable());

        // Añadimos solo los Organismos del estado que nos indican
        Hibernate.initialize(this.getOrganizativasOfi());
        for (RelacionOrganizativaOfi relacionOrganizativaOfi : this.getOrganizativasOfi()) {
            if (RegwebConstantes.ESTADO_ENTIDAD_VIGENTE.equals(relacionOrganizativaOfi.getEstado().getCodigoEstadoEntidad())) {
                organismos.add(relacionOrganizativaOfi.getOrganismo());
            }
        }

        return organismos;

    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PAIS", foreignKey = @ForeignKey(name = "RWE_OFICINA_PAIS_FK"))
    @JsonIgnore
    public CatPais getCodPais() {
        return codPais;
    }

    public void setCodPais(CatPais codPais) {
        this.codPais = codPais;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMUNIDAD", foreignKey = @ForeignKey(name = "RWE_OFICINA_COMUNIDAD_FK"))
    @JsonIgnore
    public CatComunidadAutonoma getCodComunidad() {
        return codComunidad;
    }

    public void setCodComunidad(CatComunidadAutonoma codComunidad) {
        this.codComunidad = codComunidad;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LOCALIDAD", foreignKey = @ForeignKey(name = "RWE_OFICINA_LOCALIDAD_FK"))
    @JsonIgnore
    public CatLocalidad getLocalidad() {
        return localidad;
    }

    public void setLocalidad(CatLocalidad localidad) {
        this.localidad = localidad;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ISLA", foreignKey = @ForeignKey(name = "RWE_OFICINA_ISLA_FK"))
    @JsonIgnore
    public CatIsla getIsla() {
        return isla;
    }

    public void setIsla(CatIsla isla) {
        this.isla = isla;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIPOVIA", foreignKey = @ForeignKey(name = "RWE_OFICINA_TIPOVIA_FK"))
    @JsonIgnore
    public CatTipoVia getTipoVia() {
        return tipoVia;
    }

    public void setTipoVia(CatTipoVia tipoVia) {
        this.tipoVia = tipoVia;
    }

    @Column(name = "NOMBREVIA", length = 300)
    @JsonIgnore
    public String getNombreVia() {
        return nombreVia;
    }

    public void setNombreVia(String nombreVia) {
        this.nombreVia = nombreVia;
    }

    @Column(name = "NUMVIA", length = 20)
    @JsonIgnore
    public String getNumVia() {
        return numVia;
    }

    public void setNumVia(String numVia) {
        this.numVia = numVia;
    }

    @Column(name = "CODPOSTAL", length = 14)
    @JsonIgnore
    public String getCodPostal() {
        return codPostal;
    }

    public void setCodPostal(String codPostal) {
        this.codPostal = codPostal;
    }

    @ManyToMany(targetEntity = CatServicio.class, fetch = FetchType.EAGER)
    @JoinTable(name = "RWE_OFICINA_SERVICIO", foreignKey = @ForeignKey(name = "RWE_SERVICIO_OFICINA_FK"),
            joinColumns = {@JoinColumn(name = "IDOFICINA")}, inverseJoinColumns = {@JoinColumn(name = "IDSERVICIO")})
    @OrderBy("id")
    @JsonIgnore
    public Set<CatServicio> getServicios() {
        return servicios;
    }

    public void setServicios(Set<CatServicio> servicios) {
        this.servicios = servicios;
    }

    @Column(name = "OAMR", nullable = false)
    public Boolean getOamr() {
        return oamr;
    }

    public void setOamr(Boolean oamr) {
        this.oamr = oamr;
    }

    @Transient
    @JsonIgnore
    public Boolean getSirRecepcion() {
        return isSirRecepcion;
    }

    public void setSirRecepcion(Boolean sirRecepcion) {
        isSirRecepcion = sirRecepcion;
    }

    @Transient
    @JsonIgnore
    public Boolean getSir() {
        for (CatServicio servicio : servicios) {
            if (servicio.getCodServicio().equals(RegwebConstantes.OFICINA_INTEGRADA_SIR) ||
                    servicio.getCodServicio().equals(RegwebConstantes.OFICINA_INTEGRADA_SIR_ENVIO) ||
                    servicio.getCodServicio().equals(RegwebConstantes.OFICINA_INTEGRADA_SIR_RECEPCION)) {
                return true;
            }
        }
        return false;
    }


    @Transient
    @JsonIgnore
    public Boolean getSirEnvio() {
        return isSirEnvio;
    }

    public void setSirEnvio(Boolean sirEnvio) {
        isSirEnvio = sirEnvio;
    }

    @Transient
    public String getNombre() {
        return getDenominacion();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Oficina oficina = (Oficina) o;

        return id != null ? id.equals(oficina.id) : oficina.id == null;

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
