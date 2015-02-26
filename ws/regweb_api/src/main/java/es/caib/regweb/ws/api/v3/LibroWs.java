
package es.caib.regweb.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for libroWs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="libroWs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoLibro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoOrganismo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreCorto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreLargo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "libroWs", propOrder = {
    "codigoLibro",
    "codigoOrganismo",
    "nombreCorto",
    "nombreLargo"
})
public class LibroWs {

    protected String codigoLibro;
    protected String codigoOrganismo;
    protected String nombreCorto;
    protected String nombreLargo;

    /**
     * Gets the value of the codigoLibro property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoLibro() {
        return codigoLibro;
    }

    /**
     * Sets the value of the codigoLibro property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoLibro(String value) {
        this.codigoLibro = value;
    }

    /**
     * Gets the value of the codigoOrganismo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoOrganismo() {
        return codigoOrganismo;
    }

    /**
     * Sets the value of the codigoOrganismo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoOrganismo(String value) {
        this.codigoOrganismo = value;
    }

    /**
     * Gets the value of the nombreCorto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreCorto() {
        return nombreCorto;
    }

    /**
     * Sets the value of the nombreCorto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreCorto(String value) {
        this.nombreCorto = value;
    }

    /**
     * Gets the value of the nombreLargo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreLargo() {
        return nombreLargo;
    }

    /**
     * Sets the value of the nombreLargo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreLargo(String value) {
        this.nombreLargo = value;
    }

}
