
package org.plugin.lema.api.realizadas.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Receptor complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Receptor">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nombreReceptor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nifReceptor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nombreRepresentante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nifRepresentante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Receptor", namespace = "http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas", propOrder = {
    "nombreReceptor",
    "nifReceptor",
    "nombreRepresentante",
    "nifRepresentante"
})
public class Receptor {

    @XmlElement(required = true)
    protected String nombreReceptor;
    @XmlElement(required = true)
    protected String nifReceptor;
    protected String nombreRepresentante;
    protected String nifRepresentante;

    /**
     * Obtiene el valor de la propiedad nombreReceptor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreReceptor() {
        return nombreReceptor;
    }

    /**
     * Define el valor de la propiedad nombreReceptor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreReceptor(String value) {
        this.nombreReceptor = value;
    }

    /**
     * Obtiene el valor de la propiedad nifReceptor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNifReceptor() {
        return nifReceptor;
    }

    /**
     * Define el valor de la propiedad nifReceptor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNifReceptor(String value) {
        this.nifReceptor = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreRepresentante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreRepresentante() {
        return nombreRepresentante;
    }

    /**
     * Define el valor de la propiedad nombreRepresentante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreRepresentante(String value) {
        this.nombreRepresentante = value;
    }

    /**
     * Obtiene el valor de la propiedad nifRepresentante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNifRepresentante() {
        return nifRepresentante;
    }

    /**
     * Define el valor de la propiedad nifRepresentante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNifRepresentante(String value) {
        this.nifRepresentante = value;
    }

}
