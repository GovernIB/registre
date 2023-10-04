
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para nuevoRegistroSalida complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="nuevoRegistroSalida"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="entidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="registroSalidaWs" type="{http://impl.v3.ws.regweb3.caib.es/}registroSalidaWs" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nuevoRegistroSalida", propOrder = {
    "entidad",
    "registroSalidaWs"
})
public class NuevoRegistroSalida {

    protected String entidad;
    protected RegistroSalidaWs registroSalidaWs;

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
     * Obtiene el valor de la propiedad registroSalidaWs.
     * 
     * @return
     *     possible object is
     *     {@link RegistroSalidaWs }
     *     
     */
    public RegistroSalidaWs getRegistroSalidaWs() {
        return registroSalidaWs;
    }

    /**
     * Define el valor de la propiedad registroSalidaWs.
     * 
     * @param value
     *     allowed object is
     *     {@link RegistroSalidaWs }
     *     
     */
    public void setRegistroSalidaWs(RegistroSalidaWs value) {
        this.registroSalidaWs = value;
    }

}
