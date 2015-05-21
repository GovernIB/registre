
package es.caib.regweb.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for libroOficinaWs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="libroOficinaWs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="libroWs" type="{http://impl.v3.ws.regweb.caib.es/}libroWs" minOccurs="0"/>
 *         &lt;element name="oficinaWs" type="{http://impl.v3.ws.regweb.caib.es/}oficinaWs" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "libroOficinaWs", propOrder = {
    "libroWs",
    "oficinaWs"
})
public class LibroOficinaWs {

    protected LibroWs libroWs;
    protected OficinaWs oficinaWs;

    /**
     * Gets the value of the libroWs property.
     * 
     * @return
     *     possible object is
     *     {@link LibroWs }
     *     
     */
    public LibroWs getLibroWs() {
        return libroWs;
    }

    /**
     * Sets the value of the libroWs property.
     * 
     * @param value
     *     allowed object is
     *     {@link LibroWs }
     *     
     */
    public void setLibroWs(LibroWs value) {
        this.libroWs = value;
    }

    /**
     * Gets the value of the oficinaWs property.
     * 
     * @return
     *     possible object is
     *     {@link OficinaWs }
     *     
     */
    public OficinaWs getOficinaWs() {
        return oficinaWs;
    }

    /**
     * Sets the value of the oficinaWs property.
     * 
     * @param value
     *     allowed object is
     *     {@link OficinaWs }
     *     
     */
    public void setOficinaWs(OficinaWs value) {
        this.oficinaWs = value;
    }

}
