package es.caib.regweb3.model;

import es.caib.regweb3.model.utils.DocumentacionFisica;
import es.caib.regweb3.model.utils.EstadoRegistroSir;
import es.caib.regweb3.model.utils.IndicadorPrueba;
import es.caib.regweb3.model.utils.TipoRegistro;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementación de un RegistroSir de intercambio
 */
@Entity
@Table(name = "RWE_REGISTRO_SIR")
@SequenceGenerator(name="generator",sequenceName = "RWE_REGISTROSIR_SEQ", allocationSize = 1)
public class RegistroSir implements Serializable {

    /**
     * Id del RegistroSir
     */
    private Long id;

    /**
     * Entidad a la que pertenece el RegistroSir
     */
    private Entidad entidad;

    /**
     * Código único de la entidad registral a la que pertenece el registro
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
    private String timestampRegistro;

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
     * Estado del RegistroSir
     */
    private EstadoRegistroSir estado;

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
     * Lista de anexos del RegistroSir.
     */
    private List<AnexoSir> anexos = null;

    /**
     * Lista de interesados del RegistroSir.
     */
    private List<InteresadoSir> interesados = null;

    private Date fechaRecepcion;
    private Date fechaEstado;
    private Integer numeroReintentos = 0;
    private String codigoError;
    private String descripcionError;

    public RegistroSir() {
    }

    public RegistroSir(Long id) {
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
    @JoinColumn(name = "ENTIDAD", foreignKey = @ForeignKey(name = "RWE_RES_ENTIDAD_FK"))
    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    @Column(name = "COD_ENT_REG", length = 21, nullable = false)
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
       if(decodificacionEntidadRegistralOrigen != null && decodificacionEntidadRegistralOrigen.length() > 80){
           this.decodificacionEntidadRegistralOrigen = decodificacionEntidadRegistralOrigen.substring(0,79);
       }else{
           this.decodificacionEntidadRegistralOrigen = decodificacionEntidadRegistralOrigen;
       }
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

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "TIMESTAMP", length = 2147483647)
    public String getTimestampRegistro() {
        return timestampRegistro;
    }

    public void setTimestampRegistro(String timestampRegistro) {
        this.timestampRegistro = timestampRegistro;
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
        if(decodificacionUnidadTramitacionOrigen != null && decodificacionUnidadTramitacionOrigen.length() > 80){
            this.decodificacionUnidadTramitacionOrigen = decodificacionUnidadTramitacionOrigen.substring(0,79);
        }else{
            this.decodificacionUnidadTramitacionOrigen = decodificacionUnidadTramitacionOrigen;
        }
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
        if(decodificacionEntidadRegistralDestino != null && decodificacionEntidadRegistralDestino.length() > 80){
            this.decodificacionEntidadRegistralDestino = decodificacionEntidadRegistralDestino.substring(0,79);
        }else{
            this.decodificacionEntidadRegistralDestino = decodificacionEntidadRegistralDestino;
        }
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
        if(decodificacionUnidadTramitacionDestino != null && decodificacionUnidadTramitacionDestino.length() > 80){
            this.decodificacionUnidadTramitacionDestino = decodificacionUnidadTramitacionDestino.substring(0,79);
        }else{
            this.decodificacionUnidadTramitacionDestino = decodificacionUnidadTramitacionDestino;
        }
    }

    @Column(name = "RESUMEN", length = 240, nullable = false)
    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        if(resumen.length() > 240){
            this.resumen = resumen.substring(0,239);
        }else{
            this.resumen = resumen;
        }
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
        if(decodificacionTipoAnotacion != null && decodificacionTipoAnotacion.length() > 80){
            this.decodificacionTipoAnotacion = decodificacionTipoAnotacion.substring(0,79);
        }else{
            this.decodificacionTipoAnotacion = decodificacionTipoAnotacion;
        }
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
        if(decodificacionEntidadRegistralInicio != null && decodificacionEntidadRegistralInicio.length() > 80){
            this.decodificacionEntidadRegistralInicio = decodificacionEntidadRegistralInicio.substring(0,79);
        }else{
            this.decodificacionEntidadRegistralInicio = decodificacionEntidadRegistralInicio;
        }
    }

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "EXPONE", length = 2147483647)
    public String getExpone() {
        return expone;
    }

    public void setExpone(String expone) {
        this.expone = expone;
    }

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "SOLICITA", length = 2147483647)
    public String getSolicita() {
        return solicita;
    }

    public void setSolicita(String solicita) {
        this.solicita = solicita;
    }


    @OneToMany(cascade= CascadeType.ALL,targetEntity=AnexoSir.class, mappedBy="registroSir", fetch = FetchType.LAZY)
    public List<AnexoSir> getAnexos() {
        if (anexos == null) {
            anexos = new ArrayList<AnexoSir>();
        }

        return anexos;
    }

    public void setAnexos(List<AnexoSir> anexos) {
        this.anexos = anexos;
    }

    @OneToMany(cascade= CascadeType.ALL,targetEntity=InteresadoSir.class, mappedBy="registroSir", fetch = FetchType.LAZY)
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
    public EstadoRegistroSir getEstado() {
        return estado;
    }

    public void setEstado(EstadoRegistroSir estado) {
        this.estado = estado;
    }

    @Column(name = "FECHA_ESTADO", length = 14)
    public Date getFechaEstado() {
        return fechaEstado;
    }

    public void setFechaEstado(Date fechaEstado) {
        this.fechaEstado = fechaEstado;
    }

    @Column(name = "FECHA_RECEPCION", length = 14)
    public Date getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(Date fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    @Column(name = "REINTENTOS")
    public Integer getNumeroReintentos() {
        return numeroReintentos;
    }

    public void setNumeroReintentos(Integer numeroReintentos) {
        this.numeroReintentos = numeroReintentos;
    }

    @Column(name = "COD_ERROR")
    public String getCodigoError() {
        return codigoError;
    }

    public void setCodigoError(String codigoError) {
        this.codigoError = codigoError;
    }

    @Column(name = "DESC_ERROR", length = 2000)
    public String getDescripcionError() {
        return descripcionError;
    }

    public void setDescripcionError(String descripcionError) {
        this.descripcionError = descripcionError;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegistroSir registroSir = (RegistroSir) o;

        if (!id.equals(registroSir.id)) return false;

        return true;
    }

    @Transient
    public String getResumenCorto(){

        String resumenCorto = getResumen();

        if (resumenCorto.length() > 40) {
            resumenCorto = resumenCorto.substring(0, 40) + "...";
        }

        return resumenCorto;
    }

    @Transient
    public String getNombreInteresado(){

        if(!getInteresados().isEmpty()){
            return getInteresados().get(0).getNombreCompleto();
        }else{
            return "";
        }

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
