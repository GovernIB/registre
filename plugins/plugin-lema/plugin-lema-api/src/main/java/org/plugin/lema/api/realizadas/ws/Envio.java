
package org.plugin.lema.api.realizadas.ws;

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
 *         &lt;element name="codigoOrigen" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
 *         &lt;element name="organismoEmisor" type="{http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas}Organismo"/>
 *         &lt;element name="organismoEmisorRaiz" type="{http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas}Organismo"/>
 *         &lt;element name="fechaPuestaDisposicion" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="tipoEnvio" type="{http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas}TipoEnvio"/>
 *         &lt;element name="postal" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="codigoProcedimiento" type="{http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas}Sia" minOccurs="0"/>
 *         &lt;element name="estado" type="{http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas}Estado"/>
 *         &lt;element name="vinculo" type="{http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas}Vinculo"/>
 *         &lt;element name="titular" type="{http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas}Persona"/>
 *         &lt;element name="receptor" type="{http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas}Receptor" minOccurs="0"/>
 *         &lt;element name="metadatosPublicos" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;whiteSpace value="preserve"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="referenciaPdfAcuse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="csvResguardo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="opcionesEnvio" type="{http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas}Opciones" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Envio", namespace = "http://administracion.gob.es/punto-unico-notificaciones/localizaRealizadas", propOrder = {
    "identificador",
    "codigoOrigen",
    "concepto",
    "descripcion",
    "organismoEmisor",
    "organismoEmisorRaiz",
    "fechaPuestaDisposicion",
    "tipoEnvio",
    "postal",
    "codigoProcedimiento",
    "estado",
    "vinculo",
    "titular",
    "receptor",
    "metadatosPublicos",
    "referenciaPdfAcuse",
    "csvResguardo",
    "opcionesEnvio"
})
public class Envio {

    @XmlElement(required = true)
    protected String identificador;
    @XmlElement(required = true)
    protected String codigoOrigen;
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
    protected String tipoEnvio;
    protected boolean postal;
    protected Sia codigoProcedimiento;
    @XmlElement(required = true)
    protected Estado estado;
    @XmlElement(required = true)
    protected String vinculo;
    @XmlElement(required = true)
    protected Persona titular;
    protected Receptor receptor;
    protected String metadatosPublicos;
    protected String referenciaPdfAcuse;
    protected String csvResguardo;
    protected Opciones opcionesEnvio;

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
     *     {@link String }
     *     
     */
    public String getCodigoOrigen() {
        return codigoOrigen;
    }

    /**
     * Define el valor de la propiedad codigoOrigen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoOrigen(String value) {
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
     * Obtiene el valor de la propiedad postal.
     * 
     */
    public boolean isPostal() {
        return postal;
    }

    /**
     * Define el valor de la propiedad postal.
     * 
     */
    public void setPostal(boolean value) {
        this.postal = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoProcedimiento.
     * 
     * @return
     *     possible object is
     *     {@link Sia }
     *     
     */
    public Sia getCodigoProcedimiento() {
        return codigoProcedimiento;
    }

    /**
     * Define el valor de la propiedad codigoProcedimiento.
     * 
     * @param value
     *     allowed object is
     *     {@link Sia }
     *     
     */
    public void setCodigoProcedimiento(Sia value) {
        this.codigoProcedimiento = value;
    }

    /**
     * Obtiene el valor de la propiedad estado.
     * 
     * @return
     *     possible object is
     *     {@link Estado }
     *     
     */
    public Estado getEstado() {
        return estado;
    }

    /**
     * Define el valor de la propiedad estado.
     * 
     * @param value
     *     allowed object is
     *     {@link Estado }
     *     
     */
    public void setEstado(Estado value) {
        this.estado = value;
    }

    /**
     * Obtiene el valor de la propiedad vinculo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVinculo() {
        return vinculo;
    }

    /**
     * Define el valor de la propiedad vinculo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVinculo(String value) {
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
     * Obtiene el valor de la propiedad receptor.
     * 
     * @return
     *     possible object is
     *     {@link Receptor }
     *     
     */
    public Receptor getReceptor() {
        return receptor;
    }

    /**
     * Define el valor de la propiedad receptor.
     * 
     * @param value
     *     allowed object is
     *     {@link Receptor }
     *     
     */
    public void setReceptor(Receptor value) {
        this.receptor = value;
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
     * Obtiene el valor de la propiedad referenciaPdfAcuse.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenciaPdfAcuse() {
        return referenciaPdfAcuse;
    }

    /**
     * Define el valor de la propiedad referenciaPdfAcuse.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenciaPdfAcuse(String value) {
        this.referenciaPdfAcuse = value;
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

    /**
     * Obtiene el valor de la propiedad opcionesEnvio.
     * 
     * @return
     *     possible object is
     *     {@link Opciones }
     *     
     */
    public Opciones getOpcionesEnvio() {
        return opcionesEnvio;
    }

    /**
     * Define el valor de la propiedad opcionesEnvio.
     * 
     * @param value
     *     allowed object is
     *     {@link Opciones }
     *     
     */
    public void setOpcionesEnvio(Opciones value) {
        this.opcionesEnvio = value;
    }

}
