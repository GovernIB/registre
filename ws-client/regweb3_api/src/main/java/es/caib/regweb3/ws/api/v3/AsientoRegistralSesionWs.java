
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para asientoRegistralSesionWs complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="asientoRegistralSesionWs"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="asientoRegistralWs" type="{http://impl.v3.ws.regweb3.caib.es/}asientoRegistralWs" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "asientoRegistralSesionWs", propOrder = {
    "estado",
    "asientoRegistralWs"
})
public class AsientoRegistralSesionWs {

    protected Long estado;
    protected AsientoRegistralWs asientoRegistralWs;

    /**
     * Obtiene el valor de la propiedad estado.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEstado() {
        return estado;
    }

    /**
     * Define el valor de la propiedad estado.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEstado(Long value) {
        this.estado = value;
    }

    /**
     * Obtiene el valor de la propiedad asientoRegistralWs.
     * 
     * @return
     *     possible object is
     *     {@link AsientoRegistralWs }
     *     
     */
    public AsientoRegistralWs getAsientoRegistralWs() {
        return asientoRegistralWs;
    }

    /**
     * Define el valor de la propiedad asientoRegistralWs.
     * 
     * @param value
     *     allowed object is
     *     {@link AsientoRegistralWs }
     *     
     */
    public void setAsientoRegistralWs(AsientoRegistralWs value) {
        this.asientoRegistralWs = value;
    }

}
