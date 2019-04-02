
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para oficioWs complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="oficioWs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="oficio" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "oficioWs", propOrder = {
    "oficio"
})
public class OficioWs {

    protected byte[] oficio;

    /**
     * Obtiene el valor de la propiedad oficio.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getOficio() {
        return oficio;
    }

    /**
     * Define el valor de la propiedad oficio.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setOficio(byte[] value) {
        this.oficio = value;
    }

}
