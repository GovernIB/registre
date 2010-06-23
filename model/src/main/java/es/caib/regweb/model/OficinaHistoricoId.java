package es.caib.regweb.model;


import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OficinaHistoricoId implements java.io.Serializable {

	private int codigo;
	private int fechaAlta;

    public OficinaHistoricoId() {
	}

	public OficinaHistoricoId(int codigo, int fechaAlta) {
		this.codigo = codigo;
		this.fechaAlta = fechaAlta;
	}

	@Column(name = "FHACAGCO", nullable = false, precision = 2, scale = 0)
	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	@Column(name = "FHAFALTA", nullable = false, precision = 8, scale = 0)
	public int getFechaAlta() {
		return this.fechaAlta;
	}

	public void setFechaAlta(int fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof OficinaHistoricoId))
			return false;
		OficinaHistoricoId castOther = (OficinaHistoricoId) other;

		return (this.getCodigo() == castOther.getCodigo())
				&& (this.getFechaAlta() == castOther.getFechaAlta());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + this.getCodigo();
		result = 37 * result + this.getFechaAlta();
		return result;
	}

}
