
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anexoType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="anexoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="identificador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="validez" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}validezDocumentoEnum" minOccurs="0"/>
 *         &lt;element name="tipoDocumento" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}tipoDocAnexoEnum"/>
 *         &lt;element name="tipoFirma" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}tipoFirmaEnum"/>
 *         &lt;element name="hash" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tipoMime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tamanioFichero" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="anexo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreFirma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hashFirma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoMimeFirma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tamanioFicheroFirma" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="firma" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreCSV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hashCSV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipomimeCSV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tamanioFicheroCSV" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="anexoCSV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoCSV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="observaciones" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "anexoType", propOrder = {
    "nombre",
    "identificador",
    "validez",
    "tipoDocumento",
    "tipoFirma",
    "hash",
    "tipoMime",
    "tamanioFichero",
    "anexo",
    "nombreFirma",
    "hashFirma",
    "tipoMimeFirma",
    "tamanioFicheroFirma",
    "firma",
    "nombreCSV",
    "hashCSV",
    "tipomimeCSV",
    "tamanioFicheroCSV",
    "anexoCSV",
    "codigoCSV",
    "observaciones"
})
public class AnexoType {

    @XmlElement(required = true)
    protected String nombre;
    protected String identificador;
    protected ValidezDocumentoEnum validez;
    @XmlElement(required = true)
    protected TipoDocAnexoEnum tipoDocumento;
    @XmlElement(required = true)
    protected TipoFirmaEnum tipoFirma;
    @XmlElement(required = true)
    protected String hash;
    protected String tipoMime;
    protected Long tamanioFichero;
    protected String anexo;
    protected String nombreFirma;
    protected String hashFirma;
    protected String tipoMimeFirma;
    protected Long tamanioFicheroFirma;
    protected String firma;
    protected String nombreCSV;
    protected String hashCSV;
    protected String tipomimeCSV;
    protected Long tamanioFicheroCSV;
    protected String anexoCSV;
    protected String codigoCSV;
    protected String observaciones;

    /**
     * Obtiene el valor de la propiedad nombre.
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
     * Define el valor de la propiedad nombre.
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
     * Obtiene el valor de la propiedad validez.
     * 
     * @return
     *     possible object is
     *     {@link ValidezDocumentoEnum }
     *     
     */
    public ValidezDocumentoEnum getValidez() {
        return validez;
    }

    /**
     * Define el valor de la propiedad validez.
     * 
     * @param value
     *     allowed object is
     *     {@link ValidezDocumentoEnum }
     *     
     */
    public void setValidez(ValidezDocumentoEnum value) {
        this.validez = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoDocumento.
     * 
     * @return
     *     possible object is
     *     {@link TipoDocAnexoEnum }
     *     
     */
    public TipoDocAnexoEnum getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Define el valor de la propiedad tipoDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoDocAnexoEnum }
     *     
     */
    public void setTipoDocumento(TipoDocAnexoEnum value) {
        this.tipoDocumento = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoFirma.
     * 
     * @return
     *     possible object is
     *     {@link TipoFirmaEnum }
     *     
     */
    public TipoFirmaEnum getTipoFirma() {
        return tipoFirma;
    }

    /**
     * Define el valor de la propiedad tipoFirma.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoFirmaEnum }
     *     
     */
    public void setTipoFirma(TipoFirmaEnum value) {
        this.tipoFirma = value;
    }

    /**
     * Obtiene el valor de la propiedad hash.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHash() {
        return hash;
    }

    /**
     * Define el valor de la propiedad hash.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHash(String value) {
        this.hash = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoMime.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoMime() {
        return tipoMime;
    }

    /**
     * Define el valor de la propiedad tipoMime.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoMime(String value) {
        this.tipoMime = value;
    }

    /**
     * Obtiene el valor de la propiedad tamanioFichero.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTamanioFichero() {
        return tamanioFichero;
    }

    /**
     * Define el valor de la propiedad tamanioFichero.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTamanioFichero(Long value) {
        this.tamanioFichero = value;
    }

    /**
     * Obtiene el valor de la propiedad anexo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnexo() {
        return anexo;
    }

    /**
     * Define el valor de la propiedad anexo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnexo(String value) {
        this.anexo = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreFirma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreFirma() {
        return nombreFirma;
    }

    /**
     * Define el valor de la propiedad nombreFirma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreFirma(String value) {
        this.nombreFirma = value;
    }

    /**
     * Obtiene el valor de la propiedad hashFirma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHashFirma() {
        return hashFirma;
    }

    /**
     * Define el valor de la propiedad hashFirma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHashFirma(String value) {
        this.hashFirma = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoMimeFirma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoMimeFirma() {
        return tipoMimeFirma;
    }

    /**
     * Define el valor de la propiedad tipoMimeFirma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoMimeFirma(String value) {
        this.tipoMimeFirma = value;
    }

    /**
     * Obtiene el valor de la propiedad tamanioFicheroFirma.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTamanioFicheroFirma() {
        return tamanioFicheroFirma;
    }

    /**
     * Define el valor de la propiedad tamanioFicheroFirma.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTamanioFicheroFirma(Long value) {
        this.tamanioFicheroFirma = value;
    }

    /**
     * Obtiene el valor de la propiedad firma.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirma() {
        return firma;
    }

    /**
     * Define el valor de la propiedad firma.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirma(String value) {
        this.firma = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreCSV.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreCSV() {
        return nombreCSV;
    }

    /**
     * Define el valor de la propiedad nombreCSV.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreCSV(String value) {
        this.nombreCSV = value;
    }

    /**
     * Obtiene el valor de la propiedad hashCSV.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHashCSV() {
        return hashCSV;
    }

    /**
     * Define el valor de la propiedad hashCSV.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHashCSV(String value) {
        this.hashCSV = value;
    }

    /**
     * Obtiene el valor de la propiedad tipomimeCSV.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipomimeCSV() {
        return tipomimeCSV;
    }

    /**
     * Define el valor de la propiedad tipomimeCSV.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipomimeCSV(String value) {
        this.tipomimeCSV = value;
    }

    /**
     * Obtiene el valor de la propiedad tamanioFicheroCSV.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTamanioFicheroCSV() {
        return tamanioFicheroCSV;
    }

    /**
     * Define el valor de la propiedad tamanioFicheroCSV.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTamanioFicheroCSV(Long value) {
        this.tamanioFicheroCSV = value;
    }

    /**
     * Obtiene el valor de la propiedad anexoCSV.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnexoCSV() {
        return anexoCSV;
    }

    /**
     * Define el valor de la propiedad anexoCSV.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnexoCSV(String value) {
        this.anexoCSV = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoCSV.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoCSV() {
        return codigoCSV;
    }

    /**
     * Define el valor de la propiedad codigoCSV.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoCSV(String value) {
        this.codigoCSV = value;
    }

    /**
     * Obtiene el valor de la propiedad observaciones.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObservaciones() {
        return observaciones;
    }

    /**
     * Define el valor de la propiedad observaciones.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObservaciones(String value) {
        this.observaciones = value;
    }

}
