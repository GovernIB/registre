
package org.plugin.lema.api.ws;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para Envio complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Envio">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identificador" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="codigoOrigen" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="concepto">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;whiteSpace value="preserve"/>
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="descripcion" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;whiteSpace value="preserve"/>
 *               &lt;maxLength value="1000"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="organismoEmisor" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaLocaliza}Organismo"/>
 *         &lt;element name="organismoEmisorRaiz" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaLocaliza}Organismo"/>
 *         &lt;element name="fechaPuestaDisposicion" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="tipoEnvio" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaLocaliza}TipoEnvio"/>
 *         &lt;element name="vinculo" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaLocaliza}Vinculo"/>
 *         &lt;element name="titular" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaLocaliza}Persona"/>
 *         &lt;element name="metadatosPublicos" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;whiteSpace value="preserve"/>
 *               &lt;maxLength value="255"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="opcionesEnvio" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaLocaliza}Opciones" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Envio", namespace = "http://administracion.gob.es/punto-unico-notificaciones/respuestaLocaliza", propOrder = {
    "identificador",
    "codigoOrigen",
    "concepto",
    "descripcion",
    "organismoEmisor",
    "organismoEmisorRaiz",
    "fechaPuestaDisposicion",
    "tipoEnvio",
    "vinculo",
    "titular",
    "metadatosPublicos",
    "opcionesEnvio"
})
public class Envio {

    @XmlElement(required = true)
    protected String identificador;
    @XmlElement(required = true)
    protected BigInteger codigoOrigen;
    @XmlElement(required = true)
    protected String concepto;
    protected String descripcion;
    @XmlElement(required = true)
    protected Organismo organismoEmisor;
    @XmlElement(required = true)
    protected Organismo organismoEmisorRaiz;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaPuestaDisposicion;
    @XmlElement(required = true)
    protected BigInteger tipoEnvio;
    @XmlElement(required = true)
    protected BigInteger vinculo;
    @XmlElement(required = true)
    protected Persona titular;
    protected String metadatosPublicos;
    protected Opciones8 opcionesEnvio;

    /**
     * Obtiene el valor de la propiedad identificador.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificador() {
        return identificador;
    }

    /**
     * Define el valor de la propiedad identificador.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificador(String value) {
        this.identificador = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoOrigen.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getCodigoOrigen() {
        return codigoOrigen;
    }

    /**
     * Define el valor de la propiedad codigoOrigen.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setCodigoOrigen(BigInteger value) {
        this.codigoOrigen = value;
    }

    /**
     * Obtiene el valor de la propiedad concepto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConcepto() {
        return concepto;
    }

    /**
     * Define el valor de la propiedad concepto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConcepto(String value) {
        this.concepto = value;
    }

    /**
     * Obtiene el valor de la propiedad descripcion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Define el valor de la propiedad descripcion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcion(String value) {
        this.descripcion = value;
    }

    /**
     * Obtiene el valor de la propiedad organismoEmisor.
     * 
     * @return
     *     possible object is
     *     {@link Organismo }
     *     
     */
    public Organismo getOrganismoEmisor() {
        return organismoEmisor;
    }

    /**
     * Define el valor de la propiedad organismoEmisor.
     * 
     * @param value
     *     allowed object is
     *     {@link Organismo }
     *     
     */
    public void setOrganismoEmisor(Organismo value) {
        this.organismoEmisor = value;
    }

    /**
     * Obtiene el valor de la propiedad organismoEmisorRaiz.
     * 
     * @return
     *     possible object is
     *     {@link Organismo }
     *     
     */
    public Organismo getOrganismoEmisorRaiz() {
        return organismoEmisorRaiz;
    }

    /**
     * Define el valor de la propiedad organismoEmisorRaiz.
     * 
     * @param value
     *     allowed object is
     *     {@link Organismo }
     *     
     */
    public void setOrganismoEmisorRaiz(Organismo value) {
        this.organismoEmisorRaiz = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaPuestaDisposicion.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaPuestaDisposicion() {
        return fechaPuestaDisposicion;
    }

    /**
     * Define el valor de la propiedad fechaPuestaDisposicion.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaPuestaDisposicion(XMLGregorianCalendar value) {
        this.fechaPuestaDisposicion = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoEnvio.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getTipoEnvio() {
        return tipoEnvio;
    }

    /**
     * Define el valor de la propiedad tipoEnvio.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setTipoEnvio(BigInteger value) {
        this.tipoEnvio = value;
    }

    /**
     * Obtiene el valor de la propiedad vinculo.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getVinculo() {
        return vinculo;
    }

    /**
     * Define el valor de la propiedad vinculo.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setVinculo(BigInteger value) {
        this.vinculo = value;
    }

    /**
     * Obtiene el valor de la propiedad titular.
     * 
     * @return
     *     possible object is
     *     {@link Persona }
     *     
     */
    public Persona getTitular() {
        return titular;
    }

    /**
     * Define el valor de la propiedad titular.
     * 
     * @param value
     *     allowed object is
     *     {@link Persona }
     *     
     */
    public void setTitular(Persona value) {
        this.titular = value;
    }

    /**
     * Obtiene el valor de la propiedad metadatosPublicos.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMetadatosPublicos() {
        return metadatosPublicos;
    }

    /**
     * Define el valor de la propiedad metadatosPublicos.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMetadatosPublicos(String value) {
        this.metadatosPublicos = value;
    }

    /**
     * Obtiene el valor de la propiedad opcionesEnvio.
     * 
     * @return
     *     possible object is
     *     {@link Opciones8 }
     *     
     */
    public Opciones8 getOpcionesEnvio() {
        return opcionesEnvio;
    }

    /**
     * Define el valor de la propiedad opcionesEnvio.
     * 
     * @param value
     *     allowed object is
     *     {@link Opciones8 }
     *     
     */
    public void setOpcionesEnvio(Opciones8 value) {
        this.opcionesEnvio = value;
    }

}
