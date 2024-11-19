
package org.plugin.lema.api.ws;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ConsultaAnexos complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ConsultaAnexos">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nifReceptor" type="{http://administracion.gob.es/punto-unico-notificaciones/consultaAnexos}Nif"/>
 *         &lt;element name="identificador" type="{http://administracion.gob.es/punto-unico-notificaciones/consultaAnexos}Identificador"/>
 *         &lt;element name="codigoOrigen" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="referencia" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *         &lt;element name="opcionesConsultaAnexos" type="{http://administracion.gob.es/punto-unico-notificaciones/consultaAnexos}Opciones" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConsultaAnexos", namespace = "http://administracion.gob.es/punto-unico-notificaciones/consultaAnexos", propOrder = {
    "nifReceptor",
    "identificador",
    "codigoOrigen",
    "referencia",
    "opcionesConsultaAnexos"
})
public class ConsultaAnexos {

    @XmlElement(required = true)
    protected String nifReceptor;
    @XmlElement(required = true)
    protected String identificador;
    @XmlElement(required = true)
    protected BigInteger codigoOrigen;
    @XmlElement(required = true)
    protected byte[] referencia;
    protected Opciones3 opcionesConsultaAnexos;

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
     * Obtiene el valor de la propiedad referencia.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getReferencia() {
        return referencia;
    }

    /**
     * Define el valor de la propiedad referencia.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setReferencia(byte[] value) {
        this.referencia = value;
    }

    /**
     * Obtiene el valor de la propiedad opcionesConsultaAnexos.
     * 
     * @return
     *     possible object is
     *     {@link Opciones3 }
     *     
     */
    public Opciones3 getOpcionesConsultaAnexos() {
        return opcionesConsultaAnexos;
    }

    /**
     * Define el valor de la propiedad opcionesConsultaAnexos.
     * 
     * @param value
     *     allowed object is
     *     {@link Opciones3 }
     *     
     */
    public void setOpcionesConsultaAnexos(Opciones3 value) {
        this.opcionesConsultaAnexos = value;
    }

}
