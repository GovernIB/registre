
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;


/**
 * <p>Java class for anexoWs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="anexoWs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="titulo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreFicheroAnexado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ficheroAnexado" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="tipoMIMEFicheroAnexado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoDocumental" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="validezDocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoDocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="observaciones" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="origenCiudadanoAdmin" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="fechaCaptura" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="modoFirma" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="nombreFirmaAnexada" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="firmaAnexada" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="tipoMIMEFirmaAnexada" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="csv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "anexoWs", propOrder = {
    "titulo",
    "nombreFicheroAnexado",
    "ficheroAnexado",
    "tipoMIMEFicheroAnexado",
    "tipoDocumental",
    "validezDocumento",
    "tipoDocumento",
    "observaciones",
    "origenCiudadanoAdmin",
    "fechaCaptura",
    "modoFirma",
    "nombreFirmaAnexada",
    "firmaAnexada",
    "tipoMIMEFirmaAnexada",
    "csv"
})
public class AnexoWs {

    protected String titulo;
    protected String nombreFicheroAnexado;
    protected byte[] ficheroAnexado;
    protected String tipoMIMEFicheroAnexado;
    protected String tipoDocumental;
    protected String validezDocumento;
    protected String tipoDocumento;
    protected String observaciones;
    protected Integer origenCiudadanoAdmin;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Timestamp fechaCaptura;
    protected Integer modoFirma;
    protected String nombreFirmaAnexada;
    protected byte[] firmaAnexada;
    protected String tipoMIMEFirmaAnexada;
    protected String csv;

    /**
     * Gets the value of the titulo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Sets the value of the titulo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitulo(String value) {
        this.titulo = value;
    }

    /**
     * Gets the value of the nombreFicheroAnexado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreFicheroAnexado() {
        return nombreFicheroAnexado;
    }

    /**
     * Sets the value of the nombreFicheroAnexado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreFicheroAnexado(String value) {
        this.nombreFicheroAnexado = value;
    }

    /**
     * Gets the value of the ficheroAnexado property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getFicheroAnexado() {
        return ficheroAnexado;
    }

    /**
     * Sets the value of the ficheroAnexado property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setFicheroAnexado(byte[] value) {
        this.ficheroAnexado = ((byte[]) value);
    }

    /**
     * Gets the value of the tipoMIMEFicheroAnexado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoMIMEFicheroAnexado() {
        return tipoMIMEFicheroAnexado;
    }

    /**
     * Sets the value of the tipoMIMEFicheroAnexado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoMIMEFicheroAnexado(String value) {
        this.tipoMIMEFicheroAnexado = value;
    }

    /**
     * Gets the value of the tipoDocumental property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDocumental() {
        return tipoDocumental;
    }

    /**
     * Sets the value of the tipoDocumental property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDocumental(String value) {
        this.tipoDocumental = value;
    }

    /**
     * Gets the value of the validezDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValidezDocumento() {
        return validezDocumento;
    }

    /**
     * Sets the value of the validezDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidezDocumento(String value) {
        this.validezDocumento = value;
    }

    /**
     * Gets the value of the tipoDocumento property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Sets the value of the tipoDocumento property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDocumento(String value) {
        this.tipoDocumento = value;
    }

    /**
     * Gets the value of the observaciones property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Sets the value of the observaciones property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObservaciones(String value) {
        this.observaciones = value;
    }

    /**
     * Gets the value of the origenCiudadanoAdmin property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOrigenCiudadanoAdmin() {
        return origenCiudadanoAdmin;
    }

    /**
     * Sets the value of the origenCiudadanoAdmin property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOrigenCiudadanoAdmin(Integer value) {
        this.origenCiudadanoAdmin = value;
    }

    /**
     * Gets the value of the fechaCaptura property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Timestamp getFechaCaptura() {
        return fechaCaptura;
    }

    /**
     * Sets the value of the fechaCaptura property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaCaptura(Timestamp value) {
        this.fechaCaptura = value;
    }

    /**
     * Gets the value of the modoFirma property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getModoFirma() {
        return modoFirma;
    }

    /**
     * Sets the value of the modoFirma property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setModoFirma(Integer value) {
        this.modoFirma = value;
    }

    /**
     * Gets the value of the nombreFirmaAnexada property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreFirmaAnexada() {
        return nombreFirmaAnexada;
    }

    /**
     * Sets the value of the nombreFirmaAnexada property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreFirmaAnexada(String value) {
        this.nombreFirmaAnexada = value;
    }

    /**
     * Gets the value of the firmaAnexada property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getFirmaAnexada() {
        return firmaAnexada;
    }

    /**
     * Sets the value of the firmaAnexada property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setFirmaAnexada(byte[] value) {
        this.firmaAnexada = ((byte[]) value);
    }

    /**
     * Gets the value of the tipoMIMEFirmaAnexada property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoMIMEFirmaAnexada() {
        return tipoMIMEFirmaAnexada;
    }

    /**
     * Sets the value of the tipoMIMEFirmaAnexada property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoMIMEFirmaAnexada(String value) {
        this.tipoMIMEFirmaAnexada = value;
    }

    /**
     * Gets the value of the csv property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCsv() {
        return csv;
    }

    /**
     * Sets the value of the csv property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCsv(String value) {
        this.csv = value;
    }

}
