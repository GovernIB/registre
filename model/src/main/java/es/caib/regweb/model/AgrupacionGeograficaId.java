package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;

/**

 */
@Embeddable
public class AgrupacionGeograficaId implements java.io.Serializable {

	private int tipo;
	private int codigo;

	public AgrupacionGeograficaId() {
	}

	public AgrupacionGeograficaId(int tipo, int codigo) {
		this.tipo = tipo;
		this.codigo = codigo;
	}

	@Column(name = "FABCTAGG", nullable = false, precision = 2, scale = 0)
	public int getTipo() {
		return this.tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	@Column(name = "FABCAGGE", nullable = false, precision = 3, scale = 0)
	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}


	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof AgrupacionGeograficaId))
			return false;
		AgrupacionGeograficaId castOther = (AgrupacionGeograficaId) other;

		return (this.getTipo() == castOther.getTipo())
				&& (this.getCodigo() == castOther.getCodigo());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getTipo();
		result = 37 * result + this.getCodigo();
		return result;
	}

}
