//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.12.05 at 08:05:06 AM CET 
//


package es.caib.regweb.persistence.utils.sir;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Codigo_Entidad_Registral_Origen">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="21"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Codigo_Entidad_Registral_Destino">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="21"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Identificador_Intercambio">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="33"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Tipo_Mensaje">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="2"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Descripcion_Mensaje" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="1024"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Numero_Registro_Entrada_Destino" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Fecha_Hora_Entrada_Destino" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="14"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Indicador_Prueba">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="0"/>
 *               &lt;enumeration value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Identificador_Fichero" maxOccurs="10" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Codigo_Error" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="4"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "codigoEntidadRegistralOrigen",
    "codigoEntidadRegistralDestino",
    "identificadorIntercambio",
    "tipoMensaje",
    "descripcionMensaje",
    "numeroRegistroEntradaDestino",
    "fechaHoraEntradaDestino",
    "indicadorPrueba",
    "identificadorFichero",
    "codigoError"
})
@XmlRootElement(name = "De_Mensaje")
public class DeMensaje {

    @XmlElement(name = "Codigo_Entidad_Registral_Origen", required = true)
    protected String codigoEntidadRegistralOrigen;
    @XmlElement(name = "Codigo_Entidad_Registral_Destino", required = true)
    protected String codigoEntidadRegistralDestino;
    @XmlElement(name = "Identificador_Intercambio", required = true)
    protected String identificadorIntercambio;
    @XmlElement(name = "Tipo_Mensaje", required = true)
    protected String tipoMensaje;
    @XmlElement(name = "Descripcion_Mensaje")
    protected String descripcionMensaje;
    @XmlElement(name = "Numero_Registro_Entrada_Destino")
    protected String numeroRegistroEntradaDestino;
    @XmlElement(name = "Fecha_Hora_Entrada_Destino")
    protected String fechaHoraEntradaDestino;
    @XmlElement(name = "Indicador_Prueba", required = true)
    protected String indicadorPrueba;
    @XmlElement(name = "Identificador_Fichero")
    protected List<String> identificadorFichero;
    @XmlElement(name = "Codigo_Error")
    protected String codigoError;

    /**
     * Gets the value of the codigoEntidadRegistralOrigen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoEntidadRegistralOrigen() {
        return codigoEntidadRegistralOrigen;
    }

    /**
     * Sets the value of the codigoEntidadRegistralOrigen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoEntidadRegistralOrigen(String value) {
        this.codigoEntidadRegistralOrigen = value;
    }

    /**
     * Gets the value of the codigoEntidadRegistralDestino property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoEntidadRegistralDestino() {
        return codigoEntidadRegistralDestino;
    }

    /**
     * Sets the value of the codigoEntidadRegistralDestino property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoEntidadRegistralDestino(String value) {
        this.codigoEntidadRegistralDestino = value;
    }

    /**
     * Gets the value of the identificadorIntercambio property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificadorIntercambio() {
        return identificadorIntercambio;
    }

    /**
     * Sets the value of the identificadorIntercambio property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificadorIntercambio(String value) {
        this.identificadorIntercambio = value;
    }

    /**
     * Gets the value of the tipoMensaje property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoMensaje() {
        return tipoMensaje;
    }

    /**
     * Sets the value of the tipoMensaje property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoMensaje(String value) {
        this.tipoMensaje = value;
    }

    /**
     * Gets the value of the descripcionMensaje property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionMensaje() {
        return descripcionMensaje;
    }

    /**
     * Sets the value of the descripcionMensaje property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionMensaje(String value) {
        this.descripcionMensaje = value;
    }

    /**
     * Gets the value of the numeroRegistroEntradaDestino property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroRegistroEntradaDestino() {
        return numeroRegistroEntradaDestino;
    }

    /**
     * Sets the value of the numeroRegistroEntradaDestino property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroRegistroEntradaDestino(String value) {
        this.numeroRegistroEntradaDestino = value;
    }

    /**
     * Gets the value of the fechaHoraEntradaDestino property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaHoraEntradaDestino() {
        return fechaHoraEntradaDestino;
    }

    /**
     * Sets the value of the fechaHoraEntradaDestino property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaHoraEntradaDestino(String value) {
        this.fechaHoraEntradaDestino = value;
    }

    /**
     * Gets the value of the indicadorPrueba property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIndicadorPrueba() {
        return indicadorPrueba;
    }

    /**
     * Sets the value of the indicadorPrueba property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIndicadorPrueba(String value) {
        this.indicadorPrueba = value;
    }

    /**
     * Gets the value of the identificadorFichero property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the identificadorFichero property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdentificadorFichero().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getIdentificadorFichero() {
        if (identificadorFichero == null) {
            identificadorFichero = new ArrayList<String>();
        }
        return this.identificadorFichero;
    }

    /**
     * Gets the value of the codigoError property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoError() {
        return codigoError;
    }

    /**
     * Sets the value of the codigoError property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoError(String value) {
        this.codigoError = value;
    }

}
