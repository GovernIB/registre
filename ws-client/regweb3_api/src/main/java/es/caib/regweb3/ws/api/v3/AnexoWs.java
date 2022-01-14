
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;


/**
 * <p>Clase Java para anexoWs complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
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
 *         &lt;element name="justificante" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="confidencial" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="hash" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="tamanoFichero" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "csv",
    "justificante",
    "confidencial",
    "hash",
    "tamanoFichero"
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
    protected Boolean justificante;
    protected Boolean confidencial;
    protected byte[] hash;
    protected int tamanoFichero;

    /**
     * Obtiene el valor de la propiedad titulo.
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
     * Define el valor de la propiedad titulo.
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
     * Obtiene el valor de la propiedad nombreFicheroAnexado.
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
     * Define el valor de la propiedad nombreFicheroAnexado.
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
     * Obtiene el valor de la propiedad ficheroAnexado.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getFicheroAnexado() {
        return ficheroAnexado;
    }

    /**
     * Define el valor de la propiedad ficheroAnexado.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setFicheroAnexado(byte[] value) {
        this.ficheroAnexado = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoMIMEFicheroAnexado.
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
     * Define el valor de la propiedad tipoMIMEFicheroAnexado.
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
     * Obtiene el valor de la propiedad tipoDocumental.
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
     * Define el valor de la propiedad tipoDocumental.
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
     * Obtiene el valor de la propiedad validezDocumento.
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
     * Define el valor de la propiedad validezDocumento.
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
     * Obtiene el valor de la propiedad tipoDocumento.
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
     * Define el valor de la propiedad tipoDocumento.
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
     * Obtiene el valor de la propiedad observaciones.
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
     * Define el valor de la propiedad observaciones.
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
     * Obtiene el valor de la propiedad origenCiudadanoAdmin.
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
     * Define el valor de la propiedad origenCiudadanoAdmin.
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
     * Obtiene el valor de la propiedad fechaCaptura.
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
     * Define el valor de la propiedad fechaCaptura.
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
     * Obtiene el valor de la propiedad modoFirma.
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
     * Define el valor de la propiedad modoFirma.
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
     * Obtiene el valor de la propiedad nombreFirmaAnexada.
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
     * Define el valor de la propiedad nombreFirmaAnexada.
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
     * Obtiene el valor de la propiedad firmaAnexada.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getFirmaAnexada() {
        return firmaAnexada;
    }

    /**
     * Define el valor de la propiedad firmaAnexada.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setFirmaAnexada(byte[] value) {
        this.firmaAnexada = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoMIMEFirmaAnexada.
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
     * Define el valor de la propiedad tipoMIMEFirmaAnexada.
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
     * Obtiene el valor de la propiedad csv.
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
     * Define el valor de la propiedad csv.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCsv(String value) {
        this.csv = value;
    }

    /**
     * Obtiene el valor de la propiedad justificante.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isJustificante() {
        return justificante;
    }

    /**
     * Define el valor de la propiedad justificante.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setJustificante(Boolean value) {
        this.justificante = value;
    }

    /**
     * Obtiene el valor de la propiedad confidencial.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isConfidencial() {
        return confidencial;
    }

    /**
     * Define el valor de la propiedad confidencial.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setConfidencial(Boolean value) {
        this.confidencial = value;
    }

    /**
     * Obtiene el valor de la propiedad hash.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getHash() {
        return hash;
    }

    /**
     * Define el valor de la propiedad hash.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setHash(byte[] value) {
        this.hash = value;
    }

    /**
     * Obtiene el valor de la propiedad tamanoFichero.
     * 
     */
    public int getTamanoFichero() {
        return tamanoFichero;
    }

    /**
     * Define el valor de la propiedad tamanoFichero.
     * 
     */
    public void setTamanoFichero(int value) {
        this.tamanoFichero = value;
    }

}
