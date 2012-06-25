package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;


/**

 */
@Embeddable
public class ModeloEmailId implements java.io.Serializable {

	private String  idioma;
	private String  tipo;

	public ModeloEmailId() {
	}

	public ModeloEmailId(String  idioma, String  tipo) {
		this.idioma = idioma;
		this.tipo = tipo;
	}

	@Column(name = "BME_IDIOMA ", nullable = false, length = 2)
	public String getIdioma() {
		return this.idioma;
	}

	public void setIdioma(String idioma) {
		this.idioma = idioma;
	}

	@Column(name = "BME_TIPO", nullable = false, length = 2)
	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String  tipo) {
		this.tipo = tipo;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ModeloEmailId))
			return false;
		ModeloEmailId castOther = (ModeloEmailId) other;

		return (this.getIdioma().equals(castOther.getIdioma())
				&& (this.getTipo()).equals(castOther.getTipo()));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getTipo() == null ? 0 : this.getTipo().hashCode());
		result = 37 * result + (getIdioma() == null ? 0 : this.getIdioma().hashCode());;
		return result;
	}

}
