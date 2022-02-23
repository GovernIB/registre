
package org.plugin.geiser.api.ws;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para basePeticionRegistroType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="basePeticionRegistroType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tipoAsiento" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}tipoAsientoEnum"/>
 *         &lt;element name="timestampPresentado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cdOrganoOrigen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cdOrganoDestino" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="interesados" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}interesadoType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="anexos" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}anexoType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="formulario" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}formularioType" minOccurs="0"/>
 *         &lt;element name="resumen" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cdAsunto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="referenciaExterna" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nuExpediente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoTransporte" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}tipoTransporteEnum" minOccurs="0"/>
 *         &lt;element name="nuTransporte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreUsuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contactoUsuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="observaciones" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="expone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="solicita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "basePeticionRegistroType", propOrder = {
    "tipoAsiento",
    "timestampPresentado",
    "cdOrganoOrigen",
    "cdOrganoDestino",
    "interesados",
    "anexos",
    "formulario",
    "resumen",
    "cdAsunto",
    "referenciaExterna",
    "nuExpediente",
    "tipoTransporte",
    "nuTransporte",
    "nombreUsuario",
    "contactoUsuario",
    "observaciones",
    "expone",
    "solicita"
})
@XmlSeeAlso({
    PeticionRegistroEnvioType.class,
    PeticionRegistroType.class,
    PeticionRegistroEnvioSimpleType.class
})
public abstract class BasePeticionRegistroType {

    @XmlElement(required = true)
    protected TipoAsientoEnum tipoAsiento;
    protected String timestampPresentado;
    protected String cdOrganoOrigen;
    protected String cdOrganoDestino;
    protected List<InteresadoType> interesados;
    protected List<AnexoType> anexos;
    protected FormularioType formulario;
    @XmlElement(required = true)
    protected String resumen;
    @XmlElement(required = true)
    protected String cdAsunto;
    protected String referenciaExterna;
    protected String nuExpediente;
    protected TipoTransporteEnum tipoTransporte;
    protected String nuTransporte;
    protected String nombreUsuario;
    protected String contactoUsuario;
    protected String observaciones;
    protected String expone;
    protected String solicita;

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
     * {@link InteresadoType }
     * 
     * 
     */
    public List<InteresadoType> getInteresados() {
        if (interesados == null) {
            interesados = new ArrayList<InteresadoType>();
        }
        return this.interesados;
    }

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
     * {@link AnexoType }
     * 
     * 
     */
    public List<AnexoType> getAnexos() {
        if (anexos == null) {
            anexos = new ArrayList<AnexoType>();
        }
        return this.anexos;
    }

    /**
     * Obtiene el valor de la propiedad formulario.
     * 
     * @return
     *     possible object is
     *     {@link FormularioType }
     *     
     */
    public FormularioType getFormulario() {
        return formulario;
    }

    /**
     * Define el valor de la propiedad formulario.
     * 
     * @param value
     *     allowed object is
     *     {@link FormularioType }
     *     
     */
    public void setFormulario(FormularioType value) {
        this.formulario = value;
    }

    /**
     * Obtiene el valor de la propiedad resumen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResumen() {
        return resumen;
    }

    /**
     * Define el valor de la propiedad resumen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResumen(String value) {
        this.resumen = value;
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
     * Obtiene el valor de la propiedad referenciaExterna.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenciaExterna() {
        return referenciaExterna;
    }

    /**
     * Define el valor de la propiedad referenciaExterna.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenciaExterna(String value) {
        this.referenciaExterna = value;
    }

    /**
     * Obtiene el valor de la propiedad nuExpediente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNuExpediente() {
        return nuExpediente;
    }

    /**
     * Define el valor de la propiedad nuExpediente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNuExpediente(String value) {
        this.nuExpediente = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoTransporte.
     * 
     * @return
     *     possible object is
     *     {@link TipoTransporteEnum }
     *     
     */
    public TipoTransporteEnum getTipoTransporte() {
        return tipoTransporte;
    }

    /**
     * Define el valor de la propiedad tipoTransporte.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoTransporteEnum }
     *     
     */
    public void setTipoTransporte(TipoTransporteEnum value) {
        this.tipoTransporte = value;
    }

    /**
     * Obtiene el valor de la propiedad nuTransporte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNuTransporte() {
        return nuTransporte;
    }

    /**
     * Define el valor de la propiedad nuTransporte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNuTransporte(String value) {
        this.nuTransporte = value;
    }

    /**
     * Obtiene el valor de la propiedad nombreUsuario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /**
     * Define el valor de la propiedad nombreUsuario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreUsuario(String value) {
        this.nombreUsuario = value;
    }

    /**
     * Obtiene el valor de la propiedad contactoUsuario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContactoUsuario() {
        return contactoUsuario;
    }

    /**
     * Define el valor de la propiedad contactoUsuario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContactoUsuario(String value) {
        this.contactoUsuario = value;
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

}
