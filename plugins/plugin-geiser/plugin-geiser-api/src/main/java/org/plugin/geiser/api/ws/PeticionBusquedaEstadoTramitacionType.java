
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para peticionBusquedaEstadoTramitacionType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="peticionBusquedaEstadoTramitacionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nuRegistro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoAsiento" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}tipoAsientoEnum" minOccurs="0"/>
 *         &lt;element name="estado" type="{http://registro.ws.rgeco.geiser.minhap.gob.es/}estadoAsientoTramitacionEnum" minOccurs="0"/>
 *         &lt;element name="cdOficinaOrigen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cdIdentificadorIntercambio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="timestampRegistradoDesde" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="timestampRegistradoHasta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoIdentificadorInteresadoRepresentante" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}tipoIdentificacionEnum"/>
 *         &lt;element name="identificadorInteresadoRepresentante" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="incluirEnviadosSIR" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "peticionBusquedaEstadoTramitacionType", propOrder = {
    "nuRegistro",
    "tipoAsiento",
    "estado",
    "cdOficinaOrigen",
    "cdIdentificadorIntercambio",
    "timestampRegistradoDesde",
    "timestampRegistradoHasta",
    "tipoIdentificadorInteresadoRepresentante",
    "identificadorInteresadoRepresentante",
    "incluirEnviadosSIR"
})
public class PeticionBusquedaEstadoTramitacionType {

    protected String nuRegistro;
    protected TipoAsientoEnum tipoAsiento;
    protected EstadoAsientoTramitacionEnum estado;
    protected String cdOficinaOrigen;
    protected String cdIdentificadorIntercambio;
    protected String timestampRegistradoDesde;
    protected String timestampRegistradoHasta;
    @XmlElement(required = true)
    protected TipoIdentificacionEnum tipoIdentificadorInteresadoRepresentante;
    @XmlElement(required = true)
    protected String identificadorInteresadoRepresentante;
    protected boolean incluirEnviadosSIR;

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
     * Obtiene el valor de la propiedad cdOficinaOrigen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCdOficinaOrigen() {
        return cdOficinaOrigen;
    }

    /**
     * Define el valor de la propiedad cdOficinaOrigen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCdOficinaOrigen(String value) {
        this.cdOficinaOrigen = value;
    }

    /**
     * Obtiene el valor de la propiedad cdIdentificadorIntercambio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCdIdentificadorIntercambio() {
        return cdIdentificadorIntercambio;
    }

    /**
     * Define el valor de la propiedad cdIdentificadorIntercambio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCdIdentificadorIntercambio(String value) {
        this.cdIdentificadorIntercambio = value;
    }

    /**
     * Obtiene el valor de la propiedad timestampRegistradoDesde.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimestampRegistradoDesde() {
        return timestampRegistradoDesde;
    }

    /**
     * Define el valor de la propiedad timestampRegistradoDesde.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimestampRegistradoDesde(String value) {
        this.timestampRegistradoDesde = value;
    }

    /**
     * Obtiene el valor de la propiedad timestampRegistradoHasta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimestampRegistradoHasta() {
        return timestampRegistradoHasta;
    }

    /**
     * Define el valor de la propiedad timestampRegistradoHasta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimestampRegistradoHasta(String value) {
        this.timestampRegistradoHasta = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoIdentificadorInteresadoRepresentante.
     * 
     * @return
     *     possible object is
     *     {@link TipoIdentificacionEnum }
     *     
     */
    public TipoIdentificacionEnum getTipoIdentificadorInteresadoRepresentante() {
        return tipoIdentificadorInteresadoRepresentante;
    }

    /**
     * Define el valor de la propiedad tipoIdentificadorInteresadoRepresentante.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoIdentificacionEnum }
     *     
     */
    public void setTipoIdentificadorInteresadoRepresentante(TipoIdentificacionEnum value) {
        this.tipoIdentificadorInteresadoRepresentante = value;
    }

    /**
     * Obtiene el valor de la propiedad identificadorInteresadoRepresentante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificadorInteresadoRepresentante() {
        return identificadorInteresadoRepresentante;
    }

    /**
     * Define el valor de la propiedad identificadorInteresadoRepresentante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificadorInteresadoRepresentante(String value) {
        this.identificadorInteresadoRepresentante = value;
    }

    /**
     * Obtiene el valor de la propiedad incluirEnviadosSIR.
     * 
     */
    public boolean isIncluirEnviadosSIR() {
        return incluirEnviadosSIR;
    }

    /**
     * Define el valor de la propiedad incluirEnviadosSIR.
     * 
     */
    public void setIncluirEnviadosSIR(boolean value) {
        this.incluirEnviadosSIR = value;
    }

}
