
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for registroSalidaResponseWs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="registroSalidaResponseWs">
 *   &lt;complexContent>
 *     &lt;extension base="{http://impl.v3.ws.regweb3.caib.es/}registroResponseWs">
 *       &lt;sequence>
 *         &lt;element name="origenCodigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="origenDenominacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registroSalidaResponseWs", propOrder = {
    "origenCodigo",
    "origenDenominacion"
})
public class RegistroSalidaResponseWs
    extends RegistroResponseWs
{

    protected String origenCodigo;
    protected String origenDenominacion;

    /**
     * Gets the value of the origenCodigo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigenCodigo() {
        return origenCodigo;
    }

    /**
     * Sets the value of the origenCodigo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigenCodigo(String value) {
        this.origenCodigo = value;
    }

    /**
     * Gets the value of the origenDenominacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigenDenominacion() {
        return origenDenominacion;
    }

    /**
     * Sets the value of the origenDenominacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigenDenominacion(String value) {
        this.origenDenominacion = value;
    }

}
