package es.caib.regweb3.model;

import org.codehaus.jackson.annotate.JsonIgnore;
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
    private byte[] firma;
    @XmlElement
    private byte[] validacionOCSPCertificado;
    @XmlElement
    private byte[] timestamp;
    @XmlElement
    private byte[] hash;

    @XmlTransient
    private String custodiaID;
    @XmlTransient
    private String csv; // TODO este campo parece que sobra, verificar que no se emplee en NTI

    
    public Anexo() {
    }
    
    
    

    /**
     * @param id
     * @param titulo
     * @param tipoDocumental
     * @param validezDocumento
     * @param tipoDocumento
     * @param registroDetalle
     * @param observaciones
     * @param origenCiudadanoAdmin
     * @param fechaCaptura
     * @param modoFirma
     * @param custodiaID
     * @param csv
     */
    public Anexo(Anexo a) {
      super();
      this.id = a.id;
      this.titulo = a.titulo;
      this.tipoDocumental = a.tipoDocumental == null? null: new TipoDocumental(a.tipoDocumental);
      this.validezDocumento = a.validezDocumento;
      this.tipoDocumento = a.tipoDocumento;
      //this.registroDetalle = a.registroDetalle;
      this.observaciones = a.observaciones;
      this.origenCiudadanoAdmin = a.origenCiudadanoAdmin;
      this.fechaCaptura = a.fechaCaptura;
      this.modoFirma = a.modoFirma;
      this.custodiaID = a.custodiaID;
      this.csv = a.csv;
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


    @ManyToOne(cascade = CascadeType.PERSIST)
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

    @ManyToOne(cascade = CascadeType.PERSIST)
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


    @Column(name = "CERTIFICADO", nullable = true)
    public byte[] getCertificado() {
        return certificado;
    }

    public void setCertificado(byte[] certificado) {
        this.certificado = certificado;
    }

    @Column(name = "FIRMA", nullable = true)
    public byte[] getFirma() {
        return firma;
    }

    public void setFirma(byte[] firmaDocumento) {
        this.firma = firmaDocumento;
    }

    @Column(name = "TIMESTAMP", nullable = true)
    public byte[] getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(byte[] timestamp) {
        this.timestamp = timestamp;
    }

    @Column(name = "VAL_OCSP_CERTIFICADO", nullable = true)
    public byte[] getValidacionOCSPCertificado() {
        return validacionOCSPCertificado;
    }

    public void setValidacionOCSPCertificado(byte[] validacionOCSPCertificado) {
        this.validacionOCSPCertificado = validacionOCSPCertificado;
    }

    @Column(name = "HASH", nullable = false)
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


