package es.caib.regweb3.webapp.form;

import es.caib.regweb3.model.RegistroSalida;

import java.io.Serializable;
import java.util.Date;

/**
 * Created 4/04/14 16:09
 *
 * @author earrivi
 */
public class RegistroSalidaBusqueda implements Serializable {

    private RegistroSalida registroSalida;
    private Integer pageNumber;
    private Date fechaInicio;
    private Date fechaFin;
    private Boolean anexos;
    private String interessatNom;
    private String interessatDoc;
    private String organOrigen;
    private String organOrigenNom;
    private String observaciones;
    private String usuario;

    public RegistroSalidaBusqueda() {}

    public RegistroSalidaBusqueda(RegistroSalida registroSalida, Integer pageNumber) {
        this.registroSalida = registroSalida;
        this.pageNumber = pageNumber;
    }

    public RegistroSalida getRegistroSalida() {
        return registroSalida;
    }

    public void setRegistroSalida(RegistroSalida registroSalida) {
        this.registroSalida = registroSalida;
    }

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

    public Boolean getAnexos() {
        return anexos;
    }

    public void setAnexos(Boolean anexos) {
        this.anexos = anexos;
    }

	public String getInteressatNom() {
		return interessatNom;
	}

	public void setInteressatNom(String interessatNom) {
		this.interessatNom = interessatNom;
	}

	public String getInteressatDoc() {
		return interessatDoc;
	}

	public void setInteressatDoc(String interessatDoc) {
		this.interessatDoc = interessatDoc;
	}

	public String getOrganOrigen() {
		return organOrigen;
	}

	public void setOrganOrigen(String organOrigen) {
		this.organOrigen = organOrigen;
	}

	public String getOrganOrigenNom() {
		return organOrigenNom;
	}

	public void setOrganOrigenNom(String organOrigenNom) {
		this.organOrigenNom = organOrigenNom;
	}

    public String getObservaciones() { return observaciones; }

    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getUsuario() { return usuario; }

    public void setUsuario(String usuario) { this.usuario = usuario; }

}