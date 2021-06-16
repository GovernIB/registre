
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Clase Java para asientoRegistralWs complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="asientoRegistralWs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="anexos" type="{http://impl.v3.ws.regweb3.caib.es/}anexoWs" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="aplicacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="aplicacionTelematica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoAsunto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoAsuntoDenominacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoEntidadRegistralProcesado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoError" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoSia" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="codigoUsuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="decodificacionEntidadRegistralProcesado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descripcionError" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidadCodigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidadDenominacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidadRegistralDestinoCodigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidadRegistralDestinoDenominacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidadRegistralInicioCodigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidadRegistralInicioDenominacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidadRegistralOrigenCodigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidadRegistralOrigenDenominacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="expone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fechaRecepcion" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaRegistro" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="fechaRegistroDestino" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="identificadorIntercambio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idioma" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="interesados" type="{http://impl.v3.ws.regweb3.caib.es/}interesadoWs" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="libroCodigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="motivo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroExpediente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroRegistro" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="numeroRegistroDestino" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroRegistroFormateado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroTransporte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="observaciones" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="presencial" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="referenciaExterna" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="resumen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="solicita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoDocumentacionFisicaCodigo" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="tipoEnvioDocumentacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoRegistro" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="tipoTransporte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unidadTramitacionDestinoCodigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unidadTramitacionDestinoDenominacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unidadTramitacionOrigenCodigo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unidadTramitacionOrigenDenominacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "asientoRegistralWs", propOrder = {
    "anexos",
    "aplicacion",
    "aplicacionTelematica",
    "codigoAsunto",
    "codigoAsuntoDenominacion",
    "codigoEntidadRegistralProcesado",
    "codigoError",
    "codigoSia",
    "codigoUsuario",
    "decodificacionEntidadRegistralProcesado",
    "descripcionError",
    "entidadCodigo",
    "entidadDenominacion",
    "entidadRegistralDestinoCodigo",
    "entidadRegistralDestinoDenominacion",
    "entidadRegistralInicioCodigo",
    "entidadRegistralInicioDenominacion",
    "entidadRegistralOrigenCodigo",
    "entidadRegistralOrigenDenominacion",
    "estado",
    "expone",
    "fechaRecepcion",
    "fechaRegistro",
    "fechaRegistroDestino",
    "id",
    "identificadorIntercambio",
    "idioma",
    "interesados",
    "libroCodigo",
    "motivo",
    "numeroExpediente",
    "numeroRegistro",
    "numeroRegistroDestino",
    "numeroRegistroFormateado",
    "numeroTransporte",
    "observaciones",
    "presencial",
    "referenciaExterna",
    "resumen",
    "solicita",
    "tipoDocumentacionFisicaCodigo",
    "tipoEnvioDocumentacion",
    "tipoRegistro",
    "tipoTransporte",
    "unidadTramitacionDestinoCodigo",
    "unidadTramitacionDestinoDenominacion",
    "unidadTramitacionOrigenCodigo",
    "unidadTramitacionOrigenDenominacion",
    "version"
})
public class AsientoRegistralWs {

    @XmlElement(nillable = true)
    protected List<AnexoWs> anexos;
    protected String aplicacion;
    protected String aplicacionTelematica;
    protected String codigoAsunto;
    protected String codigoAsuntoDenominacion;
    protected String codigoEntidadRegistralProcesado;
    protected String codigoError;
    protected Long codigoSia;
    protected String codigoUsuario;
    protected String decodificacionEntidadRegistralProcesado;
    protected String descripcionError;
    protected String entidadCodigo;
    protected String entidadDenominacion;
    protected String entidadRegistralDestinoCodigo;
    protected String entidadRegistralDestinoDenominacion;
    protected String entidadRegistralInicioCodigo;
    protected String entidadRegistralInicioDenominacion;
    protected String entidadRegistralOrigenCodigo;
    protected String entidadRegistralOrigenDenominacion;
    protected Long estado;
    protected String expone;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Timestamp fechaRecepcion;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Timestamp fechaRegistro;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Timestamp fechaRegistroDestino;
    protected Long id;
    protected String identificadorIntercambio;
    protected Long idioma;
    @XmlElement(nillable = true)
    protected List<InteresadoWs> interesados;
    protected String libroCodigo;
    protected String motivo;
    protected String numeroExpediente;
    protected int numeroRegistro;
    protected String numeroRegistroDestino;
    protected String numeroRegistroFormateado;
    protected String numeroTransporte;
    protected String observaciones;
    protected Boolean presencial;
    protected String referenciaExterna;
    protected String resumen;
    protected String solicita;
    protected Long tipoDocumentacionFisicaCodigo;
    protected String tipoEnvioDocumentacion;
    protected Long tipoRegistro;
    protected String tipoTransporte;
    protected String unidadTramitacionDestinoCodigo;
    protected String unidadTramitacionDestinoDenominacion;
    protected String unidadTramitacionOrigenCodigo;
    protected String unidadTramitacionOrigenDenominacion;
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
     * Obtiene el valor de la propiedad aplicacionTelematica.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAplicacionTelematica() {
        return aplicacionTelematica;
    }

    /**
     * Define el valor de la propiedad aplicacionTelematica.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAplicacionTelematica(String value) {
        this.aplicacionTelematica = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoAsunto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoAsunto() {
        return codigoAsunto;
    }

    /**
     * Define el valor de la propiedad codigoAsunto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoAsunto(String value) {
        this.codigoAsunto = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoAsuntoDenominacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoAsuntoDenominacion() {
        return codigoAsuntoDenominacion;
    }

    /**
     * Define el valor de la propiedad codigoAsuntoDenominacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoAsuntoDenominacion(String value) {
        this.codigoAsuntoDenominacion = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoEntidadRegistralProcesado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoEntidadRegistralProcesado() {
        return codigoEntidadRegistralProcesado;
    }

    /**
     * Define el valor de la propiedad codigoEntidadRegistralProcesado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoEntidadRegistralProcesado(String value) {
        this.codigoEntidadRegistralProcesado = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoError.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoError() {
        return codigoError;
    }

    /**
     * Define el valor de la propiedad codigoError.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoError(String value) {
        this.codigoError = value;
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
     * Obtiene el valor de la propiedad decodificacionEntidadRegistralProcesado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDecodificacionEntidadRegistralProcesado() {
        return decodificacionEntidadRegistralProcesado;
    }

    /**
     * Define el valor de la propiedad decodificacionEntidadRegistralProcesado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDecodificacionEntidadRegistralProcesado(String value) {
        this.decodificacionEntidadRegistralProcesado = value;
    }

    /**
     * Obtiene el valor de la propiedad descripcionError.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcionError() {
        return descripcionError;
    }

    /**
     * Define el valor de la propiedad descripcionError.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcionError(String value) {
        this.descripcionError = value;
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
     * Obtiene el valor de la propiedad entidadRegistralDestinoCodigo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidadRegistralDestinoCodigo() {
        return entidadRegistralDestinoCodigo;
    }

    /**
     * Define el valor de la propiedad entidadRegistralDestinoCodigo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidadRegistralDestinoCodigo(String value) {
        this.entidadRegistralDestinoCodigo = value;
    }

    /**
     * Obtiene el valor de la propiedad entidadRegistralDestinoDenominacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidadRegistralDestinoDenominacion() {
        return entidadRegistralDestinoDenominacion;
    }

    /**
     * Define el valor de la propiedad entidadRegistralDestinoDenominacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidadRegistralDestinoDenominacion(String value) {
        this.entidadRegistralDestinoDenominacion = value;
    }

    /**
     * Obtiene el valor de la propiedad entidadRegistralInicioCodigo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidadRegistralInicioCodigo() {
        return entidadRegistralInicioCodigo;
    }

    /**
     * Define el valor de la propiedad entidadRegistralInicioCodigo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidadRegistralInicioCodigo(String value) {
        this.entidadRegistralInicioCodigo = value;
    }

    /**
     * Obtiene el valor de la propiedad entidadRegistralInicioDenominacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidadRegistralInicioDenominacion() {
        return entidadRegistralInicioDenominacion;
    }

    /**
     * Define el valor de la propiedad entidadRegistralInicioDenominacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidadRegistralInicioDenominacion(String value) {
        this.entidadRegistralInicioDenominacion = value;
    }

    /**
     * Obtiene el valor de la propiedad entidadRegistralOrigenCodigo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidadRegistralOrigenCodigo() {
        return entidadRegistralOrigenCodigo;
    }

    /**
     * Define el valor de la propiedad entidadRegistralOrigenCodigo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidadRegistralOrigenCodigo(String value) {
        this.entidadRegistralOrigenCodigo = value;
    }

    /**
     * Obtiene el valor de la propiedad entidadRegistralOrigenDenominacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidadRegistralOrigenDenominacion() {
        return entidadRegistralOrigenDenominacion;
    }

    /**
     * Define el valor de la propiedad entidadRegistralOrigenDenominacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidadRegistralOrigenDenominacion(String value) {
        this.entidadRegistralOrigenDenominacion = value;
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
     * Obtiene el valor de la propiedad fechaRecepcion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Timestamp getFechaRecepcion() {
        return fechaRecepcion;
    }

    /**
     * Define el valor de la propiedad fechaRecepcion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaRecepcion(Timestamp value) {
        this.fechaRecepcion = value;
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
     * Obtiene el valor de la propiedad fechaRegistroDestino.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Timestamp getFechaRegistroDestino() {
        return fechaRegistroDestino;
    }

    /**
     * Define el valor de la propiedad fechaRegistroDestino.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaRegistroDestino(Timestamp value) {
        this.fechaRegistroDestino = value;
    }

    /**
     * Obtiene el valor de la propiedad id.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Define el valor de la propiedad id.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Obtiene el valor de la propiedad identificadorIntercambio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificadorIntercambio() {
        return identificadorIntercambio;
    }

    /**
     * Define el valor de la propiedad identificadorIntercambio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificadorIntercambio(String value) {
        this.identificadorIntercambio = value;
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
     * Obtiene el valor de la propiedad motivo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * Define el valor de la propiedad motivo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMotivo(String value) {
        this.motivo = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroExpediente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    /**
     * Define el valor de la propiedad numeroExpediente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroExpediente(String value) {
        this.numeroExpediente = value;
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
     * Obtiene el valor de la propiedad numeroRegistroDestino.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroRegistroDestino() {
        return numeroRegistroDestino;
    }

    /**
     * Define el valor de la propiedad numeroRegistroDestino.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroRegistroDestino(String value) {
        this.numeroRegistroDestino = value;
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
     * Obtiene el valor de la propiedad numeroTransporte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroTransporte() {
        return numeroTransporte;
    }

    /**
     * Define el valor de la propiedad numeroTransporte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroTransporte(String value) {
        this.numeroTransporte = value;
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
     * Obtiene el valor de la propiedad tipoDocumentacionFisicaCodigo.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTipoDocumentacionFisicaCodigo() {
        return tipoDocumentacionFisicaCodigo;
    }

    /**
     * Define el valor de la propiedad tipoDocumentacionFisicaCodigo.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTipoDocumentacionFisicaCodigo(Long value) {
        this.tipoDocumentacionFisicaCodigo = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoEnvioDocumentacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoEnvioDocumentacion() {
        return tipoEnvioDocumentacion;
    }

    /**
     * Define el valor de la propiedad tipoEnvioDocumentacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoEnvioDocumentacion(String value) {
        this.tipoEnvioDocumentacion = value;
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

    /**
     * Obtiene el valor de la propiedad tipoTransporte.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoTransporte() {
        return tipoTransporte;
    }

    /**
     * Define el valor de la propiedad tipoTransporte.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoTransporte(String value) {
        this.tipoTransporte = value;
    }

    /**
     * Obtiene el valor de la propiedad unidadTramitacionDestinoCodigo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnidadTramitacionDestinoCodigo() {
        return unidadTramitacionDestinoCodigo;
    }

    /**
     * Define el valor de la propiedad unidadTramitacionDestinoCodigo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnidadTramitacionDestinoCodigo(String value) {
        this.unidadTramitacionDestinoCodigo = value;
    }

    /**
     * Obtiene el valor de la propiedad unidadTramitacionDestinoDenominacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnidadTramitacionDestinoDenominacion() {
        return unidadTramitacionDestinoDenominacion;
    }

    /**
     * Define el valor de la propiedad unidadTramitacionDestinoDenominacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnidadTramitacionDestinoDenominacion(String value) {
        this.unidadTramitacionDestinoDenominacion = value;
    }

    /**
     * Obtiene el valor de la propiedad unidadTramitacionOrigenCodigo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnidadTramitacionOrigenCodigo() {
        return unidadTramitacionOrigenCodigo;
    }

    /**
     * Define el valor de la propiedad unidadTramitacionOrigenCodigo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnidadTramitacionOrigenCodigo(String value) {
        this.unidadTramitacionOrigenCodigo = value;
    }

    /**
     * Obtiene el valor de la propiedad unidadTramitacionOrigenDenominacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnidadTramitacionOrigenDenominacion() {
        return unidadTramitacionOrigenDenominacion;
    }

    /**
     * Define el valor de la propiedad unidadTramitacionOrigenDenominacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnidadTramitacionOrigenDenominacion(String value) {
        this.unidadTramitacionOrigenDenominacion = value;
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
