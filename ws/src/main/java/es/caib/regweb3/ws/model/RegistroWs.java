package es.caib.regweb3.ws.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Fundació BIT.
 * 
 * @author earrivi
 * @author anadal (herència)
 */
@XmlRootElement
@Deprecated
public class RegistroWs implements Serializable {

    private Integer numero;
    private Date fecha;
    private String numeroRegistroFormateado;

    // private IdentificadorWs identificador;
    private String oficina;
    private String libro;
    private String extracto;
    private String tipoAsunto;
    private String codigoAsunto;
    private String refExterna;
    private String numExpediente;
    private String idioma;
    private String tipoTransporte;
    private String numTransporte;
    private String codigoUsuario;
    private String contactoUsuario;
    private String aplicacion;
    private String version;
    private Long docFisica;
    private String observaciones;
    private String expone;
    private String solicita;
    private List<InteresadoWs> interesados;
    private List<AnexoWs> anexos;

  /*
   * public IdentificadorWs getIdentificador() { return identificador; }
   * 
   * public void setIdentificador(IdentificadorWs identificador) {
   * this.identificador = identificador; }
   */

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getNumeroRegistroFormateado() {
        return numeroRegistroFormateado;
    }

    public void setNumeroRegistroFormateado(String numeroRegistroFormateado) {
        this.numeroRegistroFormateado = numeroRegistroFormateado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getOficina() {
        return oficina;
    }

    public void setOficina(String oficina) {
        this.oficina = oficina;
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

    public String getTipoAsunto() {
        return tipoAsunto;
    }

    public void setTipoAsunto(String tipoAsunto) {
        this.tipoAsunto = tipoAsunto;
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

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
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

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getContactoUsuario() {
        return contactoUsuario;
    }

    public void setContactoUsuario(String contactoUsuario) {
        this.contactoUsuario = contactoUsuario;
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

    public Long getDocFisica() {
        return docFisica;
    }

    public void setDocFisica(Long docFisica) {
        this.docFisica = docFisica;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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
}