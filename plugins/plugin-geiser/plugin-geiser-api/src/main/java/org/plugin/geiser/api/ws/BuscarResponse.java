
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para buscarResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="buscarResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ResultadoBusquedaType" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}resultadoBusquedaType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "buscarResponse", namespace = "http://registro.ws.rgeco.geiser.minhap.gob.es/", propOrder = {
    "resultadoBusquedaType"
})
public class BuscarResponse {

    @XmlElement(name = "ResultadoBusquedaType", namespace = "")
    protected ResultadoBusquedaType resultadoBusquedaType;

    /**
     * Obtiene el valor de la propiedad resultadoBusquedaType.
     * 
     * @return
     *     possible object is
     *     {@link ResultadoBusquedaType }
     *     
     */
    public ResultadoBusquedaType getResultadoBusquedaType() {
        return resultadoBusquedaType;
    }

    /**
     * Define el valor de la propiedad resultadoBusquedaType.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultadoBusquedaType }
     *     
     */
    public void setResultadoBusquedaType(ResultadoBusquedaType value) {
        this.resultadoBusquedaType = value;
    }

}
