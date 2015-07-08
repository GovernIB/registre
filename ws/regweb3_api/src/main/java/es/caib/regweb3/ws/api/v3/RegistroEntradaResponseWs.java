
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for registroEntradaResponseWs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="registroEntradaResponseWs">
 *   &lt;complexContent>
 *     &lt;extension base="{http://impl.v3.ws.regweb3.caib.es/}registroResponseWs">
 *       &lt;sequence>
 *         &lt;element name="destinoCodigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="destinoDenominacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registroEntradaResponseWs", propOrder = {
    "destinoCodigo",
    "destinoDenominacion"
})
public class RegistroEntradaResponseWs
    extends RegistroResponseWs
{

    protected String destinoCodigo;
    protected String destinoDenominacion;

    /**
     * Gets the value of the destinoCodigo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinoCodigo() {
        return destinoCodigo;
    }

    /**
     * Sets the value of the destinoCodigo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinoCodigo(String value) {
        this.destinoCodigo = value;
    }

    /**
     * Gets the value of the destinoDenominacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinoDenominacion() {
        return destinoDenominacion;
    }

    /**
     * Sets the value of the destinoDenominacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinoDenominacion(String value) {
        this.destinoDenominacion = value;
    }

}
