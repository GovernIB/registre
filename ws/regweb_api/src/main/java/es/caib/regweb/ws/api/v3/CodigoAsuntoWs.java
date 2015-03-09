
package es.caib.regweb.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for codigoAsuntoWs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="codigoAsuntoWs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoAsunto" type="{http://impl.v3.ws.regweb.caib.es/}tipoAsuntoWs" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "codigoAsuntoWs", propOrder = {
    "codigo",
    "nombre",
    "tipoAsunto"
})
public class CodigoAsuntoWs {

    protected String codigo;
    protected String nombre;
    protected TipoAsuntoWs tipoAsunto;

    /**
     * Gets the value of the codigo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Sets the value of the codigo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigo(String value) {
        this.codigo = value;
    }

    /**
     * Gets the value of the nombre property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the value of the nombre property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Gets the value of the tipoAsunto property.
     * 
     * @return
     *     possible object is
     *     {@link TipoAsuntoWs }
     *     
     */
    public TipoAsuntoWs getTipoAsunto() {
        return tipoAsunto;
    }

    /**
     * Sets the value of the tipoAsunto property.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoAsuntoWs }
     *     
     */
    public void setTipoAsunto(TipoAsuntoWs value) {
        this.tipoAsunto = value;
    }

}
