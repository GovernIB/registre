package es.caib.regweb3.webapp.utils;

import es.caib.regweb3.model.Anexo;
import es.caib.regweb3.model.Interesado;
import es.caib.regweb3.model.RegistroEntrada;
import es.caib.regweb3.model.RegistroSalida;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jpernia
 * Date: 15/04/14
 */
public class DatosRecibo implements Serializable {

    private String codigoOficina;
    private String nombreOficina;
    private String localidadOficina;
    private Date fechaRegistro;
    private String destinatario;
    private String origen;
    private String extracto;
    private String libro;
    private String numLibro;
    private String nomLibro;
    private String numeroRegistro;
    private String tipoRegistro;
    private String usuarioNombre;
    private String usuarioNombreCompleto;
    private String entitat;
    private String decodificacioEntitat;
    private List<Interesado> interesados;
    private List<Anexo> anexos;


    public DatosRecibo(RegistroEntrada registro, String tipoRegistro) {
        this.codigoOficina = registro.getOficina().getCodigo();
        this.nombreOficina = registro.getOficina().getDenominacion();
        if(registro.getOficina().getLocalidad() != null) {
            this.localidadOficina = registro.getOficina().getLocalidad().getNombre();
        }
        this.fechaRegistro = registro.getFecha();
        if(registro.getDestino() != null) {
            this.destinatario = registro.getDestino().getDenominacion();
        }
        this.origen = null;
        this.extracto = registro.getRegistroDetalle().getExtracto();
        this.libro = registro.getLibro().getNombre();
        this.numLibro = registro.getLibro().getCodigo();
        this.nomLibro = registro.getLibro().getNombre();
        this.numeroRegistro = String.valueOf(registro.getNumeroRegistro());
        this.tipoRegistro = tipoRegistro;
        this.usuarioNombre = registro.getUsuario().getUsuario().getNombre();
        this.usuarioNombreCompleto = registro.getUsuario().getNombreCompleto();
        this.entitat = registro.getUsuario().getEntidad().getNombre();
        this.decodificacioEntitat = registro.getUsuario().getEntidad().getDescripcion();
        this.interesados = registro.getRegistroDetalle().getInteresados();
        this.anexos = registro.getRegistroDetalle().getAnexos();
    }

    public DatosRecibo(RegistroSalida registro, String tipoRegistro) {
        this.codigoOficina = registro.getOficina().getCodigo();
        this.nombreOficina = registro.getOficina().getDenominacion();
        if(registro.getOficina().getLocalidad() != null) {
            this.localidadOficina = registro.getOficina().getLocalidad().getNombre();
        }
        this.fechaRegistro = registro.getFecha();
        this.destinatario = null;
        if(registro.getOrigen() != null) {
            this.origen = registro.getOrigen().getDenominacion();
        }
        this.extracto = registro.getRegistroDetalle().getExtracto();
        this.libro = registro.getLibro().getNombre();
        this.numLibro = registro.getLibro().getCodigo();
        this.nomLibro = registro.getLibro().getNombre();
        this.numeroRegistro = String.valueOf(registro.getNumeroRegistro());
        this.tipoRegistro = tipoRegistro;
        this.usuarioNombre = registro.getUsuario().getUsuario().getNombre();
        this.usuarioNombreCompleto = registro.getUsuario().getNombreCompleto();
        this.entitat = registro.getUsuario().getEntidad().getNombre();
        this.decodificacioEntitat = registro.getUsuario().getEntidad().getDescripcion();
        this.interesados = registro.getRegistroDetalle().getInteresados();
        this.anexos = registro.getRegistroDetalle().getAnexos();
    }

    public String getCodigoOficina() {
        return codigoOficina;
    }

    public void setCodigoOficina(String codigoOficina) {
        this.codigoOficina = codigoOficina;
    }

    public String getNombreOficina() {
        return nombreOficina;
    }

    public void setNombreOficina(String nombreOficina) {
        this.nombreOficina = nombreOficina;
    }

    public String getLocalidadOficina() {
        return localidadOficina;
    }

    public void setLocalidadOficina(String localidadOficina) {
        this.localidadOficina = localidadOficina;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getExtracto() {
        return extracto;
    }

    public void setExtracto(String extracto) {
        this.extracto = extracto;
    }

    public String getLibro() {
        return libro;
    }

    public void setLibro(String libro) {
        this.libro = libro;
    }

    public String getNumeroRegistro() {
        return numeroRegistro;
    }

    public void setNumeroRegistro(String numeroRegistro) {
        this.numeroRegistro = numeroRegistro;
    }

    public String getTipoRegistro() {
        return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
        this.tipoRegistro = tipoRegistro;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getUsuarioNombreCompleto() {
        return usuarioNombreCompleto;
    }

    public void setUsuarioNombreCompleto(String usuarioNombreCompleto) {
        this.usuarioNombreCompleto = usuarioNombreCompleto;
    }

    public String getEntitat() {
        return entitat;
    }

    public void setEntitat(String entitat) {
        this.entitat = entitat;
    }

    public String getDecodificacioEntitat() {
        return decodificacioEntitat;
    }

    public void setDecodificacioEntitat(String decodificacioEntitat) {
        this.decodificacioEntitat = decodificacioEntitat;
    }

    public List<Interesado> getInteresados() {
        return interesados;
    }

    public void setInteresados(List<Interesado> interesados) {
        this.interesados = interesados;
    }

    public List<Anexo> getAnexos() {
        return anexos;
    }

    public void setAnexos(List<Anexo> anexos) {
        this.anexos = anexos;
    }

    public String getNumLibro() {
        return numLibro;
    }

    public void setNumLibro(String numLibro) {
        this.numLibro = numLibro;
    }

    public String getNomLibro() {
        return nomLibro;
    }

    public void setNomLibro(String nomLibro) {
        this.nomLibro = nomLibro;
    }
}
