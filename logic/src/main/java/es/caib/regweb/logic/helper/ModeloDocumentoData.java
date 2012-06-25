package es.caib.regweb.logic.helper;

import java.io.Serializable;

public class ModeloDocumentoData implements Serializable {


	String idioma = "";
	String tipo = "";
	String titulo = "";
	String cuerpo = "";

	public ModeloDocumentoData(String idioma, String tipo, String titulo,
			String cuerpo) {
		super();
		this.idioma = idioma;
		this.tipo = tipo;
		this.titulo = titulo;
		this.cuerpo = cuerpo;
	}
	
	public ModeloDocumentoData() {
		super();
	}
	/**
	 * @return the idioma
	 */
	public String getIdioma() {
		return idioma;
	}
	/**
	 * @param idioma the idioma to set
	 */
	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}
	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	/**
	 * @return the titulo
	 */
	public String getTitulo() {
		return titulo;
	}
	/**
	 * @param titulo the titulo to set
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	/**
	 * @return the cuerpo
	 */
	public String getCuerpo() {
		return cuerpo;
	}
	/**
	 * @param cuerpo the cuerpo to set
	 */
	public void setCuerpo(String cuerpo) {
		this.cuerpo = cuerpo;
	}
	
	

}
