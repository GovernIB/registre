
package org.plugin.lema.api.realizadas.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para RespuestaConsultaRealizadas complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="RespuestaConsultaRealizadas">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoRespuesta" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="descripcionRespuesta" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="identificador" type="{http://administracion.gob.es/punto-unico-notificaciones/consultaRealizadas}Identificador"/>
 *         &lt;element name="codigoOrigen" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="postal" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="codigoProcedimiento" type="{http://administracion.gob.es/punto-unico-notificaciones/consultaRealizadas}Sia" minOccurs="0"/>
 *         &lt;element name="fechaUltimoEstado" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="documento" type="{http://administracion.gob.es/punto-unico-notificaciones/consultaRealizadas}DetalleDocumento" minOccurs="0"/>
 *         &lt;element name="anexos" type="{http://administracion.gob.es/punto-unico-notificaciones/consultaRealizadas}Anexos" minOccurs="0"/>
 *         &lt;element name="opcionesRespuestaConsultaRealizadas" type="{http://administracion.gob.es/punto-unico-notificaciones/consultaRealizadas}Opciones" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RespuestaConsultaRealizadas", propOrder = {
    "codigoRespuesta",
    "descripcionRespuesta",
    "identificador",
    "codigoOrigen",
    "postal",
    "codigoProcedimiento",
    "fechaUltimoEstado",
    "documento",
    "anexos",
    "opcionesRespuestaConsultaRealizadas"
})
public class RespuestaConsultaRealizadas {

    @XmlElement(required = true)
    protected String codigoRespuesta;
    @XmlElement(required = true)
    protected String descripcionRespuesta;
    @XmlElement(required = true)
    protected String identificador;
    @XmlElement(required = true)
    protected String codigoOrigen;
    protected boolean postal;
    protected Sia2 codigoProcedimiento;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaUltimoEstado;
    protected DetalleDocumento documento;
    protected Anexos anexos;
    protected Opciones2 opcionesRespuestaConsultaRealizadas;

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
     *     {@link Sia2 }
     *     
     */
    public Sia2 getCodigoProcedimiento() {
        return codigoProcedimiento;
    }

    /**
     * Define el valor de la propiedad codigoProcedimiento.
     * 
     * @param value
     *     allowed object is
     *     {@link Sia2 }
     *     
     */
    public void setCodigoProcedimiento(Sia2 value) {
        this.codigoProcedimiento = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaUltimoEstado.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaUltimoEstado() {
        return fechaUltimoEstado;
    }

    /**
     * Define el valor de la propiedad fechaUltimoEstado.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaUltimoEstado(XMLGregorianCalendar value) {
        this.fechaUltimoEstado = value;
    }

    /**
     * Obtiene el valor de la propiedad documento.
     * 
     * @return
     *     possible object is
     *     {@link DetalleDocumento }
     *     
     */
    public DetalleDocumento getDocumento() {
        return documento;
    }

    /**
     * Define el valor de la propiedad documento.
     * 
     * @param value
     *     allowed object is
     *     {@link DetalleDocumento }
     *     
     */
    public void setDocumento(DetalleDocumento value) {
        this.documento = value;
    }

    /**
     * Obtiene el valor de la propiedad anexos.
     * 
     * @return
     *     possible object is
     *     {@link Anexos }
     *     
     */
    public Anexos getAnexos() {
        return anexos;
    }

    /**
     * Define el valor de la propiedad anexos.
     * 
     * @param value
     *     allowed object is
     *     {@link Anexos }
     *     
     */
    public void setAnexos(Anexos value) {
        this.anexos = value;
    }

    /**
     * Obtiene el valor de la propiedad opcionesRespuestaConsultaRealizadas.
     * 
     * @return
     *     possible object is
     *     {@link Opciones2 }
     *     
     */
    public Opciones2 getOpcionesRespuestaConsultaRealizadas() {
        return opcionesRespuestaConsultaRealizadas;
    }

    /**
     * Define el valor de la propiedad opcionesRespuestaConsultaRealizadas.
     * 
     * @param value
     *     allowed object is
     *     {@link Opciones2 }
     *     
     */
    public void setOpcionesRespuestaConsultaRealizadas(Opciones2 value) {
        this.opcionesRespuestaConsultaRealizadas = value;
    }

}
