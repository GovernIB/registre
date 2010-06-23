package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;

/**

 */
@Embeddable
public class EntradaId implements java.io.Serializable {

	private int anyo;
	private int numero;
	private int oficina;

	public EntradaId() {
	}

	public EntradaId(int anyo, int numero, int oficina) {
		this.anyo = anyo;
		this.numero = numero;
		this.oficina = oficina;
	}

	@Column(name = "FZAANOEN", nullable = false, precision = 4, scale = 0)
	public int getAnyo() {
		return this.anyo;
	}

	public void setAnyo(int anyo) {
		this.anyo = anyo;
	}

	@Column(name = "FZANUMEN", nullable = false, precision = 5, scale = 0)
	public int getNumero() {
		return this.numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	@Column(name = "FZACAGCO", nullable = false, precision = 2, scale = 0)
	public int getOficina() {
		return this.oficina;
	}

	public void setOficina(int oficina) {
		this.oficina = oficina;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EntradaId))
			return false;
		EntradaId castOther = (EntradaId) other;

		return (this.getAnyo() == castOther.getAnyo())
				&& (this.getNumero() == castOther.getNumero())
				&& (this.getOficina() == castOther.getOficina());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getAnyo();
		result = 37 * result + this.getNumero();
		result = 37 * result + this.getOficina();
		return result;
	}

}
