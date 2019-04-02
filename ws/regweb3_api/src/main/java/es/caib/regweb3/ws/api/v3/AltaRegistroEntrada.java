
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para altaRegistroEntrada complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="altaRegistroEntrada">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="registroEntradaWs" type="{http://impl.v3.ws.regweb3.caib.es/}registroEntradaWs" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "altaRegistroEntrada", propOrder = {
    "registroEntradaWs"
})
public class AltaRegistroEntrada {

    protected RegistroEntradaWs registroEntradaWs;

    /**
     * Obtiene el valor de la propiedad registroEntradaWs.
     * 
     * @return
     *     possible object is
     *     {@link RegistroEntradaWs }
     *     
     */
    public RegistroEntradaWs getRegistroEntradaWs() {
        return registroEntradaWs;
    }

    /**
     * Define el valor de la propiedad registroEntradaWs.
     * 
     * @param value
     *     allowed object is
     *     {@link RegistroEntradaWs }
     *     
     */
    public void setRegistroEntradaWs(RegistroEntradaWs value) {
        this.registroEntradaWs = value;
    }

}
