
package es.caib.regweb3.ws.api.v3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para personaWs complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="personaWs">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="apellido1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="apellido2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="canal" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="codigoEntidadGeograficaDir3ID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="direccion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="direccionElectronica" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="documento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entidadDir3ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="guardarInteresado" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="localidadDir3ID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="observaciones" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="paisDir3ID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="provinciaDir3ID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="razonSocial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="telefono" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoDocumentoIdentificacionNTI" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoPersonaID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "personaWs", propOrder = {
    "apellido1",
    "apellido2",
    "canal",
    "codigoEntidadGeograficaDir3ID",
    "cp",
    "direccion",
    "direccionElectronica",
    "documento",
    "email",
    "entidadDir3ID",
    "guardarInteresado",
    "id",
    "localidadDir3ID",
    "nombre",
    "observaciones",
    "paisDir3ID",
    "provinciaDir3ID",
    "razonSocial",
    "telefono",
    "tipoDocumentoIdentificacionNTI",
    "tipoPersonaID"
})
public class PersonaWs {

    protected String apellido1;
    protected String apellido2;
    protected Long canal;
    protected String codigoEntidadGeograficaDir3ID;
    protected String cp;
    protected String direccion;
    protected String direccionElectronica;
    protected String documento;
    protected String email;
    @XmlElement(required = true)
    protected String entidadDir3ID;
    protected boolean guardarInteresado;
    protected Long id;
    protected Long localidadDir3ID;
    protected String nombre;
    protected String observaciones;
    protected Long paisDir3ID;
    protected Long provinciaDir3ID;
    protected String razonSocial;
    protected String telefono;
    protected String tipoDocumentoIdentificacionNTI;
    protected Long tipoPersonaID;

    /**
     * Obtiene el valor de la propiedad apellido1.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApellido1() {
        return apellido1;
    }

    /**
     * Define el valor de la propiedad apellido1.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApellido1(String value) {
        this.apellido1 = value;
    }

    /**
     * Obtiene el valor de la propiedad apellido2.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApellido2() {
        return apellido2;
    }

    /**
     * Define el valor de la propiedad apellido2.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApellido2(String value) {
        this.apellido2 = value;
    }

    /**
     * Obtiene el valor de la propiedad canal.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCanal() {
        return canal;
    }

    /**
     * Define el valor de la propiedad canal.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCanal(Long value) {
        this.canal = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoEntidadGeograficaDir3ID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoEntidadGeograficaDir3ID() {
        return codigoEntidadGeograficaDir3ID;
    }

    /**
     * Define el valor de la propiedad codigoEntidadGeograficaDir3ID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoEntidadGeograficaDir3ID(String value) {
        this.codigoEntidadGeograficaDir3ID = value;
    }

    /**
     * Obtiene el valor de la propiedad cp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCp() {
        return cp;
    }

    /**
     * Define el valor de la propiedad cp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCp(String value) {
        this.cp = value;
    }

    /**
     * Obtiene el valor de la propiedad direccion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Define el valor de la propiedad direccion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDireccion(String value) {
        this.direccion = value;
    }

    /**
     * Obtiene el valor de la propiedad direccionElectronica.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDireccionElectronica() {
        return direccionElectronica;
    }

    /**
     * Define el valor de la propiedad direccionElectronica.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDireccionElectronica(String value) {
        this.direccionElectronica = value;
    }

    /**
     * Obtiene el valor de la propiedad documento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumento() {
        return documento;
    }

    /**
     * Define el valor de la propiedad documento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumento(String value) {
        this.documento = value;
    }

    /**
     * Obtiene el valor de la propiedad email.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define el valor de la propiedad email.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Obtiene el valor de la propiedad entidadDir3ID.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntidadDir3ID() {
        return entidadDir3ID;
    }

    /**
     * Define el valor de la propiedad entidadDir3ID.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntidadDir3ID(String value) {
        this.entidadDir3ID = value;
    }

    /**
     * Obtiene el valor de la propiedad guardarInteresado.
     * 
     */
    public boolean isGuardarInteresado() {
        return guardarInteresado;
    }

    /**
     * Define el valor de la propiedad guardarInteresado.
     * 
     */
    public void setGuardarInteresado(boolean value) {
        this.guardarInteresado = value;
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
     * Obtiene el valor de la propiedad localidadDir3ID.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getLocalidadDir3ID() {
        return localidadDir3ID;
    }

    /**
     * Define el valor de la propiedad localidadDir3ID.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setLocalidadDir3ID(Long value) {
        this.localidadDir3ID = value;
    }

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
     * Obtiene el valor de la propiedad paisDir3ID.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getPaisDir3ID() {
        return paisDir3ID;
    }

    /**
     * Define el valor de la propiedad paisDir3ID.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setPaisDir3ID(Long value) {
        this.paisDir3ID = value;
    }

    /**
     * Obtiene el valor de la propiedad provinciaDir3ID.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getProvinciaDir3ID() {
        return provinciaDir3ID;
    }

    /**
     * Define el valor de la propiedad provinciaDir3ID.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setProvinciaDir3ID(Long value) {
        this.provinciaDir3ID = value;
    }

    /**
     * Obtiene el valor de la propiedad razonSocial.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRazonSocial() {
        return razonSocial;
    }

    /**
     * Define el valor de la propiedad razonSocial.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRazonSocial(String value) {
        this.razonSocial = value;
    }

    /**
     * Obtiene el valor de la propiedad telefono.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Define el valor de la propiedad telefono.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefono(String value) {
        this.telefono = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoDocumentoIdentificacionNTI.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDocumentoIdentificacionNTI() {
        return tipoDocumentoIdentificacionNTI;
    }

    /**
     * Define el valor de la propiedad tipoDocumentoIdentificacionNTI.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDocumentoIdentificacionNTI(String value) {
        this.tipoDocumentoIdentificacionNTI = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoPersonaID.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTipoPersonaID() {
        return tipoPersonaID;
    }

    /**
     * Define el valor de la propiedad tipoPersonaID.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTipoPersonaID(Long value) {
        this.tipoPersonaID = value;
    }

}
