package es.caib.regweb3.ws.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 */
@XmlRootElement
public class DatosInteresadoWs implements Serializable {

    private Long tipoInteresado;
    private String tipoDocumentoIdentificacion;
    private String documento;
    private String razonSocial;
    private String codDirectoriosUnificados;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private Long pais;
    private Long provincia;
    private Long localidad;
    private String direccion;
    private String cp;
    private String email;
    private String telefono;
    private String direccionElectronica;
    private Long canal;
    private String observaciones;

    //SICRES4
    private Boolean receptorNotificaciones;
    private String telefonoMovil;
    private Boolean avisoNotificacionSMS;
    private Boolean avisoCorreoElectronico;

    public Long getTipoInteresado() {
        return tipoInteresado;
    }

    public void setTipoInteresado(Long tipoInteresado) {
        this.tipoInteresado = tipoInteresado;
    }

    public String getTipoDocumentoIdentificacion() {
        return tipoDocumentoIdentificacion;
    }

    public void setTipoDocumentoIdentificacion(String tipoDocumentoIdentificacion) {
        this.tipoDocumentoIdentificacion = tipoDocumentoIdentificacion;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public Long getPais() {
        return pais;
    }

    public void setPais(Long pais) {
        this.pais = pais;
    }

    public Long getProvincia() {
        return provincia;
    }

    public void setProvincia(Long provincia) {
        this.provincia = provincia;
    }

    public Long getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Long localidad) {
        this.localidad = localidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccionElectronica() {
        return direccionElectronica;
    }

    public void setDireccionElectronica(String direccionElectronica) {
        this.direccionElectronica = direccionElectronica;
    }

    public Long getCanal() {
        return canal;
    }

    public void setCanal(Long canal) {
        this.canal = canal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getCodDirectoriosUnificados() {
        return codDirectoriosUnificados;
    }

    public void setCodDirectoriosUnificados(String codDirectoriosUnificados) {
        this.codDirectoriosUnificados = codDirectoriosUnificados;
    }

    public Boolean getReceptorNotificaciones() {
        return receptorNotificaciones;
    }

    public void setReceptorNotificaciones(Boolean receptorNotificaciones) {
        this.receptorNotificaciones = receptorNotificaciones;
    }

    public String getTelefonoMovil() {
        return telefonoMovil;
    }

    public void setTelefonoMovil(String telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
    }

    public Boolean getAvisoNotificacionSMS() {
        return avisoNotificacionSMS;
    }

    public void setAvisoNotificacionSMS(Boolean avisoNotificacionSMS) {
        this.avisoNotificacionSMS = avisoNotificacionSMS;
    }

    public Boolean getAvisoCorreoElectronico() {
        return avisoCorreoElectronico;
    }

    public void setAvisoCorreoElectronico(Boolean avisoCorreoElectronico) {
        this.avisoCorreoElectronico = avisoCorreoElectronico;
    }
}
