
package org.plugin.lema.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para DetalleDocumento complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="DetalleDocumento">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nombre" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaPeticionAcceso}NombreDocumento" minOccurs="0"/>
 *         &lt;element name="contenido" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaPeticionAcceso}Contenido" minOccurs="0"/>
 *         &lt;element name="hashDocumento" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaPeticionAcceso}HashDocumento" minOccurs="0"/>
 *         &lt;element name="mimeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="metadatos" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enlaceDocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="referenciaDocumento" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="referenciaPdfAcuse" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *         &lt;element name="csvResguardo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DetalleDocumento", propOrder = {
    "nombre",
    "contenido",
    "hashDocumento",
    "mimeType",
    "metadatos",
    "enlaceDocumento",
    "referenciaDocumento",
    "referenciaPdfAcuse",
    "csvResguardo"
})
public class DetalleDocumento {

    protected String nombre;
    protected Contenido2 contenido;
    protected HashDocumento hashDocumento;
    protected String mimeType;
    protected String metadatos;
    protected String enlaceDocumento;
    protected byte[] referenciaDocumento;
    protected byte[] referenciaPdfAcuse;
    protected String csvResguardo;

    /**
     * Obtiene el valor de la propiedad nombre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el valor de la propiedad nombre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Obtiene el valor de la propiedad contenido.
     * 
     * @return
     *     possible object is
     *     {@link Contenido2 }
     *     
     */
    public Contenido2 getContenido() {
        return contenido;
    }

    /**
     * Define el valor de la propiedad contenido.
     * 
     * @param value
     *     allowed object is
     *     {@link Contenido2 }
     *     
     */
    public void setContenido(Contenido2 value) {
        this.contenido = value;
    }

    /**
     * Obtiene el valor de la propiedad hashDocumento.
     * 
     * @return
     *     possible object is
     *     {@link HashDocumento }
     *     
     */
    public HashDocumento getHashDocumento() {
        return hashDocumento;
    }

    /**
     * Define el valor de la propiedad hashDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link HashDocumento }
     *     
     */
    public void setHashDocumento(HashDocumento value) {
        this.hashDocumento = value;
    }

    /**
     * Obtiene el valor de la propiedad mimeType.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * Define el valor de la propiedad mimeType.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMimeType(String value) {
        this.mimeType = value;
    }

    /**
     * Obtiene el valor de la propiedad metadatos.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMetadatos() {
        return metadatos;
    }

    /**
     * Define el valor de la propiedad metadatos.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMetadatos(String value) {
        this.metadatos = value;
    }

    /**
     * Obtiene el valor de la propiedad enlaceDocumento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnlaceDocumento() {
        return enlaceDocumento;
    }

    /**
     * Define el valor de la propiedad enlaceDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnlaceDocumento(String value) {
        this.enlaceDocumento = value;
    }

    /**
     * Obtiene el valor de la propiedad referenciaDocumento.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getReferenciaDocumento() {
        return referenciaDocumento;
    }

    /**
     * Define el valor de la propiedad referenciaDocumento.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setReferenciaDocumento(byte[] value) {
        this.referenciaDocumento = value;
    }

    /**
     * Obtiene el valor de la propiedad referenciaPdfAcuse.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getReferenciaPdfAcuse() {
        return referenciaPdfAcuse;
    }

    /**
     * Define el valor de la propiedad referenciaPdfAcuse.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setReferenciaPdfAcuse(byte[] value) {
        this.referenciaPdfAcuse = value;
    }

    /**
     * Obtiene el valor de la propiedad csvResguardo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCsvResguardo() {
        return csvResguardo;
    }

    /**
     * Define el valor de la propiedad csvResguardo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCsvResguardo(String value) {
        this.csvResguardo = value;
    }

}
