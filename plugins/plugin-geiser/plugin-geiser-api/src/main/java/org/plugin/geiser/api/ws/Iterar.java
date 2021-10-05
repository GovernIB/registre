
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para iterar complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="iterar">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="authentication" type="{http://types.core.ws.rgeco.geiser.minhap.gob.es/}authenticationType" minOccurs="0"/>
 *         &lt;element name="uidIterator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "iterar", namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", propOrder = {
    "authentication",
    "uidIterator"
})
public class Iterar {

    @XmlElement(namespace = "")
    protected AuthenticationType authentication;
    @XmlElement(namespace = "")
    protected String uidIterator;

    /**
     * Obtiene el valor de la propiedad authentication.
     * 
     * @return
     *     possible object is
     *     {@link AuthenticationType }
     *     
     */
    public AuthenticationType getAuthentication() {
        return authentication;
    }

    /**
     * Define el valor de la propiedad authentication.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthenticationType }
     *     
     */
    public void setAuthentication(AuthenticationType value) {
        this.authentication = value;
    }

    /**
     * Obtiene el valor de la propiedad uidIterator.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUidIterator() {
        return uidIterator;
    }

    /**
     * Define el valor de la propiedad uidIterator.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUidIterator(String value) {
        this.uidIterator = value;
    }

}
