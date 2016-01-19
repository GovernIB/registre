//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2015.11.17 a las 01:53:09 PM CET 
//


package es.caib.regweb3.sir.core.schema;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Clase Java para anonymous complex type.
 * <p/>
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * <p/>
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Codigo_Entidad_Registral_Origen"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="21"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Codigo_Entidad_Registral_Destino"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="21"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Identificador_Intercambio"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="33"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Tipo_Mensaje"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Descripcion_Mensaje" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="1024"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Numero_Registro_Entrada_Destino" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="20"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Fecha_Hora_Entrada_Destino" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="14"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Indicador_Prueba"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;enumeration value="0"/&gt;
 *               &lt;enumeration value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Identificador_Fichero" maxOccurs="10" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="Codigo_Error" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="4"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
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
     * Obtiene el valor de la propiedad codigoEntidadRegistralOrigen.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCodigoEntidadRegistralOrigen() {
        return codigoEntidadRegistralOrigen;
    }

    /**
     * Define el valor de la propiedad codigoEntidadRegistralOrigen.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCodigoEntidadRegistralOrigen(String value) {
        this.codigoEntidadRegistralOrigen = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoEntidadRegistralDestino.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCodigoEntidadRegistralDestino() {
        return codigoEntidadRegistralDestino;
    }

    /**
     * Define el valor de la propiedad codigoEntidadRegistralDestino.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCodigoEntidadRegistralDestino(String value) {
        this.codigoEntidadRegistralDestino = value;
    }

    /**
     * Obtiene el valor de la propiedad identificadorIntercambio.
     *
     * @return possible object is
     * {@link String }
     */
    public String getIdentificadorIntercambio() {
        return identificadorIntercambio;
    }

    /**
     * Define el valor de la propiedad identificadorIntercambio.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setIdentificadorIntercambio(String value) {
        this.identificadorIntercambio = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoMensaje.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTipoMensaje() {
        return tipoMensaje;
    }

    /**
     * Define el valor de la propiedad tipoMensaje.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTipoMensaje(String value) {
        this.tipoMensaje = value;
    }

    /**
     * Obtiene el valor de la propiedad descripcionMensaje.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDescripcionMensaje() {
        return descripcionMensaje;
    }

    /**
     * Define el valor de la propiedad descripcionMensaje.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDescripcionMensaje(String value) {
        this.descripcionMensaje = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroRegistroEntradaDestino.
     *
     * @return possible object is
     * {@link String }
     */
    public String getNumeroRegistroEntradaDestino() {
        return numeroRegistroEntradaDestino;
    }

    /**
     * Define el valor de la propiedad numeroRegistroEntradaDestino.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNumeroRegistroEntradaDestino(String value) {
        this.numeroRegistroEntradaDestino = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaHoraEntradaDestino.
     *
     * @return possible object is
     * {@link String }
     */
    public String getFechaHoraEntradaDestino() {
        return fechaHoraEntradaDestino;
    }

    /**
     * Define el valor de la propiedad fechaHoraEntradaDestino.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFechaHoraEntradaDestino(String value) {
        this.fechaHoraEntradaDestino = value;
    }

    /**
     * Obtiene el valor de la propiedad indicadorPrueba.
     *
     * @return possible object is
     * {@link String }
     */
    public String getIndicadorPrueba() {
        return indicadorPrueba;
    }

    /**
     * Define el valor de la propiedad indicadorPrueba.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setIndicadorPrueba(String value) {
        this.indicadorPrueba = value;
    }

    /**
     * Gets the value of the identificadorFichero property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the identificadorFichero property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdentificadorFichero().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     */
    public List<String> getIdentificadorFichero() {
        if (identificadorFichero == null) {
            identificadorFichero = new ArrayList<String>();
        }
        return this.identificadorFichero;
    }

    /**
     * Obtiene el valor de la propiedad codigoError.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCodigoError() {
        return codigoError;
    }

    /**
     * Define el valor de la propiedad codigoError.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCodigoError(String value) {
        this.codigoError = value;
    }

}
