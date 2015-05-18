package es.caib.regweb.webapp.form;

import es.caib.regweb.model.RegistroEntrada;

import java.io.Serializable;
import java.util.Date;

/**
 * Created 4/04/14 16:09
 *
 * @author mgonzalez
 */
public class RegistroEntradaBusqueda implements Serializable {

    private RegistroEntrada registroEntrada;
    private Integer pageNumber;
    private Date fechaInicio;
    private Date fechaFin;
    private Boolean anexos;
    private String interessatNom;
    private String interessatDoc;
    private String organDestinatari;
    private String organDestinatariNom;

    public RegistroEntradaBusqueda() {}

    public RegistroEntradaBusqueda(RegistroEntrada registroEntrada, Integer pageNumber) {
        this.registroEntrada = registroEntrada;
        this.pageNumber = pageNumber;
    }

    public RegistroEntrada getRegistroEntrada() {
        return registroEntrada;
    }

    public void setRegistroEntrada(RegistroEntrada registroEntrada) {
        this.registroEntrada = registroEntrada;
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

	public String getOrganDestinatari() {
		return organDestinatari;
	}

	public void setOrganDestinatari(String organDestinatari) {
		this.organDestinatari = organDestinatari;
	}

	public String getOrganDestinatariNom() {
		return organDestinatariNom;
	}

	public void setOrganDestinatariNom(String organDestinatariNom) {
		this.organDestinatariNom = organDestinatariNom;
	}

}