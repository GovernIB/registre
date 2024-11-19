
package org.plugin.lema.api.realizadas.ws;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ConsultaRealizadas complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ConsultaRealizadas">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identificador" type="{http://administracion.gob.es/punto-unico-notificaciones/consultaRealizadas}Identificador"/>
 *         &lt;element name="codigoOrigen" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="nifPeticion" type="{http://administracion.gob.es/punto-unico-notificaciones/consultaRealizadas}Nif"/>
 *         &lt;element name="nombrePeticion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="concepto" type="{http://administracion.gob.es/punto-unico-notificaciones/consultaRealizadas}Concepto"/>
 *         &lt;element name="opcionesConsultaRealizadas" type="{http://administracion.gob.es/punto-unico-notificaciones/consultaRealizadas}Opciones" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConsultaRealizadas", propOrder = {
    "identificador",
    "codigoOrigen",
    "nifPeticion",
    "nombrePeticion",
    "concepto",
    "opcionesConsultaRealizadas"
})
public class ConsultaRealizadas {

    @XmlElement(required = true)
    protected String identificador;
    @XmlElement(required = true)
    protected BigInteger codigoOrigen;
    @XmlElement(required = true)
    protected String nifPeticion;
    @XmlElement(required = true)
    protected String nombrePeticion;
    @XmlElement(required = true)
    protected String concepto;
    protected Opciones2 opcionesConsultaRealizadas;

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
     * Obtiene el valor de la propiedad nombrePeticion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombrePeticion() {
        return nombrePeticion;
    }

    /**
     * Define el valor de la propiedad nombrePeticion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombrePeticion(String value) {
        this.nombrePeticion = value;
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
     * Obtiene el valor de la propiedad opcionesConsultaRealizadas.
     * 
     * @return
     *     possible object is
     *     {@link Opciones2 }
     *     
     */
    public Opciones2 getOpcionesConsultaRealizadas() {
        return opcionesConsultaRealizadas;
    }

    /**
     * Define el valor de la propiedad opcionesConsultaRealizadas.
     * 
     * @param value
     *     allowed object is
     *     {@link Opciones2 }
     *     
     */
    public void setOpcionesConsultaRealizadas(Opciones2 value) {
        this.opcionesConsultaRealizadas = value;
    }

}
