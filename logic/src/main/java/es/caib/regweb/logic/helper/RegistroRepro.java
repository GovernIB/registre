package es.caib.regweb.logic.helper;

import java.io.Serializable;

/**
 * Classe per desar la informació de les repros
 * @author  Victor Herrera
 * @version 1.0
 */

public class RegistroRepro implements Serializable {
	private String repro;
	private String codUsuario;
	private String nomRepro;
	/**
	 * @return Returns the codUsuario.
	 */
	public String getCodUsuario() {
		return codUsuario;
	}
	/**
	 * @param codUsuario The codUsuario to set.
	 */
	public void setCodUsuario(String codUsuario) {
		this.codUsuario = codUsuario;
	}
	/**
	 * @return Returns the nomRepro.
	 */
	public String getNomRepro() {
		return nomRepro;
	}
	/**
	 * @param nomRepro The nomRepro to set.
	 */
	public void setNomRepro(String nomRepro) {
		this.nomRepro = nomRepro;
	}
	/**
	 * @return Returns the repro.
	 */
	public String getRepro() {
		return repro;
	}
	/**
	 * @param repro The repro to set.
	 */
	public void setRepro(String repro) {
		this.repro = repro;
	}

}
