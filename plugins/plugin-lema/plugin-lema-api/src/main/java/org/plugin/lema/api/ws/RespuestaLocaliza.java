
package org.plugin.lema.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para RespuestaLocaliza complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="RespuestaLocaliza">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoRespuesta" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descripcionRespuesta" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nifPeticion">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="9"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="envios" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaLocaliza}Envios" minOccurs="0"/>
 *         &lt;element name="hayMasResultados" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="opcionesRespuestaLocaliza" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaLocaliza}Opciones" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RespuestaLocaliza", namespace = "http://administracion.gob.es/punto-unico-notificaciones/respuestaLocaliza", propOrder = {
    "codigoRespuesta",
    "descripcionRespuesta",
    "nifPeticion",
    "envios",
    "hayMasResultados",
    "opcionesRespuestaLocaliza"
})
public class RespuestaLocaliza {

    @XmlElement(required = true)
    protected String codigoRespuesta;
    @XmlElement(required = true)
    protected String descripcionRespuesta;
    @XmlElement(required = true)
    protected String nifPeticion;
    protected Envios envios;
    protected boolean hayMasResultados;
    protected Opciones8 opcionesRespuestaLocaliza;

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
     * Obtiene el valor de la propiedad nifPeticion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNifPeticion() {
        return nifPeticion;
    }

    /**
     * Define el valor de la propiedad nifPeticion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNifPeticion(String value) {
        this.nifPeticion = value;
    }

    /**
     * Obtiene el valor de la propiedad envios.
     * 
     * @return
     *     possible object is
     *     {@link Envios }
     *     
     */
    public Envios getEnvios() {
        return envios;
    }

    /**
     * Define el valor de la propiedad envios.
     * 
     * @param value
     *     allowed object is
     *     {@link Envios }
     *     
     */
    public void setEnvios(Envios value) {
        this.envios = value;
    }

    /**
     * Obtiene el valor de la propiedad hayMasResultados.
     * 
     */
    public boolean isHayMasResultados() {
        return hayMasResultados;
    }

    /**
     * Define el valor de la propiedad hayMasResultados.
     * 
     */
    public void setHayMasResultados(boolean value) {
        this.hayMasResultados = value;
    }

    /**
     * Obtiene el valor de la propiedad opcionesRespuestaLocaliza.
     * 
     * @return
     *     possible object is
     *     {@link Opciones8 }
     *     
     */
    public Opciones8 getOpcionesRespuestaLocaliza() {
        return opcionesRespuestaLocaliza;
    }

    /**
     * Define el valor de la propiedad opcionesRespuestaLocaliza.
     * 
     * @param value
     *     allowed object is
     *     {@link Opciones8 }
     *     
     */
    public void setOpcionesRespuestaLocaliza(Opciones8 value) {
        this.opcionesRespuestaLocaliza = value;
    }

}
