package es.caib.regweb3.ws.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 */
@XmlRootElement
@Deprecated
public class RegistroResponseWs implements Serializable {

    private String entidadCodigo;
    private String entidadDenominacion;
    private String numeroRegistro;
    private String numeroRegistroFormateado;
    private Date fechaRegistro;
    private String codigoUsuario;
    private String nombreUsuario;
    private String contactoUsuario;

    private String oficinaCodigo;
    private String oficinaDenominacion;
    private String libroCodigo;
    private String libroDescripcion;
    private String extracto;
    private String docFisicaCodigo;
    private String docFisicaDescripcion;
    private String tipoAsuntoCodigo;
    private String tipoAsuntoDescripcion;
    private String idiomaCodigo;
    private String idiomaDescripcion;
    private String codigoAsuntoCodigo;
    private String codigoAsuntoDescripcion;
    private String refExterna;
    private String numExpediente;
    private String tipoTransporteCodigo;
    private String tipoTransporteDescripcion;
    private String numTransporte;
    private String observaciones;
    private String numeroRegistroOrigen;
    private Date fechaOrigen;
    private String aplicacion;
    private String version;

    private String expone;
    private String solicita;
    private List<InteresadoWs> interesados;
    private List<AnexoWs> anexos;


    public String getEntidadCodigo() {
        return entidadCodigo;
    }

    public void setEntidadCodigo(String entidadCodigo) {
        this.entidadCodigo = entidadCodigo;
    }

    public String getEntidadDenominacion() {
        return entidadDenominacion;
    }

    public void setEntidadDenominacion(String entidadDenominacion) {
        this.entidadDenominacion = entidadDenominacion;
    }

    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContactoUsuario() {
        return contactoUsuario;
    }

    public void setContactoUsuario(String contactoUsuario) {
        this.contactoUsuario = contactoUsuario;
    }

    public String getNumeroRegistroFormateado() {
        return numeroRegistroFormateado;
    }

    public void setNumeroRegistroFormateado(String numeroRegistroFormateado) {
        this.numeroRegistroFormateado = numeroRegistroFormateado;
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

    public String getLibroCodigo() {
        return libroCodigo;
    }

    public void setLibroCodigo(String libroCodigo) {
        this.libroCodigo = libroCodigo;
    }

    public String getLibroDescripcion() {
        return libroDescripcion;
    }

    public void setLibroDescripcion(String libroDescripcion) {
        this.libroDescripcion = libroDescripcion;
    }

    public String getExtracto() {
        return extracto;
    }

    public void setExtracto(String extracto) {
        this.extracto = extracto;
    }

    public String getDocFisicaCodigo() {
        return docFisicaCodigo;
    }

    public void setDocFisicaCodigo(String docFisicaCodigo) {
        this.docFisicaCodigo = docFisicaCodigo;
    }

    public String getDocFisicaDescripcion() {
        return docFisicaDescripcion;
    }

    public void setDocFisicaDescripcion(String docFisicaDescripcion) {
        this.docFisicaDescripcion = docFisicaDescripcion;
    }

    public String getTipoAsuntoCodigo() {
        return tipoAsuntoCodigo;
    }

    public void setTipoAsuntoCodigo(String tipoAsuntoCodigo) {
        this.tipoAsuntoCodigo = tipoAsuntoCodigo;
    }

    public String getTipoAsuntoDescripcion() {
        return tipoAsuntoDescripcion;
    }

    public void setTipoAsuntoDescripcion(String tipoAsuntoDescripcion) {
        this.tipoAsuntoDescripcion = tipoAsuntoDescripcion;
    }

    public String getIdiomaCodigo() {
        return idiomaCodigo;
    }

    public void setIdiomaCodigo(String idiomaCodigo) {
        this.idiomaCodigo = idiomaCodigo;
    }

    public String getIdiomaDescripcion() {
        return idiomaDescripcion;
    }

    public void setIdiomaDescripcion(String idiomaDescripcion) {
        this.idiomaDescripcion = idiomaDescripcion;
    }

    public String getCodigoAsuntoCodigo() {
        return codigoAsuntoCodigo;
    }

    public void setCodigoAsuntoCodigo(String codigoAsuntoCodigo) {
        this.codigoAsuntoCodigo = codigoAsuntoCodigo;
    }

    public String getCodigoAsuntoDescripcion() {
        return codigoAsuntoDescripcion;
    }

    public void setCodigoAsuntoDescripcion(String codigoAsuntoDescripcion) {
        this.codigoAsuntoDescripcion = codigoAsuntoDescripcion;
    }

    public String getRefExterna() {
        return refExterna;
    }

    public void setRefExterna(String refExterna) {
        this.refExterna = refExterna;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public String getTipoTransporteCodigo() {
        return tipoTransporteCodigo;
    }

    public void setTipoTransporteCodigo(String tipoTransporteCodigo) {
        this.tipoTransporteCodigo = tipoTransporteCodigo;
    }

    public String getTipoTransporteDescripcion() {
        return tipoTransporteDescripcion;
    }

    public void setTipoTransporteDescripcion(String tipoTransporteDescripcion) {
        this.tipoTransporteDescripcion = tipoTransporteDescripcion;
    }

    public String getNumTransporte() {
        return numTransporte;
    }

    public void setNumTransporte(String numTransporte) {
        this.numTransporte = numTransporte;
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

    public Date getFechaOrigen() {
        return fechaOrigen;
    }

    public void setFechaOrigen(Date fechaOrigen) {
        this.fechaOrigen = fechaOrigen;
    }

    public String getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(String aplicacion) {
        this.aplicacion = aplicacion;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getExpone() {
        return expone;
    }

    public void setExpone(String expone) {
        this.expone = expone;
    }

    public String getSolicita() {
        return solicita;
    }

    public void setSolicita(String solicita) {
        this.solicita = solicita;
    }

    public List<InteresadoWs> getInteresados() {
        return interesados;
    }

    public void setInteresados(List<InteresadoWs> interesados) {
        this.interesados = interesados;
    }

    public List<AnexoWs> getAnexos() {
        return anexos;
    }

    public void setAnexos(List<AnexoWs> anexos) {
        this.anexos = anexos;
    }

    @Override
    public String toString() {
        return "RegistroResponseWs{" +
                "entidadCodigo='" + entidadCodigo + '\'' +
                ", entidadDenominacion='" + entidadDenominacion + '\'' +
                ", numeroRegistro=" + numeroRegistro +
                ", numeroRegistroFormateado='" + numeroRegistroFormateado + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                ", codigoUsuario='" + codigoUsuario + '\'' +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", contactoUsuario='" + contactoUsuario + '\'' +
                ", oficinaCodigo='" + oficinaCodigo + '\'' +
                ", oficinaDenominacion='" + oficinaDenominacion + '\'' +
                ", libroCodigo='" + libroCodigo + '\'' +
                ", libroDescripcion='" + libroDescripcion + '\'' +
                ", extracto='" + extracto + '\'' +
                ", docFisicaCodigo='" + docFisicaCodigo + '\'' +
                ", docFisicaDescripcion='" + docFisicaDescripcion + '\'' +
                ", tipoAsuntoCodigo='" + tipoAsuntoCodigo + '\'' +
                ", tipoAsuntoDescripcion='" + tipoAsuntoDescripcion + '\'' +
                ", idiomaCodigo='" + idiomaCodigo + '\'' +
                ", idiomaDescripcion='" + idiomaDescripcion + '\'' +
                ", codigoAsuntoCodigo='" + codigoAsuntoCodigo + '\'' +
                ", codigoAsuntoDescripcion='" + codigoAsuntoDescripcion + '\'' +
                ", refExterna='" + refExterna + '\'' +
                ", numExpediente='" + numExpediente + '\'' +
                ", tipoTransporteCodigo='" + tipoTransporteCodigo + '\'' +
                ", tipoTransporteDescripcion='" + tipoTransporteDescripcion + '\'' +
                ", numTransporte='" + numTransporte + '\'' +
                ", observaciones='" + observaciones + '\'' +
                ", numeroRegistroOrigen='" + numeroRegistroOrigen + '\'' +
                ", fechaOrigen=" + fechaOrigen +
                ", aplicacion='" + aplicacion + '\'' +
                ", version='" + version + '\'' +
                ", expone='" + expone + '\'' +
                ", solicita='" + solicita + '\'' +
                ", interesados=" + interesados +
                ", anexos=" + anexos +
                '}';
    }
}
