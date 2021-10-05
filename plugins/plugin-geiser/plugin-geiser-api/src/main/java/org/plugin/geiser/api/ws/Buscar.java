
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para buscar complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="buscar">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="authentication" type="{http://types.core.ws.rgeco.geiser.minhap.gob.es/}authenticationType" minOccurs="0"/>
 *         &lt;element name="peticion" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}peticionBusquedaType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "buscar", namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", propOrder = {
    "authentication",
    "peticion"
})
public class Buscar {

    @XmlElement(namespace = "")
    protected AuthenticationType authentication;
    @XmlElement(namespace = "")
    protected PeticionBusquedaType peticion;

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
     *     {@link PeticionBusquedaType }
     *     
     */
    public PeticionBusquedaType getPeticion() {
        return peticion;
    }

    /**
     * Define el valor de la propiedad peticion.
     * 
     * @param value
     *     allowed object is
     *     {@link PeticionBusquedaType }
     *     
     */
    public void setPeticion(PeticionBusquedaType value) {
        this.peticion = value;
    }

}
