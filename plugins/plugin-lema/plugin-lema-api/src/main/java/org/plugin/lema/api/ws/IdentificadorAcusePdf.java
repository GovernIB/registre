
package org.plugin.lema.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para IdentificadorAcusePdf complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="IdentificadorAcusePdf">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;choice>
 *           &lt;element name="referencia" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *           &lt;element name="csvResguardo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IdentificadorAcusePdf", namespace = "http://administracion.gob.es/punto-unico-notificaciones/consultaAcusePdf", propOrder = {
    "referencia",
    "csvResguardo"
})
public class IdentificadorAcusePdf {

    protected byte[] referencia;
    protected String csvResguardo;

    /**
     * Obtiene el valor de la propiedad referencia.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getReferencia() {
        return referencia;
    }

    /**
     * Define el valor de la propiedad referencia.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setReferencia(byte[] value) {
        this.referencia = value;
    }

    /**
     * Obtiene el valor de la propiedad csvResguardo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCsvResguardo() {
        return csvResguardo;
    }

    /**
     * Define el valor de la propiedad csvResguardo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCsvResguardo(String value) {
        this.csvResguardo = value;
    }

}
