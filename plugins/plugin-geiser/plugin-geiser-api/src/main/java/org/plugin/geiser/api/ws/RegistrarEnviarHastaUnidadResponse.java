
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para registrarEnviarHastaUnidadResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="registrarEnviarHastaUnidadResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResultadoRegistroType" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}resultadoRegistroType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registrarEnviarHastaUnidadResponse", namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", propOrder = {
    "resultadoRegistroType"
})
public class RegistrarEnviarHastaUnidadResponse {

    @XmlElement(name = "ResultadoRegistroType", namespace = "")
    protected ResultadoRegistroType resultadoRegistroType;

    /**
     * Obtiene el valor de la propiedad resultadoRegistroType.
     * 
     * @return
     *     possible object is
     *     {@link ResultadoRegistroType }
     *     
     */
    public ResultadoRegistroType getResultadoRegistroType() {
        return resultadoRegistroType;
    }

    /**
     * Define el valor de la propiedad resultadoRegistroType.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultadoRegistroType }
     *     
     */
    public void setResultadoRegistroType(ResultadoRegistroType value) {
        this.resultadoRegistroType = value;
    }

}
