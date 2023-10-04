
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para nuevoRegistroEntrada complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="nuevoRegistroEntrada"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="entidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="registroEntradaWs" type="{http://impl.v3.ws.regweb3.caib.es/}registroEntradaWs" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nuevoRegistroEntrada", propOrder = {
    "entidad",
    "registroEntradaWs"
})
public class NuevoRegistroEntrada {

    protected String entidad;
    protected RegistroEntradaWs registroEntradaWs;

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
