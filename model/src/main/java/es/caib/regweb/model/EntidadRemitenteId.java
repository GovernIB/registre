package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;

/**

 */
@Embeddable
public class EntidadRemitenteId implements java.io.Serializable {

	private String codigo;
	private int numero;
	private int fechaBaja;

	public EntidadRemitenteId() {
	}

	public EntidadRemitenteId(String codigo, int numero, int fechaBaja) {
		this.codigo = codigo;
		this.numero = numero;
		this.fechaBaja = fechaBaja;
	}

	@Column(name = "FZGCENTI", nullable = false, length = 7)
	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Column(name = "FZGNENTI", nullable = false, precision = 3, scale = 0)
	public int getNumero() {
		return this.numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	@Column(name = "FZGFBAJA", nullable = false, precision = 8, scale = 0)
	public int getFechaBaja() {
		return this.fechaBaja;
	}

	public void setFechaBaja(int fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof EntidadRemitenteId))
			return false;
		EntidadRemitenteId castOther = (EntidadRemitenteId) other;

		return ((this.getCodigo().equals(castOther.getCodigo())) || (this
				.getCodigo() != null
				&& castOther.getCodigo() != null && this.getCodigo()
				.equals(castOther.getCodigo())))
				&& (this.getNumero() == castOther.getNumero())
				&& (this.getFechaBaja() == castOther.getFechaBaja());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getCodigo() == null ? 0 : this.getCodigo().hashCode());
		result = 37 * result + this.getNumero();
		result = 37 * result + this.getFechaBaja();
		return result;
	}

}
