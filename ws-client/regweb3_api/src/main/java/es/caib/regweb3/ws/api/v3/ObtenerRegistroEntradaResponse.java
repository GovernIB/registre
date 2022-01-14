
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para obtenerRegistroEntradaResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="obtenerRegistroEntradaResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://impl.v3.ws.regweb3.caib.es/}registroEntradaResponseWs" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obtenerRegistroEntradaResponse", propOrder = {
    "_return"
})
public class ObtenerRegistroEntradaResponse {

    @XmlElement(name = "return")
    protected RegistroEntradaResponseWs _return;

    /**
     * Obtiene el valor de la propiedad return.
     * 
     * @return
     *     possible object is
     *     {@link RegistroEntradaResponseWs }
     *     
     */
    public RegistroEntradaResponseWs getReturn() {
        return _return;
    }

    /**
     * Define el valor de la propiedad return.
     * 
     * @param value
     *     allowed object is
     *     {@link RegistroEntradaResponseWs }
     *     
     */
    public void setReturn(RegistroEntradaResponseWs value) {
        this._return = value;
    }

}
