package es.caib.regweb3.model;

import es.caib.regweb3.utils.RegwebConstantes;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 12/04/18
 */
@Entity
@Table(name = "RWE_NOTIFICACION", indexes = {
        @Index(name = "RWE_NOTIF_REMIT_FK_I", columnList = "REMITENTE"),
        @Index(name = "RWE_NOTIF_DEST_FK_I", columnList = "DESTINATARIO")
})
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class Notificacion implements Serializable {

    private Long id;
    private UsuarioEntidad remitente;
    private UsuarioEntidad destinatario;
    private Long tipo;
    private Long estado;
    private Date fechaEnviado;
    private Date fechaLeido;
    private String asunto;
    private String mensaje;


    public Notificacion() {
    }

    public Notificacion(Long tipo) {
        this.tipo = tipo;
        this.estado = 0L; // Por defecto "Nuevo"
        this.fechaEnviado = new Date();
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REMITENTE", foreignKey = @ForeignKey(name = "RWE_NOTIF_REMIT_FK"))
    public UsuarioEntidad getRemitente() {
        return remitente;
    }

    public void setRemitente(UsuarioEntidad remitente) {
        this.remitente = remitente;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "DESTINATARIO", foreignKey = @ForeignKey(name = "RWE_NOTIF_DEST_FK"))
    public UsuarioEntidad getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(UsuarioEntidad destinatario) {
        this.destinatario = destinatario;
    }

    @Column(name = "TIPO", nullable = false)
    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    @Column(name = "ESTADO", nullable = false)
    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    @Column(name = "FECHA_ENVIADO")
    public Date getFechaEnviado() {
        return fechaEnviado;
    }

    public void setFechaEnviado(Date fechaEnviado) {
        this.fechaEnviado = fechaEnviado;
    }

    @Column(name = "FECHA_LEIDO")
    public Date getFechaLeido() {
        return fechaLeido;
    }

    public void setFechaLeido(Date fechaLeido) {
        this.fechaLeido = fechaLeido;
    }

    @Column(name = "ASUNTO", length = 200)
    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    @Column(name = "MENSAJE", length = 4000)
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Transient
    private Integer pageNumber = 1;

    @Transient
    public Integer getPageNumber() {
        return pageNumber;
    }

    @Transient
    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Transient
    public String getFechaEnviadoFormateada() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(fechaEnviado);
    }

    @Transient
    public String getNombreRemitente() {
        if (remitente != null) {
            return remitente.getNombreCompleto();
        } else {
            return RegwebConstantes.APLICACION_NOMBRE;
        }

    }

    @Transient
    public String getNombreDestinatario() {
        return destinatario.getNombreCompleto();
    }

    @Transient
    public String getAsuntoCorto() {

        String asuntoCorto = getAsunto();

        if (asuntoCorto.length() > 40) {
            asuntoCorto = asuntoCorto.substring(0, 40) + "...";
        }

        return asuntoCorto;
    }
}
