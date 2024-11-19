
package org.plugin.lema.api.realizadas.ws;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para RespuestaLocalizaRealizadas complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="RespuestaLocalizaRealizadas">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoRespuesta" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descripcionRespuesta" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nifPeticion" type="{http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas}Nif"/>
 *         &lt;element name="envios" type="{http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas}Envios" minOccurs="0"/>
 *         &lt;element name="totalPaginas" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="paginaActual" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="opcionesRespuestaLocalizaRealizadas" type="{http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas}Opciones" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RespuestaLocalizaRealizadas", namespace = "http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas", propOrder = {
    "codigoRespuesta",
    "descripcionRespuesta",
    "nifPeticion",
    "envios",
    "totalPaginas",
    "paginaActual",
    "opcionesRespuestaLocalizaRealizadas"
})
public class RespuestaLocalizaRealizadas {

    @XmlElement(required = true)
    protected String codigoRespuesta;
    @XmlElement(required = true)
    protected String descripcionRespuesta;
    @XmlElement(required = true)
    protected String nifPeticion;
    protected Envios envios;
    @XmlElement(required = true)
    protected BigInteger totalPaginas;
    @XmlElement(required = true)
    protected BigInteger paginaActual;
    protected Opciones opcionesRespuestaLocalizaRealizadas;

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
     * Obtiene el valor de la propiedad totalPaginas.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTotalPaginas() {
        return totalPaginas;
    }

    /**
     * Define el valor de la propiedad totalPaginas.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTotalPaginas(BigInteger value) {
        this.totalPaginas = value;
    }

    /**
     * Obtiene el valor de la propiedad paginaActual.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPaginaActual() {
        return paginaActual;
    }

    /**
     * Define el valor de la propiedad paginaActual.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPaginaActual(BigInteger value) {
        this.paginaActual = value;
    }

    /**
     * Obtiene el valor de la propiedad opcionesRespuestaLocalizaRealizadas.
     * 
     * @return
     *     possible object is
     *     {@link Opciones }
     *     
     */
    public Opciones getOpcionesRespuestaLocalizaRealizadas() {
        return opcionesRespuestaLocalizaRealizadas;
    }

    /**
     * Define el valor de la propiedad opcionesRespuestaLocalizaRealizadas.
     * 
     * @param value
     *     allowed object is
     *     {@link Opciones }
     *     
     */
    public void setOpcionesRespuestaLocalizaRealizadas(Opciones value) {
        this.opcionesRespuestaLocalizaRealizadas = value;
    }

}
