package es.caib.regweb.model;

import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 27/03/14
 */
@Entity
@Table(name = "RWE_REGISTRO_SALIDA")
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@XmlRootElement(name = "registroSalida")
@XmlAccessorType(XmlAccessType.FIELD)
public class RegistroSalida implements Serializable{

    @XmlAttribute
    private Long id;
    @XmlElement
    private UsuarioEntidad usuario;
    @XmlElement
    private Oficina oficina;
    @XmlElement
    private Organismo origen;
    @XmlElement
    private String origenExternoCodigo;
    @XmlElement
    private String origenExternoDenominacion;
    @XmlElement
    private Date fecha;
    @XmlElement
    private Libro libro;
    @XmlElement
    private Integer numeroRegistro;
    @XmlElement
    private String numeroRegistroFormateado;
    @XmlElement
    private Long estado;
    @XmlElement
    private RegistroDetalle registroDetalle;



    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "generator")
    @Column(name="ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(cascade= CascadeType.PERSIST, optional = false)
    @JoinColumn(name="USUARIO")
    @ForeignKey(name="RWE_REGSAL_USUSAL_FK")
    public UsuarioEntidad getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntidad usuario) {
        this.usuario = usuario;
    }

    @ManyToOne(cascade= CascadeType.PERSIST, optional = false)
    @JoinColumn(name="OFICINA")
    @ForeignKey(name="RWE_REGSAL_OFICINA_FK")
    public Oficina getOficina() {
        return oficina;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    @ManyToOne(cascade= CascadeType.PERSIST)
    @JoinColumn(name="ORIGEN")
    @ForeignKey(name="RWE_REGSAL_ORIGEN_FK")
    public Organismo getOrigen() {
        return origen;
    }

    public void setOrigen(Organismo origen) {
        this.origen = origen;
    }

    @Column(name = "DESTEXTCOD", length = 9)
    public String getOrigenExternoCodigo() {
        return origenExternoCodigo;
    }

    public void setOrigenExternoCodigo(String origenExternoCodigo) {
        this.origenExternoCodigo = origenExternoCodigo;
    }

    @Column(name = "DESTEXTDEN", length = 300)
    public String getOrigenExternoDenominacion() {
        return origenExternoDenominacion;
    }

    public void setOrigenExternoDenominacion(String origenExternoDenominacion) {
        this.origenExternoDenominacion = origenExternoDenominacion;
    }

    @Column(name = "FECHA", nullable = false)
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @ManyToOne(cascade= CascadeType.PERSIST, optional = false)
    @JoinColumn(name="LIBRO")
    @ForeignKey(name="RWE_REGSAL_LIBRO_FK")
    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    @Column(name = "NUMREGISTRO", nullable = false)
    public Integer getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(Integer numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    @Column(name = "NUMREGFORMAT", nullable = false)
    public String getNumeroRegistroFormateado() {
        return numeroRegistroFormateado;
    }

    public void setNumeroRegistroFormateado(String numeroRegistroFormateado) {
        this.numeroRegistroFormateado = numeroRegistroFormateado;
    }

    
    @Column(name="ESTADO", nullable=false)    
    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "REGISTRO_DETALLE")
    @ForeignKey(name = "RWE_REGSAL_REGDET_FK")
    public RegistroDetalle getRegistroDetalle() {
        return registroDetalle;
    }

    public void setRegistroDetalle(RegistroDetalle registroDetalle) {
        this.registroDetalle = registroDetalle;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegistroSalida that = (RegistroSalida) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public String toString() {
        if(id != null){
            return id.toString();
        }else{
            return null;
        }
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


}
