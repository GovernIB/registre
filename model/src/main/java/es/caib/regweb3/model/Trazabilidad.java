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
@Table(name = "RWE_TRAZABILIDAD")
@SequenceGenerator(name = "generator", sequenceName = "RWE_TRAZABILIDAD_SEQ", allocationSize = 1)
public class Trazabilidad implements Serializable {

    private Long id;
    private Long tipo;
    private OficioRemision oficioRemision;
    private RegistroEntrada registroEntradaOrigen;
    private RegistroEntrada registroEntradaDestino;
    private RegistroSalida registroSalida;
    private RegistroSalida registroSalidaRectificado;
    private RegistroSir registroSir;
    private Date fecha;

    public Trazabilidad() {
    }

    public Trazabilidad(Long tipo) {
        this.tipo = tipo;
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

    @ManyToOne(optional = true)
    @JoinColumn(name = "REGENT_ORIGEN", foreignKey = @ForeignKey(name = "RWE_TRAZAB_REGENTO_FK"))
    public RegistroEntrada getRegistroEntradaOrigen() {
        return registroEntradaOrigen;
    }

    public void setRegistroEntradaOrigen(RegistroEntrada registroEntradaOrigen) {
        this.registroEntradaOrigen = registroEntradaOrigen;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "REGENT_DESTINO", foreignKey = @ForeignKey(name = "RWE_TRAZAB_REGENTD_FK"))
    public RegistroEntrada getRegistroEntradaDestino() {
        return registroEntradaDestino;
    }

    public void setRegistroEntradaDestino(RegistroEntrada registroEntradaDestino) {
        this.registroEntradaDestino = registroEntradaDestino;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "REGISTRO_SALIDA", foreignKey = @ForeignKey(name = "RWE_TRAZAB_REGSAL_FK"))
    public RegistroSalida getRegistroSalida() {
        return registroSalida;
    }

    public void setRegistroSalida(RegistroSalida registroSalida) {
        this.registroSalida = registroSalida;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "REGISTRO_SALIDA_RECT", foreignKey = @ForeignKey(name = "RWE_TRAZAB_RGSRCT_FK"))
    public RegistroSalida getRegistroSalidaRectificado() {
        return registroSalidaRectificado;
    }

    public void setRegistroSalidaRectificado(RegistroSalida registroSalidaRectificado) {
        this.registroSalidaRectificado = registroSalidaRectificado;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "OFICIO_REMISION", foreignKey = @ForeignKey(name = "RWE_TRAZAB_OFIREM_FK"))
    public OficioRemision getOficioRemision() {
        return oficioRemision;
    }

    public void setOficioRemision(OficioRemision oficioRemision) {
        this.oficioRemision = oficioRemision;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name = "REGISTRO_SIR", foreignKey = @ForeignKey(name = "RWE_TRAZAB_REGSIR_FK"))

    public RegistroSir getRegistroSir() {
        return registroSir;
    }

    public void setRegistroSir(RegistroSir registroSir) {
        this.registroSir = registroSir;
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
