
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for altaRegistroSalida complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="altaRegistroSalida">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="entidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="registroSalidaWs" type="{http://impl.v3.ws.regweb3.caib.es/}registroSalidaWs" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "altaRegistroSalida", propOrder = {
    "entidad",
    "registroSalidaWs"
})
public class AltaRegistroSalida {

    protected String entidad;
    protected RegistroSalidaWs registroSalidaWs;

    /**
     * Gets the value of the entidad property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidad() {
        return entidad;
    }

    /**
     * Sets the value of the entidad property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidad(String value) {
        this.entidad = value;
    }

    /**
     * Gets the value of the registroSalidaWs property.
     * 
     * @return
     *     possible object is
     *     {@link RegistroSalidaWs }
     *     
     */
    public RegistroSalidaWs getRegistroSalidaWs() {
        return registroSalidaWs;
    }

    /**
     * Sets the value of the registroSalidaWs property.
     * 
     * @param value
     *     allowed object is
     *     {@link RegistroSalidaWs }
     *     
     */
    public void setRegistroSalidaWs(RegistroSalidaWs value) {
        this.registroSalidaWs = value;
    }

}
