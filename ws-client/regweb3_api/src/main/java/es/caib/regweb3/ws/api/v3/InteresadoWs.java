
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para interesadoWs complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="interesadoWs"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="interesado" type="{http://impl.v3.ws.regweb3.caib.es/}datosInteresadoWs" minOccurs="0"/&gt;
 *         &lt;element name="representante" type="{http://impl.v3.ws.regweb3.caib.es/}datosInteresadoWs" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "interesadoWs", propOrder = {
    "interesado",
    "representante"
})
public class InteresadoWs {

    protected DatosInteresadoWs interesado;
    protected DatosInteresadoWs representante;

    /**
     * Obtiene el valor de la propiedad interesado.
     * 
     * @return
     *     possible object is
     *     {@link DatosInteresadoWs }
     *     
     */
    public DatosInteresadoWs getInteresado() {
        return interesado;
    }

    /**
     * Define el valor de la propiedad interesado.
     * 
     * @param value
     *     allowed object is
     *     {@link DatosInteresadoWs }
     *     
     */
    public void setInteresado(DatosInteresadoWs value) {
        this.interesado = value;
    }

    /**
     * Obtiene el valor de la propiedad representante.
     * 
     * @return
     *     possible object is
     *     {@link DatosInteresadoWs }
     *     
     */
    public DatosInteresadoWs getRepresentante() {
        return representante;
    }

    /**
     * Define el valor de la propiedad representante.
     * 
     * @param value
     *     allowed object is
     *     {@link DatosInteresadoWs }
     *     
     */
    public void setRepresentante(DatosInteresadoWs value) {
        this.representante = value;
    }

}
