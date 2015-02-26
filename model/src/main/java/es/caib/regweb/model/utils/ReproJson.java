package es.caib.regweb.model.utils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Bean para almacenar el contenido de una Repro recibida mediante JSON
 * @author earrivi on 27/10/14.
 */
@XmlRootElement(name = "repro")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReproJson implements Serializable {

    @XmlElement
    private String idLibro;
    @XmlElement
    private String extracto;
    @XmlElement
    private String idTipoDocumentacionFisica;
    @XmlElement
    private String destino;
    @XmlElement
    private String destinoExternoCodigo;
    @XmlElement
    private String destinoExternoDenominacion;
    @XmlElement
    private String origen;
    @XmlElement
    private String origenExternoCodigo;
    @XmlElement
    private String origenExternoDenominacion;
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
    private String oficinaOrigen;
    @XmlElement
    private String oficinaOrigenExterno;
    @XmlElement
    private String denominacionOfiOrigenExt;
    @XmlElement
    private String numeroRegistroOrigen;
    @XmlElement
    private String fechaOrigen;

    public ReproJson() {
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

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getDestinoExternoCodigo() {
        return destinoExternoCodigo;
    }

    public void setDestinoExternoCodigo(String destinoExternoCodigo) {
        this.destinoExternoCodigo = destinoExternoCodigo;
    }

    public String getDestinoExternoDenominacion() {
        return destinoExternoDenominacion;
    }

    public void setDestinoExternoDenominacion(String destinoExternoDenominacion) {
        this.destinoExternoDenominacion = destinoExternoDenominacion;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getOrigenExternoCodigo() {
        return origenExternoCodigo;
    }

    public void setOrigenExternoCodigo(String origenExternoCodigo) {
        this.origenExternoCodigo = origenExternoCodigo;
    }

    public String getOrigenExternoDenominacion() {
        return origenExternoDenominacion;
    }

    public void setOrigenExternoDenominacion(String origenExternoDenominacion) {
        this.origenExternoDenominacion = origenExternoDenominacion;
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

    public String getNumeroRegistroOrigen() {
        return numeroRegistroOrigen;
    }

    public void setNumeroRegistroOrigen(String numeroRegistroOrigen) {
        this.numeroRegistroOrigen = numeroRegistroOrigen;
    }

    public String getOficinaOrigen() {
        return oficinaOrigen;
    }

    public void setOficinaOrigen(String oficinaOrigen) {
        this.oficinaOrigen = oficinaOrigen;
    }

    public String getOficinaOrigenExterno() {
        return oficinaOrigenExterno;
    }

    public void setOficinaOrigenExterno(String oficinaOrigenExterno) {
        this.oficinaOrigenExterno = oficinaOrigenExterno;
    }

    public String getDenominacionOfiOrigenExt() {
        return denominacionOfiOrigenExt;
    }

    public void setDenominacionOfiOrigenExt(String denominacionOfiOrigenExt) {
        this.denominacionOfiOrigenExt = denominacionOfiOrigenExt;
    }

    public String getFechaOrigen() {
        return fechaOrigen;
    }

    public void setFechaOrigen(String fechaOrigen) {
        this.fechaOrigen = fechaOrigen;
    }
}

