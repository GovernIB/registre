package es.caib.regweb3.webapp.form;

import java.io.Serializable;
import java.util.Date;

import es.caib.regweb3.model.Remesa;

/**
 * @author Limit Tecnologies S.L.
 */
public class RemesaBusqueda implements Serializable {

	private static final long serialVersionUID = 1L;

	private Remesa remesa;
	private String emisor;
	private Date fechaPuestaDisposicionDesde;
	private Date fechaPuestaDisposicionHasta;

	private Integer pageNumber;

	public RemesaBusqueda() {
	}

	public RemesaBusqueda(Remesa remesa, String emisor, Date fechaPuestaDisposicionDesde, Date fechaPuestaDisposicionHasta,
			Integer pageNumber) {
		this.remesa = remesa;
		this.fechaPuestaDisposicionDesde = fechaPuestaDisposicionDesde;
		this.fechaPuestaDisposicionHasta = fechaPuestaDisposicionHasta;
		this.pageNumber = pageNumber;
	}

	public Remesa getRemesa() {
		return remesa;
	}

	public void setRemesa(Remesa remesa) {
		this.remesa = remesa;
	}

	public String getEmisor() {
		return emisor;
	}

	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}

	public Date getFechaPuestaDisposicionDesde() {
		return fechaPuestaDisposicionDesde;
	}

	public void setFechaPuestaDisposicionDesde(Date fechaPuestaDisposicionDesde) {
		this.fechaPuestaDisposicionDesde = fechaPuestaDisposicionDesde;
	}

	public Date getFechaPuestaDisposicionHasta() {
		return fechaPuestaDisposicionHasta;
	}

	public void setFechaPuestaDisposicionHasta(Date fechaPuestaDisposicionHasta) {
		this.fechaPuestaDisposicionHasta = fechaPuestaDisposicionHasta;
	}

	public Integer getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

}