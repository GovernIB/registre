package es.caib.regweb.model;



import javax.persistence.Column;
import javax.persistence.Embeddable;

/**

 */
@Embeddable
public class HistoricoEmailsId implements java.io.Serializable {

	private String codigoEntradaSalida;
	private int anyoEntradaSalida;
	private int numeroEntradaSalida;
	private int oficinaEntradaSalida;
	private int numero;

	public HistoricoEmailsId() {
	}

	public HistoricoEmailsId(String codigoEntradaSalida, int anyoEntradaSalida, int numeroEntradaSalida,
			int oficinaEntradaSalida, int numero) {
		this.codigoEntradaSalida = codigoEntradaSalida;
		this.anyoEntradaSalida = anyoEntradaSalida;
		this.numeroEntradaSalida = numeroEntradaSalida;
		this.oficinaEntradaSalida = oficinaEntradaSalida;
		this.numero = numero;
	}

	@Column(name = "BHE_TIPUS", nullable = false, length = 1)
	public String getCodigoEntradaSalida() {
		return this.codigoEntradaSalida;
	}

	public void setCodigoEntradaSalida(String codigoEntradaSalida) {
		this.codigoEntradaSalida = codigoEntradaSalida;
	}

	@Column(name = "BHE_ANY", nullable = false, precision = 4, scale = 0)
	public int getAnyoEntradaSalida() {
		return this.anyoEntradaSalida;
	}

	public void setAnyoEntradaSalida(int anyoEntradaSalida) {
		this.anyoEntradaSalida = anyoEntradaSalida;
	}

	@Column(name = "BHE_NUMREG", nullable = false, precision = 5, scale = 0)
	public int getNumeroEntradaSalida() {
		return this.numeroEntradaSalida;
	}

	public void setNumeroEntradaSalida(int numeroEntradaSalida) {
		this.numeroEntradaSalida = numeroEntradaSalida;
	}

	@Column(name = "BHE_OFI", nullable = false, precision = 2, scale = 0)
	public int getOficinaEntradaSalida() {
		return this.oficinaEntradaSalida;
	}

	public void setOficinaEntradaSalida(int oficinaEntradaSalida) {
		this.oficinaEntradaSalida = oficinaEntradaSalida;
	}

	@Column(name = "BHE_NUM", nullable = false, precision = 3, scale = 0)
	public int getNumero() {
		return this.numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof HistoricoEmailsId))
			return false;
		HistoricoEmailsId castOther = (HistoricoEmailsId) other;

		return (
				this.getCodigoEntradaSalida().equals(castOther.getCodigoEntradaSalida())
				&& (this.getAnyoEntradaSalida() == castOther.getAnyoEntradaSalida())
				&& (this.getNumeroEntradaSalida() == castOther.getNumeroEntradaSalida())
				&& (this.getOficinaEntradaSalida() == castOther.getOficinaEntradaSalida())
				&& (this.getNumero() == castOther.getNumero())
				); 
	}

	public int hashCode() {
		int result = 17;

        result = 37 * result
            + (getCodigoEntradaSalida() == null ? 0 : this.getCodigoEntradaSalida().hashCode());
		result = 37 * result + this.getAnyoEntradaSalida();
		result = 37 * result + this.getNumeroEntradaSalida();
		result = 37 * result + this.getOficinaEntradaSalida();
		result = 37 * result + this.getNumero();
		return result;
	}

}
