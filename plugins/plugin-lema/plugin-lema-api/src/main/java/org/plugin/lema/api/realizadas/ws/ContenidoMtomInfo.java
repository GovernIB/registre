
package org.plugin.lema.api.realizadas.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ContenidoMtomInfo complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ContenidoMtomInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="contenido" type="{http://administracion.gob.es/punto-unico-notificaciones/consultaRealizadas}Contenido"/>
 *         &lt;element name="tipoMIME" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContenidoMtomInfo", propOrder = {
    "contenido",
    "tipoMIME"
})
public class ContenidoMtomInfo {

    @XmlElement(required = true)
    protected Contenido contenido;
    @XmlElement(required = true)
    protected String tipoMIME;

    /**
     * Obtiene el valor de la propiedad contenido.
     * 
     * @return
     *     possible object is
     *     {@link Contenido }
     *     
     */
    public Contenido getContenido() {
        return contenido;
    }

    /**
     * Define el valor de la propiedad contenido.
     * 
     * @param value
     *     allowed object is
     *     {@link Contenido }
     *     
     */
    public void setContenido(Contenido value) {
        this.contenido = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoMIME.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoMIME() {
        return tipoMIME;
    }

    /**
     * Define el valor de la propiedad tipoMIME.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoMIME(String value) {
        this.tipoMIME = value;
    }

}
