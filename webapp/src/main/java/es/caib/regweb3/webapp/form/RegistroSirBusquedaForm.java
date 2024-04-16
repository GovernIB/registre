package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.InteresadoSir;
import es.caib.regweb3.model.RegistroSir;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by Fundació BIT.
 *
 * @author earrivi
 * Date: 16/06/16
 */
public class RegistroSirBusquedaForm {

    private RegistroSir registroSir;
    private InteresadoSir interesadoSir;
    /*private String nombreInteresado;
    private String primerApellidoInteresado;
    private String segundoApellidoInteresado;
    private String documentoIdentificacionInteresado;*/
    private Integer pageNumber;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fechaInicio;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date fechaFin;
    private String estado;


    public RegistroSirBusquedaForm() {}

    public RegistroSirBusquedaForm(RegistroSir registroSir, Integer pageNumber) {
        this.registroSir = registroSir;
        this.pageNumber = pageNumber;
        this.fechaInicio =  new Date();
        this.fechaFin =  new Date();
        this.interesadoSir = new InteresadoSir();
    }

    public RegistroSir getRegistroSir() {
        return registroSir;
    }

    public void setRegistroSir(RegistroSir registroSir) {
        this.registroSir = registroSir;
    }

    public InteresadoSir getInteresadoSir() {
        return interesadoSir;
    }

    public void setInteresadoSir(InteresadoSir interesadoSir) {
        this.interesadoSir = interesadoSir;
    }

    /*public String getNombreInteresado() {
        return nombreInteresado;
    }

    public void setNombreInteresado(String nombreInteresado) {
        this.nombreInteresado = nombreInteresado;
    }

    public String getPrimerApellidoInteresado() {
        return primerApellidoInteresado;
    }

    public void setPrimerApellidoInteresado(String primerApellidoInteresado) {
        this.primerApellidoInteresado = primerApellidoInteresado;
    }

    public String getSegundoApellidoInteresado() {
        return segundoApellidoInteresado;
    }

    public void setSegundoApellidoInteresado(String segundoApellidoInteresado) {
        this.segundoApellidoInteresado = segundoApellidoInteresado;
    }

    public String getDocumentoIdentificacionInteresado() {
        return documentoIdentificacionInteresado;
    }

    public void setDocumentoIdentificacionInteresado(String documentoIdentificacionInteresado) {
        this.documentoIdentificacionInteresado = documentoIdentificacionInteresado;
    }*/

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }
}
