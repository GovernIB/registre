
package org.plugin.lema.api.realizadas.ws;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para LocalizaRealizadas complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="LocalizaRealizadas">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nifTitular" type="{http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas}Nif" minOccurs="0"/>
 *         &lt;element name="nifDestinatario" type="{http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas}Nif" minOccurs="0"/>
 *         &lt;element name="codigoDestino" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaDesde" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaHasta" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="tipoEnvio" type="{http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas}TipoEnvio" minOccurs="0"/>
 *         &lt;element name="pagina" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="opcionesLocalizaRealizadas" type="{http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas}Opciones" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LocalizaRealizadas", namespace = "http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas", propOrder = {
    "nifTitular",
    "nifDestinatario",
    "codigoDestino",
    "fechaDesde",
    "fechaHasta",
    "tipoEnvio",
    "pagina",
    "opcionesLocalizaRealizadas"
})
public class LocalizaRealizadas {

    protected String nifTitular;
    protected String nifDestinatario;
    protected String codigoDestino;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaDesde;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaHasta;
    protected String tipoEnvio;
    protected BigInteger pagina;
    protected Opciones opcionesLocalizaRealizadas;

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
     *     {@link String }
     *     
     */
    public String getTipoEnvio() {
        return tipoEnvio;
    }

    /**
     * Define el valor de la propiedad tipoEnvio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoEnvio(String value) {
        this.tipoEnvio = value;
    }

    /**
     * Obtiene el valor de la propiedad pagina.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPagina() {
        return pagina;
    }

    /**
     * Define el valor de la propiedad pagina.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPagina(BigInteger value) {
        this.pagina = value;
    }

    /**
     * Obtiene el valor de la propiedad opcionesLocalizaRealizadas.
     * 
     * @return
     *     possible object is
     *     {@link Opciones }
     *     
     */
    public Opciones getOpcionesLocalizaRealizadas() {
        return opcionesLocalizaRealizadas;
    }

    /**
     * Define el valor de la propiedad opcionesLocalizaRealizadas.
     * 
     * @param value
     *     allowed object is
     *     {@link Opciones }
     *     
     */
    public void setOpcionesLocalizaRealizadas(Opciones value) {
        this.opcionesLocalizaRealizadas = value;
    }

}
