
package es.caib.regweb.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for actualizarPersona complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="actualizarPersona">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="personaWs" type="{http://personas.impl.v3.ws.regweb.caib.es/}personaWs" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "actualizarPersona", propOrder = {
    "personaWs"
})
public class ActualizarPersona {

    protected PersonaWs personaWs;

    /**
     * Gets the value of the personaWs property.
     * 
     * @return
     *     possible object is
     *     {@link PersonaWs }
     *     
     */
    public PersonaWs getPersonaWs() {
        return personaWs;
    }

    /**
     * Sets the value of the personaWs property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonaWs }
     *     
     */
    public void setPersonaWs(PersonaWs value) {
        this.personaWs = value;
    }

}
