package es.caib.regweb3.model.sir;

import es.caib.regweb3.model.Entidad;
import es.caib.regweb3.model.utils.IndicadorPrueba;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Implementación de un Mensaje de Control
 */
@Entity
@Table(name = "RWE_MENSAJE_CONTROL")
@SequenceGenerator(name="sir_sequence",sequenceName = "RWE_SIR_SEQ", allocationSize = 1)
public class MensajeControl implements Serializable {

    /**
     * Clave primaria
     */
    private Long id;

    /**
     * Entidad a la que pertenece
     */
    private Entidad entidad;

    /**
     * Código único de la entidad registral origen en el directorio común.
     * Obligatorio.
     */
    private String codigoEntidadRegistralOrigen;

    /**
     * Código único de la entidad registral destino en el directorio común.
     * Obligatorio.
     */
    private String codigoEntidadRegistralDestino;

    /**
     * Identificador de intercambio único de la operación.
     * Obligatorio.
     */
    private String identificadorIntercambio;

    /**
     * Tipo de mensaje. Obligatorio.
     */
    private String tipoMensaje;

    /**
     * Texto descriptivo del mensaje. Opcional.
     */
    private String descripcionMensaje;

    /**
     * Número de registro de entrada en la entidad Registral destino. Utilizado
     * para completar el ciclo de envío. Opcional.
     */
    private String numeroRegistroEntradaDestino;

    /**
     * Fecha y hora de entrada en destino.
     * Opcional.
     */
    private Date fechaEntradaDestino;

    /**
     * Indicador de prueba.
     */
    private IndicadorPrueba indicadorPrueba = IndicadorPrueba.NORMAL;

    /**
     * Identificador del tipo de error que se ha producido durante el proceso de
     * intercambio. Opcional.
     */
    private String codigoError;

    /**
     * Fecha creación
     */
    private Date fecha = new Date();

    /**
     * Indica los mensajes enviados o recibidos
     */
    private Long tipoComunicacion;

    public MensajeControl() { }

    public MensajeControl(Long tipoComunicacion) {
        this.tipoComunicacion = tipoComunicacion;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator = "sir_sequence")
    @Column(name="ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "ENTIDAD", foreignKey = @javax.persistence.ForeignKey(name = "RWE_MC_ENTIDAD_FK"))
    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }


    @Column(name = "COD_ENT_REG_ORI", length = 21, nullable = false)
    public String getCodigoEntidadRegistralOrigen() {
        return codigoEntidadRegistralOrigen;
    }

    public void setCodigoEntidadRegistralOrigen(String codigoEntidadRegistralOrigen) {
        this.codigoEntidadRegistralOrigen = codigoEntidadRegistralOrigen;
    }

    @Column(name = "COD_ENT_REG_DEST", length = 21, nullable = false)
    public String getCodigoEntidadRegistralDestino() {
        return codigoEntidadRegistralDestino;
    }

    public void setCodigoEntidadRegistralDestino(String codigoEntidadRegistralDestino) {
        this.codigoEntidadRegistralDestino = codigoEntidadRegistralDestino;
    }

    @Column(name = "ID_INTERCAMBIO", length = 33, nullable = false)
    public String getIdentificadorIntercambio() {
        return identificadorIntercambio;
    }

    public void setIdentificadorIntercambio(String identificadorIntercambio) {
        this.identificadorIntercambio = identificadorIntercambio;
    }

    @Column(name = "TIPO_MENSAJE", length = 2, nullable = false)
    public String getTipoMensaje() {
        return tipoMensaje;
    }

    public void setTipoMensaje(String tipoMensaje) {
        this.tipoMensaje = tipoMensaje;
    }

    @Column(name = "DESCRIPCION", length = 1024)
    public String getDescripcionMensaje() {
        return descripcionMensaje;
    }

    public void setDescripcionMensaje(String descripcionMensaje) {
        this.descripcionMensaje = descripcionMensaje;
    }

    @Column(name = "NUM_REG_DESTINO", length = 20)
    public String getNumeroRegistroEntradaDestino() {
        return numeroRegistroEntradaDestino;
    }

    public void setNumeroRegistroEntradaDestino(String numeroRegistroEntradaDestino) {
        this.numeroRegistroEntradaDestino = numeroRegistroEntradaDestino;
    }

    @Column(name = "FECHA_DESTINO", nullable = true)
    public Date getFechaEntradaDestino() {
        return fechaEntradaDestino;
    }

    public void setFechaEntradaDestino(Date fechaEntradaDestino) {
        this.fechaEntradaDestino = fechaEntradaDestino;
    }

    @Column(name = "INDICADOR_PRUEBA", length = 1, nullable = false)
    @Enumerated(EnumType.ORDINAL)
    public IndicadorPrueba getIndicadorPrueba() {
        return indicadorPrueba;
    }

    public void setIndicadorPrueba(IndicadorPrueba indicadorPrueba) {
        this.indicadorPrueba = indicadorPrueba;
    }

    @Column(name = "COD_ERROR", length = 4)
    public String getCodigoError() {
        return codigoError;
    }

    public void setCodigoError(String codigoError) {
        this.codigoError = codigoError;
    }

    @Column(name = "FECHA")
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Column(name = "TIPO_COMUNICACION", nullable = false)
    public Long getTipoComunicacion() {
        return tipoComunicacion;
    }

    public void setTipoComunicacion(Long tipoComunicacion) {
        this.tipoComunicacion = tipoComunicacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MensajeControl mensajeControl = (MensajeControl) o;

        if (!id.equals(mensajeControl.id)) return false;

        return true;
    }


    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
