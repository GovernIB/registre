
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para libroOficinaWs complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="libroOficinaWs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="libroWs" type="{http://impl.v3.ws.regweb3.caib.es/}libroWs" minOccurs="0"/>
 *         &lt;element name="oficinaWs" type="{http://impl.v3.ws.regweb3.caib.es/}oficinaWs" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "libroOficinaWs", propOrder = {
    "libroWs",
    "oficinaWs"
})
public class LibroOficinaWs {

    protected LibroWs libroWs;
    protected OficinaWs oficinaWs;

    /**
     * Obtiene el valor de la propiedad libroWs.
     * 
     * @return
     *     possible object is
     *     {@link LibroWs }
     *     
     */
    public LibroWs getLibroWs() {
        return libroWs;
    }

    /**
     * Define el valor de la propiedad libroWs.
     * 
     * @param value
     *     allowed object is
     *     {@link LibroWs }
     *     
     */
    public void setLibroWs(LibroWs value) {
        this.libroWs = value;
    }

    /**
     * Obtiene el valor de la propiedad oficinaWs.
     * 
     * @return
     *     possible object is
     *     {@link OficinaWs }
     *     
     */
    public OficinaWs getOficinaWs() {
        return oficinaWs;
    }

    /**
     * Define el valor de la propiedad oficinaWs.
     * 
     * @param value
     *     allowed object is
     *     {@link OficinaWs }
     *     
     */
    public void setOficinaWs(OficinaWs value) {
        this.oficinaWs = value;
    }

}
