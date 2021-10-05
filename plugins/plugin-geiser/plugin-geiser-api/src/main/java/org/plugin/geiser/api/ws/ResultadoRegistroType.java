
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para resultadoRegistroType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="resultadoRegistroType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="respuesta" type="{http://types.core.ws.rgeco.geiser.minhap.gob.es/}respuestaType"/>
 *         &lt;element name="apunte" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}apunteRegistroType" minOccurs="0"/>
 *         &lt;element name="codigoAsientoSalida" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resultadoRegistroType", propOrder = {
    "respuesta",
    "apunte",
    "codigoAsientoSalida"
})
public class ResultadoRegistroType {

    @XmlElement(required = true)
    protected RespuestaType respuesta;
    protected ApunteRegistroType apunte;
    protected String codigoAsientoSalida;

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
     * Obtiene el valor de la propiedad apunte.
     * 
     * @return
     *     possible object is
     *     {@link ApunteRegistroType }
     *     
     */
    public ApunteRegistroType getApunte() {
        return apunte;
    }

    /**
     * Define el valor de la propiedad apunte.
     * 
     * @param value
     *     allowed object is
     *     {@link ApunteRegistroType }
     *     
     */
    public void setApunte(ApunteRegistroType value) {
        this.apunte = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoAsientoSalida.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoAsientoSalida() {
        return codigoAsientoSalida;
    }

    /**
     * Define el valor de la propiedad codigoAsientoSalida.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoAsientoSalida(String value) {
        this.codigoAsientoSalida = value;
    }

}
