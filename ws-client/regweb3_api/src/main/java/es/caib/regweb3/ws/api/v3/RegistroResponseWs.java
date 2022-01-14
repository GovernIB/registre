
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Clase Java para registroResponseWs complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
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
     * Obtiene el valor de la propiedad aplicacion.
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
     * Define el valor de la propiedad aplicacion.
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
     * Obtiene el valor de la propiedad codigoAsuntoCodigo.
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
     * Define el valor de la propiedad codigoAsuntoCodigo.
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
     * Obtiene el valor de la propiedad codigoAsuntoDescripcion.
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
     * Define el valor de la propiedad codigoAsuntoDescripcion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoAsuntoDescripcion(String value) {
        this.codigoAsuntoDescripcion = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoUsuario.
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
     * Define el valor de la propiedad codigoUsuario.
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
     * Obtiene el valor de la propiedad docFisicaCodigo.
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
     * Define el valor de la propiedad docFisicaCodigo.
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
     * Obtiene el valor de la propiedad docFisicaDescripcion.
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
     * Define el valor de la propiedad docFisicaDescripcion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocFisicaDescripcion(String value) {
        this.docFisicaDescripcion = value;
    }

    /**
     * Obtiene el valor de la propiedad entidadCodigo.
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
     * Define el valor de la propiedad entidadCodigo.
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
     * Obtiene el valor de la propiedad entidadDenominacion.
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
     * Define el valor de la propiedad entidadDenominacion.
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
     * Obtiene el valor de la propiedad fechaOrigen.
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
     * Define el valor de la propiedad fechaOrigen.
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
     * Obtiene el valor de la propiedad idiomaCodigo.
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
     * Define el valor de la propiedad idiomaCodigo.
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
     * Obtiene el valor de la propiedad idiomaDescripcion.
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
     * Define el valor de la propiedad idiomaDescripcion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
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
     * Obtiene el valor de la propiedad libroCodigo.
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
     * Define el valor de la propiedad libroCodigo.
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
     * Obtiene el valor de la propiedad libroDescripcion.
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
     * Define el valor de la propiedad libroDescripcion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibroDescripcion(String value) {
        this.libroDescripcion = value;
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
     * Obtiene el valor de la propiedad numExpediente.
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
     * Define el valor de la propiedad numExpediente.
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
     * Obtiene el valor de la propiedad numTransporte.
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
     * Define el valor de la propiedad numTransporte.
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
     * Obtiene el valor de la propiedad numeroRegistro.
     * 
     */
    public int getNumeroRegistro() {
        return numeroRegistro;
    }

    /**
     * Define el valor de la propiedad numeroRegistro.
     * 
     */
    public void setNumeroRegistro(int value) {
        this.numeroRegistro = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroRegistroFormateado.
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
     * Define el valor de la propiedad numeroRegistroFormateado.
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
     * Obtiene el valor de la propiedad numeroRegistroOrigen.
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
     * Define el valor de la propiedad numeroRegistroOrigen.
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
     * Obtiene el valor de la propiedad oficinaCodigo.
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
     * Define el valor de la propiedad oficinaCodigo.
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
     * Obtiene el valor de la propiedad oficinaDenominacion.
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
     * Define el valor de la propiedad oficinaDenominacion.
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
     * Obtiene el valor de la propiedad refExterna.
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
     * Define el valor de la propiedad refExterna.
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
     * Obtiene el valor de la propiedad tipoAsuntoCodigo.
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
     * Define el valor de la propiedad tipoAsuntoCodigo.
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
     * Obtiene el valor de la propiedad tipoAsuntoDescripcion.
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
     * Define el valor de la propiedad tipoAsuntoDescripcion.
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
     * Obtiene el valor de la propiedad tipoTransporteCodigo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoTransporteCodigo() {
        return tipoTransporteCodigo;
    }

    /**
     * Define el valor de la propiedad tipoTransporteCodigo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoTransporteCodigo(String value) {
        this.tipoTransporteCodigo = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoTransporteDescripcion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoTransporteDescripcion() {
        return tipoTransporteDescripcion;
    }

    /**
     * Define el valor de la propiedad tipoTransporteDescripcion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoTransporteDescripcion(String value) {
        this.tipoTransporteDescripcion = value;
    }

    /**
     * Obtiene el valor de la propiedad version.
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
     * Define el valor de la propiedad version.
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
