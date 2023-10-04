
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para registroEntradaResponseWs complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="registroEntradaResponseWs"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://impl.v3.ws.regweb3.caib.es/}registroResponseWs"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="destinoCodigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="destinoDenominacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
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
     * Obtiene el valor de la propiedad destinoCodigo.
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
     * Define el valor de la propiedad destinoCodigo.
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
     * Obtiene el valor de la propiedad destinoDenominacion.
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
     * Define el valor de la propiedad destinoDenominacion.
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
