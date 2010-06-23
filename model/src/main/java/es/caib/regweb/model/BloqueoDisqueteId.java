package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;

/**

 */
@Embeddable
public class BloqueoDisqueteId implements java.io.Serializable {

	private String codigoEntradaSalida;
	private int anyoEntradaSalida;
	private int oficinaEntradaSalida;

	public BloqueoDisqueteId() {
	}

	public BloqueoDisqueteId(String codigoEntradaSalida, int anyoEntradaSalida, int oficinaEntradaSalida) {
		this.codigoEntradaSalida = codigoEntradaSalida;
		this.anyoEntradaSalida = anyoEntradaSalida;
		this.oficinaEntradaSalida = oficinaEntradaSalida;
	}

	@Column(name = "FZNCENSA", nullable = false, length = 1)
	public String getCodigoEntradaSalida() {
		return this.codigoEntradaSalida;
	}

	public void setCodigoEntradaSalida(String codigoEntradaSalida) {
		this.codigoEntradaSalida = codigoEntradaSalida;
	}

	@Column(name = "FZNAENSA", nullable = false, precision = 4, scale = 0)
	public int getAnyoEntradaSalida() {
		return this.anyoEntradaSalida;
	}

	public void setAnyoEntradaSalida(int anyoEntradaSalida) {
		this.anyoEntradaSalida = anyoEntradaSalida;
	}

	@Column(name = "FZNCAGCO", nullable = false, precision = 2, scale = 0)
	public int getOficinaEntradaSalida() {
		return this.oficinaEntradaSalida;
	}

	public void setOficinaEntradaSalida(int oficinaEntradaSalida) {
		this.oficinaEntradaSalida = oficinaEntradaSalida;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BloqueoDisqueteId))
			return false;
		BloqueoDisqueteId castOther = (BloqueoDisqueteId) other;

		return (this.getCodigoEntradaSalida().equals(castOther.getCodigoEntradaSalida()))
				&& (this.getAnyoEntradaSalida() == castOther.getAnyoEntradaSalida())
				&& (this.getOficinaEntradaSalida() == castOther.getOficinaEntradaSalida());
	}

	public int hashCode() {
		int result = 17;

        result = 37 * result
            + (getCodigoEntradaSalida() == null ? 0 : this.getCodigoEntradaSalida().hashCode());
		result = 37 * result + this.getAnyoEntradaSalida();
		result = 37 * result + this.getOficinaEntradaSalida();
		return result;
	}

}
