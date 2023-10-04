
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para registroSalidaResponseWs complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="registroSalidaResponseWs"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://impl.v3.ws.regweb3.caib.es/}registroResponseWs"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="origenCodigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="origenDenominacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
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
     * Obtiene el valor de la propiedad origenCodigo.
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
     * Define el valor de la propiedad origenCodigo.
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
     * Obtiene el valor de la propiedad origenDenominacion.
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
     * Define el valor de la propiedad origenDenominacion.
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
