
package org.plugin.lema.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para RespuestaConsultaAcusePdf complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="RespuestaConsultaAcusePdf">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoRespuesta" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaConsultaAcusePdf}Codigo"/>
 *         &lt;element name="descripcionRespuesta" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaConsultaAcusePdf}Descripcion"/>
 *         &lt;element name="acusePdf" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaConsultaAcusePdf}AcusePdf" minOccurs="0"/>
 *         &lt;element name="opcionesRespuestaConsultaAcusePdf" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaConsultaAcusePdf}Opciones" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RespuestaConsultaAcusePdf", namespace = "http://administracion.gob.es/punto-unico-notificaciones/respuestaConsultaAcusePdf", propOrder = {
    "codigoRespuesta",
    "descripcionRespuesta",
    "acusePdf",
    "opcionesRespuestaConsultaAcusePdf"
})
public class RespuestaConsultaAcusePdf {

    @XmlElement(required = true)
    protected String codigoRespuesta;
    @XmlElement(required = true)
    protected String descripcionRespuesta;
    protected AcusePdf acusePdf;
    protected Opciones opcionesRespuestaConsultaAcusePdf;

    /**
     * Obtiene el valor de la propiedad codigoRespuesta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoRespuesta() {
        return codigoRespuesta;
    }

    /**
     * Define el valor de la propiedad codigoRespuesta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoRespuesta(String value) {
        this.codigoRespuesta = value;
    }

    /**
     * Obtiene el valor de la propiedad descripcionRespuesta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionRespuesta() {
        return descripcionRespuesta;
    }

    /**
     * Define el valor de la propiedad descripcionRespuesta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionRespuesta(String value) {
        this.descripcionRespuesta = value;
    }

    /**
     * Obtiene el valor de la propiedad acusePdf.
     * 
     * @return
     *     possible object is
     *     {@link AcusePdf }
     *     
     */
    public AcusePdf getAcusePdf() {
        return acusePdf;
    }

    /**
     * Define el valor de la propiedad acusePdf.
     * 
     * @param value
     *     allowed object is
     *     {@link AcusePdf }
     *     
     */
    public void setAcusePdf(AcusePdf value) {
        this.acusePdf = value;
    }

    /**
     * Obtiene el valor de la propiedad opcionesRespuestaConsultaAcusePdf.
     * 
     * @return
     *     possible object is
     *     {@link Opciones }
     *     
     */
    public Opciones getOpcionesRespuestaConsultaAcusePdf() {
        return opcionesRespuestaConsultaAcusePdf;
    }

    /**
     * Define el valor de la propiedad opcionesRespuestaConsultaAcusePdf.
     * 
     * @param value
     *     allowed object is
     *     {@link Opciones }
     *     
     */
    public void setOpcionesRespuestaConsultaAcusePdf(Opciones value) {
        this.opcionesRespuestaConsultaAcusePdf = value;
    }

}
