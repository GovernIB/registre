package es.caib.regweb3.sir.core.model;

import es.caib.regweb3.model.OficioRemision;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * Created by Fundació BIT.
 * @author earrivi
 * Date: 01/02/17
 */
@Entity
@Table(name = "RWE_TRAZABILIDAD_SIR")
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class TrazabilidadSir implements Serializable {

    private Long id;
    private Long tipoIntercambio;
    private OficioRemision oficioRemision;
    private AsientoRegistralSir asientoRegistralSir;
    private Date fechaRecepcion;
    private Date fechaEnvio;
    private String codigoError;
    private String descripcionError;
    private Integer numeroReintentos;


    public TrazabilidadSir() {
      super();
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

    @Column(name = "TIPO_INTERCAMBIO", nullable = false)
    public Long getTipoIntercambio() {
        return tipoIntercambio;
    }

    public void setTipoIntercambio(Long tipoIntercambio) {
        this.tipoIntercambio = tipoIntercambio;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name="ASIENTO")
    @ForeignKey(name="RWE_TRASIR_ARS_FK")
    public AsientoRegistralSir getAsientoRegistralSir() {
        return asientoRegistralSir;
    }

    public void setAsientoRegistralSir(AsientoRegistralSir asientoRegistralSir) {
        this.asientoRegistralSir = asientoRegistralSir;
    }

    @ManyToOne(optional = true)
    @JoinColumn(name="OFICIO_REMISION")
    @ForeignKey(name="RWE_TRASIR_OFIREM_FK")
    public OficioRemision getOficioRemision() {
        return oficioRemision;
    }

    public void setOficioRemision(OficioRemision oficioRemision) {
        this.oficioRemision = oficioRemision;
    }

    @Column(name = "FECHA_RECEPCION", nullable = true)
    public Date getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(Date fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    @Column(name = "FECHA_ENVIO", nullable = true)
    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    @Column(name = "COD_ERROR", nullable = true)
    public String getCodigoError() {
        return codigoError;
    }

    public void setCodigoError(String codigoError) {
        this.codigoError = codigoError;
    }

    @Column(name = "DESC_ERROR", nullable = true)
    public String getDescripcionError() {
        return descripcionError;
    }

    public void setDescripcionError(String descripcionError) {
        this.descripcionError = descripcionError;
    }

    @Column(name = "REINTENTOS", nullable = true)
    public Integer getNumeroReintentos() {
        return numeroReintentos;
    }

    public void setNumeroReintentos(Integer numeroReintentos) {
        this.numeroReintentos = numeroReintentos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TrazabilidadSir trazabilidadSir = (TrazabilidadSir) o;

        if (id != null ? !id.equals(trazabilidadSir.id) : trazabilidadSir.id != null) return false;

        return true;
    }

}
