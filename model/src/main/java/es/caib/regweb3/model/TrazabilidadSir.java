package es.caib.regweb3.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created 3/09/14 9:47
 *
 * @author mgonzalez
 */
@Entity
@Table(name = "RWE_TRAZABILIDAD_SIR")
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class TrazabilidadSir implements Serializable {

    private Long id;
    private Long tipo;
    private RegistroSir registroSir;
    private RegistroEntrada registroEntrada;
    private String codigoEntidadRegistralOrigen;
    private String decodificacionEntidadRegistralOrigen;
    private String codigoEntidadRegistralDestino;
    private String decodificacionEntidadRegistralDestino;
    private String codigoUnidadTramitacionDestino;
    private String decodificacionUnidadTramitacionDestino;
    private String aplicacion;
    private String nombreUsuario;
    private String contactoUsuario;
    private String observaciones;
    private Date fecha;

    public TrazabilidadSir() {
    }

    public TrazabilidadSir(Long tipo) {
        this.tipo = tipo;
        this.fecha = new Date();
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

    @Column(name = "tipo", nullable = false)
    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "REGISTRO_SIR", foreignKey = @ForeignKey(name = "RWE_TRASIR_REGSIR_FK"))
    public RegistroSir getRegistroSir() {
        return registroSir;
    }

    public void setRegistroSir(RegistroSir registroSir) {
        this.registroSir = registroSir;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "REGISTRO_ENTRADA", foreignKey = @ForeignKey(name = "RWE_TRASIR_REGENT_FK"))
    public RegistroEntrada getRegistroEntrada() {
        return registroEntrada;
    }

    public void setRegistroEntrada(RegistroEntrada registroEntrada) {
        this.registroEntrada = registroEntrada;
    }

    @Column(name = "COD_ENT_REG_ORI", length = 21, nullable = false)
    public String getCodigoEntidadRegistralOrigen() {
        return codigoEntidadRegistralOrigen;
    }

    public void setCodigoEntidadRegistralOrigen(String codigoEntidadRegistralOrigen) {
        this.codigoEntidadRegistralOrigen = codigoEntidadRegistralOrigen;
    }

    @Column(name = "DEC_ENT_REG_ORI", length = 120, nullable = true)
    public String getDecodificacionEntidadRegistralOrigen() {
        return decodificacionEntidadRegistralOrigen;
    }

    public void setDecodificacionEntidadRegistralOrigen(String decodificacionEntidadRegistralOrigen) {
        this.decodificacionEntidadRegistralOrigen = decodificacionEntidadRegistralOrigen;
    }

    @Column(name = "COD_ENT_REG_DEST", length = 21, nullable = false)
    public String getCodigoEntidadRegistralDestino() {
        return codigoEntidadRegistralDestino;
    }

    public void setCodigoEntidadRegistralDestino(String codigoEntidadRegistralDestino) {
        this.codigoEntidadRegistralDestino = codigoEntidadRegistralDestino;
    }

    @Column(name = "DEC_ENT_REG_DEST", length = 120, nullable = true)
    public String getDecodificacionEntidadRegistralDestino() {
        return decodificacionEntidadRegistralDestino;
    }

    public void setDecodificacionEntidadRegistralDestino(String decodificacionEntidadRegistralDestino) {
        this.decodificacionEntidadRegistralDestino = decodificacionEntidadRegistralDestino;
    }

    @Column(name = "COD_UNI_TRA_DEST", length = 21, nullable = true)
    public String getCodigoUnidadTramitacionDestino() {
        return codigoUnidadTramitacionDestino;
    }

    public void setCodigoUnidadTramitacionDestino(String codigoUnidadTramitacionDestino) {
        this.codigoUnidadTramitacionDestino = codigoUnidadTramitacionDestino;
    }

    @Column(name = "DEC_UNI_TRA_DEST", length = 120, nullable = true)
    public String getDecodificacionUnidadTramitacionDestino() {
        return decodificacionUnidadTramitacionDestino;
    }

    public void setDecodificacionUnidadTramitacionDestino(String decodificacionUnidadTramitacionDestino) {
        this.decodificacionUnidadTramitacionDestino = decodificacionUnidadTramitacionDestino;
    }

    //SICRES4
    @Column(name = "APLICACION", length = 20, nullable = true)
    public String getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    @Column(name = "NOMBRE_USUARIO", length = 80, nullable = true)
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    @Column(name = "CONTACTO_USUARIO", length = 160, nullable = true)
    public String getContactoUsuario() {
        return contactoUsuario;
    }

    public void setContactoUsuario(String contactoUsuario) {
        this.contactoUsuario = contactoUsuario;
    }


    @Column(name = "OBSERVACIONES", length = 2000, nullable = true)
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Column(name = "FECHA", nullable = false)
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }


    @Override
    public String toString() {
        if (id != null) {
            return id.toString();
        } else {
            return null;
        }
    }
}
