package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;

/**

 */
@Embeddable
public class NumeroCorreoId implements java.io.Serializable {

	private String codigoEntradaSalida;
	private int oficinaEntradaSalida;
	private int anyoEntradaSalida;
	private int numeroEntradaSalida;

	public NumeroCorreoId() {
	}

	public NumeroCorreoId(String codigoEntradaSalida, int oficinaEntradaSalida, int anyoEntradaSalida,
			int numeroEntradaSalida) {
		this.codigoEntradaSalida = codigoEntradaSalida;
		this.oficinaEntradaSalida = oficinaEntradaSalida;
		this.anyoEntradaSalida = anyoEntradaSalida;
		this.numeroEntradaSalida = numeroEntradaSalida;
	}

	@Column(name = "FZPCENSA", nullable = false, length = 1)
	public String getCodigoEntradaSalida() {
		return this.codigoEntradaSalida;
	}

	public void setCodigoEntradaSalida(String codigoEntradaSalida) {
		this.codigoEntradaSalida = codigoEntradaSalida;
	}

	@Column(name = "FZPCAGCO", nullable = false, precision = 2, scale = 0)
	public int getOficinaEntradaSalida() {
		return this.oficinaEntradaSalida;
	}

	public void setOficinaEntradaSalida(int oficinaEntradaSalida) {
		this.oficinaEntradaSalida = oficinaEntradaSalida;
	}

	@Column(name = "FZPANOEN", nullable = false, precision = 4, scale = 0)
	public int getAnyoEntradaSalida() {
		return this.anyoEntradaSalida;
	}

	public void setAnyoEntradaSalida(int anyoEntradaSalida) {
		this.anyoEntradaSalida = anyoEntradaSalida;
	}

	@Column(name = "FZPNUMEN", nullable = false, precision = 5, scale = 0)
	public int getNumeroEntradaSalida() {
		return this.numeroEntradaSalida;
	}

	public void setNumeroEntradaSalida(int numeroEntradaSalida) {
		this.numeroEntradaSalida = numeroEntradaSalida;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof NumeroCorreoId))
			return false;
		NumeroCorreoId castOther = (NumeroCorreoId) other;

		return (this.getCodigoEntradaSalida().equals(castOther.getCodigoEntradaSalida()))
				&& (this.getOficinaEntradaSalida() == castOther.getOficinaEntradaSalida())
				&& (this.getAnyoEntradaSalida() == castOther.getAnyoEntradaSalida())
				&& (this.getNumeroEntradaSalida() == castOther.getNumeroEntradaSalida());
	}

	public int hashCode() {
		int result = 17;

        result = 37 * result
            + (getCodigoEntradaSalida() == null ? 0 : this.getCodigoEntradaSalida().hashCode());
		result = 37 * result + this.getOficinaEntradaSalida();
		result = 37 * result + this.getAnyoEntradaSalida();
		result = 37 * result + this.getNumeroEntradaSalida();
		return result;
	}

}
