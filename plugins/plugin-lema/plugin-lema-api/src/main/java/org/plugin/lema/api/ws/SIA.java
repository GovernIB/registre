
package org.plugin.lema.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para SIA complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="SIA">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="codigoSIA" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaPeticionAcceso}CodigoSIA"/>
 *           &lt;element name="nombreSIA" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaPeticionAcceso}NombreSIA"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SIA", propOrder = {
    "codigoSIA",
    "nombreSIA"
})
public class SIA {

    protected String codigoSIA;
    protected String nombreSIA;

    /**
     * Obtiene el valor de la propiedad codigoSIA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoSIA() {
        return codigoSIA;
    }

    /**
     * Define el valor de la propiedad codigoSIA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoSIA(String value) {
        this.codigoSIA = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreSIA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreSIA() {
        return nombreSIA;
    }

    /**
     * Define el valor de la propiedad nombreSIA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreSIA(String value) {
        this.nombreSIA = value;
    }

}
