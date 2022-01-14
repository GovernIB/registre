
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para libroWs complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
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
     * Obtiene el valor de la propiedad codigoLibro.
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
     * Define el valor de la propiedad codigoLibro.
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
     * Obtiene el valor de la propiedad codigoOrganismo.
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
     * Define el valor de la propiedad codigoOrganismo.
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
     * Obtiene el valor de la propiedad nombreCorto.
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
     * Define el valor de la propiedad nombreCorto.
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
     * Obtiene el valor de la propiedad nombreLargo.
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
     * Define el valor de la propiedad nombreLargo.
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
