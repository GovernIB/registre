package es.caib.regweb3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.caib.regweb3.model.utils.ObjetoBasico;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created 27/05/14 12:59
 * Clase que representa un anexo dentro de regweb3.
 *
 * @author mgonzalez
 * @author anadal (index, refactoring anexos)
 */

@Entity
@SequenceGenerator(name = "generator", sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
@Table(name = "RWE_ANEXO",
        indexes = {
                @Index(name = "RWE_ANEXO_TDOCAL_FK_I", columnList = "TDOCUMENTAL"),
                @Index(name = "RWE_ANEXO_REGDET_FK_I", columnList = "REGISTRODETALLE"),
        })
@XmlRootElement(name = "anexo")
@XmlAccessorType(XmlAccessType.FIELD)
public class Anexo implements Serializable {

    @XmlAttribute
    private Long id;
    @XmlElement
    private String titulo; // Campo descriptivo del anexo.
    @XmlElement
    private TipoDocumental tipoDocumental; // reso, acord, factura, ..
    @XmlElement
    private Long tipoDocumento; //tipoAnexo SICRES4 ( ha cambiado el nombre del atributo y los valores)
    @XmlTransient
    private Entidad entidad;
    @XmlTransient
    private RegistroDetalle registroDetalle;
    @XmlElement
    private String observaciones;
    @XmlElement
    private Integer origenCiudadanoAdmin;
    @XmlElement
    private Date fechaCaptura;
    @XmlElement
    private int modoFirma;
    @XmlTransient
    private String custodiaID; // [DocumentCustody {1} uuid del expediente#documento creado]  [ArxiuCaib {2} uuid del documento creado en Arxiu]
    @XmlTransient
    private String csv;
    @XmlTransient
    private Long perfilCustodia; // Tipo de API utilizado para gestionar el Anexo [DocumentCustody {1} || ArxiuCaib {2}]
    @XmlTransient
    private String expedienteID; // uuid del expediente creado en Arxiu
    @XmlTransient
    private Boolean custodiado = false; // Indica si el Anexo está custodiado, es decir, si tiene csv
    @XmlTransient
    private Boolean scan = false; // Indica si el anexo se ha escaneado o no

    //SIR
    private Boolean firmaValida; // Indicará si la firma es vàlida o no
    private boolean justificante = false; // Indica si el anexo es justificante.

    //Validacion Firma
    private String signType;
    private String signFormat;
    private String signProfile;
    private String motivoNoValidacion;
    private Date fechaValidacion;
    private int estadoFirma;

    //Gestión anexos distribuidos
    private boolean purgado = false;

    // Anexos confidenciales (Son anexos sin documento, solo la información necesaria paa generar Justificante)
    @XmlTransient
    private Boolean confidencial = false; // Indica si un Anexo es confidencial (sin fichero) o no
    @XmlTransient
    private String nombreFichero;
    @XmlTransient
    private Integer tamanoFichero;

    //SICRES4
    private String resumen;
    private String codigoFormulario;
    private List<Metadato> metadatosGenerales;
    private List<Metadato> metadatosParticulares;

    //Referencia Única
    private String endpointRFU; //endpoint de INTERDOC
    private String identificadorRFU; // identificador INTERDOC



    public Anexo() {
    }

    public Anexo(Long perfilCustodia) {
        this.perfilCustodia = perfilCustodia;
    }

    /**
     *
     */
    public Anexo(Anexo a) {
        super();
        this.id = a.id;
        this.entidad = a.entidad;
        this.perfilCustodia = a.perfilCustodia;
        this.titulo = a.titulo;
        this.tipoDocumental = a.tipoDocumental == null ? null : new TipoDocumental(a.tipoDocumental);
        this.tipoDocumento = a.tipoDocumento;
        this.registroDetalle = a.registroDetalle;
        this.observaciones = a.observaciones;
        this.origenCiudadanoAdmin = a.origenCiudadanoAdmin;
        this.fechaCaptura = a.fechaCaptura;
        this.modoFirma = a.modoFirma;
        this.custodiaID = a.custodiaID;
        this.firmaValida = a.firmaValida;
        this.justificante = a.justificante;
        this.signFormat = a.signFormat;
        this.signProfile = a.signProfile;
        this.signType = a.signType;
        this.purgado = a.purgado;
        this.scan = a.scan;
    }

    public static List<Anexo> clone(List<Anexo> list) {
        if (list == null) {
            return null;
        }

        List<Anexo> clone = new ArrayList<Anexo>(list.size());
        for (Anexo anexo : list) {
            clone.add(anexo == null ? null : new Anexo(anexo));
        }

        return clone;
    }

    public Anexo(String titulo, Long tipoDocumento) {
        this.titulo = titulo;
        this.tipoDocumento = tipoDocumento;
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

    @Column(name = "TITULO", nullable = false, length = 200)
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }


    @Column(name = "CUSTODIAID", length = 256)
    public String getCustodiaID() {
        return custodiaID;
    }

    public void setCustodiaID(String custodyID) {
        this.custodiaID = custodyID;
    }


    @Column(name = "PERFIL_CUSTODIA")
    public Long getPerfilCustodia() {
        return perfilCustodia;
    }

    public void setPerfilCustodia(Long perfilCustodia) {
        this.perfilCustodia = perfilCustodia;
    }

    @Column(name = "EXPEDIENTEID", length = 256)
    public String getExpedienteID() {
        return expedienteID;
    }

    public void setExpedienteID(String expedienteID) {
        this.expedienteID = expedienteID;
    }

    @Column(name = "CUSTODIADO", nullable = true)
    public Boolean getCustodiado() {
        return custodiado;
    }

    public void setCustodiado(Boolean filesystem) {
        this.custodiado = filesystem;
    }

    @Column(name = "SCAN")
    public Boolean getScan() {
        return scan;
    }

    public void setScan(Boolean scan) {
        this.scan = scan;
    }

    @Column(name = "CONFIDENCIAL")
    public Boolean getConfidencial() {
        return confidencial;
    }

    public void setConfidencial(Boolean confidencial) {
        this.confidencial = confidencial;
    }

    @Column(name = "NOMBRE_FICHERO", length = 200)
    public String getNombreFichero() {
        return nombreFichero;
    }

    public void setNombreFichero(String nombreFichero) {
        this.nombreFichero = nombreFichero;
    }

    @Column(name = "TAMANO_FICHERO")
    public Integer getTamanoFichero() {
        return tamanoFichero;
    }

    public void setTamanoFichero(Integer tamanoFichero) {
        this.tamanoFichero = tamanoFichero;
    }

    @ManyToOne()
    @JoinColumn(name = "TDOCUMENTAL", foreignKey = @ForeignKey(name = "RWE_ANEXO_TDOCAL_FK"))
    public TipoDocumental getTipoDocumental() {
        return tipoDocumental;
    }

    public void setTipoDocumental(TipoDocumental tipoDocumental) {
        this.tipoDocumental = tipoDocumental;
    }


    @Column(name = "TIPODOC")
    public Long getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(Long tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REGISTRODETALLE", foreignKey = @ForeignKey(name = "RWE_ANEXO_REGDET_FK"))
    @JsonIgnore
    public RegistroDetalle getRegistroDetalle() {
        return registroDetalle;
    }

    public void setRegistroDetalle(RegistroDetalle registroDetalle) {
        this.registroDetalle = registroDetalle;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ENTIDAD", foreignKey = @ForeignKey(name = "RWE_ANEXO_ENTIDAD_FK"))
    @JsonIgnore
    public Entidad getEntidad() {
        return entidad;
    }

    public void setEntidad(Entidad entidad) {
        this.entidad = entidad;
    }

    @Column(name = "OBSERVACIONES", length = 50)
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Column(name = "ORIGEN")
    public Integer getOrigenCiudadanoAdmin() {
        return origenCiudadanoAdmin;
    }

    public void setOrigenCiudadanoAdmin(Integer origenCiudadanoAdmin) {
        this.origenCiudadanoAdmin = origenCiudadanoAdmin;
    }


    @Column(name = "FECHACAPTURA", nullable = false)
    @JsonIgnore
    public Date getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(Date fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    @Column(name = "MODOFIRMA")
    public int getModoFirma() {
        return modoFirma;
    }

    public void setModoFirma(int modoFirma) {
        this.modoFirma = modoFirma;
    }


    @Column(name = "CSV")
    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }

    @Column(name = "FIRMAVALIDA")
    public Boolean getFirmaValida() {
        return firmaValida;
    }

    public void setFirmaValida(Boolean firmaValida) {
        this.firmaValida = firmaValida;
    }

    @Column(name = "JUSTIFICANTE", nullable = false)
    public boolean isJustificante() {
        return justificante;
    }

    public void setJustificante(boolean justificante) {
        this.justificante = justificante;
    }

    @Column(name = "SIGNTYPE")
    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    @Column(name = "SIGNFORMAT")
    public String getSignFormat() {
        return signFormat;
    }

    public void setSignFormat(String signFormat) {
        this.signFormat = signFormat;
    }

    @Column(name = "SIGNPROFILE")
    public String getSignProfile() {
        return signProfile;
    }

    public void setSignProfile(String signProfile) {
        this.signProfile = signProfile;
    }

    @Column(name = "MOTIVONOVALID")
    public String getMotivoNoValidacion() {
        return motivoNoValidacion;
    }

    public void setMotivoNoValidacion(String motivoNoValidacion) {
        this.motivoNoValidacion = motivoNoValidacion;
    }

    @Column(name = "FECHAVALIDACION")
    public Date getFechaValidacion() {
        return fechaValidacion;
    }

    public void setFechaValidacion(Date fechaValidacion) {
        this.fechaValidacion = fechaValidacion;
    }

    @Column(name = "ESTADOFIRMA")
    public int getEstadoFirma() {
        return estadoFirma;
    }

    public void setEstadoFirma(int estadoFirma) {
        this.estadoFirma = estadoFirma;
    }

    @Column(name = "PURGADO")
    public boolean isPurgado() {
        return purgado;
    }

    public void setPurgado(boolean purgado) {
        this.purgado = purgado;
    }



    //SICRES4

    @Column(name = "RESUMEN", length = 160)
    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    @Column(name = "CODFORMUL", length = 80)
    public String getCodigoFormulario() {
        return codigoFormulario;
    }

    public void setCodigoFormulario(String codigoFormulario) {
        this.codigoFormulario = codigoFormulario;
    }


    @OneToMany(cascade = CascadeType.REMOVE, targetEntity = Metadato.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ANEXOGEN", foreignKey = @ForeignKey(name = "RWE_METADGEN_ANEXO_FK"))
    @OrderBy("campo")
    public List<Metadato> getMetadatosGenerales() {
        return metadatosGenerales;
    }

    public void setMetadatosGenerales(List<Metadato> metadatosGenerales) {
        this.metadatosGenerales = metadatosGenerales;
    }

    @OneToMany(cascade = CascadeType.REMOVE, targetEntity = Metadato.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ANEXOPART", foreignKey = @ForeignKey(name = "RWE_METADPAR_ANEXO_FK"))
    @OrderBy("campo")
    public List<Metadato> getMetadatosParticulares() {
        return metadatosParticulares;
    }

    public void setMetadatosParticulares(List<Metadato> metadatosParticulares) {
        this.metadatosParticulares = metadatosParticulares;
    }

    @Column(name = "ENDPOINTRFU")
    public String getEndpointRFU() {
        return endpointRFU;
    }

    public void setEndpointRFU(String endpointRFU) {
        this.endpointRFU = endpointRFU;
    }

    @Column(name = "IDENTIFRFU")
    public String getIdentificadorRFU() {
        return identificadorRFU;
    }

    public void setIdentificadorRFU(String identificadorRFU) {
        this.identificadorRFU = identificadorRFU;
    }

    @Transient
    public String getTituloCorto() {

        String tituloCorto = getTitulo();

        if (tituloCorto.length() > 20) {
            tituloCorto = getTitulo().substring(0, 20) + "...";
        }

        return tituloCorto;
    }

    @Transient
    public Integer getConfidencialSize() {

        Integer size = getTamanoFichero();

        if (size != null) {
            if (size < 1024) {
                return 1;
            } else {
                return size / 1024;
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Anexo anexo = (Anexo) o;

        if (id != null ? !id.equals(anexo.id) : anexo.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}


