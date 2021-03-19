package es.caib.regweb3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created 27/05/14 12:59
 * Clase que representa un anexo dentro de regweb3.
 * @author mgonzalez
 * @author anadal (index, refactoring anexos)
 */

@Entity
@Table(name = "RWE_ANEXO")
@org.hibernate.annotations.Table(appliesTo = "RWE_ANEXO", indexes = {
    @Index(name="RWE_ANEXO_TDOCAL_FK_I", columnNames = {"TDOCUMENTAL"}),
    @Index(name="RWE_ANEXO_REGDET_FK_I", columnNames = {"REGISTRODETALLE"})
})
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
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
    private Long validezDocumento;
    @XmlElement
    private Long tipoDocumento;
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
    @XmlElement
    private byte[] certificado;
    @XmlElement
    private String firma;  //Corresponde al campo Firma del Documento del segmento "De_Anexo"( solo viene informado cuando la firma es CSV)
    @XmlElement
    private byte[] validacionOCSPCertificado;
    @XmlElement
    private byte[] timestamp;
    @XmlElement
    private byte[] hash;
    @XmlTransient
    private String custodiaID; // [DocumentCustody {1} uuid del expediente#documento creado]  [ArxiuCaib {2} uuid del documento creado en Arxiu]
    @XmlTransient
    private String csv;
    @XmlTransient
    private Long perfilCustodia; // Tipo de API utilizado para gestionar el Anexo [DocumentCustody {1} || ArxiuCaib {2}]
    @XmlTransient
    private String expedienteID; // uuid del expediente creado en Arxiu
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
      this.perfilCustodia = a.perfilCustodia;
      this.titulo = a.titulo;
      this.tipoDocumental = a.tipoDocumental == null? null: new TipoDocumental(a.tipoDocumental);
      this.validezDocumento = a.validezDocumento;
      this.tipoDocumento = a.tipoDocumento;
      this.registroDetalle = a.registroDetalle;
      this.observaciones = a.observaciones;
      this.origenCiudadanoAdmin = a.origenCiudadanoAdmin;
      this.fechaCaptura = a.fechaCaptura;
      this.modoFirma = a.modoFirma;
      this.custodiaID = a.custodiaID;
      this.certificado = a.certificado;
      this.firma = a.firma;
      this.validacionOCSPCertificado = a.validacionOCSPCertificado;
      this.hash = a.hash;
      this.timestamp = a.timestamp;
      this.firmaValida = a.firmaValida;
      this.justificante = a.justificante;
      this.signFormat = a.signFormat;
      this.signProfile = a.signProfile;
      this.signType = a.signType;
      this.purgado=a.purgado;
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
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "generator")
    @Column(name="ID")
    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    @Column(name ="TITULO", nullable = false, length=200)
    public String getTitulo() {
      return titulo;
    }

    public void setTitulo(String titulo) {
      this.titulo = titulo;
    }


    @Column(name = "CUSTODIAID", length= 256)
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

    @Column(name = "EXPEDIENTEID", length= 256)
    public String getExpedienteID() {
        return expedienteID;
    }

    public void setExpedienteID(String expedienteID) {
        this.expedienteID = expedienteID;
    }

    @Column(name ="SCAN")
    public Boolean getScan() {
        return scan;
    }

    public void setScan(Boolean scan) {
        this.scan = scan;
    }

    @ManyToOne()
    @JoinColumn(name = "TDOCUMENTAL")
    @ForeignKey(name = "RWE_ANEXO_TDOCAL_FK")
    public TipoDocumental getTipoDocumental() {
      return tipoDocumental;
    }

    public void setTipoDocumental(TipoDocumental tipoDocumental) {
      this.tipoDocumental = tipoDocumental;
    }


    @Column(name = "TVALDOC")
    public Long getValidezDocumento() {
      return validezDocumento;
    }

    public void setValidezDocumento(Long validezDocumento) {
      this.validezDocumento = validezDocumento;
    }

    @Column(name = "TIPODOC")
    public Long getTipoDocumento() {
      return tipoDocumento;
    }

    public void setTipoDocumento(Long tipoDocumento) {
      this.tipoDocumento = tipoDocumento;
    }

    @ManyToOne()
    @JoinColumn(name = "REGISTRODETALLE")
    @ForeignKey(name = "RWE_ANEXO_REGDET_FK")
    @JsonIgnore
    public RegistroDetalle getRegistroDetalle() {
        return registroDetalle;
    }

    public void setRegistroDetalle(RegistroDetalle registroDetalle) {
        this.registroDetalle = registroDetalle;
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


    @Column(name = "CERTIFICADO", nullable = true, length = 2000)
    public byte[] getCertificado() {
        return certificado;
    }

    public void setCertificado(byte[] certificado) {
        this.certificado = certificado;
    }

    @Lob
    @Column(name = "FIRMA", length = 2147483647)
    public String getFirma() {
        return firma;
    }

    public void setFirma(String firmaDocumento) {
        this.firma = firmaDocumento;
    }

    @Column(name = "TIMESTAMP", nullable = true, length = 2000)
    public byte[] getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(byte[] timestamp) {
        this.timestamp = timestamp;
    }

    @Column(name = "VAL_OCSP_CERTIFICADO", nullable = true, length = 2000)
    public byte[] getValidacionOCSPCertificado() {
        return validacionOCSPCertificado;
    }

    public void setValidacionOCSPCertificado(byte[] validacionOCSPCertificado) {
        this.validacionOCSPCertificado = validacionOCSPCertificado;
    }

    @Column(name = "HASH", length = 2000)
    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }


    @Column(name = "CSV")
    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }

    @Column(name ="FIRMAVALIDA")
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

    @Transient
    public String getTituloCorto(){

        String tituloCorto = getTitulo();

        if (tituloCorto.length() > 20) {
            tituloCorto = getTitulo().substring(0, 20) + "...";
        }

        return tituloCorto;
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


