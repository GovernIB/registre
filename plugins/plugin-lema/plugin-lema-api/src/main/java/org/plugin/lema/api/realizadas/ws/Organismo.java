
package org.plugin.lema.api.realizadas.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Organismo complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Organismo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoOrganismo">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="9"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="nombreOrganismo">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="nifOrganismo" type="{http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas}Nif" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Organismo", namespace = "http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas", propOrder = {
    "codigoOrganismo",
    "nombreOrganismo",
    "nifOrganismo"
})
public class Organismo {

    @XmlElement(required = true)
    protected String codigoOrganismo;
    @XmlElement(required = true)
    protected String nombreOrganismo;
    protected String nifOrganismo;

    /**
     * Obtiene el valor de la propiedad codigoOrganismo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoOrganismo() {
        return codigoOrganismo;
    }

    /**
     * Define el valor de la propiedad codigoOrganismo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoOrganismo(String value) {
        this.codigoOrganismo = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreOrganismo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreOrganismo() {
        return nombreOrganismo;
    }

    /**
     * Define el valor de la propiedad nombreOrganismo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreOrganismo(String value) {
        this.nombreOrganismo = value;
    }

    /**
     * Obtiene el valor de la propiedad nifOrganismo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNifOrganismo() {
        return nifOrganismo;
    }

    /**
     * Define el valor de la propiedad nifOrganismo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNifOrganismo(String value) {
        this.nifOrganismo = value;
    }

}
