
package es.caib.regweb.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for interesadoWs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="interesadoWs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="interesado" type="{http://registrosalida.impl.v3.ws.regweb.caib.es/}datosInteresadoWs" minOccurs="0"/>
 *         &lt;element name="representante" type="{http://registrosalida.impl.v3.ws.regweb.caib.es/}datosInteresadoWs" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "interesadoWs", propOrder = {
    "interesado",
    "representante"
})
public class InteresadoWs {

    protected DatosInteresadoWs interesado;
    protected DatosInteresadoWs representante;

    /**
     * Gets the value of the interesado property.
     * 
     * @return
     *     possible object is
     *     {@link DatosInteresadoWs }
     *     
     */
    public DatosInteresadoWs getInteresado() {
        return interesado;
    }

    /**
     * Sets the value of the interesado property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatosInteresadoWs }
     *     
     */
    public void setInteresado(DatosInteresadoWs value) {
        this.interesado = value;
    }

    /**
     * Gets the value of the representante property.
     * 
     * @return
     *     possible object is
     *     {@link DatosInteresadoWs }
     *     
     */
    public DatosInteresadoWs getRepresentante() {
        return representante;
    }

    /**
     * Sets the value of the representante property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatosInteresadoWs }
     *     
     */
    public void setRepresentante(DatosInteresadoWs value) {
        this.representante = value;
    }

}
