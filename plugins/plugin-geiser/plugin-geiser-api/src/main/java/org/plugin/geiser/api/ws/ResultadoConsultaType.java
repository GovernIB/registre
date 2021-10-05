
package org.plugin.geiser.api.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para resultadoConsultaType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="resultadoConsultaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="respuesta" type="{http://types.core.ws.rgeco.geiser.minhap.gob.es/}respuestaType"/>
 *         &lt;element name="apuntes" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}apunteRegistroType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resultadoConsultaType", propOrder = {
    "respuesta",
    "apuntes"
})
public class ResultadoConsultaType {

    @XmlElement(required = true)
    protected RespuestaType respuesta;
    protected List<ApunteRegistroType> apuntes;

    /**
     * Obtiene el valor de la propiedad respuesta.
     * 
     * @return
     *     possible object is
     *     {@link RespuestaType }
     *     
     */
    public RespuestaType getRespuesta() {
        return respuesta;
    }

    /**
     * Define el valor de la propiedad respuesta.
     * 
     * @param value
     *     allowed object is
     *     {@link RespuestaType }
     *     
     */
    public void setRespuesta(RespuestaType value) {
        this.respuesta = value;
    }

    /**
     * Gets the value of the apuntes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the apuntes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getApuntes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ApunteRegistroType }
     * 
     * 
     */
    public List<ApunteRegistroType> getApuntes() {
        if (apuntes == null) {
            apuntes = new ArrayList<ApunteRegistroType>();
        }
        return this.apuntes;
    }

}
