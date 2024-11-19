
package org.plugin.lema.api.realizadas.ws;

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
 *         &lt;element name="nombre" type="{http://administracion.gob.es/punto-unico-notificaciones/consultaRealizadas}NombreDocumento" minOccurs="0"/>
 *         &lt;element name="contenido" type="{http://administracion.gob.es/punto-unico-notificaciones/consultaRealizadas}ContenidoMtomInfo" minOccurs="0"/>
 *         &lt;element name="hashDocumento" type="{http://administracion.gob.es/punto-unico-notificaciones/consultaRealizadas}HashDocumento" minOccurs="0"/>
 *         &lt;element name="mimeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="metadatos" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enlaceDocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "csvResguardo"
})
public class DetalleDocumento {

    protected String nombre;
    protected ContenidoMtomInfo contenido;
    protected HashDocumento hashDocumento;
    protected String mimeType;
    protected String metadatos;
    protected String enlaceDocumento;
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
     *     {@link ContenidoMtomInfo }
     *     
     */
    public ContenidoMtomInfo getContenido() {
        return contenido;
    }

    /**
     * Define el valor de la propiedad contenido.
     * 
     * @param value
     *     allowed object is
     *     {@link ContenidoMtomInfo }
     *     
     */
    public void setContenido(ContenidoMtomInfo value) {
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
