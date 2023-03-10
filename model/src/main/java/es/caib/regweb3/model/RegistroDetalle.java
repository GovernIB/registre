package es.caib.regweb3.model;

import es.caib.regweb3.model.utils.AnexoFull;
import es.caib.regweb3.model.utils.IndicadorPrueba;
import es.caib.regweb3.utils.RegwebConstantes;
import es.caib.regweb3.utils.Versio;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundació Bit
 * User: earrivi
 * Date: 1/08/14
 */
@Entity
@Table(name = "RWE_REGISTRO_DETALLE")
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@XmlRootElement(name = "registroDetalle")
@XmlAccessorType(XmlAccessType.FIELD)
public class RegistroDetalle implements Serializable {

    @XmlAttribute
    private Long id;
    @XmlElement
    private String extracto;
    @XmlElement
    private Long tipoDocumentacionFisica;
    @XmlElement
    private TipoAsunto tipoAsunto;
    @XmlElement
    private Long idioma;
    @XmlElement
    private CodigoAsunto codigoAsunto;
    @XmlElement
    private String referenciaExterna;
    @XmlElement
    private String expediente;
    @XmlElement
    private Long transporte;
    @XmlElement
    private String numeroTransporte;
    @XmlElement
    private String observaciones;
    @XmlElement
    private Oficina oficinaOrigen;
    @XmlElement
    private String oficinaOrigenExternoCodigo;
    @XmlElement
    private String oficinaOrigenExternoDenominacion;
    @XmlElement
    private String numeroRegistroOrigen;
    @XmlElement
    private Date fechaOrigen; //FECHA Y HORA
    @XmlElement
    private String expedienteJustificante; // Identificador, de ArxiuCaib, del expediente del Justificante generado

    // Campos añadidos SIR
    @XmlTransient
    private IndicadorPrueba indicadorPrueba = IndicadorPrueba.NORMAL;
    @XmlTransient
    private String tipoAnotacion;
    @XmlTransient
    private String decodificacionTipoAnotacion;
    @XmlTransient
    private String codigoEntidadRegistralDestino;
    @XmlTransient
    private String decodificacionEntidadRegistralDestino;
    @XmlTransient
    private String identificadorIntercambio;
    // Fin Campos añadidos SIR

    @XmlElement
    private String expone;
    @XmlElement
    private String solicita;
    @XmlElement
    private String reserva;
    @XmlElement(name = "interesado")
    @XmlElementWrapper(name = "interesados")
    private List<Interesado> interesados = new ArrayList<Interesado>();
    @XmlElementWrapper(name = "anexos")
    private List<Anexo> anexos = new ArrayList<Anexo>();
    @XmlTransient
    private String aplicacion = "RWE3";
    @XmlTransient
    private String aplicacionTelematica;
    @XmlTransient
    private String version = Versio.VERSIO;
    @XmlTransient
    @Transient
    private List<AnexoFull> anexosFull = new ArrayList<AnexoFull>();

    //Metadatos nueva arquitectura SIR
    @XmlElement
    private String codigoSia;
    @XmlTransient
    private Boolean presencial;
    @XmlTransient
    private String tipoEnvioDocumentacion;
    // Fin Metadatos nueva arquitectura SIR

    @XmlTransient
    private Boolean recibidoSir = false;

    public RegistroDetalle() {
    }

    public RegistroDetalle(Long id) {
        this.id = id;
    }


    /**
     * @param rd
     */
    public RegistroDetalle(RegistroDetalle rd) {
        this.id = rd.id;
        this.extracto = rd.extracto;
        this.tipoDocumentacionFisica = rd.tipoDocumentacionFisica;
        this.idioma = rd.idioma;
        this.codigoAsunto = rd.codigoAsunto == null ? null : new CodigoAsunto(rd.codigoAsunto);
        this.referenciaExterna = rd.referenciaExterna;
        this.expediente = rd.expediente;
        this.transporte = rd.transporte;
        this.numeroTransporte = rd.numeroTransporte;
        this.observaciones = rd.observaciones;
        this.oficinaOrigen = rd.oficinaOrigen == null ? null : new Oficina(rd.oficinaOrigen);
        this.oficinaOrigenExternoCodigo = rd.oficinaOrigenExternoCodigo;
        this.oficinaOrigenExternoDenominacion = rd.oficinaOrigenExternoDenominacion;
        this.numeroRegistroOrigen = rd.numeroRegistroOrigen;
        this.fechaOrigen = rd.fechaOrigen;
        this.expedienteJustificante = rd.expedienteJustificante;
        this.expone = rd.expone;
        this.solicita = rd.solicita;
        this.reserva = rd.reserva;
        this.interesados = Interesado.clone(rd.interesados);
        //this.anexos = rd.anexos;
        this.aplicacion = rd.aplicacion;
        this.version = rd.version;
        this.indicadorPrueba = rd.getIndicadorPrueba();
        this.tipoAnotacion = rd.getTipoAnotacion();
        this.decodificacionTipoAnotacion = rd.getDecodificacionTipoAnotacion();
        this.codigoEntidadRegistralDestino = rd.getCodigoEntidadRegistralDestino();
        this.decodificacionEntidadRegistralDestino = rd.getDecodificacionEntidadRegistralDestino();
        this.identificadorIntercambio = rd.getIdentificadorIntercambio();
        this.codigoSia = rd.getCodigoSia();
        this.presencial = rd.getPresencial();
        this.tipoEnvioDocumentacion = rd.getTipoEnvioDocumentacion();
        this.aplicacionTelematica = rd.getAplicacionTelematica();
    }

    /**
     * Constructro para Informe LibroRegistro
     */
    public RegistroDetalle(Long idRegistroDetalle, String extracto, Long idOficinaOrigen, String denominacionOficinaOrigen,
                           String numeroRegistroOrigen, Date fechaOrigen, Long tipoDocumentacionFisica, Long idioma, String observaciones, String expediente,
                           Long idCodigoAsunto, String referenciaExterna, Long transporte, String numeroTransporte, List<Interesado> interesados) {

        this.id = idRegistroDetalle;
        this.extracto = extracto;
        this.tipoDocumentacionFisica = tipoDocumentacionFisica;
        this.numeroRegistroOrigen = numeroRegistroOrigen;
        this.fechaOrigen = fechaOrigen;
        this.idioma = idioma;
        this.observaciones = observaciones;
        this.expediente = expediente;
        this.oficinaOrigen = new Oficina(idOficinaOrigen, null, denominacionOficinaOrigen);
        this.codigoAsunto = new CodigoAsunto(idCodigoAsunto);
        this.referenciaExterna = referenciaExterna;
        this.transporte = transporte;
        this.numeroTransporte = numeroTransporte;
        this.interesados = interesados;
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


    @Column(name = "EXTRACTO", length = 240)
    public String getExtracto() {
        return extracto;
    }

    public void setExtracto(String extracto) {
        this.extracto = extracto;
    }


    @Column(name = "TIPODOCFISICA")
    public Long getTipoDocumentacionFisica() {
        return tipoDocumentacionFisica;
    }

    public void setTipoDocumentacionFisica(Long tipoDocumentacionFisica) {
        this.tipoDocumentacionFisica = tipoDocumentacionFisica;
    }

    @ManyToOne()
    @JoinColumn(name = "TIPOASUNTO", foreignKey = @ForeignKey(name = "RWE_REGDET_TIPOASUNTO_FK"))
    public TipoAsunto getTipoAsunto() {
        return tipoAsunto;
    }

    public void setTipoAsunto(TipoAsunto tipoAsunto) {
        this.tipoAsunto = tipoAsunto;
    }


    @Column(name = "IDIOMA")
    public Long getIdioma() {
        return idioma;
    }

    public void setIdioma(Long idioma) {
        this.idioma = idioma;
    }

    @ManyToOne()
    @JoinColumn(name = "CODASUNTO", foreignKey = @ForeignKey(name = "RWE_REGDET_CODASUNTO_FK"))
    public CodigoAsunto getCodigoAsunto() {
        return codigoAsunto;
    }

    public void setCodigoAsunto(CodigoAsunto codigoAsunto) {
        this.codigoAsunto = codigoAsunto;
    }

    @Column(name = "REFEXT", nullable = true, length = 16)
    public String getReferenciaExterna() {
        return referenciaExterna;
    }

    public void setReferenciaExterna(String referenciaExterna) {
        this.referenciaExterna = referenciaExterna;
    }

    @Column(name = "EXPEDIENTE", nullable = true, length = 80)
    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    @Column(name = "TRANSPORTE")
    public Long getTransporte() {
        return transporte;
    }

    public void setTransporte(Long transporte) {
        this.transporte = transporte;
    }

    //SICRES4
    @Column(name = "NUMTRANSPORTE", nullable = true, length = 40)
    public String getNumeroTransporte() {
        return numeroTransporte;
    }

    public void setNumeroTransporte(String numeroTransporte) {
        this.numeroTransporte = numeroTransporte;
    }

    //SICRES4
    @Column(name = "OBSERVACIONES", nullable = true, length = 160)
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @ManyToOne()
    @JoinColumn(name = "OFICINAORIG", foreignKey = @ForeignKey(name = "RWE_REGDET_OFICINAORIG_FK"))
    public Oficina getOficinaOrigen() {
        return oficinaOrigen;
    }

    public void setOficinaOrigen(Oficina oficinaOrigen) {
        this.oficinaOrigen = oficinaOrigen;
    }

    @Column(name = "OFICINAEXTERNO", length = 9)
    public String getOficinaOrigenExternoCodigo() {
        return oficinaOrigenExternoCodigo;
    }

    public void setOficinaOrigenExternoCodigo(String oficinaOrigenExternoCodigo) {
        this.oficinaOrigenExternoCodigo = oficinaOrigenExternoCodigo;
    }

    @Column(name = "DENOMOFIORIGEXT", length = 300)
    public String getOficinaOrigenExternoDenominacion() {
        return oficinaOrigenExternoDenominacion;
    }

    public void setOficinaOrigenExternoDenominacion(String oficinaOrigenExternoDenominacion) {
        this.oficinaOrigenExternoDenominacion = oficinaOrigenExternoDenominacion;
    }

    @Column(name = "NUMREG_ORIGEN", nullable = true, length = 20)
    public String getNumeroRegistroOrigen() {
        return numeroRegistroOrigen;
    }

    public void setNumeroRegistroOrigen(String numeroRegistroOrigen) {
        this.numeroRegistroOrigen = numeroRegistroOrigen;
    }

    @Column(name = "FECHAORIGEN", nullable = true)
    public Date getFechaOrigen() {
        return fechaOrigen;
    }

    public void setFechaOrigen(Date fechaOrigen) {
        this.fechaOrigen = fechaOrigen;
    }

    @Column(name = "EXPEDIENTE_JUST", length = 256)
    public String getExpedienteJustificante() {
        return expedienteJustificante;
    }

    public void setExpedienteJustificante(String expedienteJustificante) {
        this.expedienteJustificante = expedienteJustificante;
    }

    @Column(name = "INDICADOR_PRUEBA", length = 1, nullable = true) //SICRES4
    @Enumerated(EnumType.ORDINAL)
    public IndicadorPrueba getIndicadorPrueba() {
        return indicadorPrueba;
    }

    public void setIndicadorPrueba(IndicadorPrueba indicadorPrueba) {
        this.indicadorPrueba = indicadorPrueba;
    }

    @Column(name = "TIPO_ANOTACION", length = 2, nullable = true)
    public String getTipoAnotacion() {
        return tipoAnotacion;
    }

    public void setTipoAnotacion(String tipoAnotacion) {
        this.tipoAnotacion = tipoAnotacion;
    }

    @Column(name = "DEC_T_ANOTACION", length = 160, nullable = true)
    public String getDecodificacionTipoAnotacion() {
        return decodificacionTipoAnotacion;
    }

    public void setDecodificacionTipoAnotacion(String decodificacionTipoAnotacion) {
        this.decodificacionTipoAnotacion = decodificacionTipoAnotacion;
    }

    @Column(name = "COD_ENT_REG_DEST", length = 21, nullable = true)
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

    @Column(name = "ID_INTERCAMBIO", length = 33, nullable = true)
    public String getIdentificadorIntercambio() {
        return identificadorIntercambio;
    }

    public void setIdentificadorIntercambio(String identificadorIntercambio) {
        this.identificadorIntercambio = identificadorIntercambio;
    }

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "EXPONE", length = 2147483647, nullable = true)
    public String getExpone() {
        return expone;
    }

    public void setExpone(String expone) {
        this.expone = expone;
    }

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "SOLICITA", length = 2147483647, nullable = true)
    public String getSolicita() {
        return solicita;
    }

    public void setSolicita(String solicita) {
        this.solicita = solicita;
    }

    @Column(name = "RESERVA", length = 4000, nullable = true)
    public String getReserva() {
        return reserva;
    }

    public void setReserva(String reserva) {
        this.reserva = reserva;
    }

    @OneToMany(cascade = CascadeType.ALL, targetEntity = Interesado.class, mappedBy = "registroDetalle", fetch = FetchType.LAZY)
    @OrderBy("id")
    public List<Interesado> getInteresados() {
        return interesados;
    }

    public void setInteresados(List<Interesado> interesados) {
        this.interesados = interesados;
    }

    @OneToMany(cascade = CascadeType.ALL, targetEntity = Anexo.class, mappedBy = "registroDetalle", fetch = FetchType.LAZY)
    @OrderBy("tipoDocumento")
    public List<Anexo> getAnexos() {
        if (anexos == null) {
            return new ArrayList<Anexo>();
        }
        return anexos;
    }

    public void setAnexos(List<Anexo> anexos) {
        this.anexos = anexos;
    }

    @Column(name = "APLICACION")
    public String getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    @Column(name = "APLICACION_TELEMATICA")
    public String getAplicacionTelematica() {
        return aplicacionTelematica;
    }

    public void setAplicacionTelematica(String aplicacionTelematica) {
        this.aplicacionTelematica = aplicacionTelematica;
    }

    @Column(name = "VERSION")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Column(name = "CODIGOSIA", length = 80)
    public String getCodigoSia() {
        return codigoSia;
    }

    public void setCodigoSia(String codigoSia) {
        this.codigoSia = codigoSia;
    }

    @Column(name = "PRESENCIAL")
    public Boolean getPresencial() {
        return presencial;
    }

    public void setPresencial(Boolean presencial) {
        this.presencial = presencial;
    }

    @Column(name = "TIPOENVIODOC")
    public String getTipoEnvioDocumentacion() {
        return tipoEnvioDocumentacion;
    }

    public void setTipoEnvioDocumentacion(String tipoEnvioDocumentacion) {
        this.tipoEnvioDocumentacion = tipoEnvioDocumentacion;
    }

    @Column(name = "RECIBIDO_SIR")
    public Boolean getRecibidoSir() {
        return recibidoSir;
    }

    public void setRecibidoSir(Boolean sir) {
        this.recibidoSir = sir;
    }


    @Transient
    public List<AnexoFull> getAnexosFull() {
        return anexosFull;
    }

    @Transient
    public void setAnexosFull(List<AnexoFull> anexosFull) {
        this.anexosFull = anexosFull;
    }

    @Transient
    public String getNombreInteresadosHtml() {
        if (interesados != null && interesados.size() > 0) {
            String nombres = "";
            for (Interesado interesado : interesados) {
                if (!interesado.getIsRepresentante()) {
                    nombres = nombres.concat("- " + interesado.getNombreCompleto());

                    if (interesado.getRepresentante() != null) {
                        nombres = nombres.concat(" (R: " + interesado.getRepresentante().getNombreCompleto() + ")");

                    } else {
                        nombres = nombres.concat(" <br/>");
                    }
                }


            }

            return nombres;
        }

        return "";
    }

    @Transient
    public String getDocumentoInteresado() {
        if (!interesados.isEmpty()) {

            for (Interesado interesado : interesados) {
                if (!interesado.getIsRepresentante() && !interesado.getTipo().equals(RegwebConstantes.TIPO_INTERESADO_ADMINISTRACION)) {

                    return interesado.getDocumento();
                }
            }
        }
        return null;
    }

    @Transient
    public Integer getTotalInteresados() {

        int total = 0;

        for (Interesado interesado : interesados) {
            if (!interesado.getIsRepresentante()) {
                total = total + 1;
            }
        }

        return total;
    }

    /**
     * Comprueba si el Registro tiene el Justificante generado
     *
     * @return
     */
    @Transient
    public boolean getTieneJustificante() {
        for (Anexo anexo : anexos) {
            if (anexo.isJustificante()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Comprueba si el Registro tiene el Justificante generado
     *
     * @return
     */
    @Transient
    public boolean getTieneJustificanteCustodiado() {
        for (Anexo anexo : anexos) {
            if (anexo.isJustificante() && anexo.getCustodiado()) {
                return true;
            }
        }
        return false;
    }

    /**
     * devuelve el Justificante generado
     *
     * @return
     */
    @Transient
    public Anexo getJustificante() {
        for (Anexo anexo : anexos) {
            if (anexo.isJustificante()) {
                return anexo;
            }
        }
        return null;
    }

    /**
     * Devuelve el Justificante generado de tipo AnexoFull
     *
     * @return
     */
    @Transient
    public AnexoFull getJustificanteAnexoFull() {

        if (getAnexosFull() != null && getAnexosFull().size() > 0) {

            for (AnexoFull anexoFull : getAnexosFull()) {
                if (anexoFull.getAnexo().isJustificante()) {
                    return anexoFull;
                }
            }
        }

        return null;
    }

    /**
     * Devuelve si el registroDetalle tiene anexos
     *
     * @return
     */
    @Transient
    public boolean getTieneAnexos() {

        if (anexos.size() >= 1) {
            //Si solo tiene un anexo y es el justificante, no tiene anexos
            if (anexos.size() == 1 && getTieneJustificante()) {
                return false;
            }
            //Si solo tiene un anexo y no es el justificante, tiene un anexo
            if (anexos.size() == 1 && !getTieneJustificante()) {
                return true;
            }
            return true; //Tiene más de un anexo
        }
        return false; // No tiene ningun anexo
    }

    /**
     * Comprueba si tiene los anexos purgados
     *
     * @return
     */
    @Transient
    public boolean isAnexosPurgado() {

        for (Anexo anexo : anexos) {
            if (anexo.isPurgado() && !anexo.isJustificante()) {
                return true;
            }
        }
        return false; // No tiene ningun anexo
    }

    @Transient
    public String getExtractoCorto() {

        String extractoCorto = getExtracto();

        if (extractoCorto.length() > 40) {
            extractoCorto = extractoCorto.substring(0, 40) + "...";
        }

        return extractoCorto;
    }

    @Transient
    public String getReservaCorto() {

        String reservaCorto = getReserva();

        if (reservaCorto.length() > 40) {
            reservaCorto = reservaCorto.substring(0, 40) + "...";
        }

        return reservaCorto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RegistroDetalle that = (RegistroDetalle) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
