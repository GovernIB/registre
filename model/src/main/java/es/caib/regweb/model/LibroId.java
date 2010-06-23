package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;

/**

 */
@Embeddable
public class LibroId implements java.io.Serializable {

	private int anyoEntradaSalida;
	private String codigoEntradaSalida;
	private int oficinaEntradaSalida;

	public LibroId() {
	}

	public LibroId(int anyoEntradaSalida, String codigoEntradaSalida, int oficinaEntradaSalida) {
		this.anyoEntradaSalida = anyoEntradaSalida;
		this.codigoEntradaSalida = codigoEntradaSalida;
		this.oficinaEntradaSalida = oficinaEntradaSalida;
	}

	@Column(name = "FZCAENSA", nullable = false, precision = 4, scale = 0)
	public int getAnyoEntradaSalida() {
		return this.anyoEntradaSalida;
	}

	public void setAnyoEntradaSalida(int anyoEntradaSalida) {
		this.anyoEntradaSalida = anyoEntradaSalida;
	}

	@Column(name = "FZCCENSA", nullable = false, length = 1)
	public String getCodigoEntradaSalida() {
		return this.codigoEntradaSalida;
	}

	public void setCodigoEntradaSalida(String codigoEntradaSalida) {
		this.codigoEntradaSalida = codigoEntradaSalida;
	}

	@Column(name = "FZCCAGCO", nullable = false, precision = 2, scale = 0)
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
		if (!(other instanceof LibroId))
			return false;
		LibroId castOther = (LibroId) other;

		return (this.getAnyoEntradaSalida() == castOther.getAnyoEntradaSalida())
				&& (this.getCodigoEntradaSalida().equals(castOther.getCodigoEntradaSalida()))
				&& (this.getOficinaEntradaSalida() == castOther.getOficinaEntradaSalida());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getAnyoEntradaSalida();
        result = 37 * result
            + (getCodigoEntradaSalida() == null ? 0 : this.getCodigoEntradaSalida().hashCode());
		result = 37 * result + this.getOficinaEntradaSalida();
		return result;
	}

}
