package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;

/**

 */
@Embeddable
public class DisqueteId implements java.io.Serializable {

	private String codigoEntradaSalida;
	private int oficinaEntradaSalida;
	private int anyoEntradaSalida;

	public DisqueteId() {
	}

	public DisqueteId(String codigoEntradaSalida, int oficinaEntradaSalida, int anyoEntradaSalida) {
		this.codigoEntradaSalida = codigoEntradaSalida;
		this.oficinaEntradaSalida = oficinaEntradaSalida;
		this.anyoEntradaSalida = anyoEntradaSalida;
	}

	@Column(name = "FZLCENSA", nullable = false, length = 1)
	public String getCodigoEntradaSalida() {
		return this.codigoEntradaSalida;
	}

	public void setCodigoEntradaSalida(String codigoEntradaSalida) {
		this.codigoEntradaSalida = codigoEntradaSalida;
	}

	@Column(name = "FZLCAGCO", nullable = false, precision = 2, scale = 0)
	public int getOficinaEntradaSalida() {
		return this.oficinaEntradaSalida;
	}

	public void setOficinaEntradaSalida(int oficinaEntradaSalida) {
		this.oficinaEntradaSalida = oficinaEntradaSalida;
	}

	@Column(name = "FZLAENSA", nullable = false, precision = 4, scale = 0)
	public int getAnyoEntradaSalida() {
		return this.anyoEntradaSalida;
	}

	public void setAnyoEntradaSalida(int anyoEntradaSalida) {
		this.anyoEntradaSalida = anyoEntradaSalida;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DisqueteId))
			return false;
		DisqueteId castOther = (DisqueteId) other;

		return (this.getCodigoEntradaSalida().equals(castOther.getCodigoEntradaSalida()))
				&& (this.getOficinaEntradaSalida() == castOther.getOficinaEntradaSalida())
				&& (this.getAnyoEntradaSalida() == castOther.getAnyoEntradaSalida());
	}

	public int hashCode() {
		int result = 17;

        result = 37 * result
            + (getCodigoEntradaSalida() == null ? 0 : this.getCodigoEntradaSalida().hashCode());
		result = 37 * result + this.getOficinaEntradaSalida();
		result = 37 * result + this.getAnyoEntradaSalida();
		return result;
	}

}
