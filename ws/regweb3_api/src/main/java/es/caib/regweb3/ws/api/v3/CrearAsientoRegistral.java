
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para crearAsientoRegistral complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="crearAsientoRegistral">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idSesion" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="entidad" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="asientoRegistral" type="{http://impl.v3.ws.regweb3.caib.es/}asientoRegistralWs" minOccurs="0"/>
 *         &lt;element name="tipoOperacion" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="justificante" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="distribuir" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "crearAsientoRegistral", propOrder = {
    "idSesion",
    "entidad",
    "asientoRegistral",
    "tipoOperacion",
    "justificante",
    "distribuir"
})
public class CrearAsientoRegistral {

    protected Long idSesion;
    protected String entidad;
    protected AsientoRegistralWs asientoRegistral;
    protected Long tipoOperacion;
    protected Boolean justificante;
    protected Boolean distribuir;

    /**
     * Obtiene el valor de la propiedad idSesion.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdSesion() {
        return idSesion;
    }

    /**
     * Define el valor de la propiedad idSesion.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdSesion(Long value) {
        this.idSesion = value;
    }

    /**
     * Obtiene el valor de la propiedad entidad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidad() {
        return entidad;
    }

    /**
     * Define el valor de la propiedad entidad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidad(String value) {
        this.entidad = value;
    }

    /**
     * Obtiene el valor de la propiedad asientoRegistral.
     * 
     * @return
     *     possible object is
     *     {@link AsientoRegistralWs }
     *     
     */
    public AsientoRegistralWs getAsientoRegistral() {
        return asientoRegistral;
    }

    /**
     * Define el valor de la propiedad asientoRegistral.
     * 
     * @param value
     *     allowed object is
     *     {@link AsientoRegistralWs }
     *     
     */
    public void setAsientoRegistral(AsientoRegistralWs value) {
        this.asientoRegistral = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoOperacion.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTipoOperacion() {
        return tipoOperacion;
    }

    /**
     * Define el valor de la propiedad tipoOperacion.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTipoOperacion(Long value) {
        this.tipoOperacion = value;
    }

    /**
     * Obtiene el valor de la propiedad justificante.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isJustificante() {
        return justificante;
    }

    /**
     * Define el valor de la propiedad justificante.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setJustificante(Boolean value) {
        this.justificante = value;
    }

    /**
     * Obtiene el valor de la propiedad distribuir.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDistribuir() {
        return distribuir;
    }

    /**
     * Define el valor de la propiedad distribuir.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDistribuir(Boolean value) {
        this.distribuir = value;
    }

}
