
package org.plugin.lema.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Anexos complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Anexos">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="anexosReferencia" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaPeticionAcceso}AnexosReferencia"/>
 *         &lt;element name="anexosUrl" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaPeticionAcceso}AnexosUrl"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Anexos", propOrder = {
    "anexosReferencia",
    "anexosUrl"
})
public class Anexos {

    protected AnexosReferencia anexosReferencia;
    protected AnexosUrl anexosUrl;

    /**
     * Obtiene el valor de la propiedad anexosReferencia.
     * 
     * @return
     *     possible object is
     *     {@link AnexosReferencia }
     *     
     */
    public AnexosReferencia getAnexosReferencia() {
        return anexosReferencia;
    }

    /**
     * Define el valor de la propiedad anexosReferencia.
     * 
     * @param value
     *     allowed object is
     *     {@link AnexosReferencia }
     *     
     */
    public void setAnexosReferencia(AnexosReferencia value) {
        this.anexosReferencia = value;
    }

    /**
     * Obtiene el valor de la propiedad anexosUrl.
     * 
     * @return
     *     possible object is
     *     {@link AnexosUrl }
     *     
     */
    public AnexosUrl getAnexosUrl() {
        return anexosUrl;
    }

    /**
     * Define el valor de la propiedad anexosUrl.
     * 
     * @param value
     *     allowed object is
     *     {@link AnexosUrl }
     *     
     */
    public void setAnexosUrl(AnexosUrl value) {
        this.anexosUrl = value;
    }

}
