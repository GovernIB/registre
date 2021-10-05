
package org.plugin.geiser.api.ws;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para apunteRegistroType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="apunteRegistroType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nuRegistro" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="timestampPresentado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="timestampRegistrado" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="justificanteFirmado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tamanioJustificanteFirmado" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="hashJustificanteFirmado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoMimeJustificanteFirmado" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="justificanteCSV" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tamanioJustificanteCVS" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="hashJustificanteCVS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoMimeJustificanteCVS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tieneFirmaJustificanteCSV" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="csv" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cdAmbitoCreacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ambitoCreacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="cdAmbitoActual" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ambitoActual" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tipoAsiento" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}tipoAsientoEnum"/>
 *         &lt;element name="estado" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}estadoAsientoEnum"/>
 *         &lt;element name="cdOrganoOrigen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="organoOrigen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cdOrganoDestino" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="organoDestino" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="interesados" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}interesadoType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="anexos" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}anexoType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="formulario" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}formularioType" minOccurs="0"/>
 *         &lt;element name="resumen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cdAsunto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="deAsunto" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cdSIA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deCdSIA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cdFormulario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deFormulario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="referenciaExterna" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nuExpediente" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoTransporte" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}tipoTransporteEnum" minOccurs="0"/>
 *         &lt;element name="nuTransporte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nombreUsuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contactoUsuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="documentacionFisica" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}tipoDocumentacionFisicaEnum"/>
 *         &lt;element name="observaciones" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="expone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="solicita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="plazos" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="silencioAdministrativo" type="{http://types.registro.ws.rgeco.geiser.minhap.gob.es/}tipoSilencioAdministrativoEnum" minOccurs="0"/>
 *         &lt;element name="asuntoInterno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoCadenaAsientos" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="timestampFactura" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nuRegistroInterno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nuRegistroOrigen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="importeFactura" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="justificanteUnidadTramitacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroFactura" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cdTipodocumento" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="deTipodocumento" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cdZonaHorariaCreacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deZonaHorariaCreacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cdZonaHorariaUsuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deZonaHorariaUsuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "apunteRegistroType", propOrder = {
    "nuRegistro",
    "timestampPresentado",
    "timestampRegistrado",
    "justificanteFirmado",
    "tamanioJustificanteFirmado",
    "hashJustificanteFirmado",
    "tipoMimeJustificanteFirmado",
    "justificanteCSV",
    "tamanioJustificanteCVS",
    "hashJustificanteCVS",
    "tipoMimeJustificanteCVS",
    "tieneFirmaJustificanteCSV",
    "csv",
    "cdAmbitoCreacion",
    "ambitoCreacion",
    "cdAmbitoActual",
    "ambitoActual",
    "tipoAsiento",
    "estado",
    "cdOrganoOrigen",
    "organoOrigen",
    "cdOrganoDestino",
    "organoDestino",
    "interesados",
    "anexos",
    "formulario",
    "resumen",
    "cdAsunto",
    "deAsunto",
    "cdSIA",
    "deCdSIA",
    "cdFormulario",
    "deFormulario",
    "referenciaExterna",
    "nuExpediente",
    "tipoTransporte",
    "nuTransporte",
    "nombreUsuario",
    "contactoUsuario",
    "documentacionFisica",
    "observaciones",
    "expone",
    "solicita",
    "plazos",
    "silencioAdministrativo",
    "asuntoInterno",
    "codigoCadenaAsientos",
    "timestampFactura",
    "nuRegistroInterno",
    "nuRegistroOrigen",
    "importeFactura",
    "justificanteUnidadTramitacion",
    "numeroFactura",
    "cdTipodocumento",
    "deTipodocumento",
    "cdZonaHorariaCreacion",
    "deZonaHorariaCreacion",
    "cdZonaHorariaUsuario",
    "deZonaHorariaUsuario"
})
public class ApunteRegistroType {

    @XmlElement(required = true)
    protected String nuRegistro;
    @XmlElement(required = true)
    protected String timestampPresentado;
    @XmlElement(required = true)
    protected String timestampRegistrado;
    protected String justificanteFirmado;
    protected Long tamanioJustificanteFirmado;
    protected String hashJustificanteFirmado;
    protected String tipoMimeJustificanteFirmado;
    protected String justificanteCSV;
    protected Long tamanioJustificanteCVS;
    protected String hashJustificanteCVS;
    protected String tipoMimeJustificanteCVS;
    protected Boolean tieneFirmaJustificanteCSV;
    @XmlElement(required = true)
    protected String csv;
    @XmlElement(required = true)
    protected String cdAmbitoCreacion;
    @XmlElement(required = true)
    protected String ambitoCreacion;
    @XmlElement(required = true)
    protected String cdAmbitoActual;
    @XmlElement(required = true)
    protected String ambitoActual;
    @XmlElement(required = true)
    protected TipoAsientoEnum tipoAsiento;
    @XmlElement(required = true)
    protected EstadoAsientoEnum estado;
    protected String cdOrganoOrigen;
    protected String organoOrigen;
    protected String cdOrganoDestino;
    protected String organoDestino;
    protected List<InteresadoType> interesados;
    protected List<AnexoType> anexos;
    protected FormularioType formulario;
    protected String resumen;
    @XmlElement(required = true)
    protected String cdAsunto;
    protected String deAsunto;
    protected String cdSIA;
    protected String deCdSIA;
    protected String cdFormulario;
    protected String deFormulario;
    protected String referenciaExterna;
    protected String nuExpediente;
    protected TipoTransporteEnum tipoTransporte;
    protected String nuTransporte;
    protected String nombreUsuario;
    protected String contactoUsuario;
    @XmlElement(required = true)
    protected TipoDocumentacionFisicaEnum documentacionFisica;
    protected String observaciones;
    protected String expone;
    protected String solicita;
    protected String plazos;
    protected TipoSilencioAdministrativoEnum silencioAdministrativo;
    protected String asuntoInterno;
    protected String codigoCadenaAsientos;
    protected String timestampFactura;
    protected String nuRegistroInterno;
    protected String nuRegistroOrigen;
    protected BigDecimal importeFactura;
    protected String justificanteUnidadTramitacion;
    protected String numeroFactura;
    protected Long cdTipodocumento;
    protected String deTipodocumento;
    protected String cdZonaHorariaCreacion;
    protected String deZonaHorariaCreacion;
    protected String cdZonaHorariaUsuario;
    protected String deZonaHorariaUsuario;

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
     * Obtiene el valor de la propiedad timestampRegistrado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimestampRegistrado() {
        return timestampRegistrado;
    }

    /**
     * Define el valor de la propiedad timestampRegistrado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimestampRegistrado(String value) {
        this.timestampRegistrado = value;
    }

    /**
     * Obtiene el valor de la propiedad justificanteFirmado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJustificanteFirmado() {
        return justificanteFirmado;
    }

    /**
     * Define el valor de la propiedad justificanteFirmado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJustificanteFirmado(String value) {
        this.justificanteFirmado = value;
    }

    /**
     * Obtiene el valor de la propiedad tamanioJustificanteFirmado.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTamanioJustificanteFirmado() {
        return tamanioJustificanteFirmado;
    }

    /**
     * Define el valor de la propiedad tamanioJustificanteFirmado.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTamanioJustificanteFirmado(Long value) {
        this.tamanioJustificanteFirmado = value;
    }

    /**
     * Obtiene el valor de la propiedad hashJustificanteFirmado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHashJustificanteFirmado() {
        return hashJustificanteFirmado;
    }

    /**
     * Define el valor de la propiedad hashJustificanteFirmado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHashJustificanteFirmado(String value) {
        this.hashJustificanteFirmado = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoMimeJustificanteFirmado.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoMimeJustificanteFirmado() {
        return tipoMimeJustificanteFirmado;
    }

    /**
     * Define el valor de la propiedad tipoMimeJustificanteFirmado.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoMimeJustificanteFirmado(String value) {
        this.tipoMimeJustificanteFirmado = value;
    }

    /**
     * Obtiene el valor de la propiedad justificanteCSV.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJustificanteCSV() {
        return justificanteCSV;
    }

    /**
     * Define el valor de la propiedad justificanteCSV.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJustificanteCSV(String value) {
        this.justificanteCSV = value;
    }

    /**
     * Obtiene el valor de la propiedad tamanioJustificanteCVS.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTamanioJustificanteCVS() {
        return tamanioJustificanteCVS;
    }

    /**
     * Define el valor de la propiedad tamanioJustificanteCVS.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTamanioJustificanteCVS(Long value) {
        this.tamanioJustificanteCVS = value;
    }

    /**
     * Obtiene el valor de la propiedad hashJustificanteCVS.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHashJustificanteCVS() {
        return hashJustificanteCVS;
    }

    /**
     * Define el valor de la propiedad hashJustificanteCVS.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHashJustificanteCVS(String value) {
        this.hashJustificanteCVS = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoMimeJustificanteCVS.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoMimeJustificanteCVS() {
        return tipoMimeJustificanteCVS;
    }

    /**
     * Define el valor de la propiedad tipoMimeJustificanteCVS.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoMimeJustificanteCVS(String value) {
        this.tipoMimeJustificanteCVS = value;
    }

    /**
     * Obtiene el valor de la propiedad tieneFirmaJustificanteCSV.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTieneFirmaJustificanteCSV() {
        return tieneFirmaJustificanteCSV;
    }

    /**
     * Define el valor de la propiedad tieneFirmaJustificanteCSV.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTieneFirmaJustificanteCSV(Boolean value) {
        this.tieneFirmaJustificanteCSV = value;
    }

    /**
     * Obtiene el valor de la propiedad csv.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCsv() {
        return csv;
    }

    /**
     * Define el valor de la propiedad csv.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCsv(String value) {
        this.csv = value;
    }

    /**
     * Obtiene el valor de la propiedad cdAmbitoCreacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCdAmbitoCreacion() {
        return cdAmbitoCreacion;
    }

    /**
     * Define el valor de la propiedad cdAmbitoCreacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCdAmbitoCreacion(String value) {
        this.cdAmbitoCreacion = value;
    }

    /**
     * Obtiene el valor de la propiedad ambitoCreacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmbitoCreacion() {
        return ambitoCreacion;
    }

    /**
     * Define el valor de la propiedad ambitoCreacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmbitoCreacion(String value) {
        this.ambitoCreacion = value;
    }

    /**
     * Obtiene el valor de la propiedad cdAmbitoActual.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCdAmbitoActual() {
        return cdAmbitoActual;
    }

    /**
     * Define el valor de la propiedad cdAmbitoActual.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCdAmbitoActual(String value) {
        this.cdAmbitoActual = value;
    }

    /**
     * Obtiene el valor de la propiedad ambitoActual.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAmbitoActual() {
        return ambitoActual;
    }

    /**
     * Define el valor de la propiedad ambitoActual.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAmbitoActual(String value) {
        this.ambitoActual = value;
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
     * Obtiene el valor de la propiedad organoOrigen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganoOrigen() {
        return organoOrigen;
    }

    /**
     * Define el valor de la propiedad organoOrigen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganoOrigen(String value) {
        this.organoOrigen = value;
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
     * Obtiene el valor de la propiedad organoDestino.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrganoDestino() {
        return organoDestino;
    }

    /**
     * Define el valor de la propiedad organoDestino.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrganoDestino(String value) {
        this.organoDestino = value;
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
     * Obtiene el valor de la propiedad deAsunto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeAsunto() {
        return deAsunto;
    }

    /**
     * Define el valor de la propiedad deAsunto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeAsunto(String value) {
        this.deAsunto = value;
    }

    /**
     * Obtiene el valor de la propiedad cdSIA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCdSIA() {
        return cdSIA;
    }

    /**
     * Define el valor de la propiedad cdSIA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCdSIA(String value) {
        this.cdSIA = value;
    }

    /**
     * Obtiene el valor de la propiedad deCdSIA.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeCdSIA() {
        return deCdSIA;
    }

    /**
     * Define el valor de la propiedad deCdSIA.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeCdSIA(String value) {
        this.deCdSIA = value;
    }

    /**
     * Obtiene el valor de la propiedad cdFormulario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCdFormulario() {
        return cdFormulario;
    }

    /**
     * Define el valor de la propiedad cdFormulario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCdFormulario(String value) {
        this.cdFormulario = value;
    }

    /**
     * Obtiene el valor de la propiedad deFormulario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeFormulario() {
        return deFormulario;
    }

    /**
     * Define el valor de la propiedad deFormulario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeFormulario(String value) {
        this.deFormulario = value;
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

    /**
     * Obtiene el valor de la propiedad plazos.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPlazos() {
        return plazos;
    }

    /**
     * Define el valor de la propiedad plazos.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPlazos(String value) {
        this.plazos = value;
    }

    /**
     * Obtiene el valor de la propiedad silencioAdministrativo.
     * 
     * @return
     *     possible object is
     *     {@link TipoSilencioAdministrativoEnum }
     *     
     */
    public TipoSilencioAdministrativoEnum getSilencioAdministrativo() {
        return silencioAdministrativo;
    }

    /**
     * Define el valor de la propiedad silencioAdministrativo.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoSilencioAdministrativoEnum }
     *     
     */
    public void setSilencioAdministrativo(TipoSilencioAdministrativoEnum value) {
        this.silencioAdministrativo = value;
    }

    /**
     * Obtiene el valor de la propiedad asuntoInterno.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAsuntoInterno() {
        return asuntoInterno;
    }

    /**
     * Define el valor de la propiedad asuntoInterno.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAsuntoInterno(String value) {
        this.asuntoInterno = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoCadenaAsientos.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoCadenaAsientos() {
        return codigoCadenaAsientos;
    }

    /**
     * Define el valor de la propiedad codigoCadenaAsientos.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoCadenaAsientos(String value) {
        this.codigoCadenaAsientos = value;
    }

    /**
     * Obtiene el valor de la propiedad timestampFactura.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimestampFactura() {
        return timestampFactura;
    }

    /**
     * Define el valor de la propiedad timestampFactura.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimestampFactura(String value) {
        this.timestampFactura = value;
    }

    /**
     * Obtiene el valor de la propiedad nuRegistroInterno.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNuRegistroInterno() {
        return nuRegistroInterno;
    }

    /**
     * Define el valor de la propiedad nuRegistroInterno.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNuRegistroInterno(String value) {
        this.nuRegistroInterno = value;
    }

    /**
     * Obtiene el valor de la propiedad nuRegistroOrigen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNuRegistroOrigen() {
        return nuRegistroOrigen;
    }

    /**
     * Define el valor de la propiedad nuRegistroOrigen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNuRegistroOrigen(String value) {
        this.nuRegistroOrigen = value;
    }

    /**
     * Obtiene el valor de la propiedad importeFactura.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImporteFactura() {
        return importeFactura;
    }

    /**
     * Define el valor de la propiedad importeFactura.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImporteFactura(BigDecimal value) {
        this.importeFactura = value;
    }

    /**
     * Obtiene el valor de la propiedad justificanteUnidadTramitacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJustificanteUnidadTramitacion() {
        return justificanteUnidadTramitacion;
    }

    /**
     * Define el valor de la propiedad justificanteUnidadTramitacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJustificanteUnidadTramitacion(String value) {
        this.justificanteUnidadTramitacion = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroFactura.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroFactura() {
        return numeroFactura;
    }

    /**
     * Define el valor de la propiedad numeroFactura.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroFactura(String value) {
        this.numeroFactura = value;
    }

    /**
     * Obtiene el valor de la propiedad cdTipodocumento.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getCdTipodocumento() {
        return cdTipodocumento;
    }

    /**
     * Define el valor de la propiedad cdTipodocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setCdTipodocumento(Long value) {
        this.cdTipodocumento = value;
    }

    /**
     * Obtiene el valor de la propiedad deTipodocumento.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeTipodocumento() {
        return deTipodocumento;
    }

    /**
     * Define el valor de la propiedad deTipodocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeTipodocumento(String value) {
        this.deTipodocumento = value;
    }

    /**
     * Obtiene el valor de la propiedad cdZonaHorariaCreacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCdZonaHorariaCreacion() {
        return cdZonaHorariaCreacion;
    }

    /**
     * Define el valor de la propiedad cdZonaHorariaCreacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCdZonaHorariaCreacion(String value) {
        this.cdZonaHorariaCreacion = value;
    }

    /**
     * Obtiene el valor de la propiedad deZonaHorariaCreacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeZonaHorariaCreacion() {
        return deZonaHorariaCreacion;
    }

    /**
     * Define el valor de la propiedad deZonaHorariaCreacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeZonaHorariaCreacion(String value) {
        this.deZonaHorariaCreacion = value;
    }

    /**
     * Obtiene el valor de la propiedad cdZonaHorariaUsuario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCdZonaHorariaUsuario() {
        return cdZonaHorariaUsuario;
    }

    /**
     * Define el valor de la propiedad cdZonaHorariaUsuario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCdZonaHorariaUsuario(String value) {
        this.cdZonaHorariaUsuario = value;
    }

    /**
     * Obtiene el valor de la propiedad deZonaHorariaUsuario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeZonaHorariaUsuario() {
        return deZonaHorariaUsuario;
    }

    /**
     * Define el valor de la propiedad deZonaHorariaUsuario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeZonaHorariaUsuario(String value) {
        this.deZonaHorariaUsuario = value;
    }

}
