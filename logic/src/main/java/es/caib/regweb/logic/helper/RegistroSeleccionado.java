/*
 * RegistroSeleccionado.java
 *
 * Created on 13 de octubre de 2004, 14:48
 */

package es.caib.regweb.logic.helper;

import java.io.Serializable;
/**
 * Classe per desar un registre seleccionat
 * @author  FJMARTINEZ
 * @version 1.0
 */

public class RegistroSeleccionado implements Comparable,Serializable  {
	
	/** Creates a new instance of RegistroSeleccionado */
	public RegistroSeleccionado() {
	}
	
	public String anoEntrada, numeroEntrada, oficina, descripcionOficina, fechaES,
	data, descripcionRemitente, descripcionOrganismoDestinatario, descripcionDocumento,
    descripcionIdiomaDocumento, registroAnulado, extracto, descripcionGeografico, oficio,
    oficinaFisica, descripcionOficinaFisica;
	
	private int ano;
	private int numero;
	private int numeroDocumentosRegistro060 = 1;
	
	public void setAnoEntrada(String anoEntrada) {
		this.anoEntrada=anoEntrada;
		ano=Integer.parseInt(anoEntrada);
	}
	public void setNumeroEntrada(String numeroEntrada) {
		this.numeroEntrada=numeroEntrada;
		numero=Integer.parseInt(numeroEntrada);
	}
	public void setOficina(String oficina) {
		this.oficina=oficina;
	}
    public void setOficinaFisica(String oficinaFisica) {
        this.oficinaFisica=oficinaFisica;
    }
	public void setExtracto(String extracto) {
		this.extracto=extracto;
	}
	public void setDescripcionOficina(String descripcionOficina) {
		this.descripcionOficina=descripcionOficina;
	}
    public void setDescripcionOficinaFisica(String descripcionOficinaFisica) {
        this.descripcionOficinaFisica=descripcionOficinaFisica;
    }
	public void setData(String data) {
		this.data=data;
	}
	public void setOficio(String oficio) {
		this.oficio=oficio;
	}
	public void setFechaES(String fechaES) {
		this.fechaES=fechaES;
	}
	public void setDescripcionRemitente(String descripcionRemitente) {
		this.descripcionRemitente=descripcionRemitente;
	}
	public void setDescripcionOrganismoDestinatario(String descripcionOrganismoDestinatario) {
		this.descripcionOrganismoDestinatario=descripcionOrganismoDestinatario;
	}
	public void setDescripcionDocumento(String descripcionDocumento) {
		this.descripcionDocumento=descripcionDocumento;
	}
	public void setDescripcionIdiomaDocumento(String descripcionIdiomaDocumento) {
		this.descripcionIdiomaDocumento=descripcionIdiomaDocumento;
	}
	public void setRegistroAnulado(String registroAnulado) {
		this.registroAnulado=registroAnulado;
	}
	public void setDescripcionGeografico(String descripcionGeografico) {
		this.descripcionGeografico=descripcionGeografico;
	}
	
	
	public String getExtracto() {
		return extracto;
	}
	public String getAnoEntrada() {
		return anoEntrada;
	}
	public String getNumeroEntrada() {
		return numeroEntrada;
	}
	public String getOficina() {
		return oficina; 
	}
    public String getOficinaFisica() {
        return oficinaFisica; 
    }
	public String getOficio() {
		return oficio; 
	}
	public String getDescripcionOficina() {
		return descripcionOficina;
	}
    public String getDescripcionOficinaFisica() {
        return descripcionOficinaFisica;
    }
	public String getDescripcionGeografico() {
		return descripcionGeografico;
	}
	public String getData() {
		return data;
	}
	public String getFechaES() {
		return fechaES;
	}
	public String getDescripcionRemitente() {
		return descripcionRemitente;
	}
	public String getDescripcionOrganismoDestinatario() {
		return descripcionOrganismoDestinatario;
	}
	public String getDescripcionDocumento() {
		return descripcionDocumento;
	}
	public String getDescripcionIdiomaDocumento() {
		return descripcionIdiomaDocumento;
	}
	public String getRegistroAnulado() {
		return registroAnulado;
	}
	public int getAno(){
		return ano;
	}
	public int getNumero() {
		return numero;
	}
	
	public int compareTo(Object o) {
		
		if (!(o instanceof RegistroSeleccionado)) {
			throw new ClassCastException();
		}
		
		RegistroSeleccionado reg=(RegistroSeleccionado)o;
		int resultado=ano-reg.getAno();
		if (resultado==0) {
			return numero-reg.getNumero();
		} else {
			return numero;
		}
	}
	public int getNumeroDocumentosRegistro060() {
		return numeroDocumentosRegistro060;
	}
	public void setNumeroDocumentosRegistro060(int numeroDocumentosRegistro060) {
		this.numeroDocumentosRegistro060 = numeroDocumentosRegistro060;
	}
	
}
