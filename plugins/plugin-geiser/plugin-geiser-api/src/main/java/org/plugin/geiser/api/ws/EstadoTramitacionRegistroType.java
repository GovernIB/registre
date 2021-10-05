
package org.plugin.geiser.api.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para estadoTramitacionRegistroType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="estadoTramitacionRegistroType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tipoAsiento" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}tipoAsientoEnum"/>
 *         &lt;element name="nuRegistro" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="nuRegistroOrigen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nuRegistroInterno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="timestampPresentado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="timestampRegistrado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="timestampConfirmadoRechazado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estado" type="{http://registro.ws.rgeco.geiser.minhap.gob.es/}estadoAsientoTramitacionEnum"/>
 *         &lt;element name="identificadoresIntercambioSIR" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "estadoTramitacionRegistroType", propOrder = {
    "tipoAsiento",
    "nuRegistro",
    "nuRegistroOrigen",
    "nuRegistroInterno",
    "timestampPresentado",
    "timestampRegistrado",
    "timestampConfirmadoRechazado",
    "estado",
    "identificadoresIntercambioSIR"
})
public class EstadoTramitacionRegistroType {

    @XmlElement(required = true)
    protected TipoAsientoEnum tipoAsiento;
    @XmlElement(required = true)
    protected String nuRegistro;
    protected String nuRegistroOrigen;
    protected String nuRegistroInterno;
    @XmlElement(required = true)
    protected String timestampPresentado;
    @XmlElement(required = true)
    protected String timestampRegistrado;
    protected String timestampConfirmadoRechazado;
    @XmlElement(required = true)
    protected EstadoAsientoTramitacionEnum estado;
    protected List<String> identificadoresIntercambioSIR;

    /**
     * Obtiene el valor de la propiedad tipoAsiento.
     * 
     * @return
     *     possible object is
     *     {@link TipoAsientoEnum }
     *     
     */
    public TipoAsientoEnum getTipoAsiento() {
        return tipoAsiento;
    }

    /**
     * Define el valor de la propiedad tipoAsiento.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoAsientoEnum }
     *     
     */
    public void setTipoAsiento(TipoAsientoEnum value) {
        this.tipoAsiento = value;
    }

    /**
     * Obtiene el valor de la propiedad nuRegistro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNuRegistro() {
        return nuRegistro;
    }

    /**
     * Define el valor de la propiedad nuRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNuRegistro(String value) {
        this.nuRegistro = value;
    }

    /**
     * Obtiene el valor de la propiedad nuRegistroOrigen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNuRegistroOrigen() {
        return nuRegistroOrigen;
    }

    /**
     * Define el valor de la propiedad nuRegistroOrigen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNuRegistroOrigen(String value) {
        this.nuRegistroOrigen = value;
    }

    /**
     * Obtiene el valor de la propiedad nuRegistroInterno.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNuRegistroInterno() {
        return nuRegistroInterno;
    }

    /**
     * Define el valor de la propiedad nuRegistroInterno.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNuRegistroInterno(String value) {
        this.nuRegistroInterno = value;
    }

    /**
     * Obtiene el valor de la propiedad timestampPresentado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimestampPresentado() {
        return timestampPresentado;
    }

    /**
     * Define el valor de la propiedad timestampPresentado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimestampPresentado(String value) {
        this.timestampPresentado = value;
    }

    /**
     * Obtiene el valor de la propiedad timestampRegistrado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimestampRegistrado() {
        return timestampRegistrado;
    }

    /**
     * Define el valor de la propiedad timestampRegistrado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimestampRegistrado(String value) {
        this.timestampRegistrado = value;
    }

    /**
     * Obtiene el valor de la propiedad timestampConfirmadoRechazado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimestampConfirmadoRechazado() {
        return timestampConfirmadoRechazado;
    }

    /**
     * Define el valor de la propiedad timestampConfirmadoRechazado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimestampConfirmadoRechazado(String value) {
        this.timestampConfirmadoRechazado = value;
    }

    /**
     * Obtiene el valor de la propiedad estado.
     * 
     * @return
     *     possible object is
     *     {@link EstadoAsientoTramitacionEnum }
     *     
     */
    public EstadoAsientoTramitacionEnum getEstado() {
        return estado;
    }

    /**
     * Define el valor de la propiedad estado.
     * 
     * @param value
     *     allowed object is
     *     {@link EstadoAsientoTramitacionEnum }
     *     
     */
    public void setEstado(EstadoAsientoTramitacionEnum value) {
        this.estado = value;
    }

    /**
     * Gets the value of the identificadoresIntercambioSIR property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the identificadoresIntercambioSIR property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdentificadoresIntercambioSIR().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getIdentificadoresIntercambioSIR() {
        if (identificadoresIntercambioSIR == null) {
            identificadoresIntercambioSIR = new ArrayList<String>();
        }
        return this.identificadoresIntercambioSIR;
    }

}
