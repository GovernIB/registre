
package org.plugin.lema.api.realizadas.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Persona complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Persona">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nombreTitular" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nifTitular" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoDIR3" type="{http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas}CodigoOrganismo" minOccurs="0"/>
 *         &lt;element name="codigoDIRe" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionEntidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Persona", namespace = "http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas", propOrder = {
    "nombreTitular",
    "nifTitular",
    "codigoDIR3",
    "codigoDIRe",
    "descripcionEntidad"
})
public class Persona {

    @XmlElement(required = true)
    protected String nombreTitular;
    protected String nifTitular;
    protected String codigoDIR3;
    protected String codigoDIRe;
    protected String descripcionEntidad;

    /**
     * Obtiene el valor de la propiedad nombreTitular.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreTitular() {
        return nombreTitular;
    }

    /**
     * Define el valor de la propiedad nombreTitular.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreTitular(String value) {
        this.nombreTitular = value;
    }

    /**
     * Obtiene el valor de la propiedad nifTitular.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNifTitular() {
        return nifTitular;
    }

    /**
     * Define el valor de la propiedad nifTitular.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNifTitular(String value) {
        this.nifTitular = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoDIR3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoDIR3() {
        return codigoDIR3;
    }

    /**
     * Define el valor de la propiedad codigoDIR3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoDIR3(String value) {
        this.codigoDIR3 = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoDIRe.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoDIRe() {
        return codigoDIRe;
    }

    /**
     * Define el valor de la propiedad codigoDIRe.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoDIRe(String value) {
        this.codigoDIRe = value;
    }

    /**
     * Obtiene el valor de la propiedad descripcionEntidad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionEntidad() {
        return descripcionEntidad;
    }

    /**
     * Define el valor de la propiedad descripcionEntidad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionEntidad(String value) {
        this.descripcionEntidad = value;
    }

}
