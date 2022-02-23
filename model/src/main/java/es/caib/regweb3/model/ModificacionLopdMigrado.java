package es.caib.regweb3.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jpernia on 07/11/2014.
 */
@Entity
@Table(name = "RWE_MODIFICACIONLOPD_MIGRADO")
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class ModificacionLopdMigrado implements Serializable {

    private Long id;
    private RegistroMigrado registroMigrado;
    private Date fecha;
    private Date fechaModificacion;
    private String tipoAcceso;
    private String usuario;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
    //@Index(name="RWE_MODLOPD_MIGRADO_PK_I")
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REGMIG", foreignKey = @ForeignKey(name = "RWE_MODLOPDMIG_REGMIG_FK"))
    public RegistroMigrado getRegistroMigrado() {
        return registroMigrado;
    }

    public void setRegistroMigrado(RegistroMigrado registroMigrado) {
        this.registroMigrado = registroMigrado;
    }

    @Column(name = "FECHA", nullable = false)
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Column(name = "FECHAMOD", nullable = false)
    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    @Column(name = "TIPOACCESO", nullable = false, length = 10)
    public String getTipoAcceso() {
        return this.tipoAcceso;
    }

    public void setTipoAcceso(String tipoAcceso) {
        this.tipoAcceso = tipoAcceso;
    }

    @Column(name = "USUARIO", nullable = false, length = 255)
    public String getUsuario() {
        return this.usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

}