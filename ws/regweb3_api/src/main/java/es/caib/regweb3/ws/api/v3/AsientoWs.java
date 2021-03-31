
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Clase Java para asientoWs complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="asientoWs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="anexos" type="{http://impl.v3.ws.regweb3.caib.es/}fileInfoWs" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="codigoDestino" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoOficinaOrigen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoSia" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="codigoUnidadOrigen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="denominacionDestino" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="denominacionOficinaOrigen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="denominacionUnidadOrigen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionEstado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="expone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="extracto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaRegistro" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="idioma" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="interesados" type="{http://impl.v3.ws.regweb3.caib.es/}interesadoWs" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="justificante" type="{http://impl.v3.ws.regweb3.caib.es/}fileInfoWs" minOccurs="0"/>
 *         &lt;element name="numeroRegistro" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="presencial" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="solicita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoDocumetacionFisica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoRegistro" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "asientoWs", propOrder = {
    "anexos",
    "codigoDestino",
    "codigoOficinaOrigen",
    "codigoSia",
    "codigoUnidadOrigen",
    "denominacionDestino",
    "denominacionOficinaOrigen",
    "denominacionUnidadOrigen",
    "descripcionEstado",
    "estado",
    "expone",
    "extracto",
    "fechaRegistro",
    "idioma",
    "interesados",
    "justificante",
    "numeroRegistro",
    "presencial",
    "solicita",
    "tipoDocumetacionFisica",
    "tipoRegistro"
})
public class AsientoWs {

    @XmlElement(nillable = true)
    protected List<FileInfoWs> anexos;
    protected String codigoDestino;
    protected String codigoOficinaOrigen;
    protected Long codigoSia;
    protected String codigoUnidadOrigen;
    protected String denominacionDestino;
    protected String denominacionOficinaOrigen;
    protected String denominacionUnidadOrigen;
    protected String descripcionEstado;
    protected Long estado;
    protected String expone;
    protected String extracto;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Timestamp fechaRegistro;
    protected Long idioma;
    @XmlElement(nillable = true)
    protected List<InteresadoWs> interesados;
    protected FileInfoWs justificante;
    protected String numeroRegistro;
    protected Boolean presencial;
    protected String solicita;
    protected String tipoDocumetacionFisica;
    protected Long tipoRegistro;

    /**
     * Gets the value of the anexos property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the anexos property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnexos().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FileInfoWs }
     * 
     * 
     */
    public List<FileInfoWs> getAnexos() {
        if (anexos == null) {
            anexos = new ArrayList<FileInfoWs>();
        }
        return this.anexos;
    }

    /**
     * Obtiene el valor de la propiedad codigoDestino.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoDestino() {
        return codigoDestino;
    }

    /**
     * Define el valor de la propiedad codigoDestino.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoDestino(String value) {
        this.codigoDestino = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoOficinaOrigen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoOficinaOrigen() {
        return codigoOficinaOrigen;
    }

    /**
     * Define el valor de la propiedad codigoOficinaOrigen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoOficinaOrigen(String value) {
        this.codigoOficinaOrigen = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoSia.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCodigoSia() {
        return codigoSia;
    }

    /**
     * Define el valor de la propiedad codigoSia.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCodigoSia(Long value) {
        this.codigoSia = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoUnidadOrigen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoUnidadOrigen() {
        return codigoUnidadOrigen;
    }

    /**
     * Define el valor de la propiedad codigoUnidadOrigen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoUnidadOrigen(String value) {
        this.codigoUnidadOrigen = value;
    }

    /**
     * Obtiene el valor de la propiedad denominacionDestino.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenominacionDestino() {
        return denominacionDestino;
    }

    /**
     * Define el valor de la propiedad denominacionDestino.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenominacionDestino(String value) {
        this.denominacionDestino = value;
    }

    /**
     * Obtiene el valor de la propiedad denominacionOficinaOrigen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenominacionOficinaOrigen() {
        return denominacionOficinaOrigen;
    }

    /**
     * Define el valor de la propiedad denominacionOficinaOrigen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenominacionOficinaOrigen(String value) {
        this.denominacionOficinaOrigen = value;
    }

    /**
     * Obtiene el valor de la propiedad denominacionUnidadOrigen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDenominacionUnidadOrigen() {
        return denominacionUnidadOrigen;
    }

    /**
     * Define el valor de la propiedad denominacionUnidadOrigen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDenominacionUnidadOrigen(String value) {
        this.denominacionUnidadOrigen = value;
    }

    /**
     * Obtiene el valor de la propiedad descripcionEstado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionEstado() {
        return descripcionEstado;
    }

    /**
     * Define el valor de la propiedad descripcionEstado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionEstado(String value) {
        this.descripcionEstado = value;
    }

    /**
     * Obtiene el valor de la propiedad estado.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEstado() {
        return estado;
    }

    /**
     * Define el valor de la propiedad estado.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEstado(Long value) {
        this.estado = value;
    }

    /**
     * Obtiene el valor de la propiedad expone.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExpone() {
        return expone;
    }

    /**
     * Define el valor de la propiedad expone.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExpone(String value) {
        this.expone = value;
    }

    /**
     * Obtiene el valor de la propiedad extracto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtracto() {
        return extracto;
    }

    /**
     * Define el valor de la propiedad extracto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtracto(String value) {
        this.extracto = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaRegistro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    /**
     * Define el valor de la propiedad fechaRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaRegistro(Timestamp value) {
        this.fechaRegistro = value;
    }

    /**
     * Obtiene el valor de la propiedad idioma.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getIdioma() {
        return idioma;
    }

    /**
     * Define el valor de la propiedad idioma.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setIdioma(Long value) {
        this.idioma = value;
    }

    /**
     * Gets the value of the interesados property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the interesados property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInteresados().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InteresadoWs }
     * 
     * 
     */
    public List<InteresadoWs> getInteresados() {
        if (interesados == null) {
            interesados = new ArrayList<InteresadoWs>();
        }
        return this.interesados;
    }

    /**
     * Obtiene el valor de la propiedad justificante.
     * 
     * @return
     *     possible object is
     *     {@link FileInfoWs }
     *     
     */
    public FileInfoWs getJustificante() {
        return justificante;
    }

    /**
     * Define el valor de la propiedad justificante.
     * 
     * @param value
     *     allowed object is
     *     {@link FileInfoWs }
     *     
     */
    public void setJustificante(FileInfoWs value) {
        this.justificante = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroRegistro.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    /**
     * Define el valor de la propiedad numeroRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroRegistro(String value) {
        this.numeroRegistro = value;
    }

    /**
     * Obtiene el valor de la propiedad presencial.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPresencial() {
        return presencial;
    }

    /**
     * Define el valor de la propiedad presencial.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPresencial(Boolean value) {
        this.presencial = value;
    }

    /**
     * Obtiene el valor de la propiedad solicita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSolicita() {
        return solicita;
    }

    /**
     * Define el valor de la propiedad solicita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSolicita(String value) {
        this.solicita = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoDocumetacionFisica.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDocumetacionFisica() {
        return tipoDocumetacionFisica;
    }

    /**
     * Define el valor de la propiedad tipoDocumetacionFisica.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDocumetacionFisica(String value) {
        this.tipoDocumetacionFisica = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoRegistro.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTipoRegistro() {
        return tipoRegistro;
    }

    /**
     * Define el valor de la propiedad tipoRegistro.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTipoRegistro(Long value) {
        this.tipoRegistro = value;
    }

}
