
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para peticionBusquedaType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="peticionBusquedaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tipoAsiento" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}tipoAsientoEnum" minOccurs="0"/>
 *         &lt;element name="cdOrganoOrigen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cdOrganoDestino" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="timestampPresentado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="timestampPresentadoDesde" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="timestampPresentadoHasta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="timestampRegistradoDesde" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="timestampRegistradoHasta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="timestampUltimoEventoDesde" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="timestampUltimoEventoHasta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cdAsunto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estado" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}estadoAsientoEnum" minOccurs="0"/>
 *         &lt;element name="documentacionFisica" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}tipoDocumentacionFisicaEnum" minOccurs="0"/>
 *         &lt;element name="identificadorInteresado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="identificadorRepresentante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "peticionBusquedaType", propOrder = {
    "tipoAsiento",
    "cdOrganoOrigen",
    "cdOrganoDestino",
    "timestampPresentado",
    "timestampPresentadoDesde",
    "timestampPresentadoHasta",
    "timestampRegistradoDesde",
    "timestampRegistradoHasta",
    "timestampUltimoEventoDesde",
    "timestampUltimoEventoHasta",
    "cdAsunto",
    "estado",
    "documentacionFisica",
    "identificadorInteresado",
    "identificadorRepresentante"
})
public class PeticionBusquedaType {

    protected TipoAsientoEnum tipoAsiento;
    protected String cdOrganoOrigen;
    protected String cdOrganoDestino;
    protected String timestampPresentado;
    protected String timestampPresentadoDesde;
    protected String timestampPresentadoHasta;
    protected String timestampRegistradoDesde;
    protected String timestampRegistradoHasta;
    protected String timestampUltimoEventoDesde;
    protected String timestampUltimoEventoHasta;
    protected String cdAsunto;
    protected EstadoAsientoEnum estado;
    protected TipoDocumentacionFisicaEnum documentacionFisica;
    protected String identificadorInteresado;
    protected String identificadorRepresentante;

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
     * Obtiene el valor de la propiedad cdOrganoOrigen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCdOrganoOrigen() {
        return cdOrganoOrigen;
    }

    /**
     * Define el valor de la propiedad cdOrganoOrigen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCdOrganoOrigen(String value) {
        this.cdOrganoOrigen = value;
    }

    /**
     * Obtiene el valor de la propiedad cdOrganoDestino.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCdOrganoDestino() {
        return cdOrganoDestino;
    }

    /**
     * Define el valor de la propiedad cdOrganoDestino.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCdOrganoDestino(String value) {
        this.cdOrganoDestino = value;
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
     * Obtiene el valor de la propiedad timestampPresentadoDesde.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimestampPresentadoDesde() {
        return timestampPresentadoDesde;
    }

    /**
     * Define el valor de la propiedad timestampPresentadoDesde.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimestampPresentadoDesde(String value) {
        this.timestampPresentadoDesde = value;
    }

    /**
     * Obtiene el valor de la propiedad timestampPresentadoHasta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimestampPresentadoHasta() {
        return timestampPresentadoHasta;
    }

    /**
     * Define el valor de la propiedad timestampPresentadoHasta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimestampPresentadoHasta(String value) {
        this.timestampPresentadoHasta = value;
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
     * Obtiene el valor de la propiedad timestampUltimoEventoDesde.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimestampUltimoEventoDesde() {
        return timestampUltimoEventoDesde;
    }

    /**
     * Define el valor de la propiedad timestampUltimoEventoDesde.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimestampUltimoEventoDesde(String value) {
        this.timestampUltimoEventoDesde = value;
    }

    /**
     * Obtiene el valor de la propiedad timestampUltimoEventoHasta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimestampUltimoEventoHasta() {
        return timestampUltimoEventoHasta;
    }

    /**
     * Define el valor de la propiedad timestampUltimoEventoHasta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimestampUltimoEventoHasta(String value) {
        this.timestampUltimoEventoHasta = value;
    }

    /**
     * Obtiene el valor de la propiedad cdAsunto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCdAsunto() {
        return cdAsunto;
    }

    /**
     * Define el valor de la propiedad cdAsunto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCdAsunto(String value) {
        this.cdAsunto = value;
    }

    /**
     * Obtiene el valor de la propiedad estado.
     * 
     * @return
     *     possible object is
     *     {@link EstadoAsientoEnum }
     *     
     */
    public EstadoAsientoEnum getEstado() {
        return estado;
    }

    /**
     * Define el valor de la propiedad estado.
     * 
     * @param value
     *     allowed object is
     *     {@link EstadoAsientoEnum }
     *     
     */
    public void setEstado(EstadoAsientoEnum value) {
        this.estado = value;
    }

    /**
     * Obtiene el valor de la propiedad documentacionFisica.
     * 
     * @return
     *     possible object is
     *     {@link TipoDocumentacionFisicaEnum }
     *     
     */
    public TipoDocumentacionFisicaEnum getDocumentacionFisica() {
        return documentacionFisica;
    }

    /**
     * Define el valor de la propiedad documentacionFisica.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoDocumentacionFisicaEnum }
     *     
     */
    public void setDocumentacionFisica(TipoDocumentacionFisicaEnum value) {
        this.documentacionFisica = value;
    }

    /**
     * Obtiene el valor de la propiedad identificadorInteresado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificadorInteresado() {
        return identificadorInteresado;
    }

    /**
     * Define el valor de la propiedad identificadorInteresado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificadorInteresado(String value) {
        this.identificadorInteresado = value;
    }

    /**
     * Obtiene el valor de la propiedad identificadorRepresentante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificadorRepresentante() {
        return identificadorRepresentante;
    }

    /**
     * Define el valor de la propiedad identificadorRepresentante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificadorRepresentante(String value) {
        this.identificadorRepresentante = value;
    }

}
