package es.caib.regweb.logic.helper;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * Bean que guarda els parametres de les consultes sobre el registre d'accessos al registre
 * @author  Toni Rogel
 * @version 1.0
 */

public class ParametrosListadoAcceso implements Serializable {
	
	private String oficinaDesde="";
	private String oficinaHasta="";
	private String fechaDesde="";
	private String fechaHasta="";
	private Hashtable errores=new Hashtable();
	private boolean validado;
	private boolean error=false;
	private String registreES="";
	private String numRegistre="";
	private String anyRegistre="";
	private boolean calcularTotalRegistres=false;
	private String totalFiles="";
	
	
	
	/**
	 * @return Returns the calcularTotalRegistres.
	 */
	public boolean isCalcularTotalRegistres() {
		return calcularTotalRegistres;
	}

	/**
	 * @param calcularTotalRegistres The calcularTotalRegistres to set.
	 */
	public void setCalcularTotalRegistres(boolean calcularTotalRegistres) {
		this.calcularTotalRegistres = calcularTotalRegistres;
	}

	/**
	 * @return Returns the totalFiles.
	 */
	public String getTotalFiles() {
		return totalFiles;
	}

	/**
	 * @param totalFiles The totalFiles to set.
	 */
	public void setTotalFiles(String totalFiles) {
		this.totalFiles = totalFiles;
	}
	
	
	/**
	 * @return Returns the registreES.
	 */
	public String getRegistreES() {
		return registreES;
	}


	/**
	 * @param registreES The registreES to set.
	 */
	public void setRegistreES(String registreES) {
		this.registreES = registreES.toUpperCase();
	}


	public String getAnyRegistre() {
		return anyRegistre;
	}


	public void setAnyRegistre(String anyRegistre) {
		this.anyRegistre = anyRegistre;
	}


	public String getNumRegistre() {
		return numRegistre;
	}
	public void setNumRegistre(String numRegistre) {
		this.numRegistre = numRegistre;
	}

	public void setoficinaDesde(String oficinaDesde) {
		this.oficinaDesde=oficinaDesde;
	}
	public void setoficinaHasta(String oficinaHasta) {
		this.oficinaHasta=oficinaHasta;
	}
	public void setfechaDesde(String fechaDesde) {
		this.fechaDesde=fechaDesde;
	}
	public void setfechaHasta(String fechaHasta) {
		this.fechaHasta=fechaHasta;
	}

    public void setValidado(boolean es_validado) {
		this.validado=es_validado;
	}
	
	public String getOficinaDesde() {
		return oficinaDesde;
	}
	public String getOficinaHasta() {
		return oficinaHasta;
	}

    public boolean getValidado() {
		return validado;
	}

	public String getFechaDesde() {
		return fechaDesde;
	}
	public String getFechaHasta() {
		return fechaHasta;
	}
	
	/**
	 * Mètode per veure els errors que s'han generat en l'execució del Bean.
	 * @return Torna els errors que s'han generat.
	 */
	public Hashtable getErrores() {
		return errores;
	}

	
	/**
	 * Inicialització del Bean
	 */	
	public void inizializar() {
		oficinaDesde="";
		oficinaHasta="";
		fechaDesde="";
		fechaHasta="";
		numRegistre="";
		anyRegistre="";
	}

	
}