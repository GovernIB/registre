
package org.plugin.lema.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para RespuestaConsultaAnexos complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="RespuestaConsultaAnexos">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoRespuesta" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaConsultaAnexos}Codigo"/>
 *         &lt;element name="descripcionRespuesta" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaConsultaAnexos}Descripcion"/>
 *         &lt;element name="documento" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaConsultaAnexos}DocumentoAnexo" minOccurs="0"/>
 *         &lt;element name="opcionesRespuestaConsultaAnexo" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaConsultaAnexos}Opciones" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RespuestaConsultaAnexos", namespace = "http://administracion.gob.es/punto-unico-notificaciones/respuestaConsultaAnexos", propOrder = {
    "codigoRespuesta",
    "descripcionRespuesta",
    "documento",
    "opcionesRespuestaConsultaAnexo"
})
public class RespuestaConsultaAnexos {

    @XmlElement(required = true)
    protected String codigoRespuesta;
    @XmlElement(required = true)
    protected String descripcionRespuesta;
    protected DocumentoAnexo documento;
    protected Opciones4 opcionesRespuestaConsultaAnexo;

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
     * Obtiene el valor de la propiedad documento.
     * 
     * @return
     *     possible object is
     *     {@link DocumentoAnexo }
     *     
     */
    public DocumentoAnexo getDocumento() {
        return documento;
    }

    /**
     * Define el valor de la propiedad documento.
     * 
     * @param value
     *     allowed object is
     *     {@link DocumentoAnexo }
     *     
     */
    public void setDocumento(DocumentoAnexo value) {
        this.documento = value;
    }

    /**
     * Obtiene el valor de la propiedad opcionesRespuestaConsultaAnexo.
     * 
     * @return
     *     possible object is
     *     {@link Opciones4 }
     *     
     */
    public Opciones4 getOpcionesRespuestaConsultaAnexo() {
        return opcionesRespuestaConsultaAnexo;
    }

    /**
     * Define el valor de la propiedad opcionesRespuestaConsultaAnexo.
     * 
     * @param value
     *     allowed object is
     *     {@link Opciones4 }
     *     
     */
    public void setOpcionesRespuestaConsultaAnexo(Opciones4 value) {
        this.opcionesRespuestaConsultaAnexo = value;
    }

}
