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
public class RegistroResponseWs implements Serializable {

    private String entidadCodigo;
    private String entidadDenominacion;
    private int numeroRegistro;
    private String numeroRegistroFormateado;
    private Date fechaRegistro;
    private String codigoUsuario;
    private String nombreUsuario;
    private String contactoUsuario;

    private String oficinaCodigo;
    private String oficinaDenominacion;
    private String libro;
    private String extracto;
    private String docFisica;
    private String tipoAsunto;
    private String idioma;
    private String codigoAsunto;
    private String refExterna;
    private String numExpediente;
    private String tipoTransporte;
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

    public int getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(int numeroRegistro) {
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

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public String getExtracto() {
        return extracto;
    }

    public void setExtracto(String extracto) {
        this.extracto = extracto;
    }

    public String getDocFisica() {
        return docFisica;
    }

    public void setDocFisica(String docFisica) {
        this.docFisica = docFisica;
    }

    public String getTipoAsunto() {
        return tipoAsunto;
    }

    public void setTipoAsunto(String tipoAsunto) {
        this.tipoAsunto = tipoAsunto;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getCodigoAsunto() {
        return codigoAsunto;
    }

    public void setCodigoAsunto(String codigoAsunto) {
        this.codigoAsunto = codigoAsunto;
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

    public String getTipoTransporte() {
        return tipoTransporte;
    }

    public void setTipoTransporte(String tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
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
                ", libro='" + libro + '\'' +
                ", extracto='" + extracto + '\'' +
                ", docFisica='" + docFisica + '\'' +
                ", tipoAsunto='" + tipoAsunto + '\'' +
                ", idioma='" + idioma + '\'' +
                ", codigoAsunto='" + codigoAsunto + '\'' +
                ", refExterna='" + refExterna + '\'' +
                ", numExpediente='" + numExpediente + '\'' +
                ", tipoTransporte='" + tipoTransporte + '\'' +
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
