package es.caib.regweb3.model.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Bean para almacenar el contenido de una Plantilla recibida mediante JSON
 * @author earrivi on 27/10/14.
 */
@XmlRootElement(name = "repro")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlantillaJson implements Serializable {

    @XmlElement
    private String nombreRepro;
    @XmlElement
    private String idLibro;
    @XmlElement
    private String extracto;
    @XmlElement
    private String idTipoDocumentacionFisica;
    @XmlElement
    private String destinoCodigo;
    @XmlElement
    private String destinoDenominacion;
    @XmlElement
    private Boolean destinoExterno;
    @XmlElement
    private String origenCodigo;
    @XmlElement
    private String origenDenominacion;
    @XmlElement
    private Boolean origenExterno;
    @XmlElement
    private String idTipoAsunto;
    @XmlElement
    private String idCodigoAsunto;
    @XmlElement
    private String idIdioma;
    @XmlElement
    private String referenciaExterna;
    @XmlElement
    private String expediente;
    @XmlElement
    private String idTransporte;
    @XmlElement
    private String numeroTransporte;
    @XmlElement
    private String observaciones;
    @XmlElement
    private String oficinaCodigo;
    @XmlElement
    private String oficinaDenominacion;
    @XmlElement
    private Boolean oficinaExterna;
    @XmlElement
    private String numeroRegistroOrigen;
    @XmlElement
    private String fechaOrigen;
    @XmlElement
    private String codigoSia;
    @XmlElement
    private String interesado;

    public PlantillaJson() {
    }

    public String getNombreRepro() {
        return nombreRepro;
    }

    public void setNombreRepro(String nombreRepro) {
        this.nombreRepro = nombreRepro;
    }

    public String getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(String idLibro) {
        this.idLibro = idLibro;
    }

    public String getExtracto() {
        return extracto;
    }

    public void setExtracto(String extracto) {
        this.extracto = extracto;
    }

    public String getIdTipoDocumentacionFisica() {
        return idTipoDocumentacionFisica;
    }

    public void setIdTipoDocumentacionFisica(String idTipoDocumentacionFisica) {
        this.idTipoDocumentacionFisica = idTipoDocumentacionFisica;
    }

    public String getDestinoCodigo() {
        return destinoCodigo;
    }

    public void setDestinoCodigo(String destinoCodigo) {
        this.destinoCodigo = destinoCodigo;
    }

    public String getDestinoDenominacion() {
        return destinoDenominacion;
    }

    public void setDestinoDenominacion(String destinoDenominacion) {
        this.destinoDenominacion = destinoDenominacion;
    }

    public Boolean isDestinoExterno() {
        return destinoExterno;
    }

    public void setDestinoExterno(Boolean destinoExterno) {
        this.destinoExterno = destinoExterno;
    }

    public String getOrigenCodigo() {
        return origenCodigo;
    }

    public void setOrigenCodigo(String origenCodigo) {
        this.origenCodigo = origenCodigo;
    }

    public String getOrigenDenominacion() {
        return origenDenominacion;
    }

    public void setOrigenDenominacion(String origenDenominacion) {
        this.origenDenominacion = origenDenominacion;
    }

    public Boolean isOrigenExterno() {
        return origenExterno;
    }

    public void setOrigenExterno(Boolean origenExterno) {
        this.origenExterno = origenExterno;
    }

    public String getIdTipoAsunto() {
        return idTipoAsunto;
    }

    public void setIdTipoAsunto(String idTipoAsunto) {
        this.idTipoAsunto = idTipoAsunto;
    }

    public String getIdCodigoAsunto() {
        return idCodigoAsunto;
    }

    public void setIdCodigoAsunto(String idCodigoAsunto) {
        this.idCodigoAsunto = idCodigoAsunto;
    }

    public String getIdIdioma() {
        return idIdioma;
    }

    public void setIdIdioma(String idIdioma) {
        this.idIdioma = idIdioma;
    }

    public String getReferenciaExterna() {
        return referenciaExterna;
    }

    public void setReferenciaExterna(String referenciaExterna) {
        this.referenciaExterna = referenciaExterna;
    }

    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

    public String getIdTransporte() {
        return idTransporte;
    }

    public void setIdTransporte(String idTransporte) {
        this.idTransporte = idTransporte;
    }

    public String getNumeroTransporte() {
        return numeroTransporte;
    }

    public void setNumeroTransporte(String numeroTransporte) {
        this.numeroTransporte = numeroTransporte;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getOficinaCodigo() {
        return oficinaCodigo;
    }

    public void setOficinaCodigo(String oficinaCodigo) {
        this.oficinaCodigo = oficinaCodigo;
    }

    public String getOficinaDenominacion() {
        return oficinaDenominacion;
    }

    public void setOficinaDenominacion(String oficinaDenominacion) {
        this.oficinaDenominacion = oficinaDenominacion;
    }

    public Boolean isOficinaExterna() {
        return oficinaExterna;
    }

    public void setOficinaExterna(Boolean oficinaExterna) {
        this.oficinaExterna = oficinaExterna;
    }

    public String getNumeroRegistroOrigen() {
        return numeroRegistroOrigen;
    }

    public void setNumeroRegistroOrigen(String numeroRegistroOrigen) { this.numeroRegistroOrigen = numeroRegistroOrigen; }

    public String getFechaOrigen() {
        return fechaOrigen;
    }

    public void setFechaOrigen(String fechaOrigen) {
        this.fechaOrigen = fechaOrigen;
    }

    public String getCodigoSia() {
        return codigoSia;
    }

    public void setCodigoSia(String codigoSia) { this.codigoSia = codigoSia; }

    public String getInteresado() {
        return interesado;
    }

    public void setInteresado(String interesado) { this.interesado = interesado; }
}

