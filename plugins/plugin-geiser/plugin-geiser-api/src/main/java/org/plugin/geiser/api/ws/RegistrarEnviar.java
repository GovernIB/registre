
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para registrarEnviar complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="registrarEnviar">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="authentication" type="{http://types.core.ws.rgeco.geiser.minhap.gob.es/}authenticationType" minOccurs="0"/>
 *         &lt;element name="peticion" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}peticionRegistroEnvioSimpleType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registrarEnviar", namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", propOrder = {
    "authentication",
    "peticion"
})
public class RegistrarEnviar {

    @XmlElement(namespace = "")
    protected AuthenticationType authentication;
    @XmlElement(namespace = "")
    protected PeticionRegistroEnvioSimpleType peticion;

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
     * Obtiene el valor de la propiedad peticion.
     * 
     * @return
     *     possible object is
     *     {@link PeticionRegistroEnvioSimpleType }
     *     
     */
    public PeticionRegistroEnvioSimpleType getPeticion() {
        return peticion;
    }

    /**
     * Define el valor de la propiedad peticion.
     * 
     * @param value
     *     allowed object is
     *     {@link PeticionRegistroEnvioSimpleType }
     *     
     */
    public void setPeticion(PeticionRegistroEnvioSimpleType value) {
        this.peticion = value;
    }

}
