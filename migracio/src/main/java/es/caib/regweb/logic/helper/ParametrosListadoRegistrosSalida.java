package es.caib.regweb.logic.helper;

import java.io.Serializable;
import java.util.*;
import java.text.*;

public class ParametrosListadoRegistrosSalida  implements Serializable {
	
    
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
	private String destinatario="";
	private String codiRemitent="";
	private String destino="";
	
	private boolean calcularTotalRegistres=false;
	private String totalFiles="";
	
	
	public boolean isCalcularTotalRegistres() {
		return calcularTotalRegistres;
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

	public void setRemitente(String remitente) {
		this.remitente=remitente;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario=destinatario;
	}

	public void setDestino(String destino) {
		this.destino=destino;
	}

    public void setCodiRemitent(String codiremitent) {
		this.codiRemitent=codiremitent;
	}

    public void setValidado(boolean validado) {
		this.validado=validado;
	}

    public boolean getValidado(){
        return validado;
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

	public void setAny(String any) {
		this.any=any;
	}

    public String getAny() {
		return any;
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

	public String getCodiRemitent() {
		return codiRemitent;
	}
	
    public String getDestino() {
		return destino;
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
		destinatario="";
		destino="";
		codiRemitent="";
	}
	
}