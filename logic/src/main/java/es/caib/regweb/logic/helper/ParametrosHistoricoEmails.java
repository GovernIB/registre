package es.caib.regweb.logic.helper;

import java.io.Serializable;
import java.util.Vector;

public class ParametrosHistoricoEmails implements Serializable {

	private int anoRegistro = 0;
	private int numeroRegistro= 0;
	private int codigoOficina= 0;
	private int numeroEmail= -1;
	private String tipoRegistro = null;
	private String tipoEmail= null;
	private String CodigoUsuario= null;
	private String fecha= null;
	private String hora= null;
	private String emailDestinatario= null;
	/**
	 * @return the anoRegistro
	 */
	public int getAnoRegistro() {
		return anoRegistro;
	}
	/**
	 * @param anoRegistro the anoRegistro to set
	 */
	public void setAnoRegistro(int anoRegistro) {
		this.anoRegistro = anoRegistro;
	}
	/**
	 * @return the numeroRegistro
	 */
	public int getNumeroRegistro() {
		return numeroRegistro;
	}
	/**
	 * @param numeroRegistro the numeroRegistro to set
	 */
	public void setNumeroRegistro(int numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}
	/**
	 * @return the codigoOficina
	 */
	public int getCodigoOficina() {
		return codigoOficina;
	}
	/**
	 * @param codigoOficina the codigoOficina to set
	 */
	public void setCodigoOficina(int codigoOficina) {
		this.codigoOficina = codigoOficina;
	}
	/**
	 * @return the numeroEmail
	 */
	public int getNumeroEmail() {
		return numeroEmail;
	}
	/**
	 * @param numeroEmail the numeroEmail to set
	 */
	public void setNumeroEmail(int numeroEmail) {
		this.numeroEmail = numeroEmail;
	}
	/**
	 * @return the tipoRegistro
	 */
	public String getTipoRegistro() {
		return tipoRegistro;
	}
	/**
	 * @param tipoRegistro the tipoRegistro to set
	 */
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	/**
	 * @return the tipoEmail
	 */
	public String getTipoEmail() {
		return tipoEmail;
	}
	/**
	 * @param tipoEmail the tipoEmail to set
	 */
	public void setTipoEmail(String tipoEmail) {
		this.tipoEmail = tipoEmail;
	}
	/**
	 * @return the codigoUsuario
	 */
	public String getCodigoUsuario() {
		return CodigoUsuario;
	}
	/**
	 * @param codigoUsuario the codigoUsuario to set
	 */
	public void setCodigoUsuario(String codigoUsuario) {
		CodigoUsuario = codigoUsuario;
	}
	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}
	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	/**
	 * @return the hora
	 */
	public String getHora() {
		return hora;
	}
	/**
	 * @param hora the hora to set
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}
	/**
	 * @return the emailDestinatario
	 */
	public String getEmailDestinatario() {
		return emailDestinatario;
	}
	/**
	 * @param emailDestinatario the emailDestinatario to set
	 */
	public void setEmailDestinatario(String emailDestinatario) {
		this.emailDestinatario = emailDestinatario;
	}
	

}
