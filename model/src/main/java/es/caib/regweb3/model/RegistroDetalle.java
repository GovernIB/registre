package es.caib.regweb3.model;

import es.caib.regweb3.utils.Versio;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
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
    private Date fechaOrigen;
    @XmlElement
    private String expone;
    @XmlElement
    private String solicita;
    @XmlElement
    private String reserva;
    @XmlElement( name="interesado" )
    @XmlElementWrapper( name="interesados" )
    private List<Interesado> interesados = new ArrayList<Interesado>();
    @XmlElementWrapper( name="anexos" )
    private List<Anexo> anexos = new ArrayList<Anexo>();
    @XmlTransient
    private String aplicacion = "REGWEB3";
    @XmlTransient
    private String version = Versio.VERSIO;


    public RegistroDetalle() {
    }

    public RegistroDetalle(Long id) {
        this.id = id;
    }
    
    
    
    
    

    /**
     * @param id
     * @param extracto
     * @param tipoDocumentacionFisica
     * @param tipoAsunto
     * @param idioma
     * @param codigoAsunto
     * @param referenciaExterna
     * @param expediente
     * @param transporte
     * @param numeroTransporte
     * @param observaciones
     * @param oficinaOrigen
     * @param oficinaOrigenExternoCodigo
     * @param oficinaOrigenExternoDenominacion
     * @param numeroRegistroOrigen
     * @param fechaOrigen
     * @param expone
     * @param solicita
     * @param reserva
     * @param interesados
     * @param anexos
     * @param aplicacion
     * @param version
     */
    public RegistroDetalle(RegistroDetalle rd) {
      this.id = rd.id;
      this.extracto = rd.extracto;
      this.tipoDocumentacionFisica = rd.tipoDocumentacionFisica;
      this.tipoAsunto = rd.tipoAsunto == null? null : new TipoAsunto(rd.tipoAsunto);
      this.idioma = rd.idioma;
      this.codigoAsunto = rd.codigoAsunto == null? null : new CodigoAsunto(rd.codigoAsunto);
      this.referenciaExterna = rd.referenciaExterna;
      this.expediente = rd.expediente;
      this.transporte = rd.transporte;
      this.numeroTransporte = rd.numeroTransporte;
      this.observaciones = rd.observaciones;
      this.oficinaOrigen = rd.oficinaOrigen == null? null : new Oficina(rd.oficinaOrigen);
      this.oficinaOrigenExternoCodigo = rd.oficinaOrigenExternoCodigo;
      this.oficinaOrigenExternoDenominacion = rd.oficinaOrigenExternoDenominacion;
      this.numeroRegistroOrigen = rd.numeroRegistroOrigen;
      this.fechaOrigen = rd.fechaOrigen;
      this.expone = rd.expone;
      this.solicita = rd.solicita;
      this.reserva = rd.reserva;
      this.interesados = Interesado.clone(rd.interesados);
      //this.anexos = rd.anexos;
      this.aplicacion = rd.aplicacion;
      this.version = rd.version;
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


    @Column(name = "EXTRACTO", length = 240)
    public String getExtracto() {
        return extracto;
    }

    public void setExtracto(String extracto) {
        this.extracto = extracto;
    }



    @Column(name="TIPODOCFISICA")
    public Long getTipoDocumentacionFisica() {
        return tipoDocumentacionFisica;
    }

    public void setTipoDocumentacionFisica(Long tipoDocumentacionFisica) {
        this.tipoDocumentacionFisica = tipoDocumentacionFisica;
    }

    @ManyToOne(cascade= CascadeType.PERSIST)
    @JoinColumn(name="TIPOASUNTO")
    @ForeignKey(name="RWE_REGDET_TIPOASUNTO_FK")
    public TipoAsunto getTipoAsunto() {
        return tipoAsunto;
    }

    public void setTipoAsunto(TipoAsunto tipoAsunto) {
        this.tipoAsunto = tipoAsunto;
    }


    @Column(name="IDIOMA")
    public Long getIdioma() {
        return idioma;
    }

    public void setIdioma(Long idioma) {
        this.idioma = idioma;
    }

    @ManyToOne(cascade= CascadeType.PERSIST)
    @JoinColumn(name="CODASUNTO")
    @ForeignKey(name="RWE_REGDET_CODASUNTO_FK")
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

    @Column(name="TRANSPORTE")
    public Long getTransporte() {
        return transporte;
    }

    public void setTransporte(Long transporte) {
        this.transporte = transporte;
    }

    @Column(name = "NUMTRANSPORTE", nullable = true, length = 20)
    public String getNumeroTransporte() {
        return numeroTransporte;
    }

    public void setNumeroTransporte(String numeroTransporte) {
        this.numeroTransporte = numeroTransporte;
    }

    @Column(name = "OBSERVACIONES", nullable = true, length = 50)
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @ManyToOne(cascade= CascadeType.PERSIST)
    @JoinColumn(name="OFICINAORIG")
    @ForeignKey(name="RWE_REGDET_OFICINAORIG_FK")
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

    @Column(name = "RESERVA", length = 4000, nullable = true)
    public String getReserva() {
        return reserva;
    }

    public void setReserva(String reserva) {
        this.reserva = reserva;
    }

    @OneToMany(cascade= CascadeType.ALL,targetEntity=Interesado.class, mappedBy="registroDetalle")
    @LazyCollection(value= LazyCollectionOption.FALSE)
    public List<Interesado> getInteresados() {
        return interesados;
    }

    public void setInteresados(List<Interesado> interesados) {
        this.interesados = interesados;
    }

    @OneToMany(cascade= CascadeType.ALL,targetEntity=Anexo.class, mappedBy="registroDetalle")
    @LazyCollection(value= LazyCollectionOption.FALSE)
    public List<Anexo> getAnexos() {
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

    @Column(name = "VERSION")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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
