
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para WsI18NError complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="WsI18NError">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="translation" type="{http://impl.v3.ws.regweb3.caib.es/}wsI18NTranslation"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WsI18NError", propOrder = {
    "translation"
})
public class WsI18NError {

    @XmlElement(required = true, nillable = true)
    protected WsI18NTranslation translation;

    /**
     * Obtiene el valor de la propiedad translation.
     * 
     * @return
     *     possible object is
     *     {@link WsI18NTranslation }
     *     
     */
    public WsI18NTranslation getTranslation() {
        return translation;
    }

    /**
     * Define el valor de la propiedad translation.
     * 
     * @param value
     *     allowed object is
     *     {@link WsI18NTranslation }
     *     
     */
    public void setTranslation(WsI18NTranslation value) {
        this.translation = value;
    }

}
