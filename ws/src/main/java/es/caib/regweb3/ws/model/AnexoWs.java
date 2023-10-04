package es.caib.regweb3.ws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlInlineBinaryData;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Calendar;
import java.util.Set;


/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * @author mgonzalez
 * @author anadal
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AnexoWs {

    private String titulo;
    private String nombreFicheroAnexado;
    @XmlInlineBinaryData
    private byte[] ficheroAnexado;
    private String tipoMIMEFicheroAnexado;
    private String tipoDocumental;
    private String validezDocumento;
    private String tipoDocumento; //SICRES 4 le llama tipoAnexo
    private String observaciones;
    private Integer origenCiudadanoAdmin;
    private Calendar fechaCaptura;
    private Integer modoFirma;
    private String nombreFirmaAnexada;
    @XmlInlineBinaryData
    private byte[] firmaAnexada;
    private String tipoMIMEFirmaAnexada;
    private String csv;
    private Boolean justificante = false;
    private Boolean confidencial = false;
    @XmlInlineBinaryData
    private byte[] hash;
    private int tamanoFichero;


    //SICRES4
    private String resumen;
    private String codigoFormulario;
    private Set<MetadatoWs> metadatos;

    public String getTitulo() {
      return titulo;
    }

    public void setTitulo(String titulo) {
      this.titulo = titulo;
    }

    public String getNombreFicheroAnexado() {
      return nombreFicheroAnexado;
    }

    public void setNombreFicheroAnexado(String nombreFicheroAnexado) {
      this.nombreFicheroAnexado = nombreFicheroAnexado;
    }

    public byte[] getFicheroAnexado() {
      return ficheroAnexado;
    }

    public void setFicheroAnexado(byte[] ficheroAnexado) {
      this.ficheroAnexado = ficheroAnexado;
    }

    public String getTipoMIMEFicheroAnexado() {
      return tipoMIMEFicheroAnexado;
    }

    public void setTipoMIMEFicheroAnexado(String tipoMIMEFicheroAnexado) {
      this.tipoMIMEFicheroAnexado = tipoMIMEFicheroAnexado;
    }

    public String getTipoDocumental() {
      return tipoDocumental;
    }

    public void setTipoDocumental(String tipoDocumental) {
      this.tipoDocumental = tipoDocumental;
    }

    public String getValidezDocumento() {
      return validezDocumento;
    }

    public void setValidezDocumento(String validezDocumento) {
      this.validezDocumento = validezDocumento;
    }

    public String getTipoDocumento() {
      return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
      this.tipoDocumento = tipoDocumento;
    }

    public String getObservaciones() {
          return observaciones;
      }

    public void setObservaciones(String observaciones) {
      this.observaciones = observaciones;
    }

    public Integer getOrigenCiudadanoAdmin() {
      return origenCiudadanoAdmin;
    }

    public void setOrigenCiudadanoAdmin(Integer origenCiudadanoAdmin) {
      this.origenCiudadanoAdmin = origenCiudadanoAdmin;
    }

    public Calendar getFechaCaptura() {
      return fechaCaptura;
    }

    public void setFechaCaptura(Calendar fechaCaptura) {
      this.fechaCaptura = fechaCaptura;
    }

    public Integer getModoFirma() {
      return modoFirma;
    }

    public void setModoFirma(Integer modoFirma) {
      this.modoFirma = modoFirma;
    }

    public String getNombreFirmaAnexada() {
      return nombreFirmaAnexada;
    }

    public void setNombreFirmaAnexada(String nombreFirmaAnexada) {
      this.nombreFirmaAnexada = nombreFirmaAnexada;
    }

    public byte[] getFirmaAnexada() {
      return firmaAnexada;
    }

    public void setFirmaAnexada(byte[] firmaAnexada) {
      this.firmaAnexada = firmaAnexada;
    }


    public String getTipoMIMEFirmaAnexada() {
      return tipoMIMEFirmaAnexada;
    }

    public void setTipoMIMEFirmaAnexada(String tipoMIMEFirmaAnexada) {
      this.tipoMIMEFirmaAnexada = tipoMIMEFirmaAnexada;
    }

    public String getCsv() {
      return csv;
    }

    public void setCsv(String csv) {
      this.csv = csv;
    }

    public Boolean isJustificante() {
        return justificante;
    }

    public void setJustificante(Boolean justificante) {
        this.justificante = justificante;
    }

    public Boolean getJustificante() {
        return justificante;
    }

    public Boolean getConfidencial() {
        return confidencial;
    }

    public void setConfidencial(Boolean confidencial) {
        this.confidencial = confidencial;
    }

    public byte[] getHash() {
        return hash;
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public int getTamanoFichero() {
        return tamanoFichero;
    }

    public void setTamanoFichero(int tamanoFichero) {
        this.tamanoFichero = tamanoFichero;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public String getCodigoFormulario() {
        return codigoFormulario;
    }

    public void setCodigoFormulario(String codigoFormulario) {
        this.codigoFormulario = codigoFormulario;
    }

    public Set<MetadatoWs> getMetadatos() {
        return metadatos;
    }

    public void setMetadatos(Set<MetadatoWs> metadatos) {
        this.metadatos = metadatos;
    }
}
