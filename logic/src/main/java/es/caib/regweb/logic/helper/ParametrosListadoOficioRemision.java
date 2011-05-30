package es.caib.regweb.logic.helper;

import java.io.Serializable;
import java.util.Hashtable;

public class ParametrosListadoOficioRemision implements Serializable {
    
	private Hashtable errores=new Hashtable();
	
	private boolean validado=false;;
	private boolean error=false;
	private boolean calcularTotalRegistres=false;
	
	private String oficinaDesde="";
	private String oficinaHasta="";
	private String fechaDesde="";
	private String fechaHasta="";
	/**
	 * @return the validado
	 */
	public boolean isValidado() {
		return validado;
	}
	/**
	 * @param validado the validado to set
	 */
	public void setValidado(boolean validado) {
		this.validado = validado;
	}
	/**
	 * @return the error
	 */
	public boolean isError() {
		return error;
	}
	/**
	 * @param error the error to set
	 */
	public void setError(boolean error) {
		this.error = error;
	}
	/**
	 * @return the calcularTotalRegistres
	 */
	public boolean isCalcularTotalRegistres() {
		return calcularTotalRegistres;
	}
	/**
	 * @param calcularTotalRegistres the calcularTotalRegistres to set
	 */
	public void setCalcularTotalRegistres(boolean calcularTotalRegistres) {
		this.calcularTotalRegistres = calcularTotalRegistres;
	}
	/**
	 * @return the oficinaDesde
	 */
	public String getOficinaDesde() {
		return oficinaDesde;
	}
	/**
	 * @param oficinaDesde the oficinaDesde to set
	 */
	public void setOficinaDesde(String oficinaDesde) {
		this.oficinaDesde = oficinaDesde;
	}
	/**
	 * @return the oficinaHasta
	 */
	public String getOficinaHasta() {
		return oficinaHasta;
	}
	/**
	 * @param oficinaHasta the oficinaHasta to set
	 */
	public void setOficinaHasta(String oficinaHasta) {
		this.oficinaHasta = oficinaHasta;
	}
	/**
	 * @return the fechaDesde
	 */
	public String getFechaDesde() {
		return fechaDesde;
	}
	/**
	 * @param fechaDesde the fechaDesde to set
	 */
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	/**
	 * @return the fechaHasta
	 */
	public String getFechaHasta() {
		return fechaHasta;
	}
	/**
	 * @param fechaHasta the fechaHasta to set
	 */
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	/**
	 * @return the errores
	 */
	public Hashtable getErrores() {
		return errores;
	}
	/**
	 * @param errores the errores to set
	 */
	public void setErrores(Hashtable errores) {
		this.errores = errores;
	}
	

}
