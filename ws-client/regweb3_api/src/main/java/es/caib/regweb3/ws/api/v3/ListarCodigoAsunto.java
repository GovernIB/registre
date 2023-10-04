
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para listarCodigoAsunto complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="listarCodigoAsunto"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="entidadCodigoDir3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codigoTipoAsunto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listarCodigoAsunto", propOrder = {
    "entidadCodigoDir3",
    "codigoTipoAsunto"
})
public class ListarCodigoAsunto {

    protected String entidadCodigoDir3;
    protected String codigoTipoAsunto;

    /**
     * Obtiene el valor de la propiedad entidadCodigoDir3.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidadCodigoDir3() {
        return entidadCodigoDir3;
    }

    /**
     * Define el valor de la propiedad entidadCodigoDir3.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidadCodigoDir3(String value) {
        this.entidadCodigoDir3 = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoTipoAsunto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoTipoAsunto() {
        return codigoTipoAsunto;
    }

    /**
     * Define el valor de la propiedad codigoTipoAsunto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoTipoAsunto(String value) {
        this.codigoTipoAsunto = value;
    }

}
