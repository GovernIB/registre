
package org.plugin.lema.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para AcusePdf complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="AcusePdf">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nombreAcuse" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaConsultaAcusePdf}nombre" minOccurs="0"/>
 *         &lt;element name="contenido" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaConsultaAcusePdf}Contenido" minOccurs="0"/>
 *         &lt;element name="mimeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="metadatos" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AcusePdf", namespace = "http://administracion.gob.es/punto-unico-notificaciones/respuestaConsultaAcusePdf", propOrder = {
    "nombreAcuse",
    "contenido",
    "mimeType",
    "metadatos"
})
public class AcusePdf {

    protected String nombreAcuse;
    protected Contenido contenido;
    protected String mimeType;
    protected String metadatos;

    /**
     * Obtiene el valor de la propiedad nombreAcuse.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreAcuse() {
        return nombreAcuse;
    }

    /**
     * Define el valor de la propiedad nombreAcuse.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreAcuse(String value) {
        this.nombreAcuse = value;
    }

    /**
     * Obtiene el valor de la propiedad contenido.
     * 
     * @return
     *     possible object is
     *     {@link Contenido }
     *     
     */
    public Contenido getContenido() {
        return contenido;
    }

    /**
     * Define el valor de la propiedad contenido.
     * 
     * @param value
     *     allowed object is
     *     {@link Contenido }
     *     
     */
    public void setContenido(Contenido value) {
        this.contenido = value;
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

}
