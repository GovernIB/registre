package es.caib.regweb.model;

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
    private OficioRemision oficioRemision;
    private RegistroEntrada registroEntradaOrigen;
    private RegistroSalida registroSalida;
    private RegistroEntrada registroEntradaDestino;
    private Date fecha;



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
    @JoinColumn(name="REGENT_ORIGEN")
    @ForeignKey(name="RWE_TRAZAB_REGENTO_FK")
    public RegistroEntrada getRegistroEntradaOrigen() {
        return registroEntradaOrigen;
    }

    public void setRegistroEntradaOrigen(RegistroEntrada registroEntradaOrigen) {
        this.registroEntradaOrigen = registroEntradaOrigen;
    }

    @ManyToOne(cascade= CascadeType.PERSIST, optional = false)
    @JoinColumn(name="REGISTRO_SALIDA")
    @ForeignKey(name="RWE_TRAZAB_REGSAL_FK")
    public RegistroSalida getRegistroSalida() {
        return registroSalida;
    }

    public void setRegistroSalida(RegistroSalida registroSalida) {
        this.registroSalida = registroSalida;
    }

    @ManyToOne(cascade= CascadeType.PERSIST, optional = true)
    @JoinColumn(name="REGENT_DESTINO")
    @ForeignKey(name="RWE_TRAZAB_REGENTD_FK")
    public RegistroEntrada getRegistroEntradaDestino() {
        return registroEntradaDestino;
    }

    public void setRegistroEntradaDestino(RegistroEntrada registroEntradaDestino) {
        this.registroEntradaDestino = registroEntradaDestino;
    }

    @ManyToOne(cascade= CascadeType.PERSIST, optional = false)
    @JoinColumn(name="OFICIO_REMISION")
    @ForeignKey(name="RWE_TRAZAB_OFIREM_FK")
    public OficioRemision getOficioRemision() {
        return oficioRemision;
    }

    public void setOficioRemision(OficioRemision oficioRemision) {
        this.oficioRemision = oficioRemision;
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
