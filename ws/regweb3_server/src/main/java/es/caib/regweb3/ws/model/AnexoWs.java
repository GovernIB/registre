package es.caib.regweb3.ws.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlInlineBinaryData;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Calendar;


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
    /**
     * Long TIPOVALIDEZDOCUMENTO_COPIA="01";
     * TIPOVALIDEZDOCUMENTO_COPIA_COMPULSADA="02";
     * TIPOVALIDEZDOCUMENTO_COPIA_ORIGINAL="03";
     * TIPOVALIDEZDOCUMENTO_ORIGINAL="04";
     */
    private String validezDocumento;
    /** 
     * Codi NTI: 
     * 
     *  TIPO_DOCUMENTO_FORMULARIO = "01";
     *  TIPO_DOCUMENTO_DOC_ADJUNTO = "02";
     *  TIPO_DOCUMENTO_FICHERO_TECNICO ="03";
     *  
     */
    private String tipoDocumento;
    private String observaciones;
    /**
     * ANEXO_ORIGEN_CIUDADANO = 0L;
     * ANEXO_ORIGEN_ADMINISTRACION = 1L;
     */
    private Integer origenCiudadanoAdmin;
    private Calendar fechaCaptura;
    private Integer modoFirma;
    
    
    private String nombreFirmaAnexada;
    @XmlInlineBinaryData
    private byte[] firmaAnexada;

    private String tipoMIMEFirmaAnexada;


    private String csv; // TODO este campo se creo para el plugin de digitalización de IECISA, verificar que no se emplee en NTI,SICRES.

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

}
