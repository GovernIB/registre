
package org.plugin.lema.api.ws;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para ConsultaAcusePdf complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="ConsultaAcusePdf">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="nifReceptor" type="{http://administracion.gob.es/punto-unico-notificaciones/consultaAcusePdf}Nif" minOccurs="0"/>
 *         &lt;element name="identificador" type="{http://administracion.gob.es/punto-unico-notificaciones/consultaAcusePdf}Identificador" minOccurs="0"/>
 *         &lt;element name="codigoOrigen" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="identificadorAcusePdf" type="{http://administracion.gob.es/punto-unico-notificaciones/consultaAcusePdf}IdentificadorAcusePdf" minOccurs="0"/>
 *         &lt;element name="opcionesConsultaAcusePdf" type="{http://administracion.gob.es/punto-unico-notificaciones/consultaAcusePdf}Opciones" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConsultaAcusePdf", namespace = "http://administracion.gob.es/punto-unico-notificaciones/consultaAcusePdf", propOrder = {

})
public class ConsultaAcusePdf {

    protected String nifReceptor;
    protected String identificador;
    protected BigInteger codigoOrigen;
    protected IdentificadorAcusePdf identificadorAcusePdf;
    protected Opciones5 opcionesConsultaAcusePdf;

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
     * Obtiene el valor de la propiedad identificadorAcusePdf.
     * 
     * @return
     *     possible object is
     *     {@link IdentificadorAcusePdf }
     *     
     */
    public IdentificadorAcusePdf getIdentificadorAcusePdf() {
        return identificadorAcusePdf;
    }

    /**
     * Define el valor de la propiedad identificadorAcusePdf.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentificadorAcusePdf }
     *     
     */
    public void setIdentificadorAcusePdf(IdentificadorAcusePdf value) {
        this.identificadorAcusePdf = value;
    }

    /**
     * Obtiene el valor de la propiedad opcionesConsultaAcusePdf.
     * 
     * @return
     *     possible object is
     *     {@link Opciones5 }
     *     
     */
    public Opciones5 getOpcionesConsultaAcusePdf() {
        return opcionesConsultaAcusePdf;
    }

    /**
     * Define el valor de la propiedad opcionesConsultaAcusePdf.
     * 
     * @param value
     *     allowed object is
     *     {@link Opciones5 }
     *     
     */
    public void setOpcionesConsultaAcusePdf(Opciones5 value) {
        this.opcionesConsultaAcusePdf = value;
    }

}
