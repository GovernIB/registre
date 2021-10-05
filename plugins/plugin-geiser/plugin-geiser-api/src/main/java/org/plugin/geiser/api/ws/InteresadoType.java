
package org.plugin.geiser.api.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para interesadoType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="interesadoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tipoIdentificadorInteresado" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}tipoIdentificacionEnum" minOccurs="0"/>
 *         &lt;element name="identificadorInteresado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreInteresado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="primerApellidoInteresado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="segundoApellidoInteresado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="razonSocialInteresado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cdPaisInteresado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cdProvinciaInteresado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cdMunicipioInteresado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="direccionInteresado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoPostalInteresado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mailInteresado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="telefonoInteresado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="canalNotificacionInteresado" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}canalNotificacionEnum" minOccurs="0"/>
 *         &lt;element name="direccionElectronicaInteresado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoIdentificadorRepresentante" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}tipoIdentificacionEnum" minOccurs="0"/>
 *         &lt;element name="identificadorRepresentante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreRepresentante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="primerApellidoRepresentante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="segundoApellidoRepresentante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="razonSocialRepresentante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cdPaisRepresentante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cdProvinciaRepresentante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cdMunicipioRepresentante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="direccionRepresentante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoPostalRepresentante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mailRepresentante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="telefonoRepresentante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="canalNotificacionRepresentante" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}canalNotificacionEnum" minOccurs="0"/>
 *         &lt;element name="direccionElectronicaRepresentante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "interesadoType", propOrder = {
    "tipoIdentificadorInteresado",
    "identificadorInteresado",
    "nombreInteresado",
    "primerApellidoInteresado",
    "segundoApellidoInteresado",
    "razonSocialInteresado",
    "cdPaisInteresado",
    "cdProvinciaInteresado",
    "cdMunicipioInteresado",
    "direccionInteresado",
    "codigoPostalInteresado",
    "mailInteresado",
    "telefonoInteresado",
    "canalNotificacionInteresado",
    "direccionElectronicaInteresado",
    "tipoIdentificadorRepresentante",
    "identificadorRepresentante",
    "nombreRepresentante",
    "primerApellidoRepresentante",
    "segundoApellidoRepresentante",
    "razonSocialRepresentante",
    "cdPaisRepresentante",
    "cdProvinciaRepresentante",
    "cdMunicipioRepresentante",
    "direccionRepresentante",
    "codigoPostalRepresentante",
    "mailRepresentante",
    "telefonoRepresentante",
    "canalNotificacionRepresentante",
    "direccionElectronicaRepresentante",
    "observaciones"
})
public class InteresadoType {

    protected TipoIdentificacionEnum tipoIdentificadorInteresado;
    protected String identificadorInteresado;
    protected String nombreInteresado;
    protected String primerApellidoInteresado;
    protected String segundoApellidoInteresado;
    protected String razonSocialInteresado;
    protected String cdPaisInteresado;
    protected String cdProvinciaInteresado;
    protected String cdMunicipioInteresado;
    protected String direccionInteresado;
    protected String codigoPostalInteresado;
    protected String mailInteresado;
    protected String telefonoInteresado;
    protected CanalNotificacionEnum canalNotificacionInteresado;
    protected String direccionElectronicaInteresado;
    protected TipoIdentificacionEnum tipoIdentificadorRepresentante;
    protected String identificadorRepresentante;
    protected String nombreRepresentante;
    protected String primerApellidoRepresentante;
    protected String segundoApellidoRepresentante;
    protected String razonSocialRepresentante;
    protected String cdPaisRepresentante;
    protected String cdProvinciaRepresentante;
    protected String cdMunicipioRepresentante;
    protected String direccionRepresentante;
    protected String codigoPostalRepresentante;
    protected String mailRepresentante;
    protected String telefonoRepresentante;
    protected CanalNotificacionEnum canalNotificacionRepresentante;
    protected String direccionElectronicaRepresentante;
    protected String observaciones;

    /**
     * Obtiene el valor de la propiedad tipoIdentificadorInteresado.
     * 
     * @return
     *     possible object is
     *     {@link TipoIdentificacionEnum }
     *     
     */
    public TipoIdentificacionEnum getTipoIdentificadorInteresado() {
        return tipoIdentificadorInteresado;
    }

    /**
     * Define el valor de la propiedad tipoIdentificadorInteresado.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoIdentificacionEnum }
     *     
     */
    public void setTipoIdentificadorInteresado(TipoIdentificacionEnum value) {
        this.tipoIdentificadorInteresado = value;
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
     * Obtiene el valor de la propiedad nombreInteresado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreInteresado() {
        return nombreInteresado;
    }

    /**
     * Define el valor de la propiedad nombreInteresado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreInteresado(String value) {
        this.nombreInteresado = value;
    }

    /**
     * Obtiene el valor de la propiedad primerApellidoInteresado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimerApellidoInteresado() {
        return primerApellidoInteresado;
    }

    /**
     * Define el valor de la propiedad primerApellidoInteresado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimerApellidoInteresado(String value) {
        this.primerApellidoInteresado = value;
    }

    /**
     * Obtiene el valor de la propiedad segundoApellidoInteresado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSegundoApellidoInteresado() {
        return segundoApellidoInteresado;
    }

    /**
     * Define el valor de la propiedad segundoApellidoInteresado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSegundoApellidoInteresado(String value) {
        this.segundoApellidoInteresado = value;
    }

    /**
     * Obtiene el valor de la propiedad razonSocialInteresado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRazonSocialInteresado() {
        return razonSocialInteresado;
    }

    /**
     * Define el valor de la propiedad razonSocialInteresado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRazonSocialInteresado(String value) {
        this.razonSocialInteresado = value;
    }

    /**
     * Obtiene el valor de la propiedad cdPaisInteresado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCdPaisInteresado() {
        return cdPaisInteresado;
    }

    /**
     * Define el valor de la propiedad cdPaisInteresado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCdPaisInteresado(String value) {
        this.cdPaisInteresado = value;
    }

    /**
     * Obtiene el valor de la propiedad cdProvinciaInteresado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCdProvinciaInteresado() {
        return cdProvinciaInteresado;
    }

    /**
     * Define el valor de la propiedad cdProvinciaInteresado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCdProvinciaInteresado(String value) {
        this.cdProvinciaInteresado = value;
    }

    /**
     * Obtiene el valor de la propiedad cdMunicipioInteresado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCdMunicipioInteresado() {
        return cdMunicipioInteresado;
    }

    /**
     * Define el valor de la propiedad cdMunicipioInteresado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCdMunicipioInteresado(String value) {
        this.cdMunicipioInteresado = value;
    }

    /**
     * Obtiene el valor de la propiedad direccionInteresado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDireccionInteresado() {
        return direccionInteresado;
    }

    /**
     * Define el valor de la propiedad direccionInteresado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDireccionInteresado(String value) {
        this.direccionInteresado = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoPostalInteresado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoPostalInteresado() {
        return codigoPostalInteresado;
    }

    /**
     * Define el valor de la propiedad codigoPostalInteresado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoPostalInteresado(String value) {
        this.codigoPostalInteresado = value;
    }

    /**
     * Obtiene el valor de la propiedad mailInteresado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMailInteresado() {
        return mailInteresado;
    }

    /**
     * Define el valor de la propiedad mailInteresado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMailInteresado(String value) {
        this.mailInteresado = value;
    }

    /**
     * Obtiene el valor de la propiedad telefonoInteresado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefonoInteresado() {
        return telefonoInteresado;
    }

    /**
     * Define el valor de la propiedad telefonoInteresado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefonoInteresado(String value) {
        this.telefonoInteresado = value;
    }

    /**
     * Obtiene el valor de la propiedad canalNotificacionInteresado.
     * 
     * @return
     *     possible object is
     *     {@link CanalNotificacionEnum }
     *     
     */
    public CanalNotificacionEnum getCanalNotificacionInteresado() {
        return canalNotificacionInteresado;
    }

    /**
     * Define el valor de la propiedad canalNotificacionInteresado.
     * 
     * @param value
     *     allowed object is
     *     {@link CanalNotificacionEnum }
     *     
     */
    public void setCanalNotificacionInteresado(CanalNotificacionEnum value) {
        this.canalNotificacionInteresado = value;
    }

    /**
     * Obtiene el valor de la propiedad direccionElectronicaInteresado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDireccionElectronicaInteresado() {
        return direccionElectronicaInteresado;
    }

    /**
     * Define el valor de la propiedad direccionElectronicaInteresado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDireccionElectronicaInteresado(String value) {
        this.direccionElectronicaInteresado = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoIdentificadorRepresentante.
     * 
     * @return
     *     possible object is
     *     {@link TipoIdentificacionEnum }
     *     
     */
    public TipoIdentificacionEnum getTipoIdentificadorRepresentante() {
        return tipoIdentificadorRepresentante;
    }

    /**
     * Define el valor de la propiedad tipoIdentificadorRepresentante.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoIdentificacionEnum }
     *     
     */
    public void setTipoIdentificadorRepresentante(TipoIdentificacionEnum value) {
        this.tipoIdentificadorRepresentante = value;
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

    /**
     * Obtiene el valor de la propiedad nombreRepresentante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombreRepresentante() {
        return nombreRepresentante;
    }

    /**
     * Define el valor de la propiedad nombreRepresentante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombreRepresentante(String value) {
        this.nombreRepresentante = value;
    }

    /**
     * Obtiene el valor de la propiedad primerApellidoRepresentante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimerApellidoRepresentante() {
        return primerApellidoRepresentante;
    }

    /**
     * Define el valor de la propiedad primerApellidoRepresentante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimerApellidoRepresentante(String value) {
        this.primerApellidoRepresentante = value;
    }

    /**
     * Obtiene el valor de la propiedad segundoApellidoRepresentante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSegundoApellidoRepresentante() {
        return segundoApellidoRepresentante;
    }

    /**
     * Define el valor de la propiedad segundoApellidoRepresentante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSegundoApellidoRepresentante(String value) {
        this.segundoApellidoRepresentante = value;
    }

    /**
     * Obtiene el valor de la propiedad razonSocialRepresentante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRazonSocialRepresentante() {
        return razonSocialRepresentante;
    }

    /**
     * Define el valor de la propiedad razonSocialRepresentante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRazonSocialRepresentante(String value) {
        this.razonSocialRepresentante = value;
    }

    /**
     * Obtiene el valor de la propiedad cdPaisRepresentante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCdPaisRepresentante() {
        return cdPaisRepresentante;
    }

    /**
     * Define el valor de la propiedad cdPaisRepresentante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCdPaisRepresentante(String value) {
        this.cdPaisRepresentante = value;
    }

    /**
     * Obtiene el valor de la propiedad cdProvinciaRepresentante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCdProvinciaRepresentante() {
        return cdProvinciaRepresentante;
    }

    /**
     * Define el valor de la propiedad cdProvinciaRepresentante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCdProvinciaRepresentante(String value) {
        this.cdProvinciaRepresentante = value;
    }

    /**
     * Obtiene el valor de la propiedad cdMunicipioRepresentante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCdMunicipioRepresentante() {
        return cdMunicipioRepresentante;
    }

    /**
     * Define el valor de la propiedad cdMunicipioRepresentante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCdMunicipioRepresentante(String value) {
        this.cdMunicipioRepresentante = value;
    }

    /**
     * Obtiene el valor de la propiedad direccionRepresentante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDireccionRepresentante() {
        return direccionRepresentante;
    }

    /**
     * Define el valor de la propiedad direccionRepresentante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDireccionRepresentante(String value) {
        this.direccionRepresentante = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoPostalRepresentante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoPostalRepresentante() {
        return codigoPostalRepresentante;
    }

    /**
     * Define el valor de la propiedad codigoPostalRepresentante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoPostalRepresentante(String value) {
        this.codigoPostalRepresentante = value;
    }

    /**
     * Obtiene el valor de la propiedad mailRepresentante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMailRepresentante() {
        return mailRepresentante;
    }

    /**
     * Define el valor de la propiedad mailRepresentante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMailRepresentante(String value) {
        this.mailRepresentante = value;
    }

    /**
     * Obtiene el valor de la propiedad telefonoRepresentante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefonoRepresentante() {
        return telefonoRepresentante;
    }

    /**
     * Define el valor de la propiedad telefonoRepresentante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefonoRepresentante(String value) {
        this.telefonoRepresentante = value;
    }

    /**
     * Obtiene el valor de la propiedad canalNotificacionRepresentante.
     * 
     * @return
     *     possible object is
     *     {@link CanalNotificacionEnum }
     *     
     */
    public CanalNotificacionEnum getCanalNotificacionRepresentante() {
        return canalNotificacionRepresentante;
    }

    /**
     * Define el valor de la propiedad canalNotificacionRepresentante.
     * 
     * @param value
     *     allowed object is
     *     {@link CanalNotificacionEnum }
     *     
     */
    public void setCanalNotificacionRepresentante(CanalNotificacionEnum value) {
        this.canalNotificacionRepresentante = value;
    }

    /**
     * Obtiene el valor de la propiedad direccionElectronicaRepresentante.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDireccionElectronicaRepresentante() {
        return direccionElectronicaRepresentante;
    }

    /**
     * Define el valor de la propiedad direccionElectronicaRepresentante.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDireccionElectronicaRepresentante(String value) {
        this.direccionElectronicaRepresentante = value;
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
