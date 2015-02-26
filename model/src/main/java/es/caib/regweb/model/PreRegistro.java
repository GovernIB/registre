package es.caib.regweb.model;

import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Fundació BIT.
 *
 * @author jpernia
 * Date: 09/12/14
 */
@Entity
@Table(name = "RWE_PRE_REGISTRO")
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class PreRegistro implements Serializable{

    private Long id;
    private String codigoEntidadRegistralOrigen; //Oficina
    private String decodificacionEntidadRegistralOrigen;
    private String codigoUnidadTramitacionOrigen;
    private String decodificacionUnidadTramitacionOrigen;
    private String codigoEntidadRegistralDestino;
    private String decodificacionEntidadRegistralDestino;
    private String codigoUnidadTramitacionDestino;  //Entidad
    private String decodificacionUnidadTramitacionDestino;
    private String usuario;
    private String contactoUsuario;
    private String idIntercambio;
    private Long tipoAnotacion;
    private String descripcionTipoAnotacion;
    private String tipoRegistro;
    private String indicadorPrueba;
    private String numeroPreregistro;
    private Long contador;
    private Date fecha;
    private String codigoEntidadRegistralInicio;
    private String decodificacionEntidadRegistralInicio;
    private Long estado;
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

    @Column(name="CODENTREGORIGEN", length = 21, nullable = false)
    public String getCodigoEntidadRegistralOrigen() {
        return codigoEntidadRegistralOrigen;
    }

    public void setCodigoEntidadRegistralOrigen(String codigoEntidadRegistralOrigen) {
        this.codigoEntidadRegistralOrigen = codigoEntidadRegistralOrigen;
    }

    @Column(name="DECENTREGORIGEN", length = 80, nullable = true)
    public String getDecodificacionEntidadRegistralOrigen() {
        return decodificacionEntidadRegistralOrigen;
    }

    public void setDecodificacionEntidadRegistralOrigen(String decodificacionEntidadRegistralOrigen) {
        this.decodificacionEntidadRegistralOrigen = decodificacionEntidadRegistralOrigen;
    }

    @Column(name="CODUNITRAORIGEN", length = 21)
    public String getCodigoUnidadTramitacionOrigen() {
        return codigoUnidadTramitacionOrigen;
    }

    public void setCodigoUnidadTramitacionOrigen(String codigoUnidadTramitacionOrigen) {
        this.codigoUnidadTramitacionOrigen = codigoUnidadTramitacionOrigen;
    }

    @Column(name="DECUNITRAORIGEN", length = 80, nullable = true)
    public String getDecodificacionUnidadTramitacionOrigen() {
        return decodificacionUnidadTramitacionOrigen;
    }

    public void setDecodificacionUnidadTramitacionOrigen(String decodificacionUnidadTramitacionOrigen) {
        this.decodificacionUnidadTramitacionOrigen = decodificacionUnidadTramitacionOrigen;
    }

    @Column(name="USUARIO", length = 80)
    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Column(name="CONTACTOUSUARIO", length = 160)
    public String getContactoUsuario() {
        return contactoUsuario;
    }

    public void setContactoUsuario(String contactoUsuario) {
        this.contactoUsuario = contactoUsuario;
    }

    @Column(name="CODENTREGDES", length = 21, nullable = false)
    public String getCodigoEntidadRegistralDestino() {
        return codigoEntidadRegistralDestino;
    }

    public void setCodigoEntidadRegistralDestino(String codigoEntidadRegistralDestino) {
        this.codigoEntidadRegistralDestino = codigoEntidadRegistralDestino;
    }

    @Column(name="DECENTREGDES", length = 80, nullable = true)
    public String getDecodificacionEntidadRegistralDestino() {
        return decodificacionEntidadRegistralDestino;
    }

    public void setDecodificacionEntidadRegistralDestino(String decodificacionEntidadRegistralDestino) {
        this.decodificacionEntidadRegistralDestino = decodificacionEntidadRegistralDestino;
    }

    @Column(name="CODUNITRADES", length = 21)
    public String getCodigoUnidadTramitacionDestino() {
        return codigoUnidadTramitacionDestino;
    }

    public void setCodigoUnidadTramitacionDestino(String codigoUnidadTramitacionDestino) {
        this.codigoUnidadTramitacionDestino = codigoUnidadTramitacionDestino;
    }

    @Column(name="DECUNITRADES", length = 80, nullable = true)
    public String getDecodificacionUnidadTramitacionDestino() {
        return decodificacionUnidadTramitacionDestino;
    }

    public void setDecodificacionUnidadTramitacionDestino(String decodificacionUnidadTramitacionDestino) {
        this.decodificacionUnidadTramitacionDestino = decodificacionUnidadTramitacionDestino;
    }

    @Column(name="IDINTERCAMBIO", length = 33, nullable = false)
    public String getIdIntercambio() {
        return idIntercambio;
    }

    public void setIdIntercambio(String idIntercambio) {
        this.idIntercambio = idIntercambio;
    }


    @Column(name = "TIPOANOTACION")
    public Long getTipoAnotacion() {
        return tipoAnotacion;
    }

    public void setTipoAnotacion(Long tipoAnotacion) {
        this.tipoAnotacion = tipoAnotacion;
    }

    @Column(name="DESCTIPOANOTACION", length = 80)
    public String getDescripcionTipoAnotacion() { return descripcionTipoAnotacion; }

    public void setDescripcionTipoAnotacion(String descripcionTipoAnotacion) { this.descripcionTipoAnotacion = descripcionTipoAnotacion; }

    @Column(name="TIPOREGISTRO", length = 1, nullable = false)
    public String getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    @Column(name="INDPRUEBA", length = 1, nullable = false)
    public String getIndicadorPrueba() {
        return indicadorPrueba;
    }

    public void setIndicadorPrueba(String indicadorPrueba) {
        this.indicadorPrueba = indicadorPrueba;
    }

    @Column(name = "NUMPREREGISTRO", nullable = false)
    public String getNumeroPreregistro() {
        return numeroPreregistro;
    }

    public void setNumeroPreregistro(String numeroPreregistro) {
        this.numeroPreregistro = numeroPreregistro;
    }

    @Column(name = "CONTADOR", nullable = false)
    public Long getContador() {
        return contador;
    }

    public void setContador(Long contador) {
        this.contador = contador;
    }

    @Column(name = "FECHA", nullable = false)
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Column(name="CODENTREGINI", length = 21, nullable = true)
    public String getCodigoEntidadRegistralInicio() {
        return codigoEntidadRegistralInicio;
    }

    public void setCodigoEntidadRegistralInicio(String codigoEntidadRegistralInicio) {
        this.codigoEntidadRegistralInicio = codigoEntidadRegistralInicio;
    }

    @Column(name="DECENTREGINI", length = 80, nullable = true)
    public String getDecodificacionEntidadRegistralInicio() {
        return decodificacionEntidadRegistralInicio;
    }

    public void setDecodificacionEntidadRegistralInicio(String decodificacionEntidadRegistralInicio) {
        this.decodificacionEntidadRegistralInicio = decodificacionEntidadRegistralInicio;
    }

    @Column(name="ESTADO")
    public Long getEstado() { return estado; }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "REGISTRO_DETALLE")
    @ForeignKey(name = "RWE_PREREG_REGDET_FK")
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

        PreRegistro that = (PreRegistro) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return "PreRegistro{" +
                "id=" + id +
                ", codigoEntidadRegistralOrigen='" + codigoEntidadRegistralOrigen + '\'' +
                ", decodificacionEntidadRegistralOrigen='" + decodificacionEntidadRegistralOrigen + '\'' +
                ", codigoUnidadTramitacionOrigen='" + codigoUnidadTramitacionOrigen + '\'' +
                ", decodificacionUnidadTramitacionOrigen='" + decodificacionUnidadTramitacionOrigen + '\'' +
                ", codigoEntidadRegistralDestino='" + codigoEntidadRegistralDestino + '\'' +
                ", decodificacionEntidadRegistralDestino='" + decodificacionEntidadRegistralDestino + '\'' +
                ", codigoUnidadTramitacionDestino='" + codigoUnidadTramitacionDestino + '\'' +
                ", decodificacionUnidadTramitacionDestino='" + decodificacionUnidadTramitacionDestino + '\'' +
                ", usuario='" + usuario + '\'' +
                ", contactoUsuario='" + contactoUsuario + '\'' +
                ", idIntercambio='" + idIntercambio + '\'' +
                ", tipoAnotacion=" + tipoAnotacion +
                ", descripcionTipoAnotacion=" + descripcionTipoAnotacion +
                ", tipoRegistro='" + tipoRegistro + '\'' +
                ", indicadorPrueba='" + indicadorPrueba + '\'' +
                ", numeroPreregistro='" + numeroPreregistro + '\'' +
                ", fecha=" + fecha +
                ", codigoEntidadRegistralInicio='" + codigoEntidadRegistralInicio + '\'' +
                ", decodificacionEntidadRegistralInicio='" + decodificacionEntidadRegistralInicio + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}