
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for justificanteWs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="justificanteWs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="justificante" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "justificanteWs", propOrder = {
    "justificante"
})
public class JustificanteWs {

    protected byte[] justificante;

    /**
     * Gets the value of the justificante property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getJustificante() {
        return justificante;
    }

    /**
     * Sets the value of the justificante property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setJustificante(byte[] value) {
        this.justificante = ((byte[]) value);
    }

}
