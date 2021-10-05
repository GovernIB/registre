
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para buscarEstadoTramitacionResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="buscarEstadoTramitacionResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResultadoBusquedaEstadoTramitacionType" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}resultadoBusquedaEstadoTramitacionType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "buscarEstadoTramitacionResponse", namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", propOrder = {
    "resultadoBusquedaEstadoTramitacionType"
})
public class BuscarEstadoTramitacionResponse {

    @XmlElement(name = "ResultadoBusquedaEstadoTramitacionType", namespace = "")
    protected ResultadoBusquedaEstadoTramitacionType resultadoBusquedaEstadoTramitacionType;

    /**
     * Obtiene el valor de la propiedad resultadoBusquedaEstadoTramitacionType.
     * 
     * @return
     *     possible object is
     *     {@link ResultadoBusquedaEstadoTramitacionType }
     *     
     */
    public ResultadoBusquedaEstadoTramitacionType getResultadoBusquedaEstadoTramitacionType() {
        return resultadoBusquedaEstadoTramitacionType;
    }

    /**
     * Define el valor de la propiedad resultadoBusquedaEstadoTramitacionType.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultadoBusquedaEstadoTramitacionType }
     *     
     */
    public void setResultadoBusquedaEstadoTramitacionType(ResultadoBusquedaEstadoTramitacionType value) {
        this.resultadoBusquedaEstadoTramitacionType = value;
    }

}
