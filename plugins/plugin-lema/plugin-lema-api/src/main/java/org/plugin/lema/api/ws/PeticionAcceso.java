
package org.plugin.lema.api.ws;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para PeticionAcceso complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="PeticionAcceso">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identificador" type="{http://administracion.gob.es/punto-unico-notificaciones/peticionAcceso}Identificador"/>
 *         &lt;element name="codigoOrigen" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="nifReceptor" type="{http://administracion.gob.es/punto-unico-notificaciones/peticionAcceso}Nif"/>
 *         &lt;element name="nombreReceptor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="evento" type="{http://administracion.gob.es/punto-unico-notificaciones/peticionAcceso}Evento"/>
 *         &lt;element name="concepto" type="{http://administracion.gob.es/punto-unico-notificaciones/peticionAcceso}Concepto"/>
 *         &lt;element name="opcionesPeticionAcceso" type="{http://administracion.gob.es/punto-unico-notificaciones/peticionAcceso}Opciones" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PeticionAcceso", namespace = "http://administracion.gob.es/punto-unico-notificaciones/peticionAcceso", propOrder = {
    "identificador",
    "codigoOrigen",
    "nifReceptor",
    "nombreReceptor",
    "evento",
    "concepto",
    "opcionesPeticionAcceso"
})
public class PeticionAcceso {

    @XmlElement(required = true)
    protected String identificador;
    @XmlElement(required = true)
    protected BigInteger codigoOrigen;
    @XmlElement(required = true)
    protected String nifReceptor;
    @XmlElement(required = true)
    protected String nombreReceptor;
    @XmlElement(required = true)
    protected String evento;
    @XmlElement(required = true)
    protected String concepto;
    protected Opciones7 opcionesPeticionAcceso;

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
     * Obtiene el valor de la propiedad nifReceptor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNifReceptor() {
        return nifReceptor;
    }

    /**
     * Define el valor de la propiedad nifReceptor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNifReceptor(String value) {
        this.nifReceptor = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreReceptor.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreReceptor() {
        return nombreReceptor;
    }

    /**
     * Define el valor de la propiedad nombreReceptor.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreReceptor(String value) {
        this.nombreReceptor = value;
    }

    /**
     * Obtiene el valor de la propiedad evento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEvento() {
        return evento;
    }

    /**
     * Define el valor de la propiedad evento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEvento(String value) {
        this.evento = value;
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
     * Obtiene el valor de la propiedad opcionesPeticionAcceso.
     * 
     * @return
     *     possible object is
     *     {@link Opciones7 }
     *     
     */
    public Opciones7 getOpcionesPeticionAcceso() {
        return opcionesPeticionAcceso;
    }

    /**
     * Define el valor de la propiedad opcionesPeticionAcceso.
     * 
     * @param value
     *     allowed object is
     *     {@link Opciones7 }
     *     
     */
    public void setOpcionesPeticionAcceso(Opciones7 value) {
        this.opcionesPeticionAcceso = value;
    }

}
