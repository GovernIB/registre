
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para confirmarResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="confirmarResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResultadoCambioEstadoType" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}resultadoCambioEstadoType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "confirmarResponse", namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", propOrder = {
    "resultadoCambioEstadoType"
})
public class ConfirmarResponse {

    @XmlElement(name = "ResultadoCambioEstadoType", namespace = "")
    protected ResultadoCambioEstadoType resultadoCambioEstadoType;

    /**
     * Obtiene el valor de la propiedad resultadoCambioEstadoType.
     * 
     * @return
     *     possible object is
     *     {@link ResultadoCambioEstadoType }
     *     
     */
    public ResultadoCambioEstadoType getResultadoCambioEstadoType() {
        return resultadoCambioEstadoType;
    }

    /**
     * Define el valor de la propiedad resultadoCambioEstadoType.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultadoCambioEstadoType }
     *     
     */
    public void setResultadoCambioEstadoType(ResultadoCambioEstadoType value) {
        this.resultadoCambioEstadoType = value;
    }

}
