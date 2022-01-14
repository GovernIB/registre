
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para obtenerAsientosCiudadano complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="obtenerAsientosCiudadano">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="entidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="documento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pageNumber" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "obtenerAsientosCiudadano", propOrder = {
    "entidad",
    "documento",
    "pageNumber"
})
public class ObtenerAsientosCiudadano {

    protected String entidad;
    protected String documento;
    protected Integer pageNumber;

    /**
     * Obtiene el valor de la propiedad entidad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidad() {
        return entidad;
    }

    /**
     * Define el valor de la propiedad entidad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidad(String value) {
        this.entidad = value;
    }

    /**
     * Obtiene el valor de la propiedad documento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumento() {
        return documento;
    }

    /**
     * Define el valor de la propiedad documento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumento(String value) {
        this.documento = value;
    }

    /**
     * Obtiene el valor de la propiedad pageNumber.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPageNumber() {
        return pageNumber;
    }

    /**
     * Define el valor de la propiedad pageNumber.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPageNumber(Integer value) {
        this.pageNumber = value;
    }

}
