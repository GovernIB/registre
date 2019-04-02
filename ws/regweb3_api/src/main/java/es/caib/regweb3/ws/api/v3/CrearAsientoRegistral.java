
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para crearAsientoRegistral complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="crearAsientoRegistral">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="entidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="asientoRegistral" type="{http://impl.v3.ws.regweb3.caib.es/}asientoRegistralWs" minOccurs="0"/>
 *         &lt;element name="tipoOperacion" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "crearAsientoRegistral", propOrder = {
    "entidad",
    "asientoRegistral",
    "tipoOperacion"
})
public class CrearAsientoRegistral {

    protected String entidad;
    protected AsientoRegistralWs_Type asientoRegistral;
    protected Long tipoOperacion;

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
     * Obtiene el valor de la propiedad asientoRegistral.
     * 
     * @return
     *     possible object is
     *     {@link AsientoRegistralWs_Type }
     *     
     */
    public AsientoRegistralWs_Type getAsientoRegistral() {
        return asientoRegistral;
    }

    /**
     * Define el valor de la propiedad asientoRegistral.
     * 
     * @param value
     *     allowed object is
     *     {@link AsientoRegistralWs_Type }
     *     
     */
    public void setAsientoRegistral(AsientoRegistralWs_Type value) {
        this.asientoRegistral = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoOperacion.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTipoOperacion() {
        return tipoOperacion;
    }

    /**
     * Define el valor de la propiedad tipoOperacion.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTipoOperacion(Long value) {
        this.tipoOperacion = value;
    }

}
