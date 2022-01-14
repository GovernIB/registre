
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para tipoDocumentalWs complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="tipoDocumentalWs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoNTI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tipoDocumentalWs", propOrder = {
    "codigoNTI"
})
public class TipoDocumentalWs {

    protected String codigoNTI;

    /**
     * Obtiene el valor de la propiedad codigoNTI.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoNTI() {
        return codigoNTI;
    }

    /**
     * Define el valor de la propiedad codigoNTI.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoNTI(String value) {
        this.codigoNTI = value;
    }

}
