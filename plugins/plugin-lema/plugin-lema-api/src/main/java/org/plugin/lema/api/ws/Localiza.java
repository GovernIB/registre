
package org.plugin.lema.api.ws;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para Localiza complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="Localiza">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nifTitular" type="{http://administracion.gob.es/punto-unico-notificaciones/localiza}Nif" minOccurs="0"/>
 *         &lt;element name="nifDestinatario" type="{http://administracion.gob.es/punto-unico-notificaciones/localiza}Nif" minOccurs="0"/>
 *         &lt;element name="codigoDestino" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaDesde" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaHasta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="tipoEnvio" type="{http://administracion.gob.es/punto-unico-notificaciones/localiza}TipoEnvio" minOccurs="0"/>
 *         &lt;element name="opcionesLocaliza" type="{http://administracion.gob.es/punto-unico-notificaciones/localiza}Opciones" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Localiza", namespace = "http://administracion.gob.es/punto-unico-notificaciones/localiza", propOrder = {
    "nifTitular",
    "nifDestinatario",
    "codigoDestino",
    "fechaDesde",
    "fechaHasta",
    "tipoEnvio",
    "opcionesLocaliza"
})
public class Localiza {

    protected String nifTitular;
    protected String nifDestinatario;
    protected String codigoDestino;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaDesde;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaHasta;
    protected BigInteger tipoEnvio;
    protected Opciones6 opcionesLocaliza;

    /**
     * Obtiene el valor de la propiedad nifTitular.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNifTitular() {
        return nifTitular;
    }

    /**
     * Define el valor de la propiedad nifTitular.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNifTitular(String value) {
        this.nifTitular = value;
    }

    /**
     * Obtiene el valor de la propiedad nifDestinatario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNifDestinatario() {
        return nifDestinatario;
    }

    /**
     * Define el valor de la propiedad nifDestinatario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNifDestinatario(String value) {
        this.nifDestinatario = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoDestino.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoDestino() {
        return codigoDestino;
    }

    /**
     * Define el valor de la propiedad codigoDestino.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoDestino(String value) {
        this.codigoDestino = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaDesde.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaDesde() {
        return fechaDesde;
    }

    /**
     * Define el valor de la propiedad fechaDesde.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaDesde(XMLGregorianCalendar value) {
        this.fechaDesde = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaHasta.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaHasta() {
        return fechaHasta;
    }

    /**
     * Define el valor de la propiedad fechaHasta.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaHasta(XMLGregorianCalendar value) {
        this.fechaHasta = value;
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
     * Obtiene el valor de la propiedad opcionesLocaliza.
     * 
     * @return
     *     possible object is
     *     {@link Opciones6 }
     *     
     */
    public Opciones6 getOpcionesLocaliza() {
        return opcionesLocaliza;
    }

    /**
     * Define el valor de la propiedad opcionesLocaliza.
     * 
     * @param value
     *     allowed object is
     *     {@link Opciones6 }
     *     
     */
    public void setOpcionesLocaliza(Opciones6 value) {
        this.opcionesLocaliza = value;
    }

}
