
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para consultarResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="consultarResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResultadoConsultaType" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}resultadoConsultaType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "consultarResponse", namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", propOrder = {
    "resultadoConsultaType"
})
public class ConsultarResponse {

    @XmlElement(name = "ResultadoConsultaType", namespace = "")
    protected ResultadoConsultaType resultadoConsultaType;

    /**
     * Obtiene el valor de la propiedad resultadoConsultaType.
     * 
     * @return
     *     possible object is
     *     {@link ResultadoConsultaType }
     *     
     */
    public ResultadoConsultaType getResultadoConsultaType() {
        return resultadoConsultaType;
    }

    /**
     * Define el valor de la propiedad resultadoConsultaType.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultadoConsultaType }
     *     
     */
    public void setResultadoConsultaType(ResultadoConsultaType value) {
        this.resultadoConsultaType = value;
    }

}
