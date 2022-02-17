package es.caib.regweb3.model;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Fundació BIT.
 *
 * @author anadal (index)
 * @author earrivi
 * Date: 16/01/14
 */
@Entity
@Table(name = "RWE_LIBRO", indexes = {
        @Index(name = "RWE_LIBRO_CONENT_FK_I", columnList = "CONTADOR_ENTRADA"),
        @Index(name = "RWE_LIBRO_CONSAL_FK_I", columnList = "CONTADOR_SALIDA"),
        @Index(name = "RWE_LIBRO_CONOFI_FK_I", columnList = "CONTADOR_OFICIO_REMISION"),
        @Index(name = "RWE_LIBRO_ORGANI_FK_I", columnList = "ORGANISMO")
})
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@XmlRootElement(name = "libro")
@XmlAccessorType(XmlAccessType.FIELD)
public class Libro implements Serializable {

    @XmlAttribute
    private Long id;
    @XmlElement
    private String nombre;
    @XmlTransient
    private String codigo;
    @XmlTransient
    private Boolean activo = true;
    @XmlTransient
    private Contador contadorEntrada;
    @XmlTransient
    private Contador contadorSalida;
    @XmlTransient
    private Contador contadorOficioRemision;
    @XmlTransient
    private Contador contadorSir;
    @XmlTransient
    private Organismo organismo;

    public Libro() {
    }

    public Libro(String id) {
        this.id = Long.valueOf(id);
    }

    public Libro(Long id) {

        this.id = id;
    }

    public Libro(Long id, String nombre, Long idOrganismo) {
        this.id = id;
        this.nombre = nombre;
        this.organismo = new Organismo(idOrganismo);
    }

    public Libro(Long id, String nombre, String codigo) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
    }

    public Libro(Long id, String nombre, String codigo, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.activo = activo;
    }

    public Libro(Long id, String nombre, String codigo, Long idOrganismo, String denominacionOrganismo) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.organismo = new Organismo(idOrganismo, denominacionOrganismo);
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

    @Column(name = "NOMBRE", nullable = false)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Column(name = "CODIGO", nullable = false, length = 4)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @Column(name = "ACTIVO", nullable = false)
    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @ManyToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "CONTADOR_ENTRADA", foreignKey = @ForeignKey(name = "RWE_LIBRO_CONT_ENT_FK"))
    public Contador getContadorEntrada() {
        return contadorEntrada;
    }

    public void setContadorEntrada(Contador contadorEntrada) {
        this.contadorEntrada = contadorEntrada;
    }

    @ManyToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "CONTADOR_SALIDA", foreignKey = @ForeignKey(name = "RWE_LIBRO_CONT_SAL_FK"))
    public Contador getContadorSalida() {
        return contadorSalida;
    }

    public void setContadorSalida(Contador contadorSalida) {
        this.contadorSalida = contadorSalida;
    }

    @ManyToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "CONTADOR_OFICIO_REMISION", foreignKey = @ForeignKey(name = "RWE_LIBRO_CONT_ORM_FK"))
    public Contador getContadorOficioRemision() {
        return contadorOficioRemision;
    }

    public void setContadorOficioRemision(Contador contadorOficioRemision) {
        this.contadorOficioRemision = contadorOficioRemision;
    }

    @ManyToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "CONTADOR_SIR", foreignKey = @ForeignKey(name = "RWE_LIBRO_CONT_SIR_FK"))
    public Contador getContadorSir() {
        return contadorSir;
    }

    public void setContadorSir(Contador contadorSir) {
        this.contadorSir = contadorSir;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORGANISMO", foreignKey = @ForeignKey(name = "RWE_LIBRO_ORGANISMO_FK"))
    public Organismo getOrganismo() {
        return organismo;
    }

    public void setOrganismo(Organismo organismo) {
        this.organismo = organismo;
    }

    @Transient
    public String getLibroOrganismo() {
        return getNombre() + " - " + getOrganismo().getDenominacion();
    }

    @Transient
    public String getNombreCompleto() {
        if (getOrganismo() != null) {
            return getOrganismo().getDenominacion() + " - " + getNombre();
        } else {
            return getNombre();
        }
    }

    @Transient
    public String getCodigoNombre() {
        return getCodigo() + " - " + getNombre();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Libro libro = (Libro) o;

        if (id != null ? !id.equals(libro.id) : libro.id != null) return false;

        return true;
    }

    @Override
    public String toString() {
        if (nombre != null) {
            return nombre;
        } else if (id != null) {
            return id.toString();
        } else {
            return null;
        }
    }
}
