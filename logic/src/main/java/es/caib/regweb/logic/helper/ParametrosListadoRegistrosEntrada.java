package es.caib.regweb.logic.helper;

import java.io.Serializable;
import java.util.*;
import java.text.*;

public class ParametrosListadoRegistrosEntrada implements Serializable {
	
    
	private String oficinaDesde="";
	private String oficinaHasta="";
	private String fechaDesde="";
	private String fechaHasta="";
	private String oficinaFisica="";
	private Hashtable errores=new Hashtable();
	private boolean validado;
	private boolean error=false;
	private String any="";
	private String extracto="";
	private String tipo="";
	private String remitente="";
	private String procedencia="";
	private String destinatario="";
	private String codiDestinatari="";
	private boolean calcularTotalRegistres=false;
	private String totalFiles="";
	private String codiMun060="";
	private String numeroRegistroSalidaRelacionado="";
	private String anyoRegistroSalidaRelacionado="";
	
	
	public boolean isCalcularTotalRegistres() {
		return calcularTotalRegistres;
	}
	
	public void setNumeroRegistroSalidaRelacionado(String numeroRegistroSalidaRelacionado) {
		this.numeroRegistroSalidaRelacionado = numeroRegistroSalidaRelacionado;
	}
	
	public String getNumeroRegistroSalidaRelacionado() {
		return numeroRegistroSalidaRelacionado;   
	}
	public void setAnyoRegistroSalidaRelacionado(String anyoRegistroSalidaRelacionado) {
		this.anyoRegistroSalidaRelacionado = anyoRegistroSalidaRelacionado;
	}

	public String getAnyoRegistroSalidaRelacionado() {
		return anyoRegistroSalidaRelacionado;   
	}
	
	public void setCalcularTotalRegistres(boolean calcularTotalRegistres) {
		this.calcularTotalRegistres = calcularTotalRegistres;
	}


	public String getTotalFiles() {
		return totalFiles;   
	}


	public void setTotalFiles(String totalFiles) {
		this.totalFiles = totalFiles;
	}

    public void setAny(String any) {
		this.any=any;
	}

    public String getAny() {
		return any;
	}

	public void setoficinaDesde(String oficinaDesde) {
		this.oficinaDesde=oficinaDesde;
	}

	public void setoficinaHasta(String oficinaHasta) {
		this.oficinaHasta=oficinaHasta;
	}

	public void setoficinaFisica(String oficinaFisica) {
		this.oficinaFisica=oficinaFisica;
	}

	public void setfechaDesde(String fechaDesde) {
		this.fechaDesde=fechaDesde;
	}

	public void setfechaHasta(String fechaHasta) {
		this.fechaHasta=fechaHasta;
	}

	public void setExtracto(String extracto) {
		this.extracto=extracto;
	}

	public void setTipo(String tipo) {
		this.tipo=tipo;
	}
	
    public void setCodiMunicipi060(String codiMun060) {
		//if(!codiMun060.equals("000"))
			this.codiMun060 = codiMun060;
	}

	public void setRemitente(String remitente) {
		this.remitente=remitente;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario=destinatario;
	}

	public void setProcedencia(String procedencia) {
		this.procedencia=procedencia;
	}

	public void setCodiDestinatari(String codidestinatari) {
		this.codiDestinatari=codidestinatari;
	}

    public void setValidado(boolean es_validado) {
		this.validado=es_validado;
	}

    public boolean getValidado() {
		return validado;
	}
	
    public String getCodiMunicipi060() {
		return codiMun060;
	}
	
    public String getOficinaDesde() {
		return oficinaDesde;
	}

	public String getOficinaHasta() {
		return oficinaHasta;
	}

	public String getOficinaFisica() {
		return oficinaFisica;
	}

	public String getFechaDesde() {
		return fechaDesde;
	}

	public String getFechaHasta() {
		return fechaHasta;
	}

	public String getExtracto() {
		return extracto;
	}

	public String getTipo() {
		return tipo;
	}

	public String getRemitente() {
		return remitente;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public String getCodiDestinatari() {
		return codiDestinatari;
	}

	public String getProcedencia() {
		return procedencia;
	}
	
	public Hashtable getErrores() {
		return errores;
	}
	
	
    public void inizializar() {
		oficinaDesde="";
		oficinaHasta="";
		fechaDesde="";
		fechaHasta="";
		extracto="";
		tipo="";
		remitente="";
		procedencia="";
		destinatario="";
		codiDestinatari="";
		codiMun060 = "";
	}
	

}