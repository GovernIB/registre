package es.caib.regweb.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created 27/05/14 12:59
 * Clase que representa un anexo dentro de regweb.
 * @author mgonzalez
 * @author anadal (index)
 */

@Entity
@Table(name = "RWE_ANEXO")
@org.hibernate.annotations.Table(appliesTo = "RWE_ANEXO", indexes = {
    @Index(name="RWE_ANEXO_TDOCAL_FK_I", columnNames = {"TDOCUMENTAL"}),
    @Index(name="RWE_ANEXO_REGDET_FK_I", columnNames = {"REGISTRODETALLE"})
})
@SequenceGenerator(name="generator",sequenceName = "RWE_ALL_SEQ", allocationSize = 1)
public class Anexo implements Serializable {

    private Long id;
    private String titulo; // Campo descriptivo del anexo.
    private String nombreFicheroAnexado;
    private Long tamano;
    private String tipoMIME;
    private TipoDocumental tipoDocumental; // reso, acord, factura, ..
    private Long validezDocumento;
    private Long tipoDocumento;
    private RegistroDetalle registroDetalle;
    private String observaciones;
    private Integer origenCiudadanoAdmin;
    private Date fechaCaptura;
    private int modoFirma;
    private String nombreFirmaAnexada;
    
    private String custodiaID;

    private String certificado;
    private String firmacsv;
    private String timestamp;
    private String validacionOCSP;

    private String csv; // código seguro verificación

    private boolean borrar = false;
    private boolean borrarfirma = false;



    public Anexo() {
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

    @Column(name = "NOMBREFICANEXADO", length= 80)
    public String getNombreFicheroAnexado() {
      return nombreFicheroAnexado;
    }

    public void setNombreFicheroAnexado(String nombreFicheroAnexado) {
      this.nombreFicheroAnexado = nombreFicheroAnexado;
    }

    @Column(name = "CUSTODIAID", length= 256)
    public String getCustodiaID() {
      return custodiaID;
    }

    public void setCustodiaID(String custodyID) {
      this.custodiaID = custodyID;
    }

    @Column(name = "TAMANO")
    @JsonIgnore
    public Long getTamano() {
      return tamano;
    }

    public void setTamano(Long tamano) {
      this.tamano = tamano;
    }

    @Column(name = "TIPOMIME")
    @JsonIgnore
    public String getTipoMIME() {
      return tipoMIME;
    }

    public void setTipoMIME(String tipoMIME) {
      this.tipoMIME = tipoMIME;
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

    @Column(name = "NOMBREFIRMAANEX", length= 80)
    public String getNombreFirmaAnexada() {
      return nombreFirmaAnexada;
    }

    public void setNombreFirmaAnexada(String nombreFirmaAnexada) {
      this.nombreFirmaAnexada = nombreFirmaAnexada;
    }
    @Column(name = "CERTIFICADO")
    public String getCertificado() {
      return certificado;
    }

    public void setCertificado(String certificado) {
      this.certificado = certificado;
    }
    
    
    @Column(name = "FIRMACSV")
    public String getFirmacsv() {
      return firmacsv;
    }

    public void setFirmacsv(String firmacsv) {
      this.firmacsv = firmacsv;
    }
    

    @Column(name = "TIMESTAMP")
    public String getTimestamp() {
      return timestamp;
    }

    public void setTimestamp(String timestamp) {
      this.timestamp = timestamp;
    }
    @Column(name = "VALIDACIONOCSP")
    public String getValidacionOCSP() {
      return validacionOCSP;
    }

    public void setValidacionOCSP(String validacionOCSP) {
      this.validacionOCSP = validacionOCSP;
    }


    @Column(name = "CSV")
    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }


    @Transient
    public boolean isBorrar() {
      return borrar;
    }

    public void setBorrar(boolean borrar) {
      this.borrar = borrar;
    }

    @Transient
    public boolean isBorrarfirma() {
      return borrarfirma;
    }

    public void setBorrarfirma(boolean borrarfirma) {
      this.borrarfirma = borrarfirma;
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


