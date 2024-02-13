package es.caib.regweb3.model;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author anadal (index)
 * Date: 16/01/14
 */
@Entity
@Table(name = "RWE_ENTIDAD", indexes =
@Index(name = "RWE_ENTIDA_PRO_FK_I", columnList = "PROPIETARIO"))
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@XmlRootElement(name = "entidad")
@XmlAccessorType(XmlAccessType.FIELD)
public class Entidad implements Serializable {

    @XmlAttribute
    private Long id;
    @XmlElement
    private String nombre;
    @XmlTransient
    private String descripcion;
    @XmlTransient
    private String codigoDir3;
    @XmlTransient
    private String sello;
    @XmlTransient
    private Usuario propietario;
    @XmlTransient
    private Set<UsuarioEntidad> administradores;
    @XmlTransient
    private List<Organismo> organismos;
    @XmlTransient
    private String numRegistro;
    @XmlTransient
    private Long configuracionPersona;
    @XmlTransient
    private String colorMenu = "#ff9523";
    @XmlTransient
    private String textoPie;
    @XmlTransient
    private Archivo logoMenu;
    @XmlTransient
    private Archivo logoPie;
    @XmlTransient
    private Archivo logoSello;
    @XmlTransient
    private Integer diasVisado;
    @XmlTransient
    private Boolean sir = false;
    @XmlTransient
    private Contador contadorSir;
    @XmlTransient
    private Boolean activo = true;
    @XmlTransient
    private Boolean mantenimiento = false;

    /**
     * IMPORTANT: Ja no s'utilitza. Es mante per si en un futur a
     * l'Administrador d'Entitat se i permet tenir varis Plugins donats d'alta i
     * des d'aquest valor poder anar canviant de tipus d'scan.
     */
    @XmlTransient
    private String tipoScan;
    @XmlTransient
    private Integer posXsello;
    @XmlTransient
    private Integer posYsello;
    @XmlTransient
    private Long perfilCustodia;
    @XmlTransient
    private Libro libro;
    @XmlTransient
    private Boolean regSalidasPersonas = true;


    public Entidad() {
        super();
    }


    public Entidad(Long id) {
        this.id = id;
    }

    public Entidad(Long id, String nombre, String codigoDir3) {
        this.id = id;
        this.nombre = nombre;
        this.codigoDir3 = codigoDir3;
    }

    /**
     * @param e
     */
    public Entidad(Entidad e) {
        super();
        this.id = e.id;
        this.nombre = e.nombre;
        this.descripcion = e.descripcion;
        this.codigoDir3 = e.codigoDir3;
        this.sello = e.sello;
        this.propietario = e.propietario;
        this.administradores = e.administradores;
        this.organismos = e.organismos;
        this.numRegistro = e.numRegistro;
        this.configuracionPersona = e.configuracionPersona;
        this.colorMenu = e.colorMenu;
        this.textoPie = e.textoPie;
        this.logoMenu = e.logoMenu;
        this.logoPie = e.logoPie;
        this.logoSello = e.logoSello;
        this.diasVisado = e.diasVisado;
        this.sir = e.sir;
        this.activo = e.activo;
        this.tipoScan = e.tipoScan;
        this.posXsello = e.posXsello;
        this.posYsello = e.posYsello;
    }

    public Entidad(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    /**
     * IMPORTANT: Ja no s'utilitza. Es mante per si en un futur a
     * l'Administrador d'Entitat se i permet tenir varis Plugins donats d'alta i
     * des d'aquest valor poder anar canviant de tipus d'scan.
     */
    @Deprecated
    @Column(name = "TIPSCAN", length = 20)
    public String getTipoScan() {
        return tipoScan;
    }

    @Deprecated
    public void setTipoScan(String tipoScan) {
        this.tipoScan = tipoScan;
    }


    @Column(name = "NOMBRE", nullable = false)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Column(name = "DESCRIPCION", nullable = false, length = 4000)
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Column(name = "CODIGODIR3", nullable = false, unique = true)
    public String getCodigoDir3() {
        return codigoDir3;
    }

    public void setCodigoDir3(String codigoDir3) {
        this.codigoDir3 = codigoDir3;
    }

    @Column(name = "SELLO", length = 4000)
    public String getSello() {
        return sello;
    }

    public void setSello(String sello) {
        this.sello = sello;
    }

    @ManyToOne()
    @JoinColumn(name = "PROPIETARIO", foreignKey = @ForeignKey(name = "RWE_ENTIDAD_USU_PROP_FK"))
    public Usuario getPropietario() {
        return propietario;
    }

    public void setPropietario(Usuario propietario) {
        this.propietario = propietario;
    }

    @ManyToMany(targetEntity = UsuarioEntidad.class, fetch = FetchType.EAGER)
    @JoinTable(name = "RWE_ENTIDAD_USUENT", foreignKey = @ForeignKey(name = "RWE_USU_ADM_ENTIDAD_FK"),
            joinColumns = {@JoinColumn(name = "IDENTIDAD")}, inverseJoinColumns = {@JoinColumn(name = "IDUSUENT")})
    @OrderBy("id")
    public Set<UsuarioEntidad> getAdministradores() {
        return administradores;
    }

    public void setAdministradores(Set<UsuarioEntidad> administradores) {
        this.administradores = administradores;
    }


    @OneToMany(cascade = CascadeType.REMOVE, targetEntity = Organismo.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ENTIDAD", foreignKey = @ForeignKey(name = "RWE_ORGANISMO_ENTIDAD_FK"))
    public List<Organismo> getOrganismos() {
        return organismos;
    }

    public void setOrganismos(List<Organismo> organismos) {
        this.organismos = organismos;
    }

    @Column(name = "NUMREGISTRO", length = 4000)
    public String getNumRegistro() {
        return numRegistro;
    }

    public void setNumRegistro(String numRegistro) {
        this.numRegistro = numRegistro;
    }


    @Column(name = "CONFIGPERSONA")
    public Long getConfiguracionPersona() {
        return configuracionPersona;
    }

    public void setConfiguracionPersona(Long configuracionPersona) {
        this.configuracionPersona = configuracionPersona;
    }

    @Column(name = "COLORMENU")
    public String getColorMenu() {
        return colorMenu;
    }

    public void setColorMenu(String colorMenu) {
        this.colorMenu = colorMenu;
    }

    @Column(name = "TEXTOPIE", length = 4000)
    public String getTextoPie() {
        return textoPie;
    }

    public void setTextoPie(String textoPie) {
        this.textoPie = textoPie;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "LOGOMENU", foreignKey = @ForeignKey(name = "RWE_ENTIDAD_LOGOMENU_FK"))
    public Archivo getLogoMenu() {
        return logoMenu;
    }

    public void setLogoMenu(Archivo logoMenu) {
        this.logoMenu = logoMenu;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "LOGOPIE", foreignKey = @ForeignKey(name = "RWE_ENTIDAD_LOGOPIE_FK"))
    public Archivo getLogoPie() {
        return logoPie;
    }

    public void setLogoPie(Archivo logoPie) {
        this.logoPie = logoPie;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "LOGOSELLO", foreignKey = @ForeignKey(name = "RWE_ENTIDAD_LOGOSELLO_FK"))
    public Archivo getLogoSello() {
        return logoSello;
    }

    public void setLogoSello(Archivo logoSello) {
        this.logoSello = logoSello;
    }


    @Column(name = "DIASVISADO")
    public Integer getDiasVisado() {
        return diasVisado;
    }

    public void setDiasVisado(Integer diasVisado) {
        this.diasVisado = diasVisado;
    }

    @Column(name = "SIR", nullable = false)
    public Boolean getSir() {
        return sir;
    }

    public void setSir(Boolean sir) {
        this.sir = sir;
    }

    @ManyToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "CONTADOR_SIR", foreignKey = @ForeignKey(name = "RWE_ENTIDAD_CONT_SIR_FK"))
    public Contador getContadorSir() {
        return contadorSir;
    }

    public void setContadorSir(Contador contadorSir) {
        this.contadorSir = contadorSir;
    }

    @Column(name = "ACTIVO", nullable = false)
    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Column(name = "MANTENIMIENTO", nullable = false)
    public Boolean getMantenimiento() {
        return mantenimiento;
    }

    public void setMantenimiento(Boolean mantenimiento) {
        this.mantenimiento = mantenimiento;
    }

    @Column(name = "POSXSELLO", length = 3)
    public Integer getPosXsello() {
        return posXsello;
    }

    public void setPosXsello(Integer posXsello) {
        this.posXsello = posXsello;
    }

    @Column(name = "POSYSELLO", length = 3)
    public Integer getPosYsello() {
        return posYsello;
    }

    public void setPosYsello(Integer posYsello) {
        this.posYsello = posYsello;
    }

    @Column(name = "PERFIL_CUSTODIA")
    public Long getPerfilCustodia() {
        return perfilCustodia;
    }

    public void setPerfilCustodia(Long perfilCustodia) {
        this.perfilCustodia = perfilCustodia;
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "LIBRO", foreignKey = @ForeignKey(name = "RWE_ENTIDAD_LIBRO_FK"))
    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    @Column(name = "REG_SALIDAS_PERSONAS")
    public Boolean getRegSalidasPersonas() {
        return regSalidasPersonas;
    }

    public void setRegSalidasPersonas(Boolean regSalidasPersonas) {
        this.regSalidasPersonas = regSalidasPersonas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entidad entidad = (Entidad) o;

        if (id != null ? !id.equals(entidad.id) : entidad.id != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
