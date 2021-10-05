package es.caib.regweb3.ws.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@XmlRootElement
public class AsientoRegistralWs implements Serializable {

    private Long id;

    private String entidadCodigo;
    private String entidadDenominacion;

    private String entidadRegistralInicioCodigo;//Oficina Inicio
    private String entidadRegistralInicioDenominacion;

    private String entidadRegistralOrigenCodigo;//Oficina Origen
    private String entidadRegistralOrigenDenominacion;

    private String unidadTramitacionOrigenCodigo;//Organismo Origen
    private String unidadTramitacionOrigenDenominacion;

    private String entidadRegistralDestinoCodigo;//Oficina Destino
    private String entidadRegistralDestinoDenominacion;

    private String unidadTramitacionDestinoCodigo; //destino
    private String unidadTramitacionDestinoDenominacion;

    private Long tipoRegistro;
    private Date fechaRegistro;
    private String numeroRegistroFormateado;
    private String numeroRegistro;
    private String libroCodigo;
    private String resumen; //extracto
    private Long tipoDocumentacionFisicaCodigo;
    private String codigoAsunto;
    private String codigoAsuntoDenominacion;
    private Long idioma;
    private String referenciaExterna;
    private String numeroExpediente;
    private String tipoTransporte;
    private String numeroTransporte;
    private Long codigoSia;
    private String codigoUsuario;
    private String aplicacionTelematica;
    private String aplicacion;
    private String version;
    private String observaciones;
    private String expone;
    private String solicita;
    private List<InteresadoWs> interesados;
    private List<AnexoWs> anexos;
    private Boolean presencial;

    private Long estado;


    // Intercambio SIR
    private String identificadorIntercambio;
    private String tipoEnvioDocumentacion;
    private Date fechaRecepcion;
    private String codigoError;
    private String descripcionError;
    private String numeroRegistroDestino;// Numero de registro aceptado en destino.
    private Date fechaRegistroDestino;
    private String motivo; //Motivo rechazo o reenvio
    private String codigoEntidadRegistralProcesado; // Codigo de la oficina que acepta o rechaza, reenvia
    private String decodificacionEntidadRegistralProcesado; // Denominacion de la oficina que acepta o rechaza, reenvia


    public AsientoRegistralWs() {
    }

    public AsientoRegistralWs(Long tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getEntidadRegistralInicioCodigo() {
        return entidadRegistralInicioCodigo;
    }

    public void setEntidadRegistralInicioCodigo(String entidadRegistralInicioCodigo) {
        this.entidadRegistralInicioCodigo = entidadRegistralInicioCodigo;
    }

    public String getEntidadRegistralInicioDenominacion() {
        return entidadRegistralInicioDenominacion;
    }

    public void setEntidadRegistralInicioDenominacion(String entidadRegistralInicioDenominacion) {
        this.entidadRegistralInicioDenominacion = entidadRegistralInicioDenominacion;
    }

    public String getEntidadRegistralOrigenCodigo() {
        return entidadRegistralOrigenCodigo;
    }

    public void setEntidadRegistralOrigenCodigo(String entidadRegistralOrigenCodigo) {
        this.entidadRegistralOrigenCodigo = entidadRegistralOrigenCodigo;
    }

    public String getEntidadRegistralOrigenDenominacion() {
        return entidadRegistralOrigenDenominacion;
    }

    public void setEntidadRegistralOrigenDenominacion(String entidadRegistralOrigenDenominacion) {
        this.entidadRegistralOrigenDenominacion = entidadRegistralOrigenDenominacion;
    }

    public String getUnidadTramitacionOrigenCodigo() {
        return unidadTramitacionOrigenCodigo;
    }

    public void setUnidadTramitacionOrigenCodigo(String unidadTramitacionOrigenCodigo) {
        this.unidadTramitacionOrigenCodigo = unidadTramitacionOrigenCodigo;
    }

    public String getUnidadTramitacionOrigenDenominacion() {
        return unidadTramitacionOrigenDenominacion;
    }

    public void setUnidadTramitacionOrigenDenominacion(String unidadTramitacionOrigenDenominacion) {
        this.unidadTramitacionOrigenDenominacion = unidadTramitacionOrigenDenominacion;
    }

    public String getEntidadRegistralDestinoCodigo() {
        return entidadRegistralDestinoCodigo;
    }

    public void setEntidadRegistralDestinoCodigo(String entidadRegistralDestinoCodigo) {
        this.entidadRegistralDestinoCodigo = entidadRegistralDestinoCodigo;
    }

    public String getEntidadRegistralDestinoDenominacion() {
        return entidadRegistralDestinoDenominacion;
    }

    public void setEntidadRegistralDestinoDenominacion(String entidadRegistralDestinoDenominacion) {
        this.entidadRegistralDestinoDenominacion = entidadRegistralDestinoDenominacion;
    }

    public String getUnidadTramitacionDestinoCodigo() {
        return unidadTramitacionDestinoCodigo;
    }

    public void setUnidadTramitacionDestinoCodigo(String unidadTramitacionDestinoCodigo) {
        this.unidadTramitacionDestinoCodigo = unidadTramitacionDestinoCodigo;
    }

    public String getUnidadTramitacionDestinoDenominacion() {
        return unidadTramitacionDestinoDenominacion;
    }

    public void setUnidadTramitacionDestinoDenominacion(String unidadTramitacionDestinoDenominacion) {
        this.unidadTramitacionDestinoDenominacion = unidadTramitacionDestinoDenominacion;
    }

    public Long getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(Long tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getNumeroRegistroFormateado() {
        return numeroRegistroFormateado;
    }

    public void setNumeroRegistroFormateado(String numeroRegistroFormateado) {
        this.numeroRegistroFormateado = numeroRegistroFormateado;
    }

    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public String getLibroCodigo() {
        return libroCodigo;
    }

    public void setLibroCodigo(String libroCodigo) {
        this.libroCodigo = libroCodigo;
    }

    public String getResumen() {
        return resumen;
    }

    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    public Long getTipoDocumentacionFisicaCodigo() {
        return tipoDocumentacionFisicaCodigo;
    }

    public void setTipoDocumentacionFisicaCodigo(Long tipoDocumentacionFisicaCodigo) {
        this.tipoDocumentacionFisicaCodigo = tipoDocumentacionFisicaCodigo;
    }

    public String getCodigoAsunto() {
        return codigoAsunto;
    }

    public void setCodigoAsunto(String codigoAsunto) {
        this.codigoAsunto = codigoAsunto;
    }

    public String getCodigoAsuntoDenominacion() {
        return codigoAsuntoDenominacion;
    }

    public void setCodigoAsuntoDenominacion(String codigoAsuntoDenominacion) {
        this.codigoAsuntoDenominacion = codigoAsuntoDenominacion;
    }

    public Long getIdioma() {
        return idioma;
    }

    public void setIdioma(Long idioma) {
        this.idioma = idioma;
    }

    public String getReferenciaExterna() {
        return referenciaExterna;
    }

    public void setReferenciaExterna(String referenciaExterna) {
        this.referenciaExterna = referenciaExterna;
    }

    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }

    public String getTipoTransporte() {
        return tipoTransporte;
    }

    public void setTipoTransporte(String tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public String getNumeroTransporte() {
        return numeroTransporte;
    }

    public void setNumeroTransporte(String numeroTransporte) {
        this.numeroTransporte = numeroTransporte;
    }

    public Long getCodigoSia() {
        return codigoSia;
    }

    public void setCodigoSia(Long codigoSia) {
        this.codigoSia = codigoSia;
    }

    public String getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getAplicacionTelematica() {
        return aplicacionTelematica;
    }

    public void setAplicacionTelematica(String aplicacionTelematica) {
        this.aplicacionTelematica = aplicacionTelematica;
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

    public Boolean getPresencial() {
        return presencial;
    }

    public void setPresencial(Boolean presencial) {
        this.presencial = presencial;
    }

    public Long getEstado() {
        return estado;
    }

    public void setEstado(Long estado) {
        this.estado = estado;
    }

    public String getIdentificadorIntercambio() {
        return identificadorIntercambio;
    }

    public void setIdentificadorIntercambio(String identificadorIntercambio) {
        this.identificadorIntercambio = identificadorIntercambio;
    }

    public String getTipoEnvioDocumentacion() {
        return tipoEnvioDocumentacion;
    }

    public void setTipoEnvioDocumentacion(String tipoEnvioDocumentacion) {
        this.tipoEnvioDocumentacion = tipoEnvioDocumentacion;
    }

    public Date getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(Date fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public String getCodigoError() {
        return codigoError;
    }

    public void setCodigoError(String codigoError) {
        this.codigoError = codigoError;
    }

    public String getDescripcionError() {
        return descripcionError;
    }

    public void setDescripcionError(String descripcionError) {
        this.descripcionError = descripcionError;
    }

    public String getNumeroRegistroDestino() {
        return numeroRegistroDestino;
    }

    public void setNumeroRegistroDestino(String numeroRegistroDestino) {
        this.numeroRegistroDestino = numeroRegistroDestino;
    }

    public Date getFechaRegistroDestino() {
        return fechaRegistroDestino;
    }

    public void setFechaRegistroDestino(Date fechaRegistroDestino) {
        this.fechaRegistroDestino = fechaRegistroDestino;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getCodigoEntidadRegistralProcesado() {
        return codigoEntidadRegistralProcesado;
    }

    public void setCodigoEntidadRegistralProcesado(String codigoEntidadRegistralProcesado) {
        this.codigoEntidadRegistralProcesado = codigoEntidadRegistralProcesado;
    }

    public String getDecodificacionEntidadRegistralProcesado() {
        return decodificacionEntidadRegistralProcesado;
    }

    public void setDecodificacionEntidadRegistralProcesado(String decodificacionEntidadRegistralProcesado) {
        this.decodificacionEntidadRegistralProcesado = decodificacionEntidadRegistralProcesado;
    }
}
