package es.caib.regweb3.sir.core.model;

import es.caib.regweb3.model.Entidad;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementación de un Asiento Registral de intercambio
 */
@Entity
@Table(name = "RWE_ASIENTO_REGISTRAL_SIR")
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class AsientoRegistralSir implements Serializable {

    /**
     * Id del AsientoRegistralSir
     */
    private Long id;

    /**
     * Entidad a la que pertenece el AsientoRegistralSir
     */
    private Entidad entidad;

    /**
     * Código único de la entidad registral propietaria del asiento registral
     * obtenido del directorio común.
     */
    private String codigoEntidadRegistral;

    /**
     * Código único de la entidad registral origen obtenido del directorio
     * común.
     */
    private String codigoEntidadRegistralOrigen;

    /**
     * Descripción de la entidad registral origen.
     */
    private String decodificacionEntidadRegistralOrigen;

    /**
     * Número de registro en la entidad registral origen.
     */
    private String numeroRegistro;

    /**
     * Fecha y hora de registro en la entidad registral origen.
     */
    private Date fechaRegistro;

    /**
     * Sello de tiempo del registro de entrada en origen.
     */
    private byte[] timestampRegistro;

    /**
     * Número del registro inicial.
     */
    private String numeroRegistroInicial; //Todo averiguar si es útil este campo

    /**
     * Fecha y hora del registro inicial.
     */
    private Date fechaRegistroInicial; //Todo averiguar si es útil este campo

    /**
     * Sello de tiempo del registro inicial.
     */
    private byte[] timestampRegistroInicial; //Todo averiguar si es útil este campo

    /**
     * Código único de la unidad de tramitación de origen obtenido del
     * directorio común.
     */
    private String codigoUnidadTramitacionOrigen;

    /**
     * Descripción de la unidad de tramitación de origen.
     */
    private String decodificacionUnidadTramitacionOrigen;

    /**
     * Código único de la entidad registral de destino obtenido del directorio
     * común.
     */
    private String codigoEntidadRegistralDestino;

    /**
     * Descripción de la entidad registral de destino.
     */
    private String decodificacionEntidadRegistralDestino;

    /**
     * Código único de la unidad de tramitación de destino obtenido del
     * directorio común.
     */
    private String codigoUnidadTramitacionDestino;

    /**
     * Descripción de la unidad de tramitación de destino.
     */
    private String decodificacionUnidadTramitacionDestino;

    /**
     * Abstract o resumen.
     */
    private String resumen;

    /**
     * Código de asunto según destino.
     */
    private String codigoAsunto;

    /**
     * Referencia externa.
     */
    private String referenciaExterna;

    /**
     * Número de expediente objeto de la tramitación administrativa.
     */
    private String numeroExpediente;

    /**
     * Tipo de transporte de entrada.
     */
    private String tipoTransporte;

    /**
     * Número de transporte de entrada.
     */
    private String numeroTransporte;

    /**
     * Nombre del usuario de origen.
     */
    private String nombreUsuario;

    /**
     * Contacto del usuario de origen (teléfono o dirección de correo
     * electrónico).
     */
    private String contactoUsuario;

    /**
     * Identificador de intercambio único de la operación.
     */
    private String identificadorIntercambio;

    /**
     * Estado del asiento registral.
     */
    private EstadoAsientoRegistralSir estado;

    /**
     * Aplicación y versión emisora.
     */
    private String aplicacion;

    /**
     * Tipo de anotación.
     */
    private String tipoAnotacion;

    /**
     * Descripción del tipo de anotación.
     */
    private String decodificacionTipoAnotacion;

    /**
     * Tipo de registro.
     */
    private TipoRegistro tipoRegistro;

    /**
     * Documentación física que acompaña al fichero.
     */
    private String documentacionFisica;

    /**
     * Observaciones del registro de datos de intercambio recogidos por el
     * funcionario de registro.
     */
    private String observacionesApunte;

    /**
     * Indicador de prueba
     */
    private IndicadorPrueba indicadorPrueba = IndicadorPrueba.NORMAL;

    /**
     * Código único de la entidad registral de inicio obtenido del directorio
     * común.
     */
    private String codigoEntidadRegistralInicio;

    /**
     * Descripción de la entidad registral de inicio.
     */
    private String decodificacionEntidadRegistralInicio;

    /**
     * Exposición de los hechos y antecedentes relacionados con la solicitud.
     */
    private String expone;

    /**
     * Descripción del objeto de la solicitud.
     */
    private String solicita;


    /**
     * Lista de anexos del asiento registral.
     */
    private List<AnexoSir> anexos = null;

    /**
     * Lista de interesados del asiento registral.
     */
    private List<InteresadoSir> interesados = null;

    /**
     * Fecha de estado del asiento registral.
     */
    private Date fechaEstado;

    /**
     * Fecha de envío del asiento registral.
     */
    private Date fechaEnvio;

    /**
     * Fecha de recepción del asiento registral.
     */
    private Date fechaRecepcion;

    /**
     * Número de reintentos de envío.
     */
    private int numeroReintentos;

    /**
     * Código del error producido.
     */
    private String codigoError;

    /**
     * Descripción del error producido.
     */
    private String decodificacionError;


    public AsientoRegistralSir() {
    }

    public AsientoRegistralSir(Long id) {
        this.id=id;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator = "generator")
    @Column(name="ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(optional = false)
    @JoinColumn(name = "ENTIDAD")
    @ForeignKey(name = "RWE_ARS_ENTIDAD_FK")
    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    @Column(name = "COD_ENT_REG", length = 21)
    public String getCodigoEntidadRegistral() {
        return codigoEntidadRegistral;
    }

    public void setCodigoEntidadRegistral(String codigoEntidadRegistral) {
        this.codigoEntidadRegistral = codigoEntidadRegistral;
    }

    @Column(name = "COD_ENT_REG_ORI", length = 21, nullable = false)
    public String getCodigoEntidadRegistralOrigen() {
        return codigoEntidadRegistralOrigen;
    }

    public void setCodigoEntidadRegistralOrigen(String codigoEntidadRegistralOrigen) {
        this.codigoEntidadRegistralOrigen = codigoEntidadRegistralOrigen;
    }

    @Column(name = "DEC_ENT_REG_ORI", length = 80, nullable = true)
    public String getDecodificacionEntidadRegistralOrigen() {
        return decodificacionEntidadRegistralOrigen;
    }

    public void setDecodificacionEntidadRegistralOrigen(String decodificacionEntidadRegistralOrigen) {
        this.decodificacionEntidadRegistralOrigen = decodificacionEntidadRegistralOrigen;
    }

    @Column(name = "NUMERO_REGISTRO", length = 20, nullable = false)
    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    @Column(name = "FECHAR_EGISTRO", length = 14, nullable = false)
    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Column(name = "TIMESTAMP_REGISTRO", nullable = true)
    public byte[] getTimestampRegistro() {
        return timestampRegistro;
    }

    public void setTimestampRegistro(byte[] timestampRegistro) {
        this.timestampRegistro = timestampRegistro;
    }

    public String getNumeroRegistroInicial() {
        return numeroRegistroInicial;
    }

    public void setNumeroRegistroInicial(String numeroRegistroInicial) {
        this.numeroRegistroInicial = numeroRegistroInicial;
    }

    public Date getFechaRegistroInicial() {
        return fechaRegistroInicial;
    }

    public void setFechaRegistroInicial(Date fechaRegistroInicial) {
        this.fechaRegistroInicial = fechaRegistroInicial;
    }

    public byte[] getTimestampRegistroInicial() {
        return timestampRegistroInicial;
    }

    public void setTimestampRegistroInicial(byte[] timestampRegistroInicial) {
        this.timestampRegistroInicial = timestampRegistroInicial;
    }

    @Column(name = "COD_UNI_TRA_ORI", length = 21, nullable = true)
    public String getCodigoUnidadTramitacionOrigen() {
        return codigoUnidadTramitacionOrigen;
    }

    public void setCodigoUnidadTramitacionOrigen(String codigoUnidadTramitacionOrigen) {
        this.codigoUnidadTramitacionOrigen = codigoUnidadTramitacionOrigen;
    }

    @Column(name = "DEC_UNI_TRA_ORI", length = 80, nullable = true)
    public String getDecodificacionUnidadTramitacionOrigen() {
        return decodificacionUnidadTramitacionOrigen;
    }

    public void setDecodificacionUnidadTramitacionOrigen(String decodificacionUnidadTramitacionOrigen) {
        this.decodificacionUnidadTramitacionOrigen = decodificacionUnidadTramitacionOrigen;
    }

    @Column(name = "COD_ENT_REG_DEST", length = 21, nullable = false)
    public String getCodigoEntidadRegistralDestino() {
        return codigoEntidadRegistralDestino;
    }

    public void setCodigoEntidadRegistralDestino(String codigoEntidadRegistralDestino) {
        this.codigoEntidadRegistralDestino = codigoEntidadRegistralDestino;
    }

    @Column(name = "DEC_ENT_REG_DEST", length = 80, nullable = true)
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

    @Column(name = "DEC_UNI_TRA_DEST", length = 80, nullable = true)
    public String getDecodificacionUnidadTramitacionDestino() {
        return decodificacionUnidadTramitacionDestino;
    }

    public void setDecodificacionUnidadTramitacionDestino(String decodificacionUnidadTramitacionDestino) {
        this.decodificacionUnidadTramitacionDestino = decodificacionUnidadTramitacionDestino;
    }

    @Column(name = "RESUMEN", length = 240, nullable = false)
    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    @Column(name = "COD_ASUNTO", length = 16, nullable = true)
    public String getCodigoAsunto() {
        return codigoAsunto;
    }

    public void setCodigoAsunto(String codigoAsunto) {
        this.codigoAsunto = codigoAsunto;
    }

    @Column(name = "REF_EXTERNA", length = 16, nullable = true)
    public String getReferenciaExterna() {
        return referenciaExterna;
    }

    public void setReferenciaExterna(String referenciaExterna) {
        this.referenciaExterna = referenciaExterna;
    }

    @Column(name = "NUM_EXPEDIENTE", length = 80, nullable = true)
    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    @Column(name = "TIPO_TRANSPORTE", length = 2, nullable = true)
    public String getTipoTransporte() {
        return tipoTransporte;
    }

    public void setTipoTransporte(String tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    @Column(name = "NUM_TRANSPORTE", length = 20, nullable = true)
    public String getNumeroTransporte() {
        return numeroTransporte;
    }

    public void setNumeroTransporte(String numeroTransporte) {
        this.numeroTransporte = numeroTransporte;
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

    @Column(name = "ID_INTERCAMBIO", length = 33, nullable = false)
    public String getIdentificadorIntercambio() {
        return identificadorIntercambio;
    }

    public void setIdentificadorIntercambio(String identificadorIntercambio) {
        this.identificadorIntercambio = identificadorIntercambio;
    }

    @Column(name = "APLICACION", length = 4, nullable = true)
    public String getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    @Column(name = "TIPO_ANOTACION", length = 2, nullable = false)
    public String getTipoAnotacion() {
        return tipoAnotacion;
    }

    public void setTipoAnotacion(String tipoAnotacion) {
        this.tipoAnotacion = tipoAnotacion;
    }

    @Column(name = "DEC_T_ANOTACION", length = 80, nullable = true)
    public String getDecodificacionTipoAnotacion() {
        return decodificacionTipoAnotacion;
    }

    public void setDecodificacionTipoAnotacion(String decodificacionTipoAnotacion) {
        this.decodificacionTipoAnotacion = decodificacionTipoAnotacion;
    }

    @Column(name = "TIPO_REGISTRO", length = 1, nullable = false)
    public TipoRegistro getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(TipoRegistro tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    @Column(name = "DOC_FISICA", length = 1, nullable = false)
    public String getDocumentacionFisica() {
        return DocumentacionFisica.getDocumentacionFisicaValue(documentacionFisica);
    }

    public void setDocumentacionFisica(String documentacionFisica) {
        this.documentacionFisica = documentacionFisica;
    }

    @Column(name = "OBSERVACIONES", length = 50, nullable = true)
    public String getObservacionesApunte() {
        return observacionesApunte;
    }

    public void setObservacionesApunte(String observacionesApunte) {
        this.observacionesApunte = observacionesApunte;
    }

    @Column(name = "INDICADOR_PRUEBA", length = 1, nullable = false)
    @Enumerated(EnumType.ORDINAL)
    public IndicadorPrueba getIndicadorPrueba() {
        return indicadorPrueba;
    }

    public void setIndicadorPrueba(IndicadorPrueba indicadorPrueba) {
        this.indicadorPrueba = indicadorPrueba;
    }

    @Column(name = "COD_ENT_REG_INI", length = 21, nullable = false)
    public String getCodigoEntidadRegistralInicio() {
        return codigoEntidadRegistralInicio;
    }

    public void setCodigoEntidadRegistralInicio(String codigoEntidadRegistralInicio) {
        this.codigoEntidadRegistralInicio = codigoEntidadRegistralInicio;
    }

    @Column(name = "DEC_ENT_REG_INI", length = 80, nullable = true)
    public String getDecodificacionEntidadRegistralInicio() {
        return decodificacionEntidadRegistralInicio;
    }

    public void setDecodificacionEntidadRegistralInicio(String decodificacionEntidadRegistralInicio) {
        this.decodificacionEntidadRegistralInicio = decodificacionEntidadRegistralInicio;
    }

    @Column(name = "EXPONE", length = 4000, nullable = true)
    public String getExpone() {
        return expone;
    }

    public void setExpone(String expone) {
        this.expone = expone;
    }

    @Column(name = "SOLICITA", length = 4000, nullable = true)
    public String getSolicita() {
        return solicita;
    }

    public void setSolicita(String solicita) {
        this.solicita = solicita;
    }


    @OneToMany(cascade= CascadeType.ALL,targetEntity=AnexoSir.class, mappedBy="idAsientoRegistralSir")
    @LazyCollection(value= LazyCollectionOption.FALSE)
    public List<AnexoSir> getAnexos() {
        if (anexos == null) {
            anexos = new ArrayList<AnexoSir>();
        }

        return anexos;
    }

    public void setAnexos(List<AnexoSir> anexos) {
        this.anexos = anexos;
    }

    @OneToMany(cascade= CascadeType.ALL,targetEntity=InteresadoSir.class, mappedBy="idAsientoRegistralSir")
    @LazyCollection(value= LazyCollectionOption.FALSE)
    public List<InteresadoSir> getInteresados() {
        if (interesados == null) {
            interesados = new ArrayList<InteresadoSir>();
        }

        return interesados;
    }

    public void setInteresados(List<InteresadoSir> interesados) {
        this.interesados = interesados;
    }

    @Column(name = "ESTADO", length = 2, nullable = false)
    @Enumerated(EnumType.ORDINAL)
    public EstadoAsientoRegistralSir getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsientoRegistralSir estado) {
        this.estado = estado;
    }

    /*public Date getFechaEstado() {
        return fechaEstado;
    }

    public void setFechaEstado(Date fechaEstado) {
        this.fechaEstado = fechaEstado;
    }

    public Date getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Date fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public Date getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(Date fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public int getNumeroReintentos() {
        return numeroReintentos;
    }

    public void setNumeroReintentos(int numeroReintentos) {
        this.numeroReintentos = numeroReintentos;
    }

    public String getCodigoError() {
        return codigoError;
    }

    public void setCodigoError(String codigoError) {
        this.codigoError = codigoError;
    }

    public String getDecodificacionError() {
        return decodificacionError;
    }

    public void setDecodificacionError(String decodificacionError) {
        this.decodificacionError = decodificacionError;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AsientoRegistralSir asientoRegistral = (AsientoRegistralSir) o;

        if (!id.equals(asientoRegistral.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
