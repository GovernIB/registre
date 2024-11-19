
package org.plugin.lema.api.ws;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Clase Java para RespuestaPeticionAcceso complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="RespuestaPeticionAcceso">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoRespuesta" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaPeticionAcceso}Codigo"/>
 *         &lt;element name="descripcionRespuesta" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaPeticionAcceso}Descripcion"/>
 *         &lt;element name="identificador" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaPeticionAcceso}Identificador"/>
 *         &lt;element name="codigoOrigen" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="fechaEvento" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="documento" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaPeticionAcceso}DetalleDocumento" minOccurs="0"/>
 *         &lt;element name="anexos" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaPeticionAcceso}Anexos" minOccurs="0"/>
 *         &lt;element name="opcionesRespuestaPeticionAcceso" type="{http://administracion.gob.es/punto-unico-notificaciones/respuestaPeticionAcceso}Opciones" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RespuestaPeticionAcceso", propOrder = {
    "codigoRespuesta",
    "descripcionRespuesta",
    "identificador",
    "codigoOrigen",
    "fechaEvento",
    "documento",
    "anexos",
    "opcionesRespuestaPeticionAcceso"
})
public class RespuestaPeticionAcceso {

    @XmlElement(required = true)
    protected String codigoRespuesta;
    @XmlElement(required = true)
    protected String descripcionRespuesta;
    @XmlElement(required = true)
    protected String identificador;
    @XmlElement(required = true)
    protected BigInteger codigoOrigen;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar fechaEvento;
    protected DetalleDocumento documento;
    protected Anexos anexos;
    protected Opciones2 opcionesRespuestaPeticionAcceso;

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
     * Obtiene el valor de la propiedad fechaEvento.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getFechaEvento() {
        return fechaEvento;
    }

    /**
     * Define el valor de la propiedad fechaEvento.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setFechaEvento(XMLGregorianCalendar value) {
        this.fechaEvento = value;
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
     * Obtiene el valor de la propiedad opcionesRespuestaPeticionAcceso.
     * 
     * @return
     *     possible object is
     *     {@link Opciones2 }
     *     
     */
    public Opciones2 getOpcionesRespuestaPeticionAcceso() {
        return opcionesRespuestaPeticionAcceso;
    }

    /**
     * Define el valor de la propiedad opcionesRespuestaPeticionAcceso.
     * 
     * @param value
     *     allowed object is
     *     {@link Opciones2 }
     *     
     */
    public void setOpcionesRespuestaPeticionAcceso(Opciones2 value) {
        this.opcionesRespuestaPeticionAcceso = value;
    }

}
