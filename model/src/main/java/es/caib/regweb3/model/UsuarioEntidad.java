package es.caib.regweb3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 19/03/14
 */
@Entity
@Table(name = "RWE_USUARIO_ENTIDAD")
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@XmlRootElement(name = "usuarioEntidad")
@XmlAccessorType(XmlAccessType.FIELD)
public class UsuarioEntidad implements Serializable{

    @XmlAttribute
    private Long id;
    @XmlElement
    private Usuario usuario;
    @XmlTransient
    private Entidad entidad;
    @XmlTransient
    private Boolean activo = true;
    @XmlTransient
    private Oficina ultimaOficina;
    @XmlTransient
    private Long categoria;
    @XmlTransient
    private Long funcion;
    @XmlTransient
    private String codigoTrabajo;
    @XmlTransient
    private String nombreTrabajo;
    @XmlTransient
    private String observaciones;
    @XmlTransient
    private String telefono;
    @XmlTransient
    private Integer cai;
    @XmlTransient
    private Boolean clave = false;
    @XmlTransient
    private Boolean bitcita = false;
    @XmlTransient
    private Boolean asistencia = false;
    @XmlTransient
    private Boolean apodera = false;
    @XmlTransient
    private Boolean notificacionEspontanea = false;
    @XmlTransient
    private Date fechaAlta;

    public UsuarioEntidad() {
    }

    public UsuarioEntidad(Long id) {
        this.id = id;
    }

    public UsuarioEntidad(Long id, Usuario usuario, Long idEntidad) {
        this.id = id;
        this.usuario = usuario;
        this.entidad = new Entidad(idEntidad);
    }

    public UsuarioEntidad(Long id, Long idUsuario, String identificadorUsuario) {
        this.id = id;
        this.usuario = new Usuario(idUsuario, identificadorUsuario);
    }

    public UsuarioEntidad(Long idUsuario, String identificador, String nombre, String apellido1, String apellido2, String documento, Long tipo, String mail) {
        this.id = idUsuario;
        this.usuario = new Usuario(identificador, nombre, apellido1, apellido2, documento, tipo, mail);
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

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "USUARIO", foreignKey = @ForeignKey(name = "RWE_USUENT_USUARIO_FK"))
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENTIDAD", foreignKey = @ForeignKey(name = "RWE_USUENT_ENTIDAD_FK"))
    @JsonIgnore
    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    @Column(name = "ACTIVO", nullable = false)
    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ULTIMAOFICINA", foreignKey = @ForeignKey(name = "RWE_USUENT_OFICINA_FK"))
    @JsonIgnore
    public Oficina getUltimaOficina() {
        return ultimaOficina;
    }

    public void setUltimaOficina(Oficina ultimaOficina) {
        this.ultimaOficina = ultimaOficina;
    }

    @Column(name="CATEGORIA")
    public Long getCategoria() {
        return categoria;
    }

    public void setCategoria(Long categoria) {
        this.categoria = categoria;
    }

    @Column(name="FUNCION")
    public Long getFuncion() {
        return funcion;
    }

    public void setFuncion(Long funcion) {
        this.funcion = funcion;
    }

    @Column(name="CODIGOTRABAJO")
    public String getCodigoTrabajo() {
        return codigoTrabajo;
    }

    public void setCodigoTrabajo(String codigoTrabajo) {
        this.codigoTrabajo = codigoTrabajo;
    }
    @Column(name="NOMBRETRABAJO")

    public String getNombreTrabajo() {
        return nombreTrabajo;
    }

    public void setNombreTrabajo(String nombreTrabajo) {
        this.nombreTrabajo = nombreTrabajo;
    }

    @Column(name="OBSERVACIONES")
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Column(name="TELEFONO", length = 25)
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Column(name="CAI")
    public Integer getCai() {
        return cai;
    }

    public void setCai(Integer cai) {
        this.cai = cai;
    }

    @Column(name="CLAVE")
    public Boolean getClave() {
        return clave;
    }

    public void setClave(Boolean clave) {
        this.clave = clave;
    }

    @Column(name="BITCITA")
    public Boolean getBitcita() {
        return bitcita;
    }

    public void setBitcita(Boolean bitcita) {
        this.bitcita = bitcita;
    }

    @Column(name="ASISTENCIA")
    public Boolean getAsistencia() {
        return asistencia;
    }

    public void setAsistencia(Boolean asistencia) {
        this.asistencia = asistencia;
    }

    @Column(name="APODERA")
    public Boolean getApodera() {
        return apodera;
    }

    public void setApodera(Boolean apodera) {
        this.apodera = apodera;
    }

    @Column(name="NOTIFICACION")
    public Boolean getNotificacionEspontanea() {
        return notificacionEspontanea;
    }

    public void setNotificacionEspontanea(Boolean notificacionEspontanea) {
        this.notificacionEspontanea = notificacionEspontanea;
    }

    @Column(name="FECHAALTA")
    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    @Transient
    public String getNombreCompleto(){
        return getUsuario().getNombreCompleto();
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UsuarioEntidad usuarioEntidad = (UsuarioEntidad) o;

        if (id != null ? !id.equals(usuarioEntidad.id) : usuarioEntidad.id != null) return false;

        return true;
    }
}
