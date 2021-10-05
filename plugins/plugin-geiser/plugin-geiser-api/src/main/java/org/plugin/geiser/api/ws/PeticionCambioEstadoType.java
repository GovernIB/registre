
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para peticionCambioEstadoType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="peticionCambioEstadoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nuRegistro" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="motivo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "peticionCambioEstadoType", propOrder = {
    "nuRegistro",
    "motivo"
})
public class PeticionCambioEstadoType {

    @XmlElement(required = true)
    protected String nuRegistro;
    @XmlElement(required = true)
    protected String motivo;

    /**
     * Obtiene el valor de la propiedad nuRegistro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNuRegistro() {
        return nuRegistro;
    }

    /**
     * Define el valor de la propiedad nuRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNuRegistro(String value) {
        this.nuRegistro = value;
    }

    /**
     * Obtiene el valor de la propiedad motivo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * Define el valor de la propiedad motivo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotivo(String value) {
        this.motivo = value;
    }

}
