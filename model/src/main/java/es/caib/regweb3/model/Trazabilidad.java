package es.caib.regweb3.model;

import org.hibernate.annotations.ForeignKey;

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
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class Trazabilidad implements Serializable {

    private Long id;
    private Long tipo;
    private OficioRemision oficioRemision;
    private RegistroEntrada registroEntradaOrigen;
    private RegistroSalida registroSalida;
    private RegistroEntrada registroEntradaDestino;
    private AsientoRegistralSir asientoRegistralSir;
    private Date fecha;

    public Trazabilidad() {
    }

    public Trazabilidad(Long tipo) {
        this.tipo = tipo;
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

    @Column(name="tipo", nullable = false)
    public Long getTipo() {
        return tipo;
    }

    public void setTipo(Long tipo) {
        this.tipo = tipo;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name="REGENT_ORIGEN")
    @ForeignKey(name="RWE_TRAZAB_REGENTO_FK")
    public RegistroEntrada getRegistroEntradaOrigen() {
        return registroEntradaOrigen;
    }

    public void setRegistroEntradaOrigen(RegistroEntrada registroEntradaOrigen) {
        this.registroEntradaOrigen = registroEntradaOrigen;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name="REGISTRO_SALIDA")
    @ForeignKey(name="RWE_TRAZAB_REGSAL_FK")
    public RegistroSalida getRegistroSalida() {
        return registroSalida;
    }

    public void setRegistroSalida(RegistroSalida registroSalida) {
        this.registroSalida = registroSalida;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name="REGENT_DESTINO")
    @ForeignKey(name="RWE_TRAZAB_REGENTD_FK")
    public RegistroEntrada getRegistroEntradaDestino() {
        return registroEntradaDestino;
    }

    public void setRegistroEntradaDestino(RegistroEntrada registroEntradaDestino) {
        this.registroEntradaDestino = registroEntradaDestino;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name="OFICIO_REMISION")
    @ForeignKey(name="RWE_TRAZAB_OFIREM_FK")
    public OficioRemision getOficioRemision() {
        return oficioRemision;
    }

    public void setOficioRemision(OficioRemision oficioRemision) {
        this.oficioRemision = oficioRemision;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name="ASIENTO_REGISTRAL_SIR")
    @ForeignKey(name="RWE_TRAZAB_ASR_FK")
    public AsientoRegistralSir getAsientoRegistralSir() {
        return asientoRegistralSir;
    }

    public void setAsientoRegistralSir(AsientoRegistralSir asientoRegistralSir) {
        this.asientoRegistralSir = asientoRegistralSir;
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
        if(id != null){
            return id.toString();
        }else{
            return null;
        }
    }
}
