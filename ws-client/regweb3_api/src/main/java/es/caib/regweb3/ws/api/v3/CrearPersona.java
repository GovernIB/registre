
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para crearPersona complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="crearPersona"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="personaWs" type="{http://impl.v3.ws.regweb3.caib.es/}personaWs" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "crearPersona", propOrder = {
    "personaWs"
})
public class CrearPersona {

    protected PersonaWs personaWs;

    /**
     * Obtiene el valor de la propiedad personaWs.
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
     * Define el valor de la propiedad personaWs.
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
