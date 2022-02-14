package es.caib.regweb3.webapp.form;

import java.util.Date;

/**
 * Created 04/01/2022	
 * 
 * @author Limit Tecnologies
 */
public class RangoFechasBusqueda {
	
	private Date fechaInicioImportacion;
    private Date fechaFinImportacion;
    
    public RangoFechasBusqueda() {}

	public Date getFechaInicioImportacion() {
		return fechaInicioImportacion;
	}

	public void setFechaInicioImportacion(Date fechaInicioImportacion) {
		this.fechaInicioImportacion = fechaInicioImportacion;
	}

	public Date getFechaFinImportacion() {
		return fechaFinImportacion;
	}

	public void setFechaFinImportacion(Date fechaFinImportacion) {
		this.fechaFinImportacion = fechaFinImportacion;
	}

}