
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for registroResponseWs complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="registroResponseWs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="anexos" type="{http://impl.v3.ws.regweb3.caib.es/}anexoWs" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="aplicacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoAsuntoCodigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoAsuntoDescripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoUsuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contactoUsuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="docFisicaCodigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="docFisicaDescripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidadCodigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidadDenominacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="expone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="extracto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaOrigen" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaRegistro" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="idiomaCodigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idiomaDescripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="interesados" type="{http://impl.v3.ws.regweb3.caib.es/}interesadoWs" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="libroCodigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="libroDescripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreUsuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numExpediente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numTransporte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroRegistro" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="numeroRegistroFormateado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroRegistroOrigen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="observaciones" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="oficinaCodigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="oficinaDenominacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="refExterna" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="solicita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoAsuntoCodigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoAsuntoDescripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoTransporteCodigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoTransporteDescripcion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="version" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "registroResponseWs", propOrder = {
    "anexos",
    "aplicacion",
        "codigoAsuntoCodigo",
        "codigoAsuntoDescripcion",
    "codigoUsuario",
    "contactoUsuario",
        "docFisicaCodigo",
        "docFisicaDescripcion",
    "entidadCodigo",
    "entidadDenominacion",
    "expone",
    "extracto",
    "fechaOrigen",
    "fechaRegistro",
        "idiomaCodigo",
        "idiomaDescripcion",
    "interesados",
        "libroCodigo",
        "libroDescripcion",
    "nombreUsuario",
    "numExpediente",
    "numTransporte",
    "numeroRegistro",
    "numeroRegistroFormateado",
    "numeroRegistroOrigen",
    "observaciones",
    "oficinaCodigo",
    "oficinaDenominacion",
    "refExterna",
    "solicita",
        "tipoAsuntoCodigo",
        "tipoAsuntoDescripcion",
        "tipoTransporteCodigo",
        "tipoTransporteDescripcion",
    "version"
})
@XmlSeeAlso({
    RegistroSalidaResponseWs.class
})
public class RegistroResponseWs {

    @XmlElement(nillable = true)
    protected List<AnexoWs> anexos;
    protected String aplicacion;
    protected String codigoAsuntoCodigo;
    protected String codigoAsuntoDescripcion;
    protected String codigoUsuario;
    protected String contactoUsuario;
    protected String docFisicaCodigo;
    protected String docFisicaDescripcion;
    protected String entidadCodigo;
    protected String entidadDenominacion;
    protected String expone;
    protected String extracto;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Timestamp fechaOrigen;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Timestamp fechaRegistro;
    protected String idiomaCodigo;
    protected String idiomaDescripcion;
    @XmlElement(nillable = true)
    protected List<InteresadoWs> interesados;
    protected String libroCodigo;
    protected String libroDescripcion;
    protected String nombreUsuario;
    protected String numExpediente;
    protected String numTransporte;
    protected int numeroRegistro;
    protected String numeroRegistroFormateado;
    protected String numeroRegistroOrigen;
    protected String observaciones;
    protected String oficinaCodigo;
    protected String oficinaDenominacion;
    protected String refExterna;
    protected String solicita;
    protected String tipoAsuntoCodigo;
    protected String tipoAsuntoDescripcion;
    protected String tipoTransporteCodigo;
    protected String tipoTransporteDescripcion;
    protected String version;

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
     * {@link AnexoWs }
     * 
     * 
     */
    public List<AnexoWs> getAnexos() {
        if (anexos == null) {
            anexos = new ArrayList<AnexoWs>();
        }
        return this.anexos;
    }

    /**
     * Gets the value of the aplicacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAplicacion() {
        return aplicacion;
    }

    /**
     * Sets the value of the aplicacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAplicacion(String value) {
        this.aplicacion = value;
    }

    /**
     * Gets the value of the codigoAsuntoCodigo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoAsuntoCodigo() {
        return codigoAsuntoCodigo;
    }

    /**
     * Sets the value of the codigoAsuntoCodigo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoAsuntoCodigo(String value) {
        this.codigoAsuntoCodigo = value;
    }

    /**
     * Gets the value of the codigoAsuntoDescripcion property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCodigoAsuntoDescripcion() {
        return codigoAsuntoDescripcion;
    }

    /**
     * Sets the value of the codigoAsuntoDescripcion property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCodigoAsuntoDescripcion(String value) {
        this.codigoAsuntoDescripcion = value;
    }

    /**
     * Gets the value of the codigoUsuario property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    /**
     * Sets the value of the codigoUsuario property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoUsuario(String value) {
        this.codigoUsuario = value;
    }

    /**
     * Gets the value of the contactoUsuario property.
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
     * Sets the value of the contactoUsuario property.
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
     * Gets the value of the docFisicaCodigo property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocFisicaCodigo() {
        return docFisicaCodigo;
    }

    /**
     * Sets the value of the docFisicaCodigo property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocFisicaCodigo(String value) {
        this.docFisicaCodigo = value;
    }

    /**
     * Gets the value of the docFisicaDescripcion property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDocFisicaDescripcion() {
        return docFisicaDescripcion;
    }

    /**
     * Sets the value of the docFisicaDescripcion property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDocFisicaDescripcion(String value) {
        this.docFisicaDescripcion = value;
    }

    /**
     * Gets the value of the entidadCodigo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidadCodigo() {
        return entidadCodigo;
    }

    /**
     * Sets the value of the entidadCodigo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidadCodigo(String value) {
        this.entidadCodigo = value;
    }

    /**
     * Gets the value of the entidadDenominacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidadDenominacion() {
        return entidadDenominacion;
    }

    /**
     * Sets the value of the entidadDenominacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidadDenominacion(String value) {
        this.entidadDenominacion = value;
    }

    /**
     * Gets the value of the expone property.
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
     * Sets the value of the expone property.
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
     * Gets the value of the extracto property.
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
     * Sets the value of the extracto property.
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
     * Gets the value of the fechaOrigen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Timestamp getFechaOrigen() {
        return fechaOrigen;
    }

    /**
     * Sets the value of the fechaOrigen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaOrigen(Timestamp value) {
        this.fechaOrigen = value;
    }

    /**
     * Gets the value of the fechaRegistro property.
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
     * Sets the value of the fechaRegistro property.
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
     * Gets the value of the idiomaCodigo property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdiomaCodigo() {
        return idiomaCodigo;
    }

    /**
     * Sets the value of the idiomaCodigo property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdiomaCodigo(String value) {
        this.idiomaCodigo = value;
    }

    /**
     * Gets the value of the idiomaDescripcion property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIdiomaDescripcion() {
        return idiomaDescripcion;
    }

    /**
     * Sets the value of the idiomaDescripcion property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setIdiomaDescripcion(String value) {
        this.idiomaDescripcion = value;
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
     * Gets the value of the libroCodigo property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibroCodigo() {
        return libroCodigo;
    }

    /**
     * Sets the value of the libroCodigo property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibroCodigo(String value) {
        this.libroCodigo = value;
    }

    /**
     * Gets the value of the libroDescripcion property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLibroDescripcion() {
        return libroDescripcion;
    }

    /**
     * Sets the value of the libroDescripcion property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLibroDescripcion(String value) {
        this.libroDescripcion = value;
    }

    /**
     * Gets the value of the nombreUsuario property.
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
     * Sets the value of the nombreUsuario property.
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
     * Gets the value of the numExpediente property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumExpediente() {
        return numExpediente;
    }

    /**
     * Sets the value of the numExpediente property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumExpediente(String value) {
        this.numExpediente = value;
    }

    /**
     * Gets the value of the numTransporte property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumTransporte() {
        return numTransporte;
    }

    /**
     * Sets the value of the numTransporte property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumTransporte(String value) {
        this.numTransporte = value;
    }

    /**
     * Gets the value of the numeroRegistro property.
     * 
     */
    public int getNumeroRegistro() {
        return numeroRegistro;
    }

    /**
     * Sets the value of the numeroRegistro property.
     * 
     */
    public void setNumeroRegistro(int value) {
        this.numeroRegistro = value;
    }

    /**
     * Gets the value of the numeroRegistroFormateado property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroRegistroFormateado() {
        return numeroRegistroFormateado;
    }

    /**
     * Sets the value of the numeroRegistroFormateado property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroRegistroFormateado(String value) {
        this.numeroRegistroFormateado = value;
    }

    /**
     * Gets the value of the numeroRegistroOrigen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroRegistroOrigen() {
        return numeroRegistroOrigen;
    }

    /**
     * Sets the value of the numeroRegistroOrigen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroRegistroOrigen(String value) {
        this.numeroRegistroOrigen = value;
    }

    /**
     * Gets the value of the observaciones property.
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
     * Sets the value of the observaciones property.
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
     * Gets the value of the oficinaCodigo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOficinaCodigo() {
        return oficinaCodigo;
    }

    /**
     * Sets the value of the oficinaCodigo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOficinaCodigo(String value) {
        this.oficinaCodigo = value;
    }

    /**
     * Gets the value of the oficinaDenominacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOficinaDenominacion() {
        return oficinaDenominacion;
    }

    /**
     * Sets the value of the oficinaDenominacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOficinaDenominacion(String value) {
        this.oficinaDenominacion = value;
    }

    /**
     * Gets the value of the refExterna property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRefExterna() {
        return refExterna;
    }

    /**
     * Sets the value of the refExterna property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRefExterna(String value) {
        this.refExterna = value;
    }

    /**
     * Gets the value of the solicita property.
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
     * Sets the value of the solicita property.
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
     * Gets the value of the tipoAsuntoCodigo property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoAsuntoCodigo() {
        return tipoAsuntoCodigo;
    }

    /**
     * Sets the value of the tipoAsuntoCodigo property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoAsuntoCodigo(String value) {
        this.tipoAsuntoCodigo = value;
    }

    /**
     * Gets the value of the tipoAsuntoDescripcion property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoAsuntoDescripcion() {
        return tipoAsuntoDescripcion;
    }

    /**
     * Sets the value of the tipoAsuntoDescripcion property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoAsuntoDescripcion(String value) {
        this.tipoAsuntoDescripcion = value;
    }

    /**
     * Gets the value of the tipoTransporteCodigo property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTipoTransporteCodigo() {
        return tipoTransporteCodigo;
    }

    /**
     * Sets the value of the tipoTransporteCodigo property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTipoTransporteCodigo(String value) {
        this.tipoTransporteCodigo = value;
    }

    /**
     * Gets the value of the tipoTransporteDescripcion property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTipoTransporteDescripcion() {
        return tipoTransporteDescripcion;
    }

    /**
     * Sets the value of the tipoTransporteDescripcion property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTipoTransporteDescripcion(String value) {
        this.tipoTransporteDescripcion = value;
    }

    /**
     * Gets the value of the version property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the value of the version property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVersion(String value) {
        this.version = value;
    }

}
